/*
 * [camera._setAccessPoint](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._set_access_point.md)
 */
package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * set access point request
 */
@Serializable
data class SetAccessPointRequest(
    override val name: String = "camera._setAccessPoint",
    override val parameters: SetAccessPointParams,
) : CommandApiRequest

/**
 * set access point request parameters
 */
@Serializable
data class SetAccessPointParams(
    /**
     * SSID (Up to 32 bytes)
     */
    val ssid: String,

    /**
     * (Optional) SSID stealth (true: enable, false: disable. Default
     * is false.)
     */
    val ssidStealth: Boolean? = null,

    /**
     * (Optional) Authentication mode("none", "WEP","WPA/WPA2 PSK")
     */
    val security: AuthenticationMode? = null,

    /**
     * (Optional) Password.
     * This can be set when security is not "none"
     */
    val password: String? = null,

    /**
     * (Optional)
     * (RICOH THETA V, Z1) Connection priority (1 to 5). Default is
     * 1.(RICOH THETA X) Fixed to 1 (The access point registered later
     * has a higher priority.
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val connectionPriority: Int? = null,

    /**
     * (Optional) IP address allocation "dynamic" or
     * "static". Default is "dynamic"
     */
    val ipAddressAllocation: IpAddressAllocation? = null,

    /**
     * (Optional) IP address assigned to camera. This setting can be
     * set when ipAddressAllocation is "static"
     */
    val ipAddress: String? = null,

    /**
     * (Optional) Subnet mask. This setting can be set when
     * ipAddressAllocation is "static"
     */
    val subnetMask: String? = null,

    /**
     * (Optional) Default gateway. This setting can be set when
     * ipAddressAllocation is "static"
     */
    val defaultGateway: String? = null,
)

/**
 * set access point response
 */
@Serializable
data class SetAccessPointResponse(
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
    override val results: JsonElement? = null,

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
