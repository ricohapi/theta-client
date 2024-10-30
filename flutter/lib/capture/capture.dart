import '../theta_client_flutter.dart';
import '../theta_client_flutter_platform_interface.dart';
import '../utils/convert_utils.dart';

/// Capture
class Capture {
  /// options of capture
  final Map<String, dynamic> _options;

  /// Get aperture value.
  ApertureEnum? getAperture() => _options[OptionNameEnum.aperture.rawValue];

  /// Get color temperature of the camera (Kelvin).
  int? getColorTemperature() =>
      _options[OptionNameEnum.colorTemperature.rawValue];

  /// Get exposure compensation (EV).
  ExposureCompensationEnum? getExposureCompensation() =>
      _options[OptionNameEnum.exposureCompensation.rawValue];

  /// Get operating time (sec.) of the self-timer.
  ExposureDelayEnum? getExposureDelay() =>
      _options[OptionNameEnum.exposureDelay.rawValue];

  /// Get exposure program.
  ExposureProgramEnum? getExposureProgram() =>
      _options[OptionNameEnum.exposureProgram.rawValue];

  /// Get GPS information.
  GpsInfo? getGpsInfo() => _options[OptionNameEnum.gpsInfo.rawValue];

  /// Get turns position information assigning ON/OFF.
  GpsTagRecordingEnum? getGpsTagRecording() =>
      _options[TagNameEnum.gpsTagRecording.rawValue];

  /// Set ISO sensitivity.
  ///
  /// It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
  /// Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
  ///
  /// When the exposure program (exposureProgram) is set to Manual or ISO Priority
  IsoEnum? getIso() => _options[OptionNameEnum.iso.rawValue];

  /// Get ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  IsoAutoHighLimitEnum? getIsoAutoHighLimit() =>
      _options[OptionNameEnum.isoAutoHighLimit.rawValue];

  /// Get white balance.
  WhiteBalanceEnum? getWhiteBalance() =>
      _options[OptionNameEnum.whiteBalance.rawValue];

  Capture(this._options);
}

/// Capturing status
enum CapturingStatusEnum {
  /// Capture in progress
  capturing('CAPTURING'),

  /// Self-timer in progress
  selfTimerCountdown('SELF_TIMER_COUNTDOWN'),
  ;

  final String rawValue;

  const CapturingStatusEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }

  static CapturingStatusEnum? getValue(String rawValue) {
    return CapturingStatusEnum.values.cast<CapturingStatusEnum?>().firstWhere(
        (element) => element?.rawValue == rawValue,
        orElse: () => null);
  }
}

/// Common PhotoCapture class
class PhotoCaptureBase extends Capture {
  PhotoCaptureBase(super.options);

  /// Get photo file format.
  PhotoFileFormatEnum? getFileFormat() {
    return _options[TagNameEnum.photoFileFormat.rawValue];
  }
}

/// Capture of Photo
class PhotoCapture extends PhotoCaptureBase {
  final int _interval;

  PhotoCapture(super.options, this._interval);

  int getCheckStatusCommandInterval() {
    return _interval;
  }

  /// Get image processing filter.
  FilterEnum? getFilter() {
    return _options[OptionNameEnum.filter.rawValue];
  }

  /// Get preset mode of Theta SC2 and Theta SC2 for business.
  PresetEnum? getPreset() {
    return _options[OptionNameEnum.preset.rawValue];
  }

  /// Take a picture.
  void takePicture(void Function(String? fileUrl) onSuccess,
      void Function(Exception exception) onError,
      {void Function(CapturingStatusEnum status)? onCapturing}) {
    ThetaClientFlutterPlatform.instance
        .takePicture(onCapturing)
        .then((value) => onSuccess(value!))
        .onError((error, stackTrace) => onError(error as Exception));
  }
}

/// Capture of TimeShift
class TimeShiftCapture extends Capture {
  final int _interval;

  TimeShiftCapture(super.options, this._interval);

  int getCheckStatusCommandInterval() {
    return _interval;
  }

  TimeShift? getTimeShiftSetting() {
    return _options[OptionNameEnum.timeShift.rawValue];
  }

  /// Starts TimeShift capture.
  TimeShiftCapturing startCapture(
      void Function(String? fileUrl) onCaptureCompleted,
      void Function(double completion) onProgress,
      void Function(Exception exception) onCaptureFailed,
      {void Function(Exception exception)? onStopFailed,
        void Function(CapturingStatusEnum status)? onCapturing}) {
    ThetaClientFlutterPlatform.instance
        .startTimeShiftCapture(onProgress, onStopFailed, onCapturing)
        .then((value) => onCaptureCompleted(value))
        .onError((error, stackTrace) => onCaptureFailed(error as Exception));
    return TimeShiftCapturing();
  }
}

/// Capture of Video
class VideoCapture extends Capture {
  final int _interval;

  VideoCapture(super.options, this._interval);

  int getCheckStatusCommandInterval() {
    return _interval;
  }

  /// Get maximum recordable time (in seconds) of the camera.
  MaxRecordableTimeEnum? getMaxRecordableTime() {
    return _options[OptionNameEnum.maxRecordableTime.rawValue];
  }

  /// Get video file format.
  VideoFileFormatEnum? getFileFormat() {
    return _options[TagNameEnum.videoFileFormat.rawValue];
  }

  /// Starts video capture.
  VideoCapturing startCapture(void Function(String? fileUrl) onCaptureCompleted,
      void Function(Exception exception) onCaptureFailed,
      {void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing,
      void Function(String? fileUrl)? onCaptureStarted}) {
    ThetaClientFlutterPlatform.instance
        .startVideoCapture(onStopFailed, onCapturing, onCaptureStarted)
        .then((value) => onCaptureCompleted(value))
        .onError((error, stackTrace) => onCaptureFailed(error as Exception));
    return VideoCapturing();
  }
}

/// Capture of limitless interval
class LimitlessIntervalCapture extends Capture {
  final int _interval;

  LimitlessIntervalCapture(super.options, this._interval);

  int getCheckStatusCommandInterval() {
    return _interval;
  }

  /// Get shooting interval (sec.) for interval shooting.
  int? getCaptureInterval() =>
      _options[OptionNameEnum.captureInterval.rawValue];

  /// Starts limitless interval capture.
  LimitlessIntervalCapturing startCapture(
      void Function(List<String>? fileUrls) onCaptureCompleted,
      void Function(Exception exception) onCaptureFailed,
      {void Function(Exception exception)? onStopFailed,
        void Function(CapturingStatusEnum status)? onCapturing}) {
    ThetaClientFlutterPlatform.instance
        .startLimitlessIntervalCapture(onStopFailed, onCapturing)
        .then((value) => onCaptureCompleted(value))
        .onError((error, stackTrace) => onCaptureFailed(error as Exception));
    return LimitlessIntervalCapturing();
  }
}

/// Capture of interval shooting with the shot count specified
class ShotCountSpecifiedIntervalCapture extends Capture {
  final int _interval;

  ShotCountSpecifiedIntervalCapture(super.options, this._interval);

  int getCheckStatusCommandInterval() {
    return _interval;
  }

  /// Get shooting interval (sec.) for interval shooting.
  int? getCaptureInterval() =>
      _options[OptionNameEnum.captureInterval.rawValue];

  /// Get number of shots for interval shooting.
  int? getCaptureNumber() => _options[OptionNameEnum.captureNumber.rawValue];

  /// Starts interval shooting with the shot count specified.
  ShotCountSpecifiedIntervalCapturing startCapture(
      void Function(List<String>? fileUrls) onSuccess,
      void Function(double completion) onProgress,
      void Function(Exception exception) onCaptureFailed,
      {void Function(Exception exception)? onStopFailed,
        void Function(CapturingStatusEnum status)? onCapturing}) {
    ThetaClientFlutterPlatform.instance
        .startShotCountSpecifiedIntervalCapture(
        onProgress, onStopFailed, onCapturing)
        .then((value) => onSuccess(value))
        .onError((error, stackTrace) => onCaptureFailed(error as Exception));
    return ShotCountSpecifiedIntervalCapturing();
  }
}

/// Capture of interval composite shooting
class CompositeIntervalCapture extends Capture {
  final int _interval;

  CompositeIntervalCapture(super.options, this._interval);

  int getCheckStatusCommandInterval() {
    return _interval;
  }

  /// Get In-progress save interval for interval composite shooting (sec).
  int? getCompositeShootingOutputInterval() =>
      _options[OptionNameEnum.compositeShootingOutputInterval.rawValue];

  /// Get Shooting time for interval composite shooting (sec).
  int? getCompositeShootingTime() =>
      _options[OptionNameEnum.compositeShootingTime.rawValue];

  /// Starts interval composite shooting.
  CompositeIntervalCapturing startCapture(
      void Function(List<String>? fileUrls) onSuccess,
      void Function(double completion) onProgress,
      void Function(Exception exception) onCaptureFailed,
      {void Function(Exception exception)? onStopFailed,
        void Function(CapturingStatusEnum status)? onCapturing}) {
    ThetaClientFlutterPlatform.instance
        .startCompositeIntervalCapture(onProgress, onStopFailed, onCapturing)
        .then((value) => onSuccess(value))
        .onError((error, stackTrace) => onCaptureFailed(error as Exception));
    return CompositeIntervalCapturing();
  }
}

/// Capture of burst shooting
class BurstCapture extends Capture {
  final int _interval;

  BurstCapture(super.options, this._interval);

  int getCheckStatusCommandInterval() {
    return _interval;
  }

  /// Get Burst shooting setting.
  BurstOption? getBurstOption() =>
      _options[OptionNameEnum.burstOption.rawValue];

  /// Get BurstMode setting.
  BurstModeEnum? getBurstMode() => _options[OptionNameEnum.burstMode.rawValue];

  /// Starts burst shooting
  BurstCapturing startCapture(
      void Function(List<String>? fileUrls) onSuccess,
      void Function(double completion) onProgress,
      void Function(Exception exception) onCaptureFailed,
      {void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing}) {
    ThetaClientFlutterPlatform.instance
        .startBurstCapture(onProgress, onStopFailed, onCapturing)
        .then((value) => onSuccess(value))
        .onError((error, stackTrace) => onCaptureFailed(error as Exception));
    return BurstCapturing();
  }
}

/// Capture of multi bracket shooting
class MultiBracketCapture extends Capture {
  final int _interval;

  MultiBracketCapture(super.options, this._interval);

  int getCheckStatusCommandInterval() {
    return _interval;
  }

  /// Starts interval composite shooting.
  MultiBracketCapturing startCapture(
      void Function(List<String>? fileUrls) onSuccess,
      void Function(double completion) onProgress,
      void Function(Exception exception) onCaptureFailed,
      {void Function(Exception exception)? onStopFailed,
      void Function(CapturingStatusEnum status)? onCapturing}) {
    ThetaClientFlutterPlatform.instance
        .startMultiBracketCapture(onProgress, onStopFailed, onCapturing)
        .then((value) => onSuccess(value))
        .onError((error, stackTrace) => onCaptureFailed(error as Exception));
    return MultiBracketCapturing();
  }
}

/// Capture of continuous shooting
class ContinuousCapture extends Capture {
  final int _interval;

  ContinuousCapture(super.options, this._interval);

  int getCheckStatusCommandInterval() {
    return _interval;
  }

  /// Get photo file format.
  PhotoFileFormatEnum? getFileFormat() {
    return _options[TagNameEnum.photoFileFormat.rawValue];
  }

  /// Get Number of shots for continuous shooting.
  Future<ContinuousNumberEnum> getContinuousNumber() async {
    return (await ThetaClientFlutterPlatform.instance
                .getOptions([OptionNameEnum.continuousNumber]))
            .continuousNumber ??
        ContinuousNumberEnum.unsupported;
  }

  /// Starts continuous shooting
  void startCapture(void Function(List<String>? fileUrls) onSuccess,
      void Function(double completion) onProgress,
      void Function(Exception exception) onCaptureFailed,
      {void Function(CapturingStatusEnum status)? onCapturing}) {
    ThetaClientFlutterPlatform.instance
        .startContinuousCapture(onProgress, onCapturing)
        .then((value) => onSuccess(value))
        .onError((error, stackTrace) => onCaptureFailed(error as Exception));
  }
}
