import type { Proxy } from './option-proxy';

/**
 * IP address allocation to be used when wired LAN is enabled.
 *
 * For
 * - RICOH THETA X firmware v2.40.0 or later
 */
export type EthernetConfig = {
  /** Using DHCP or not */
  usingDhcp: boolean;
  /** (optional) IPv4 for IP address. Do not specify this property when usingDhcp is true. */
  ipAddress?: string;
  /** (optional) IPv4 for subnet mask. Do not specify this property when usingDhcp is true. */
  subnetMask?: string;
  /** (optional) IPv4 for default gateway. Do not specify this property when usingDhcp is true. */
  defaultGateway?: string;
  /** (optional) IPv4 for Primary DNS server. Do not specify this property when usingDhcp is true. */
  dns1?: string;
  /** (optional) IPv4 for Secondary DNS server. Do not specify this property when usingDhcp is true. */
  dns2?: string;
  /**
   * (optional) refer to _proxy for detail
   *
   * If "use" is set to true, "url" and "port" must be set.
   * "userid" and "password" must be set together.
   * It is recommended to set proxy as this three patterns:
   * - (use = false)
   * - (use = true, url = {url}, port = {port})
   * - (use = true, url = {url}, port = {port}, userid = {userid}, password = {password})
   */
  proxy?: Proxy;
};
