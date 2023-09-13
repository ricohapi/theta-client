/*
 * [camera._getMetadata](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._get_metadata.md)
 */
package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * get metadata request
 */
@Serializable
internal data class GetMetadataRequest(
    override val name: String = "camera._getMetadata",
    override val parameters: GetMetadataParams,
) : CommandApiRequest

/**
 * get metadata request parameters
 */
@Serializable
internal data class GetMetadataParams(
    /**
     * JPEG file ID
     */
    val fileUrl: String,
)

/**
 * get metadata response
 */
@Serializable
internal data class GetMetadataResponse(
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
    override val results: ResultGetMetadata? = null,

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
 * get metadata results
 */
@Serializable
internal data class ResultGetMetadata(
    /**
     * EXIF information in JSON format
     */
    val exif: ExifInfo,

    /**
     * Photo Sphere XMP information in JSON format
     */
    val xmp: XmpInfo,
)

/**
 * exif information (exif 2.3)
 * https://www.cipa.jp/std/documents/j/DC-008-2012_J.pdf
 */
@Serializable
internal data class ExifInfo(
    /**
     * EXIF Support version
     */
    val ExifVersion: String,

    /**
     * Title or name
     */
    val ImageDescription: String,

    /**
     * Time created or updated
     */
    val DateTime: String,

    /**
     * Width (px)
     * RICOH THETA X is not supported
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val ImageWidth: Int?,

    /**
     * Height (px)
     * RICOH THETA X is not supported
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val ImageLength: Int?,

    /**
     * Color space
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val ColorSpace: Int,

    /**
     * Compression format.
     *
     * RICOH THETA X is not supported
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val Compression: Int?,

    /**
     * Image orientation
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val Orientation: Int,

    /**
     * Flash exposure
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val Flash: Int,

    /**
     * Focal length
     */
    val FocalLength: Float,

    /**
     * White balance
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val WhiteBalance: Int,

    /**
     * Exposure time
     */
    val ExposureTime: Double,

    /**
     * F number
     */
    val FNumber: Double,

    /**
     * Exposure program
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val ExposureProgram: Int,

    /**
     * Shooting sensitivity
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val PhotographicSensitivity: Int,

    /**
     * Aperture value
     */
    val ApertureValue: Double,

    /**
     * Brightness value
     */
    val BrightnessValue: Float,

    /**
     * Exposure compensation value
     */
    val ExposureBiasValue: Float,

    /**
     * Latitudinal direction of movement
     */
    val GPSLatitudeRef: String?,

    /**
     * Latitude
     */
    val GPSLatitude: Double?,

    /**
     * Longitudinal direction of movement
     */
    val GPSLongitudeRef: String?,

    /**
     * Longitude
     */
    val GPSLongitude: Double?,

    /**
     * Camera manufacturer
     */
    val Make: String,

    /**
     * Camera model
     */
    val Model: String,

    /**
     * Firmware software name
     */
    val Software: String,

    /**
     * Copyright
     */
    val Copyright: String,
)

/**
 * XMP information
 */
@Serializable
internal data class XmpInfo(
    /**
     * Open Choice of Text Projection type. Google currently only
     * supports equirectangular.
     */
    val ProjectionType: ProjectionType,

    /**
     *  Whether to display using the panorama viewer
     */
    val UsePanoramaViewer: Boolean,

    /**
     * Real Compass heading, for the center the image.
     *
     * RICOH THETA X is not supported
     */
    val PoseHeadingDegrees: Double?,

    /**
     * Width of actual image (px)
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val CroppedAreaImageWidthPixels: Int,

    /**
     * Height of actual image (px)
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val CroppedAreaImageHeightPixels: Int,

    /**
     * Width (px) when the actual image size is based on a panoramic
     * image
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val FullPanoWidthPixels: Int,

    /**
     * Height (px) when the actual image size is based on a panoramic
     * image
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val FullPanoHeightPixels: Int,

    /**
     * Width (px) from the panoramic image of the actual image
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val CroppedAreaLeftPixels: Int,

    /**
     * Height (px) from the panoramic image of the actual image
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val CroppedAreaTopPixels: Int,
)

/**
 * projection type
 */
@Serializable
enum class ProjectionType {
    /**
     * equirectangular
     */
    @SerialName("equirectangular")
    EQUIRECTANGULAR,
}
