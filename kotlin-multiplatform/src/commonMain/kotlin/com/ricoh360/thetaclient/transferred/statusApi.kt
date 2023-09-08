/*
 * [osc/commands/status](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/protocols/commands_status.md)
 */
package com.ricoh360.thetaclient.transferred

import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Status api request
 */
internal object StatusApi {
    const val PATH = "/osc/commands/status"
    val METHOD = HttpMethod.Post
}

/**
 * Status API request information class
 */
@Serializable
internal data class StatusApiRequest(
    /**
     * request related command name
     */
    val name: String? = null,

    /**
     * request related id
     */
    val id: String? = null,
)

/**
 * Status API request parameters class
 */
internal data class StatusApiParams(
    /**
     * status of this command
     */
    val name: String? = null,

    /**
     * status of this id
     */
    val id: String? = null,
)

/**
 * decode status api response depends on {body}.name
 * and return decoded object
 */
@kotlinx.serialization.ExperimentalSerializationApi
internal fun decodeStatusApiResponse(body: String?): CommandApiResponse {
    if (body == null) {
        return UnknownResponse("unknown")
    }
    var js = Json {
        encodeDefaults = true    // Encode properties with default value.
        explicitNulls = false    // Don't encode properties with null value.
        ignoreUnknownKeys = true // Ignore unknown keys on decode.
    }
    var json: JsonObject? = null
    try {
        json = js.decodeFromString<JsonObject>(body)
    } catch (t: Throwable) {
    }
    if (json != null && json.containsKey("name")) {
        when (val name: String = json["name"]!!.jsonPrimitive.content) {
            "camera._cancelVideoConvert" -> {
                return js.decodeFromString<CancelVideoConvertResponse>(body)
            }
            "camera._convertVideoFormats" -> {
                return js.decodeFromString<ConvertVideoFormatsResponse>(body)
            }
            "camera.delete" -> {
                return js.decodeFromString<DeleteResponse>(body)
            }
            "camera._finishWlan" -> {
                return js.decodeFromString<FinishWlanResponse>(body)
            }
            "camera._getMetadata" -> {
                return js.decodeFromString<GetMetadataResponse>(body)
            }
            "camera.listFiles" -> {
                return js.decodeFromString<ListFilesResponse>(body)
            }
            "camera.reset" -> {
                return js.decodeFromString<ResetResponse>(body)
            }
            "camera._listAccessPoints" -> {
                return js.decodeFromString<ListAccessPointsResponse>(body)
            }
            "camera.startCapture" -> {
                return js.decodeFromString<StartCaptureResponse>(body)
            }
            "camera.stopCapture" -> {
                return js.decodeFromString<StopCaptureResponse>(body)
            }
            "camera.takePicture" -> {
                return js.decodeFromString<TakePictureResponse>(body)
            }
            "camera._deleteAccessPoint" -> {
                return js.decodeFromString<DeleteAccessPointResponse>(body)
            }
            "camera._setAccessPoint" -> {
                return js.decodeFromString<SetAccessPointResponse>(body)
            }
            "camera._stopSelfTimer" -> {
                return js.decodeFromString<StopSelfTimerResponse>(body)
            }
            "camera.setOptions" -> {
                return js.decodeFromString<SetOptionsResponse>(body)
            }
            "camera.getOptions" -> {
                return js.decodeFromString<GetOptionsResponse>(body)
            }
            "camera.startSession" -> {
                return js.decodeFromString<StartSessionResponse>(body)
            }
            else -> {
                return UnknownResponse(name = name)
            }
        }
    } else {
        return UnknownResponse(name = "unknown")
    }
}

