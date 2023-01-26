package com.ricoh360.thetaclient

import io.ktor.client.*

/**
 * Return default HttpClient for unit test.
 *
 * Return mock HttpClient when useMock is true.
 *
 * @return HttpClient
 */
fun getHttpClient(): HttpClient {
    return if (MockApiClient.useMock) {
        MockApiClient.mockHttpClient
    } else {
        if (ApiClient.httpClient == null) {
            ApiClient.httpClient = ApiClient.newHttpClient()
        }
        ApiClient.httpClient!!
    }
}

/**
 * Return PreviewClient for getLivePreview unit test.
 *
 * Return mock PreviewClient when useMock is true.
 *
 * @return PreviewClient
 */
fun getHPreviewClient(): PreviewClient {
    return if (MockApiClient.useMock) {
        MockApiClient.mockPreviewClient
    } else {
        ApiClient.previewClient
    }
}
