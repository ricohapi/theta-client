import 'dart:typed_data';

import 'package:flutter_test/flutter_test.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';
import 'package:theta_client_flutter/theta_client_flutter_method_channel.dart';
import 'package:theta_client_flutter/theta_client_flutter_platform_interface.dart';

class MockThetaClientFlutterPlatform
    with MockPlatformInterfaceMixin
    implements ThetaClientFlutterPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  Future<void> getLivePreview(bool Function(Uint8List p1) frameHandler) {
    return onCallGetLivePreview();
  }

  @override
  Future<ThetaModel?> getThetaModel() {
    return onGetThetaModel();
  }

  @override
  Future<ThetaInfo> getThetaInfo() {
    return onGetThetaInfo();
  }

  @override
  Future<String> getThetaLicense() {
    return onGetThetaLicense();
  }

  @override
  Future<ThetaState> getThetaState() {
    return onGetThetaState();
  }

  @override
  Future<void> initialize(
      String endpoint, ThetaConfig? config, ThetaTimeout? timeout) {
    return onCallInitialize();
  }

  @override
  Future<bool> isInitialized() {
    return onCallIsInitialized();
  }

  @override
  Future<ThetaFiles> listFiles(FileTypeEnum fileType, int entryCount,
      int startPosition, StorageEnum? storage) {
    return onCallListFiles();
  }

  @override
  Future<void> getPhotoCaptureBuilder() {
    return onCallGetPhotoCaptureBuilder();
  }

  @override
  Future<void> buildPhotoCapture(Map<String, dynamic> options, int interval) {
    return onCallBuildPhotoCapture(options, interval);
  }

  @override
  Future<String?> takePicture(
      void Function(CapturingStatusEnum status)? onCapturing) {
    return onCallTakePicture(onCapturing);
  }

  @override
  Future<void> getTimeShiftCaptureBuilder() {
    return onCallGetTimeShiftCaptureBuilder();
  }

  @override
  Future<void> buildTimeShiftCapture(
      Map<String, dynamic> options, int interval) {
    return onCallBuildTimeShiftCapture(options, interval);
  }

  @override
  Future<String?> startTimeShiftCapture(void Function(double)? onProgress,
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) {
    return onCallStartTimeShiftCapture(onProgress, onStopFailed, onCapturing);
  }

  @override
  Future<void> stopTimeShiftCapture() {
    return onCallStopTimeShiftCapture();
  }

  @override
  Future<void> getVideoCaptureBuilder() {
    return onCallGetVideoCaptureBuilder();
  }

  @override
  Future<void> buildVideoCapture(Map<String, dynamic> options, int interval) {
    return onCallBuildVideoCapture(options, interval);
  }

  @override
  Future<String?> startVideoCapture(
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) {
    return onCallStartVideoCapture(onStopFailed, onCapturing);
  }

  @override
  Future<void> stopVideoCapture() {
    return onCallStopVideoCapture();
  }

  @override
  Future<void> buildLimitlessIntervalCapture(Map<String, dynamic> options,
      int interval) {
    return onCallBuildLimitlessIntervalCapture(options, interval);
  }

  @override
  Future<void> getLimitlessIntervalCaptureBuilder() {
    return onCallGetLimitlessIntervalCaptureBuilder();
  }

  @override
  Future<List<String>?> startLimitlessIntervalCapture(
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) {
    return onCallStartLimitlessIntervalCapture(onStopFailed, onCapturing);
  }

  @override
  Future<void> stopLimitlessIntervalCapture() {
    return onCallStopLimitlessIntervalCapture();
  }

  @override
  Future<void> buildShotCountSpecifiedIntervalCapture(
      Map<String, dynamic> options, int interval) {
    return onCallBuildShotCountSpecifiedIntervalCapture(options, interval);
  }

  @override
  Future<void> getShotCountSpecifiedIntervalCaptureBuilder(int shotCount) {
    return onCallGetShotCountSpecifiedIntervalCaptureBuilder(shotCount);
  }

  @override
  Future<List<String>?> startShotCountSpecifiedIntervalCapture(
      void Function(double)? onProgress,
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) {
    return onCallStartShotCountSpecifiedIntervalCapture(
        onProgress, onStopFailed, onCapturing);
  }

  @override
  Future<void> stopShotCountSpecifiedIntervalCapture() {
    return onCallStopShotCountSpecifiedIntervalCapture();
  }

  @override
  Future<void> buildCompositeIntervalCapture(
      Map<String, dynamic> options, int interval) {
    return onCallBuildCompositeIntervalCapture(options, interval);
  }

  @override
  Future<void> getCompositeIntervalCaptureBuilder(int shootingTimeSec) {
    return onCallGetCompositeIntervalCaptureBuilder(shootingTimeSec);
  }

  @override
  Future<List<String>?> startCompositeIntervalCapture(
      void Function(double)? onProgress,
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) {
    return onCallStartCompositeIntervalCapture(
        onProgress, onStopFailed, onCapturing);
  }

  @override
  Future<void> stopCompositeIntervalCapture() {
    return onCallStopCompositeIntervalCapture();
  }

  @override
  Future<void> buildBurstCapture(Map<String, dynamic> options, int interval) {
    return onCallBuildBurstCapture(options, interval);
  }

  @override
  Future<void> getBurstCaptureBuilder(
      BurstCaptureNumEnum burstCaptureNum,
      BurstBracketStepEnum burstBracketStep,
      BurstCompensationEnum burstCompensation,
      BurstMaxExposureTimeEnum burstMaxExposureTime,
      BurstEnableIsoControlEnum burstEnableIsoControl,
      BurstOrderEnum burstOrder) {
    return onCallGetBurstCaptureBuilder();
  }

  @override
  Future<List<String>?> startBurstCapture(
      void Function(double p1)? onProgress,
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) {
    return onCallStartBurstCapture(onProgress, onStopFailed, onCapturing);
  }

  @override
  Future<void> stopBurstCapture() {
    return onCallStopBurstCapture();
  }

  @override
  Future<void> buildMultiBracketCapture(
      Map<String, dynamic> options, int interval) {
    return onCallBuildMultiBracketCapture(options, interval);
  }

  @override
  Future<void> getMultiBracketCaptureBuilder() {
    return onCallGetMultiBracketCaptureBuilder();
  }

  @override
  Future<List<String>?> startMultiBracketCapture(
      void Function(double)? onProgress,
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) {
    return onCallStartMultiBracketCapture(
        onProgress, onStopFailed, onCapturing);
  }

  @override
  Future<void> stopMultiBracketCapture() {
    return onCallStopMultiBracketCapture();
  }

  @override
  Future<void> buildContinuousCapture(
      Map<String, dynamic> options, int interval) {
    return onCallBuildContinuousCapture(options, interval);
  }

  @override
  Future<void> getContinuousCaptureBuilder() {
    return onCallGetContinuousCaptureBuilder();
  }

  @override
  Future<List<String>?> startContinuousCapture(
      void Function(double p1)? onProgress,
      void Function(CapturingStatusEnum status)? onCapturing) {
    return onCallStartContinuousCapture(onProgress, onCapturing);
  }

  @override
  Future<Options> getOptions(List<OptionNameEnum> optionNames) {
    return onCallGetOptions(optionNames);
  }

  @override
  Future<void> setOptions(Options options) {
    return onCallSetOptions(options);
  }

  @override
  Future<void> restoreSettings() {
    return onCallRestoreSettings();
  }

  @override
  Future<void> deleteAllFiles() {
    return Future.value();
  }

  @override
  Future<void> deleteAllImageFiles() {
    return Future.value();
  }

  @override
  Future<void> deleteAllVideoFiles() {
    return Future.value();
  }

  @override
  Future<void> deleteFiles(List<String> fileUrls) {
    return Future.value();
  }

  @override
  Future<Metadata> getMetadata(String fileUrl) {
    return Future.value(Metadata(Exif('', '', 0, 0, 0.0, 0.0), Xmp(0.0, 0, 0)));
  }

  @override
  Future<void> reset() {
    return Future.value();
  }

  @override
  Future<void> stopSelfTimer() {
    return Future.value();
  }

  @override
  Future<String> convertVideoFormats(
      String fileUrl, bool toLowResolution, bool applyTopBottomCorrection) {
    return Future.value('');
  }

  @override
  Future<void> cancelVideoConvert() {
    return Future.value();
  }

  @override
  Future<void> finishWlan() {
    return Future.value();
  }

  @override
  Future<List<AccessPoint>> listAccessPoints() {
    return Future.value([]);
  }

  @override
  Future<void> setAccessPointDynamically(
      String ssid,
      bool ssidStealth,
      AuthModeEnum authMode,
      String password,
      int connectionPriority,
      Proxy? proxy) {
    return Future.value();
  }

  @override
  Future<void> setAccessPointStatically(
      String ssid,
      bool ssidStealth,
      AuthModeEnum authMode,
      String password,
      int connectionPriority,
      String ipAddress,
      String subnetMask,
      String defaultGateway,
      Proxy? proxy) {
    return Future.value();
  }

  @override
  Future<void> deleteAccessPoint(String ssid) {
    return Future.value();
  }

  @override
  Future<Options> getMySetting(CaptureModeEnum? captureMode) {
    return Future.value(Options());
  }

  @override
  Future<Options> getMySettingFromOldModel(List<OptionNameEnum> optionNames) {
    return Future.value(Options());
  }

  @override
  Future<void> setMySetting(CaptureModeEnum captureMode, Options options) {
    return Future.value();
  }

  @override
  Future<void> deleteMySetting(CaptureModeEnum captureMode) {
    return Future.value();
  }

  @override
  Future<List<PluginInfo>> listPlugins() {
    return onCallGetPluginInfos();
  }

  @override
  Future<void> setPlugin(String packageName) {
    return Future.value();
  }

  @override
  Future<void> startPlugin(String? packageName) {
    return Future.value();
  }

  @override
  Future<void> stopPlugin() {
    return Future.value();
  }

  @override
  Future<String> getPluginLicense(String packageName) {
    return onCallGetString(packageName);
  }

  @override
  Future<List<String>> getPluginOrders() {
    return onCallGetStringList();
  }

  @override
  Future<void> setPluginOrders(List<String> plugins) {
    return Future.value();
  }

  @override
  Future<String> setBluetoothDevice(String uuid) {
    return onCallGetString(uuid);
  }
}

Future<void> Function() onCallInitialize = Future.value;
Future<bool> Function() onCallIsInitialized = Future.value;
Future<ThetaModel?> Function() onGetThetaModel = Future.value;
Future<ThetaInfo> Function() onGetThetaInfo = Future.value;
Future<String> Function() onGetThetaLicense = Future.value;
Future<ThetaState> Function() onGetThetaState = Future.value;
Future<void> Function() onCallGetLivePreview = Future.value;
Future<ThetaFiles> Function() onCallListFiles = Future.value;

Future<void> Function() onCallGetPhotoCaptureBuilder = Future.value;
Future<void> Function(Map<String, dynamic> options, int interval) onCallBuildPhotoCapture =
    (options, interval) => Future.value();
Future<String?> Function(void Function(CapturingStatusEnum)? onCapturing)
    onCallTakePicture = (onCapturing) => Future.value();

Future<void> Function() onCallGetTimeShiftCaptureBuilder = Future.value;
Future<void> Function(Map<String, dynamic> options, int interval)
onCallBuildTimeShiftCapture = (options, interval) => Future.value();
Future<String?> Function(void Function(double)? onProgress,
    void Function(Exception exception)? onStopFailed,
    void Function(CapturingStatusEnum status)? onCapturing)
onCallStartTimeShiftCapture = (onProgress, onStopFailed, onCapturing) =>
    Future.value();
Future<void> Function() onCallStopTimeShiftCapture = Future.value;

Future<void> Function() onCallGetVideoCaptureBuilder = Future.value;
Future<void> Function(Map<String, dynamic> options, int interval)
    onCallBuildVideoCapture = (options, interval) => Future.value();
Future<String?> Function(void Function(Exception exception)? onStopFailed,
        void Function(CapturingStatusEnum status)? onCapturing)
    onCallStartVideoCapture = (onStopFailed, onCapturing) => Future.value();
Future<void> Function() onCallStopVideoCapture = Future.value;

Future<void> Function() onCallGetLimitlessIntervalCaptureBuilder = Future.value;
Future<void> Function(Map<String, dynamic> options, int interval)
onCallBuildLimitlessIntervalCapture = (options, interval) => Future.value();
Future<List<String>?> Function(void Function(Exception exception)? onStopFailed,
    void Function(CapturingStatusEnum status)? onCapturing)
onCallStartLimitlessIntervalCapture = (onStopFailed, onCapturing) =>
    Future.value();
Future<void> Function() onCallStopLimitlessIntervalCapture = Future.value;

Future<void> Function(int shotCount)
onCallGetShotCountSpecifiedIntervalCaptureBuilder = Future.value;
Future<void> Function(Map<String, dynamic> options, int interval)
onCallBuildShotCountSpecifiedIntervalCapture =
    (options, interval) => Future.value();
Future<List<String>?> Function(void Function(double)? onProgress,
    void Function(Exception exception)? onStopFailed,
    void Function(CapturingStatusEnum status)? onCapturing)
onCallStartShotCountSpecifiedIntervalCapture =
    (onProgress, onStopFailed, onCapturing) => Future.value();
Future<void> Function() onCallStopShotCountSpecifiedIntervalCapture =
    Future.value;

Future<void> Function(int shootingTimeSec)
onCallGetCompositeIntervalCaptureBuilder = Future.value;
Future<void> Function(Map<String, dynamic> options, int interval)
onCallBuildCompositeIntervalCapture = (options, interval) => Future.value();
Future<List<String>?> Function(void Function(double)? onProgress,
    void Function(Exception exception)? onStopFailed,
    void Function(CapturingStatusEnum status)? onCapturing)
onCallStartCompositeIntervalCapture =
    (onProgress, onStopFailed, onCapturing) => Future.value();
Future<void> Function() onCallStopCompositeIntervalCapture = Future.value;

Future<void> Function() onCallGetBurstCaptureBuilder = Future.value;
Future<void> Function(Map<String, dynamic> options, int interval)
    onCallBuildBurstCapture = (options, interval) => Future.value();
Future<List<String>?> Function(
        void Function(double)? onProgress,
        void Function(Exception exception)? onStopFailed,
        void Function(CapturingStatusEnum status)? onCapturing)
    onCallStartBurstCapture =
    (onProgress, onStopFailed, onCapturing) => Future.value();
Future<void> Function() onCallStopBurstCapture = Future.value;

Future<void> Function() onCallGetMultiBracketCaptureBuilder = Future.value;
Future<void> Function(Map<String, dynamic> options, int interval)
    onCallBuildMultiBracketCapture = (options, interval) => Future.value();
Future<List<String>?> Function(
        void Function(double)? onProgress,
        void Function(Exception exception)? onStopFailed,
        void Function(CapturingStatusEnum status)? onCapturing)
    onCallStartMultiBracketCapture =
    (onProgress, onStopFailed, onCapturing) => Future.value();
Future<void> Function() onCallStopMultiBracketCapture = Future.value;

Future<void> Function() onCallGetContinuousCaptureBuilder = Future.value;
Future<void> Function(Map<String, dynamic> options, int interval)
onCallBuildContinuousCapture = (options, interval) => Future.value();
Future<List<String>?> Function(void Function(double)? onProgress,
    void Function(CapturingStatusEnum status)? onCapturing)
onCallStartContinuousCapture = (onProgress, onCapturing) => Future.value();

Future<Options> Function(List<OptionNameEnum> optionNames) onCallGetOptions =
    (optionNames) => Future.value(Options());
Future<void> Function(Options options) onCallSetOptions = Future.value;
Future<void> Function() onCallRestoreSettings = Future.value;
Future<List<PluginInfo>> Function() onCallGetPluginInfos = Future.value;
Future<String> Function(String str) onCallGetString = Future.value;
Future<List<String>> Function() onCallGetStringList = Future.value;

void main() {
  final ThetaClientFlutterPlatform initialPlatform =
      ThetaClientFlutterPlatform.instance;

  test('$MethodChannelThetaClientFlutter is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelThetaClientFlutter>());
  });

  test('getPlatformVersion', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    expect(await thetaClientPlugin.getPlatformVersion(), '42');
  });

  test('initialize', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallInitialize = Future.value;
    await thetaClientPlugin.initialize('endpoint');

    expect(true, isTrue);
  });

  test('isInitialized', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallIsInitialized = () {
      return Future.value(true);
    };
    expect(await thetaClientPlugin.isInitialized(), isTrue);
  });

  test('getThetaModel', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const thetaModel = ThetaModel.thetaZ1;
    onGetThetaModel = () {
      return Future.value(thetaModel);
    };

    var model = await thetaClientPlugin.getThetaModel();
    expect(model, thetaModel);
  });

  test('getThetaInfo', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const model = 'RICOH THETA Z1';
    const thetaModel = ThetaModel.thetaZ1;
    const api = ['a', 'b'];
    const apiLevel = [2];
    var endpoints = Endpoints(80, 80);
    onGetThetaInfo = () {
      return Future.value(ThetaInfo(
          'RICOH',
          model,
          'serialNo',
          'wlanMac',
          'blMac',
          'firmVersion',
          'supportUrl',
          true,
          true,
          1,
          api,
          endpoints,
          apiLevel,
          thetaModel));
    };

    var thetaInfo = await thetaClientPlugin.getThetaInfo();
    expect(thetaInfo.model, model);
    expect(thetaInfo.thetaModel, thetaModel);
  });

  test('getThetaLicense', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var license = """
    <html>
      <head></head>
      <body></body>
    </html>
    """;
    onGetThetaLicense = () {
      return Future.value(license);
    };

    var result = await thetaClientPlugin.getThetaLicense();
    expect(result, license);
  });

  test('getThetaState', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const fingerprint = 'fingerprint_1';
    const batteryLevel = 1.0;
    const storageUri = 'storageUri storageUri';
    const storageID = 'storageID storageID';
    const captureStatus = CaptureStatusEnum.idle;
    const recordedTime = 1;
    const recordableTime = 2;
    const capturedPictures = 3;
    const compositeShootingElapsedTime = 4;
    const latestFileUrl = 'latestFileUrl latestFileUrl';
    const chargingState = ChargingStateEnum.charging;
    const apiVersion = 2;
    const isPluginRunning = true;
    const isPluginWebServer = false;
    const function = ShootingFunctionEnum.normal;
    const isMySettingChanged = true;
    const currentMicrophone = MicrophoneOptionEnum.auto;
    const isSdCard = true;
    const cameraError = [
      CameraErrorEnum.batteryChargeFail,
      CameraErrorEnum.batteryHighTemperature
    ];
    const isBatteryInsert = false;
    var externalGpsInfo =
        StateGpsInfo(GpsInfo(1.0, 2.0, 3.0, '2022:01:01 00:01:00+09:00'));
    var internalGpsInfo = StateGpsInfo(null);
    const boardTemp = 40;
    const batteryTemp = 50;
    onGetThetaState = () {
      return Future.value(ThetaState(
          fingerprint,
          batteryLevel,
          storageUri,
          storageID,
          captureStatus,
          recordedTime,
          recordableTime,
          capturedPictures,
          compositeShootingElapsedTime,
          latestFileUrl,
          chargingState,
          apiVersion,
          isPluginRunning,
          isPluginWebServer,
          function,
          isMySettingChanged,
          currentMicrophone,
          isSdCard,
          cameraError,
          isBatteryInsert,
          externalGpsInfo,
          internalGpsInfo,
          boardTemp,
          batteryTemp));
    };

    var thetaState = await thetaClientPlugin.getThetaState();

    expect(thetaState.fingerprint, fingerprint);
    expect(thetaState.batteryLevel, batteryLevel);
    expect(thetaState.storageUri, storageUri);
    expect(thetaState.storageID, storageID);
    expect(thetaState.captureStatus, captureStatus);
    expect(thetaState.recordedTime, recordedTime);
    expect(thetaState.recordableTime, recordableTime);
    expect(thetaState.capturedPictures, capturedPictures);
    expect(
        thetaState.compositeShootingElapsedTime, compositeShootingElapsedTime);
    expect(thetaState.latestFileUrl, latestFileUrl);
    expect(thetaState.chargingState, chargingState);
    expect(thetaState.apiVersion, apiVersion);
    expect(thetaState.isPluginRunning, isPluginRunning);
    expect(thetaState.isPluginWebServer, isPluginWebServer);
    expect(thetaState.function, function);
    expect(thetaState.isMySettingChanged, isMySettingChanged);
    expect(thetaState.currentMicrophone, currentMicrophone);
    expect(thetaState.isSdCard, isSdCard);
    expect(thetaState.cameraError, cameraError);
    expect(thetaState.isBatteryInsert, isBatteryInsert);
    expect(thetaState.externalGpsInfo, externalGpsInfo);
    expect(thetaState.internalGpsInfo?.gpsInfo, null);
    expect(thetaState.boardTemp, 40);
    expect(thetaState.batteryTemp, 50);
  });

  test('getOptions', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallGetOptions = (optionNames) {
      return Future.value(Options());
    };

    const optionNames = [
      OptionNameEnum.captureMode,
      OptionNameEnum.gpsInfo,
    ];

    var options = thetaClientPlugin.getOptions(optionNames);
    expect(options, isNotNull);
  });

  test('setOptions', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallSetOptions = Future.value;

    final options = Options();
    options.captureMode = CaptureModeEnum.image;

    thetaClientPlugin.setOptions(options);
    expect(true, isTrue);
  });

  test('restoreSettings', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallRestoreSettings = Future.value;
    await thetaClientPlugin.restoreSettings();

    expect(true, isTrue);
  });

  test('getMySetting', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallGetOptions = (optionNames) {
      return Future.value(Options());
    };

    var options = thetaClientPlugin.getMySetting(CaptureModeEnum.image);
    expect(options, isNotNull);
  });

  test('getMySettingFromOldModel', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallGetOptions = (optionNames) {
      return Future.value(Options());
    };

    const optionNames = [
      OptionNameEnum.aperture,
      OptionNameEnum.colorTemperature,
    ];

    var options = thetaClientPlugin.getMySettingFromOldModel(optionNames);
    expect(options, isNotNull);
  });

  test('setMySetting', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallSetOptions = Future.value;

    final options = Options();
    options.aperture = ApertureEnum.apertureAuto;

    thetaClientPlugin.setMySetting(CaptureModeEnum.image, options);
    expect(true, isTrue);
  });

  test('deleteMySetting', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    thetaClientPlugin.deleteMySetting(CaptureModeEnum.image);
    expect(true, isTrue);
  });

  test('listPlugins', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var infoList = List<PluginInfo>.empty(growable: true);
    infoList.add(PluginInfo(
      "AwesomeProject",
      "com.awesomeproject",
      '1.0',
      false,
      false,
      false,
      false,
      false,
      "success",
      '',
    ));

    onCallGetPluginInfos = () {
      return Future.value(infoList);
    };

    var result = await thetaClientPlugin.listPlugins();
    expect(result, infoList);
  });

  test('setPlugin', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    thetaClientPlugin.setPlugin('com.theta360.usbstorage');
    expect(true, isTrue);
  });

  test('startPlugin', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    thetaClientPlugin.startPlugin('com.theta360.usbstorage');
    expect(true, isTrue);
  });

  test('stopPlugin', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    thetaClientPlugin.stopPlugin();
    expect(true, isTrue);
  });

  test('getPluginLicense', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var license = """
    <!DOCTYPE html>
    <html>
    <head>
      <meta charset="UTF-8"/>
      <meta name="viewport" content="width=device-width,initial-scale=1,viewport-fit=cover">
      <title>RICOH THETA - Underside cover</title>
    </head>
    <body>
    </body>
    </html>
    """;
    var packageName = 'com.theta360.usbstorage';
    onCallGetString = (packageName) {
      return Future.value(license);
    };

    var result = await thetaClientPlugin.getPluginLicense(packageName);
    expect(result, license);
  });

  test('getPluginOrders', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var strList = List<String>.empty(growable: true);
    strList.add("com.awesomeproject");

    onCallGetStringList = () {
      return Future.value(strList);
    };

    var result = await thetaClientPlugin.getPluginOrders();
    expect(result, strList);
  });

  test('setPluginOrders', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    thetaClientPlugin.setPluginOrders(['com.theta360.usbstorage']);
    expect(true, isTrue);
  });

  test('setBluetoothDevice', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform =
        MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    var name = '10107709';
    var uuid = '00000000-1111-2222-3333-555555555555';
    onCallGetString = (uuid) {
      return Future.value(name);
    };

    var result = await thetaClientPlugin.setBluetoothDevice(uuid);
    expect(result, name);
  });
}
