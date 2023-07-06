/*
 * [camera.stopCapture](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.stop_capture.md)
 */
package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.Serializable

/**
 * stop capture request
 */
@Serializable
data class StopCaptureRequest(
    override val name: String = "camera.stopCapture",
    override val parameters: EmptyParameter = EmptyParameter(),
) : CommandApiRequest

/**
 * stop capture response
 */
@Serializable
data class StopCaptureResponse(
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
    override val results: ResultStopCapture? = null,

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
 * stop capgure results
 */
@Serializable
data class ResultStopCapture(
    /**
     * List of file URLS
     */
    val fileUrls: List<String>? = null,
    /**
     * File URL
     * For SC2
     */
    val fileUrl: String? = null,
)
