import React from 'react';
import { View, Alert, ScrollView, Text } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import styles from './styles';
import Button from '../../components/ui/button';
import {
  CaptureModeEnum,
  CapturingStatusEnum,
  MaxRecordableTimeEnum,
  Options,
  VideoCapture,
  VideoFileFormatEnum,
  getVideoCaptureBuilder,
  setOptions,
  stopSelfTimer,
} from '../../modules/theta-client';
import { CaptureCommonOptionsEdit } from '../../components/capture/capture-common-options';
import type { NativeStackScreenProps } from '@react-navigation/native-stack';
import type { RootStackParamList } from '../../App';
import { EnumEdit } from '../../components/options/enum-edit';
import { InputNumber } from '../../components/ui/input-number';

const VideoCaptureScreen: React.FC<
  NativeStackScreenProps<RootStackParamList, 'videoCapture'>
> = ({ navigation }) => {
  const [interval, setInterval] = React.useState<number>();
  const [message, setMessage] = React.useState('');
  const [message2, setMessage2] = React.useState('');
  const [capturingStatus, setCapturingStatus] =
    React.useState<CapturingStatusEnum>();
  const [captureOptions, setCaptureOptions] = React.useState<Options>();
  const [isTaking, setIsTaking] = React.useState(false);
  const [capture, setCapture] = React.useState<VideoCapture>();

  const onTake = async () => {
    if (isTaking) {
      return;
    }

    const builder = getVideoCaptureBuilder();
    if (interval != null) {
      builder.setCheckStatusCommandInterval(interval);
    }
    captureOptions?.maxRecordableTime &&
      builder.setMaxRecordableTime(captureOptions.maxRecordableTime);
    captureOptions?.fileFormat &&
      builder.setFileFormat(captureOptions.fileFormat as VideoFileFormatEnum);

    captureOptions?.aperture && builder.setAperture(captureOptions.aperture);
    if (captureOptions?.colorTemperature != null) {
      builder.setColorTemperature(captureOptions.colorTemperature);
    }
    captureOptions?.exposureCompensation &&
      builder.setExposureCompensation(captureOptions.exposureCompensation);
    captureOptions?.exposureDelay &&
      builder.setExposureDelay(captureOptions.exposureDelay);
    captureOptions?.exposureProgram &&
      builder.setExposureProgram(captureOptions.exposureProgram);
    captureOptions?.gpsInfo && builder.setGpsInfo(captureOptions.gpsInfo);
    captureOptions?._gpsTagRecording &&
      builder.setGpsTagRecording(captureOptions._gpsTagRecording);
    captureOptions?.iso && builder.setIso(captureOptions.iso);
    captureOptions?.isoAutoHighLimit &&
      builder.setIsoAutoHighLimit(captureOptions.isoAutoHighLimit);
    captureOptions?.whiteBalance &&
      builder.setWhiteBalance(captureOptions.whiteBalance);

    console.log('videoCapture options: :' + JSON.stringify(captureOptions));
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
    setCapturingStatus(undefined);
    try {
      console.log('startVideoCapture startCapture');
      const url = await capture.startCapture(
        (error) => {
          Alert.alert('stopCapture error', JSON.stringify(error), [
            { text: 'OK' },
          ]);
        },
        (status) => {
          setCapturingStatus(status);
        },
        (fileUrl) => {
          setMessage2('file = ' + fileUrl);
        }
      );
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
    setOptions({ captureMode: CaptureModeEnum.VIDEO }).catch();
  }, []);

  React.useEffect(() => {
    navigation.setOptions({ title: 'Video Capture' });
  }, [navigation]);

  React.useEffect(() => {
    if (capture != null && !isTaking) {
      setIsTaking(true);
      startVideoCapture();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [capture]);

  React.useEffect(() => {
    setMessage(`capturing = ${capturingStatus}`);
  }, [capturingStatus]);

  return (
    <SafeAreaView
      style={styles.safeAreaContainer}
      edges={['left', 'right', 'bottom']}
    >
      <View style={styles.topViewContainer}>
        <Text style={styles.itemText}>{message}</Text>
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
        <Text style={styles.itemText}>{message2}</Text>
      </View>
      <View style={styles.contentContainer}>
        <ScrollView>
          <InputNumber
            title="CheckStatusCommandInterval"
            placeHolder="Input value"
            value={interval}
            onChange={(value) => {
              setInterval(value);
            }}
          />
          <EnumEdit
            title={'maxRecordableTime'}
            option={captureOptions?.maxRecordableTime}
            onChange={(maxRecordableTime) => {
              setCaptureOptions((prevState) => ({
                ...prevState,
                maxRecordableTime,
              }));
            }}
            optionEnum={MaxRecordableTimeEnum}
          />
          <EnumEdit
            title={'fileFormat'}
            option={captureOptions?.fileFormat}
            onChange={(fileFormat) => {
              setCaptureOptions((prevState) => ({
                ...prevState,
                fileFormat,
              }));
            }}
            optionEnum={VideoFileFormatEnum}
          />
          <CaptureCommonOptionsEdit
            onChange={(option) => {
              setCaptureOptions(option);
            }}
            options={captureOptions}
          />
        </ScrollView>
      </View>
    </SafeAreaView>
  );
};

export default VideoCaptureScreen;
