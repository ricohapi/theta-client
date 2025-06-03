package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.EndPoint
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GetThetaInfoTest {
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
    fun getThetaInfoForZ1Test() = runTest {
        // setup
        val jsonString = Resource("src/commonTest/resources/info/info_z1.json").readText()
        MockApiClient.onRequest = { request ->
            assertEquals(request.headers.get("Cache-Control"), "no-store")
            assertEquals(request.url.encodedPath, "/osc/info", "request path")
            ByteReadChannel(jsonString)
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        val thetaInfo = thetaRepository.getThetaInfo()

        // check
        assertTrue(thetaRepository.cameraModel == ThetaRepository.ThetaModel.THETA_Z1, "ThetaRepository cameraModel")
        assertTrue(thetaInfo.manufacturer == "RICOH", "info manufacturer")
        assertTrue(thetaInfo.model == "RICOH THETA Z1", "info model")
        assertTrue(thetaInfo.serialNumber.length > 1, "info serialNumber")
        assertNotNull(thetaInfo.wlanMacAddress, "info wlanMacAddress")
        assertNotNull(thetaInfo.bluetoothMacAddress, "info bluetoothMacAddress")
        assertTrue(thetaInfo.firmwareVersion.length > 1, "info firmwareVersion")
        assertTrue(thetaInfo.supportUrl == "https://theta360.com/en/support/", "info supportUrl")
        assertTrue(!thetaInfo.hasGps, "info hasGps")
        assertTrue(thetaInfo.hasGyro, "info hasGyro")
        assertTrue(thetaInfo.uptime > 0, "info uptime")
        assertTrue(
            thetaInfo.api == listOf(
                "/osc/info", "/osc/state", "/osc/checkForUpdates",
                "/osc/commands/execute", "/osc/commands/status"
            ), "info api"
        )
        assertTrue(thetaInfo.endpoints == EndPoint(80, 80), "info endpoints")
        assertTrue(thetaInfo.apiLevel == listOf(2), "info apiLevel")
    }

    /**
     * call getThetaInfo for THETA S.
     */
    @Test
    fun getThetaInfoForSTest() = runTest {
        // setup
        val jsonString = Resource("src/commonTest/resources/info/info_s.json").readText()
        MockApiClient.onRequest = { request ->
            assertEquals(request.url.encodedPath, "/osc/info", "request path")
            ByteReadChannel(jsonString)
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        val thetaInfo = thetaRepository.getThetaInfo()

        // check
        assertTrue(thetaRepository.cameraModel == ThetaRepository.ThetaModel.THETA_S, "ThetaRepository cameraModel")
        assertTrue(thetaInfo.manufacturer == "RICOH", "info manufacturer")
        assertTrue(thetaInfo.model == "RICOH THETA S", "info model")
        assertTrue(thetaInfo.serialNumber.length > 1, "info serialNumber")
        assertNull(thetaInfo.wlanMacAddress, "info wlanMacAddress")
        assertNull(thetaInfo.bluetoothMacAddress, "info bluetoothMacAddress")
        assertTrue(thetaInfo.firmwareVersion.length > 1, "info firmwareVersion")
        assertTrue(thetaInfo.supportUrl == "https://theta360.com/en/support/", "info supportUrl")
        assertTrue(!thetaInfo.hasGps, "info hasGps")
        assertTrue(!thetaInfo.hasGyro, "info hasGyro")
        assertTrue(thetaInfo.uptime > 0, "info uptime")
        assertTrue(
            thetaInfo.api == listOf(
                "/osc/info", "/osc/state", "/osc/checkForUpdates",
                "/osc/commands/execute", "/osc/commands/status"
            ), "info api"
        )
        assertTrue(thetaInfo.endpoints == EndPoint(80, 80), "info endpoints")
        assertTrue(thetaInfo.apiLevel == listOf(1, 2), "info apiLevel")
    }

    /**
     * call getThetaInfo for THETA X.
     */
    @Test
    fun getThetaInfoForXTest() = runTest {
        // setup
        val jsonString = Resource("src/commonTest/resources/info/info_x.json").readText()
        MockApiClient.onRequest = { request ->
            assertEquals(request.url.encodedPath, "/osc/info", "request path")
            ByteReadChannel(jsonString)
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        val thetaInfo = thetaRepository.getThetaInfo()

        // check
        assertTrue(thetaRepository.cameraModel == ThetaRepository.ThetaModel.THETA_X, "ThetaRepository cameraModel")
        assertTrue(thetaInfo.manufacturer == "RICOH", "info manufacturer")
        assertTrue(thetaInfo.model == "RICOH THETA X", "info model")
        assertTrue(thetaInfo.serialNumber.length > 1, "info serialNumber")
        assertNotNull(thetaInfo.wlanMacAddress, "info wlanMacAddress")
        assertNotNull(thetaInfo.bluetoothMacAddress, "info bluetoothMacAddress")
        assertTrue(thetaInfo.firmwareVersion.length > 1, "info firmwareVersion")
        assertTrue(thetaInfo.supportUrl == "https://theta360.com/en/support/", "info supportUrl")
        assertTrue(thetaInfo.hasGps, "info hasGps")
        assertTrue(thetaInfo.hasGyro, "info hasGyro")
        assertTrue(thetaInfo.uptime > 0, "info uptime")
        assertTrue(
            thetaInfo.api == listOf(
                "/osc/info", "/osc/state", "/osc/checkForUpdates",
                "/osc/commands/execute", "/osc/commands/status"
            ), "info api"
        )
        assertTrue(thetaInfo.endpoints == EndPoint(80, 80), "info endpoints")
        assertTrue(thetaInfo.apiLevel == listOf(2), "info apiLevel")
    }

    /**
     * call getThetaInfo for THETA SC2.
     */
    @Test
    fun getThetaInfoForSC2Test() = runTest {
        // setup
        val jsonString = Resource("src/commonTest/resources/info/info_sc2.json").readText()
        MockApiClient.onRequest = { request ->
            assertEquals(request.url.encodedPath, "/osc/info", "request path")
            ByteReadChannel(jsonString)
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        val thetaInfo = thetaRepository.getThetaInfo()

        // check
        assertTrue(thetaRepository.cameraModel == ThetaRepository.ThetaModel.THETA_SC2, "ThetaRepository cameraModel")
        assertTrue(thetaInfo.manufacturer == "RICOH", "info manufacturer")
        assertTrue(thetaInfo.model == "RICOH THETA SC2", "info model")
        assertTrue(thetaInfo.serialNumber.length > 1, "info serialNumber")
        assertNotNull(thetaInfo.wlanMacAddress, "info wlanMacAddress")
        assertNotNull(thetaInfo.bluetoothMacAddress, "info bluetoothMacAddress")
        assertTrue(thetaInfo.firmwareVersion.length > 1, "info firmwareVersion")
        val result = Regex("^\\d+").find(thetaInfo.firmwareVersion)
        assertNotNull(result)
        result.value.toIntOrNull()?.let { assertTrue(it <= 5) } ?: assertTrue(false, "firmware version")
        assertTrue(thetaInfo.supportUrl == "https://theta360.com/en/support/", "info supportUrl")
        assertTrue(!thetaInfo.hasGps, "info hasGps")
        assertTrue(thetaInfo.hasGyro, "info hasGyro")
        assertTrue(thetaInfo.uptime > 0, "info uptime")
        assertTrue(
            thetaInfo.api == listOf(
                "/osc/info", "/osc/state", "/osc/checkForUpdates",
                "/osc/commands/execute", "/osc/commands/status"
            ), "info api"
        )
        assertTrue(thetaInfo.endpoints == EndPoint(80, 80), "info endpoints")
        assertTrue(thetaInfo.apiLevel == listOf(2), "info apiLevel")
    }

    /**
     * call getThetaInfo for THETA SC2 for business.
     */
    @Test
    fun getThetaInfoForSC2BusinessTest() = runTest {
        // setup
        val jsonString = Resource("src/commonTest/resources/info/info_sc2_business.json").readText()
        MockApiClient.onRequest = { request ->
            assertEquals(request.url.encodedPath, "/osc/info", "request path")
            ByteReadChannel(jsonString)
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        val thetaInfo = thetaRepository.getThetaInfo()

        // check
        assertTrue(thetaRepository.cameraModel == ThetaRepository.ThetaModel.THETA_SC2_B, "ThetaRepository cameraModel")
        assertTrue(thetaInfo.manufacturer == "RICOH", "info manufacturer")
        assertTrue(thetaInfo.model == "RICOH THETA SC2", "info model")
        assertTrue(thetaInfo.serialNumber.length > 1, "info serialNumber")
        assertNotNull(thetaInfo.wlanMacAddress, "info wlanMacAddress")
        assertNotNull(thetaInfo.bluetoothMacAddress, "info bluetoothMacAddress")
        assertTrue(thetaInfo.firmwareVersion.length > 1, "info firmwareVersion")
        val result = Regex("^\\d+").find(thetaInfo.firmwareVersion)
        assertNotNull(result)
        result.value.toIntOrNull()?.let { assertTrue(it >= 6) } ?: assertTrue(false, "firmware version")
        assertTrue(thetaInfo.supportUrl == "https://theta360.com/en/support/", "info supportUrl")
        assertTrue(!thetaInfo.hasGps, "info hasGps")
        assertTrue(thetaInfo.hasGyro, "info hasGyro")
        assertTrue(thetaInfo.uptime > 0, "info uptime")
        assertTrue(
            thetaInfo.api == listOf(
                "/osc/info", "/osc/state", "/osc/checkForUpdates",
                "/osc/commands/execute", "/osc/commands/status"
            ), "info api"
        )
        assertTrue(thetaInfo.endpoints == EndPoint(80, 80), "info endpoints")
        assertTrue(thetaInfo.apiLevel == listOf(2), "info apiLevel")
    }

    /**
     * Error not json response to getThetaInfo call
     */
    @Test
    fun getThetaInfoNotJsonResponseTest() = runTest {
        // setup
        MockApiClient.onRequest = { _ ->
            ByteReadChannel("Not json")
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.getThetaInfo()
            assertTrue(false, "call getThetaInfo")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(
                e.message!!.indexOf("json", 0, true) >= 0 ||
                        e.message!!.indexOf("Illegal", 0, true) >= 0,
                "error response"
            )
        }
    }

    /**
     * Status error to getThetaInfo call
     */
    @Test
    fun getThetaInfoStatusErrorTest() = runTest {
        // setup
        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel("status error")
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.getThetaInfo()
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            assertTrue(false, "status error")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("503", 0, true) >= 0, "status error")
        }
    }

    /**
     * Error exception to getThetaInfo call
     */
    @Test
    fun getThetaInfoExceptionTest() = runTest {
        // setup
        MockApiClient.onRequest = { _ ->
            throw ConnectTimeoutException("timeout")
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.getThetaInfo()
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            assertTrue(false, "exception error")
        } catch (e: ThetaRepository.NotConnectedException) {
            assertTrue(e.message!!.indexOf("time", 0, true) >= 0, "timeout exception")
        }
    }

    /**
     * Check ThetaModel enum.
     */
    @Test
    fun enumThetaModelTest() = runTest {
        val modelEnumArray = arrayOf(
            ThetaRepository.ThetaModel.THETA_S,
            ThetaRepository.ThetaModel.THETA_SC,
            ThetaRepository.ThetaModel.THETA_SC2,
            ThetaRepository.ThetaModel.THETA_SC2_B,
            ThetaRepository.ThetaModel.THETA_V,
            ThetaRepository.ThetaModel.THETA_Z1,
            ThetaRepository.ThetaModel.THETA_X,
            ThetaRepository.ThetaModel.THETA_A1,
        )
        val modelNameArray = arrayOf(
            "RICOH THETA S",
            "RICOH THETA SC",
            "RICOH THETA SC2",
            "RICOH THETA SC2", // SC2 for business
            "RICOH THETA V",
            "RICOH THETA Z1",
            "RICOH THETA X",
            "RICOH360 THETA A1",
        )

        modelEnumArray.forEachIndexed { index, it ->
            assertEquals(it.value, modelNameArray[index], "ThetaModel: $it.value Name: $modelNameArray[index]")
        }

        modelNameArray.forEachIndexed { index, it ->
            when (index) {
                // SC2
                2 -> assertEquals(ThetaRepository.ThetaModel.get(it, "00118200"), modelEnumArray[index], "Name: $it ThetaModel: $modelEnumArray[index].value")
                // SC2 for business
                3 -> assertEquals(ThetaRepository.ThetaModel.get(it, "40118200"), modelEnumArray[index], "Name: $it ThetaModel: $modelEnumArray[index].value")
                // other models
                else -> assertEquals(ThetaRepository.ThetaModel.get(it), modelEnumArray[index], "Name: $it ThetaModel: $modelEnumArray[index].value")
            }
        }

        assertNull(ThetaRepository.ThetaModel.get("Other camera"), "Other camera enum")
    }
}
