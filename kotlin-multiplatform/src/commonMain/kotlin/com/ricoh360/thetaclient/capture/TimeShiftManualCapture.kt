package com.ricoh360.thetaclient.capture

import com.ricoh360.thetaclient.CHECK_COMMAND_STATUS_INTERVAL
import com.ricoh360.thetaclient.ThetaApi
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.WeakReference
import com.ricoh360.thetaclient.transferred.CaptureMode
import com.ricoh360.thetaclient.transferred.CaptureStatus
import com.ricoh360.thetaclient.transferred.CommandApiResponse
import com.ricoh360.thetaclient.transferred.CommandState
import com.ricoh360.thetaclient.transferred.FirstShootingEnum
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.SetOptionsParams
import com.ricoh360.thetaclient.transferred.ShootingMode
import com.ricoh360.thetaclient.transferred.StartCaptureParams
import com.ricoh360.thetaclient.transferred.StartCaptureResponse
import com.ricoh360.thetaclient.transferred.StatusApiParams
import com.ricoh360.thetaclient.transferred.TimeShift
import io.ktor.client.plugins.ResponseException
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.experimental.ExperimentalNativeApi

/*
 * TimeShiftManualCapture
 *
 * @property endpoint URL of Theta web API endpoint
 * @property options option of manual time-shift capture
 * @property checkCommandStatusInterval the interval for executing commands/status API when state "inProgress"
 */
@OptIn(ExperimentalNativeApi::class)
class TimeShiftManualCapture private constructor(
    private val endpoint: String,
    options: Options,
    private val checkStatusCommandInterval: Long,
) : Capture(options) {

    private val scope = CoroutineScope(Dispatchers.Default)

    internal var weakCapturing: WeakReference<TimeShiftManualCapturing>? = null
    internal fun getCapturing(): TimeShiftManualCapturing? {
        return weakCapturing?.get()
    }

    fun getCheckStatusCommandInterval(): Long {
        return checkStatusCommandInterval
    }

    /**
     * Get manual time-shift setting object.
     * @return ThetaRepository.TimeShiftSetting
     */
    fun getTimeShiftSetting() = options._timeShift?.let { ThetaRepository.TimeShiftSetting(it) }


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
         * @param fileUrl URL of the manual time-shift. When the manual time-shift is canceled, this URL will be null.
         */
        fun onCaptureCompleted(fileUrl: String?)
    }

    /**
     * Starts manual time-shift.
     *
     * Later, need to call TimeShiftManualCapturing.startSecondCapture() or TimeShiftManualCapturing.stopCapture()
     *
     * @param callback Success or failure of the call
     * @return TimeShiftManualCapturing instance
     */
    fun startCapture(callback: StartCaptureCallback): TimeShiftManualCapturing {
        var capturing: TimeShiftManualCapturing? = null
        scope.launch {
            val monitor = CaptureStatusMonitor(
                endpoint,
                { newStatus, _ ->
                    getCapturing()?.captureStatus = newStatus
                    when (newStatus) {
                        CaptureStatus.SELF_TIMER_COUNTDOWN -> callback.onCapturing(
                            CapturingStatusEnum.SELF_TIMER_COUNTDOWN
                        )

                        CaptureStatus.TIME_SHIFT_SHOOTING -> {
                            callback.onCapturing(
                                CapturingStatusEnum.TIME_SHIFT_SHOOTING
                            )
                        }

                        CaptureStatus.TIME_SHIFT_SHOOTING_IDLE -> {
                            callback.onCapturing(
                                CapturingStatusEnum.TIME_SHIFT_SHOOTING_IDLE
                            )
                        }

                        CaptureStatus.SHOOTING -> callback.onCapturing(
                            CapturingStatusEnum.TIME_SHIFT_SHOOTING_SECOND
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
            lateinit var startCaptureResponse: StartCaptureResponse
            try {
                startCaptureResponse = ThetaApi.callStartCaptureCommand(
                    endpoint = endpoint,
                    params = StartCaptureParams(_mode = ShootingMode.TIMESHIFT_MANUAL_SHOOTING)
                )
                monitor.start()

                runBlocking {
                    var response: CommandApiResponse = startCaptureResponse
                    while (response.state == CommandState.IN_PROGRESS && getCapturing() != null) {
                        delay(timeMillis = checkStatusCommandInterval)

                        var id: String?
                        var capt = getCapturing()
                        capt ?: break
                        id = if (capt.secondCalled) {
                            capt.secondCaptureResponse?.id
                        } else {
                            startCaptureResponse.id
                        }
                        if (id == null) {
                            capt = null
                            continue
                        }
                        capt = null

                        response = ThetaApi.callStatusApi(
                            endpoint = endpoint,
                            params = StatusApiParams(id = id)
                        )
                        callback.onProgress(completion = response.progress?.completion ?: 0f)

                        if (monitor.currentStatus == CaptureStatus.TIME_SHIFT_SHOOTING_IDLE && capturing != null) {
                            // What to do if startSecondCapture is not called
                            capturing = null
                        }
                    }
                    monitor.stop()
                    if (getCapturing() == null && monitor.currentStatus == CaptureStatus.TIME_SHIFT_SHOOTING_IDLE) {
                        // If startSecondCapture is not called, it is not finished and stopCapture must be called.
                        // If you don't call it up, you won't be able to operate it.
                        println("TimeShiftManual cancel")
                        ThetaApi.callStopCaptureCommand(endpoint)
                    }

                    if (response.state == CommandState.DONE) {
                        val captureResponse = response as StartCaptureResponse
                        val fileUrl: String? = captureResponse.results?.fileUrls?.firstOrNull() ?: captureResponse.results?.fileUrl
                        callback.onCaptureCompleted(fileUrl = fileUrl)
                        return@runBlocking
                    }

                    val error = response.error
                    if (error != null && !error.isCanceledShootingCode()) {
                        callback.onCaptureFailed(exception = ThetaRepository.ThetaWebApiException(message = error.message))
                    } else if (response.name == "unknown") {
                        callback.onCaptureFailed(exception = ThetaRepository.ThetaWebApiException(message = "Unknown response"))
                    } else {
                        callback.onCaptureCompleted(fileUrl = null) // canceled
                    }
                }
            } catch (e: JsonConvertException) {
                monitor.stop()
                callback.onCaptureFailed(
                    exception = ThetaRepository.ThetaWebApiException(
                        message = e.message ?: e.toString()
                    )
                )
            } catch (e: ResponseException) {
                monitor.stop()
                if (isCanceledShootingResponse(e.response)) {
                    callback.onCaptureCompleted(fileUrl = null) // canceled
                } else {
                    callback.onCaptureFailed(
                        exception = ThetaRepository.ThetaWebApiException.create(
                            exception = e
                        )
                    )
                }
            } catch (e: Exception) {
                monitor.stop()
                callback.onCaptureFailed(
                    exception = ThetaRepository.NotConnectedException(
                        message = e.message ?: e.toString()
                    )
                )
            }
        }

        val result = TimeShiftManualCapturing(endpoint = endpoint, callback = callback)
        weakCapturing = WeakReference(result)
        capturing = result
        return result
    }

    /*
     * Builder of TimeShiftManualCapture
     *
     * @property endpoint URL of Theta web API endpoint
     */
    class Builder internal constructor(
        private val endpoint: String,
    ) : Capture.Builder<Builder>() {
        private var interval: Long? = null

        /**
         * Builds an instance of a TimeShiftCapture that has all the combined parameters of the Options that have been added to the Builder.
         *
         * @return VideoCapture
         */
        @Throws(Throwable::class)
        suspend fun build(): TimeShiftManualCapture {
            try {
                val modeOptions = Options(
                    captureMode = CaptureMode.IMAGE
                )

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
            return TimeShiftManualCapture(
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
            options._timeShift?.firstShooting =
                if (isFrontFirst) FirstShootingEnum.FRONT else FirstShootingEnum.REAR
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
    }
}
