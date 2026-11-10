import { AuthModeEnum } from './modules/theta-client';

/**
 * Settings of the "Client Mode" menu.
 * Commit with empty values.
 */
const clientModeConfig = {
    /** Wi-Fi network (router or phone tethering) that THETA joins in client mode. Registered to THETA in AP mode. */
    ssid: '',
    password: '',
    /** WPA covers WPA-PSK and WPA2-PSK. Use AuthModeEnum.NONE for an open network. */
    authMode: AuthModeEnum.WPA as AuthModeEnum,
    /**
     * Username of digest authentication in client mode, set only when changed from the default one.
     * Leave it empty to use the default one "THETA" + prefix + serial number such as "THETAYR12345678".
     * The SSID "<name>.OSC" and the host name "<name>.local" are always made from the serial number.
     */
    username: '',
    /**
     * Password of client mode, set to THETA in AP mode and used to connect in client mode.
     * Required for THETA A1, X (serial YR10/YR20/YR30) and Z1 (serial YN10-YN50), which have no default password.
     * Leave it empty to keep THETA's password and use the default one (numeric portion of the serial number).
     */
    thetaPassword: '',
    /**
     * Password of THETA's Wi-Fi in AP mode, used to connect the phone to THETA when leaving the client mode menu.
     * Set the actual one. A wrong one breaks the network saved on iOS, which then has to be forgotten in the settings app.
     * Empty uses the numeric portion of the serial number, which is not the password of many models.
     */
    thetaApPassword: '',
};

export default clientModeConfig;
