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
  (typeof ChargingStateEnum)[keyof typeof ChargingStateEnum];

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
  (typeof CaptureStatusEnum)[keyof typeof CaptureStatusEnum];

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
  (typeof ShootingFunctionEnum)[keyof typeof ShootingFunctionEnum];

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
  (typeof MicrophoneOptionEnum)[keyof typeof MicrophoneOptionEnum];

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
  (typeof CameraErrorEnum)[keyof typeof CameraErrorEnum];

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
