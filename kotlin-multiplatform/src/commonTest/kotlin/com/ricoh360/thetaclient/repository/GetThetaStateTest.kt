package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.*
import io.ktor.client.network.sockets.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class GetThetaStateTest {
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
     * call getThetaState.
     */
    @Test
    fun getThetaStateTest() = runTest {
        // setup
        val jsonString = Resource("src/commonTest/resources/state/state_z1.json").readText()
        MockApiClient.onRequest = { request ->
            assertEquals(request.url.encodedPath, "/osc/state", "request path")
            ByteReadChannel(jsonString)
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        val thetaState = thetaRepository.getThetaState()

        // check
        assertTrue(thetaState.fingerprint.isNotEmpty(), "state fingerprint")
        assertTrue(thetaState.batteryLevel > 0, "state batteryLevel")
        assertEquals(thetaState.chargingState, ThetaRepository.ChargingStateEnum.NOT_CHARGING, "state chargingState")
        assertTrue(!thetaState.isSdCard, "state isSdCard")
        assertTrue(thetaState.recordedTime >= 0, "state recordedTime")
        assertTrue(thetaState.recordableTime >= 0, "state recordableTime")
        assertTrue(thetaState.latestFileUrl.startsWith("http://"), "state latestFileUrl")
    }

    /**
     * Check setting of ThetaState.
     */
    @Test
    fun settingThetaStateTest() = runTest {
        val fingerprint = "fingerprint test"

        // Check isSdCard
        val sdOptionList = listOf(StorageOption.SD, StorageOption.IN, null)
        val sdResponseList = mutableListOf<StateApiResponse>()
        sdOptionList.forEach {
            sdResponseList.add(
                StateApiResponse(
                    fingerprint,
                    CameraState(
                        batteryLevel = 0.0,
                        _batteryState = ChargingState.CHARGED,
                        storageUri = "http://dummy",
                        _captureStatus = CaptureStatus.IDLE,
                        _recordedTime = 0,
                        _recordableTime = 0,
                        _apiVersion = 2,
                        _currentStorage = it,
                        _latestFileUrl = "http://192.168.1.1:80/dummy.JPG"
                    )
                )
            )
        }
        sdResponseList.forEachIndexed { index, response ->
            val thetaState = ThetaRepository.ThetaState(response)
            assertEquals(thetaState.isSdCard, sdOptionList[index] == StorageOption.SD, "ThetaState isSdCard")
        }

        // check ChargingStateEnum
        assertEquals(
            ThetaRepository.ChargingStateEnum.get(ChargingState.CHARGING),
            ThetaRepository.ChargingStateEnum.CHARGING,
            "ChargingStateEnum"
        )
        assertEquals(
            ThetaRepository.ChargingStateEnum.get(ChargingState.CHARGED),
            ThetaRepository.ChargingStateEnum.COMPLETED,
            "ChargingStateEnum"
        )
        assertEquals(
            ThetaRepository.ChargingStateEnum.get(ChargingState.DISCONNECT),
            ThetaRepository.ChargingStateEnum.NOT_CHARGING,
            "ChargingStateEnum"
        )
    }

    /**
     * Error not json response to getThetaState call
     */
    @Test
    fun getThetaStateNotJsonResponseTest() = runTest {
        // setup
        val jsonString = "Not json"
        MockApiClient.onRequest = { _ ->
            ByteReadChannel(jsonString)
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.getThetaState()
            assertTrue(false, "call getThetaState")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(
                e.message!!.indexOf("json", 0, true) >= 0 ||
                    e.message!!.indexOf("Illegal", 0, true) >= 0,
                "error response"
            )
        }
    }

    /**
     * Status error to getThetaState call
     */
    @Test
    fun getThetaStateStatusErrorTest() = runTest {
        // setup
        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel("status error")
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.getThetaState()
            assertTrue(false, "status error")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("503", 0, true) >= 0, "status error")
        }
    }

    /**
     * Error exception to getThetaState call
     */
    @Test
    fun getThetaStateExceptionTest() = runTest {
        // setup
        MockApiClient.onRequest = { _ ->
            throw ConnectTimeoutException("timeout")
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.getThetaState()
            assertTrue(false, "status error")
        } catch (e: ThetaRepository.NotConnectedException) {
            assertTrue(e.message!!.indexOf("time", 0, true) >= 0, "timeout exception")
        }
    }
}
