import 'package:flutter_test/flutter_test.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';

void main() {
  setUp(() {
  });

  tearDown(() {

  });

  test('CaptureStatusEnum', () async {
    List<List<dynamic>> data = [
      [CaptureStatusEnum.shooting, 'SHOOTING'],
      [CaptureStatusEnum.idle, 'IDLE'],
      [CaptureStatusEnum.selfTimerCountdown, 'SELF_TIMER_COUNTDOWN'],
      [CaptureStatusEnum.bracketShooting, 'BRACKET_SHOOTING'],
      [CaptureStatusEnum.converting, 'CONVERTING'],
      [CaptureStatusEnum.timeShiftShooting, 'TIME_SHIFT_SHOOTING'],
      [CaptureStatusEnum.continuousShooting, 'CONTINUOUS_SHOOTING'],
      [CaptureStatusEnum.retrospectiveImageRecording, 'RETROSPECTIVE_IMAGE_RECORDING'],
    ];
    expect(data.length, CaptureStatusEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('ChargingStateEnum', () async {
    List<List<dynamic>> data = [
      [ChargingStateEnum.charging, 'CHARGING'],
      [ChargingStateEnum.completed, 'COMPLETED'],
      [ChargingStateEnum.notCharging, 'NOT_CHARGING'],
    ];
    expect(data.length, ChargingStateEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('ShootingFunctionEnum', () async {
    List<List<dynamic>> data = [
      [ShootingFunctionEnum.normal, 'NORMAL'],
      [ShootingFunctionEnum.selfTimer, 'SELF_TIMER'],
      [ShootingFunctionEnum.mySetting, 'MY_SETTING'],
    ];
    expect(data.length, ShootingFunctionEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('MicrophoneOptionEnum', () async {
    List<List<dynamic>> data = [
      [MicrophoneOptionEnum.auto, 'AUTO'],
      [MicrophoneOptionEnum.internal, 'INTERNAL'],
      [MicrophoneOptionEnum.external, 'EXTERNAL'],
    ];
    expect(data.length, MicrophoneOptionEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('CameraErrorEnum', () async {
    List<List<dynamic>> data = [
      [CameraErrorEnum.noMemory, 'NO_MEMORY'],
      [CameraErrorEnum.fileNumberOver, 'FILE_NUMBER_OVER'],
      [CameraErrorEnum.noDateSetting, 'NO_DATE_SETTING'],
      [CameraErrorEnum.readError, 'READ_ERROR'],
      [CameraErrorEnum.notSupportedMediaType, 'NOT_SUPPORTED_MEDIA_TYPE'],
      [CameraErrorEnum.notSupportedFileSystem, 'NOT_SUPPORTED_FILE_SYSTEM'],
      [CameraErrorEnum.mediaNotReady, 'MEDIA_NOT_READY'],
      [CameraErrorEnum.notEnoughBattery, 'NOT_ENOUGH_BATTERY'],
      [CameraErrorEnum.invalidFile, 'INVALID_FILE'],
      [CameraErrorEnum.pluginBootError, 'PLUGIN_BOOT_ERROR'],
      [CameraErrorEnum.inProgressError, 'IN_PROGRESS_ERROR'],
      [CameraErrorEnum.cannotRecording, 'CANNOT_RECORDING'],
      [CameraErrorEnum.cannotRecordLowbat, 'CANNOT_RECORD_LOWBAT'],
      [CameraErrorEnum.captureHwFailed, 'CAPTURE_HW_FAILED'],
      [CameraErrorEnum.captureSwFailed, 'CAPTURE_SW_FAILED'],
      [CameraErrorEnum.internalMemAccessFail, 'INTERNAL_MEM_ACCESS_FAIL'],
      [CameraErrorEnum.unexpectedError, 'UNEXPECTED_ERROR'],
      [CameraErrorEnum.batteryChargeFail, 'BATTERY_CHARGE_FAIL'],
      [CameraErrorEnum.highTemperatureWarning, 'HIGH_TEMPERATURE_WARNING'],
      [CameraErrorEnum.highTemperature, 'HIGH_TEMPERATURE'],
      [CameraErrorEnum.batteryHighTemperature, 'BATTERY_HIGH_TEMPERATURE'],
      [CameraErrorEnum.compassCalibration, 'COMPASS_CALIBRATION'],
    ];
    expect(data.length, CameraErrorEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('AuthModeEnum', () async {
    List<List<dynamic>> data = [
      [AuthModeEnum.none, 'NONE'],
      [AuthModeEnum.wep, 'WEP'],
      [AuthModeEnum.wpa, 'WPA'],
    ];
    expect(data.length, AuthModeEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('ApertureEnum', () async {
    List<List<dynamic>> data = [
      [ApertureEnum.apertureAuto, 'APERTURE_AUTO'],
      [ApertureEnum.aperture_2_0, 'APERTURE_2_0'],
      [ApertureEnum.aperture_2_1, 'APERTURE_2_1'],
      [ApertureEnum.aperture_2_4, 'APERTURE_2_4'],
      [ApertureEnum.aperture_3_5, 'APERTURE_3_5'],
      [ApertureEnum.aperture_5_6, 'APERTURE_5_6'],
    ];
    expect(data.length, ApertureEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('CameraControlSourceEnum', () async {
    List<List<dynamic>> data = [
      [CameraControlSourceEnum.camera, 'CAMERA'],
      [CameraControlSourceEnum.app, 'APP'],
    ];
    expect(data.length, CameraControlSourceEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('CameraModeEnum', () async {
    List<List<dynamic>> data = [
      [CameraModeEnum.capture, 'CAPTURE'],
      [CameraModeEnum.playback, 'PLAYBACK'],
      [CameraModeEnum.setting, 'SETTING'],
      [CameraModeEnum.plugin, 'PLUGIN'],
    ];
    expect(data.length, CameraModeEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('CaptureModeEnum', () async {
    List<List<dynamic>> data = [
      [CaptureModeEnum.image, 'IMAGE'],
      [CaptureModeEnum.video, 'VIDEO'],
    ];
    expect(data.length, CaptureModeEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('ExposureCompensationEnum', () async {
    List<List<dynamic>> data = [
      [ExposureCompensationEnum.m2_0, 'M2_0'],
      [ExposureCompensationEnum.m1_7, 'M1_7'],
      [ExposureCompensationEnum.m1_3, 'M1_3'],
      [ExposureCompensationEnum.m1_0, 'M1_0'],
      [ExposureCompensationEnum.m0_7, 'M0_7'],
      [ExposureCompensationEnum.m0_3, 'M0_3'],
      [ExposureCompensationEnum.zero, 'ZERO'],
      [ExposureCompensationEnum.p0_3, 'P0_3'],
      [ExposureCompensationEnum.p0_7, 'P0_7'],
      [ExposureCompensationEnum.p1_0, 'P1_0'],
      [ExposureCompensationEnum.p1_3, 'P1_3'],
      [ExposureCompensationEnum.p1_7, 'P1_7'],
      [ExposureCompensationEnum.p2_0, 'P2_0'],
    ];
    expect(data.length, ExposureCompensationEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('ExposureDelayEnum', () async {
    List<List<dynamic>> data = [
      [ExposureDelayEnum.delayOff, 'DELAY_OFF'],
      [ExposureDelayEnum.delay1, 'DELAY_1'],
      [ExposureDelayEnum.delay2, 'DELAY_2'],
      [ExposureDelayEnum.delay3, 'DELAY_3'],
      [ExposureDelayEnum.delay4, 'DELAY_4'],
      [ExposureDelayEnum.delay5, 'DELAY_5'],
      [ExposureDelayEnum.delay6, 'DELAY_6'],
      [ExposureDelayEnum.delay7, 'DELAY_7'],
      [ExposureDelayEnum.delay8, 'DELAY_8'],
      [ExposureDelayEnum.delay9, 'DELAY_9'],
      [ExposureDelayEnum.delay10, 'DELAY_10'],
    ];
    expect(data.length, ExposureDelayEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('ExposureProgramEnum', () async {
    List<List<dynamic>> data = [
      [ExposureProgramEnum.manual, 'MANUAL'],
      [ExposureProgramEnum.normalProgram, 'NORMAL_PROGRAM'],
      [ExposureProgramEnum.aperturePriority, 'APERTURE_PRIORITY'],
      [ExposureProgramEnum.shutterPriority, 'SHUTTER_PRIORITY'],
      [ExposureProgramEnum.isoPriority, 'ISO_PRIORITY'],
    ];
    expect(data.length, ExposureProgramEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('FileFormatEnum', () async {
    List<List<dynamic>> data = [
      [FileFormatEnum.image_2K, 'IMAGE_2K'],
      [FileFormatEnum.image_5K, 'IMAGE_5K'],
      [FileFormatEnum.image_6_7K, 'IMAGE_6_7K'],
      [FileFormatEnum.rawP_6_7K, 'RAW_P_6_7K'],
      [FileFormatEnum.image_5_5K, 'IMAGE_5_5K'],
      [FileFormatEnum.image_11K, 'IMAGE_11K'],
      [FileFormatEnum.videoHD, 'VIDEO_HD'],
      [FileFormatEnum.videoFullHD, 'VIDEO_FULL_HD'],
      [FileFormatEnum.video_2K, 'VIDEO_2K'],
      [FileFormatEnum.video_4K, 'VIDEO_4K'],
      [FileFormatEnum.video_2K_30F, 'VIDEO_2K_30F'],
      [FileFormatEnum.video_2K_60F, 'VIDEO_2K_60F'],
      [FileFormatEnum.video_4K_30F, 'VIDEO_4K_30F'],
      [FileFormatEnum.video_4K_60F, 'VIDEO_4K_60F'],
      [FileFormatEnum.video_5_7K_2F, 'VIDEO_5_7K_2F'],
      [FileFormatEnum.video_5_7K_5F, 'VIDEO_5_7K_5F'],
      [FileFormatEnum.video_5_7K_30F, 'VIDEO_5_7K_30F'],
      [FileFormatEnum.video_7K_2F, 'VIDEO_7K_2F'],
      [FileFormatEnum.video_7K_5F, 'VIDEO_7K_5F'],
      [FileFormatEnum.video_7K_10F, 'VIDEO_7K_10F'],
    ];
    expect(data.length, FileFormatEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('FileTypeEnum', () async {
    List<List<dynamic>> data = [
      [FileTypeEnum.all, 'ALL'],
      [FileTypeEnum.image, 'IMAGE'],
      [FileTypeEnum.video, 'VIDEO'],
    ];
    expect(data.length, FileTypeEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('PhotoFileFormatEnum', () async {
    List<List<dynamic>> data = [
      [PhotoFileFormatEnum.image_2K, 'IMAGE_2K'],
      [PhotoFileFormatEnum.image_5K, 'IMAGE_5K'],
      [PhotoFileFormatEnum.image_6_7K, 'IMAGE_6_7K'],
      [PhotoFileFormatEnum.rawP_6_7K, 'RAW_P_6_7K'],
      [PhotoFileFormatEnum.image_5_5K, 'IMAGE_5_5K'],
      [PhotoFileFormatEnum.image_11K, 'IMAGE_11K'],
    ];
    expect(data.length, PhotoFileFormatEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('VideoFileFormatEnum', () async {
    List<List<dynamic>> data = [
      [VideoFileFormatEnum.videoHD, 'VIDEO_HD'],
      [VideoFileFormatEnum.videoFullHD, 'VIDEO_FULL_HD'],
      [VideoFileFormatEnum.video_2K, 'VIDEO_2K'],
      [VideoFileFormatEnum.video_4K, 'VIDEO_4K'],
      [VideoFileFormatEnum.video_2K_30F, 'VIDEO_2K_30F'],
      [VideoFileFormatEnum.video_2K_60F, 'VIDEO_2K_60F'],
      [VideoFileFormatEnum.video_4K_30F, 'VIDEO_4K_30F'],
      [VideoFileFormatEnum.video_4K_60F, 'VIDEO_4K_60F'],
      [VideoFileFormatEnum.video_5_7K_2F, 'VIDEO_5_7K_2F'],
      [VideoFileFormatEnum.video_5_7K_5F, 'VIDEO_5_7K_5F'],
      [VideoFileFormatEnum.video_5_7K_30F, 'VIDEO_5_7K_30F'],
      [VideoFileFormatEnum.video_7K_2F, 'VIDEO_7K_2F'],
      [VideoFileFormatEnum.video_7K_5F, 'VIDEO_7K_5F'],
      [VideoFileFormatEnum.video_7K_10F, 'VIDEO_7K_10F'],
    ];
    expect(data.length, VideoFileFormatEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('FilterEnum', () async {
    List<List<dynamic>> data = [
      [FilterEnum.off, 'OFF'],
      [FilterEnum.noiseReduction, 'NOISE_REDUCTION'],
      [FilterEnum.hdr, 'HDR'],
    ];
    expect(data.length, FilterEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('GpsTagRecordingEnum', () async {
    List<List<dynamic>> data = [
      [GpsTagRecordingEnum.on, 'ON'],
      [GpsTagRecordingEnum.off, 'OFF'],
    ];
    expect(data.length, GpsTagRecordingEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('IsoEnum', () async {
    List<List<dynamic>> data = [
      [IsoEnum.isoAuto, 'ISO_AUTO'],
      [IsoEnum.iso50, 'ISO_50'],
      [IsoEnum.iso64, 'ISO_64'],
      [IsoEnum.iso80, 'ISO_80'],
      [IsoEnum.iso100, 'ISO_100'],
      [IsoEnum.iso125, 'ISO_125'],
      [IsoEnum.iso160, 'ISO_160'],
      [IsoEnum.iso200, 'ISO_200'],
      [IsoEnum.iso250, 'ISO_250'],
      [IsoEnum.iso320, 'ISO_320'],
      [IsoEnum.iso400, 'ISO_400'],
      [IsoEnum.iso500, 'ISO_500'],
      [IsoEnum.iso640, 'ISO_640'],
      [IsoEnum.iso800, 'ISO_800'],
      [IsoEnum.iso1000, 'ISO_1000'],
      [IsoEnum.iso1250, 'ISO_1250'],
      [IsoEnum.iso1600, 'ISO_1600'],
      [IsoEnum.iso2000, 'ISO_2000'],
      [IsoEnum.iso2500, 'ISO_2500'],
      [IsoEnum.iso3200, 'ISO_3200'],
      [IsoEnum.iso4000, 'ISO_4000'],
      [IsoEnum.iso5000, 'ISO_5000'],
      [IsoEnum.iso6400, 'ISO_6400'],
    ];
    expect(data.length, IsoEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('IsoAutoHighLimitEnum', () async {
    List<List<dynamic>> data = [
      [IsoAutoHighLimitEnum.iso100, 'ISO_100'],
      [IsoAutoHighLimitEnum.iso125, 'ISO_125'],
      [IsoAutoHighLimitEnum.iso160, 'ISO_160'],
      [IsoAutoHighLimitEnum.iso200, 'ISO_200'],
      [IsoAutoHighLimitEnum.iso250, 'ISO_250'],
      [IsoAutoHighLimitEnum.iso320, 'ISO_320'],
      [IsoAutoHighLimitEnum.iso400, 'ISO_400'],
      [IsoAutoHighLimitEnum.iso500, 'ISO_500'],
      [IsoAutoHighLimitEnum.iso640, 'ISO_640'],
      [IsoAutoHighLimitEnum.iso800, 'ISO_800'],
      [IsoAutoHighLimitEnum.iso1000, 'ISO_1000'],
      [IsoAutoHighLimitEnum.iso1250, 'ISO_1250'],
      [IsoAutoHighLimitEnum.iso1600, 'ISO_1600'],
      [IsoAutoHighLimitEnum.iso2000, 'ISO_2000'],
      [IsoAutoHighLimitEnum.iso2500, 'ISO_2500'],
      [IsoAutoHighLimitEnum.iso3200, 'ISO_3200'],
      [IsoAutoHighLimitEnum.iso4000, 'ISO_4000'],
      [IsoAutoHighLimitEnum.iso5000, 'ISO_5000'],
      [IsoAutoHighLimitEnum.iso6400, 'ISO_6400'],
    ];
    expect(data.length, IsoAutoHighLimitEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('LanguageEnum', () async {
    List<List<dynamic>> data = [
      [LanguageEnum.de, 'DE'],
      [LanguageEnum.enGB, 'EN_GB'],
      [LanguageEnum.enUS, 'EN_US'],
      [LanguageEnum.fr, 'FR'],
      [LanguageEnum.it, 'IT'],
      [LanguageEnum.ja, 'JA'],
      [LanguageEnum.ko, 'KO'],
      [LanguageEnum.zhCN, 'ZH_CN'],
      [LanguageEnum.zhTW, 'ZH_TW'],
    ];
    expect(data.length, LanguageEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('MaxRecordableTimeEnum', () async {
    List<List<dynamic>> data = [
      [MaxRecordableTimeEnum.time_180, 'RECORDABLE_TIME_180'],
      [MaxRecordableTimeEnum.time_300, 'RECORDABLE_TIME_300'],
      [MaxRecordableTimeEnum.time_1500, 'RECORDABLE_TIME_1500'],
      [MaxRecordableTimeEnum.time_7200, 'RECORDABLE_TIME_7200'],
      [MaxRecordableTimeEnum.doNotUpdateMySettingCondition, 'DO_NOT_UPDATE_MY_SETTING_CONDITION'],
    ];
    expect(data.length, MaxRecordableTimeEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('NetworkTypeEnum', () async {
    List<List<dynamic>> data = [
      [NetworkTypeEnum.client, 'CLIENT'],
      [NetworkTypeEnum.direct, 'DIRECT'],
      [NetworkTypeEnum.ethernet, 'ETHERNET'],
      [NetworkTypeEnum.off, 'OFF'],
    ];
    expect(data.length, NetworkTypeEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('OffDelayEnum', () async {
    List<List<dynamic>> data = [
      [OffDelayEnum.disable, 'DISABLE'],
      [OffDelayEnum.offDelay_5m, 'OFF_DELAY_5M'],
      [OffDelayEnum.offDelay_10m, 'OFF_DELAY_10M'],
      [OffDelayEnum.offDelay_15m, 'OFF_DELAY_15M'],
      [OffDelayEnum.offDelay_30m, 'OFF_DELAY_30M'],
    ];
    expect(data.length, OffDelayEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('PowerSavingEnum', () async {
    List<List<dynamic>> data = [
      [PowerSavingEnum.on, 'ON'],
      [PowerSavingEnum.off, 'OFF'],
    ];
    expect(data.length, PowerSavingEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('SleepDelayEnum', () async {
    List<List<dynamic>> data = [
      [SleepDelayEnum.sleepDelay_3m, 'SLEEP_DELAY_3M'],
      [SleepDelayEnum.sleepDelay_5m, 'SLEEP_DELAY_5M'],
      [SleepDelayEnum.sleepDelay_7m, 'SLEEP_DELAY_7M'],
      [SleepDelayEnum.sleepDelay_10m, 'SLEEP_DELAY_10M'],
      [SleepDelayEnum.disable, 'DISABLE'],
    ];
    expect(data.length, SleepDelayEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('StorageEnum', () async {
    List<List<dynamic>> data = [
      [StorageEnum.internal, 'INTERNAL'],
      [StorageEnum.sd, 'SD'],
      [StorageEnum.current, 'CURRENT'],
    ];
    expect(data.length, StorageEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('WhiteBalanceEnum', () async {
    List<List<dynamic>> data = [
      [WhiteBalanceEnum.auto, 'AUTO'],
      [WhiteBalanceEnum.daylight, 'DAYLIGHT'],
      [WhiteBalanceEnum.sade, 'SHADE'],
      [WhiteBalanceEnum.cloudyDaylight, 'CLOUDY_DAYLIGHT'],
      [WhiteBalanceEnum.incandescent, 'INCANDESCENT'],
      [WhiteBalanceEnum.warmWhiteFluorescent, 'WARM_WHITE_FLUORESCENT'],
      [WhiteBalanceEnum.daylightFluorescent, 'DAYLIGHT_FLUORESCENT'],
      [WhiteBalanceEnum.daywhiteFluorescent, 'DAYWHITE_FLUORESCENT'],
      [WhiteBalanceEnum.fluorescent, 'FLUORESCENT'],
      [WhiteBalanceEnum.bulbFluorescent, 'BULB_FLUORESCENT'],
      [WhiteBalanceEnum.colorTemperature, 'COLOR_TEMPERATURE'],
      [WhiteBalanceEnum.underwater, 'UNDERWATER'],
    ];
    expect(data.length, WhiteBalanceEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('WhiteBalanceAutoStrengthEnum', () async {
    List<List<dynamic>> data = [
      [WhiteBalanceAutoStrengthEnum.on, 'ON'],
      [WhiteBalanceAutoStrengthEnum.off, 'OFF'],
    ];
    expect(data.length, WhiteBalanceAutoStrengthEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('WlanFrequencyEnum', () async {
    List<List<dynamic>> data = [
      [WlanFrequencyEnum.ghz_2_4, 'GHZ_2_4'],
      [WlanFrequencyEnum.ghz_5, 'GHZ_5'],
    ];
    expect(data.length, WlanFrequencyEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });
}
