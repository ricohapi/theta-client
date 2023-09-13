import React from 'react';
import { View, Alert, ScrollView } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import styles from './styles';
import Button from '../../components/ui/button';
import {
  Options,
  PhotoFileFormatEnum,
  getPhotoCaptureBuilder,
  stopSelfTimer,
} from 'theta-client-react-native';
import {
  FilterEdit,
  PhotoFileFormatEdit,
  PresetEdit,
} from '../../components/options';
import { CaptureCommonOptionsEdit } from '../../components/capture/capture-common-options';

const PhotoCaptureScreen: React.FC = ({ navigation }) => {
  const [options, setOptions] = React.useState<Options>();
  const [isTaking, setIsTaking] = React.useState(false);

  React.useEffect(() => {
    navigation.setOptions({ title: 'Photo Capture' });
  }, [navigation]);

  const onTakePhoto = async () => {
    setIsTaking(true);

    const builder = getPhotoCaptureBuilder();
    options?.filter && builder.setFilter(options.filter);
    options?.fileFormat &&
      builder.setFileFormat(options.fileFormat as PhotoFileFormatEnum);
    options?.preset && builder.setPreset(options.preset);

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

    console.log('takePicture options: :' + JSON.stringify(options));
    console.log('takePicture builder: :' + JSON.stringify(builder));

    try {
      const photoCapture = await builder.build();
      const url = await photoCapture.takePicture();

      setIsTaking(false);

      Alert.alert('takePicture file url : ', url, [{ text: 'OK' }]);

      // const urls = url.split('/');
      // navigation.navigate('filePreview', {
      //     item: { fileUrl: url, name: urls[urls.length - 1] },
      // });
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
              setOptions((prevState) => ({
                ...prevState,
                filter: option.filter,
              }));
            }}
            options={options}
          />
          <PhotoFileFormatEdit
            onChange={(option) => {
              setOptions((prevState) => ({
                ...prevState,
                fileFormat: option.fileFormat,
              }));
            }}
            options={options}
          />
          <PresetEdit
            onChange={(option) => {
              setOptions((prevState) => ({
                ...prevState,
                preset: option.preset,
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

export default PhotoCaptureScreen;
