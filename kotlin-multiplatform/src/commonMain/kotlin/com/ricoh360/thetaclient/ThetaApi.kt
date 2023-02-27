/*
 * theta API
 */
package com.ricoh360.thetaclient

import com.ricoh360.thetaclient.transferred.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi

/**
 * Http client using [Ktor](https://jp.ktor.work/clients/index.html)
 */
@OptIn(ExperimentalSerializationApi::class) // explicitNulls
object ThetaApi {
    val httpClient: HttpClient // for commands other than getLivePreview command
        get() = getHttpClient()

    val previewClient: PreviewClient // Just for getLivePreview command
        get() = getHPreviewClient()

    /**
     * Call [/osc/info](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/protocols/info.md)
     * @param endpoint Endpoint of Theta web API
     * @return response of Theta web API
     * @see InfoApiResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callInfoApi(
        endpoint: String,
    ): InfoApiResponse {
        return httpClient.get(getApiUrl(endpoint, InfoApi.PATH)).body()
    }

    /**
     * Call [/osc/state](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/protocols/state.md)
     * @param endpoint Endpoint of Theta web API
     * @return response of /osc/state API
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callStateApi(
        endpoint: String,
    ): StateApiResponse {
        return httpClient.post(getApiUrl(endpoint, StateApi.PATH)).body()
    }

    /**
     * Call [/osc/commands/status](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/protocols/commands_status.md)
     * @param endpoint Endpoint of Theta web API
     * @param params status parameters
     * @return response of /osc/commands/status API
     * @see StatusApiParams
     * @see CommandApiResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callStatusApi(
        endpoint: String,
        params: StatusApiParams,
    ): CommandApiResponse {
        val request = StatusApiRequest(name = params.name, id = params.id)
        val response = httpClient.post(getApiUrl(endpoint, StatusApi.PATH)) {
            headers {
                append("Content-Type", "application/json; charset=utf-8")
                append("Cache-Control", "no-cache")
            }
            setBody(request)
        }
        return decodeStatusApiResponse(response.bodyAsText())
    }

    /*
     * Call camera commands
     */

    /**
     * Call [camera.startSession](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.0/commands/camera.start_session.md)
     * @param endpoint Endpoint of Theta web API
     * @return response of startSession command.
     * @see StartSessionResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callStartSessionCommand(
        endpoint: String,
    ): StartSessionResponse {
        val request = StartSessionRequest()
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera._cancelVideoConvert](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._cancel_video_convert.md)
     * @param endpoint Endpoint of Theta web API
     * @return response of cancelVideoConvert command.
     * @see CancelVideoConvertResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callCancelVideoConvertCommand(
        endpoint: String,
    ): CancelVideoConvertResponse {
        val request = CancelVideoConvertRequest()
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera._convertVideoFormats](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._convert_video_formats.md)
     * @param endpoint Endpoint of Theta web API
     * @param params convert video formats parameters
     * @return response of convertVideoFormats command
     * @see ConvertVideoFormatsParams
     * @see ConvertVideoFormatsResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callConvertVideoFormatsCommand(
        endpoint: String,
        params: ConvertVideoFormatsParams,
    ): ConvertVideoFormatsResponse {
        val request = ConvertVideoFormatsRequest(parameters = params)
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera.delete](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.delete.md)
     * @param endpoint Endpoint of Theta web API
     * @param params delete fileUrls parameters
     * @return response of delete command
     * @see DeleteParams
     * @see DeleteResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callDeleteCommand(
        endpoint: String,
        params: DeleteParams,
    ): DeleteResponse {
        val request = DeleteRequest(parameters = params)
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera._finishWlan](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._finish_wlan.md)
     * @param endpoint Endpoint of Theta web API
     * @return response of _finishWlan command
     * @see FinishWlanResponse
     */
    @Throws(Throwable::class)
    suspend fun callFinishWlanCommand(
        endpoint: String,
    ): FinishWlanResponse {
        val request = FinishWlanRequest()
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera.getLivePreview](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.get_live_preview.md)
     * @param endpoint Endpoint of Theta web API
     * @return You can get the newest frame in a CoroutineScope like this:
     * ```kotlin
     * callGetLivePreviewCommand(endpoint)
     *     .collect { byteReadPacket ->
     *         if (isActive) {
     *             // Read byteReadPacket
     *         }
     *         byteReadPacket.release()
     *     }
     * ```
     * @exception com.ricoh360.thetaclient.PreviewClientException can not get preview frames
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    fun callGetLivePreviewCommand(endpoint: String): Flow<ByteReadPacket> = flow {
        while(true) {
            var retry = 3 // retry count when preview command failed
            val WAIT = 500L // time between retry (ms)
            var isPreviewStarted = false

            while(retry-- > 0) {
                kotlin.runCatching {
                    previewClient.request(endpoint)
                }.onSuccess {
                    isPreviewStarted = true
                }.onFailure {
                    if(retry <= 0) {
                        throw(PreviewClientException("Can't start preview"))
                    }
                    runBlocking {
                        delay(WAIT)
                    }
                }
                if (isPreviewStarted) break
            }

            try {
                while (previewClient.hasNextPart()) {
                    val part = previewClient.nextPart()
                    emit(ByteReadPacket(part.first, 0, part.second))
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                throw t
            } finally {
                try {
                    previewClient.close()
                } catch (_: Throwable) {
                }
            }

            // Sometimes Theta SC2 doesn't send chunk data, so hasNextPart() finishes.
            // After a while, call preview command again.
            runBlocking {
                delay(WAIT)
            }
        }
    }

    /**
     * Call [camera.getLivePreview](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.get_live_preview.md)
     * @param endpoint Endpoint of Theta web API
     * @param frameHandler Callback function for each JPEG data.  If
     * it returns false, this function exits.
     * @exception com.ricoh360.thetaclient.PreviewClientException can not get preview frames
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callGetLivePreviewCommand(
        endpoint: String,
        frameHandler: suspend (Pair<ByteArray, Int>) -> Boolean,
    ) {
        while(true) {
            var retry = 3 // retry count when preview command failed
            val WAIT = 500L // time between retry (ms)
            var isPreviewStarted = false

            while(retry-- > 0) {
                kotlin.runCatching {
                    previewClient.request(endpoint)
                }.onSuccess {
                    isPreviewStarted = true
                }.onFailure {
                    if(retry <= 0) {
                        throw(PreviewClientException("Can't start preview"))
                    }
                    runBlocking {
                        delay(WAIT)
                    }
                }
                if (isPreviewStarted) break
            }

            try {
                var isContinued = true
                while (isContinued && previewClient.hasNextPart()) {
                    isContinued = frameHandler(previewClient.nextPart())
                }
                if (!isContinued) return
            } catch (t: Throwable) {
                t.printStackTrace()
                throw t
            } finally {
                try {
                    previewClient.close()
                } catch (_: Throwable) {
                }
            }

            // Sometimes Theta SC2 doesn't send chunk data, so hasNextPart() finishes.
            // After a while, call preview command again.
            runBlocking {
                delay(WAIT)
            }
        }
    }

    /**
     * Call [camera._getMetadata](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._get_metadata.md)
     * @param endpoint Endpoint of Theta web API
     * @param params fileUrl to get metadata parameters
     * @return response of _getMetadata command
     * @see GetMetadataParams
     * @see GetMetadataResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callGetMetadataCommand(
        endpoint: String,
        params: GetMetadataParams,
    ): GetMetadataResponse {
        val request = GetMetadataRequest(parameters = params)
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera.listFiles](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.list_files.md)
     * @param endpoint Endpoint of Theta web API
     * @param params list files parameters
     * @return response of listFiles command
     * @see ListFilesParams
     * @see ListFilesResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callListFilesCommand(
        endpoint: String,
        params: ListFilesParams,
    ): ListFilesResponse {
        val request = ListFilesRequest(parameters = params)
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera.reset](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.reset.md)
     * @param endpoint Endpoint of Theta web API
     * @return response of reset command
     * @see ResetResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callResetCommand(
        endpoint: String,
    ): ResetResponse {
        val request = ResetRequest()
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera._listAccessPoints](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._list_access_points.md)
     * @param endpoint Endpoint of Theta web API
     * @return response of _listAccessPoints command
     * @see ListAccessPointsResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callListAccessPointsCommand(
        endpoint: String,
    ): ListAccessPointsResponse {
        val request = ListAccessPointsRequest()
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera.startCapture](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.start_capture.md)
     * @param endpoint Endpoint of Theta web API
     * @return response of startCapture command
     * @see StartCaptureParams
     * @see StartCaptureResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callStartCaptureCommand(
        endpoint: String,
        params: StartCaptureParams,
    ): StartCaptureResponse {
        val request = StartCaptureRequest(parameters = params)
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera.stopCapture](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.stop_capture.md)
     * @param endpoint Endpoint of Theta web API
     * @return response of stopCapture command
     * @see StopCaptureResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callStopCaptureCommand(
        endpoint: String,
    ): StopCaptureResponse {
        val request = StopCaptureRequest()
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera.takePicture](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.take_picture.md)
     * @param endpoint Endpoint of Theta web API
     * @return response of takePicture command
     * @see TakePictureResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callTakePictureCommand(
        endpoint: String,
    ): TakePictureResponse {
        val request = TakePictureRequest()
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera._deleteAccessPoint](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._delete_access_point.md)
     * @param endpoint Endpoint of Theta web API
     * @param params delete access point parameters
     * @return response of _deleteAccessPoint command
     * @see DeleteAccessPointParams
     * @see DeleteAccessPointResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callDeleteAccessPointCommand(
        endpoint: String,
        params: DeleteAccessPointParams,
    ): DeleteAccessPointResponse {
        val request = DeleteAccessPointRequest(parameters = params)
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera._setAccessPoint](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._set_access_point.md)
     * @param endpoint Endpoint of Theta web API
     * @param params set access point parameters
     * @return response of _setAccessPoint command
     * @see SetAccessPointParams
     * @see SetAccessPointResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callSetAccessPointCommand(
        endpoint: String,
        params: SetAccessPointParams,
    ): SetAccessPointResponse {
        val request = SetAccessPointRequest(parameters = params)
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera._stopSelfTimer](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._stop_self_timer.md)
     * @param endpoint Endpoint of Theta web API
     * @return response of _stopSelfTimer command
     * @see StopSelfTimerResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callStopSelfTimerCommand(
        endpoint: String,
    ): StopSelfTimerResponse {
        val request = StopSelfTimerRequest()
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera.setOptions](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.set_options.md)
     * @param endpoint Endpoint of Theta web API
     * @param params set options parameters
     * @return response of setOptions command
     * @see SetOptionsParams
     * @see SetOptionsResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callSetOptionsCommand(
        endpoint: String,
        params: SetOptionsParams,
    ): SetOptionsResponse {
        val request = SetOptionsRequest(parameters = params)
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera.getOptions](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.get_options.md)
     * @param endpoint Endpoint of Theta web API
     * @param params get options parameters
     * @return response of getOptions command
     * @see GetOptionsParams
     * @see GetOptionsResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callGetOptionsCommand(
        endpoint: String,
        params: GetOptionsParams,
    ): GetOptionsResponse {
        val request = GetOptionsRequest(parameters = params)
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Post request {body} to {endpoint} APIs then return its response
     */
    private suspend fun postCommandApi(
        endpoint: String,
        body: CommandApiRequest,
    ): HttpResponse {
        return httpClient.post(getApiUrl(endpoint, CommandApi.PATH)) {
            headers {
                append("Content-Type", "application/json; charset=utf-8")
                append("Cache-Control", "no-cache")
            }
            setBody(body)
        }
    }

    /**
     * Concatinate the {endpoint} of Theta and each {apiPath}.
     */
    private fun getApiUrl(
        endpoint: String,
        apiPath: String,
    ): String {
        if (endpoint.endsWith('/')) {
            return endpoint.dropLast(1) + apiPath
        } else {
            return endpoint + apiPath
        }
    }
}
