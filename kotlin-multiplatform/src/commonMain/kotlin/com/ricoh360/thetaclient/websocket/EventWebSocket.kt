package com.ricoh360.thetaclient.websocket

import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.newEventWebSocketClient
import com.ricoh360.thetaclient.transferred.CameraEventResponse
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

data class CameraEvent(
    val options: ThetaRepository.Options?,
    val state: ThetaRepository.ThetaState?,
) {
    internal constructor(event: CameraEventResponse) : this(
        options = event.options?.let { ThetaRepository.Options(it) },
        state = event.state?.let { ThetaRepository.ThetaState(it) },
    )
}

class EventWebSocket(val endpoint: String) {
    interface Callback {
        fun onReceive(event: CameraEvent)
        fun onClose()
    }

    internal val client = newEventWebSocketClient(endpoint)

    @OptIn(ExperimentalSerializationApi::class)
    @Throws(Throwable::class)
    suspend fun start(callback: Callback) {

        client.start(object : WebSocketClientCallback {
            override fun onReceive(message: String?) {
                message?.let {
                    val js = Json {
                        encodeDefaults = true // Encode properties with default value.
                        explicitNulls = false // Don't encode properties with null value.
                        ignoreUnknownKeys = true // Ignore unknown keys on decode.
                    }
                    try {
                        val response = js.decodeFromString<CameraEventResponse>(it)
                        callback.onReceive(CameraEvent(response))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onClose() {
                callback.onClose()
            }
        })
    }

    suspend fun stop() {
        client.stop()
    }
}
