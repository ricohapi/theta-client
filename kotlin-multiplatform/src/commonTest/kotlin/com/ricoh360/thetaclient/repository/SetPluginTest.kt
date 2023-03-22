package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import io.ktor.client.network.sockets.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class SetPluginTest {
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
    fun setPluginTest() = runTest {
        // setup
        val jsonString = Resource("src/commonTest/resources/setPlugin/set_plugin_done_v.json").readText()
        MockApiClient.onRequest = { request ->
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            ByteReadChannel(jsonString)
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        kotlin.runCatching {
            thetaRepository.setPlugin("com.theta360.automaticfaceblur")
        }.onSuccess {
            assertTrue(true, "setPlugin")
        }.onFailure {
            println(it.toString())
            assertTrue(false, "setPlugin")
        }

    }
}
