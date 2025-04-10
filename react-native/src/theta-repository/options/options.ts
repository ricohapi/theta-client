import type { AiAutoThumbnailEnum } from './option-ai-auto-thumbnail';
import type { BitrateEnum } from './option-bitrate';
import type { BluetoothRoleEnum } from './option-bluetooth-role';
import type { BracketSetting } from './option-auto-bracket';
import type { BurstModeEnum } from './option-burst-mode';
import type { BurstOption } from './option-burst-option';
import type { CameraControlSourceEnum } from './option-camera-control-source';
import type { CameraModeEnum } from './option-camera-mode';
import type { CaptureModeEnum } from './option-capture-mode';
import type { ContinuousNumberEnum } from './option-continuous-number';
import type { FaceDetectEnum } from './option-face-detect';
import type { FilterEnum } from './option-filter';
import type { ShootingFunctionEnum } from './option-function';
import type { GainEnum } from './option-gain';
import type { ImageStitchingEnum } from './option-image-stitching';
import type { MaxRecordableTimeEnum } from './option-max-recordable-time';
import type { NetworkTypeEnum } from './option-network-type';
import type { PowerSavingEnum } from './option-power-saving';
import type { PresetEnum } from './option-preset';
import type { PreviewFormatEnum } from './option-preview-format';
import type { Proxy } from './option-proxy';
import type { ShootingMethodEnum } from './option-shooting-method';
import type { ShutterSpeedEnum } from './option-shutter-speed';
import type { TimeShift } from './option-time-shift';
import type { TopBottomCorrectionOptionEnum } from './option-top-bottom-correction';
import type { TopBottomCorrectionRotation } from './option-top-bottom-correction-rotation';
import type { VideoStitchingEnum } from './option-video-stitching';
import type { VisibilityReductionEnum } from './option-visibility-reduction';
import type { WhiteBalanceAutoStrengthEnum } from './option-white-balance-auto-strength';
import type { WlanFrequencyEnum } from './option-wlan-frequency';
import type { OffDelayEnum } from './option-off-delay';
import type { SleepDelayEnum } from './option-sleep-delay';
import type { EthernetConfig } from './option-ethernet-config';
import type { FileFormatEnum } from './option-file-format';
import type { CameraPowerEnum } from './option-camera-power';
import type { ColorTemperatureSupport } from './option-color-temperature-support';
import type { TopBottomCorrectionRotationSupport } from './option-top-bottom-correction-rotation-support';
import type { GpsTagRecordingEnum } from './option-gps-tag-recording';

/** Aperture value. */
export const ApertureEnum = {
  /** AUTO(0) */
  APERTURE_AUTO: 'APERTURE_AUTO',
  /** 2.0F RICOH THETA V or prior */
  APERTURE_2_0: 'APERTURE_2_0',
  /** 2.1F RICOH THETA Z1 and the exposure program (exposureProgram) is set to Manual or Aperture Priority */
  APERTURE_2_1: 'APERTURE_2_1',
  /** 2.4F RICOH THETA X or later */
  APERTURE_2_4: 'APERTURE_2_4',
  /** 3.5F RICOH THETA Z1 and the exposure program (exposureProgram) is set to Manual or Aperture Priority */
  APERTURE_3_5: 'APERTURE_3_5',
  /** 5.6F RICOH THETA Z1 and the exposure program (exposureProgram) is set to Manual or Aperture Priority */
  APERTURE_5_6: 'APERTURE_5_6',
} as const;

/** type definition of ApertureEnum */
export type ApertureEnum = (typeof ApertureEnum)[keyof typeof ApertureEnum];

/** BluetoothPower value. */
export const BluetoothPowerEnum = {
  /** ON */
  ON: 'ON',
  /** OFF */
  OFF: 'OFF',
} as const;

/** type definition of BluetoothPowerEnum */
export type BluetoothPowerEnum =
  (typeof BluetoothPowerEnum)[keyof typeof BluetoothPowerEnum];

/** Exposure compensation (EV). */
export const ExposureCompensationEnum = {
  /** -2.0 */
  M_2_0: 'M2_0',
  /** -1.7 */
  M_1_7: 'M1_7',
  /** -1.3 */
  M_1_3: 'M1_3',
  /** -1.0 */
  M_1_0: 'M1_0',
  /** -0.7 */
  M_0_7: 'M0_7',
  /** -0.3 */
  M_0_3: 'M0_3',
  /** 0 */
  ZERO: 'ZERO',
  /** 0.3 */
  P_0_3: 'P0_3',
  /** 0.7 */
  P_0_7: 'P0_7',
  /** 1.0 */
  P_1_0: 'P1_0',
  /** 1.3 */
  P_1_3: 'P1_3',
  /** 1.7 */
  P_1_7: 'P1_7',
  /** 2.0 */
  P_2_0: 'P2_0',
} as const;

/** type definition of ExposureCompensationEnum */
export type ExposureCompensationEnum =
  (typeof ExposureCompensationEnum)[keyof typeof ExposureCompensationEnum];

/** Operating time (sec.) of the self-timer. */
export const ExposureDelayEnum = {
  /** Disable self-timer. */
  DELAY_OFF: 'DELAY_OFF',
  /** a second */
  DELAY_1: 'DELAY_1',
  /** 2 seconds */
  DELAY_2: 'DELAY_2',
  /** 3 seconds */
  DELAY_3: 'DELAY_3',
  /** 4 seconds */
  DELAY_4: 'DELAY_4',
  /** 5 seconds */
  DELAY_5: 'DELAY_5',
  /** 6 seconds */
  DELAY_6: 'DELAY_6',
  /** 7 seconds */
  DELAY_7: 'DELAY_7',
  /** 8 seconds */
  DELAY_8: 'DELAY_8',
  /** 9 seconds */
  DELAY_9: 'DELAY_9',
  /** 10 seconds */
  DELAY_10: 'DELAY_10',
} as const;

/** type definition of ExposureDelayEnum */
export type ExposureDelayEnum =
  (typeof ExposureDelayEnum)[keyof typeof ExposureDelayEnum];

/** Exposure program. The exposure settings that take priority can be selected. */
export const ExposureProgramEnum = {
  /** Manual program */
  MANUAL: 'MANUAL',
  /** Normal program */
  NORMAL_PROGRAM: 'NORMAL_PROGRAM',
  /** Aperture priority program */
  APERTURE_PRIORITY: 'APERTURE_PRIORITY',
  /** Shutter priority program */
  SHUTTER_PRIORITY: 'SHUTTER_PRIORITY',
  /** ISO priority program */
  ISO_PRIORITY: 'ISO_PRIORITY',
} as const;

/** type definition of ExposureProgramEnum */
export type ExposureProgramEnum =
  (typeof ExposureProgramEnum)[keyof typeof ExposureProgramEnum];

/** GPS information */
export type GpsInfo = {
  /** Latitude (-90.000000 – 90.000000) */
  latitude: number;
  /** Longitude (-180.000000 – 180.000000) */
  longitude: number;
  /** Altitude (meters) */
  altitude: number;
  /** Location information acquisition time */
  dateTimeZone: string;
};

/** ISO sensitivity. */
export const IsoEnum = {
  /** AUTO (0) */
  ISO_AUTO: 'ISO_AUTO',
  /** 50 */
  ISO_50: 'ISO_50',
  /** 64 */
  ISO_64: 'ISO_64',
  /** 80 */
  ISO_80: 'ISO_80',
  /** 100 */
  ISO_100: 'ISO_100',
  /** 125 */
  ISO_125: 'ISO_125',
  /** 160 */
  ISO_160: 'ISO_160',
  /** 200 */
  ISO_200: 'ISO_200',
  /** 250 */
  ISO_250: 'ISO_250',
  /** 320 */
  ISO_320: 'ISO_320',
  /** 400 */
  ISO_400: 'ISO_400',
  /** 500 */
  ISO_500: 'ISO_500',
  /** 640 */
  ISO_640: 'ISO_640',
  /** 800 */
  ISO_800: 'ISO_800',
  /** 1000 */
  ISO_1000: 'ISO_1000',
  /** 1250 */
  ISO_1250: 'ISO_1250',
  /** 1600 */
  ISO_1600: 'ISO_1600',
  /** 2000 */
  ISO_2000: 'ISO_2000',
  /** 2500 */
  ISO_2500: 'ISO_2500',
  /** 3200 */
  ISO_3200: 'ISO_3200',
  /** 4000 */
  ISO_4000: 'ISO_4000',
  /** 5000 */
  ISO_5000: 'ISO_5000',
  /** 6400 */
  ISO_6400: 'ISO_6400',
} as const;

/** type definition IsoEnum */
export type IsoEnum = (typeof IsoEnum)[keyof typeof IsoEnum];

/** ISO sensitivity upper limit when ISO sensitivity is set to automatic. */
export const IsoAutoHighLimitEnum = {
  /** 100 */
  ISO_100: 'ISO_100',
  /** 125 */
  ISO_125: 'ISO_125',
  /** 160 */
  ISO_160: 'ISO_160',
  /** 200 */
  ISO_200: 'ISO_200',
  /** 250 */
  ISO_250: 'ISO_250',
  /** 320 */
  ISO_320: 'ISO_320',
  /** 400 */
  ISO_400: 'ISO_400',
  /** 500 */
  ISO_500: 'ISO_500',
  /** 640 */
  ISO_640: 'ISO_640',
  /** 800 */
  ISO_800: 'ISO_800',
  /** 1000 */
  ISO_1000: 'ISO_1000',
  /** 1250 */
  ISO_1250: 'ISO_1250',
  /** 1600 */
  ISO_1600: 'ISO_1600',
  /** 2000 */
  ISO_2000: 'ISO_2000',
  /** 2500 */
  ISO_2500: 'ISO_2500',
  /** 3200 */
  ISO_3200: 'ISO_3200',
  /** 4000 */
  ISO_4000: 'ISO_4000',
  /** 5000 */
  ISO_5000: 'ISO_5000',
  /** 6400 */
  ISO_6400: 'ISO_6400',
} as const;

/** type definition of IsoAutoHighLimitEnum */
export type IsoAutoHighLimitEnum =
  (typeof IsoAutoHighLimitEnum)[keyof typeof IsoAutoHighLimitEnum];

/** language */
export const LanguageEnum = {
  /** german */
  DE: 'DE',
  /** british english */
  EN_GB: 'EN_GB',
  /** us english */
  EN_US: 'EN_US',
  /** french */
  FR: 'FR',
  /** italian */
  IT: 'IT',
  /** japanese */
  JA: 'JA',
  /** korean */
  KO: 'KO',
  /** chinese */
  ZH_CN: 'ZH_CH',
  /** taiwan chinese */
  ZH_TW: 'ZH_TW',
} as const;

/** type definition of LanguageEnum */
export type LanguageEnum = (typeof LanguageEnum)[keyof typeof LanguageEnum];

/** White balance. */
export const WhiteBalanceEnum = {
  /** Automatic */
  AUTO: 'AUTO',
  /** Outdoor */
  DAYLIGHT: 'DAYLIGHT',
  /** Shade */
  SHADE: 'SHADE',
  /** Cloudy */
  CLOUDY_DAYLIGHT: 'CLOUDY_DAYLIGHT',
  /** Incandescent light 1 */
  INCANDESCENT: 'INCANDESCENT',
  /** Incandescent light 2 */
  WARM_WHITE_FLUORESCENT: 'WARM_WHITE_FLUORESCENT',
  /** Fluorescent light 1 (daylight) */
  DAYLIGHT_FLUORESCENT: 'DAYLIGHT_FLUORESCENT',
  /** Fluorescent light 2 (natural white) */
  DAYWHITE_FLUORESCENT: 'DAYWHITE_FLUORESCENT',
  /** Fluorescent light 3 (white) */
  FLUORESCENT: 'FLUORESCENT',
  /** Fluorescent light 4 (light bulb color) */
  BULB_FLUORESCENT: 'BULB_FLUORESCENT',
  /** CT settings (specified by the _colorTemperature option) */
  COLOR_TEMPERATURE: 'COLOR_TEMPERATURE',
  /** Underwater */
  UNDERWATER: 'UNDERWATER',
} as const;

/** type definition of WhiteBalanceEnum */
export type WhiteBalanceEnum =
  (typeof WhiteBalanceEnum)[keyof typeof WhiteBalanceEnum];

/** Camera setting options name. */
export const OptionNameEnum = {
  /** _aiAutoThumbnail */
  AiAutoThumbnail: 'AiAutoThumbnail',
  /** _aiAutoThumbnailSupport */
  AiAutoThumbnailSupport: 'AiAutoThumbnailSupport',
  /** aperture */
  Aperture: 'Aperture',
  /** autoBracket */
  AutoBracket: 'AutoBracket',
  /** _bitrate*/
  Bitrate: 'Bitrate',
  /** _bluetoothPower*/
  BluetoothPower: 'BluetoothPower',
  /** _bluetoothRole*/
  BluetoothRole: 'BluetoothRole',
  /** _burstMode*/
  BurstMode: 'BurstMode',
  /** _burstOption*/
  BurstOption: 'BurstOption',
  /** _cameraControlSource */
  CameraControlSource: 'CameraControlSource',
  /** cameraMode */
  CameraMode: 'CameraMode',
  /** cameraPower */
  CameraPower: 'CameraPower',
  /** captureInterval */
  CaptureInterval: 'CaptureInterval',
  /** captureMode */
  CaptureMode: 'CaptureMode',
  /** captureNumber */
  CaptureNumber: 'CaptureNumber',
  /** colorTemperature */
  ColorTemperature: 'ColorTemperature',
  /** _colorTemperatureSupport */
  ColorTemperatureSupport: 'ColorTemperatureSupport',
  /** _compositeShootingOutputInterval */
  CompositeShootingOutputInterval: 'CompositeShootingOutputInterval',
  /** _compositeShootingTime */
  CompositeShootingTime: 'CompositeShootingTime',
  /** continuousNumber */
  ContinuousNumber: 'ContinuousNumber',
  /** dateTimeZone */
  DateTimeZone: 'DateTimeZone',
  /** ethernetConfig */
  EthernetConfig: 'EthernetConfig',
  /** exposureCompensation */
  ExposureCompensation: 'ExposureCompensation',
  /** exposureDelay */
  ExposureDelay: 'ExposureDelay',
  /** exposureProgram */
  ExposureProgram: 'ExposureProgram',
  /** _faceDetect */
  FaceDetect: 'FaceDetect',
  /** fileFormat */
  FileFormat: 'FileFormat',
  /** filter */
  Filter: 'Filter',
  /** _function */
  Function: 'Function',
  /** _gain */
  Gain: 'Gain',
  /** gpsInfo */
  GpsInfo: 'GpsInfo',
  /** _gpsTagRecordingSupport */
  GpsTagRecordingSupport: 'GpsTagRecordingSupport',
  /** imageStitching */
  ImageStitching: 'ImageStitching',
  /** isGpsOn */
  IsGpsOn: 'IsGpsOn',
  /** iso */
  Iso: 'Iso',
  /** isoAutoHighLimit */
  IsoAutoHighLimit: 'IsoAutoHighLimit',
  /** language */
  Language: 'Language',
  /** _latestEnabledExposureDelayTime */
  LatestEnabledExposureDelayTime: 'LatestEnabledExposureDelayTime',
  /** maxRecordableTime */
  MaxRecordableTime: 'MaxRecordableTime',
  /** networkType */
  NetworkType: 'NetworkType',
  /** offDelay */
  OffDelay: 'OffDelay',
  /** password */
  Password: 'Password',
  /** powerSaving */
  PowerSaving: 'PowerSaving',
  /** preset */
  Preset: 'Preset',
  /** previewFormat */
  PreviewFormat: 'PreviewFormat',
  /** proxy */
  Proxy: 'Proxy',
  /** shootingMethod */
  ShootingMethod: 'ShootingMethod',
  /** shutterSpeed */
  ShutterSpeed: 'ShutterSpeed',
  /** sleepDelay */
  SleepDelay: 'SleepDelay',
  /** remainingPictures */
  RemainingPictures: 'RemainingPictures',
  /** remainingVideoSeconds */
  RemainingVideoSeconds: 'RemainingVideoSeconds',
  /** remainingSpace */
  RemainingSpace: 'RemainingSpace',
  /** timeShift */
  TimeShift: 'TimeShift',
  /** topBottomCorrection */
  TopBottomCorrection: 'TopBottomCorrection',
  /** topBottomCorrectionRotation */
  TopBottomCorrectionRotation: 'TopBottomCorrectionRotation',
  /** topBottomCorrectionRotationSupport */
  TopBottomCorrectionRotationSupport: 'TopBottomCorrectionRotationSupport',
  /** totalSpace */
  TotalSpace: 'TotalSpace',
  /** shutterVolume */
  ShutterVolume: 'ShutterVolume',
  /** username */
  Username: 'Username',
  /** videoStitching */
  VideoStitching: 'VideoStitching',
  /** _visibilityReduction */
  VisibilityReduction: 'VisibilityReduction',
  /** whiteBalance */
  WhiteBalance: 'WhiteBalance',
  /** _whiteBalanceAutoStrength */
  WhiteBalanceAutoStrength: 'WhiteBalanceAutoStrength',
  /** _wlanFrequency */
  WlanFrequency: 'WlanFrequency',
} as const;

/** type definition of OptionNameEnum */
export type OptionNameEnum =
  (typeof OptionNameEnum)[keyof typeof OptionNameEnum];

/** camera setting options */
export type Options = {
  /** AI auto thumbnail setting. */
  aiAutoThumbnail?: AiAutoThumbnailEnum;
  /** Supported AI auto thumbnail setting. */
  aiAutoThumbnailSupport?: AiAutoThumbnailEnum[];
  /** Aperture value. */
  aperture?: ApertureEnum;
  /**  */
  autoBracket?: BracketSetting[];
  /** Bitrate */
  bitrate?: BitrateEnum;
  /** BluetoothPower */
  bluetoothPower?: BluetoothPowerEnum;
  /** BluetoothRole */
  bluetoothRole?: BluetoothRoleEnum;
  /** BurstMode */
  burstMode?: BurstModeEnum;
  /** BurstOption */
  burstOption?: BurstOption;
  /** camera control source. */
  cameraControlSource?: CameraControlSourceEnum;
  /** Camera mode. */
  cameraMode?: CameraModeEnum;
  /** Camera power state */
  cameraPower?: CameraPowerEnum;
  /**
   * Shooting interval (sec.) for interval shooting.
   *
   * ### Support value
   * The value that can be set differs depending on the image format ([fileFormat]) to be shot.
   * #### For RICOH THETA X or later
   * | Image format | Image size  | Support value |
   * | ------------ | ----------- | ------------- |
   * | JPEG         | 11008 x 5504 <br>5504 x 2752 | Minimum value(minInterval):6 <br>Maximum value(maxInterval):3600 |
   *
   * #### For RICOH THETA Z1
   * | Image format | Image size  | Support value |
   * | ------------ | ----------- | ------------- |
   * | JPEG         | 6720 x 3360 | Minimum value(minInterval):6 <br>Maximum value(maxInterval):3600 |
   * | RAW+         | 6720 x 3360 | Minimum value(minInterval):10 <br>Maximum value(maxInterval):3600 |
   *
   * #### For RICOH THETA V
   * | Image format | Image size  | Support value |
   * | ------------ | ----------- | ------------- |
   * | JPEG         | 5376 x 2688 | Minimum value(minInterval):4 <br>Maximum value(maxInterval):3600 |
   *
   * #### For RICOH THETA S or SC
   * | Image format | Image size  | Support value |
   * | ------------ | ----------- | ------------- |
   * | JPEG         | 5376 x 2688 | Minimum value(minInterval):8 <br>Maximum value(maxInterval):3600 |
   * | JPEG         | 2048 x 1024 | Minimum value(minInterval):5 <br>Maximum value(maxInterval):3600 |
   */
  captureInterval?: number;
  /** Shooting mode. */
  captureMode?: CaptureModeEnum;
  /**
   * Number of shots for interval shooting.
   *
   * ### Support value
   * - 0: Unlimited (_limitless)
   * - 2: Minimum value (minNumber)
   * - 9999: Maximum value (maxNumber)
   */
  captureNumber?: number;
  /** Color temperature of the camera (Kelvin). */
  colorTemperature?: number;
  /**
   * In-progress save interval for interval composite shooting (sec).
   *
   * 0 (no saving), 60 to 600. In 60-second units.
   *
   * For
   * RICOH THETA Z1
   * RICOH THETA SC firmware v1.10 or later
   * RICOH THETA S firmware v01.82 or later
   */
  compositeShootingOutputInterval?: number;
  /**
   * Shooting time for interval composite shooting (sec).
   *
   * 600 to 86400. In 600-second units.
   *
   * For
   * RICOH THETA Z1
   * RICOH THETA SC firmware v1.10 or later
   * RICOH THETA S firmware v01.82 or later
   */
  compositeShootingTime?: number;
  /** Number of shots for continuous shooting. */
  continuousNumber?: ContinuousNumberEnum;
  /** supported color temperature. */
  colorTemperatureSupport?: ColorTemperatureSupport;
  /** Current system time of RICOH THETA. Setting another options will result in an error. */
  dateTimeZone?: string;
  /** IP address allocation to be used when wired LAN is enabled. */
  ethernetConfig?: EthernetConfig;
  /** Exposure compensation (EV). */
  exposureCompensation?: ExposureCompensationEnum;
  /** Operating time (sec.) of the self-timer. */
  exposureDelay?: ExposureDelayEnum;
  /** Exposure program. The exposure settings that take priority can be selected. */
  exposureProgram?: ExposureProgramEnum;
  /** Face detection */
  faceDetect?: FaceDetectEnum;
  /** Image format used in shooting. */
  fileFormat?: FileFormatEnum;
  /** Image processing filter. */
  filter?: FilterEnum;
  /** Shooting function. */
  function?: ShootingFunctionEnum;
  /** Microphone gain. */
  gain?: GainEnum;
  /** GPS location information. */
  gpsInfo?: GpsInfo;
  /**
   * Supported GpsTagRecording
   * For THETA X
   */
  gpsTagRecordingSupport?: GpsTagRecordingEnum[];
  /** Still image stitching setting during shooting. */
  imageStitching?: ImageStitchingEnum;
  /** Turns position information assigning ON/OFF. */
  isGpsOn?: boolean;
  /** ISO sensitivity */
  iso?: IsoEnum;
  /** ISO sensitivity upper limit when ISO sensitivity is set to automatic. */
  isoAutoHighLimit?: IsoAutoHighLimitEnum;
  /** Language used in camera OS. */
  language?: LanguageEnum;
  /** Self-timer operating time (sec.) when the self-timer (exposureDelay) was effective. */
  latestEnabledExposureDelayTime?: ExposureDelayEnum;
  /** Maximum recordable time (in seconds) of the camera. */
  maxRecordableTime?: MaxRecordableTimeEnum;
  /** Network type of the camera */
  networkType?: NetworkTypeEnum;
  /** Length of standby time before the camera automatically powers OFF. */
  offDelay?: OffDelayEnum;
  /** Password used for digest authentication when _networkType is set to client mode. */
  password?: String;
  /** Power saving mode */
  powerSaving?: PowerSavingEnum;
  /** Preset mode */
  preset?: PresetEnum;
  /** Format of live view  */
  previewFormat?: PreviewFormatEnum;
  /** Proxy information to be used when wired LAN is enabled. */
  proxy?: Proxy;
  /** The estimated remaining number of shots for the current shooting settings. */
  remainingPictures?: number;
  /** The estimated remaining shooting time (sec.) for the current video shooting settings. */
  remainingVideoSeconds?: number;
  /** Remaining usable storage space (byte). */
  remainingSpace?: number;
  /** Shooting method for My Settings mode. In RICOH THETA X, it is used outside of MySetting.  */
  shootingMethod?: ShootingMethodEnum;
  /**
   * Shutter speed (sec).
   *
   * It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
   * Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
   */
  shutterSpeed?: ShutterSpeedEnum;
  /** Shutter volume. */
  shutterVolume?: number;
  /** Length of standby time before the camera enters the sleep mode. */
  sleepDelay?: SleepDelayEnum;
  /** Time shift shooting */
  timeShift?: TimeShift;
  /** top bottom correction */
  topBottomCorrection?: TopBottomCorrectionOptionEnum;
  /**
   * Sets the front position for the top/bottom correction.
   * Enabled only for _topBottomCorrection Manual.
   */
  topBottomCorrectionRotation?: TopBottomCorrectionRotation;
  /** Supported TopBottomCorrectionRotation */
  topBottomCorrectionRotationSupport?: TopBottomCorrectionRotationSupport;
  /** Total storage space (byte). */
  totalSpace?: number;
  /** User name used for digest authentication when _networkType is set to client mode. */
  username?: String;
  /** Video stitching during shooting. */
  videoStitching?: VideoStitchingEnum;
  /** Reduction visibility of camera body to still image when stitching. */
  visibilityReduction?: VisibilityReductionEnum;
  /** White balance. */
  whiteBalance?: WhiteBalanceEnum;
  /** White balance auto strength. */
  whiteBalanceAutoStrength?: WhiteBalanceAutoStrengthEnum;
  /** WLAN frequency */
  wlanFrequency?: WlanFrequencyEnum;
  /** GPS setting used in only capturing */
  _gpsTagRecording?: GpsTagRecordingEnum;
};
