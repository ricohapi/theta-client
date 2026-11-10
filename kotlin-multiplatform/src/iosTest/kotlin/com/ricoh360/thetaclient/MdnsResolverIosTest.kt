package com.ricoh360.thetaclient

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Tests of the host name resolution on iOS.
 */
class MdnsResolverIosTest {
    @Test
    fun isTargetTest() {
        assertTrue(MdnsResolver.isTarget("THETAAA12345678.local"))
        // Other models are resolved by the OS on iOS
        assertFalse(MdnsResolver.isTarget("THETAYR12345678.local"))
        assertFalse(MdnsResolver.isTarget("THETAYL12345678.local"))
        assertFalse(MdnsResolver.isTarget("THETAYN12345678.local"))
    }

    @Test
    fun resolveMdnsHostTest() = runTest {
        assertEquals("127.0.0.1", resolveMdnsHost("localhost", 1))
    }
}
