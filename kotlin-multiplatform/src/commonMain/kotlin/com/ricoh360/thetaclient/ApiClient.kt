package com.ricoh360.thetaclient

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
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
                logger = Logger.SIMPLE // DEFAULT, SIMPLE or EMPTY
                level = LogLevel.ALL // ALL, HEADERS, BODY, INFO or NONE
            }
        }
        setupDigestAuth(client)
        return client
    }

    internal val previewClient = PreviewClientImpl()
    internal val multipartPostClient = MultipartPostClientImpl(connectionTimeout = 30_000L, socketTimeout = 300_000L)
}

/**
 * Return default HttpClient
 *
 * Override when unit test.
 *
 * @return HttpClient
 */
fun getHttpClient(): HttpClient {
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
fun getPreviewClient(): PreviewClient {
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
