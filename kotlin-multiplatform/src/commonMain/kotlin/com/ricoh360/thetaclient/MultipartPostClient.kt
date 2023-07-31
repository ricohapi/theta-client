package com.ricoh360.thetaclient

import com.ricoh360.thetaclient.MultipartPostClient.Companion.BOUNDARY
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.http.auth.parseAuthorizationHeader
import io.ktor.http.isSuccess
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import io.ktor.utils.io.charsets.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.io.*
import kotlinx.io.files.*

/**
 * Base class of custom HTTP client
 +
 * @param connectionTimeout connection timeout of [Ktor](https://ktor.io/)
 * @param socketTimeout socket timeout of [Ktor](https://ktor.io/)
 */
open class BaseHttpClient(connectionTimeout: Long = 20_000L, socketTimeout: Long = 20_000L) : Closeable {

    class URL(url: String) {
        /** protocol, only http or https */
        private var protocol: String = "http"

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

    private val connectionTimeout = connectionTimeout
    private val socketTimeout = socketTimeout

    /** parsed endpoint */
    private var endpoint: URL? = null

    /**
     * [ByteReadCHanel](https://api.ktor.io/ktor-io/io.ktor.utils.io/-byte-read-channel/index.html)
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
        val buffer: ByteArray = ByteArray(10 * 1024)

        /** line buffer for response headers */
        val lineBuffer: ByteArray = ByteArray(1024)
    }

    /**
     * connect to the host
     */
    protected suspend fun connect(endpoint: String) {
        reset()
        this.endpoint = URL(endpoint)
        this.selector = SelectorManager(Dispatchers.Default)
        val builder = aSocket(selector!!).tcpNoDelay().tcp()
        withContext(Dispatchers.Default) {
            withTimeout(connectionTimeout) {
                println("wait socket connection")
                val socket = builder.connect(
                    InetSocketAddress(this@BaseHttpClient.endpoint!!.host, this@BaseHttpClient.endpoint!!.port),
                ) {
                    socketTimeout = this@BaseHttpClient.socketTimeout
                    receiveBufferSize = buffer.size
                }
                this@BaseHttpClient.input = socket.openReadChannel()
                this@BaseHttpClient.output = socket.openWriteChannel(autoFlush = true)
                this@BaseHttpClient.socket = socket
            }
        }
    }

    /**
     * close connection
     */
    override fun close() {
        runCatching {
            input?.cancel()
        }
        runCatching {
            output?.close()
        }
        runCatching {
            socket?.close()
        }
        runCatching {
            selector?.close()
        }
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
     * write [bytes] to endpoint
     */
    protected suspend fun write(bytes: ByteArray) {
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
        write("${method.value} $path HTTP/1.1\r\n")
        println("${method.value} $path HTTP/1.1\n")
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
            println("$name: $value\r\n")
        }
        write("\r\n")
        println("\r\n")
    }

    /**
     * fill read buffer from endpoint
     */
    private suspend fun fillBuffer(): Int {
        pos = 0
        curr = input!!.readAvailable(buffer, 0, buffer.size)
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
    protected suspend fun readStatusLine() {
        val line: String = readUtf8Line() ?: throw PreviewClientException("no response status")
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
    }

    /**
     * Read response headers.
     * Call this function after calling [readStatusLine]
     */
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
        if(contentLength != null) {
            val buf = ByteArray(contentLength!!)
            for (i in buf.indices) {
                val byte = readByte()
                byte?: {
                    throw(BaseHttpClientException("response body is too short"))
                }
                buf.set(i, byte!!)
            }
            this.responseBody = buf
        } else {
            var buf = ByteArray(0)
            do {
                val byte = readByte()
                byte?: break
                buf += byte
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
     * @param endpoint endpount of Web API, eg. "http://192.198.1.1:80/"
     * @param path request target of HTTP
     * @param filePaths Content of each part
     * @param boundary boundary parameter of multipart/form-data
     * @return body of the response
     */
    suspend fun request(
        endpoint: String,
        path: String,
        filePaths: List<String>,
        boundary: String = BOUNDARY,
    ): ByteArray
}

/**
 * http client implementation just for update firmware
 */
class MultipartPostClientImpl(connectionTimeout: Long = 30_000L, socketTimeout: Long = 180_000L) : MultipartPostClient, BaseHttpClient(connectionTimeout, socketTimeout) {

    companion object {
        // read buffer size for reading firmware file
        const val READ_BUFFER_SIZE = 4096
        // boundary, can be any uuid, which is constraint of Theta SC2
        //const val BOUNDARY = "ab0c85f4-6d89-4a7f-a0a5-115d7f43b5f1"
        // refular expression to get a file name from a file path
        val regexFileName = Regex("""[^/\\]+$""")

        /**
         * generate requet headers
         *
         * @param contentLength size of the entity-body
         * @param authorization credentials containing the authentication information of the user agent if needed
         * @param boundary boundary parameter of Content-Type header
         * @return list of request headers
         */
        private fun genRequestHeaders(contentLength: Long, authorization: String?, boundary: String = BOUNDARY): List<Pair<String, String>> {
            val headers = mutableListOf(
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

        private fun genBoundaryDelimiter(boundary: String): String {
            return "--$boundary\r\n"
        }

        /**
         * generate part headers for a file
         *
         * @param fileName file name for the part
         * @param boundary boundary parameter of Content-Type header
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
         * get length pf a part
         *
         * @param fileName corresponding to the part
         * @param filePath file path corresponding to the part
         * @return
         */
        private suspend fun getPartLength(fileName: String, filePath: String, boundary: String): Long {
            var size = 0L
            size += genBoundaryDelimiter(boundary).length
            val headers = genPartHeaders(fileName)
            headers.forEach {
                size += it.first.length + it.second.length + ": ".length + "\r\n".length
            }
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
     * Send a HTTP request.
     *
     * @param endpoint endpount of Web API, eg. "http://192.198.1.1:80/"
     * @param path request target of HTTP
     * @param filePaths Content of each part
     * @param boundary boundary parameter of multipart/form-data
     * @return body of the response
     */
    override suspend fun request(
        endpoint: String,
        path: String,
        filePaths: List<String>,
        boundary: String,
    ): ByteArray {
        requestOnce(endpoint, path, filePaths, boundary)
        if(this.status == HttpStatusCode.Unauthorized.value) { // need digest authentication
            ApiClient.digestAuth?.let { digestAuth ->
                responseHeaders?.get(HttpHeaders.WWWAuthenticate.lowercase())?.let { header ->
                    val authHeader = parseAuthorizationHeader(header) as HttpAuthHeader.Parameterized
                    digestAuth.updateAuthHeaderInfo(authHeader)
                    requestOnce(endpoint, path, filePaths, boundary, digestAuth.makeDigestHeader(path, HttpMethod.Post.value))
                }
            } ?: {
                throw(BaseHttpClientException("Failed daigest authentication"))
            }
        }
        readBody()
        if(HttpStatusCode(this.status, this.statusMessage?: "").isSuccess()) {
            return this.responseBody ?: byteArrayOf()
        } else {
            throw(BaseHttpClientException("${this.status} ${this.statusMessage} ${this.responseBody}"))
        }
    }

    /**
     * Send a HTTP request and read the response.
     *
     * @param host
     * @param path
     * @param filePaths
     * @param boundary
     * @param port
     * @param digest value of Authorization header if needed
     */
    internal suspend fun requestOnce(
        endpont: String,
        path: String,
        filePaths: List<String>,
        boundary: String,
        digest: String? = null,
    ) {
        connect(endpont)
        writeRequestLine(path, HttpMethod.Post)
        writeHeaders(genRequestHeaders(getContentLength(filePaths, boundary), digest))
        val buffer = ByteArray(READ_BUFFER_SIZE)
        filePaths.forEach {
            var src: Source? = null
            try {
                src = Path(it).source()
                writePartHeaders(boundary, genPartHeaders(getFileName(it)))
                // write part body
                var count = 0
                do {
                    write(buffer, count)
                    println("count: $count")
                    var s = ""
                    for(i in 0..count-1) s += buffer.elementAt(i).toInt().toChar()
                    println(s)
                    count = src.readAtMostTo(buffer, 0, READ_BUFFER_SIZE)
                } while(count != -1)
            } finally {
                src?.close()
            }
        }
        writeCloseDelimiter(boundary)
        readStatusLine()
        readHeaders()
        readBody()
    }

    private suspend fun getContentLength(filePaths: List<String>, boundary: String): Long {
        var size = 0L
        filePaths.forEach {
            size += getPartLength(getFileName(it), it, boundary)
        }
        size += genCloseDelimiter().length
        return size
    }

    /**
     * Write all headers for a part of multipart data when cpntent type is multipart.
     * Aftre calling this function, you can not write a part header.
     *
     * @param boundary "Boundary" Parameter of multipart content type
     * @param headers headers for a part
     */
    protected suspend fun writePartHeaders(boundary: String, headers: List<Pair<String, String>>) {
        write(genBoundaryDelimiter(boundary))
        println(genBoundaryDelimiter(boundary))
        for ((name, value) in headers) {
            write("$name: $value\r\n")
            println("$name: $value\r\n")
        }
        write("\r\n")
        println("\r\n")
    }

    /**
     * Write a close delimiter of multipart data when cpntent type is multipart.
     * If you write data after calling this function, it is ignored on HTTP server
     *
     * @param boundary "Boundary" Parameter of multipart content type
     */
    private suspend fun writeCloseDelimiter(boundary: String) {
        write(genCloseDelimiter(boundary))
        println(genCloseDelimiter(boundary))
    }

}
