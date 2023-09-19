import React from 'react';
import {
  View,
  NativeModules,
  NativeEventEmitter,
  Alert,
  Text,
  Platform,
} from 'react-native';
import styles from './styles';
import {
  getLivePreview,
  isInitialized,
  stopLivePreview,
  THETA_EVENT_NAME,
} from 'theta-client-react-native';
import { useIsFocused } from '@react-navigation/native';
import Button from '../../components/ui/button';
import WebView from 'react-native-webview';

const LivePreviewScreen: React.FC = () => {
  const isFocused = useIsFocused();
  const [dataUrl, setDataUrl] = React.useState<string | undefined>();
  const [previewing, setPreviewing] = React.useState<boolean>(false);
  const [isLoaded, setLoaded] = React.useState(false);
  const webViewRef = React.useRef<WebView>(null);
  const source =
    Platform.OS === 'android'
      ? 'file:///android_asset/live-preview/index.html'
      : './Web.bundle/live-preview/index.html';

  const startLivePreview = async () => {
    setPreviewing(true);
    getLivePreview()
      .then((x) => {
        setPreviewing(false);
        console.log(`live preview done with ${x}`);
      })
      .catch((error) => {
        setPreviewing(false);
        Alert.alert('getLivePreview', 'error: \n' + JSON.stringify(error), [
          { text: 'OK' },
        ]);
      });
  };

  const setFrameData = (data: string) => {
    if (isLoaded && webViewRef.current) {
      webViewRef.current.injectJavaScript(`
      setFrame('${data}');true
      `);
    }
  };

  const onStart = () => {
    startLivePreview();
  };

  const onStop = () => {
    isInitialized().then((isInit) => {
      if (isInit) {
        stopLivePreview();
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
    if (isFocused) {
      const emitter = new NativeEventEmitter(
        NativeModules.ThetaClientReactNative
      );
      const eventListener = emitter.addListener(THETA_EVENT_NAME, (event) => {
        setDataUrl(event.data);
      });
      return () => {
        isInitialized().then((isInit) => {
          if (isInit) {
            stopLivePreview();
          }
        });
        eventListener.remove();
      };
    }
    return;
  }, [isFocused]);

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
        <Text>{previewing ? 'Previewing...' : 'Stopped'}</Text>
        <View style={styles.bottomViewContainerLayout}>
          <Button style={styles.button} title="Start" onPress={onStart} />
          <Button style={styles.button} title="Stop" onPress={onStop} />
        </View>
      </View>
    </View>
  );
};

export default LivePreviewScreen;
