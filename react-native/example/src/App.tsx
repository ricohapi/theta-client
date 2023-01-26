import * as React from 'react';

import {
  StyleSheet,
  View,
  Text,
  Image,
  NativeEventEmitter,
  NativeModules,
} from 'react-native';
import {
  initialize,
  getThetaInfo,
  getThetaState,
  listFiles,
  FileTypeEnum,
  getOptions,
  setOptions,
  OptionNameEnum,
  deleteFiles,
  deleteAllFiles,
  deleteAllImageFiles,
  deleteAllVideoFiles,
  getLivePreview,
  stopLivePreview,
  getPhotoCaptureBuilder,
  PhotoFileFormatEnum,
  VideoFileFormatEnum,
  FilterEnum,
  ApertureEnum,
  ExposureCompensationEnum,
  ExposureDelayEnum,
  ExposureProgramEnum,
  GpsTagRecordingEnum,
  IsoEnum,
  IsoAutoHighLimitEnum,
  WhiteBalanceEnum,
  getVideoCaptureBuilder,
  MaxRecordableTimeEnum,
  getMetadata,
  reset,
  restoreSettings,
  stopSelfTimer,
  convertVideoFormats,
  cancelVideoConvert,
  finishWlan,
  listAccessPoints,
  setAccessPointDynamically,
  setAccessPointStatically,
  deleteAccessPoint,
  AuthModeEnum,
  THETA_EVENT_NAME,
} from '@ricohapi/theta-client-react-native';

async function optionsTest() {
  const optionList: [OptionNameEnum] = [
    OptionNameEnum.Aperture,
    OptionNameEnum.CaptureMode,
    OptionNameEnum.ColorTemperature,
    OptionNameEnum.DateTimeZone,
    OptionNameEnum.ExposureCompensation,
    OptionNameEnum.ExposureDelay,
    OptionNameEnum.ExposureProgram,
    OptionNameEnum.FileFormat,
    OptionNameEnum.Filter,
    OptionNameEnum.GpsInfo,
    OptionNameEnum.IsGpsOn,
    OptionNameEnum.Iso,
    OptionNameEnum.IsoAutoHighLimit,
    OptionNameEnum.Language,
    OptionNameEnum.MaxRecordableTime,
    OptionNameEnum.OffDelay,
    OptionNameEnum.SleepDelay,
    OptionNameEnum.RemainingPictures,
    OptionNameEnum.RemainingVideoSeconds,
    OptionNameEnum.RemainingSpace,
    OptionNameEnum.TotalSpace,
    OptionNameEnum.ShutterVolume,
    OptionNameEnum.WhiteBalance,
  ];
  const options = await getOptions(optionList);
  console.log(options);
  const result = await setOptions(options);
  console.log(`set result = ${result}`);
}

async function listFilesTest() {
  const lists = await listFiles(FileTypeEnum.IMAGE, 0, 10);
  console.log(lists);
}

async function deleteTest() {
  const del1 = await deleteFiles(['file1', 'file2']);
  console.log(`deleteFiles = ${del1}`);
  const del2 = await deleteAllFiles();
  console.log(`deleteAllFiles = ${del2}`);
  const del3 = await deleteAllImageFiles();
  console.log(`deleteAllImageFiles = ${del3}`);
  const del4 = await deleteAllVideoFiles();
  console.log(`deleteAllVideoFiles = ${del4}`);
}

const delay = (m) => {
  return new Promise((resolve) => setTimeout(resolve, m));
};

async function livePreviewTest() {
  getLivePreview().then((x) => {
    console.log(`preview done: ${x}`);
  });
  await delay(5000);
  stopLivePreview();
}

async function photoCaptureTest() {
  const photoCapture = await getPhotoCaptureBuilder()
    .setFileFormat(PhotoFileFormatEnum.IMAGE_4K)
    .setFilter(FilterEnum.HDR)
    .setAperture(ApertureEnum.APERTURE_5_6)
    .setColorTemperature(2500)
    .setExposureCompensation(ExposureCompensationEnum.ZERO)
    .setExposureDelay(ExposureDelayEnum.DELAY_OFF)
    .setExposureProgram(ExposureProgramEnum.NORMAL_PROGRAM)
    .setGpsInfo({ latitude: 10, longitude: 10, altitude: 10, dateTimeZone: '' })
    .setGpsTagRecording(GpsTagRecordingEnum.OFF)
    .setIso(IsoEnum.ISO_100)
    .setIsoAutoHighLimit(IsoAutoHighLimitEnum.ISO_1000)
    .setWhiteBalance(WhiteBalanceEnum.AUTO)
    .build();
  const url = await photoCapture.takePicture();
  console.log(`taken picture: ${url}`);
}

async function videoCaptureTest() {
  const videoCapture = await getVideoCaptureBuilder()
    .setFileFormat(VideoFileFormatEnum.VIDEO_4K)
    .setMaxRecordableTime(MaxRecordableTimeEnum.RECORDABLE_TIME_300)
    .setAperture(ApertureEnum.APERTURE_5_6)
    .setColorTemperature(2500)
    .setExposureCompensation(ExposureCompensationEnum.ZERO)
    .setExposureDelay(ExposureDelayEnum.DELAY_OFF)
    .setExposureProgram(ExposureProgramEnum.NORMAL_PROGRAM)
    .setGpsInfo({ latitude: 10, longitude: 10, altitude: 10, dateTimeZone: '' })
    .setGpsTagRecording(GpsTagRecordingEnum.OFF)
    .setIso(IsoEnum.ISO_100)
    .setIsoAutoHighLimit(IsoAutoHighLimitEnum.ISO_1000)
    .setWhiteBalance(WhiteBalanceEnum.AUTO)
    .build();
  videoCapture
    .startCapture()
    .then((x) => {
      console.log(`captured file: ${x}`);
    })
    .catch((error) => {
      console.log(`capture start failed ${error}`);
    });
  await delay(1000);
  videoCapture.stopCapture();
}

function stringify(object: Any) {
  return JSON.stringify(object);
}
async function otherTest() {
  console.log(
    `get metadata = ${stringify(await getMetadata('/test/xxx.jpg'))}`
  );
  console.log(`reset = ${await reset()}`);
  console.log(`restore settings = ${await restoreSettings()}`);
  console.log(`stop selfTimer = ${await stopSelfTimer()}`);
  console.log(
    `convert videoFormat = ${await convertVideoFormats('aaa.jpg', true, false)}`
  );
  console.log(`cancel video format = ${await cancelVideoConvert()}`);
  console.log(`finishWlan = ${await finishWlan()}`);
  console.log(`listAP = ${stringify(await listAccessPoints())}`);
  console.log(`setAP d = ${await setAccessPointDynamically('ssid')}`);
  console.log(
    `setAP s = ${await setAccessPointStatically(
      'ssid',
      false,
      AuthModeEnum.NONE,
      'pass',
      1,
      '192.47.121.16',
      '255.255.255.0',
      '192.47.121.1'
    )}`
  );
  console.log(`delete accessPoint = ${await deleteAccessPoint('ssid')}`);
}

export default function App() {
  const [thetaInfo, setThetaInfo] = React.useState<ThetaInfo | undefined>();
  const [thetaState, setThetaState] = React.useState<ThetaState | undefined>();
  const [dataurl, setDataurl] = React.useState<string | undefined>();

  React.useEffect(() => {
    const eventListener = new NativeEventEmitter(
      NativeModules.ThetaClientReactNative
    ).addListener(THETA_EVENT_NAME, (event) => {
      setDataurl(event.data);
    });
    initialize('http://172.16.1.6:8000').then(async () => {
      getThetaInfo().then(setThetaInfo);
      getThetaState().then(setThetaState);
      await listFilesTest();
      await optionsTest();
      await deleteTest();
      await livePreviewTest();
      await photoCaptureTest();
      await videoCaptureTest();
      await otherTest();
    });
    return () => {
      eventListener.remove();
    };
  }, []);

  return (
    <View style={styles.container}>
      <Image style={styles.image} source={{ uri: dataurl }} />
      <Text>Theta INFO</Text>
      <Text>model: {thetaInfo?.model}</Text>
      <Text>serialNumber: {thetaInfo?.serialNumber}</Text>
      <Text>firmwareVersion: {thetaInfo?.firmwareVersion}</Text>
      <Text>hasGps: {'' + thetaInfo?.hasGps}</Text>
      <Text>hasGyro: {'' + thetaInfo?.hasGyro}</Text>
      <Text>uptime: {thetaInfo?.uptime}</Text>
      <Text>Theta State</Text>
      <Text>fingerprint: {thetaState?.fingerprint}</Text>
      <Text>batteryLevel: {thetaState?.batteryLevel}</Text>
      <Text>chargingState: {thetaState?.chargingState}</Text>
      <Text>isSdCard: {'' + thetaState?.isSdCard}</Text>
      <Text>recordedTime: {'' + thetaState?.recordedTime}</Text>
      <Text>recordableTime: {'' + thetaState?.recordableTime}</Text>
      <Text>latestFileUrl: {thetaState?.latestFileUrl}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  image: {
    width: 200,
    height: 100,
    resizeMode: 'contain',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
