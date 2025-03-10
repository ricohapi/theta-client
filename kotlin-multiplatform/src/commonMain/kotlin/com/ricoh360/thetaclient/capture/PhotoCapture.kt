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
class PhotoCapture private constructor(
    private val endpoint: String,
    options: Options,
    private val checkStatusCommandInterval: Long
    ) : PhotoCaptureBase(options) {

    private val scope = CoroutineScope(Dispatchers.Default)

    fun getCheckStatusCommandInterval(): Long {
        return checkStatusCommandInterval
    }

    /**
     * Get image processing filter.
     *
     * @return Image processing filter
     */
    fun getFilter() = options._filter?.let { ThetaRepository.FilterEnum.get(it) }

    /**
     * Get preset mode of Theta SC2 and Theta SC2 for business.
     *
     * @return Preset mode
     */
    fun getPreset() = options._preset?.let {
        ThetaRepository.PresetEnum.get(it)
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
        fun onSuccess(fileUrl: String?)

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
            val monitor = CaptureStatusMonitor(
                endpoint,
                { newStatus, _ ->
                    when (newStatus) {
                        CaptureStatus.SELF_TIMER_COUNTDOWN -> callback.onCapturing(
                            CapturingStatusEnum.SELF_TIMER_COUNTDOWN
                        )

                        else -> callback.onCapturing(CapturingStatusEnum.CAPTURING)
                    }
                },
                { error ->
                    println("CaptureStatusMonitor error: ${error.message}")
                },
                checkStatusCommandInterval,
                1
            )
            try {
                takePictureResponse = ThetaApi.callTakePictureCommand(endpoint = endpoint)
                takePictureResponse.error ?: let {
                    callback.onCapturing(CapturingStatusEnum.STARTING)
                }
                monitor.start()
                val id = takePictureResponse.id
                while (takePictureResponse.state == CommandState.IN_PROGRESS) {
                    delay(timeMillis = checkStatusCommandInterval)
                    takePictureResponse = ThetaApi.callStatusApi(
                        endpoint = endpoint,
                        params = StatusApiParams(id = id)
                    ) as TakePictureResponse
                }
                monitor.stop()
            } catch (e: JsonConvertException) {
                monitor.stop()
                callback.onError(exception = ThetaRepository.ThetaWebApiException(message = e.message ?: e.toString()))
                return@launch
            } catch (e: ResponseException) {
                monitor.stop()
                if (isCanceledShootingResponse(e.response)) {
                    callback.onSuccess(fileUrl = null) // canceled
                } else {
                    callback.onError(exception = ThetaRepository.ThetaWebApiException.create(exception = e))
                }
                return@launch
            } catch (e: Exception) {
                monitor.stop()
                callback.onError(exception = ThetaRepository.NotConnectedException(message = e.message ?: e.toString()))
                return@launch
            }

            if (takePictureResponse.state == CommandState.DONE) {
                callback.onSuccess(fileUrl = takePictureResponse.results?.fileUrl)
                return@launch
            }

            val error = takePictureResponse.error
            if (error != null && !error.isCanceledShootingCode()) {
                callback.onError(exception = ThetaRepository.ThetaWebApiException(message = error.message))
            } else {
                callback.onSuccess(fileUrl = null) // canceled
            }
        }
    }

    /*
     * Builder of PhotoCapture
     *
     * @property endpoint URL of Theta web API endpoint
     * @property cameraModel Camera model info.
     */
    class Builder internal constructor(
        private val endpoint: String,
        private val cameraModel: ThetaRepository.ThetaModel? = null
    ) : PhotoCaptureBase.Builder<Builder>() {
        private var interval: Long? = null

        internal fun isPreset(): Boolean {
            return options._preset != null && (cameraModel == ThetaRepository.ThetaModel.THETA_SC2 || cameraModel == ThetaRepository.ThetaModel.THETA_SC2_B)
        }

        /**
         * Builds an instance of a PhotoCapture that has all the combined parameters of the Options that have been added to the Builder.
         *
         * @return PhotoCapture
         */
        @Throws(Throwable::class)
        suspend fun build(): PhotoCapture {
            try {
                val modeOptions = when (cameraModel) {
                    ThetaRepository.ThetaModel.THETA_X -> Options(
                        captureMode = CaptureMode.IMAGE,
                        _shootingMethod = ShootingMethod.NORMAL,
                    )

                    else -> {
                        Options(captureMode = if (isPreset()) CaptureMode.PRESET else CaptureMode.IMAGE)
                    }
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
            return PhotoCapture(
                endpoint = endpoint,
                options = options,
                checkStatusCommandInterval = interval ?: CHECK_COMMAND_STATUS_INTERVAL,
            )
        }

        fun setCheckStatusCommandInterval(timeMillis: Long): Builder {
            this.interval = timeMillis
            return this
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
         * Set preset mode of Theta SC2 and Theta SC2 for business.
         *
         * @param preset Preset mode
         * @return Builder
         */
        fun setPreset(preset: ThetaRepository.PresetEnum): Builder {
            options._preset = preset.value
            return this
        }

        // TODO: Add set photo option property
    }
}
