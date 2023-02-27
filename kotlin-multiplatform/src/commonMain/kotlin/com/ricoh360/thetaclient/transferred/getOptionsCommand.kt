/*
 * [camera.getOptions](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.get_options.md)
 */
package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.Serializable

/**
 * get options request
 */
@Serializable
data class GetOptionsRequest(
    override val name: String = "camera.getOptions",
    override val parameters: GetOptionsParams,
) : CommandApiRequest

/**
 * get options parameters
 */
@Serializable
data class GetOptionsParams(
    /**
     * option name list to be acquired
     */
    val optionNames: List<String>,
)

/**
 * get options response
 */
@Serializable
data class GetOptionsResponse(
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
    override val results: ResultGetOptions? = null,

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
data class ResultGetOptions(
    /**
     * option key value pair
     */
    val options: Options,
)
