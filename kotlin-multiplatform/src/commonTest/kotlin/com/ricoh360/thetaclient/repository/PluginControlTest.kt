package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import io.ktor.client.network.sockets.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class PluginControlTest {
    private val endpoint = "http://192.168.1.1:80/"

    @BeforeTest
    fun setup() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @AfterTest
    fun teardown() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @Test
    fun pluginControlTest() = runTest {
        // setup
        val jsonString = Resource("src/commonTest/resources/pluginControl/plugin_control_done.json").readText()
        MockApiClient.onRequest = { request ->
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            ByteReadChannel(jsonString)
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        // start plugin
        kotlin.runCatching {
            thetaRepository.startPlugin()
        }.onSuccess {
            assertTrue(true, "startPlugin")
        }.onFailure {
            assertTrue(false, "startPlugin")
            println(it.toString())
        }
        // stop plugin
        runBlocking {
            delay(2000)
        }
        kotlin.runCatching {
            thetaRepository.stopPlugin()
        }.onSuccess {
            assertTrue(true, "stopPlugin")
        }.onFailure {
            assertTrue(false, "stopPlugin")
            println(it.toString())
        }

        // start plugin
        kotlin.runCatching {
            thetaRepository.startPlugin("com.theta360.automaticfaceblur")
        }.onSuccess {
            assertTrue(true, "startPlugin")
        }.onFailure {
            assertTrue(false, "startPlugin")
            println(it.toString())
        }
        // stop plugin
        runBlocking {
            delay(2000)
        }
        kotlin.runCatching {
            thetaRepository.stopPlugin()
        }.onSuccess {
            assertTrue(true, "stopPlugin")
        }.onFailure {
            assertTrue(false, "stopPlugin")
            println(it.toString())
        }
    }
}
