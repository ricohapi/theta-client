/**
 * BurstMode setting.
 * When this is set to ON, burst shooting is enabled,
 * and a screen dedicated to burst shooting is displayed in Live View.
 */
export const BurstModeEnum = {
  /** BurstMode ON */
  ON: 'ON',
  /** BurstMode OFF */
  OFF: 'OFF',
} as const;

/** Type definition of BurstModeEnum */
export type BurstModeEnum = (typeof BurstModeEnum)[keyof typeof BurstModeEnum];
