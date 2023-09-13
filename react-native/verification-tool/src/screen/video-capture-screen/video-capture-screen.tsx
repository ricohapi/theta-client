import React from 'react';
import { View, Alert, ScrollView } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import styles from './styles';
import Button from '../../components/ui/button';
import {
  Options,
  VideoCapture,
  VideoFileFormatEnum,
  getVideoCaptureBuilder,
  stopSelfTimer,
} from 'theta-client-react-native';
import {
  MaxRecordableTimeEdit,
  VideoFileFormatEdit,
} from '../../components/options';
import { CaptureCommonOptionsEdit } from '../../components/capture/capture-common-options';

const VideoCaptureScreen: React.FC = ({ navigation }) => {
  const [options, setOptions] = React.useState<Options>();
  const [isTaking, setIsTaking] = React.useState(false);
  const [capture, setCapture] = React.useState<VideoCapture>();

  React.useEffect(() => {
    navigation.setOptions({ title: 'Video Capture' });
  }, [navigation]);

  const onTake = async () => {
    if (isTaking) {
      return;
    }

    const builder = getVideoCaptureBuilder();
    options?.maxRecordableTime &&
      builder.setMaxRecordableTime(options.maxRecordableTime);
    options?.fileFormat &&
      builder.setFileFormat(options.fileFormat as VideoFileFormatEnum);

    options?.aperture && builder.setAperture(options.aperture);
    if (options?.colorTemperature != null) {
      builder.setColorTemperature(options.colorTemperature);
    }
    options?.exposureCompensation &&
      builder.setExposureCompensation(options.exposureCompensation);
    options?.exposureDelay && builder.setExposureDelay(options.exposureDelay);
    options?.exposureProgram &&
      builder.setExposureProgram(options.exposureProgram);
    options?.gpsInfo && builder.setGpsInfo(options.gpsInfo);
    options?._gpsTagRecording &&
      builder.setGpsTagRecording(options._gpsTagRecording);
    options?.iso && builder.setIso(options.iso);
    options?.isoAutoHighLimit &&
      builder.setIsoAutoHighLimit(options.isoAutoHighLimit);
    options?.whiteBalance && builder.setWhiteBalance(options.whiteBalance);

    console.log('videoCapture options: :' + JSON.stringify(options));
    console.log('videoCapture builder: :' + JSON.stringify(builder));

    try {
      setCapture(await builder.build());
      setIsTaking(false);
    } catch (error) {
      setIsTaking(false);
      Alert.alert('VideoCaptureBuilder build error', JSON.stringify(error), [
        { text: 'OK' },
      ]);
    }
  };

  const initCapture = () => {
    setCapture(undefined);
    setIsTaking(false);
  };

  const startVideoCapture = async () => {
    if (capture == null) {
      initCapture();
      return;
    }
    try {
      console.log('startVideoCapture startCapture');
      const url = await capture.startCapture((error) => {
        Alert.alert('stopCapture error', JSON.stringify(error), [
          { text: 'OK' },
        ]);
      });
      initCapture();
      Alert.alert('video file url : ', url, [{ text: 'OK' }]);
    } catch (error) {
      initCapture();
      Alert.alert('startCapture error', JSON.stringify(error), [
        { text: 'OK' },
      ]);
    }
  };

  const onStop = async () => {
    if (capture == null) {
      return;
    }
    console.log('ready to stop...');
    try {
      capture.stopCapture();
    } catch (error) {
      Alert.alert('stopCapture error', JSON.stringify(error), [{ text: 'OK' }]);
    }
  };

  const onStopSelfTimer = async () => {
    try {
      await stopSelfTimer();
    } catch (error) {
      Alert.alert('stopSelfTimer error', JSON.stringify(error), [
        { text: 'OK' },
      ]);
    }
  };

  React.useEffect(() => {
    if (capture != null && !isTaking) {
      setIsTaking(true);
      startVideoCapture();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [capture]);

  return (
    <SafeAreaView
      style={styles.safeAreaContainer}
      edges={['left', 'right', 'bottom']}
    >
      <View style={styles.topViewContainer}>
        <View style={styles.bottomViewContainerLayout}>
          <Button
            style={styles.button}
            title="take video"
            onPress={onTake}
            disabled={isTaking}
          />
          <Button
            style={styles.button}
            title="stop"
            onPress={onStop}
            disabled={!isTaking}
          />
        </View>
        <Button
          style={styles.button}
          title="stop self timer"
          onPress={onStopSelfTimer}
          disabled={!isTaking}
        />
      </View>
      <View style={styles.contentContainer}>
        <ScrollView>
          <MaxRecordableTimeEdit
            onChange={(option) => {
              setOptions((prevState) => ({
                ...prevState,
                maxRecordableTime: option.maxRecordableTime,
              }));
            }}
            options={options}
          />
          <VideoFileFormatEdit
            onChange={(option) => {
              setOptions((prevState) => ({
                ...prevState,
                fileFormat: option.fileFormat,
              }));
            }}
            options={options}
          />
          <CaptureCommonOptionsEdit
            onChange={(option) => {
              setOptions(option);
            }}
            options={options}
          />
        </ScrollView>
      </View>
    </SafeAreaView>
  );
};

export default VideoCaptureScreen;
