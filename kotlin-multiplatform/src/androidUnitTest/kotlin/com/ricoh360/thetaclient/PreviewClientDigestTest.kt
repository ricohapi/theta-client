package com.ricoh360.thetaclient

import kotlinx.coroutines.runBlocking
import java.net.ServerSocket
import kotlin.concurrent.thread
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Digest authentication of PreviewClient with a local HTTP server.
 */
class PreviewClientDigestTest {
    @AfterTest
    fun teardown() {
        ApiClient.digestAuth = null
    }

    @Test
    fun digestUriTest() = runBlocking {
        val server = ServerSocket(0)
        val authorizations = mutableListOf<String?>()
        val serverThread = thread {
            repeat(2) { index ->
                server.accept().use { socket ->
                    val reader = socket.getInputStream().bufferedReader()
                    val headers = generateSequence { reader.readLine() }.takeWhile { it.isNotEmpty() }.toList()
                    authorizations.add(headers.firstOrNull { it.startsWith("Authorization:") })
                    // Read the body before closing, or the connection is reset.
                    val length = headers.firstOrNull { it.startsWith("Content-Length:") }?.substringAfter(':')?.trim()?.toInt() ?: 0
                    repeat(length) { reader.read() }
                    val response = if (index == 0) {
                        "HTTP/1.1 401 Unauthorized\r\n" +
                            "WWW-Authenticate: Digest realm=\"RICOH360\", nonce=\"dummy\", qop=\"auth\", algorithm=MD5\r\n" +
                            "Content-Length: 0\r\n\r\n"
                    } else {
                        "HTTP/1.1 400 Bad Request\r\nContent-Length: 0\r\n\r\n"
                    }
                    socket.getOutputStream().write(response.toByteArray())
                }
            }
        }
        ApiClient.digestAuth = DigestAuth("THETAAA12345678", "password")

        val client = PreviewClientImpl()
        // The response to the authenticated request does not matter: only the request is checked.
        runCatching {
            client.request(
                "http://127.0.0.1:${server.localPort}",
                "POST",
                "/osc/commands/execute",
                "{\"name\":\"camera.getLivePreview\",\"parameters\":{}}",
                "application/json",
            )
        }
        client.close()
        serverThread.join(5_000)
        server.close()

        val authorization = authorizations.getOrNull(1)
        assertTrue(authorization?.contains("uri=\"/osc/commands/execute\"") == true, "$authorization")
    }
}
