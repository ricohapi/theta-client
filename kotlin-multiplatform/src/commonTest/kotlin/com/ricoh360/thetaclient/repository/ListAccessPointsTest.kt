package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.AccessPoint
import com.ricoh360.thetaclient.transferred.AuthenticationMode
import com.ricoh360.thetaclient.transferred.IpAddressAllocation
import io.ktor.client.network.sockets.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ListAccessPointsTest {
    private val endpoint = "http://dummy/"

    @BeforeTest
    fun setup() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @AfterTest
    fun teardown() {
        MockApiClient.status = HttpStatusCode.OK
    }

    /**
     * call listAccessPoints.
     */
    @Test
    fun listAccessPointsTest() = runTest {
        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkCommandName(request, "camera._listAccessPoints")
            ByteReadChannel(Resource("src/commonTest/resources/listAccessPoints/list_access_points_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val response = thetaRepository.listAccessPoints()

        assertTrue(response.isNotEmpty(), "Has AccessPoints")
        response.forEach {
            assertTrue(it.ssid.isNotEmpty(), "ssid")
            assertTrue(ThetaRepository.AuthModeEnum.values().indexOf(it.authMode) >= 0, "authMode")
            assertTrue(it.connectionPriority > 0, "connectionPriority")
            if (it.usingDhcp) {
                assertNull(it.ipAddress, "ipAddress")
                assertNull(it.subnetMask, "subnetMask")
                assertNull(it.defaultGateway, "defaultGateway")
            } else {
                assertNotNull(it.ipAddress, "ipAddress")
                assertNotNull(it.subnetMask, "subnetMask")
                assertNotNull(it.defaultGateway, "defaultGateway")
            }
        }
    }

    /**
     * call listAccessPoints. Response is empty data.
     */
    @Test
    fun listAccessPointsEmptyTest() = runTest {
        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkCommandName(request, "camera._listAccessPoints")
            ByteReadChannel(Resource("src/commonTest/resources/listAccessPoints/list_access_points_empty_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val response = thetaRepository.listAccessPoints()

        assertTrue(response.isEmpty(), "No AccessPoints")
    }

    /**
     * Error not json response to listAccessPoints call
     */
    @Test
    fun listAccessPointsNotJsonResponseTest() = runTest {
        MockApiClient.onRequest = { _ ->
            ByteReadChannel("Not json")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.listAccessPoints()
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
     * Error response to listAccessPoints call
     */
    @Test
    fun listAccessPointsErrorResponseTest() = runTest {
        MockApiClient.onRequest = { _ ->
            ByteReadChannel(Resource("src/commonTest/resources/listAccessPoints/list_access_points_error.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.listAccessPoints()
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Error response and status error to listAccessPoints call
     */
    @Test
    fun listAccessPointsErrorResponseAndStatusErrorTest() = runTest {
        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel(Resource("src/commonTest/resources/listAccessPoints/list_access_points_error.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.listAccessPoints()
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Status error to listAccessPoints call
     */
    @Test
    fun listAccessPointsStatusErrorTest() = runTest {
        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel("Not json")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.listAccessPoints()
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("503", 0, true) >= 0, "status error")
        }
    }

    /**
     * Error exception to listAccessPoints call
     */
    @Test
    fun listAccessPointsExceptionTest() = runTest {
        MockApiClient.onRequest = { _ ->
            throw ConnectTimeoutException("timeout")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.listAccessPoints()
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.NotConnectedException) {
            assertTrue(e.message!!.indexOf("time", 0, true) >= 0, "timeout exception")
        }
    }

    private fun checkAccessPoint(
        security: AuthenticationMode,
        ipAddressAllocation: IpAddressAllocation,
        authMode: ThetaRepository.AuthModeEnum,
        usingDhcp: Boolean
    ) {
        val orgAccessPoint = AccessPoint(
            ssid = "ssid",
            ssidStealth = true,
            security = security,
            connectionPriority = 1,
            ipAddressAllocation = ipAddressAllocation,
            ipAddress = "ipAddress",
            subnetMask = "subnetMask",
            defaultGateway = "defaultGateway"
        )
        val accessPoint = ThetaRepository.AccessPoint(orgAccessPoint)

        assertEquals(accessPoint.authMode, authMode, "authMode")
        assertEquals(accessPoint.usingDhcp, usingDhcp, "usingDhcp")

        assertEquals(accessPoint.ssid, orgAccessPoint.ssid, "ssid")
        assertEquals(accessPoint.ssidStealth, orgAccessPoint.ssidStealth, "ssidStealth")
        assertEquals(accessPoint.connectionPriority, orgAccessPoint.connectionPriority, "connectionPriority")
        assertEquals(accessPoint.ipAddress, orgAccessPoint.ipAddress, "ipAddress")
        assertEquals(accessPoint.subnetMask, orgAccessPoint.subnetMask, "subnetMask")
        assertEquals(accessPoint.defaultGateway, orgAccessPoint.defaultGateway, "defaultGateway")
    }

    /**
     * Check AccessPoints constructor
     */
    @Test
    fun checkAccessPointsTest() = runTest {
        checkAccessPoint(AuthenticationMode.NONE, IpAddressAllocation.DYNAMIC, ThetaRepository.AuthModeEnum.NONE, true)
        checkAccessPoint(AuthenticationMode.WEP, IpAddressAllocation.STATIC, ThetaRepository.AuthModeEnum.WEP, false)
        checkAccessPoint(AuthenticationMode.WPA_WPA2_PSK, IpAddressAllocation.DYNAMIC, ThetaRepository.AuthModeEnum.WPA, true)
    }
}
