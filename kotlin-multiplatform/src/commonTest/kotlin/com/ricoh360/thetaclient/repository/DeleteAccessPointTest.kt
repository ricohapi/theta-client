package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.DeleteAccessPointRequest
import io.ktor.client.network.sockets.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteAccessPointTest {
    private val endpoint = "http://192.168.1.1:80/"

    @BeforeTest
    fun setup() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @AfterTest
    fun teardown() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun checkRequest(request: HttpRequestData, ssid: String) {
        assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
        val body = request.body as TextContent
        val js = Json {
            encodeDefaults = true // Encode properties with default value.
            explicitNulls = false // Don't encode properties with null value.
            ignoreUnknownKeys = true // Ignore unknown keys on decode.
        }
        val deleteAccessPoint = js.decodeFromString<DeleteAccessPointRequest>(body.text)

        // check
        assertEquals(deleteAccessPoint.name, "camera._deleteAccessPoint", "command name")
        assertEquals(deleteAccessPoint.parameters.ssid, ssid, "ssid")
    }

    /**
     * call deleteAccessPoint.
     */
    @Test
    fun deleteAccessPointTest() = runTest {
        val ssid = "ssid_test"
        MockApiClient.onRequest = { request ->
            // check request
            checkRequest(request, ssid)

            ByteReadChannel(Resource("src/commonTest/resources/deleteAccessPoint/delete_access_point_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.deleteAccessPoint(ssid)
        assertTrue(true, "call deleteAccessPoint")
    }

    /**
     * Error not json response to deleteAccessPoint call
     */
    @Test
    fun deleteAccessPointNotJsonResponseTest() = runTest {
        MockApiClient.onRequest = { _ ->
            ByteReadChannel("Not json")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val ssid = "ssid_test"
            thetaRepository.deleteAccessPoint(ssid)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(
                e.message!!.indexOf("json", 0, true) >= 0 ||
                    e.message!!.indexOf("Illegal", 0, true) >= 0,
                "error response"
            )
        }
    }

    /**
     * Error response to deleteAccessPoint call
     */
    @Test
    fun deleteAccessPointErrorResponseTest() = runTest {
        MockApiClient.onRequest = { _ ->
            ByteReadChannel(Resource("src/commonTest/resources/deleteAccessPoint/delete_access_point_error.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val ssid = "ssid_test"
            thetaRepository.deleteAccessPoint(ssid)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Error response and status error to deleteAccessPoint call
     */
    @Test
    fun deleteAccessPointErrorResponseAndStatusErrorTest() = runTest {
        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel(Resource("src/commonTest/resources/deleteAccessPoint/delete_access_point_error.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val ssid = "ssid_test"
            thetaRepository.deleteAccessPoint(ssid)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Status error to deleteAccessPoint call
     */
    @Test
    fun deleteAccessPointStatusErrorTest() = runTest {
        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel("Not json")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val ssid = "ssid_test"
            thetaRepository.deleteAccessPoint(ssid)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("503", 0, true) >= 0, "status error")
        }
    }

    /**
     * Error exception to deleteAccessPoint call
     */
    @Test
    fun deleteAccessPointExceptionTest() = runTest {
        MockApiClient.onRequest = { _ ->
            throw ConnectTimeoutException("timeout")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val ssid = "ssid_test"
            thetaRepository.deleteAccessPoint(ssid)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.NotConnectedException) {
            assertTrue(e.message!!.indexOf("time", 0, true) >= 0, "timeout exception")
        }
    }
}
