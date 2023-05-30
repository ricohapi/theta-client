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

    /**
     * Get image processing filter.
     *
     * @return Image processing filter
     */
    fun getFilter() = options._filter?.let { ThetaRepository.FilterEnum.get(it) }

    /**
     * Get photo file format.
     *
     * @return Photo file format
     */
    fun getFileFormat() = options.fileFormat?.let { it ->
        ThetaRepository.FileFormatEnum.get(it)?.let {
            ThetaRepository.PhotoFileFormatEnum.get(it)
        }
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
                callback.onSuccess(fileUrl = startCaptureResponse.results?.fileUrls?.get(0) ?: "")
                return@launch
            }

            callback.onError(exception = ThetaRepository.ThetaWebApiException(message = startCaptureResponse.error?.message ?: startCaptureResponse.error.toString()))
        }
    }

    /*
     * Builder of TimeShiftCapture
     *
     * @property endpoint URL of Theta web API endpoint
     */
    class Builder internal constructor(private val endpoint: String) : Capture.Builder<Builder>() {

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
            return TimeShiftCapture(endpoint = endpoint, options = options)
        }

        /**
         * Set image processing filter.
         *
         * @param filter Image processing filter
         * @return Builder
         */
        fun setFilter(filter: ThetaRepository.FilterEnum): TimeShiftCapture.Builder {
            options._filter = filter.filter
            return this
        }

        /**
         * Set photo file format.
         *
         * @param fileFormat Photo file format
         * @return Builder
         */
        fun setFileFormat(fileFormat: ThetaRepository.PhotoFileFormatEnum): TimeShiftCapture.Builder {
            options.fileFormat = fileFormat.fileFormat.toMediaFileFormat()
            return this
        }

        // TODO: Add set photo option property
    }
}
