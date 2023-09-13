/*
 * [camera.listFiles](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.list_files.md)
 */
package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * list files request
 */
@Serializable
internal data class ListFilesRequest(
    override val name: String = "camera.listFiles",
    override val parameters: ListFilesParams,
) : CommandApiRequest

/**
 * list files request parameters
 */
@Serializable
internal data class ListFilesParams(
    /**
     * File type to acquire.
     * FileType.ALL: All types
     * FileType.IMAGE: Still image
     * FileType.VIDEO: movie
     */
    val fileType: FileType = FileType.ALL,

    /**
     * Position to start acquiring the file list.
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val startPosition: Int? = null,

    /**
     * First file to return to the list.
     */
    val _startFileUrl: String? = null,

    /**
     * Number of files to acquire.  If the number of existing files is
     * smaller than the specified number of files, all available files
     * are only acquired.
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val entryCount: Int,

    /**
     * "640": Acquire thumbnail, "0": Do not acquire thumbnail
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val maxThumbSize: Int = 0,

    /**
     * Specifies whether or not to acquire the file details.
     */
    val _detail: Boolean? = true,

    /**
     * Specifies the sort order: “newest” (default) or “oldest”
     */
    val _sort: SortOrder? = null,

    /**
     * Specifies the storage.
     *
     * "IN" : internal storage
     * "SD" : external storage (SD card)
     * "Default" : current storage
     * Default is "Default".
     * (RICOH THETA X Version 2.00.0 or later)
     */
    val _storage: Storage? = null,
)

/**
 * list files response
 */
@Serializable
internal data class ListFilesResponse(
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
    override val results: ResultListFiles? = null,

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
 * list files request results
 */
@Serializable
internal data class ResultListFiles(
    /**
     * The list of still image files and movie files acquired.
     */
    val entries: List<CameraFileInfo>,

    /**
     * Number of still image files and movie files stored in the
     * camera
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val totalEntries: Int,
)

/**
 * camera file information
 */
@Serializable
internal data class CameraFileInfo(
    /**
     * File name
     */
    val name: String,

    /**
     * File URL
     */
    val fileUrl: String,

    /**
     * File size in bytes
     */
    @Serializable(with = NumberAsLongSerializer::class)
    val size: Long,

    /**
     * File creation or update time with the time zone
     */
    val dateTimeZone: String?,

    /**
     * File creation or update time in local time.
     */
    val dateTime: String?,

    /**
     * Latitude
     */
    val lat: Float?,

    /**
     * Longitude
     */
    val lng: Float?,

    /**
     * Horizontal size of image (pixels).
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val width: Int?,

    /**
     * Vertical size of image (pixels).
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val height: Int?,

    /**
     * Base64-encoded thumbnail
     */
    val thumbnail: String?,

    /**
     * Thumbnail file size (bytes).
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val _thumbSize: Int?,

    /**
     * Group ID of a still image shot by interval shooting.
     */
    val _intervalCaptureGroupId: String?,

    /**
     * Group ID of a still image shot by interval composite shooting.
     */
    val _compositeShootingGroupId: String?,

    /**
     * Group ID of a still image shot by multi bracket shooting.
     */
    val _autoBracketGroupId: String?,

    /**
     * Video shooting time (sec).
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val _recordTime: Int?,

    /**
     * Whether or not image processing has been completed
     */
    val isProcessed: Boolean,

    /**
     * URL of the file being processed.
     */
    val previewUrl: String,

    /**
     * Codec. (RICOH THETA V or later)
     */
    val _codec: String?,

    /**
     * Projection type of movie file. (RICOH THETA V or later)
     */
    val _projectionType: _ProjectionType?,

    /**
     * Group ID of continuous shooting.  (RICOH THETA X or later)
     */
    val _continuousShootingGroupId: String?,

    /**
     * Frame rate.  (RICOH THETA X or later)
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val _frameRate: Int?,

    /**
     * Favorite.  (RICOH THETA X or later)
     */
    val _favorite: Boolean?,

    /**
     * Image description.  (RICOH THETA X or later)
     */
    val _imageDescription: String?,

    /**
     * Storage ID.
     *
     * Can be acquired when "_detail" is "true".
     * (RICOH THETA X Version 2.00.0 or later)
     */
    val _storageID: String?,
) {
    companion object {
        const val THUMBNAIL_QUERY = "?type=thumb"
    }

    fun getThumbnailUrl(): String {
        return fileUrl + THUMBNAIL_QUERY
    }
}

/**
 * theta projection type
 */
@Serializable
enum class _ProjectionType {
    /**
     * Equirectangular type
     */
    @SerialName("Equirectangular")
    EQUIRECTANGULAR,

    /**
     * Dual Fisheye type
     */
    @SerialName("Dual-Fisheye")
    DUAL_FISHEYE,

    /**
     * Fisheye type
     */
    @SerialName("Fisheye")
    FISHEYE,
}

/**
 * list file type
 */
@Serializable
enum class FileType {
    /**
     * All types
     */
    @SerialName("all")
    ALL,

    /**
     * Still image
     */
    @SerialName("image")
    IMAGE,

    /**
     * Movie
     */
    @SerialName("video")
    VIDEO,
}

/**
 * list files to sort order
 */
@Serializable
enum class SortOrder {
    /**
     * newest order (default)
     */
    @SerialName("newest")
    NEWEST,

    /**
     * oldest order
     */
    @SerialName("oldest")
    OLDEST,
}

/**
 * Specifies the storage
 */
@Serializable
enum class Storage {
    /**
     * internal storage
     */
    @SerialName("IN")
    IN,

    /**
     * external storage (SD card)
     */
    @SerialName("SD")
    SD,

    /**
     * current storage
     */
    @SerialName("Default")
    DEFAULT,
}
