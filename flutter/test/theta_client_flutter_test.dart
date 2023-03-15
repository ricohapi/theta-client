import 'dart:async';
import 'dart:typed_data';

import 'package:flutter_test/flutter_test.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';
import 'package:theta_client_flutter/theta_client_flutter_platform_interface.dart';
import 'package:theta_client_flutter/theta_client_flutter_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

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
  Future<ThetaInfo> getThetaInfo() {
    return onGetThetaInfo();
  }

  @override
  Future<ThetaState> getThetaState() {
    return onGetThetaState();
  }

  @override
  Future<void> initialize(String endpoint, ThetaConfig? config, ThetaTimeout? timeout) {
    return onCallInitialize();
  }

  @override
  Future<bool> isInitialized() {
    return onCallIsInitialized();
  }

  @override
  Future<List<FileInfo>> listFiles(FileTypeEnum fileType, int entryCount, int startPosition) {
    return onCallListFiles();
  }

  @override
  Future<void> getPhotoCaptureBuilder() {
    return onCallGetPhotoCaptureBuilder();
  }

  @override
  Future<void> buildPhotoCapture(Map<String, dynamic> options) {
    return onCallBuildPhotoCapture(options);
  }

  @override
  Future<String?> takePicture() {
    return onCallTakePicture();
  }
  
  @override
  Future<void> getVideoCaptureBuilder() {
    return onCallGetVideoCaptureBuilder();
  }

  @override
  Future<void> buildVideoCapture(Map<String, dynamic> options) {
    return onCallBuildVideoCapture(options);
  }
  
  @override
  Future<String?> startVideoCapture() {
    return onCallStartVideoCapture();
  }
  
  @override
  Future<void> stopVideoCapture() {
    return onCallStopVideoCapture();
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
    return Future.value(Metadata(
      Exif('', '', 0, 0, 0.0, 0.0),
      Xmp(0.0, 0, 0)
    ));
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
  Future<String> convertVideoFormats(String fileUrl, bool toLowResolution, bool applyTopBottomCorrection) {
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
  Future<void> setAccessPointDynamically(String ssid, bool ssidStealth, AuthModeEnum authMode, String password, int connectionPriority) {
    return Future.value();
  }
  
  @override
  Future<void> setAccessPointStatically(String ssid, bool ssidStealth, AuthModeEnum authMode, String password, int connectionPriority, String ipAddress, String subnetMask, String defaultGateway) {
    return Future.value();
  }
  
  @override
  Future<void> deleteAccessPoint(String ssid) {
    return Future.value();
  }
}

Future<void> Function() onCallInitialize = Future.value;
Future<bool> Function() onCallIsInitialized = Future.value;
Future<ThetaInfo> Function() onGetThetaInfo = Future.value;
Future<ThetaState> Function() onGetThetaState = Future.value;
Future<void> Function() onCallGetLivePreview = Future.value;
Future<List<FileInfo>> Function() onCallListFiles = Future.value;
Future<void> Function() onCallGetPhotoCaptureBuilder = Future.value;
Future<void> Function(Map<String, dynamic> options) onCallBuildPhotoCapture = Future.value;
Future<String?> Function() onCallTakePicture = Future.value;
Future<void> Function() onCallGetVideoCaptureBuilder = Future.value;
Future<void> Function(Map<String, dynamic> options) onCallBuildVideoCapture = Future.value;
Future<String?> Function() onCallStartVideoCapture = Future.value;
Future<void> Function() onCallStopVideoCapture = Future.value;
Future<Options> Function(List<OptionNameEnum> optionNames) onCallGetOptions = (optionNames) => Future.value(Options());
Future<void> Function(Options options) onCallSetOptions = Future.value;
Future<void> Function() onCallRestoreSettings = Future.value;

void main() {
  final ThetaClientFlutterPlatform initialPlatform = ThetaClientFlutterPlatform.instance;

  test('$MethodChannelThetaClientFlutter is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelThetaClientFlutter>());
  });

  test('getPlatformVersion', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform = MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    expect(await thetaClientPlugin.getPlatformVersion(), '42');
  });

  test('initialize', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform = MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallInitialize = Future.value;
    await thetaClientPlugin.initialize('endpoint');

    expect(true, isTrue);
  });

  test('isInitialized', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform = MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallIsInitialized = () {
      return Future.value(true);
    };
    expect(await thetaClientPlugin.isInitialized(), isTrue);
  });

  test('getThetaInfo', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform = MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const model = 'RICOH THETA Z1';
    const api = ['a', 'b'];
    const apiLevel = [2];
    var endpoints = Endpoints(80, 80);
    onGetThetaInfo = () {
      return Future.value(ThetaInfo('RICOH', model, 'serialNo', 'wlanMac', 'blMac',
      'firmVersion', 'supportUrl', true, true, 1, api, endpoints, apiLevel));
    };

    var thetaInfo = await thetaClientPlugin.getThetaInfo();
    expect(thetaInfo.model, model);
  });

  test('getThetaState', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform = MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const fingerprint = 'fingerprint_1';
    onGetThetaState = () {
      return Future.value(ThetaState(
        fingerprint,
        0.9,
        ChargingStateEnum.charging,
        true,
        0,
        0,
        '')
      );
    };

    var thetaState = await thetaClientPlugin.getThetaState();
    expect(thetaState.fingerprint, fingerprint);
  });

  test('listFiles', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform = MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const name = 'R0013336.JPG';
    var infoList = List<FileInfo>.empty(growable: true);
    infoList.add(
      FileInfo(
        name,
        100,
        '2022:11:15 14:00:15',
        'http://192.168.1.1/files/150100524436344d4201375fda9dc400/100RICOH/R0013336.JPG',
        'http://192.168.1.1/files/150100524436344d4201375fda9dc400/100RICOH/R0013336.JPG?type=thumb'
      )
    );
    onCallListFiles = () {
      return Future.value(infoList);
    };

    var resultList = await thetaClientPlugin.listFiles(FileTypeEnum.image, 10, 10);
    expect(resultList, infoList);
  });

  test('getPhotoCaptureBuilder', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform = MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallGetPhotoCaptureBuilder = Future.value;

    var builder = thetaClientPlugin.getPhotoCaptureBuilder();
    expect(builder, isNotNull);
  });

  test('buildPhotoCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform = MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallGetPhotoCaptureBuilder = Future.value;

    const aperture = [ApertureEnum.aperture_2_0, 'Aperture'];
    const colorTemperature = [2, 'ColorTemperature'];
    const exposureCompensation = [ExposureCompensationEnum.m0_3, 'ExposureCompensation'];
    const exposureDelay = [ExposureDelayEnum.delay1, 'ExposureDelay'];
    const exposureProgram = [ExposureProgramEnum.aperturePriority, 'ExposureProgram'];
    const fileFormat = [PhotoFileFormatEnum.image_11K, 'PhotoFileFormat'];
    const filter = [FilterEnum.hdr, 'Filter'];
    final gpsInfo = [GpsInfo(1.0, 2.0, 3.0, '2022:01:01 00:01:00+09:00'), 'GpsInfo'];
    const gpsTagRecording = [GpsTagRecordingEnum.on, 'GpsTagRecording'];
    const iso = [IsoEnum.iso100, 'Iso'];
    const isoAutoHighLimit = [IsoAutoHighLimitEnum.iso125, 'IsoAutoHighLimit'];
    const whiteBalance = [WhiteBalanceEnum.auto, 'WhiteBalance'];

    onCallBuildPhotoCapture = (options) {
      expect(options[aperture[1]], aperture[0]);
      expect(options[colorTemperature[1]], colorTemperature[0]);
      expect(options[exposureCompensation[1]], exposureCompensation[0]);
      expect(options[exposureDelay[1]], exposureDelay[0]);
      expect(options[exposureProgram[1]], exposureProgram[0]);
      expect(options[fileFormat[1]], fileFormat[0]);
      expect(options[filter[1]], filter[0]);
      expect(options[gpsInfo[1]], gpsInfo[0]);
      expect(options[gpsTagRecording[1]], gpsTagRecording[0]);
      expect(options[iso[1]], iso[0]);
      expect(options[isoAutoHighLimit[1]], isoAutoHighLimit[0]);
      expect(options[whiteBalance[1]], whiteBalance[0]);
      return Future.value(null);
    };

    final builder = thetaClientPlugin.getPhotoCaptureBuilder();
    builder.setAperture(aperture[0] as ApertureEnum);
    builder.setColorTemperature(colorTemperature[0] as int);
    builder.setExposureCompensation(exposureCompensation[0] as ExposureCompensationEnum);
    builder.setExposureDelay(exposureDelay[0] as ExposureDelayEnum);
    builder.setExposureProgram(exposureProgram[0] as ExposureProgramEnum);
    builder.setFileFormat(fileFormat[0] as PhotoFileFormatEnum);
    builder.setFilter(filter[0] as FilterEnum);
    builder.setGpsInfo(gpsInfo[0] as GpsInfo);
    builder.setGpsTagRecording(gpsTagRecording[0] as GpsTagRecordingEnum);
    builder.setIso(iso[0] as IsoEnum);
    builder.setIsoAutoHighLimit(isoAutoHighLimit[0] as IsoAutoHighLimitEnum);
    builder.setWhiteBalance(whiteBalance[0] as WhiteBalanceEnum);

    var capture = await builder.build();
    expect(capture, isNotNull);
    expect(capture.getAperture(), aperture[0]);
    expect(capture.getColorTemperature(), colorTemperature[0]);
    expect(capture.getExposureCompensation(), exposureCompensation[0]);
    expect(capture.getExposureDelay(), exposureDelay[0]);
    expect(capture.getExposureProgram(), exposureProgram[0]);
    expect(capture.getFileFormat(), fileFormat[0]);
    expect(capture.getFilter(), filter[0]);
    expect(capture.getGpsInfo(), gpsInfo[0]);
    expect(capture.getGpsTagRecording(), gpsTagRecording[0]);
    expect(capture.getIso(), iso[0]);
    expect(capture.getIsoAutoHighLimit(), isoAutoHighLimit[0]);
    expect(capture.getWhiteBalance(), whiteBalance[0]);
  });

  test('takePicture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform = MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const imageUrl = 'http://test.jpg';

    onCallGetPhotoCaptureBuilder = Future.value;
    onCallBuildPhotoCapture = Future.value;
    onCallTakePicture = () {
      return Future.value(imageUrl);
    };

    var builder = thetaClientPlugin.getPhotoCaptureBuilder();
    var capture = await builder.build();
    String? fileUrl;
    capture.takePicture((value) { 
      expect(value, imageUrl);
      fileUrl = value;
    }, (exception) {
      expect(false, isTrue, reason: 'Error. takePicture');
    });
    await Future.delayed(const Duration(milliseconds: 10), (){});
    expect(fileUrl, imageUrl);
    expect(capture.getAperture(), isNull);
  });

  test('takePicture Exception', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform = MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallGetPhotoCaptureBuilder = Future.value;
    onCallBuildPhotoCapture = Future.value;
    onCallTakePicture = () {
      return Future.error(Exception('Error. takePicture'));
    };

    var builder = thetaClientPlugin.getPhotoCaptureBuilder();
    var capture = await builder.build();
    String? fileUrl;
    dynamic error;
    capture.takePicture((value) { 
      fileUrl = value;
      expect(false, isTrue, reason: 'Error. takePicture');
    }, (exception) {
      error = exception;
      expect(exception, isNotNull);
    });
    await Future.delayed(const Duration(milliseconds: 10), (){});
    expect(fileUrl, isNull);
    expect(error, isNotNull);
  });

  test('getVideoCaptureBuilder', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform = MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallGetVideoCaptureBuilder = Future.value;

    var builder = thetaClientPlugin.getVideoCaptureBuilder();
    expect(builder, isNotNull);
  });

  test('buildVideoCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform = MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallGetVideoCaptureBuilder = Future.value;

    const aperture = [ApertureEnum.aperture_2_0, 'Aperture'];
    const colorTemperature = [2, 'ColorTemperature'];
    const exposureCompensation = [ExposureCompensationEnum.m0_3, 'ExposureCompensation'];
    const exposureDelay = [ExposureDelayEnum.delay1, 'ExposureDelay'];
    const exposureProgram = [ExposureProgramEnum.aperturePriority, 'ExposureProgram'];
    const fileFormat = [VideoFileFormatEnum.video_2K_30F, 'VideoFileFormat'];
    final gpsInfo = [GpsInfo(1.0, 2.0, 3.0, '2022:01:01 00:01:00+09:00'), 'GpsInfo'];
    const gpsTagRecording = [GpsTagRecordingEnum.on, 'GpsTagRecording'];
    const iso = [IsoEnum.iso100, 'Iso'];
    const isoAutoHighLimit = [IsoAutoHighLimitEnum.iso125, 'IsoAutoHighLimit'];
    const whiteBalance = [WhiteBalanceEnum.auto, 'WhiteBalance'];
    const maxRecordableTime = [MaxRecordableTimeEnum.time_1500, 'MaxRecordableTime'];

    onCallBuildVideoCapture = (options) {
      expect(options[aperture[1]], aperture[0]);
      expect(options[colorTemperature[1]], colorTemperature[0]);
      expect(options[exposureCompensation[1]], exposureCompensation[0]);
      expect(options[exposureDelay[1]], exposureDelay[0]);
      expect(options[exposureProgram[1]], exposureProgram[0]);
      expect(options[fileFormat[1]], fileFormat[0]);
      expect(options[gpsInfo[1]], gpsInfo[0]);
      expect(options[gpsTagRecording[1]], gpsTagRecording[0]);
      expect(options[iso[1]], iso[0]);
      expect(options[isoAutoHighLimit[1]], isoAutoHighLimit[0]);
      expect(options[maxRecordableTime[1]], maxRecordableTime[0]);
      expect(options[whiteBalance[1]], whiteBalance[0]);
      return Future.value(null);
    };

    final builder = thetaClientPlugin.getVideoCaptureBuilder();
    builder.setAperture(aperture[0] as ApertureEnum);
    builder.setColorTemperature(colorTemperature[0] as int);
    builder.setExposureCompensation(exposureCompensation[0] as ExposureCompensationEnum);
    builder.setExposureDelay(exposureDelay[0] as ExposureDelayEnum);
    builder.setExposureProgram(exposureProgram[0] as ExposureProgramEnum);
    builder.setFileFormat(fileFormat[0] as VideoFileFormatEnum);
    builder.setMaxRecordableTime(maxRecordableTime[0] as MaxRecordableTimeEnum);
    builder.setGpsInfo(gpsInfo[0] as GpsInfo);
    builder.setGpsTagRecording(gpsTagRecording[0] as GpsTagRecordingEnum);
    builder.setIso(iso[0] as IsoEnum);
    builder.setIsoAutoHighLimit(isoAutoHighLimit[0] as IsoAutoHighLimitEnum);
    builder.setWhiteBalance(whiteBalance[0] as WhiteBalanceEnum);

    var capture = await builder.build();
    expect(capture, isNotNull);
    expect(capture.getAperture(), aperture[0]);
    expect(capture.getColorTemperature(), colorTemperature[0]);
    expect(capture.getExposureCompensation(), exposureCompensation[0]);
    expect(capture.getExposureDelay(), exposureDelay[0]);
    expect(capture.getExposureProgram(), exposureProgram[0]);
    expect(capture.getFileFormat(), fileFormat[0]);
    expect(capture.getMaxRecordableTime(), maxRecordableTime[0]);
    expect(capture.getGpsInfo(), gpsInfo[0]);
    expect(capture.getGpsTagRecording(), gpsTagRecording[0]);
    expect(capture.getIso(), iso[0]);
    expect(capture.getIsoAutoHighLimit(), isoAutoHighLimit[0]);
    expect(capture.getWhiteBalance(), whiteBalance[0]);
  });

  test('startVideoCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform = MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const imageUrl = 'http://test.mp4';

    onCallGetVideoCaptureBuilder = Future.value;
    onCallBuildVideoCapture = Future.value;
    onCallStartVideoCapture = () {
      return Future.value(imageUrl);
    };

    var builder = thetaClientPlugin.getVideoCaptureBuilder();
    var capture = await builder.build();
    String? fileUrl;
    capture.startCapture((value) { 
      expect(value, imageUrl);
      fileUrl = value;
    }, (exception) {
      expect(false, isTrue, reason: 'Error. startCapture');
    });
    await Future.delayed(const Duration(milliseconds: 10), (){});
    expect(fileUrl, imageUrl);
    expect(capture.getAperture(), isNull);
  });

  test('startVideoCapture Exception', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform = MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallGetVideoCaptureBuilder = Future.value;
    onCallBuildVideoCapture = Future.value;
    var completer = Completer<String>();
    onCallStartVideoCapture = () {
      return completer.future;
    };
    onCallStopVideoCapture = () {
      completer.completeError(Exception('Error. startVideoCapture'));
      return Future.value();
    };

    var builder = thetaClientPlugin.getVideoCaptureBuilder();
    var capture = await builder.build();
    var capturing = capture.startCapture((value) { 
      expect(false, isTrue, reason: 'startCapture');
    }, (exception) {
      expect(exception, isNotNull, reason: 'Error. startCapture');
    });
    capturing.stopCapture();
    await Future.delayed(const Duration(milliseconds: 10), (){});
    expect(capture.getAperture(), isNull);
  });

  test('stopVideoCapture', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform = MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    const imageUrl = 'http://test.mp4';

    onCallGetVideoCaptureBuilder = Future.value;
    onCallBuildVideoCapture = Future.value;
    var completer = Completer<String>();
    onCallStartVideoCapture = () {
      return completer.future;
    };
    onCallStopVideoCapture = () {
      completer.complete(imageUrl);
      return Future.value();
    };

    var builder = thetaClientPlugin.getVideoCaptureBuilder();
    var capture = await builder.build();
    String? fileUrl;
    var capturing = capture.startCapture((value) { 
      expect(value, imageUrl);
      fileUrl = value;
    }, (exception) {
      expect(false, isTrue, reason: 'Error. startCapture');
    });
    capturing.stopCapture();
    await Future.delayed(const Duration(milliseconds: 10), (){});
    expect(fileUrl, imageUrl);
    expect(capture.getAperture(), isNull);
  });

  test('getOptions', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform = MockThetaClientFlutterPlatform();
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
    MockThetaClientFlutterPlatform fakePlatform = MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallSetOptions = Future.value;

    final options = Options();
    options.captureMode = CaptureModeEnum.image;

    thetaClientPlugin.setOptions(options);
    expect(true, isTrue);
  });

  test('restoreSettings', () async {
    ThetaClientFlutter thetaClientPlugin = ThetaClientFlutter();
    MockThetaClientFlutterPlatform fakePlatform = MockThetaClientFlutterPlatform();
    ThetaClientFlutterPlatform.instance = fakePlatform;

    onCallRestoreSettings = Future.value;
    await thetaClientPlugin.restoreSettings();

    expect(true, isTrue);
  });
}
