import type { AccessInfo } from './option-access-info';
import type { AiAutoThumbnailEnum } from './option-ai-auto-thumbnail';
import type { ApertureEnum } from './option-aperture';
import type { BitrateEnum } from './option-bitrate';
import type { BluetoothRoleEnum } from './option-bluetooth-role';
import type { BracketSetting } from './option-auto-bracket';
import type { BurstModeEnum } from './option-burst-mode';
import type { BurstOption } from './option-burst-option';
import type { CameraControlSourceEnum } from './option-camera-control-source';
import type { CameraLockConfig } from './option-camera-lock-config';
import type { CameraLockEnum } from './option-camera-lock';
import type { CameraModeEnum } from './option-camera-mode';
import type { CameraPowerEnum } from './option-camera-power';
import type { CaptureModeEnum } from './option-capture-mode';
import type { CompassDirectionRefEnum } from './option-compass-direction-ref';
import type { ContinuousNumberEnum } from './option-continuous-number';
import type { EthernetConfig } from './option-ethernet-config';
import type { ExposureCompensationEnum } from './option-exposure-compensation';
import type { FaceDetectEnum } from './option-face-detect';
import type { FileFormatEnum } from './option-file-format';
import type { FilterEnum } from './option-filter';
import type { GainEnum } from './option-gain';
import type { GpsTagRecordingEnum } from './option-gps-tag-recording';
import type { ImageStitchingEnum } from './option-image-stitching';
import type { MaxRecordableTimeEnum } from './option-max-recordable-time';
import type { MicrophoneNoiseReductionEnum } from './option-microphone-noise-reduction';
import type { MobileNetworkSetting } from './option-mobile-network-setting';
import type { NetworkTypeEnum } from './option-network-type';
import type { OffDelayEnum } from './option-off-delay';
import type { OffDelayUsbEnum } from './option-off-delay-usb';
import type { PowerSavingEnum } from './option-power-saving';
import type { PresetEnum } from './option-preset';
import type { PreviewFormatEnum } from './option-preview-format';
import type { Proxy } from './option-proxy';
import type { ShootingFunctionEnum } from './option-function';
import type { ShootingMethodEnum } from './option-shooting-method';
import type { ShutterSpeedEnum } from './option-shutter-speed';
import type { SleepDelayEnum } from './option-sleep-delay';
import type { TimeShift } from './option-time-shift';
import type { TopBottomCorrectionOptionEnum } from './option-top-bottom-correction';
import type { TopBottomCorrectionRotation } from './option-top-bottom-correction-rotation';
import type { TopBottomCorrectionRotationSupport } from './option-top-bottom-correction-rotation-support';
import type { UsbConnectionEnum } from './option-usb-connection';
import type { VideoStitchingEnum } from './option-video-stitching';
import type { VisibilityReductionEnum } from './option-visibility-reduction';
import type { WhiteBalanceAutoStrengthEnum } from './option-white-balance-auto-strength';
import type { WlanAntennaConfigEnum } from './option-wlan-antenna-config';
import type { WlanFrequencyClMode } from './option-wlan-frequency-cl-mode';
import type { WlanFrequencyEnum } from './option-wlan-frequency';

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
  /** _accessInfo */
  AccessInfo: 'AccessInfo',
  /** _aiAutoThumbnail */
  AiAutoThumbnail: 'AiAutoThumbnail',
  /** _aiAutoThumbnailSupport */
  AiAutoThumbnailSupport: 'AiAutoThumbnailSupport',
  /** aperture */
  Aperture: 'Aperture',
  /** apertureSupport */
  ApertureSupport: 'ApertureSupport',
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
  /** _cameraControlSourceSupport */
  CameraControlSourceSupport: 'CameraControlSourceSupport',
  /** _cameraLock */
  CameraLock: 'CameraLock',
  /** _cameraLockConfig */
  CameraLockConfig: 'CameraLockConfig',
  /** cameraMode */
  CameraMode: 'CameraMode',
  /** cameraPower */
  CameraPower: 'CameraPower',
  /** _cameraPowerSupport */
  CameraPowerSupport: 'CameraPowerSupport',
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
  /** _compassDirectionRef */
  CompassDirectionRef: 'CompassDirectionRef',
  /** _compositeShootingOutputInterval */
  CompositeShootingOutputInterval: 'CompositeShootingOutputInterval',
  /** _compositeShootingOutputIntervalSupport */
  CompositeShootingOutputIntervalSupport:
    'CompositeShootingOutputIntervalSupport',
  /** _compositeShootingTime */
  CompositeShootingTime: 'CompositeShootingTime',
  /** _compositeShootingTimeSupport */
  CompositeShootingTimeSupport: 'CompositeShootingTimeSupport',
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
  /** exposureDelaySupport */
  ExposureDelaySupport: 'ExposureDelaySupport',
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
  /** microphoneNoiseReduction */
  MicrophoneNoiseReduction: 'MicrophoneNoiseReduction',
  /** _mobileNetworkSetting */
  MobileNetworkSetting: 'MobileNetworkSetting',
  /** networkType */
  NetworkType: 'NetworkType',
  /** offDelay */
  OffDelay: 'OffDelay',
  /** _offDelayUSB */
  OffDelayUsb: 'OffDelayUsb',
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
  /** _usbConnection */
  UsbConnection: 'UsbConnection',
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
  /** _wlanAntennaConfig */
  WlanAntennaConfig: 'WlanAntennaConfig',
  /** _wlanFrequency */
  WlanFrequency: 'WlanFrequency',
  /** _wlanFrequencySupport */
  WlanFrequencySupport: 'WlanFrequencySupport',
  /** _wlanFrequencyCLmode */
  WlanFrequencyClMode: 'WlanFrequencyClMode',
} as const;

/** type definition of OptionNameEnum */
export type OptionNameEnum =
  (typeof OptionNameEnum)[keyof typeof OptionNameEnum];

/** camera setting options */
export type Options = {
  /** Connected network information. */
  accessInfo?: AccessInfo;
  /** AI auto thumbnail setting. */
  aiAutoThumbnail?: AiAutoThumbnailEnum;
  /** Supported AI auto thumbnail setting. */
  aiAutoThumbnailSupport?: AiAutoThumbnailEnum[];
  /** Aperture value. */
  aperture?: ApertureEnum;
  /** Supported aperture value. */
  apertureSupport?: ApertureEnum[];
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
  /** Supported Camera Control Source. */
  cameraControlSourceSupport?: CameraControlSourceEnum[];
  /** Camera lock. */
  cameraLock?: CameraLockEnum;
  /** Camera lock config. */
  cameraLockConfig?: CameraLockConfig;
  /** Camera mode. */
  cameraMode?: CameraModeEnum;
  /** Camera power state */
  cameraPower?: CameraPowerEnum;
  /** Supported Camera Power. */
  cameraPowerSupport?: CameraPowerEnum[];
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
  /** supported color temperature. */
  colorTemperatureSupport?: ValueRange;
  /** _compassDirectionRef */
  compassDirectionRef?: CompassDirectionRefEnum;
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
   * Supported in-progress save interval for interval composite shooting (sec).
   */
  compositeShootingOutputIntervalSupport?: ValueRange;
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
  /**
   * Supported shooting time for interval composite shooting (sec).
   */
  compositeShootingTimeSupport?: ValueRange;
  /** Number of shots for continuous shooting. */
  continuousNumber?: ContinuousNumberEnum;
  /** Current system time of RICOH THETA. Setting another options will result in an error. */
  dateTimeZone?: string;
  /** IP address allocation to be used when wired LAN is enabled. */
  ethernetConfig?: EthernetConfig;
  /** Exposure compensation (EV). */
  exposureCompensation?: ExposureCompensationEnum;
  /** Operating time (sec.) of the self-timer. */
  exposureDelay?: ExposureDelayEnum;
  /** Supported operating time (sec.) of the self-timer. */
  exposureDelaySupport?: ExposureDelayEnum[];
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
  /** _microphoneNoiseReduction */
  microphoneNoiseReduction?: MicrophoneNoiseReductionEnum;
  /** _mobileNetworkSetting */
  mobileNetworkSetting?: MobileNetworkSetting;
  /** Network type of the camera */
  networkType?: NetworkTypeEnum;
  /** Length of standby time before the camera automatically powers OFF. */
  offDelay?: OffDelayEnum;
  /**
   * Auto power off time with USB power supply.
   *
   * For RICOH THETA A1
   */
  offDelayUsb?: OffDelayUsbEnum;
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
  /** _usbConnection */
  usbConnection?: UsbConnectionEnum;
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
  wlanAntennaConfig?: WlanAntennaConfigEnum;
  /** WlanAntennaConfig */
  wlanFrequency?: WlanFrequencyEnum;
  /**
   * Supported WlanFrequency
   *
   * For RICOH THETA X, Z1 and V.
   */
  wlanFrequencySupport?: WlanFrequencyEnum[];
  /**
   * Whether the camera's WLAN CL mode uses 2.4 GHz, 5.2 GHz, or 5.8 GHz frequencies
   *
   * For RICOH THETA A1
   */
  wlanFrequencyClMode?: WlanFrequencyClMode;
  /** GPS setting used in only capturing */
  _gpsTagRecording?: GpsTagRecordingEnum;
};

export type ValueRange = {
  /** maximum value */
  max: number;
  /** minimum value */
  min: number;
  /** step size */
  stepSize: number;
};
