/**
 * Camera power state.
 * _cameraPower is the power status of camera.
 *
 * For RICOH THETA X v2.61.0 or later
 */
export const CameraPowerEnum = {
  /** Undefined value */
  UNKNOWN: 'UNKNOWN',
  /** Power ON */
  ON: 'ON',
  /** Power OFF */
  OFF: 'OFF',
  /** Sleep */
  SLEEP: 'SLEEP',
  /**
   * Power on, power saving mode. Camera is closed.
   * Unavailable parameter when plugin is running. In this case, invalidParameterValue error will be returned.
   */
  POWER_SAVING: 'POWER_SAVING',

  /**
   * Power on, silent mode. LCD/LED is turned off.
   * Unavailable parameter when plugin is running. In this case, invalidParameterValue error will be returned.
   */
  SILENT_MODE: 'SILENT_MODE',
} as const;

/** type definition of CameraPowerEnum */
export type CameraPowerEnum =
  (typeof CameraPowerEnum)[keyof typeof CameraPowerEnum];
