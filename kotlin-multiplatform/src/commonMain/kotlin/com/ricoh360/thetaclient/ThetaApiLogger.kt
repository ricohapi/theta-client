package com.ricoh360.thetaclient

import io.ktor.client.plugins.logging.Logger

internal class ThetaApiLogger : Logger {
    override fun log(message: String) {
        val newMessage = "ThetaApi: $message"
        println(newMessage)
        ThetaApi.apiLogListener?.let { it(newMessage) }
    }
}
