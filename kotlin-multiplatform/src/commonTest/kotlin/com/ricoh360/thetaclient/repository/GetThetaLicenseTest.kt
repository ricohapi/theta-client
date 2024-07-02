package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalSerializationApi::class, ExperimentalCoroutinesApi::class)
class GetThetaLicenseTest {
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
            assertEquals(request.url.encodedPath, "/legal-information/open-source-licenses", "request path")
            ByteReadChannel(Resource("src/commonTest/resources/getThetaLicense/license.html").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        var html = ""
        kotlin.runCatching {
            html = thetaRepository.getThetaLicense()
        }.onSuccess {
            assertTrue(html.startsWith("<html>"), "getThetaLicense()")
        }.onFailure {
            println(it.toString())
            assertTrue(false, "getThetaLicense()")
        }
    }
}
