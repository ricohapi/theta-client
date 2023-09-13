package com.ricoh360.thetaclient.capture

import com.ricoh360.thetaclient.CHECK_COMMAND_STATUS_INTERVAL
import com.ricoh360.thetaclient.ThetaApi
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.*
import io.ktor.client.plugins.*
import io.ktor.serialization.*
import kotlinx.coroutines.*

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
    private val checkStatusCommandInterval: Long
) : Capture(options) {

    private val scope = CoroutineScope(Dispatchers.Default)

    fun getCheckStatusCommandInterval(): Long {
        return checkStatusCommandInterval
    }

    /**
     * Get Time-shift setting object.
     * @return ThetaRepository.TimeShiftSetting
     */
    fun getTimeShiftSetting() = options._timeShift?.let { ThetaRepository.TimeShiftSetting(it) }


    // TODO: Add get photo option property

    /**
     * Callback of startCapture
     */
    interface StartCaptureCallback {
        /**
         * Called when successful.
         *
         * @param fileUrl URL of the time-shift. When the time-shift is canceled, this URL will be null.
         */
        fun onSuccess(fileUrl: String?)

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
     * Starts time-shift.
     *
     * @param callback Success or failure of the call
     * @return TimeShiftCapturing instance
     */
    fun startCapture(callback: StartCaptureCallback): TimeShiftCapturing {
        scope.launch {
            lateinit var startCaptureResponse: StartCaptureResponse
            try {
                startCaptureResponse = ThetaApi.callStartCaptureCommand(
                    endpoint = endpoint,
                    params = StartCaptureParams(_mode = ShootingMode.TIME_SHIFT_SHOOTING)
                )

                /*
                 * Note that Theta SC2 for business returns a response different from Theta X like this:
                 *   {"name":"camera.takePicture","id":"2543","progress":{"completion":0.0},"state":"inProgress"}
                 *   {"name":"camera.takePicture","results":{"fileUrl":"http://192.168.1.1/files/thetasc22050e7735b9b5838795e2ee7/100RICOH/R0010075.JPG"},"state":"done"}
                 * So it can not cast to StartCaptureResponse.
                 * Dirty hacks are unavoidable!
                 */
                runBlocking {
                    val id = startCaptureResponse.id
                    var response: CommandApiResponse = startCaptureResponse
                    while (response.state == CommandState.IN_PROGRESS) {
                        delay(timeMillis = checkStatusCommandInterval)
                        response = ThetaApi.callStatusApi(
                            endpoint = endpoint,
                            params = StatusApiParams(id = id)
                        )
                        callback.onProgress(completion = response.progress?.completion ?: 0f)
                    }

                    if (response.state == CommandState.DONE) {
                        var fileUrl: String? = when (response.name) {
                            // Theta X returns "results.fileUrls".
                            // Theta SC2 for business (after taking a video) returns "results.fileUrl".
                            "camera.startCapture" -> {
                                val captureResponse = response as StartCaptureResponse
                                captureResponse.results?.fileUrls?.firstOrNull() ?: captureResponse.results?.fileUrl
                            }
                            // Theta SC2 for business after taking a photo
                            "camera.takePicture" -> (response as TakePictureResponse).results?.fileUrl
                            else -> null
                        }
                        callback.onSuccess(fileUrl = fileUrl)
                        return@runBlocking
                    }
                    callback.onError(exception = ThetaRepository.ThetaWebApiException(message = startCaptureResponse.error?.message ?: startCaptureResponse.error.toString()))
                }
            } catch (e: JsonConvertException) {
                callback.onError(exception = ThetaRepository.ThetaWebApiException(message = e.message ?: e.toString()))
            } catch (e: ResponseException) {
                callback.onError(exception = ThetaRepository.ThetaWebApiException.create(exception = e))
            } catch (e: Exception) {
                callback.onError(exception = ThetaRepository.NotConnectedException(message = e.message ?: e.toString()))
            }
        }

        return TimeShiftCapturing(endpoint = endpoint, callback = callback)
    }

    /*
     * Builder of TimeShiftCapture
     *
     * @property endpoint URL of Theta web API endpoint
     * @property cameraModel Camera model info.
     */
    class Builder internal constructor(private val endpoint: String, private val cameraModel: ThetaRepository.ThetaModel? = null) : Capture.Builder<Builder>() {
        private var interval: Long? = null

        /**
         * Builds an instance of a TimeShiftCapture that has all the combined parameters of the Options that have been added to the Builder.
         *
         * @return VideoCapture
         */
        @Throws(Throwable::class)
        suspend fun build(): TimeShiftCapture {
            try {
                val modeOptions = when (cameraModel) {
                    ThetaRepository.ThetaModel.THETA_X -> Options(captureMode = CaptureMode.IMAGE, _shootingMethod = ShootingMethod.TIMESHIFT)
                    ThetaRepository.ThetaModel.THETA_SC2_B -> Options(
                        captureMode = CaptureMode.PRESET,
                        _preset = Preset.ROOM,
                        _timeShift = TimeShift(firstShooting = FirstShootingEnum.FRONT, firstInterval = SC2B_DEFAULT_FIRST_INTERVAL, secondInterval = SC2B_DEFAULT_SECOND_INTERVAL),
                        exposureDelay = SC2B_DEFAULT_EXPOSURE_DELAY, // without this option, sometimes shooting is normal but time-shift
                    )

                    else -> Options(captureMode = CaptureMode.IMAGE)
                }

                ThetaApi.callSetOptionsCommand(
                    endpoint = endpoint,
                    params = SetOptionsParams(options = modeOptions)
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
            return TimeShiftCapture(
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
         * Set is front first.
         *
         * @param isFrontFirst is front first
         * @return Builder
         */
        fun setIsFrontFirst(isFrontFirst: Boolean): Builder {
            checkAndInitTimeShiftSetting()
            options._timeShift?.firstShooting = if (isFrontFirst) FirstShootingEnum.FRONT else FirstShootingEnum.REAR
            return this
        }

        /**
         * set time (sec) before 1st lens shooting
         *
         * @param interval 1st interval
         * @return Builder
         */
        fun setFirstInterval(interval: ThetaRepository.TimeShiftIntervalEnum): Builder {
            checkAndInitTimeShiftSetting()
            options._timeShift?.firstInterval = interval.sec
            return this
        }

        /**
         * set time (sec) from 1st lens shooting until start of 2nd lens shooting.
         *
         * @param interval 2nd interval
         * @return Builder
         */
        fun setSecondInterval(interval: ThetaRepository.TimeShiftIntervalEnum): Builder {
            checkAndInitTimeShiftSetting()
            options._timeShift?.secondInterval = interval.sec
            return this
        }

        private fun checkAndInitTimeShiftSetting() {
            if (options._timeShift == null) {
                options._timeShift = TimeShift()
            }
        }

        // TODO: Add set photo option property

        companion object {
            // default values for time-shift settings of Theta SC2 for business
            const val SC2B_DEFAULT_FIRST_INTERVAL = 2
            const val SC2B_DEFAULT_SECOND_INTERVAL = 5
            const val SC2B_DEFAULT_EXPOSURE_DELAY = 2
        }
    }
}
