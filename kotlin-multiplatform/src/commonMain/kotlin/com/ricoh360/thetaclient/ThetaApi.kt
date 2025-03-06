/*
 * theta API
 */
package com.ricoh360.thetaclient

import com.ricoh360.thetaclient.transferred.*
import com.ricoh360.thetaclient.websocket.WebSocketClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.io.files.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

internal const val ALLOWED_CAPTURE_INTERVAL = 1000

/**
 * Http client using [Ktor](https://jp.ktor.work/clients/index.html)
 */
@OptIn(ExperimentalSerializationApi::class, ExperimentalCoroutinesApi::class) // explicitNulls
internal object ThetaApi {
    val httpClient: HttpClient // for commands other than getLivePreview command
        get() = getHttpClient()

    val previewClient: PreviewClient // Just for getLivePreview command
        get() = getHPreviewClient()

    val multipartPostClient: MultipartPostClient // just for updateFirmware protcol
        get() = getMultipartPostClient()

    val requestScope = CoroutineScope(Dispatchers.Default.limitedParallelism(1))
    var lastSetTimeConsumingOptionTime: Long = 0
    var currentOptions = Options()

    fun initOptions() {
        currentOptions = Options()
        lastSetTimeConsumingOptionTime = 0
    }

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
        return syncExecutor(requestScope, ApiClient.timeout.requestTimeout) {
            httpClient.get(getApiUrl(endpoint, InfoApi.PATH)) {
                headers {
                    append("Content-Type", "application/json; charset=utf-8")
                    append("Cache-Control", "no-store")
                }
            }.body()
        }
    }

    /**
     * Call [/legal-information/open-source-licenses](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/protocols/open_source_licenses.md)
     * @param endpoint Endpoint of Theta web API
     * @return response of /legal-information/open-source-licenses API
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callLicenseApi(
        endpoint: String,
    ): HttpResponse {
        return syncExecutor(requestScope, ApiClient.timeout.requestTimeout) {
            httpClient.get(getApiUrl(endpoint, LicenseApi.PATH))
        }
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
        return syncExecutor(requestScope, ApiClient.timeout.requestTimeout) {
            httpClient.post(getApiUrl(endpoint, StateApi.PATH)) {
                headers {
                    append("Content-Type", "application/json; charset=utf-8")
                    append("Cache-Control", "no-store")
                }
            }.body()
        }
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
        return syncExecutor(requestScope, ApiClient.timeout.requestTimeout) {
            val request = StatusApiRequest(name = params.name, id = params.id)
            val response = httpClient.post(getApiUrl(endpoint, StatusApi.PATH)) {
                headers {
                    append("Content-Type", "application/json; charset=utf-8")
                    append("Cache-Control", "no-store")
                }
                setBody(request)
            }
            decodeStatusApiResponse(response.bodyAsText())
        }
    }

    /**
     * Call update firmware API which is non-public.
     * To execute this function, you have to set environment variable THETA_FU_API_PATH
     * to the path of firmware update API.
     * @param endpoint Endpoint of Theta web API
     * @param apiPath The path of firmware update API which is non-public.
     * @param filePaths List of firmware file path
     * @param connectionTimeout Timeout (milli seconds) of socket connection
     * @param socketTimeout Timeout (milli seconds) of socket
     * @Param callback function to pass the percentage of sent firmware
     * @return response of update firmware API
     *
     * @exception IllegalArgumentException The method has been passed an illegal or inappropriate argument
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    internal suspend fun callUpdateFirmwareApi(
        endpoint: String,
        apiPath: String,
        filePaths: List<String>,
        connectTimeout: Long,
        socketTimeout: Long,
        callback: ((Int) -> Unit)?,
    ): UpdateFirmwareApiResponse {
        val DUMMY_RESPONSE = "{\"name\":\"camera.${apiPath}\",\"state\":\"done\"}"
        if (filePaths.isEmpty()) {
            throw IllegalArgumentException("Empty filePaths")
        }
        val responseBody = multipartPostClient.request(endpoint, apiPath, filePaths, connectTimeout, socketTimeout, callback)
        return if (responseBody.size > 0) Json.decodeFromString<UpdateFirmwareApiResponse>(String(responseBody))
        else Json.decodeFromString(DUMMY_RESPONSE) // Theta X does not send response body
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
        waitCaptureStartTime()
        var retry = 4 // retry count when preview command failed
        val WAIT = 500L // time between retry (ms)
        while (retry-- > 0) {
            try {
                previewClient.request(endpoint)
            } catch (ex: PreviewClientException) {
                if (retry <= 0) throw ex
                runBlocking {
                    delay(WAIT)
                }
                continue
            }

            try {
                while (previewClient.hasNextPart()) {
                    val part = previewClient.nextPart()
                    emit(ByteReadPacket(part.first, 0, part.second))
                }
            } catch (ex: PreviewClientException) {
                if (retry <= 0) throw ex
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
        waitCaptureStartTime()
        var retry = 4 // retry count when preview command failed
        val WAIT = 500L // time between retry (ms)
        while (retry-- > 0) {
            try {
                previewClient.request(endpoint)
            } catch (ex: PreviewClientException) {
                if (retry <= 0) throw ex
                runBlocking {
                    delay(WAIT)
                }
                continue
            }

            var isContinued = true
            try {
                while (isContinued && previewClient.hasNextPart()) {
                    isContinued = frameHandler(previewClient.nextPart())
                }
                if (!isContinued) return
            } finally {
                try {
                    if (isContinued && retry > 0) {
                        delay(WAIT)
                    }
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
        waitCaptureStartTime()
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
        waitCaptureStartTime()
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

    fun updateConsumingOptions(options: Options, isUpdateTime: Boolean = true) {
        var isUpdateOptions = false
        options._filter?.let {
            if (currentOptions._filter != it) {
                currentOptions._filter = it
                isUpdateOptions = true
            }
        }
        options.captureMode?.let {
            if (currentOptions.captureMode != it) {
                currentOptions.captureMode = it
                isUpdateOptions = true
            }
        }
        if (isUpdateTime && isUpdateOptions) {
            lastSetTimeConsumingOptionTime = currentTimeMillis()
        }
    }

    suspend fun waitCaptureStartTime() {
        val interval = currentTimeMillis() - lastSetTimeConsumingOptionTime
        if (interval < ALLOWED_CAPTURE_INTERVAL) {
            println("waitCaptureStartTime wait: ${ALLOWED_CAPTURE_INTERVAL - interval}")
            delay(ALLOWED_CAPTURE_INTERVAL - interval)
        }
        lastSetTimeConsumingOptionTime = 0
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
        val response: SetOptionsResponse = postCommandApi(endpoint, request).body()
        if (response.state == CommandState.DONE) {
            updateConsumingOptions(params.options)
        }
        return response
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
        val response: GetOptionsResponse = postCommandApi(endpoint, request).body()
        response.results?.options?.let {
            updateConsumingOptions(it, false)
        }
        return response
    }

    /**
     * Call [camera._getMySetting](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._get_my_setting.md)
     * @param endpoint Endpoint of Theta web API
     * @param params getMySetting parameters
     * @return response of getMySetting command
     * @see GetMySettingParams
     * @see GetMySettingResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callGetMySettingCommand(
        endpoint: String,
        params: GetMySettingParams,
    ): GetMySettingResponse {
        val request = GetMySettingRequest(parameters = params)
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera._setMySetting](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._set_my_setting.md)
     * @param endpoint Endpoint of Theta web API
     * @param params setMySetting parameters
     * @return response of setMySetting command
     * @see SetMySettingParams
     * @see SetMySettingResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callSetMySettingCommand(
        endpoint: String,
        params: SetMySettingParams,
    ): SetMySettingResponse {
        val request = SetMySettingRequest(parameters = params)
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera._deleteMySetting](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._delete_mysetting.md)
     * @param endpoint Endpoint of Theta web API
     * @param params deleteMySetting parameters
     * @return response of deleteMySetting command
     * @see DeleteMySettingParams
     * @see DeleteMySettingResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callDeleteMySettingCommand(
        endpoint: String,
        params: DeleteMySettingParams,
    ): DeleteMySettingResponse {
        val request = DeleteMySettingRequest(parameters = params)
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera._listPlugins](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._list_plugins.md)
     * @param endpoint Endpoint of Theta web API
     * @return response of listPlugins command
     * @see ListPluginsResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callListPluginsCommand(
        endpoint: String,
    ): ListPluginsResponse {
        val request = ListPluginsRequest()
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera_setPlugin](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._set_plugin.md)
     *
     * @param endpoint Endpoint of Theta web API
     * @param params setPlugin parameters
     * @return response of setPlugin command
     * @see SetPluginResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callSetPluginCommand(
        endpoint: String,
        params: SetPluginParams,
    ): SetPluginResponse {
        val request = SetPluginRequest(parameters = params)
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Call [camera._pluginControl](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera._plugin_control.md)
     *
     * @param endpoint Endpoint of Theta web API
     * @param params _pluginControl parameters
     * @return response of _pluginControl command
     * @see PluginControlResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callPluginControlCommand(
        endpoint: String,
        params: PluginControlParams,
    ): PluginControlResponse {
        val request = PluginControlRequest(parameters = params)
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Acquires the license for the installed plugin
     *
     * @param endpoint Endpoint of Theta web API
     * @param params camera._getPluginLicense parameters
     * @return [HttpResponse](https://api.ktor.io/ktor-client/ktor-client-core/io.ktor.client.statement/-http-response/index.html)
     * If `response.status` is `OK`, you can get html string of the license to call `response.bodyAsText()`.
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callGetPluginLicenseCommand(
        endpoint: String,
        params: GetPluginLicenseParams,
    ): HttpResponse {
        val request = GetPluginLicenseRequest(parameters = params)
        return postCommandApi(endpoint, request)
    }

    /**
     * Return the plugins for plugin mode.
     * Supported just by Theta X and Z1.
     *
     * @param endpoint Endpoint of Theta web API
     * @return list of plugin package names.  For Z1, list length is fixed to three. For X, list length is not fixed.
     * @see GetPluginOrdersResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callGetPluginOrdersCommand(
        endpoint: String,
    ): GetPluginOrdersResponse {
        val request = GetPluginOrdersRequest()
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Sets the plugins for plugin mode.
     *
     * @param endpoint Endpoint of Theta web API
     * @param params camera._setPluginOrders parameters
     * @return no return value
     * @see SetPluginOrdersResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callSetPluginOrdersCommand(
        endpoint: String,
        params: SetPluginOrdersParams,
    ): SetPluginOrdersResponse {
        val request = SetPluginOrdersRequest(parameters = params)
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Registers identification information (UUID) of a BLE device (Smartphone application) connected to the camera.
     * UUID can be set while the wireless LAN function of the camera is placed in the direct mode.
     *
     * @param endpoint Endpoint of Theta web API
     * @param params camera._setBluetoothDevice parameters
     * @return Device name generated from the serial number (S/N) of the camera.
     * @see SetBluetoothDeviceResponse
     * @exception java.net.ConnectException can not connect to target endpoint
     * @exception io.ktor.client.network.sockets.ConnectTimeoutException timeout to connect target endpoint
     * @exception io.ktor.client.plugins.RedirectResponseException target response 3xx status
     * @exception io.ktor.client.plugins.ClientRequestException target response 4xx status
     * @exception io.ktor.client.plugins.ServerResponseException target response 5xx status
     */
    @Throws(Throwable::class)
    suspend fun callSetBluetoothDeviceCommand(
        endpoint: String,
        params: SetBluetoothDeviceParams,
    ): SetBluetoothDeviceResponse {
        val request = SetBluetoothDeviceRequest(parameters = params)
        return postCommandApi(endpoint, request).body()
    }

    /**
     * Post request {body} to {endpoint} APIs then return its response
     */
    private suspend inline fun <reified T : CommandApiRequest> postCommandApi(
        endpoint: String,
        body: T,
    ): HttpResponse {
        return syncExecutor(requestScope, ApiClient.timeout.requestTimeout) {
            httpClient.post(getApiUrl(endpoint, CommandApi.PATH)) {
                headers {
                    append("Content-Type", "application/json; charset=utf-8")
                    append("Cache-Control", "no-store")
                }
                setBody<T>(body)
            }
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

    fun getEventWebSocket(endpoint: String): WebSocketClient {
        return newEventWebSocketClient(endpoint)
    }
}
