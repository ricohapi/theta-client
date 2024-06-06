import React from 'react';
import { View, Alert, ScrollView, Text } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import styles from './styles';
import Button from '../../components/ui/button';
import {
  CaptureModeEnum,
  ExposureDelayEnum,
  MultiBracketCapture,
  OptionNameEnum,
  Options,
  getMultiBracketCaptureBuilder,
  getOptions,
  setOptions,
  stopSelfTimer,
} from '../../modules/theta-client';
import { AutoBracketEdit } from '../../components/options/auto-bracket';
import { InputNumber } from '../../components/ui/input-number';
import type { NativeStackScreenProps } from '@react-navigation/native-stack';
import type { RootStackParamList } from '../../App';
import { EnumEdit } from '../../components/options';

const MultiBracketCaptureScreen: React.FC<
  NativeStackScreenProps<RootStackParamList, 'multiBracketCapture'>
> = ({ navigation }) => {
  const [interval, setInterval] = React.useState<number>();
  const [message, setMessage] = React.useState('progress = 0');
  const [captureOptions, setCaptureOptions] = React.useState<Options>();
  const [isTaking, setIsTaking] = React.useState(false);
  const [capture, setCapture] = React.useState<MultiBracketCapture>();

  const onTake = async () => {
    if (isTaking) {
      return;
    }

    const builder = getMultiBracketCaptureBuilder();
    if (interval != null) {
      builder.setCheckStatusCommandInterval(interval);
    }
    captureOptions?.exposureDelay &&
      builder.setExposureDelay(captureOptions.exposureDelay);
    captureOptions?.autoBracket &&
      builder.setBracketSettings(captureOptions.autoBracket);

    console.log('MultiBracketCapture interval: ' + interval);
    console.log(
      'MultiBracketCapture options: ' + JSON.stringify(captureOptions)
    );
    console.log('MultiBracketCapture builder: ' + JSON.stringify(builder));

    try {
      setCapture(await builder.build());
      setIsTaking(false);
    } catch (error) {
      setIsTaking(false);
      if (error instanceof Error) {
        Alert.alert(
          'MultiBracketCaptureBuilder build error',
          error.name + ': ' + error.message,
          [{ text: 'OK' }]
        );
      }
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
      console.log('MultiBracketCapture startCapture');
      const urls = await capture.startCapture(
        (completion) => {
          if (isTaking) return;
          setMessage(`progress = ${completion}`);
        },
        (error) => {
          if (error instanceof Error) {
            Alert.alert('Cancel error', error.name + ': ' + error.message, [
              { text: 'OK' },
            ]);
          }
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
      if (error instanceof Error) {
        Alert.alert('startCapture error', error.name + ': ' + error.message, [
          { text: 'OK' },
        ]);
      }
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
      if (error instanceof Error) {
        Alert.alert('stopCapture error', error.name + ': ' + error.message, [
          { text: 'OK' },
        ]);
      }
    }
  };

  const onStopSelfTimer = async () => {
    try {
      await stopSelfTimer();
    } catch (error) {
      if (error instanceof Error) {
        Alert.alert('stopSelfTimer error', error.name + ': ' + error.message, [
          { text: 'OK' },
        ]);
      }
    }
  };

  const onGetAutoBracket = async () => {
    try {
      const res = await getOptions([OptionNameEnum.AutoBracket]);
      if (res?.autoBracket) {
        setCaptureOptions((prevState) => ({
          ...prevState,
          autoBracket: res.autoBracket,
        }));
      }
    } catch (error) {
      if (error instanceof Error) {
        Alert.alert('getOptions error', error.name + ': ' + error.message, [
          { text: 'OK' },
        ]);
      }
    }
  };

  React.useEffect(() => {
    setOptions({ captureMode: CaptureModeEnum.IMAGE }).catch();
  }, []);

  React.useEffect(() => {
    navigation.setOptions({ title: 'MultiBracket Capture' });
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
            title="take MultiBracket"
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
        <View style={styles.bottomViewContainerLayout}>
          <Button
            style={styles.button}
            title="get autoBracket"
            onPress={onGetAutoBracket}
            disabled={isTaking}
          />
          <Button
            style={styles.button}
            title="stop self timer"
            onPress={onStopSelfTimer}
            disabled={!isTaking}
          />
        </View>
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
            title={'exposureDelay'}
            option={captureOptions?.exposureDelay}
            onChange={(exposureDelay) => {
              setCaptureOptions((prevState) => ({
                ...prevState,
                exposureDelay,
              }));
            }}
            optionEnum={ExposureDelayEnum}
          />
          <AutoBracketEdit
            onChange={(option) => {
              setCaptureOptions((prevState) => ({
                ...prevState,
                autoBracket: option.autoBracket,
              }));
            }}
            options={captureOptions}
          />
        </ScrollView>
      </View>
    </SafeAreaView>
  );
};

export default MultiBracketCaptureScreen;
