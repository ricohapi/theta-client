import React from 'react';
import {
  View,
  Image,
  TouchableOpacity,
  NativeModules,
  NativeEventEmitter,
} from 'react-native';
import styles from './Styles';
import {
  getLivePreview,
  stopLivePreview,
  getPhotoCaptureBuilder,
  THETA_EVENT_NAME,
} from 'theta-client-react-native';
import {useIsFocused} from '@react-navigation/native';

const startLivePreview = async () => {
  getLivePreview().then(x => {
    console.log(`live preview done with ${x}`);
  });
};

const TakePhoto = ({navigation}) => {
  const [dataUrl, setDataUrl] = React.useState<string | undefined>();
  const isFocused = useIsFocused();

  const onShutter = async () => {
    stopLivePreview();
    const photoCapture = await getPhotoCaptureBuilder().build();
    const url = await photoCapture.takePicture();
    const urls = url.split('/');
    navigation.navigate('sphere', {
      item: {fileUrl: url, name: urls[urls.length - 1]},
    });
  };

  React.useEffect(() => {
    if (isFocused) {
      const emitter = new NativeEventEmitter(NativeModules.ThetaClientReactNative);
      const eventListener = emitter.addListener(THETA_EVENT_NAME, event => {
        setDataUrl(event.data);
      });
      startLivePreview();
      return () => {
        stopLivePreview();
        eventListener.remove();
      };
    }
  }, [isFocused]);

  return (
    <View style={styles.takePhotoBack}>
      <Image style={styles.takePhoto} source={{uri: dataUrl}} />
      <View style={styles.shutterBack}>
        <TouchableOpacity style={styles.shutter} onPress={onShutter} />
      </View>
    </View>
  );
};

export default TakePhoto;
