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
import com.ricoh360.thetaclient.transferred.ShootingFunction
import com.ricoh360.thetaclient.transferred.ShootingMethod
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

/*
 * ContinuousCapture
 *
 * @property theta instance of ThetaRepository
 * @property endpoint URL of Theta web API endpoint
 * @property options option of continuous shooting
 * @property checkCommandStatusInterval the interval for executing commands/status API when state "inProgress"
 */
class ContinuousCapture private constructor(
    private val theta: ThetaRepository,
    private val endpoint: String,
    options: Options,
    private val checkStatusCommandInterval: Long
) : Capture(options) {

    private val scope = CoroutineScope(Dispatchers.Default)

    fun getCheckStatusCommandInterval(): Long {
        return checkStatusCommandInterval
    }

    /**
     * Get photo file format.
     *
     * @return Photo file format
     */
    fun getFileFormat() = options.fileFormat?.let { format ->
        ThetaRepository.FileFormatEnum.get(format).let {
            ThetaRepository.PhotoFileFormatEnum.get(it)
        }
    }

    /**
     * Get Number of shots for continuous shooting.
     */
    @Throws(Throwable::class)
    suspend fun getContinuousNumber(): ThetaRepository.ContinuousNumberEnum {
        return theta.getOptions(optionNames = listOf(ThetaRepository.OptionNameEnum.ContinuousNumber)).continuousNumber ?: ThetaRepository.ContinuousNumberEnum.UNSUPPORTED
    }

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
         * Called when error occurs.
         *
         * @param exception Exception of error occurs
         */
        fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException)

        /**
         * Called when successful.
         *
         * @param fileUrls URLs of continuous shooting
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
                monitor.stop()
                callback.onCaptureFailed(
                    exception = ThetaRepository.ThetaWebApiException(
                        message = exception.message ?: exception.toString()
                    )
                )
            }
        } catch (e: Exception) {
            monitor.stop()
            callback.onCaptureFailed(exception = ThetaRepository.NotConnectedException(message = e.message ?: e.toString()))
        }
    }

    /**
     * Starts continuous shooting.
     *
     * @param callback Success or failure of the call
     */
    fun startCapture(callback: StartCaptureCallback) {
        scope.launch {
            lateinit var startCaptureResponse: StartCaptureResponse
            try {
                startCaptureResponse = ThetaApi.callStartCaptureCommand(
                    endpoint = endpoint,
                    params = StartCaptureParams()
                )
                startCaptureResponse.error?.let { error ->
                    callback.onCaptureFailed(exception = ThetaRepository.ThetaWebApiException(message = error.message))
                    return@launch
                }

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
    }

    /*
     * Builder of ContinuousCapture
     *
     * @property theta instance of ThetaRepository
     * @property endpoint URL of Theta web API endpoint
     */
    class Builder internal constructor(
        private val theta: ThetaRepository,
        private val endpoint: String
    ) : Capture.Builder<Builder>() {
        private var interval: Long? = null

        /**
         * Builds an instance of a ContinuousCapture that has all the combined parameters of the Options that have been added to the Builder.
         *
         * @return ContinuousCapture
         */
        @Throws(Throwable::class)
        suspend fun build(): ContinuousCapture {
            try {
                ThetaApi.callSetOptionsCommand(
                    endpoint = endpoint,
                    params = SetOptionsParams(
                        options =
                        Options(
                            captureMode = CaptureMode.IMAGE,
                            _function = ShootingFunction.NORMAL, // If the THETA is using MySetting, it cannot start continuous shooting without this _function setting
                            _shootingMethod = ShootingMethod.CONTINUOUS
                        )
                    )
                ).error?.let {
                    throw ThetaRepository.ThetaWebApiException(message = it.message)
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
            return ContinuousCapture(
                theta = theta,
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
         * Set photo file format.
         * Continuous shooting only supports 5.5K and 11K
         *
         * @param fileFormat Photo file format
         * @return Builder
         */
        fun setFileFormat(fileFormat: ThetaRepository.PhotoFileFormatEnum): Builder {
            options.fileFormat = fileFormat.fileFormat.toMediaFileFormat()
            return this
        }

        // TODO: Add set photo option property
    }
}