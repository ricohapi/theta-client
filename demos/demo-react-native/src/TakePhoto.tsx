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
import {useIsFocused} from '@react-navigation/native';
import WebView from 'react-native-webview';

const TakePhoto = ({navigation}) => {
  const isFocused = useIsFocused();
  const [dataUrl, setDataUrl] = React.useState<string | undefined>();
  const [isLoaded, setLoaded] = React.useState(false);
  const webViewRef = React.useRef<WebView>(null);
  const source =
    Platform.OS === 'android'
      ? 'file:///android_asset/live-preview/index.html'
      : './Web.bundle/live-preview/index.html';

  const startLivePreview = async () => {
    getLivePreview()
      .then(x => {
        console.log(`live preview done with ${x}`);
      })
      .catch(error => {
        Alert.alert('getLivePreview', 'error: \n' + JSON.stringify(error), [
          {text: 'OK'},
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
    if (isFocused) {
      const emitter = new NativeEventEmitter(
        NativeModules.ThetaClientReactNative,
      );
      const eventListener = emitter.addListener(THETA_EVENT_NAME, event => {
        setDataUrl(event.data);
      });
      startLivePreview();
      return () => {
        isInitialized().then(isInit => {
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

  const onShutter = async () => {
    stopLivePreview();
    const photoCapture = await getPhotoCaptureBuilder().build();
    const url = await photoCapture.takePicture();
    if (url) {
      const urls = url.split('/');
      navigation.navigate('sphere', {
        item: {fileUrl: url, name: urls[urls.length - 1]},
      });
    } else {
      Alert.alert('takePicture canceled.', undefined, [
        {text: 'OK', onPress: () => startLivePreview},
      ]);
    }
  };

  return (
    <View style={styles.takePhotoBack}>
      <View style={styles.livePreviewContainer}>
        <WebView
          style={styles.livePreviewWebview}
          ref={webViewRef}
          originWhitelist={['*']}
          source={{uri: source}}
          onLoad={onLoad}
        />
      </View>
      <View style={styles.livePreviewBottomContainer}>
        <TouchableOpacity style={styles.shutter} onPress={onShutter} />
      </View>
    </View>
  );
};

export default TakePhoto;
