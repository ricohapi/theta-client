package com.ricoh360.thetaclient

import com.ricoh360.thetaclient.transferred.*
import io.ktor.client.request.*
import io.ktor.http.content.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlin.test.assertEquals

@OptIn(ExperimentalSerializationApi::class)
internal class CheckRequest {
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

        fun getCommandName(request: HttpRequestData): String? {
            if (request.url.encodedPath != "/osc/commands/execute") {
                return null
            }

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
            return requestData.name
        }

        fun checkSetOptions(
            request: HttpRequestData,
            aiAutoThumbnail: AiAutoThumbnail? = null,
            aperture: Float? = null,
            autoBracket: AutoBracket? = null,
            bitrate: String? = null,
            bluetoothPower: BluetoothPower? = null,
            bluetoothRole: BluetoothRole? = null,
            burstMode: BurstMode? = null,
            burstOption: BurstOption? = null,
            cameraControlSource: CameraControlSource? = null,
            cameraMode: CameraMode? = null,
            cameraPower: CameraPower? = null,
            captureInterval: Int? = null,
            captureMode: CaptureMode? = null,
            captureNumber: Int? = null,
            compositeShootingOutputInterval: Int? = null,
            compositeShootingTime: Int? = null,
            clientVersion: Int? = null,
            colorTemperature: Int? = null,
            dateTimeZone: String? = null,
            ethernetConfig: EthernetConfig? = null,
            exposureCompensation: Float? = null,
            exposureDelay: Int? = null,
            exposureProgram: Int? = null,
            faceDetect: FaceDetect? = null,
            filter: ImageFilter? = null,
            fileFormat: MediaFileFormat? = null,
            function: ShootingFunction? = null,
            gain: Gain? = null,
            gpsInfo: GpsInfo? = null,
            gpsTagRecording: GpsTagRecording? = null,
            imageStitching: ImageStitching? = null,
            iso: Int? = null,
            isoAutoHighLimit: Int? = null,
            language: Language? = null,
            maxRecordableTime: Int? = null,
            networkType: NetworkType? = null,
            offDelay: Int? = null,
            powerSaving: PowerSaving? = null,
            preset: Preset? = null,
            previewFormat: PreviewFormat? = null,
            proxy: Proxy? = null,
            shootingMethod: ShootingMethod? = null,
            shutterSpeed: Double? = null,
            shutterVolume: Int? = null,
            sleepDelay: Int? = null,
            timeShift: TimeShift? = null,
            topBottomCorrection: TopBottomCorrectionOption? = null,
            topBottomCorrectionRotation: TopBottomCorrectionRotation? = null,
            topBottomCorrectionRotationSupport: TopBottomCorrectionRotationSupport? = null,
            videoStitching: VideoStitching? = null,
            visibilityReduction: VisibilityReduction? = null,
            whiteBalance: WhiteBalance? = null,
            whiteBalanceAutoStrength: WhiteBalanceAutoStrength? = null,
            wlanFrequency: WlanFrequency? = null,
        ) {
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "command request path")

            val body = request.body as TextContent
            val js = Json {
                encodeDefaults = true // Encode properties with default value.
                explicitNulls = false // Don't encode properties with null value.
                ignoreUnknownKeys = true // Ignore unknown keys on decode.
            }
            val optionsRequest = js.decodeFromString<SetOptionsRequest>(body.text)

            aiAutoThumbnail?.let {
                assertEquals(optionsRequest.parameters.options._aiAutoThumbnail, it, "setOptions _aiAutoThumbnail")
            }
            aperture?.let {
                assertEquals(optionsRequest.parameters.options.aperture, it, "setOptions aperture")
            }
            autoBracket?.let {
                assertEquals(optionsRequest.parameters.options._autoBracket, it, "setOptions autoBracket")
            }
            bitrate?.let {
                assertEquals(optionsRequest.parameters.options._bitrate, it, "setOptions bitrate")
            }
            bluetoothPower?.let {
                assertEquals(optionsRequest.parameters.options._bluetoothPower, it, "setOptions bluetoothPower")
            }
            bluetoothRole?.let {
                assertEquals(optionsRequest.parameters.options._bluetoothRole, it, "setOptions bluetoothRole")
            }
            burstMode?.let {
                assertEquals(optionsRequest.parameters.options._burstMode, it, "setOptions burstMode")
            }
            burstOption?.let {
                assertEquals(optionsRequest.parameters.options._burstOption, it, "setOptions burstOption")
            }
            cameraControlSource?.let {
                assertEquals(optionsRequest.parameters.options._cameraControlSource, it, "setOptions cameraControlSource")
            }
            cameraMode?.let {
                assertEquals(optionsRequest.parameters.options._cameraMode, it, "setOptions cameraMode")
            }
            cameraPower?.let {
                assertEquals(optionsRequest.parameters.options._cameraPower, it, "setOptions cameraPower")
            }
            captureInterval?.let {
                assertEquals(optionsRequest.parameters.options.captureInterval, it, "setOptions captureInterval")
            }
            captureMode?.let {
                assertEquals(optionsRequest.parameters.options.captureMode, it, "setOptions captureMode")
            }
            captureNumber?.let {
                assertEquals(optionsRequest.parameters.options.captureNumber, it, "setOptions captureNumber")
            }
            compositeShootingOutputInterval?.let {
                assertEquals(optionsRequest.parameters.options._compositeShootingOutputInterval, it, "setOptions compositeShootingOutputInterval")
            }
            compositeShootingTime?.let {
                assertEquals(optionsRequest.parameters.options._compositeShootingTime, it, "setOptions compositeShootingTime")
            }
            clientVersion?.let {
                assertEquals(optionsRequest.parameters.options.clientVersion, it, "setOptions clientVersion")
            }
            colorTemperature?.let {
                assertEquals(optionsRequest.parameters.options._colorTemperature, it, "setOptions colorTemperature")
            }
            dateTimeZone?.let {
                assertEquals(optionsRequest.parameters.options.dateTimeZone, it, "setOptions dateTimeZone")
            }
            ethernetConfig?.let {
                assertEquals(optionsRequest.parameters.options._ethernetConfig, it, "setOptions ethernetConfig")
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
            faceDetect?.let {
                assertEquals(optionsRequest.parameters.options._faceDetect, it, "setOptions _faceDetect")
            }
            filter?.let {
                assertEquals(optionsRequest.parameters.options._filter, it, "setOptions _filter ${optionsRequest.parameters.options._filter} $it")
            }
            fileFormat?.let {
                assertEquals(optionsRequest.parameters.options.fileFormat, it, "setOptions fileFormat")
            }
            function?.let {
                assertEquals(optionsRequest.parameters.options._function, it, "setOptions _function")
            }
            gain?.let {
                assertEquals(optionsRequest.parameters.options._gain, it, "setOptions _gain")
            }
            gpsInfo?.let {
                assertEquals(optionsRequest.parameters.options.gpsInfo, it, "setOptions gpsInfo")
            }
            gpsTagRecording?.let {
                assertEquals(optionsRequest.parameters.options._gpsTagRecording, it, "setOptions gpsTagRecording")
            }
            imageStitching?.let {
                assertEquals(optionsRequest.parameters.options._imageStitching, it, "setOptions imageStitching")
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
            maxRecordableTime?.let {
                assertEquals(optionsRequest.parameters.options._maxRecordableTime, it, "setOptions maxRecordableTime")
            }
            networkType?.let {
                assertEquals(optionsRequest.parameters.options._networkType, it, "setOptions networkType")
            }
            offDelay?.let {
                assertEquals(optionsRequest.parameters.options.offDelay, it, "setOptions offDelay")
            }
            powerSaving?.let {
                assertEquals(optionsRequest.parameters.options._powerSaving, it, "setOptions powerSaving")
            }
            preset?.let {
                assertEquals(optionsRequest.parameters.options._preset, it, "setOptions preset")
            }
            previewFormat?.let {
                assertEquals(optionsRequest.parameters.options.previewFormat, it, "setOptions previewFormat")
            }
            proxy?.let {
                assertEquals(optionsRequest.parameters.options._proxy, it, "setOptions proxy")
            }
            shootingMethod?.let {
                assertEquals(optionsRequest.parameters.options._shootingMethod, it, "setOptions shootingMethod")
            }
            shutterSpeed?.let {
                assertEquals(optionsRequest.parameters.options.shutterSpeed, it, "setOptions shutterSpeed")
            }
            shutterVolume?.let {
                assertEquals(optionsRequest.parameters.options._shutterVolume, it, "setOptions shutterVolume")
            }
            sleepDelay?.let {
                assertEquals(optionsRequest.parameters.options.sleepDelay, it, "setOptions sleepDelay")
            }
            timeShift?.let {
                assertEquals(optionsRequest.parameters.options._timeShift, it, "setOptions timeShift")
            }
            topBottomCorrection?.let {
                assertEquals(optionsRequest.parameters.options._topBottomCorrection, it, "setOptions topBottomCorrection")
            }
            topBottomCorrectionRotation?.let {
                assertEquals(optionsRequest.parameters.options._topBottomCorrectionRotation, it, "setOptions topBottomCorrectionRotation")
            }
            topBottomCorrectionRotationSupport?.let {
                assertEquals(optionsRequest.parameters.options._topBottomCorrectionRotationSupport, it, "setOptions topBottomCorrectionRotationSupport")
            }
            videoStitching?.let {
                assertEquals(optionsRequest.parameters.options.videoStitching, it, "setOptions videoStitching")
            }
            visibilityReduction?.let {
                assertEquals(optionsRequest.parameters.options._visibilityReduction, it, "setOptions visibilityReduction")
            }
            whiteBalance?.let {
                assertEquals(optionsRequest.parameters.options.whiteBalance, it, "setOptions whiteBalance")
            }
            whiteBalanceAutoStrength?.let {
                assertEquals(optionsRequest.parameters.options._whiteBalanceAutoStrength, it, "setOptions whiteBalanceAutoStrength")
            }
            wlanFrequency?.let {
                assertEquals(optionsRequest.parameters.options._wlanFrequency, it, "setOptions wlanFrequency")
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
