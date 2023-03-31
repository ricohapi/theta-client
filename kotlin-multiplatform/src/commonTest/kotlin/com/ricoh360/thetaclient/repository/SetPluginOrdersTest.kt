package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.test.*

@OptIn(ExperimentalSerializationApi::class, ExperimentalCoroutinesApi::class)
class SetPluginOrdersTest {
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
    fun setPluginOrdersTest() = runTest {
        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            ByteReadChannel(Resource("src/commonTest/resources/setPluginOrders/set_plugin_orders_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val pluginList = listOf("com.theta360.usbstorage", "com.theta.remoteplayback", "")
        kotlin.runCatching {
            thetaRepository.setPluginOrders(pluginList)
        }.onSuccess {
            assertTrue(true, "setPluginOrders()")
        }.onFailure {
            println(it.toString())
            assertTrue(false, "getPluginOrders()")
        }
    }
}
