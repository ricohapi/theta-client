package com.ricoh360.thetaclient

import platform.Foundation.NSProcessInfo
import kotlin.test.Test
import kotlin.test.assertTrue

class iosTest {

    @Test
    fun testExample() {
        assertTrue(Greeting().greeting().contains("iOS"), "Check iOS is mentioned")
    }
}

actual fun getEnv(name: String): String? {
    return NSProcessInfo.processInfo.environment[name] as? String
}
