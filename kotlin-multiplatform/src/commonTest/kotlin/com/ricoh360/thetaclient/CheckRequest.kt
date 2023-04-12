package com.ricoh360.thetaclient

import com.ricoh360.thetaclient.transferred.*
import io.ktor.client.request.*
import io.ktor.http.content.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlin.test.assertEquals

@OptIn(ExperimentalSerializationApi::class)
class CheckRequest {
    companion object {
        fun checkCommandName(request: HttpRequestData, command: String) {
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "command request path")

            val body = request.body as TextContent
            val js = Json {
                encodeDefaults = true // Encode properties with default value.
                explicitNulls = false // Don't encode properties with null value.
                ignoreUnknownKeys = true // Ignore unknown keys on decode.
            }

            @Serializable
            data class CommandApiRequestAny(
                override val name: String,
                override val parameters: JsonObject
            ) : CommandApiRequest

            val requestData = js.decodeFromString<CommandApiRequestAny>(body.text)
            assertEquals(requestData.name, command, "command name")
        }

        fun checkSetOptions(
            request: HttpRequestData,
            captureMode: CaptureMode? = null,
            filter: ImageFilter? = null,
            fileFormat: MediaFileFormat? = null,
            maxRecordableTime: Int? = null,
            offDelay: Int? = null,
            sleepDelay: Int? = null,
            aperture: Float? = null,
            colorTemperature: Int? = null,
            exposureCompensation: Float? = null,
            exposureDelay: Int? = null,
            exposureProgram: Int? = null,
            gpsInfo: GpsInfo? = null,
            gpsTagRecording: GpsTagRecording? = null,
            iso: Int? = null,
            isoAutoHighLimit: Int? = null,
            language: Language? = null,
            whiteBalance: WhiteBalance? = null,
            clientVersion: Int? = null,
            shutterVolume: Int? = null,
            dateTimeZone: String? = null,
            bluetoothPower: BluetoothPower? = null,
            whiteBalanceAutoStrength: WhiteBalanceAutoStrength? = null
        ) {
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "command request path")

            val body = request.body as TextContent
            val js = Json {
                encodeDefaults = true // Encode properties with default value.
                explicitNulls = false // Don't encode properties with null value.
                ignoreUnknownKeys = true // Ignore unknown keys on decode.
            }
            val optionsRequest = js.decodeFromString<SetOptionsRequest>(body.text)

            captureMode?.let {
                assertEquals(optionsRequest.parameters.options.captureMode, it, "setOptions captureMode")
            }
            filter?.let {
                assertEquals(optionsRequest.parameters.options._filter, it, "setOptions _filter ${optionsRequest.parameters.options._filter} $it")
            }
            fileFormat?.let {
                assertEquals(optionsRequest.parameters.options.fileFormat, it, "setOptions fileFormat")
            }
            maxRecordableTime?.let {
                assertEquals(optionsRequest.parameters.options._maxRecordableTime, it, "setOptions maxRecordableTime")
            }
            offDelay?.let {
                assertEquals(optionsRequest.parameters.options.offDelay, it, "setOptions offDelay")
            }
            sleepDelay?.let {
                assertEquals(optionsRequest.parameters.options.sleepDelay, it, "setOptions sleepDelay")
            }
            aperture?.let {
                assertEquals(optionsRequest.parameters.options.aperture, it, "setOptions aperture")
            }
            colorTemperature?.let {
                assertEquals(optionsRequest.parameters.options._colorTemperature, it, "setOptions colorTemperature")
            }
            exposureCompensation?.let {
                assertEquals(optionsRequest.parameters.options.exposureCompensation, it, "setOptions exposureCompensation")
            }
            exposureDelay?.let {
                assertEquals(optionsRequest.parameters.options.exposureDelay, it, "setOptions exposureDelay")
            }
            exposureProgram?.let {
                assertEquals(optionsRequest.parameters.options.exposureProgram, it, "setOptions exposureProgram")
            }
            gpsInfo?.let {
                assertEquals(optionsRequest.parameters.options.gpsInfo, it, "setOptions gpsInfo")
            }
            gpsTagRecording?.let {
                assertEquals(optionsRequest.parameters.options._gpsTagRecording, it, "setOptions gpsTagRecording")
            }
            iso?.let {
                assertEquals(optionsRequest.parameters.options.iso, it, "setOptions iso")
            }
            isoAutoHighLimit?.let {
                assertEquals(optionsRequest.parameters.options.isoAutoHighLimit, it, "setOptions isoAutoHighLimit")
            }
            language?.let {
                assertEquals(optionsRequest.parameters.options._language, it, "setOptions language")
            }
            whiteBalance?.let {
                assertEquals(optionsRequest.parameters.options.whiteBalance, it, "setOptions whiteBalance")
            }
            clientVersion?.let {
                assertEquals(optionsRequest.parameters.options.clientVersion, it, "setOptions clientVersion")
            }
            shutterVolume?.let {
                assertEquals(optionsRequest.parameters.options._shutterVolume, it, "setOptions shutterVolume")
            }
            dateTimeZone?.let {
                assertEquals(optionsRequest.parameters.options.dateTimeZone, it, "setOptions dateTimeZone")
            }
            bluetoothPower?.let {
                assertEquals(optionsRequest.parameters.options._bluetoothPower, it, "setOptions bluetoothPower")
            }
            whiteBalanceAutoStrength?.let {
                assertEquals(optionsRequest.parameters.options._whiteBalanceAutoStrength, it, "setOptions whiteBalanceAutoStrength")
            }
        }

        fun checkGetOptions(request: HttpRequestData, optionNames: List<String>) {
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            val body = request.body as TextContent
            val js = Json {
                encodeDefaults = true // Encode properties with default value.
                explicitNulls = false // Don't encode properties with null value.
                ignoreUnknownKeys = true // Ignore unknown keys on decode.
            }
            val getOptionsRequest = js.decodeFromString<GetOptionsRequest>(body.text)

            // check
            assertEquals(getOptionsRequest.name, "camera.getOptions", "command name")
            assertEquals(getOptionsRequest.parameters.optionNames.sorted(), optionNames.sorted(), "optionNames")
        }
    }
}
