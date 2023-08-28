/**
 * Shooting function.
 * Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
 * Setting them at the same time as exposureDelay will result in an error.
 *
 * For
 * - RICOH THETA X
 * - RICOH THETA Z1
 */
export const ShootingFunctionEnum = {
  /** Normal shooting function */
  NORMAL: 'NORMAL',
  /** Self-timer shooting function(RICOH THETA X is not supported.) */
  SELF_TIMER: 'SELF_TIMER',
  /** My setting shooting function */
  MY_SETTING: 'MY_SETTING',
} as const;

/** type definition of ShootingFunctionEnum */
export type ShootingFunctionEnum =
  (typeof ShootingFunctionEnum)[keyof typeof ShootingFunctionEnum];
