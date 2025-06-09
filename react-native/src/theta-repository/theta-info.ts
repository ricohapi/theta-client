/** Support THETA model */
export const ThetaModel = {
  /** THETA S */
  THETA_S: 'THETA_S',
  /** THETA SC */
  THETA_SC: 'THETA_SC',
  /** THETA V */
  THETA_V: 'THETA_V',
  /** THETA Z1 */
  THETA_Z1: 'THETA_Z1',
  /** THETA X */
  THETA_X: 'THETA_X',
  /** THETA SC2 */
  THETA_SC2: 'THETA_SC2',
  /** THETA SC2 for business */
  THETA_SC2_B: 'THETA_SC2_B',
  /** THETA A1 */
  THETA_A1: 'THETA_A1',
} as const;

/** type definition of ThetaModel */
export type ThetaModel = (typeof ThetaModel)[keyof typeof ThetaModel];

/** Static attributes of Theta. */
export type ThetaInfo = {
  /** Manufacturer name */
  manufacturer: string;
  /** Theta model name */
  model: string;
  /** Theta serial number */
  serialNumber: string;
  /** MAC address of wireless LAN
   * (RICOH THETA V firmware v2.11.1 or later)
   *
   * For THETA X, firmware versions v2.63.0 and earlier display `the communication MAC address`,
   * while v2.71.1 and later diplay `the physical MAC address`.
   * For other than THETA X, `the physical MAC address` is displayed.
   */
  wlanMacAddress: string | null;
  /** MAC address of Bluetooth (RICOH THETA V firmware v2.11.1 or later) */
  bluetoothMacAddress: string | null;
  /** Theta firmware version */
  firmwareVersion: string;
  /* URL of the support page */
  supportUrl: string;
  /** true if Theta has GPS. */
  hasGps: boolean;
  /** true if Theta has Gyroscope */
  hasGyro: boolean;
  /** Number of seconds since Theta boot */
  uptime: number;
  /** List of supported APIs */
  api: string[];
  /** Endpoint information */
  endpoints: EndPoint;
  /** List of supported APIs (1: v2.0, 2: v2.1) */
  apiLevel: number[];
  /** THETA model */
  thetaModel?: ThetaModel;
};

/** endpoint information */
export type EndPoint = {
  httpPort: number;
  httpUpdatesPort: number;
};
