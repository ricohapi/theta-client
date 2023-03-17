package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.Serializable

/**
 * Set my setting request
 */
@Serializable
data class SetMySettingRequest(
    override val name: String = "camera._setMySetting",
    override val parameters: SetMySettingParams,
) : CommandApiRequest

/**
 * Set my setting parameters
 */
@Serializable
data class SetMySettingParams(
    /**
     * The target shooting mode
     * ("image": still image capture mode, "video": video capture)
     * In RICOH THETA S and SC, do not set then it can be acquired for still image.
     */
    val mode: String? = null,
    /**
     * Names of the options specified for acquisition in the JSON format and the set of current values.
     */
    val options: Options,
)

/**
 * Set my setting response
 */
@Serializable
data class SetMySettingResponse(
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
    override val results: ResultSetMySetting? = null,

    /**
     * Error information (See Errors for details).  This output occurs
     * in state "error"
     */
    override val error: CommandError? = null,

    /**
     * Progress information.  This output occurs in state
     * "inProgress"
     */
    override val progress: CommandProgress? = null,) : CommandApiResponse

/**
 * Set my setting results
 */
@Serializable
data class ResultSetMySetting(
    /**
     * option key value pair
     */
    val options: Options,
)