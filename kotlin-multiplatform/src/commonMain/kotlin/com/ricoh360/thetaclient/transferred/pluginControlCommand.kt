package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Plugin control request
 */
@Serializable
internal data class PluginControlRequest(
    override val name: String = "camera._pluginControl",
    override val parameters: PluginControlParams,
) : CommandApiRequest

/**
 * Starts or stops plugin.
 * Supported just by Theta V and later.
 */
@Serializable
internal data class PluginControlParams(
    /**
     * Type of action: ("boot": start plugin; "finish": stop)
     */
    val action: String,
    /**
     * Target plugin package name
     * If no target is specified, then Plugin 1 will start. This parameter is ignored when action parameter is "finish".
     * Not supported by Theta V.
     */
    val plugin: String? = null,
)

/**
 * Set plugin response
 */
@Serializable
internal data class PluginControlResponse(
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