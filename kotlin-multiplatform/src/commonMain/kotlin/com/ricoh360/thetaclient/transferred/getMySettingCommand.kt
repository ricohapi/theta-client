package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.Serializable

/**
 * get my setting request
 */
@Serializable
internal data class GetMySettingRequest(
    override val name: String = "camera._getMySetting",
    override val parameters: GetMySettingParams,
) : CommandApiRequest

/**
 * get my setting parameters
 */
@Serializable
internal data class GetMySettingParams(
    /**
     * The target shooting mode
     * ("image": still image capture mode, "video": video capture)
     * In RICOH THETA S and SC, do not set then it can be acquired for still image.
     */
    val mode: CaptureMode? = null,
    /**
     * option name list to be acquired
     * Other than Theta S and SC, do not set value then all properties are acquired.
     */
    val optionNames: List<String>? = null,
)

/**
 * get my setting response
 */
@Serializable
internal data class GetMySettingResponse(
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
    override val results: ResultGetMySetting? = null,

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
internal data class ResultGetMySetting(
    /**
     * option key value pair
     */
    val options: Options,
)