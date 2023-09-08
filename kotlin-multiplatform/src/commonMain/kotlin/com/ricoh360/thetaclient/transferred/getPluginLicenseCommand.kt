/*
 * [camera._getPluginLicense](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._get_plugin_license.md)
 */
package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.Serializable

@Serializable
internal data class GetPluginLicenseRequest(
    override val name: String = "camera._getPluginLicense",
    override val parameters: GetPluginLicenseParams,
) : CommandApiRequest

@Serializable
internal data class GetPluginLicenseParams(
    /**
     * Package name of the target plugin
     */
    val packageName: String,
)

