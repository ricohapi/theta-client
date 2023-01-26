/*
 * [camera._listAccessPoints](https://api.ricoh/docs/theta-web-api-v2.1/commands/camera._list_access_points/)
 */
package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * list access points request
 */
@Serializable
data class ListAccessPointsRequest(
    override val name: String = "camera._listAccessPoints",
    override val parameters: EmptyParameter = EmptyParameter(),
) : CommandApiRequest

/**
 * list access points response
 */
@Serializable
data class ListAccessPointsResponse(
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
data class ResultListAccessPoints(
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
data class AccessPoint(
    /**
     * SSID
     */
    val ssid: String,

    /**
     * True if SSID stealth is enabled
     */
    val ssidStealth: Boolean,

    /**
     * Authentication mode
     */
    val security: AuthenticationMode,

    /**
     * Connection priority
     */
    val connectionPriority: Int,

    /**
     * IP address allocation. This can be acquired when SSID is
     * registered as an enable access point.
     */
    val ipAddressAllocation: IpAddressAllocation,

    /**
     * IP address assigned to camera. This setting can be acquired
     * when "ipAddressAllocation" is "static"
     */
    val ipAddress: String?,

    /**
     * Subnet Mask. This setting can be acquired when
     * "ipAddressAllocation" is "static"
     */
    val subnetMask: String?,

    /**
     * Default Gateway. This setting can be acquired when
     * "ipAddressAllocation" is "static
     */
    val defaultGateway: String?,
)

/**
 * authentication mode
 */
@Serializable
enum class AuthenticationMode {
    /**
     * no authetication required
     */
    @SerialName("none")
    NONE,

    /**
     * WEP authentication
     */
    @SerialName("WEP")
    WEP,

    /**
     * WPA or WPA2 PSK authentication
     */
    @SerialName("WPA/WPA2 PSK")
    WPA_WPA2_PSK,
}

/**
 * ip address allocation
 */
@Serializable
enum class IpAddressAllocation {
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
