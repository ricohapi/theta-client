import type { Proxy } from './options';

/**
 * Enum for authentication mode.
 */
export const AuthModeEnum = {
  /** Undefined value */
  UNKNOWN: 'UNKNOWN',
  /** None of authentication */
  NONE: 'NONE',
  /** Wep authentication */
  WEP: 'WEP',
  /** WPA PSK or WPA2 PSK authentication */
  WPA: 'WPA',
  /** WPA3-SAE authentication */
  WPA3: 'WPA3',
} as const;

/** type definition of AuthModeEnum */
export type AuthModeEnum = (typeof AuthModeEnum)[keyof typeof AuthModeEnum];

/** Access point information. */
export type AccessPoint = {
  /** SSID */
  ssid: string;
  /** SSID stealth. Default is false. */
  ssidStealth: boolean;
  /** Authentication mode. */
  authMode: AuthModeEnum;
  /** Connection priority (1 to 5). Default is 1. */
  connectionPriority: number;
  /** Using DHCP or not. */
  usingDhcp: boolean;
  /** IP address assigned to camera. */
  ipAddress?: string;
  /** Subnet Mask. */
  subnetMask?: string;
  /** Default Gateway. */
  defaultGateway?: string;
  /** Primary DNS server. */
  dns1?: string;
  /** Secondary DNS server. */
  dns2?: string;
  /** Proxy information to be used when wired LAN is enabled. */
  proxy?: Proxy;
};
