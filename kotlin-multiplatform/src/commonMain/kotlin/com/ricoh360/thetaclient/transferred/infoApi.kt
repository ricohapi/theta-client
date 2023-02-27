/*
 * [/osc/info](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/protocols/info.md)
 */
package com.ricoh360.thetaclient.transferred

import io.ktor.http.*
import kotlinx.serialization.Serializable

/**
 * info api request
 */
object InfoApi {
    const val PATH = "/osc/info"
    val METHOD = HttpMethod.Get
}

/**
 * info api response
 */
@Serializable
data class InfoApiResponse(
    /**
     * Manufacturer name
     */
    val manufacturer: String,

    /**
     * model
     */
    val model: String,

    /**
     * Serial number
     */
    val serialNumber: String,

    /**
     * MAC address of wireless LAN
     * (RICOH THETA V firmware v2.11.1 or later)
     */
    val _wlanMacAddress: String?,

    /**
     * MAC address of Bluetooth
     * (RICOH THETA V firmware v2.11.1 or later)
     */
    val _bluetoothMacAddress: String?,

    /**
     * Firmware version
     */
    val firmwareVersion: String,

    /**
     * URL of the support page
     */
    val supportUrl: String,

    /**
     * Presence of GPS
     */
    val gps: Boolean,

    /**
     * Presence of gyroscope
     */
    val gyro: Boolean,

    /**
     * Elapsed time after startup in second
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val uptime: Int,

    /**
     * List of supported APIs
     */
    val api: List<String>,

    /**
     * Endpoint information (Refer to the next section for details.)
     */
    val endpoints: EndPoint,

    /**
     * List of supported APIs: ("1": v2.0, "2": v2.1)
     */
    val apiLevel: List<Int>,
)

/**
 * endpoint information
 */
@Serializable
data class EndPoint(
    @Serializable(with = NumberAsIntSerializer::class)
    val httpPort: Int,
    @Serializable(with = NumberAsIntSerializer::class)
    val httpUpdatesPort: Int,
)
