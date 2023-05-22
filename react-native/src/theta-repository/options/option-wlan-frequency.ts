/** 
 * Wireless LAN frequency of the camera.
 * Can be acquired by camera.getOptions and set by camera.setOptions.
 * 
 * For Theta X, Z1 amd V.
 */
export const WlanFrequencyEnum = {
  /** 2.4GHz */
  GHZ_2_4: 'GHZ_2_4',
  /** 5GHz */
  GHZ_5: 'GHZ_5'
} as const;

/** type definition of WlanFrequency */
export type WlanFrequencyEnum =
  (typeof WlanFrequencyEnum)[keyof typeof WlanFrequencyEnum];
