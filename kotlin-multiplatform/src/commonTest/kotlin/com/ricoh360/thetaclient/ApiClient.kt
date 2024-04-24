package com.ricoh360.thetaclient

import com.ricoh360.thetaclient.websocket.WebSocketClient
import io.ktor.client.HttpClient

/**
 * Return default HttpClient for unit test.
 *
 * Return mock HttpClient when useMock is true.
 *
 * @return HttpClient
 */
internal fun getHttpClient(): HttpClient {
    return if (MockApiClient.useMock) {
        MockApiClient.mockHttpClient
    } else {
        ApiClient.httpClient ?: run {
            ApiClient.newHttpClient().let { httpClient ->
                ApiClient.httpClient = httpClient
                httpClient
            }
        }
    }
}

/**
 * Return PreviewClient for getLivePreview unit test.
 *
 * Return mock PreviewClient when useMock is true.
 *
 * @return PreviewClient
 */
internal fun getHPreviewClient(): PreviewClient {
    return if (MockApiClient.useMock) {
        MockApiClient.mockPreviewClient
    } else {
        ApiClient.previewClient
    }
}

/**
 * Return MultipartPostClient for updateFirmware unit test.
 *
 * @return mock MultipartPostClient when useMock is true.
 */
fun getMultipartPostClient(): MultipartPostClient {
    return if (MockApiClient.useMock) {
        MockApiClient.mockMultipartPostClient
    } else {
        ApiClient.multipartPostClient
    }
}

internal fun newEventWebSocketClient(endpoint: String): WebSocketClient {
    val httpClient = if (MockApiClient.useMock) {
        MockApiClient.newWebSocketHttpClient()
    } else {
        ApiClient.newWebSocketHttpClient()
    }
    return WebSocketClient(httpClient, endpoint, "/events")
}
