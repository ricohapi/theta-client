/*
 * commands API
 */
package com.ricoh360.thetaclient.transferred

import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * POST [/osc/commands/execute](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/protocols/commands_execute.md)
 */
internal object CommandApi {
    const val PATH = "/osc/commands/execute"
    val METHOD = HttpMethod.Post
}

/**
 * command api request interface
 */
internal interface CommandApiRequest {
    /**
     * Command to execute
     */
    val name: String

    /**
     * Input parameters required to execute each command
     */
    val parameters: Any
}

/**
 * command api response interface
 */
internal interface CommandApiResponse {
    /**
     * Executed command
     */
    val name: String

    /**
     * Command execution status, either "done", "inProgress" or
     * "error" is returned
     */
    val state: CommandState

    /**
     * Command ID used to check the execution status with
     * Commands/Status
     */
    val id: String?

    /**
     * Results when each command is successfully executed.  This
     * output occurs in state "done"
     */
    val results: Any?

    /**
     * Error information (See Errors for details).  This output occurs
     * in state "error"
     *
     * @see CommandError
     */
    val error: CommandError?

    /**
     * Progress information.  This output occurs in state
     * "inProgress"
     *
     * @see CommandProgress
     */
    val progress: CommandProgress?
}

/**
 * state in command api response
 */
@Serializable
internal enum class CommandState {
    /**
     * command complete
     */
    @SerialName("done")
    DONE,

    /**
     * command in progress
     */
    @SerialName("inProgress")
    IN_PROGRESS,

    /**
     * command error
     */
    @SerialName("error")
    ERROR,
}

/**
 * error in command api response
 */
@Serializable
internal data class CommandError(
    /**
     *  Error code
     */
    val code: String,

    /**
     * Error message
     */
    val message: String
) {
    companion object {
        private const val CODE_CANCELED_SHOOTING = "canceledShooting"
    }

    fun isCanceledShootingCode(): Boolean {
        return this.code == CODE_CANCELED_SHOOTING
    }
}

/**
 * completion at inProgress status
 */
@Serializable
internal data class CommandProgress(
    /**
     * Progress rate of command executed
     */
    val completion: Float,
)

/**
 * empty parameters
 */
@Serializable
internal class EmptyParameter

/**
 * Unknown Response data class
 */
@Serializable
internal data class UnknownResponse(
    /**
     * Executed command
     */
    override val name: String,

    /**
     * Command execution status
     * @see CommandState
     */
    override val state: CommandState = CommandState.ERROR,

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
