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

/// Capture of Photo
class PhotoCapture extends Capture {
  PhotoCapture(super.options);

  /// Get image processing filter.
  FilterEnum? getFilter() {
    return _options[OptionNameEnum.filter.rawValue];
  }

  /// Get photo file format.
  PhotoFileFormatEnum? getFileFormat() {
    return _options[TagNameEnum.photoFileFormat.rawValue];
  }

  /// Take a picture.
  void takePicture(void Function(String fileUrl) onSuccess,
      void Function(Exception exception) onError) {
    ThetaClientFlutterPlatform.instance
        .takePicture()
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
    void Function(String? fileUrl) onSuccess,
    void Function(double completion) onProgress,
    void Function(Exception exception) onError,
  ) {
    ThetaClientFlutterPlatform.instance
        .startTimeShiftCapture(onProgress)
        .then((value) => onSuccess(value))
        .onError((error, stackTrace) => onError(error as Exception));
    return TimeShiftCapturing();
  }
}

/// Capture of Video
class VideoCapture extends Capture {
  VideoCapture(super.options);

  /// Get maximum recordable time (in seconds) of the camera.
  MaxRecordableTimeEnum? getMaxRecordableTime() {
    return _options[OptionNameEnum.maxRecordableTime.rawValue];
  }

  /// Get video file format.
  VideoFileFormatEnum? getFileFormat() {
    return _options[TagNameEnum.videoFileFormat.rawValue];
  }

  /// Starts video capture.
  VideoCapturing startCapture(void Function(String fileUrl) onSuccess,
      void Function(Exception exception) onError) {
    ThetaClientFlutterPlatform.instance
        .startVideoCapture()
        .then((value) => onSuccess(value!))
        .onError((error, stackTrace) => onError(error as Exception));
    return VideoCapturing();
  }
}
