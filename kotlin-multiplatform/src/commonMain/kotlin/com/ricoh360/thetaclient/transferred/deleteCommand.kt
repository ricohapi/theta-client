/*
 * [camera.delete](https://api.ricoh/docs/theta-web-api-v2.1/commands/camera.delete)
 */
package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * delete request
 */
@Serializable
data class DeleteRequest(
    override val name: String = "camera.delete",
    override val parameters: DeleteParams,
) : CommandApiRequest

/**
 * delete request parameters
 */
@Serializable
data class DeleteParams(
    /**
     * URLs of the file to delete The number of file URLs is up to
     * 128.  When you want to delete all at once, special parameters
     * can be used: “all” as all files, “image” as all still
     * images and “video” as all videos. (These should be specified
     * alone.)
     */
    val fileUrls: List<String>,
)

/**
 * delete response
 */
@Serializable
data class DeleteResponse(
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
