/**
 * Network type of the camera.
 * Can be acquired by camera.getOptions and set by camera.setOptions.
 *
 * For Theta A1, X, Z1 and V.
 */
export const NetworkTypeEnum = {
  /** Undefined value */
  UNKNOWN: 'UNKNOWN',
  /** Direct mode */
  DIRECT: 'DIRECT',
  /** Client mode via WLAN */
  CLIENT: 'CLIENT',
  /** Client mode via Ethernet cable */
  ETHERNET: 'ETHERNET',
  /** Network is off. This value can be gotten only by plugin. */
  OFF: 'OFF',
  /** LTE plan-D */
  LTE_D: 'LTE_D',
  /** LTE plan-DU */
  LTE_DU: 'LTE_DU',
  /** LTE plan01s */
  LTE_01S: 'LTE_01S',
  /** LTE planX3 */
  LTE_X3: 'LTE_X3',
  /** LTE planP1 */
  LTE_P1: 'LTE_P1',
  /** LTE plan-K2 */
  LTE_K2: 'LTE_K2',
  /** LTE plan-K */
  LTE_K: 'LTE_K',
} as const;

/** Type definition of NetworkTypeEnum */
export type NetworkTypeEnum =
  (typeof NetworkTypeEnum)[keyof typeof NetworkTypeEnum];
