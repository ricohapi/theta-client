package com.ricoh360.thetaclient

import com.goncalossilva.resources.Resource
import kotlin.test.Test
import kotlin.test.assertTrue

class CommonGreetingTest {

    @Test
    fun testExample() {
        assertTrue(Greeting().greeting().contains("Hello"), "Check 'Hello' is mentioned")
        Resource("")
        print("CommonGreetingTest testExample")
    }
}

/**
 * Get environment variable value by name
 * @param name Environment variable name
 * @return Environment variable value, or null if not set
 */
expect fun getEnv(name: String): String?
