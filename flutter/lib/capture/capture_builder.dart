import 'dart:async';

import '../theta_client_flutter.dart';
import '../theta_client_flutter_platform_interface.dart';
import '../utils/convert_utils.dart';

/// Builder
class CaptureBuilder<T> {
  /// options of capture
  final Map<String, dynamic> _options = {};

  /// Set aperture value.
  T setAperture(ApertureEnum aperture) {
    _options[OptionNameEnum.aperture.rawValue] = aperture;
    return this as T;
  }

  /// Set color temperature of the camera (Kelvin).
  ///
  /// 2500 to 10000. In 100-Kelvin units.
  T setColorTemperature(int kelvin) {
    _options[OptionNameEnum.colorTemperature.rawValue] = kelvin;
    return this as T;
  }

  /// Set exposure compensation (EV).
  T setExposureCompensation(ExposureCompensationEnum value) {
    _options[OptionNameEnum.exposureCompensation.rawValue] = value;
    return this as T;
  }

  /// Set operating time (sec.) of the self-timer.
  T setExposureDelay(ExposureDelayEnum value) {
    _options[OptionNameEnum.exposureDelay.rawValue] = value;
    return this as T;
  }

  /// Set exposure program. The exposure settings that take priority can be selected.
  T setExposureProgram(ExposureProgramEnum program) {
    _options[OptionNameEnum.exposureProgram.rawValue] = program;
    return this as T;
  }

  /// Set GPS information.
  T setGpsInfo(GpsInfo gpsInfo) {
    _options[OptionNameEnum.gpsInfo.rawValue] = gpsInfo;
    return this as T;
  }

  /// Set turns position information assigning ON/OFF.
  ///
  /// For RICOH THETA X
  T setGpsTagRecording(GpsTagRecordingEnum value) {
    _options[TagNameEnum.gpsTagRecording.rawValue] = value;
    return this as T;
  }

  /// Set ISO sensitivity.
  ///
  /// It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
  /// Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
  ///
  /// When the exposure program (exposureProgram) is set to Manual or ISO Priority
  T setIso(IsoEnum iso) {
    _options[OptionNameEnum.iso.rawValue] = iso;
    return this as T;
  }

  /// Set ISO sensitivity upper limit when ISO sensitivity is set to automatic.
  T setIsoAutoHighLimit(IsoAutoHighLimitEnum iso) {
    _options[OptionNameEnum.isoAutoHighLimit.rawValue] = iso;
    return this as T;
  }

  /// Set white balance.
  ///
  /// It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
  /// Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
  T setWhiteBalance(WhiteBalanceEnum whiteBalance) {
    _options[OptionNameEnum.whiteBalance.rawValue] = whiteBalance;
    return this as T;
  }
}

/// Builder of [PhotoCapture]
class PhotoCaptureBuilder extends CaptureBuilder<PhotoCaptureBuilder> {
  /// Set photo file format.
  PhotoCaptureBuilder setFileFormat(PhotoFileFormatEnum fileFormat) {
    _options[TagNameEnum.photoFileFormat.rawValue] = fileFormat;
    return this;
  }

  /// Set image processing filter.
  PhotoCaptureBuilder setFilter(FilterEnum filter) {
    _options[OptionNameEnum.filter.rawValue] = filter;
    return this;
  }

  /// Set preset mode of Theta SC2 and Theta SC2 for business.
  PhotoCaptureBuilder setPreset(PresetEnum preset) {
    _options[OptionNameEnum.preset.rawValue] = preset;
    return this;
  }

  /// Builds an instance of a PhotoCapture that has all the combined parameters of the Options that have been added to the Builder.
  Future<PhotoCapture> build() async {
    var completer = Completer<PhotoCapture>();
    try {
      await ThetaClientFlutterPlatform.instance.buildPhotoCapture(_options);
      completer.complete(PhotoCapture(_options));
    } catch (e) {
      completer.completeError(e);
    }
    return completer.future;
  }
}

/// Builder of TimeShiftCapture
class TimeShiftCaptureBuilder extends CaptureBuilder<TimeShiftCaptureBuilder> {
  int _interval = -1;
  TimeShift? _timeShift;

  void _checkAndInitTimeShiftSetting() {
    if (_timeShift == null) {
      _timeShift = TimeShift();
      _options[OptionNameEnum.timeShift.rawValue] = _timeShift;
    }
  }

  TimeShiftCaptureBuilder setCheckStatusCommandInterval(int timeMillis) {
    _interval = timeMillis;
    return this;
  }

  /// Set is front first
  TimeShiftCaptureBuilder setIsFrontFirst(bool isFrontFirst) {
    _checkAndInitTimeShiftSetting();
    _timeShift?.isFrontFirst = isFrontFirst;
    return this;
  }

  /// set time (sec) before 1st lens shooting
  TimeShiftCaptureBuilder setSecondInterval(TimeShiftIntervalEnum interval) {
    _checkAndInitTimeShiftSetting();
    _timeShift?.secondInterval = interval;
    return this;
  }

  /// set time (sec) before 1st lens shooting
  TimeShiftCaptureBuilder setFirstInterval(TimeShiftIntervalEnum interval) {
    _checkAndInitTimeShiftSetting();
    _timeShift?.firstInterval = interval;
    return this;
  }

  /// Builds an instance of a TimeShiftCapture that has all the combined parameters of the Options that have been added to the Builder.
  Future<TimeShiftCapture> build() async {
    var completer = Completer<TimeShiftCapture>();
    try {
      await ThetaClientFlutterPlatform.instance
          .buildTimeShiftCapture(_options, _interval);
      completer.complete(TimeShiftCapture(_options, _interval));
    } catch (e) {
      completer.completeError(e);
    }
    return completer.future;
  }
}

/// Builder of VideoCapture
class VideoCaptureBuilder extends CaptureBuilder<VideoCaptureBuilder> {
  /// Set video file format.
  VideoCaptureBuilder setFileFormat(VideoFileFormatEnum fileFormat) {
    _options[TagNameEnum.videoFileFormat.rawValue] = fileFormat;
    return this;
  }

  /// Set maximum recordable time (in seconds) of the camera.
  VideoCaptureBuilder setMaxRecordableTime(MaxRecordableTimeEnum time) {
    _options[OptionNameEnum.maxRecordableTime.rawValue] = time;
    return this;
  }

  /// Builds an instance of a VideoCapture that has all the combined parameters of the Options that have been added to the Builder.
  Future<VideoCapture> build() async {
    var completer = Completer<VideoCapture>();
    try {
      await ThetaClientFlutterPlatform.instance.buildVideoCapture(_options);
      completer.complete(VideoCapture(_options));
    } catch (e) {
      completer.completeError(e);
    }
    return completer.future;
  }
}

/// Builder of LimitlessIntervalCapture
class LimitlessIntervalCaptureBuilder
    extends CaptureBuilder<LimitlessIntervalCaptureBuilder> {
  /// Set shooting interval (sec.) for interval shooting.
  LimitlessIntervalCaptureBuilder setCaptureInterval(int interval) {
    _options[OptionNameEnum.captureInterval.rawValue] = interval;
    return this;
  }

  /// Builds an instance of a LimitlessIntervalCapture that has all the combined parameters of the Options that have been added to the Builder.
  Future<LimitlessIntervalCapture> build() async {
    var completer = Completer<LimitlessIntervalCapture>();
    try {
      await ThetaClientFlutterPlatform.instance
          .buildLimitlessIntervalCapture(_options);
      completer.complete(LimitlessIntervalCapture(_options));
    } catch (e) {
      completer.completeError(e);
    }
    return completer.future;
  }
}

/// Builder of ShotCountSpecifiedIntervalCapture
class ShotCountSpecifiedIntervalCaptureBuilder
    extends CaptureBuilder<ShotCountSpecifiedIntervalCaptureBuilder> {
  int _interval = -1;

  ShotCountSpecifiedIntervalCaptureBuilder setCheckStatusCommandInterval(
      int timeMillis) {
    _interval = timeMillis;
    return this;
  }

  /// Set shooting interval (sec.) for interval shooting.
  ShotCountSpecifiedIntervalCaptureBuilder setCaptureInterval(int interval) {
    _options[OptionNameEnum.captureInterval.rawValue] = interval;
    return this;
  }

  /// Builds an instance of a ShotCountSpecifiedIntervalCapture that has all the combined parameters of the Options that have been added to the Builder.
  Future<ShotCountSpecifiedIntervalCapture> build() async {
    var completer = Completer<ShotCountSpecifiedIntervalCapture>();
    try {
      await ThetaClientFlutterPlatform.instance
          .buildShotCountSpecifiedIntervalCapture(_options, _interval);
      completer
          .complete(ShotCountSpecifiedIntervalCapture(_options, _interval));
    } catch (e) {
      completer.completeError(e);
    }
    return completer.future;
  }
}

/// Builder of CompositeIntervalCapture
class CompositeIntervalCaptureBuilder
    extends CaptureBuilder<CompositeIntervalCaptureBuilder> {
  int _interval = -1;

  CompositeIntervalCaptureBuilder setCheckStatusCommandInterval(
      int timeMillis) {
    _interval = timeMillis;
    return this;
  }

  /// Set In-progress save interval for interval composite shooting (sec).
  CompositeIntervalCaptureBuilder setCompositeShootingOutputInterval(int sec) {
    _options[OptionNameEnum.compositeShootingOutputInterval.rawValue] = sec;
    return this;
  }

  /// Builds an instance of a CompositeIntervalCapture that has all the combined parameters of the Options that have been added to the Builder.
  Future<CompositeIntervalCapture> build() async {
    var completer = Completer<CompositeIntervalCapture>();
    try {
      await ThetaClientFlutterPlatform.instance
          .buildCompositeIntervalCapture(_options, _interval);
      completer.complete(CompositeIntervalCapture(_options, _interval));
    } catch (e) {
      completer.completeError(e);
    }
    return completer.future;
  }
}

/// Builder of BurstCapture
class BurstCaptureBuilder extends CaptureBuilder<BurstCaptureBuilder> {
  int _interval = -1;

  BurstCaptureBuilder setCheckStatusCommandInterval(int timeMillis) {
    _interval = timeMillis;
    return this;
  }

  /// BurstMode setting.
  BurstCaptureBuilder setBurstMode(BurstModeEnum mode) {
    _options[OptionNameEnum.burstMode.rawValue] = mode;
    return this;
  }

  /// Builds an instance of a BurstCapture that has all the combined parameters of the Options that have been added to the Builder.
  Future<BurstCapture> build() async {
    var completer = Completer<BurstCapture>();
    try {
      await ThetaClientFlutterPlatform.instance
          .buildBurstCapture(_options, _interval);
      completer.complete(BurstCapture(_options, _interval));
    } catch (e) {
      completer.completeError(e);
    }
    return completer.future;
  }
}

/// Photo image format used in PhotoCapture.
enum PhotoFileFormatEnum {
  /// Image File format.
  /// type: jpeg
  /// size: 2048 x 1024
  ///
  /// For RICOH THETA S or SC
  image_2K('IMAGE_2K'),

  /// Image File format.
  /// type: jpeg
  /// size: 5376 x 2688
  ///
  /// For RICOH THETA V or S or SC
  image_5K('IMAGE_5K'),

  /// Image File format.
  /// type: jpeg
  /// size: 6720 x 3360
  ///
  /// For RICOH THETA Z1
  image_6_7K('IMAGE_6_7K'),

  /// Image File format.
  /// type: raw+
  /// size: 6720 x 3360
  ///
  /// For RICOH THETA Z1
  rawP_6_7K('RAW_P_6_7K'),

  /// Image File format.
  /// type: jpeg
  /// size: 5504 x 2752
  ///
  /// For RICOH THETA X or later
  image_5_5K('IMAGE_5_5K'),

  /// Image File format.
  /// type: jpeg
  /// size: 11008 x 5504
  ///
  /// For RICOH THETA X or later
  image_11K('IMAGE_11K');

  final String rawValue;

  const PhotoFileFormatEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }
}

/// Video image format used in VideoCapture.
enum VideoFileFormatEnum {
  /// Video File format.
  /// type: mp4
  /// size: 1280 x 570
  ///
  /// For RICOH THETA S or SC
  videoHD('VIDEO_HD'),

  /// Video File format.
  /// type: mp4
  /// size: 1920 x 1080
  ///
  /// For RICOH THETA S or SC
  videoFullHD('VIDEO_FULL_HD'),

  /// Video File format.
  /// type: mp4
  /// size: 1920 x 960
  /// codec: H.264/MPEG-4 AVC
  ///
  /// For RICOH THETA Z1 or V
  video_2K('VIDEO_2K'),

  /// Video File format.
  /// type: mp4
  /// size: 3840 x 1920
  /// codec: H.264/MPEG-4 AVC
  ///
  /// For RICOH THETA Z1 or V
  video_4K('VIDEO_4K'),

  /// Video File format.
  /// type: mp4
  /// size: 1920 x 960
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 30
  ///
  /// For RICOH THETA X or later
  video_2K_30F('VIDEO_2K_30F'),

  /// Video File format.
  /// type: mp4
  /// size: 1920 x 960
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 60
  ///
  /// For RICOH THETA X or later
  video_2K_60F('VIDEO_2K_60F'),

  /// Video File format.
  /// type: mp4
  /// size: 3840 x 1920
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 30
  ///
  /// For RICOH THETA X or later
  video_4K_30F('VIDEO_4K_30F'),

  /// Video File format.
  /// type: mp4
  /// size: 3840 x 1920
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 60
  ///
  /// For RICOH THETA X or later
  video_4K_60F('VIDEO_4K_60F'),

  /// Video File format.
  /// type: mp4
  /// size: 5760 x 2880
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 2
  ///
  /// For RICOH THETA X or later
  video_5_7K_2F('VIDEO_5_7K_2F'),

  /// Video File format.
  /// type: mp4
  /// size: 5760 x 2880
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 5
  ///
  /// For RICOH THETA X or later
  video_5_7K_5F('VIDEO_5_7K_5F'),

  /// Video File format.
  /// type: mp4
  /// size: 5760 x 2880
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 30
  ///
  /// For RICOH THETA X or later
  video_5_7K_30F('VIDEO_5_7K_30F'),

  /// Video File format.
  /// type: mp4
  /// size: 7680 x 3840
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 2
  ///
  /// For RICOH THETA X or later
  video_7K_2F('VIDEO_7K_2F'),

  /// Video File format.
  /// type: mp4
  /// size: 7680 x 3840
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 5
  ///
  /// For RICOH THETA X or later
  video_7K_5F('VIDEO_7K_5F'),

  /// Video File format.
  /// type: mp4
  /// size: 7680 x 3840
  /// codec: H.264/MPEG-4 AVC
  /// frame rate: 10
  ///
  /// For RICOH THETA X or later
  video_7K_10F('VIDEO_7K_10F');

  final String rawValue;

  const VideoFileFormatEnum(this.rawValue);

  @override
  String toString() {
    return rawValue;
  }
}
