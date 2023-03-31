package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.Serializable

/**
 * get plugin order request
 */
@Serializable
data class GetPluginOrdersRequest(
    override val name: String = "camera._getPluginOrders",
    override val parameters: EmptyParameter = EmptyParameter(),
) : CommandApiRequest

/**
 * get plugin order response
 */
@Serializable
data class GetPluginOrdersResponse(
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
    override val results: ResultGetPluginOrders? = null,

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
 * plugin order results
 */
@Serializable
data class ResultGetPluginOrders(
    /**
     * Supported just by Theta X and  Z1.
     * For Z1, list of three package names for the start-up plugin.
     * No restrictions for the number of package names for X.
     */
    val pluginOrders: List<String>,
)