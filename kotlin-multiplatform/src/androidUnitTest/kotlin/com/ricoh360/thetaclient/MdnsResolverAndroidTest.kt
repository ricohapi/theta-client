package com.ricoh360.thetaclient

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Tests of MdnsResolver with the targets of Android.
 */
class MdnsResolverAndroidTest {
    @Test
    fun isTargetTest() {
        assertTrue(MdnsResolver.isTarget("THETAYR12345678.local"))
        assertTrue(MdnsResolver.isTarget("THETAYL12345678.local"))
        assertTrue(MdnsResolver.isTarget("THETAYN12345678.local"))
        // THETA A1 is resolved by the OS on Android
        assertFalse(MdnsResolver.isTarget("THETAAA12345678.local"))
    }

    @Test
    fun resolveEndpointTest() = runTest {
        assertEquals(
            "http://192.168.10.200",
            MdnsResolver.resolveEndpoint("http://THETAYN12345678.local", 5) { host, retryCount ->
                assertEquals("THETAYN12345678.local", host)
                assertEquals(5, retryCount)
                "192.168.10.200"
            },
        )
        // Not a target
        assertEquals("http://192.168.1.1", MdnsResolver.resolveEndpoint("http://192.168.1.1", 5) { _, _ -> error("not called") })
    }

    @Test
    fun resolveEndpointNoAnswerTest() = runTest {
        // The OS would resolve it to the IPv6 address THETA refuses, so it is not connected.
        assertFailsWith<ThetaRepository.NotConnectedException> {
            MdnsResolver.resolveEndpoint("http://THETAYN12345678.local", 5) { _, _ -> null }
        }
        assertFailsWith<ThetaRepository.NotConnectedException> {
            MdnsResolver.resolveEndpoint("http://THETAYN12345678.local", 5) { _, _ -> throw Exception("network error") }
        }
    }

    @Test
    fun resolveEndpointApAddressTest() = runTest {
        // THETA answers its AP mode address while the phone is still on its access point.
        assertFailsWith<ThetaRepository.NotConnectedException> {
            MdnsResolver.resolveEndpoint("http://THETAYN12345678.local", 5) { _, _ -> "192.168.1.1" }
        }
    }
}
