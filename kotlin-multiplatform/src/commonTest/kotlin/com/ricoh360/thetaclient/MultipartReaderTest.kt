package com.ricoh360.thetaclient

import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlinx.io.readByteArray
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MultipartReaderTest {
    private val headers = headersOf(HttpHeaders.ContentType, "multipart/x-mixed-replace; boundary=\"---osclivepreview---\"")

    private fun reader(body: String) = MultipartReader(headers, ByteReadChannel(body.encodeToByteArray()))

    @Test
    fun nextPartTest() = runTest {
        val reader = reader(
            "garbage\r\n" +
                "---osclivepreview---\r\nContent-Type: image/jpeg\r\nContent-Length: 3\r\n\r\nabc\r\n" +
                "---osclivepreview---\r\nContent-Length: 2\r\n\r\nde\r\n",
        )
        assertEquals("abc", reader.nextPart()?.readByteArray()?.decodeToString())
        assertEquals("de", reader.nextPart()?.readByteArray()?.decodeToString())
        // End of the body
        assertNull(reader.nextPart())
    }

    @Test
    fun nextPartErrorTest() = runTest {
        // No boundary
        assertNull(MultipartReader(headersOf(), ByteReadChannel("abc")).nextPart())
        // No Content-Length header
        assertNull(reader("---osclivepreview---\r\nContent-Type: image/jpeg\r\n\r\nabc\r\n").nextPart())
        // Ends in the headers
        assertNull(reader("---osclivepreview---\r\nContent-Length: 3\r\n").nextPart())
        // Ends in the body
        assertNull(reader("---osclivepreview---\r\nContent-Length: 10\r\n\r\nabc").nextPart())
    }
}
