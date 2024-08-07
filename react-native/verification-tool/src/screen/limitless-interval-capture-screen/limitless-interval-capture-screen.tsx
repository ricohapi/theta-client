import React from 'react';
import { View, Alert, ScrollView, Text } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import styles from './styles';
import Button from '../../components/ui/button';
import {
  CapturingStatusEnum,
  LimitlessIntervalCapture,
  Options,
  getLimitlessIntervalCaptureBuilder,
  stopSelfTimer,
} from '../../modules/theta-client';
import { CaptureCommonOptionsEdit } from '../../components/capture/capture-common-options';
import { NumberEdit } from '../../components/options/number-edit';
import type { NativeStackScreenProps } from '@react-navigation/native-stack';
import type { RootStackParamList } from '../../App';
import { InputNumber } from '../../components/ui/input-number';

const LimitlessIntervalCaptureScreen: React.FC<
  NativeStackScreenProps<RootStackParamList, 'limitlessIntervalCapture'>
> = ({ navigation }) => {
  const [interval, setInterval] = React.useState<number>();
  const [message, setMessage] = React.useState('');
  const [capturingStatus, setCapturingStatus] =
    React.useState<CapturingStatusEnum>();
  const [options, setOptions] = React.useState<Options>();
  const [isTaking, setIsTaking] = React.useState(false);
  const [capture, setCapture] = React.useState<LimitlessIntervalCapture>();

  React.useEffect(() => {
    navigation.setOptions({ title: 'Limitless Interval Capture' });
  }, [navigation]);

  const onTake = async () => {
    if (isTaking) {
      return;
    }

    const builder = getLimitlessIntervalCaptureBuilder();
    if (interval != null) {
      builder.setCheckStatusCommandInterval(interval);
    }
    if (options?.captureInterval != null) {
      builder.setCaptureInterval(options.captureInterval);
    }

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

    console.log(
      'LimitlessIntervalCapture options: :' + JSON.stringify(options)
    );
    console.log(
      'LimitlessIntervalCapture builder: :' + JSON.stringify(builder)
    );

    try {
      setCapture(await builder.build());
      setIsTaking(false);
    } catch (error) {
      setIsTaking(false);
      Alert.alert(
        'LimitlessIntervalCaptureBuilder build error',
        JSON.stringify(error),
        [{ text: 'OK' }]
      );
    }
  };

  const initCapture = () => {
    setCapture(undefined);
    setIsTaking(false);
  };

  const startCapture = async () => {
    if (capture == null) {
      initCapture();
      return;
    }
    setCapturingStatus(undefined);
    try {
      const urls = await capture.startCapture(
        (error) => {
          Alert.alert('stopCapture error', JSON.stringify(error), [
            { text: 'OK' },
          ]);
        },
        (status) => {
          setCapturingStatus(status);
        }
      );
      initCapture();
      if (urls) {
        Alert.alert(`file ${urls.length} urls : `, urls.join('\n'), [
          { text: 'OK' },
        ]);
      } else {
        Alert.alert('Capture', 'Capture cancel', [{ text: 'OK' }]);
      }
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
      startCapture();
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
            title="start"
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
          <InputNumber
            title="CheckStatusCommandInterval"
            placeHolder="Input value"
            value={interval}
            onChange={(value) => {
              setInterval(value);
            }}
          />
          <NumberEdit
            propName={'captureInterval'}
            onChange={(option) => {
              setOptions(option);
            }}
            options={options}
            placeHolder="Input value"
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

export default LimitlessIntervalCaptureScreen;
