import React from 'react';
import { View, Alert, ScrollView, Text } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import styles from './styles';
import Button from '../../components/ui/button';
import {
  Options,
  TimeShiftCapture,
  getTimeShiftCaptureBuilder,
  stopSelfTimer,
} from 'theta-client-react-native';
import { CaptureCommonOptionsEdit } from '../../components/capture/capture-common-options';
import { TimeShiftEdit } from '../../components/options/time-shift';
import { InputNumber } from '../../components/ui/input-number';

const TimeShiftCaptureScreen: React.FC = ({ navigation }) => {
  const [interval, setInterval] = React.useState<number>();
  const [message, setMessage] = React.useState('progress = 0');
  const [options, setOptions] = React.useState<Options>();
  const [isTaking, setIsTaking] = React.useState(false);
  const [capture, setCapture] = React.useState<TimeShiftCapture>();

  React.useEffect(() => {
    navigation.setOptions({ title: 'TimeShift Capture' });
  }, [navigation]);

  const onTake = async () => {
    if (isTaking) {
      return;
    }

    const builder = getTimeShiftCaptureBuilder();
    if (interval != null) {
      builder.setCheckStatusCommandInterval(interval);
    }
    options?.timeShift?.isFrontFirst &&
      builder.setIsFrontFirst(options.timeShift.isFrontFirst);
    options?.timeShift?.firstInterval &&
      builder.setFirstInterval(options.timeShift.firstInterval);
    options?.timeShift?.secondInterval &&
      builder.setSecondInterval(options.timeShift.secondInterval);

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

    console.log('TimeShiftCapture interval: ' + interval);
    console.log('TimeShiftCapture options: ' + JSON.stringify(options));
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
      const url = await capture.startCapture((completion) => {
        if (isTaking) return;
        setMessage(`progress = ${completion}`);
      });
      initCapture();
      Alert.alert('TimeShift file url : ', url, [{ text: 'OK' }]);
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
              setOptions(option);
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

export default TimeShiftCaptureScreen;
