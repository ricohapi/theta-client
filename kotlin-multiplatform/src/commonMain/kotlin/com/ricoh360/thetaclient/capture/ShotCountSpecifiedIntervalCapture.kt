package com.ricoh360.thetaclient.capture

import com.ricoh360.thetaclient.CHECK_COMMAND_STATUS_INTERVAL
import com.ricoh360.thetaclient.ThetaApi
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.*
import io.ktor.client.plugins.*
import io.ktor.serialization.*
import kotlinx.coroutines.*

/*
 * ShotCountSpecifiedIntervalCapture
 *
 * @property endpoint URL of Theta web API endpoint
 * @property cameraModel Camera model info.
 * @property options option of interval shooting with the shot count specified
 * @property checkCommandStatusInterval the interval for executing commands/status API when state "inProgress"
 */
class ShotCountSpecifiedIntervalCapture private constructor(
    private val endpoint: String,
    private val cameraModel: ThetaRepository.ThetaModel? = null,
    options: Options,
    private val checkStatusCommandInterval: Long
) : Capture(options) {

    private val scope = CoroutineScope(Dispatchers.Default)
    private var captureStatusMonitor: CaptureStatusMonitor? = null

    fun getCheckStatusCommandInterval(): Long {
        return checkStatusCommandInterval
    }

    /**
     * Get shooting interval (sec.) for interval shooting.
     */
    fun getCaptureInterval() = options.captureInterval

    /**
     * Get number of shots for interval shooting.
     */
    fun getCaptureNumber() = options.captureNumber

    // TODO: Add get photo option property

    /**
     * Callback of startCapture
     */
    interface StartCaptureCallback {
        /**
         * Called when state "inProgress".
         *
         * @param completion Progress rate of command executed
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
         * @param fileUrls URLs of the limitless interval capture
         */
        fun onCaptureCompleted(fileUrls: List<String>?)
    }

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

            if (response?.state == CommandState.DONE) {
                val captureResponse = response as StartCaptureResponse
                callback.onCaptureCompleted(fileUrls = captureResponse.results?.fileUrls ?: listOf())
                return
            }

            response?.error?.let { error ->
                if (error.isCanceledShootingCode()) {
                    callback.onCaptureCompleted(fileUrls = null) // canceled
                } else {
                    callback.onCaptureFailed(exception = ThetaRepository.ThetaWebApiException(message = error.message))
                }
            } ?: run {
                callback.onCaptureCompleted(fileUrls = null) // canceled
            }
        } catch (e: JsonConvertException) {
            callback.onCaptureFailed(exception = ThetaRepository.ThetaWebApiException(message = e.message ?: e.toString()))
        } catch (e: ResponseException) {
            callback.onCaptureFailed(exception = ThetaRepository.ThetaWebApiException.create(exception = e))
        } catch (e: Exception) {
            callback.onCaptureFailed(exception = ThetaRepository.NotConnectedException(message = e.message ?: e.toString()))
        }
    }

    private fun monitorCaptureStatus(callback: StartCaptureCallback) {
        var isCaptured = false
        val monitor = CaptureStatusMonitor(endpoint,
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

                    CaptureStatus.SHOOTING -> {
                        isCaptured = true
                    }

                    else -> {}
                }
            }, { e ->
                captureStatusMonitor?.stop()
                callback.onCaptureFailed(
                    exception = ThetaRepository.NotConnectedException(
                        message = e.message ?: e.toString()
                    )
                )
            })
        captureStatusMonitor = monitor
        monitor.start()
    }

    /**
     * Starts interval shooting with the shot count specified.
     *
     * @param callback Success or failure of the call
     * @return ShotCountSpecifiedIntervalCapturing instance
     */
    fun startCapture(callback: StartCaptureCallback): ShotCountSpecifiedIntervalCapturing {
        scope.launch {
            lateinit var startCaptureResponse: StartCaptureResponse
            try {
                val params = when (cameraModel) {
                    ThetaRepository.ThetaModel.THETA_X -> StartCaptureParams()
                    else -> StartCaptureParams(_mode = ShootingMode.INTERVAL_SHOOTING)
                }

                startCaptureResponse = ThetaApi.callStartCaptureCommand(
                    endpoint = endpoint,
                    params = params
                )
                startCaptureResponse.error?.let { error ->
                    callback.onCaptureFailed(exception = ThetaRepository.ThetaWebApiException(message = error.message))
                    return@launch
                }

                delay(timeMillis = checkStatusCommandInterval)
                when(cameraModel) {
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
                callback.onCaptureFailed(exception = ThetaRepository.ThetaWebApiException(message = e.message ?: e.toString()))
            } catch (e: ResponseException) {
                callback.onCaptureFailed(exception = ThetaRepository.ThetaWebApiException.create(exception = e))
            } catch (e: Exception) {
                callback.onCaptureFailed(exception = ThetaRepository.NotConnectedException(message = e.message ?: e.toString()))
            }
        }
        return ShotCountSpecifiedIntervalCapturing(endpoint = endpoint, callback = callback)
    }

    /*
     * Builder of ShotCountSpecifiedIntervalCapture
     *
     * @property shotCount shot count specified
     * @property endpoint URL of Theta web API endpoint
     * @property cameraModel Camera model info.
     */
    class Builder internal constructor(private val shotCount: Int, private val endpoint: String, private val cameraModel: ThetaRepository.ThetaModel? = null) :
        Capture.Builder<Builder>() {
        private var interval: Long? = null

        /**
         * Builds an instance of a ShotCountSpecifiedIntervalCapture that has all the combined parameters of the Options that have been added to the Builder.
         *
         * @return ShotCountSpecifiedIntervalCapture
         */
        @Throws(Throwable::class)
        suspend fun build(): ShotCountSpecifiedIntervalCapture {
            try {
                ThetaApi.callSetOptionsCommand(
                    endpoint = endpoint,
                    params = SetOptionsParams(options = Options(captureMode = CaptureMode.IMAGE))
                ).error?.let {
                    throw ThetaRepository.ThetaWebApiException(message = it.message)
                }

                options.captureNumber = shotCount // Specified

                when (cameraModel) {
                    ThetaRepository.ThetaModel.THETA_X -> options._shootingMethod = ShootingMethod.INTERVAL
                    else -> {}
                }
                ThetaApi.callSetOptionsCommand(endpoint = endpoint, params = SetOptionsParams(options)).error?.let {
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
            return ShotCountSpecifiedIntervalCapture(
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
         * Set shooting interval (sec.) for interval shooting.
         * @param interval sec
         * @return Builder
         */
        fun setCaptureInterval(interval: Int): Builder {
            options.captureInterval = interval
            return this
        }

        // TODO: Add set photo option property
    }
}
