/**
 * Configure SISO or MIMO for Wireless LAN.
 */
export const WlanAntennaConfigEnum = {
  /** Undefined value */
  UNKNOWN: 'UNKNOWN',
  /** SISO */
  SISO: 'SISO',
  /** MIMO */
  MIMO: 'MIMO',
} as const;

/** type definition of WlanAntennaConfigEnum */
export type WlanAntennaConfigEnum =
  (typeof WlanAntennaConfigEnum)[keyof typeof WlanAntennaConfigEnum];
