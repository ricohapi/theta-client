import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:theta_client_flutter/digest_auth.dart';

import 'capture/capture_builder.dart';
import 'options/importer.dart';
import 'state/importer.dart';
import 'theta_client_flutter_platform_interface.dart';

export 'capture/capture.dart';
export 'capture/capture_builder.dart';
export 'capture/capturing.dart';
export 'options/importer.dart';
export 'state/importer.dart';

/// Handle Theta web APIs.
class ThetaClientFlutter {
  Future<String?> getPlatformVersion() {
    return ThetaClientFlutterPlatform.instance.getPlatformVersion();
  }

  /// Initialize object.
  ///
  /// - @param [endpoint] URL of Theta web API endpoint.
  /// - @param config Configuration of initialize. If null, get from THETA.
  /// - @param timeout Timeout of HTTP call.
  /// - @throws If an error occurs in THETA.
  Future<void> initialize(
      [String endpoint = 'http://192.168.1.1:80/',
      ThetaConfig? config,
      ThetaTimeout? timeout]) {
    return ThetaClientFlutterPlatform.instance
        .initialize(endpoint, config, timeout);
  }

  /// Returns whether it is initialized or not.
  ///
  /// - @return Whether it is initialized or not.
  /// - @throws If an error occurs in THETA.
  Future<bool> isInitialized() {
    return ThetaClientFlutterPlatform.instance.isInitialized();
  }

  /// Restore setting to THETA
  ///
  /// - @throws If an error occurs in THETA.
  Future<void> restoreSettings() {
    return ThetaClientFlutterPlatform.instance.restoreSettings();
  }

  /// Returns the connected THETA model.
  ///
  /// - @return THETA model.
  /// - @throws If an error occurs in THETA.
  Future<ThetaModel?> getThetaModel() {
    return ThetaClientFlutterPlatform.instance.getThetaModel();
  }

  /// Get basic information about Theta.
  ///
  /// - @return Static attributes of Theta.
  /// - @throws If an error occurs in THETA.
  Future<ThetaInfo> getThetaInfo() {
    return ThetaClientFlutterPlatform.instance.getThetaInfo();
  }

  /// Acquires open source license information related to the camera.
  ///
  /// - @return HTML string of the license
  /// - @throws If an error occurs in THETA.
  Future<String> getThetaLicense() {
    return ThetaClientFlutterPlatform.instance.getThetaLicense();
  }

  /// Get current state of Theta.
  ///
  /// - @return Mutable values representing Theta status.
  /// - @throws If an error occurs in THETA.
  Future<ThetaState> getThetaState() {
    return ThetaClientFlutterPlatform.instance.getThetaState();
  }

  /// Start live preview as motion JPEG.
  ///
  /// - @param [frameHandler] Called for each JPEG frame.
  /// - @throws Command is currently disabled; for example, the camera is shooting a video.
  Future<void> getLivePreview(bool Function(Uint8List) frameHandler) {
    return ThetaClientFlutterPlatform.instance.getLivePreview(frameHandler);
  }

  /// Lists information of images and videos in Theta.
  ///
  /// - @param [fileType] Type of the files to be listed.
  /// - @param [entryCount] Desired number of entries to return.
  /// If [entryCount] is more than the number of remaining files, just return entries of actual remaining files.
  /// - @param [startPosition] The position of the first file to be returned in the list. 0 represents the first file.
  /// If [startPosition] is larger than the position of the last file, an empty list is returned.
  /// - @return A list of file information and number of totalEntries.
  /// see https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.list_files.md
  /// - @throws If an error occurs in THETA.
  Future<ThetaFiles> listFiles(FileTypeEnum fileType, int entryCount,
      [int startPosition = 0, StorageEnum? storage]) {
    return ThetaClientFlutterPlatform.instance
        .listFiles(fileType, entryCount, startPosition, storage);
  }

  /// Delete files in Theta.
  ///
  /// - @param [fileUrls] URLs of the file to be deleted.
  /// - @throws Some of [fileUrls] don't exist.  All specified files cannot be deleted.
  Future<void> deleteFiles(List<String> fileUrls) {
    return ThetaClientFlutterPlatform.instance.deleteFiles(fileUrls);
  }

  /// Delete all files in Theta.
  ///
  /// - @throws If an error occurs in THETA.
  Future<void> deleteAllFiles() {
    return ThetaClientFlutterPlatform.instance.deleteAllFiles();
  }

  /// Delete all image files in Theta.
  ///
  /// - @throws If an error occurs in THETA.
  Future<void> deleteAllImageFiles() {
    return ThetaClientFlutterPlatform.instance.deleteAllImageFiles();
  }

  /// Delete all video files in Theta.
  ///
  /// - @throws If an error occurs in THETA.
  Future<void> deleteAllVideoFiles() {
    return ThetaClientFlutterPlatform.instance.deleteAllVideoFiles();
  }

  /// Get PhotoCapture.Builder for take a picture.
  PhotoCaptureBuilder getPhotoCaptureBuilder() {
    ThetaClientFlutterPlatform.instance.getPhotoCaptureBuilder();
    return PhotoCaptureBuilder();
  }

  /// Get TimeShiftCapture.Builder for capture time shift.
  TimeShiftCaptureBuilder getTimeShiftCaptureBuilder() {
    ThetaClientFlutterPlatform.instance.getTimeShiftCaptureBuilder();
    return TimeShiftCaptureBuilder();
  }

  /// Get VideoCapture.Builder for capture video.
  VideoCaptureBuilder getVideoCaptureBuilder() {
    ThetaClientFlutterPlatform.instance.getVideoCaptureBuilder();
    return VideoCaptureBuilder();
  }

  /// Get LimitlessIntervalCapture.Builder for capture limitless interval.
  LimitlessIntervalCaptureBuilder getLimitlessIntervalCaptureBuilder() {
    ThetaClientFlutterPlatform.instance.getLimitlessIntervalCaptureBuilder();
    return LimitlessIntervalCaptureBuilder();
  }

  /// Get ShotCountSpecifiedIntervalCapture.Builder for capture interval shooting with the shot count specified.
  ShotCountSpecifiedIntervalCaptureBuilder
      getShotCountSpecifiedIntervalCaptureBuilder(int shotCount) {
    ThetaClientFlutterPlatform.instance
        .getShotCountSpecifiedIntervalCaptureBuilder(shotCount);
    return ShotCountSpecifiedIntervalCaptureBuilder();
  }

  /// Get getCompositeIntervalCapture.Builder for capture interval composite shooting.
  CompositeIntervalCaptureBuilder getCompositeIntervalCaptureBuilder(
      int shootingTimeSec) {
    ThetaClientFlutterPlatform.instance
        .getCompositeIntervalCaptureBuilder(shootingTimeSec);
    return CompositeIntervalCaptureBuilder();
  }

  /// Get BurstCapture.Builder for burst shooting
  BurstCaptureBuilder getBurstCaptureBuilder(
      BurstCaptureNumEnum burstCaptureNum,
      BurstBracketStepEnum burstBracketStep,
      BurstCompensationEnum burstCompensation,
      BurstMaxExposureTimeEnum burstMaxExposureTime,
      BurstEnableIsoControlEnum burstEnableIsoControl,
      BurstOrderEnum burstOrder) {
    ThetaClientFlutterPlatform.instance.getBurstCaptureBuilder(
        burstCaptureNum,
        burstBracketStep,
        burstCompensation,
        burstMaxExposureTime,
        burstEnableIsoControl,
        burstOrder);
    return BurstCaptureBuilder();
  }

  /// Get getMultiBracketCapture.Builder for capture interval composite shooting.
  MultiBracketCaptureBuilder getMultiBracketCaptureBuilder() {
    ThetaClientFlutterPlatform.instance.getMultiBracketCaptureBuilder();
    return MultiBracketCaptureBuilder();
  }

  /// Get ContinuousCaptureBuilder.Builder for capture limitless interval.
  ContinuousCaptureBuilder getContinuousCaptureBuilder() {
    ThetaClientFlutterPlatform.instance.getContinuousCaptureBuilder();
    return ContinuousCaptureBuilder();
  }

  /// Acquires the properties and property support specifications for shooting, the camera, etc.
  ///
  /// Refer to the [options category](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/options.md)
  /// of API v2.1 reference for details on properties that can be acquired.
  ///
  /// - @param optionNames List of [OptionNameEnum].
  /// - @return [Options] acquired
  Future<Options> getOptions(List<OptionNameEnum> optionNames) {
    return ThetaClientFlutterPlatform.instance.getOptions(optionNames);
  }

  /// Property settings for shooting, the camera, etc.
  ///
  /// Check the properties that can be set and specifications by the API v2.1 reference options
  /// category or [camera.getOptions](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/options.md).
  ///
  /// - @param options Camera setting options.
  /// - @throws When an invalid option is specified.
  Future<void> setOptions(Options options) {
    return ThetaClientFlutterPlatform.instance.setOptions(options);
  }

  /// Get metadata of a still image
  ///
  /// This command cannot be executed during video recording.
  /// RICOH THETA V firmware v2.00.2 or later
  ///
  /// - @param[fileUrl] URL of a still image file
  /// - @return Exif and [photo sphere XMP](https://developers.google.com/streetview/spherical-metadata/)
  /// - @throws Command is currently disabled; for example, the camera is shooting a video.
  Future<Metadata> getMetadata(String fileUrl) {
    return ThetaClientFlutterPlatform.instance.getMetadata(fileUrl);
  }

  /// Reset all device settings and capture settings.
  /// After reset, the camera will be restarted.
  ///
  /// - @throws If an error occurs in THETA.
  Future<void> reset() {
    return ThetaClientFlutterPlatform.instance.reset();
  }

  /// Stop running self-timer.
  ///
  /// - @throws If an error occurs in THETA.
  Future<void> stopSelfTimer() {
    return ThetaClientFlutterPlatform.instance.stopSelfTimer();
  }

  /// Converts the movie format of a saved movie.
  ///
  /// Theta S and Theta SC don't support this functionality, so always [fileUrl] is returned.
  ///
  /// - @param fileUrl URL of a saved movie file.
  /// - @param toLowResolution If true generates lower resolution video, otherwise same resolution.
  /// - @param applyTopBottomCorrection apply Top/bottom correction. This parameter is ignored on Theta X.
  /// - @param onProgress the block for convertVideoFormats progress.
  /// - @return URL of a converted movie file.
  /// - @throws Command is currently disabled.
  Future<String> convertVideoFormats(String fileUrl,
      bool toLowResolution,
      [bool applyTopBottomCorrection = true,
        void Function(double)? onProgress]) {
    return ThetaClientFlutterPlatform.instance.convertVideoFormats(
        fileUrl, toLowResolution, applyTopBottomCorrection, onProgress);
  }

  /// Cancels the movie format conversion.
  ///
  /// - @throws When convertVideoFormats is not started.
  Future<void> cancelVideoConvert() {
    return ThetaClientFlutterPlatform.instance.cancelVideoConvert();
  }

  /// Turns the wireless LAN off.
  ///
  /// - @throws If an error occurs in THETA.
  Future<void> finishWlan() {
    return ThetaClientFlutterPlatform.instance.finishWlan();
  }

  /// Acquires the access point list used in client mode.
  ///
  /// For RICOH THETA X, only the access points registered with [setAccessPoint] can be acquired.
  /// (The access points automatically detected with the camera UI cannot be acquired with this API.)
  ///
  /// - @return Lists the access points stored on the camera and the access points detected by the camera.
  /// - @throws If an error occurs in THETA.
  Future<List<AccessPoint>> listAccessPoints() {
    return ThetaClientFlutterPlatform.instance.listAccessPoints();
  }

  /// Set access point. IP address is set dynamically.
  ///
  /// - @param ssid SSID of the access point.
  /// - @param ssidStealth True if SSID stealth is enabled.
  /// - @param authMode Authentication mode.
  /// - @param password Password. If [authMode] is "[none]", pass empty String.
  /// - @param connectionPriority Connection priority 1 to 5. Theta X fixes to 1 (The access point registered later has a higher priority.)
  /// - @param proxy Proxy information to be used for the access point.
  /// - @throws If an error occurs in THETA.
  Future<void> setAccessPointDynamically(String ssid,
      {bool ssidStealth = false,
      AuthModeEnum authMode = AuthModeEnum.none,
      String password = '',
      int connectionPriority = 1,
      Proxy? proxy}) {
    return ThetaClientFlutterPlatform.instance.setAccessPointDynamically(
        ssid, ssidStealth, authMode, password, connectionPriority, proxy);
  }

  /// Set access point. IP address is set statically.
  ///
  /// - @param ssid SSID of the access point.
  /// - @param ssidStealth True if SSID stealth is enabled.
  /// - @param authMode Authentication mode.
  /// - @param password Password. If [authMode] is "[none]", pass empty String.
  /// - @param connectionPriority Connection priority 1 to 5. Theta X fixes to 1 (The access point registered later has a higher priority.)
  /// - @param ipAddress IP address assigns to Theta.
  /// - @param subnetMask Subnet mask.
  /// - @param defaultGateway Default gateway.
  /// - @param proxy Proxy information to be used for the access point.
  /// - @throws If an error occurs in THETA.
  Future<void> setAccessPointStatically(String ssid,
      {bool ssidStealth = false,
      AuthModeEnum authMode = AuthModeEnum.none,
      String password = '',
      int connectionPriority = 1,
      required String ipAddress,
      required String subnetMask,
      required String defaultGateway,
      Proxy? proxy}) {
    return ThetaClientFlutterPlatform.instance.setAccessPointStatically(
        ssid,
        ssidStealth,
        authMode,
        password,
        connectionPriority,
        ipAddress,
        subnetMask,
        defaultGateway,
        proxy);
  }

  /// Deletes access point information used in client mode.
  /// Only the access points registered with [setAccessPoint] can be deleted.
  ///
  /// - @param ssid SSID of the access point.
  /// - @throws If an error occurs in THETA.
  Future<void> deleteAccessPoint(String ssid) {
    return ThetaClientFlutterPlatform.instance.deleteAccessPoint(ssid);
  }

  /// Acquires the shooting properties set by the camera._setMySetting command.
  /// Just for Theta V and later.
  ///
  /// Refer to the [options Overview](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/options.md)
  /// of API v2.1 reference  for properties available for acquisition.
  ///
  /// - @param captureMode The target shooting mode.
  /// - @return Options of my setting
  /// - @exception ThetaWebApiException When an invalid option is specified.
  /// - @exception NotConnectedException
  Future<Options> getMySetting(CaptureModeEnum captureMode) {
    return ThetaClientFlutterPlatform.instance.getMySetting(captureMode);
  }

  /// Acquires the shooting properties set by the camera._setMySetting command.
  /// Just for Theta S and SC.
  ///
  /// Refer to the [options Overview](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/options.md)
  /// of API v2.1 reference  for properties available for acquisition.
  ///
  /// - @param optionNames List of option names to acquire.
  /// - @return Options of my setting
  /// - @exception ThetaWebApiException When an invalid option is specified.
  /// - @exception NotConnectedException
  Future<Options> getMySettingFromOldModel(List<OptionNameEnum> optionNames) {
    return ThetaClientFlutterPlatform.instance
        .getMySettingFromOldModel(optionNames);
  }

  /// Registers shooting conditions in My Settings.
  ///
  /// - @param captureMode The target shooting mode.  RICOH THETA S and SC do not support My Settings in video capture mode.
  /// - @param options registered to My Settings.
  /// - @exception ThetaWebApiException When an invalid option is specified.
  /// - @exception NotConnectedException
  Future<void> setMySetting(CaptureModeEnum captureMode, Options options) {
    return ThetaClientFlutterPlatform.instance
        .setMySetting(captureMode, options);
  }

  /// Delete shooting conditions in My Settings. Supported just by Theta X and Z1.
  ///
  /// - @param captureMode The target shooting mode.
  /// - @exception ThetaWebApiException When an invalid option is specified.
  /// - @exception NotConnectedException
  Future<void> deleteMySetting(CaptureModeEnum captureMode) {
    return ThetaClientFlutterPlatform.instance.deleteMySetting(captureMode);
  }

  /// Acquires a list of installed plugins. Supported just by Theta X, Z1 and V.
  /// - @return a list of installed plugin information
  /// - @exception ThetaWebApiException When an invalid option is specified.
  /// - @exception NotConnectedException
  Future<List<PluginInfo>> listPlugins() {
    return ThetaClientFlutterPlatform.instance.listPlugins();
  }

  /// Sets the installed plugin for boot. Supported just by Theta V.
  ///
  /// - @param packageName package name of the plugin
  /// - @exception ThetaWebApiException When an invalid option is specified.
  /// - @exception NotConnectedException
  Future<void> setPlugin(String packageName) {
    return ThetaClientFlutterPlatform.instance.setPlugin(packageName);
  }

  /// Start the plugin specified by the [packageName].
  /// If [packageName] is not specified, plugin 1 will start.
  /// Supported just by Theta X, Z1 and V.
  ///
  /// - @param packageName package name of the plugin.  Theta V does not support this parameter.
  /// - @exception ThetaWebApiException When an invalid option is specified.
  /// - @exception NotConnectedException
  Future<void> startPlugin([String? packageName]) {
    return ThetaClientFlutterPlatform.instance.startPlugin(packageName);
  }

  /// Stop the running plugin.
  /// Supported just by Theta X, Z1 and V.
  /// - @exception ThetaWebApiException When an invalid option is specified.
  /// - @exception NotConnectedException
  Future<void> stopPlugin() {
    return ThetaClientFlutterPlatform.instance.stopPlugin();
  }

  /// Acquires the license for the installed plugin
  ///
  /// - @param packageName package name of the target plugin
  /// - @return HTML string of the license
  /// - @exception ThetaWebApiException When an invalid option is specified.
  /// - @exception NotConnectedException
  Future<String> getPluginLicense(String packageName) {
    return ThetaClientFlutterPlatform.instance.getPluginLicense(packageName);
  }

  /// Return the plugin orders.  Supported just by Theta X and Z1.
  ///
  /// - @return list of package names of plugins
  /// For Z1, list of three package names for the start-up plugin. No restrictions for the number of package names for X.
  /// - @exception ThetaWebApiException When an invalid option is specified.
  /// - @exception NotConnectedException
  Future<List<String>> getPluginOrders() {
    return ThetaClientFlutterPlatform.instance.getPluginOrders();
  }

  /// Sets the plugin orders.  Supported just by Theta X and Z1.
  ///
  /// - @param plugins list of package names of plugins
  /// For Z1, list size must be three. No restrictions for the size for X.
  /// When not specifying, set an empty string.
  /// If an empty string is placed mid-way, it will be moved to the front.
  /// Specifying zero package name will result in an error.
  /// - @exception ThetaWebApiException When an invalid option is specified.
  /// - @exception NotConnectedException
  Future<void> setPluginOrders(List<String> plugins) {
    return ThetaClientFlutterPlatform.instance.setPluginOrders(plugins);
  }

  /// Registers identification information (UUID) of a BLE device (Smartphone application) connected to the camera.
  /// UUID can be set while the wireless LAN function of the camera is placed in the direct mode.
  ///
  /// - @param uuid Format: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
  /// Alphabetic letters are not case-sensitive.
  /// - @return Device name generated from the serial number (S/N) of the camera.
  /// Eg. "00101234" or "THETAXS00101234" when the serial number (S/N) is "XS00101234"
  /// - @exception ThetaWebApiException When an invalid option is specified.
  /// - @exception NotConnectedException
  Future<String> setBluetoothDevice(String uuid) {
    return ThetaClientFlutterPlatform.instance.setBluetoothDevice(uuid);
  }
}

/// Support THETA model.
enum ThetaModel {
  /// THETA S
  thetaS('THETA_S'),

  /// THETA SC
  thetaSC('THETA_SC'),

  /// THETA V
  thetaV('THETA_V'),

  /// THETA Z1
  thetaZ1('THETA_Z1'),

  /// THETA X
  thetaX('THETA_X'),

  /// THETA SC2
  thetaSC2('THETA_SC2'),

  /// THETA SC2 for business
  thetaSC2B('THETA_SC2_B');

  final String rawValue;

  const ThetaModel(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static ThetaModel? getValue(String? rawValue) {
    return ThetaModel.values.cast<ThetaModel?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Static attributes of Theta.
class ThetaInfo {
  /// Manufacturer name
  final String manufacturer;

  /// Theta model name.
  final String model;

  /// Theta serial number.
  final String serialNumber;

  /// MAC address of wireless LAN (RICOH THETA V firmware v2.11.1 or later)
  final String? wlanMacAddress;

  /// MAC address of Bluetooth (RICOH THETA V firmware v2.11.1 or later)
  final String? bluetoothMacAddress;

  /// Theta firmware version.
  final String firmwareVersion;

  /// URL of the support page
  final String supportUrl;

  /// True if Theta has GPS.
  final bool hasGps;

  /// True if Theta has Gyroscope.
  final bool hasGyro;

  /// Number of seconds since Theta boot.
  final int uptime;

  /// List of supported APIs
  final List<String> api;

  /// Endpoint information
  final Endpoints endpoints;

  /// List of supported APIs (1: v2.0, 2: v2.1)
  final List<int> apiLevel;

  /// THETA model
  final ThetaModel? thetaModel;

  ThetaInfo(
      this.manufacturer,
      this.model,
      this.serialNumber,
      this.wlanMacAddress,
      this.bluetoothMacAddress,
      this.firmwareVersion,
      this.supportUrl,
      this.hasGps,
      this.hasGyro,
      this.uptime,
      this.api,
      this.endpoints,
      this.apiLevel,
      this.thetaModel);
}

/// Endpoint information
class Endpoints {
  /// Port number for using APIs
  final int httpPort;

  /// Port number for status update check (CheckForUpdates)
  final int httpUpdatesPort;

  Endpoints(this.httpPort, this.httpUpdatesPort);
}

/// File type in Theta.
enum FileTypeEnum {
  /// All files.
  all('ALL'),

  /// Still image files.
  image('IMAGE'),

  /// Video files.
  video('VIDEO');

  final String rawValue;

  const FileTypeEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }
}

/// Specifies the storage
enum StorageEnum {
  /// internal storage
  internal('INTERNAL'),

  /// external storage (SD card)
  sd('SD'),

  /// current storage
  current('CURRENT');

  final String rawValue;

  const StorageEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }
}

/// Video codec
enum CodecEnum {
  /// codec H.264/MPEG-4 AVC
  h264mp4avc('H264MP4AVC');

  final String rawValue;

  const CodecEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static CodecEnum? getValue(String rawValue) {
    return CodecEnum.values.cast<CodecEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// THETA projection type
enum ProjectionTypeEnum {
  /// Equirectangular type
  equirectangular('EQUIRECTANGULAR'),

  /// Dual Fisheye type
  dualFisheye('DUAL_FISHEYE'),

  /// Fisheye type
  fisheye('FISHEYE');

  final String rawValue;

  const ProjectionTypeEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static ProjectionTypeEnum? getValue(String rawValue) {
    return ProjectionTypeEnum.values.cast<ProjectionTypeEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// File information in Theta.
class FileInfo {
  /// File name.
  final String name;

  /// You can get a file using HTTP GET to [fileUrl].
  final String fileUrl;

  /// File size in bytes.
  final int size;

  /// File creation or update time with the time zone in the format "YYYY:MM:DD hh:mm:ss+(-)hh:mm".
  final String dateTimeZone;

  /// File creation time in the format "YYYY:MM:DD HH:MM:SS".
  final String dateTime;

  /// Latitude.
  final double? lat;

  /// Longitude.
  final double? lng;

  /// Horizontal size of image (pixels).
  final int? width;

  /// Vertical size of image (pixels).
  final int? height;

  /// You can get a thumbnail image using HTTP GET to [thumbnailUrl].
  final String thumbnailUrl;

  /// Group ID of a still image shot by interval shooting.
  final String? intervalCaptureGroupId;

  /// Group ID of a still image shot by interval composite shooting.
  final String? compositeShootingGroupId;

  /// Group ID of a still image shot by multi bracket shooting.
  final String? autoBracketGroupId;

  /// Video shooting time (sec).
  final int? recordTime;

  /// Whether or not image processing has been completed.
  final bool? isProcessed;

  /// URL of the file being processed.
  final String? previewUrl;

  /// Codec. (RICOH THETA V or later)
  final CodecEnum? codec;

  /// Projection type of movie file. (RICOH THETA V or later)
  final ProjectionTypeEnum? projectionType;

  /// Group ID of continuous shooting.  (RICOH THETA X or later)
  final String? continuousShootingGroupId;

  /// Frame rate.  (RICOH THETA Z1 Version 3.01.1 or later, RICOH THETA X or later)
  final int? frameRate;

  /// Favorite.  (RICOH THETA X or later)
  final bool? favorite;

  /// Image description.  (RICOH THETA X or later)
  final String? imageDescription;

  /// Storage ID. (RICOH THETA X Version 2.00.0 or later)
  final String? storageID;

  FileInfo(
      this.name,
      this.fileUrl,
      this.size,
      this.dateTimeZone,
      this.dateTime,
      this.lat,
      this.lng,
      this.width,
      this.height,
      this.thumbnailUrl,
      this.intervalCaptureGroupId,
      this.compositeShootingGroupId,
      this.autoBracketGroupId,
      this.recordTime,
      this.isProcessed,
      this.previewUrl,
      this.codec,
      this.projectionType,
      this.continuousShootingGroupId,
      this.frameRate,
      this.favorite,
      this.imageDescription,
      this.storageID);
}

/// Data about files in Theta.
class ThetaFiles {
  /// A list of file information.
  final List<FileInfo> fileList;

  /// Number of totalEntries.
  final int totalEntries;

  ThetaFiles(this.fileList, this.totalEntries);
}

/// bluetooth power.
enum BluetoothPowerEnum {
  /// bluetooth ON
  on('ON'),

  /// bluetooth OFF
  off('OFF');

  final String rawValue;

  const BluetoothPowerEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static BluetoothPowerEnum? getValue(String rawValue) {
    return BluetoothPowerEnum.values.cast<BluetoothPowerEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// BurstMode setting.
/// When this is set to ON, burst shooting is enabled,
/// and a screen dedicated to burst shooting is displayed in Live View.
///
/// only For RICOH THETA Z1 firmware v2.10.1 or later
enum BurstModeEnum {
  /// BurstMode ON
  on('ON'),

  /// BurstMode OFF
  off('OFF');

  final String rawValue;

  const BurstModeEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static BurstModeEnum? getValue(String rawValue) {
    return BurstModeEnum.values.cast<BurstModeEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Burst shooting setting.
///
/// only For RICOH THETA Z1 firmware v2.10.1 or later
class BurstOption {
  /// see [BurstCaptureNumEnum]
  BurstCaptureNumEnum? burstCaptureNum;

  /// see [BurstBracketStepEnum]
  BurstBracketStepEnum? burstBracketStep;

  /// see [BurstCompensationEnum]
  BurstCompensationEnum? burstCompensation;

  /// see [BurstMaxExposureTimeEnum]
  BurstMaxExposureTimeEnum? burstMaxExposureTime;

  /// see [BurstEnableIsoControlEnum]
  BurstEnableIsoControlEnum? burstEnableIsoControl;

  /// see [BurstOrderEnum]
  BurstOrderEnum? burstOrder;

  BurstOption(
      this.burstCaptureNum,
      this.burstBracketStep,
      this.burstCompensation,
      this.burstMaxExposureTime,
      this.burstEnableIsoControl,
      this.burstOrder);

  @override
  bool operator ==(Object other) => hashCode == other.hashCode;

  @override
  int get hashCode => Object.hashAll([
        burstCaptureNum,
        burstBracketStep,
        burstCompensation,
        burstMaxExposureTime,
        burstEnableIsoControl,
        burstOrder
      ]);
}

/// Number of shots for burst shooting
/// 1, 3, 5, 7, 9
enum BurstCaptureNumEnum {
  burstCaptureNum_1('BURST_CAPTURE_NUM_1'),
  burstCaptureNum_3('BURST_CAPTURE_NUM_3'),
  burstCaptureNum_5('BURST_CAPTURE_NUM_5'),
  burstCaptureNum_7('BURST_CAPTURE_NUM_7'),
  burstCaptureNum_9('BURST_CAPTURE_NUM_9');

  final String rawValue;

  const BurstCaptureNumEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static BurstCaptureNumEnum? getValue(String rawValue) {
    return BurstCaptureNumEnum.values.cast<BurstCaptureNumEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Bracket value range between each shot for burst shooting
/// 0.0, 0.3, 0.7, 1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0
enum BurstBracketStepEnum {
  bracketStep_0_0('BRACKET_STEP_0_0'),
  bracketStep_0_3('BRACKET_STEP_0_3'),
  bracketStep_0_7('BRACKET_STEP_0_7'),
  bracketStep_1_0('BRACKET_STEP_1_0'),
  bracketStep_1_3('BRACKET_STEP_1_3'),
  bracketStep_1_7('BRACKET_STEP_1_7'),
  bracketStep_2_0('BRACKET_STEP_2_0'),
  bracketStep_2_3('BRACKET_STEP_2_3'),
  bracketStep_2_7('BRACKET_STEP_2_7'),
  bracketStep_3_0('BRACKET_STEP_3_0');

  final String rawValue;

  const BurstBracketStepEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static BurstBracketStepEnum? getValue(String rawValue) {
    return BurstBracketStepEnum.values.cast<BurstBracketStepEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Exposure compensation for the base image and entire shooting for burst shooting
/// -5.0, -4.7, -4,3, -4.0, -3.7, -3,3, -3.0, -2.7, -2,3, -2.0, -1.7, -1,3, -1.0, -0.7, -0,3,
/// 0.0, 0.3, 0.7, 1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0, 3.3, 3.7, 4.0, 4.3, 4.7, 5.0
enum BurstCompensationEnum {
  burstCompensationDown_5_0('BURST_COMPENSATION_DOWN_5_0'),
  burstCompensationDown_4_7('BURST_COMPENSATION_DOWN_4_7'),
  burstCompensationDown_4_3('BURST_COMPENSATION_DOWN_4_3'),
  burstCompensationDown_4_0('BURST_COMPENSATION_DOWN_4_0'),
  burstCompensationDown_3_7('BURST_COMPENSATION_DOWN_3_7'),
  burstCompensationDown_3_3('BURST_COMPENSATION_DOWN_3_3'),
  burstCompensationDown_3_0('BURST_COMPENSATION_DOWN_3_0'),
  burstCompensationDown_2_7('BURST_COMPENSATION_DOWN_2_7'),
  burstCompensationDown_2_3('BURST_COMPENSATION_DOWN_2_3'),
  burstCompensationDown_2_0('BURST_COMPENSATION_DOWN_2_0'),
  burstCompensationDown_1_7('BURST_COMPENSATION_DOWN_1_7'),
  burstCompensationDown_1_3('BURST_COMPENSATION_DOWN_1_3'),
  burstCompensationDown_1_0('BURST_COMPENSATION_DOWN_1_0'),
  burstCompensationDown_0_7('BURST_COMPENSATION_DOWN_0_7'),
  burstCompensationDown_0_3('BURST_COMPENSATION_DOWN_0_3'),
  burstCompensation_0_0('BURST_COMPENSATION_0_0'),
  burstCompensationUp_0_3('BURST_COMPENSATION_UP_0_3'),
  burstCompensationUp_0_7('BURST_COMPENSATION_UP_0_7'),
  burstCompensationUp_1_0('BURST_COMPENSATION_UP_1_0'),
  burstCompensationUp_1_3('BURST_COMPENSATION_UP_1_3'),
  burstCompensationUp_1_7('BURST_COMPENSATION_UP_1_7'),
  burstCompensationUp_2_0('BURST_COMPENSATION_UP_2_0'),
  burstCompensationUp_2_3('BURST_COMPENSATION_UP_2_3'),
  burstCompensationUp_2_7('BURST_COMPENSATION_UP_2_7'),
  burstCompensationUp_3_0('BURST_COMPENSATION_UP_3_0'),
  burstCompensationUp_3_3('BURST_COMPENSATION_UP_3_3'),
  burstCompensationUp_3_7('BURST_COMPENSATION_UP_3_7'),
  burstCompensationUp_4_0('BURST_COMPENSATION_UP_4_0'),
  burstCompensationUp_4_3('BURST_COMPENSATION_UP_4_3'),
  burstCompensationUp_4_7('BURST_COMPENSATION_UP_4_7'),
  burstCompensationUp_5_0('BURST_COMPENSATION_UP_5_0');

  final String rawValue;

  const BurstCompensationEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static BurstCompensationEnum? getValue(String rawValue) {
    return BurstCompensationEnum.values
        .cast<BurstCompensationEnum?>()
        .firstWhere((element) => element?.rawValue == rawValue,
            orElse: () => null);
  }
}

/// Maximum exposure time for burst shooting
/// 0.5, 0.625, 0.76923076, 1, 1.3, 1.6, 2, 2.5, 3.2, 4, 5, 6, 8, 10, 13, 15, 20, 25, 30, 40, 50, 60
enum BurstMaxExposureTimeEnum {
  maxExposureTime_0_5('MAX_EXPOSURE_TIME_0_5'),
  maxExposureTime_0_625('MAX_EXPOSURE_TIME_0_625'),
  maxExposureTime_0_76923076('MAX_EXPOSURE_TIME_0_76923076'),
  maxExposureTime_1('MAX_EXPOSURE_TIME_1'),
  maxExposureTime_1_3('MAX_EXPOSURE_TIME_1_3'),
  maxExposureTime_1_6('MAX_EXPOSURE_TIME_1_6'),
  maxExposureTime_2('MAX_EXPOSURE_TIME_2'),
  maxExposureTime_2_5('MAX_EXPOSURE_TIME_2_5'),
  maxExposureTime_3_2('MAX_EXPOSURE_TIME_3_2'),
  maxExposureTime_4('MAX_EXPOSURE_TIME_4'),
  maxExposureTime_5('MAX_EXPOSURE_TIME_5'),
  maxExposureTime_6('MAX_EXPOSURE_TIME_6'),
  maxExposureTime_8('MAX_EXPOSURE_TIME_8'),
  maxExposureTime_10('MAX_EXPOSURE_TIME_10'),
  maxExposureTime_13('MAX_EXPOSURE_TIME_13'),
  maxExposureTime_15('MAX_EXPOSURE_TIME_15'),
  maxExposureTime_20('MAX_EXPOSURE_TIME_20'),
  maxExposureTime_25('MAX_EXPOSURE_TIME_25'),
  maxExposureTime_30('MAX_EXPOSURE_TIME_30'),
  maxExposureTime_40('MAX_EXPOSURE_TIME_40'),
  maxExposureTime_50('MAX_EXPOSURE_TIME_50'),
  maxExposureTime_60('MAX_EXPOSURE_TIME_60');

  final String rawValue;

  const BurstMaxExposureTimeEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static BurstMaxExposureTimeEnum? getValue(String rawValue) {
    return BurstMaxExposureTimeEnum.values
        .cast<BurstMaxExposureTimeEnum?>()
        .firstWhere((element) => element?.rawValue == rawValue,
            orElse: () => null);
  }
}

/// Adjustment with ISO sensitivity for burst shooting
/// 0: Do not adjust with ISO sensitivity, 1: Adjust with ISO sensitivity
enum BurstEnableIsoControlEnum {
  off('OFF'),
  on('ON');

  final String rawValue;

  const BurstEnableIsoControlEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static BurstEnableIsoControlEnum? getValue(String rawValue) {
    return BurstEnableIsoControlEnum.values
        .cast<BurstEnableIsoControlEnum?>()
        .firstWhere((element) => element?.rawValue == rawValue,
            orElse: () => null);
  }
}

/// Shooting order for burst shooting
/// 0: '0' → '-' → '+', 1: '-' → '0' → '+'
enum BurstOrderEnum {
  burstBracketOrder_0('BURST_BRACKET_ORDER_0'),
  burstBracketOrder_1('BURST_BRACKET_ORDER_1');

  final String rawValue;

  const BurstOrderEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static BurstOrderEnum? getValue(String rawValue) {
    return BurstOrderEnum.values.cast<BurstOrderEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Exif metadata of a still image.
class Exif {
  /// EXIF Support version
  String exifVersion;

  /// File created or updated date and time
  String dateTime;

  /// Image width (pixel). Theta X returns null.
  int? imageWidth;

  /// Image height (pixel). Theta X returns null.
  int? imageLength;

  /// GPS latitude if exists.
  double? gpsLatitude;

  /// GPS longitude if exists.
  double? gpsLongitude;

  Exif(this.exifVersion, this.dateTime, this.imageWidth, this.imageLength,
      this.gpsLatitude, this.gpsLongitude);
}

/// Photo sphere XMP metadata of a still image.
class Xmp {
  /// Compass heading, for the center the image. Theta X returns null.
  double? poseHeadingDegrees;

  /// Image width (pixel).
  int fullPanoWidthPixels;

  /// Image height (pixel).
  int fullPanoHeightPixels;

  Xmp(this.poseHeadingDegrees, this.fullPanoWidthPixels,
      this.fullPanoHeightPixels);
}

/// Metadata of a still image
class Metadata {
  /// Exif metadata of a still image.
  Exif exif;

  /// Photo sphere XMP metadata of a still image.
  Xmp xmp;

  Metadata(this.exif, this.xmp);
}

/// Enum for authentication mode.
enum AuthModeEnum {
  /// Authentication mode. none
  none('NONE'),

  /// Authentication mode. WEP
  wep('WEP'),

  /// Authentication mode. WPA/WPA2 PSK
  wpa('WPA');

  final String rawValue;

  const AuthModeEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static AuthModeEnum? getValue(String rawValue) {
    return AuthModeEnum.values.cast<AuthModeEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Access point information.
class AccessPoint {
  /// SSID of the access point.
  String ssid;

  /// True if SSID stealth is enabled.
  bool ssidStealth;

  /// Authentication mode.
  AuthModeEnum authMode;

  /// Connection priority 1 to 5. Theta X fixes to 1 (The access point registered later has a higher priority.)
  int connectionPriority = 1;

  /// Using DHCP or not. This can be acquired when SSID is registered as an enable access point.
  bool usingDhcp;

  /// IP address assigned to camera. This setting can be acquired when “usingDhcp” is false.
  String? ipAddress;

  /// Subnet Mask. This setting can be acquired when “usingDhcp” is false.
  String? subnetMask;

  /// Default Gateway. This setting can be acquired when “usingDhcp” is false.
  String? defaultGateway;

  /// Proxy information to be used for the access point.
  Proxy? proxy;

  AccessPoint(
      this.ssid,
      this.ssidStealth,
      this.authMode,
      this.connectionPriority,
      this.usingDhcp,
      this.ipAddress,
      this.subnetMask,
      this.defaultGateway,
      this.proxy);
}

/// Camera setting options name.
///
/// [options name](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/options.md)
enum OptionNameEnum {
  /// Option name _aiAutoThumbnail
  aiAutoThumbnail('AiAutoThumbnail', AiAutoThumbnailEnum),

  /// Option name _autoBracket
  autoBracket('AutoBracket', List<BracketSetting>),

  /// Option name aperture
  aperture('Aperture', ApertureEnum),

  /// Option name _bitrate
  bitrate('Bitrate', Bitrate),

  /// Option name _burstMode
  burstMode('BurstMode', BurstModeEnum),

  /// Option name _bluetoothPower
  bluetoothPower('BluetoothPower', BluetoothPowerEnum),

  /// Option name _bluetoothRole
  bluetoothRole('BluetoothRole', BluetoothRoleEnum),

  /// Option name _burstOption
  burstOption('BurstOption', BurstOption),

  /// Option name _cameraControlSource
  cameraControlSource('CameraControlSource', CameraControlSourceEnum),

  /// Option name _cameraMode
  cameraMode('CameraMode', CameraModeEnum),

  /// Option name _cameraPower
  cameraPower('CameraPower', CameraPowerEnum),

  /// Option name captureInterval
  captureInterval('CaptureInterval', int),

  /// Option name captureMode
  captureMode('CaptureMode', CaptureModeEnum),

  /// Option name captureNumber
  captureNumber('CaptureNumber', int),

  /// Option name _colorTemperature
  colorTemperature('ColorTemperature', int),

  /// Option name _compositeShootingOutputInterval
  compositeShootingOutputInterval('CompositeShootingOutputInterval', int),

  /// Option name _compositeShootingTime
  compositeShootingTime('CompositeShootingTime', int),

  /// Option name continuousNumber
  continuousNumber('ContinuousNumber', ContinuousNumberEnum),

  /// Option name dateTimeZone
  dateTimeZone('DateTimeZone', String),

  /// Option name _ethernetConfig
  ethernetConfig('EthernetConfig', EthernetConfig),

  /// Option name exposureCompensation
  exposureCompensation('ExposureCompensation', ExposureCompensationEnum),

  /// Option name exposureDelay
  exposureDelay('ExposureDelay', ExposureDelayEnum),

  /// Option name exposureProgram
  exposureProgram('ExposureProgram', ExposureProgramEnum),

  /// Option name faceDetect
  faceDetect('FaceDetect', FaceDetectEnum),

  /// Option name fileFormat
  fileFormat('FileFormat', FileFormatEnum),

  /// Option name _filter
  filter('Filter', FilterEnum),

  /// Option name function
  function('Function', ShootingFunctionEnum),

  /// Option name gain
  gain('Gain', GainEnum),

  /// Option name gpsInfo
  gpsInfo('GpsInfo', GpsInfo),

  /// Option name imageStitching
  imageStitching('ImageStitching', ImageStitchingEnum),

  /// Option name _gpsTagRecording
  ///
  /// For RICOH THETA X or later
  isGpsOn('IsGpsOn', bool),

  /// Option name iso
  iso('Iso', IsoEnum),

  /// Option name isoAutoHighLimit
  isoAutoHighLimit('IsoAutoHighLimit', IsoAutoHighLimitEnum),

  /// Option name _language
  language('Language', LanguageEnum),

  /// Option name _latestEnabledExposureDelayTime
  latestEnabledExposureDelayTime(
      'LatestEnabledExposureDelayTime', ExposureDelayEnum),

  /// Option name _maxRecordableTime
  maxRecordableTime('MaxRecordableTime', MaxRecordableTimeEnum),

  /// Option name _networkType
  networkType('NetworkType', NetworkTypeEnum),

  /// Option name offDelay
  offDelay('OffDelay', OffDelayEnum),

  /// Option name _password
  password('Password', String),

  /// Option name powerSaving
  powerSaving('PowerSaving', PowerSavingEnum),

  /// Option name preset
  preset('Preset', PresetEnum),

  /// Option name previewFormat
  previewFormat('PreviewFormat', PreviewFormatEnum),

  /// Option name _proxy
  proxy('Proxy', Proxy),

  /// Option name remainingPictures
  remainingPictures('RemainingPictures', int),

  /// Option name remainingVideoSeconds
  remainingVideoSeconds('RemainingVideoSeconds', int),

  /// Option name remainingSpace
  remainingSpace('RemainingSpace', int),

  /// Option name shootingMethod
  shootingMethod('ShootingMethod', ShootingMethodEnum),

  /// Shutter speed (sec).
  shutterSpeed('ShutterSpeed', ShutterSpeedEnum),

  /// Option name _shutterVolume
  shutterVolume('ShutterVolume', int),

  /// Option name sleepDelay
  sleepDelay('SleepDelay', SleepDelayEnum),

  /// Option name timeShift
  timeShift('TimeShift', TimeShift),

  /// Option name topBottomCorrection
  topBottomCorrection('TopBottomCorrection', TopBottomCorrectionOptionEnum),

  /// Option name topBottomCorrectionRotation
  topBottomCorrectionRotation(
      'TopBottomCorrectionRotation', TopBottomCorrectionRotation),

  /// Option name totalSpace
  totalSpace('TotalSpace', int),

  /// Option name _username
  username('Username', String),

  /// Option name videoStitching
  videoStitching('VideoStitching', VideoStitchingEnum),

  /// Option name _visibilityReduction
  visibilityReduction('VisibilityReduction', VisibilityReductionEnum),

  /// Option name whiteBalance
  whiteBalance('WhiteBalance', WhiteBalanceEnum),

  /// Option name WhiteBalanceAutoStrength
  whiteBalanceAutoStrength(
      'WhiteBalanceAutoStrength', WhiteBalanceAutoStrengthEnum),

  // Option name wlanfrequency
  wlanFrequency('WlanFrequency', WlanFrequencyEnum);

  final String rawValue;
  final dynamic valueType;

  const OptionNameEnum(this.rawValue, this.valueType);

  @override
  String toString() {
    return rawValue;
  }

  static OptionNameEnum? getValue(String rawValue) {
    return OptionNameEnum.values.cast<OptionNameEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// AI auto thumbnail setting.
///
/// For RICOH THETA X
enum AiAutoThumbnailEnum {
  /// AI auto setting ON.
  on('ON'),

  /// AI auto setting OFF.
  off('OFF');

  final String rawValue;

  const AiAutoThumbnailEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static AiAutoThumbnailEnum? getValue(String rawValue) {
    return AiAutoThumbnailEnum.values.cast<AiAutoThumbnailEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Aperture value.
enum ApertureEnum {
  /// Aperture AUTO(0).
  apertureAuto('APERTURE_AUTO'),

  /// Aperture 2.0F.
  ///
  /// RICOH THETA V or prior
  aperture_2_0('APERTURE_2_0'),

  /// Aperture 2.1F.
  ///
  /// RICOH THETA Z1 and the exposure program [exposureProgram] is set to Manual or Aperture Priority
  aperture_2_1('APERTURE_2_1'),

  /// Aperture 2.4F.
  ///
  /// RICOH THETA X or later
  aperture_2_4('APERTURE_2_4'),

  /// Aperture 3.5F.
  ///
  /// RICOH THETA Z1 and the exposure program [exposureProgram] is set to Manual or Aperture Priority
  aperture_3_5('APERTURE_3_5'),

  /// Aperture 5.6F.
  ///
  /// RICOH THETA Z1 and the exposure program [exposureProgram] is set to Manual or Aperture Priority
  aperture_5_6('APERTURE_5_6');

  final String rawValue;

  const ApertureEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static ApertureEnum? getValue(String rawValue) {
    return ApertureEnum.values.cast<ApertureEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Movie bit rate.
///
/// ### Support value
/// The supported value depends on the shooting mode [CaptureMode].
///
/// | Shooting mode | Supported value |
/// | ------------- | --------------- |
/// |         video | "Fine", "Normal", "Economy"(RICOH THETA X or later) <br/>"2000000"-"120000000" (RICOH THETA X v1.20 or later) |
/// |         image | "Auto", "1048576"-"20971520" (RICOH THETA X v1.20 or later) |
/// | _liveStreaming |         "Auto" |
///
/// #### RICOH THETA X
/// | Video mode | Fine<br/>[Mbps] | Normal<br/>[Mbps] | Economy<br/>[Mbps] | Remark |
/// |------------| --------------- | ------------------| ------------------ | ------ |
/// |   2K 30fps |              32 |                16 |                  8 |        |
/// |   2K 60fps |              64 |                32 |                 16 |        |
/// |   4K 10fps |              48 |                24 |                 12 |        |
/// |   4K 15fps |              64 |                32 |                 16 |        |
/// |   4K 30fps |             100 |                54 |                 32 |        |
/// |   4K 60fps |             120 |                64 |                 32 |        |
/// | 5.7K  2fps |              16 |                12 |                  8 | firmware v2.00.0 or later   |
/// |            |              64 |                32 |                 16 | firmware v1.40.0 or later   (I-frame only)|
/// |            |              16 |                 8 |                  4 | firmware v1.30.0 or earlier |
/// | 5.7K  5fps |              40 |                30 |                 20 | firmware v2.00.0 or later   |
/// |            |             120 |                80 |                 40 | firmware v1.40.0 or later   (I-frame only)|
/// |            |              32 |                16 |                  8 | firmware v1.30.0 or earlier |
/// | 5.7K 10fps |              80 |                60 |                 40 | firmware v2.00.0 or later   |
/// |            |              64 |                40 |                 20 | firmware v1.40.0 or later   |
/// |            |              48 |                24 |                 12 | firmware v1.30.0 or earlier |
/// | 5.7K 15fps |              64 |                32 |                 16 |        |
/// | 5.7K 30fps |             120 |                64 |                 32 |        |
/// |   8K  2fps |              64 |                32 |                 16 | firmware v1.40.0 or later   (I-frame only)|
/// |            |              32 |                16 |                  8 | firmware v1.30.0 or earlier (I-frame only)|
/// |   8K  5fps |             120 |                96 |                 40 | firmware v1.40.0 or later   (I-frame only)|
/// |            |              64 |                32 |                 16 | firmware v1.30.0 or earlier (I-frame only)|
/// |   8K 10fps |             120 |                96 |                 40 | firmware v1.40.0 or later   (I-frame only)|
/// |            |             120 |                64 |                 32 | firmware v1.30.0 or earlier (I-frame only)|
///
/// For
/// - RICOH THETA X
/// - RICOH THETA Z1
/// - RICOH THETA V firmware v2.50.1 or later
class Bitrate {
  final String rawValue;

  Bitrate._internal(String rateStr) : rawValue = rateStr;

  @override
  bool operator ==(Object other) => hashCode == other.hashCode;

  @override
  int get hashCode => Object.hashAll([rawValue]);

  @override
  String toString() => rawValue;

  static final auto = Bitrate._internal('AUTO');
  static final fine = Bitrate._internal('FINE');
  static final normal = Bitrate._internal('NORMAL');
  static final economy = Bitrate._internal('ECONOMY');

  static final values = [auto, fine, normal, economy];

  static Bitrate? getValue(String rawValue) {
    var list = List<Bitrate?>.of(values);
    list.add(null);
    return list.firstWhere((element) {
      return element?.rawValue == rawValue;
    }, orElse: () => null);
  }
}

/// Movie bit rate value of number.
class BitrateNumber extends Bitrate {
  final int value;

  BitrateNumber(this.value) : super._internal(value.toString());
}

/// BracketSetting value.
class BracketSetting {
  /// Aperture value.
  final ApertureEnum? aperture;

  /// Color temperature of the camera (Kelvin).
  final int? colorTemperature;

  /// Exposure compensation (EV).
  final ExposureCompensationEnum? exposureCompensation;

  /// Exposure program.
  final ExposureProgramEnum? exposureProgram;

  /// ISO sensitivity.
  final IsoEnum? iso;

  /// Shutter speed (sec).
  final ShutterSpeedEnum? shutterSpeed;

  /// White balance.
  final WhiteBalanceEnum? whiteBalance;

  BracketSetting(
      {this.aperture,
      this.colorTemperature,
      this.exposureCompensation,
      this.exposureProgram,
      this.iso,
      this.shutterSpeed,
      this.whiteBalance});

  @override
  bool operator ==(Object other) => hashCode == other.hashCode;

  @override
  int get hashCode => Object.hashAll([
        aperture,
        colorTemperature,
        exposureCompensation,
        exposureProgram,
        iso,
        shutterSpeed,
        whiteBalance
      ]);
}

/// Camera control source.
enum CameraControlSourceEnum {
  /// Operation is possible with the camera. Locks the smartphone
  /// application UI (supported app only).
  camera('CAMERA'),

  /// Operation is possible with the smartphone application. Locks
  /// the UI on the shooting screen on the camera.
  app('APP');

  final String rawValue;

  const CameraControlSourceEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static CameraControlSourceEnum? getValue(String rawValue) {
    return CameraControlSourceEnum.values
        .cast<CameraControlSourceEnum?>()
        .firstWhere((element) => element?.rawValue == rawValue,
            orElse: () => null);
  }
}

/// Camera mode.
enum CameraModeEnum {
  /// shooting screen
  capture('CAPTURE'),

  /// playback screen
  playback('PLAYBACK'),

  /// shooting setting screen
  setting('SETTING'),

  /// plugin selection screen
  plugin('PLUGIN');

  final String rawValue;

  const CameraModeEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static CameraModeEnum? getValue(String rawValue) {
    return CameraModeEnum.values.cast<CameraModeEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Shooting mode.
enum CaptureModeEnum {
  /// Shooting mode. Still image capture mode
  image('IMAGE'),

  /// Shooting mode. Video capture mode
  video('VIDEO'),

  /// Shooting mode. Live streaming mode just for Theta S.
  liveStreaming('LIVE_STREAMING'),

  /// Shooting mode. Interval still image capture mode just for Theta SC2 and Theta SC2 for business
  interval('INTERVAL'),

  /// Shooting mode. Preset mode just for Theta SC2 and Theta SC2 for business
  preset('PRESET');

  final String rawValue;

  const CaptureModeEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static CaptureModeEnum? getValue(String rawValue) {
    return CaptureModeEnum.values.cast<CaptureModeEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Number of shots for continuous shooting.
/// It can be acquired by camera.getOptions.
///
/// For RICOH THETA X
/// - 11k image: Maximum value 8
/// - 5.5k image: Maximum value 20
///
/// Depending on available storage capacity, the value may be less than maximum.
enum ContinuousNumberEnum {
  /// Disable continuous shooting.
  off('OFF'),

  /// Maximum value 1
  max1('MAX_1'),

  /// Maximum value 2
  max2('MAX_2'),

  /// Maximum value 3
  max3('MAX_3'),

  /// Maximum value 4
  max4('MAX_4'),

  /// Maximum value 5
  max5('MAX_5'),

  /// Maximum value 6
  max6('MAX_6'),

  /// Maximum value 7
  max7('MAX_7'),

  /// Maximum value 8
  max8('MAX_8'),

  /// Maximum value 9
  max9('MAX_9'),

  /// Maximum value 10
  max10('MAX_10'),

  /// Maximum value 11
  max11('MAX_11'),

  /// Maximum value 12
  max12('MAX_12'),

  /// Maximum value 13
  max13('MAX_13'),

  /// Maximum value 14
  max14('MAX_14'),

  /// Maximum value 15
  max15('MAX_15'),

  /// Maximum value 16
  max16('MAX_16'),

  /// Maximum value 17
  max17('MAX_17'),

  /// Maximum value 18
  max18('MAX_18'),

  /// Maximum value 19
  max19('MAX_19'),

  /// Maximum value 20
  max20('MAX_20'),

  /// Unsupported value
  ///
  /// If camera.getOptions returns the number other than 0 to 20, this value is set.
  /// Do not use this value to setOptions().
  unsupported('UNSUPPORTED');

  final String rawValue;

  const ContinuousNumberEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static ContinuousNumberEnum? getValue(String rawValue) {
    return ContinuousNumberEnum.values.cast<ContinuousNumberEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Exposure compensation (EV).
enum ExposureCompensationEnum {
  /// Exposure compensation -2.0
  m2_0('M2_0'),

  /// Exposure compensation -1.7
  m1_7('M1_7'),

  /// Exposure compensation -1.3
  m1_3('M1_3'),

  /// Exposure compensation -1.0
  m1_0('M1_0'),

  /// Exposure compensation -0.7
  m0_7('M0_7'),

  /// Exposure compensation -0.3
  m0_3('M0_3'),

  /// Exposure compensation 0.0
  zero('ZERO'),

  /// Exposure compensation 0.3
  p0_3('P0_3'),

  /// Exposure compensation 0.7
  p0_7('P0_7'),

  /// Exposure compensation 1.0
  p1_0('P1_0'),

  /// Exposure compensation 1.3
  p1_3('P1_3'),

  /// Exposure compensation 1.7
  p1_7('P1_7'),

  /// Exposure compensation 2.0
  p2_0('P2_0');

  final String rawValue;

  const ExposureCompensationEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static ExposureCompensationEnum? getValue(String rawValue) {
    return ExposureCompensationEnum.values
        .cast<ExposureCompensationEnum?>()
        .firstWhere((element) => element?.rawValue == rawValue,
            orElse: () => null);
  }
}

/// Operating time (sec.) of the self-timer.
enum ExposureDelayEnum {
  /// Disable self-timer.
  delayOff('DELAY_OFF'),

  /// Self-timer time. 1sec.
  delay1('DELAY_1'),

  /// Self-timer time. 2sec.
  delay2('DELAY_2'),

  /// Self-timer time. 3sec.
  delay3('DELAY_3'),

  /// Self-timer time. 4sec.
  delay4('DELAY_4'),

  /// Self-timer time. 5sec.
  delay5('DELAY_5'),

  /// Self-timer time. 6sec.
  delay6('DELAY_6'),

  /// Self-timer time. 7sec.
  delay7('DELAY_7'),

  /// Self-timer time. 8sec.
  delay8('DELAY_8'),

  /// Self-timer time. 9sec.
  delay9('DELAY_9'),

  /// Self-timer time. 10sec.
  delay10('DELAY_10');

  final String rawValue;

  const ExposureDelayEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static ExposureDelayEnum? getValue(String rawValue) {
    return ExposureDelayEnum.values.cast<ExposureDelayEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Exposure program. The exposure settings that take priority can be selected.
///
/// It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
/// Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
enum ExposureProgramEnum {
  /// Manual program
  ///
  /// Manually set the ISO sensitivity (iso) setting, shutter speed (shutterSpeed) and aperture (aperture, RICOH THETA Z1).
  manual('MANUAL'),

  /// Normal program
  ///
  /// Exposure settings are all set automatically.
  normalProgram('NORMAL_PROGRAM'),

  /// Aperture priority program
  ///
  /// Manually set the aperture (aperture).
  /// (RICOH THETA Z1)
  aperturePriority('APERTURE_PRIORITY'),

  /// Shutter priority program
  ///
  /// Manually set the shutter speed (shutterSpeed).
  shutterPriority('SHUTTER_PRIORITY'),

  /// ISO priority program
  ///
  /// Manually set the ISO sensitivity (iso) setting.
  isoPriority('ISO_PRIORITY');

  final String rawValue;

  const ExposureProgramEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static ExposureProgramEnum? getValue(String rawValue) {
    return ExposureProgramEnum.values.cast<ExposureProgramEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Face detection
///
/// For
/// - RICOH THETA X
enum FaceDetectEnum {
  /// Face detection ON
  on('ON'),

  /// Face detection OFF
  off('OFF');

  final String rawValue;

  const FaceDetectEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static FaceDetectEnum? getValue(String rawValue) {
    return FaceDetectEnum.values.cast<FaceDetectEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Image processing filter.
enum FilterEnum {
  /// Image processing filter. No filter.
  off('OFF'),

  /// Image processing filter. DR compensation.
  ///
  /// RICOH THETA X is not supported.
  drComp('DR_COMP'),

  /// Image processing filter. Noise reduction.
  noiseReduction('NOISE_REDUCTION'),

  /// Image processing filter. HDR.
  hdr('HDR'),

  /// Image processing filter. Handheld HDR.
  ///
  /// RICOH THETA X firmware v2.40.0 or later,
  /// RICOH THETA Z1 firmware v1.20.1 or later,
  /// and RICOH THETA V firmware v3.10.1 or later.
  hhHdr('HH_HDR');

  final String rawValue;

  const FilterEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static FilterEnum? getValue(String rawValue) {
    return FilterEnum.values.cast<FilterEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Microphone gain.
///
/// For
/// - RICOH THETA X
/// - RICOH THETA Z1
/// - RICOH THETA V
enum GainEnum {
  /// Normal mode
  normal('NORMAL'),

  /// Loud volume mode
  megaVolume('MEGA_VOLUME'),

  /// Mute mode
  /// (RICOH THETA V firmware v2.50.1 or later, RICOH THETA X is not supported.)
  mute('MUTE');

  final String rawValue;

  const GainEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static GainEnum? getValue(String rawValue) {
    return GainEnum.values.cast<GainEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// ImageStitching
enum ImageStitchingEnum {
  /// Refer to stitching when shooting with "auto"
  auto('AUTO'),

  /// Performs static stitching
  static('STATIC'),

  /// Performs dynamic stitching (RICOH THETA X or later)
  dynamic('DYNAMIC'),

  /// For Normal shooting, performs dynamic stitching,
  /// for Interval shooting, saves dynamic distortion correction parameters
  /// for the first image and then uses them for the 2nd and subsequent
  /// images (RICOH THETA X is not supported)
  dynamicAuto('DYNAMIC_AUTO'),

  /// Performs semi-dynamic stitching
  /// Saves dynamic distortion correction parameters for the first image
  /// and then uses them for the 2nd and subsequent images (RICOH THETA X or later)
  dynamicSemiAuto('DYNAMIC_SEMI_AUTO'),

  /// Performs dynamic stitching and then saves distortion correction parameters
  dynamicSave('DYNAMIC_SAVE'),

  /// Performs stitching using the saved distortion correction parameters
  dynamicLoad('DYNAMIC_LOAD'),

  /// Does not perform stitching
  none('NONE');

  final String rawValue;

  const ImageStitchingEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static ImageStitchingEnum? getValue(String rawValue) {
    return ImageStitchingEnum.values.cast<ImageStitchingEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// ISO sensitivity.
///
/// It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
/// Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
///
/// When the exposure program [exposureProgram] is set to Manual or ISO Priority
enum IsoEnum {
  /// ISO sensitivity. AUTO (0)
  isoAuto('ISO_AUTO'),

  /// ISO sensitivity. ISO 50
  ///
  /// For RICOH THETA X or later
  iso50('ISO_50'),

  /// ISO sensitivity. ISO 64
  ///
  /// For RICOH THETA V or X or later
  iso64('ISO_64'),

  /// ISO sensitivity. ISO 80
  ///
  /// For RICOH THETA V or Z1 or X or later
  iso80('ISO_80'),

  /// ISO sensitivity. ISO 100
  iso100('ISO_100'),

  /// ISO sensitivity. ISO 125
  iso125('ISO_125'),

  /// ISO sensitivity. ISO 160
  iso160('ISO_160'),

  /// ISO sensitivity. ISO 200
  iso200('ISO_200'),

  /// ISO sensitivity. ISO 250
  iso250('ISO_250'),

  /// ISO sensitivity. ISO 320
  iso320('ISO_320'),

  /// ISO sensitivity. ISO 400
  iso400('ISO_400'),

  /// ISO sensitivity. ISO 500
  iso500('ISO_500'),

  /// ISO sensitivity. ISO 640
  iso640('ISO_640'),

  /// ISO sensitivity. ISO 800
  iso800('ISO_800'),

  /// ISO sensitivity. ISO 1000
  iso1000('ISO_1000'),

  /// ISO sensitivity. ISO 1250
  iso1250('ISO_1250'),

  /// ISO sensitivity. ISO 1600
  iso1600('ISO_1600'),

  /// ISO sensitivity. ISO 2000
  ///
  /// For RICOH THETA V or Z1 or X or later
  iso2000('ISO_2000'),

  /// ISO sensitivity. ISO 2500
  ///
  /// For RICOH THETA V or Z1 or X or later
  iso2500('ISO_2500'),

  /// ISO sensitivity. ISO 3200
  ///
  /// For RICOH THETA V or Z1 or X or later
  iso3200('ISO_3200'),

  /// ISO sensitivity. ISO 4000
  ///
  /// For RICOH THETA Z1
  /// For RICOH THETA V, Available in video shooting mode.
  iso4000('ISO_4000'),

  /// ISO sensitivity. ISO 5000
  ///
  /// For RICOH THETA Z1
  /// For RICOH THETA V, Available in video shooting mode.
  iso5000('ISO_5000'),

  /// ISO sensitivity. ISO 6400
  ///
  /// For RICOH THETA Z1
  /// For RICOH THETA V, Available in video shooting mode.
  iso6400('ISO_6400');

  final String rawValue;

  const IsoEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static IsoEnum? getValue(String rawValue) {
    return IsoEnum.values.cast<IsoEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
///
/// 100*1, 125*1, 160*1, 200, 250, 320, 400, 500, 640, 800, 1000, 1250, 1600, 2000, 2500, 3200, 4000*2, 5000*2, 6400*2
/// *1 Enabled only with RICOH THETA X.
/// *2 Enabled with RICOH THETA Z1's image shooting mode and video shooting mode, and with RICOH THETA V's video shooting mode.
enum IsoAutoHighLimitEnum {
  /// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  /// ISO 100
  ///
  /// Enabled only with RICOH THETA X.
  iso100('ISO_100'),

  /// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  /// ISO 125
  ///
  /// Enabled only with RICOH THETA X.
  iso125('ISO_125'),

  /// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  /// ISO 160
  ///
  /// Enabled only with RICOH THETA X.
  iso160('ISO_160'),

  /// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  /// ISO 200
  iso200('ISO_200'),

  /// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  /// ISO 250
  iso250('ISO_250'),

  /// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  /// ISO 320
  iso320('ISO_320'),

  /// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  /// ISO 400
  iso400('ISO_400'),

  /// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  /// ISO 500
  iso500('ISO_500'),

  /// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  /// ISO 640
  iso640('ISO_640'),

  /// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  /// ISO 800
  iso800('ISO_800'),

  /// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  /// ISO 1000
  iso1000('ISO_1000'),

  /// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  /// ISO 1250
  iso1250('ISO_1250'),

  /// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  /// ISO 1600
  iso1600('ISO_1600'),

  /// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  /// ISO 2000
  iso2000('ISO_2000'),

  /// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  /// ISO 2500
  iso2500('ISO_2500'),

  /// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  /// ISO 3200
  iso3200('ISO_3200'),

  /// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  /// ISO 4000
  ///
  /// Enabled with RICOH THETA Z1's image shooting mode and video shooting mode, and with RICOH THETA V's video shooting mode.
  iso4000('ISO_4000'),

  /// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  /// ISO 5000
  ///
  /// Enabled with RICOH THETA Z1's image shooting mode and video shooting mode, and with RICOH THETA V's video shooting mode.
  iso5000('ISO_5000'),

  /// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  /// ISO 6400
  ///
  /// Enabled with RICOH THETA Z1's image shooting mode and video shooting mode, and with RICOH THETA V's video shooting mode.
  iso6400('ISO_6400');

  final String rawValue;

  const IsoAutoHighLimitEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static IsoAutoHighLimitEnum? getValue(String rawValue) {
    return IsoAutoHighLimitEnum.values.cast<IsoAutoHighLimitEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Language used in camera OS.
enum LanguageEnum {
  /// Language used in camera OS.
  /// de
  de('DE'),

  /// Language used in camera OS.
  /// en-GB
  enGB('EN_GB'),

  /// Language used in camera OS.
  /// en-US
  enUS('EN_US'),

  /// Language used in camera OS.
  /// fr
  fr('FR'),

  /// Language used in camera OS.
  /// it
  it('IT'),

  /// Language used in camera OS.
  /// ja
  ja('JA'),

  /// Language used in camera OS.
  /// ko
  ko('KO'),

  /// Language used in camera OS.
  /// zh-CN
  zhCN('ZH_CN'),

  /// Language used in camera OS.
  /// zh-TW
  zhTW('ZH_TW');

  final String rawValue;

  const LanguageEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static LanguageEnum? getValue(String rawValue) {
    return LanguageEnum.values.cast<LanguageEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Network type of the camera supported by Theta X, Z1 and V.
enum NetworkTypeEnum {
  // Direct mode
  direct('DIRECT'),

  // Client mode via WLAN
  client('CLIENT'),

  // Client mode via Ethernet cable supported by Theta Z1 and V.
  ethernet('ETHERNET'),

  // Network is off. This value can be gotten only by plugin.
  off('OFF');

  final String rawValue;

  const NetworkTypeEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static NetworkTypeEnum? getValue(String rawValue) {
    return NetworkTypeEnum.values.cast<NetworkTypeEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// PowerSaving
///
/// For Theta X only
enum PowerSavingEnum {
  /// Power saving mode ON
  on('ON'),

  /// Power saving mode OFF
  off('OFF');

  final String rawValue;

  const PowerSavingEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static PowerSavingEnum? getValue(String rawValue) {
    return PowerSavingEnum.values.cast<PowerSavingEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Preset
enum PresetEnum {
  face('FACE'),
  nightView('NIGHT_VIEW'),
  lensByLensExposure('LENS_BY_LENS_EXPOSURE'),
  room('ROOM');

  final String rawValue;

  const PresetEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static PresetEnum? getValue(String rawValue) {
    return PresetEnum.values.cast<PresetEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Format of live view
enum PreviewFormatEnum {
  /// width_height_framerate
  /// For Theta X, Z1, V and SC2
  // ignore: constant_identifier_names
  w1024_h512_f30('W1024_H512_F30'),

  /// For Theta X. This value can't set.
  // ignore: constant_identifier_names
  w1024_h512_f15('W1024_H512_F15'),

  /// For Theta X
  // ignore: constant_identifier_names
  w512_h512_f30('W512_H512_F30'),

  /// For Theta Z1 and V
  // ignore: constant_identifier_names
  w1920_h960_f8('W1920_H960_F8'),

  /// For Theta Z1 and V
  // ignore: constant_identifier_names
  w1024_h512_f8('W1024_H512_F8'),

  /// For Theta Z1 and V
  // ignore: constant_identifier_names
  w640_h320_f30('W640_H320_F30'),

  /// For Theta Z1 and V
  // ignore: constant_identifier_names
  w640_h320_f8('W640_H320_F8'),

  /// For Theta S and SC
  // ignore: constant_identifier_names
  w640_h320_f10('W640_H320_F10'),

  /// For Theta X
  // ignore: constant_identifier_names
  w3840_h1920_f30('W3840_H1920_F30');

  final String rawValue;

  const PreviewFormatEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static PreviewFormatEnum? getValue(String rawValue) {
    return PreviewFormatEnum.values.cast<PreviewFormatEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Shooting method
///
/// Shooting method for My Settings mode. In RICOH THETA X, it is used outside of MySetting.
/// Can be acquired and set only when in the Still image shooting mode and _function is the My Settings shooting function.
/// Changing _function initializes the setting details to Normal shooting.
enum ShootingMethodEnum {
  /// Normal shooting
  normal('NORMAL'),

  /// Interval shooting
  interval('INTERVAL'),

  /// Move interval shooting (RICOH THETA Z1 firmware v1.50.1 or later, RICOH THETA X is not supported)
  moveInterval('MOVE_INTERVAL'),

  /// Fixed interval shooting (RICOH THETA Z1 firmware v1.50.1 or later, RICOH THETA X is not supported)
  fixedInterval('FIXED_INTERVAL'),

  /// Multi bracket shooting
  bracket('BRACKET'),

  /// Interval composite shooting (RICOH THETA X is not supported)
  composite('COMPOSITE'),

  /// Continuous shooting (RICOH THETA X or later)
  continuous('CONTINUOUS'),

  /// Time shift shooting (RICOH THETA X or later)
  timeShift('TIME_SHIFT'),

  /// Burst shooting (RICOH THETA Z1 v2.10.1 or later, RICOH THETA X is not supported)
  burst('BURST');

  final String rawValue;

  const ShootingMethodEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static ShootingMethodEnum? getValue(String rawValue) {
    return ShootingMethodEnum.values.cast<ShootingMethodEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Shutter speed (sec).
///
///  It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
///  Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
///
///  ### Support value
///  The choice is listed below. There are certain range difference between each models and settings.
///
///  | captureMode | exposureProgram | X or later | V or Z1 | SC | S |
///  | --- | --- | --- | --- | --- | --- |
///  | Still image shooting mode | Manual | 0.0000625 (1/16000) to 60 | 0.00004 (1/25000) to 60 | 0.000125 (1/8000) to 60 | 0.00015625 (1/6400) to 60 |
///  |                           | Shutter priority  | 0.0000625 (1/16000) to 15 | 0.00004 (1/25000) to 0.125 (1/8) | 0.00004 (1/25000) to 15 `*2`  |  |  |
///  | Video shooting mode `*1`    | Manual or Shutter priority | 0.0000625 (1/16000) to 0.03333333 (1/30) | 0.00004 (1/25000) to 0.03333333 (1/30) |  |  |
///  | Otherwise  |  | 0 (AUTO)  | 0 (AUTO)  | 0 (AUTO)  | 0 (AUTO)  |
///
///  `*1` RICOH THETA Z1 and RICOH THETA V firmware v3.00.1 or later
///
///  `*2` RICOH THETA Z1 firmware v1.50.1 or later and RICOH THETA V firmware v3.40.1 or later
enum ShutterSpeedEnum {
  /// Shutter speed. auto
  shutterSpeedAuto('SHUTTER_SPEED_AUTO'),

  /// Shutter speed. 60 sec
  shutterSpeed_60('SHUTTER_SPEED_60'),

  /// Shutter speed. 50 sec
  ///
  /// RICOH THETA Z1 firmware v2.10.1 or later and RICOH THETA V firmware v3.80.1 or later.
  /// For RICOH THETA X, all versions are supported.
  shutterSpeed_50('SHUTTER_SPEED_50'),

  /// Shutter speed. 40 sec
  ///
  /// RICOH THETA Z1 firmware v2.10.1 or later and RICOH THETA V firmware v3.80.1 or later.
  /// For RICOH THETA X, all versions are supported.
  shutterSpeed_40('SHUTTER_SPEED_40'),

  /// Shutter speed. 30 sec
  shutterSpeed_30('SHUTTER_SPEED_30'),

  /// Shutter speed. 25 sec
  shutterSpeed_25('SHUTTER_SPEED_25'),

  /// Shutter speed. 20 sec
  shutterSpeed_20('SHUTTER_SPEED_20'),

  /// Shutter speed. 15 sec
  shutterSpeed_15('SHUTTER_SPEED_15'),

  /// Shutter speed. 13 sec
  shutterSpeed_13('SHUTTER_SPEED_13'),

  /// Shutter speed. 10 sec
  shutterSpeed_10('SHUTTER_SPEED_10'),

  /// Shutter speed. 8 sec
  shutterSpeed_8('SHUTTER_SPEED_8'),

  /// Shutter speed. 6 sec
  shutterSpeed_6('SHUTTER_SPEED_6'),

  /// Shutter speed. 5 sec
  shutterSpeed_5('SHUTTER_SPEED_5'),

  /// Shutter speed. 4 sec
  shutterSpeed_4('SHUTTER_SPEED_4'),

  /// Shutter speed. 3.2 sec
  shutterSpeed_3_2('SHUTTER_SPEED_3_2'),

  /// Shutter speed. 2.5 sec
  shutterSpeed_2_5('SHUTTER_SPEED_2_5'),

  /// Shutter speed. 2 sec
  shutterSpeed_2('SHUTTER_SPEED_2'),

  /// Shutter speed. 1.6 sec
  shutterSpeed_1_6('SHUTTER_SPEED_1_6'),

  /// Shutter speed. 1.3 sec
  shutterSpeed_1_3('SHUTTER_SPEED_1_3'),

  /// Shutter speed. 1 sec
  shutterSpeed_1('SHUTTER_SPEED_1'),

  /// Shutter speed. 1/3 sec(0.76923076)
  shutterSpeedOneOver_1_3('SHUTTER_SPEED_ONE_OVER_1_3'),

  /// Shutter speed. 1/6 sec(0.625)
  shutterSpeedOneOver_1_6('SHUTTER_SPEED_ONE_OVER_1_6'),

  /// Shutter speed. 1/2 sec(0.5)
  shutterSpeedOneOver_2('SHUTTER_SPEED_ONE_OVER_2'),

  /// Shutter speed. 1/2.5 sec(0.4)
  shutterSpeedOneOver_2_5('SHUTTER_SPEED_ONE_OVER_2_5'),

  /// Shutter speed. 1/3 sec(0.33333333)
  shutterSpeedOneOver_3('SHUTTER_SPEED_ONE_OVER_3'),

  /// Shutter speed. 1/4 sec(0.25)
  shutterSpeedOneOver_4('SHUTTER_SPEED_ONE_OVER_4'),

  /// Shutter speed. 1/5 sec(0.2)
  shutterSpeedOneOver_5('SHUTTER_SPEED_ONE_OVER_5'),

  /// Shutter speed. 1/6 sec(0.16666666)
  shutterSpeedOneOver_6('SHUTTER_SPEED_ONE_OVER_6'),

  /// Shutter speed. 1/8 sec(0.125)
  shutterSpeedOneOver_8('SHUTTER_SPEED_ONE_OVER_8'),

  /// Shutter speed. 1/10 sec(0.1)
  shutterSpeedOneOver_10('SHUTTER_SPEED_ONE_OVER_10'),

  /// Shutter speed. 1/13 sec(0.07692307)
  shutterSpeedOneOver_13('SHUTTER_SPEED_ONE_OVER_13'),

  /// Shutter speed. 1/15 sec(0.06666666)
  shutterSpeedOneOver_15('SHUTTER_SPEED_ONE_OVER_15'),

  /// Shutter speed. 1/20 sec(0.05)
  shutterSpeedOneOver_20('SHUTTER_SPEED_ONE_OVER_20'),

  /// Shutter speed. 1/25 sec(0.04)
  shutterSpeedOneOver_25('SHUTTER_SPEED_ONE_OVER_25'),

  /// Shutter speed. 1/30 sec(0.03333333)
  shutterSpeedOneOver_30('SHUTTER_SPEED_ONE_OVER_30'),

  /// Shutter speed. 1/40 sec(0.025)
  shutterSpeedOneOver_40('SHUTTER_SPEED_ONE_OVER_40'),

  /// Shutter speed. 1/50 sec(0.02)
  shutterSpeedOneOver_50('SHUTTER_SPEED_ONE_OVER_50'),

  /// Shutter speed. 1/60 sec(0.01666666)
  shutterSpeedOneOver_60('SHUTTER_SPEED_ONE_OVER_60'),

  /// Shutter speed. 1/80 sec(0.0125)
  shutterSpeedOneOver_80('SHUTTER_SPEED_ONE_OVER_80'),

  /// Shutter speed. 1/100 sec(0.01)
  shutterSpeedOneOver_100('SHUTTER_SPEED_ONE_OVER_100'),

  /// Shutter speed. 1/125 sec(0.008)
  shutterSpeedOneOver_125('SHUTTER_SPEED_ONE_OVER_125'),

  /// Shutter speed. 1/160 sec(0.00625)
  shutterSpeedOneOver_160('SHUTTER_SPEED_ONE_OVER_160'),

  /// Shutter speed. 1/200 sec(0.005)
  shutterSpeedOneOver_200('SHUTTER_SPEED_ONE_OVER_200'),

  /// Shutter speed. 1/250 sec(0.004)
  shutterSpeedOneOver_250('SHUTTER_SPEED_ONE_OVER_250'),

  /// Shutter speed. 1/320 sec(0.003125)
  shutterSpeedOneOver_320('SHUTTER_SPEED_ONE_OVER_320'),

  /// Shutter speed. 1/400 sec(0.0025)
  shutterSpeedOneOver_400('SHUTTER_SPEED_ONE_OVER_400'),

  /// Shutter speed. 1/500 sec(0.002)
  shutterSpeedOneOver_500('SHUTTER_SPEED_ONE_OVER_500'),

  /// Shutter speed. 1/640 sec(0.0015625)
  shutterSpeedOneOver_640('SHUTTER_SPEED_ONE_OVER_640'),

  /// Shutter speed. 1/800 sec(0.00125)
  shutterSpeedOneOver_800('SHUTTER_SPEED_ONE_OVER_800'),

  /// Shutter speed. 1/1000 sec(0.001)
  shutterSpeedOneOver_1000('SHUTTER_SPEED_ONE_OVER_1000'),

  /// Shutter speed. 1/1250 sec(0.0008)
  shutterSpeedOneOver_1250('SHUTTER_SPEED_ONE_OVER_1250'),

  /// Shutter speed. 1/1600 sec(0.000625)
  shutterSpeedOneOver_1600('SHUTTER_SPEED_ONE_OVER_1600'),

  /// Shutter speed. 1/2000 sec(0.0005)
  shutterSpeedOneOver_2000('SHUTTER_SPEED_ONE_OVER_2000'),

  /// Shutter speed. 1/2500 sec(0.0004)
  shutterSpeedOneOver_2500('SHUTTER_SPEED_ONE_OVER_2500'),

  /// Shutter speed. 1/3200 sec(0.0003125)
  shutterSpeedOneOver_3200('SHUTTER_SPEED_ONE_OVER_3200'),

  /// Shutter speed. 1/4000 sec(0.00025)
  shutterSpeedOneOver_4000('SHUTTER_SPEED_ONE_OVER_4000'),

  /// Shutter speed. 1/5000 sec(0.0002)
  shutterSpeedOneOver_5000('SHUTTER_SPEED_ONE_OVER_5000'),

  /// Shutter speed. 1/6400 sec(0.00015625)
  shutterSpeedOneOver_6400('SHUTTER_SPEED_ONE_OVER_6400'),

  /// Shutter speed. 1/8000 sec(0.000125)
  shutterSpeedOneOver_8000('SHUTTER_SPEED_ONE_OVER_8000'),

  /// Shutter speed. 1/10000 sec(0.0001)
  shutterSpeedOneOver_10000('SHUTTER_SPEED_ONE_OVER_10000'),

  /// Shutter speed. 1/12500 sec(0.00008)
  ///
  /// No support for RICOH THETA X.
  shutterSpeedOneOver_12500('SHUTTER_SPEED_ONE_OVER_12500'),

  /// Shutter speed. 1/12800 sec(0.00007812)
  ///
  /// Enabled only for RICOH THETA X.
  shutterSpeedOneOver_12800('SHUTTER_SPEED_ONE_OVER_12800'),

  /// Shutter speed. 1/16000 sec(0.0000625)
  shutterSpeedOneOver_16000('SHUTTER_SPEED_ONE_OVER_16000'),

  /// Shutter speed. 1/20000 sec(0.00005)
  shutterSpeedOneOver_20000('SHUTTER_SPEED_ONE_OVER_20000'),

  /// Shutter speed. 1/25000 sec(0.00004)
  shutterSpeedOneOver_25000('SHUTTER_SPEED_ONE_OVER_25000'),
  ;

  final String rawValue;

  const ShutterSpeedEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static ShutterSpeedEnum? getValue(String rawValue) {
    return ShutterSpeedEnum.values.cast<ShutterSpeedEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// White balance.
///
/// It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
/// Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
enum WhiteBalanceEnum {
  /// White balance.
  /// Automatic
  auto('AUTO'),

  /// White balance.
  /// Outdoor
  daylight('DAYLIGHT'),

  /// White balance.
  /// Shade
  sade('SHADE'),

  /// White balance.
  /// Cloudy
  cloudyDaylight('CLOUDY_DAYLIGHT'),

  /// White balance.
  /// Incandescent light 1
  incandescent('INCANDESCENT'),

  /// White balance.
  /// Incandescent light 2
  warmWhiteFluorescent('WARM_WHITE_FLUORESCENT'),

  /// White balance.
  /// Fluorescent light 1 (daylight)
  daylightFluorescent('DAYLIGHT_FLUORESCENT'),

  /// White balance.
  /// Fluorescent light 2 (natural white)
  daywhiteFluorescent('DAYWHITE_FLUORESCENT'),

  /// White balance.
  /// Fluorescent light 3 (white)
  fluorescent('FLUORESCENT'),

  /// White balance.
  /// Fluorescent light 4 (light bulb color)
  bulbFluorescent('BULB_FLUORESCENT'),

  /// White balance.
  /// CT settings (specified by the _colorTemperature option)
  ///
  /// RICOH THETA S firmware v01.82 or later and RICOH THETA SC firmware v01.10 or later
  colorTemperature('COLOR_TEMPERATURE'),

  /// White balance.
  /// Underwater
  ///
  /// RICOH THETA V firmware v3.21.1 or later
  underwater('UNDERWATER');

  final String rawValue;

  const WhiteBalanceEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static WhiteBalanceEnum? getValue(String rawValue) {
    return WhiteBalanceEnum.values.cast<WhiteBalanceEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Time shift shooting option
class TimeShift {
  /// Shooting order.
  /// true: first shoot the front side (side with Theta logo) then shoot the rear side (side with monitor).
  /// false: first shoot the rear side then shoot the front side.
  /// default is front first.
  bool? isFrontFirst;

  /// Time before 1st lens shooting.
  /// For V or Z1, default is 5 seconds. For X, default is 2 seconds.
  TimeShiftIntervalEnum? firstInterval;

  /// Time from 1st lens shooting until start of 2nd lens shooting.
  /// Default is 5 seconds.
  TimeShiftIntervalEnum? secondInterval;

  TimeShift({this.isFrontFirst, this.firstInterval, this.secondInterval});

  @override
  bool operator ==(Object other) => hashCode == other.hashCode;

  @override
  int get hashCode =>
      Object.hashAll([isFrontFirst, firstInterval, secondInterval]);

  @override
  String toString() {
    return '${isFrontFirst.toString()}, ${firstInterval?.toString()}, ${secondInterval?.toString()}';
  }
}

/// Interval of TimeShit
enum TimeShiftIntervalEnum {
  /// 0 second
  interval_0('INTERVAL_0'),

  /// 1second
  interval_1('INTERVAL_1'),

  /// 2 seconds
  interval_2('INTERVAL_2'),

  /// 3 seconds
  interval_3('INTERVAL_3'),

  /// 4 seconds
  interval_4('INTERVAL_4'),

  /// 5 seconds
  interval_5('INTERVAL_5'),

  /// 6 seconds
  interval_6('INTERVAL_6'),

  /// 7 seconds
  interval_7('INTERVAL_7'),

  /// 8 seconds
  interval_8('INTERVAL_8'),

  /// 9 seconds
  interval_9('INTERVAL_9'),

  /// 10 seconds
  interval_10('INTERVAL_10');

  final String rawValue;

  const TimeShiftIntervalEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static TimeShiftIntervalEnum? getValue(String rawValue) {
    return TimeShiftIntervalEnum.values
        .cast<TimeShiftIntervalEnum?>()
        .firstWhere((element) => element?.rawValue == rawValue,
            orElse: () => null);
  }
}

/// top bottom correction
///
/// Sets the top/bottom correction.  For RICOH THETA V and RICOH
/// THETA Z1, the top/bottom correction can be set only for still
/// images.  For RICOH THETA X, the top/bottom correction can be
/// set for both still images and videos.
enum TopBottomCorrectionOptionEnum {
  /// Top/bottom correction is performed.
  apply('APPLY'),

  /// Refer to top/bottom correction when shooting with "ApplyAuto"
  applyAuto('APPLY_AUTO'),

  /// Top/bottom correction is performed. The parameters used for
  /// top/bottom correction for the first image are saved and used
  /// for the 2nd and subsequent images.(RICOH THETA X or later)
  applySemiauto('APPLY_SEMIAUTO'),

  /// Performs top/bottom correction and then saves the parameters.
  applySave('APPLY_SAVE'),

  /// Performs top/bottom correction using the saved parameters.
  applyLoad('APPLY_LOAD'),

  /// Does not perform top/bottom correction.
  disapply('DISAPPLY'),

  /// Performs the top/bottom correction with the specified front
  /// position. The front position can be specified with
  /// _topBottomCorrectionRotation.
  manual('MANUAL');

  final String rawValue;

  const TopBottomCorrectionOptionEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static TopBottomCorrectionOptionEnum? getValue(String rawValue) {
    return TopBottomCorrectionOptionEnum.values
        .cast<TopBottomCorrectionOptionEnum?>()
        .firstWhere((element) => element?.rawValue == rawValue,
            orElse: () => null);
  }
}

/// Video stitching during shooting.
enum VideoStitchingEnum {
  /// Stitching is OFF
  none('NONE'),

  /// Stitching by the camera is ON
  ondevice('ONDEVICE');

  final String rawValue;

  const VideoStitchingEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static VideoStitchingEnum? getValue(String rawValue) {
    return VideoStitchingEnum.values.cast<VideoStitchingEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Reduction visibility of camera body to still image when stitching.
enum VisibilityReductionEnum {
  /// Reduction is ON.
  on('ON'),

  /// Reduction is OFF.
  off('OFF');

  final String rawValue;

  const VisibilityReductionEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static VisibilityReductionEnum? getValue(String rawValue) {
    return VisibilityReductionEnum.values
        .cast<VisibilityReductionEnum?>()
        .firstWhere((element) => element?.rawValue == rawValue,
            orElse: () => null);
  }
}

/// Sets the front position for the top/bottom correction.
/// Enabled only for _topBottomCorrection Manual.
class TopBottomCorrectionRotation {
  /// Specifies the pitch.
  /// Specified range is -90.0 to +90.0, stepSize is 0.1
  double pitch;

  /// Specifies the roll.
  /// Specified range is -180.0 to +180.0, stepSize is 0.1
  double roll;

  /// Specifies the yaw.
  /// Specified range is -180.0 to +180.0, stepSize is 0.1
  double yaw;

  TopBottomCorrectionRotation(this.pitch, this.roll, this.yaw);

  @override
  bool operator ==(Object other) => hashCode == other.hashCode;

  @override
  int get hashCode => Object.hashAll([pitch, roll, yaw]);
}

/// White balance auto strength.
///
/// To set the strength of white balance auto for low color temperature scene.
/// This option can be set for photo mode and video mode separately.
/// Also this option will not be cleared by power-off.
///
/// For RICOH THETA Z1 firmware v2.20.3 or later
enum WhiteBalanceAutoStrengthEnum {
  /// correct tint for low color temperature scene
  on('ON'),

  /// not correct tint for low color temperature scene
  off('OFF');

  final String rawValue;

  const WhiteBalanceAutoStrengthEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static WhiteBalanceAutoStrengthEnum? getValue(String rawValue) {
    return WhiteBalanceAutoStrengthEnum.values
        .cast<WhiteBalanceAutoStrengthEnum?>()
        .firstWhere((element) => element?.rawValue == rawValue,
            orElse: () => null);
  }
}

/// Wireless LAN frequency of the camera supported by Theta X, Z1 and V.
enum WlanFrequencyEnum {
  /// 2.4GHz
  ghz_2_4('GHZ_2_4'),

  /// 5GHz
  ghz_5('GHZ_5');

  final String rawValue;

  const WlanFrequencyEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static WlanFrequencyEnum? getValue(String rawValue) {
    return WlanFrequencyEnum.values.cast<WlanFrequencyEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Turns position information assigning ON/OFF.
///
/// For RICOH THETA X
enum GpsTagRecordingEnum {
  /// Position information assigning ON.
  on('ON'),

  /// Position information assigning OFF.
  off('OFF');

  final String rawValue;

  const GpsTagRecordingEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }
}

/// GPS information.
/// 65535 is set for latitude and longitude when disabling the GPS setting at
/// RICOH THETA Z1 and prior.
///
/// For RICOH THETA X, ON/OFF for assigning position information is
/// set at [Options.isGpsOn]
class GpsInfo {
  /// Latitude (-90.000000 – 90.000000)
  /// When GPS is disabled: 65535
  double latitude;

  /// Longitude (-180.000000 – 180.000000)
  /// When GPS is disabled: 65535
  double longitude;

  /// Altitude (meters)
  /// When GPS is disabled: 0
  double altitude;

  /// Location information acquisition time
  /// YYYY:MM:DD hh:mm:ss+(-)hh:mm
  /// hh is in 24-hour time, +(-)hh:mm is the time zone
  /// when GPS is disabled: ""(null characters)
  String dateTimeZone;

  GpsInfo(this.latitude, this.longitude, this.altitude, this.dateTimeZone);

  @override
  bool operator ==(Object other) => hashCode == other.hashCode;

  @override
  int get hashCode =>
      Object.hashAll([latitude, longitude, altitude, dateTimeZone]);
}

/// Proxy information to be used when wired LAN is enabled.
///
/// The current setting can be acquired by camera.getOptions,
/// and it can be changed by camera.setOptions.
///
/// For
/// RICOH THETA Z1 firmware v2.20.3 or later
/// RICOH THETA X firmware v2.00.0 or later
class Proxy {
  /// true: use proxy false: do not use proxy
  bool use;

  /// Proxy server URL
  String? url;

  /// Proxy server port number: 0 to 65535
  int? port;

  /// User ID used for proxy authentication
  String? userid;

  /// Password used for proxy authentication
  String? password;

  Proxy(this.use, [this.url, this.port, this.userid, this.password]);

  @override
  bool operator ==(Object other) => hashCode == other.hashCode;

  @override
  int get hashCode => Object.hashAll([use, url, port, userid, password]);
}

/// Camera setting options.
///
/// Refer to the [options category](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/options.md)
class Options {
  /// AI auto thumbnail setting.
  AiAutoThumbnailEnum? aiAutoThumbnail;

  /// Aperture value.
  ApertureEnum? aperture;

  /// Multi bracket shooting setting.
  List<BracketSetting>? autoBracket;

  /// see [Bitrate]
  Bitrate? bitrate;

  /// see [BluetoothPowerEnum]
  BluetoothPowerEnum? bluetoothPower;

  /// Role of the Bluetooth module.
  BluetoothRoleEnum? bluetoothRole;

  /// BurstMode setting.
  /// When this is set to ON, burst shooting is enabled,
  /// and a screen dedicated to burst shooting is displayed in Live View.
  BurstModeEnum? burstMode;

  /// Burst shooting setting.
  BurstOption? burstOption;

  /// camera control source
  /// Sets whether to lock/unlock the camera UI.
  /// The current setting can be acquired by camera.getOptions, and it can be changed by camera.setOptions.
  ///
  /// For RICOH THETA X
  CameraControlSourceEnum? cameraControlSource;

  /// Camera mode.
  /// The current setting can be acquired by camera.getOptions, and it can be changed by camera.setOptions.
  ///
  /// For RICOH THETA X
  CameraModeEnum? cameraMode;

  /// see [CameraPowerEnum]
  CameraPowerEnum? cameraPower;

  /// Shooting interval (sec.) for interval shooting.
  ///
  /// ### Support value
  /// The value that can be set differs depending on the image format ([fileFormat]) to be shot.
  /// #### For RICOH THETA X or later
  /// | Image format | Image size  | Support value |
  /// | ------------ | ----------- | ------------- |
  /// | JPEG         | 11008 x 5504 <br>5504 x 2752 | Minimum value(minInterval):6 <br>Maximum value(maxInterval):3600 |
  ///
  /// #### For RICOH THETA Z1
  /// | Image format | Image size  | Support value |
  /// | ------------ | ----------- | ------------- |
  /// | JPEG         | 6720 x 3360 | Minimum value(minInterval):6 <br>Maximum value(maxInterval):3600 |
  /// | RAW+         | 6720 x 3360 | Minimum value(minInterval):10 <br>Maximum value(maxInterval):3600 |
  ///
  /// #### For RICOH THETA V
  /// | Image format | Image size  | Support value |
  /// | ------------ | ----------- | ------------- |
  /// | JPEG         | 5376 x 2688 | Minimum value(minInterval):4 <br>Maximum value(maxInterval):3600 |
  ///
  /// #### For RICOH THETA S or SC
  /// | Image format | Image size  | Support value |
  /// | ------------ | ----------- | ------------- |
  /// | JPEG         | 5376 x 2688 | Minimum value(minInterval):8 <br>Maximum value(maxInterval):3600 |
  /// | JPEG         | 2048 x 1024 | Minimum value(minInterval):5 <br>Maximum value(maxInterval):3600 |
  int? captureInterval;

  /// Shooting mode.
  CaptureModeEnum? captureMode;

  /// Number of shots for interval shooting.
  ///
  /// ### Support value
  /// - 0: Unlimited (_limitless)
  /// - 2: Minimum value (minNumber)
  /// - 9999: Maximum value (maxNumber)
  int? captureNumber;

  /// Color temperature of the camera (Kelvin).
  ///
  /// It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
  /// Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
  ///
  /// Support value
  /// 2500 to 10000. In 100-Kelvin units.
  int? colorTemperature;

  /// In-progress save interval for interval composite shooting (sec).
  ///
  /// 0 (no saving), 60 to 600. In 60-second units.
  ///
  /// For
  /// RICOH THETA Z1
  /// RICOH THETA SC firmware v1.10 or later
  /// RICOH THETA S firmware v01.82 or later
  int? compositeShootingOutputInterval;

  /// Shooting time for interval composite shooting (sec).
  ///
  /// 600 to 86400. In 600-second units.
  ///
  /// For
  /// RICOH THETA Z1
  /// RICOH THETA SC firmware v1.10 or later
  /// RICOH THETA S firmware v01.82 or later
  int? compositeShootingTime;

  /// see [ContinuousNumberEnum]
  ContinuousNumberEnum? continuousNumber;

  /// Current system time of RICOH THETA. Setting another options will result in an error.
  ///
  /// With RICOH THETA X camera.setOptions can be changed only when Date/time setting is AUTO in menu UI.
  ///
  /// Time format
  /// YYYY:MM:DD hh:mm:ss+(-)hh:mm
  /// hh is in 24-hour time, +(-)hh:mm is the time zone.
  /// e.g. 2014:05:18 01:04:29+08:00
  String? dateTimeZone;

  /// see [EthernetConfig]
  EthernetConfig? ethernetConfig;

  /// Exposure compensation (EV).
  ///
  /// It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
  /// Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
  ExposureCompensationEnum? exposureCompensation;

  /// Operating time (sec.) of the self-timer.
  ///
  /// If exposureDelay is enabled, self-timer is used by shooting.
  /// If exposureDelay is disabled, use _latestEnabledExposureDelayTime to
  /// get the operating time of the self-timer stored in the camera.
  ExposureDelayEnum? exposureDelay;

  /// Exposure program. The exposure settings that take priority can be selected.
  ///
  /// It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
  /// Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
  ExposureProgramEnum? exposureProgram;

  /// see [FaceDetectEnum]
  FaceDetectEnum? faceDetect;

  /// Image format used in shooting.
  ///
  /// The supported value depends on the shooting mode [captureMode].
  FileFormatEnum? fileFormat;

  /// Image processing filter.
  ///
  /// Configured the filter will be applied while in still image shooting mode.
  /// However, it is disabled during interval shooting, interval composite group shooting,
  /// multi bracket shooting or continuous shooting.
  ///
  /// When filter is enabled, it takes priority over the exposure program [exposureProgram].
  /// Also, when filter is enabled, the exposure program is set to the Normal program.
  ///
  /// The condition below will result in an error.
  /// [fileFormat] is raw+ and _filter is Noise reduction, HDR or Handheld HDR
  /// shootingMethod is except for Normal shooting and [filter] is enabled
  /// Access during video capture mode
  FilterEnum? filter;

  /// see [ShootingFunctionEnum]
  ShootingFunctionEnum? function;

  /// see [GainEnum]
  GainEnum? gain;

  /// GPS location information.
  ///
  /// In order to append the location information, this property should be specified by the client.
  GpsInfo? gpsInfo;

  /// Still image stitching setting during shooting.
  ImageStitchingEnum? imageStitching;

  /// Turns position information assigning ON/OFF.
  /// For THETA X
  bool? isGpsOn;

  /// Turns position information assigning ON/OFF.
  ///
  /// It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
  /// Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
  ///
  /// When the exposure program [exposureProgram] is set to Manual or ISO Priority
  IsoEnum? iso;

  /// ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  IsoAutoHighLimitEnum? isoAutoHighLimit;

  /// Language used in camera OS.
  LanguageEnum? language;

  ExposureDelayEnum? latestEnabledExposureDelayTime;

  /// Maximum recordable time (in seconds) of the camera.
  MaxRecordableTimeEnum? maxRecordableTime;

  /// Network type of the camera supported by Theta X, Z1 and V.
  NetworkTypeEnum? networkType;

  /// Length of standby time before the camera automatically powers OFF.
  ///
  /// Specify [OffDelayEnum]
  OffDelayEnum? offDelay;

  /// Password used for digest authentication when _networkType is set to client mode.
  String? password;

  /// PowerSaving
  PowerSavingEnum? powerSaving;

  /// Preset
  PresetEnum? preset;

  /// PreviewFormat
  PreviewFormatEnum? previewFormat;

  /// see [Proxy]
  Proxy? proxy;

  /// The estimated remaining number of shots for the current shooting settings.
  int? remainingPictures;

  /// The estimated remaining shooting time (sec.) for the current video shooting settings.
  int? remainingVideoSeconds;

  /// Remaining usable storage space (byte).
  int? remainingSpace;

  /// ShootingMethod
  ShootingMethodEnum? shootingMethod;

  /// Shutter speed (sec).
  ///
  /// It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
  /// Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
  ShutterSpeedEnum? shutterSpeed;

  /// Shutter volume.
  ///
  /// Support value
  /// 0: Minimum volume (minShutterVolume)
  /// 100: Maximum volume (maxShutterVolume)
  int? shutterVolume;

  /// Length of standby time before the camera enters the sleep mode.
  SleepDelayEnum? sleepDelay;

  /// TimeShift
  TimeShift? timeShift;

  /// see [TopBottomCorrectionOptionEnum]
  TopBottomCorrectionOptionEnum? topBottomCorrection;

  /// see [TopBottomCorrectionRotation]
  TopBottomCorrectionRotation? topBottomCorrectionRotation;

  /// Total storage space (byte).
  int? totalSpace;

  /// User name used for digest authentication when _networkType is set to client mode.
  String? username;

  VideoStitchingEnum? videoStitching;

  VisibilityReductionEnum? visibilityReduction;

  /// White balance.
  ///
  /// It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
  /// Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
  WhiteBalanceEnum? whiteBalance;

  /// White balance auto strength.
  ///
  /// To set the strength of white balance auto for low color temperature scene.
  /// This option can be set for photo mode and video mode separately.
  /// Also this option will not be cleared by power-off.
  ///
  /// For RICOH THETA Z1 firmware v2.20.3 or later
  WhiteBalanceAutoStrengthEnum? whiteBalanceAutoStrength;

  /// Wireless LAN frequency of the camera supported by Theta X, Z1 and V.
  WlanFrequencyEnum? wlanFrequency;

  /// Get Option value.
  T? getValue<T>(OptionNameEnum name) {
    switch (name) {
      case OptionNameEnum.aiAutoThumbnail:
        return aiAutoThumbnail as T;
      case OptionNameEnum.aperture:
        return aperture as T;
      case OptionNameEnum.autoBracket:
        return autoBracket as T;
      case OptionNameEnum.bitrate:
        return bitrate as T;
      case OptionNameEnum.bluetoothPower:
        return bluetoothPower as T;
      case OptionNameEnum.bluetoothRole:
        return bluetoothRole as T;
      case OptionNameEnum.burstMode:
        return burstMode as T;
      case OptionNameEnum.burstOption:
        return burstOption as T;
      case OptionNameEnum.cameraControlSource:
        return cameraControlSource as T;
      case OptionNameEnum.cameraMode:
        return cameraMode as T;
      case OptionNameEnum.cameraPower:
        return cameraPower as T;
      case OptionNameEnum.captureInterval:
        return captureInterval as T;
      case OptionNameEnum.captureMode:
        return captureMode as T;
      case OptionNameEnum.captureNumber:
        return captureNumber as T;
      case OptionNameEnum.colorTemperature:
        return colorTemperature as T;
      case OptionNameEnum.compositeShootingOutputInterval:
        return compositeShootingOutputInterval as T;
      case OptionNameEnum.compositeShootingTime:
        return compositeShootingTime as T;
      case OptionNameEnum.continuousNumber:
        return continuousNumber as T;
      case OptionNameEnum.dateTimeZone:
        return dateTimeZone as T;
      case OptionNameEnum.ethernetConfig:
        return ethernetConfig as T;
      case OptionNameEnum.exposureCompensation:
        return exposureCompensation as T;
      case OptionNameEnum.exposureDelay:
        return exposureDelay as T;
      case OptionNameEnum.exposureProgram:
        return exposureProgram as T;
      case OptionNameEnum.faceDetect:
        return faceDetect as T;
      case OptionNameEnum.fileFormat:
        return fileFormat as T;
      case OptionNameEnum.filter:
        return filter as T;
      case OptionNameEnum.function:
        return function as T;
      case OptionNameEnum.gain:
        return gain as T;
      case OptionNameEnum.gpsInfo:
        return gpsInfo as T;
      case OptionNameEnum.imageStitching:
        return imageStitching as T;
      case OptionNameEnum.isGpsOn:
        return isGpsOn as T;
      case OptionNameEnum.iso:
        return iso as T;
      case OptionNameEnum.isoAutoHighLimit:
        return isoAutoHighLimit as T;
      case OptionNameEnum.language:
        return language as T;
      case OptionNameEnum.latestEnabledExposureDelayTime:
        return latestEnabledExposureDelayTime as T;
      case OptionNameEnum.maxRecordableTime:
        return maxRecordableTime as T;
      case OptionNameEnum.networkType:
        return networkType as T;
      case OptionNameEnum.offDelay:
        return offDelay as T;
      case OptionNameEnum.password:
        return password as T;
      case OptionNameEnum.powerSaving:
        return powerSaving as T;
      case OptionNameEnum.preset:
        return preset as T;
      case OptionNameEnum.previewFormat:
        return previewFormat as T;
      case OptionNameEnum.proxy:
        return proxy as T;
      case OptionNameEnum.remainingPictures:
        return remainingPictures as T;
      case OptionNameEnum.remainingVideoSeconds:
        return remainingVideoSeconds as T;
      case OptionNameEnum.remainingSpace:
        return remainingSpace as T;
      case OptionNameEnum.shootingMethod:
        return shootingMethod as T;
      case OptionNameEnum.shutterSpeed:
        return shutterSpeed as T;
      case OptionNameEnum.shutterVolume:
        return shutterVolume as T;
      case OptionNameEnum.sleepDelay:
        return sleepDelay as T;
      case OptionNameEnum.timeShift:
        return timeShift as T;
      case OptionNameEnum.topBottomCorrection:
        return topBottomCorrection as T;
      case OptionNameEnum.topBottomCorrectionRotation:
        return topBottomCorrectionRotation as T;
      case OptionNameEnum.totalSpace:
        return totalSpace as T;
      case OptionNameEnum.username:
        return username as T;
      case OptionNameEnum.videoStitching:
        return videoStitching as T;
      case OptionNameEnum.visibilityReduction:
        return visibilityReduction as T;
      case OptionNameEnum.whiteBalance:
        return whiteBalance as T;
      case OptionNameEnum.whiteBalanceAutoStrength:
        return whiteBalanceAutoStrength as T;
      case OptionNameEnum.wlanFrequency:
        return wlanFrequency as T;
    }
  }

  /// Set option value.
  setValue(OptionNameEnum name, dynamic value) {
    if (name.valueType != value.runtimeType) {
      throw Exception('Invalid value type');
    }

    switch (name) {
      case OptionNameEnum.aiAutoThumbnail:
        aiAutoThumbnail = value;
        break;
      case OptionNameEnum.aperture:
        aperture = value;
        break;
      case OptionNameEnum.autoBracket:
        autoBracket = value;
        break;
      case OptionNameEnum.bitrate:
        bitrate = value;
        break;
      case OptionNameEnum.bluetoothPower:
        bluetoothPower = value;
        break;
      case OptionNameEnum.bluetoothRole:
        bluetoothRole = value;
        break;
      case OptionNameEnum.burstMode:
        burstMode = value;
        break;
      case OptionNameEnum.burstOption:
        burstOption = value;
        break;
      case OptionNameEnum.cameraControlSource:
        cameraControlSource = value;
        break;
      case OptionNameEnum.cameraMode:
        cameraMode = value;
        break;
      case OptionNameEnum.cameraPower:
        cameraPower = value;
        break;
      case OptionNameEnum.captureInterval:
        captureInterval = value;
        break;
      case OptionNameEnum.captureMode:
        captureMode = value;
        break;
      case OptionNameEnum.captureNumber:
        captureNumber = value;
        break;
      case OptionNameEnum.colorTemperature:
        colorTemperature = value;
        break;
      case OptionNameEnum.compositeShootingOutputInterval:
        compositeShootingOutputInterval = value;
        break;
      case OptionNameEnum.compositeShootingTime:
        compositeShootingTime = value;
        break;
      case OptionNameEnum.continuousNumber:
        continuousNumber = value;
        break;
      case OptionNameEnum.dateTimeZone:
        dateTimeZone = value;
        break;
      case OptionNameEnum.ethernetConfig:
        ethernetConfig = value;
        break;
      case OptionNameEnum.exposureCompensation:
        exposureCompensation = value;
        break;
      case OptionNameEnum.exposureDelay:
        exposureDelay = value;
        break;
      case OptionNameEnum.exposureProgram:
        exposureProgram = value;
        break;
      case OptionNameEnum.faceDetect:
        faceDetect = value;
        break;
      case OptionNameEnum.fileFormat:
        fileFormat = value;
        break;
      case OptionNameEnum.filter:
        filter = value;
        break;
      case OptionNameEnum.function:
        function = value;
        break;
      case OptionNameEnum.gain:
        gain = value;
        break;
      case OptionNameEnum.gpsInfo:
        gpsInfo = value;
        break;
      case OptionNameEnum.imageStitching:
        imageStitching = value;
        break;
      case OptionNameEnum.isGpsOn:
        isGpsOn = value;
        break;
      case OptionNameEnum.iso:
        iso = value;
        break;
      case OptionNameEnum.isoAutoHighLimit:
        isoAutoHighLimit = value;
        break;
      case OptionNameEnum.language:
        language = value;
        break;
      case OptionNameEnum.latestEnabledExposureDelayTime:
        latestEnabledExposureDelayTime = value;
        break;
      case OptionNameEnum.maxRecordableTime:
        maxRecordableTime = value;
        break;
      case OptionNameEnum.networkType:
        networkType = value;
        break;
      case OptionNameEnum.offDelay:
        offDelay = value;
        break;
      case OptionNameEnum.password:
        password = value;
        break;
      case OptionNameEnum.powerSaving:
        powerSaving = value;
        break;
      case OptionNameEnum.preset:
        preset = value;
        break;
      case OptionNameEnum.previewFormat:
        previewFormat = value;
        break;
      case OptionNameEnum.proxy:
        proxy = value;
        break;
      case OptionNameEnum.remainingPictures:
        remainingPictures = value;
        break;
      case OptionNameEnum.remainingVideoSeconds:
        remainingVideoSeconds = value;
        break;
      case OptionNameEnum.remainingSpace:
        remainingSpace = value;
        break;
      case OptionNameEnum.shootingMethod:
        shootingMethod = value;
        break;
      case OptionNameEnum.shutterSpeed:
        shutterSpeed = value;
        break;
      case OptionNameEnum.shutterVolume:
        shutterVolume = value;
        break;
      case OptionNameEnum.sleepDelay:
        sleepDelay = value;
        break;
      case OptionNameEnum.timeShift:
        timeShift = value;
        break;
      case OptionNameEnum.topBottomCorrection:
        topBottomCorrection = value;
        break;
      case OptionNameEnum.topBottomCorrectionRotation:
        topBottomCorrectionRotation = value;
        break;
      case OptionNameEnum.totalSpace:
        totalSpace = value;
        break;
      case OptionNameEnum.username:
        username = value;
        break;
      case OptionNameEnum.videoStitching:
        videoStitching = value;
        break;
      case OptionNameEnum.visibilityReduction:
        visibilityReduction = value;
        break;
      case OptionNameEnum.whiteBalance:
        whiteBalance = value;
        break;
      case OptionNameEnum.whiteBalanceAutoStrength:
        whiteBalanceAutoStrength = value;
        break;
      case OptionNameEnum.wlanFrequency:
        wlanFrequency = value;
        break;
    }
  }
}

/// Configuration of THETA
class ThetaConfig {
  /// Location information acquisition time
  String? dateTime;

  /// Language used in camera OS
  LanguageEnum? language;

  /// Length of standby time before the camera automatically power OFF
  OffDelayEnum? offDelay;

  /// Length of standby time before the camera enters the sleep mode.
  SleepDelayEnum? sleepDelay;

  /// Shutter volume.
  int? shutterVolume;

  /// Authentication information used for client mode connections
  DigestAuth? clientMode;
}

/// Timeout of HTTP call.
class ThetaTimeout {
  /// Specifies a time period (in milliseconds) in
  /// which a client should establish a connection with a server.
  int connectTimeout = 20000;

  /// Specifies a time period (in milliseconds) required to process an HTTP call:
  /// from sending a request to receiving first response bytes.
  /// To disable this timeout, set its value to 0.
  int requestTimeout = 20000;

  /// Specifies a maximum time (in milliseconds) of inactivity between two data packets
  /// when exchanging data with a server.
  int socketTimeout = 20000;
}

/// Plugin information
class PluginInfo {
  /// Plugin name
  String name;

  /// Package name
  String packageName;

  /// Plugin version
  String version;

  /// Pre-installed plugin or not
  bool isPreInstalled;

  /// Plugin power status
  bool isRunning;

  /// Process status
  bool isForeground;

  /// To be started on boot or not
  bool isBoot;

  /// Has Web UI or not
  bool hasWebServer;

  /// Exit status
  String exitStatus;

  /// Message
  String message;

  PluginInfo(
      this.name,
      this.packageName,
      this.version,
      this.isPreInstalled,
      this.isRunning,
      this.isForeground,
      this.isBoot,
      this.hasWebServer,
      this.exitStatus,
      this.message);
}
