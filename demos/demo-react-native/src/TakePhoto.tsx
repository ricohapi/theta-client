import React from 'react';
import {
  View,
  TouchableOpacity,
  NativeModules,
  NativeEventEmitter,
  Alert,
  Platform,
} from 'react-native';
import styles from './Styles';
import {
  getLivePreview,
  stopLivePreview,
  getPhotoCaptureBuilder,
  THETA_EVENT_NAME,
  isInitialized,
} from './modules/theta-client';
import WebView from 'react-native-webview';
import { Navigation } from './types';

type TakePhotoProps = {
  navigation: Navigation;
};

const TakePhoto = ({ navigation }: TakePhotoProps) => {
  const [dataUrl, setDataUrl] = React.useState<string | undefined>();
  const [isLoaded, setLoaded] = React.useState(false);
  const [listenerReady, setListenerReady] = React.useState(false);
  const webViewRef = React.useRef<WebView>(null);
  const source =
    Platform.OS === 'android'
      ? 'file:///android_asset/live-preview/index.html'
      : './Web.bundle/live-preview/index.html';

  const startLivePreview = () => {
    if (!listenerReady) {
      console.log('Listener not ready yet, waiting...');
      return;
    }
    getLivePreview()
      .then((x: boolean) => {
        console.log(`live preview done with ${x}`);
      })
      .catch((error: any) => {
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

  const onLoad = () => {
    console.log('onLoad');
    setLoaded(true);
  };

  React.useEffect(() => {
    const emitter = new NativeEventEmitter(
      NativeModules.ThetaClientReactNative,
    );
    const eventListener = emitter.addListener(THETA_EVENT_NAME, event => {
      setDataUrl(event.data);
    });
    setListenerReady(true);
    return () => {
      isInitialized().then((isInit: boolean) => {
        if (isInit) {
          stopLivePreview();
        }
      });
      eventListener.remove();
      setListenerReady(false);
    };
  }, []);

  React.useEffect(() => {
    if (listenerReady) {
      startLivePreview();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [listenerReady]);

  React.useEffect(() => {
    if (isLoaded && dataUrl) {
      setFrameData(dataUrl);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [dataUrl, isLoaded]);

  const onShutter = async () => {
    try {
      stopLivePreview();
      const photoCapture = await getPhotoCaptureBuilder().build();
      const url = await photoCapture.takePicture();
      if (url) {
        navigation.navigate('sphere', { fileUrl: url });
      } else {
        Alert.alert('takePicture canceled.', undefined, [
          { text: 'OK', onPress: () => startLivePreview },
        ]);
      }
    } catch (error) {
      // Error taking picture
    }
  };

  return (
    <View style={styles.takePhotoBack}>
      <View style={styles.livePreviewContainer}>
        <WebView
          style={styles.livePreviewWebview}
          ref={webViewRef}
          originWhitelist={['*']}
          source={{ uri: source }}
          onLoad={onLoad}
          allowFileAccess={true}
          allowUniversalAccessFromFileURLs={true}
        />
      </View>
      <View style={styles.livePreviewBottomContainer}>
        <TouchableOpacity style={styles.shutter} onPress={onShutter} />
      </View>
    </View>
  );
};

export default React.memo(TakePhoto, (prevProps, nextProps) => {
  return prevProps.navigation === nextProps.navigation;
});
