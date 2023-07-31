package com.ricoh360.thetaclient.transferred

import io.ktor.http.HttpMethod
import kotlinx.serialization.Serializable

/**
 * Firmware update API request.
 */
object UpdateFirmwareApi {
    val method = HttpMethod.Post
}

/**
 * Firmware update API response
 */
@Serializable
data class UpdateFirmwareApiResponse(
    /**
     * Executed API
     */
    val name: String,

    /**
     * Command execution status, either "done", "inProgress" or
     * "error" is returned
     */
    val state: CommandState,

    /**
     * Error information (See Errors for details).
     * This output occurs in state "error"
     *
     * @see CommandError
     */
    val error: CommandError? = null
)