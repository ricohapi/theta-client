import React from 'react';
import { View, Alert, Text, Platform, NativeModules, NativeEventEmitter } from 'react-native';
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
    };
}

const LivePreviewScreen: React.FC<LivePreviewScreenProps> = ({ navigation }) => {
    const [dataUrl, setDataUrl] = React.useState<string | undefined>();
    const [previewing, setPreviewing] = React.useState<boolean>(false);
    const [isLoaded, setLoaded] = React.useState(false);
    const [frameSize, setFrameSize] = React.useState<number>(0);
    const webViewRef = React.useRef<any>(null);
    const source =
        Platform.OS === 'android'
            ? 'file:///android_asset/live-preview/index.html'
            : './Web.bundle/live-preview/index.html';

    const startLivePreview = async () => {
        setPreviewing(true);
        console.log('Starting live preview...');
        try {
            const ret = await getLivePreview();
            setPreviewing(false);
            console.log(`live preview done with ${ret}`);
        } catch (error: unknown) {
            setPreviewing(false);
            console.log('getLivePreview error: ' + JSON.stringify(error));
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
