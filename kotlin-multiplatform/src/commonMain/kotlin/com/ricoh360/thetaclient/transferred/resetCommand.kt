/*
 * [camera.reset](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.reset.md)
 */
package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * reset request
 */
@Serializable
internal data class ResetRequest(
    override val name: String = "camera.reset",
    override val parameters: EmptyParameter = EmptyParameter(),
) : CommandApiRequest

/**
 * reset response
 */
@Serializable
internal data class ResetResponse(
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
