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
                        var fileUrl: String? = null;
                        if (response.name == "camera.startCapture") { // Theta X
                            fileUrl = (response as StartCaptureResponse).results?.fileUrls?.firstOrNull()
                        } else if (response.name == "camera.takePicture") { // Theta SC2 for business
                            fileUrl = (response as TakePictureResponse).results?.fileUrl
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
                        _timeShift = TimeShift(firstShooting = FirstShootingEnum.FRONT, firstInterval = 2, secondInterval = 5),
                        exposureDelay = 2, // without this option, sometimes shooting is normal but time-shift
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

        // TODO: Add set photo option property
    }
}
