/*
 * [/osc/info](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/protocols/info.md)
 */
package com.ricoh360.thetaclient.transferred

import io.ktor.http.HttpMethod
import kotlinx.serialization.Serializable

/**
 * info api request
 */
internal object InfoApi {
    const val PATH = "/osc/info"
    val METHOD = HttpMethod.Get
}

/**
 * info api response
 */
@Serializable
internal data class InfoApiResponse(
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
     *
     * For THETA X, firmware versions v2.63.0 and earlier display `the communication MAC address`,
     * while v2.71.1 and later diplay `the physical MAC address`.
     * For other than THETA X, `the physical MAC address` is displayed.
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
