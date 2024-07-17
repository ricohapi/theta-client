import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';
import 'package:theta_client_flutter/utils/convert_utils.dart';

import 'theta_client_flutter_platform_interface.dart';

const notifyIdLivePreview = 10001;
const notifyIdTimeShiftProgress = 10011;
const notifyIdTimeShiftStopError = 10012;
const notifyIdTimeShiftCapturing = 10013;
const notifyIdLimitlessIntervalCaptureStopError = 10004;
const notifyIdLimitlessIntervalCaptureCapturing = 10005;
const notifyIdShotCountSpecifiedIntervalCaptureProgress = 10021;
const notifyIdShotCountSpecifiedIntervalCaptureStopError = 10022;
const notifyIdShotCountSpecifiedIntervalCaptureCapturing = 10023;
const notifyIdCompositeIntervalCaptureProgress = 10031;
const notifyIdCompositeIntervalCaptureStopError = 10032;
const notifyIdCompositeIntervalCaptureCapturing = 10033;
const notifyIdMultiBracketCaptureProgress = 10041;
const notifyIdMultiBracketCaptureStopError = 10042;
const notifyIdMultiBracketCaptureCapturing = 10043;
const notifyIdBurstCaptureProgress = 10051;
const notifyIdBurstCaptureStopError = 10052;
const notifyIdBurstCaptureCapturing = 10053;
const notifyIdContinuousCaptureProgress = 10061;
const notifyIdContinuousCaptureCapturing = 10062;
const notifyIdPhotoCapturing = 10071;
const notifyIdVideoCaptureStopError = 10081;
const notifyIdVideoCaptureCapturing = 10082;

/// An implementation of [ThetaClientFlutterPlatform] that uses method channels.
class MethodChannelThetaClientFlutter extends ThetaClientFlutterPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('theta_client_flutter');
  final notifyStream = const EventChannel('theta_client_flutter/theta_notify');
  StreamSubscription? notifyStreamSubscription;
  Map<int, Function(Map<dynamic, dynamic>?)> notifyList = {};

  void addNotify(int id, Function(Map<dynamic, dynamic>?) callback) {
    notifyList[id] = callback;
  }

  void removeNotify(int id) {
    notifyList.remove(id);
  }

  void clearNotify() {
    notifyList.clear();
  }

  void onNotify(Map<dynamic, dynamic> event) {
    final id = event['id'] as int;
    final params = event['params'] as Map<dynamic, dynamic>?;
    final handler = notifyList[id];
    handler?.call(params);
  }

  void enableNotifyEventReceiver() {
    if (notifyStreamSubscription != null) {
      return;
    }
    notifyStreamSubscription =
        notifyStream.receiveBroadcastStream().listen((dynamic event) {
      onNotify(event);
    }, onError: (dynamic error) {
      debugPrint('Received error: ${error.message}');
      disableNotifyEventReceiver();
    }, cancelOnError: true);
  }

  void disableNotifyEventReceiver() {
    notifyStreamSubscription?.cancel();
    notifyStreamSubscription = null;
  }

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<void> initialize(
      String endpoint, ThetaConfig? config, ThetaTimeout? timeout) async {
    clearNotify();
    disableNotifyEventReceiver();
    enableNotifyEventReceiver();

    var completer = Completer<void>();
    try {
      final Map params = <String, dynamic>{
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
    } catch (e) {
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<bool> isInitialized() async {
    var isInit = await methodChannel.invokeMethod<bool?>('isInitialized');
    return Future.value(isInit != null && isInit);
  }

  @override
  Future<void> restoreSettings() async {
    return methodChannel.invokeMethod<void>('restoreSettings');
  }

  @override
  Future<ThetaModel?> getThetaModel() async {
    var completer = Completer<ThetaModel?>();
    final thetaModel =
        await methodChannel.invokeMethod<String?>('getThetaModel');
    completer.complete(ThetaModel.getValue(thetaModel));
    return completer.future;
  }

  @override
  Future<ThetaInfo> getThetaInfo() async {
    var completer = Completer<ThetaInfo>();
    try {
      debugPrint('call getThetaInfo');
      var result = await methodChannel.invokeMethod<Map<dynamic, dynamic>>(
          'getThetaInfo') as Map<dynamic, dynamic>;
      var thetaInfo = ConvertUtils.convertThetaInfo(result);
      completer.complete(thetaInfo);
    } catch (e) {
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<String> getThetaLicense() async {
    var completer = Completer<String>();
    try {
      var result = await methodChannel.invokeMethod<String>('getThetaLicense');
      completer.complete(result);
    } catch (e) {
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<ThetaState> getThetaState() async {
    var completer = Completer<ThetaState>();
    try {
      debugPrint('call getThetaState');
      var result = await methodChannel.invokeMethod<Map<dynamic, dynamic>>(
          'getThetaState') as Map<dynamic, dynamic>;
      var thetaState = ConvertUtils.convertThetaState(result);
      completer.complete(thetaState);
    } catch (e) {
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<void> getLivePreview(bool Function(Uint8List) frameHandler) async {
    var completer = Completer<void>();
    try {
      enableNotifyEventReceiver();
      addNotify(notifyIdLivePreview, (params) {
        final image = params?['image'] as Uint8List?;
        if (image != null && !frameHandler(image)) {
          removeNotify(notifyIdLivePreview);
          methodChannel.invokeMethod<String>('stopLivePreview');
        }
      });
      await methodChannel.invokeMethod<void>('getLivePreview');
      completer.complete(null);
    } catch (e) {
      removeNotify(notifyIdLivePreview);
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<ThetaFiles> listFiles(FileTypeEnum fileType, int entryCount,
      int startPosition, StorageEnum? storage) async {
    var completer = Completer<ThetaFiles>();
    try {
      final Map params = <String, dynamic>{
        'fileType': fileType.rawValue,
        'entryCount': entryCount,
        'startPosition': startPosition,
        'storage': storage?.rawValue,
      };
      if (storage != null) {
        params['storage'] = storage.rawValue;
      }
      var result = await methodChannel.invokeMethod<Map<dynamic, dynamic>>(
          'listFiles', params) as Map<dynamic, dynamic>;
      var thetaFiles = ConvertUtils.convertThetaFiles(result);
      completer.complete(thetaFiles);
    } catch (e) {
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
  Future<void> buildPhotoCapture(Map<String, dynamic> options, int interval) async {
    final params = ConvertUtils.convertCaptureParams(options);
    params['_capture_interval'] = interval;
    return methodChannel.invokeMethod<void>(
        'buildPhotoCapture', params);
  }

  @override
  Future<String?> takePicture(
      void Function(CapturingStatusEnum status)? onCapturing) async {
    var completer = Completer<String?>();
    try {
      enableNotifyEventReceiver();
      if (onCapturing != null) {
        addNotify(notifyIdPhotoCapturing, (params) {
          final strStatus = params?['status'] as String?;
          if (strStatus != null) {
            final status = CapturingStatusEnum.getValue(strStatus);
            if (status != null) {
              onCapturing(status);
            }
          }
        });
      }
      final fileUrl = await methodChannel.invokeMethod<String>('takePicture');
      removeNotify(notifyIdPhotoCapturing);
      completer.complete(fileUrl);
    } catch (e) {
      removeNotify(notifyIdPhotoCapturing);
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<void> getTimeShiftCaptureBuilder() async {
    return methodChannel.invokeMethod<void>('getTimeShiftCaptureBuilder');
  }

  @override
  Future<void> buildTimeShiftCapture(
      Map<String, dynamic> options, int interval) async {
    final params = ConvertUtils.convertCaptureParams(options);
    params['_capture_interval'] = interval;
    return methodChannel.invokeMethod<void>('buildTimeShiftCapture', params);
  }

  @override
  Future<String?> startTimeShiftCapture(void Function(double)? onProgress,
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) async {
    var completer = Completer<String?>();
    try {
      enableNotifyEventReceiver();
      if (onProgress != null) {
        addNotify(notifyIdTimeShiftProgress, (params) {
          final completion = params?['completion'] as double?;
          if (completion != null) {
            onProgress(completion);
          }
        });
      }
      if (onStopFailed != null) {
        addNotify(notifyIdTimeShiftStopError, (params) {
          final message = params?['message'] as String?;
          if (message != null) {
            onStopFailed(Exception(message));
          }
        });
      }
      if (onCapturing != null) {
        addNotify(notifyIdTimeShiftCapturing, (params) {
          final strStatus = params?['status'] as String?;
          if (strStatus != null) {
            final status = CapturingStatusEnum.getValue(strStatus);
            if (status != null) {
              onCapturing(status);
            }
          }
        });
      }
      final fileUrl =
          await methodChannel.invokeMethod<String>('startTimeShiftCapture');
      removeNotify(notifyIdTimeShiftProgress);
      removeNotify(notifyIdTimeShiftStopError);
      removeNotify(notifyIdTimeShiftCapturing);
      completer.complete(fileUrl);
    } catch (e) {
      removeNotify(notifyIdTimeShiftProgress);
      removeNotify(notifyIdTimeShiftStopError);
      removeNotify(notifyIdTimeShiftCapturing);
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<void> stopTimeShiftCapture() async {
    return methodChannel.invokeMethod<void>('stopTimeShiftCapture');
  }

  @override
  Future<void> getVideoCaptureBuilder() async {
    return methodChannel.invokeMethod<void>('getVideoCaptureBuilder');
  }

  @override
  Future<void> buildVideoCapture(
      Map<String, dynamic> options, int interval) async {
    final params = ConvertUtils.convertCaptureParams(options);
    params['_capture_interval'] = interval;
    return methodChannel.invokeMethod<void>('buildVideoCapture', params);
  }

  @override
  Future<String?> startVideoCapture(
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) async {
    var completer = Completer<String?>();
    try {
      enableNotifyEventReceiver();
      if (onStopFailed != null) {
        addNotify(notifyIdVideoCaptureStopError, (params) {
          final message = params?['message'] as String?;
          if (message != null) {
            onStopFailed(Exception(message));
          }
        });
      }
      if (onCapturing != null) {
        addNotify(notifyIdVideoCaptureCapturing, (params) {
          final strStatus = params?['status'] as String?;
          if (strStatus != null) {
            final status = CapturingStatusEnum.getValue(strStatus);
            if (status != null) {
              onCapturing(status);
            }
          }
        });
      }
      final fileUrl =
          await methodChannel.invokeMethod<String>('startVideoCapture');
      removeNotify(notifyIdVideoCaptureStopError);
      removeNotify(notifyIdVideoCaptureCapturing);
      completer.complete(fileUrl);
    } catch (e) {
      removeNotify(notifyIdVideoCaptureStopError);
      removeNotify(notifyIdVideoCaptureCapturing);
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<void> stopVideoCapture() async {
    return methodChannel.invokeMethod<void>('stopVideoCapture');
  }

  @override
  Future<void> getLimitlessIntervalCaptureBuilder() async {
    return methodChannel
        .invokeMethod<void>('getLimitlessIntervalCaptureBuilder');
  }

  @override
  Future<void> buildLimitlessIntervalCapture(Map<String, dynamic> options,
      int interval) async {
    final params = ConvertUtils.convertCaptureParams(options);
    params['_capture_interval'] = interval;
    return methodChannel.invokeMethod<void>(
        'buildLimitlessIntervalCapture', params);
  }

  @override
  Future<List<String>?> startLimitlessIntervalCapture(
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) async {
    var completer = Completer<List<String>?>();
    try {
      enableNotifyEventReceiver();
      if (onStopFailed != null) {
        addNotify(notifyIdLimitlessIntervalCaptureStopError, (params) {
          final message = params?['message'] as String?;
          if (message != null) {
            onStopFailed(Exception(message));
          }
        });
      }
      if (onCapturing != null) {
        addNotify(notifyIdLimitlessIntervalCaptureCapturing, (params) {
          final strStatus = params?['status'] as String?;
          if (strStatus != null) {
            final status = CapturingStatusEnum.getValue(strStatus);
            if (status != null) {
              onCapturing(status);
            }
          }
        });
      }
      final fileUrls = await methodChannel
          .invokeMethod<List<dynamic>?>('startLimitlessIntervalCapture');
      removeNotify(notifyIdLimitlessIntervalCaptureStopError);
      removeNotify(notifyIdLimitlessIntervalCaptureCapturing);
      if (fileUrls == null) {
        completer.complete(null);
      } else {
        completer.complete(ConvertUtils.convertStringList(fileUrls));
      }
    } catch (e) {
      removeNotify(notifyIdLimitlessIntervalCaptureStopError);
      removeNotify(notifyIdLimitlessIntervalCaptureCapturing);
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<void> stopLimitlessIntervalCapture() async {
    return methodChannel.invokeMethod<void>('stopLimitlessIntervalCapture');
  }

  @override
  Future<void> getShotCountSpecifiedIntervalCaptureBuilder(
      int shotCount) async {
    return methodChannel.invokeMethod<void>(
        'getShotCountSpecifiedIntervalCaptureBuilder', shotCount);
  }

  @override
  Future<void> buildShotCountSpecifiedIntervalCapture(
      Map<String, dynamic> options, int interval) async {
    final params = ConvertUtils.convertCaptureParams(options);
    params['_capture_interval'] = interval;
    return methodChannel.invokeMethod<void>(
        'buildShotCountSpecifiedIntervalCapture', params);
  }

  @override
  Future<List<String>?> startShotCountSpecifiedIntervalCapture(
      void Function(double)? onProgress,
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) async {
    var completer = Completer<List<String>?>();
    try {
      enableNotifyEventReceiver();
      if (onProgress != null) {
        addNotify(notifyIdShotCountSpecifiedIntervalCaptureProgress, (params) {
          final completion = params?['completion'] as double?;
          if (completion != null) {
            onProgress(completion);
          }
        });
      }
      if (onStopFailed != null) {
        addNotify(notifyIdShotCountSpecifiedIntervalCaptureStopError, (params) {
          final message = params?['message'] as String?;
          if (message != null) {
            onStopFailed(Exception(message));
          }
        });
      }
      if (onCapturing != null) {
        addNotify(notifyIdShotCountSpecifiedIntervalCaptureCapturing, (params) {
          final strStatus = params?['status'] as String?;
          if (strStatus != null) {
            final status = CapturingStatusEnum.getValue(strStatus);
            if (status != null) {
              onCapturing(status);
            }
          }
        });
      }
      final fileUrls = await methodChannel.invokeMethod<List<dynamic>?>(
          'startShotCountSpecifiedIntervalCapture');
      removeNotify(notifyIdShotCountSpecifiedIntervalCaptureProgress);
      removeNotify(notifyIdShotCountSpecifiedIntervalCaptureStopError);
      removeNotify(notifyIdShotCountSpecifiedIntervalCaptureCapturing);
      if (fileUrls == null) {
        completer.complete(null);
      } else {
        completer.complete(ConvertUtils.convertStringList(fileUrls));
      }
    } catch (e) {
      removeNotify(notifyIdShotCountSpecifiedIntervalCaptureProgress);
      removeNotify(notifyIdShotCountSpecifiedIntervalCaptureStopError);
      removeNotify(notifyIdShotCountSpecifiedIntervalCaptureCapturing);
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<void> stopShotCountSpecifiedIntervalCapture() async {
    return methodChannel
        .invokeMethod<void>('stopShotCountSpecifiedIntervalCapture');
  }

  @override
  Future<void> getCompositeIntervalCaptureBuilder(int shootingTimeSec) async {
    return methodChannel.invokeMethod<void>(
        'getCompositeIntervalCaptureBuilder', shootingTimeSec);
  }

  @override
  Future<void> buildCompositeIntervalCapture(
      Map<String, dynamic> options, int interval) async {
    final params = ConvertUtils.convertCaptureParams(options);
    params['_capture_interval'] = interval;
    return methodChannel.invokeMethod<void>(
        'buildCompositeIntervalCapture', params);
  }

  @override
  Future<List<String>?> startCompositeIntervalCapture(
      void Function(double)? onProgress,
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) async {
    var completer = Completer<List<String>?>();
    try {
      enableNotifyEventReceiver();
      if (onProgress != null) {
        addNotify(notifyIdCompositeIntervalCaptureProgress, (params) {
          final completion = params?['completion'] as double?;
          if (completion != null) {
            onProgress(completion);
          }
        });
      }
      if (onStopFailed != null) {
        addNotify(notifyIdCompositeIntervalCaptureStopError, (params) {
          final message = params?['message'] as String?;
          if (message != null) {
            onStopFailed(Exception(message));
          }
        });
      }
      if (onCapturing != null) {
        addNotify(notifyIdCompositeIntervalCaptureCapturing, (params) {
          final strStatus = params?['status'] as String?;
          if (strStatus != null) {
            final status = CapturingStatusEnum.getValue(strStatus);
            if (status != null) {
              onCapturing(status);
            }
          }
        });
      }
      final fileUrls = await methodChannel
          .invokeMethod<List<dynamic>?>('startCompositeIntervalCapture');
      removeNotify(notifyIdCompositeIntervalCaptureProgress);
      removeNotify(notifyIdCompositeIntervalCaptureStopError);
      removeNotify(notifyIdCompositeIntervalCaptureCapturing);
      if (fileUrls == null) {
        completer.complete(null);
      } else {
        completer.complete(ConvertUtils.convertStringList(fileUrls));
      }
    } catch (e) {
      removeNotify(notifyIdCompositeIntervalCaptureProgress);
      removeNotify(notifyIdCompositeIntervalCaptureStopError);
      removeNotify(notifyIdCompositeIntervalCaptureCapturing);
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<void> stopCompositeIntervalCapture() async {
    return methodChannel.invokeMethod<void>('stopCompositeIntervalCapture');
  }

  @override
  Future<void> getBurstCaptureBuilder(
      BurstCaptureNumEnum burstCaptureNum,
      BurstBracketStepEnum burstBracketStep,
      BurstCompensationEnum burstCompensation,
      BurstMaxExposureTimeEnum burstMaxExposureTime,
      BurstEnableIsoControlEnum burstEnableIsoControl,
      BurstOrderEnum burstOrder) async {
    final Map params = <String, dynamic>{
      'burstCaptureNum': burstCaptureNum.rawValue,
      'burstBracketStep': burstBracketStep.rawValue,
      'burstCompensation': burstCompensation.rawValue,
      'burstMaxExposureTime': burstMaxExposureTime.rawValue,
      'burstEnableIsoControl': burstEnableIsoControl.rawValue,
      'burstOrder': burstOrder.rawValue,
    };
    return methodChannel.invokeMethod<void>('getBurstCaptureBuilder', params);
  }

  @override
  Future<void> buildBurstCapture(
      Map<String, dynamic> options, int interval) async {
    final params = ConvertUtils.convertCaptureParams(options);
    params['_capture_interval'] = interval;
    return methodChannel.invokeMethod<void>('buildBurstCapture', params);
  }

  @override
  Future<List<String>?> startBurstCapture(
      void Function(double)? onProgress,
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) async {
    var completer = Completer<List<String>?>();
    try {
      enableNotifyEventReceiver();
      if (onProgress != null) {
        addNotify(notifyIdBurstCaptureProgress, (params) {
          final completion = params?['completion'] as double?;
          if (completion != null) {
            onProgress(completion);
          }
        });
      }
      if (onStopFailed != null) {
        addNotify(notifyIdBurstCaptureStopError, (params) {
          final message = params?['message'] as String?;
          if (message != null) {
            onStopFailed(Exception(message));
          }
        });
      }
      if (onCapturing != null) {
        addNotify(notifyIdBurstCaptureCapturing, (params) {
          final strStatus = params?['status'] as String?;
          if (strStatus != null) {
            final status = CapturingStatusEnum.getValue(strStatus);
            if (status != null) {
              onCapturing(status);
            }
          }
        });
      }
      final fileUrls =
          await methodChannel.invokeMethod<List<dynamic>?>('startBurstCapture');
      removeNotify(notifyIdBurstCaptureProgress);
      removeNotify(notifyIdBurstCaptureStopError);
      removeNotify(notifyIdBurstCaptureCapturing);
      if (fileUrls == null) {
        completer.complete(null);
      } else {
        completer.complete(ConvertUtils.convertStringList(fileUrls));
      }
    } catch (e) {
      removeNotify(notifyIdBurstCaptureProgress);
      removeNotify(notifyIdBurstCaptureStopError);
      removeNotify(notifyIdBurstCaptureCapturing);
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<void> stopBurstCapture() async {
    return methodChannel.invokeMethod<void>('stopBurstCapture');
  }

  @override
  Future<void> getMultiBracketCaptureBuilder() async {
    return methodChannel.invokeMethod<void>('getMultiBracketCaptureBuilder');
  }

  @override
  Future<void> buildMultiBracketCapture(
      Map<String, dynamic> options, int interval) async {
    final params = ConvertUtils.convertCaptureParams(options);
    params['_capture_interval'] = interval;
    return methodChannel.invokeMethod<void>('buildMultiBracketCapture', params);
  }

  @override
  Future<List<String>?> startMultiBracketCapture(
      void Function(double)? onProgress,
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) async {
    var completer = Completer<List<String>?>();
    try {
      enableNotifyEventReceiver();
      if (onProgress != null) {
        addNotify(notifyIdMultiBracketCaptureProgress, (params) {
          final completion = params?['completion'] as double?;
          if (completion != null) {
            onProgress(completion);
          }
        });
      }
      if (onStopFailed != null) {
        addNotify(notifyIdMultiBracketCaptureStopError, (params) {
          final message = params?['message'] as String?;
          if (message != null) {
            onStopFailed(Exception(message));
          }
        });
      }
      if (onCapturing != null) {
        addNotify(notifyIdMultiBracketCaptureCapturing, (params) {
          final strStatus = params?['status'] as String?;
          if (strStatus != null) {
            final status = CapturingStatusEnum.getValue(strStatus);
            if (status != null) {
              onCapturing(status);
            }
          }
        });
      }
      final fileUrls = await methodChannel
          .invokeMethod<List<dynamic>?>('startMultiBracketCapture');
      removeNotify(notifyIdMultiBracketCaptureProgress);
      removeNotify(notifyIdMultiBracketCaptureStopError);
      removeNotify(notifyIdMultiBracketCaptureCapturing);
      if (fileUrls == null) {
        completer.complete(null);
      } else {
        completer.complete(ConvertUtils.convertStringList(fileUrls));
      }
    } catch (e) {
      removeNotify(notifyIdMultiBracketCaptureProgress);
      removeNotify(notifyIdMultiBracketCaptureStopError);
      removeNotify(notifyIdMultiBracketCaptureCapturing);
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<void> stopMultiBracketCapture() async {
    return methodChannel.invokeMethod<void>('stopMultiBracketCapture');
  }

  @override
  Future<void> getContinuousCaptureBuilder() async {
    return methodChannel.invokeMethod<void>('getContinuousCaptureBuilder');
  }

  @override
  Future<void> buildContinuousCapture(
      Map<String, dynamic> options, int interval) async {
    final params = ConvertUtils.convertCaptureParams(options);
    params['_capture_interval'] = interval;
    return methodChannel.invokeMethod<void>('buildContinuousCapture', params);
  }

  @override
  Future<List<String>?> startContinuousCapture(
      void Function(double)? onProgress,
      void Function(CapturingStatusEnum status)? onCapturing) async {
    var completer = Completer<List<String>?>();
    try {
      enableNotifyEventReceiver();
      if (onProgress != null) {
        addNotify(notifyIdContinuousCaptureProgress, (params) {
          final completion = params?['completion'] as double?;
          if (completion != null) {
            onProgress(completion);
          }
        });
      }
      if (onCapturing != null) {
        addNotify(notifyIdContinuousCaptureCapturing, (params) {
          final strStatus = params?['status'] as String?;
          if (strStatus != null) {
            final status = CapturingStatusEnum.getValue(strStatus);
            if (status != null) {
              onCapturing(status);
            }
          }
        });
      }
      final fileUrls = await methodChannel
          .invokeMethod<List<dynamic>?>('startContinuousCapture');
      removeNotify(notifyIdContinuousCaptureProgress);
      removeNotify(notifyIdContinuousCaptureCapturing);
      if (fileUrls == null) {
        completer.complete(null);
      } else {
        completer.complete(ConvertUtils.convertStringList(fileUrls));
      }
    } catch (e) {
      removeNotify(notifyIdContinuousCaptureProgress);
      removeNotify(notifyIdContinuousCaptureCapturing);
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<Options> getOptions(List<OptionNameEnum> optionNames) async {
    var completer = Completer<Options>();
    try {
      var options = await methodChannel.invokeMethod<Map<dynamic, dynamic>>(
          'getOptions', ConvertUtils.convertGetOptionsParam(optionNames));
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
      await methodChannel.invokeMethod<void>(
          'setOptions', ConvertUtils.convertSetOptionsParam(options));
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
      var data = await methodChannel.invokeMethod<Map<dynamic, dynamic>>(
          'getMetadata', fileUrl);
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
  Future<void> stopSelfTimer() async {
    return methodChannel.invokeMethod<void>('stopSelfTimer');
  }

  @override
  Future<String> convertVideoFormats(String fileUrl, bool toLowResolution,
      bool applyTopBottomCorrection) async {
    var completer = Completer<String>();
    try {
      final Map params = <String, dynamic>{
        'fileUrl': fileUrl,
        'toLowResolution': toLowResolution,
        'applyTopBottomCorrection': applyTopBottomCorrection,
      };
      var result = await methodChannel.invokeMethod<String>(
          'convertVideoFormats', params);
      completer.complete(result);
    } catch (e) {
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
      var result = await methodChannel
          .invokeMethod<List<dynamic>>('listAccessPoints') as List<dynamic>;
      var fileInfoList =
          ConvertUtils.toAccessPointList(result.cast<Map<dynamic, dynamic>>());
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
      int connectionPriority,
      Proxy? proxy) async {
    final Map params = <String, dynamic>{
      'ssid': ssid,
      'ssidStealth': ssidStealth,
      'authMode': authMode.rawValue,
      'password': password,
      'connectionPriority': connectionPriority,
      'proxy': proxy != null ? ConvertUtils.convertProxyParam(proxy) : null
    };
    return methodChannel.invokeMethod<void>(
        'setAccessPointDynamically', params);
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
      Proxy? proxy) async {
    final Map params = <String, dynamic>{
      'ssid': ssid,
      'ssidStealth': ssidStealth,
      'authMode': authMode.rawValue,
      'password': password,
      'connectionPriority': connectionPriority,
      'ipAddress': ipAddress,
      'subnetMask': subnetMask,
      'defaultGateway': defaultGateway,
      'proxy': proxy != null ? ConvertUtils.convertProxyParam(proxy) : null
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
      var options = await methodChannel.invokeMethod<Map<dynamic, dynamic>>(
          'getMySetting', params);

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
  Future<Options> getMySettingFromOldModel(
      List<OptionNameEnum> optionNames) async {
    var completer = Completer<Options>();
    try {
      final Map params = <String, dynamic>{
        'optionNames': ConvertUtils.convertGetOptionsParam(optionNames),
      };
      var options = await methodChannel.invokeMethod<Map<dynamic, dynamic>>(
          'getMySettingFromOldModel', params);

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
  Future<void> setMySetting(
      CaptureModeEnum captureMode, Options options) async {
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

  @override
  Future<List<PluginInfo>> listPlugins() async {
    var completer = Completer<List<PluginInfo>>();
    try {
      var result = await methodChannel
          .invokeMethod<List<dynamic>>('listPlugins') as List<dynamic>;
      var plugins =
          ConvertUtils.toPluginInfoList(result.cast<Map<dynamic, dynamic>>());
      completer.complete(plugins);
    } catch (e) {
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<void> setPlugin(String packageName) async {
    return methodChannel.invokeMethod<void>('setPlugin', packageName);
  }

  @override
  Future<void> startPlugin(String? packageName) async {
    return methodChannel.invokeMethod<void>('startPlugin', packageName);
  }

  @override
  Future<void> stopPlugin() async {
    return methodChannel.invokeMethod<void>('stopPlugin');
  }

  @override
  Future<String> getPluginLicense(String packageName) async {
    var completer = Completer<String>();
    try {
      var result = await methodChannel.invokeMethod<String>(
          'getPluginLicense', packageName);
      completer.complete(result);
    } catch (e) {
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<List<String>> getPluginOrders() async {
    var completer = Completer<List<String>>();
    try {
      var result = await methodChannel
          .invokeMethod<List<dynamic>>('getPluginOrders') as List<dynamic>;
      completer.complete(ConvertUtils.convertStringList(result));
    } catch (e) {
      completer.completeError(e);
    }
    return completer.future;
  }

  @override
  Future<void> setPluginOrders(List<String> plugins) async {
    return methodChannel.invokeMethod<void>('setPluginOrders', plugins);
  }

  @override
  Future<String> setBluetoothDevice(String uuid) async {
    var completer = Completer<String>();
    try {
      var result =
          await methodChannel.invokeMethod<String>('setBluetoothDevice', uuid);
      completer.complete(result);
    } catch (e) {
      completer.completeError(e);
    }
    return completer.future;
  }
}
