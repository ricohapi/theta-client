import type { Proxy } from './option-proxy';

/**
 * IP address allocation to be used when wired LAN is enabled.
 *
 * For
 * - RICOH THETA X firmware v2.40.0 or later
 */
export type EthernetConfig = {
  /** Using DHCP or not */
  usingDhcp?: boolean;
  /** (optional) IPv4 for IP address */
  ipAddress?: string;
  /** (optional) IPv4 for subnet mask */
  subnetMask?: string;
  /** (optional) IPv4 for default gateway */
  defaultGateway?: string;
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
