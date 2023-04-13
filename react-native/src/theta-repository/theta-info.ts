/** Static attributes of Theta. */
export type ThetaInfo = {
  /** Manufacturer name */
  manufacturer: string;
  /** Theta model name */
  model: string;
  /** Theta serial number */
  serialNumber: string;
  /** MAC address of wireless LAN (RICOH THETA V firmware v2.11.1 or later) */
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
};

/** endpoint information */
export type EndPoint = {
  httpPort: number;
  httpUpdatesPort: number;
};
