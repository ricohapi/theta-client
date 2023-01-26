/*
 * [camera.takePicture](https://api.ricoh/docs/theta-web-api-v2.1/commands/camera.take_picture/)
 */
package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.Serializable

/**
 * take picture request
 */
@Serializable
data class TakePictureRequest(
    override val name: String = "camera.takePicture",
    override val parameters: EmptyParameter = EmptyParameter(),
) : CommandApiRequest

/**
 * take picture Response
 */
@Serializable
data class TakePictureResponse(
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
    override val results: ResultTakePicture? = null,

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
 * take picture results
 */
@Serializable
data class ResultTakePicture(
    /**
     * URL of the captured still image file
     */
    val fileUrl: String,

    /**
     * DNG file URL when shooting with raw+ (THETA Z1 or later)
     */
    val _dngFileUrl: String? = null,
)
