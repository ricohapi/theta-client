/**
 * Authentication information used for client mode
 */
export type DigestAuth = {
  /**
   * User name
   */
  username: string;
  /**
   * Password
   *
   * If omitted, the default password is used.
   * Default password is "THETA" + "XX" after the beginning of the serial number.
   */
  password?: string;
};
