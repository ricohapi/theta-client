/*
 * [camera._cancelVideoConvert](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._cancel_video_convert.md)
 */
package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * cancel video convert request
 */
@Serializable
internal data class CancelVideoConvertRequest(
    override val name: String = "camera._cancelVideoConvert",
    override val parameters: EmptyParameter = EmptyParameter(),
) : CommandApiRequest

/**
 * cancel video convert response
 */
@Serializable
internal data class CancelVideoConvertResponse(
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
