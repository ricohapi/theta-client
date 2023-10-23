package com.ricoh360.thetaclient.capture

import com.ricoh360.thetaclient.ThetaApi
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.*
import io.ktor.client.plugins.*
import io.ktor.serialization.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val CHECK_STATE_INTERVAL = 1000L
private const val CHECK_STATE_RETRY = 3
private const val CHECK_SHOOTING_IDLE_COUNT = 2
private const val ERROR_GET_CAPTURE_STATUS = "Capture status cannot be retrieved."

/*
 * VideoCapture
 *
 * @property endpoint URL of Theta web API endpoint
 * @property options option of video capture
 */
class VideoCapture private constructor(private val endpoint: String, options: Options) :
    Capture(options) {

    private val scope = CoroutineScope(Dispatchers.Default)

    /**
     * Get maximum recordable time (in seconds) of the camera.
     *
     * @return Maximum recordable time
     */
    fun getMaxRecordableTime() = options._maxRecordableTime?.let {
        ThetaRepository.MaxRecordableTimeEnum.get(it)
    }

    /**
     * Get video file format.
     *
     * @return Video file format
     */
    fun getFileFormat() = options.fileFormat?.let { it ->
        ThetaRepository.FileFormatEnum.get(it)?.let {
            ThetaRepository.VideoFileFormatEnum.get(it)
        }
    }

    // TODO: Add get video option property

    /**
     * Callback of startCapture
     */
    interface StartCaptureCallback {

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
         * @param fileUrl URL of the video capture
         */
        fun onCaptureCompleted(fileUrl: String?)
    }

    internal suspend fun getCaptureStatus(): CaptureStatus? {
        var retry = CHECK_STATE_RETRY
        while (retry > 0) {
            try {
                val stateResponse = ThetaApi.callStateApi(endpoint)
                return stateResponse.state._captureStatus
            } catch (e: Exception) {
                println("getCaptureStatus retry: $retry")
                delay(CHECK_STATE_INTERVAL)
            }
            retry -= 1
        }
        return null
    }

    /**
     * Starts video capture.
     *
     * @param callback Success or failure of the call
     */
    fun startCapture(callback: StartCaptureCallback): VideoCapturing {
        var isEndCapture = false

        fun callOnCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
            if (isEndCapture) {
                return
            }
            isEndCapture = true
            callback.onCaptureFailed(exception)
        }

        fun callOnCaptureCompleted(fileUrl: String?) {
            println("call callOnCaptureCompleted: $fileUrl")
            if (isEndCapture) {
                return
            }
            isEndCapture = true
            callback.onCaptureCompleted(fileUrl)
        }

        val captureCallback = object : StartCaptureCallback {
            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                if (!isEndCapture) {
                    callback.onStopFailed(exception)
                }
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
            }

            override fun onCaptureCompleted(fileUrl: String?) {
                callOnCaptureCompleted(fileUrl)
            }

        }
        scope.launch {
            try {
                ThetaApi.callStartCaptureCommand(endpoint, StartCaptureParams()).error?.let {
                    callOnCaptureFailed(ThetaRepository.ThetaWebApiException(it.message))
                }
            } catch (e: JsonConvertException) {
                callOnCaptureFailed(ThetaRepository.ThetaWebApiException(e.message ?: e.toString()))
            } catch (e: ResponseException) {
                callOnCaptureFailed(ThetaRepository.ThetaWebApiException.create(e))
            } catch (e: Exception) {
                callOnCaptureFailed(
                    ThetaRepository.NotConnectedException(
                        e.message ?: e.toString()
                    )
                )
            }

            var idleCount = CHECK_SHOOTING_IDLE_COUNT
            while (!isEndCapture) {
                delay(CHECK_STATE_INTERVAL)
                when (getCaptureStatus()) {
                    null -> {
                        callOnCaptureFailed(
                            ThetaRepository.ThetaWebApiException(
                                ERROR_GET_CAPTURE_STATUS
                            )
                        )
                        break
                    }

                    CaptureStatus.IDLE -> {
                        idleCount -= 1
                        // In the case of SC2, it becomes idle in the middle, so wait multiple times
                        if (idleCount <= 0) {
                            break
                        }
                    }

                    else -> {
                        idleCount = CHECK_SHOOTING_IDLE_COUNT
                    }
                }
            }
            callOnCaptureCompleted(null)
        }
        return VideoCapturing(endpoint, captureCallback)
    }

    /*
     * Builder of VideoCapture
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
        suspend fun build(): VideoCapture {
            try {
                ThetaApi.callSetOptionsCommand(
                    endpoint,
                    SetOptionsParams(options = Options(captureMode = CaptureMode.VIDEO))
                ).error?.let {
                    throw ThetaRepository.ThetaWebApiException(it.message)
                }
                if (options != Options()) {
                    ThetaApi.callSetOptionsCommand(endpoint, SetOptionsParams(options)).error?.let {
                        throw ThetaRepository.ThetaWebApiException(it.message)
                    }
                }
            } catch (e: JsonConvertException) {
                throw ThetaRepository.ThetaWebApiException(e.message ?: e.toString())
            } catch (e: ResponseException) {
                throw ThetaRepository.ThetaWebApiException.create(e)
            } catch (e: ThetaRepository.ThetaWebApiException) {
                throw e
            } catch (e: Exception) {
                throw ThetaRepository.NotConnectedException(e.message ?: e.toString())
            }
            return VideoCapture(endpoint, options)
        }

        /**
         * Set maximum recordable time (in seconds) of the camera.
         *
         * @param time Maximum recordable time
         * @return Builder
         */
        fun setMaxRecordableTime(time: ThetaRepository.MaxRecordableTimeEnum): Builder {
            options._maxRecordableTime = time.sec
            return this
        }

        /**
         * Set video file format.
         *
         * @param fileFormat Video file format
         * @return Builder
         */
        fun setFileFormat(fileFormat: ThetaRepository.VideoFileFormatEnum): Builder {
            options.fileFormat = fileFormat.fileFormat.toMediaFileFormat()
            return this
        }

        // TODO: Add set video option property
    }
}
