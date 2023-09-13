import React from 'react';
import {
  View,
  Image,
  NativeModules,
  NativeEventEmitter,
  Alert,
  Text,
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

const LivePreviewScreen: React.FC = () => {
  const [dataUrl, setDataUrl] = React.useState<string | undefined>();
  const isFocused = useIsFocused();
  const [previewing, setPreviewing] = React.useState<boolean>(false);

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

  return (
    <View style={styles.takePhotoBack}>
      <View style={styles.contentContainer}>
        {dataUrl && (
          <Image style={styles.takePhoto} source={{ uri: dataUrl }} />
        )}
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
