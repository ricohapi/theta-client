/**
 * White balance auto strength
 *
 * To set the strength of white balance auto for low color temperature scene.
 * This option can be set for photo mode and video mode separately.
 * Also this option will not be cleared by power-off.
 *
 * For RICOH THETA Z1 firmware v2.20.3 or later
 */
export const WhiteBalanceAutoStrengthEnum = {
  /** ON */
  ON: 'ON',
  /** OFF */
  OFF: 'OFF',
} as const;

/** type definition of WhiteBalanceAutoStrengthEnum */
export type WhiteBalanceAutoStrengthEnum =
  (typeof WhiteBalanceAutoStrengthEnum)[keyof typeof WhiteBalanceAutoStrengthEnum];
