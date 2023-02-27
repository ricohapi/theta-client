/*
 * [camera._convertVideoFormats](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._convert_video_formats.md)
 */
package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Convert video formats request
 */
@Serializable
data class ConvertVideoFormatsRequest(
    override val name: String = "camera._convertVideoFormats",
    override val parameters: ConvertVideoFormatsParams,
) : CommandApiRequest

/**
 * Convert video format parameters
 */
@Serializable
data class ConvertVideoFormatsParams(
    /**
     * URL of a saved movie file
     */
    val fileUrl: String,

    /**
     * Recorded size (pixels)
     * Z1, V: Specify "3840 x 1920" or "1920 x 960".
     * X: Specify "3840 x 1920".
     *
     * @see VideoFormat
     */
    val size: VideoFormat? = null,

    /**
     * Projection type of movie file
     * Specify "Equirectangular" .
     *
     * RICOH THETA X is not supported.
     */
    val projectionType: _ProjectionType? = null,

    /**
     * Codec
     * Specify "H.264/MPEG-4 AVC".
     *
     * RICOH THETA X is not supported.
     */
    val codec: String? = null,

    /**
     * Rotational shake correction and rotational shake perfect
     * correction are supported from RICOH THETA V firmware v1.20.1 or
     * later.
     *
     * RICOH THETA X is not supported.
     *
     * @see TopBottomCorrection
     */
    val topBottomCorrection: TopBottomCorrection? = null,
)

/**
 * Top Buttoncorrection value
 */
@Serializable
enum class TopBottomCorrection {
    /**
     * Top/bottom correction enabled and rotational shake correction
     * enabled.
     */
    @SerialName("Apply")
    APPLY,

    /**
     * Top/bottom correction enabled and rotational shake correction
     * enabled.
     *
     * Rotational shake correction and rotational shake perfect
     * correction are supported from RICOH THETA V firmware v1.20.1 or
     * later.
     *
     * RICOH THETA X is not supported.
     */
    @SerialName("ApplyFixedDirection")
    APPLY_FIXED_DIRECTION,

    /**
     * Top/bottom correction disabled.
     */
    @SerialName("Disapply")
    DISAPPLY,
}

/**
 * Convert video formats response
 */
@Serializable
data class ConvertVideoFormatsResponse(
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
    override val results: ResultConvertVideoFormats? = null,

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
 * Convert video formats results
 */
@Serializable
data class ResultConvertVideoFormats(
    /**
     * URL of a converted movie file
     */
    val fileUrl: String,
)

/**
 * video formats
 */
@Serializable
enum class VideoFormat {
    /**
     * 1920x960
     *
     * RICOH THETA Z1, V
     */
    @SerialName("1920x960")
    VIDEO_2K,

    /**
     * 3840x1920
     *
     * RICOH THETA Z1, V, X
     */
    @SerialName("3840x1920")
    VIDEO_4K,
}
