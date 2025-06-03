/**
 * Wireless LAN frequency of the camera.
 * Can be acquired by camera.getOptions and set by camera.setOptions.
 *
 * For Theta A1, X, Z1 amd V.
 */
export const WlanFrequencyEnum = {
  /** Undefined value */
  UNKNOWN: 'UNKNOWN',
  /** 2.4GHz */
  GHZ_2_4: 'GHZ_2_4',
  /** 5GHz */
  GHZ_5: 'GHZ_5',
  /** 5.2GHz */
  GHZ_5_2: 'GHZ_5_2',
  /** 5.8GHz */
  GHZ_5_8: 'GHZ_5_8',
} as const;

/** type definition of WlanFrequency */
export type WlanFrequencyEnum =
  (typeof WlanFrequencyEnum)[keyof typeof WlanFrequencyEnum];
