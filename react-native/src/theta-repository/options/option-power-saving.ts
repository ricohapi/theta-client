/**
 * Power saving mode.
 * The current setting can be acquired by camera.getOptions,
 * and it can be changed by camera.setOptions.
 *
 * For Theta X only.
 **/

export const PowerSavingEnum = {
  /** Power saving mode ON */
  ON: 'ON',
  /** Power saving mode OFF */
  OFF: 'OFF',
} as const;

/** Type definition of PowerSavingEnum */
export type PowerSavingEnum =
 (typeof PowerSavingEnum)[keyof typeof PowerSavingEnum];
