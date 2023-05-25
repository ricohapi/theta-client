import 'package:theta_client_flutter/digest_auth.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';

class ConvertUtils {
  static ThetaFiles convertThetaFiles(Map<dynamic, dynamic> data) {
    var inputList = data['fileList'] as List<dynamic>;
    var fileList = List<FileInfo>.empty(growable: true);
    for (Map<dynamic, dynamic> element in inputList.cast<Map<dynamic, dynamic>>()) {
      var info = FileInfo(
        element['name'],
        element['size'],
        element['dateTime'],
        element['fileUrl'],
        element['thumbnailUrl'],
        element['storageID']
      );
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
      data['endpoints']['httpPort'],
      data['endpoints']['httpUpdatesPort']
    );
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
      apiLevelList
    );
    return thetaInfo;
  }

  static ThetaState convertThetaState(Map<dynamic, dynamic> data) {
    var thetaInfo = ThetaState(
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
    );
    return thetaInfo;
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
      } else if (entry.value.runtimeType == int || entry.value.runtimeType == double) {
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

  static GpsInfo convertGpsInfo(Map<dynamic, dynamic> data) {
    var gpsInfo = GpsInfo(
      data['latitude'],
      data['longitude'],
      data['altitude'],
      data['dateTimeZone'],
    );
    return gpsInfo;
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

  static Proxy convertProxy(Map<dynamic, dynamic> data) {
    var proxy = Proxy(
      data['use'] ?? false,
      data['url'],
      data['port'],
      data['userid'],
      data['password'],
    );
    return proxy;
  }

  static Options convertOptions(Map<dynamic, dynamic> data) {
    var result = Options();
    for (var entry in data.entries) {
      final name = OptionNameEnum.getValue(entry.key)!;
      switch (name) {
        
        case OptionNameEnum.aperture:
          result.aperture = ApertureEnum.getValue(entry.value);
          break;
        case OptionNameEnum.cameraControlSource:
          result.cameraControlSource = CameraControlSourceEnum.getValue(entry.value);
          break;
        case OptionNameEnum.cameraMode:
          result.cameraMode = CameraModeEnum.getValue(entry.value);
          break;
        case OptionNameEnum.captureMode:
          result.captureMode = CaptureModeEnum.getValue(entry.value);
          break;
        case OptionNameEnum.colorTemperature:
          result.colorTemperature = entry.value;
          break;
        case OptionNameEnum.dateTimeZone:
          result.dateTimeZone = entry.value;
          break;
        case OptionNameEnum.exposureCompensation:
          result.exposureCompensation = ExposureCompensationEnum.getValue(entry.value);
          break;
        case OptionNameEnum.exposureDelay:
          result.exposureDelay = ExposureDelayEnum.getValue(entry.value);
          break;
        case OptionNameEnum.exposureProgram:
          result.exposureProgram = ExposureProgramEnum.getValue(entry.value);
          break;
        case OptionNameEnum.fileFormat:
          result.fileFormat = FileFormatEnum.getValue(entry.value);
          break;
        case OptionNameEnum.filter:
          result.filter = FilterEnum.getValue(entry.value);
          break;
        case OptionNameEnum.gpsInfo:
          result.gpsInfo = convertGpsInfo(entry.value);
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
        case OptionNameEnum.maxRecordableTime:
          result.maxRecordableTime = MaxRecordableTimeEnum.getValue(entry.value);
          break;
        case OptionNameEnum.networkType:
          result.networkType = NetworkTypeEnum.getValue(entry.value);
          break;
        case OptionNameEnum.offDelay:
          result.offDelay = OffDelayEnum.getValue(entry.value);
          break;
        case OptionNameEnum.password:
          result.password = entry.value;
          break;
        case OptionNameEnum.proxy:
          result.proxy = convertProxy(entry.value);
          break;
        case OptionNameEnum.sleepDelay:
          result.sleepDelay = SleepDelayEnum.getValue(entry.value);
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
        case OptionNameEnum.totalSpace:
          result.totalSpace = entry.value;
          break;
        case OptionNameEnum.shutterVolume:
          result.shutterVolume = entry.value;
          break;
        case OptionNameEnum.username:
          result.username = entry.value;
          break;
        case OptionNameEnum.whiteBalance:
          result.whiteBalance = WhiteBalanceEnum.getValue(entry.value);
          break;
        case OptionNameEnum.whiteBalanceAutoStrength:
          result.whiteBalanceAutoStrength = WhiteBalanceAutoStrengthEnum.getValue(entry.value);
          break;
        case OptionNameEnum.wlanFrequency:
          result.wlanFrequency = WlanFrequencyEnum.getValue(entry.value);
          break;
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
    if (value is ApertureEnum) {
      return value.rawValue;
    } else if (value is CameraControlSourceEnum) {
      return value.rawValue;
    } else if (value is CameraModeEnum) {
      return value.rawValue;
    } else if (value is CaptureModeEnum) {
      return value.rawValue;
    } else if (value is ExposureCompensationEnum) {
      return value.rawValue;
    } else if (value is ExposureDelayEnum) {
      return value.rawValue;
    } else if (value is ExposureProgramEnum) {
      return value.rawValue;
    } else if (value is FileFormatEnum) {
      return value.rawValue;
    } else if (value is FilterEnum) {
      return value.rawValue;
    } else if (value is IsoEnum) {
      return value.rawValue;
    } else if (value is IsoAutoHighLimitEnum) {
      return value.rawValue;
    } else if (value is LanguageEnum) {
      return value.rawValue;
    } else if (value is MaxRecordableTimeEnum) {
      return value.rawValue;
    } else if (value is NetworkTypeEnum) {
      return value.rawValue;
    } else if (value is OffDelayEnum) {
      return value.rawValue;
    } else if (value is SleepDelayEnum) {
      return value.rawValue;
    } else if (value is WhiteBalanceEnum) {
      return value.rawValue;
    } else if (value is WhiteBalanceAutoStrengthEnum) {
      return value.rawValue;
    } else if (value is WlanFrequencyEnum) {
      return value.rawValue;
    } else if (value is int || value is double || value is String || value is bool) {
      return value;
    } else if (value is GpsInfo) {
      return convertGpsInfoParam(value);
    } else if (value is Proxy) {
      return convertProxyParam(value);
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
    var exif = Exif(
      data['exifVersion'],
      data['dateTime'],
      data['imageWidth'],
      data['imageLength'],
      data['gpsLatitude'],
      data['gpsLongitude']
    );
    return exif;
  }

  static Xmp convertXmp(Map<dynamic, dynamic> data) {
    var xmp = Xmp(
      data['poseHeadingDegrees'],
      data['fullPanoWidthPixels'],
      data['fullPanoHeightPixels']
    );
    return xmp;
  }

  static Metadata convertMetadata(Map<dynamic, dynamic> data) {
    var metadata = Metadata(
      convertExif(data['exif']),
      convertXmp(data['xmp'])
    );
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

  static List<AccessPoint> toAccessPointList(List<Map<dynamic, dynamic>> data) {
    var accessPointList = List<AccessPoint>.empty(growable: true);
    for (Map<dynamic, dynamic> element in data) {
      var accessPoint = AccessPoint(
        element['ssid'],
        element['ssidStealth'],
        AuthModeEnum.getValue(element['authMode'])!,
        element['connectionPriority'],
        element['usingDhcp'],
        element['ipAddress'],
        element['subnetMask'],
        element['defaultGateway']
      );
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
