package com.ricoh360.thetaclient

import com.ricoh360.thetaclient.websocket.WebSocketHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class) // explicitNulls
internal object MockApiClient {
    var onRequest: ((HttpRequestData) -> ByteReadChannel)? = null
    var status: HttpStatusCode? = null
    var responseHeaders: Headers? = null
    var useMock = true
    var onPreviewRequest: (
        (
        endpoint: String,
        method: String,
        path: String,
        body: String,
        contentType: String
    ) -> PreviewClient
    )? = null
    var onPreviewClose: (() -> Unit)? = null
    var onPreviewHasNextPart: (() -> Boolean)? = null
    var onPreviewNextPart: (() -> Pair<ByteArray, Int>)? = null
    var onMultipartPostRequest: (
        (
        endpoint: String,
        path: String,
        filePaths: List<String>,
        boundary: String
    ) -> ByteArray
    )? = null
    var onCallWebSocketConnect: ((urlString: String) -> Unit)? = null
    var onCallWebSocketClose: (() -> Unit)? = null

    val mockHttpClient = HttpClient(MockEngine) {
        expectSuccess = true
        engine {
            addHandler { request ->
                if (onRequest == null) {
                    throw Exception("")
                }
                respond(
                    content = onRequest?.let { it(request) } ?: run { throw Exception("") },
                    status = status ?: HttpStatusCode.OK,
                    headers = responseHeaders ?: headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        }
        install(ContentNegotiation) {
            json(
                Json {
                    encodeDefaults = true // Encode properties with default value.
                    explicitNulls = false // Don't encode properties with null value.
                    ignoreUnknownKeys = true // Ignore unknown keys on decode.
                }
            )
        }
        install(Logging) {
            logger = Logger.DEFAULT // DEFAULT, SIMPLE or EMPTY
            level = LogLevel.ALL // ALL, HEADERS, BODY, INFO or NONE
        }
    }

    val mockPreviewClient = object : PreviewClient {
        override suspend fun request(
            endpoint: String,
            method: String,
            path: String,
            body: String,
            contentType: String
        ): PreviewClient {
            onPreviewRequest?.let { it(endpoint, method, path, body, contentType) }
            return this
        }

        override suspend fun hasNextPart(): Boolean {
            return if (onPreviewHasNextPart == null) {
                true
            } else {
                onPreviewHasNextPart!!()
            }
        }

        override suspend fun nextPart(): Pair<ByteArray, Int> {
            return onPreviewNextPart?.let { it() } ?: Pair(ByteArray(1), 1)
        }

        override suspend fun close() {
            onPreviewClose?.let { it() }
        }
    }

    val mockMultipartPostClient = object : MultipartPostClient {
        override suspend fun request(
            endpoint: String,
            path: String,
            filePaths: List<String>,
            connectionTimeout: Long,
            socketTimeout: Long,
            callback: ((Int) -> Boolean)?,
            boundary: String,
        ): ByteArray {
            onMultipartPostRequest?.let {
                return it(endpoint, path, filePaths, boundary)
            }
            return byteArrayOf()
        }
    }

    val mockWebSocketHttpClient = MockWebSocketHttpClient()

    internal fun newWebSocketHttpClient(): WebSocketHttpClient {
        mockWebSocketHttpClient.onCallConnect = onCallWebSocketConnect
        mockWebSocketHttpClient.onCallClose = onCallWebSocketClose
        return mockWebSocketHttpClient
    }

    init {
        setupDigestAuth(mockHttpClient)
    }

    fun reset() {
        onRequest = null
        status = null
        responseHeaders = null
        onPreviewRequest = null
        onPreviewClose = null
        onPreviewHasNextPart = null
        onPreviewNextPart = null
        onMultipartPostRequest = null
        mockWebSocketHttpClient.clear()
        onCallWebSocketConnect = null
        onCallWebSocketClose = null
    }
}
