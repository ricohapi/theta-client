package com.ricoh360.thetaclient.capture

import com.ricoh360.thetaclient.CHECK_COMMAND_STATUS_INTERVAL
import com.ricoh360.thetaclient.ThetaApi
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.*
import io.ktor.client.plugins.*
import io.ktor.serialization.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
 * TimeShiftCapture
 *
 * @property endpoint URL of Theta web API endpoint
 * @property options option of time-shift capture
 * @property checkCommandStatusInterval the interval for executing commands/status API when state "inProgress"
 */
class TimeShiftCapture private constructor(
    private val endpoint: String,
    options: Options,
    private val checkStatusCommandInterval: Long = CHECK_COMMAND_STATUS_INTERVAL
) : Capture(options) {

    private val scope = CoroutineScope(Dispatchers.Default)

    fun getCheckStatusCommandInterval(): Long {
        return checkStatusCommandInterval
    }

    // TODO: Add get photo option property

    /**
     * Callback of startCapture
     */
    interface StartCaptureCallback {
        /**
         * Called when successful.
         *
         * @param fileUrl URL of the video capture
         */
        fun onSuccess(fileUrl: String)

        /**
         * Called when state "inProgress".
         *
         * @param completion Progress rate of command executed
         */
        fun onProgress(completion: Float)

        /**
         * Called when error occurs.
         *
         * @param exception Exception of error occurs
         */
        fun onError(exception: ThetaRepository.ThetaRepositoryException)
    }

    /**
     * Callback of stopCapture
     */
    interface StopCaptureCallback {
        /**
         * Called when successful.
         */
        fun onSuccess()

        /**
         * Called when error occurs.
         *
         * @param exception Exception of error occurs
         */
        fun onError(exception: ThetaRepository.ThetaRepositoryException)
    }

    /**
     * Starts time-shift capture.
     *
     * @param callback Success or failure of the call
     */
    fun startCapture(callback: StartCaptureCallback) {
        scope.launch {
            lateinit var startCaptureResponse: StartCaptureResponse
            try {
                startCaptureResponse = ThetaApi.callStartCaptureCommand(
                    endpoint = endpoint,
                    params = StartCaptureParams(_mode = ShootingMode.TIME_SHIFT_SHOOTING)
                )
                val id = startCaptureResponse.id
                while (startCaptureResponse.state == CommandState.IN_PROGRESS) {
                    delay(timeMillis = checkStatusCommandInterval)
                    startCaptureResponse = ThetaApi.callStatusApi(
                        endpoint = endpoint,
                        params = StatusApiParams(id = id)
                    ) as StartCaptureResponse
                    callback.onProgress(completion = startCaptureResponse.progress?.completion ?: 0f)
                }
            } catch (e: JsonConvertException) {
                callback.onError(exception = ThetaRepository.ThetaWebApiException(message = e.message ?: e.toString()))
            } catch (e: ResponseException) {
                callback.onError(exception = ThetaRepository.ThetaWebApiException.create(exception = e))
            } catch (e: Exception) {
                callback.onError(exception = ThetaRepository.NotConnectedException(message = e.message ?: e.toString()))
            }

            if (startCaptureResponse.state == CommandState.DONE) {
                var fileUrl = ""
                if ((startCaptureResponse.results?.fileUrls?.size ?: 0) > 0) {
                    fileUrl = startCaptureResponse.results?.fileUrls?.get(0) as String
                }
                callback.onSuccess(fileUrl = fileUrl)
                return@launch
            }

            callback.onError(exception = ThetaRepository.ThetaWebApiException(message = startCaptureResponse.error?.message ?: startCaptureResponse.error.toString()))
        }
    }

    /**
     * Stops time-shift capture.
     * When call stopCapture() then call property callback.
     */
    fun stopCapture(callback: StopCaptureCallback) {
        scope.launch {
            lateinit var response: StopCaptureResponse
            try {
                response = ThetaApi.callStopCaptureCommand(endpoint = endpoint)
                response.error?.let {
                    callback.onError(exception = ThetaRepository.ThetaWebApiException(message = it.message))
                    return@launch
                }
            } catch (e: JsonConvertException) {
                callback.onError(exception = ThetaRepository.ThetaWebApiException(message = e.message ?: e.toString()))
                return@launch
            } catch (e: ResponseException) {
                callback.onError(exception = ThetaRepository.ThetaWebApiException.create(exception = e))
                return@launch
            } catch (e: Exception) {
                callback.onError(exception = ThetaRepository.NotConnectedException(message = e.message ?: e.toString()))
                return@launch
            }

            callback.onSuccess()
        }
    }

    /*
     * Builder of TimeShiftCapture
     *
     * @property endpoint URL of Theta web API endpoint
     */
    class Builder internal constructor(private val endpoint: String) : Capture.Builder<Builder>() {
        var interval: Long? = null

        /**
         * Builds an instance of a VideoCapture that has all the combined parameters of the Options that have been added to the Builder.
         *
         * @return VideoCapture
         */
        @Throws(Throwable::class)
        suspend fun build(): TimeShiftCapture {
            try {
                ThetaApi.callSetOptionsCommand(
                    endpoint = endpoint,
                    params = SetOptionsParams(options = Options(captureMode = CaptureMode.IMAGE))
                ).error?.let {
                    throw ThetaRepository.ThetaWebApiException(message = it.message)
                }
                if (options != Options()) {
                    ThetaApi.callSetOptionsCommand(
                        endpoint = endpoint,
                        params = SetOptionsParams(options)
                    ).error?.let {
                        throw ThetaRepository.ThetaWebApiException(message = it.message)
                    }
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
            return interval?.let {
                TimeShiftCapture(endpoint = endpoint, options = options, checkStatusCommandInterval = it)
            } ?: kotlin.run {
                TimeShiftCapture(endpoint = endpoint, options = options)
            }
        }

        fun setCheckStatusCommandInterval(timeMillis: Long): TimeShiftCapture.Builder {
            this.interval = timeMillis
            return this
        }

        // TODO: Add set photo option property
    }
}
