package com.ricoh360.thetaclient.capture

import com.ricoh360.thetaclient.CHECK_COMMAND_STATUS_INTERVAL
import com.ricoh360.thetaclient.ThetaApi
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.*
import io.ktor.client.plugins.*
import io.ktor.serialization.*
import kotlinx.coroutines.*

/*
 * PhotoCapture
 *
 * @property endpoint URL of Theta web API endpoint
 * @property options option of take a picture
 */
class PhotoCapture private constructor(private val endpoint: String, options: Options) : Capture(options) {

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
     * Callback of takePicture
     */
    interface TakePictureCallback {
        /**
         * Called when successful.
         *
         * @param fileUrl URL of the picture taken
         */
        fun onSuccess(fileUrl: String)

        /**
         * Called when state "inProgress".
         *
         * @param completion Progress rate of command executed
         */
        fun onProgress(completion: Float) {}

        /**
         * Called when error occurs.
         *
         * @param exception Exception of error occurs
         */
        fun onError(exception: ThetaRepository.ThetaRepositoryException)
    }

    /**
     * Take a picture.
     *
     * @param callback Success or failure of the call
     */
    fun takePicture(callback: TakePictureCallback) {
        scope.launch {
            lateinit var takePictureResponse: TakePictureResponse
            try {
                takePictureResponse = ThetaApi.callTakePictureCommand(endpoint = endpoint)
                val id = takePictureResponse.id
                while (takePictureResponse.state == CommandState.IN_PROGRESS) {
                    delay(timeMillis = CHECK_COMMAND_STATUS_INTERVAL)
                    takePictureResponse = ThetaApi.callStatusApi(
                        endpoint = endpoint,
                        params = StatusApiParams(id = id)
                    ) as TakePictureResponse
                    callback.onProgress(completion = takePictureResponse.progress?.completion ?: 0f)
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

            if (takePictureResponse.state == CommandState.DONE) {
                callback.onSuccess(fileUrl = takePictureResponse.results!!.fileUrl)
                return@launch
            }

            callback.onError(exception = ThetaRepository.ThetaWebApiException(message = takePictureResponse.error?.message ?: takePictureResponse.error.toString()))
        }
    }

    /*
     * Builder of PhotoCapture
     *
     * @property endpoint URL of Theta web API endpoint
     * @property cameraModel Camera model info.
     */
    class Builder internal constructor(private val endpoint: String, private val cameraModel: ThetaRepository.ThetaModel? = null) : Capture.Builder<Builder>() {

        /**
         * Builds an instance of a PhotoCapture that has all the combined parameters of the Options that have been added to the Builder.
         *
         * @return PhotoCapture
         */
        @Throws(Throwable::class)
        suspend fun build(): PhotoCapture {
            try {
                val modeOptions = when (cameraModel) {
                    ThetaRepository.ThetaModel.THETA_X -> Options(captureMode = CaptureMode.IMAGE, _shootingMethod = ShootingMethod.NORMAL)
                    else -> Options(captureMode = CaptureMode.IMAGE)
                }

                ThetaApi.callSetOptionsCommand(
                    endpoint,
                    SetOptionsParams(modeOptions)
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
            return PhotoCapture(endpoint, options)
        }

        /**
         * Set image processing filter.
         *
         * @param filter Image processing filter
         * @return Builder
         */
        fun setFilter(filter: ThetaRepository.FilterEnum): Builder {
            options._filter = filter.filter
            return this
        }

        /**
         * Set photo file format.
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
