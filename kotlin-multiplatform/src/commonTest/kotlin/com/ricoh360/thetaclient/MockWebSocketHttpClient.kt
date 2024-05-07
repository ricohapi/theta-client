package com.ricoh360.thetaclient

import com.ricoh360.thetaclient.websocket.WebSocketHttpClient
import com.ricoh360.thetaclient.websocket.WebSocketHttpSession
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.withTimeout

internal class MockWebSocketHttpClient : WebSocketHttpClient {

    var onCallConnect: ((urlString: String) -> Unit)? = null
    var onCallClose: (() -> Unit)? = null

    var session: MockWebSocketHttpSession? = null

    override suspend fun connect(urlString: String): WebSocketHttpSession {
        onCallConnect?.let { it(urlString) }
        val mockSession = MockWebSocketHttpSession(onCallClose)
        session = mockSession
        return mockSession
    }

    suspend fun sendMessage(message: String?) {
        session?.sendMessage(message)
    }

    suspend fun sendException(message: String) {
        session?.sendException(message)
    }

    fun clear() {
        onCallConnect = null
        onCallClose = null
        session?.let {
            it.deferred?.complete(null)
        }
        session = null
    }
}

internal class MockWebSocketHttpSession(
    val onCallClose: (() -> Unit)?,
) : WebSocketHttpSession {

    var deferred: CompletableDeferred<String?>? = null

    override suspend fun receive(): String? {
        deferred = CompletableDeferred()
        val message = try {
            withTimeout(100_000) {
                deferred?.await()
            }
        } catch (e: Exception) {
            throw e
        }
        deferred = null
        return message
    }

    override suspend fun close() {
        onCallClose?.let { it() }
        deferred?.completeExceptionally(Exception("close"))
    }

    suspend fun sendMessage(message: String?) {
        deferred?.complete(message)
    }

    suspend fun sendException(message: String) {
        deferred?.completeExceptionally(Exception(message))
    }
}
