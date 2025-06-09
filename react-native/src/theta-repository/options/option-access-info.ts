/**
 * Connected network information.
 */
export type AccessInfo = {
  /**
   * SSID of the wireless LAN access point that THETA connects to
   */
  ssid: string;

  /**
   * IP address of access point
   */
  ipAddress: string;

  /**
   * subnet mask of access point
   */
  subnetMask: string;

  /**
   * default gateway of access point
   */
  defaultGateway: string;

  /**
   * Primary DNS server
   */
  dns1?: string;

  /**
   * Secondary DNS server
   */
  dns2?: string;

  /**
   * proxy URL of access point
   */
  proxyURL: string;

  /**
   * Radio frequency.
   * @see WlanFrequencyAccessInfoEnum
   */
  frequency: WlanFrequencyAccessInfoEnum;

  /**
   * WLAN signal strength.
   */
  wlanSignalStrength: number;

  /**
   * WLAN signal level.
   */
  wlanSignalLevel: number;

  /**
   * LTE signal strength.
   */
  lteSignalStrength: number;

  /**
   * LTE signal level.
   */
  lteSignalLevel: number;

  /**
   * client devices information
   */
  dhcpLeaseAddress?: DhcpLeaseAddress[];
};

/** WLAN standards of the access points. */
export const WlanFrequencyAccessInfoEnum = {
  /** Undefined value */
  UNKNOWN: 'UNKNOWN',
  /** 2.4GHz */
  GHZ_2_4: 'GHZ_2_4',
  /** 5.2GHz */
  GHZ_5_2: 'GHZ_5_2',
  /** 5.8GHz */
  GHZ_5_8: 'GHZ_5_8',
  /** Initial value */
  INITIAL_VALUE: 'INITIAL_VALUE',
} as const;

/** type definition of WlanFrequencyAccessInfoEnum */
export type WlanFrequencyAccessInfoEnum =
  (typeof WlanFrequencyAccessInfoEnum)[keyof typeof WlanFrequencyAccessInfoEnum];

/**
 * client devices information
 */
export interface DhcpLeaseAddress {
  /**
   * IP address of client device
   */
  ipAddress: string;

  /**
   * MAC address of client device
   */
  macAddress: string;

  /**
   * host name of client device
   */
  hostName: string;
}
