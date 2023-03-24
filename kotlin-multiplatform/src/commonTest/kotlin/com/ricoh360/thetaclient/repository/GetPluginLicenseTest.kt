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
class GetPluginLicenseTest {
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
    fun getPluginLicenseTest() = runTest {
        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            ByteReadChannel(Resource("src/commonTest/resources/getPluginLicense/license.html").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        var html = ""
        kotlin.runCatching {
            html = thetaRepository.getPluginLicense("com.theta360.automaticfaceblur")
        }.onSuccess {
            assertTrue(true, "getPluginLicense()")
            println(html)
        }.onFailure {
            println(it.toString())
            assertTrue(false, "getPluginLicense()")
        }
    }
}
