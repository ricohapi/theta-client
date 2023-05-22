/**
 * Network type of the camera.
 * Can be acquired by camera.getOptions and set by camera.setOptions.
 * 
 * For Theta X, Z1 and V.
 */
export const NetworkTypeEnum = {
  /** Direct mode */
  DIRECT: 'DIRECT',
  /** Client mode via WLAN */
  CLIENT: 'CLIENT',
  /** Client mode via Ethernet cable */
  ETHERNET: 'ETHERNET',
  /** Network is off. This value can be gotten only by plugin. */
  OFF: 'OFF',
} as const;
  
/** Type definition of NetworkTypeEnum */
export type NetworkTypeEnum =
  (typeof NetworkTypeEnum)[keyof typeof NetworkTypeEnum];