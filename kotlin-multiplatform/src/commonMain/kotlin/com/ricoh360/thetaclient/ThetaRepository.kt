package com.ricoh360.thetaclient

import com.ricoh360.thetaclient.capture.PhotoCapture
import com.ricoh360.thetaclient.capture.VideoCapture
import com.ricoh360.thetaclient.transferred.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

/**
 * Repository to handle Theta web APIs.
 *
 * @property endpoint URL of Theta web API endpoint.
 * @param config Configuration of initialize. If null, get from THETA.
 * @param timeout Timeout of HTTP call.
 */
class ThetaRepository internal constructor(val endpoint: String, config: Config? = null, timeout: Timeout? = null) {

    /**
     * Configuration of THETA
     */
    data class Config(
        var dateTime: String? = null,
        var language: LanguageEnum? = null,
        var offDelay: OffDelay? = null,
        var sleepDelay: SleepDelay? = null,
        var shutterVolume: Int? = null,

        /**
         * Authentication information used for client mode connections
         */
        var clientMode: DigestAuth? = null,
    ) {
        constructor() : this(
            dateTime = null,
            language = null,
            offDelay = null,
            sleepDelay = null,
            shutterVolume = null,
            clientMode = null,
        )

        /**
         * Set transferred.Options value to Config
         *
         * @param options transferred Options
         */
        internal fun setOptionsValue(options: com.ricoh360.thetaclient.transferred.Options) {
            dateTime = options.dateTimeZone
            language = options._language?.let { LanguageEnum.get(it) }
            offDelay = options.offDelay?.let { OffDelayEnum.get(it) }
            sleepDelay = options.sleepDelay?.let { SleepDelayEnum.get(it) }
            shutterVolume = options._shutterVolume
        }

        /**
         * Convert Config to transferred Options
         *
         * @return transferred Options
         */
        internal fun getOptions(): com.ricoh360.thetaclient.transferred.Options {
            return Options(
                dateTimeZone = dateTime,
                _language = language?.value,
                offDelay = offDelay?.sec,
                sleepDelay = sleepDelay?.sec,
                _shutterVolume = shutterVolume
            )
        }
    }

    /**
     * Timeout of HTTP call.
     */
    data class Timeout(
        /**
         * Specifies a time period (in milliseconds) in
         * which a client should establish a connection with a server.
         */
        val connectTimeout: Long = 20_000,

        /**
         * Specifies a time period (in milliseconds) required to process an HTTP call:
         * from sending a request to receiving first response bytes.
         * To disable this timeout, set its value to 0.
         */
        val requestTimeout: Long = 20_000,

        /**
         * Specifies a maximum time (in milliseconds) of inactivity between two data packets
         * when exchanging data with a server.
         */
        val socketTimeout: Long = 20_000
    )

    companion object {
        /**
         * Configuration of initialize
         */
        var initConfig: Config? = null
            internal set

        /**
         * Configuration of restore setting
         *
         * Obtained from THETA at initialization.
         */
        var restoreConfig: Config? = null
            internal set

        /**
         * Create ThetaRepository object.
         *
         * @param endpoint URL of Theta web API endpoint.
         * @param config Configuration of initialize. If null, get from THETA.
         * @param timeout Timeout of HTTP call.
         * @exception ThetaWebApiException If an error occurs in THETA.
         * @exception NotConnectedException
         * @exception ThetaUnauthorizedException If an authentication　error occurs in client mode.
         */
        @Throws(Throwable::class)
        suspend fun newInstance(endpoint: String, config: Config? = null, timeout: Timeout? = null): ThetaRepository {
            val thetaRepository = ThetaRepository(endpoint, config, timeout)
            thetaRepository.init()
            return thetaRepository
        }
    }

    init {
        timeout?.let { ApiClient.timeout = it } ?: run { ApiClient.timeout = Timeout() }
        config?.clientMode?.let { ApiClient.digestAuth = it } ?: run { ApiClient.digestAuth = null }
        initConfig = config
    }

    /**
     * Initialize ThetaRepository
     *
     * @exception ThetaWebApiException If an error occurs in THETA.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    internal suspend fun init() {
        try {
            val info = ThetaApi.callInfoApi(endpoint)
            cameraModel = info.model
            if (checkChangedApi2(info.model, info.firmwareVersion)) {
                val state = ThetaApi.callStateApi(endpoint)
                if (state.state._apiVersion == 1) {
                    // Start session and change api version
                    val startSession = ThetaApi.callStartSessionCommand(endpoint)
                    startSession.error?.let {
                        throw ThetaWebApiException(it.message)
                    }
                    ThetaApi.callSetOptionsCommand(
                        endpoint,
                        SetOptionsParams(
                            startSession.results!!.sessionId,
                            Options(clientVersion = 2)
                        )
                    ).error?.let {
                        throw ThetaWebApiException(it.message)
                    }
                }
            }
            restoreConfig = Config()
            getConfigSetting(restoreConfig!!, cameraModel!!)
            initConfig?.let { setConfigSettings(it) }
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            when (e.response.status) {
                HttpStatusCode.Unauthorized -> {
                    throw ThetaUnauthorizedException(e.message ?: e.toString())
                }
                else -> {
                    throw ThetaWebApiException.create(e)
                }
            }
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Restore setting to THETA
     *
     * @exception ThetaWebApiException If an error occurs in THETA.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun restoreSettings() {
        try {
            setConfigSettings(restoreConfig!!)
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Set configuration setting to THETA
     *
     * @param config configuration
     * @exception ThetaWebApiException If an error occurs in THETA.
     */
    @Throws(Throwable::class)
    internal suspend fun setConfigSettings(config: Config) {
        val options = config.getOptions()
        ThetaModel.get(cameraModel)?.let {
            if (it == ThetaModel.THETA_S || it == ThetaModel.THETA_SC || it == ThetaModel.THETA_SC2) {
                // _language is THETA V or later
                options._language = null
            }
        }
        options.dateTimeZone?.let {
            val datetimeOptions = com.ricoh360.thetaclient.transferred.Options(
                dateTimeZone = it
            )
            ThetaApi.callSetOptionsCommand(endpoint, SetOptionsParams(options = datetimeOptions)).error?.let {
                throw ThetaWebApiException(it.message)
            }
        }
        options.dateTimeZone = null
        if (options != com.ricoh360.thetaclient.transferred.Options()) {
            ThetaApi.callSetOptionsCommand(endpoint, SetOptionsParams(options = options)).error?.let {
                throw ThetaWebApiException(it.message)
            }
        }
    }

    /**
     * Get configuration from THETA
     *
     * @param config Configuration
     * @param model Camera model name
     * @exception ThetaWebApiException If an error occurs in THETA.
     */
    @Throws(Throwable::class)
    internal suspend fun getConfigSetting(config: Config, model: String) {
        val optionNameList = listOfNotNull(
            OptionNameEnum.DateTimeZone.value,
            // For THETA V or later
            ThetaModel.get(model)
                ?.let { if (it != ThetaModel.THETA_S && it != ThetaModel.THETA_SC && it != ThetaModel.THETA_SC2) OptionNameEnum.Language.value else null },
            OptionNameEnum.OffDelay.value,
            OptionNameEnum.SleepDelay.value,
            OptionNameEnum.ShutterVolume.value
        )
        val response = ThetaApi.callGetOptionsCommand(endpoint, GetOptionsParams(optionNameList))
        response.error?.let {
            throw ThetaWebApiException(it.message)
        }
        response.results?.let { config.setOptionsValue(it.options) }
    }

    /**
     *
     * @param model Camera model
     * @return ok
     * @exception ThetaWebApiException If an error occurs in THETA.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    private fun checkChangedApi2(model: String, firmwareVersion: String): Boolean {
        val thetaModel = ThetaModel.get(model)
        return if (thetaModel == ThetaModel.THETA_S) {
            if (firmwareVersion < "01.62") {
                throw ThetaWebApiException("Unsupported RICOH THETA S firmware version $firmwareVersion")
            }
            true
        } else {
            thetaModel == ThetaModel.THETA_SC
        }
    }

    /**
     * Camera model.
     */
    var cameraModel: String? = null
        internal set

    /**
     * Support THETA model
     */
    enum class ThetaModel(val value: String) {
        /**
         * THETA S
         */
        THETA_S("RICOH THETA S"),

        /**
         * THETA SC
         */
        THETA_SC("RICOH THETA SC"),

        /**
         * THETA V
         */
        THETA_V("RICOH THETA V"),

        /**
         * THETA Z1
         */
        THETA_Z1("RICOH THETA Z1"),

        /**
         * THETA X
         */
        THETA_X("RICOH THETA X"),

        /**
         * THETA SC2
         */
        THETA_SC2("RICOH THETA SC2");

        companion object {
            /**
             * Get THETA model
             *
             * @param model Camera model
             * @return ThetaModel
             */
            fun get(model: String?): ThetaModel? {
                return values().firstOrNull {
                    it.value == model
                }
            }
        }
    }

    /**
     * Get basic information about Theta.
     *
     * @return Static attributes of Theta.
     * @exception ThetaWebApiException If an error occurs in THETA.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun getThetaInfo(): ThetaInfo {
        try {
            val response = ThetaApi.callInfoApi(endpoint)
            cameraModel = response.model
            return ThetaInfo(response)
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Get current state of Theta.
     *
     * @return Mutable values representing Theta status.
     * @exception ThetaWebApiException If an error occurs in THETA.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun getThetaState(): ThetaState {
        try {
            val response = ThetaApi.callStateApi(endpoint)
            return ThetaState(response)
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Acquires a list of still image files and movie files.
     *
     * @param[fileType] File types to acquire.
     * @param[startPosition] Position to start acquiring the file list.
     * If a number larger than the number of existing files is specified, a null list is acquired.
     * Default is the top of the list.
     * @param[entryCount] Number of still image and movie files to acquire.
     * If the number of existing files is smaller than the specified number of files, all available files are only acquired.
     * @param[storage] Specifies the storage. If omitted, return current storage. (RICOH THETA X Version 2.00.0 or later)
     * @return A list of file information and number of totalEntries.
     * see [camera.listFiles](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.list_files.md).
     * @exception ThetaWebApiException If an error occurs in THETA.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun listFiles(
        fileType: FileTypeEnum,
        startPosition: Int = 0,
        entryCount: Int,
        storage: StorageEnum? = null,
    ): ThetaFiles {
        try {
            val params = ListFilesParams(
                fileType = fileType.value,
                startPosition = startPosition,
                entryCount = entryCount,
                _storage = storage?.value,
            )
            val listFilesResponse = ThetaApi.callListFilesCommand(endpoint, params)
            listFilesResponse.error?.let {
                throw ThetaWebApiException(it.message)
            }
            val fileList = mutableListOf<FileInfo>()
            listFilesResponse.results!!.entries.forEach {
                fileList.add(FileInfo(it))
            }
            return ThetaFiles(fileList, listFilesResponse.results!!.totalEntries)
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Acquires a list of still image files and movie files.
     *
     * @param[fileType] File types to acquire.
     * @param[startPosition] Position to start acquiring the file list.
     * If a number larger than the number of existing files is specified, a null list is acquired.
     * Default is the top of the list.
     * @param[entryCount] Number of still image and movie files to acquire.
     * If the number of existing files is smaller than the specified number of files, all available files are only acquired.
     * @return A list of file information and number of totalEntries.
     * see [camera.listFiles](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.list_files.md).
     * @exception ThetaWebApiException If an error occurs in THETA.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun listFiles(
        fileType: FileTypeEnum,
        startPosition: Int = 0,
        entryCount: Int,
    ): ThetaFiles {
        return listFiles(fileType, startPosition, entryCount, null)
    }

    /**
     * Data about files in Theta.
     *
     * @property fileList A list of file information
     * @property totalEntries number of totalEntries
     * see [camera.listFiles](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.list_files.md).
     */
    data class ThetaFiles(
        val fileList: List<FileInfo>,
        val totalEntries: Int,
    )

    /**
     * Delete files in Theta.
     *
     * @param[fileUrls] URLs of the file to be deleted.
     * @exception ThetaWebApiException Some of [fileUrls] don't exist.  All specified files cannot be deleted.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun deleteFiles(fileUrls: List<String>) {
        try {
            val params = DeleteParams(fileUrls)
            val deleteFilesResponse = ThetaApi.callDeleteCommand(endpoint, params)
            deleteFilesResponse.error?.let {
                throw ThetaWebApiException(it.message)
            }
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Delete all files in Theta.
     * @exception ThetaWebApiException If an error occurs in THETA.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun deleteAllFiles() {
        deleteFiles(listOf("all"))
    }

    /**
     * Delete all image files in Theta.
     * @exception ThetaWebApiException If an error occurs in THETA.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun deleteAllImageFiles() {
        deleteFiles(listOf("image"))
    }

    /**
     * Delete all video files in Theta.
     * @exception ThetaWebApiException If an error occurs in THETA.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun deleteAllVideoFiles() {
        deleteFiles(listOf("video"))
    }

    /**
     * Acquires the properties and property support specifications for shooting, the camera, etc.
     *
     * Refer to the [options category](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/options.md)
     * of API v2.1 reference for details on properties that can be acquired.
     *
     * @param optionNames List of [OptionNameEnum].
     * @return Options acquired
     * @exception ThetaWebApiException When an invalid option is specified.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun getOptions(optionNames: List<OptionNameEnum>): Options {
        try {
            val names = optionNames.map {
                it.value
            }
            val params = GetOptionsParams(names.distinct())
            val getOptionsResponse = ThetaApi.callGetOptionsCommand(endpoint, params)
            getOptionsResponse.error?.let {
                throw ThetaWebApiException(it.message)
            }
            return Options(getOptionsResponse.results!!.options)
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Property settings for shooting, the camera, etc.
     *
     * Check the properties that can be set and specifications by the API v2.1 reference options
     * category or [camera.getOptions](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/options.md).
     *
     * @param options Camera setting options.
     * @exception ThetaWebApiException When an invalid option is specified.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun setOptions(options: Options) {
        try {
            val params = SetOptionsParams(options.toOptions())
            ThetaApi.callSetOptionsCommand(endpoint, params).error?.let {
                throw ThetaWebApiException(it.message)
            }
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Camera setting options name.
     * [options name](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/options.md)
     */
    enum class OptionNameEnum(val value: String, val valueType: KClass<*>) {
        /**
         * Option name
         * aperture
         */
        Aperture("aperture", ApertureEnum::class),

        /**
         * Option name
         * _bluetoothPower
         */
        BluetoothPower("_bluetoothPower", BluetoothPowerEnum::class),

        /**
         * Option name
         * _cameraControlSource
         */
        CameraControlSource("_cameraControlSource", CameraControlSourceEnum::class),

        /**
         * Option name
         * _cameraMode
         */
        CameraMode("_cameraMode", CameraModeEnum::class),

        /**
         * Option name
         * captureMode
         */
        CaptureMode("captureMode", CaptureModeEnum::class),

        /**
         * Option name
         * _colorTemperature
         */
        ColorTemperature("_colorTemperature", Int::class),

        /**
         * Option name
         * dateTimeZone
         */
        DateTimeZone("dateTimeZone", String::class),

        /**
         * Option name
         * exposureCompensation
         */
        ExposureCompensation("exposureCompensation", ExposureCompensationEnum::class),

        /**
         * Option name
         * exposureDelay
         */
        ExposureDelay("exposureDelay", ExposureDelayEnum::class),

        /**
         * Option name
         * exposureProgram
         */
        ExposureProgram("exposureProgram", ExposureProgramEnum::class),

        /**
         * Option name
         * fileFormat
         */
        FileFormat("fileFormat", FileFormatEnum::class),

        /**
         * Option name
         * _filter
         */
        Filter("_filter", FilterEnum::class),

        /**
         * Option name
         * gpsInfo
         */
        GpsInfo("gpsInfo", ThetaRepository.GpsInfo::class),

        /**
         * Option name
         * _gpsTagRecording
         *
         * For RICOH THETA X or later
         */
        IsGpsOn("_gpsTagRecording", Boolean::class),

        /**
         * Option name
         * iso
         */
        Iso("iso", IsoEnum::class),

        /**
         * Option name
         * isoAutoHighLimit
         */
        IsoAutoHighLimit("isoAutoHighLimit", IsoAutoHighLimitEnum::class),

        /**
         * Option name
         * _language
         */
        Language("_language", LanguageEnum::class),

        /**
         * Option name
         * _maxRecordableTime
         */
        MaxRecordableTime("_maxRecordableTime", MaxRecordableTimeEnum::class),

        /**
         * Option name
         * _networkType
         */
        NetworkType("_networkType", NetworkTypeEnum::class),

        /**
         * Option name
         * offDelay
         */
        OffDelay("offDelay", ThetaRepository.OffDelay::class),

        /**
         * Option name
         * Password
         */
        Password("_password", String::class),

        /**
         * Option name
         * _powerSaving
         */
        PowerSaving("_powerSaving", PowerSavingEnum::class),

        /**
         * Option name
         * previewFormat
         */
        PreviewFormat("previewFormat", PreviewFormatEnum::class),

        /**
         * Option name
         * _proxy
         */
        Proxy("_proxy", ThetaRepository.Proxy::class),

        /**
         * Option name
         * remainingPictures
         */
        RemainingPictures("remainingPictures", Int::class),

        /**
         * Option name
         * remainingVideoSeconds
         */
        RemainingVideoSeconds("remainingVideoSeconds", Int::class),

        /**
         * Option name
         * remainingSpace
         */
        RemainingSpace("remainingSpace", Long::class),

        /**
         * Option name
         * sleepDelay
         */
        SleepDelay("sleepDelay", ThetaRepository.SleepDelay::class),

        /**
         * Option name
         * totalSpace
         */
        TotalSpace("totalSpace", Long::class),

        /**
         * Option name
         * _shootingMethod
         */
        ShootingMethod("_shootingMethod", ShootingMethodEnum::class),

        /**
         * Option name
         * shutterSpeed
         */
        ShutterSpeed("shutterSpeed", ShutterSpeedEnum::class),

        /**
         * Option name
         * _shutterVolume
         */
        ShutterVolume("_shutterVolume", Int::class),

        /**
         *  Option name
         *  _username
         */
        Username("_username", String::class),

        /**
         * Option name
         * whiteBalance
         */
        WhiteBalance("whiteBalance", WhiteBalanceEnum::class),

        /**
         * Option name
         * _whiteBalanceAutoStrength
         */
        WhiteBalanceAutoStrength("_whiteBalanceAutoStrength", WhiteBalanceAutoStrengthEnum::class),

        /**
         * Option name
         * _wlanFrequency
         */
        WlanFrequency("_wlanFrequency", WlanFrequencyEnum::class),
    }

    /**
     * Camera setting options.
     * Refer to the [options category](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/options.md)
     */
    data class Options(
        /**
         * Aperture value.
         */
        var aperture: ApertureEnum? = null,

        /**
         * Bluetooth power.
         */
        var bluetoothPower: BluetoothPowerEnum? = null,

        /**
         * @see CameraControlSourceEnum
         */
        var cameraControlSource: CameraControlSourceEnum? = null,

        /**
         * @see CameraModeEnum
         */
        var cameraMode: CameraModeEnum? = null,

        /**
         * Shooting mode.
         */
        var captureMode: CaptureModeEnum? = null,

        /**
         * Color temperature of the camera (Kelvin).
         *
         * It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
         * Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
         *
         * Support value
         * 2500 to 10000. In 100-Kelvin units.
         */
        var colorTemperature: Int? = null,

        /**
         * Current system time of RICOH THETA. Setting another options will result in an error.
         *
         * With RICOH THETA X camera.setOptions can be changed only when Date/time setting is AUTO in menu UI.
         *
         * Time format
         * YYYY:MM:DD hh:mm:ss+(-)hh:mm
         * hh is in 24-hour time, +(-)hh:mm is the time zone.
         * e.g. 2014:05:18 01:04:29+08:00
         */
        var dateTimeZone: String? = null,

        /**
         * Exposure compensation (EV).
         *
         * It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
         * Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
         */
        var exposureCompensation: ExposureCompensationEnum? = null,

        /**
         * Operating time (sec.) of the self-timer.
         *
         * If exposureDelay is enabled, self-timer is used by shooting.
         * If exposureDelay is disabled, use _latestEnabledExposureDelayTime to
         * get the operating time of the self-timer stored in the camera.
         */
        var exposureDelay: ExposureDelayEnum? = null,

        /**
         * Exposure program. The exposure settings that take priority can be selected.
         *
         * It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
         * Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
         */
        var exposureProgram: ExposureProgramEnum? = null,

        /**
         * Image format used in shooting.
         *
         * The supported value depends on the shooting mode [captureMode].
         */
        var fileFormat: FileFormatEnum? = null,

        /**
         * Image processing filter.
         *
         * Configured the filter will be applied while in still image shooting mode.
         * However, it is disabled during interval shooting, interval composite group shooting,
         * multi bracket shooting or continuous shooting.
         *
         * When filter is enabled, it takes priority over the exposure program [exposureProgram].
         * Also, when filter is enabled, the exposure program is set to the Normal program.
         *
         * The condition below will result in an error.
         *  [fileFormat] is raw+ and _filter is Noise reduction, HDR or Handheld HDR
         *  shootingMethod is except for Normal shooting and [filter] is enabled
         *  Access during video capture mode
         */
        var filter: FilterEnum? = null,

        /**
         * GPS location information.
         *
         * In order to append the location information, this property should be specified by the client.
         */
        var gpsInfo: GpsInfo? = null,

        /**
         * Turns position information assigning ON/OFF.
         * For THETA X
         */
        var isGpsOn: Boolean? = null,

        /**
         * Turns position information assigning ON/OFF.
         *
         * It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
         * Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
         *
         * When the exposure program [exposureProgram] is set to Manual or ISO Priority
         */
        var iso: IsoEnum? = null,

        /**
         * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         */
        var isoAutoHighLimit: IsoAutoHighLimitEnum? = null,

        /**
         * Language used in camera OS.
         */
        var language: LanguageEnum? = null,

        /**
         * Maximum recordable time (in seconds) of the camera.
         */
        var maxRecordableTime: MaxRecordableTimeEnum? = null,

        /**
         * Network type of the camera.
         */
        var networkType: NetworkTypeEnum? = null,

        /**
         * Length of standby time before the camera automatically powers OFF.
         *
         * Specify [OffDelayEnum] or [OffDelaySec]
         */
        var offDelay: OffDelay? = null,

        /**
         * Password used for digest authentication when _networkType is set to client mode.
         * Can be set by camera.setOptions during direct mode.
         */
        var password: String? = null,

        /**
         * Power saving mode.
         * Only for Theta X.
         */
        var powerSaving: PowerSavingEnum? = null,

        /**
         * Format of live view.
         */
        var previewFormat: PreviewFormatEnum? = null,

        /**
         * @see Proxy
         */
        var proxy: Proxy? = null,

        /**
         * The estimated remaining number of shots for the current shooting settings.
         */
        var remainingPictures: Int? = null,

        /**
         * The estimated remaining shooting time (sec.) for the current video shooting settings.
         */
        var remainingVideoSeconds: Int? = null,

        /**
         * Remaining usable storage space (byte).
         */
        var remainingSpace: Long? = null,

        /**
         * Shooting method for My Settings mode. In RICOH THETA X, it is used outside of MySetting.
         * Can be acquired and set only when in the Still image shooting mode and _function is the My Settings shooting function.
         * Changing _function initializes the setting details to Normal shooting.
         */
        var shootingMethod: ShootingMethodEnum? = null,

        /**
         * Shutter speed (sec).
         *
         * It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
         * Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
         */
        var shutterSpeed: ShutterSpeedEnum? = null,

        /**
         * Length of standby time before the camera enters the sleep mode.
         */
        var sleepDelay: SleepDelay? = null,

        /**
         * Total storage space (byte).
         */
        var totalSpace: Long? = null,

        /**
         * Shutter volume.
         *
         * Support value
         * 0: Minimum volume (minShutterVolume)
         * 100: Maximum volume (maxShutterVolume)
         */
        var shutterVolume: Int? = null,

        /**
         * User name used for digest authentication when _networkType is set to client mode.
         * Can be set by camera.setOptions during direct mode.
         */
        var username: String? = null,

        /**
         * White balance.
         *
         * It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
         * Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
         */
        var whiteBalance: WhiteBalanceEnum? = null,

        /**
         * @see WhiteBalanceAutoStrengthEnum
         */
        var whiteBalanceAutoStrength: WhiteBalanceAutoStrengthEnum? = null,

        /**
         * Wireless LAN frequency of the camera
         *
         * For RICOH THETA X, Z1 and V.
         */
        var wlanFrequency: WlanFrequencyEnum? = null,
    ) {
        constructor() : this(
            aperture = null,
            bluetoothPower = null,
            cameraControlSource = null,
            cameraMode = null,
            captureMode = null,
            colorTemperature = null,
            dateTimeZone = null,
            exposureCompensation = null,
            exposureDelay = null,
            exposureProgram = null,
            fileFormat = null,
            filter = null,
            gpsInfo = null,
            isGpsOn = null,
            iso = null,
            isoAutoHighLimit = null,
            language = null,
            maxRecordableTime = null,
            networkType = null,
            offDelay = null,
            password = null,
            powerSaving = null,
            previewFormat = null,
            proxy = null,
            shootingMethod = null,
            shutterSpeed = null,
            sleepDelay = null,
            remainingPictures = null,
            remainingVideoSeconds = null,
            remainingSpace = null,
            totalSpace = null,
            shutterVolume = null,
            username = null,
            whiteBalance = null,
            whiteBalanceAutoStrength = null,
            wlanFrequency = null,
        )

        constructor(options: com.ricoh360.thetaclient.transferred.Options) : this(
            aperture = options.aperture?.let { ApertureEnum.get(it) },
            bluetoothPower = options._bluetoothPower?.let { BluetoothPowerEnum.get(it) },
            cameraControlSource = options._cameraControlSource?.let { CameraControlSourceEnum.get(it) },
            cameraMode = options._cameraMode?.let { CameraModeEnum.get(it) },
            captureMode = options.captureMode?.let { CaptureModeEnum.get(it) },
            colorTemperature = options._colorTemperature,
            dateTimeZone = options.dateTimeZone,
            exposureCompensation = options.exposureCompensation?.let {
                ExposureCompensationEnum.get(
                    it
                )
            },
            exposureDelay = options.exposureDelay?.let { ExposureDelayEnum.get(it) },
            exposureProgram = options.exposureProgram?.let { ExposureProgramEnum.get(it) },
            fileFormat = options.fileFormat?.let { FileFormatEnum.get(it) },
            filter = options._filter?.let { FilterEnum.get(it) },
            gpsInfo = options.gpsInfo?.let { GpsInfo(it) },
            isGpsOn = options._gpsTagRecording?.let { it == GpsTagRecording.ON },
            iso = options.iso?.let { IsoEnum.get(it) },
            isoAutoHighLimit = options.isoAutoHighLimit?.let { IsoAutoHighLimitEnum.get(it) },
            language = options._language?.let { LanguageEnum.get(it) },
            maxRecordableTime = options._maxRecordableTime?.let { MaxRecordableTimeEnum.get(it) },
            networkType = options._networkType?.let { NetworkTypeEnum.get(it) },
            offDelay = options.offDelay?.let { OffDelayEnum.get(it) },
            password = options._password,
            powerSaving = options._powerSaving?.let { PowerSavingEnum.get(it) },
            previewFormat = options.previewFormat?.let { PreviewFormatEnum.get(it) },
            proxy = options._proxy?.let { Proxy(it) },
            shootingMethod = options._shootingMethod?.let { ShootingMethodEnum.get(it) },
            shutterSpeed = options.shutterSpeed?.let { ShutterSpeedEnum.get(it) },
            sleepDelay = options.sleepDelay?.let { SleepDelayEnum.get(it) },
            remainingPictures = options.remainingPictures,
            remainingVideoSeconds = options.remainingVideoSeconds,
            remainingSpace = options.remainingSpace,
            totalSpace = options.totalSpace,
            shutterVolume = options._shutterVolume,
            username = options._username,
            whiteBalance = options.whiteBalance?.let { WhiteBalanceEnum.get(it) },
            whiteBalanceAutoStrength = options._whiteBalanceAutoStrength?.let { WhiteBalanceAutoStrengthEnum.get(it) },
            wlanFrequency = options._wlanFrequency?.let { WlanFrequencyEnum.get(it) },
        )

        /**
         * Convert transferred.Options
         * @return transferred.Options
         */
        fun toOptions(): com.ricoh360.thetaclient.transferred.Options {
            return Options(
                aperture = aperture?.value,
                _bluetoothPower = bluetoothPower?.value,
                _cameraControlSource = cameraControlSource?.value,
                _cameraMode = cameraMode?.value,
                captureMode = captureMode?.value,
                _colorTemperature = colorTemperature,
                dateTimeZone = dateTimeZone,
                exposureCompensation = exposureCompensation?.value,
                exposureDelay = exposureDelay?.sec,
                exposureProgram = exposureProgram?.value,
                fileFormat = fileFormat?.toMediaFileFormat(),
                _filter = filter?.filter,
                gpsInfo = gpsInfo?.toTransferredGpsInfo(),
                _gpsTagRecording = isGpsOn?.let { if (it) GpsTagRecording.ON else GpsTagRecording.OFF },
                iso = iso?.value,
                isoAutoHighLimit = isoAutoHighLimit?.value,
                _language = language?.value,
                _maxRecordableTime = maxRecordableTime?.sec,
                _networkType = networkType?.value,
                offDelay = offDelay?.sec,
                _password = password,
                _powerSaving = powerSaving?.value,
                previewFormat = previewFormat?.toPreviewFormat(),
                _proxy = proxy?.toTransferredProxy(),
                sleepDelay = sleepDelay?.sec,
                remainingPictures = remainingPictures,
                remainingVideoSeconds = remainingVideoSeconds,
                remainingSpace = remainingSpace,
                totalSpace = totalSpace,
                _shootingMethod = shootingMethod?.value,
                shutterSpeed = shutterSpeed?.value,
                _shutterVolume = shutterVolume,
                _username = username,
                whiteBalance = whiteBalance?.value,
                _whiteBalanceAutoStrength = whiteBalanceAutoStrength?.value,
                _wlanFrequency = wlanFrequency?.value,
            )
        }

        /**
         * Get Option value.
         *
         * @param name Option name.
         * @return Setting value. Requires type definition.
         * @exception ClassCastException When an invalid type is specified.
         */
        @Suppress("IMPLICIT_CAST_TO_ANY")
        @Throws(Throwable::class)
        fun <T> getValue(name: OptionNameEnum): T? {
            @Suppress("UNCHECKED_CAST")
            return when (name) {
                OptionNameEnum.Aperture -> aperture
                OptionNameEnum.BluetoothPower -> bluetoothPower
                OptionNameEnum.CameraControlSource -> cameraControlSource
                OptionNameEnum.CameraMode -> cameraMode
                OptionNameEnum.CaptureMode -> captureMode
                OptionNameEnum.ColorTemperature -> colorTemperature
                OptionNameEnum.DateTimeZone -> dateTimeZone
                OptionNameEnum.ExposureCompensation -> exposureCompensation
                OptionNameEnum.ExposureDelay -> exposureDelay
                OptionNameEnum.ExposureProgram -> exposureProgram
                OptionNameEnum.FileFormat -> fileFormat
                OptionNameEnum.Filter -> filter
                OptionNameEnum.GpsInfo -> gpsInfo
                OptionNameEnum.IsGpsOn -> isGpsOn
                OptionNameEnum.Iso -> iso
                OptionNameEnum.IsoAutoHighLimit -> isoAutoHighLimit
                OptionNameEnum.Language -> language
                OptionNameEnum.MaxRecordableTime -> maxRecordableTime
                OptionNameEnum.NetworkType -> networkType
                OptionNameEnum.OffDelay -> offDelay
                OptionNameEnum.Password -> password
                OptionNameEnum.PowerSaving -> powerSaving
                OptionNameEnum.PreviewFormat -> previewFormat
                OptionNameEnum.Proxy -> proxy
                OptionNameEnum.SleepDelay -> sleepDelay
                OptionNameEnum.RemainingPictures -> remainingPictures
                OptionNameEnum.RemainingVideoSeconds -> remainingVideoSeconds
                OptionNameEnum.RemainingSpace -> remainingSpace
                OptionNameEnum.TotalSpace -> totalSpace
                OptionNameEnum.ShootingMethod -> shootingMethod
                OptionNameEnum.ShutterSpeed -> shutterSpeed
                OptionNameEnum.ShutterVolume -> shutterVolume
                OptionNameEnum.Username -> username
                OptionNameEnum.WhiteBalance -> whiteBalance
                OptionNameEnum.WhiteBalanceAutoStrength -> whiteBalanceAutoStrength
                OptionNameEnum.WlanFrequency -> wlanFrequency
            } as T
        }

        /**
         * Set option value.
         *
         * @param name Option name.
         * @param value Option value.
         * @exception ThetaWebApiException When an invalid option is specified.
         */
        @Throws(Throwable::class)
        fun setValue(name: OptionNameEnum, value: Any) {
            if (!name.valueType.isInstance(value)) {
                throw ThetaWebApiException("Invalid value type")
            }
            when (name) {
                OptionNameEnum.Aperture -> aperture = value as ApertureEnum
                OptionNameEnum.BluetoothPower -> bluetoothPower = value as BluetoothPowerEnum
                OptionNameEnum.CameraControlSource -> cameraControlSource = value as CameraControlSourceEnum
                OptionNameEnum.CameraMode -> cameraMode = value as CameraModeEnum
                OptionNameEnum.CaptureMode -> captureMode = value as CaptureModeEnum
                OptionNameEnum.ColorTemperature -> colorTemperature = value as Int
                OptionNameEnum.DateTimeZone -> dateTimeZone = value as String
                OptionNameEnum.ExposureCompensation -> exposureCompensation = value as ExposureCompensationEnum
                OptionNameEnum.ExposureDelay -> exposureDelay = value as ExposureDelayEnum
                OptionNameEnum.ExposureProgram -> exposureProgram = value as ExposureProgramEnum
                OptionNameEnum.FileFormat -> fileFormat = value as FileFormatEnum
                OptionNameEnum.Filter -> filter = value as FilterEnum
                OptionNameEnum.GpsInfo -> gpsInfo = value as GpsInfo
                OptionNameEnum.IsGpsOn -> isGpsOn = value as Boolean
                OptionNameEnum.Iso -> iso = value as IsoEnum
                OptionNameEnum.IsoAutoHighLimit -> isoAutoHighLimit = value as IsoAutoHighLimitEnum
                OptionNameEnum.Language -> language = value as LanguageEnum
                OptionNameEnum.MaxRecordableTime -> maxRecordableTime = value as MaxRecordableTimeEnum
                OptionNameEnum.NetworkType -> networkType = value as NetworkTypeEnum
                OptionNameEnum.OffDelay -> offDelay = value as OffDelay
                OptionNameEnum.Password -> password = value as String
                OptionNameEnum.PowerSaving -> powerSaving = value as PowerSavingEnum
                OptionNameEnum.PreviewFormat -> previewFormat = value as PreviewFormatEnum
                OptionNameEnum.Proxy -> proxy = value as Proxy
                OptionNameEnum.ShootingMethod -> shootingMethod = value as ShootingMethodEnum
                OptionNameEnum.ShutterSpeed -> shutterSpeed = value as ShutterSpeedEnum
                OptionNameEnum.SleepDelay -> sleepDelay = value as SleepDelay
                OptionNameEnum.RemainingPictures -> remainingPictures = value as Int
                OptionNameEnum.RemainingVideoSeconds -> remainingVideoSeconds = value as Int
                OptionNameEnum.RemainingSpace -> remainingSpace = value as Long
                OptionNameEnum.TotalSpace -> totalSpace = value as Long
                OptionNameEnum.ShutterVolume -> shutterVolume = value as Int
                OptionNameEnum.Username -> username = value as String
                OptionNameEnum.WhiteBalance -> whiteBalance = value as WhiteBalanceEnum
                OptionNameEnum.WhiteBalanceAutoStrength -> whiteBalanceAutoStrength = value as WhiteBalanceAutoStrengthEnum
                OptionNameEnum.WlanFrequency -> wlanFrequency = value as WlanFrequencyEnum
            }
        }
    }

    /**
     * Aperture value.
     */
    enum class ApertureEnum(val value: Float) {
        /**
         * Aperture value.
         * AUTO(0)
         */
        APERTURE_AUTO(0.0f),

        /**
         * Aperture value.
         * 2.0F
         *
         * RICOH THETA V or prior
         */
        APERTURE_2_0(2.0f),

        /**
         * Aperture value.
         * 2.1F
         *
         * RICOH THETA Z1 and the exposure program (exposureProgram) is set to Manual or Aperture Priority
         */
        APERTURE_2_1(2.1f),

        /**
         * Aperture value.
         * 2.4F
         *
         * RICOH THETA X or later
         */
        APERTURE_2_4(2.4f),

        /**
         * Aperture value.
         * 3.5F
         *
         * RICOH THETA Z1 and the exposure program (exposureProgram) is set to Manual or Aperture Priority
         */
        APERTURE_3_5(3.5f),

        /**
         * Aperture value.
         * 5.6F
         *
         * RICOH THETA Z1 and the exposure program (exposureProgram) is set to Manual or Aperture Priority
         */
        APERTURE_5_6(5.6f);

        companion object {
            /**
             * Convert value to ApertureEnum
             *
             * @param value Aperture value.
             * @return ApertureEnum
             */
            fun get(value: Float): ApertureEnum? {
                return values().firstOrNull { it.value == value }
            }
        }
    }

    /**
     * bluetooth power.
     */
    enum class BluetoothPowerEnum(val value: BluetoothPower) {
        /**
         * bluetooth ON
         */
        ON(BluetoothPower.ON),

        /**
         * bluetooth OFF
         */
        OFF(BluetoothPower.OFF);

        companion object {
            /**
             * Convert BluetoothPower to BluetoothPowerEnum
             *
             * @param value bluetooth power.
             * @return BluetoothPowerEnum
             */
            fun get(value: BluetoothPower): BluetoothPowerEnum? {
                return values().firstOrNull { it.value == value }
            }
        }
    }

    /**
     * camera control source
     * Sets whether to lock/unlock the camera UI.
     * The current setting can be acquired by camera.getOptions, and it can be changed by camera.setOptions.
     *
     * For RICOH THETA X
     */
    enum class CameraControlSourceEnum(val value: CameraControlSource) {
        /**
         * Operation is possible with the camera. Locks the smartphone
         * application UI (supported app only).
         */
        CAMERA(CameraControlSource.CAMERA),

        /**
         * Operation is possible with the smartphone application. Locks
         * the UI on the shooting screen on the camera.
         */
        APP(CameraControlSource.APP);

        companion object {
            /**
             * Convert CameraControlSource to CameraControlSourceEnum
             *
             * @param value camera control source
             * @return CameraControlSourceEnum
             */
            fun get(value: CameraControlSource): CameraControlSourceEnum? {
                return values().firstOrNull { it.value == value }
            }
        }
    }

    /**
     * Camera mode.
     * The current setting can be acquired by camera.getOptions, and it can be changed by camera.setOptions.
     *
     * For RICOH THETA X
     */
    enum class CameraModeEnum(val value: CameraMode) {
        /**
         * shooting screen
         */
        CAPTURE(CameraMode.CAPTURE),

        /**
         * playback screen
         */
        PLAYBACK(CameraMode.PLAYBACK),

        /**
         * shooting setting screen
         */
        SETTING(CameraMode.SETTING),

        /**
         * plugin selection screen
         */
        PLUGIN(CameraMode.PLUGIN);

        companion object {
            /**
             * Convert CameraMode to CameraModeEnum
             *
             * @param value Camera mode.
             * @return CameraModeEnum
             */
            fun get(value: CameraMode): CameraModeEnum? {
                return values().firstOrNull { it.value == value }
            }
        }
    }

    /**
     * Shooting mode.
     */
    enum class CaptureModeEnum(val value: CaptureMode) {
        /**
         * Shooting mode.
         * Still image capture mode
         */
        IMAGE(CaptureMode.IMAGE),

        /**
         * Shooting mode.
         * Video capture mode
         */
        VIDEO(CaptureMode.VIDEO);

        companion object {
            /**
             * Convert CaptureMode to CaptureModeEnum
             *
             * @param value Shooting mode.
             * @return CaptureModeEnum
             */
            fun get(value: CaptureMode): CaptureModeEnum? {
                return values().firstOrNull { it.value == value }
            }
        }
    }

    /**
     * Exposure compensation (EV).
     */
    enum class ExposureCompensationEnum(val value: Float) {
        /**
         * Exposure compensation -2.0
         */
        M2_0(-2.0f),

        /**
         * Exposure compensation -1.7
         */
        M1_7(-1.7f),

        /**
         * Exposure compensation -1.3
         */
        M1_3(-1.3f),

        /**
         * Exposure compensation -1.0
         */
        M1_0(-1.0f),

        /**
         * Exposure compensation -0.7
         */
        M0_7(-0.7f),

        /**
         * Exposure compensation -0.3
         */
        M0_3(-0.3f),

        /**
         * Exposure compensation 0.0
         */
        ZERO(0.0f),

        /**
         * Exposure compensation 0.3
         */
        P0_3(0.3f),

        /**
         * Exposure compensation 0.7
         */
        P0_7(0.7f),

        /**
         * Exposure compensation 1.0
         */
        P1_0(1.0f),

        /**
         * Exposure compensation 1.3
         */
        P1_3(1.3f),

        /**
         * Exposure compensation 1.7
         */
        P1_7(1.7f),

        /**
         * Exposure compensation 2.0
         */
        P2_0(2.0f);

        companion object {
            /**
             * Convert value to ExposureCompensationEnum
             *
             * @param value Exposure compensation value.
             * @return ExposureCompensationEnum
             */
            fun get(value: Float): ExposureCompensationEnum? {
                return values().firstOrNull { it.value == value }
            }
        }
    }

    /**
     * Operating time (sec.) of the self-timer.
     */
    enum class ExposureDelayEnum(val sec: Int) {
        /**
         * Disable self-timer.
         */
        DELAY_OFF(0),

        /**
         * Self-timer time. 1sec.
         */
        DELAY_1(1),

        /**
         * Self-timer time. 2sec.
         */
        DELAY_2(2),

        /**
         * Self-timer time. 3sec.
         */
        DELAY_3(3),

        /**
         * Self-timer time. 4sec.
         */
        DELAY_4(4),

        /**
         * Self-timer time. 5sec.
         */
        DELAY_5(5),

        /**
         * Self-timer time. 6sec.
         */
        DELAY_6(6),

        /**
         * Self-timer time. 7sec.
         */
        DELAY_7(7),

        /**
         * Self-timer time. 8sec.
         */
        DELAY_8(8),

        /**
         * Self-timer time. 9sec.
         */
        DELAY_9(9),

        /**
         * Self-timer time. 10sec.
         */
        DELAY_10(10),

        /**
         * Just used by getMySetting/setMySetting command.
         */
        DO_NOT_UPDATE_MY_SETTING_CONDITION(-1);

        companion object {
            /**
             * Convert second to ExposureDelayEnum
             *
             * @param sec Self-timer time.
             * @return ExposureDelayEnum
             */
            fun get(sec: Int): ExposureDelayEnum? {
                return values().firstOrNull { it.sec == sec }
            }
        }
    }

    /**
     * Exposure program. The exposure settings that take priority can be selected.
     *
     * It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
     * Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
     */
    enum class ExposureProgramEnum(val value: Int) {
        /**
         * Exposure program.
         *
         * Manual program
         * Manually set the ISO sensitivity (iso) setting, shutter speed (shutterSpeed) and aperture (aperture, RICOH THETA Z1).
         */
        MANUAL(1),

        /**
         * Exposure program.
         *
         * Normal program
         * Exposure settings are all set automatically.
         */
        NORMAL_PROGRAM(2),

        /**
         * Exposure program.
         *
         * Aperture priority program
         * Manually set the aperture (aperture).
         * (RICOH THETA Z1)
         */
        APERTURE_PRIORITY(3),

        /**
         * Exposure program.
         *
         * Shutter priority program
         * Manually set the shutter speed (shutterSpeed).
         */
        SHUTTER_PRIORITY(4),

        /**
         * Exposure program.
         *
         * ISO priority program
         * Manually set the ISO sensitivity (iso) setting.
         */
        ISO_PRIORITY(9);

        companion object {
            /**
             * Convert exposure program value to ExposureProgramEnum.
             *
             * @param value Exposure program value.
             * @return ExposureProgramEnum
             */
            fun get(value: Int): ExposureProgramEnum? {
                return values().firstOrNull { it.value == value }
            }
        }
    }

    enum class FileFormatTypeEnum(val mediaType: MediaType) {
        /**
         * jpeg image
         */
        JPEG(MediaType.JPEG),

        /**
         * mp4 video
         */
        MP4(MediaType.MP4),

        /**
         * raw+ image
         */
        RAW(MediaType.RAW)
    }

    /**
     * File format used in shooting.
     */
    enum class FileFormatEnum(
        val type: FileFormatTypeEnum,
        val width: Int,
        val height: Int,
        val _codec: String?,
        val _frameRate: Int?
    ) {
        /**
         * Image File format.
         *
         * type: jpeg
         * size: 2048 x 1024
         *
         * For RICOH THETA S or SC
         */
        IMAGE_2K(FileFormatTypeEnum.JPEG, 2048, 1024, null, null),

        /**
         * Image File format.
         *
         * type: jpeg
         * size: 5376 x 2688
         *
         * For RICOH THETA V or S or SC
         */
        IMAGE_5K(FileFormatTypeEnum.JPEG, 5376, 2688, null, null),

        /**
         * Image File format.
         *
         * type: jpeg
         * size: 6720 x 3360
         *
         * For RICOH THETA Z1
         */
        IMAGE_6_7K(FileFormatTypeEnum.JPEG, 6720, 3360, null, null),

        /**
         * Image File format.
         *
         * type: raw+
         * size: 6720 x 3360
         *
         * For RICOH THETA Z1
         */
        RAW_P_6_7K(FileFormatTypeEnum.RAW, 6720, 3360, null, null),

        /**
         * Image File format.
         *
         * type: jpeg
         * size: 5504 x 2752
         *
         * For RICOH THETA X or later
         */
        IMAGE_5_5K(FileFormatTypeEnum.JPEG, 5504, 2752, null, null),

        /**
         * Image File format.
         *
         * type: jpeg
         * size: 11008 x 5504
         *
         * For RICOH THETA X or later
         */
        IMAGE_11K(FileFormatTypeEnum.JPEG, 11008, 5504, null, null),

        /**
         * Just used by getMySetting/setMySetting command
         */
        IMAGE_DO_NOT_UPDATE_MY_SETTING_CONDITION(FileFormatTypeEnum.JPEG, 0, 0, null, null),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 1280 x 570
         *
         * For RICOH THETA S or SC
         */
        VIDEO_HD(FileFormatTypeEnum.MP4, 1280, 720, null, null),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 1920 x 1080
         *
         * For RICOH THETA S or SC
         */
        VIDEO_FULL_HD(FileFormatTypeEnum.MP4, 1920, 1080, null, null),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 1920 x 960
         * codec: H.264/MPEG-4 AVC
         *
         * For RICOH THETA Z1 or V
         */
        VIDEO_2K(FileFormatTypeEnum.MP4, 1920, 960, "H.264/MPEG-4 AVC", null),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 3840 x 1920
         * codec: H.264/MPEG-4 AVC
         *
         * For RICOH THETA Z1 or V
         */
        VIDEO_4K(FileFormatTypeEnum.MP4, 3840, 1920, "H.264/MPEG-4 AVC", null),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 1920 x 960
         * codec: H.264/MPEG-4 AVC
         * frame rate: 30
         *
         * For RICOH THETA X or later
         */
        VIDEO_2K_30F(FileFormatTypeEnum.MP4, 1920, 960, "H.264/MPEG-4 AVC", 30),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 1920 x 960
         * codec: H.264/MPEG-4 AVC
         * frame rate: 60
         *
         * For RICOH THETA X or later
         */
        VIDEO_2K_60F(FileFormatTypeEnum.MP4, 1920, 960, "H.264/MPEG-4 AVC", 60),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 3840 x 1920
         * codec: H.264/MPEG-4 AVC
         * frame rate: 30
         *
         * For RICOH THETA X or later
         */
        VIDEO_4K_30F(FileFormatTypeEnum.MP4, 3840, 1920, "H.264/MPEG-4 AVC", 30),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 3840 x 1920
         * codec: H.264/MPEG-4 AVC
         * frame rate: 60
         *
         * For RICOH THETA X or later
         */
        VIDEO_4K_60F(FileFormatTypeEnum.MP4, 3840, 1920, "H.264/MPEG-4 AVC", 60),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 5760 x 2880
         * codec: H.264/MPEG-4 AVC
         * frame rate: 2
         *
         * For RICOH THETA X or later
         */
        VIDEO_5_7K_2F(FileFormatTypeEnum.MP4, 5760, 2880, "H.264/MPEG-4 AVC", 2),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 5760 x 2880
         * codec: H.264/MPEG-4 AVC
         * frame rate: 5
         *
         * For RICOH THETA X or later
         */
        VIDEO_5_7K_5F(FileFormatTypeEnum.MP4, 5760, 2880, "H.264/MPEG-4 AVC", 5),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 5760 x 2880
         * codec: H.264/MPEG-4 AVC
         * frame rate: 30
         *
         * For RICOH THETA X or later
         */
        VIDEO_5_7K_30F(FileFormatTypeEnum.MP4, 5760, 2880, "H.264/MPEG-4 AVC", 30),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 7680 x 3840
         * codec: H.264/MPEG-4 AVC
         * frame rate: 2
         *
         * For RICOH THETA X or later
         */
        VIDEO_7K_2F(FileFormatTypeEnum.MP4, 7680, 3840, "H.264/MPEG-4 AVC", 2),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 7680 x 3840
         * codec: H.264/MPEG-4 AVC
         * frame rate: 5
         *
         * For RICOH THETA X or later
         */
        VIDEO_7K_5F(FileFormatTypeEnum.MP4, 7680, 3840, "H.264/MPEG-4 AVC", 5),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 7680 x 3840
         * codec: H.264/MPEG-4 AVC
         * frame rate: 10
         *
         * For RICOH THETA X or later
         */
        VIDEO_7K_10F(FileFormatTypeEnum.MP4, 7680, 3840, "H.264/MPEG-4 AVC", 10),

        /**
         * Just used by getMySetting/setMySetting command
         */
        VIDEO_DO_NOT_UPDATE_MY_SETTING_CONDITION(FileFormatTypeEnum.MP4, 0, 0, null, null);

        /**
         * Convert FileFormatEnum to MediaFileFormat.
         *
         * @return MediaFileFormat
         */
        fun toMediaFileFormat(): MediaFileFormat {
            return MediaFileFormat(type.mediaType, width, height, _codec, _frameRate)
        }

        companion object {
            /**
             * Convert MediaFileFormat to FileFormatEnum.
             *
             * @param mediaFileFormat File format for ThetaApi.
             * @return FileFormatEnum
             */
            fun get(mediaFileFormat: MediaFileFormat): FileFormatEnum? {
                return values().firstOrNull {
                    it.type.mediaType == mediaFileFormat.type &&
                            it.width == mediaFileFormat.width &&
                            it.height == mediaFileFormat.height &&
                            it._codec == mediaFileFormat._codec &&
                            it._frameRate == mediaFileFormat._frameRate
                }
            }
        }
    }

    /**
     * Photo image format used in PhotoCapture.
     */
    enum class PhotoFileFormatEnum(val fileFormat: FileFormatEnum) {
        /**
         * Image File format.
         *
         * type: jpeg
         * size: 2048 x 1024
         *
         * For RICOH THETA S or SC
         */
        IMAGE_2K(FileFormatEnum.IMAGE_2K),

        /**
         * Image File format.
         *
         * type: jpeg
         * size: 5376 x 2688
         *
         * For RICOH THETA V or S or SC
         */
        IMAGE_5K(FileFormatEnum.IMAGE_5K),

        /**
         * Image File format.
         *
         * type: jpeg
         * size: 6720 x 3360
         *
         * For RICOH THETA Z1
         */
        IMAGE_6_7K(FileFormatEnum.IMAGE_6_7K),

        /**
         * Image File format.
         *
         * type: raw+
         * size: 6720 x 3360
         *
         * For RICOH THETA Z1
         */
        RAW_P_6_7K(FileFormatEnum.RAW_P_6_7K),

        /**
         * Image File format.
         *
         * type: jpeg
         * size: 5504 x 2752
         *
         * For RICOH THETA X or later
         */
        IMAGE_5_5K(FileFormatEnum.IMAGE_5_5K),

        /**
         * Image File format.
         *
         * type: jpeg
         * size: 11008 x 5504
         *
         * For RICOH THETA X or later
         */
        IMAGE_11K(FileFormatEnum.IMAGE_11K);

        companion object {
            /**
             * Convert FileFormatEnum to PhotoFileFormatEnum.
             *
             * @param fileformat FileFormatEnum.
             * @return PhotoFileFormatEnum
             */
            fun get(fileformat: FileFormatEnum): PhotoFileFormatEnum? {
                return values().firstOrNull { it.fileFormat == fileformat }
            }
        }
    }

    /**
     * Video image format used in VideoCapture.
     */
    enum class VideoFileFormatEnum(val fileFormat: FileFormatEnum) {
        /**
         * Video File format.
         *
         * type: mp4
         * size: 1280 x 570
         *
         * For RICOH THETA S or SC
         */
        VIDEO_HD(FileFormatEnum.VIDEO_HD),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 1920 x 1080
         *
         * For RICOH THETA S or SC
         */
        VIDEO_FULL_HD(FileFormatEnum.VIDEO_FULL_HD),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 1920 x 960
         * codec: H.264/MPEG-4 AVC
         *
         * For RICOH THETA Z1 or V
         */
        VIDEO_2K(FileFormatEnum.VIDEO_2K),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 3840 x 1920
         * codec: H.264/MPEG-4 AVC
         *
         * For RICOH THETA Z1 or V
         */
        VIDEO_4K(FileFormatEnum.VIDEO_4K),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 1920 x 960
         * codec: H.264/MPEG-4 AVC
         * frame rate: 30
         *
         * For RICOH THETA X or later
         */
        VIDEO_2K_30F(FileFormatEnum.VIDEO_2K_30F),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 1920 x 960
         * codec: H.264/MPEG-4 AVC
         * frame rate: 60
         *
         * For RICOH THETA X or later
         */
        VIDEO_2K_60F(FileFormatEnum.VIDEO_2K_60F),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 3840 x 1920
         * codec: H.264/MPEG-4 AVC
         * frame rate: 30
         *
         * For RICOH THETA X or later
         */
        VIDEO_4K_30F(FileFormatEnum.VIDEO_4K_30F),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 3840 x 1920
         * codec: H.264/MPEG-4 AVC
         * frame rate: 60
         *
         * For RICOH THETA X or later
         */
        VIDEO_4K_60F(FileFormatEnum.VIDEO_4K_60F),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 5760 x 2880
         * codec: H.264/MPEG-4 AVC
         * frame rate: 2
         *
         * For RICOH THETA X or later
         */
        VIDEO_5_7K_2F(FileFormatEnum.VIDEO_5_7K_2F),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 5760 x 2880
         * codec: H.264/MPEG-4 AVC
         * frame rate: 5
         *
         * For RICOH THETA X or later
         */
        VIDEO_5_7K_5F(FileFormatEnum.VIDEO_5_7K_5F),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 5760 x 2880
         * codec: H.264/MPEG-4 AVC
         * frame rate: 30
         *
         * For RICOH THETA X or later
         */
        VIDEO_5_7K_30F(FileFormatEnum.VIDEO_5_7K_30F),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 7680 x 3840
         * codec: H.264/MPEG-4 AVC
         * frame rate: 2
         *
         * For RICOH THETA X or later
         */
        VIDEO_7K_2F(FileFormatEnum.VIDEO_7K_2F),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 7680 x 3840
         * codec: H.264/MPEG-4 AVC
         * frame rate: 5
         *
         * For RICOH THETA X or later
         */
        VIDEO_7K_5F(FileFormatEnum.VIDEO_7K_5F),

        /**
         * Video File format.
         *
         * type: mp4
         * size: 7680 x 3840
         * codec: H.264/MPEG-4 AVC
         * frame rate: 10
         *
         * For RICOH THETA X or later
         */
        VIDEO_7K_10F(FileFormatEnum.VIDEO_7K_10F);

        companion object {
            /**
             * Convert FileFormatEnum to VideoFileFormatEnum.
             *
             * @param fileformat FileFormatEnum.
             * @return VideoFileFormatEnum
             */
            fun get(fileformat: FileFormatEnum): VideoFileFormatEnum? {
                return values().firstOrNull { it.fileFormat == fileformat }
            }
        }
    }

    /**
     * Image processing filter.
     */
    enum class FilterEnum(val filter: ImageFilter) {
        /**
         * Image processing filter. No filter.
         */
        OFF(ImageFilter.OFF),

        /**
         * Image processing filter. Noise reduction.
         */
        NOISE_REDUCTION(ImageFilter.NOISE_REDUCTION),

        /**
         * Image processing filter. HDR.
         */
        HDR(ImageFilter.HDR);

        companion object {
            /**
             * Convert ImageFilter to FilterEnum
             *
             * @param filter Image processing filter for ThetaApi.
             * @return FilterEnum
             */
            fun get(filter: ImageFilter): FilterEnum? {
                return values().firstOrNull { it.filter == filter }
            }
        }
    }

    /**
     * GPS information
     *
     * 65535 is set for latitude and longitude when disabling the GPS setting at
     * RICOH THETA Z1 and prior.
     *
     * For RICOH THETA X, ON/OFF for assigning position information is
     * set at [Options.isGpsOn]
     */
    data class GpsInfo(
        /**
         * Latitude (-90.000000 – 90.000000)
         * When GPS is disabled: 65535
         */
        val latitude: Float,

        /**
         * Longitude (-180.000000 – 180.000000)
         * When GPS is disabled: 65535
         */
        val longitude: Float,

        /**
         * Altitude (meters)
         * When GPS is disabled: 0
         */
        val altitude: Float,

        /**
         * Location information acquisition time
         * YYYY:MM:DD hh:mm:ss+(-)hh:mm
         * hh is in 24-hour time, +(-)hh:mm is the time zone
         * when GPS is disabled: ""(null characters)
         */
        val dateTimeZone: String
    ) {

        companion object {
            /**
             * Value when GPS setting is disabled.
             */
            val disabled = GpsInfo(65535f, 65535f, 0f, "")
        }

        constructor(gpsInfo: com.ricoh360.thetaclient.transferred.GpsInfo) : this(
            latitude = gpsInfo.lat ?: 65535f,
            longitude = gpsInfo.lng ?: 65535f,
            altitude = gpsInfo._altitude ?: 0f,
            dateTimeZone = gpsInfo._dateTimeZone ?: ""
        )

        /**
         * Determine if setting value is invalid
         *
         * @return If disabled, true
         */
        fun isDisabled(): Boolean {
            return this == disabled
        }

        /**
         * Convert GpsInfo to transferred.GpsInfo. for ThetaApi.
         *
         * @return transferred.GpsInfo
         */
        fun toTransferredGpsInfo(): com.ricoh360.thetaclient.transferred.GpsInfo {
            return GpsInfo(
                lat = latitude,
                lng = longitude,
                _altitude = altitude,
                _dateTimeZone = dateTimeZone,
                _datum = if (isDisabled()) "" else "WGS84"
            )
        }
    }

    /**
     * Turns position information assigning ON/OFF.
     *
     * For RICOH THETA X
     */
    enum class GpsTagRecordingEnum(val value: GpsTagRecording) {
        /**
         * Position information assigning ON.
         */
        ON(GpsTagRecording.ON),

        /**
         * Position information assigning OFF.
         */
        OFF(GpsTagRecording.OFF);

        companion object {
            /**
             * Convert GpsTagRecording to GpsTagRecordingEnum
             *
             * @param value Turns position information assigning for ThetaApi.
             * @return GpsTagRecordingEnum
             */
            fun get(value: GpsTagRecording): GpsTagRecordingEnum? {
                return values().firstOrNull { it.value == value }
            }
        }
    }

    /**
     * ISO sensitivity.
     *
     * It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
     * Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
     *
     * When the exposure program (exposureProgram) is set to Manual or ISO Priority
     *
     */
    enum class IsoEnum(val value: Int) {
        /**
         * ISO sensitivity.
         * AUTO (0)
         */
        ISO_AUTO(0),

        /**
         * ISO sensitivity.
         * ISO 50
         *
         * For RICOH THETA X or later
         */
        ISO_50(50),

        /**
         * ISO sensitivity.
         * ISO 64
         *
         * For RICOH THETA V or X or later
         */
        ISO_64(64),

        /**
         * ISO sensitivity.
         * ISO 80
         *
         * For RICOH THETA V or Z1 or X or later
         */
        ISO_80(80),

        /**
         * ISO sensitivity.
         * ISO 100
         */
        ISO_100(100),

        /**
         * ISO sensitivity.
         * ISO 125
         */
        ISO_125(125),

        /**
         * ISO sensitivity.
         * ISO 160
         */
        ISO_160(160),

        /**
         * ISO sensitivity.
         * ISO 200
         */
        ISO_200(200),

        /**
         * ISO sensitivity.
         * ISO 250
         */
        ISO_250(250),

        /**
         * ISO sensitivity.
         * ISO 320
         */
        ISO_320(320),

        /**
         * ISO sensitivity.
         * ISO 400
         */
        ISO_400(400),

        /**
         * ISO sensitivity.
         * ISO 500
         */
        ISO_500(500),

        /**
         * ISO sensitivity.
         * ISO 640
         */
        ISO_640(640),

        /**
         * ISO sensitivity.
         * ISO 800
         */
        ISO_800(800),

        /**
         * ISO sensitivity.
         * ISO 1000
         */
        ISO_1000(1000),

        /**
         * ISO sensitivity.
         * ISO 1250
         */
        ISO_1250(1250),

        /**
         * ISO sensitivity.
         * ISO 1600
         */
        ISO_1600(1600),

        /**
         * ISO sensitivity.
         * ISO 2000
         *
         * For RICOH THETA V or Z1 or X or later
         */
        ISO_2000(2000),

        /**
         * ISO sensitivity.
         * ISO 2500
         *
         * For RICOH THETA V or Z1 or X or later
         */
        ISO_2500(2500),

        /**
         * ISO sensitivity.
         * ISO 3200
         *
         * For RICOH THETA V or Z1 or X or later
         */
        ISO_3200(3200),

        /**
         * ISO sensitivity.
         * ISO 4000
         *
         * For RICOH THETA Z1
         * For RICOH THETA V, Available in video shooting mode.
         */
        ISO_4000(4000),

        /**
         * ISO sensitivity.
         * ISO 5000
         *
         * For RICOH THETA Z1
         * For RICOH THETA V, Available in video shooting mode.
         */
        ISO_5000(5000),

        /**
         * ISO sensitivity.
         * ISO 6400
         *
         * For RICOH THETA Z1
         * For RICOH THETA V, Available in video shooting mode.
         */
        ISO_6400(6400);

        companion object {
            /**
             * Convert ISO value to IsoEnum
             *
             * @param value ISO value
             * @return IsoEnum
             */
            fun get(value: Int): IsoEnum? {
                return values().firstOrNull { it.value == value }
            }
        }
    }

    /**
     * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
     *
     * 100*1, 125*1, 160*1, 200, 250, 320, 400, 500, 640, 800, 1000, 1250, 1600, 2000, 2500, 3200, 4000*2, 5000*2, 6400*2
     * *1 Enabled only with RICOH THETA X.
     * *2 Enabled with RICOH THETA Z1's image shooting mode and video shooting mode, and with RICOH THETA V's video shooting mode.
     */
    enum class IsoAutoHighLimitEnum(val value: Int) {
        /**
         * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         * ISO 100
         *
         * Enabled only with RICOH THETA X.
         */
        ISO_100(100),

        /**
         * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         * ISO 125
         *
         * Enabled only with RICOH THETA X.
         */
        ISO_125(125),

        /**
         * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         * ISO 160
         *
         * Enabled only with RICOH THETA X.
         */
        ISO_160(160),

        /**
         * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         * ISO 200
         */
        ISO_200(200),

        /**
         * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         * ISO 250
         */
        ISO_250(250),

        /**
         * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         * ISO 320
         */
        ISO_320(320),

        /**
         * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         * ISO 400
         */
        ISO_400(400),

        /**
         * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         * ISO 500
         */
        ISO_500(500),

        /**
         * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         * ISO 640
         */
        ISO_640(640),

        /**
         * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         * ISO 800
         */
        ISO_800(800),

        /**
         * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         * ISO 1000
         */
        ISO_1000(1000),

        /**
         * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         * ISO 1250
         */
        ISO_1250(1250),

        /**
         * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         * ISO 1600
         */
        ISO_1600(1600),

        /**
         * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         * ISO 2000
         */
        ISO_2000(2000),

        /**
         * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         * ISO 2500
         */
        ISO_2500(2500),

        /**
         * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         * ISO 3200
         */
        ISO_3200(3200),

        /**
         * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         * ISO 4000
         *
         * Enabled with RICOH THETA Z1's image shooting mode and video shooting mode, and with RICOH THETA V's video shooting mode.
         */
        ISO_4000(4000),

        /**
         * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         * ISO 5000
         *
         * Enabled with RICOH THETA Z1's image shooting mode and video shooting mode, and with RICOH THETA V's video shooting mode.
         */
        ISO_5000(5000),

        /**
         * ISO sensitivity upper limit when ISO sensitivity is set to automatic.
         * ISO 6400
         *
         * Enabled with RICOH THETA Z1's image shooting mode and video shooting mode, and with RICOH THETA V's video shooting mode.
         */
        ISO_6400(6400),

        /**
         * Just used by getMySetting/setMySetting command
         */
        DO_NOT_UPDATE_MY_SETTING_CONDITION(-1);

        companion object {
            /**
             * Convert ISO value to IsoAutoHighLimitEnum
             *
             * @param value ISO value
             * @return IsoAutoHighLimitEnum
             */
            fun get(value: Int): IsoAutoHighLimitEnum? {
                return values().firstOrNull { it.value == value }
            }
        }
    }

    /**
     * Language used in camera OS.
     */
    enum class LanguageEnum(val value: Language) {
        /**
         * Language used in camera OS.
         * de
         */
        DE(Language.DE),

        /**
         * Language used in camera OS.
         * en-GB
         */
        EN_GB(Language.GB),

        /**
         * Language used in camera OS.
         * en-US
         */
        EN_US(Language.US),

        /**
         * Language used in camera OS.
         * fr
         */
        FR(Language.FR),

        /**
         * Language used in camera OS.
         * it
         */
        IT(Language.IT),

        /**
         * Language used in camera OS.
         * ja
         */
        JA(Language.JA),

        /**
         * Language used in camera OS.
         * ko
         */
        KO(Language.KO),

        /**
         * Language used in camera OS.
         * zh-CN
         */
        ZH_CN(Language.CN),

        /**
         * Language used in camera OS.
         * zh-TW
         */
        ZH_TW(Language.TW);

        companion object {
            /**
             * Convert Language to LanguageEnum
             *
             * @param value Language.
             * @return LanguageEnum
             */
            fun get(value: Language): LanguageEnum? {
                return values().firstOrNull { it.value == value }
            }
        }
    }

    /**
     * Maximum recordable time (in seconds) of the camera
     */
    enum class MaxRecordableTimeEnum(val sec: Int) {
        /**
         * Maximum recordable time. 180sec for SC2 only.
         */
        RECORDABLE_TIME_180(180),

        /**
         * Maximum recordable time. 300sec for other than SC2.
         */
        RECORDABLE_TIME_300(300),

        /**
         * Maximum recordable time. 1500sec for other than SC2.
         */
        RECORDABLE_TIME_1500(1500),

        /**
         * Maximum recordable time. 7200sec for Theta X version 2.00.0 or later,
         * only for 5.7K 2/5/10fps and 8K 2/5/10fps.
         * If you set 7200 seconds in 8K 10fps mode and then set back to 4K 30fps mode,
         * the max recordable time will be overwritten to 1500 seconds automatically.
         */
        RECORDABLE_TIME_7200(7200),

        /**
         * Just used by getMySetting/setMySetting command
         */
        DO_NOT_UPDATE_MY_SETTING_CONDITION(-1);

        companion object {
            /**
             * Convert second to MaxRecordableTimeEnum
             *
             * @param sec Maximum recordable time.
             * @return MaxRecordableTimeEnum
             */
            fun get(sec: Int): MaxRecordableTimeEnum? {
                return values().firstOrNull { it.sec == sec }
            }
        }
    }

    /**
     * Network type supported by Theta V, Z1 and X.
     */
    enum class NetworkTypeEnum(val value: NetworkType) {
        /**
         * Direct mode
         */
        DIRECT(NetworkType.DIRECT),

        /**
         * Client mode via WLAN
         */
        CLIENT(NetworkType.CLIENT),

        /**
         * Client mode via Ethernet cable
         */
        ETHERNET(NetworkType.ETHERNET),

        /**
         * Network is off. This value can be gotten only by plugin
         */
        OFF(NetworkType.OFF);

        companion object {
            /**
             * Convert NetworkType to NetworkTypeEnum
             *
             * @param value Network type.
             * @return NetworkTypeEnum
             */
            fun get(value: NetworkType): NetworkTypeEnum? {
                return values().firstOrNull { it.value == value }
            }
        }
    }

    /**
     * Length of standby time before the camera automatically powers OFF.
     *
     * Use in [OffDelayEnum] or [OffDelaySec]
     */
    interface OffDelay {
        val sec: Int
    }

    /**
     * Length of standby time before the camera automatically powers OFF.
     *
     * For RICOH THETA V or later
     * 0, or a value that is a multiple of 60 out of 600 or more and 2592000 or less (unit: second), or 65535.
     * Return 0 when 65535 is set and obtained (Do not turn power OFF).
     *
     * For RICOH THETA S or SC
     * 30 or more and 1800 or less (unit: seconds), 65535 (Do not turn power OFF).
     */
    class OffDelaySec(override val sec: Int) : OffDelay {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as OffDelaySec

            if (sec != other.sec) return false

            return true
        }

        override fun hashCode(): Int {
            return sec
        }
    }

    /**
     * Length of standby time before the camera automatically powers OFF.
     *
     * For RICOH THETA V or later
     */
    enum class OffDelayEnum(override val sec: Int) : OffDelay {
        /**
         * Do not turn power off.
         */
        DISABLE(65535),

        /**
         * Power off after 5 minutes.(300sec)
         */
        OFF_DELAY_5M(300),

        /**
         * Power off after 10 minutes.(600sec)
         */
        OFF_DELAY_10M(600),

        /**
         * Power off after 15 minutes.(900sec)
         */
        OFF_DELAY_15M(900),

        /**
         * Power off after 30 minutes.(1,800sec)
         */
        OFF_DELAY_30M(1800);

        companion object {
            /**
             * Convert second to OffDelay
             *
             * @return [OffDelayEnum] or [OffDelay]
             */
            fun get(sec: Int): OffDelay {
                return values().firstOrNull { it.sec == sec } ?: OffDelaySec(sec)
            }
        }
    }

    /**
     * Power saving mode
     *
     * For Theta X only.
     */
    enum class PowerSavingEnum(val value: PowerSaving) {
        /**
         * Power saving mode ON
         */
        ON(PowerSaving.ON),

        /**
         * Power saving mode OFF
         */
        OFF(PowerSaving.OFF);

        companion object {
            /**
             * Convert PowerSaving to PowerSavingEnum
             *
             * @param value
             * @return PowerSavingEnum
             */
            fun get(value: PowerSaving): PowerSavingEnum? {
                return values().firstOrNull { it.value == value }
            }
        }
    }

    /**
     * Format of live view
     */
    enum class PreviewFormatEnum(val width: Int, val height: Int, val framerate: Int) {
        W1024_H512_F30(1024, 512, 30), // For Theta X, Z1, V and SC2
        W1024_H512_F15(1024, 512, 15), // For Theta X. This value can't set.
        W512_H512_F30(512, 512, 30), // For Theta X
        W1920_H960_F8(1920, 960, 8), // For Theta Z1 and V
        W1024_H512_F8(1024, 512, 8), // For Theta Z1 and V
        W640_H320_F30(640, 320, 30), // For Theta Z1 and V
        W640_H320_F8(640, 320, 8), // For Theta Z1 and V
        W640_H320_F10(640, 320, 10); // For Theta S and SC

        /**
         * Convert PreviewFormatEnum to PreviewFormat.
         */
        fun toPreviewFormat(): PreviewFormat {
            return PreviewFormat(width, height, framerate)
        }

        companion object {
            /**
             * Convert PreviewFormat to PreviewFormatEnum
             */
            fun get(value: PreviewFormat): PreviewFormatEnum? {
                return PreviewFormatEnum.values().firstOrNull {
                    it.height == value.height &&
                            it.width == value.width &&
                            it.framerate == value.framerate
                }
            }
        }
    }

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
    data class Proxy(
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
        val port: Int? = null,
        /**
         * User ID used for proxy authentication
         */
        val userid: String? = null,
        /**
         * Password used for proxy authentication
         */
        val password: String? = null,
    ) {
        constructor(use: Boolean) : this(
            use = use,
            url = null,
            port = null,
            userid = null,
            password = null
        )

        constructor(info: com.ricoh360.thetaclient.transferred.Proxy) : this(
            use = info.use,
            url = info.url,
            port = info.port,
            userid = info.userid,
            password = info.password
        )

        /**
         * Convert Proxy to transferred.Proxy
         *
         * @return transferred.Proxy
         */
        fun toTransferredProxy(): com.ricoh360.thetaclient.transferred.Proxy {
            return com.ricoh360.thetaclient.transferred.Proxy(
                use = use,
                url = url,
                port = port,
                userid = userid,
                password = password
            )
        }
    }

    /**
     * Shooting method
     *
     * Shooting method for My Settings mode. In RICOH THETA X, it is used outside of MySetting.
     * Can be acquired and set only when in the Still image shooting mode and _function is the My Settings shooting function.
     * Changing _function initializes the setting details to Normal shooting.
     *
     * For Theta X and Z1 only.
     */
    enum class ShootingMethodEnum(val value: ShootingMethod) {
        /**
         * Normal shooting
         */
        NORMAL(ShootingMethod.NORMAL),

        /**
         * Interval shooting
         */
        INTERVAL(ShootingMethod.INTERVAL),

        /**
         * Move interval shooting (RICOH THETA Z1 firmware v1.50.1 or later, RICOH THETA X is not supported)
         */
        MOVE_INTERVAL(ShootingMethod.MOVE_INTERVAL),

        /**
         * Fixed interval shooting (RICOH THETA Z1 firmware v1.50.1 or later, RICOH THETA X is not supported)
         */
        FIXED_INTERVAL(ShootingMethod.FIXED_INTERVAL),

        /**
         * Multi bracket shooting
         */
        BRACKET(ShootingMethod.BRACKET),

        /**
         * Interval composite shooting (RICOH THETA X is not supported)
         */
        COMPOSITE(ShootingMethod.COMPOSITE),

        /**
         * Continuous shooting (RICOH THETA X or later)
         */
        CONTINUOUS(ShootingMethod.CONTINUOUS),

        /**
         * Time shift shooting (RICOH THETA X or later)
         */
        TIME_SHIFT(ShootingMethod.TIMESHIFT),

        /**
         * Burst shooting (RICOH THETA Z1 v2.10.1 or later, RICOH THETA X is not supported)
         */
        BURST(ShootingMethod.BURST);

        companion object {
            /**
             * Convert ShootingMethod to ShootingMethodEnum
             *
             * @param value
             * @return ShootingMethodEnum
             */
            fun get(value: ShootingMethod): ShootingMethodEnum? {
                return values().firstOrNull { it.value == value }
            }
        }
    }

    /**
     * Shutter speed (sec).
     *
     * It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
     * Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
     *
     * ### Support value
     * The choice is listed below. There are certain range difference between each models and settings.
     *
     * | captureMode | exposureProgram | X or later | V or Z1 | SC | S |
     * | --- | --- | --- | --- | --- | --- |
     * | Still image shooting mode | Manual | 0.0000625 (1/16000) to 60 | 0.00004 (1/25000) to 60 | 0.000125 (1/8000) to 60 | 0.00015625 (1/6400) to 60 |
     * |                           | Shutter priority  | 0.0000625 (1/16000) to 15 | 0.00004 (1/25000) to 0.125 (1/8) | 0.00004 (1/25000) to 15 `*2`  |  |  |
     * | Video shooting mode `*1`    | Manual or Shutter priority | 0.0000625 (1/16000) to 0.03333333 (1/30) | 0.00004 (1/25000) to 0.03333333 (1/30) |  |  |
     * | Otherwise  |  | 0 (AUTO)  | 0 (AUTO)  | 0 (AUTO)  | 0 (AUTO)  |
     *
     * `*1` RICOH THETA Z1 and RICOH THETA V firmware v3.00.1 or later
     *
     * `*2` RICOH THETA Z1 firmware v1.50.1 or later and RICOH THETA V firmware v3.40.1 or later
     */
    enum class ShutterSpeedEnum(val value: Double) {
        /**
         * Shutter speed. auto
         */
        SHUTTER_SPEED_AUTO(0.0),

        /**
         * Shutter speed. 60 sec
         */
        SHUTTER_SPEED_60(60.0),

        /**
         * Shutter speed. 50 sec
         *
         * RICOH THETA Z1 firmware v2.10.1 or later and RICOH THETA V firmware v3.80.1 or later.
         * For RICOH THETA X, all versions are supported.
         */
        SHUTTER_SPEED_50(50.0),

        /**
         * Shutter speed. 40 sec
         *
         * RICOH THETA Z1 firmware v2.10.1 or later and RICOH THETA V firmware v3.80.1 or later.
         * For RICOH THETA X, all versions are supported.
         */
        SHUTTER_SPEED_40(40.0),

        /**
         * Shutter speed. 30 sec
         */
        SHUTTER_SPEED_30(30.0),

        /**
         * Shutter speed. 25 sec
         */
        SHUTTER_SPEED_25(25.0),

        /**
         * Shutter speed. 20 sec
         */
        SHUTTER_SPEED_20(20.0),

        /**
         * Shutter speed. 15 sec
         */
        SHUTTER_SPEED_15(15.0),

        /**
         * Shutter speed. 13 sec
         */
        SHUTTER_SPEED_13(13.0),

        /**
         * Shutter speed. 10 sec
         */
        SHUTTER_SPEED_10(10.0),

        /**
         * Shutter speed. 8 sec
         */
        SHUTTER_SPEED_8(8.0),

        /**
         * Shutter speed. 6 sec
         */
        SHUTTER_SPEED_6(6.0),

        /**
         * Shutter speed. 5 sec
         */
        SHUTTER_SPEED_5(5.0),

        /**
         * Shutter speed. 4 sec
         */
        SHUTTER_SPEED_4(4.0),

        /**
         * Shutter speed. 3.2 sec
         */
        SHUTTER_SPEED_3_2(3.2),

        /**
         * Shutter speed. 2.5 sec
         */
        SHUTTER_SPEED_2_5(2.5),

        /**
         * Shutter speed. 2 sec
         */
        SHUTTER_SPEED_2(2.0),

        /**
         * Shutter speed. 1.6 sec
         */
        SHUTTER_SPEED_1_6(1.6),

        /**
         * Shutter speed. 1.3 sec
         */
        SHUTTER_SPEED_1_3(1.3),

        /**
         * Shutter speed. 1 sec
         */
        SHUTTER_SPEED_1(1.0),

        /**
         * Shutter speed. 1/3 sec(0.76923076)
         */
        SHUTTER_SPEED_ONE_OVER_1_3(0.76923076),

        /**
         * Shutter speed. 1/6 sec(0.625)
         */
        SHUTTER_SPEED_ONE_OVER_1_6(0.625),

        /**
         * Shutter speed. 1/2 sec(0.5)
         */
        SHUTTER_SPEED_ONE_OVER_2(0.5),

        /**
         * Shutter speed. 1/2.5 sec(0.4)
         */
        SHUTTER_SPEED_ONE_OVER_2_5(0.4),

        /**
         * Shutter speed. 1/3 sec(0.33333333)
         */
        SHUTTER_SPEED_ONE_OVER_3(0.33333333),

        /**
         * Shutter speed. 1/4 sec(0.25)
         */
        SHUTTER_SPEED_ONE_OVER_4(0.25),

        /**
         * Shutter speed. 1/5 sec(0.2)
         */
        SHUTTER_SPEED_ONE_OVER_5(0.2),

        /**
         * Shutter speed. 1/6 sec(0.16666666)
         */
        SHUTTER_SPEED_ONE_OVER_6(0.16666666),

        /**
         * Shutter speed. 1/8 sec(0.125)
         */
        SHUTTER_SPEED_ONE_OVER_8(0.125),

        /**
         * Shutter speed. 1/10 sec(0.1)
         */
        SHUTTER_SPEED_ONE_OVER_10(0.1),

        /**
         * Shutter speed. 1/13 sec(0.07692307)
         */
        SHUTTER_SPEED_ONE_OVER_13(0.07692307),

        /**
         * Shutter speed. 1/15 sec(0.06666666)
         */
        SHUTTER_SPEED_ONE_OVER_15(0.06666666),

        /**
         * Shutter speed. 1/20 sec(0.05)
         */
        SHUTTER_SPEED_ONE_OVER_20(0.05),

        /**
         * Shutter speed. 1/25 sec(0.04)
         */
        SHUTTER_SPEED_ONE_OVER_25(0.04),

        /**
         * Shutter speed. 1/30 sec(0.03333333)
         */
        SHUTTER_SPEED_ONE_OVER_30(0.03333333),

        /**
         * Shutter speed. 1/40 sec(0.025)
         */
        SHUTTER_SPEED_ONE_OVER_40(0.025),

        /**
         * Shutter speed. 1/50 sec(0.02)
         */
        SHUTTER_SPEED_ONE_OVER_50(0.02),

        /**
         * Shutter speed. 1/60 sec(0.01666666)
         */
        SHUTTER_SPEED_ONE_OVER_60(0.01666666),

        /**
         * Shutter speed. 1/80 sec(0.0125)
         */
        SHUTTER_SPEED_ONE_OVER_80(0.0125),

        /**
         * Shutter speed. 1/100 sec(0.01)
         */
        SHUTTER_SPEED_ONE_OVER_100(0.01),

        /**
         * Shutter speed. 1/125 sec(0.008)
         */
        SHUTTER_SPEED_ONE_OVER_125(0.008),

        /**
         * Shutter speed. 1/160 sec(0.00625)
         */
        SHUTTER_SPEED_ONE_OVER_160(0.00625),

        /**
         * Shutter speed. 1/200 sec(0.005)
         */
        SHUTTER_SPEED_ONE_OVER_200(0.005),

        /**
         * Shutter speed. 1/250 sec(0.004)
         */
        SHUTTER_SPEED_ONE_OVER_250(0.004),

        /**
         * Shutter speed. 1/320 sec(0.003125)
         */
        SHUTTER_SPEED_ONE_OVER_320(0.003125),

        /**
         * Shutter speed. 1/400 sec(0.0025)
         */
        SHUTTER_SPEED_ONE_OVER_400(0.0025),

        /**
         * Shutter speed. 1/500 sec(0.002)
         */
        SHUTTER_SPEED_ONE_OVER_500(0.002),

        /**
         * Shutter speed. 1/640 sec(0.0015625)
         */
        SHUTTER_SPEED_ONE_OVER_640(0.0015625),

        /**
         * Shutter speed. 1/800 sec(0.00125)
         */
        SHUTTER_SPEED_ONE_OVER_800(0.00125),

        /**
         * Shutter speed. 1/1000 sec(0.001)
         */
        SHUTTER_SPEED_ONE_OVER_1000(0.001),

        /**
         * Shutter speed. 1/1250 sec(0.0008)
         */
        SHUTTER_SPEED_ONE_OVER_1250(0.0008),

        /**
         * Shutter speed. 1/1600 sec(0.000625)
         */
        SHUTTER_SPEED_ONE_OVER_1600(0.000625),

        /**
         * Shutter speed. 1/2000 sec(0.0005)
         */
        SHUTTER_SPEED_ONE_OVER_2000(0.0005),

        /**
         * Shutter speed. 1/2500 sec(0.0004)
         */
        SHUTTER_SPEED_ONE_OVER_2500(0.0004),

        /**
         * Shutter speed. 1/3200 sec(0.0003125)
         */
        SHUTTER_SPEED_ONE_OVER_3200(0.0003125),

        /**
         * Shutter speed. 1/4000 sec(0.00025)
         */
        SHUTTER_SPEED_ONE_OVER_4000(0.00025),

        /**
         * Shutter speed. 1/5000 sec(0.0002)
         */
        SHUTTER_SPEED_ONE_OVER_5000(0.0002),

        /**
         * Shutter speed. 1/6400 sec(0.00015625)
         */
        SHUTTER_SPEED_ONE_OVER_6400(0.00015625),

        /**
         * Shutter speed. 1/8000 sec(0.000125)
         */
        SHUTTER_SPEED_ONE_OVER_8000(0.000125),

        /**
         * Shutter speed. 1/10000 sec(0.0001)
         */
        SHUTTER_SPEED_ONE_OVER_10000(0.0001),

        /**
         * Shutter speed. 1/12500 sec(0.00008)
         *
         * No support for RICOH THETA X.
         */
        SHUTTER_SPEED_ONE_OVER_12500(0.00008),

        /**
         * Shutter speed. 1/12800 sec(0.00007812)
         *
         * Enabled only for RICOH THETA X.
         */
        SHUTTER_SPEED_ONE_OVER_12800(0.00007812),

        /**
         * Shutter speed. 1/16000 sec(0.0000625)
         */
        SHUTTER_SPEED_ONE_OVER_16000(0.0000625),

        /**
         * Shutter speed. 1/20000 sec(0.00005)
         */
        SHUTTER_SPEED_ONE_OVER_20000(0.00005),

        /**
         * Shutter speed. 1/25000 sec(0.00004)
         */
        SHUTTER_SPEED_ONE_OVER_25000(0.00004);

        companion object {
            /**
             * Convert shutter speed value to ShutterSpeedEnum
             *
             * @param value shutter speed(sec)
             * @return ShutterSpeedEnum
             */
            fun get(value: Double): ShutterSpeedEnum? {
                return ShutterSpeedEnum.values().firstOrNull { it.value == value }
            }
        }
    }

    /**
     * Length of standby time before the camera enters the sleep mode.
     *
     * Use in [SleepDelayEnum] or [SleepDelaySec]
     */
    interface SleepDelay {
        val sec: Int
    }

    /**
     * Length of standby time before the camera enters the sleep mode.
     *
     * For RICOH THETA V or later
     * 60 to 65534, or 65535 (to disable the sleep mode).
     * If a value from "0" to "59" is specified, and error (invalidParameterValue) is returned.
     *
     * For RICOH THETA S or SC
     * 30 to 1800, or 65535 (to disable the sleep mode)
     */
    class SleepDelaySec(override val sec: Int) : SleepDelay {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as SleepDelaySec

            if (sec != other.sec) return false

            return true
        }

        override fun hashCode(): Int {
            return sec
        }
    }

    /**
     * Length of standby time before the camera enters the sleep mode.
     */
    enum class SleepDelayEnum(override val sec: Int) : SleepDelay {
        /**
         * sleep mode after 3 minutes.(180sec)
         */
        SLEEP_DELAY_3M(180),

        /**
         * sleep mode after 5 minutes.(300sec)
         */
        SLEEP_DELAY_5M(300),

        /**
         * sleep mode after 7 minutes.(420sec)
         */
        SLEEP_DELAY_7M(420),

        /**
         * sleep mode after 10 minutes.(600sec)
         */
        SLEEP_DELAY_10M(600),

        /**
         * Do not turn sleep mode.
         */
        DISABLE(65535);

        companion object {
            /**
             * Convert second to SleepDelay
             *
             * @return [SleepDelayEnum] or [SleepDelaySec]
             */
            fun get(sec: Int): SleepDelay {
                return values().firstOrNull { it.sec == sec } ?: SleepDelaySec(sec)
            }
        }
    }

    /**
     * White balance.
     *
     * It can be set for video shooting mode at RICOH THETA V firmware v3.00.1 or later.
     * Shooting settings are retained separately for both the Still image shooting mode and Video shooting mode.
     */
    enum class WhiteBalanceEnum(val value: WhiteBalance) {
        /**
         * White balance.
         * Automatic
         */
        AUTO(WhiteBalance.AUTO),

        /**
         * White balance.
         * Outdoor
         */
        DAYLIGHT(WhiteBalance.DAYLIGHT),

        /**
         * White balance.
         * Shade
         */
        SHADE(WhiteBalance.SHADE),

        /**
         * White balance.
         * Cloudy
         */
        CLOUDY_DAYLIGHT(WhiteBalance.CLOUDY_DAYLIGHT),

        /**
         * White balance.
         * Incandescent light 1
         */
        INCANDESCENT(WhiteBalance.INCANDESCENT),

        /**
         * White balance.
         * Incandescent light 2
         */
        WARM_WHITE_FLUORESCENT(WhiteBalance._WARM_WHITE_FLUORESCENT),

        /**
         * White balance.
         * Fluorescent light 1 (daylight)
         */
        DAYLIGHT_FLUORESCENT(WhiteBalance._DAYLIGHT_FLUORESCENT),

        /**
         * White balance.
         * Fluorescent light 2 (natural white)
         */
        DAYWHITE_FLUORESCENT(WhiteBalance._DAYWHITE_FLUORESCENT),

        /**
         * White balance.
         * Fluorescent light 3 (white)
         */
        FLUORESCENT(WhiteBalance.FLUORESCENT),

        /**
         * White balance.
         * Fluorescent light 4 (light bulb color)
         */
        BULB_FLUORESCENT(WhiteBalance._BULB_FLUORESCENT),

        /**
         * White balance.
         * CT settings (specified by the _colorTemperature option)
         *
         * RICOH THETA S firmware v01.82 or later and RICOH THETA SC firmware v01.10 or later
         */
        COLOR_TEMPERATURE(WhiteBalance._COLOR_TEMPERATURE),

        /**
         * White balance.
         * Underwater
         *
         * RICOH THETA V firmware v3.21.1 or later
         */
        UNDERWATER(WhiteBalance._UNDERWATER);

        companion object {
            /**
             * Convert WhiteBalance to WhiteBalanceEnum
             *
             * @param value Maximum recordable time.
             * @return WhiteBalanceEnum
             */
            fun get(value: WhiteBalance): WhiteBalanceEnum? {
                return values().firstOrNull { it.value == value }
            }
        }
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
    enum class WhiteBalanceAutoStrengthEnum(val value: WhiteBalanceAutoStrength) {
        /**
         * correct tint for low color temperature scene
         */
        ON(WhiteBalanceAutoStrength.ON),

        /**
         * not correct tint for low color temperature scene
         */
        OFF(WhiteBalanceAutoStrength.OFF);

        companion object {
            /**
             * Convert WhiteBalanceAutoStrength to WhiteBalanceAutoStrengthEnum
             *
             * @param value White balance auto strength
             * @return WhiteBalanceAutoStrengthEnum
             */
            fun get(value: WhiteBalanceAutoStrength): WhiteBalanceAutoStrengthEnum? {
                return values().firstOrNull { it.value == value }
            }
        }
    }

    /**
     * Wireless LAN frequency of the camera supported by Theta V, Z1 and X.
     */
    enum class WlanFrequencyEnum(val value: WlanFrequency) {
        /**
         * 2.4GHz
         */
        GHZ_2_4(WlanFrequency.GHZ_2_4),

        /**
         * 5GHz
         */
        GHZ_5(WlanFrequency.GHZ_5);

        companion object {
            /**
             * Convert WlanFrequency to WlanFrequencyEnum
             *
             * @param value wlan frequency
             * @return  WlanFrequencyEnum
             */
            fun get(value: WlanFrequency): WlanFrequencyEnum? {
                return values().firstOrNull { it.value == value }
            }
        }

    }

    /**
     * File type in Theta.
     */
    enum class FileTypeEnum(val value: FileType) {
        /**
         * File type in Theta.
         *
         * all files.
         */
        ALL(FileType.ALL),

        /**
         * File type in Theta.
         *
         * still image files.
         */
        IMAGE(FileType.IMAGE),

        /**
         * File type in Theta.
         *
         * video files.
         */
        VIDEO(FileType.VIDEO)
    }

    /**
     * Specifies the storage
     */
    enum class StorageEnum(val value: Storage) {
        /**
         * internal storage
         */
        INTERNAL(Storage.IN),

        /**
         * external storage (SD card)
         */
        SD(Storage.SD),

        /**
         * current storage
         */
        CURRENT(Storage.DEFAULT),
    }

    /**
     * File information in Theta.
     * @property name File name.
     * @property size File size in bytes.
     * @property dateTime File creation time in the format "YYYY:MM:DD HH:MM:SS".
     * @property fileUrl You can get a file using HTTP GET to [fileUrl].
     * @property thumbnailUrl You can get a thumbnail image using HTTP GET to [thumbnailUrl].
     * @property storageID Storage ID. (RICOH THETA X Version 2.00.0 or later)
     */
    data class FileInfo(
        val name: String,
        val size: Long,
        val dateTime: String,
        val fileUrl: String,
        val thumbnailUrl: String,
        val storageID: String?,
    ) {
        constructor(cameraFileInfo: CameraFileInfo) : this(
            cameraFileInfo.name,
            cameraFileInfo.size,
            cameraFileInfo.dateTimeZone!!.take(16), // Delete timezone
            cameraFileInfo.fileUrl,
            thumbnailUrl = cameraFileInfo.getThumbnailUrl(),
            cameraFileInfo._storageID,
        )
    }

    /**
     * Start live preview as motion JPEG.
     *
     * @return You can get the newest frame in a CoroutineScope like this:
     * ```kotlin
     * getLivePreview()
     *     .conflate()
     *     .collect { byteReadPacket ->
     *         if (isActive) {
     *             // Read byteReadPacket
     *         }
     *         byteReadPacket.release()
     *     }
     * ```
     */
    @Throws(Throwable::class)
    fun getLivePreview(): Flow<ByteReadPacket> {
        try {
            return ThetaApi.callGetLivePreviewCommand(endpoint)
        } catch (e: PreviewClientException) {
            throw ThetaWebApiException("PreviewClientException")
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Start live preview as motion JPEG.
     *
     * @param[frameHandler] Called for each JPEG frame.
     * If [frameHandler] returns false, live preview finishes.
     * @exception ThetaWebApiException Command is currently disabled; for example, the camera is shooting a video.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun getLivePreview(frameHandler: suspend (Pair<ByteArray, Int>) -> Boolean) {
        try {
            ThetaApi.callGetLivePreviewCommand(endpoint) {
                return@callGetLivePreviewCommand frameHandler(it)
            }
        } catch (e: PreviewClientException) {
            throw ThetaWebApiException("PreviewClientException")
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Get PhotoCapture.Builder for take a picture.
     *
     * @return PhotoCapture.Builder
     */
    fun getPhotoCaptureBuilder(): PhotoCapture.Builder {
        return PhotoCapture.Builder(endpoint)
    }

    /**
     * Get PhotoCapture.Builder for capture video.
     *
     * @return PhotoCapture.Builder
     */
    fun getVideoCaptureBuilder(): VideoCapture.Builder {
        return VideoCapture.Builder(endpoint)
    }

    /**
     * Base exception of ThetaRepository
     */
    abstract class ThetaRepositoryException(message: String) : RuntimeException(message)

    /**
     * Thrown if an error occurs on Theta Web API.
     */
    class ThetaWebApiException(message: String) : ThetaRepositoryException(message) {
        companion object {
            suspend inline fun create(exception: ResponseException): ThetaWebApiException {
                val message = try {
                    val response: UnknownResponse = exception.response.body()
                    response.error?.message ?: exception.message ?: exception.toString()
                } catch (e: Exception) {
                    exception.message ?: exception.toString()
                }
                return ThetaWebApiException(message)
            }
        }
    }

    /**
     * Thrown if the mobile device doesn't connect to Theta.
     */
    class NotConnectedException(message: String) : ThetaRepositoryException(message)

    /**
     * Thrown if the argument wrong.
     */
    class ArgumentException(message: String) : ThetaRepositoryException(message)

    /**
     * Thrown if an authentication　error occurs in client mode.
     */
    class ThetaUnauthorizedException(message: String) : ThetaRepositoryException(message)

    /**
     * Static attributes of Theta.
     *
     * @property manufacturer Manufacturer name
     * @property model Theta model name
     * @property serialNumber Theta serial number
     * @property wlanMacAddress MAC address of wireless LAN (RICOH THETA V firmware v2.11.1 or later)
     * @property bluetoothMacAddress MAC address of Bluetooth (RICOH THETA V firmware v2.11.1 or later)
     * @property firmwareVersion Theta firmware version
     * @property supportUrl URL of the support page
     * @property hasGps True if Theta has GPS.
     * @property hasGyro True if Theta has Gyroscope
     * @property uptime Number of seconds since Theta boot
     * @property api List of supported APIs
     * @property endpoints Endpoint information
     * @property apiLevel List of supported APIs (1: v2.0, 2: v2.1)
     */
    data class ThetaInfo(
        val manufacturer: String,
        val model: String,
        val serialNumber: String,
        val wlanMacAddress: String?,
        val bluetoothMacAddress: String?,
        val firmwareVersion: String,
        val supportUrl: String,
        val hasGps: Boolean,
        val hasGyro: Boolean,
        val uptime: Int,
        val api: List<String>,
        val endpoints: EndPoint,
        val apiLevel: List<Int>,
    ) {
        constructor(res: InfoApiResponse) : this(
            manufacturer = res.manufacturer,
            model = res.model,
            serialNumber = res.serialNumber,
            wlanMacAddress = res._wlanMacAddress,
            bluetoothMacAddress = res._bluetoothMacAddress,
            firmwareVersion = res.firmwareVersion,
            supportUrl = res.supportUrl,
            hasGps = res.gps,
            hasGyro = res.gyro,
            uptime = res.uptime,
            api = res.api,
            endpoints = res.endpoints,
            apiLevel = res.apiLevel
        )
    }

    /**
     * Mutable values representing Theta status.
     *
     * @property fingerprint Fingerprint (unique identifier) of the current camera state
     * @property batteryLevel Battery level between 0.0 and 1.0
     * @property storageUri Storage URI
     * @property storageID Storage ID
     * @property captureStatus Continuously shoots state
     * @property recordedTime Recorded time of movie (seconds)
     * @property recordableTime Recordable time of movie (seconds)
     * @property capturedPictures Number of still images captured during continuous shooting, Unit: images
     * @property compositeShootingElapsedTime Elapsed time for interval composite shooting (sec)
     * @property latestFileUrl URL of the last saved file
     * @property chargingState Charging state
     * @property apiVersion API version currently set (1: v2.0, 2: v2.1)
     * @property isPluginRunning Plugin running state (true: running, false: stop)
     * @property isPluginWebServer Plugin web server state (true: enabled, false: disabled)
     * @property function Shooting function status
     * @property isMySettingChanged My setting changed state
     * @property currentMicrophone Identifies the microphone used while recording video
     * @property isSdCard True if record to SD card
     * @property cameraError Error information of the camera
     * @property isBatteryInsert true: Battery inserted; false: Battery not inserted
     */
    data class ThetaState(
        val fingerprint: String,
        val batteryLevel: Float,
        val storageUri: String?,
        val storageID: String?,
        val captureStatus: CaptureStatusEnum,
        val recordedTime: Int,
        val recordableTime: Int,
        val capturedPictures: Int?,
        val compositeShootingElapsedTime: Int?,
        val latestFileUrl: String,
        val chargingState: ChargingStateEnum,
        val apiVersion: Int,
        val isPluginRunning: Boolean?,
        val isPluginWebServer: Boolean?,
        val function: ShootingFunctionEnum?,
        val isMySettingChanged: Boolean?,
        val currentMicrophone: MicrophoneOptionEnum?,
        val isSdCard: Boolean,
        val cameraError: List<CameraErrorEnum>?,
        val isBatteryInsert: Boolean?
    ) {
        constructor(response: StateApiResponse) : this(
            response.fingerprint,
            response.state.batteryLevel.toFloat(),
            response.state.storageUri,
            response.state._storageID,
            CaptureStatusEnum.get(response.state._captureStatus),
            response.state._recordedTime,
            response.state._recordedTime,
            response.state._capturedPictures,
            response.state._compositeShootingElapsedTime,
            response.state._latestFileUrl ?: "",
            ChargingStateEnum.get(response.state._batteryState),
            response.state._apiVersion,
            response.state._pluginRunning,
            response.state._pluginWebServer,
            response.state._function?.let { ShootingFunctionEnum.get(response.state._function) },
            response.state._mySettingChanged,
            response.state._currentMicrophone?.let { MicrophoneOptionEnum.get(response.state._currentMicrophone) },
            response.state._currentStorage == StorageOption.SD,
            response.state._cameraError?.map { it -> CameraErrorEnum.get(it) },
            response.state._batteryInsert
        )
    }

    /**
     *  Capture Status
     */
    enum class CaptureStatusEnum {
        /**
         * Capture status
         * Performing continuously shoot
         */
        SHOOTING,

        /**
         * Capture status
         * In standby
         */
        IDLE,

        /**
         * Capture status
         * Self-timer is operating
         */
        SELF_TIMER_COUNTDOWN,

        /**
         * Capture status
         * Performing multi bracket shooting
         */
        BRACKET_SHOOTING,

        /**
         * Capture status
         * Converting post file...
         */
        CONVERTING,

        /**
         * Capture status
         * Performing timeShift shooting
         */
        TIME_SHIFT_SHOOTING,

        /**
         * Capture status
         * Performing continuous shooting
         */
        CONTINUOUS_SHOOTING,

        /**
         * Capture status
         * Waiting for retrospective video...
         */
        RETROSPECTIVE_IMAGE_RECORDING;

        companion object {
            /**
             * Convert value to CaptureStatus
             *
             * @param captureStatus Capture status.
             * @return CaptureStatusEnum
             */
            fun get(captureStatus: CaptureStatus): CaptureStatusEnum {
                return when (captureStatus) {
                    CaptureStatus.SHOOTING -> SHOOTING
                    CaptureStatus.IDLE -> IDLE
                    CaptureStatus.SELF_TIMER_COUNTDOWN -> SELF_TIMER_COUNTDOWN
                    CaptureStatus.BRACKET_SHOOTING -> BRACKET_SHOOTING
                    CaptureStatus.CONVERTING -> CONVERTING
                    CaptureStatus.TIME_SHIFT_SHOOTING -> TIME_SHIFT_SHOOTING
                    CaptureStatus.CONTINUOUS_SHOOTING -> CONTINUOUS_SHOOTING
                    CaptureStatus.RETROSPECTIVE_IMAGE_RECORDING -> RETROSPECTIVE_IMAGE_RECORDING
                }
            }
        }
    }

    /**
     * Battery charging state
     */
    enum class ChargingStateEnum {
        /**
         * Battery charging state
         * Charging
         */
        CHARGING,

        /**
         * Battery charging state
         * Charging completed
         */
        COMPLETED,

        /**
         * Battery charging state
         * Not charging
         */
        NOT_CHARGING;

        companion object {
            /**
             * Convert value to ChargingState
             *
             * @param chargingState Charging state.
             * @return ChargingStateEnum
             */
            fun get(chargingState: ChargingState): ChargingStateEnum {
                return when (chargingState) {
                    ChargingState.CHARGING -> CHARGING
                    ChargingState.CHARGED -> COMPLETED
                    ChargingState.DISCONNECT -> NOT_CHARGING
                }
            }
        }
    }

    /**
     * Shooting function status
     */
    enum class ShootingFunctionEnum {
        /**
         * Shooting function status
         * normal
         */
        NORMAL,

        /**
         * Shooting function status
         * selfTimer
         */
        SELF_TIMER,

        /**
         * Shooting function status
         * mySetting
         */
        MY_SETTING;

        companion object {
            /**
             * Convert value to ShootingFunction
             *
             * @param shootingFunction Shooting function.
             * @return ShootingFunctionEnum
             */
            fun get(shootingFunction: ShootingFunction): ShootingFunctionEnum {
                return when (shootingFunction) {
                    ShootingFunction.NORMAL -> NORMAL
                    ShootingFunction.SELF_TIMER -> SELF_TIMER
                    ShootingFunction.MY_SETTING -> MY_SETTING
                }
            }
        }
    }

    /**
     * Microphone option
     */
    enum class MicrophoneOptionEnum {
        /**
         * Microphone option
         * auto
         */
        AUTO,

        /**
         * Microphone option
         * built-in microphone
         */
        INTERNAL,

        /**
         * Microphone option
         * external microphone
         */
        EXTERNAL;

        companion object {
            /**
             * Convert value to MicrophoneOption
             *
             * @param microphoneOption Microphone option.
             * @return MicrophoneOptionEnum
             */
            fun get(microphoneOption: MicrophoneOption): MicrophoneOptionEnum {
                return when (microphoneOption) {
                    MicrophoneOption.AUTO -> AUTO
                    MicrophoneOption.INTERNAL -> INTERNAL
                    MicrophoneOption.EXTERNAL -> EXTERNAL
                }
            }
        }
    }

    /**
     * Camera error
     */
    enum class CameraErrorEnum {
        /**
         * Camera error
         * Insufficient memory
         */
        NO_MEMORY,

        /**
         * Camera error
         * Maximum file number exceeded
         */
        FILE_NUMBER_OVER,

        /**
         * Camera error
         * Camera clock not set
         */
        NO_DATE_SETTING,

        /**
         * Camera error
         * Includes when the card is removed
         */
        READ_ERROR,

        /**
         * Camera error
         * Unsupported media (SDHC, etc.)
         */
        NOT_SUPPORTED_MEDIA_TYPE,

        /**
         * Camera error
         * FAT32, etc.
         */
        NOT_SUPPORTED_FILE_SYSTEM,

        /**
         * Camera error
         * Error warning while mounting
         */
        MEDIA_NOT_READY,

        /**
         * Camera error
         * Battery level warning (firmware update)
         */
        NOT_ENOUGH_BATTERY,

        /**
         * Camera error
         * Firmware file mismatch warning
         */
        INVALID_FILE,

        /**
         * Camera error
         * Plugin start warning (IoT technical standards compliance)
         */
        PLUGIN_BOOT_ERROR,

        /**
         * Camera error
         * When performing continuous shooting by operating the camera while executing <Delete object>,
         * <Transfer firmware file>, <Install plugin> or <Uninstall plugin> with the WebAPI or MTP.
         */
        IN_PROGRESS_ERROR,

        /**
         * Camera error
         * Battery inserted + WLAN ON + Video mode + 4K 60fps / 5.7K 10fps / 5.7K 15fps / 5.7K 30fps / 8K 10fps
         */
        CANNOT_RECORDING,

        /**
         * Camera error
         * Battery inserted AND Specified battery level or lower + WLAN ON + Video mode + 4K 30fps
         */
        CANNOT_RECORD_LOWBAT,

        /**
         * Camera error
         * Shooting hardware failure
         */
        CAPTURE_HW_FAILED,

        /**
         * Camera error
         * Software error
         */
        CAPTURE_SW_FAILED,

        /**
         * Camera error
         * Internal memory access error
         */
        INTERNAL_MEM_ACCESS_FAIL,

        /**
         * Camera error
         * Undefined error
         */
        UNEXPECTED_ERROR,

        /**
         * Camera error
         * Charging error
         */
        BATTERY_CHARGE_FAIL,

        /**
         * Camera error
         * (Board) temperature warning
         */
        HIGH_TEMPERATURE_WARNING,

        /**
         * Camera error
         * (Board) temperature error
         */
        HIGH_TEMPERATURE,

        /**
         * Camera error
         * Battery temperature error
         */
        BATTERY_HIGH_TEMPERATURE,

        /**
         * Camera error
         * Electronic compass error
         */
        COMPASS_CALIBRATION;

        companion object {
            /**
             * Convert value to CameraError
             *
             * @param cameraError Camera error.
             * @return CameraErrorEnum
             */
            fun get(cameraError: CameraError): CameraErrorEnum {
                return when (cameraError) {
                    CameraError.NO_MEMORY -> NO_MEMORY
                    CameraError.FILE_NUMBER_OVER -> FILE_NUMBER_OVER
                    CameraError.NO_DATE_SETTING -> NO_DATE_SETTING
                    CameraError.READ_ERROR -> READ_ERROR
                    CameraError.NOT_SUPPORTED_MEDIA_TYPE -> NOT_SUPPORTED_MEDIA_TYPE
                    CameraError.NOT_SUPPORTED_FILE_SYSTEM -> NOT_SUPPORTED_FILE_SYSTEM
                    CameraError.MEDIA_NOT_READY -> MEDIA_NOT_READY
                    CameraError.NOT_ENOUGH_BATTERY -> NOT_ENOUGH_BATTERY
                    CameraError.INVALID_FILE -> INVALID_FILE
                    CameraError.PLUGIN_BOOT_ERROR -> PLUGIN_BOOT_ERROR
                    CameraError.IN_PROGRESS_ERROR -> IN_PROGRESS_ERROR
                    CameraError.CANNOT_RECORDING -> CANNOT_RECORDING
                    CameraError.CANNOT_RECORD_LOWBAT -> CANNOT_RECORD_LOWBAT
                    CameraError.CAPTURE_HW_FAILED -> CAPTURE_HW_FAILED
                    CameraError.CAPTURE_SW_FAILED -> CAPTURE_SW_FAILED
                    CameraError.INTERNAL_MEM_ACCESS_FAIL -> INTERNAL_MEM_ACCESS_FAIL
                    CameraError.UNEXPECTED_ERROR -> UNEXPECTED_ERROR
                    CameraError.BATTERY_CHARGE_FAIL -> BATTERY_CHARGE_FAIL
                    CameraError.HIGH_TEMPERATURE_WARNING -> HIGH_TEMPERATURE_WARNING
                    CameraError.HIGH_TEMPERATURE -> HIGH_TEMPERATURE
                    CameraError.BATTERY_HIGH_TEMPERATURE -> BATTERY_HIGH_TEMPERATURE
                    CameraError.COMPASS_CALIBRATION -> COMPASS_CALIBRATION
                }
            }
        }
    }

    /**
     * Get metadata of a still image
     *
     * This command cannot be executed during video recording.
     * RICOH THETA V firmware v2.00.2 or later
     *
     * @param[fileUrl] URL of a still image file
     * @return Exif and [photo sphere XMP](https://developers.google.com/streetview/spherical-metadata/)
     * @exception ThetaWebApiException Command is currently disabled; for example, the camera is shooting a video.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun getMetadata(fileUrl: String): Pair<Exif, Xmp> {
        try {
            val params = GetMetadataParams(fileUrl)
            val metadataResponse = ThetaApi.callGetMetadataCommand(endpoint, params)
            metadataResponse.error?.let {
                throw ThetaWebApiException(it.message)
            }
            return Exif(metadataResponse.results!!.exif) to Xmp(metadataResponse.results.xmp)
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Exif metadata of a still image.
     *
     * @property exifVersion EXIF Support version
     * @property dateTime File created or updated date and time
     * @property imageWidth Image width (pixel). Theta X returns null.
     * @property imageLength Image height (pixel). Theta X returns null.
     * @property gpsLatitude GPS latitude if exists.
     * @property gpsLongitude GPS longitude if exists.
     */
    data class Exif(
        val exifVersion: String,
        val dateTime: String,
        val imageWidth: Int?,
        val imageLength: Int?,
        val gpsLatitude: Double?,
        val gpsLongitude: Double?
    ) {
        constructor(exif: ExifInfo) : this(
            exifVersion = exif.ExifVersion,
            dateTime = exif.DateTime,
            imageWidth = exif.ImageWidth,
            imageLength = exif.ImageLength,
            gpsLatitude = exif.GPSLatitude,
            gpsLongitude = exif.GPSLongitude
        )
    }

    /**
     * Photo sphere XMP metadata of a still image.
     *
     * @property poseHeadingDegrees Compass heading, for the center the image. Theta X returns null.
     * @property fullPanoWidthPixels Image width (pixel).
     * @property fullPanoHeightPixels Image height (pixel).
     */
    data class Xmp(
        val poseHeadingDegrees: Double?,
        val fullPanoWidthPixels: Int,
        val fullPanoHeightPixels: Int
    ) {
        constructor(xmp: XmpInfo) : this(
            poseHeadingDegrees = xmp.PoseHeadingDegrees,
            fullPanoWidthPixels = xmp.FullPanoWidthPixels,
            fullPanoHeightPixels = xmp.FullPanoHeightPixels
        )
    }

    /**
     * Reset all device settings and capture settings.
     * After reset, the camera will be restarted.
     *
     * @exception ThetaWebApiException If an error occurs in THETA.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun reset() {
        try {
            ThetaApi.callResetCommand(endpoint).error?.let {
                throw ThetaWebApiException(it.message)
            }
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Stop running self-timer.
     *
     * @exception ThetaWebApiException If an error occurs in THETA.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun stopSelfTimer() {
        try {
            ThetaApi.callStopSelfTimerCommand(endpoint).error?.let {
                throw ThetaWebApiException(it.message)
            }
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Converts the movie format of a saved movie.
     *
     * Theta S and Theta SC don't support this functionality, so always [fileUrl] is returned.
     *
     * @param fileUrl URL of a saved movie file.
     * @param toLowResolution If true generates lower resolution video, otherwise same resolution.
     * @param applyTopBottomCorrection apply Top/bottom correction. This parameter is ignored on Theta X.
     * @return URL of a converted movie file.
     * @exception ThetaWebApiException Command is currently disabled.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun convertVideoFormats(fileUrl: String, toLowResolution: Boolean, applyTopBottomCorrection: Boolean = true): String {
        val params = when (ThetaModel.get(cameraModel)) {
            ThetaModel.THETA_X -> {
                if (!toLowResolution) {
                    return fileUrl
                }
                ConvertVideoFormatsParams(
                    fileUrl = fileUrl,
                    size = VideoFormat.VIDEO_4K
                )
            }
            ThetaModel.THETA_S, ThetaModel.THETA_SC, ThetaModel.THETA_SC2 -> {
                return fileUrl
            }
            else -> {
                ConvertVideoFormatsParams(
                    fileUrl = fileUrl,
                    size = if (toLowResolution) VideoFormat.VIDEO_2K else VideoFormat.VIDEO_4K,
                    projectionType = _ProjectionType.EQUIRECTANGULAR,
                    codec = "H.264/MPEG-4 AVC",
                    topBottomCorrection = if (applyTopBottomCorrection) TopBottomCorrection.APPLY else TopBottomCorrection.DISAPPLY
                )
            }
        }
        lateinit var convertVideoFormatsResponse: ConvertVideoFormatsResponse
        try {
            convertVideoFormatsResponse = ThetaApi.callConvertVideoFormatsCommand(endpoint, params)
            val id = convertVideoFormatsResponse.id
            while (convertVideoFormatsResponse.state == CommandState.IN_PROGRESS) {
                delay(CHECK_COMMAND_STATUS_INTERVAL)
                convertVideoFormatsResponse = ThetaApi.callStatusApi(
                    endpoint,
                    StatusApiParams(id = id)
                ) as ConvertVideoFormatsResponse
            }
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }

        if (convertVideoFormatsResponse.state == CommandState.DONE) {
            return convertVideoFormatsResponse.results!!.fileUrl
        }

        throw ThetaWebApiException(convertVideoFormatsResponse.error!!.message)
    }

    /**
     * Cancels the movie format conversion.
     *
     * @exception ThetaWebApiException When convertVideoFormats is not started.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun cancelVideoConvert() {
        try {
            ThetaApi.callCancelVideoConvertCommand(endpoint).error?.let {
                throw ThetaWebApiException(it.message)
            }
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Turns the wireless LAN off.
     *
     * @exception ThetaWebApiException If an error occurs in THETA.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun finishWlan() {
        try {
            ThetaApi.callFinishWlanCommand(endpoint).error?.let {
                throw ThetaWebApiException(it.message)
            }
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Acquires the access point list used in client mode.
     *
     * For RICOH THETA X, only the access points registered with [setAccessPoint] can be acquired.
     * (The access points automatically detected with the camera UI cannot be acquired with this API.)
     *
     * @return Lists the access points stored on the camera and the access points detected by the camera.
     * @exception ThetaWebApiException If an error occurs in THETA.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun listAccessPoints(): List<AccessPoint> {
        try {
            val listAccessPointsResponse = ThetaApi.callListAccessPointsCommand(endpoint)
            listAccessPointsResponse.error?.let {
                throw ThetaWebApiException(it.message)
            }
            val accessPointList = mutableListOf<AccessPoint>()
            listAccessPointsResponse.results!!.accessPoints.forEach {
                accessPointList.add(AccessPoint(it))
            }
            return accessPointList
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Set access point. IP address is set statically.
     *
     * @param ssid SSID of the access point.
     * @param ssidStealth True if SSID stealth is enabled.
     * @param authMode Authentication mode.
     * @param password Password. If [authMode] is "NONE", pass empty String.
     * @param ipAddressAllocation [IpAddressAllocation] IP address allocation. DYNAMIC or STATIC.
     * @param connectionPriority Connection priority 1 to 5. Theta X fixes to 1 (The access point registered later has a higher priority.)
     * @param ipAddress IP address assigns to Theta. If DYNAMIC ip is null.
     * @param subnetMask Subnet mask. If DYNAMIC ip is null.
     * @param defaultGateway Default gateway. If DYNAMIC ip is null.
     * @exception ThetaWebApiException If an error occurs in THETA.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    internal suspend fun setAccessPoint(
        ssid: String,
        ssidStealth: Boolean = false,
        authMode: AuthModeEnum = AuthModeEnum.NONE,
        password: String? = null,
        connectionPriority: Int = 1,
        ipAddressAllocation: IpAddressAllocation,
        ipAddress: String? = null,
        subnetMask: String? = null,
        defaultGateway: String? = null
    ) {
        val params = SetAccessPointParams(
            ssid = ssid,
            ssidStealth = ssidStealth,
            security = authMode.value,
            password = password,
            connectionPriority = connectionPriority,
            ipAddressAllocation = ipAddressAllocation,
            ipAddress = ipAddress,
            subnetMask = subnetMask,
            defaultGateway = defaultGateway
        )
        try {
            ThetaApi.callSetAccessPointCommand(endpoint, params).error?.let {
                throw ThetaWebApiException(it.message)
            }
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Set access point. IP address is set dynamically.
     *
     * @param ssid SSID of the access point.
     * @param ssidStealth True if SSID stealth is enabled.
     * @param authMode Authentication mode.
     * @param password Password. If [authMode] is "NONE", pass empty String.
     * @param connectionPriority Connection priority 1 to 5. Theta X fixes to 1 (The access point registered later has a higher priority.)
     * @exception ThetaWebApiException If an error occurs in THETA.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun setAccessPointDynamically(
        ssid: String,
        ssidStealth: Boolean = false,
        authMode: AuthModeEnum = AuthModeEnum.NONE,
        password: String = "",
        connectionPriority: Int = 1
    ) {
        setAccessPoint(
            ssid = ssid,
            ssidStealth = ssidStealth,
            authMode = authMode,
            password = password,
            connectionPriority = connectionPriority,
            ipAddressAllocation = IpAddressAllocation.DYNAMIC
        )
    }

    /**
     * Set access point. IP address is set statically.
     *
     * @param ssid SSID of the access point.
     * @param ssidStealth True if SSID stealth is enabled.
     * @param authMode Authentication mode.
     * @param password Password. If [authMode] is "NONE", pass empty String.
     * @param connectionPriority Connection priority 1 to 5. Theta X fixes to 1 (The access point registered later has a higher priority.)
     * @param ipAddress IP address assigns to Theta.
     * @param subnetMask Subnet mask.
     * @param defaultGateway Default gateway.
     * @exception ThetaWebApiException If an error occurs in THETA.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun setAccessPointStatically(
        ssid: String,
        ssidStealth: Boolean = false,
        authMode: AuthModeEnum = AuthModeEnum.NONE,
        password: String? = null,
        connectionPriority: Int = 1,
        ipAddress: String,
        subnetMask: String,
        defaultGateway: String
    ) {
        setAccessPoint(
            ssid = ssid,
            ssidStealth = ssidStealth,
            authMode = authMode,
            password = password,
            connectionPriority = connectionPriority,
            ipAddressAllocation = IpAddressAllocation.STATIC,
            ipAddress = ipAddress,
            subnetMask = subnetMask,
            defaultGateway = defaultGateway
        )
    }

    /**
     * Deletes access point information used in client mode.
     * Only the access points registered with [setAccessPoint] can be deleted.
     *
     * @param ssid SSID of the access point to delete.
     * @exception ThetaWebApiException When the specified SSID does not exist.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun deleteAccessPoint(ssid: String) {
        try {
            val params = DeleteAccessPointParams(ssid)
            ThetaApi.callDeleteAccessPointCommand(endpoint, params).error?.let {
                throw ThetaWebApiException(it.message)
            }
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Access point information.
     *
     * @property ssid SSID of the access point.
     * @property ssidStealth True if SSID stealth is enabled.
     * @property authMode Authentication mode.
     * @property connectionPriority Connection priority 1 to 5. Theta X fixes to 1 (The access point registered later has a higher priority.)
     * @property usingDhcp Using DHCP or not. This can be acquired when SSID is registered as an enable access point.
     * @property ipAddress IP address assigned to camera. This setting can be acquired when “usingDhcp” is false.
     * @property subnetMask Subnet Mask. This setting can be acquired when “usingDhcp” is false.
     * @property defaultGateway Default Gateway. This setting can be acquired when “usingDhcp” is false.
     */
    data class AccessPoint(
        val ssid: String,
        val ssidStealth: Boolean,
        val authMode: AuthModeEnum,
        val connectionPriority: Int = 1,
        val usingDhcp: Boolean,
        val ipAddress: String?,
        val subnetMask: String?,
        val defaultGateway: String?
    ) {
        constructor(accessPoint: com.ricoh360.thetaclient.transferred.AccessPoint) : this(
            ssid = accessPoint.ssid,
            ssidStealth = accessPoint.ssidStealth,
            authMode = AuthModeEnum.get(accessPoint.security)!!,
            connectionPriority = accessPoint.connectionPriority,
            usingDhcp = accessPoint.ipAddressAllocation == IpAddressAllocation.DYNAMIC,
            ipAddress = accessPoint.ipAddress,
            subnetMask = accessPoint.subnetMask,
            defaultGateway = accessPoint.defaultGateway
        )
    }

    /**
     * Enum for authentication mode.
     *
     * @property value AuthenticationMode.
     */
    enum class AuthModeEnum(val value: AuthenticationMode) {
        /**
         * Authentication mode
         * none
         */
        NONE(AuthenticationMode.NONE),

        /**
         * Authentication mode
         * WEP
         */
        WEP(AuthenticationMode.WEP),

        /**
         * Authentication mode
         * WPA/WPA2 PSK
         */
        WPA(AuthenticationMode.WPA_WPA2_PSK);

        companion object {
            /**
             * Convert AuthenticationMode to AuthModeEnum
             *
             * @param value AuthenticationMode.
             * @return AuthModeEnum
             */
            fun get(value: AuthenticationMode): AuthModeEnum? {
                return values().firstOrNull { it.value == value }
            }
        }
    }

    /**
     * Plugin information
     *
     * @property name Plugin name
     * @property packageName Package name
     * @property version Plugin version
     * @property isPreInstalled Pre-installed plugin or not
     * @property isRunning Plugin power status
     * @property isForeground Process status
     * @property isBoot To be started on boot or not
     * @property hasWebServer Has Web UI or not
     * @property exitStatus Exit status
     * @property message Message
     */
    data class PluginInfo(
        val name: String,
        val packageName: String,
        val version: String,
        val isPreInstalled: Boolean,
        val isRunning: Boolean,
        val isForeground: Boolean,
        val isBoot: Boolean,
        val hasWebServer: Boolean,
        val exitStatus: String,
        val message: String,
    ) {
        constructor(plugin: Plugin) : this(
            name = plugin.pluginName,
            packageName = plugin.packageName,
            version = plugin.version,
            isPreInstalled = plugin.type == "system",
            isRunning = plugin.running,
            isForeground = plugin.foreground,
            isBoot = plugin.boot,
            hasWebServer = plugin.webServer,
            exitStatus = plugin.exitStatus,
            message = plugin.message,
        )
    }

    /**
     * Acquires the shooting properties set by the camera._setMySetting command.
     * Just for Theta V and later.
     *
     * Refer to the [options Overview](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/options.md)
     * of API v2.1 reference  for properties available for acquisition.
     *
     * @param captureMode The target shooting mode.
     * @return Options of my setting
     * @exception ThetaWebApiException When an invalid option is specified.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun getMySetting(captureMode: CaptureModeEnum): Options {
        try {
            val params = GetMySettingParams(mode = captureMode.value)
            val getMySettingResponse = ThetaApi.callGetMySettingCommand(endpoint, params)
            getMySettingResponse.error?.let {
                throw ThetaWebApiException(it.message)
            }
            return Options(getMySettingResponse.results!!.options)
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Acquires the shooting properties set by the camera._setMySetting command.
     * Just for Theta S and SC.
     *
     * Refer to the [options Overview](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/options.md)
     * of API v2.1 reference  for properties available for acquisition.
     *
     * @param optionNames List of option names to acquire.
     * @return Options of my setting
     * @exception ThetaWebApiException When an invalid option is specified.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun getMySetting(optionNames: List<OptionNameEnum>): Options {
        try {
            val names = optionNames.map {
                it.value
            }
            val params = GetMySettingParams(optionNames = names)
            val getMySettingResponse = ThetaApi.callGetMySettingCommand(endpoint, params)
            getMySettingResponse.error?.let {
                throw ThetaWebApiException(it.message)
            }
            return Options(getMySettingResponse.results!!.options)
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Registers shooting conditions in My Settings.
     *
     * @param captureMode The target shooting mode.  RICOH THETA S and SC do not support My Settings in video capture mode.
     * @param options registered to My Settings.
     * @exception ThetaWebApiException When an invalid option is specified.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun setMySetting(captureMode: CaptureModeEnum, options: Options) {
        try {
            val params = SetMySettingParams(captureMode.value, options.toOptions())
            val setMySettingResponse = ThetaApi.callSetMySettingCommand(endpoint, params)
            setMySettingResponse.error?.let {
                throw ThetaWebApiException(it.message)
            }
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Delete shooting conditions in My Settings. Supported just by Theta X and Z1.
     *
     * @param captureMode The target shooting mode.
     * @exception ThetaWebApiException When an invalid option is specified.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun deleteMySetting(captureMode: CaptureModeEnum) {
        try {
            val params = DeleteMySettingParams(captureMode.value)
            val deleteMySettingResponse = ThetaApi.callDeleteMySettingCommand(endpoint, params)
            deleteMySettingResponse.error?.let {
                throw ThetaWebApiException(it.message)
            }
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Acquires a list of installed plugins. Supported just by Theta X, Z1 and V.
     * @return a list of installed plugin information
     * @exception ThetaWebApiException When an invalid option is specified.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun listPlugins(): List<PluginInfo> {
        val listPluginsResponse: ListPluginsResponse
        try {
            listPluginsResponse = ThetaApi.callListPluginsCommand(endpoint)
            listPluginsResponse.error?.let {
                throw ThetaWebApiException(it.message)
            }
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }

        return listPluginsResponse.results!!.plugins.map {
            PluginInfo(it)
        }
    }

    /**
     * Sets the installed plugin for boot. Supported just by Theta V.
     *
     * @param packageName package name of the plugin
     * @exception ThetaWebApiException When an invalid option is specified.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun setPlugin(packageName: String) {
        try {
            val params = SetPluginParams(packageName, true)
            val setPluginResponse = ThetaApi.callSetPluginCommand(endpoint, params)
            setPluginResponse.error?.let {
                throw ThetaWebApiException(it.message)
            }
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Start the plugin specified by the [packageName].
     * If [packageName] is not specified, plugin 1 will start.
     * Supported just by Theta X, Z1 and V.
     *
     * @param packageName package name of the plugin.  Theta V does not support this parameter.
     * @exception ThetaWebApiException When an invalid option is specified.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun startPlugin(packageName: String? = null) {
        try {
            val params = PluginControlParams(action = "boot", plugin = packageName)
            val pluginControlResponse = ThetaApi.callPluginControlCommand(endpoint, params)
            pluginControlResponse.error?.let {
                throw ThetaWebApiException(it.message)
            }
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Stop the running plugin.
     * Supported just by Theta X, Z1 and V.
     * @exception ThetaWebApiException When an invalid option is specified.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun stopPlugin() {
        try {
            val params = PluginControlParams(action = "finish")
            val pluginControlResponse = ThetaApi.callPluginControlCommand(endpoint, params)
            pluginControlResponse.error?.let {
                throw ThetaWebApiException(it.message)
            }
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Acquires the license for the installed plugin
     *
     * @param packageName package name of the target plugin
     * @return HTML string of the license
     * @exception ThetaWebApiException When an invalid option is specified.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun getPluginLicense(packageName: String): String {
        try {
            val params = GetPluginLicenseParams(packageName)
            val response = ThetaApi.callGetPluginLicenseCommand(endpoint, params)
            if (response.status != HttpStatusCode.OK) {
                throw ThetaWebApiException(response.toString())
            }
            return response.bodyAsText()
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Return the plugin orders.  Supported just by Theta X and Z1.
     *
     * @return list of package names of plugins
     * For Z1, list of three package names for the start-up plugin. No restrictions for the number of package names for X.
     * @exception ThetaWebApiException When an invalid option is specified.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun getPluginOrders(): List<String> {
        try {
            val response = ThetaApi.callGetPluginOrdersCommand(endpoint)
            response.error?.let {
                throw ThetaWebApiException(it.message)
            }
            return response.results!!.pluginOrders
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Sets the plugin orders.  Supported just by Theta X and Z1.
     *
     * @param plugins list of package names of plugins
     * For Z1, list size must be three. No restrictions for the size for X.
     * When not specifying, set an empty string.
     * If an empty string is placed mid-way, it will be moved to the front.
     * Specifying zero package name will result in an error.
     * @exception ThetaWebApiException When an invalid option is specified.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun setPluginOrders(plugins: List<String>) {
        val plugins = plugins.toMutableList()
        if (ThetaModel.get(cameraModel) == ThetaModel.THETA_Z1) {
            when {
                plugins.size > SIZE_OF_SET_PLUGIN_ORDERS_ARGUMENT_LIST_FOR_Z1 -> {
                    throw ArgumentException("Argument list must have $SIZE_OF_SET_PLUGIN_ORDERS_ARGUMENT_LIST_FOR_Z1 or less elements for RICOH THETA Z1")
                }
                plugins.size < SIZE_OF_SET_PLUGIN_ORDERS_ARGUMENT_LIST_FOR_Z1 -> {
                    do { // autocomplete
                        plugins += ""
                    } while (plugins.size < SIZE_OF_SET_PLUGIN_ORDERS_ARGUMENT_LIST_FOR_Z1)
                }
                else -> {}
            }
        }

        try {
            val params = SetPluginOrdersParams(pluginOrders = plugins)
            val response = ThetaApi.callSetPluginOrdersCommand(endpoint, params)
            response.error?.let {
                throw ThetaWebApiException(it.message)
            }
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

    /**
     * Registers identification information (UUID) of a BLE device (Smartphone application) connected to the camera.
     * UUID can be set while the wireless LAN function of the camera is placed in the direct mode.
     *
     * @param uuid Format: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
     * Alphabetic letters are not case-sensitive.
     * @return Device name generated from the serial number (S/N) of the camera.
     * Eg. "00101234" or "THETAXS00101234" when the serial number (S/N) is "XS00101234"
     * @exception ThetaWebApiException When an invalid option is specified.
     * @exception NotConnectedException
     */
    @Throws(Throwable::class)
    suspend fun setBluetoothDevice(uuid: String): String {
        try {
            val params = SetBluetoothDeviceParams(uuid)
            val response = ThetaApi.callSetBluetoothDeviceCommand(endpoint, params)
            response.error?.let {
                throw ThetaWebApiException(it.message)
            }
            return response.results!!.deviceName
        } catch (e: JsonConvertException) {
            throw ThetaWebApiException(e.message ?: e.toString())
        } catch (e: ResponseException) {
            throw ThetaWebApiException.create(e)
        } catch (e: ThetaWebApiException) {
            throw e
        } catch (e: Exception) {
            throw NotConnectedException(e.message ?: e.toString())
        }
    }

}


/**
 * Check status interval for Command
 */
const val CHECK_COMMAND_STATUS_INTERVAL = 1000L

/**
 * The size of setPluginOrders()'s argument list for Z1
 */
const val SIZE_OF_SET_PLUGIN_ORDERS_ARGUMENT_LIST_FOR_Z1 = 3
