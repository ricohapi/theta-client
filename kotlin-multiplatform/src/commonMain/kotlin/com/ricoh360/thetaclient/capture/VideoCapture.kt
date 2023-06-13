package com.ricoh360.thetaclient.capture

import com.ricoh360.thetaclient.ThetaApi
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.*
import io.ktor.client.plugins.*
import io.ktor.serialization.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
 * VideoCapture
 *
 * @property endpoint URL of Theta web API endpoint
 * @property options option of video capture
 */
class VideoCapture private constructor(private val endpoint: String, options: Options) : Capture(options) {

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
         * Called when successful.
         *
         * @param fileUrl URL of the video capture
         */
        fun onSuccess(fileUrl: String)

        /**
         * Called when error occurs.
         *
         * @param exception Exception of error occurs
         */
        fun onError(exception: ThetaRepository.ThetaRepositoryException)
    }

    /**
     * Starts video capture.
     *
     * @param callback Success or failure of the call
     */
    fun startCapture(callback: StartCaptureCallback): VideoCapturing {
        scope.launch {
            try {
                ThetaApi.callStartCaptureCommand(endpoint, StartCaptureParams()).error?.let {
                    callback.onError(ThetaRepository.ThetaWebApiException(it.message))
                }
            } catch (e: JsonConvertException) {
                callback.onError(ThetaRepository.ThetaWebApiException(e.message ?: e.toString()))
            } catch (e: ResponseException) {
                callback.onError(ThetaRepository.ThetaWebApiException.create(e))
            } catch (e: Exception) {
                callback.onError(ThetaRepository.NotConnectedException(e.message ?: e.toString()))
            }
        }

        return VideoCapturing(endpoint, callback)
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
