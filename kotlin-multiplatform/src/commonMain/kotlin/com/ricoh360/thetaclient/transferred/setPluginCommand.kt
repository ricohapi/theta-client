package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Set plugin request
 */
@Serializable
data class SetPluginRequest(
    override val name: String = "camera._setPlugin",
    override val parameters: SetPluginParams,
) : CommandApiRequest

/**
 * Sets the installed plugin for boot.
 * Supported just by Theta V.
 */
@Serializable
data class SetPluginParams(
    /**
     * Target package name for boot
     */
    val packageName: String,
    /**
     * Boot selection
     */
    val boot: Boolean,
)

/**
 * Set plugin response
 */
@Serializable
data class SetPluginResponse(
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