import React from 'react';
import {
    ActivityIndicator,
    Alert,
    Modal,
    PermissionsAndroid,
    Platform,
    ScrollView,
    Text,
    View,
} from 'react-native';
import {
    getThetaInfo,
    getThetaModel,
    initialize,
    isInitialized,
    setAccessPointDynamically,
    NetworkTypeEnum,
    ThetaModel,
    ThetaWebApiError,
    setOptions,
} from '../../modules/theta-client';
import WifiManager from 'react-native-wifi-reborn';
import Button from '../../components/ui/button';
import clientModeConfig from '../../client-mode-config';
import styles from './styles.tsx';
import { clientModeSession, connectClientMode } from './client-mode-session';

interface ClientModeScreenProps {
    navigation: {
        navigate: (screen: string) => void;
        goBack: () => void;
        setBackHandler: (handler: (() => void) | null) => void;
    };
    /** Update the connection message of the main menu */
    onChangeMenuMessage: (message: string) => void;
}

// Functions that the SDK handles specially in client mode (digest authentication, access points, client mode options).
// Get Info and Live preview reuse the screens of the main menu, which work on the current (client mode) connection.
// Firmware update is not listed because the RN library does not provide it.
const menuList: { name: string; screen: string }[] = [
    { name: 'Get Info', screen: 'getInfo' }, // digest authentication of Web API
    { name: 'Live preview', screen: 'livePreview' }, // digest authentication of PreviewClient
    { name: 'Access Points', screen: 'clientModeAccessPoints' },
    { name: 'Client Mode Options', screen: 'clientModeOptions' },
];

const AP_ENDPOINT = 'http://192.168.1.1';
// Waiting for the user to switch the phone's Wi-Fi and for THETA to join the network.
const RECONNECT_TIMEOUT_MS = 180_000;
const RECONNECT_INTERVAL_MS = 3_000;
// Shorter than the SDK default (20s) so that each reconnection attempt fails fast while the network is unreachable.
// It also stays as the timeout of the requests in client mode.
const RECONNECT_TIMEOUT = {
    connectTimeout: 10_000,
    requestTimeout: 10_000,
    socketTimeout: 10_000,
};

// Prefix of the serial number in the name of THETA "THETA" + prefix + serial number,
// which is the SSID "<name>.OSC" in AP mode, the host name "<name>.local" in client mode and the default username.
const SERIAL_PREFIX: Partial<Record<ThetaModel, string>> = {
    [ThetaModel.THETA_A1]: 'AA',
    [ThetaModel.THETA_X]: 'YR',
    [ThetaModel.THETA_Z1]: 'YN',
    [ThetaModel.THETA_V]: 'YL',
};

/** Default password: the numeric portion of the serial number in the name "THETA" + 2 letters + number */
const defaultPassword = (name: string) => name.slice('THETAXX'.length);

const sleep = (ms: number) => new Promise<void>((resolve) => setTimeout(() => resolve(), ms));

// iOS drops an alert shown while a modal is being dismissed, so wait for the loading modal to close.
const alertAfterLoading = (...args: Parameters<typeof Alert.alert>) =>
    setTimeout(() => Alert.alert(...args), 500);

/**
 * Connect the phone to THETA's Wi-Fi in AP mode. The OS shows a dialog and the user approves it.
 * Rejects when the user cancels it or the connection fails.
 */
const joinThetaWifi = async (name: string) => {
    const ssid = `${name}.OSC`;
    const password = clientModeConfig.thetaApPassword || defaultPassword(name);
    if (Platform.OS === 'ios') {
        // joinOnce: a persistent configuration added by the app breaks the network saved in the settings app,
        // which then cannot be joined even manually until it is forgotten.
        try {
            await WifiManager.connectToProtectedSSIDOnce(ssid, password, false, true);
        } catch (error) {
            // After joining, the library checks the SSID with NEHotspotNetwork, which returns nil even when joined,
            // and rejects with "unableToConnect". The connection to THETA after this confirms it instead.
            if ((error as { code?: string }).code !== 'unableToConnect') {
                throw error;
            }
            console.log('joinThetaWifi: SSID check failed, confirming with THETA: ' + error);
        }
        return;
    }
    // react-native-wifi-reborn requires the location permission to connect to Wi-Fi on Android.
    const result = await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION);
    if (result !== PermissionsAndroid.RESULTS.GRANTED) {
        throw new Error('The location permission is required to connect to Wi-Fi.');
    }
    // timeout: wait for THETA to start AP mode
    await WifiManager.connectToProtectedWifiSSID({ ssid, password, isWEP: false, timeout: 60 });
};

/**
 * Repeat [connect] until it succeeds or times out.
 * @return true if connected
 */
const connectWithRetry = async (connect: () => Promise<boolean>): Promise<boolean> => {
    const deadline = Date.now() + RECONNECT_TIMEOUT_MS;
    while (Date.now() < deadline) {
        try {
            await connect();
            return true;
        } catch (error) {
            console.log(`connect error: ${error}`);
        }
        await sleep(RECONNECT_INTERVAL_MS);
    }
    return false;
};

const ClientModeScreen: React.FC<ClientModeScreenProps> = ({ navigation, onChangeMenuMessage }) => {
    const [loadingMessage, setLoadingMessage] = React.useState<string | null>(
        'Checking connection...'
    );
    const [message, setMessage] = React.useState('');
    const [isMenuVisible, setMenuVisible] = React.useState(false);
    const navigationRef = React.useRef(navigation);
    navigationRef.current = navigation;

    const switchToClientMode = async () => {
        if (!(await isInitialized())) {
            setLoadingMessage(null);
            alertAfterLoading('Client Mode', 'Not connected. Connect to THETA in AP mode first.', [
                { text: 'OK', onPress: () => navigationRef.current.goBack() },
            ]);
            return;
        }
        try {
            // 1. AP mode: register the access point THETA joins in client mode.
            const { ssid, password, authMode } = clientModeConfig;
            if (!ssid) {
                throw new Error('Set the Wi-Fi SSID in src/client-mode-config.ts.');
            }
            setLoadingMessage(`Registering Wi-Fi "${ssid}" to THETA...`);
            await setAccessPointDynamically(ssid, { authMode, password: password || undefined });

            // 2. AP mode: get the name of THETA from the model and the serial number.
            setLoadingMessage('Getting client mode settings...');
            const info = await getThetaInfo();
            const prefix = info.thetaModel && SERIAL_PREFIX[info.thetaModel];
            if (!prefix) {
                throw new Error(`Client mode of ${info.model} is not supported.`);
            }
            const name = `THETA${prefix}${info.serialNumber}`;
            // _username option cannot be read in AP mode (THETA X returns disabledCommand), so it is configured.
            const username = clientModeConfig.username || name;

            // 3. AP mode: set the password of client mode. Some models have no default password.
            const { thetaPassword } = clientModeConfig;
            if (thetaPassword) {
                setLoadingMessage('Setting the password of client mode...');
                await setOptions({ password: thetaPassword });
            }

            // 4. Switch to client mode last, because the AP mode connection is lost after this.
            setLoadingMessage('Switching THETA to client mode...');
            try {
                await setOptions({ networkType: NetworkTypeEnum.CLIENT });
            } catch (error) {
                // THETA stops AP mode before responding, so the connection is usually lost here.
                // Only an error response from THETA means a failure. The reconnection below confirms the switch.
                if (error instanceof ThetaWebApiError) {
                    throw error;
                }
                console.log('setOptions(CL) connection lost as expected: ' + error);
            }
            // The SDK resolves "<name>.local" itself where the OS resolution does not work (see mdnsHostPrefixes of the SDK).
            const host = `${name}.local`;
            Object.assign(clientModeSession, {
                active: true,
                ssid,
                name,
                host,
                username,
                // The default password of the SDK is made from the username, which may not be the default one.
                password: thetaPassword || defaultPassword(name),
            });

            setLoadingMessage(
                `Switch the phone's Wi-Fi to the network THETA joins:\n${ssid}\n\nConnecting to ${host} ...`
            );
            if (!(await connectWithRetry(() => connectClientMode(RECONNECT_TIMEOUT)))) {
                // Cannot send the AP mode command without a connection, so leave without it.
                clientModeSession.active = false;
                // The AP mode connection is lost, so the main menu must not show "Connected" any more.
                onChangeMenuMessage('Not connected.');
                throw new Error(
                    `Cannot connect to ${host} in client mode.\nTHETA may stay in client mode. Switch it back to AP mode on the camera.`
                );
            }
            setMessage(`Connected in client mode: ${host}`);
            setMenuVisible(true);
        } catch (error) {
            console.log('switchToClientMode error: ' + error);
            setMessage('Failed to switch to client mode.\n' + error);
            alertAfterLoading('Client Mode', `Failed to switch to client mode.\n${error}`, [
                { text: 'OK', onPress: () => navigationRef.current.goBack() },
            ]);
        } finally {
            setLoadingMessage(null);
        }
    };

    const switchToApModeAndGoBack = async () => {
        if (!clientModeSession.active) {
            navigationRef.current.goBack();
            return;
        }
        try {
            setLoadingMessage('Switching THETA to AP mode...');
            try {
                await setOptions({ networkType: NetworkTypeEnum.DIRECT });
            } catch (error) {
                // Same as switching to client mode: the connection is usually lost before the response.
                if (error instanceof ThetaWebApiError) {
                    throw error;
                }
                console.log('setOptions(AP) connection lost as expected: ' + error);
            }

            const ssid = `${clientModeSession.name}.OSC`;
            setLoadingMessage(`Connecting the phone to THETA's Wi-Fi (${ssid}).\nApprove the dialog of the OS.`);
            // THETA's Wi-Fi is not found when the command did not reach THETA (e.g. the host name was not resolved).
            // Keep the session active until joined, so that "Retry" below sends the command again.
            await joinThetaWifi(clientModeSession.name);
            clientModeSession.active = false;

            setLoadingMessage(`Connecting to ${AP_ENDPOINT} ...`);
            if (!(await connectWithRetry(() => initialize(AP_ENDPOINT, undefined, RECONNECT_TIMEOUT)))) {
                throw new Error(`Cannot connect to ${AP_ENDPOINT} in AP mode.`);
            }
            // Same message as "connect" in the main menu
            onChangeMenuMessage(`Connected.\nModel: ${await getThetaModel()}`);
            setLoadingMessage(null);
            navigationRef.current.goBack();
        } catch (error) {
            console.log('switchToApMode error: ' + error);
            setLoadingMessage(null);
            setMessage('Failed to switch back to AP mode.\n' + error);
            if (clientModeSession.active) {
                alertAfterLoading('AP Mode', `Failed to switch THETA to AP mode.\n${error}`, [
                    { text: 'Retry', onPress: () => switchToApModeAndGoBack() },
                    {
                        text: 'Leave anyway',
                        style: 'destructive',
                        onPress: () => {
                            clientModeSession.active = false;
                            onChangeMenuMessage('Not connected.');
                            navigationRef.current.goBack();
                        },
                    },
                ]);
            } else {
                // AP mode is set but the reconnection failed. Leave and reconnect from the main menu.
                onChangeMenuMessage('Not connected.');
                alertAfterLoading('AP Mode', 'Press "connect" in the main menu after the phone joins THETA.', [
                    { text: 'OK', onPress: () => navigationRef.current.goBack() },
                ]);
            }
        }
    };

    React.useEffect(() => {
        navigationRef.current.setBackHandler(() => {
            switchToApModeAndGoBack();
        });
        if (clientModeSession.active) {
            // Back from a sub screen: already connected in client mode.
            setLoadingMessage(null);
            setMessage(`Connected in client mode: ${clientModeSession.host}`);
            setMenuVisible(true);
        } else {
            switchToClientMode();
        }
        return () => navigationRef.current.setBackHandler(null);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const menuItems = menuList.map((item) => (
        <View key={item.name}>
            <View style={styles.spacer} />
            <Button title={item.name} onPress={() => navigation.navigate(item.screen)} />
        </View>
    ));

    return (
        <View style={styles.safeAreaContainer}>
            {/* Full screen modal blocks every operation including the back button in the header */}
            <Modal
                visible={loadingMessage != null}
                transparent
                animationType="none"
                onRequestClose={() => {}}
            >
                <View style={styles.loadingOverlay}>
                    <View style={styles.loadingBox}>
                        <ActivityIndicator size="large" color="#6200ee" />
                        <Text style={styles.loadingText}>{loadingMessage}</Text>
                    </View>
                </View>
            </Modal>
            {message !== '' && (
                <ScrollView style={styles.messageArea}>
                    <Text style={styles.messageText}>{message}</Text>
                </ScrollView>
            )}
            {isMenuVisible && (
                <ScrollView style={styles.container}>{menuItems}</ScrollView>
            )}
        </View>
    );
};

export default ClientModeScreen;
