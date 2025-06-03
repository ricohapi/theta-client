/**
 * _compassDirectionRef
 */
export const CompassDirectionRefEnum = {
  /** Undefined value */
  UNKNOWN: 'UNKNOWN',
  /**
   * If GPS positioning is available, record in true north;
   * if GPS is off or not available, record in magnetic north.
   */
  AUTO: 'AUTO',
  /**
   * If the azimuth is set to true north, GPS is turned off, or positioning is not possible,
   * the azimuth information is not recorded (positioning information is required for conversion).
   */
  TRUE_NORTH: 'TRUE_NORTH',
  /**
   * Do not set azimuth to true north, set azimuth to magnetic north
   */
  MAGNETIC: 'MAGNETIC',
} as const;

/** type definition of CompassDirectionRefEnum */
export type CompassDirectionRefEnum =
  (typeof CompassDirectionRefEnum)[keyof typeof CompassDirectionRefEnum];
