import 'package:theta_client_flutter/digest_auth.dart';
import 'package:theta_client_flutter/options/access_info.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';

class ConvertUtils {
  static AccessInfo? convertAccessInfo(Map<dynamic, dynamic>? data) {
    if (data == null) {
      return null;
    }

    List<Map<dynamic, dynamic>>? dataList =
        (data['dhcpLeaseAddress'] as List<dynamic>?)
            ?.map((item) => item as Map<dynamic, dynamic>)
            .toList();
    List<DhcpLeaseAddress>? dhcpLeaseAddressList = dataList
        ?.map((item) => convertDhcpLeaseAddress(item))
        .where((address) => address != null)
        .cast<DhcpLeaseAddress>()
        .toList();

    var accessInfo = AccessInfo(
        data['ssid'],
        data['ipAddress'],
        data['subnetMask'],
        data['defaultGateway'],
        data['dns1'],
        data['dns2'],
        data['proxyURL'],
        WlanFrequencyAccessInfoEnum.getValue(data['frequency'] as String)!,
        data['wlanSignalStrength'],
        data['wlanSignalLevel'],
        data['lteSignalStrength'],
        data['lteSignalLevel'],
        dhcpLeaseAddressList);
    return accessInfo;
  }

  static Map<String, dynamic> convertAccessInfoParam(AccessInfo accessInfo) {
    return {
      'ssid': accessInfo.ssid,
      'ipAddress': accessInfo.ipAddress,
      'subnetMask': accessInfo.subnetMask,
      'defaultGateway': accessInfo.defaultGateway,
      'dns1': accessInfo.dns1,
      'dns2': accessInfo.dns2,
      'proxyURL': accessInfo.proxyURL,
      'frequency': accessInfo.frequency.rawValue,
      'wlanSignalStrength': accessInfo.wlanSignalStrength,
      'wlanSignalLevel': accessInfo.wlanSignalLevel,
      'lteSignalStrength': accessInfo.lteSignalStrength,
      'lteSignalLevel': accessInfo.lteSignalLevel,
      'dhcpLeaseAddress': accessInfo.dhcpLeaseAddress
          ?.map((address) => convertDhcpLeaseAddressParam(address))
          .toList()
    };
  }

  static DhcpLeaseAddress? convertDhcpLeaseAddress(
      Map<dynamic, dynamic>? data) {
    if (data == null) {
      return null;
    }

    var result = DhcpLeaseAddress(
      ipAddress: data['ipAddress'],
      macAddress: data['macAddress'],
      hostName: data['hostName'],
    );
    return result;
  }

  static Map<String, dynamic> convertDhcpLeaseAddressParam(
      DhcpLeaseAddress dhcpLeaseAddress) {
    return {
      'ipAddress': dhcpLeaseAddress.ipAddress,
      'macAddress': dhcpLeaseAddress.macAddress,
      'hostName': dhcpLeaseAddress.hostName,
    };
  }

  static List<BracketSetting>? convertAutoBracketOption(List<dynamic>? data) {
    if (data == null) {
      return null;
    }

    var autoBracket = List<BracketSetting>.empty(growable: true);

    for (var element in data.cast<Map<dynamic, dynamic>>()) {
      autoBracket.add(BracketSetting(
          aperture: (element['aperture'] != null)
              ? ApertureEnum.getValue(element['aperture'] as String)
              : null,
          colorTemperature: element['colorTemperature'],
          exposureCompensation: (element['exposureCompensation'] != null)
              ? ExposureCompensationEnum.getValue(
                  element['exposureCompensation'] as String)
              : null,
          exposureProgram: (element['exposureProgram'] != null)
              ? ExposureProgramEnum.getValue(
                  element['exposureProgram'] as String)
              : null,
          iso: (element['iso'] != null)
              ? IsoEnum.getValue(element['iso'] as String)
              : null,
          shutterSpeed: (element['shutterSpeed'] != null)
              ? ShutterSpeedEnum.getValue(element['shutterSpeed'] as String)
              : null,
          whiteBalance: (element['whiteBalance'] != null)
              ? WhiteBalanceEnum.getValue(element['whiteBalance'] as String)
              : null));
    }

    return autoBracket;
  }

  static List<Map<String, dynamic>> convertAutoBracketOptionParam(
      List<BracketSetting> bracketSetting) {
    var list = List<Map<String, dynamic>>.empty(growable: true);

    for (var bracketSetting in bracketSetting) {
      list.add({
        'aperture': bracketSetting.aperture?.rawValue,
        'colorTemperature': bracketSetting.colorTemperature,
        'exposureCompensation': bracketSetting.exposureCompensation?.rawValue,
        'exposureProgram': bracketSetting.exposureProgram?.rawValue,
        'iso': bracketSetting.iso?.rawValue,
        'shutterSpeed': bracketSetting.shutterSpeed?.rawValue,
        'whiteBalance': bracketSetting.whiteBalance?.rawValue,
      });
    }

    return list;
  }

  static BurstOption? convertBurstOption(Map<dynamic, dynamic>? data) {
    if (data == null) {
      return null;
    }

    var burstOption = BurstOption(
      BurstCaptureNumEnum.getValue(data['burstCaptureNum']),
      BurstBracketStepEnum.getValue(data['burstBracketStep']),
      BurstCompensationEnum.getValue(data['burstCompensation']),
      BurstMaxExposureTimeEnum.getValue(data['burstMaxExposureTime']),
      BurstEnableIsoControlEnum.getValue(data['burstEnableIsoControl']),
      BurstOrderEnum.getValue(data['burstOrder']),
    );
    return burstOption;
  }

  static Map<String, dynamic> convertBurstOptionParam(BurstOption burstOption) {
    return {
      'burstCaptureNum': burstOption.burstCaptureNum?.rawValue,
      'burstBracketStep': burstOption.burstBracketStep?.rawValue,
      'burstCompensation': burstOption.burstCompensation?.rawValue,
      'burstMaxExposureTime': burstOption.burstMaxExposureTime?.rawValue,
      'burstEnableIsoControl': burstOption.burstEnableIsoControl?.rawValue,
      'burstOrder': burstOption.burstOrder?.rawValue,
    };
  }

  static ThetaFiles convertThetaFiles(Map<dynamic, dynamic> data) {
    var inputList = data['fileList'] as List<dynamic>;
    var fileList = List<FileInfo>.empty(growable: true);
    for (Map<dynamic, dynamic> element
        in inputList.cast<Map<dynamic, dynamic>>()) {
      var info = FileInfo(
          element['name'],
          element['fileUrl'],
          element['size'],
          element['dateTimeZone'],
          element['dateTime'],
          element['lat'],
          element['lng'],
          element['width'],
          element['height'],
          element['thumbnailUrl'],
          element['intervalCaptureGroupId'],
          element['compositeShootingGroupId'],
          element['autoBracketGroupId'],
          element['recordTime'],
          element['isProcessed'],
          element['previewUrl'],
          (element['codec'] != null)
              ? CodecEnum.getValue(element['codec'] as String)
              : null,
          (element['projectionType'] != null)
              ? ProjectionTypeEnum.getValue(element['projectionType'] as String)
              : null,
          element['continuousShootingGroupId'],
          element['frameRate'],
          element['favorite'],
          element['imageDescription'],
          element['storageID']);
      fileList.add(info);
    }
    return ThetaFiles(fileList, data['totalEntries']);
  }

  static ThetaInfo convertThetaInfo(Map<dynamic, dynamic> data) {
    var apiList = List<String>.empty(growable: true);
    data['api'].forEach((str) {
      apiList.add(str);
    });
    Endpoints endpoints = Endpoints(
        data['endpoints']['httpPort'], data['endpoints']['httpUpdatesPort']);
    var apiLevelList = List<int>.empty(growable: true);
    data['apiLevel'].forEach((n) {
      apiLevelList.add(n);
    });
    var thetaInfo = ThetaInfo(
        data['manufacturer'],
        data['model'],
        data['serialNumber'],
        data['wlanMacAddress'],
        data['bluetoothMacAddress'],
        data['firmwareVersion'],
        data['supportUrl'],
        data['hasGps'],
        data['hasGyro'],
        data['uptime'],
        apiList,
        endpoints,
        apiLevelList,
        ThetaModel.getValue(data['thetaModel']));
    return thetaInfo;
  }

  static ThetaState convertThetaState(Map<dynamic, dynamic> data) {
    var externalGpsInfo = data['externalGpsInfo'] != null
        ? convertStateGpsInfo(data['externalGpsInfo'])
        : null;
    var internalGpsInfo = data['internalGpsInfo'] != null
        ? convertStateGpsInfo(data['internalGpsInfo'])
        : null;
    var thetaState = ThetaState(
      data['fingerprint'],
      data['batteryLevel'],
      data['storageUri'],
      data['storageID'],
      CaptureStatusEnum.getValue(data['captureStatus'] as String)!,
      data['recordedTime'],
      data['recordableTime'],
      data['capturedPictures'],
      data['compositeShootingElapsedTime'],
      data['latestFileUrl'],
      ChargingStateEnum.getValue(data['chargingState'] as String)!,
      data['apiVersion'],
      data['isPluginRunning'],
      data['isPluginWebServer'],
      ShootingFunctionEnum.getValue(data['function'] as String? ?? ''),
      data['isMySettingChanged'],
      MicrophoneOptionEnum.getValue(data['currentMicrophone'] as String? ?? ''),
      data['isSdCard'],
      ConvertUtils.toCameraErrorList(data['cameraError']),
      data['isBatteryInsert'],
      externalGpsInfo,
      internalGpsInfo,
      data['boardTemp'],
      data['batteryTemp'],
    );
    return thetaState;
  }

  static Map<String, dynamic> convertGpsInfoParam(GpsInfo gpsInfo) {
    return {
      'latitude': gpsInfo.latitude,
      'longitude': gpsInfo.longitude,
      'altitude': gpsInfo.altitude,
      'dateTimeZone': gpsInfo.dateTimeZone,
    };
  }

  static Map<String, dynamic> convertCaptureParams(Map<String, dynamic> data) {
    Map<String, dynamic> result = {};
    for (var entry in data.entries) {
      if (entry.value.runtimeType == GpsInfo) {
        result[entry.key] = convertGpsInfoParam(entry.value);
      } else if (entry.value.runtimeType == TimeShift) {
        result[entry.key] = convertTimeShiftParam(entry.value);
      } else if (entry.value.runtimeType == List<BracketSetting>) {
        result[entry.key] = convertAutoBracketParam(entry.value);
      } else if (entry.value.runtimeType == int ||
          entry.value.runtimeType == double) {
        result[entry.key] = entry.value;
      } else {
        result[entry.key] = entry.value.toString();
      }
    }
    return result;
  }

  static List<String> convertGetOptionsParam(List<OptionNameEnum> names) {
    var optionNameList = List<String>.empty(growable: true);
    for (var element in names) {
      optionNameList.add(element.rawValue);
    }
    return optionNameList;
  }

  static Map<String, dynamic> convertCameraLockConfigParam(
      CameraLockConfig config) {
    Map<String, dynamic> result = {};

    if (config.isPowerKeyLocked != null) {
      result['isPowerKeyLocked'] = config.isPowerKeyLocked;
    }
    if (config.isShutterKeyLocked != null) {
      result['isShutterKeyLocked'] = config.isShutterKeyLocked;
    }
    if (config.isModeKeyLocked != null) {
      result['isModeKeyLocked'] = config.isModeKeyLocked;
    }
    if (config.isWlanKeyLocked != null) {
      result['isWlanKeyLocked'] = config.isWlanKeyLocked;
    }
    if (config.isFnKeyLocked != null) {
      result['isFnKeyLocked'] = config.isFnKeyLocked;
    }
    if (config.isPanelLocked != null) {
      result['isPanelLocked'] = config.isPanelLocked;
    }
    return result;
  }

  static Map<String, dynamic> convertEthernetConfigParam(
      EthernetConfig ethernetConfig) {
    Map<String, dynamic> result = {};

    result['usingDhcp'] = ethernetConfig.usingDhcp;

    if (ethernetConfig.ipAddress != null) {
      result['ipAddress'] = ethernetConfig.ipAddress;
    }
    if (ethernetConfig.subnetMask != null) {
      result['subnetMask'] = ethernetConfig.subnetMask;
    }
    if (ethernetConfig.defaultGateway != null) {
      result['defaultGateway'] = ethernetConfig.defaultGateway;
    }
    if (ethernetConfig.dns1 != null) {
      result['dns1'] = ethernetConfig.dns1;
    }
    if (ethernetConfig.dns2 != null) {
      result['dns2'] = ethernetConfig.dns2;
    }

    Proxy? proxy = ethernetConfig.proxy;
    if (proxy != null) {
      result['proxy'] = convertProxyParam(proxy);
    }

    return result;
  }

  static GpsInfo convertGpsInfo(Map<dynamic, dynamic> data) {
    var gpsInfo = GpsInfo(
      data['latitude'],
      data['longitude'],
      data['altitude'],
      data['dateTimeZone'],
    );
    return gpsInfo;
  }

  static StateGpsInfo convertStateGpsInfo(Map<dynamic, dynamic> data) {
    var gpsInfo = data['gpsInfo'];
    if (gpsInfo == null) {
      return StateGpsInfo(null);
    } else {
      return StateGpsInfo(convertGpsInfo(gpsInfo));
    }
  }

  static Map<String, dynamic> convertProxyParam(Proxy proxy) {
    return {
      'use': proxy.use,
      'url': proxy.url,
      'port': proxy.port,
      'userid': proxy.userid,
      'password': proxy.password,
    };
  }

  static Map<String, dynamic> convertTimeShiftParam(TimeShift timeShift) {
    var map = <String, dynamic>{};
    if (timeShift.isFrontFirst != null) {
      map['isFrontFirst'] = timeShift.isFrontFirst;
    }
    if (timeShift.firstInterval != null) {
      map['firstInterval'] = timeShift.firstInterval.toString();
    }
    if (timeShift.secondInterval != null) {
      map['secondInterval'] = timeShift.secondInterval.toString();
    }
    return map;
  }

  static List<Map<String, dynamic>> convertAutoBracketParam(
      List<BracketSetting> settingList) {
    List<Map<String, dynamic>> list = [];
    for (var setting in settingList) {
      var map = <String, dynamic>{};
      if (setting.aperture != null) {
        map['aperture'] = setting.aperture.toString();
      }
      if (setting.colorTemperature != null) {
        map['colorTemperature'] = setting.colorTemperature;
      }
      if (setting.exposureCompensation != null) {
        map['exposureCompensation'] = setting.exposureCompensation.toString();
      }
      if (setting.exposureProgram != null) {
        map['exposureProgram'] = setting.exposureProgram.toString();
      }
      if (setting.iso != null) {
        map['iso'] = setting.iso.toString();
      }
      if (setting.shutterSpeed != null) {
        map['shutterSpeed'] = setting.shutterSpeed.toString();
      }
      if (setting.whiteBalance != null) {
        map['whiteBalance'] = setting.whiteBalance.toString();
      }
      list.add(map);
    }
    return list;
  }

  static Map<String, dynamic> convertTopBottomCorrectionRotationParam(
      TopBottomCorrectionRotation rotation) {
    return {
      'pitch': rotation.pitch,
      'roll': rotation.roll,
      'yaw': rotation.yaw,
    };
  }

  static ValueRange<T>? convertValueRangeSupport<T extends num>(
      Map<dynamic, dynamic>? data) {
    if (data == null) {
      return null;
    }
    return ValueRange<T>(
      data['max'] as T,
      data['min'] as T,
      data['stepSize'] as T,
    );
  }

  static Map<String, dynamic> convertWlanFrequencyClModeParam(
      WlanFrequencyClMode wlanFrequencyClMode) {
    return {
      'enable2_4': wlanFrequencyClMode.enable2_4,
      'enable5_2': wlanFrequencyClMode.enable5_2,
      'enable5_8': wlanFrequencyClMode.enable5_8,
    };
  }

  static CameraLockConfig? convertCameraLockConfig(
      Map<dynamic, dynamic>? data) {
    if (data == null) {
      return null;
    }

    var config = CameraLockConfig();
    if (data['isPowerKeyLocked'] != null) {
      config.isPowerKeyLocked = data['isPowerKeyLocked'];
    }
    if (data['isShutterKeyLocked'] != null) {
      config.isShutterKeyLocked = data['isShutterKeyLocked'];
    }
    if (data['isModeKeyLocked'] != null) {
      config.isModeKeyLocked = data['isModeKeyLocked'];
    }
    if (data['isWlanKeyLocked'] != null) {
      config.isWlanKeyLocked = data['isWlanKeyLocked'];
    }
    if (data['isFnKeyLocked'] != null) {
      config.isFnKeyLocked = data['isFnKeyLocked'];
    }
    if (data['isPanelLocked'] != null) {
      config.isPanelLocked = data['isPanelLocked'];
    }
    return config;
  }

  static EthernetConfig? convertEthernetConfig(Map<dynamic, dynamic>? data) {
    if (data == null) {
      return null;
    }

    var ethernetConfig = EthernetConfig(data['usingDhcp']);

    if (data['ipAddress'] != null) {
      ethernetConfig.ipAddress = data['ipAddress'];
    }
    if (data['subnetMask'] != null) {
      ethernetConfig.subnetMask = data['subnetMask'];
    }
    if (data['defaultGateway'] != null) {
      ethernetConfig.defaultGateway = data['defaultGateway'];
    }
    if (data['dns1'] != null) {
      ethernetConfig.dns1 = data['dns1'];
    }
    if (data['dns2'] != null) {
      ethernetConfig.dns2 = data['dns2'];
    }
    if (data['proxy'] != null) {
      ethernetConfig.proxy = convertProxy(data['proxy']);
    }
    return ethernetConfig;
  }

  static TimeShift convertTimeShift(Map<dynamic, dynamic> data) {
    var timeShift = TimeShift();
    if (data['isFrontFirst'] != null) {
      timeShift.isFrontFirst = data['isFrontFirst'];
    }
    if (data['firstInterval'] != null) {
      timeShift.firstInterval =
          TimeShiftIntervalEnum.getValue(data['firstInterval']);
    }
    if (data['secondInterval'] != null) {
      timeShift.secondInterval =
          TimeShiftIntervalEnum.getValue(data['secondInterval']);
    }
    return timeShift;
  }

  static Proxy? convertProxy(Map<dynamic, dynamic>? data) {
    if (data == null) {
      return null;
    }

    var proxy = Proxy(
      data['use'] ?? false,
      data['url'],
      data['port'],
      data['userid'],
      data['password'],
    );
    return proxy;
  }

  static TopBottomCorrectionRotation? convertTopBottomCorrectionRotation(
      Map<dynamic, dynamic>? data) {
    if (data == null) {
      return null;
    }

    var rotation =
        TopBottomCorrectionRotation(data['pitch'], data['roll'], data['yaw']);
    return rotation;
  }

  static TopBottomCorrectionRotationSupport?
      convertTopBottomCorrectionRotationSupport(Map<dynamic, dynamic>? data) {
    if (data == null) {
      return null;
    }

    var pitchData = data['pitch'];
    var rollData = data['roll'];
    var yawData = data['yaw'];

    var support = TopBottomCorrectionRotationSupport(
      pitch: ValueRange<double>(
        double.parse(pitchData['max']),
        double.parse(pitchData['min']),
        double.parse(pitchData['stepSize']),
      ),
      roll: ValueRange<double>(
        double.parse(rollData['max']),
        double.parse(rollData['min']),
        double.parse(rollData['stepSize']),
      ),
      yaw: ValueRange<double>(
        double.parse(yawData['max']),
        double.parse(yawData['min']),
        double.parse(yawData['stepSize']),
      ),
    );
    return support;
  }

  static Options convertOptions(Map<dynamic, dynamic> data) {
    var result = Options();
    for (var entry in data.entries) {
      final name = OptionNameEnum.getValue(entry.key)!;
      switch (name) {
        case OptionNameEnum.accessInfo:
          result.accessInfo = convertAccessInfo(entry.value);
          break;
        case OptionNameEnum.aiAutoThumbnail:
          result.aiAutoThumbnail = AiAutoThumbnailEnum.getValue(entry.value);
          break;
        case OptionNameEnum.aiAutoThumbnailSupport:
          result.aiAutoThumbnailSupport =
              convertSupportValueList(entry.value, AiAutoThumbnailEnum.values);
          break;
        case OptionNameEnum.aperture:
          result.aperture = ApertureEnum.getValue(entry.value);
          break;
        case OptionNameEnum.apertureSupport:
          result.apertureSupport =
              convertSupportValueList(entry.value, ApertureEnum.values);
          break;
        case OptionNameEnum.autoBracket:
          result.autoBracket = convertAutoBracketOption(entry.value);
          break;
        case OptionNameEnum.bitrate:
          result.bitrate = (entry.value is int)
              ? BitrateNumber(entry.value)
              : Bitrate.getValue(entry.value);
          break;
        case OptionNameEnum.bluetoothPower:
          result.bluetoothPower = BluetoothPowerEnum.getValue(entry.value);
          break;
        case OptionNameEnum.bluetoothRole:
          result.bluetoothRole = BluetoothRoleEnum.getValue(entry.value);
          break;
        case OptionNameEnum.burstMode:
          result.burstMode = BurstModeEnum.getValue(entry.value);
          break;
        case OptionNameEnum.burstOption:
          result.burstOption = convertBurstOption(entry.value);
          break;
        case OptionNameEnum.cameraControlSource:
          result.cameraControlSource =
              CameraControlSourceEnum.getValue(entry.value);
          break;
        case OptionNameEnum.cameraControlSourceSupport:
          result.cameraControlSourceSupport = convertSupportValueList(
              entry.value, CameraControlSourceEnum.values);
          break;
        case OptionNameEnum.cameraLock:
          result.cameraLock = CameraLockEnum.getValue(entry.value);
          break;
        case OptionNameEnum.cameraLockConfig:
          result.cameraLockConfig = convertCameraLockConfig(entry.value);
          break;
        case OptionNameEnum.cameraMode:
          result.cameraMode = CameraModeEnum.getValue(entry.value);
          break;
        case OptionNameEnum.cameraPower:
          result.cameraPower = CameraPowerEnum.getValue(entry.value);
          break;
        case OptionNameEnum.cameraPowerSupport:
          result.cameraPowerSupport =
              convertSupportValueList(entry.value, CameraPowerEnum.values);
          break;
        case OptionNameEnum.captureInterval:
          result.captureInterval = entry.value;
          break;
        case OptionNameEnum.captureMode:
          result.captureMode = CaptureModeEnum.getValue(entry.value);
          break;
        case OptionNameEnum.captureNumber:
          result.captureNumber = entry.value;
          break;
        case OptionNameEnum.colorTemperature:
          result.colorTemperature = entry.value;
          break;
        case OptionNameEnum.colorTemperatureSupport:
          result.colorTemperatureSupport =
              convertValueRangeSupport<int>(entry.value);
          break;
        case OptionNameEnum.compassDirectionRef:
          result.compassDirectionRef =
              CompassDirectionRefEnum.getValue(entry.value);
          break;
        case OptionNameEnum.compositeShootingOutputInterval:
          result.compositeShootingOutputInterval = entry.value;
          break;
        case OptionNameEnum.compositeShootingOutputIntervalSupport:
          result.compositeShootingOutputIntervalSupport =
              convertValueRangeSupport<int>(entry.value);
          break;
        case OptionNameEnum.compositeShootingTime:
          result.compositeShootingTime = entry.value;
          break;
        case OptionNameEnum.compositeShootingTimeSupport:
          result.compositeShootingTimeSupport =
              convertValueRangeSupport<int>(entry.value);
          break;
        case OptionNameEnum.continuousNumber:
          result.continuousNumber = ContinuousNumberEnum.getValue(entry.value);
          break;
        case OptionNameEnum.dateTimeZone:
          result.dateTimeZone = entry.value;
          break;
        case OptionNameEnum.ethernetConfig:
          result.ethernetConfig = convertEthernetConfig(entry.value);
          break;
        case OptionNameEnum.exposureCompensation:
          result.exposureCompensation =
              ExposureCompensationEnum.getValue(entry.value);
          break;
        case OptionNameEnum.exposureDelay:
          result.exposureDelay = ExposureDelayEnum.getValue(entry.value);
          break;
        case OptionNameEnum.exposureDelaySupport:
          result.exposureDelaySupport =
              convertSupportValueList(entry.value, ExposureDelayEnum.values);
          break;
        case OptionNameEnum.exposureProgram:
          result.exposureProgram = ExposureProgramEnum.getValue(entry.value);
          break;
        case OptionNameEnum.faceDetect:
          result.faceDetect = FaceDetectEnum.getValue(entry.value);
          break;
        case OptionNameEnum.fileFormat:
          result.fileFormat = FileFormatEnum.getValue(entry.value);
          break;
        case OptionNameEnum.filter:
          result.filter = FilterEnum.getValue(entry.value);
          break;
        case OptionNameEnum.function:
          result.function = ShootingFunctionEnum.getValue(entry.value);
          break;
        case OptionNameEnum.gain:
          result.gain = GainEnum.getValue(entry.value);
          break;
        case OptionNameEnum.gpsInfo:
          result.gpsInfo = convertGpsInfo(entry.value);
          break;
        case OptionNameEnum.gpsTagRecordingSupport:
          result.gpsTagRecordingSupport =
              convertSupportValueList(entry.value, GpsTagRecordingEnum.values);
          break;
        case OptionNameEnum.imageStitching:
          result.imageStitching = ImageStitchingEnum.getValue(entry.value);
          break;
        case OptionNameEnum.isGpsOn:
          result.isGpsOn = entry.value;
          break;
        case OptionNameEnum.iso:
          result.iso = IsoEnum.getValue(entry.value);
          break;
        case OptionNameEnum.isoAutoHighLimit:
          result.isoAutoHighLimit = IsoAutoHighLimitEnum.getValue(entry.value);
          break;
        case OptionNameEnum.language:
          result.language = LanguageEnum.getValue(entry.value);
          break;
        case OptionNameEnum.latestEnabledExposureDelayTime:
          result.latestEnabledExposureDelayTime =
              ExposureDelayEnum.getValue(entry.value);
          break;
        case OptionNameEnum.maxRecordableTime:
          result.maxRecordableTime =
              MaxRecordableTimeEnum.getValue(entry.value);
          break;
        case OptionNameEnum.microphoneNoiseReduction:
          result.microphoneNoiseReduction =
              MicrophoneNoiseReductionEnum.getValue(entry.value);
          break;
        case OptionNameEnum.mobileNetworkSetting:
          result.mobileNetworkSetting =
              convertMobileNetworkSetting(entry.value);
          break;
        case OptionNameEnum.networkType:
          result.networkType = NetworkTypeEnum.getValue(entry.value);
          break;
        case OptionNameEnum.offDelay:
          result.offDelay = OffDelayEnum.getValue(entry.value);
          break;
        case OptionNameEnum.offDelayUsb:
          result.offDelayUsb = OffDelayUsbEnum.getValue(entry.value);
          break;
        case OptionNameEnum.password:
          result.password = entry.value;
          break;
        case OptionNameEnum.powerSaving:
          result.powerSaving = PowerSavingEnum.getValue(entry.value);
          break;
        case OptionNameEnum.preset:
          result.preset = PresetEnum.getValue(entry.value);
          break;
        case OptionNameEnum.previewFormat:
          result.previewFormat = PreviewFormatEnum.getValue(entry.value);
          break;
        case OptionNameEnum.proxy:
          result.proxy = convertProxy(entry.value);
          break;
        case OptionNameEnum.remainingPictures:
          result.remainingPictures = entry.value;
          break;
        case OptionNameEnum.remainingVideoSeconds:
          result.remainingVideoSeconds = entry.value;
          break;
        case OptionNameEnum.remainingSpace:
          result.remainingSpace = entry.value;
          break;
        case OptionNameEnum.shootingMethod:
          result.shootingMethod = ShootingMethodEnum.getValue(entry.value);
          break;
        case OptionNameEnum.shutterSpeed:
          result.shutterSpeed = ShutterSpeedEnum.getValue(entry.value);
          break;
        case OptionNameEnum.shutterVolume:
          result.shutterVolume = entry.value;
          break;
        case OptionNameEnum.sleepDelay:
          result.sleepDelay = SleepDelayEnum.getValue(entry.value);
          break;
        case OptionNameEnum.timeShift:
          result.timeShift = convertTimeShift(entry.value);
          break;
        case OptionNameEnum.topBottomCorrection:
          result.topBottomCorrection =
              TopBottomCorrectionOptionEnum.getValue(entry.value);
          break;
        case OptionNameEnum.topBottomCorrectionRotation:
          result.topBottomCorrectionRotation =
              convertTopBottomCorrectionRotation(entry.value);
          break;
        case OptionNameEnum.topBottomCorrectionRotationSupport:
          result.topBottomCorrectionRotationSupport =
              convertTopBottomCorrectionRotationSupport(entry.value);
          break;
        case OptionNameEnum.totalSpace:
          result.totalSpace = entry.value;
          break;
        case OptionNameEnum.usbConnection:
          result.usbConnection = UsbConnectionEnum.getValue(entry.value);
          break;
        case OptionNameEnum.username:
          result.username = entry.value;
          break;
        case OptionNameEnum.videoStitching:
          result.videoStitching = VideoStitchingEnum.getValue(entry.value);
          break;
        case OptionNameEnum.visibilityReduction:
          result.visibilityReduction =
              VisibilityReductionEnum.getValue(entry.value);
          break;
        case OptionNameEnum.whiteBalance:
          result.whiteBalance = WhiteBalanceEnum.getValue(entry.value);
          break;
        case OptionNameEnum.whiteBalanceAutoStrength:
          result.whiteBalanceAutoStrength =
              WhiteBalanceAutoStrengthEnum.getValue(entry.value);
          break;
        case OptionNameEnum.wlanAntennaConfig:
          result.wlanAntennaConfig =
              WlanAntennaConfigEnum.getValue(entry.value);
          break;
        case OptionNameEnum.wlanFrequency:
          result.wlanFrequency = WlanFrequencyEnum.getValue(entry.value);
          break;
        case OptionNameEnum.wlanFrequencySupport:
          result.wlanFrequencySupport =
              convertSupportValueList(entry.value, WlanFrequencyEnum.values);
          break;
        case OptionNameEnum.wlanFrequencyClMode:
          result.wlanFrequencyClMode = convertWlanFrequencyClMode(entry.value);
          break;
      }
    }
    return result;
  }

  static List<T> convertSupportValueList<T extends Enum>(
      List<Object?> supportValueList, List<T?> enumValues) {
    List<T> result = [];
    for (var value in supportValueList) {
      var element = enumValues
          .firstWhere((element) => element.toString() == value.toString());
      if (element != null) {
        result.add(element);
      }
    }
    return result;
  }

  static Map<String, dynamic> convertSetOptionsParam(Options options) {
    Map<String, dynamic> result = {};
    for (var element in OptionNameEnum.values) {
      var value = options.getValue(element);
      if (value != null) {
        result[element.rawValue] = convertOptionValueToMapValue(value);
      }
    }
    return result;
  }

  static dynamic convertOptionValueToMapValue(dynamic value) {
    if (value is AccessInfo) {
      return convertAccessInfoParam(value);
    } else if (value is AiAutoThumbnailEnum) {
      return value.rawValue;
    } else if (value is ApertureEnum) {
      return value.rawValue;
    } else if (value is BitrateNumber) {
      return value.value;
    } else if (value is Bitrate) {
      return value.rawValue;
    } else if (value is BluetoothPowerEnum) {
      return value.rawValue;
    } else if (value is BluetoothRoleEnum) {
      return value.rawValue;
    } else if (value is List<BracketSetting>) {
      return convertAutoBracketOptionParam(value);
    } else if (value is BurstModeEnum) {
      return value.rawValue;
    } else if (value is BurstOption) {
      return convertBurstOptionParam(value);
    } else if (value is CameraControlSourceEnum) {
      return value.rawValue;
    } else if (value is CameraLockEnum) {
      return value.rawValue;
    } else if (value is CameraLockConfig) {
      return convertCameraLockConfigParam(value);
    } else if (value is CameraModeEnum) {
      return value.rawValue;
    } else if (value is CameraPowerEnum) {
      return value.rawValue;
    } else if (value is CaptureModeEnum) {
      return value.rawValue;
    } else if (value is CompassDirectionRefEnum) {
      return value.rawValue;
    } else if (value is ContinuousNumberEnum) {
      return value.rawValue;
    } else if (value is EthernetConfig) {
      return convertEthernetConfigParam(value);
    } else if (value is ExposureCompensationEnum) {
      return value.rawValue;
    } else if (value is ExposureDelayEnum) {
      return value.rawValue;
    } else if (value is ExposureProgramEnum) {
      return value.rawValue;
    } else if (value is FaceDetectEnum) {
      return value.rawValue;
    } else if (value is FileFormatEnum) {
      return value.rawValue;
    } else if (value is FilterEnum) {
      return value.rawValue;
    } else if (value is ShootingFunctionEnum) {
      return value.rawValue;
    } else if (value is GainEnum) {
      return value.rawValue;
    } else if (value is ImageStitchingEnum) {
      return value.rawValue;
    } else if (value is IsoEnum) {
      return value.rawValue;
    } else if (value is IsoAutoHighLimitEnum) {
      return value.rawValue;
    } else if (value is LanguageEnum) {
      return value.rawValue;
    } else if (value is MaxRecordableTimeEnum) {
      return value.rawValue;
    } else if (value is MicrophoneNoiseReductionEnum) {
      return value.rawValue;
    } else if (value is MobileNetworkSetting) {
      return convertMobileNetworkSettingParam(value);
    } else if (value is NetworkTypeEnum) {
      return value.rawValue;
    } else if (value is OffDelayEnum) {
      return value.rawValue;
    } else if (value is OffDelayUsbEnum) {
      return value.rawValue;
    } else if (value is PowerSavingEnum) {
      return value.rawValue;
    } else if (value is PresetEnum) {
      return value.rawValue;
    } else if (value is PreviewFormatEnum) {
      return value.rawValue;
    } else if (value is ShootingMethodEnum) {
      return value.rawValue;
    } else if (value is ShutterSpeedEnum) {
      return value.rawValue;
    } else if (value is SleepDelayEnum) {
      return value.rawValue;
    } else if (value is TopBottomCorrectionOptionEnum) {
      return value.rawValue;
    } else if (value is UsbConnectionEnum) {
      return value.rawValue;
    } else if (value is VideoStitchingEnum) {
      return value.rawValue;
    } else if (value is VisibilityReductionEnum) {
      return value.rawValue;
    } else if (value is WhiteBalanceEnum) {
      return value.rawValue;
    } else if (value is WhiteBalanceAutoStrengthEnum) {
      return value.rawValue;
    } else if (value is WlanAntennaConfigEnum) {
      return value.rawValue;
    } else if (value is WlanFrequencyEnum) {
      return value.rawValue;
    } else if (value is int ||
        value is double ||
        value is String ||
        value is bool) {
      return value;
    } else if (value is GpsInfo) {
      return convertGpsInfoParam(value);
    } else if (value is Proxy) {
      return convertProxyParam(value);
    } else if (value is TimeShift) {
      return convertTimeShiftParam(value);
    } else if (value is TopBottomCorrectionRotation) {
      return convertTopBottomCorrectionRotationParam(value);
    } else if (value is WlanFrequencyClMode) {
      return convertWlanFrequencyClModeParam(value);
    }
    return null;
  }

  static Map<String, dynamic> convertDigestAuthParam(DigestAuth digestAuth) {
    Map<String, dynamic> result = {};
    result["username"] = digestAuth.username;
    if (digestAuth.password != null) {
      result["password"] = digestAuth.password;
    }
    return result;
  }

  static Map<String, dynamic> convertConfigParam(ThetaConfig config) {
    Map<String, dynamic> result = {};
    if (config.dateTime != null) {
      result[OptionNameEnum.dateTimeZone.rawValue] = config.dateTime;
    }
    if (config.language != null) {
      result[OptionNameEnum.language.rawValue] = config.language!.rawValue;
    }
    if (config.offDelay != null) {
      result[OptionNameEnum.offDelay.rawValue] = config.offDelay!.rawValue;
    }
    if (config.sleepDelay != null) {
      result[OptionNameEnum.sleepDelay.rawValue] = config.sleepDelay!.rawValue;
    }
    if (config.shutterVolume != null) {
      result[OptionNameEnum.shutterVolume.rawValue] = config.shutterVolume;
    }
    final clientMode = config.clientMode;
    if (clientMode != null) {
      result["clientMode"] = convertDigestAuthParam(clientMode);
    }
    return result;
  }

  static Map<String, dynamic> convertTimeoutParam(ThetaTimeout timeout) {
    return {
      'connectTimeout': timeout.connectTimeout,
      'requestTimeout': timeout.requestTimeout,
      'socketTimeout': timeout.socketTimeout,
    };
  }

  static Exif convertExif(Map<dynamic, dynamic> data) {
    var exif = Exif(data['exifVersion'], data['dateTime'], data['imageWidth'],
        data['imageLength'], data['gpsLatitude'], data['gpsLongitude']);
    return exif;
  }

  static Xmp convertXmp(Map<dynamic, dynamic> data) {
    var xmp = Xmp(data['poseHeadingDegrees'], data['fullPanoWidthPixels'],
        data['fullPanoHeightPixels']);
    return xmp;
  }

  static Metadata convertMetadata(Map<dynamic, dynamic> data) {
    var metadata = Metadata(convertExif(data['exif']), convertXmp(data['xmp']));
    return metadata;
  }

  static List<String> convertStringList(List<dynamic> data) {
    var nameList = List<String>.empty(growable: true);
    for (var element in data) {
      if (element is String) {
        nameList.add(element);
      }
    }
    return nameList;
  }

  static WlanFrequencyClMode convertWlanFrequencyClMode(
      Map<dynamic, dynamic> data) {
    var value = WlanFrequencyClMode(data['enable2_4'] ?? false,
        data['enable5_2'] ?? false, data['enable5_8'] ?? false);
    return value;
  }

  static MobileNetworkSetting? convertMobileNetworkSetting(
      Map<dynamic, dynamic>? data) {
    if (data == null) {
      return null;
    }

    var result = MobileNetworkSetting(
        data['roaming'] != null
            ? RoamingEnum.getValue(data['roaming'] as String)
            : null,
        data['plan'] != null
            ? PlanEnum.getValue(data['plan'] as String)
            : null);
    return result;
  }

  static Map<String, dynamic> convertMobileNetworkSettingParam(
      MobileNetworkSetting mobileNetworkSetting) {
    return {
      'roaming': mobileNetworkSetting.roaming?.rawValue,
      'plan': mobileNetworkSetting.plan?.rawValue,
    };
  }

  static List<AccessPoint> toAccessPointList(List<Map<dynamic, dynamic>> data) {
    var accessPointList = List<AccessPoint>.empty(growable: true);
    for (Map<dynamic, dynamic> element in data) {
      var authModeValue = AuthModeEnum.getValue(element['authMode']);
      var accessPoint = AccessPoint(
          element['ssid'],
          element['ssidStealth'],
          (authModeValue != null) ? authModeValue : AuthModeEnum.none,
          element['usingDhcp'],
          element['connectionPriority'],
          element['ipAddress'],
          element['subnetMask'],
          element['defaultGateway'],
          element['dns1'],
          element['dns2'],
          convertProxy(element['proxy']));
      accessPointList.add(accessPoint);
    }
    return accessPointList;
  }

  static List<CameraErrorEnum>? toCameraErrorList(List<dynamic>? data) {
    if (data == null) {
      return null;
    }
    var cameraErrorList = List<CameraErrorEnum>.empty(growable: true);
    for (String element in data) {
      final cameraError = CameraErrorEnum.getValue(element)!;
      cameraErrorList.add(cameraError);
    }
    return cameraErrorList;
  }

  static List<PluginInfo>? toPluginInfoList(List<Map<dynamic, dynamic>>? data) {
    if (data == null) {
      return null;
    }

    var pluginInfoList = List<PluginInfo>.empty(growable: true);
    for (Map<dynamic, dynamic> element in data) {
      var pluginInfo = PluginInfo(
          element['name'],
          element['packageName'],
          element['version'],
          element['isPreInstalled'],
          element['isRunning'],
          element['isForeground'],
          element['isBoot'],
          element['hasWebServer'],
          element['exitStatus'],
          element['message']);
      pluginInfoList.add(pluginInfo);
    }
    return pluginInfoList;
  }
}

enum TagNameEnum {
  gpsTagRecording('GpsTagRecording', GpsTagRecordingEnum),
  photoFileFormat('PhotoFileFormat', PhotoFileFormatEnum),
  videoFileFormat('VideoFileFormat', VideoFileFormatEnum),
  ;

  final String rawValue;
  final dynamic valueType;

  const TagNameEnum(this.rawValue, this.valueType);

  @override
  String toString() {
    return rawValue;
  }
}
