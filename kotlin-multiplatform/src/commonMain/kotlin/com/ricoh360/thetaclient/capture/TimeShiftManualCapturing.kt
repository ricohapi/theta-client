package com.ricoh360.thetaclient.capture

import com.ricoh360.thetaclient.ThetaApi
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CaptureStatus
import com.ricoh360.thetaclient.transferred.ShootingMode
import com.ricoh360.thetaclient.transferred.StartCaptureParams
import com.ricoh360.thetaclient.transferred.StartCaptureResponse
import com.ricoh360.thetaclient.transferred.StopCaptureResponse
import io.ktor.client.plugins.ResponseException
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.Volatile

/*
 * TimeShiftManualCapturing
 *
 * @property endpoint URL of Theta web API endpoint
 * @property callback Success or failure of the call
 */
class TimeShiftManualCapturing internal constructor(
    private val endpoint: String,
    private val callback: TimeShiftManualCapture.StartCaptureCallback
) : Capturing() {
    @Volatile
    internal var secondCaptureResponse: StartCaptureResponse? = null

    @Volatile
    internal var captureStatus: CaptureStatus? = null
    val isAvailableSecondCapture: Boolean
        get() = captureStatus == CaptureStatus.TIME_SHIFT_SHOOTING_IDLE

    private val scope = CoroutineScope(Dispatchers.Default)

    // Has second startCapture been called.
    @Volatile
    internal var secondCalled = false

    /**
     * Starts manual time-shift second capture.
     */
    fun startSecondCapture() {
        if (!isAvailableSecondCapture) {
            println("First Capture is not yet finish.")
            return
        }
        if (secondCalled) {
            println("Second startCapture() has been called.")
            return
        }

        scope.launch {
            try {
                secondCalled = true
                secondCaptureResponse = ThetaApi.callStartCaptureCommand(
                    endpoint = endpoint,
                    params = StartCaptureParams(_mode = ShootingMode.TIMESHIFT_MANUAL_SHOOTING)
                )
            } catch (e: JsonConvertException) {
                callback.onCaptureFailed(
                    exception = ThetaRepository.ThetaWebApiException(
                        message = e.message ?: e.toString()
                    )
                )
            } catch (e: ResponseException) {
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
                callback.onCaptureFailed(
                    exception = ThetaRepository.NotConnectedException(
                        message = e.message ?: e.toString()
                    )
                )
            }
        }
    }

    fun cancelCapture() {
        stopCapture()
    }

    /**
     * Stops time-shift manual.
     * When call stopCapture() then call property callback.
     */
    override fun stopCapture() {
        scope.launch {
            lateinit var response: StopCaptureResponse
            try {
                response = ThetaApi.callStopCaptureCommand(endpoint = endpoint)
                response.error?.let {
                    callback.onStopFailed(exception = ThetaRepository.ThetaWebApiException(message = it.message))
                    return@launch
                }
            } catch (e: JsonConvertException) {
                callback.onStopFailed(exception = ThetaRepository.ThetaWebApiException(message = e.message ?: e.toString()))
                return@launch
            } catch (e: ResponseException) {
                callback.onStopFailed(exception = ThetaRepository.ThetaWebApiException.create(exception = e))
                return@launch
            } catch (e: Exception) {
                callback.onStopFailed(exception = ThetaRepository.NotConnectedException(message = e.message ?: e.toString()))
                return@launch
            }
        }
    }
}
