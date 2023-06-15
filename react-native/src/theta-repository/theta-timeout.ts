/**
 * Timeout of HTTP call.
 */
export type ThetaTimeout = {
  /**
   * Specifies a time period (in milliseconds) in
   * which a client should establish a connection with a server.
   */
  connectTimeout: number;
  /**
   * Specifies a time period (in milliseconds) required to process an HTTP call:
   * from sending a request to receiving first response bytes.
   * To disable this timeout, set its value to 0.
   */
  requestTimeout: number;
  /**
   * Specifies a maximum time (in milliseconds) of inactivity between two data packets
   * when exchanging data with a server.
   */
  socketTimeout: number;
};
