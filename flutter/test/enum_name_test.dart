import 'package:flutter_test/flutter_test.dart';
import 'package:theta_client_flutter/theta_client_flutter.dart';

void main() {
  setUp(() {});

  tearDown(() {});

  test('BluetoothPowerEnum', () async {
    List<List<dynamic>> data = [
      [BluetoothPowerEnum.on, 'ON'],
      [BluetoothPowerEnum.off, 'OFF'],
    ];
    expect(data.length, BluetoothPowerEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('BurstModeEnum', () async {
    List<List<dynamic>> data = [
      [BurstModeEnum.on, 'ON'],
      [BurstModeEnum.off, 'OFF'],
    ];
    expect(data.length, BurstModeEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('BurstCaptureNumEnum', () async {
    List<List<dynamic>> data = [
      [BurstCaptureNumEnum.burstCaptureNum_1, 'BURST_CAPTURE_NUM_1'],
      [BurstCaptureNumEnum.burstCaptureNum_3, 'BURST_CAPTURE_NUM_3'],
      [BurstCaptureNumEnum.burstCaptureNum_5, 'BURST_CAPTURE_NUM_5'],
      [BurstCaptureNumEnum.burstCaptureNum_7, 'BURST_CAPTURE_NUM_7'],
      [BurstCaptureNumEnum.burstCaptureNum_9, 'BURST_CAPTURE_NUM_9'],
    ];
    expect(data.length, BurstCaptureNumEnum.values.length,
        reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('BurstBracketStepEnum', () async {
    List<List<dynamic>> data = [
      [BurstBracketStepEnum.bracketStep_0_0, 'BRACKET_STEP_0_0'],
      [BurstBracketStepEnum.bracketStep_0_3, 'BRACKET_STEP_0_3'],
      [BurstBracketStepEnum.bracketStep_0_7, 'BRACKET_STEP_0_7'],
      [BurstBracketStepEnum.bracketStep_1_0, 'BRACKET_STEP_1_0'],
      [BurstBracketStepEnum.bracketStep_1_3, 'BRACKET_STEP_1_3'],
      [BurstBracketStepEnum.bracketStep_1_7, 'BRACKET_STEP_1_7'],
      [BurstBracketStepEnum.bracketStep_2_0, 'BRACKET_STEP_2_0'],
      [BurstBracketStepEnum.bracketStep_2_3, 'BRACKET_STEP_2_3'],
      [BurstBracketStepEnum.bracketStep_2_7, 'BRACKET_STEP_2_7'],
      [BurstBracketStepEnum.bracketStep_3_0, 'BRACKET_STEP_3_0'],
    ];
    expect(data.length, BurstBracketStepEnum.values.length,
        reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('BurstCompensationEnum', () async {
    List<List<dynamic>> data = [
      [
        BurstCompensationEnum.burstCompensationDown_5_0,
        'BURST_COMPENSATION_DOWN_5_0'
      ],
      [
        BurstCompensationEnum.burstCompensationDown_4_7,
        'BURST_COMPENSATION_DOWN_4_7'
      ],
      [
        BurstCompensationEnum.burstCompensationDown_4_3,
        'BURST_COMPENSATION_DOWN_4_3'
      ],
      [
        BurstCompensationEnum.burstCompensationDown_4_0,
        'BURST_COMPENSATION_DOWN_4_0'
      ],
      [
        BurstCompensationEnum.burstCompensationDown_3_7,
        'BURST_COMPENSATION_DOWN_3_7'
      ],
      [
        BurstCompensationEnum.burstCompensationDown_3_3,
        'BURST_COMPENSATION_DOWN_3_3'
      ],
      [
        BurstCompensationEnum.burstCompensationDown_3_0,
        'BURST_COMPENSATION_DOWN_3_0'
      ],
      [
        BurstCompensationEnum.burstCompensationDown_2_7,
        'BURST_COMPENSATION_DOWN_2_7'
      ],
      [
        BurstCompensationEnum.burstCompensationDown_2_3,
        'BURST_COMPENSATION_DOWN_2_3'
      ],
      [
        BurstCompensationEnum.burstCompensationDown_2_0,
        'BURST_COMPENSATION_DOWN_2_0'
      ],
      [
        BurstCompensationEnum.burstCompensationDown_1_7,
        'BURST_COMPENSATION_DOWN_1_7'
      ],
      [
        BurstCompensationEnum.burstCompensationDown_1_3,
        'BURST_COMPENSATION_DOWN_1_3'
      ],
      [
        BurstCompensationEnum.burstCompensationDown_1_0,
        'BURST_COMPENSATION_DOWN_1_0'
      ],
      [
        BurstCompensationEnum.burstCompensationDown_0_7,
        'BURST_COMPENSATION_DOWN_0_7'
      ],
      [
        BurstCompensationEnum.burstCompensationDown_0_3,
        'BURST_COMPENSATION_DOWN_0_3'
      ],
      [BurstCompensationEnum.burstCompensation_0_0, 'BURST_COMPENSATION_0_0'],
      [
        BurstCompensationEnum.burstCompensationUp_0_3,
        'BURST_COMPENSATION_UP_0_3'
      ],
      [
        BurstCompensationEnum.burstCompensationUp_0_7,
        'BURST_COMPENSATION_UP_0_7'
      ],
      [
        BurstCompensationEnum.burstCompensationUp_1_0,
        'BURST_COMPENSATION_UP_1_0'
      ],
      [
        BurstCompensationEnum.burstCompensationUp_1_3,
        'BURST_COMPENSATION_UP_1_3'
      ],
      [
        BurstCompensationEnum.burstCompensationUp_1_7,
        'BURST_COMPENSATION_UP_1_7'
      ],
      [
        BurstCompensationEnum.burstCompensationUp_2_0,
        'BURST_COMPENSATION_UP_2_0'
      ],
      [
        BurstCompensationEnum.burstCompensationUp_2_3,
        'BURST_COMPENSATION_UP_2_3'
      ],
      [
        BurstCompensationEnum.burstCompensationUp_2_7,
        'BURST_COMPENSATION_UP_2_7'
      ],
      [
        BurstCompensationEnum.burstCompensationUp_3_0,
        'BURST_COMPENSATION_UP_3_0'
      ],
      [
        BurstCompensationEnum.burstCompensationUp_3_3,
        'BURST_COMPENSATION_UP_3_3'
      ],
      [
        BurstCompensationEnum.burstCompensationUp_3_7,
        'BURST_COMPENSATION_UP_3_7'
      ],
      [
        BurstCompensationEnum.burstCompensationUp_4_0,
        'BURST_COMPENSATION_UP_4_0'
      ],
      [
        BurstCompensationEnum.burstCompensationUp_4_3,
        'BURST_COMPENSATION_UP_4_3'
      ],
      [
        BurstCompensationEnum.burstCompensationUp_4_7,
        'BURST_COMPENSATION_UP_4_7'
      ],
      [
        BurstCompensationEnum.burstCompensationUp_5_0,
        'BURST_COMPENSATION_UP_5_0'
      ],
    ];
    expect(data.length, BurstCompensationEnum.values.length,
        reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('BurstMaxExposureTimeEnum', () async {
    List<List<dynamic>> data = [
      [BurstMaxExposureTimeEnum.maxExposureTime_0_5, 'MAX_EXPOSURE_TIME_0_5'],
      [
        BurstMaxExposureTimeEnum.maxExposureTime_0_625,
        'MAX_EXPOSURE_TIME_0_625'
      ],
      [
        BurstMaxExposureTimeEnum.maxExposureTime_0_76923076,
        'MAX_EXPOSURE_TIME_0_76923076'
      ],
      [BurstMaxExposureTimeEnum.maxExposureTime_1, 'MAX_EXPOSURE_TIME_1'],
      [BurstMaxExposureTimeEnum.maxExposureTime_1_3, 'MAX_EXPOSURE_TIME_1_3'],
      [BurstMaxExposureTimeEnum.maxExposureTime_1_6, 'MAX_EXPOSURE_TIME_1_6'],
      [BurstMaxExposureTimeEnum.maxExposureTime_2, 'MAX_EXPOSURE_TIME_2'],
      [BurstMaxExposureTimeEnum.maxExposureTime_2_5, 'MAX_EXPOSURE_TIME_2_5'],
      [BurstMaxExposureTimeEnum.maxExposureTime_3_2, 'MAX_EXPOSURE_TIME_3_2'],
      [BurstMaxExposureTimeEnum.maxExposureTime_4, 'MAX_EXPOSURE_TIME_4'],
      [BurstMaxExposureTimeEnum.maxExposureTime_5, 'MAX_EXPOSURE_TIME_5'],
      [BurstMaxExposureTimeEnum.maxExposureTime_6, 'MAX_EXPOSURE_TIME_6'],
      [BurstMaxExposureTimeEnum.maxExposureTime_8, 'MAX_EXPOSURE_TIME_8'],
      [BurstMaxExposureTimeEnum.maxExposureTime_10, 'MAX_EXPOSURE_TIME_10'],
      [BurstMaxExposureTimeEnum.maxExposureTime_13, 'MAX_EXPOSURE_TIME_13'],
      [BurstMaxExposureTimeEnum.maxExposureTime_15, 'MAX_EXPOSURE_TIME_15'],
      [BurstMaxExposureTimeEnum.maxExposureTime_20, 'MAX_EXPOSURE_TIME_20'],
      [BurstMaxExposureTimeEnum.maxExposureTime_25, 'MAX_EXPOSURE_TIME_25'],
      [BurstMaxExposureTimeEnum.maxExposureTime_30, 'MAX_EXPOSURE_TIME_30'],
      [BurstMaxExposureTimeEnum.maxExposureTime_40, 'MAX_EXPOSURE_TIME_40'],
      [BurstMaxExposureTimeEnum.maxExposureTime_50, 'MAX_EXPOSURE_TIME_50'],
      [BurstMaxExposureTimeEnum.maxExposureTime_60, 'MAX_EXPOSURE_TIME_60'],
    ];
    expect(data.length, BurstMaxExposureTimeEnum.values.length,
        reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('BurstEnableIsoControlEnum', () async {
    List<List<dynamic>> data = [
      [BurstEnableIsoControlEnum.off, 'OFF'],
      [BurstEnableIsoControlEnum.on, 'ON'],
    ];
    expect(data.length, BurstEnableIsoControlEnum.values.length,
        reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('BurstOrderEnum', () async {
    List<List<dynamic>> data = [
      [BurstOrderEnum.burstBracketOrder_0, 'BURST_BRACKET_ORDER_0'],
      [BurstOrderEnum.burstBracketOrder_1, 'BURST_BRACKET_ORDER_1'],
    ];
    expect(data.length, BurstOrderEnum.values.length, reason: 'enum count');
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

  test('ContinuousNumberEnum', () async {
    List<List<dynamic>> data = [
      [ContinuousNumberEnum.off, 'OFF'],
      [ContinuousNumberEnum.max1, 'MAX_1'],
      [ContinuousNumberEnum.max2, 'MAX_2'],
      [ContinuousNumberEnum.max3, 'MAX_3'],
      [ContinuousNumberEnum.max4, 'MAX_4'],
      [ContinuousNumberEnum.max5, 'MAX_5'],
      [ContinuousNumberEnum.max6, 'MAX_6'],
      [ContinuousNumberEnum.max7, 'MAX_7'],
      [ContinuousNumberEnum.max8, 'MAX_8'],
      [ContinuousNumberEnum.max9, 'MAX_9'],
      [ContinuousNumberEnum.max10, 'MAX_10'],
      [ContinuousNumberEnum.max11, 'MAX_11'],
      [ContinuousNumberEnum.max12, 'MAX_12'],
      [ContinuousNumberEnum.max13, 'MAX_13'],
      [ContinuousNumberEnum.max14, 'MAX_14'],
      [ContinuousNumberEnum.max15, 'MAX_15'],
      [ContinuousNumberEnum.max16, 'MAX_16'],
      [ContinuousNumberEnum.max17, 'MAX_17'],
      [ContinuousNumberEnum.max18, 'MAX_18'],
      [ContinuousNumberEnum.max19, 'MAX_19'],
      [ContinuousNumberEnum.max20, 'MAX_20'],
      [ContinuousNumberEnum.unsupported, 'UNSUPPORTED'],
    ];
    expect(data.length, ContinuousNumberEnum.values.length,
        reason: 'enum count');
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
    expect(data.length, ShootingFunctionEnum.values.length,
        reason: 'enum count');
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
    expect(data.length, MicrophoneOptionEnum.values.length,
        reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('CameraErrorEnum', () async {
    List<List<dynamic>> data = [
      [CameraErrorEnum.unknown, 'UNKNOWN'],
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

  test('AiAutoThumbnailEnum', () async {
    List<List<dynamic>> data = [
      [AiAutoThumbnailEnum.on, 'ON'],
      [AiAutoThumbnailEnum.off, 'OFF'],
    ];
    expect(data.length, AiAutoThumbnailEnum.values.length,
        reason: 'enum count');
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
    expect(data.length, CameraControlSourceEnum.values.length,
        reason: 'enum count');
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
      [CaptureModeEnum.liveStreaming, 'LIVE_STREAMING'],
      [CaptureModeEnum.interval, 'INTERVAL'],
      [CaptureModeEnum.preset, 'PRESET'],
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
    expect(data.length, ExposureCompensationEnum.values.length,
        reason: 'enum count');
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
    expect(data.length, ExposureProgramEnum.values.length,
        reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('FaceDetectEnum', () async {
    List<List<dynamic>> data = [
      [FaceDetectEnum.on, 'ON'],
      [FaceDetectEnum.off, 'OFF'],
    ];
    expect(data.length, FaceDetectEnum.values.length, reason: 'enum count');
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

  test('ImageStitchingEnum', () async {
    List<List<dynamic>> data = [
      [ImageStitchingEnum.auto, 'AUTO'],
      [ImageStitchingEnum.static, 'STATIC'],
      [ImageStitchingEnum.dynamic, 'DYNAMIC'],
      [ImageStitchingEnum.dynamicAuto, 'DYNAMIC_AUTO'],
      [ImageStitchingEnum.dynamicSemiAuto, 'DYNAMIC_SEMI_AUTO'],
      [ImageStitchingEnum.dynamicSave, 'DYNAMIC_SAVE'],
      [ImageStitchingEnum.dynamicLoad, 'DYNAMIC_LOAD'],
      [ImageStitchingEnum.none, 'NONE'],
    ];
    expect(data.length, ImageStitchingEnum.values.length, reason: 'enum count');
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
    expect(data.length, PhotoFileFormatEnum.values.length,
        reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('VideoFileFormatEnum', () async {
    List<List<dynamic>> data = [
      [VideoFileFormatEnum.videoHD, 'VIDEO_HD'],
      [VideoFileFormatEnum.videoFullHD, 'VIDEO_FULL_HD'],
      [VideoFileFormatEnum.video_2K, 'VIDEO_2K'],
      [VideoFileFormatEnum.video_2KnoCodec, 'VIDEO_2K_NO_CODEC'],
      [VideoFileFormatEnum.video_4K, 'VIDEO_4K'],
      [VideoFileFormatEnum.video_4KnoCodec, 'VIDEO_4K_NO_CODEC'],
      [VideoFileFormatEnum.video_2K_30F, 'VIDEO_2K_30F'],
      [VideoFileFormatEnum.video_2K_60F, 'VIDEO_2K_60F'],
      [VideoFileFormatEnum.video_2_7K_2752_2F, 'VIDEO_2_7K_2752_2F'],
      [VideoFileFormatEnum.video_2_7K_2752_5F, 'VIDEO_2_7K_2752_5F'],
      [VideoFileFormatEnum.video_2_7K_2752_10F, 'VIDEO_2_7K_2752_10F'],
      [VideoFileFormatEnum.video_2_7K_2752_30F, 'VIDEO_2_7K_2752_30F'],
      [VideoFileFormatEnum.video_2_7K_1F, 'VIDEO_2_7K_1F'],
      [VideoFileFormatEnum.video_2_7K_2F, 'VIDEO_2_7K_2F'],
      [VideoFileFormatEnum.video_3_6K_1F, 'VIDEO_3_6K_1F'],
      [VideoFileFormatEnum.video_3_6K_2F, 'VIDEO_3_6K_2F'],
      [VideoFileFormatEnum.video_4K_10F, 'VIDEO_4K_10F'],
      [VideoFileFormatEnum.video_4K_15F, 'VIDEO_4K_15F'],
      [VideoFileFormatEnum.video_4K_30F, 'VIDEO_4K_30F'],
      [VideoFileFormatEnum.video_4K_60F, 'VIDEO_4K_60F'],
      [VideoFileFormatEnum.video_5_7K_2F, 'VIDEO_5_7K_2F'],
      [VideoFileFormatEnum.video_5_7K_5F, 'VIDEO_5_7K_5F'],
      [VideoFileFormatEnum.video_5_7K_10F, 'VIDEO_5_7K_10F'],
      [VideoFileFormatEnum.video_5_7K_15F, 'VIDEO_5_7K_15F'],
      [VideoFileFormatEnum.video_5_7K_30F, 'VIDEO_5_7K_30F'],
      [VideoFileFormatEnum.video_7K_2F, 'VIDEO_7K_2F'],
      [VideoFileFormatEnum.video_7K_5F, 'VIDEO_7K_5F'],
      [VideoFileFormatEnum.video_7K_10F, 'VIDEO_7K_10F'],
    ];
    expect(data.length, VideoFileFormatEnum.values.length,
        reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('FilterEnum', () async {
    List<List<dynamic>> data = [
      [FilterEnum.off, 'OFF'],
      [FilterEnum.drComp, 'DR_COMP'],
      [FilterEnum.noiseReduction, 'NOISE_REDUCTION'],
      [FilterEnum.hdr, 'HDR'],
      [FilterEnum.hhHdr, 'HH_HDR'],
    ];
    expect(data.length, FilterEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('GainEnum', () async {
    List<List<dynamic>> data = [
      [GainEnum.normal, 'NORMAL'],
      [GainEnum.megaVolume, 'MEGA_VOLUME'],
      [GainEnum.mute, 'MUTE'],
    ];
    expect(data.length, GainEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('GpsTagRecordingEnum', () async {
    List<List<dynamic>> data = [
      [GpsTagRecordingEnum.on, 'ON'],
      [GpsTagRecordingEnum.off, 'OFF'],
    ];
    expect(data.length, GpsTagRecordingEnum.values.length,
        reason: 'enum count');
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
    expect(data.length, IsoAutoHighLimitEnum.values.length,
        reason: 'enum count');
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

  test('PresetEnum', () async {
    List<List<dynamic>> data = [
      [PresetEnum.face, 'FACE'],
      [PresetEnum.nightView, 'NIGHT_VIEW'],
      [PresetEnum.lensByLensExposure, 'LENS_BY_LENS_EXPOSURE'],
      [PresetEnum.room, 'ROOM'],
    ];
    expect(data.length, PresetEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('PreviewFormatEnum', () async {
    List<List<dynamic>> data = [
      [PreviewFormatEnum.w1024_h512_f30, 'W1024_H512_F30'],
      [PreviewFormatEnum.w1024_h512_f15, 'W1024_H512_F15'],
      [PreviewFormatEnum.w512_h512_f30, 'W512_H512_F30'],
      [PreviewFormatEnum.w1920_h960_f8, 'W1920_H960_F8'],
      [PreviewFormatEnum.w1024_h512_f8, 'W1024_H512_F8'],
      [PreviewFormatEnum.w640_h320_f30, 'W640_H320_F30'],
      [PreviewFormatEnum.w640_h320_f8, 'W640_H320_F8'],
      [PreviewFormatEnum.w640_h320_f10, 'W640_H320_F10'],
      [PreviewFormatEnum.w3840_h1920_f30, 'W3840_H1920_F30'],
    ];
    expect(data.length, PreviewFormatEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('ShootingMethodEnum', () async {
    List<List<dynamic>> data = [
      [ShootingMethodEnum.normal, 'NORMAL'],
      [ShootingMethodEnum.interval, 'INTERVAL'],
      [ShootingMethodEnum.moveInterval, 'MOVE_INTERVAL'],
      [ShootingMethodEnum.fixedInterval, 'FIXED_INTERVAL'],
      [ShootingMethodEnum.bracket, 'BRACKET'],
      [ShootingMethodEnum.composite, 'COMPOSITE'],
      [ShootingMethodEnum.continuous, 'CONTINUOUS'],
      [ShootingMethodEnum.timeShift, 'TIME_SHIFT'],
      [ShootingMethodEnum.burst, 'BURST'],
    ];
    expect(data.length, ShootingMethodEnum.values.length, reason: 'enum count');
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

  test('ThetaModel', () async {
    List<List<dynamic>> data = [
      [ThetaModel.thetaS, 'THETA_S'],
      [ThetaModel.thetaSC, 'THETA_SC'],
      [ThetaModel.thetaV, 'THETA_V'],
      [ThetaModel.thetaZ1, 'THETA_Z1'],
      [ThetaModel.thetaX, 'THETA_X'],
      [ThetaModel.thetaSC2, 'THETA_SC2'],
      [ThetaModel.thetaSC2B, 'THETA_SC2_B'],
    ];
    expect(data.length, ThetaModel.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('TimeShiftIntervalEnum', () async {
    List<List<dynamic>> data = [
      [TimeShiftIntervalEnum.interval_0, 'INTERVAL_0'],
      [TimeShiftIntervalEnum.interval_1, 'INTERVAL_1'],
      [TimeShiftIntervalEnum.interval_2, 'INTERVAL_2'],
      [TimeShiftIntervalEnum.interval_3, 'INTERVAL_3'],
      [TimeShiftIntervalEnum.interval_4, 'INTERVAL_4'],
      [TimeShiftIntervalEnum.interval_5, 'INTERVAL_5'],
      [TimeShiftIntervalEnum.interval_6, 'INTERVAL_6'],
      [TimeShiftIntervalEnum.interval_7, 'INTERVAL_7'],
      [TimeShiftIntervalEnum.interval_8, 'INTERVAL_8'],
      [TimeShiftIntervalEnum.interval_9, 'INTERVAL_9'],
      [TimeShiftIntervalEnum.interval_10, 'INTERVAL_10'],
    ];
    expect(data.length, TimeShiftIntervalEnum.values.length,
        reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('TopBottomCorrectionOptionEnum', () async {
    List<List<dynamic>> data = [
      [TopBottomCorrectionOptionEnum.apply, 'APPLY'],
      [TopBottomCorrectionOptionEnum.applyAuto, 'APPLY_AUTO'],
      [TopBottomCorrectionOptionEnum.applySemiauto, 'APPLY_SEMIAUTO'],
      [TopBottomCorrectionOptionEnum.applySave, 'APPLY_SAVE'],
      [TopBottomCorrectionOptionEnum.applyLoad, 'APPLY_LOAD'],
      [TopBottomCorrectionOptionEnum.disapply, 'DISAPPLY'],
      [TopBottomCorrectionOptionEnum.manual, 'MANUAL'],
    ];
    expect(data.length, TopBottomCorrectionOptionEnum.values.length,
        reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('VideoStitchingEnum', () async {
    List<List<dynamic>> data = [
      [VideoStitchingEnum.none, 'NONE'],
      [VideoStitchingEnum.ondevice, 'ONDEVICE'],
    ];
    expect(data.length, VideoStitchingEnum.values.length, reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });

  test('VisibilityReductionEnum', () async {
    List<List<dynamic>> data = [
      [VisibilityReductionEnum.on, 'ON'],
      [VisibilityReductionEnum.off, 'OFF'],
    ];
    expect(data.length, VisibilityReductionEnum.values.length,
        reason: 'enum count');
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
    expect(data.length, WhiteBalanceAutoStrengthEnum.values.length,
        reason: 'enum count');
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

  test('CapturingStatusEnum', () async {
    List<List<dynamic>> data = [
      [CapturingStatusEnum.capturing, 'CAPTURING'],
      [CapturingStatusEnum.selfTimerCountdown, 'SELF_TIMER_COUNTDOWN'],
    ];
    expect(data.length, CapturingStatusEnum.values.length,
        reason: 'enum count');
    for (int i = 0; i < data.length; i++) {
      expect(data[i][0].toString(), data[i][1], reason: data[i][1]);
    }
  });
}
