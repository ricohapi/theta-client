/**
 * Maximum recordable time (in seconds) of the camera
 */
export const MaxRecordableTimeEnum = {
  /** Undefined value */
  UNKNOWN: 'UNKNOWN',
  /** 180 seconds for SC2 only. */
  RECORDABLE_TIME_180: 'RECORDABLE_TIME_180',
  /** 300 seconds for other than SC2. */
  RECORDABLE_TIME_300: 'RECORDABLE_TIME_300',
  /** 1500 seconds for other than SC2. */
  RECORDABLE_TIME_1500: 'RECORDABLE_TIME_1500',
  /** 3000 seconds for other than SC2. */
  RECORDABLE_TIME_3000: 'RECORDABLE_TIME_3000',
  /** 7200 seconds for Theta X only */
  RECORDABLE_TIME_7200: 'RECORDABLE_TIME_7200',
  /** Just used by getMySetting/setMySetting command */
  DO_NOT_UPDATE_MY_SETTING_CONDITION: 'DO_NOT_UPDATE_MY_SETTING_CONDITION',
} as const;

/** type definition of MaxRecordableTimeEnum */
export type MaxRecordableTimeEnum =
  (typeof MaxRecordableTimeEnum)[keyof typeof MaxRecordableTimeEnum];
