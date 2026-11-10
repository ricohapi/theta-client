import React from 'react';
import {
    View,
    Alert,
    Text,
    Platform,
    NativeModules,
    NativeEventEmitter,
    Modal,
    ActivityIndicator,
} from 'react-native';
import styles from './styles.tsx';
import {
    getLivePreview,
    isInitialized,
    stopLivePreview,
    THETA_EVENT_NAME,
} from '../../modules/theta-client';
import Button from '../../components/ui/button';
import WebView from 'react-native-webview';

interface LivePreviewScreenProps {
    navigation: {
        setOptions: (options: { title: string }) => void;
        goBack: () => void;
        setBackHandler: (handler: (() => void) | null) => void;
    };
}

const LivePreviewScreen: React.FC<LivePreviewScreenProps> = ({ navigation }) => {
    const [dataUrl, setDataUrl] = React.useState<string | undefined>();
    const [previewing, setPreviewing] = React.useState<boolean>(false);
    const [isLoaded, setLoaded] = React.useState(false);
    const [frameSize, setFrameSize] = React.useState<number>(0);
    const [isStopping, setStopping] = React.useState(false);
    const webViewRef = React.useRef<any>(null);
    // getLivePreview() settles when the live preview finishes.
    const previewPromiseRef = React.useRef<Promise<unknown> | null>(null);
    const isLeavingRef = React.useRef(false);
    const navigationRef = React.useRef(navigation);
    navigationRef.current = navigation;
    const source =
        Platform.OS === 'android'
            ? 'file:///android_asset/live-preview/index.html'
            : './Web.bundle/live-preview/index.html';

    const startLivePreview = async () => {
        setPreviewing(true);
        console.log('Starting live preview...');
        try {
            previewPromiseRef.current = getLivePreview();
            const ret = await previewPromiseRef.current;
            setPreviewing(false);
            previewPromiseRef.current = null;
            console.log(`live preview done with ${ret}`);
        } catch (error: unknown) {
            setPreviewing(false);
            previewPromiseRef.current = null;
            console.log('getLivePreview error: ' + JSON.stringify(error));
            if (isLeavingRef.current) {
                return;
            }
            const errStr = error instanceof Error ? error.message : String(error);
            const message =
                errStr.includes('serviceUnavailable') || errStr.includes('503')
                    ? 'Please check if the THETA is in sleep mode.'
                    : 'Failed to start live preview.';
            Alert.alert('getLivePreview', message, [{ text: 'OK' }]);
        }
    };

    const setFrameData = (data: string) => {
        console.log('setFrameData called, data length:', data?.length || 0);
        if (!data || typeof data !== 'string' || !data.startsWith('data:')) {
            return;
        }
        if (isLoaded && webViewRef.current) {
            // JSON.stringify to avoid breaking the injected script (quotes, backslashes, length)
            const escaped = JSON.stringify(data);
            webViewRef.current.injectJavaScript(`setFrame(${escaped});true`);
        } else {
            console.log('Cannot set frame data:', { isLoaded, hasWebView: !!webViewRef.current });
        }
    };

    const onStart = () => {
        startLivePreview();
    };

    const onStop = () => {
        isInitialized().then((isInit) => {
            if (isInit) {
                stopLivePreview().then((isStopped) => {
                    console.log(`isStop = ${isStopped}`);
                });
            } else {
                Alert.alert('stopLivePreview', 'error: Not initialized.', [
                    { text: 'OK' },
                ]);
            }
        });
    };

    const onLoad = () => {
        console.log('onLoad');
        setLoaded(true);
    };

    React.useEffect(() => {
        navigation.setOptions({ title: 'Live Preview' });
    }, [navigation]);

    // Back: stop the live preview and wait for it to finish before leaving.
    React.useEffect(() => {
        navigationRef.current.setBackHandler(async () => {
            const previewPromise = previewPromiseRef.current;
            if (previewPromise == null) {
                navigationRef.current.goBack();
                return;
            }
            isLeavingRef.current = true;
            setStopping(true);
            try {
                stopLivePreview().catch((error) => console.log('stopLivePreview error: ' + error));
                // The SDK stops at the next frame. Give up after 10s when no frame comes.
                await Promise.race([
                    previewPromise.catch(() => { }),
                    new Promise((resolve) => setTimeout(resolve, 10_000)),
                ]);
            } finally {
                setStopping(false);
                navigationRef.current.goBack();
            }
        });
        return () => navigationRef.current.setBackHandler(null);
    }, []);

    React.useEffect(() => {
        const emitter = new NativeEventEmitter(
            NativeModules.ThetaClientReactNative
        );
        const eventListener = emitter.addListener(THETA_EVENT_NAME, (event) => {
            setDataUrl(event.data);
            setFrameSize(event.dataSize);
        });
        return () => {
            isInitialized().then((isInit) => {
                if (isInit) {
                    stopLivePreview();
                }
            });
            eventListener.remove();
        };
    }, []);

    React.useEffect(() => {
        if (isLoaded && dataUrl) {
            setFrameData(dataUrl);
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [dataUrl, isLoaded]);

    return (
        <View style={styles.takePhotoBack}>
            {/* Full screen modal blocks every operation including the back button in the header */}
            <Modal visible={isStopping} transparent animationType="none" onRequestClose={() => { }}>
                <View style={styles.loadingOverlay}>
                    <View style={styles.loadingBox}>
                        <ActivityIndicator size="large" color="#6200ee" />
                        <Text style={styles.loadingText}>Stopping live preview...</Text>
                    </View>
                </View>
            </Modal>
            <View style={styles.contentContainer}>
                <WebView
                    style={styles.webview}
                    ref={webViewRef}
                    originWhitelist={['*']}
                    source={{ uri: source }}
                    onLoad={onLoad}
                />
            </View>
            <View style={styles.bottomViewContainer}>
                <Text style={styles.itemText}>
                    {previewing ? 'Previewing...' : 'Stopped'} ({frameSize} byte)
                </Text>
                <View style={styles.bottomViewContainerLayout}>
                    <Button style={styles.button} title="Start" onPress={onStart} disabled={previewing} />
                    <Button style={styles.button} title="Stop" onPress={onStop} />
                </View>
            </View>
        </View>
    );
};

export default LivePreviewScreen;
