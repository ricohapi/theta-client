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
} from '../../modules/theta-client';
import { useIsFocused } from '@react-navigation/native';
import Button from '../../components/ui/button';
import WebView from 'react-native-webview';
import type { NativeStackScreenProps } from '@react-navigation/native-stack';
import type { RootStackParamList } from '../../App';

const LivePreviewScreen: React.FC<
  NativeStackScreenProps<RootStackParamList, 'livePreview'>
> = () => {
  const isFocused = useIsFocused();
  const [dataUrl, setDataUrl] = React.useState<string | undefined>();
  const [previewing, setPreviewing] = React.useState<boolean>(false);
  const [isLoaded, setLoaded] = React.useState(false);
  const [frameSize, setFrameSize] = React.useState<number>(0);
  const webViewRef = React.useRef<WebView>(null);
  const source =
    Platform.OS === 'android'
      ? 'file:///android_asset/live-preview/index.html'
      : './Web.bundle/live-preview/index.html';

  const startLivePreview = async () => {
    setPreviewing(true);
    try {
      const ret = await getLivePreview();
      setPreviewing(false);
      console.log(`live preview done with ${ret}`);
    } catch (error) {
      Alert.alert('getLivePreview', 'error: \n' + JSON.stringify(error), [
        { text: 'OK' },
      ]);
    } finally {
      setPreviewing(false);
    }
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
    if (isFocused) {
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
        <Text style={styles.itemText}>
          {previewing ? 'Previewing...' : 'Stopped'} ({frameSize} byte)
        </Text>
        <View style={styles.bottomViewContainerLayout}>
          <Button style={styles.button} title="Start" onPress={onStart} />
          <Button style={styles.button} title="Stop" onPress={onStop} />
        </View>
      </View>
    </View>
  );
};

export default LivePreviewScreen;
