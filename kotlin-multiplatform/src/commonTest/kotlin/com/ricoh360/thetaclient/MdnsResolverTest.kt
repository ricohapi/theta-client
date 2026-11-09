package com.ricoh360.thetaclient

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MdnsResolverTest {
    private val host = "THETAYR12345678.local"

    /**
     * Response repeating the question, with the A record whose name is a compression pointer to the question.
     */
    private fun response(ipAddress: List<Int>, flags: Int = 0x8400): ByteArray {
        val query = MdnsResolver.buildQuery(host)
        val header = byteArrayOf(0, 0, (flags shr 8).toByte(), flags.toByte(), 0, 1, 0, 1, 0, 0, 0, 0)
        val answer = byteArrayOf(
            0xc0.toByte(), 12, // name: pointer to the question name
            0, 1, 0, 1, // TYPE A, CLASS IN
            0, 0, 0x0e, 0x10, // TTL
            0, 4, // RDLENGTH
        ) + ipAddress.map { it.toByte() }.toByteArray()
        return header + query.copyOfRange(12, query.size) + answer
    }

    @Test
    fun buildQueryTest() {
        val expected = byteArrayOf(0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0) +
            byteArrayOf(15) + "THETAYR12345678".encodeToByteArray() +
            byteArrayOf(5) + "local".encodeToByteArray() +
            byteArrayOf(0, 0, 1, 0, 1)
        assertContentEquals(expected, MdnsResolver.buildQuery(host))
    }

    @Test
    fun parseAnswerTest() {
        assertEquals("192.168.10.200", MdnsResolver.parseAnswer(response(listOf(192, 168, 10, 200)), host))
        assertEquals("192.168.10.200", MdnsResolver.parseAnswer(response(listOf(192, 168, 10, 200)), host.lowercase()))
        // Another host
        assertNull(MdnsResolver.parseAnswer(response(listOf(192, 168, 10, 200)), "THETAYR00000000.local"))
        // Not a response
        assertNull(MdnsResolver.parseAnswer(response(listOf(192, 168, 10, 200), flags = 0), host))
        // Broken packet
        assertNull(MdnsResolver.parseAnswer(response(listOf(192, 168, 10, 200)).copyOf(30), host))
    }

    @Test
    fun isTargetTest() {
        val prefixes = listOf("THETAYR", "THETAYL")
        assertTrue(MdnsResolver.isTarget("THETAYR12345678.local", prefixes))
        assertTrue(MdnsResolver.isTarget("thetayr12345678.LOCAL", prefixes))
        assertTrue(MdnsResolver.isTarget("THETAYL12345678.local", prefixes))
        assertFalse(MdnsResolver.isTarget("THETAYN12345678.local", prefixes))
        assertFalse(MdnsResolver.isTarget("THETAYR12345678", prefixes))
        assertFalse(MdnsResolver.isTarget("192.168.1.1", prefixes))
    }
}
