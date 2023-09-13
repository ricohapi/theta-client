package com.ricoh360.thetaclient.capture

import com.ricoh360.thetaclient.ThetaApi
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.StopCaptureResponse
import io.ktor.client.plugins.ResponseException
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
 * VideoCapturing
 *
 * @property endpoint URL of Theta web API endpoint
 * @property callback Success or failure of the call
 */
class VideoCapturing internal constructor(
    private val endpoint: String,
    private val callback: VideoCapture.StartCaptureCallback
) : Capturing() {

    private val scope = CoroutineScope(Dispatchers.Default)

    /**
     * Stops video capture.
     *   When call stopCapture() then call property callback.
     */
    override fun stopCapture() {
        scope.launch {
            lateinit var response: StopCaptureResponse
            try {
                response = ThetaApi.callStopCaptureCommand(endpoint)
                response.error?.let {
                    callback.onStopFailed(ThetaRepository.ThetaWebApiException(it.message))
                    return@launch
                }
            } catch (e: JsonConvertException) {
                callback.onStopFailed(ThetaRepository.ThetaWebApiException(e.message ?: e.toString()))
                return@launch
            } catch (e: ResponseException) {
                callback.onStopFailed(ThetaRepository.ThetaWebApiException(e.message ?: e.toString()))
                return@launch
            } catch (e: Exception) {
                callback.onStopFailed(ThetaRepository.NotConnectedException(e.message ?: e.toString()))
                return@launch
            }

            val fileUrl = response.results?.fileUrls?.firstOrNull() ?: response.results?.fileUrl ?: ""
            callback.onCaptureCompleted(fileUrl)
        }
    }
}
