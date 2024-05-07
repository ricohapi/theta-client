import '../theta_client_flutter.dart';
import 'state_gps_info.dart';

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
