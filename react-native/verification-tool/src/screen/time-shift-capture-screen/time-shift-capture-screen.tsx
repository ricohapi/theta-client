import React from 'react';
import { View, Alert, ScrollView, Text } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import styles from './styles';
import Button from '../../components/ui/button';
import {
  CaptureModeEnum,
  Options,
  TimeShiftCapture,
  getTimeShiftCaptureBuilder,
  setOptions,
  stopSelfTimer,
} from 'theta-client-react-native';
import { CaptureCommonOptionsEdit } from '../../components/capture/capture-common-options';
import { TimeShiftEdit } from '../../components/options/time-shift';
import { InputNumber } from '../../components/ui/input-number';

const TimeShiftCaptureScreen: React.FC = ({ navigation }) => {
  const [interval, setInterval] = React.useState<number>();
  const [message, setMessage] = React.useState('progress = 0');
  const [captureOptions, setCaptureOptions] = React.useState<Options>();
  const [isTaking, setIsTaking] = React.useState(false);
  const [capture, setCapture] = React.useState<TimeShiftCapture>();

  const onTake = async () => {
    if (isTaking) {
      return;
    }

    const builder = getTimeShiftCaptureBuilder();
    if (interval != null) {
      builder.setCheckStatusCommandInterval(interval);
    }
    captureOptions?.timeShift?.isFrontFirst &&
      builder.setIsFrontFirst(captureOptions.timeShift.isFrontFirst);
    captureOptions?.timeShift?.firstInterval &&
      builder.setFirstInterval(captureOptions.timeShift.firstInterval);
    captureOptions?.timeShift?.secondInterval &&
      builder.setSecondInterval(captureOptions.timeShift.secondInterval);

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

    console.log('TimeShiftCapture interval: ' + interval);
    console.log('TimeShiftCapture options: ' + JSON.stringify(captureOptions));
    console.log('TimeShiftCapture builder: ' + JSON.stringify(builder));

    try {
      setCapture(await builder.build());
      setIsTaking(false);
    } catch (error) {
      setIsTaking(false);
      Alert.alert(
        'TimeShiftCaptureBuilder build error',
        JSON.stringify(error),
        [{ text: 'OK' }]
      );
    }
  };

  const initCapture = () => {
    setCapture(undefined);
    setIsTaking(false);
  };

  const startTimeShiftCapture = async () => {
    if (capture == null) {
      initCapture();
      return;
    }
    try {
      console.log('startTimeShiftCapture startCapture');
      const url = await capture.startCapture(
        (completion) => {
          if (isTaking) return;
          setMessage(`progress = ${completion}`);
        },
        (error) => {
          Alert.alert('Cancel error', JSON.stringify(error), [{ text: 'OK' }]);
        }
      );
      initCapture();
      if (url) {
        Alert.alert('TimeShift file url : ', url, [{ text: 'OK' }]);
      } else {
        Alert.alert('TimeShift canceled.');
      }
    } catch (error) {
      initCapture();
      Alert.alert('startCapture error', JSON.stringify(error), [
        { text: 'OK' },
      ]);
    }
  };

  const onCancel = async () => {
    if (capture == null) {
      return;
    }
    console.log('ready to cancel...');
    try {
      capture.cancelCapture();
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
    setOptions({ captureMode: CaptureModeEnum.IMAGE }).catch();
  }, []);

  React.useEffect(() => {
    navigation.setOptions({ title: 'TimeShift Capture' });
  }, [navigation]);

  React.useEffect(() => {
    if (capture != null && !isTaking) {
      setIsTaking(true);
      startTimeShiftCapture();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [capture]);

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
            title="take TimeShift"
            onPress={onTake}
            disabled={isTaking}
          />
          <Button
            style={styles.button}
            title="cancel"
            onPress={onCancel}
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
          <InputNumber
            title="CheckStatusCommandInterval"
            placeHolder="Input value"
            value={interval}
            onChange={(value) => {
              setInterval(value);
            }}
          />
          <TimeShiftEdit
            onChange={(option) => {
              setCaptureOptions(option);
            }}
            options={captureOptions}
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

export default TimeShiftCaptureScreen;
