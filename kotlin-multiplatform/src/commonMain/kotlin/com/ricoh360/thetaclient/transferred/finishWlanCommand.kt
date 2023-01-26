/*
 * [camera._finishWlan](https://api.ricoh/docs/theta-web-api-v2.1/commands/camera._finish_wlan)
 */
package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * finish wlan request
 */
@Serializable
data class FinishWlanRequest(
    override val name: String = "camera._finishWlan",
    override val parameters: EmptyParameter = EmptyParameter(),
) : CommandApiRequest

/**
 * finish wlan response
 */
@Serializable
data class FinishWlanResponse(
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
