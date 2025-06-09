package com.ricoh360.thetaclient

import com.ricoh360.thetaclient.websocket.WebSocketClient
import com.ricoh360.thetaclient.websocket.WebSocketHttpClient
import com.ricoh360.thetaclient.websocket.WebSocketHttpClientImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

/*
 * HttpClient management.
 */
@OptIn(ExperimentalSerializationApi::class) // explicitNulls
internal object ApiClient {
    /**
     * Timeout of HTTP call.
     */
    var timeout = ThetaRepository.Timeout()
        set(value) {
            field = value
            httpClient?.close()
            httpClient = null
        }

    var digestAuth: DigestAuth? = null

    var httpClient: HttpClient? = null
    fun newHttpClient(): HttpClient {
        val client = HttpClient(CIO) {
            expectSuccess = true
            engine {
                endpoint {
                    connectTimeout = timeout.connectTimeout
                    requestTimeout = timeout.requestTimeout
                    socketTimeout = timeout.socketTimeout
                }
            }
            // See [ContentNegotiation](https://ktor.io/docs/serialization.html)
            install(ContentNegotiation) {
                json(
                    Json {
                        encodeDefaults = true // Encode properties with default value.
                        explicitNulls = false // Don't encode properties with null value.
                        ignoreUnknownKeys = true // Ignore unknown keys on decode.
                    }
                )
            }
            // See [Logging](https://ktor.io/docs/client-logging.html)
            install(Logging) {
                logger = ThetaApiLogger()
                level = LogLevel.ALL // ALL, HEADERS, BODY, INFO or NONE
            }
        }
        setupDigestAuth(client)
        return client
    }

    internal val previewClient = PreviewClientImpl()
    internal val multipartPostClient = MultipartPostClientImpl()

    internal fun newWebSocketHttpClient(): WebSocketHttpClient {
        return WebSocketHttpClientImpl()
    }
}

/**
 * Return default HttpClient
 *
 * Override when unit test.
 *
 * @return HttpClient
 */
internal fun getHttpClient(): HttpClient {
    return ApiClient.httpClient ?: run {
        ApiClient.newHttpClient().let { httpClient ->
            ApiClient.httpClient = httpClient
            httpClient
        }
    }
}

/**
 * Return default PreviewClient for preview
 *
 * Override when unit test.
 *
 * @return PreviewClient
 */
internal fun getHPreviewClient(): PreviewClient {
    return ApiClient.previewClient
}

/**
 * Return default MultipartPostHttpClient for update firmware
 *
 * Override when unit test.
 *
 * @return MultipartPostHttpClient
 */
fun getMultipartPostClient(): MultipartPostClient {
    return ApiClient.multipartPostClient
}

internal fun newEventWebSocketClient(endpoint: String): WebSocketClient {
    return WebSocketClient(ApiClient.newWebSocketHttpClient(), endpoint, "/events")
}
