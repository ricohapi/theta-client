import React from 'react';
import { View, Alert, ScrollView } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import styles from './styles';
import Button from '../../components/ui/button';
import {
  CaptureModeEnum,
  Options,
  PhotoFileFormatEnum,
  getPhotoCaptureBuilder,
  setOptions,
  stopSelfTimer,
} from 'theta-client-react-native';
import {
  FilterEdit,
  PhotoFileFormatEdit,
  PresetEdit,
} from '../../components/options';
import { CaptureCommonOptionsEdit } from '../../components/capture/capture-common-options';

const PhotoCaptureScreen: React.FC = ({ navigation }) => {
  const [captureOptions, setCaptureOptions] = React.useState<Options>();
  const [isTaking, setIsTaking] = React.useState(false);

  const onTakePhoto = async () => {
    setIsTaking(true);

    const builder = getPhotoCaptureBuilder();
    captureOptions?.filter && builder.setFilter(captureOptions.filter);
    captureOptions?.fileFormat &&
      builder.setFileFormat(captureOptions.fileFormat as PhotoFileFormatEnum);
    captureOptions?.preset && builder.setPreset(captureOptions.preset);

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

    console.log('takePicture options: :' + JSON.stringify(captureOptions));
    console.log('takePicture builder: :' + JSON.stringify(builder));

    try {
      const photoCapture = await builder.build();
      const url = await photoCapture.takePicture();

      setIsTaking(false);

      Alert.alert('takePicture file url : ', url, [{ text: 'OK' }]);
    } catch (error) {
      setIsTaking(false);
      Alert.alert('takePicture error', JSON.stringify(error), [{ text: 'OK' }]);
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
    navigation.setOptions({ title: 'Photo Capture' });
  }, [navigation]);

  return (
    <SafeAreaView
      style={styles.safeAreaContainer}
      edges={['left', 'right', 'bottom']}
    >
      <View style={styles.topViewContainer}>
        <View style={styles.bottomViewContainerLayout}>
          <Button
            style={styles.button}
            title="take photo"
            onPress={onTakePhoto}
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
          <FilterEdit
            onChange={(option) => {
              setCaptureOptions((prevState) => ({
                ...prevState,
                filter: option.filter,
              }));
            }}
            options={captureOptions}
          />
          <PhotoFileFormatEdit
            onChange={(option) => {
              setCaptureOptions((prevState) => ({
                ...prevState,
                fileFormat: option.fileFormat,
              }));
            }}
            options={captureOptions}
          />
          <PresetEdit
            onChange={(option) => {
              setCaptureOptions((prevState) => ({
                ...prevState,
                preset: option.preset,
              }));
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

export default PhotoCaptureScreen;
