package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.AuthenticationMode
import com.ricoh360.thetaclient.transferred.IpAddressAllocation
import com.ricoh360.thetaclient.transferred.SetAccessPointRequest
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalSerializationApi::class)
class SetAccessPointTest {
    private val endpoint = "http://192.168.1.1:80/"

    @BeforeTest
    fun setup() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @AfterTest
    fun teardown() {
        MockApiClient.status = HttpStatusCode.OK
    }

    private fun checkRequest(
        request: HttpRequestData,
        ssid: String,
        ssidStealth: Boolean,
        security: AuthenticationMode?,
        password: String?,
        connectionPriority: Int,
        ipAddressAllocation: IpAddressAllocation?,
        ipAddress: String?,
        subnetMask: String?,
        defaultGateway: String?,
        dns1: String?,
        dns2: String?,
        proxy: ThetaRepository.Proxy?
    ) {
        assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
        val body = request.body as TextContent
        val js = Json {
            encodeDefaults = true // Encode properties with default value.
            explicitNulls = false // Don't encode properties with null value.
            ignoreUnknownKeys = true // Ignore unknown keys on decode.
        }
        val setAccessPointRequest = js.decodeFromString<SetAccessPointRequest>(body.text)

        // check
        assertEquals(setAccessPointRequest.name, "camera._setAccessPoint", "command name")
        setAccessPointRequest.parameters.let {
            assertEquals(it.ssid, ssid, "ssid")
            assertEquals(it.ssidStealth, ssidStealth, "ssidStealth")
            assertEquals(it.security, security, "security")
            assertEquals(it.password, password, "password")
            assertEquals(it.connectionPriority, connectionPriority, "connectionPriority")
            assertEquals(it.ipAddressAllocation, ipAddressAllocation, "ipAddressAllocation")
            assertEquals(it.ipAddress, ipAddress, "ipAddress")
            assertEquals(it.subnetMask, subnetMask, "subnetMask")
            assertEquals(it.defaultGateway, defaultGateway, "defaultGateway")
            assertEquals(it.dns1, dns1, "dns1")
            assertEquals(it.dns2, dns2, "dns2")
            assertEquals(it.proxy, proxy?.toTransferredProxy(), "_proxy")
        }
    }

    /**
     * call setAccessPointDynamically.
     */
    @Test
    fun setAccessPointDynamicallyTest() = runTest {
        val ssid = "ssid_test"
        val ssidStealth = true
        val authMode = ThetaRepository.AuthModeEnum.WEP
        val password = "password_test"
        val connectionPriority = 2
        val proxy = ThetaRepository.Proxy(use = true, url = "https://xxx", port = 8081, userid = "abc", password = "pwpwpw111")

        MockApiClient.onRequest = { request ->
            checkRequest(
                request,
                ssid,
                ssidStealth,
                authMode.value,
                password,
                connectionPriority,
                IpAddressAllocation.DYNAMIC,
                null,
                null,
                null,
                null,
                null,
                proxy = proxy
            )

            ByteReadChannel(Resource("src/commonTest/resources/setAccessPoint/set_access_point_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.setAccessPointDynamically(
            ssid = ssid,
            ssidStealth = ssidStealth,
            authMode = authMode,
            password = password,
            connectionPriority = connectionPriority,
            proxy = proxy
        )
        assertTrue(true, "response is normal.")
    }

    /**
     * call setAccessPointStatically.
     */
    @Test
    fun setAccessPointStaticallyTest() = runTest {
        val ssid = "ssid_test"
        val ssidStealth = true
        val authMode = ThetaRepository.AuthModeEnum.WPA
        val password = "password_test"
        val connectionPriority = 2
        val ipAddress = "192.168.1.2"
        val subnetMask = "255.255.255.0"
        val defaultGateway = "192.168.1.3"
        val dns1 = "192.168.1.55"
        val dns2 = "192.168.1.66"
        val proxy = ThetaRepository.Proxy(use = true, url = "https://xxx", port = 8081, userid = "abc", password = "pwpwpw111")

        MockApiClient.onRequest = { request ->
            checkRequest(
                request,
                ssid,
                ssidStealth,
                authMode.value,
                password,
                connectionPriority,
                IpAddressAllocation.STATIC,
                ipAddress,
                subnetMask,
                defaultGateway,
                dns1,
                dns2,
                proxy
            )

            ByteReadChannel(Resource("src/commonTest/resources/setAccessPoint/set_access_point_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.setAccessPointStatically(
            ssid = ssid,
            ssidStealth = ssidStealth,
            authMode = authMode,
            password = password,
            connectionPriority = connectionPriority,
            ipAddress = ipAddress,
            subnetMask = subnetMask,
            defaultGateway = defaultGateway,
            dns1 = dns1,
            dns2 = dns2,
            proxy = proxy
        )
        assertTrue(true, "response is normal.")
    }

    /**
     * call setAccessPointConnectionPriority.
     */
    @Test
    fun setAccessPointConnectionPriorityTest() = runTest {
        val ssid = "ssid_test"
        val ssidStealth = true
        val connectionPriority = 2

        MockApiClient.onRequest = { request ->
            checkRequest(
                request,
                ssid,
                ssidStealth,
                null,
                null,
                connectionPriority,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )

            ByteReadChannel(Resource("src/commonTest/resources/setAccessPoint/set_access_point_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.setAccessPointConnectionPriority(
            ssid = ssid,
            ssidStealth = ssidStealth,
            connectionPriority = connectionPriority,
        )
        assertTrue(true, "response is normal.")
    }

    /**
     * Error not json response to setAccessPoint call
     */
    @Test
    fun setAccessPointNotJsonResponseTest() = runTest {
        MockApiClient.onRequest = { _ ->
            ByteReadChannel("Not json")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val ssid = "ssid_test"
            val ssidStealth = true
            val authMode = ThetaRepository.AuthModeEnum.WPA
            val password = "password_test"
            val connectionPriority = 2
            val ipAddress = "192.168.1.2"
            val subnetMask = "255.255.255.0"
            val defaultGateway = "192.168.1.3"
            val dns1 = "192.168.1.55"
            val dns2 = "192.168.1.66"
            val ipAddressAllocation = IpAddressAllocation.STATIC
            val proxy = ThetaRepository.Proxy(use = true, url = "https://xxx", port = 8081, userid = "abc", password = "pwpwpw111")

            thetaRepository.setAccessPoint(
                ssid = ssid,
                ssidStealth = ssidStealth,
                authMode = authMode,
                password = password,
                connectionPriority = connectionPriority,
                ipAddress = ipAddress,
                subnetMask = subnetMask,
                defaultGateway = defaultGateway,
                dns1 = dns1,
                dns2 = dns2,
                ipAddressAllocation = ipAddressAllocation,
                proxy = proxy
            )
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
     * Error response to setAccessPoint call
     */
    @Test
    fun setAccessPointErrorResponseTest() = runTest {
        MockApiClient.onRequest = { _ ->
            ByteReadChannel(Resource("src/commonTest/resources/setAccessPoint/set_access_point_error.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val ssid = "ssid_test"
            val ssidStealth = true
            val authMode = ThetaRepository.AuthModeEnum.WPA
            val password = "password_test"
            val connectionPriority = 2
            val ipAddress = "192.168.1.2"
            val subnetMask = "255.255.255.0"
            val defaultGateway = "192.168.1.3"
            val dns1 = "192.168.1.55"
            val dns2 = "192.168.1.66"
            val ipAddressAllocation = IpAddressAllocation.STATIC
            val proxy = ThetaRepository.Proxy(use = true, url = "https://xxx", port = 8081, userid = "abc", password = "pwpwpw111")

            thetaRepository.setAccessPoint(
                ssid = ssid,
                ssidStealth = ssidStealth,
                authMode = authMode,
                password = password,
                connectionPriority = connectionPriority,
                ipAddress = ipAddress,
                subnetMask = subnetMask,
                defaultGateway = defaultGateway,
                dns1 = dns1,
                dns2 = dns2,
                ipAddressAllocation = ipAddressAllocation,
                proxy = proxy
            )
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Error response and status error to setAccessPoint call
     */
    @Test
    fun setAccessPointErrorResponseAndStatusErrorTest() = runTest {
        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel(Resource("src/commonTest/resources/setAccessPoint/set_access_point_error.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val ssid = "ssid_test"
            val ssidStealth = true
            val authMode = ThetaRepository.AuthModeEnum.WPA
            val password = "password_test"
            val connectionPriority = 2
            val ipAddress = "192.168.1.2"
            val subnetMask = "255.255.255.0"
            val defaultGateway = "192.168.1.3"
            val dns1 = "192.168.1.55"
            val dns2 = "192.168.1.66"
            val ipAddressAllocation = IpAddressAllocation.STATIC
            val proxy = ThetaRepository.Proxy(use = true, url = "https://xxx", port = 8081, userid = "abc", password = "pwpwpw111")

            thetaRepository.setAccessPoint(
                ssid = ssid,
                ssidStealth = ssidStealth,
                authMode = authMode,
                password = password,
                connectionPriority = connectionPriority,
                ipAddress = ipAddress,
                subnetMask = subnetMask,
                defaultGateway = defaultGateway,
                dns1 = dns1,
                dns2 = dns2,
                ipAddressAllocation = ipAddressAllocation,
                proxy = proxy
            )
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Status error to setAccessPoint call
     */
    @Test
    fun setAccessPointStatusErrorTest() = runTest {
        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel("Not json")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val ssid = "ssid_test"
            val ssidStealth = true
            val authMode = ThetaRepository.AuthModeEnum.WPA
            val password = "password_test"
            val connectionPriority = 2
            val ipAddress = "192.168.1.2"
            val subnetMask = "255.255.255.0"
            val defaultGateway = "192.168.1.3"
            val dns1 = "192.168.1.55"
            val dns2 = "192.168.1.66"
            val ipAddressAllocation = IpAddressAllocation.STATIC
            val proxy = ThetaRepository.Proxy(use = true, url = "https://xxx", port = 8081, userid = "abc", password = "pwpwpw111")

            thetaRepository.setAccessPoint(
                ssid = ssid,
                ssidStealth = ssidStealth,
                authMode = authMode,
                password = password,
                connectionPriority = connectionPriority,
                ipAddress = ipAddress,
                subnetMask = subnetMask,
                defaultGateway = defaultGateway,
                dns1 = dns1,
                dns2 = dns2,
                ipAddressAllocation = ipAddressAllocation,
                proxy = proxy
            )
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("503", 0, true) >= 0, "status error")
        }
    }

    /**
     * Error exception to setAccessPoint call
     */
    @Test
    fun setAccessPointExceptionTest() = runTest {
        MockApiClient.onRequest = { _ ->
            throw ConnectTimeoutException("timeout")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val ssid = "ssid_test"
            val ssidStealth = true
            val authMode = ThetaRepository.AuthModeEnum.WPA
            val password = "password_test"
            val connectionPriority = 2
            val ipAddress = "192.168.1.2"
            val subnetMask = "255.255.255.0"
            val defaultGateway = "192.168.1.3"
            val dns1 = "192.168.1.55"
            val dns2 = "192.168.1.66"
            val ipAddressAllocation = IpAddressAllocation.STATIC
            val proxy = ThetaRepository.Proxy(use = true, url = "https://xxx", port = 8081, userid = "abc", password = "pwpwpw111")

            thetaRepository.setAccessPoint(
                ssid = ssid,
                ssidStealth = ssidStealth,
                authMode = authMode,
                password = password,
                connectionPriority = connectionPriority,
                ipAddress = ipAddress,
                subnetMask = subnetMask,
                defaultGateway = defaultGateway,
                dns1 = dns1,
                dns2 = dns2,
                ipAddressAllocation = ipAddressAllocation,
                proxy = proxy
            )
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.NotConnectedException) {
            assertTrue(e.message!!.indexOf("time", 0, true) >= 0, "timeout exception")
        }
    }

    @Test
    fun setAccessPointAuthModeExceptionTest() = runTest {
        MockApiClient.onRequest = { _ ->
            ByteReadChannel(Resource("src/commonTest/resources/listAccessPoints/list_access_points_empty_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_A1
        try {
            val ssid = "ssid_test"
            val authMode = ThetaRepository.AuthModeEnum.NONE
            val ipAddressAllocation = IpAddressAllocation.STATIC

            thetaRepository.setAccessPoint(
                ssid = ssid,
                authMode = authMode,
                ipAddressAllocation = ipAddressAllocation,
            )
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.contains("does not allow authMode to be set to none."), "authMode exception")
        }
    }

    @Test
    fun setAccessPointDynamicallyAuthModeExceptionTest() = runTest {
        MockApiClient.onRequest = { _ ->
            ByteReadChannel(Resource("src/commonTest/resources/listAccessPoints/list_access_points_empty_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_A1
        try {
            val ssid = "ssid_test"
            val authMode = ThetaRepository.AuthModeEnum.NONE

            thetaRepository.setAccessPointDynamically(
                ssid = ssid,
                authMode = authMode
            )
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.contains("does not allow authMode to be set to none."), "authMode exception")
        }
    }

    @Test
    fun setAccessPointStaticallyAuthModeExceptionTest() = runTest {
        MockApiClient.onRequest = { _ ->
            ByteReadChannel(Resource("src/commonTest/resources/listAccessPoints/list_access_points_empty_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_A1
        try {
            val ssid = "ssid_test"
            val authMode = ThetaRepository.AuthModeEnum.NONE
            val ipAddress = "192.168.1.2"
            val subnetMask = "255.255.255.0"
            val defaultGateway = "192.168.1.3"

            thetaRepository.setAccessPointStatically(
                ssid = ssid,
                authMode = authMode,
                ipAddress = ipAddress,
                subnetMask = subnetMask,
                defaultGateway = defaultGateway
            )
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.contains("does not allow authMode to be set to none."), "authMode exception")
        }
    }
}
