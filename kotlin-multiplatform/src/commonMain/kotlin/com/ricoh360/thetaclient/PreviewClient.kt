/*
 * http client for live preview only
 */
package com.ricoh360.thetaclient

import com.ricoh360.thetaclient.PreviewClient.Companion.timeout
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.http.auth.parseAuthorizationHeader
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.ASocket
import io.ktor.network.sockets.InetSocketAddress
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.network.sockets.tcpNoDelay
import io.ktor.network.tls.tls
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.cancel
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.close
import io.ktor.utils.io.core.String
import io.ktor.utils.io.core.toByteArray
import io.ktor.utils.io.discard
import io.ktor.utils.io.writeFully
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withTimeout

/**
 * http client interface for preview only
 */
internal interface PreviewClient {
    companion object {
        /** Timeout of HTTP call */
        var timeout = ThetaRepository.Timeout()
    }

    /**
     * request [method] [path] with [contentType] of [body] to [endpoint]
     */
    suspend fun request(
        endpoint: String,
        method: String = "POST",
        path: String = "/osc/commands/execute",
        body: String = "{\"name\":\"camera.getLivePreview\",\"parameters\":{}}",
        contentType: String = "application/json",
    ): PreviewClient

    /**
     * check continue to next part or not
     */
    suspend fun hasNextPart(): Boolean

    /**
     * retrieve next part
     */
    suspend fun nextPart(): Pair<ByteArray, Int>

    /**
     * close connection
     */
    suspend fun close()

}

/** exception for processing preview stream */
internal class PreviewClientException(
    /** messages */
    msg: String,
    /** caused exception */
    cause: Throwable? = null,
) : Exception(msg, cause)

/**
 * http client implement for preview only
 */
internal class PreviewClientImpl : PreviewClient {
    /** parse url string */
    class URL(url: String) {
        /** protocol, only http or https */
        var protocol: String = "http"

        /** host string */
        var host: String = "localhost"

        /** port number */
        var port: Int = 80

        /** path and query expression */
        var path: String = "/"

        init {
            val match = Regex("(http|https)://([^:/]+)(:[0-9]+)?(/.*$)?").find(url)
            match?.groups?.get(1)?.value?.let {
                protocol = it
                if (protocol == "https") {
                    port = 443
                }
            }
            match?.groups?.get(2)?.value?.let {
                host = it
            }
            match?.groups?.get(3)?.value?.let {
                port = it.substring(1).toInt()
            }
            match?.groups?.get(4)?.value?.let {
                path = it
            }
        }
    }

    /** fixed buffers */
    companion object {
        /** receive buffer */
        val buffer: ByteArray = ByteArray(64 * 1024)

        /** part buffer */
        var parts: ByteArray = ByteArray(100 * 1024)

        /** line buffer for headers */
        val lineBuffer: ByteArray = ByteArray(1024)
    }

    /** logger */
    internal val logger = ThetaApiLogger()

    /** input channel */
    private var input: ByteReadChannel? = null

    /** output channel */
    private var output: ByteWriteChannel? = null

    /** socket connected to endpoint */
    private var socket: ASocket? = null

    /** io selector manager */
    private var selector: SelectorManager? = null

    /** current buffer filled position */
    private var curr: Int = 0

    /** current read position in buffer */
    private var pos: Int = 0

    /** chunked transfer encoding or not */
    private var chunked: Boolean = false

    /** current chunk size in chunked transfer encoding */
    private var chunkSize: Int = -1

    /** content-length header value if exists */
    private var contentLength: Int? = null

    /** boundary marker for multipart */
    private var boundary: String? = null

    /** content-length of current part */
    private var partLength: Int = 0

    /** http response status */
    private var status: Int = 0

    /** http response status message */
    private var statusMessage: String? = null
    private var responseHeaders: Map<String, String>? = null

    /**
     * reset all variables
     */
    private fun reset() {
        socket = null
        selector = null
        input = null
        output = null
        pos = 0
        curr = 0
        chunked = false
        chunkSize = -1
        contentLength = null
        boundary = null
        partLength = 0
        status = 0
        statusMessage = null
    }

    /**
     * connect to [endpoint]
     */
    private suspend fun connect(url: URL): PreviewClientImpl {
        selector = SelectorManager(Dispatchers.Default)
        val builder = aSocket(selector!!).tcpNoDelay().tcp()
        val self = this
        val context = currentCoroutineContext()
        withTimeout(timeout.connectTimeout) {
            val socket = builder.connect(
                InetSocketAddress(url.host, url.port),
            ) {
                socketTimeout = timeout.socketTimeout
                // Do not set receiveBufferSize since the performance is better.
            }.let {
                when (url.protocol) {
                    "https" -> it.tls(context) {
                        serverName = url.host
                    }

                    else -> it
                }
            }
            input = socket.openReadChannel()
            output = socket.openWriteChannel(autoFlush = true)
            self.socket = socket
        }
        return this
    }

    /**
     * close connection
     */
    override suspend fun close() {
        logger.log("PreviewClient: close")
        closeImple()
    }

    suspend fun closeImple() {
        try {
            input?.cancel()
        } catch (_: Throwable) {
        }
        try {
            input?.discard()
        } catch (_: Throwable) {
        }
        try {
            output?.close()
        } catch (_: Throwable) {
        }
        try {
            selector?.close()
        } catch (_: Throwable) {
        }
        try {
            socket?.close()
        } catch (_: Throwable) {
        }
        reset()
    }

    /**
     * write [bytes] to endpoint
     */
    private suspend fun write(bytes: ByteArray) {
        output?.writeFully(bytes)
    }

    /**
     * write [line] to endpoint
     */
    private suspend fun write(line: String) {
        write(line.toByteArray())
    }

    /**
     * fill buffer from endpoint
     */
    private suspend fun fillBuffer(): Int {
        pos = 0
        try {
            return withTimeout(timeout.socketTimeout) {
                curr = input!!.readAvailable(buffer, 0, buffer.size)
                curr
            }
        } catch (t: Throwable) {
            throw PreviewClientException(t.message ?: "readAvailable error", t)
        }
    }

    /**
     * read a byte from buffer
     */
    private suspend fun readFromBuffer(): Byte? {
        if (pos >= curr) {
            if (fillBuffer() <= 0) {
                return null
            }
        }
        return buffer[pos++]
    }

    /**
     * read a byte with chunk processing
     */
    private suspend fun readByte(): Byte? {
        if (chunked) {
            if (chunkSize <= 0) {
                if (chunkSize == 0) {
                    // skip chunk trailers CR+LF RFC7320
                    for (i in 0..1) {
                        readFromBuffer()
                    }
                }
                chunkSize = 0
                while (true) {
                    val ch = readFromBuffer() ?: break
                    if (ch >= '0'.code.toByte() && ch <= '9'.code.toByte()) {
                        chunkSize *= 16
                        chunkSize += ch.toUInt().toInt() - '0'.code.toUInt().toInt()
                    } else if (ch >= 'a'.code.toByte() && ch <= 'f'.code.toByte()) {
                        chunkSize *= 16
                        chunkSize += ch.toUInt().toInt() - 'a'.code.toUInt().toInt() + 10
                    } else if (ch >= 'A'.code.toByte() && ch <= 'F'.code.toByte()) {
                        chunkSize *= 16
                        chunkSize += ch.toUInt().toInt() - 'A'.code.toUInt().toInt() + 10
                    } else if (ch == '\n'.code.toByte()) {
                        break
                    } else if (ch == '\r'.code.toByte()) {
                        // skip CR
                    } else {
                        throw PreviewClientException("invalid char in chunk size: $ch")
                    }
                }
            }
            if (chunkSize <= 0) {
                return null
            }
            chunkSize--
        } else if (contentLength != null) {
            val length = contentLength!!
            if (length <= 0) {
                return null
            }
            contentLength = length - 1
        }
        return readFromBuffer()
    }

    /**
     * read until CR+LF and return String as UTF-8
     */
    private suspend fun readUtf8Line(): String? {
        val buf = lineBuffer
        var bp = 0
        while (bp < buf.size) {
            val ch = readByte()
            if (ch == null) {
                if (bp == 0) {
                    return null
                }
                break
            } else if (ch == '\n'.code.toByte()) {
                break
            } else if (ch == '\r'.code.toByte()) {
                // skip CR
            } else {
                buf[bp++] = ch
            }
        }
        if (bp > 0) {
            try {
                return String(buf, 0, bp, Charsets.UTF_8)
            } catch (t: Throwable) {
                for (i in 0 until bp) {
                    println(buf[i])
                }
                throw t
            }
        }
        return ""
    }

    /**
     * parse response status
     */
    private suspend fun responseStatus(): PreviewClientImpl {
        val line: String = readUtf8Line() ?: throw PreviewClientException("no response status")
        logger.log("PreviewClient: responseStatus: $line")
        status = 0
        statusMessage = null
        val match = Regex("HTTP/[0-9.]+ ([0-9]+) (.*)").find(line)
        match?.groups?.get(1)?.value?.let {
            status = it.toInt()
        }
        match?.groups?.get(2)?.value?.let {
            statusMessage = it
        }
        if ((status / 100) != 2 && status != HttpStatusCode.Unauthorized.value) {
            throw PreviewClientException(statusMessage ?: "unknown error")
        }
        return this
    }

    /**
     * parse response header only used
     */
    private suspend fun responseHeaders(): PreviewClientImpl {
        var headerChunked = false
        var headerContentLength: Int? = null
        val headers = mutableMapOf<String, String>()
        while (true) {
            val line = readUtf8Line() ?: break
            if (line.isEmpty()) {
                break
            }
            if (line[0] == ' ' || line[0] == '\t') { // RFC7320 obs-fold 3.2.4
                throw PreviewClientException("obsoleted header: [$line]")
            }
            val match = Regex("([^:]+):(.*)").find(line)
            var matchName: String? = null
            var matchValue: String? = null
            match?.groups?.get(1)?.value?.let {
                matchName = it.trim().lowercase()
            }
            match?.groups?.get(2)?.value?.let {
                matchValue = it.trim()
            }
            val name = matchName ?: throw PreviewClientException("malformed header: $line")
            val value = matchValue ?: throw PreviewClientException("malformed header: $line")

            if (name == "transfer-encoding") {
                if (value.lowercase() == "chunked") {
                    headerChunked = true
                }
            } else if (name == "content-length") {
                headerContentLength = value.toInt()
            } else if (name == "content-type") {
                // RFC1341 7.2
                val match0 = Regex("boundary=\"?([0-9a-zA-Z'()+_,-./:=? ]+)\"?")
                    .find(value)
                boundary = match0?.groups?.get(1)?.value
            }
            headers[name] = value
        }
        responseHeaders = headers
        contentLength = headerContentLength
        chunked = headerChunked
        return this
    }

    /**
     * request [method] [path] with [contentType] of [body] to [endpoint]
     */
    override suspend fun request(
        endpoint: String,
        method: String,
        path: String,
        body: String,
        contentType: String,
    ): PreviewClient {
        val client = requestPreview(endpoint, method, path, body, contentType) as PreviewClientImpl
        return when (client.status) {
            HttpStatusCode.Unauthorized.value -> {
                val url = URL(endpoint)
                ApiClient.digestAuth?.let { digestAuth ->
                    responseHeaders?.get(HttpHeaders.WWWAuthenticate.lowercase())?.let { header ->
                        val authHeader = parseAuthorizationHeader(header) as HttpAuthHeader.Parameterized
                        digestAuth.updateAuthHeaderInfo(authHeader)
                        requestPreview(endpoint, method, path, body, contentType, digestAuth.makeDigestHeader(url.path, HttpMethod.Post.value))
                    }
                } ?: client
            }

            else -> client
        }
    }

    internal suspend fun requestPreview(
        endpoint: String,
        method: String,
        path: String,
        body: String,
        contentType: String,
        digest: String? = null,
    ): PreviewClient {
        logger.log("PreviewClient:\nendpoint: $endpoint\nmethod: $method\npath: $path\nbody: $body\ncontentType: $contentType\ndigest: $digest")
        closeImple()  // To prevent resource leaks
        val url = URL(endpoint)
        connect(url)
        write("$method $path HTTP/1.1\r\n")
        write("Host: ${url.host}\r\n")
        write("Connection: close\r\n")
        val bodies = body.toByteArray()
        if (bodies.isNotEmpty()) {
            write("Content-Type: $contentType\r\n")
            write("Content-Length: ${bodies.size}\r\n")
            digest?.run { write("Authorization: $digest\r\n") }
            write("\r\n")
            write(bodies)
        } else {
            write("\r\n")
        }
        return responseStatus().responseHeaders()
    }

    /**
     * got all response with content-length (not used)
     */
    suspend fun response(): Pair<ByteArray, Int>? {
        if (contentLength == null || contentLength!! <= 0) {
            return null
        }
        if (parts.size < contentLength!!) {
            parts = ByteArray(kotlin.math.max(parts.size, contentLength!!))
        }
        for (i in 0 until contentLength!!) {
            readByte()?.let {
                parts[i] = it
            }
        }
        return Pair(parts, contentLength!!)
    }

    /**
     * check continue to next part or not
     */
    override suspend fun hasNextPart(): Boolean {
        if (boundary == null) {
            return false
        }
        while (true) {
            val line = readUtf8Line() ?: return false
            if (boundary.equals(line)) {
                break
            }
            // RFC1341 says to search with '--'+boundary[+'--']
            if (("--$boundary") == line) {
                break
            }
            if (("--$boundary--") == line) {
                // Last part mark
                break
            }
        }
        partLength = 0
        while (true) {
            val line = readUtf8Line() ?: break
            if (line.trim().isEmpty()) {
                break
            }
            val match = Regex("Content-Length: *(\\d+)", RegexOption.IGNORE_CASE).find(line)
            match?.groups?.get(1)?.value?.let {
                partLength = it.toInt()
            }
        }
        if (partLength == 0) {
            return false
        }
        return true
    }

    /**
     * retrieve next part
     */
    override suspend fun nextPart(): Pair<ByteArray, Int> {
        if (parts.size < partLength) {
            val size = kotlin.math.max(parts.size, ((partLength + 4095) / 4096) * 4096)
            parts = ByteArray(size)
        }
        for (i in 0 until partLength) {
            readByte()?.let {
                parts[i] = it
            }
        }
        return Pair(parts, partLength)
    }
}
