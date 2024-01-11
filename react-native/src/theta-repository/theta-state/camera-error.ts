/** Camera error constants */
export const CameraErrorEnum = {
  /** Undefined value */
  UNKNOWN: 'UNKNOWN',
  /** Insufficient memory */
  NO_MEMORY: 'NO_MEMORY',
  /** Maximum file number exceeded */
  FILE_NUMBER_OVER: 'FILE_NUMBER_OVER',
  /** Camera clock not set */
  NO_DATE_SETTING: 'NO_DATE_SETTING',
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
