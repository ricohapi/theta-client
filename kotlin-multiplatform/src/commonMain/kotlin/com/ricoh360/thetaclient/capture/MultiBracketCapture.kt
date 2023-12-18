package com.ricoh360.thetaclient.capture

import com.ricoh360.thetaclient.CHECK_COMMAND_STATUS_INTERVAL
import com.ricoh360.thetaclient.ThetaApi
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CaptureMode
import com.ricoh360.thetaclient.transferred.CaptureStatus
import com.ricoh360.thetaclient.transferred.CommandApiResponse
import com.ricoh360.thetaclient.transferred.CommandState
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.SetOptionsParams
import com.ricoh360.thetaclient.transferred.ShootingMethod
import com.ricoh360.thetaclient.transferred.ShootingMode
import com.ricoh360.thetaclient.transferred.StartCaptureParams
import com.ricoh360.thetaclient.transferred.StartCaptureResponse
import com.ricoh360.thetaclient.transferred.StatusApiParams
import com.ricoh360.thetaclient.transferred.UnknownResponse
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Multi bracket capture for multi bracket shooting.
 *
 * @property endpoint URL of Theta web API endpoint
 * @property cameraModel Camera model info.
 * @property checkStatusCommandInterval the interval for executing commands/status API when state "inProgress"
 * @param options options for multi bracket shooting
 */
class MultiBracketCapture private constructor(
    private val endpoint: String,
    private val cameraModel: ThetaRepository.ThetaModel? = null,
    options: Options,
    private val checkStatusCommandInterval: Long,
) : Capture(options) {

    companion object {
        // Interval time between state check on Theta SC2 (milli sec)
        const val SC2_STATE_CHECK_INTERVAL = 3_000L
    }

    private val scope = CoroutineScope(Dispatchers.Default)
    private var captureStatusMonitor: CaptureStatusMonitor? = null

    fun getCheckStatusCommandInterval(): Long {
        return checkStatusCommandInterval
    }

    /**
     * Get multi bracket setting object.
     * @return ThetaRepository.BracketSettingList
     */
    fun getBracketSettings(): ThetaRepository.BracketSettingList? =
        options._autoBracket?.let { ThetaRepository.BracketSettingList.get(it) }

    /**
     * Callback of startCapture
     */
    interface StartCaptureCallback {
        /**
         * Called when state "inProgress".
         *
         * @param completion Progress rate of command executed. 0 to 1.
         */
        fun onProgress(completion: Float)

        /**
         * Called when stopCapture error occurs.
         *
         * @param exception Exception of error occurs
         */
        fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException)

        /**
         * Called when error occurs.
         *
         * @param exception Exception of error occurs
         */
        fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException)

        /**
         * Called when successful.
         *
         * @param fileUrls URLs of the taken photos. When the capturing is canceled, this URLs will be null.
         * On Theta SC2, [fileUrls] is empty list.
         */
        fun onCaptureCompleted(fileUrls: List<String>?)
    }

    /**
     * Check progress of the capturing on other than Theta SC2.
     *
     * @param id id in the response of startCapture command.
     * @param callback calls according to the progress.
     */
    private suspend fun monitorCommandStatus(id: String, callback: StartCaptureCallback) {
        try {
            var response: CommandApiResponse? = null
            var state = CommandState.IN_PROGRESS
            while (state == CommandState.IN_PROGRESS) {
                delay(timeMillis = checkStatusCommandInterval)
                response = ThetaApi.callStatusApi(
                    endpoint = endpoint,
                    params = StatusApiParams(id = id)
                )
                callback.onProgress(completion = response.progress?.completion ?: 0f)
                state = response.state
            }

            if (state == CommandState.DONE) {
                val captureResponse = response as StartCaptureResponse
                callback.onCaptureCompleted(
                    fileUrls = captureResponse.results?.fileUrls ?: listOf()
                )
                return
            }

            response?.error?.let { error ->
                if (error.isCanceledShootingCode()) {
                    callback.onCaptureCompleted(fileUrls = null) // canceled
                } else {
                    callback.onCaptureFailed(
                        exception = ThetaRepository.ThetaWebApiException(
                            message = error.message
                        )
                    )
                }
            } ?: run {
                callback.onCaptureCompleted(fileUrls = null) // canceled
            }
        } catch (e: JsonConvertException) {
            callback.onCaptureFailed(
                exception = ThetaRepository.ThetaWebApiException(
                    message = e.message ?: e.toString()
                )
            )
        } catch (e: ResponseException) {
            try {
                val error: UnknownResponse = e.response.body()
                if (error.error?.isCanceledShootingCode() == true) {
                    callback.onCaptureCompleted(fileUrls = null) // canceled
                } else {
                    callback.onCaptureFailed(
                        exception = ThetaRepository.ThetaWebApiException.create(
                            exception = e
                        )
                    )
                }
            } catch (exception: Exception) {
                callback.onCaptureFailed(
                    exception = ThetaRepository.ThetaWebApiException(
                        message = exception.message ?: exception.toString()
                    )
                )
            }
        } catch (e: Exception) {
            callback.onCaptureFailed(
                exception = ThetaRepository.NotConnectedException(
                    message = e.message ?: e.toString()
                )
            )
        }
    }

    /**
     * Check progress of the capturing on Theta SC2.
     * Theta SC2 does not send a response with "state" is "done" to a status command.
     *
     * @param callback calls according to the progress.
     */
    private fun monitorCaptureStatus(callback: StartCaptureCallback) {
        var isCaptured = false
        val monitor = CaptureStatusMonitor(
            endpoint,
            { newStatus, _ ->
                when (newStatus) {
                    CaptureStatus.IDLE -> {
                        captureStatusMonitor?.stop()
                        if (isCaptured) {
                            callback.onCaptureCompleted(listOf())
                        } else {
                            callback.onCaptureCompleted(null)
                        }
                    }

                    CaptureStatus.BRACKET_SHOOTING -> {
                        isCaptured = true
                    }

                    else -> {}
                }
            },
            { e ->
                captureStatusMonitor?.stop()
                callback.onCaptureFailed(
                    exception = ThetaRepository.NotConnectedException(
                        message = e.message ?: e.toString()
                    )
                )
            },
            SC2_STATE_CHECK_INTERVAL,
        )
        captureStatusMonitor = monitor
        monitor.start()
    }

    /**
     * Starts multi bracket shooting.
     *
     * @param callback Success or failure of the capture
     * @return MultiBracketCapturing instance
     */
    fun startCapture(callback: StartCaptureCallback): MultiBracketCapturing {
        scope.launch {
            lateinit var startCaptureResponse: StartCaptureResponse
            try {
                startCaptureResponse = ThetaApi.callStartCaptureCommand(
                    endpoint = endpoint,
                    params = when (cameraModel) {
                        ThetaRepository.ThetaModel.THETA_X -> StartCaptureParams()
                        else -> StartCaptureParams(_mode = ShootingMode.MULTI_BRACKET_SHOOTING)
                    }
                )
                startCaptureResponse.error?.let { error ->
                    callback.onCaptureFailed(
                        exception = ThetaRepository.ThetaWebApiException(
                            message = error.message
                        )
                    )
                    return@launch
                }

                delay(timeMillis = checkStatusCommandInterval)
                when (cameraModel) {
                    ThetaRepository.ThetaModel.THETA_SC2, ThetaRepository.ThetaModel.THETA_SC2_B -> {
                        monitorCaptureStatus(callback)
                    }

                    else -> {
                        startCaptureResponse.id?.let {
                            monitorCommandStatus(it, callback)
                        }
                    }
                }
            } catch (e: JsonConvertException) {
                callback.onCaptureFailed(
                    exception = ThetaRepository.ThetaWebApiException(
                        message = e.message ?: e.toString()
                    )
                )
            } catch (e: ResponseException) {
                callback.onCaptureFailed(
                    exception = ThetaRepository.ThetaWebApiException.create(
                        exception = e
                    )
                )
            } catch (e: Exception) {
                callback.onCaptureFailed(
                    exception = ThetaRepository.NotConnectedException(
                        message = e.message ?: e.toString()
                    )
                )
            }
        }
        return MultiBracketCapturing(endpoint = endpoint, callback = callback)
    }

    /**
     * Builder of MultiBracketCapture
     *
     * @property endpoint URL of Theta web API endpoint
     * @property cameraModel Camera model info.
     */
    class Builder internal constructor(
        private val endpoint: String,
        private val cameraModel: ThetaRepository.ThetaModel? = null,
    ) : Capture.Builder<Builder>() {
        private var interval: Long? = null
        private val bracketSettings: MutableList<ThetaRepository.BracketSetting> = mutableListOf()

        /**
         * Builds an instance of a MultiBracketCapture that has all the combined parameters of the Options that have been added to the Builder.
         *
         * @return MultiBracketCapture
         */
        @Throws(Throwable::class)
        suspend fun build(): MultiBracketCapture {
            if (bracketSettings.size < MIN_SHOTS) throw ThetaRepository.ThetaWebApiException("Number of shots in multi bracket shooting is 2 to 13 ")
            try {
                // Turn into image capture mode
                ThetaApi.callSetOptionsCommand(
                    endpoint = endpoint,
                    params = SetOptionsParams(options = Options(captureMode = CaptureMode.IMAGE))
                ).error?.let {
                    throw ThetaRepository.ThetaWebApiException(message = it.message)
                }
                // Set bracket settings
                if (cameraModel == ThetaRepository.ThetaModel.THETA_X) {
                    options._shootingMethod = ShootingMethod.BRACKET
                }
                options._autoBracket =
                    ThetaRepository.BracketSettingList(bracketSettings).toTransferredAutoBracket()
                ThetaApi.callSetOptionsCommand(
                    endpoint = endpoint,
                    params = SetOptionsParams(options = options)
                ).error?.let {
                    throw ThetaRepository.ThetaWebApiException(message = it.message)
                }
            } catch (e: JsonConvertException) {
                throw ThetaRepository.ThetaWebApiException(message = e.message ?: e.toString())
            } catch (e: ResponseException) {
                throw ThetaRepository.ThetaWebApiException.create(exception = e)
            } catch (e: ThetaRepository.ThetaWebApiException) {
                throw e
            } catch (e: Exception) {
                throw ThetaRepository.NotConnectedException(message = e.message ?: e.toString())
            }
            return MultiBracketCapture(
                endpoint = endpoint,
                cameraModel = cameraModel,
                options = options,
                checkStatusCommandInterval = interval ?: CHECK_COMMAND_STATUS_INTERVAL
            )
        }

        fun setCheckStatusCommandInterval(timeMillis: Long): Builder {
            this.interval = timeMillis
            return this
        }

        /**
         * Add bracket parameters for a shot.
         * Number of shots in multi bracket shooting is 2 to 13.
         * Instead of this function, you can use [addBracketSettingList].
         *
         * @param aperture Only Theta Z1 supports.
         * @param colorTemperature
         * @param exposureCompensation Theta X, SC2, S and SC do not support
         * @param exposureProgram Theta X supports only MANUAL. Theta SC2, S and SC do not support.
         * @param iso
         * @param shutterSpeed
         * @param whiteBalance Theta SC2, S and SC do not support.
         * @return MultiBracketCapture.Builder
         */
        @Suppress("CyclomaticComplexMethod")
        fun addBracketParameters(
            aperture: ThetaRepository.ApertureEnum? = null,
            colorTemperature: Int? = null,
            exposureCompensation: ThetaRepository.ExposureCompensationEnum? = null,
            exposureProgram: ThetaRepository.ExposureProgramEnum? = null,
            iso: ThetaRepository.IsoEnum? = null,
            shutterSpeed: ThetaRepository.ShutterSpeedEnum? = null,
            whiteBalance: ThetaRepository.WhiteBalanceEnum? = null,
        ): Builder {
            if (bracketSettings.size >= MAX_SHOTS) throw ThetaRepository.ThetaWebApiException("Number of shots in multi bracket shooting is 2 to 13")
            val bracketSetting = ThetaRepository.BracketSetting(
                aperture = when (cameraModel) {
                    ThetaRepository.ThetaModel.THETA_Z1 -> aperture
                    else -> null
                },
                colorTemperature = when (cameraModel) {
                    ThetaRepository.ThetaModel.THETA_SC2,
                    ThetaRepository.ThetaModel.THETA_SC2_B,
                    ThetaRepository.ThetaModel.THETA_S,
                    ThetaRepository.ThetaModel.THETA_SC -> colorTemperature ?: DEFAULT_COLOR_TEMPERATURE

                    else -> colorTemperature
                },
                exposureCompensation = when (cameraModel) {
                    ThetaRepository.ThetaModel.THETA_X,
                    ThetaRepository.ThetaModel.THETA_SC2,
                    ThetaRepository.ThetaModel.THETA_SC2_B,
                    ThetaRepository.ThetaModel.THETA_S,
                    ThetaRepository.ThetaModel.THETA_SC -> null

                    else -> exposureCompensation
                },
                exposureProgram = when (cameraModel) {
                    ThetaRepository.ThetaModel.THETA_X -> ThetaRepository.ExposureProgramEnum.MANUAL
                    ThetaRepository.ThetaModel.THETA_SC2,
                    ThetaRepository.ThetaModel.THETA_SC2_B,
                    ThetaRepository.ThetaModel.THETA_S,
                    ThetaRepository.ThetaModel.THETA_SC -> null

                    else -> exposureProgram ?: ThetaRepository.ExposureProgramEnum.MANUAL
                },
                iso = when (cameraModel) {
                    ThetaRepository.ThetaModel.THETA_SC2,
                    ThetaRepository.ThetaModel.THETA_SC2_B,
                    ThetaRepository.ThetaModel.THETA_S,
                    ThetaRepository.ThetaModel.THETA_SC -> iso ?: ThetaRepository.IsoEnum.ISO_400

                    else -> iso
                },
                shutterSpeed = when (cameraModel) {
                    ThetaRepository.ThetaModel.THETA_SC2,
                    ThetaRepository.ThetaModel.THETA_SC2_B,
                    ThetaRepository.ThetaModel.THETA_S,
                    ThetaRepository.ThetaModel.THETA_SC -> shutterSpeed ?: ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_250

                    else -> shutterSpeed
                },
                whiteBalance = when (cameraModel) {
                    ThetaRepository.ThetaModel.THETA_SC2,
                    ThetaRepository.ThetaModel.THETA_SC2_B,
                    ThetaRepository.ThetaModel.THETA_S,
                    ThetaRepository.ThetaModel.THETA_SC -> null

                    else -> colorTemperature?.let {
                        ThetaRepository.WhiteBalanceEnum.COLOR_TEMPERATURE
                    } ?: whiteBalance ?: ThetaRepository.WhiteBalanceEnum.AUTO
                },
            )
            bracketSettings.add(bracketSetting)
            return this
        }

        /**
         * Add bracket setting list.
         * Instead of this function, you can use [addBracketParameters]
         *
         * @param bracketSettingList size must be between 2 and 13.
         * @return MultiBracketCapture.Builder
         */
        fun addBracketSettingList(bracketSettingList: List<ThetaRepository.BracketSetting>): Builder {
            bracketSettingList.forEach {
                addBracketParameters(
                    aperture = it.aperture,
                    colorTemperature = it.colorTemperature,
                    exposureCompensation = it.exposureCompensation,
                    exposureProgram = it.exposureProgram,
                    iso = it.iso,
                    shutterSpeed = it.shutterSpeed,
                    whiteBalance = it.whiteBalance,
                )
            }
            return this
        }

        companion object {
            // Number of shots in multi bracket shooting is 2 to 13.
            const val MIN_SHOTS = 2
            const val MAX_SHOTS = 13
            const val DEFAULT_COLOR_TEMPERATURE = 5000
        }
    }
}
