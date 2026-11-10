package com.ricoh360.thetaclient.websocket

import com.ricoh360.thetaclient.ThetaApiLogger
import com.ricoh360.thetaclient.setupDigestAuth
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText

internal const val WEBSOCKET_PING_INTERVAL = 2_000L

internal interface WebSocketHttpClient {
    suspend fun connect(urlString: String): WebSocketHttpSession
}

internal interface WebSocketHttpSession {
    suspend fun receive(): String?
    suspend fun close()
}

internal class WebSocketHttpClientImpl : WebSocketHttpClient {
    val httpClient = HttpClient(CIO) {
        install(WebSockets) {
            pingIntervalMillis = WEBSOCKET_PING_INTERVAL
        }
        install(Logging) {
            logger = ThetaApiLogger()
            level = LogLevel.ALL // ALL, HEADERS, BODY, INFO or NONE
        }
    }

    init {
        setupDigestAuth(httpClient)
    }

    override suspend fun connect(urlString: String): WebSocketHttpSession {
        val session = httpClient.webSocketSession(urlString)
        return WebSocketHttpSessionImpl(session)
    }
}

internal class WebSocketHttpSessionImpl(val session: DefaultClientWebSocketSession) :
    WebSocketHttpSession {
    override suspend fun receive(): String? {
        val othersMessage = session.incoming.receive() as? Frame.Text
        return othersMessage?.readText()
    }

    override suspend fun close() {
        session.close()
    }
}
