import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';
import 'package:theta_client_flutter/utils/convert_utils.dart';

import 'theta_client_flutter_platform_interface.dart';

/// An implementation of [ThetaClientFlutterPlatform] that uses method channels.
class MethodChannelThetaClientFlutter extends ThetaClientFlutterPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('theta_client_flutter');
  final stream  = const EventChannel('theta_client_flutter/live_preview');
  StreamSubscription? streamSubscription;
  bool Function(Uint8List)? frameHandler;

void enableEventReceiver() {
    streamSubscription = stream.receiveBroadcastStream().listen(
        (dynamic event) {
          if (frameHandler != null) {
            if (!frameHandler!(event)) {
              disableEventReceiver();
              methodChannel.invokeMethod<String>('stopLivePreview');
            }
          } else {
            disableEventReceiver();
            methodChannel.invokeMethod<String>('stopLivePreview');
          }
        },
        onError: (dynamic error) {
          debugPrint('Received error: ${error.message}');
        },
        cancelOnError: true);
  }

  void disableEventReceiver() {
    if (streamSubscription != null) {
      streamSubscription!.cancel();
      streamSubscription = null;
    }
  }

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<void> initialize(String endpoint, ThetaConfig? config, ThetaTimeout? timeout) async {
    var completer = Completer<void>();
    try {
      final Map params = <String, dynamic> {
        'endpoint': endpoint,
      };
      if (config != null) {
        params['config'] = ConvertUtils.convertConfigParam(config);
      }
      if (timeout != null) {
        params['timeout'] = ConvertUtils.convertTimeoutParam(timeout);
      }
      await methodChannel.invokeMethod<void>('initialize', params);
      completer.complete();
    } catch(e) {
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<bool> isInitialized() async {
    var isInit =  await methodChannel.invokeMethod<bool?>('isInitialized');
    return Future.value(isInit != null && isInit);
  }

  @override
  Future<void> restoreSettings() async {
    return methodChannel.invokeMethod<void>('restoreSettings');
  }

  @override
  Future<ThetaInfo> getThetaInfo() async {
      var completer = Completer<ThetaInfo>();
      try {
        debugPrint('call getThetaInfo');
        var result = await methodChannel.invokeMethod<Map<dynamic, dynamic>>('getThetaInfo') as Map<dynamic, dynamic>;
        var thetaInfo = ConvertUtils.convertThetaInfo(result);
        completer.complete(thetaInfo);
      } catch(e) {
        completer.completeError(e);
      }
      return completer.future;
  }

  @override
  Future<ThetaState> getThetaState() async {
      var completer = Completer<ThetaState>();
      try {
        debugPrint('call getThetaState');
        var result = await methodChannel.invokeMethod<Map<dynamic, dynamic>>('getThetaState') as Map<dynamic, dynamic>;
        var thetaState = ConvertUtils.convertThetaState(result);
        completer.complete(thetaState);
      } catch(e) {
        completer.completeError(e);
      }
      return completer.future;
  }

  @override
  Future<void> getLivePreview(bool Function(Uint8List) frameHandler) async {
    var completer = Completer<void>();
    try {
      enableEventReceiver();
      this.frameHandler = frameHandler;
      await methodChannel.invokeMethod<void>('getLivePreview');
      completer.complete(null);
    } catch(e) {
      this.frameHandler = null;
      disableEventReceiver();
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<ThetaFiles> listFiles(FileTypeEnum fileType, int entryCount, int startPosition) async {
    var completer = Completer<ThetaFiles>();
    try {
      debugPrint('call listFiles');
      final Map params = <String, dynamic> {
        'fileType': fileType.rawValue,
        'entryCount': entryCount,
        'startPosition': startPosition,
      };
      var result = await methodChannel.invokeMethod<Map<dynamic, dynamic>>('listFiles', params) as Map<dynamic, dynamic>;
      var thetaFiles = ConvertUtils.convertThetaFiles(result);
      completer.complete(thetaFiles);
    } catch(e) {
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<void> deleteFiles(List<String> fileUrls) async {
    return methodChannel.invokeMethod<void>('deleteFiles', fileUrls);
  }

  @override
  Future<void> deleteAllFiles() async {
    return methodChannel.invokeMethod<void>('deleteAllFiles');
  }

  @override
  Future<void> deleteAllImageFiles() async {
    return methodChannel.invokeMethod<void>('deleteAllImageFiles');
  }

  @override
  Future<void> deleteAllVideoFiles() async {
    return methodChannel.invokeMethod<void>('deleteAllVideoFiles');
  }

  @override
  Future<void> getPhotoCaptureBuilder() async {
    return methodChannel.invokeMethod<void>('getPhotoCaptureBuilder');
  }

  @override
  Future<void> buildPhotoCapture(Map<String, dynamic> options) async {
    return methodChannel.invokeMethod<void>('buildPhotoCapture', ConvertUtils.convertCaptureParams(options));
  }

  @override
  Future<String?> takePicture() async {
    return methodChannel.invokeMethod<String>('takePicture');
  }

  @override
  Future<void> getVideoCaptureBuilder() async {
    return methodChannel.invokeMethod<void>('getVideoCaptureBuilder');
  }

  @override
  Future<void> buildVideoCapture(Map<String, dynamic> options) async {
    return methodChannel.invokeMethod<void>('buildVideoCapture', ConvertUtils.convertCaptureParams(options));
  }

  @override
  Future<String?> startVideoCapture() async {
    return methodChannel.invokeMethod<String>('startVideoCapture');
  }

  @override
  Future<void> stopVideoCapture() async {
    return methodChannel.invokeMethod<void>('stopVideoCapture');
  }

  @override
  Future<Options> getOptions(List<OptionNameEnum> optionNames) async {
    var completer = Completer<Options>();
    try {
      var options = await methodChannel.invokeMethod<Map<dynamic, dynamic>>('getOptions', ConvertUtils.convertGetOptionsParam(optionNames));
      completer.complete(ConvertUtils.convertOptions(options!));
    } catch (e) {
      completer.completeError(e); 
    }
    return completer.future;
  }

  @override
  Future<void> setOptions(Options options) async {
    var completer = Completer<void>();
    try {
      await methodChannel.invokeMethod<void>('setOptions', ConvertUtils.convertSetOptionsParam(options));
      completer.complete();
    } catch (e) {
      completer.completeError(e); 
    }
    return completer.future;
  }

  @override
  Future<Metadata> getMetadata(String fileUrl) async {
    var completer = Completer<Metadata>();
    try {
      var data = await methodChannel.invokeMethod<Map<dynamic, dynamic>>('getMetadata', fileUrl);
      completer.complete(ConvertUtils.convertMetadata(data!));
    } catch (e) {
      completer.completeError(e); 
    }
    return completer.future;
  }

  @override
  Future<void> reset() async {
    return methodChannel.invokeMethod<void>('reset');
  }

  @override
  Future<void> stopSelfTimer() async{
    return methodChannel.invokeMethod<void>('stopSelfTimer');
  }

  @override
  Future<String> convertVideoFormats(String fileUrl, bool toLowResolution, bool applyTopBottomCorrection) async {
    var completer = Completer<String>();
    try {
      final Map params = <String, dynamic> {
        'fileUrl': fileUrl,
        'toLowResolution': toLowResolution,
        'applyTopBottomCorrection': applyTopBottomCorrection,
      };
      var result = await methodChannel.invokeMethod<String>('convertVideoFormats', params);
      completer.complete(result);
    } catch(e) {
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<void> cancelVideoConvert() async {
    return methodChannel.invokeMethod<void>('cancelVideoConvert');
  }

  @override
  Future<void> finishWlan() async {
    return methodChannel.invokeMethod<void>('finishWlan');
  }

  @override
  Future<List<AccessPoint>> listAccessPoints() async {
    var completer = Completer<List<AccessPoint>>();
    try {
      var result = await methodChannel.invokeMethod<List<dynamic>>('listAccessPoints') as List<dynamic>;
      var fileInfoList = ConvertUtils.toAccessPointList(result.cast<Map<dynamic, dynamic>>());
      completer.complete(fileInfoList);

    } catch (e) {
      completer.completeError(e); 
    }
    return completer.future;
  }

  @override
  Future<void> setAccessPointDynamically(
    String ssid,
    bool ssidStealth,
    AuthModeEnum authMode,
    String password,
    int connectionPriority
  ) async {
    final Map params = <String, dynamic> {
      'ssid': ssid,
      'ssidStealth': ssidStealth,
      'authMode': authMode.rawValue,
      'password': password,
      'connectionPriority': connectionPriority,
    };
    return methodChannel.invokeMethod<void>('setAccessPointDynamically', params);
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
    String defaultGateway    
  ) async {
    final Map params = <String, dynamic> {
      'ssid': ssid,
      'ssidStealth': ssidStealth,
      'authMode': authMode.rawValue,
      'password': password,
      'connectionPriority': connectionPriority,
      'ipAddress': ipAddress,
      'subnetMask': subnetMask,
      'defaultGateway': defaultGateway,
    };
    return methodChannel.invokeMethod<void>('setAccessPointStatically', params);
  }

  @override
  Future<void> deleteAccessPoint(String ssid) async {
    return methodChannel.invokeMethod<void>('deleteAccessPoint', ssid);
  }

  @override
  Future<Options> getMySetting(CaptureModeEnum captureMode) async {
    var completer = Completer<Options>();
    try {
      final Map params = <String, dynamic>{
        'captureMode': captureMode.rawValue,
      };
      var options = await methodChannel.invokeMethod<Map<dynamic, dynamic>>('getMySetting', params);

      if (options != null) {
        completer.complete(ConvertUtils.convertOptions(options));
      } else {
        completer.completeError(Exception('Got result failed.'));
      }
    } catch (e) {
      completer.completeError(e);
    }

    return completer.future;
  }

  @override
  Future<Options> getMySettingFromOldModel(List<OptionNameEnum> optionNames) async {
    var completer = Completer<Options>();
    try {
      final Map params = <String, dynamic>{
        'optionNames': ConvertUtils.convertGetOptionsParam(optionNames),
      };
      var options = await methodChannel.invokeMethod<Map<dynamic, dynamic>>('getMySettingFromOldModel', params);

      if (options != null) {
        completer.complete(ConvertUtils.convertOptions(options));
      } else {
        completer.completeError(Exception('Got result failed.'));
      }
    } catch (e) {
      completer.completeError(e);
    }

    return completer.future;
  }

  @override
  Future<void> setMySetting(CaptureModeEnum captureMode, Options options) async {
    var completer = Completer<void>();
    final Map params = <String, dynamic>{
      'captureMode': captureMode.rawValue,
      'options': ConvertUtils.convertSetOptionsParam(options),
    };
    try {
      await methodChannel.invokeMethod<void>('setMySetting', params);
      completer.complete();
    } catch (e) {
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<void> deleteMySetting(CaptureModeEnum captureMode) async {
    var completer = Completer<void>();
    final Map params = <String, dynamic>{'captureMode': captureMode.rawValue};
    try {
      await methodChannel.invokeMethod<void>('deleteMySetting', params);
      completer.complete();
    } catch (e) {
      completer.completeError(e);
    }
    return completer.future;
  }
}
