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

                runBlocking {
                    val id = startCaptureResponse.id
                    while (startCaptureResponse.state == CommandState.IN_PROGRESS) {
                        delay(timeMillis = checkStatusCommandInterval)
                        startCaptureResponse = ThetaApi.callStatusApi(
                            endpoint = endpoint,
                            params = StatusApiParams(id = id)
                        ) as StartCaptureResponse
                        callback.onProgress(completion = startCaptureResponse.progress?.completion ?: 0f)
                    }

                    if (startCaptureResponse.state == CommandState.DONE) {
                        callback.onSuccess(
                            fileUrl = when (startCaptureResponse.results?.fileUrls?.isEmpty() == false) {
                                true -> startCaptureResponse.results?.fileUrls?.first()
                                false -> null
                            }
                        )
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
    class Builder internal constructor(private val endpoint: String, private val cameraModel: String? = null) : Capture.Builder<Builder>() {
        private var interval: Long? = null

        /**
         * Builds an instance of a TimeShiftCapture that has all the combined parameters of the Options that have been added to the Builder.
         *
         * @return VideoCapture
         */
        @Throws(Throwable::class)
        suspend fun build(): TimeShiftCapture {
            try {
                val modeOptions = when (ThetaRepository.ThetaModel.get(cameraModel)) {
                    ThetaRepository.ThetaModel.THETA_X -> Options(captureMode = CaptureMode.IMAGE, _shootingMethod = ShootingMethod.TIMESHIFT)
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
            if (options._timeShift == null) {
                options._timeShift = TimeShift()
            }
            options._timeShift?.firstShooting = if (isFrontFirst) FirstShootingEnum.FRONT else FirstShootingEnum.REAR
            return this
        }

        /**
         * set time (sec) before 1st lens shooting
         *
         * @param interval ThetaRepository.TimeShiftIntervalEnum
         * @return Builder
         */
        fun setFirstInterval(interval: ThetaRepository.TimeShiftIntervalEnum): Builder {
            if (options._timeShift == null) {
                options._timeShift = TimeShift()
            }
            options._timeShift?.firstInterval = interval.sec
            return this
        }

        /**
         * set time (sec) from 1st lens shooting until start of 2nd lens shooting.
         *
         * @param interval ThetaRepository.TimeShiftIntervalEnum
         * @return Builder
         */
        fun setSecondInterval(interval: ThetaRepository.TimeShiftIntervalEnum): Builder {
            if (options._timeShift == null) {
                options._timeShift = TimeShift()
            }
            options._timeShift?.secondInterval = interval.sec
            return this
        }

        // TODO: Add set photo option property
    }
}
