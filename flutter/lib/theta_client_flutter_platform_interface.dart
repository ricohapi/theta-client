import 'package:flutter/foundation.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';

import 'theta_client_flutter_method_channel.dart';

abstract class ThetaClientFlutterPlatform extends PlatformInterface {
  /// Constructs a ThetaClientPlatform.
  ThetaClientFlutterPlatform() : super(token: _token);

  static final Object _token = Object();

  static ThetaClientFlutterPlatform _instance =
      MethodChannelThetaClientFlutter();

  /// The default instance of [ThetaClientFlutterPlatform] to use.
  ///
  /// Defaults to [MethodChannelThetaClientFlutter].
  static ThetaClientFlutterPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [ThetaClientFlutterPlatform] when
  /// they register themselves.
  static set instance(ThetaClientFlutterPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<void> initialize(
      String endpoint, ThetaConfig? config, ThetaTimeout? timeout) {
    throw UnimplementedError('initialize() has not been implemented.');
  }

  Future<bool> isInitialized() {
    throw UnimplementedError('isInitialized() has not been implemented.');
  }

  Future<void> restoreSettings() {
    throw UnimplementedError('restoreSettings() has not been implemented.');
  }

  Future<ThetaModel?> getThetaModel() {
    throw UnimplementedError('getThetaModel() has not been implemented.');
  }

  Future<ThetaInfo> getThetaInfo() {
    throw UnimplementedError('getThetaInfo() has not been implemented.');
  }

  Future<String> getThetaLicense() {
    throw UnimplementedError('getThetaLicense() has not been implemented.');
  }

  Future<ThetaState> getThetaState() {
    throw UnimplementedError('getThetaInfo() has not been implemented.');
  }

  Future<void> getLivePreview(bool Function(Uint8List) frameHandler) {
    throw UnimplementedError('getLivePreview() has not been implemented.');
  }

  Future<ThetaFiles> listFiles(FileTypeEnum fileType, int entryCount,
      int startPosition, StorageEnum? storage) {
    throw UnimplementedError('listFiles() has not been implemented.');
  }

  Future<void> deleteFiles(List<String> fileUrls) {
    throw UnimplementedError('deleteFiles() has not been implemented.');
  }

  /// Delete all files in Theta.
  Future<void> deleteAllFiles() {
    throw UnimplementedError('deleteAllFiles() has not been implemented.');
  }

  /// Delete all image files in Theta.
  Future<void> deleteAllImageFiles() {
    throw UnimplementedError('deleteAllImageFiles() has not been implemented.');
  }

  /// Delete all video files in Theta.
  Future<void> deleteAllVideoFiles() {
    throw UnimplementedError('deleteAllVideoFiles() has not been implemented.');
  }

  Future<void> getPhotoCaptureBuilder() {
    throw UnimplementedError(
        'getPhotoCaptureBuilder() has not been implemented.');
  }

  Future<void> buildPhotoCapture(Map<String, dynamic> options, int interval) {
    throw UnimplementedError('buildPhotoCapture() has not been implemented.');
  }

  Future<String?> takePicture(
      void Function(CapturingStatusEnum status)? onCapturing) {
    throw UnimplementedError('takePicture() has not been implemented.');
  }

  Future<void> getTimeShiftCaptureBuilder() {
    throw UnimplementedError(
        'getTimeShiftCaptureBuilder() has not been implemented.');
  }

  Future<void> buildTimeShiftCapture(
      Map<String, dynamic> options, int interval) {
    throw UnimplementedError(
        'buildTimeShiftCapture() has not been implemented.');
  }

  Future<String?> startTimeShiftCapture(void Function(double)? onProgress,
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) {
    throw UnimplementedError(
        'startTimeShiftCapture() has not been implemented.');
  }

  Future<void> stopTimeShiftCapture() {
    throw UnimplementedError(
        'stopTimeShiftCapture() has not been implemented.');
  }

  Future<void> getVideoCaptureBuilder() {
    throw UnimplementedError(
        'getVideoCaptureBuilder() has not been implemented.');
  }

  Future<void> buildVideoCapture(Map<String, dynamic> options, int interval) {
    throw UnimplementedError('buildVideoCapture() has not been implemented.');
  }

  Future<String?> startVideoCapture(
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) {
    throw UnimplementedError('startVideoCapture() has not been implemented.');
  }

  Future<void> stopVideoCapture() {
    throw UnimplementedError('stopVideoCapture() has not been implemented.');
  }

  Future<void> getLimitlessIntervalCaptureBuilder() {
    throw UnimplementedError(
        'getLimitlessIntervalCaptureBuilder() has not been implemented.');
  }

  Future<void> buildLimitlessIntervalCapture(Map<String, dynamic> options,
      int interval) {
    throw UnimplementedError(
        'buildLimitlessIntervalCapture() has not been implemented.');
  }

  Future<List<String>?> startLimitlessIntervalCapture(
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) {
    throw UnimplementedError(
        'startLimitlessIntervalCapture() has not been implemented.');
  }

  Future<void> stopLimitlessIntervalCapture() {
    throw UnimplementedError(
        'stopLimitlessIntervalCapture() has not been implemented.');
  }

  Future<void> getShotCountSpecifiedIntervalCaptureBuilder(int shotCount) {
    throw UnimplementedError(
        'getShotCountSpecifiedIntervalCaptureBuilder() has not been implemented.');
  }

  Future<void> buildShotCountSpecifiedIntervalCapture(
      Map<String, dynamic> options, int interval) {
    throw UnimplementedError(
        'buildShotCountSpecifiedIntervalCapture() has not been implemented.');
  }

  Future<List<String>?> startShotCountSpecifiedIntervalCapture(
      void Function(double)? onProgress,
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) {
    throw UnimplementedError(
        'startShotCountSpecifiedIntervalCapture() has not been implemented.');
  }

  Future<void> stopShotCountSpecifiedIntervalCapture() {
    throw UnimplementedError(
        'stopShotCountSpecifiedIntervalCapture() has not been implemented.');
  }

  Future<void> getCompositeIntervalCaptureBuilder(int shootingTimeSec) {
    throw UnimplementedError(
        'getCompositeIntervalCaptureBuilder() has not been implemented.');
  }

  Future<void> buildCompositeIntervalCapture(
      Map<String, dynamic> options, int interval) {
    throw UnimplementedError(
        'buildCompositeIntervalCapture() has not been implemented.');
  }

  Future<List<String>?> startCompositeIntervalCapture(
      void Function(double)? onProgress,
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) {
    throw UnimplementedError(
        'startCompositeIntervalCapture() has not been implemented.');
  }

  Future<void> stopCompositeIntervalCapture() {
    throw UnimplementedError(
        'stopCompositeIntervalCapture() has not been implemented.');
  }

  Future<void> getBurstCaptureBuilder(
      BurstCaptureNumEnum burstCaptureNum,
      BurstBracketStepEnum burstBracketStep,
      BurstCompensationEnum burstCompensation,
      BurstMaxExposureTimeEnum burstMaxExposureTime,
      BurstEnableIsoControlEnum burstEnableIsoControl,
      BurstOrderEnum burstOrder) {
    throw UnimplementedError(
        'getBurstCaptureBuilder() has not been implemented.');
  }

  Future<void> buildBurstCapture(Map<String, dynamic> options, int interval) {
    throw UnimplementedError('buildBurstCapture() has not been implemented.');
  }

  Future<List<String>?> startBurstCapture(
      void Function(double)? onProgress,
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) {
    throw UnimplementedError('startBurstCapture() has not been implemented.');
  }

  Future<void> stopBurstCapture() {
    throw UnimplementedError('stopBurstCapture() has not been implemented.');
  }

  Future<void> getMultiBracketCaptureBuilder() {
    throw UnimplementedError(
        'getMultiBracketCaptureBuilder() has not been implemented.');
  }

  Future<void> buildMultiBracketCapture(
      Map<String, dynamic> options, int interval) {
    throw UnimplementedError(
        'buildMultiBracketCapture() has not been implemented.');
  }

  Future<List<String>?> startMultiBracketCapture(
      void Function(double)? onProgress,
      void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing) {
    throw UnimplementedError(
        'startMultiBracketCapture() has not been implemented.');
  }

  Future<void> stopMultiBracketCapture() {
    throw UnimplementedError(
        'stopMultiBracketCapture() has not been implemented.');
  }

  Future<void> getContinuousCaptureBuilder() {
    throw UnimplementedError(
        'getContinuousCaptureBuilder() has not been implemented.');
  }

  Future<void> buildContinuousCapture(
      Map<String, dynamic> options, int interval) {
    throw UnimplementedError(
        'buildContinuousCapture() has not been implemented.');
  }

  Future<List<String>?> startContinuousCapture(
      void Function(double)? onProgress,
      void Function(CapturingStatusEnum status)? onCapturing) {
    throw UnimplementedError(
        'startContinuousCapture() has not been implemented.');
  }

  Future<Options> getOptions(List<OptionNameEnum> optionNames) {
    throw UnimplementedError('getOptions() has not been implemented.');
  }

  Future<void> setOptions(Options options) {
    throw UnimplementedError('setOptions() has not been implemented.');
  }

  Future<Metadata> getMetadata(String fileUrl) {
    throw UnimplementedError('getMetadata() has not been implemented.');
  }

  Future<void> reset() {
    throw UnimplementedError('reset() has not been implemented.');
  }

  Future<void> stopSelfTimer() {
    throw UnimplementedError('stopSelfTimer() has not been implemented.');
  }

  Future<String> convertVideoFormats(
      String fileUrl, bool toLowResolution, bool applyTopBottomCorrection) {
    throw UnimplementedError('convertVideoFormats() has not been implemented.');
  }

  Future<void> cancelVideoConvert() {
    throw UnimplementedError('cancelVideoConvert() has not been implemented.');
  }

  Future<void> finishWlan() {
    throw UnimplementedError('finishWlan() has not been implemented.');
  }

  Future<List<AccessPoint>> listAccessPoints() {
    throw UnimplementedError('listAccessPoints() has not been implemented.');
  }

  Future<void> setAccessPointDynamically(
      String ssid,
      bool ssidStealth,
      AuthModeEnum authMode,
      String password,
      int connectionPriority,
      Proxy? proxy) {
    throw UnimplementedError(
        'setAccessPointDynamically() has not been implemented.');
  }

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
    throw UnimplementedError(
        'setAccessPointStatically() has not been implemented.');
  }

  Future<void> deleteAccessPoint(String ssid) {
    throw UnimplementedError('deleteAccessPoint() has not been implemented.');
  }

  Future<Options> getMySetting(CaptureModeEnum captureMode) {
    throw UnimplementedError('getMySetting() has not been implemented.');
  }

  Future<Options> getMySettingFromOldModel(List<OptionNameEnum> optionNames) {
    throw UnimplementedError('getMySetting() has not been implemented.');
  }

  Future<void> setMySetting(CaptureModeEnum captureMode, Options options) {
    throw UnimplementedError('setMySetting() has not been implemented.');
  }

  Future<void> deleteMySetting(CaptureModeEnum captureMode) {
    throw UnimplementedError('deleteMySetting() has not been implemented.');
  }

  Future<List<PluginInfo>> listPlugins() {
    throw UnimplementedError('listPlugins() has not been implemented.');
  }

  Future<void> setPlugin(String packageName) {
    throw UnimplementedError('setPlugin() has not been implemented.');
  }

  Future<void> startPlugin(String? packageName) {
    throw UnimplementedError('startPlugin() has not been implemented.');
  }

  Future<void> stopPlugin() {
    throw UnimplementedError('stopPlugin() has not been implemented.');
  }

  Future<String> getPluginLicense(String packageName) {
    throw UnimplementedError('getPluginLicense() has not been implemented.');
  }

  Future<List<String>> getPluginOrders() {
    throw UnimplementedError('getPluginOrders() has not been implemented.');
  }

  Future<void> setPluginOrders(List<String> plugins) {
    throw UnimplementedError('setPluginOrders() has not been implemented.');
  }

  Future<String> setBluetoothDevice(String uuid) {
    throw UnimplementedError('setBluetoothDevice() has not been implemented.');
  }
}
