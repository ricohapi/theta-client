package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.Serializable

/**
 * list plugins request
 */
@Serializable
data class ListPluginsRequest(
    override val name: String = "camera._listPlugins",
    override val parameters: EmptyParameter = EmptyParameter(),
) : CommandApiRequest

/**
 * list plugins response
 */
@Serializable
data class ListPluginsResponse(
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
    override val results: ResultListPlugins? = null,

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
 * get options results
 */
@Serializable
data class ResultListPlugins(
    val plugins: List<Plugin>,
)

@Serializable
data class Plugin(
    /**
     * Plugin name
     */
    val pluginName: String,

    /**
     * Package name
     */
    val packageName: String,

    /**
     * Plugin version
     */
    val version: String,

    /**
     * The type of application, "system": Pre-installed plugin, "user": User-added plugin
     */
    val type: String,

    /**
     * Plugin power status, true: running, false: stop
     */
    val running: Boolean,

    /**
     * Process status, true: foreground, false: background
     */
    val foreground: Boolean,

    /**
     * Boot selection, true: Target plugin to be started; false: Do not start this plugin
     */
    val boot: Boolean,

    /**
     * Just Reserved
     */
    val force: Boolean? = null,

    /**
     * Just Reserved
     */
    val bootOptions: String? = null,

    /**
     * Existence of web server. true: Has WebUI, false: Does not have WebUI
     */
    val webServer: Boolean,

    /**
     * Exit status
     */
    val exitStatus: String,

    /**
     * Message
     */
    val message: String,
)