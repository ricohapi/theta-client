/*
 * [/osc/state](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/protocols/state.md)
 */
package com.ricoh360.thetaclient.transferred

import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * /osc/state api request
 */
internal object StateApi {
    const val PATH = "/osc/state"
    val METHOD = HttpMethod.Post
}

/**
 * state api response
 */
@Serializable
internal data class StateApiResponse(
    /**
     * Takes a unique value per current state ID.
     */
    val fingerprint: String,

    /**
     * Camera state (Refer to the next section for details.)
     */
    val state: CameraState,
)

/**
 * camera state information
 */
@Serializable
internal data class CameraState(
    /**
     * Battery level (0.0 to 1.0), When using an external power
     * source, 1 (100%)
     */
    val batteryLevel: Double,

    /**
     * Storage URI
     * V, Z1:0 http://[IP address]/files/[eMMC ID]
     *     X: http://[IP address]/files/
     */
    val storageUri: String? = null,

    /**
     * Storage ID
     *
     * RICOH THETA X or later
     */
    val _storageID: String? = null,

    /**
     * Continuously shoots state
     *
     * @see CaptureStatus
     */
    val _captureStatus: CaptureStatus,

    /**
     * Shooting time of movie (sec)
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val _recordedTime: Int,

    /**
     * Remaining time of movie (sec)
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val _recordableTime: Int,

    /**
     * Number of still images captured during continuous shooting, Unit: images
     *
     * RICOH THETA V or later
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val _capturedPictures: Int? = null,

    /**
     * Elapsed time for interval composite shooting (sec)
     *
     * RICOH THETA S firmware v01.82 or later
     * RICOH THETA V is not supported
     * RICOH THETA Z1
     * RICOH THETA X is not supported
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val _compositeShootingElapsedTime: Int? = null,

    /**
     * URL of the last saved file
     * V, Z1: http://[IP address]/files/[eMMC ID]/[Directory name]/[[File name]
     *     X: http://[IP address]/files/[Directory name]/[[File name] DNG
     * format files are not displayed. For burst shooting, files in
     * the DNG format are displayed.
     */
    val _latestFileUrl: String? = null,

    /**
     * Charging state
     *
     * @see ChargingState
     */
    val _batteryState: ChargingState,

    /**
     * API version currently set (1: v2.0, 2: v2.1)
     */
    @Serializable(with = NumberAsIntSerializer::class)
    val _apiVersion: Int,

    /**
     * Plugin running state (true: running, false: stop)
     *
     * RICOH THETA V firmware v2.21.1 or later
     * RICOH THETA Z1 or later
     */
    val _pluginRunning: Boolean? = null,

    /**
     * Plugin web server state (true: enabled, false: disabled)
     *
     * RICOH THETA V firmware v2.21.1 or later
     * RICOH THETA Z1 or later
     */
    val _pluginWebServer: Boolean? = null,

    /**
     * Shooting function status
     *
     * RICOH THETA Z1 or later
     * @see ShootingFunction
     */
    val _function: ShootingFunction? = null,

    /**
     * My setting changed state
     * true: Shooting function status is “mySetting" and some
     * my setting properties are changed
     *
     * RICOH THETA Z1 or later
     */
    val _mySettingChanged: Boolean? = null,

    /**
     * String Identifies the microphone used while recording video
     * when _microphone option
     *
     * RICOH THETA X or later
     * @see MicrophoneOption
     */
    val _currentMicrophone: MicrophoneOption? = null,

    /**
     * Used to identify IN/SD at the app
     *
     * RICOH THETA X or later
     * @see StorageOption
     */
    val _currentStorage: StorageOption? = null,

    /**
     * information of the camera
     *
     * @see CameraError
     */
    val _cameraError: List<CameraError>? = null,

    /**
     * true: Battery inserted; false: Battery not inserted.  If
     * "_batteryInsert" is true, video recording via the WebAPI is
     * restricted.  Video recording at 4K 60fps, 5.7K 10fps, 5.7K
     * 15fps, 5.7K 30fps, or 8K 10fps is not possible with the WebAPI.
     * When the battery level is at the specified value or less, video
     * recording at 4K 30fps is not possible with the WebAPI.
     *
     * RICOH THETA X or later
     */
    val _batteryInsert: Boolean? = null,

    /**
     * Location data is obtained through an external device using WebAPI or BLE-API.
     * Please refer to the object specification for [GpsInfo] as well.
     *
     * RICOH THETA X firmware v2.20.1 or later
     * RICOH THETA Z1 firmware v3.10.2 or later
     */
    val _externalGpsInfo: StateGpsInfo? = null,

    /**
     * Location data is obtained through an internal GPS module.
     * Please refer to the object specification for [GpsInfo] as well.
     * RICOH THETA Z1 does not have a built-in GPS module.
     *
     * RICOH THETA X firmware v2.20.1 or later
     * RICOH THETA Z1 firmware v3.10.2 or later
     */
    val _internalGpsInfo: StateGpsInfo? = null,

    /**
     * This represents the current temperature inside the camera as an integer value, ranging from -10°C to 100°C with a precision of 1°C.
     *
     * RICOH THETA X firmware v2.20.1 or later
     * RICOH THETA Z1 firmware v3.10.2 or later
     */
    val _boardTemp: Int? = null,

    /**
     * This represents the current temperature inside the battery as an integer value, ranging from -10°C to 100°C with a precision of 1°C.
     *
     * RICOH THETA X firmware v2.20.1 or later
     * RICOH THETA Z1 firmware v3.10.2 or later
     */
    val _batteryTemp: Int? = null,
)

/**
 * Capture status
 */
@Serializable
internal enum class CaptureStatus {
    /**
     * shooting: Performing continuously shoots,
     */
    @SerialName("shooting")
    SHOOTING,

    /**
     * idle: In standby,
     */
    @SerialName("idle")
    IDLE,

    /**
     * self-timer countdown: Self-timer is operating,
     */
    @SerialName("self-timer countdown")
    SELF_TIMER_COUNTDOWN,

    /**
     * bracket shooting: Performing multi bracket shooting,
     */
    @SerialName("bracket shooting")
    BRACKET_SHOOTING,

    /**
     * converting: Converting post file…,
     */
    @SerialName("converting")
    CONVERTING,

    /**
     * timeShift shooting: Performing timeShift shooting,
     */
    @SerialName("timeShift shooting")
    TIME_SHIFT_SHOOTING,

    /**
     * continuous shooting: Performing continuous shooting,
     */
    @SerialName("continuous shooting")
    CONTINUOUS_SHOOTING,

    /**
     * retrospective image recording: Waiting for retrospective video…
     */
    @SerialName("retrospective image recording")
    RETROSPECTIVE_IMAGE_RECORDING,
}

/**
 * Charging state
 */
@Serializable
internal enum class ChargingState {
    /**
     * battery charging
     */
    @SerialName("charging")
    CHARGING,

    /**
     * battery charged
     */
    @SerialName("charged")
    CHARGED,

    /**
     * battery disconnect
     */
    @SerialName("disconnect")
    DISCONNECT,
}

/**
 * shooting function
 */
@Serializable
internal enum class ShootingFunction {
    /**
     * normal
     */
    @SerialName("normal")
    NORMAL,

    /**
     * Self timer
     */
    @SerialName("selfTimer")
    SELF_TIMER,

    /**
     * My setting
     */
    @SerialName("mySetting")
    MY_SETTING,
}

/**
 * Microphone option
 */
@Serializable
internal enum class MicrophoneOption {
    /**
     * Auto
     */
    @SerialName("AUTO")
    AUTO,

    /**
     * Use the built-in microphone when recording video
     */
    @SerialName("Internal")
    INTERNAL,

    /**
     * Use the external microphone when recording video
     */
    @SerialName("External")
    EXTERNAL,
}

/**
 * Storage option
 */
@Serializable
internal enum class StorageOption {
    /**
     * Record to internal memory
     */
    @SerialName("IN")
    IN,

    /**
     * Record to SD card
     */
    @SerialName("SD")
    SD,
}

internal object CameraErrorSerializer :
    EnumIgnoreUnknownSerializer<CameraError>(CameraError.entries, CameraError.UNKNOWN)

/**
 * Camera error
 */
@Serializable(with = CameraErrorSerializer::class)
internal enum class CameraError {
    /**
     * Undefined value
     */
    @SerialName("UNKNOWN")
    UNKNOWN,

    /**
     * RICOH THETA X or later
     * 0x00000001: Insufficient memory
     */
    @SerialName("NO_MEMORY")
    NO_MEMORY,

    /**
     * 0x00000004: Maximum file number exceeded
     */
    @SerialName("FILE_NUMBER_OVER")
    FILE_NUMBER_OVER,

    /**
     * 0x00000008: Camera clock not set
     */
    @SerialName("NO_DATE_SETTING")
    NO_DATE_SETTING,

    /**
     * 0x00000010: Includes when the card is removed
     */
    @SerialName("READ_ERROR")
    READ_ERROR,

    /**
     * 0x00000020: Unsupported media (SDHC, etc.)
     */
    @SerialName("NOT_SUPPORTED_MEDIA_TYPE")
    NOT_SUPPORTED_MEDIA_TYPE,

    /**
     * 0x00000040: FAT32, etc.
     */
    @SerialName("NOT_SUPPORTED_FILE_SYSTEM")
    NOT_SUPPORTED_FILE_SYSTEM,

    /**
     * 0x00000100: Error warning while mounting
     */
    @SerialName("MEDIA_NOT_READY")
    MEDIA_NOT_READY,

    /**
     * 0x00000200: Battery level warning (firmware update)
     */
    @SerialName("NOT_ENOUGH_BATTERY")
    NOT_ENOUGH_BATTERY,

    /**
     * 0x00000400: Firmware file mismatch warning
     */
    @SerialName("INVALID_FILE")
    INVALID_FILE,

    /**
     * 0x00000800: Plug-in start warning (IoT technical standards compliance)
     */
    @SerialName("PLUGIN_BOOT_ERROR")
    PLUGIN_BOOT_ERROR,

    /**
     * 0x00001000: When performing continuous shooting by operating
     * the camera while executing <Delete object>, <Transfer firmware
     * file>, <Install plug-in> or <Uninstall plug-in> with the WebAPI
     * or MTP.
     */
    @SerialName("IN_PROGRESS_ERROR")
    IN_PROGRESS_ERROR,

    /**
     * 0x00001000: Battery inserted + WLAN ON + Video mode + 4K 60fps
     * / 5.7K 10fps / 5.7K 15fps / 5.7K 30fps / 8K 10fps
     */
    @SerialName("CANNOT_RECORDING")
    CANNOT_RECORDING,

    /**
     * 0x00002000: Battery inserted AND Specified battery level or
     * lower + WLAN ON + Video mode + 4K 30fps
     */
    @SerialName("CANNOT_RECORD_LOWBAT")
    CANNOT_RECORD_LOWBAT,

    /**
     * 0x00400000: Shooting hardware failure
     */
    @SerialName("CAPTURE_HW_FAILED")
    CAPTURE_HW_FAILED,

    /**
     * 0x00800000: Software error
     */
    @SerialName("CAPTURE_SW_FAILED")
    CAPTURE_SW_FAILED,

    /**
     * 0x08000000: Internal memory access error
     */
    @SerialName("INTERNAL_MEM_ACCESS_FAIL")
    INTERNAL_MEM_ACCESS_FAIL,

    /**
     * 0x20000000: Undefined error
     */
    @SerialName("UNEXPECTED_ERROR")
    UNEXPECTED_ERROR,

    /**
     * 0x40000000: Charging error
     */
    @SerialName("BATTERY_CHARGE_FAIL")
    BATTERY_CHARGE_FAIL,

    /**
     * 0x00100000: (Board) temperature warning
     */
    @SerialName("HIGH_TEMPERATURE_WARNING")
    HIGH_TEMPERATURE_WARNING,

    /**
     * 0x80000000: (Board) temperature error
     */
    @SerialName("HIGH_TEMPERATURE")
    HIGH_TEMPERATURE,

    /**
     * 0x00200000: Battery temperature error
     */
    @SerialName("BATTERY_HIGH_TEMPERATURE")
    BATTERY_HIGH_TEMPERATURE,

    // RICOH THETA Z1 or prior
    // 0x00000001: Insufficient memory
    //NO_MEMORY("NO_MEMORY")

    // 0x00000004: Maximum file number exceeded
    //FILE_NUMBER_OVER("FILE_NUMBER_OVER"),

    // 0x00000008: Camera clock not set
    //NO_DATE_SETTING("NO_DATE_SETTING"),

    /**
     * 0x00000010: Electronic compass error
     */
    @SerialName("COMPASS_CALIBRATION")
    COMPASS_CALIBRATION,

    /**
     * 0x00000010: Electronic compass error
     * for RICOH THETA X
     * Same as COMPASS_CALIBRATION and will be deleted.
     */
    @SerialName("ELECTRONIC_COMPASS_CALIBRATION")
    ELECTRONIC_COMPASS_CALIBRATION,

    // 0x00000800: Plug-in start warning (IoT technical standards
    // compliance)
    // PLUGIN_BOOT_ERROR("PLUGIN_BOOT_ERROR"),

    // 0x00400000: Shooting hardware failure
    // CAPTURE_HW_FAILED("CAPTURE_HW_FAILED"),

    // 0x08000000: Internal memory access error
    // INTERNAL_MEM_ACCESS_FAIL("INTERNAL_MEM_ACCESS_FAIL"),

    // 0x20000000: Undefined error
    // UNEXPECTED_ERROR("UNEXPECTED_ERROR"),

    // 0x40000000: Charging error
    // BATTERY_CHARGE_FAIL("BATTERY_CHARGE_FAIL"),

    // 0x80000000: (Board) temperature error
    // HIGH_TEMPERATURE("HIGH_TEMPERATURE"),

    // 0x00200000: Battery temperature error
    // BATTERY_HIGH_TEMPERATURE("BATTERY_HIGH_TEMPERATURE"),
}

/**
 * GPS information of state
 */
@Serializable
internal data class StateGpsInfo (
    /**
     * GPS information
     */
    val gpsInfo: GpsInfo? = null
)
