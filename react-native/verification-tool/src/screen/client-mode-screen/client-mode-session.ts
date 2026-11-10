import { initialize } from '../../modules/theta-client';

/**
 * Connection in client mode, kept while the screens under the "Client Mode" menu are shown.
 * The "Client Mode" screen is unmounted when moving to a sub screen, so this is kept outside of it.
 */
export const clientModeSession = {
    /** true while THETA is in client mode, i.e. it has to be switched back to AP mode */
    active: false,
    /** SSID of the access point THETA joins */
    ssid: '',
    /** "THETA" + prefix + serial number such as "THETAYR12345678", the SSID "<name>.OSC" in AP mode */
    name: '',
    /** host name "<name>.local" */
    host: '',
    /** username and password of digest authentication */
    username: '',
    password: undefined as string | undefined,
};

/**
 * Connect to THETA in client mode with the current username and password of [clientModeSession].
 */
export const connectClientMode = (timeout?: {
    connectTimeout: number;
    requestTimeout: number;
    socketTimeout: number;
}) => {
    const { host, username, password } = clientModeSession;
    return initialize(`http://${host}`, { clientMode: { username, password } }, timeout);
};
