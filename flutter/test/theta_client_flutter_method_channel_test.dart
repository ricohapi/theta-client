import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';
import 'package:theta_client_flutter/theta_client_flutter_method_channel.dart';

void main() {
  MethodChannelThetaClientFlutter platform = MethodChannelThetaClientFlutter();
  const MethodChannel channel = MethodChannel('theta_client_flutter');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler(null);
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
    expect(await platform.getPlatformVersion(), '42');
  });

  test('getThetaInfo', () async {
    const manufacturer = 'RICOH';
    const model = 'RICOH THETA Z1';
    const serialNumber = '10010065';
    const wlanMacAddress = '00:26:73:D7:93:C2';
    const bluetoothMacAddress = '00:26:73:D6:93:C2';
    const firmwareVersion = '2.10.3';
    const supportUrl = 'https://theta360.com/en/support/';
    const hasGps = true;
    const hasGyro = true;
    const uptime = 142;
    const api = ['/osc/info', '/osc/state', '/osc/checkForUpdates',
      '/osc/commands/execute','/osc/commands/status'];
    var endpoints = <String, int>{'httpPort': 80, 'httpUpdatesPort': 80};
    const apiLevel = [2];
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      final Map info = <String, dynamic> {
        'manufacturer': manufacturer,
        'model': model,
        'serialNumber': serialNumber,
        'wlanMacAddress': wlanMacAddress,
        'bluetoothMacAddress': bluetoothMacAddress,
        'firmwareVersion': firmwareVersion,
        'supportUrl': supportUrl,
        'hasGps': hasGps,
        'hasGyro': hasGyro,
        'uptime': uptime,
        'api': api,
        'endpoints': endpoints,
        'apiLevel': apiLevel,
      };
      return info;
    });

    var thetaInfo = await platform.getThetaInfo();
    expect(thetaInfo.manufacturer, manufacturer);
    expect(thetaInfo.model, model);
    expect(thetaInfo.serialNumber, serialNumber);
    expect(thetaInfo.wlanMacAddress, wlanMacAddress);
    expect(thetaInfo.bluetoothMacAddress, bluetoothMacAddress);
    expect(thetaInfo.firmwareVersion, firmwareVersion);
    expect(thetaInfo.supportUrl, supportUrl);
    expect(thetaInfo.hasGps, hasGps);
    expect(thetaInfo.hasGyro, hasGyro);
    expect(thetaInfo.uptime, uptime);
    expect(thetaInfo.api, api);
    expect(thetaInfo.endpoints.httpPort, 80);
    expect(thetaInfo.endpoints.httpUpdatesPort, 80);
    expect(thetaInfo.apiLevel, apiLevel);
  });

  test('getThetaState', () async {
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
    const cameraError = [CameraErrorEnum.batteryChargeFail, CameraErrorEnum.batteryHighTemperature];
    const isBatteryInsert = false;

    List<String> convertCameraErrorParam(List<CameraErrorEnum> cameraErrorList) {
      var stringList = List<String>.empty(growable: true);
      for (CameraErrorEnum element in cameraErrorList) {
        stringList.add(element.rawValue);
      }
      return stringList;
    }

    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      final Map state = <String, dynamic> {
        'fingerprint': fingerprint,
        'batteryLevel': batteryLevel,
        'storageUri': storageUri,
        'storageID': storageID,
        'captureStatus': captureStatus.rawValue,
        'recordedTime': recordedTime,
        'recordableTime': recordableTime,
        'capturedPictures': capturedPictures,
        'compositeShootingElapsedTime': compositeShootingElapsedTime,
        'latestFileUrl': latestFileUrl,
        'chargingState': chargingState.rawValue,
        'apiVersion': apiVersion,
        'isPluginRunning': isPluginRunning,
        'isPluginWebServer': isPluginWebServer,
        'function': function.rawValue,
        'isMySettingChanged': isMySettingChanged,
        'currentMicrophone': currentMicrophone.rawValue,
        'isSdCard': isSdCard,
        'cameraError': convertCameraErrorParam(cameraError),
        'isBatteryInsert': isBatteryInsert,
      };
      return state;
    });

    var thetaState = await platform.getThetaState();

    expect(thetaState.fingerprint, fingerprint);
    expect(thetaState.batteryLevel, batteryLevel);
    expect(thetaState.storageUri, storageUri);
    expect(thetaState.storageID, storageID);
    expect(thetaState.captureStatus, captureStatus);
    expect(thetaState.recordedTime, recordedTime);
    expect(thetaState.recordableTime, recordableTime);
    expect(thetaState.capturedPictures, capturedPictures);
    expect(thetaState.compositeShootingElapsedTime, compositeShootingElapsedTime);
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
  });

  test('getThetaState nullable', () async {
    const fingerprint = 'fingerprint_1';
    const batteryLevel = 1.0;
    const captureStatus = CaptureStatusEnum.idle;
    const recordedTime = 1;
    const recordableTime = 2;
    const latestFileUrl = 'latestFileUrl latestFileUrl';
    const chargingState = ChargingStateEnum.charging;
    const apiVersion = 2;
    const isSdCard = true;

    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      final Map state = <String, dynamic> {
        'fingerprint': fingerprint,
        'batteryLevel': batteryLevel,
        'captureStatus': captureStatus.rawValue,
        'recordedTime': recordedTime,
        'recordableTime': recordableTime,
        'latestFileUrl': latestFileUrl,
        'chargingState': chargingState.rawValue,
        'apiVersion': apiVersion,
        'isSdCard': isSdCard,
      };
      return state;
    });

    var thetaState = await platform.getThetaState();

    expect(thetaState.fingerprint, fingerprint);
    expect(thetaState.batteryLevel, batteryLevel);
    expect(thetaState.storageUri, isNull);
    expect(thetaState.storageID, isNull);
    expect(thetaState.captureStatus, captureStatus);
    expect(thetaState.recordedTime, recordedTime);
    expect(thetaState.recordableTime, recordableTime);
    expect(thetaState.capturedPictures, isNull);
    expect(thetaState.compositeShootingElapsedTime, isNull);
    expect(thetaState.latestFileUrl, latestFileUrl);
    expect(thetaState.chargingState, chargingState);
    expect(thetaState.apiVersion, apiVersion);
    expect(thetaState.isPluginRunning, isNull);
    expect(thetaState.isPluginWebServer, isNull);
    expect(thetaState.function, isNull);
    expect(thetaState.isMySettingChanged, isNull);
    expect(thetaState.currentMicrophone, isNull);
    expect(thetaState.isSdCard, isSdCard);
    expect(thetaState.cameraError, isNull);
    expect(thetaState.isBatteryInsert, isNull);
  });

  test('listFiles', () async {
    const fileTypes = [FileTypeEnum.all, FileTypeEnum.image, FileTypeEnum.video];
    const entryCount = 10;
    const startPosition = 0;
    const name = 'R0013336.JPG';
    const size = 100;
    const dateTime = '2022:11:15 14:00:15';
    const fileUrl = 'http://192.168.1.1/files/150100524436344d4201375fda9dc400/100RICOH/R0013336.JPG';
    const thumbnailUrl = 'http://192.168.1.1/files/150100524436344d4201375fda9dc400/100RICOH/R0013336.JPG?type=thumb';

    int index = 0;
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['fileType'], fileTypes[index].rawValue);
      expect(arguments['entryCount'], entryCount);
      expect(arguments['startPosition'], startPosition);

      final List fileList = List<dynamic>.empty(growable: true);
      final Map info = <String, dynamic> {
        'name': name,
        'size': size,
        'dateTime': dateTime,
        'fileUrl': fileUrl,
        'thumbnailUrl': thumbnailUrl,
      };
      fileList.add(info);
      fileList.add(info);
      final Map thetaFiles = <String, dynamic> {
        'fileList': fileList,
        'totalEntries': 10,
      };
      return thetaFiles;
    });

    for (int i = 0; i < fileTypes.length; i++) {
      index = i;
      var thetaFiles = await platform.listFiles(fileTypes[i], entryCount, startPosition);
      expect(thetaFiles.fileList.length, 2);
      var fileInfo = thetaFiles.fileList[0];
      expect(fileInfo.name, name);
      expect(fileInfo.size, size);
      expect(fileInfo.dateTime, dateTime);
      expect(fileInfo.fileUrl, fileUrl);
      expect(fileInfo.thumbnailUrl, thumbnailUrl);
      expect(thetaFiles.totalEntries, 10);
    }
  });

  test('buildPhotoCapture', () async {
    Map<String, dynamic> gpsInfoMap = {
      'latitude': 1.0, 'longitude': 2.0, 'altitude': 3.0, 'dateTimeZone': '2022:01:01 00:01:00+09:00'
    };
    List<List<dynamic>> data = [
      ['Aperture', ApertureEnum.aperture_2_0, 'APERTURE_2_0'],
      ['ColorTemperature', 2, 2],
      ['ExposureCompensation', ExposureCompensationEnum.m0_3, 'M0_3'],
      ['ExposureDelay', ExposureDelayEnum.delay1, 'DELAY_1'],
      ['ExposureProgram', ExposureProgramEnum.aperturePriority, 'APERTURE_PRIORITY'],
      ['GpsInfo', GpsInfo(1.0, 2.0, 3.0, '2022:01:01 00:01:00+09:00'), gpsInfoMap],
      ['GpsTagRecording', GpsTagRecordingEnum.on, 'ON'],
      ['Iso', IsoEnum.iso50, 'ISO_50'],
      ['IsoAutoHighLimit', IsoAutoHighLimitEnum.iso200, 'ISO_200'],
      ['WhiteBalance', WhiteBalanceEnum.bulbFluorescent, 'BULB_FLUORESCENT'],
      ['Filter', FilterEnum.hdr, 'HDR'],
      ['PhotoFileFormat', PhotoFileFormatEnum.rawP_6_7K, 'RAW_P_6_7K'],
    ];

    Map<String, dynamic> options = {};
    for (int i = 0; i < data.length; i++) {
      options[data[i][0]] = data[i][1];
    }

    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      for (int i = 0; i < data.length; i++) {
        expect(arguments[data[i][0]], data[i][2], reason: data[i][0]);
      }

      return Future.value();
    });
    await platform.buildPhotoCapture(options);
  });

  test('takePicture', () async {
    const fileUrl = 'http://192.168.1.1/files/150100524436344d4201375fda9dc400/100RICOH/R0013336.JPG';
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return fileUrl;
    });
    expect(await platform.takePicture(), fileUrl);
  });

  test('buildVideoCapture', () async {
    Map<String, dynamic> gpsInfoMap = {
      'latitude': 1.0, 'longitude': 2.0, 'altitude': 3.0, 'dateTimeZone': '2022:01:01 00:01:00+09:00'
    };
    List<List<dynamic>> data = [
      ['Aperture', ApertureEnum.aperture_2_0, 'APERTURE_2_0'],
      ['ColorTemperature', 2, 2],
      ['ExposureCompensation', ExposureCompensationEnum.m0_3, 'M0_3'],
      ['ExposureDelay', ExposureDelayEnum.delay1, 'DELAY_1'],
      ['ExposureProgram', ExposureProgramEnum.aperturePriority, 'APERTURE_PRIORITY'],
      ['GpsInfo', GpsInfo(1.0, 2.0, 3.0, '2022:01:01 00:01:00+09:00'), gpsInfoMap],
      ['GpsTagRecording', GpsTagRecordingEnum.on, 'ON'],
      ['Iso', IsoEnum.iso50, 'ISO_50'],
      ['IsoAutoHighLimit', IsoAutoHighLimitEnum.iso200, 'ISO_200'],
      ['WhiteBalance', WhiteBalanceEnum.bulbFluorescent, 'BULB_FLUORESCENT'],
      ['MaxRecordableTime', MaxRecordableTimeEnum.time_1500, 'RECORDABLE_TIME_1500'],
      ['VideoFileFormat', VideoFileFormatEnum.videoFullHD, 'VIDEO_FULL_HD'],
    ];

    Map<String, dynamic> options = {};
    for (int i = 0; i < data.length; i++) {
      options[data[i][0]] = data[i][1];
    }

    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      for (int i = 0; i < data.length; i++) {
        expect(arguments[data[i][0]], data[i][2], reason: data[i][0]);
      }

      return Future.value();
    });
    await platform.buildVideoCapture(options);
  });

  test('startVideoCapture', () async {
    const fileUrl = 'http://192.168.1.1/files/150100524436344d4201375fda9dc400/100RICOH/R0013336.MP4';
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return fileUrl;
    });
    expect(await platform.startVideoCapture(), fileUrl);
  });

  test('getOptions', () async {
    Map<String, dynamic> gpsInfoMap = {
      'latitude': 1.0, 'longitude': 2.0, 'altitude': 3.0, 'dateTimeZone': '2022:01:01 00:01:00+09:00'
    };
    List<List<dynamic>> data = [
      [OptionNameEnum.aperture, 'Aperture', ApertureEnum.aperture_2_0, 'APERTURE_2_0'],
      [OptionNameEnum.cameraMode, 'CameraMode', CameraModeEnum.capture, 'CAPTURE'],
      [OptionNameEnum.captureMode, 'CaptureMode', CaptureModeEnum.image, 'IMAGE'],
      [OptionNameEnum.colorTemperature, 'ColorTemperature', 2, 2],
      [OptionNameEnum.dateTimeZone, 'DateTimeZone', '2022:01:01 00:01:00+09:00', '2022:01:01 00:01:00+09:00'],
      [OptionNameEnum.exposureCompensation, 'ExposureCompensation', ExposureCompensationEnum.m0_3, 'M0_3'],
      [OptionNameEnum.exposureDelay, 'ExposureDelay', ExposureDelayEnum.delay1, 'DELAY_1'],
      [OptionNameEnum.exposureProgram, 'ExposureProgram', ExposureProgramEnum.aperturePriority, 'APERTURE_PRIORITY'],
      [OptionNameEnum.fileFormat, 'FileFormat', FileFormatEnum.image_2K, 'IMAGE_2K'],
      [OptionNameEnum.filter, 'Filter', FilterEnum.hdr, 'HDR'],
      [OptionNameEnum.gpsInfo, 'GpsInfo', GpsInfo(1.0, 2.0, 3.0, '2022:01:01 00:01:00+09:00'), gpsInfoMap],
      [OptionNameEnum.isGpsOn, 'IsGpsOn', true, true],
      [OptionNameEnum.iso, 'Iso', IsoEnum.iso50, 'ISO_50'],
      [OptionNameEnum.isoAutoHighLimit, 'IsoAutoHighLimit', IsoAutoHighLimitEnum.iso200, 'ISO_200'],
      [OptionNameEnum.language, 'Language', LanguageEnum.de, 'DE'],
      [OptionNameEnum.maxRecordableTime, 'MaxRecordableTime', MaxRecordableTimeEnum.time_1500, 'RECORDABLE_TIME_1500'],
      [OptionNameEnum.offDelay, 'OffDelay', OffDelayEnum.offDelay_10m, 'OFF_DELAY_10M'],
      [OptionNameEnum.sleepDelay, 'SleepDelay', SleepDelayEnum.sleepDelay_10m, 'SLEEP_DELAY_10M'],
      [OptionNameEnum.remainingPictures, 'RemainingPictures', 3, 3],
      [OptionNameEnum.remainingVideoSeconds, 'RemainingVideoSeconds', 4, 4],
      [OptionNameEnum.remainingSpace, 'RemainingSpace', 5, 5],
      [OptionNameEnum.totalSpace, 'TotalSpace', 6, 6],
      [OptionNameEnum.shutterVolume, 'ShutterVolume', 7, 7],
      [OptionNameEnum.whiteBalance, 'WhiteBalance', WhiteBalanceEnum.bulbFluorescent, 'BULB_FLUORESCENT'],
      [OptionNameEnum.whiteBalanceAutoStrength, 'WhiteBalanceAutoStrength', WhiteBalanceAutoStrengthEnum.off, 'OFF'],
    ];

    Map<String, dynamic> optionMap = {};
    var optionNames = List<OptionNameEnum>.empty(growable: true);

    for (int i = 0; i < data.length; i++) {
      optionMap[data[i][1]] = data[i][3];
      optionNames.add(data[i][0]);
    }

    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      var arguments = methodCall.arguments as List<dynamic>;
      for (int i = 0; i < data.length; i++) {
        expect(arguments[i], data[i][1], reason: data[i][1]);
      }
      return Future.value(optionMap);
    });
    Options options = await platform.getOptions(optionNames);

    expect(options, isNotNull);
    expect(options.aperture, data[0][2]);
    expect(options.cameraMode, data[1][2]);
    expect(options.captureMode, data[2][2]);
    for (int i = 0; i < data.length; i++) {
      expect(options.getValue(data[i][0]), data[i][2], reason: data[i][1]);
    }
  });

  test('setOptions', () async {
    Map<String, dynamic> gpsInfoMap = {
      'latitude': 1.0, 'longitude': 2.0, 'altitude': 3.0, 'dateTimeZone': '2022:01:01 00:01:00+09:00'
    };
    List<List<dynamic>> data = [
      [OptionNameEnum.aperture, 'Aperture', ApertureEnum.aperture_2_0, 'APERTURE_2_0'],
      [OptionNameEnum.cameraMode, 'CameraMode', CameraModeEnum.capture, 'CAPTURE'],
      [OptionNameEnum.captureMode, 'CaptureMode', CaptureModeEnum.image, 'IMAGE'],
      [OptionNameEnum.colorTemperature, 'ColorTemperature', 2, 2],
      [OptionNameEnum.dateTimeZone, 'DateTimeZone', '2022:01:01 00:01:00+09:00', '2022:01:01 00:01:00+09:00'],
      [OptionNameEnum.exposureCompensation, 'ExposureCompensation', ExposureCompensationEnum.m0_3, 'M0_3'],
      [OptionNameEnum.exposureDelay, 'ExposureDelay', ExposureDelayEnum.delay1, 'DELAY_1'],
      [OptionNameEnum.exposureProgram, 'ExposureProgram', ExposureProgramEnum.aperturePriority, 'APERTURE_PRIORITY'],
      [OptionNameEnum.fileFormat, 'FileFormat', FileFormatEnum.image_2K, 'IMAGE_2K'],
      [OptionNameEnum.filter, 'Filter', FilterEnum.hdr, 'HDR'],
      [OptionNameEnum.gpsInfo, 'GpsInfo', GpsInfo(1.0, 2.0, 3.0, '2022:01:01 00:01:00+09:00'), gpsInfoMap],
      [OptionNameEnum.isGpsOn, 'IsGpsOn', true, true],
      [OptionNameEnum.iso, 'Iso', IsoEnum.iso50, 'ISO_50'],
      [OptionNameEnum.isoAutoHighLimit, 'IsoAutoHighLimit', IsoAutoHighLimitEnum.iso200, 'ISO_200'],
      [OptionNameEnum.language, 'Language', LanguageEnum.de, 'DE'],
      [OptionNameEnum.maxRecordableTime, 'MaxRecordableTime', MaxRecordableTimeEnum.time_1500, 'RECORDABLE_TIME_1500'],
      [OptionNameEnum.offDelay, 'OffDelay', OffDelayEnum.offDelay_15m, 'OFF_DELAY_15M'],
      [OptionNameEnum.sleepDelay, 'SleepDelay', SleepDelayEnum.sleepDelay_10m, 'SLEEP_DELAY_10M'],
      [OptionNameEnum.remainingPictures, 'RemainingPictures', 3, 3],
      [OptionNameEnum.remainingVideoSeconds, 'RemainingVideoSeconds', 4, 4],
      [OptionNameEnum.remainingSpace, 'RemainingSpace', 5, 5],
      [OptionNameEnum.totalSpace, 'TotalSpace', 6, 6],
      [OptionNameEnum.shutterVolume, 'ShutterVolume', 7, 7],
      [OptionNameEnum.whiteBalance, 'WhiteBalance', WhiteBalanceEnum.bulbFluorescent, 'BULB_FLUORESCENT'],
      [OptionNameEnum.whiteBalanceAutoStrength, 'WhiteBalanceAutoStrength', WhiteBalanceAutoStrengthEnum.on, 'ON'],
    ];

    Map<String, dynamic> optionMap = {};
    var optionNames = List<OptionNameEnum>.empty(growable: true);

    for (int i = 0; i < data.length; i++) {
      optionMap[data[i][1]] = data[i][3];
      optionNames.add(data[i][0]);
    }
    final options = Options();
    for (int i = 0; i < data.length; i++) {
      options.setValue(data[i][0], data[i][2]);
    }

    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      for (int i = 0; i < data.length; i++) {
        expect(arguments[data[i][1]], data[i][3], reason: data[i][1]);
      }
      return Future.value();
    });
    await platform.setOptions(options);

    expect(true, isTrue);
  });

  test('initialize endpoint', () async {
    const endpoint = 'http://dummy/';

    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['endpoint'], endpoint);
      expect(arguments['config'], isNull);
      expect(arguments['timeout'], isNull);
      return Future.value();
    });
    await platform.initialize(endpoint, null, null);
  });

  test('initialize config', () async {
    const endpoint = 'http://dummy/';
    final config = ThetaConfig();
    config.dateTime = '2022:01:01 00:01:00+09:00';
    config.language = LanguageEnum.ja;
    config.offDelay = OffDelayEnum.offDelay_15m;
    config.sleepDelay = SleepDelayEnum.sleepDelay_10m;
    config.shutterVolume = 3;

    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['endpoint'], endpoint);
      expect(arguments['timeout'], isNull);

      Map<dynamic, dynamic> configMap = arguments['config'];
      expect(configMap['DateTimeZone'], config.dateTime);
      expect(configMap['Language'], config.language!.rawValue);
      expect(configMap['OffDelay'], config.offDelay!.rawValue);
      expect(configMap['SleepDelay'], config.sleepDelay!.rawValue);
      expect(configMap['ShutterVolume'], config.shutterVolume);

      return Future.value();
    });
    await platform.initialize(endpoint, config, null);
  });

  test('initialize timeout', () async {
    const endpoint = 'http://dummy/';
    final timeout = ThetaTimeout();
    timeout.connectTimeout = 10;
    timeout.requestTimeout = 20;
    timeout.socketTimeout = 30;

    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['endpoint'], endpoint);
      expect(arguments['config'], isNull);

      Map<dynamic, dynamic> configMap = arguments['timeout'];
      expect(configMap['connectTimeout'], timeout.connectTimeout);
      expect(configMap['requestTimeout'], timeout.requestTimeout);
      expect(configMap['socketTimeout'], timeout.socketTimeout);

      return Future.value();
    });
    await platform.initialize(endpoint, null, timeout);
  });

  test('deleteFiles', () async {
    const files = [
      'image1.jpg',
      'image2.jpg',
      'image3.jpg',
    ];

    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'deleteFiles');
      var arguments = methodCall.arguments as List<dynamic>;
      expect(arguments[0], files[0]);
      expect(arguments[1], files[1]);
      expect(arguments[2], files[2]);

      return Future.value();
    });
    await platform.deleteFiles(files);
  });

  test('deleteAllFiles', () async {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'deleteAllFiles');
      return Future.value();
    });
    await platform.deleteAllFiles();
  });

  test('deleteAllImageFiles', () async {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'deleteAllImageFiles');
      return Future.value();
    });
    await platform.deleteAllImageFiles();
  });

  test('deleteAllVideoFiles', () async {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'deleteAllVideoFiles');
      return Future.value();
    });
    await platform.deleteAllVideoFiles();
  });

  test('getMetadata', () async {
    const fileUrl = 'http://dummy.jpg';
    const exifVersion = '021';
    const dateTime = '2022:01:01 00:01:00+09:00';
    const imageWidth = 1024;
    const imageHeight = 1024;
    const gpsLatitude = 0.1;
    const gpsLongitude = 0.2;
    const poseHeadingDegrees = 0.3;

    const metadataMap = {
      'exif': {
        'exifVersion': exifVersion,
        'dateTime': dateTime,
        'imageWidth': imageWidth,
        'imageLength': imageHeight,
        'gpsLatitude': gpsLatitude,
        'gpsLongitude': gpsLongitude,
      },
      'xmp': {
        'fullPanoWidthPixels': imageWidth,
        'fullPanoHeightPixels': imageHeight,
        'poseHeadingDegrees': poseHeadingDegrees,
      }
    };

    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'getMetadata');
      return Future.value(metadataMap);
    });
    Metadata metadata = await platform.getMetadata(fileUrl);
    expect(metadata, isNotNull);
    expect(metadata.exif.exifVersion, exifVersion);
    expect(metadata.exif.dateTime, dateTime);
    expect(metadata.exif.imageWidth, imageWidth);
    expect(metadata.exif.imageLength, imageHeight);
    expect(metadata.exif.gpsLatitude, gpsLatitude);
    expect(metadata.exif.gpsLongitude, gpsLongitude);
    expect(metadata.xmp.poseHeadingDegrees, poseHeadingDegrees);
    expect(metadata.xmp.fullPanoWidthPixels, imageWidth);
    expect(metadata.xmp.fullPanoHeightPixels, imageHeight);
  });

  test('reset', () async {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'reset');
      return Future.value();
    });
    await platform.reset();
  });

  test('stopSelfTimer', () async {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'stopSelfTimer');
      return Future.value();
    });
    await platform.stopSelfTimer();
  });

  test('convertVideoFormats', () async {
    String fileUrl = 'http://dummy.MP4';
    String result = 'http://dummy_result.MP4';
    bool toLowResolution = true;
    bool applyTopBottomCorrection = true;
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'convertVideoFormats');

      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['fileUrl'], fileUrl);
      expect(arguments['toLowResolution'], toLowResolution);
      expect(arguments['applyTopBottomCorrection'], applyTopBottomCorrection);
      return Future.value(result);
    });
    expect(await platform.convertVideoFormats(fileUrl, toLowResolution, applyTopBottomCorrection), result);
  });

  test('cancelVideoConvert', () async {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'cancelVideoConvert');
      return Future.value();
    });
    await platform.cancelVideoConvert();
  });

  test('finishWlan', () async {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'finishWlan');
      return Future.value();
    });
    await platform.finishWlan();
  });

  test('listAccessPoints', () async {
    const data = [
      {
        'ssid': 'ssid_test1',
        'ssidStealth': true,
        'authMode': 'NONE',
        'connectionPriority': 1,
        'usingDhcp': true,
      },
      {
        'ssid': 'ssid_test2',
        'ssidStealth': false,
        'authMode': 'WEP',
        'connectionPriority': 2,
        'usingDhcp': false,
        'ipAddress': '192.168.1.2',
        'subnetMask': '255.255.255.0',
        'defaultGateway': '192.168.1.100',
      },
      {
        'ssid': 'ssid_test3',
        'ssidStealth': false,
        'authMode': 'WPA',
        'connectionPriority': 3,
        'usingDhcp': false,
        'ipAddress': '192.168.1.3',
        'subnetMask': '255.255.255.0',
        'defaultGateway': '192.168.1.101',
      },
    ];
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'listAccessPoints');
      return Future.value(data);
    });
    var resultList = await platform.listAccessPoints();
    for (int i = 0; i < resultList.length; i++) {
      expect(resultList[i].ssid, data[i]['ssid']);
      expect(resultList[i].ssidStealth, data[i]['ssidStealth']);
      expect(resultList[i].authMode, AuthModeEnum.getValue(data[i]['authMode'] as String));
      expect(resultList[i].connectionPriority, data[i]['connectionPriority']);
      expect(resultList[i].usingDhcp, data[i]['usingDhcp']);
      expect(resultList[i].ipAddress, data[i]['ipAddress']);
      expect(resultList[i].subnetMask, data[i]['subnetMask']);
      expect(resultList[i].defaultGateway, data[i]['defaultGateway']);
    }
  });

  test('setAccessPointDynamically', () async {
    const ssid = 'ssid_test';
    const ssidStealth = true;
    const authMode = AuthModeEnum.wep;
    const password = 'password1';
    const connectionPriority = 2;

    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'setAccessPointDynamically');

      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['ssid'], ssid);
      expect(arguments['ssidStealth'], ssidStealth);
      expect(arguments['authMode'], authMode.rawValue);
      expect(arguments['password'], password);
      expect(arguments['connectionPriority'], connectionPriority);
      return Future.value();
    });
    await platform.setAccessPointDynamically(ssid, ssidStealth, authMode, password, connectionPriority);
  });

  test('setAccessPointStatically', () async {
    const ssid = 'ssid_test';
    const ssidStealth = true;
    const authMode = AuthModeEnum.wep;
    const password = 'password1';
    const connectionPriority = 2;
    const ipAddress = '192.168.1.2';
    const subnetMask = '255.255.255.0';
    const defaultGateway = '192.168.1.3';

    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'setAccessPointStatically');

      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['ssid'], ssid);
      expect(arguments['ssidStealth'], ssidStealth);
      expect(arguments['authMode'], authMode.rawValue);
      expect(arguments['password'], password);
      expect(arguments['connectionPriority'], connectionPriority);
      expect(arguments['ipAddress'], ipAddress);
      expect(arguments['subnetMask'], subnetMask);
      expect(arguments['defaultGateway'], defaultGateway);
      return Future.value();
    });
    await platform.setAccessPointStatically(ssid, ssidStealth, authMode, password, connectionPriority, ipAddress, subnetMask, defaultGateway);
  });

  test('deleteAccessPoint', () async {
    const ssid = 'ssid_test';
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'deleteAccessPoint');
      expect(methodCall.arguments, ssid);
      return Future.value();
    });
    await platform.deleteAccessPoint(ssid);
  });

  test('getMySetting', () async {
    List<List<dynamic>> data = [
      [OptionNameEnum.aperture, 'Aperture', ApertureEnum.apertureAuto, 'APERTURE_AUTO'],
      [OptionNameEnum.colorTemperature, 'ColorTemperature', 5000, 5000],
      [OptionNameEnum.exposureCompensation, 'ExposureCompensation', ExposureCompensationEnum.zero, 'ZERO'],
      [OptionNameEnum.exposureDelay, 'ExposureDelay', ExposureDelayEnum.delayOff, 'DELAY_OFF'],
      [OptionNameEnum.exposureProgram, 'ExposureProgram', ExposureProgramEnum.normalProgram, 'NORMAL_PROGRAM'],
      [OptionNameEnum.fileFormat, 'FileFormat', FileFormatEnum.image_6_7K, 'IMAGE_6_7K'],
      [OptionNameEnum.filter, 'Filter', FilterEnum.off, 'OFF'],
      [OptionNameEnum.iso, 'Iso', IsoEnum.isoAuto, 'ISO_AUTO'],
      [OptionNameEnum.isoAutoHighLimit, 'IsoAutoHighLimit', IsoAutoHighLimitEnum.iso6400, 'ISO_6400'],
      [OptionNameEnum.whiteBalance, 'WhiteBalance', WhiteBalanceEnum.auto, 'AUTO'],
      [OptionNameEnum.whiteBalanceAutoStrength, 'WhiteBalanceAutoStrength', WhiteBalanceAutoStrengthEnum.off, 'OFF'],
    ];

    Map<String, dynamic> optionMap = {};
    var optionNames = List<OptionNameEnum>.empty(growable: true);

    for (int i = 0; i < data.length; i++) {
      optionMap[data[i][1]] = data[i][3];
      optionNames.add(data[i][0]);
    }

    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['captureMode'], CaptureModeEnum.image.rawValue);
      return Future.value(optionMap);
    });
    Options options = await platform.getMySetting(CaptureModeEnum.image);

    expect(options, isNotNull);
    expect(options.aperture, data[0][2]);
    expect(options.colorTemperature, data[1][2]);
    for (int i = 0; i < data.length; i++) {
      expect(options.getValue(data[i][0]), data[i][2], reason: data[i][1]);
    }
  });

  test('getMySettingFromOldModel', () async {
    List<List<dynamic>> data = [
      [OptionNameEnum.aperture, 'Aperture', ApertureEnum.apertureAuto, 'APERTURE_AUTO'],
      [OptionNameEnum.colorTemperature, 'ColorTemperature', 5000, 5000],
      [OptionNameEnum.exposureCompensation, 'ExposureCompensation', ExposureCompensationEnum.zero, 'ZERO'],
      [OptionNameEnum.exposureDelay, 'ExposureDelay', ExposureDelayEnum.delayOff, 'DELAY_OFF'],
      [OptionNameEnum.exposureProgram, 'ExposureProgram', ExposureProgramEnum.normalProgram, 'NORMAL_PROGRAM'],
      [OptionNameEnum.fileFormat, 'FileFormat', FileFormatEnum.image_6_7K, 'IMAGE_6_7K'],
      [OptionNameEnum.filter, 'Filter', FilterEnum.off, 'OFF'],
      [OptionNameEnum.iso, 'Iso', IsoEnum.isoAuto, 'ISO_AUTO'],
      [OptionNameEnum.isoAutoHighLimit, 'IsoAutoHighLimit', IsoAutoHighLimitEnum.iso6400, 'ISO_6400'],
      [OptionNameEnum.whiteBalance, 'WhiteBalance', WhiteBalanceEnum.auto, 'AUTO'],
      [OptionNameEnum.whiteBalanceAutoStrength, 'WhiteBalanceAutoStrength', WhiteBalanceAutoStrengthEnum.off, 'OFF'],
    ];

    Map<String, dynamic> optionMap = {};
    var optionNames = List<OptionNameEnum>.empty(growable: true);

    for (int i = 0; i < data.length; i++) {
      optionMap[data[i][1]] = data[i][3];
      optionNames.add(data[i][0]);
    }

    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      var arguments = methodCall.arguments['optionNames'] as List<dynamic>;
      for (int i = 0; i < data.length; i++) {
        expect(arguments[i], data[i][1], reason: data[i][1]);
      }
      return Future.value(optionMap);
    });
    Options options = await platform.getMySettingFromOldModel(optionNames);

    expect(options, isNotNull);
    expect(options.aperture, data[0][2]);
    expect(options.colorTemperature, data[1][2]);
    for (int i = 0; i < data.length; i++) {
      expect(options.getValue(data[i][0]), data[i][2], reason: data[i][1]);
    }
  });

  test('setMySetting', () async {
    List<List<dynamic>> data = [
      [OptionNameEnum.aperture, 'Aperture', ApertureEnum.apertureAuto, 'APERTURE_AUTO'],
      [OptionNameEnum.colorTemperature, 'ColorTemperature', 100, 100],
      [OptionNameEnum.exposureCompensation, 'ExposureCompensation', ExposureCompensationEnum.p0_3, 'P0_3'],
      [OptionNameEnum.exposureDelay, 'ExposureDelay', ExposureDelayEnum.delay2, 'DELAY_2'],
      [OptionNameEnum.exposureProgram, 'ExposureProgram', ExposureProgramEnum.shutterPriority, 'SHUTTER_PRIORITY'],
      [OptionNameEnum.fileFormat, 'FileFormat', FileFormatEnum.image_2K, 'IMAGE_2K'],
      [OptionNameEnum.filter, 'Filter', FilterEnum.hdr, 'HDR'],
      [OptionNameEnum.iso, 'Iso', IsoEnum.iso100, 'ISO_100'],
      [OptionNameEnum.isoAutoHighLimit, 'IsoAutoHighLimit', IsoAutoHighLimitEnum.iso1250, 'ISO_1250'],
      [OptionNameEnum.whiteBalance, 'WhiteBalance', WhiteBalanceEnum.auto, 'AUTO'],
      [OptionNameEnum.whiteBalanceAutoStrength, 'WhiteBalanceAutoStrength', WhiteBalanceAutoStrengthEnum.off, 'OFF'],
    ];

    Map<String, dynamic> optionMap = {};
    var optionNames = List<OptionNameEnum>.empty(growable: true);

    for (int i = 0; i < data.length; i++) {
      optionMap[data[i][1]] = data[i][3];
      optionNames.add(data[i][0]);
    }
    final options = Options();
    for (int i = 0; i < data.length; i++) {
      options.setValue(data[i][0], data[i][2]);
    }

    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      var arguments = methodCall.arguments as Map<dynamic, dynamic>;
      expect(arguments['captureMode'], CaptureModeEnum.image.rawValue);

      var options = methodCall.arguments['options'] as Map<dynamic, dynamic>;
      for (int i = 0; i < data.length; i++) {
        expect(options[data[i][1]], data[i][3], reason: data[i][1]);
      }
      return Future.value();
    });
    await platform.setMySetting(CaptureModeEnum.image, options);

    expect(true, isTrue);
  });

  test('deleteMySetting', () async {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'deleteMySetting');
      expect(methodCall.arguments['captureMode'], CaptureModeEnum.image.rawValue);
      return Future.value();
    });
    await platform.deleteMySetting(CaptureModeEnum.image);
  });

  test('listPlugins', () async {
    const data = [
      {
        "isBoot": false,
        "exitStatus": "success",
        "isPreInstalled": false,
        "isForeground": false,
        "message": "",
        "packageName": "android.example.com.tflitecamerademo",
        "name": "TfLite Camera Demo",
        "isRunning": false,
        "type": "user",
        "version": "1.0",
        "hasWebServer": false
      },
      {
        "isBoot": false,
        "exitStatus": "success",
        "isPreInstalled": false,
        "isForeground": false,
        "message": "",
        "packageName": "com.awesomeproject",
        "name": "AwesomeProject",
        "isRunning": false,
        "type": "user",
        "version": "1.0",
        "hasWebServer": false
      },
      {
        "isBoot": true,
        "exitStatus": "success",
        "isPreInstalled": false,
        "isForeground": false,
        "message": "",
        "packageName": "com.theta.remoteplayback",
        "name": "Remote Playback",
        "isRunning": false,
        "type": "system",
        "version": "2.10.3",
        "hasWebServer": false
      }
    ];
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'listPlugins');
      return Future.value(data);
    });
    var resultList = await platform.listPlugins();
    for (int i = 0; i < resultList.length; i++) {
      expect(resultList[i].name, data[i]['name']);
      expect(resultList[i].packageName, data[i]['packageName']);
      expect(resultList[i].version, data[i]['version']);
      expect(resultList[i].isPreInstalled, data[i]['isPreInstalled']);
      expect(resultList[i].isRunning, data[i]['isRunning']);
      expect(resultList[i].isForeground, data[i]['isForeground']);
      expect(resultList[i].isBoot, data[i]['isBoot']);
      expect(resultList[i].hasWebServer, data[i]['hasWebServer']);
      expect(resultList[i].exitStatus, data[i]['exitStatus']);
      expect(resultList[i].message, data[i]['message']);
    }
  });

  test('setPlugin', () async {
    const packageName = 'android.example.com.tflitecamerademo';

    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'setPlugin');

      var arguments = methodCall.arguments as String;
      expect(arguments, packageName);
      return Future.value();
    });
    await platform.setPlugin(packageName);
  });

  test('startPlugin', () async {
    const packageName = 'android.example.com.tflitecamerademo';

    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'startPlugin');

      var arguments = methodCall.arguments as String;
      expect(arguments, packageName);
      return Future.value();
    });
    await platform.startPlugin(packageName);
  });

  test('stopPlugin', () async {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'stopPlugin');
      return Future.value();
    });
    await platform.stopPlugin();
  });

  test('getPluginLicense', () async {
    const packageName = 'android.example.com.tflitecamerademo';
    const license = """
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
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'getPluginLicense');

      var arguments = methodCall.arguments as String;
      expect(arguments, packageName);
      return Future.value(license);
    });
    var result = await platform.getPluginLicense(packageName);
    expect(result, license);
  });

  test('getPluginOrders', () async {
    const plugins = ["com.theta360.usbstorage", "com.theta.remoteplayback", "com.theta360.undersidecover"];

    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'getPluginOrders');
      return Future.value(plugins);
    });
    var result = await platform.getPluginOrders();
    expect(result, plugins);
  });

  test('setPluginOrders', () async {
    const packageNames = ['android.example.com.tflitecamerademo'];

    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'setPluginOrders');

      var arguments = methodCall.arguments as List<dynamic>;
      for (int i = 0; i < packageNames.length; i++) {
        expect(arguments[i], packageNames[i]);
      }
      return Future.value();
    });
    await platform.setPluginOrders(packageNames);
  });

  test('setBluetoothDevice', () async {
    const name = '10107709';
    const uuid = '00000000-1111-2222-3333-555555555555';
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      expect(methodCall.method, 'setBluetoothDevice');

      var arguments = methodCall.arguments as String;
      expect(arguments, uuid);
      return Future.value(name);
    });
    var result = await platform.setBluetoothDevice(uuid);
    expect(result, name);
  });
}
