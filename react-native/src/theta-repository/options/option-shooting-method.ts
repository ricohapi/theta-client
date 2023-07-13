/**
 * Shooting method for My Settings mode.
 * In RICOH THETA X, it is used outside of MySetting.
 *
 * Can be acquired and set only when in the Still image shooting mode
 * and _function is the My Settings shooting function.
 * Changing _function initializes the setting details to Normal shooting.
 *
 * For Theta X and Z1 only.
 **/

export const ShootingMethodEnum = {
  /** Normal shooting */
  NORMAL: 'NORMAL',
  /** Interval shooting */
  INTERVAL: 'INTERVAL',
  /** Move interval shooting (RICOH THETA Z1 firmware v1.50.1 or later, RICOH THETA X is not supported) */
  MOVE_INTERVAL: 'MOVE_INTERVAL',
  /** Fixed interval shooting (RICOH THETA Z1 firmware v1.50.1 or later, RICOH THETA X is not supported) */
  FIXED_INTERVAL: 'FIXED_INTERVAL',
  /** Multi bracket shooting */
  BRACKET: 'BRACKET',
  /** Interval composite shooting (RICOH THETA X is not supported) */
  COMPOSITE: 'COMPOSITE',
  /** Continuous shooting (RICOH THETA X or later) */
  CONTINUOUS: 'CONTINUOUS',
  /** TimeShift shooting (RICOH THETA X or later) */
  TIME_SHIFT: 'TIME_SHIFT',
  /** Burst shooting (RICOH THETA Z1 v2.10.1 or later, RICOH THETA X is not supported) */
  BURST: 'BURST',
} as const;

/** Type definition of ShootingMethodEnum */
export type ShootingMethodEnum =
  (typeof ShootingMethodEnum)[keyof typeof ShootingMethodEnum];
