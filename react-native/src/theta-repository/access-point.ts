import type { Proxy } from './options';

/**
 * Enum for authentication mode.
 */
export const AuthModeEnum = {
  /** None of authentication */
  NONE: 'NONE',
  /** Wep authentication */
  WEP: 'WEP',
  /** WPA PSK or WPA2 PSK authentication */
  WPA: 'WPA',
} as const;

/** type definition of AuthModeEnum */
export type AuthModeEnum = (typeof AuthModeEnum)[keyof typeof AuthModeEnum];

/** Access point information. */
export type AccessPoint = {
  /** SSID of the access point. */
  ssid: string;
  /** true if SSID stealth is enabled. */
  ssidStealth: boolean;
  /** Authentication mode. */
  authMode: AuthModeEnum;
  /** Connection priority 1 to 5. */
  connectionPriority: number;
  /** Using DHCP or not. */
  usingDhcp: boolean;
  /** IP address assigned to camera. */
  ipAddress?: string;
  /** Subnet Mask. */
  subnetMask?: string;
  /** Default Gateway. */
  defaultGateway?: string;
  /** Proxy information to be used when wired LAN is enabled. */
  proxy?: Proxy;
};
