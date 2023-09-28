package com.ricoh360.thetaclient

import com.ricoh360.thetaclient.MultipartPostClient.Companion.BOUNDARY
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.http.auth.parseAuthorizationHeader
import io.ktor.http.isSuccess
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.ASocket
import io.ktor.network.sockets.InetSocketAddress
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.network.sockets.tcpNoDelay
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.cancel
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.close
import io.ktor.utils.io.core.String
import io.ktor.utils.io.core.toByteArray
import io.ktor.utils.io.writeFully
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.io.Source
import kotlinx.io.files.Path
import kotlinx.io.files.source

/**
 * Base class of custom HTTP client
 */
open class BaseHttpClient {

    @Suppress("MagicNumber")
    class URL(url: String) {
        companion object {
            /** default port number */
            const val DEFAULT_PORT = 80
        }

        /** protocol, only http or https */
        private var protocol: String = "http"

        /** host string */
        var host: String = "localhost"

        /** port number */
        var port: Int = DEFAULT_PORT

        /** path and query expression */
        var path: String = "/"

        init {
            val match = Regex("(http|https)://([^:/]+)(:[0-9]+)?(/.*$)?").find(url)
            match?.groups?.get(1)?.value?.let {
                protocol = it
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

    /** parsed endpoint */
    private var endpoint: URL? = null

    /**
     * [ByteReadChanel](https://api.ktor.io/ktor-io/io.ktor.utils.io/-byte-read-channel/index.html)
     * for asynchronous reading of sequences of bytes.
     */
    private var input: ByteReadChannel? = null

    /**
     * [ByteWriteChannel](https://api.ktor.io/ktor-io/io.ktor.utils.io/-byte-write-channel/index.html)
     * for asynchronous writing of sequences of bytes
     */
    private var output: ByteWriteChannel? = null

    /**
     * [ASocket](https://api.ktor.io/ktor-network/io.ktor.network.sockets/-a-socket/index.html)
     * async socket connected to the endpoint
     */
    private var socket: ASocket? = null

    /** [SelectorManager](https://api.ktor.io/ktor-network/io.ktor.network.selector/-selector-manager/index.html) of ktor */
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

    /** http response status */
    protected var status: Int = 0

    /** http response status message */
    protected var statusMessage: String? = null

    /** http response headers */
    protected var responseHeaders: Map<String, String>? = null

    /** http response body */
    protected var responseBody: ByteArray? = null

    /** fixed buffers for receiving a response */
    companion object {
        /** receive buffer */
        val receiveBuffer: ByteArray = ByteArray(10 * 1024)

        /** line buffer for response headers */
        val lineBuffer: ByteArray = ByteArray(1024)

        /** Send buffer size same as file read buffer size */
        const val SEND_BUFFER_SIZE = 8192

        // decimal value of hex "10"
        const val HEX_10 = 16
        // decimal value of hex "A"
        const val HEX_A = 10
    }

    /**
     * connect to the host
     *
     * @param endpoint endpoint of the host
     * @param connectionTimeout timeout of connection (millisecond)
     * @param socketTimeout read/write timeout of socket (millisecond)
     */
    protected suspend fun connect(endpoint: String, connectionTimeout: Long, socketTimeout: Long) {
        reset()
        this.endpoint = URL(endpoint)
        this.selector = SelectorManager(Dispatchers.Default)
        val builder = aSocket(selector!!).tcpNoDelay().tcp()
        withContext(Dispatchers.Default) {
            withTimeout(connectionTimeout) {
                val socket = builder.connect(
                    InetSocketAddress(this@BaseHttpClient.endpoint!!.host, this@BaseHttpClient.endpoint!!.port),
                ) {
                    this.keepAlive = true
                    this.receiveBufferSize = receiveBuffer.size
                    this.sendBufferSize = SEND_BUFFER_SIZE
                    this.socketTimeout = socketTimeout
                }
                this@BaseHttpClient.input = socket.openReadChannel()
                this@BaseHttpClient.output = socket.openWriteChannel(autoFlush = true)
                this@BaseHttpClient.socket = socket
            }
        }
    }

    /**
     * Is connected to the host
     *
     * @return whether connected to the host or not
     */
    protected fun isConnected(): Boolean {
        return this.input != null && this.output != null && this.socket != null
    }

    /**
     * close connection
     */
     fun close() {
        runCatching {
            input?.cancel()
        }
        runCatching {
            output?.close()
        }
        runCatching {
            socket?.close()
        }
        /* On iOS, close() may be suspended
        runCatching {
            selector?.close()
        }
        */
        reset()
    }

    /**
     * reset all variables
     */
    private fun reset() {
        input = null
        output = null
        socket = null
        selector = null
    }

    /**
     * Clear all response data
     */
    protected fun clearResponse() {
        status = 0
        statusMessage = null
        responseHeaders = null
        responseBody = null
    }

    /**
     * write [bytes] to endpoint
     */
    private suspend fun write(bytes: ByteArray) {
        output?.writeFully(bytes)
    }

    /**
     * write [bytes] to endpoint
     */
    protected suspend fun write(bytes: ByteArray, byteCount: Int) {
        output?.writeFully(bytes, 0, byteCount)
    }

    /**
     * write [line] to endpoint
     */
    protected suspend fun write(line: String) {
        write(line.toByteArray())
    }

    /**
     * write a request line of http.
     * http version is fixed to 1.1.
     *
     * @param path HTTP request target
     * @param method HTTP method
     */
    protected suspend fun writeRequestLine(path: String, method: HttpMethod) {
        var slash = ""
        if(path.first() != '/') slash = "/"
        write("${method.value} $slash$path HTTP/1.1\r\n")
    }

    /**
     * Write all request headers of http.
     * After calling this function, you can not write a request header.
     *
     * @param headers list of http headers.
     */
    protected suspend fun writeHeaders(headers: List<Pair<String, String>>) {
        for ((name, value) in headers) {
            write("$name: $value\r\n")
        }
        write("\r\n")
    }

    /**
     * fill read buffer from endpoint
     */
    private suspend fun fillBuffer(): Int {
        pos = 0
        curr = input!!.readAvailable(receiveBuffer, 0, receiveBuffer.size)
        return curr
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
        return receiveBuffer[pos++]
    }

    /**
     * read a byte with chunk processing
     */
    @Suppress("CognitiveComplexMethod", "CyclomaticComplexMethod", "NestedBlockDepth", "LoopWithTooManyJumpStatements")
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
                        chunkSize *= HEX_10
                        chunkSize += ch.toUInt().toInt() - '0'.code.toUInt().toInt()
                    } else if (ch >= 'a'.code.toByte() && ch <= 'f'.code.toByte()) {
                        chunkSize *= HEX_10
                        chunkSize += ch.toUInt().toInt() - 'a'.code.toUInt().toInt() + HEX_A
                    } else if (ch >= 'A'.code.toByte() && ch <= 'F'.code.toByte()) {
                        chunkSize *= HEX_10
                        chunkSize += ch.toUInt().toInt() - 'A'.code.toUInt().toInt() + HEX_A
                    } else if (ch == '\n'.code.toByte()) {
                        break
                    } else if (ch == '\r'.code.toByte()) {
                        // skip CR
                    } else {
                        throw BaseHttpClientException("invalid char in chunk size: $ch")
                    }
                }
            }
            if (chunkSize <= 0) {
                return null
            }
            chunkSize--
        } else if (contentLength != null) {
            val length = contentLength ?: 0
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
    @Suppress("LoopWithTooManyJumpStatements")
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
    protected suspend fun readStatusLine() {
        val line: String = readUtf8Line() ?: throw BaseHttpClientException("no response status line")
        status = 0
        statusMessage = null
        val match = Regex("HTTP/[0-9.]+ ([0-9]+) (.*)").find(line)
        match?.groups?.get(1)?.value?.let {
            status = it.toInt()
        }
        match?.groups?.get(2)?.value?.let {
            statusMessage = it
        }
    }

    /**
     * Read response headers.
     * Call this function after calling [readStatusLine]
     */
    @Suppress("CyclomaticComplexMethod", "LoopWithTooManyJumpStatements", "ThrowsCount")
    protected suspend fun readHeaders() {
        var headerChunked = false
        var headerContentLength: Int? = null
        val headers = mutableMapOf<String, String>()
        while (true) {
            val line = readUtf8Line() ?: break
            if (line.isEmpty()) {
                break
            }
            if (line[0] == ' ' || line[0] == '\t') { // RFC7320 obs-fold 3.2.4
                throw BaseHttpClientException("obsoleted header: [$line]")
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
            val name = matchName ?: throw BaseHttpClientException("malformed header: $line")
            val value = matchValue ?: throw BaseHttpClientException("malformed header: $line")

            if (name == "transfer-encoding") {
                if (value.lowercase() == "chunked") {
                    headerChunked = true
                }
            } else if (name == "content-length") {
                headerContentLength = value.toInt()
            }
            headers[name] = value
        }
        responseHeaders = headers
        contentLength = headerContentLength
        chunked = headerChunked
    }

    /**
     * Read whole body of the response.
     * Call this function after calling [readHeaders]
     *
     * @return The content of the response body
     */
    protected suspend fun readBody() {
        contentLength?.let {
            val buf = ByteArray(it)
            for (i in buf.indices) {
                val byte = readByte()
                byte?.let {
                    buf[i] = it
                } ?: let {
                    throw(BaseHttpClientException("response body is too short"))
                }
            }
            this.responseBody = buf
        } ?: run {
            var buf = ByteArray(0)
            do {
                val byte = readByte()
                byte?.let {
                    buf += it
                }
                byte?: break
            } while(true)
            this.responseBody = buf
        }
    }

}

/** exception for posting firmware files */
class BaseHttpClientException(
    /** messages */
    msg: String,
    /** caused exception */
    cause: Throwable? = null,
) : Exception(msg, cause)


/**
 * HTTP client interface for update firmware
 * This interface is used in unit tests.
 */
interface MultipartPostClient  {
    companion object {
        // boundary, can be any uuid, which is constraint of Theta SC2
        const val BOUNDARY = "ab0c85f4-6d89-4a7f-a0a5-115d7f43b5f1"
    }

    /**
     * Send a HTTP request.
     *
     * @param endpoint endpoint of Web API, eg. "http://192.198.1.1:80/"
     * @param path request target of HTTP
     * @param filePaths Content of each part
     * @param connectionTimeout timeout for connection (millisecond).  If null, default value is used.
     * @param socketTimeout timeout for socket (millisecond). If null, default value is used.
     * @Param callback function to pass the percentage of sent firmware
     * @param boundary boundary parameter of multipart/form-data.  If this is not specified, default value is used.
     * @return body of the response
     */
    suspend fun request(
        endpoint: String,
        path: String,
        filePaths: List<String>,
        connectionTimeout: Long,
        socketTimeout: Long,
        callback: ((Int) -> Unit)?,
        boundary: String = BOUNDARY,
    ): ByteArray
}

/**
 * http client implementation just for update firmware
 */
class MultipartPostClientImpl : MultipartPostClient, BaseHttpClient() {

    companion object {
        // read buffer size for reading firmware file
        const val READ_BUFFER_SIZE = 1024 * 8
        // boundary, can be any uuid, which is constraint of Theta SC2
        //const val BOUNDARY = "ab0c85f4-6d89-4a7f-a0a5-115d7f43b5f1"
        // regular expression to get a file name from a file path
        private val regexFileName = Regex("""[^/\\]+$""")
        // 100 percentage
        private const val PERCENTAGE_100 = 100

        /**
         * generate request headers
         *
         * @param endpoint URL of the endpoint
         * @param contentLength size of the entity-body
         * @param authorization credentials containing the authentication information of the user agent if needed
         * @param boundary boundary parameter of Content-Type header
         * @return list of request headers
         */
        private fun genRequestHeaders(endpoint: String, contentLength: Long, authorization: String?, boundary: String = BOUNDARY): List<Pair<String, String>> {
            val host = genHostHeaderValue(endpoint) ?: throw BaseHttpClientException("Can not get host header value")
            val headers = mutableListOf(
                Pair("Host", host),
                Pair("Accept", "*/*"),
                Pair("Content-Length", contentLength.toString()),
                Pair("Connection", "Keep-Alive"),
                Pair("Cache-Control", "no-cache"),
                Pair("Content-Type", "multipart/form-data; boundary=$boundary"),
            )
            authorization?.let {
                headers.add(Pair("Authorization", authorization))
            }
            return headers
        }

        /**
         * generate request headers without content related headers
         *
         * @param endpoint URL of the endpoint
         * @return list of request headers
         */
        private fun genRequestHeadersWithoutContent(endpoint: String): List<Pair<String, String>> {
            val host = genHostHeaderValue(endpoint) ?: throw BaseHttpClientException("Can not get host header value")
            return mutableListOf(
                Pair("Host", host),
                Pair("Accept", "*/*"),
                Pair("Connection", "Keep-Alive"),
                Pair("Cache-Control", "no-cache"),
            )
        }

        /**
         * Get the value of HTTP Host header
         *
         * @param endpoint URL of the endpoint
         * @return Host header value, or null if not found
         */
        private fun genHostHeaderValue(endpoint: String): String? {
            val regex = Regex("""//(.*?)([:/]|$)""")
            val match = regex.find(endpoint)
            match?.groups?.let {
                return it[1]?.value
            }
            return null
        }

        private fun genBoundaryDelimiter(boundary: String): String {
            return "--$boundary\r\n"
        }

        /**
         * generate part headers for a file
         *
         * @param fileName file name for the part
         * @return list of part headers
         */
        private fun genPartHeaders(fileName: String): List<Pair<String, String>> {
            return listOf(
                Pair("Content-Disposition", "form-data; name=\"firmware\"; filename=\"$fileName\""),
                Pair("Content-Type", "application/octet-stream"),
                Pair("Content-Transfer-Encoding", "binary"),
            )
        }

        /**
         * generate a close delimiter line
         *
         * @param boundary boundary parameter of Content-Type header
         * @return a close delimiter line
         */
        private fun genCloseDelimiter(boundary: String = BOUNDARY): String {
            return "\r\n--$boundary--\r\n"
        }

        /**
         * get a file name from a path
         *
         * @param path file path
         * @return file name
         */
        private fun getFileName(path: String): String
        {
            val fileName = regexFileName.find(path)?.value
            fileName ?: throw IllegalArgumentException("No file name: $path")
            return fileName
        }

        /**
         * get the length pf a part
         *
         * @param fileName corresponding to the part
         * @param filePath file path corresponding to the part
         * @param boundary boundary parameter of multipart/form-data
         * @return
         */
        private suspend fun getPartLength(fileName: String, filePath: String, boundary: String): Long {
            var size = 0L
            size += genBoundaryDelimiter(boundary).length
            val headers = genPartHeaders(fileName)
            headers.forEach {
                size += it.first.length + ": ".length + it.second.length + "\r\n".length
            }
            size += "\r\n".length
            return size + getFileSize(filePath)
        }

        /**
         * get file size
         *
         * @param filePath path of the target file
         * @return file size
         */
        private suspend fun getFileSize(filePath: String): Long = withContext(Dispatchers.Default){
            val buffer = ByteArray(READ_BUFFER_SIZE)
            var size = 0L
            var src: Source? = null
            try {
                src = Path(filePath).source()
                var count = 0
                while (count != -1) {
                    size += count
                    count = src.readAtMostTo(buffer, 0, READ_BUFFER_SIZE)
                }
            } finally {
                src?.close()
            }
            size
        }

    }

    /**
     * Send a HTTP request of multipart post and receive the response.
     *
     * @param endpoint endpoint of Web API, eg. "http://192.198.1.1:80/"
     * @param path request target of HTTP
     * @param filePaths Content of each part
     * @param connectionTimeout timeout for connection (millisecond).
     * @param socketTimeout timeout for socket (millisecond).
     * @Param callback function to pass the percentage of sent firmware
     * @param boundary boundary parameter of multipart/form-data
     * @return body of the response
     * @throws [BaseHttpClientException] when the host returns an error.
     */
    override suspend fun request(
        endpoint: String,
        path: String,
        filePaths: List<String>,
        connectionTimeout: Long,
        socketTimeout: Long,
        callback: ((Int) -> Unit)?,
        boundary: String,
    ): ByteArray {
        val authorizationHeader: String? = checkAuthenticationNeeded(endpoint, path, connectionTimeout, socketTimeout)
        requestWithAuth(endpoint, path, filePaths, connectionTimeout, socketTimeout, callback, boundary, authorizationHeader)
        close()
        val httpStatusCode = HttpStatusCode(this.status, this.statusMessage?: "")
        if(httpStatusCode.isSuccess() || httpStatusCode.value == 0) {
            return this.responseBody ?: byteArrayOf()
        } else if (httpStatusCode == HttpStatusCode.NotFound){
            throw(BaseHttpClientException("Request failed: ${this.status} ${this.statusMessage}: API path \"$path\" may be wrong"))
        } else {
            val headers = this.responseHeaders?.entries?.joinToString(prefix = "[", postfix = "]") ?: "[no header]"
            var body = "[empty body]"
            this.responseBody?.let {
                body = String(it)
            }
            throw(BaseHttpClientException("Request failed: ${this.status} ${this.statusMessage}\n$headers\n$body}"))
        }
    }

    /**
     * Send a HTTP request of multipart post and receive the response.
     * Authorization header value can be specified.
     *
     * @param endpoint endpoint of Web API, eg. "http://192.198.1.1:80/"
     * @param path request target of HTTP
     * @param filePaths Content of each part
     * @param connectionTimeout timeout for connection (millisecond).
     * @param socketTimeout timeout for socket (millisecond).
     * @Param callback function to pass the percentage of sent firmware
     * @param boundary boundary parameter of multipart/form-data
     * @param digest value of Authorization header if needed
     */
    @Suppress("NestedBlockDepth", "SwallowedException")
    private suspend fun requestWithAuth(
        endpoint: String,
        path: String,
        filePaths: List<String>,
        connectionTimeout: Long,
        socketTimeout: Long,
        callback: ((Int) -> Unit)?,
        boundary: String,
        digest: String? = null,
    ) {
        val contentLength = getContentLength(filePaths, boundary)
        if (!isConnected()) connect(endpoint, connectionTimeout, socketTimeout)
        writeRequestLine(path, HttpMethod.Post)
        writeHeaders(genRequestHeaders(endpoint, contentLength, digest))
        val buffer = ByteArray(READ_BUFFER_SIZE)
        var sentCount = 0L
        var lastPercent = 0
        filePaths.forEach {
            var src: Source? = null
            try {
                src = Path(it).source()
                writePartHeaders(boundary, genPartHeaders(getFileName(it)))
                // write part body
                var count = 0
                do {
                    write(buffer, count)
                    sentCount += count
                    callback?.let {
                        val percent = (sentCount * PERCENTAGE_100 / contentLength).toInt()
                        if (percent > lastPercent) {
                            it(percent)
                            lastPercent = percent
                        }
                    }
                    count = src.readAtMostTo(buffer, 0, READ_BUFFER_SIZE)
                } while(count != -1)
            } finally {
                src?.close()
            }
        }
        writeCloseDelimiter(boundary)
        callback?.let {
            it(PERCENTAGE_100)
        }
        try {
            readStatusLine()
            readHeaders()
            readBody()
        } catch(e: Throwable) {
            // Theta X does not send a status line but firmware update is executed.
        } finally {
            close()
        }
    }

    /**
     * Check the connected Theta is in client mode or not.
     * In client mode, digest authentication is needed.
     *
     * @param endpoint endpoint of Web API, eg. "http://192.198.1.1:80/"
     * @param path request target of HTTP
     * @param connectionTimeout timeout for connection (millisecond).
     * @param socketTimeout timeout for socket (millisecond).
     * @return Authorization header in client mode, null not in client mode.
     */
    private suspend fun checkAuthenticationNeeded(endpoint: String, path: String, connectionTimeout: Long, socketTimeout: Long): String? {
        ApiClient.digestAuth ?: return null
        if (!isConnected()) connect(endpoint, connectionTimeout, socketTimeout)
        writeRequestLine(path, HttpMethod.Post)
        writeHeaders(genRequestHeadersWithoutContent(endpoint))
        runCatching {
            readStatusLine()
            readHeaders()
            readBody()
        }.onFailure {
            clearResponse()
            close()
            throw BaseHttpClientException(it.toString())
        }
        val authHeaderVal: String? = when (this.status) {
            HttpStatusCode.Unauthorized.value -> genAuthorizationHeaderValue(path, this.responseHeaders)
            else -> null
        }
        clearResponse()
        close()
        return authHeaderVal
    }

    /**
     * Generate authorization header value for digest authorization
     *
     * @param path request target of HTTP
     * @param responseHeaders Response headers for a request without authorization header
     * @return Digest authorization header
     */
    private fun genAuthorizationHeaderValue(path: String, responseHeaders: Map<String, String>?): String {
        ApiClient.digestAuth?.let { digestAuth ->
            responseHeaders?.get(HttpHeaders.WWWAuthenticate.lowercase())?.let { header ->
                val authHeader = parseAuthorizationHeader(header) as HttpAuthHeader.Parameterized
                digestAuth.updateAuthHeaderInfo(authHeader)
                return digestAuth.makeDigestHeader(path, HttpMethod.Post.value)
            }
            throw(BaseHttpClientException("No WWW-Authenticate header in the 401 response"))
        }
        throw(BaseHttpClientException("No authentication information is set"))
    }


    /**
     * Get the length of the HTTP contents
     *
     * @param filePaths list of file paths to send their contents as multipart
     * @param boundary "Boundary" Parameter of multipart content type
     * @return length of the contents
     */
    private suspend fun getContentLength(filePaths: List<String>, boundary: String): Long {
        var size = 0L
        filePaths.forEach {
            size += getPartLength(getFileName(it), it, boundary)
        }
        size += genCloseDelimiter().length
        return size
    }

    /**
     * Write all headers for a part of multipart data when content type is multipart.
     * After calling this function, you can not write a part header.
     *
     * @param boundary "Boundary" Parameter of multipart content type
     * @param headers headers for a part
     */
    private suspend fun writePartHeaders(boundary: String, headers: List<Pair<String, String>>) {
        write(genBoundaryDelimiter(boundary))
        for ((name, value) in headers) {
            write("$name: $value\r\n")
        }
        write("\r\n")
    }

    /**
     * Write a close delimiter of multipart data when content type is multipart.
     * If you write data after calling this function, it is ignored on HTTP server
     *
     * @param boundary "Boundary" Parameter of multipart content type
     */
    private suspend fun writeCloseDelimiter(boundary: String) {
        write(genCloseDelimiter(boundary))
    }

}
