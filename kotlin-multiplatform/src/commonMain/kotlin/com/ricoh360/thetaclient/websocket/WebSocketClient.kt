package com.ricoh360.thetaclient.websocket

import com.ricoh360.thetaclient.ThetaRepository
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


internal interface WebSocketClientCallback {
    fun onReceive(message: String?)
    fun onClose()
}

internal class WebSocketClient(
    val httpClient: WebSocketHttpClient,
    val endpoint: String,
    val path: String
) {
    var session: WebSocketHttpSession? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    suspend fun start(
        callback: WebSocketClientCallback,
    ) {
        session?.let {
            stop()
        }

        try {
            session = httpClient.connect(
                changeUrlToWebSocket(getApiUrl(endpoint, path)),
            )
        } catch (e: Exception) {
            throw ThetaRepository.ThetaWebApiException(
                e.message ?: "Error. Can not open websocket."
            )
        }
        scope.launch {
            while (session != null && isActive) {
                try {
                    val message = session?.receive()
                    callback.onReceive(message)
                } catch (e: Exception) {
                    println("Closed websocket")
                    e.printStackTrace()
                    break
                }
            }
            stop()
            callback.onClose()
        }
    }

    suspend fun stop() {
        session?.close()
        session = null
    }
}

fun changeUrlToWebSocket(url: String): String {
    val orgUrl = Url(url)
    val builder = URLBuilder(
        protocol = URLProtocol.WS,
        host = orgUrl.host,
        port = orgUrl.port,
        pathSegments = orgUrl.pathSegments,
        parameters = orgUrl.parameters,
        fragment = orgUrl.fragment,
        user = orgUrl.user,
        password = orgUrl.password,
        trailingQuery = orgUrl.trailingQuery,
    )
    return builder.buildString()
}

private fun getApiUrl(
    endpoint: String,
    apiPath: String,
): String {
    return if (endpoint.endsWith('/')) {
        endpoint.dropLast(1) + apiPath
    } else {
        endpoint + apiPath
    }
}
