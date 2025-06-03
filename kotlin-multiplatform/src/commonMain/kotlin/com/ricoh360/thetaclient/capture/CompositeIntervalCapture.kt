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
import com.ricoh360.thetaclient.transferred.ShootingMode
import com.ricoh360.thetaclient.transferred.StartCaptureParams
import com.ricoh360.thetaclient.transferred.StartCaptureResponse
import com.ricoh360.thetaclient.transferred.StatusApiParams
import io.ktor.client.plugins.ResponseException
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
 * CompositeIntervalCapture
 *
 * @property endpoint URL of Theta web API endpoint
 * @property options option of interval composite shooting
 * @property checkCommandStatusInterval the interval for executing commands/status API when state "inProgress"
 */
class CompositeIntervalCapture private constructor(
    private val endpoint: String,
    options: Options,
    private val checkStatusCommandInterval: Long
) : Capture(options) {

    private val scope = CoroutineScope(Dispatchers.Default)

    fun getCheckStatusCommandInterval(): Long {
        return checkStatusCommandInterval
    }

    /**
     * Get In-progress save interval for interval composite shooting (sec).
     */
    fun getCompositeShootingOutputInterval() = options._compositeShootingOutputInterval

    /**
     * Get Shooting time for interval composite shooting (sec).
     */
    fun getCompositeShootingTime() = options._compositeShootingTime

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
         * Called when change capture status.
         *
         * @param status Capturing status
         */
        fun onCapturing(status: CapturingStatusEnum) {}

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
        val monitor = CaptureStatusMonitor(
            endpoint,
            onChangeStatus = { newStatus, _ ->
                when (newStatus) {
                    CaptureStatus.SELF_TIMER_COUNTDOWN -> callback.onCapturing(
                        CapturingStatusEnum.SELF_TIMER_COUNTDOWN
                    )

                    else -> callback.onCapturing(CapturingStatusEnum.CAPTURING)
                }
            },
            onError = { error ->
                println("CaptureStatusMonitor error: ${error.message}")
            },
            checkStatusCommandInterval,
            1
        )
        try {
            var response: CommandApiResponse? = null
            var state = CommandState.IN_PROGRESS
            monitor.start()
            while (state == CommandState.IN_PROGRESS) {
                delay(timeMillis = checkStatusCommandInterval)
                response = ThetaApi.callStatusApi(
                    endpoint = endpoint,
                    params = StatusApiParams(id = id)
                )
                callback.onProgress(completion = response.progress?.completion ?: 0f)
                state = response.state
            }
            monitor.stop()

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
            monitor.stop()
            callback.onCaptureFailed(exception = ThetaRepository.ThetaWebApiException(message = e.message ?: e.toString()))
        } catch (e: ResponseException) {
            monitor.stop()
            callback.onCaptureFailed(exception = ThetaRepository.ThetaWebApiException.create(exception = e))
        } catch (e: Exception) {
            monitor.stop()
            callback.onCaptureFailed(exception = ThetaRepository.NotConnectedException(message = e.message ?: e.toString()))
        }
    }

    /**
     * Starts interval composite shooting.
     *
     * @param callback Success or failure of the call
     * @return CompositeIntervalCapturing instance
     */
    fun startCapture(callback: StartCaptureCallback): CompositeIntervalCapturing {
        scope.launch {
            lateinit var startCaptureResponse: StartCaptureResponse
            try {
                val params = StartCaptureParams(_mode = ShootingMode.INTERVAL_COMPOSITE_SHOOTING)

                startCaptureResponse = ThetaApi.callStartCaptureCommand(
                    endpoint = endpoint,
                    params = params
                )
                startCaptureResponse.error?.let { error ->
                    callback.onCaptureFailed(exception = ThetaRepository.ThetaWebApiException(message = error.message))
                    return@launch
                }
                callback.onCapturing(CapturingStatusEnum.STARTING)

                startCaptureResponse.id?.let {
                    monitorCommandStatus(it, callback)
                }
            } catch (e: JsonConvertException) {
                callback.onCaptureFailed(exception = ThetaRepository.ThetaWebApiException(message = e.message ?: e.toString()))
            } catch (e: ResponseException) {
                callback.onCaptureFailed(exception = ThetaRepository.ThetaWebApiException.create(exception = e))
            } catch (e: Exception) {
                callback.onCaptureFailed(exception = ThetaRepository.NotConnectedException(message = e.message ?: e.toString()))
            }
        }
        return CompositeIntervalCapturing(endpoint = endpoint, callback = callback)
    }

    /*
     * Builder of CompositeIntervalCapture
     *
     * @property shootingTimeSec Shooting time for interval composite shooting (sec)
     * @property endpoint URL of Theta web API endpoint
     */
    class Builder internal constructor(private val shootingTimeSec: Int, private val endpoint: String) : Capture.Builder<Builder>() {
        private var interval: Long? = null

        /**
         * Builds an instance of a CompositeIntervalCapture that has all the combined parameters of the Options that have been added to the Builder.
         *
         * @return CompositeIntervalCapture
         */
        @Throws(Throwable::class)
        suspend fun build(): CompositeIntervalCapture {
            try {
                ThetaApi.callSetOptionsCommand(
                    endpoint = endpoint,
                    params = SetOptionsParams(options = Options(captureMode = CaptureMode.IMAGE))
                ).error?.let {
                    throw ThetaRepository.ThetaWebApiException(message = it.message)
                }

                options._compositeShootingTime = shootingTimeSec

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
            return CompositeIntervalCapture(
                endpoint = endpoint,
                options = options,
                checkStatusCommandInterval = interval ?: CHECK_COMMAND_STATUS_INTERVAL
            )
        }

        fun setCheckStatusCommandInterval(timeMillis: Long): Builder {
            this.interval = timeMillis
            return this
        }

        /**
         * Set In-progress save interval for interval composite shooting (sec).
         * @param sec sec
         * @return Builder
         */
        fun setCompositeShootingOutputInterval(sec: Int): Builder {
            options._compositeShootingOutputInterval = sec
            return this
        }

        // TODO: Add set photo option property
    }
}
