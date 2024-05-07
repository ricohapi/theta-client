/*
 * [camera.setOptions](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.set_options.md)
 */
package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * set options request
 */
@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
internal data class SetOptionsRequest(
    override val name: String = "camera.setOptions",
    override val parameters: SetOptionsParams,
) : CommandApiRequest

/**
 * set options request parameters
 */
@Serializable
internal data class SetOptionsParams(
    /**
     * session id
     */
    val sessionId: String? = null,

    /**
     * Set of option names and setting values to be set in JSON format
     */
    val options: Options,
) {
    constructor(options: Options) : this(null, options)
}

/**
 * set options response
 */
@Serializable
internal data class SetOptionsResponse(
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
    override val results: ResultSetOptions? = null,

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
 * set options results
 */
@Serializable
internal data class ResultSetOptions(
    /**
     * option key value pair
     */
    val options: Options,
)

/**
 * option key value pair
 */
@Serializable
@Suppress("ConstructorParameterNaming")
internal data class Options(
    /**
     * Turns the AI auto setting ON/OFF.
     */
    var _aiAutoThumbnail: AiAutoThumbnail? = null,

    /**
     * Supported turns the AI auto setting
     */
    var _aiAutoThumbnailSupport: List<AiAutoThumbnail>? = null,

    /**
     * Aperture value.
     */
    var aperture: Float? = null,

    /**
     * Supported aperture value.
     */
    var apertureSupport: List<Float>? = null,

    /**
     * Multi bracket shooting setting
     */
    var _autoBracket: AutoBracket? = null,

    /**
     * Supported AutoBracket.
     */
    var _autoBracketSupport: AutoBracket? = null,

    /**
     * Movie bit rate.
     */
    var _bitrate: String? = null,

    /**
     * Movie bit rate support.
     */
    var _bitrateSupport: List<String>? = null,

    /**
     * bluetooth power
     *
     * @see BluetoothPower
     */
    var _bluetoothPower: BluetoothPower? = null,

    /**
     * Role of the Bluetooth module.
     *
     * @see BluetoothRole
     */
    var _bluetoothRole: BluetoothRole? = null,

    /**
     * burst mode
     *
     * @see BurstMode
     */
    var _burstMode: BurstMode? = null,

    /**
     * burst option
     *
     * @see BurstOption
     */
    var _burstOption: BurstOption? = null,

    /**
     * camera control source
     *
     * @see CameraControlSource
     */
    var _cameraControlSource: CameraControlSource? = null,

    /**
     * camera control source support
     *
     * @see CameraControlSource
     */
    var _cameraControlSourceSupport: List<CameraControlSource>? = null,

    /**
     * Camera mode.
     *
     * @see CameraMode
     */
    var _cameraMode: CameraMode? = null,

    /**
     * Shooting interval (sec.) for interval shooting.
     */
    var captureInterval: Int? = null,

    /**
     * supported capture interval.
     */
    var captureIntervalSupport: CaptureIntervalSupport? = null,

    /**
     * Shooting mode.
     *
     * The current setting can be acquired by camera.getOptions, and
     * it can be changed by camera.setOptions.
     *
     * Switching modes may take time. Wait a while to send the command
     * that takes place after switching the mode.
     *
     * Live streaming mode is supported by only RICOH THETA S.
     */
    var captureMode: CaptureMode? = null,

    /**
     * supported shooting mode.
     */
    var captureModeSupport: List<CaptureMode>? = null,

    /**
     * Number of shots for interval shooting.
     */
    var captureNumber: Int? = null,

    /**
     * supported capture number.
     */
    var captureNumberSupport: CaptureNumberSupport? = null,

    /**
     * API version of the camera. (RICOH THETA S firmware version
     * 01.62 or later) Once the connection with the camera is
     * disconnected, this property returns to the default 1.
     */
    @Serializable(with = NumberAsIntSerializer::class)
    var clientVersion: Int? = null,

    /**
     * supported client versions.
     */
    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    @Serializable(with = NumbersAsIntsSerializer::class)
    var clientVersionSupport: List<Int>? = null,

    /**
     * Color temperature of the camera (Kelvin).
     * It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
     * Shooting settings are retained separately for both the Still image shooting mode
     * and Video shooting mode.
     *
     * 2500 to 10000. In 100-Kelvin units.
     */
    var _colorTemperature: Int? = null,

    /**
     * In-progress save interval for interval composite shooting (sec).
     *
     * 0 (no saving), 60 to 600. In 60-second units.
     *
     * For
     * RICOH THETA Z1
     * RICOH THETA SC firmware v1.10 or later
     * RICOH THETA S firmware v01.82 or later
     */
    var _compositeShootingOutputInterval: Int? = null,

    /**
     * Supported in-progress save interval for interval composite shooting (sec).
     */
    var _compositeShootingOutputIntervalSupport: List<Int>? = null,

    /**
     * Shooting time for interval composite shooting (sec).
     *
     * 600 to 86400. In 600-second units.
     *
     * For
     * RICOH THETA Z1
     * RICOH THETA SC firmware v1.10 or later
     * RICOH THETA S firmware v01.82 or later
     */
    var _compositeShootingTime: Int? = null,

    /**
     * Supported shooting time for interval composite shooting (sec
     */
    var _compositeShootingTimeSupport: List<Int>? = null,

    /**
     * Number of shots for continuous shooting.
     * It can be acquired by camera.getOptions.
     *
     * For RICOH THETA X
     * - 11k image: Maximum value 8
     * - 5.5k image: Maximum value 20
     *
     * Depending on available storage capacity, the value may be less than maximum.
     */
    var continuousNumber: Int? = null,

    /**
     * Current system time of RICOH THETA. Setting another options
     * will result in an error.
     *
     * The current setting can be acquired by camera.getOptions, and
     * it can be changed by camera.setOptions.
     *
     * With RICOH THETA X camera.setOptions can be changed only when
     * Date/time setting is AUTO in menu UI.
     *
     * time format
     * YYYY:MM:DD hh:mm:ss+(-)hh:mm
     * hh is in 24-hour time, +(-)hh:mm is the time zone.
     *
     * e.g. 2014:05:18 01:04:29+08:00
     */
    var dateTimeZone: String? = null,

    /**
     * @see EthernetConfig
     */
    var _ethernetConfig: EthernetConfig? = null,

    /**
     * Exposure compensation (EV).
     *
     * It can be set for video shooting mode at RICOH THETA V firmware
     * v3.00.1 or later. Shooting settings are retained separately for
     * both the Still image shooting mode and Video shooting mode.
     */
    var exposureCompensation: Float? = null,

    /**
     * Supported exposure compensation (EV).
     */
    var exposureCompensationSupport: List<Float>? = null,

    /**
     * Operating time (sec.) of the self-timer.
     * If exposureDelay is enabled, self-timer is used by shooting.
     * If exposureDelay is disabled, use
     * _latestEnabledExposureDelayTime to get the operating time of
     * the self-timer stored in the camera.
     */
    @Serializable(with = NumberAsIntSerializer::class)
    var exposureDelay: Int? = null,

    /**
     * Supported operating time (sec.) of the self-timer.
     */
    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    @Serializable(with = NumbersAsIntsSerializer::class)
    var exposureDelaySupport: List<Int>? = null,

    /**
     * Exposure program
     *
     * Exposure program. The exposure settings that take priority can
     * be selected.  It can be set for video shooting mode at RICOH
     * THETA V firmware v3.00.1 or later. Shooting settings are
     * retained separately for both the Still image shooting mode and
     * Video shooting mode.
     */
    @Serializable(with = NumberAsIntSerializer::class)
    var exposureProgram: Int? = null,

    /**
     * Supported exposure program
     */
    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    @Serializable(with = NumbersAsIntsSerializer::class)
    var exposureProgramSupport: List<Int>? = null,

    /**
     * Turns face detection ON/OFF.
     *
     * @see FaceDetect
     */
    var _faceDetect: FaceDetect? = null,

    /**
     * supported turns face detection ON/OFF.
     *
     * @see FaceDetect
     */
    var _faceDetectSupport: List<FaceDetect>? = null,

    /**
     * Image format used in shooting.
     *
     * @see MediaFileFormat
     */
    var fileFormat: MediaFileFormat? = null,

    /**
     * supported image format used in shooting.
     */
    var fileFormatSupport: List<MediaFileFormat>? = null,

    /**
     * Image processing filter.
     *
     * Configured the filter will be applied while in still image
     * shooting mode. However, it is disabled during interval shooting,
     * interval composite group shooting, multi bracket shooting or
     * continuous shooting.
     *
     * When _filter is enabled, it takes priority over the exposure
     * program (exposureProgram). Also, when _filter is enabled, the
     * exposure program is set to the Normal program.
     *
     * The condition below will result in an error.
     *
     * fileFormat is raw+ and _filter is Noise reduction, HDR or
     * Handheld HDR _shootingMethod is except for Normal shooting and
     * _filter is enabled Access during video capture mode
     *
     * @see ImageFilter
     */
    var _filter: ImageFilter? = null,

    /**
     * Supported image processing filter.
     */
    var _filterSupport: List<ImageFilter>? = null,

    /**
     * Shooting function.
     *
     * @see Function
     */
    var _function: ShootingFunction? = null,

    /**
     * Supported shooting function.
     */
    var _functionSupport: List<ShootingFunction>? = null,

    /**
     * Microphone gain.
     *
     * @see Gain
     */
    var _gain: Gain? = null,

    /**
     * Supported microphone gain.
     */
    var _gainSupport: List<Gain>? = null,

    /**
     * GPS location information.
     *
     * In order to append the location information, this property
     * should be specified by the client.
     *
     * @see GpsInfo
     */
    var gpsInfo: GpsInfo? = null,

    /**
     * Turns position information assigning ON/OFF.
     *
     * _gpsTagRecording is supported by only RICOH THETA X.
     *
     * @see GpsTagRecording
     */
    var _gpsTagRecording: GpsTagRecording? = null,

    /**
     * Still image stitching setting during shooting.
     */
    var _imageStitching: ImageStitching? = null,

    /**
     * Supported still image stitching setting
     */
    var _imageStitchingSupport: List<ImageStitching>? = null,

    /**
     * ISO sensitivity.
     *
     * It can be set for video shooting mode at RICOH THETA V firmware
     * v3.00.1 or later. Shooting settings are retained separately for
     * both the Still image shooting mode and Video shooting mode.
     */
    @Serializable(with = NumberAsIntSerializer::class)
    var iso: Int? = null,

    /**
     * Supported ISO sensitivity.
     */
    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    @Serializable(with = NumbersAsIntsSerializer::class)
    var isoSupport: List<Int>? = null,

    /**
     * ISO sensitivity upper limit when ISO sensitivity is set to
     * automatic.
     */
    @Serializable(with = NumberAsIntSerializer::class)
    var isoAutoHighLimit: Int? = null,

    /**
     * supported ISO sensitivity upper limit.
     */
    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    @Serializable(with = NumbersAsIntsSerializer::class)
    var isoAutoHighLimitSupport: List<Int>? = null,

    /**
     * Language used in camera OS.
     *
     * @see Language
     */
    var _language: Language? = null,

    /**
     * Supported language used in camera OS.
     */
    var _languageSupport: List<Language>? = null,

    /**
     * Self-timer operating time (sec.) when the self-timer (exposureDelay) was effective.
     */
    @Serializable(with = NumberAsIntSerializer::class)
    var _latestEnabledExposureDelayTime: Int? = null,

    /**
     * Maximum recordable time (in seconds) of the camera.
     */
    @Serializable(with = NumberAsIntSerializer::class)
    var _maxRecordableTime: Int? = null,

    /**
     * Supported maximum recordable time (in seconds) of the camera.
     */
    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    @Serializable(with = NumbersAsIntsSerializer::class)
    var _maxRecordableTimeSupport: List<Int>? = null,

    /**
     * Microphone to be used.
     */
    var _microphone: MicrophoneOption? = null,

    /**
     * Supported microphone to be used.
     */
    var _microphoneSupport: List<MicrophoneOption>? = null,

    /**
     * Microphone channel
     */
    var _microphoneChannel: MicrophoneChannel? = null,

    /**
     * Supported microphone channel
     */
    var _microphoneChannelSupport: List<MicrophoneChannel>? = null,

    /**
     * Network type.
     */
    var _networkType: NetworkType? = null,

    /**
     * Supported network type.
     */
    var _networkTypeSupport: List<NetworkType>? = null,

    /**
     * Length of standby time before the camera automatically powers OFF.
     *
     * For RICOH THETA V or later
     *
     * 0, or a value that is a multiple of 60 out of 600 or more and
     * 2592000 or less (unit: second), or 65535.  Return 0 when 65535
     * is set and obtained (Do not turn power OFF).
     *
     * For RICOH THETA S or SC
     *
     * 30 or more and 1800 or less (unit: seconds), 65535 (Do not turn
     * power OFF).
     */
    @Serializable(with = NumberAsIntSerializer::class)
    var offDelay: Int? = null,

    /**
     * Length of standby time before the camera automatically powers OFF.
     */
    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    @Serializable(with = NumbersAsIntsSerializer::class)
    var offDelaySupport: List<Int>? = null,

    /**
     * Password used for digest authentication when _networkType is set to client mode.
     */
    var _password: String? = null,

    /**
     * Power saving mode
     */
    var _powerSaving: PowerSaving? = null,

    /**
     * Supported PowerSaving.
     */
    var _powerSavingSupport: List<PowerSaving>? = null,

    /**
     * preview format
     */
    var previewFormat: PreviewFormat? = null,

    /**
     * Supported preview format
     */
    var previewFormatSupport: List<PreviewFormat>? = null,

    /**
     * Preset mode for Theta SC2
     */
    var _preset: Preset? = null,

    /**
     * Supported Preset.
     */
    var _presetSupport: List<Preset>? = null,

    /**
     * @see Proxy
     */
    var _proxy: Proxy? = null,

    /**
     * The estimated remaining number of shots for the current
     * shooting settings.
     */
    @Serializable(with = NumberAsIntSerializer::class)
    var remainingPictures: Int? = null,

    /**
     * Remaining usable storage space (byte).
     */
    @Serializable(with = NumberAsLongSerializer::class)
    var remainingSpace: Long? = null,

    /**
     *
     */
    @Serializable(with = NumberAsIntSerializer::class)
    var remainingVideoSeconds: Int? = null,

    /**
     * Shooting method for My Settings mode. In RICOH THETA X, it is
     * used outside of MySetting. Can be acquired and set only when in
     * the Still image shooting mode and _function is the My Settings
     * shooting function.  Changing _function initializes the setting
     * details to Normal shooting.
     */
    var _shootingMethod: ShootingMethod? = null,

    /**
     * Supported shooting method
     */
    var _shootingMethodSupport: List<ShootingMethod>? = null,

    /**
     * Shutter speed (sec).
     *
     * It can be set for video shooting mode at RICOH THETA V firmware
     * v3.00.1 or later. Shooting settings are retained separately for
     * both the Still image shooting mode and Video shooting mode.
     */
    var shutterSpeed: Double? = null,

    /**
     * Supported shutter speed.
     */
    var shutterSpeedSupport: List<Double>? = null,

    /**
     * Shutter volume. (0-100)
     */
    @Serializable(with = NumberAsIntSerializer::class)
    var _shutterVolume: Int? = null,

    /**
     * Supported shutter volume. (0-100)
     */
    var _shutterVolumeSupport: ShutterVolumeSupport? = null,

    /**
     * Length of standby time before the camera enters the sleep mode.
     *
     * For RICOH THETA V or later
     *
     * 60 to 65534, or 65535 (to disable the sleep mode).
     *
     * If a value from 0 to 59 is specified, and error
     * (invalidParameterValue) is returned.
     *
     * For RICOH THETA S or SC
     *
     * 30 to 1800, or 65535 (to disable the sleep mode)
     */
    @Serializable(with = NumberAsIntSerializer::class)
    var sleepDelay: Int? = null,

    /**
     * Length of standby time before the camera enters the sleep mode.
     */
    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    @Serializable(with = NumbersAsIntsSerializer::class)
    var sleepDelaySupport: List<Int>? = null,

    /**
     * Time shift shooting.
     */
    var _timeShift: TimeShift? = null,

    /**
     * Supported TimeShift.
     */
    var _timeShiftSupport: List<TimeShift>? = null,

    /**
     * top bottom correction
     *
     * @see TopBottomCorrectionOption
     */
    var _topBottomCorrection: TopBottomCorrectionOption? = null,

    /**
     * Supported top bottom correction
     */
    var _topBottomCorrectionSupport: List<TopBottomCorrectionOption>? = null,

    /**
     * @see TopBottomCorrectionRotation
     */
    var _topBottomCorrectionRotation: TopBottomCorrectionRotation? = null,

    /**
     * Total storage space (byte).
     */
    @Serializable(with = NumberAsLongSerializer::class)
    var totalSpace: Long? = null,

    /**
     * User name used for digest authentication when _networkType is set to client mode.
     */
    var _username: String? = null,

    /**
     * Video stitching during shooting.
     */
    var videoStitching: VideoStitching? = null,

    /**
     * Supported video stitching during shooting
     */
    var videoStitchingSupport: List<VideoStitching>? = null,

    /**
     * Reduction visibility of camera body to still image when stitching.
     */
    var _visibilityReduction: VisibilityReduction? = null,

    /**
     * Supported reduction visibility of camera body to still image when stitching.
     */
    var _visibilityReductionSupport: List<VisibilityReduction>? = null,

    /**
     * white balance
     */
    var whiteBalance: WhiteBalance? = null,

    /**
     * Supported white balance
     */
    var whiteBalanceSupport: List<WhiteBalance>? = null,

    /**
     * White Balance Auto Strength
     *
     * @see WhiteBalanceAutoStrength
     */
    var _whiteBalanceAutoStrength: WhiteBalanceAutoStrength? = null,

    /**
     * Supported WhiteBalanceAutoStrength
     */
    var _whiteBalanceAutoStrengthSupport: List<WhiteBalanceAutoStrength>? = null,

    /**
     * Wireless LAN frequency of the camera supported by Theta V, Z1 and X.
     */
    var _wlanFrequency: WlanFrequency? = null,

    /**
     * Supported WlanFrequency
     */
    var _wlanFrequencySupport: List<WlanFrequency>? = null,
)

/**
 * Multi bracket shooting setting
 *
 * [_bracketNumber] is the only supported value that can be acquired by camera.getOptions.
 * For [_bracketParameters], all parameters must be specified.
 */
@Serializable
internal data class AutoBracket(
    /**
     * Number of shots in multi bracket shooting.
     * 2 to 13 (THETA X and SC2);
     * 2 to 19 (THETA Z1 and V).
     */
    val _bracketNumber: Int,

    /**
     * Parameter array specified for multi bracket shooting.
     */
    val _bracketParameters: List<BracketParameter>,
)

/**
 * Parameter array specified for multi bracket shooting
 */
@Serializable
internal data class BracketParameter(
    /**
     * Aperture value.
     * Theta X and SC2 do not support.
     */
    var aperture: Float? = null,

    /**
     * Color temperature of the camera (Kelvin).
     * 2500 to 10000. In 100-Kelvin units.
     */
    @Serializable(with = NumberAsIntSerializer::class)
    var _colorTemperature: Int? = null,

    /**
     * Exposure compensation (EV).
     * Theta SC2 does not support.
     */
    var exposureCompensation: Float? = null,

    /**
     * Exposure program.
     * 1: Manual program, 2: Normal program, 3: Aperture priority program,
     * 4: Shutter priority program, 9: ISO priority program.
     *
     * Mandatory to Theta Z1 and V.
     * Theta SC2 does not support.
     */
    @Serializable(with = NumberAsIntSerializer::class)
    var exposureProgram: Int? = null,

    /**
     * ISO sensitivity.
     */
    @Serializable(with = NumberAsIntSerializer::class)
    var iso: Int? = null,

    /**
     * Shutter speed (sec).
     */
    var shutterSpeed: Double? = null,

    /**
     * White balance.
     * Mandatory to Theta Z1 and V.
     * Theta SC2 does not support.
     */
    var whiteBalance: WhiteBalance? = null,
)

/**
 * bluetooth power
 */
@Serializable
internal enum class BluetoothPower {
    /**
     * Power ON status
     */
    @SerialName("ON")
    ON,

    /**
     * Power OFF status
     */
    @SerialName("OFF")
    OFF,
}

/**
 * Role of the Bluetooth module
 */
@Serializable
internal enum class BluetoothRole {
    /**
     * Central: ON, Peripheral: OFF
     */
    @SerialName("Central")
    CENTRAL,

    /**
     * Central: OFF, Peripheral: ON
     */
    @SerialName("Peripheral")
    PERIPHERAL,

    /**
     * Central: ON, Peripheral: ON
     */
    @SerialName("Central_Peripheral")
    CENTRAL_PERIPHERAL,
}

/**
 * BurstMode setting.
 * When this is set to ON, burst shooting is enabled,
 * and a screen dedicated to burst shooting is displayed in Live View.
 *
 * only For RICOH THETA Z1 firmware v2.10.1 or later
 */
@Serializable
internal enum class BurstMode {
    /**
     * BurstMode ON
     */
    @SerialName("ON")
    ON,

    /**
     * BurstMode OFF
     */
    @SerialName("OFF")
    OFF,
}

/**
 * Burst shooting setting.
 *
 * only For RICOH THETA Z1 firmware v2.10.1 or later
 */
@Serializable
internal data class BurstOption(
    /**
     * @see BurstCaptureNum
     */
    val _burstCaptureNum: BurstCaptureNum? = null,

    /**
     * @see BurstBracketStep
     */
    val _burstBracketStep: BurstBracketStep? = null,

    /**
     * @see BurstCompensation
     */
    val _burstCompensation: BurstCompensation? = null,

    /**
     * @see BurstMaxExposureTime
     */
    val _burstMaxExposureTime: BurstMaxExposureTime? = null,

    /**
     * @see BurstEnableIsoControl
     */
    val _burstEnableIsoControl: BurstEnableIsoControl? = null,

    /**
     * @see BurstOrder
     */
    val _burstOrder: BurstOrder? = null
)

/**
 * Number of shots for burst shooting
 * 1, 3, 5, 7, 9
 */
@Serializable(with = BurstCaptureNumSerializer::class)
internal enum class BurstCaptureNum(val value: Int) {
    BURST_CAPTURE_NUM_1(1),
    BURST_CAPTURE_NUM_3(3),
    BURST_CAPTURE_NUM_5(5),
    BURST_CAPTURE_NUM_7(7),
    BURST_CAPTURE_NUM_9(9);

    companion object {
        fun getDefault(): BurstCaptureNum {
            return BURST_CAPTURE_NUM_1
        }

        fun getFromValue(value: Int?): BurstCaptureNum? {
            return values().firstOrNull { it.value == value }
        }
    }
}

internal object BurstCaptureNumSerializer : KSerializer<BurstCaptureNum> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("_burstCaptureNum", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: BurstCaptureNum) {
        encoder.encodeInt(value.value)
    }

    override fun deserialize(decoder: Decoder): BurstCaptureNum {
        return BurstCaptureNum.getFromValue(value = decoder.decodeInt()) ?: BurstCaptureNum.getDefault()
    }
}

/**
 * Bracket value range between each shot for burst shooting
 * 0.0, 0.3, 0.7, 1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0
 */
@Serializable(with = BurstBracketStepSerializer::class)
internal enum class BurstBracketStep(val value: Float) {
    BRACKET_STEP_0_0(0.0F),
    BRACKET_STEP_0_3(0.3F),
    BRACKET_STEP_0_7(0.7F),
    BRACKET_STEP_1_0(1.0F),
    BRACKET_STEP_1_3(1.3F),
    BRACKET_STEP_1_7(1.7F),
    BRACKET_STEP_2_0(2.0F),
    BRACKET_STEP_2_3(2.3F),
    BRACKET_STEP_2_7(2.7F),
    BRACKET_STEP_3_0(3.0F);

    companion object {
        fun getDefault(): BurstBracketStep {
            return BRACKET_STEP_1_0
        }

        fun getFromValue(value: Float?): BurstBracketStep? {
            return BurstBracketStep.values().firstOrNull { it.value == value }
        }
    }
}

internal object BurstBracketStepSerializer : KSerializer<BurstBracketStep> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("_burstBracketStep", PrimitiveKind.FLOAT)

    override fun serialize(encoder: Encoder, value: BurstBracketStep) {
        encoder.encodeFloat(value.value)
    }

    override fun deserialize(decoder: Decoder): BurstBracketStep {
        return BurstBracketStep.getFromValue(value = decoder.decodeFloat()) ?: BurstBracketStep.getDefault()
    }
}

/**
 * Exposure compensation for the base image and entire shooting for burst shooting
 * -5.0, -4.7, -4,3, -4.0, -3.7, -3,3, -3.0, -2.7, -2,3, -2.0, -1.7, -1,3, -1.0, -0.7, -0,3,
 * 0.0, 0.3, 0.7, 1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0, 3.3, 3.7, 4.0, 4.3, 4.7, 5.0
 */
@Serializable(with = BurstCompensationSerializer::class)
internal enum class BurstCompensation(val value: Float) {
    BURST_COMPENSATION_DOWN_5_0(-5.0f),
    BURST_COMPENSATION_DOWN_4_7(-4.7f),
    BURST_COMPENSATION_DOWN_4_3(-4.3f),
    BURST_COMPENSATION_DOWN_4_0(-4.0f),
    BURST_COMPENSATION_DOWN_3_7(-3.7f),
    BURST_COMPENSATION_DOWN_3_3(-3.3f),
    BURST_COMPENSATION_DOWN_3_0(-3.0f),
    BURST_COMPENSATION_DOWN_2_7(-2.7f),
    BURST_COMPENSATION_DOWN_2_3(-2.3f),
    BURST_COMPENSATION_DOWN_2_0(-2.0f),
    BURST_COMPENSATION_DOWN_1_7(-1.7f),
    BURST_COMPENSATION_DOWN_1_3(-1.3f),
    BURST_COMPENSATION_DOWN_1_0(-1.0f),
    BURST_COMPENSATION_DOWN_0_7(-0.7f),
    BURST_COMPENSATION_DOWN_0_3(-0.3f),
    BURST_COMPENSATION_0_0(0.0f),
    BURST_COMPENSATION_UP_0_3(0.3f),
    BURST_COMPENSATION_UP_0_7(0.7f),
    BURST_COMPENSATION_UP_1_0(1.0f),
    BURST_COMPENSATION_UP_1_3(1.3f),
    BURST_COMPENSATION_UP_1_7(1.7f),
    BURST_COMPENSATION_UP_2_0(2.0f),
    BURST_COMPENSATION_UP_2_3(2.3f),
    BURST_COMPENSATION_UP_2_7(2.7f),
    BURST_COMPENSATION_UP_3_0(3.0f),
    BURST_COMPENSATION_UP_3_3(3.3f),
    BURST_COMPENSATION_UP_3_7(3.7f),
    BURST_COMPENSATION_UP_4_0(4.0f),
    BURST_COMPENSATION_UP_4_3(4.3f),
    BURST_COMPENSATION_UP_4_7(4.7f),
    BURST_COMPENSATION_UP_5_0(5.0f);

    companion object {
        fun getDefault(): BurstCompensation {
            return BURST_COMPENSATION_0_0
        }

        fun getFromValue(value: Float?): BurstCompensation? {
            return BurstCompensation.values().firstOrNull { it.value == value }
        }
    }
}

internal object BurstCompensationSerializer : KSerializer<BurstCompensation> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("_burstCompensation", PrimitiveKind.FLOAT)

    override fun serialize(encoder: Encoder, value: BurstCompensation) {
        encoder.encodeFloat(value.value)
    }

    override fun deserialize(decoder: Decoder): BurstCompensation {
        return BurstCompensation.getFromValue(value = decoder.decodeFloat()) ?: BurstCompensation.getDefault()
    }
}

/**
 * Maximum exposure time for burst shooting
 * 0.5, 0.625, 0.76923076, 1, 1.3, 1.6, 2, 2.5, 3.2, 4, 5, 6, 8, 10, 13, 15, 20, 25, 30, 40, 50, 60
 */
@Serializable(with = BurstMaxExposureTimeSerializer::class)
internal enum class BurstMaxExposureTime(val value: Double) {
    MAX_EXPOSURE_TIME_0_5(0.5),
    MAX_EXPOSURE_TIME_0_625(0.625),
    MAX_EXPOSURE_TIME_0_76923076(0.76923076),
    MAX_EXPOSURE_TIME_1(1.0),
    MAX_EXPOSURE_TIME_1_3(1.3),
    MAX_EXPOSURE_TIME_1_6(1.6),
    MAX_EXPOSURE_TIME_2(2.0),
    MAX_EXPOSURE_TIME_2_5(2.5),
    MAX_EXPOSURE_TIME_3_2(3.2),
    MAX_EXPOSURE_TIME_4(4.0),
    MAX_EXPOSURE_TIME_5(5.0),
    MAX_EXPOSURE_TIME_6(6.0),
    MAX_EXPOSURE_TIME_8(8.0),
    MAX_EXPOSURE_TIME_10(10.0),
    MAX_EXPOSURE_TIME_13(13.0),
    MAX_EXPOSURE_TIME_15(15.0),
    MAX_EXPOSURE_TIME_20(20.0),
    MAX_EXPOSURE_TIME_25(25.0),
    MAX_EXPOSURE_TIME_30(30.0),
    MAX_EXPOSURE_TIME_40(40.0),
    MAX_EXPOSURE_TIME_50(50.0),
    MAX_EXPOSURE_TIME_60(60.0);

    companion object {
        fun getDefault(): BurstMaxExposureTime {
            return MAX_EXPOSURE_TIME_15
        }

        fun getFromValue(value: Double?): BurstMaxExposureTime? {
            return BurstMaxExposureTime.values().firstOrNull { it.value == value }
        }
    }
}

internal object BurstMaxExposureTimeSerializer : KSerializer<BurstMaxExposureTime> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("_burstMaxExposureTime", PrimitiveKind.DOUBLE)

    override fun serialize(encoder: Encoder, value: BurstMaxExposureTime) {
        encoder.encodeDouble(value.value)
    }

    override fun deserialize(decoder: Decoder): BurstMaxExposureTime {
        return BurstMaxExposureTime.getFromValue(value = decoder.decodeDouble()) ?: BurstMaxExposureTime.getDefault()
    }
}

/**
 * Adjustment with ISO sensitivity for burst shooting
 * 0: Do not adjust with ISO sensitivity, 1: Adjust with ISO sensitivity
 */
@Serializable(with = BurstEnableIsoControlSerializer::class)
internal enum class BurstEnableIsoControl(val value: Int) {
    OFF(0),
    ON(1);

    companion object {
        fun getDefault(): BurstEnableIsoControl {
            return OFF
        }

        fun getFromValue(value: Int?): BurstEnableIsoControl? {
            return BurstEnableIsoControl.values().firstOrNull { it.value == value }
        }
    }
}

internal object BurstEnableIsoControlSerializer : KSerializer<BurstEnableIsoControl> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("_burstEnableIsoControl", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: BurstEnableIsoControl) {
        encoder.encodeInt(value.value)
    }

    override fun deserialize(decoder: Decoder): BurstEnableIsoControl {
        return BurstEnableIsoControl.getFromValue(value = decoder.decodeInt()) ?: BurstEnableIsoControl.getDefault()
    }
}

/**
 * Shooting order for burst shooting
 * 0: '0' → '-' → '+', 1: '-' → '0' → '+'
 */
@Serializable(with = BurstOrderSerializer::class)
internal enum class BurstOrder(val value: Int) {
    BURST_BRACKET_ORDER_0(0),
    BURST_BRACKET_ORDER_1(1);

    companion object {
        fun getDefault(): BurstOrder {
            return BURST_BRACKET_ORDER_0
        }

        fun getFromValue(value: Int?): BurstOrder? {
            return BurstOrder.values().firstOrNull { it.value == value }
        }
    }
}

internal object BurstOrderSerializer : KSerializer<BurstOrder> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("_burstOrder", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: BurstOrder) {
        encoder.encodeInt(value.value)
    }

    override fun deserialize(decoder: Decoder): BurstOrder {
        return BurstOrder.getFromValue(value = decoder.decodeInt()) ?: BurstOrder.getDefault()
    }
}

/**
 * camera control source
 * Sets whether to lock/unlock the camera UI.
 * The current setting can be acquired by camera.getOptions, and it can be changed by camera.setOptions.
 *
 * For RICOH THETA X
 */
@Serializable
internal enum class CameraControlSource {
    /**
     * Operation is possible with the camera. Locks the smartphone
     * application UI (supported app only).
     */
    @SerialName("camera")
    CAMERA,

    /**
     * Operation is possible with the smartphone application. Locks
     * the UI on the shooting screen on the camera.
     */
    @SerialName("app")
    APP,
}

/**
 * Camera mode.
 * The current setting can be acquired by camera.getOptions, and it can be changed by camera.setOptions.
 *
 * For RICOH THETA X
 */
@Serializable
internal enum class CameraMode {
    /**
     * shooting screen
     */
    @SerialName("capture")
    CAPTURE,

    /**
     * playback screen
     */
    @SerialName("playback")
    PLAYBACK,

    /**
     * shooting setting screen
     */
    @SerialName("setting")
    SETTING,

    /**
     * plugin selection screen
     */
    @SerialName("plugin")
    PLUGIN,
}

/**
 * capture Mode
 */
@Serializable
internal enum class CaptureMode {
    /**
     * 	Still image capture mode
     */
    @SerialName("image")
    IMAGE,

    /**
     * Video capture mode
     */
    @SerialName("video")
    VIDEO,

    /**
     * Live streaming mode
     */
    @SerialName("_liveStreaming")
    LIVE_STREAMING,

    /**
     * Interval mode of Theta SC2.
     */
    @SerialName("interval")
    INTERVAL,

    /**
     * Preset mode of Theta SC2.
     */
    @SerialName("_preset")
    PRESET,
}

/**
 * Face detection
 *
 * For
 * - RICOH THETA X
 */
@Serializable
internal enum class FaceDetect {
    /**
     * Face detection ON
     */
    @SerialName("ON")
    ON,

    /**
     * Face detection OFF
     */
    @SerialName("OFF")
    OFF,
}

/**
 * Microphone gain.
 *
 * For
 * - RICOH THETA X
 * - RICOH THETA Z1
 * - RICOH THETA V
 */
@Serializable
internal enum class Gain {
    /**
     * Normal mode
     */
    @SerialName("normal")
    NORMAL,

    /**
     * Loud volume mode
     */
    @SerialName("megavolume")
    MEGA_VOLUME,

    /**
     * Mute mode
     * (RICOH THETA V firmware v2.50.1 or later, RICOH THETA X is not supported.)
     */
    @SerialName("mute")
    MUTE,
}

/**
 * AI auto thumbnail setting
 */
@Serializable
internal enum class AiAutoThumbnail {
    /**
     * AI auto setting ON
     */
    @SerialName("ON")
    ON,

    /**
     * AI auto setting OFF
     */
    @SerialName("OFF")
    OFF,
}

/**
 * IP address allocation to be used when wired LAN is enabled.
 */
@Serializable
internal data class EthernetConfig(
    /**
     * dynamic or static
     * dynamic is default value.
     */
    val ipAddressAllocation: IpAddressAllocation,

    /**
     * (optional) IPv4 for IP address
     */
    val ipAddress: String? = null,

    /**
     * (optional) IPv4 for subnet mask
     */
    val subnetMask: String? = null,

    /**
     * (optional) IPv4 for default gateway
     */
    val defaultGateway: String? = null,

    /**
     * (optional) refer to _proxy for detail
     */
    val _proxy: Proxy? = null,
)

/**
 * Microphone channel setting
 */
@Serializable
internal enum class MicrophoneChannel {
    /**
     * 360 degree spatial audio + monaural
     */
    @SerialName("4ch+1ch")
    SPATIAL,

    /**
     * Monaural
     */
    @SerialName("1ch")
    MONAURAL,
}

/**
 * Network type setting supported by Theta V, Z1, and X.
 */
@Serializable
internal enum class NetworkType {
    /**
     * Direct mode
     */
    @SerialName("AP")
    DIRECT,

    /**
     * Client mode
     */
    @SerialName("CL")
    CLIENT,

    /**
     * Client mode via Ethernet cable, supported only by Theta V and Z1.
     */
    @SerialName("ETHERNET")
    ETHERNET,

    /**
     * Network is off. This value can be gotten only by plugin.
     */
    @SerialName("OFF")
    OFF,
}

/**
 * Power saving mode
 */
@Serializable
internal enum class PowerSaving {
    /**
     * Power saving mode ON
     */
    ON,

    /**
     * Power saving mode OFF
     */
    OFF,
}

/**
 * Shooting Method setting
 */
@Serializable
internal enum class ShootingMethod {
    /**
     * Normal shooting
     */
    @SerialName("normal")
    NORMAL,

    /**
     * Interval shooting
     */
    @SerialName("interval")
    INTERVAL,

    /**
     * Move interval shooting (RICOH THETA Z1 firmware v1.50.1 or
     * later, RICOH THETA X is not supported)
     */
    @SerialName("moveInterval")
    MOVE_INTERVAL,

    /**
     * Fixed interval shooting (RICOH THETA Z1 firmware v1.50.1 or
     * later, RICOH THETA X is not supported)
     */
    @SerialName("fixedInterval")
    FIXED_INTERVAL,

    /**
     * Multi bracket shooting
     */
    @SerialName("bracket")
    BRACKET,

    /**
     * Interval composite shooting (RICOH THETA X is not supported)
     */
    @SerialName("composite")
    COMPOSITE,

    /**
     * Continuous shooting (RICOH THETA X or later)
     */
    @SerialName("continuous")
    CONTINUOUS,

    /**
     * TimeShift shooting (RICOH THETA X or later)
     */
    @SerialName("timeShift")
    TIMESHIFT,

    /**
     * Burst shooting (RICOH THETA Z1 v2.10.1 or later, RICOH THETA X
     * is not supported)
     */
    @SerialName("burst")
    BURST,
}

/**
 * Preset mode for Theta SC2 and Theta SC2 for business.
 */
@Serializable
internal enum class Preset {
    /**
     * Preset "Face" mode suitable for portrait shooting just for Theta SC2.
     *
     * A person’s face is detected and its position is adjusted to the center of the image
     * to obtain a clear image of the person.
     */
    @SerialName("face")
    FACE,

    /**
     * Preset "Night View" mode just for Theta SC2.
     *
     * The dynamic range of bright areas is expanded to reduce noise.
     * In addition, a person’s face is detected to obtain a clear image of the person.
     */
    @SerialName("nightView")
    NIGHT_VIEW,

    /**
     * Preset "Lens-by-Lens Exposure" mode just for Theta SC2.
     *
     * Image processing such as exposure adjustment and white balance adjustment is performed
     * individually for each image captured with the front and rear lenses.
     * This mode is suitable for capturing scenes with significantly different brightness conditions
     * between the camera front side and the camera rear side.
     * Images captured with the front and rear lenses are displayed side by side
     */
    @SerialName("lensbylensExposure")
    LENS_BY_LENS_EXPOSURE,

    /**
     * Preset "Room" mode just for SC2 for business.
     *
     * Suitable for indoor shooting where there is gap in brightness between outdoors and indoors.
     * Also, the self-timer function enables time shift between shooting with the front lens
     * and rear lens making it possible for the photographer not to be captured in the image.
     */
    @SerialName("room")
    ROOM,
}

/**
 * preview format
 */
@Serializable
internal data class PreviewFormat(
    /**
     * width
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val width: Int? = null,

    /**
     * height
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val height: Int? = null,

    /**
     * frame rate
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val framerate: Int? = null,
)

/**
 * Proxy information to be used when wired LAN is enabled.
 *
 * The current setting can be acquired by camera.getOptions,
 * and it can be changed by camera.setOptions.
 *
 * For
 * RICOH THETA Z1 firmware v2.20.3 or later
 * RICOH THETA X firmware v2.00.0 or later
 */
@Serializable
internal data class Proxy(
    /**
     * true: use proxy false: do not use proxy
     */
    val use: Boolean,

    /**
     * Proxy server URL
     */
    val url: String? = null,

    /**
     * Proxy server port number: 0 to 65535
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val port: Int? = null,

    /**
     * User ID used for proxy authentication
     */
    val userid: String? = null,

    /**
     * Password used for proxy authentication
     */
    val password: String? = null,
)

/**
 * Time shift shooting.
 *
 * For Theta X, Z1 and V.
 */
@Serializable
internal data class TimeShift(
    /**
     * Shooting order.
     * "front": first shoot the front side (side with Theta logo) then shoot the rear side (side with monitor).
     * "rear" first shoot the rear side then shoot the front side.
     */
    var firstShooting: FirstShootingEnum? = null,

    /**
     * Time (sec) before 1st lens shooting.
     * 0 to 10.  For V or Z1, default is 5. For X, default is 2.
     */
    @Serializable(with = NumberAsIntSerializer::class)
    var firstInterval: Int? = null,

    /**
     * Time (sec) from 1st lens shooting until start of 2nd lens shooting.
     * 0 to 10.  Default is 5.
     */
    @Serializable(with = NumberAsIntSerializer::class)
    var secondInterval: Int? = null,
)

/**
 * On time shift shooting, specify which side is shot first.
 */
@Serializable
internal enum class FirstShootingEnum {
    /**
     * first shoot the front side
     */
    @SerialName("front")
    FRONT,

    /**
     * first shoot the rear side
     */
    @SerialName("rear")
    REAR,
}

/**
 * top bottom correction
 *
 * Sets the top/bottom correction.  For RICOH THETA V and RICOH
 * THETA Z1, the top/bottom correction can be set only for still
 * images.  For RICOH THETA X, the top/bottom correction can be
 * set for both still images and videos.
 */
@Serializable
internal enum class TopBottomCorrectionOption {
    /**
     * Top/bottom correction is performed.
     */
    @SerialName("Apply")
    APPLY,

    /**
     * Refer to top/bottom correction when shooting with "ApplyAuto"
     */
    @SerialName("ApplyAuto")
    APPLY_AUTO,

    /**
     * Top/bottom correction is performed. The parameters used for
     * top/bottom correction for the first image are saved and used
     * for the 2nd and subsequent images.(RICOH THETA X or later)
     */
    @SerialName("ApplySemiAuto")
    APPLY_SEMIAUTO,

    /**
     * Performs top/bottom correction and then saves the parameters.
     */
    @SerialName("ApplySave")
    APPLY_SAVE,

    /**
     * Performs top/bottom correction using the saved parameters.
     */
    @SerialName("ApplyLoad")
    APPLY_LOAD,

    /**
     * Does not perform top/bottom correction.
     */
    @SerialName("Disapply")
    DISAPPLY,

    /**
     * Performs the top/bottom correction with the specified front
     * position. The front position can be specified with
     * _topBottomCorrectionRotation.
     */
    @SerialName("Manual")
    MANUAL,
}

/**
 * Sets the front position for the top/bottom correction.
 * Enabled only for _topBottomCorrection Manual.
 */
@Serializable
internal data class TopBottomCorrectionRotation(
    /**
     * Specifies the pitch.
     * Specified range is -90.0 to +90.0, stepSize is 0.1
     */
    val pitch: Float? = null,

    /**
     * Specifies the roll.
     * Specified range is -180.0 to +180.0, stepSize is 0.1
     */
    val roll: Float? = null,

    /**
     * Specifies the yaw.
     * Specified range is -180.0 to +180.0, stepSize is 0.1
     */
    val yaw: Float? = null
)

/**
 * White balance setting
 */
@Serializable
internal enum class WhiteBalance {
    /**
     * Automatic
     */
    @SerialName("auto")
    AUTO,

    /**
     * Outdoor
     */
    @SerialName("daylight")
    DAYLIGHT,

    /**
     * Shade
     */
    @SerialName("shade")
    SHADE,

    /**
     * Cloudy
     */
    @SerialName("cloudy-daylight")
    CLOUDY_DAYLIGHT,

    /**
     * Incandescent light 1
     */
    @SerialName("incandescent")
    INCANDESCENT,

    /**
     * Incandescent light 2
     */
    @SerialName("_warmWhiteFluorescent")
    _WARM_WHITE_FLUORESCENT,

    /**
     * Fluorescent light 1 (daylight)
     */
    @SerialName("_dayLightFluorescent")
    _DAYLIGHT_FLUORESCENT,

    /**
     * Fluorescent light 2 (natural white)
     */
    @SerialName("_dayWhiteFluorescent")
    _DAYWHITE_FLUORESCENT,

    /**
     * Fluorescent light 3 (white)
     */
    @SerialName("fluorescent")
    FLUORESCENT,

    /**
     * Fluorescent light 4 (light bulb color)
     */
    @SerialName("_bulbFluorescent")
    _BULB_FLUORESCENT,

    /**
     * CT settings (specified by the _colorTemperature option)
     */
    @SerialName("_colorTemperature")
    _COLOR_TEMPERATURE,

    /**
     * Underwater
     */
    @SerialName("_underwater")
    _UNDERWATER,
}

/**
 * White balance auto strength
 *
 * To set the strength of white balance auto for low color temperature scene.
 * This option can be set for photo mode and video mode separately.
 * Also this option will not be cleared by power-off.
 *
 * For RICOH THETA Z1 firmware v2.20.3 or later
 */
@Serializable
internal enum class WhiteBalanceAutoStrength {
    /**
     * correct tint for low color temperature scene
     */
    @SerialName("ON")
    ON,

    /**
     * not correct tint for low color temperature scene
     */
    @SerialName("OFF")
    OFF,
}

/**
 * Wireless LAN frequency of the camera supported by Theta V, Z1 and X.
 */
@Serializable(with = WlanFrequencySerializer::class)
internal enum class WlanFrequency(val frequency: Double) {
    /**
     * 2.4GHz
     */
    GHZ_2_4(2.4),

    /**
     * 5GHz
     */
    GHZ_5(5.0),
}

/**
 * [Custom serializer](https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/serializers.md#custom-serializers)
 * for [WlanFrequency]
 */
internal object WlanFrequencySerializer : KSerializer<WlanFrequency> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("_wlanFrequency", PrimitiveKind.DOUBLE)

    override fun serialize(encoder: Encoder, value: WlanFrequency) {
        encoder.encodeDouble(value.frequency)
    }

    override fun deserialize(decoder: Decoder): WlanFrequency {
        val frequency = decoder.decodeDouble()
        return if (frequency < 5) WlanFrequency.GHZ_2_4 else WlanFrequency.GHZ_5
    }
}

/**
 * Visibility reduction setting
 */
@Serializable
internal enum class VisibilityReduction {
    /**
     * Reduction is ON.
     */
    @SerialName("ON")
    ON,

    /**
     * Reduction is OFF. Improve accuracy of stitching for camera
     * bottom direction.
     */
    @SerialName("OFF")
    OFF,
}

/**
 * Video stitching setting
 */
@Serializable
internal enum class VideoStitching {
    /**
     * Stitching is OFF. Recorded video is saved in Dual-Fisheye
     * format.
     */
    @SerialName("none")
    NONE,

    /**
     * Stitching by the camera is ON. Recorded video is saved in
     * Equirectangular format.
     */
    @SerialName("ondevice")
    ONDEVICE,
}

/**
 * supported language in camera OS
 */
@Serializable
internal enum class Language {
    /**
     * English: en-US
     */
    @SerialName("en-US")
    US,

    /**
     * English: en-GB
     */
    @SerialName("en-GB")
    GB,

    /**
     * Japanese: ja
     */
    @SerialName("ja")
    JA,

    /**
     * French: fr
     */
    @SerialName("fr")
    FR,

    /**
     * German: de (Deutsch)
     */
    @SerialName("de")
    DE,

    /**
     * Taiwan: zh-TW
     */
    @SerialName("zh-TW")
    TW,

    /**
     * Chinese: zh-CN
     */
    @SerialName("zh-CN")
    CN,

    /**
     * Italian: it
     */
    @SerialName("it")
    IT,

    /**
     * Korean: ko
     */
    @SerialName("ko")
    KO,
}

/**
 * gps position information
 */
@Serializable
internal enum class GpsTagRecording {
    /**
     * Assign position information
     */
    @SerialName("on")
    ON,

    /**
     * Do not assign position information
     */
    @SerialName("off")
    OFF,
}

/**
 * GPS information
 */
@Serializable
internal data class GpsInfo(
    /**
     * Latitude (-90.000000 – 90.000000)
     * When GPS is disabled: 65535
     *
     * 65535 is set for lat and lng when disabling the GPS setting at
     * RICOH THETA Z1 and prior.
     *
     * For RICOH THETA X, ON/OFF for assigning position information is
     * set at _gpsTagRecording
     */
    val lat: Float? = null,

    /**
     * Longitude (-180.000000 – 180.000000)
     * When GPS is disabled: 65535
     *
     * 65535 is set for lat and lng when disabling the GPS setting at
     * RICOH THETA Z1 and prior.
     *
     * For RICOH THETA X, ON/OFF for assigning position information is
     * set at _gpsTagRecording
     */
    val lng: Float? = null,

    /**
     * Altitude (meters)
     * When GPS is disabled: 0
     *
     * 65535 is set for lat and lng when disabling the GPS setting at
     * RICOH THETA Z1 and prior.
     *
     * For RICOH THETA X, ON/OFF for assigning position information is
     * set at _gpsTagRecording
     */
    val _altitude: Float? = null,

    /**
     * Location information acquisition time
     * YYYY:MM:DD hh:mm:ss+(-)hh:mm
     * hh is in 24-hour time, +(-)hh:mm is the time zone
     * when GPS is disabled: ""(null characters)
     *
     * 65535 is set for lat and lng when disabling the GPS setting at
     * RICOH THETA Z1 and prior.
     *
     * For RICOH THETA X, ON/OFF for assigning position information is
     * set at _gpsTagRecording
     */
    val _dateTimeZone: String? = null,

    /**
     * Geodetic reference
     * When GPS is enabled: WGS84
     * When GPS is disabled: ""(null characters)
     *
     * 65535 is set for lat and lng when disabling the GPS setting at
     * RICOH THETA Z1 and prior.
     *
     * For RICOH THETA X, ON/OFF for assigning position information is
     * set at _gpsTagRecording
     */
    val _datum: String? = null,
)

/**
 * image filter setting
 */
@Serializable
internal enum class ImageFilter {
    /**
     * No filter
     */
    @SerialName("off")
    OFF,

    /**
     *	DR compensation(RICOH THETA X is not supported)
     */
    @SerialName("DR Comp")
    DR_COMP,

    /**
     * Noise reduction
     */
    @SerialName("Noise Reduction")
    NOISE_REDUCTION,

    /**
     * If you use shooting command (camera.takePicture) continuously
     * while using HDR filter, the next shooting command cannot be
     * used until the previous shooting command is completed
     * (i.e. state of Commands/Status is “done”).
     */
    @SerialName("hdr")
    HDR,

    /**
     * Handheld HDR
     * If you use shooting command (camera.takePicture) continuously
     * while using HDR filter, the next shooting command cannot be
     * used until the previous shooting command is completed
     * (i.e. state of Commands/Status is “done”).
     *
     * (RICOH THETA X firmware v2.40.0 or later,
     *  RICOH THETA Z1 firmware v1.20.1 or later,
     *  and RICOH THETA V firmware v3.10.1 or later)
     */
    @SerialName("Hh hdr")
    HH_HDR,
}

internal object MediaTypeSerializer :
    SerialNameEnumIgnoreUnknownSerializer<MediaType>(MediaType.entries, MediaType.UNKNOWN)

/**
 * Media type setting
 */
@Serializable(with = MediaTypeSerializer::class)
internal enum class MediaType : SerialNameEnum {
    /**
     * Undefined value
     */
    UNKNOWN,

    /**
     * jpeg image
     */
    JPEG {
        override val serialName: String = "jpeg"
    },


    /**
     * mp4 video
     */
    MP4 {
        override val serialName: String = "mp4"
    },

    /**
     * raw+ image
     */
    RAW {
        override val serialName: String = "raw+"
    },
}

/**
 * Media file format setting
 */
@Serializable
internal data class MediaFileFormat(
    /**
     * media type.
     */
    val type: MediaType? = null,

    /**
     * media width
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val width: Int? = null,

    /**
     * media height
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val height: Int? = null,

    /**
     * video codec
     */
    val _codec: String? = null,

    /**
     * frame rate (theta)
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val _frameRate: Int? = null,
)

/**
 * Shutter volume support
 */
@Serializable
internal data class ShutterVolumeSupport(
    /**
     * minimum shutter volume
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val minShutterVolume: Int? = null,

    /**
     * maximum shutter volume
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val maxShutterVolume: Int? = null,
)

/**
 * image stitching setting
 */
@Serializable
internal enum class ImageStitching {
    /**
     * Refer to stitching when shooting with "auto"
     */
    @SerialName("auto")
    AUTO,

    /**
     * Performs static stitching
     */
    @SerialName("static")
    STATIC,

    /**
     * Performs dynamic stitching(RICOH THETA X or later)
     */
    @SerialName("dynamic")
    DYNAMIC,

    /**
     * For Normal shooting, performs dynamic stitching, for Interval
     * shooting, saves dynamic distortion correction parameters for
     * the first image and then uses them for the 2nd and subsequent
     * images(RICOH THETA X is not supported)
     */
    @SerialName("dynamicAuto")
    DYNAMIC_AUTO,

    /**
     * Performs semi-dynamic stitching
     */
    @SerialName("dynamicSemiAuto")
    DYNAMIC_SEMI_AUTO,

    /**
     * Performs dynamic stitching and then saves distortion correction
     * parameters
     */
    @SerialName("dynamicSave")
    DYNAMIC_SAVE,

    /**
     * Performs stitching using the saved distortion correction
     * parameters
     */
    @SerialName("dynamicLoad")
    DYNAMIC_LOAD,

    /**
     * Does not perform stitching
     */
    @SerialName("none")
    NONE,
}

/**
 * Capture interval support
 */
@Serializable
internal data class CaptureIntervalSupport(
    /**
     * minimum interval
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val minInterval: Int? = null,

    /**
     * maximum interval
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val maxInterval: Int? = null,
)

/**
 * Capture number support
 */
@Serializable
internal data class CaptureNumberSupport(
    /**
     * Unlimited
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val _limitless: Int? = null,

    /**
     * minimum value
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val minNumber: Int? = null,

    /**
     * maximum value
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val maxNumber: Int? = null,
)
