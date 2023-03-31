package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Set plugin orders request
 */
@Serializable
data class SetPluginOrdersRequest(
    override val name: String = "camera._setPluginOrders",
    override val parameters: SetPluginOrdersParams,
) : CommandApiRequest

/**
 * Set plugin orders parameters
 */
@Serializable
data class SetPluginOrdersParams(
    /**
     * List of plugin package names.
     * For Z1, array of three package names for the start-up plugin. No restrictions for the number of package names for X.
     * When not specifying, set an empty string. If an empty string is placed mid-way, it will be moved to the front.
     * Specifying zero package name will result in an error.
     */
    val pluginOrders: List<String>,
)

/**
 * Set plugin orders response
 */
@Serializable
data class SetPluginOrdersResponse(
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
