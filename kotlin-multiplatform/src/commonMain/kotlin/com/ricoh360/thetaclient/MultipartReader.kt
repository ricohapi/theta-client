/*
 * multipart response parser.
 */
package com.ricoh360.thetaclient

import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.ByteReadPacket

/**
 * Reader for HTTP multipart response body Theta sends.
 * Note that Theta sends content type boundary doesn't conform to RFC 2046 chapter 5.1
 * @Param headers See [io.ktor.http.Headers](https://api.ktor.io/ktor-http/io.ktor.http/-http-headers/index.html)
 * @Param readChannel See [io.ktor.utils.io.ByteReadChannel](https://api.ktor.io/ktor-io/io.ktor.utils.io/-byte-read-channel/index.html)
 */
internal class MultipartReader(headers: Headers, val readChannel: ByteReadChannel) {
    var boundary: String? // boundary parameter of Content-Type header
    var contentLengthRegex: Regex
    val BUFFER_SIZE = 128
    val lineBuffer = StringBuilder(BUFFER_SIZE)

    init {
        val contentType = headers.get(HttpHeaders.ContentType) ?: ""
        val match = Regex("\"(.*)\"").find(contentType) // retrieve boundary parameter
        boundary = match?.groups?.get(1)?.value
        if (boundary == null) {
            println("No Content-Type header or no boundary parameter")
        }
        // don't add leading two hyphens to work around Theta's boundary bug
        contentLengthRegex = Regex("${HttpHeaders.ContentLength}: (\\d*)")
    }

    /**
     * get next part
     * if next part doesn't found, returns null
     */
    suspend fun nextPart(): ByteReadPacket? {
        if (boundary == null) return null
        var line: String?

        // skip to the next boundary
        do {
            line = readTextLine(lineBuffer, BUFFER_SIZE)
            if (line == null) return null
        } while (line != boundary)

        // read part headers
        var contentLength = 0
        do {
            line = readTextLine(lineBuffer, BUFFER_SIZE)
            if (line == null) return null
            val match = contentLengthRegex.find(line)
            match?.groups?.get(1)?.value?.let {
                contentLength = it.toInt()
            }
        } while (line!!.length > 0)
        if (contentLength == 0) {
            println("No Content-Length header")
            return null
        }

        // read the part body
        kotlin.runCatching {
            readChannel.readPacket(contentLength)
        }.onSuccess {
            return it
        }.onFailure {
            println(it.toString())
        }
        return null
    }

    /**
     * Read a text line from [readChannel].
     *
     * @param buffer At first cleared
     * @param size Size of [buffer]
     * @return Read string.  On error, null is returned.
     */
    private suspend inline fun readTextLine(buffer: StringBuilder, size: Int): String? {
        buffer.clear()
        kotlin.runCatching {
            if (!readChannel.readUTF8LineTo(buffer, size)) {
                println("Can't read a text line")
                return null
            }
        }.onFailure {
            println(it.toString())
            return null
        }
        return buffer.toString()
    }

}
