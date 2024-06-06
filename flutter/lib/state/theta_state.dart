import '../theta_client_flutter.dart';

/// Mutable values representing Theta status.
class ThetaState {
  /// Fingerprint (unique identifier) of the current camera state
  String? fingerprint;

  /// Battery level between 0.0 and 1.0
  double? batteryLevel;

  /// Storage URI
  String? storageUri;

  /// Storage ID
  String? storageID;

  /// Continuously shoots state
  CaptureStatusEnum? captureStatus;

  /// Recorded time of movie (seconds)
  int? recordedTime;

  /// Recordable time of movie (seconds)
  int? recordableTime;

  /// Number of still images captured during continuous shooting, Unit: images
  int? capturedPictures;

  /// Elapsed time for interval composite shooting (sec)
  int? compositeShootingElapsedTime;

  /// URL of the last saved file
  String? latestFileUrl;

  /// Charging state
  ChargingStateEnum? chargingState;

  /// API version currently set (1: v2.0, 2: v2.1)
  int? apiVersion;

  /// Plugin running state (true: running, false: stop)
  bool? isPluginRunning;

  /// Plugin web server state (true: enabled, false: disabled)
  bool? isPluginWebServer;

  /// Shooting function status
  ShootingFunctionEnum? function;

  /// My setting changed state
  bool? isMySettingChanged;

  /// Identifies the microphone used while recording video
  MicrophoneOptionEnum? currentMicrophone;

  /// True if record to SD card
  bool? isSdCard;

  /// Error information of the camera
  List<CameraErrorEnum>? cameraError;

  /// true: Battery inserted; false: Battery not inserted
  bool? isBatteryInsert;

  /// Location data is obtained through an external device using WebAPI or BLE-API.
  StateGpsInfo? externalGpsInfo;

  /// Location data is obtained through an internal GPS module. RICOH THETA Z1 does not have a built-in GPS module.
  StateGpsInfo? internalGpsInfo;

  /// This represents the current temperature inside the camera as an integer value, ranging from -10°C to 100°C with a precision of 1°C.
  int? boardTemp;

  /// This represents the current temperature inside the battery as an integer value, ranging from -10°C to 100°C with a precision of 1°C.
  int? batteryTemp;

  ThetaState(this.fingerprint,
      this.batteryLevel,
      this.storageUri,
      this.storageID,
      this.captureStatus,
      this.recordedTime,
      this.recordableTime,
      this.capturedPictures,
      this.compositeShootingElapsedTime,
      this.latestFileUrl,
      this.chargingState,
      this.apiVersion,
      this.isPluginRunning,
      this.isPluginWebServer,
      this.function,
      this.isMySettingChanged,
      this.currentMicrophone,
      this.isSdCard,
      this.cameraError,
      this.isBatteryInsert,
      this.externalGpsInfo,
      this.internalGpsInfo,
      this.boardTemp,
      this.batteryTemp);
}

/// Battery charging state
enum ChargingStateEnum {
  /// Battery charging state. Charging
  charging('CHARGING'),

  /// Battery charging state. Charging completed
  completed('COMPLETED'),

  /// Battery charging state. Not charging
  notCharging('NOT_CHARGING');

  final String rawValue;

  const ChargingStateEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static ChargingStateEnum? getValue(String rawValue) {
    return ChargingStateEnum.values.cast<ChargingStateEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Shooting function.
/// Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
/// Setting them at the same time as exposureDelay will result in an error.
///
/// For
/// - RICOH THETA X
/// - RICOH THETA Z1
enum ShootingFunctionEnum {
  /// Normal shooting function
  normal('NORMAL'),

  /// Self-timer shooting function(RICOH THETA X is not supported.)
  selfTimer('SELF_TIMER'),

  /// My setting shooting function
  mySetting('MY_SETTING');

  final String rawValue;

  const ShootingFunctionEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static ShootingFunctionEnum? getValue(String rawValue) {
    return ShootingFunctionEnum.values.cast<ShootingFunctionEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Microphone option
enum MicrophoneOptionEnum {
  /// Microphone option. auto
  auto('AUTO'),

  /// Microphone option. built-in microphone
  internal('INTERNAL'),

  /// Microphone option. external microphone
  external('EXTERNAL');

  final String rawValue;

  const MicrophoneOptionEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static MicrophoneOptionEnum? getValue(String rawValue) {
    return MicrophoneOptionEnum.values.cast<MicrophoneOptionEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Camera error
enum CameraErrorEnum {
  /// Camera error
  /// Undefined value
  unknown('UNKNOWN'),

  /// Camera error
  /// Insufficient memory
  noMemory('NO_MEMORY'),

  /// Camera error
  /// Maximum file number exceeded
  fileNumberOver('FILE_NUMBER_OVER'),

  /// Camera error
  /// Camera clock not set
  noDateSetting('NO_DATE_SETTING'),

  /// Camera error
  /// Includes when the card is removed
  readError('READ_ERROR'),

  /// Camera error
  /// Unsupported media (SDHC, etc.)
  notSupportedMediaType('NOT_SUPPORTED_MEDIA_TYPE'),

  /// Camera error
  /// FAT32, etc.
  notSupportedFileSystem('NOT_SUPPORTED_FILE_SYSTEM'),

  /// Camera error
  /// Error warning while mounting
  mediaNotReady('MEDIA_NOT_READY'),

  /// Camera error
  /// Battery level warning (firmware update)
  notEnoughBattery('NOT_ENOUGH_BATTERY'),

  /// Camera error
  /// Firmware file mismatch warning
  invalidFile('INVALID_FILE'),

  /// Camera error
  /// Plugin start warning (IoT technical standards compliance)
  pluginBootError('PLUGIN_BOOT_ERROR'),

  /// Camera error
  /// When performing continuous shooting by operating the camera while executing <Delete object>,
  /// <Transfer firmware file>, <Install plugin> or <Uninstall plugin> with the WebAPI or MTP.
  inProgressError('IN_PROGRESS_ERROR'),

  /// Camera error
  /// Battery inserted + WLAN ON + Video mode + 4K 60fps / 5.7K 10fps / 5.7K 15fps / 5.7K 30fps / 8K 10fps
  cannotRecording('CANNOT_RECORDING'),

  /// Camera error
  /// Battery inserted AND Specified battery level or lower + WLAN ON + Video mode + 4K 30fps
  cannotRecordLowbat('CANNOT_RECORD_LOWBAT'),

  /// Camera error
  /// Shooting hardware failure
  captureHwFailed('CAPTURE_HW_FAILED'),

  /// Camera error
  /// Software error
  captureSwFailed('CAPTURE_SW_FAILED'),

  /// Camera error
  /// Internal memory access error
  internalMemAccessFail('INTERNAL_MEM_ACCESS_FAIL'),

  /// Camera error
  /// Undefined error
  unexpectedError('UNEXPECTED_ERROR'),

  /// Camera error
  /// Charging error
  batteryChargeFail('BATTERY_CHARGE_FAIL'),

  /// Camera error
  /// (Board) temperature warning
  highTemperatureWarning('HIGH_TEMPERATURE_WARNING'),

  /// Camera error
  /// (Board) temperature error
  highTemperature('HIGH_TEMPERATURE'),

  /// Camera error
  /// Battery temperature error
  batteryHighTemperature('BATTERY_HIGH_TEMPERATURE'),

  /// Camera error
  /// Electronic compass error
  compassCalibration('COMPASS_CALIBRATION');

  final String rawValue;

  const CameraErrorEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static CameraErrorEnum? getValue(String rawValue) {
    return CameraErrorEnum.values.cast<CameraErrorEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}
