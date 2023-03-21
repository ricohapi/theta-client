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
class ListPluginsTest {
    private val endpoint = "http://192.168.1.1:80/"

    @BeforeTest
    fun setup() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @AfterTest
    fun teardown() {
        MockApiClient.status = HttpStatusCode.OK
    }

    /**
     * call getThetaInfo for THETA Z1.
     */
    @Test
    fun listPluginsTest() = runTest {
        // setup
        val jsonString = Resource("src/commonTest/resources/listPlugins/list_plugins_done.json").readText()
        MockApiClient.onRequest = { request ->
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            ByteReadChannel(jsonString)
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        val plugins = thetaRepository.listPlugins()
        plugins.forEach {
            assertTrue(it.name.length >= 1, "pluginName")
            assertTrue(it.packageName.length >= 1, "packageName")
            assertTrue(it.version.length >= 1, "version")
            assertTrue(it.exitStatus.length >= 1, "exitStatus")
        }
    }

}
