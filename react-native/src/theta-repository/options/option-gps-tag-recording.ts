/** Turns position information assigning ON/OFF. */
export const GpsTagRecordingEnum = {
  /** Undefined value */
  UNKNOWN: 'UNKNOWN',
  /** Position information assigning ON. */
  ON: 'ON',
  /** Position information assigning OFF. */
  OFF: 'OFF',
} as const;

/** type definition of GpsTagRecordingEnum */
export type GpsTagRecordingEnum =
  (typeof GpsTagRecordingEnum)[keyof typeof GpsTagRecordingEnum];
