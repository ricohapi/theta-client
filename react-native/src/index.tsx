import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'theta-client-react-native' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const ThetaClientReactNative = NativeModules.ThetaClientReactNative
  ? NativeModules.ThetaClientReactNative
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

/** Static attributes of Theta. */
export type ThetaInfo = {
  /** Manufacturer name */
  manufacturer: string;
  /** Theta model name */
  model: string;
  /** Theta serial number */
  serialNumber: string;
  /** MAC address of wireless LAN (RICOH THETA V firmware v2.11.1 or later) */
  wlanMacAddress: string | null;
  /** MAC address of Bluetooth (RICOH THETA V firmware v2.11.1 or later) */
  bluetoothMacAddress: string | null;
  /** Theta firmware version */
  firmwareVersion: string;
  /* URL of the support page */
  supportUrl: string;
  /** true if Theta has GPS. */
  hasGps: boolean;
  /** true if Theta has Gyroscope */
  hasGyro: boolean;
  /** Number of seconds since Theta boot */
  uptime: number;
  /** List of supported APIs */
  api: string[];
  /** Endpoint information */
  endpoints: EndPoint;
  /** List of supported APIs (1: v2.0, 2: v2.1) */
  apiLevel: number[];
};

/** endpoint information */
export type EndPoint = {
  httpPort: number;
  httpUpdatesPort: number;
}

/** Battery charging state constants */
export const ChargingStateEnum = {
  /** Charging */
  CHARGING: 'CHARGING',
  /** Charging completed */
  COMPLETED: 'COMPLETED',
  /** Not charging */
  NOT_CHARGING: 'NOT_CHARGING',
} as const;

/** type definition of ChargingStateEnum */
export type ChargingStateEnum =
  typeof ChargingStateEnum[keyof typeof ChargingStateEnum];

/** Capture Status constants */
export const CaptureStatusEnum = {
  /** Performing continuously shoot */
  SHOOTING: 'SHOOTING',
  /** In standby */
  IDLE: 'IDLE',
  /** Self-timer is operating */
  SELF_TIMER_COUNTDOWN: 'SELF_TIMER_COUNTDOWN',
  /** Performing multi bracket shooting */
  BRACKET_SHOOTING: 'BRACKET_SHOOTING',
  /** Converting post file... */
  CONVERTING: 'CONVERTING',
  /** Performing timeShift shooting */
  TIME_SHIFT_SHOOTING: 'TIME_SHIFT_SHOOTING',
  /** Performing continuous shooting */
  CONTINUOUS_SHOOTING: 'CONTINUOUS_SHOOTING',
  /** Waiting for retrospective video... */
  RETROSPECTIVE_IMAGE_RECORDING: 'RETROSPECTIVE_IMAGE_RECORDING',
} as const;

/** type definition of CaptureStatusEnum */
export type CaptureStatusEnum =
  typeof CaptureStatusEnum[keyof typeof CaptureStatusEnum];

/** Shooting function status constants */
export const ShootingFunctionEnum = {
  /** normal */
  NORMAL: 'NORMAL',
  /** selfTimer */
  SELF_TIMER: 'SELF_TIMER',
  /** mySetting */
  MY_SETTING: 'MY_SETTING',
} as const;

/** type definition of ShootingFunctionEnum */
export type ShootingFunctionEnum =
  typeof ShootingFunctionEnum[keyof typeof ShootingFunctionEnum];

/** Microphone option constants */
export const MicrophoneOptionEnum = {
  /** auto */
  AUTO: 'AUTO',
  /** built-in microphone */
  INTERNAL: 'INTERNAL',
  /** external microphone */
  EXTERNAL: 'EXTERNAL',
} as const;

/** type definition of MicrophoneOptionEnum */
export type MicrophoneOptionEnum =
  typeof MicrophoneOptionEnum[keyof typeof MicrophoneOptionEnum];

/** Camera error constants */
export const CameraErrorEnum = {
  /** Insufficient memory */
  NO_MEMORY: 'NO_MEMORY',
  /** Maximum file number exceeded */
  FILE_NUMBER_OVER: 'FILE_NUMBER_OVER',
  /** Camera clock not set */
  NO_DATA_SETTING: 'NO_DATE_SETTING',
  /** Includes when the card is removed */
  READ_ERROR: 'READ_ERROR',
  /** Unsupported media (SDHC, etc.) */
  NOT_SUPPORTED_MEDIA_TYPE: 'NOT_SUPPORTED_MEDIA_TYPE',
  /** FAT32, etc. */
  NOT_SUPPORTED_FILE_SYSTEM: 'NOT_SUPPORTED_FILE_SYSTEM',
  /** Error warning while mounting */
  MEDIA_NOT_READY: 'MEDIA_NOT_READY',
  /** Battery level warning (firmware update) */
  NOT_ENOUGH_BATTERY: 'NOT_ENOUGH_BATTERY',
  /** Firmware file mismatch warning */
  INVALID_FILE: 'INVALID_FILE',
  /** Plugin start warning (IoT technical standards compliance) */
  PLUGIN_BOOT_ERROR: 'PLUGIN_BOOT_ERROR',
  /**
   *  When performing continuous shooting by operating the camera while executing <Delete object>,
   *  <Transfer firmware file>, <Install plugin> or <Uninstall plugin> with the WebAPI or MTP.
   */
  IN_PROGRESS_ERROR: 'IN_PROGRESS_ERROR',
  /** Battery inserted + WLAN ON + Video mode + 4K 60fps / 5.7K 10fps / 5.7K 15fps / 5.7K 30fps / 8K 10fps */
  CANNOT_RECORDING: 'CANNOT_RECORDING',
  /** Battery inserted AND Specified battery level or lower + WLAN ON + Video mode + 4K 30fps */
  CANNOT_RECORD_LOWBAT: 'CANNOT_RECORD_LOWBAT',
  /** Shooting hardware failure */
  CAPTURE_HW_FAILED: 'CAPTURE_HW_FAILED',
  /** Software error */
  CAPTURE_SW_FAILED: 'CAPTURE_SW_FAILED',
  /** Internal memory access error */
  INTERNAL_MEM_ACCESS_FAIL: 'INTERNAL_MEM_ACCESS_FAIL',
  /** Undefined error */
  UNEXPECTED_ERROR: 'UNEXPECTED_ERROR',
  /** Charging error */
  BATTERY_CHARGE_FAIL: 'BATTERY_CHARGE_FAIL',
  /** (Board) temperature warning */
  HIGH_TEMPERATURE_WARNING: 'HIGH_TEMPERATURE_WARNING',
  /** (Board) temperature error */
  HIGH_TEMPERATURE: 'HIGH_TEMPERATURE',
  /** Battery temperature error */
  BATTERY_HIGH_TEMPERATURE: 'BATTERY_HIGH_TEMPERATURE',
  /** Electronic compass error */
  COMPASS_CALIBRATION: 'COMPASS_CALIBRATION',
} as const;

/** type definition of CameraErrorEnum */
export type CameraErrorEnum =
  typeof CameraErrorEnum[keyof typeof CameraErrorEnum];

/** Mutable values representing Theta status. */
export type ThetaState = {
  /** Fingerprint (unique identifier) of the current camera state */
  fingerprint: string;
  /** Battery level between 0.0 and 1.0 */
  batteryLevel: number;
  /** Storage URI */
  storageUri: string | null;
  /** Storage ID */
  storageID: string | null;
  /** Continuously shoots state */
  captureStatus: CaptureStatusEnum;
  /** Recorded time of movie (seconds) */
  recordedTime: number;
  /** Recordable time of movie (seconds) */
  recordableTime: number;
  /** Number of still images captured during continuous shooting, Unit: images */
  capturedPictures: number | null;
  /** Elapsed time for interval composite shooting (sec) */
  compositeShootingElapsedTime: number | null;
  /** URL of the last saved file */
  latestFileUrl: string;
  /** Charging state */
  chargingState: ChargingStateEnum;
  /** API version currently set (1: v2.0, 2: v2.1) */
  apiVersion: number;
  /**  Plugin running state (true: running, false: stop) */
  isPluginRunning: boolean | null;
  /** Plugin web server state (true: enabled, false: disabled) */
  isPluginWebServer: boolean | null;
  /** Shooting function status*/
  function: ShootingFunctionEnum;
  /** My setting changed state */
  isMySettingChanged: boolean | null;
  /** Identifies the microphone used while recording video */
  currentMicrophone: MicrophoneOptionEnum | null;
  /** true if record to SD card */
  isSdCard: boolean;
  /** Error information of the camera */
  cameraError: Array<CameraErrorEnum> | null;
  /** true: Battery inserted; false: Battery not inserted */
  isBatteryInsert: boolean | null;
};

/** File information in Theta. */
export type FileInfo = {
  /** File name. */
  name: string;
  /** File size in bytes. */
  size: number;
  /** File creation time in the format "YYYY:MM:DD HH:MM:SS". */
  dateTime: string;
  /** You can get a thumbnail image using HTTP GET to [thumbnailUrl]. */
  thumbnailUrl: string;
  /** fileUrl You can get a file using HTTP GET to [fileUrl]. */
  fileUrl: string;
};

/** Data about files in Theta. */
export type ThetaFiles = {
  /** A list of file information */
  fileList: FileInfo[],
  /** number of totalEntries */
  totalEntries: Number,
};

/** File type in Theta. */
export const FileTypeEnum = {
  /** still image files. */
  IMAGE: 'IMAGE',
  /** video files. */
  VIDEO: 'VIDEO',
  /** all files. */
  ALL: 'ALL',
} as const;

/** type definition of FileTypeEnum */
export type FileTypeEnum = typeof FileTypeEnum[keyof typeof FileTypeEnum];

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
export type ApertureEnum = typeof ApertureEnum[keyof typeof ApertureEnum];

/** Shooting mode. */
export const CaptureModeEnum = {
  /** Still image capture mode */
  IMAGE: 'IMAGE',
  /** Video capture mode */
  VIDEO: 'VIDEO',
} as const;

/** type definition of CaptureModeEnum */
export type CaptureModeEnum =
  typeof CaptureModeEnum[keyof typeof CaptureModeEnum];

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
  typeof ExposureCompensationEnum[keyof typeof ExposureCompensationEnum];

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
  typeof ExposureDelayEnum[keyof typeof ExposureDelayEnum];

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
  typeof ExposureProgramEnum[keyof typeof ExposureProgramEnum];

/** file format type in theta */
export const FileFormatTypeEnum = {
  /** jpeg image */
  JPEG: 'JPEG',
  /** mp4 video */
  MP4: 'MP4',
  /** raw+ image */
  RAW: 'RAW',
} as const;

/** type definition of FileFormatTypeEnum */
export type FileFormatTypeEnum =
  typeof FileFormatTypeEnum[keyof typeof FileFormatTypeEnum];

/** Photo image format */
export const PhotoFileFormatEnum = {
  /** jpeg 2048 x 1024 */
  IMAGE_2K: 'IMAGE_2K',
  /** jpeg 5376 x 2688 */
  IMAGE_5K: 'IMAGE_5K',
  /** jpeg 6720 x 3360 */
  IMAGE_6_7K: 'IMAGE_6_7K',
  /** row+ 6720 x 3360 */
  RAW_P_6_7K: 'RAW_P_6_7K',
  /** jpeg 5504 x 2752 */
  IMAGE_5_5K: 'IMAGE_5_5K',
  /** jpeg 11008 x 5504 */
  IMAGE_11K: 'IMAGE_11K',
} as const;

/** Video image format */
export const VideoFileFormatEnum = {
  /** mp4 1280 x 570 */
  VIDEO_HD: 'VIDEO_HD',
  /** mp4 1920 x 1080 */
  VIDEO_FULL_HD: 'VIDEO_FULL_HD',
  /** mp4 1920 x 960 */
  VIDEO_2K: 'VIDEO_2K',
  /** mp4 3840 x 1920 */
  VIDEO_4K: 'VIDEO_4K',
  /** mp4 1920 x 960 30fps */
  VIDEO_2K_30F: 'VIDEO_2K_30F',
  /** mp4 1920 x 960 60fps */
  VIDEO_2K_60F: 'VIDEO_2K_60F',
  /** mp4 3840 x 1920 30fps */
  VIDEO_4K_30F: 'VIDEO_4K_30F',
  /** mp4 3840 x 1920 60fps */
  VIDEO_4K_60F: 'VIDEO_4K_60F',
  /** mp4 5760 x 2880 2fps */
  VIDEO_5_7K_2F: 'VIDEO_5_7K_2F',
  /** mp4 5760 x 2880 5fps */
  VIDEO_5_7K_5F: 'VIDEO_5_7K_5F',
  /** mp4 5760 x 2880 30fps */
  VIDEO_5_7K_30F: 'VIDEO_5_7K_30F',
  /** mp4 7680 x 3840 2fps */
  VIDEO_7K_2F: 'VIDEO_7K_2F',
  /** mp4 7680 x 3840 5fps */
  VIDEO_7K_5F: 'VIDEO_7K_5F',
  /** mp4 7680 x 3840 10fps */
  VIDEO_7K_10F: 'VIDEO_7K_10F',
} as const;

/** type definition of PhotoFileFormatEnum */
export type PhotoFileFormatEnum =
  typeof PhotoFileFormatEnum[keyof typeof PhotoFileFormatEnum];

/** type definition of VideoFileFormatEnum */
export type VideoFileFormatEnum =
  typeof VideoFileFormatEnum[keyof typeof VideoFileFormatEnum];

/** Image processing filter. */
export const FilterEnum = {
  /** No filter. */
  OFF: 'OFF',
  /** Noise reduction. */
  NOISE_REDUCTION: 'NOISE_REDUCTION',
  /** HDR. */
  HDR: 'HDR',
} as const;

/** type definition of FilterEnum */
export type FilterEnum = typeof FilterEnum[keyof typeof FilterEnum];

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

/** Turns position information assigning ON/OFF. */
export const GpsTagRecordingEnum = {
  /** Position information assigning ON. */
  ON: 'ON',
  /** Position information assigning OFF. */
  OFF: 'OFF',
} as const;

/** type definition of GpsTagRecordingEnum */
export type GpsTagRecordingEnum =
  typeof GpsTagRecordingEnum[keyof typeof GpsTagRecordingEnum];

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
export type IsoEnum = typeof IsoEnum[keyof typeof IsoEnum];

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
  typeof IsoAutoHighLimitEnum[keyof typeof IsoAutoHighLimitEnum];

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
export type LanguageEnum = typeof LanguageEnum[keyof typeof LanguageEnum];

/** Maximum recordable time (in seconds) of the camera */
export const MaxRecordableTimeEnum = {
  /** 180 seconds for SC2 only. */
  RECORDABLE_TIME_180: 'RECORDABLE_TIME_180',
  /** 300 seconds for other than SC2. */
  RECORDABLE_TIME_300: 'RECORDABLE_TIME_300',
  /** 1500 seconds for other than SC2. */
  RECORDABLE_TIME_1500: 'RECORDABLE_TIME_1500',
} as const;

/** type definition of MaxRecordableTimeEnum */
export type MaxRecordableTimeEnum =
  typeof MaxRecordableTimeEnum[keyof typeof MaxRecordableTimeEnum];

/** Length of standby time before the camera automatically powers OFF. */
export const OffDelayEnum = {
  /** Do not turn power off. */
  DISABLE: 'DISABLE',
  /** Power off after 5 minutes.(300sec) */
  OFF_DELAY_5M: 'OFF_DELAY_5M',
  /** Power off after 10 minutes.(600sec) */
  OFF_DELAY_10M: 'OFF_DELAY_10M',
  /** Power off after 15 minutes.(900sec) */
  OFF_DELAY_15M: 'OFF_DELAY_15M',
  /** Power off after 30 minutes.(1,800sec) */
  OFF_DELAY_30M: 'OFF_DELAY_30M',
} as const;

/** type definition of OffDelayEnum */
export type OffDelayEnum =
  | typeof OffDelayEnum[keyof typeof OffDelayEnum]
  | number;

/** Length of standby time before the camera enters the sleep mode. */
export const SleepDelayEnum = {
  /** Do not turn sleep mode. */
  DISABLE: 'DISABLE',
  /** sleep mode after 3 minutes.(180seconds) */
  SLEEP_DELAY_3M: 'SLEEP_DELAY_3M',
  /** sleep mode after 5 minutes.(300seconds) */
  SLEEP_DELAY_5M: 'SLEEP_DELAY_5M',
  /** sleep mode after 7 minutes.(420seconds) */
  SLEEP_DELAY_7M: 'SLEEP_DELAY_7M',
  /** sleep mode after 10 minutes.(600seconds) */
  SLEEP_DELAY_10M: 'SLEEP_DELAY_10M',
} as const;

/** type definition of SleepDelayEnum */
export type SleepDelayEnum =
  | typeof SleepDelayEnum[keyof typeof SleepDelayEnum]
  | number;

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
  typeof WhiteBalanceEnum[keyof typeof WhiteBalanceEnum];

/** Camera setting options name. */
export const OptionNameEnum = {
  /** aperture */
  Aperture: 'Aperture',
  /** captureMode */
  CaptureMode: 'CaptureMode',
  /** colorTemperature */
  ColorTemperature: 'ColorTemperature',
  /** dateTimeZone */
  DateTimeZone: 'DateTimeZone',
  /** exposureCompensation */
  ExposureCompensation: 'ExposureCompensation',
  /** exposureDelay */
  ExposureDelay: 'ExposureDelay',
  /** exposureProgram */
  ExposureProgram: 'ExposureProgram',
  /** fileFormat */
  FileFormat: 'FileFormat',
  /** filter */
  Filter: 'Filter',
  /** gpsInfo */
  GpsInfo: 'GpsInfo',
  /** isGpsOn */
  IsGpsOn: 'IsGpsOn',
  /** iso */
  Iso: 'Iso',
  /** isoAutoHighLimit */
  IsoAutoHighLimit: 'IsoAutoHighLimit',
  /** language */
  Language: 'Language',
  /** maxRecordableTime */
  MaxRecordableTime: 'MaxRecordableTime',
  /** offDelay */
  OffDelay: 'OffDelay',
  /** sleepDelay */
  SleepDelay: 'SleepDelay',
  /** remainingPictures */
  RemainingPictures: 'RemainingPictures',
  /** remainingVideoSeconds */
  RemainingVideoSeconds: 'RemainingVideoSeconds',
  /** remainingSpace */
  RemainingSpace: 'RemainingSpace',
  /** totalSpace */
  TotalSpace: 'TotalSpace',
  /** shutterVolume */
  ShutterVolume: 'ShutterVolume',
  /** whiteBalance */
  WhiteBalance: 'WhiteBalance',
} as const;

/** type definition of OptionNameEnum */
export type OptionNameEnum = typeof OptionNameEnum[keyof typeof OptionNameEnum];

/** camera setting options */
export type Options = {
  /** Aperture value. */
  aperture?: ApertureEnum;
  /** Shooting mode. */
  captureMode?: CaptureModeEnum;
  /** Color temperature of the camera (Kelvin). */
  colorTemperature?: number;
  /** Current system time of RICOH THETA. Setting another options will result in an error. */
  dateTimeZone?: string;
  /** Exposure compensation (EV). */
  exposureCompensation?: ExposureCompensationEnum;
  /** Operating time (sec.) of the self-timer. */
  exposureDelay?: ExposureDelayEnum;
  /** Exposure program. The exposure settings that take priority can be selected. */
  exposureProgram?: ExposureProgramEnum;
  /** Image format used in shooting. */
  fileFormat?: PhotoFileFormatEnum | VideoFileFormatEnum;
  /** Image processing filter. */
  filter?: FilterEnum;
  /** GPS location information. */
  gpsInfo?: GpsInfo;
  /** Turns position information assigning ON/OFF. */
  isGpsOn?: boolean;
  /** ISO sensitivity */
  iso?: IsoEnum;
  /** ISO sensitivity upper limit when ISO sensitivity is set to automatic. */
  isoAutoHighLimit?: IsoAutoHighLimitEnum;
  /** Language used in camera OS. */
  language?: LanguageEnum;
  /** Maximum recordable time (in seconds) of the camera. */
  maxRecordableTime?: MaxRecordableTimeEnum;
  /** Length of standby time before the camera automatically powers OFF. */
  offDelay?: OffDelayEnum;
  /** Length of standby time before the camera enters the sleep mode. */
  sleepDelay?: SleepDelayEnum;
  /** The estimated remaining number of shots for the current shooting settings. */
  remainingPictures?: number;
  /** The estimated remaining shooting time (sec.) for the current video shooting settings. */
  remainingVideoSeconds?: number;
  /** Remaining usable storage space (byte). */
  remainingSpace?: number;
  /** Total storage space (byte). */
  totalSpace?: number;
  /** Shutter volume. */
  shutterVolume?: number;
  /** White balance. */
  whiteBalance?: WhiteBalanceEnum;
  /** GPS setting used in only capturing */
  _gpsTagRecording?: GpsTagRecordingEnum;
};

/**
 * initialize theta client sdk
 *
 * @function initialize
 * @param {string} endPoint optional endpoint of camera
 * @return promise of boolean result
 **/
export function initialize(endPoint?: string): Promise<boolean> {
  if (!endPoint) {
    endPoint = 'http://192.168.1.1';
  }
  return ThetaClientReactNative.initialize(endPoint);
}

/**
 * Get basic information about Theta.
 *
 * @function getThetaInfo
 * @return promise of ThetaInfo
 **/
export function getThetaInfo(): Promise<ThetaInfo> {
  return ThetaClientReactNative.getThetaInfo();
}

/**
 * Get current state of Theta.
 *
 * @function getThetaState
 * @return promise of ThetaState
 **/
export function getThetaState(): Promise<ThetaState> {
  return ThetaClientReactNative.getThetaState();
}

/**
 * Lists information of images and videos in Theta.
 *
 * @function listFiles
 * @param {FileTypeEnum} fileType Type of the files to be listed.
 * @param {number} startPosition The position of the first file to be
 *   returned in the list. 0 represents the first file.  If
 *   startPosition is larger than the position of the last file, an
 *   empty list is returned.
 * @param {number} entryCount Desired number of entries to return.  If
 *   entryCount is more than the number of remaining files, just
 *   return entries of actual remaining files.
 * @return promise with a list of file information and number of totalEntries.
 *   see [camera.listFiles](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.list_files.md).
 */
export function listFiles(
  fileTypeEnum: FileTypeEnum,
  startPosition: number = 0,
  entryCount: number
): Promise<ThetaFiles> {
  return ThetaClientReactNative.listFiles(
    fileTypeEnum,
    startPosition,
    entryCount
  );
}

/**
 * Delete files in Theta.
 *
 * @function deleteFiles
 * @param {string[]} fileUrls URLs of the file to be deleted.
 * @return promise of boolean result
 */
export function deleteFiles(fileUrls: string[]): Promise<boolean> {
  return ThetaClientReactNative.deleteFiles(fileUrls);
}

/**
 * Delete all files in Theta.
 *
 * @function deleteAllFiles
 * @return promise of boolean result
 */
export function deleteAllFiles(): Promise<boolean> {
  return ThetaClientReactNative.deleteAllFiles();
}

/**
 * Delete all image files in Theta.
 *
 * @function deleteAllImageFiles
 * @return promise of boolean result
 */
export function deleteAllImageFiles(): Promise<boolean> {
  return ThetaClientReactNative.deleteAllImageFiles();
}

/**
 * Delete all video files in Theta.
 *
 * @function deleteAllVideoFiles
 * @return promise of boolean result
 */
export function deleteAllVideoFiles(): Promise<boolean> {
  return ThetaClientReactNative.deleteAllVideoFiles();
}

/**
 * Acquires the properties and property support specifications for
 * shooting, the camera, etc.
 *
 * @function getOptions
 * @param {OptionNameEnum[]} optionNames List of OptionNameEnum.
 * @return promise of Options acquired
 */
export function getOptions(optionNames: OptionNameEnum[]): Promise<Options> {
  return ThetaClientReactNative.getOptions(optionNames);
}

/**
 * Property settings for shooting, the camera, etc.
 *
 * @function setOptions
 * @param {Options} options Camera setting options.
 * @return promise of boolean result
 */
export function setOptions(options: Options): Promise<boolean> {
  return ThetaClientReactNative.setOptions(options);
}

/** theta frame event name */
export const THETA_EVENT_NAME = ThetaClientReactNative.DEFAULT_EVENT_NAME;

/**
 * Start live preview.
 * preview frame (JPEG DataURL) send THETA_EVENT_NAME event as
 * {
 *   "data": "DataURL of JPEG Frame"
 * }
 *
 * @function StartLivePreview
 * @return promise of boolean result
 */
export function getLivePreview(): Promise<boolean> {
  return ThetaClientReactNative.getLivePreview();
}

/**
 * Stop live preview.
 *
 * @function StopLivePreview
 */
export function stopLivePreview() {
  ThetaClientReactNative.stopLivePreview();
}

/**
 * PhotoCapture class
 */
export class PhotoCapture {
  /**
   * @return promise of token file url
   */
  takePicture(): Promise<string> {
    return ThetaClientReactNative.takePicture();
  }
}

/**
 * Capture Builder class
 */
export abstract class CaptureBuilder<T extends CaptureBuilder<T>> {
  /** capture options */
  options: Options;

  /** construct capture builder instance */
  constructor() {
    this.options = {};
  }

  /**
   * Set aperture value.
   * @param {ApertureEnum} aperture aperture value to set
   * @return CaptureBuilder
   */
  setAperture(aperture: ApertureEnum): T {
    this.options.aperture = aperture;
    return this as unknown as T;
  }

  /**
   * Set color temperature of the camera (Kelvin).
   * @param {number} kelvin Color temperature to set
   * @return CaptureBuilder
   */
  setColorTemperature(kelvin: number): T {
    this.options.colorTemperature = kelvin;
    return this as unknown as T;
  }

  /**
   * Set exposure compensation (EV).
   * @param {ExposureCompensationEnum} value Exposure compensation to set
   * @return CaptureBuilder
   */
  setExposureCompensation(value: ExposureCompensationEnum): T {
    this.options.exposureCompensation = value;
    return this as unknown as T;
  }

  /**
   * Set operating time (sec.) of the self-timer.
   * @param {ExposureDelayEnum} delay Operating time to set
   * @return CaptureBuilder
   */
  setExposureDelay(delay: ExposureDelayEnum): T {
    this.options.exposureDelay = delay;
    return this as unknown as T;
  }

  /**
   * Set exposure program. The exposure settings that take priority can be selected.
   * @param {ExposureProgramEnum} program Exposure program to set
   * @return CaptureBuilder
   */
  setExposureProgram(program: ExposureProgramEnum): T {
    this.options.exposureProgram = program;
    return this as unknown as T;
  }
  /**
   * Set GPS information.
   * @param {GpsInfo} gpsInfo GPS information to set
   * @return CaptureBuilder
   */
  setGpsInfo(gpsInfo: GpsInfo): T {
    this.options.gpsInfo = gpsInfo;
    return this as unknown as T;
  }

  /**
   * Set turns position information assigning ON/OFF.
   * @param {GpsTagRecordingEnum} value Turns position information assigning ON/OFF to set
   * @return CaptureBuilder
   */
  setGpsTagRecording(value: GpsTagRecordingEnum): T {
    this.options._gpsTagRecording = value;
    return this as unknown as T;
  }

  /**
   * Set ISO sensitivity.
   * @param {IsoEnum} iso ISO sensitivity to set
   * @return CaptureBuilder
   */
  setIso(iso: IsoEnum): T {
    this.options.iso = iso;
    return this as unknown as T;
  }

  /**
   * Set ISO sensitivity upper limit when ISO sensitivity is set to automatic.
   * @param {IsoAutoHighLimitEnum} iso ISO sensitivity upper limit to set
   * @return CaptureBuilder
   */
  setIsoAutoHighLimit(iso: IsoAutoHighLimitEnum): T {
    this.options.isoAutoHighLimit = iso;
    return this as unknown as T;
  }

  /**
   * Set white balance.
   * @param {WhiteBalanceEnum} whiteBalance White balance to set
   * @return CaptureBuilder
   */
  setWhiteBalance(whiteBalance: WhiteBalanceEnum): T {
    this.options.whiteBalance = whiteBalance;
    return this as unknown as T;
  }
}

/**
 * PhotoCaptureBuilder class
 */
export class PhotoCaptureBuilder extends CaptureBuilder<PhotoCaptureBuilder> {
  /** construct PhotoCaptureBuilder instance */
  constructor() {
    super();
  }

  /**
   * Set photo file format.
   * @param {PhotoFileFormatEnum} fileFormat Photo file format to set
   * @return PhotoCaptureBuilder
   */
  setFileFormat(fileFormat: PhotoFileFormatEnum): PhotoCaptureBuilder {
    this.options.fileFormat = fileFormat;
    return this;
  }

  /**
   * Set image processing filter.
   * @param {FilterEnum} filter Image processing filter to set
   * @return PhotoCaptureBuilder
   */
  setFilter(filter: FilterEnum): PhotoCaptureBuilder {
    this.options.filter = filter;
    return this;
  }

  /**
   * Builds an instance of a PhotoCapture that has all the combined
   * parameters of the Options that have been added to the Builder.
   *
   * @return promise of PhotoCapture instance
   */
  build(): Promise<PhotoCapture> {
    return ThetaClientReactNative.buildPhotoCapture(this.options).then(
      () => new PhotoCapture()
    );
  }
}

/**
 * Get PhotoCaptureBuilder for take a picture.
 *
 * @function getPhotoCaptureBuilder
 * @return created PhotoCaptureBuilder
 */
export function getPhotoCaptureBuilder(): PhotoCaptureBuilder {
  ThetaClientReactNative.getPhotoCaptureBuilder();
  return new PhotoCaptureBuilder();
}

/**
 * VideoCapture class
 */
export class VideoCapture {
  /**
   * start video capture
   * @return promise of captured file url
   */
  startCapture(): Promise<string> {
    return ThetaClientReactNative.startCapture();
  }
  /**
   * stop video capture
   */
  stopCapture() {
    ThetaClientReactNative.stopCapture();
  }
}

/**
 * VideoCaptureBuilder class
 */
export class VideoCaptureBuilder extends CaptureBuilder<VideoCaptureBuilder> {
  /** construct VideoCaptureBuilder instance */
  constructor() {
    super();
  }

  /**
   * Set video file format.
   * @param {VideoFileFormatEnum} fileFormat Video file format to set
   * @return VideoCaptureBuilder
   */
  setFileFormat(fileFormat: VideoFileFormatEnum): VideoCaptureBuilder {
    this.options.fileFormat = fileFormat;
    return this;
  }

  /**
   * Set maximum recordable time (in seconds) of the camera.
   * @param {MaxRecordableTimeEnum} time Maximum recordable time to set
   * @return VideoCaptureBuilder
   */
  setMaxRecordableTime(time: MaxRecordableTimeEnum): VideoCaptureBuilder {
    this.options.maxRecordableTime = time;
    return this;
  }

  /**
   * Builds an instance of a VideoCapture that has all the combined
   * parameters of the Options that have been added to the Builder.
   *
   * @return promise of VideoCapture instance
   */
  build(): Promise<VideoCapture> {
    return ThetaClientReactNative.buildVideoCapture(this.options).then(
      () => new VideoCapture()
    );
  }
}

/**
 * Get PhotoCapture.Builder for capture video.
 *
 * @function getVideoCaptureBuilder
 * @return created VideoCaptureBuilder instance
 */
export function getVideoCaptureBuilder(): VideoCaptureBuilder {
  ThetaClientReactNative.getVideoCaptureBuilder();
  return new VideoCaptureBuilder();
}

/**
 * exif information
 */
export type Exif = {
  /** EXIF Support version */
  exifVersion: string;
  /** File created or updated date and time */
  dateTime: string;
  /** Image width (pixel). Theta X returns null. */
  imageWidth?: number;
  /** Image height (pixel). Theta X returns null. */
  imageLength?: number;
  /** GPS latitude if exists. */
  gpsLatitude?: number;
  /** GPS longitude if exists. */
  gpsLongitude?: number;
};

/**
 * Photo sphere XMP metadata of a still image.
 */
export type Xmp = {
  /** Compass heading, for the center the image. Theta X returns null. */
  poseHeadingDegrees?: number;
  /** Image width (pixel). */
  fullPanoWidthPixels: number;
  /** Image height (pixel). */
  fullPanoHeightPixels: number;
};

/**
 * metadata of a still image
 */
export type MetaInfo = {
  /** exif information */
  exif?: Exif;
  /** xmp information */
  xmp?: Xmp;
};

/**
 * Get metadata of a still image
 *
 * @function getMetadata
 * @param {string} fileUrl URL of a still image file to get metadata
 * @return promise of MetaInfo
 */
export function getMetadata(fileUrl: string): Promise<MetaInfo> {
  return ThetaClientReactNative.getMetadata(fileUrl);
}

/**
 * Reset all device settings and capture settings.
 * After reset, the camera will be restarted.
 *
 * @function reset
 * @return promise of boolean result
 */
export function reset(): Promise<boolean> {
  return ThetaClientReactNative.reset();
}

/**
 * Restore setting to THETA
 *
 * @function restoreSettings
 * @return promise of boolean result
 */
export function restoreSettings(): Promise<boolean> {
  return ThetaClientReactNative.restoreSettings();
}

/**
 * Stop running self-timer.
 *
 * @function stopSelfTimer
 * @return promise of boolean result
 */
export function stopSelfTimer(): Promise<boolean> {
  return ThetaClientReactNative.stopSelfTimer();
}

/**
 * Converts the movie format of a saved movie.
 *
 * @function convertVideoFormats
 * @param {string} fileUrl URL of a saved movie file to convert
 * @param {boolean} toLowResolution If true generates lower resolution
 *   video, otherwise same resolution.
 * @param {boolean} applyTopBottomCorrection apply Top/bottom
 *   correction. This parameter is ignored on Theta X.
 * @return promise of URL of a converted movie file.
 */
export function convertVideoFormats(
  fileUrl: string,
  toLowResolution: boolean,
  applyTopBottomCorrection: boolean
): Promise<string> {
  return ThetaClientReactNative.convertVideoFormats(
    fileUrl,
    toLowResolution,
    applyTopBottomCorrection
  );
}

/**
 * Cancels the movie format conversion.
 *
 * @function cancelVideoConvert
 * @return promise of boolean result
 */
export function cancelVideoConvert(): Promise<boolean> {
  return ThetaClientReactNative.cancelVideoConvert();
}

/**
 * Turns the wireless LAN off.
 *
 * @function finishWlan
 * @return promise of boolean result
 */
export function finishWlan(): Promise<boolean> {
  return ThetaClientReactNative.finishWlan();
}

/**
 * Enum for authentication mode.
 */
export const AuthModeEnum = {
  /** None of authentication */
  NONE: 'NONE',
  /** Wep authentication */
  WEP: 'WEP',
  /** WPA PSK or WPA2 PSK authentication */
  WPA: 'WPA',
} as const;

/** type definition of AuthModeEnum */
export type AuthModeEnum = typeof AuthModeEnum[keyof typeof AuthModeEnum];

/** Access point information. */
export type AccessPoint = {
  /** SSID of the access point. */
  ssid: string;
  /** true if SSID stealth is enabled. */
  ssidStealth: boolean;
  /** Authentication mode. */
  authMode: AuthModeEnum;
  /** Connection priority 1 to 5. */
  connectionPriority: number;
  /** Using DHCP or not. */
  usingDhcp: boolean;
  /** IP address assigned to camera. */
  ipAddress?: string;
  /** Subnet Mask. */
  subnetMask?: string;
  /** Default Gateway. */
  defaultGateway?: string;
};

/**
 * Acquires the access point list used in client mode.
 *
 * @function listAccessPoints
 * @return promise of AccessPoint list
 */
export function listAccessPoints(): Promise<AccessPoint[]> {
  return ThetaClientReactNative.listAccessPoints();
}

/**
 * Set access point. IP address is set dynamically.
 *
 * @function setAccessPointDynamically
 * @param {string} ssid SSID of the access point.
 * @param {boolean} ssidStealth true if SSID stealth is enabled.
 * @param {AuthModeEnum} authMode Authentication mode.
 * @param {string} password Password. If authMode is "NONE", pass empty String.
 * @param {number} connectionPriority Connection priority 1 to 5.
 * @return promise of boolean result
 */
export function setAccessPointDynamically(
  ssid: string,
  ssidStealth: boolean = false,
  authMode: AuthModeEnum = AuthModeEnum.NONE,
  password: string = '',
  connectionPriority: number = 1
): Promise<boolean> {
  return ThetaClientReactNative.setAccessPointDynamically(
    ssid,
    ssidStealth,
    authMode,
    password,
    connectionPriority
  );
}

/**
 * Set access point. IP address is set statically.
 *
 * @function setAccessPointStatically
 * @param {string} ssid SSID of the access point.
 * @param {boolean} ssidStealth True if SSID stealth is enabled.
 * @param {AuthModeEnum} authMode Authentication mode.
 * @param {string} password Password. If authMode is "NONE", pass empty String.
 * @param {number} connectionPriority Connection priority 1 to 5.
 * @param {string} ipAddress IP address assigns to Theta.
 * @param {string} subnetMask Subnet mask.
 * @param {string} defaultGateway Default gateway.
 * @return promise of boolean result
 */
export function setAccessPointStatically(
  ssid: string,
  ssidStealth: boolean = false,
  authMode: AuthModeEnum = AuthModeEnum.NONE,
  password: string = '',
  connectionPriority: number = 1,
  ipAddress: string,
  subnetMask: string,
  defaultGateway: string
): Promise<boolean> {
  return ThetaClientReactNative.setAccessPointStatically(
    ssid,
    ssidStealth,
    authMode,
    password,
    connectionPriority,
    ipAddress,
    subnetMask,
    defaultGateway
  );
}

/**
 * Deletes access point information used in client mode.
 *
 * @function deleteAccessPoint
 * @param {string} ssid SSID of the access point to delete.
 * @return promise of boolean result
 */
export function deleteAccessPoint(ssid: string): Promise<boolean> {
  return ThetaClientReactNative.deleteAccessPoint(ssid);
}
