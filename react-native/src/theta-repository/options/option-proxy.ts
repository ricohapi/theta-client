/**
 * Proxy information to be used when wired LAN is enabled.
 *
 * The current setting can be acquired by camera.getOptions,
 * and it can be changed by camera.setOptions.
 *
 * For
 * RICOH THETA Z1 firmware v2.20.3 or later
 * RICOH THETA X firmware v2.00.0 or later
 */
export type Proxy = {
  /** true: use proxy false: do not use proxy */
  use?: boolean;
  /** Proxy server URL */
  url?: string;
  /** Proxy server port number: 0 to 65535 */
  port?: number;
  /** User ID used for proxy authentication */
  userid?: string;
  /** Password used for proxy authentication */
  password?: string;
};
