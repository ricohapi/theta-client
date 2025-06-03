/*
 * [camera._listAccessPoints](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._list_access_points.md)
 */
package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * list access points request
 */
@Serializable
internal data class ListAccessPointsRequest(
    override val name: String = "camera._listAccessPoints",
    override val parameters: EmptyParameter = EmptyParameter(),
) : CommandApiRequest

/**
 * list access points response
 */
@Serializable
internal data class ListAccessPointsResponse(
    /**
     * Executed command
     */
    override val name: String,

    /**
     * Command execution status
     * @see CommandState
     */
    override val state: CommandState,

    /**
     * Command ID used to check the execution status with
     * Commands/Status
     */
    override val id: String? = null,

    /**
     * Results when each command is successfully executed.  This
     * output occurs in state "done"
     */
    override val results: ResultListAccessPoints? = null,

    /**
     * Error information (See Errors for details).  This output occurs
     * in state "error"
     */
    override val error: CommandError? = null,

    /**
     * Progress information.  This output occurs in state
     * "inProgress"
     */
    override val progress: CommandProgress? = null,
) : CommandApiResponse

/**
 * list access points results
 */
@Serializable
internal data class ResultListAccessPoints(
    /**
     * Lists the access points stored on the camera and the access
     * points detected by the camera.  See the next section for
     * details.
     */
    val accessPoints: List<AccessPoint>,
)

/**
 * access point infomation
 */
@Serializable
internal data class AccessPoint(
    /**
     * SSID (Up to 32 bytes)
     */
    val ssid: String,

    /**
     * (Optional)
     * SSID stealth
     * (true: enable, false: disable. Default is false.)
     */
    val ssidStealth: Boolean,

    /**
     * (Mandatory)
     * Authentication mode
     * ("none", "WEP", "WPA/WPA2 PSK")
     * This can be optional parameter only when overwriting access point information.
     */
    val security: AuthenticationMode,

    /**
     * (Optional)
     * (RICOH THETA V, Z1)
     * Connection priority (1 to 5). Default is 1.
     *
     * (RICOH THETA X)
     * Fixed to 1 (The access point registered later has a higher priority.)
     */
    val connectionPriority: Int,

    /**
     * (Optional)
     * IP address allocation
     * "dynamic" or "static". Default is "dynamic"
     */
    val ipAddressAllocation: IpAddressAllocation,

    /**
     * (Optional)
     * IP address assigned to camera
     * This setting must be set when ipAddressAllocation is "static"
     */
    val ipAddress: String?,

    /**
     * (Optional)
     * Subnet mask
     * This setting must be set when ipAddressAllocation is "static"
     */
    val subnetMask: String?,

    /**
     * (Optional)
     * Default gateway
     * This setting must be set when ipAddressAllocation is "static"
     */
    val defaultGateway: String?,

    /**
     * (Optional)
     * Primary DNS server.
     * This setting must be set when ipAddressAllocation is "static"
     */
    var dns1: String?,

    /**
     * (Optional)
     * Secondary DNS server.
     * This setting must be set when ipAddressAllocation is "static"
     */
    var dns2: String?,

    /**
     * Proxy information to be used for the access point.
     * Also refer to _proxy option spec to set each parameter.
     *
     * For
     * THETA Z1 Version 2.20.3 or later
     * THETA X Version 2.00.0 or later
     */
    @SerialName("_proxy")
    val proxy: Proxy? = null,
)

internal object AuthenticationModeSerializer :
    SerialNameEnumIgnoreUnknownSerializer<AuthenticationMode>(AuthenticationMode.entries, AuthenticationMode.UNKNOWN)

/**
 * authentication mode
 */
@Serializable(with = AuthenticationModeSerializer::class)
internal enum class AuthenticationMode : SerialNameEnum {
    /**
     * Undefined value
     */
    UNKNOWN,

    /**
     * no authetication required
     */
    NONE {
        override val serialName: String = "none"
    },

    /**
     * WEP authentication
     */
    WEP {
        override val serialName: String = "WEP"
    },

    /**
     * WPA or WPA2 PSK authentication
     */
    WPA_WPA2_PSK {
        override val serialName: String = "WPA/WPA2 PSK"
    },

    /**
     * WPA3-SAE authentication
     */
    WPA3_SAE {
        override val serialName: String = "WPA3-SAE"
    },
}

/**
 * ip address allocation
 */
@Serializable
internal enum class IpAddressAllocation {
    /**
     * dynamic ip address allocation
     */
    @SerialName("dynamic")
    DYNAMIC,

    /**
     * static ip address allocation
     */
    @SerialName("static")
    STATIC,
}
