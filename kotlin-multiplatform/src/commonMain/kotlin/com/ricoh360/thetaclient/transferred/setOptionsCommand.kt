/*
 * [camera.setOptions](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.set_options.md)
 */
package com.ricoh360.thetaclient.transferred

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * set options request
 */
@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
data class SetOptionsRequest(
    override val name: String = "camera.setOptions",
    override val parameters: SetOptionsParams,
) : CommandApiRequest

/**
 * set options request parameters
 */
@Serializable
data class SetOptionsParams(
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
data class SetOptionsResponse(
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
data class ResultSetOptions(
    /**
     * option key value pair
     */
    val options: Options,
)

/**
 * option key value pair
 */
@Serializable
data class Options(
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
     * bluetooth power
     *
     * @see BluetoothPower
     */
    var _bluetoothPower: BluetoothPower? = null,

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
     * Shooting mode.
     *
     * The current setting can be acquired by camera.getOptions, and
     * it can be changed by camera.setOptions.
     *
     * Swithcing modes may take time. Wait a while to send the command
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
     * Image format used in shooting.
     *
     * @see MediaFileFormat
     */
    var fileFormat: MediaFileFormat? = null,

    /**
     * supported mage format used in shooting.
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
     * Password used for digest authentication when _networkType is set to client mode.
     */
    var _password: String? = null,

    /**
     * Length of standby time before the camera automatically powers OFF.
     */
    @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
    @Serializable(with = NumbersAsIntsSerializer::class)
    var offDelaySupport: List<Int>? = null,

    /**
     * The estimated remaining number of shots for the current
     * shooting settings.
     */
    @Serializable(with = NumberAsIntSerializer::class)
    var remainingPictures: Int? = null,

    /**
     * preview format
     */
    var previewFormat: PreviewFormat? = null,

    /**
     * Supported preview format
     */
    var previewFormatSupport: List<PreviewFormat>? = null,

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
     * Supported shutter spped.
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
     * top bottom correction
     *
     * Sets the top/bottom correction.  For RICOH THETA V and RICOH
     * THETA Z1, the top/bottom correction can be set only for still
     * images.  For RICOH THETA X, the top/bottom correction can be
     * set for both still images and videos.
     */
    var _topBottomCorrection: TopBottomCorrectionOption? = null,

    /**
     * Supported top bottom correction
     */
    var _topBottomCorrectionSupport: List<TopBottomCorrectionOption>? = null,

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
     * To set the strength of white balance auto for low color temperature scene.
     * This option can be set for photo mode and video mode separately.
     * Also this option will not be cleared by power-off.
     *
     * For RICOH THETA Z1 firmware v2.20.3 or later
     */
    var _whiteBalanceAutoStrength: WhiteBalanceAutoStrength? = null,
)

/**
 * bluetooth power
 */
@Serializable
enum class BluetoothPower {
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
 * camera control source
 */
@Serializable
enum class CameraControlSource {
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
 * capture Mode
 */
@Serializable
enum class CaptureMode {
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
    LIVESTREAMING,
}

/**
 * AI auto thumbnail setting
 */
@Serializable
enum class AiAutoThumbnail {
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
 * Microphone channel setting
 */
@Serializable
enum class MicrophoneChannel {
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
 * Network type setting
 */
@Serializable
enum class NetworkType {
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
}

/**
 * Shooting Method setting
 */
@Serializable
enum class ShootingMethod {
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
 * preview format
 */
@Serializable
data class PreviewFormat(
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
 * White balance setting
 */
@Serializable
enum class WhiteBalance {
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
enum class WhiteBalanceAutoStrength {
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
 * Visibility reduction setting
 */
@Serializable
enum class VisibilityReduction {
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
enum class VideoStitching {
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
 * top bottom correction option setting
 */
@Serializable
enum class TopBottomCorrectionOption {
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
 * supported language in camera OS
 */
@Serializable
enum class Language {
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
     * Chineses: zh-CN
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
enum class GpsTagRecording {
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
data class GpsInfo(
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
enum class ImageFilter {
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
     * (RICOH THETA Z1 firmware v1.20.1 or later and RICOH THETA V
     * firmware v3.10.1 or later. RICOH THETA X is not supported)
     */
    @SerialName("Hh hdr")
    HH_HDR,
}

/**
 * Media type setting
 */
@Serializable
enum class MediaType {
    /**
     * jpeg image
     */
    @SerialName("jpeg")
    JPEG,

    /**
     * mp4 video
     */
    @SerialName("mp4")
    MP4,

    /**
     * raw+ image
     */
    @SerialName("raw+")
    RAW,
}

/**
 * Media file format setting
 */
@Serializable
data class MediaFileFormat(
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
data class ShutterVolumeSupport(
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
enum class ImageStitching {
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
    DYNAMIC_SEMIAUTO,

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
