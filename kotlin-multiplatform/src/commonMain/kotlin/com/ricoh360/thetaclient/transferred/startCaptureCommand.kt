/*
 * [camera.startCapture](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.start_capture.md)
 */
package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * start capture request
 */
@Serializable
internal data class StartCaptureRequest(
    override val name: String = "camera.startCapture",
    override val parameters: StartCaptureParams,
) : CommandApiRequest

/**
 * start capture request parameters
 */
@Serializable
internal data class StartCaptureParams(
    /**
     * Continuously shoots in the still image shooting mode.  Interval
     * shooting is started if this parameter is omitted
     * This parameter cannot be specified in the movie shooting mode.
     *
     * @see ShootingMode
     */
    val _mode: ShootingMode? = null,
)

/**
 * Shooting mode
 */
@Serializable
internal enum class ShootingMode {
    /**
     * Interval shooting
     */
    @SerialName("interval")
    INTERVAL_SHOOTING,

    /**
     * Interval composite shooting
     */
    @SerialName("composite")
    INTERVAL_COMPOSITE_SHOOTING,

    /**
     * Multi bracket shooting
     */
    @SerialName("bracket")
    MULTI_BRACKET_SHOOTING,

    /**
     * Time shift shooting
     */
    @SerialName("timeShift")
    TIME_SHIFT_SHOOTING,

    /**
     * Burst shooting
     */
    @SerialName("burst")
    BURST_SHOOTING,

    /**
     * Continuous shooting
     */
    @SerialName("continuous")
    CONTINUOUS_SHOOTING,

    //MOVIE_SHOOTING("")

    /**
     * Start time-lapse photography by manual lens
     */
    @SerialName("timeShift_manual")
    TIMESHIFT_MANUAL_SHOOTING,
}

/**
 * start capture response
 */
@Serializable
internal data class StartCaptureResponse(
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
    override val results: ResultStartCapture? = null,

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

    /**
     * List of file URLs.
     *
     * For RICOH THETA X v2.61.0 or later
     */
    val _fileUrls: List<String>? = null,
) : CommandApiResponse

/**
 * start capture results
 */
@Serializable
internal data class ResultStartCapture(
    /**
     * List of file URLs.
     * For others than Theta SC2.
     */
    val fileUrls: List<String>? = null,
    /**
     * File URL.
     * For theta SC2 only.
     */
    val fileUrl: String? = null,
)
