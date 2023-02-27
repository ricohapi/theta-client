/*
 * [camera.getLivePreview](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.get_live_preview.md)
 */
package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.Serializable

/**
 * get live preview request
 */
@Serializable
data class GetLivePreviewRequest(
    override val name: String = "camera.getLivePreview",
    override val parameters: EmptyParameter = EmptyParameter(),
) : CommandApiRequest
