import React from 'react';
import { View, Alert, ScrollView, Text } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import styles from './styles';
import Button from '../../components/ui/button';
import {
  Options,
  ShotCountSpecifiedIntervalCapture,
  getShotCountSpecifiedIntervalCaptureBuilder,
  stopSelfTimer,
} from '../../modules/theta-client';
import { CaptureCommonOptionsEdit } from '../../components/capture/capture-common-options';
import { InputNumber } from '../../components/ui/input-number';
import { NumberEdit } from 'verification-tool/src/components/options/number-edit';
import type { NativeStackScreenProps } from '@react-navigation/native-stack';
import type { RootStackParamList } from '../../App';

const ShotCountSpecifiedIntervalCaptureScreen: React.FC<
  NativeStackScreenProps<
    RootStackParamList,
    'shotCountSpecifiedIntervalCapture'
  >
> = ({ navigation }) => {
  const [interval, setInterval] = React.useState<number>();
  const [shotCount, setShotCount] = React.useState<number>(2);
  const [message, setMessage] = React.useState('progress = 0');
  const [captureOptions, setCaptureOptions] = React.useState<Options>();
  const [isTaking, setIsTaking] = React.useState(false);
  const [capture, setCapture] =
    React.useState<ShotCountSpecifiedIntervalCapture>();

  const onTake = async () => {
    if (isTaking) {
      return;
    }

    const builder = getShotCountSpecifiedIntervalCaptureBuilder(shotCount);
    if (interval != null) {
      builder.setCheckStatusCommandInterval(interval);
    }

    if (captureOptions?.captureInterval != null) {
      builder.setCaptureInterval(captureOptions.captureInterval);
    }
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

    console.log('ShotCountSpecifiedIntervalCapture interval: ' + interval);
    console.log(
      'ShotCountSpecifiedIntervalCapture options: ' +
        JSON.stringify(captureOptions)
    );
    console.log(
      'ShotCountSpecifiedIntervalCapture builder: ' + JSON.stringify(builder)
    );

    try {
      setCapture(await builder.build());
      setIsTaking(false);
    } catch (error) {
      setIsTaking(false);
      Alert.alert(
        'ShotCountSpecifiedIntervalCapture build error',
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
    try {
      console.log('ShotCountSpecifiedIntervalCapture startCapture');
      const urls = await capture.startCapture(
        (completion) => {
          if (isTaking) return;
          setMessage(`progress = ${completion}`);
        },
        (error) => {
          Alert.alert('Cancel error', JSON.stringify(error), [{ text: 'OK' }]);
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
    navigation.setOptions({
      title: 'interval shooting with the shot count specified',
    });
  }, [navigation]);

  React.useEffect(() => {
    if (capture != null && !isTaking) {
      setIsTaking(true);
      startCapture();
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
            title="start"
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
          <InputNumber
            title="shot count"
            placeHolder="Input value"
            value={shotCount}
            onChange={(value) => {
              setShotCount(value ?? 2);
            }}
          />
          <NumberEdit
            propName={'captureInterval'}
            onChange={(option) => {
              setCaptureOptions(option);
            }}
            options={captureOptions}
            placeHolder="Input value"
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

export default ShotCountSpecifiedIntervalCaptureScreen;
