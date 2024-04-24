package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.Serializable

/**
 * Camera Event
 */
@Serializable
internal data class CameraEventResponse(
    val options: Options?,
    val state: CameraState?,
)
