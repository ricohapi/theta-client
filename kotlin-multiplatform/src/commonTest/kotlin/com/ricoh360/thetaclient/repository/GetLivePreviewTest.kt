package com.ricoh360.thetaclient.repository

import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.PreviewClient
import com.ricoh360.thetaclient.PreviewClientException
import com.ricoh360.thetaclient.ThetaRepository
import io.ktor.client.network.sockets.ConnectTimeoutException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GetLivePreviewTest {
    private val endpoint = "http://192.168.1.1:80/"

    @BeforeTest
    fun setup() {
        MockApiClient.onPreviewRequest = null
        MockApiClient.onPreviewHasNextPart = null
    }

    @AfterTest
    fun teardown() {
        MockApiClient.onPreviewRequest = null
        MockApiClient.onPreviewHasNextPart = null
    }

    /**
     * call getLivePreview to stop self..
     */
    @Test
    fun getLivePreviewStopSelfTest(): TestResult = runTest {
        var counter = 10
        MockApiClient.onPreviewHasNextPart = {
            counter--
            true
        }

        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.getLivePreview {
            return@getLivePreview counter > 0
        }
        assertTrue(counter <= 0, "call getLivePreview")
    }

    /**
     * call flow getLivePreview to stop self.
     */
    @Test
    fun flowGetLivePreviewStopSelfTest(): TestResult = runTest {
        var counter = 10
        MockApiClient.onPreviewHasNextPart = {
            counter--
            true
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            runBlocking {
                try {
                    thetaRepository.getLivePreview().collect { byteReadPacket ->
                        byteReadPacket.release()
                        if (counter <= 0) {
                            cancel()
                        }
                    }
                } catch (exception: Exception) {
                    assertTrue(true, "cancel job")
                }
            }
        } catch (exception: Exception) {
            assertTrue(true, "cancel job")
        }
        assertTrue(counter <= 0, "call getLivePreview")
    }

    /**
     * call getLivePreview to end response.
     */
    @Test
    fun getLivePreviewEndResponseTest(): TestResult = runTest {
        var counter = 10
        MockApiClient.onPreviewHasNextPart = {
            counter > 0
        }

        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.getLivePreview {
            counter--
            return@getLivePreview true
        }
        assertTrue(counter <= 0, "call getLivePreview")
    }

    /**
     * call flow getLivePreview to end response.
     */
    @Test
    fun flowGetLivePreviewEndResponseTest(): TestResult = runTest {
        var counter = 10
        MockApiClient.onPreviewHasNextPart = {
            counter > 0
        }

        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.getLivePreview().collect { byteReadPacket ->
            byteReadPacket.release()
            counter--
        }
        assertTrue(counter <= 0, "call getLivePreview")
    }

    /**
     * Error exception to request
     */
    @Test
    fun getLivePreviewExceptionRequestTest(): TestResult = runTest {
        MockApiClient.onPreviewRequest = { _, _, _, _, _ ->
            throw PreviewClientException("Can't start preview")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.getLivePreview {
                return@getLivePreview true
            }
            assertTrue(false, "call getLivePreview")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("PreviewClientException") >= 0, "Exception ThetaWebApiException")
        }
    }

    /**
     * Error exception to request
     */
    @Test
    fun flowGetLivePreviewExceptionRequestTest(): TestResult = runTest {
        MockApiClient.onPreviewRequest = { _, _, _, _, _ ->
            throw PreviewClientException("Can't start preview")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val flow = thetaRepository.getLivePreview()
            flow.collect { byteReadPacket ->
                byteReadPacket.release()
            }
            assertTrue(false, "call getLivePreview")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("PreviewClientException") >= 0, "Exception ThetaWebApiException")
        } catch (e: PreviewClientException) {
            assertTrue(e.message!!.indexOf("Can't start preview") >= 0, "Exception PreviewClientException")
        }
    }

    /**
     * Error exception to request
     */
    @Test
    fun getLivePreviewExceptionTimeoutTest(): TestResult = runTest {
        MockApiClient.onPreviewRequest = { _, _, _, _, _ ->
            throw ConnectTimeoutException("timeout")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.getLivePreview {
                return@getLivePreview true
            }
            assertTrue(false, "call getLivePreview")
        } catch (e: ThetaRepository.NotConnectedException) {
            assertTrue(e.message!!.indexOf("timeout") >= 0, "Exception NotConnectedException")
        }
    }

    /**
     * Timeout test
     */
    @Test
    fun previewClientTimeoutSettingTest(): TestResult = runTest {
        var thetaRepository = ThetaRepository(endpoint)
        assertEquals(20_000, PreviewClient.timeout.connectTimeout)
        assertEquals(20_000, PreviewClient.timeout.requestTimeout)
        assertEquals(20_000, PreviewClient.timeout.socketTimeout)

        thetaRepository = ThetaRepository(endpoint, null, ThetaRepository.Timeout(1, 2, 3))
        assertEquals(1, PreviewClient.timeout.connectTimeout)
        assertEquals(2, PreviewClient.timeout.requestTimeout)
        assertEquals(3, PreviewClient.timeout.socketTimeout)
    }

    /**
     * Port of live preview
     */
    @Test
    fun getLivePreviewPortTest(): TestResult = runTest {
        val urlWithoutPort1 = "http://192.168.1.1/"
        val urlWithoutPort2 = "http://192.168.1.1"
        val urlWithPort1 = "http://192.168.1.1:80/"
        val urlWithPort2 = "http://192.168.1.1:80"
        val tmpPort = 49788
        val urlA1Tmp1 = "http://192.168.1.1:$tmpPort/"
        val urlA1Tmp2 = "http://192.168.1.1:$tmpPort"
        val thetaRepository = ThetaRepository(urlWithoutPort1)

        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_X
        assertEquals(urlWithPort1, thetaRepository.getEndpointOfLivePreview(urlWithoutPort1), "Theta X, no port, slash")
        assertEquals(urlWithPort1, thetaRepository.getEndpointOfLivePreview(urlWithoutPort2), "Theta X, no port, no slash")
        assertEquals(urlWithPort1, thetaRepository.getEndpointOfLivePreview(urlWithPort1), "Theta X, port, slash")
        assertEquals(urlWithPort2, thetaRepository.getEndpointOfLivePreview(urlWithPort2), "Theta X, port, no slash")

        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_A1
        thetaRepository.firmwareVersion = "0.136.707"
        assertEquals(urlA1Tmp1, thetaRepository.getEndpointOfLivePreview(urlWithoutPort1), "Theta A1 0.136.707, no port, slash")
        assertEquals(urlA1Tmp1, thetaRepository.getEndpointOfLivePreview(urlWithoutPort2), "Theta A1 0.136.707, no port, no slash")
        assertEquals(urlA1Tmp1, thetaRepository.getEndpointOfLivePreview(urlWithPort1), "Theta A1 0.136.707, port, slash")
        assertEquals(urlA1Tmp2, thetaRepository.getEndpointOfLivePreview(urlWithPort2), "Theta A1 0.136.707, port, no slash")

        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_A1
        thetaRepository.firmwareVersion = "1.10.0"
        assertEquals(urlA1Tmp1, thetaRepository.getEndpointOfLivePreview(urlWithoutPort1), "Theta A1 1.10.0, no port, slash")
        assertEquals(urlA1Tmp1, thetaRepository.getEndpointOfLivePreview(urlWithoutPort2), "Theta A1 1.10.0, no port, no slash")
        assertEquals(urlA1Tmp1, thetaRepository.getEndpointOfLivePreview(urlWithPort1), "Theta A1 1.10.0, port, slash")
        assertEquals(urlA1Tmp2, thetaRepository.getEndpointOfLivePreview(urlWithPort2), "Theta A1 1.10.0, port, no slash")

        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_A1
        thetaRepository.firmwareVersion = "1.10.1"
        assertEquals(urlWithPort1, thetaRepository.getEndpointOfLivePreview(urlWithoutPort1), "Theta A1 1.10.1, no port, slash")
        assertEquals(urlWithPort1, thetaRepository.getEndpointOfLivePreview(urlWithoutPort2), "Theta A1 1.10.1, no port, no slash")
        assertEquals(urlWithPort1, thetaRepository.getEndpointOfLivePreview(urlWithPort1), "Theta A1 1.10.1, port, slash")
        assertEquals(urlWithPort2, thetaRepository.getEndpointOfLivePreview(urlWithPort2), "Theta A1 1.10.1, port, no slash")

        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_A1
        thetaRepository.firmwareVersion = "1.19.9"
        assertEquals(urlA1Tmp1, thetaRepository.getEndpointOfLivePreview(urlWithoutPort1), "Theta A1 1.19.9, no port, slash")
        assertEquals(urlA1Tmp1, thetaRepository.getEndpointOfLivePreview(urlWithoutPort2), "Theta A1 1.19.9 , no port, no slash")
        assertEquals(urlA1Tmp1, thetaRepository.getEndpointOfLivePreview(urlWithPort1), "Theta A1 1.19.9, port, slash")
        assertEquals(urlA1Tmp2, thetaRepository.getEndpointOfLivePreview(urlWithPort2), "Theta A1 1.19.9, port, no slash")

        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_A1
        thetaRepository.firmwareVersion = "1.20.0"
        assertEquals(urlWithPort1, thetaRepository.getEndpointOfLivePreview(urlWithoutPort1), "Theta A1 1.20.0, no port, slash")
        assertEquals(urlWithPort1, thetaRepository.getEndpointOfLivePreview(urlWithoutPort2), "Theta A1 1.20.0, no port, no slash")
        assertEquals(urlWithPort1, thetaRepository.getEndpointOfLivePreview(urlWithPort1), "Theta A1 1.20.0, port, slash")
        assertEquals(urlWithPort2, thetaRepository.getEndpointOfLivePreview(urlWithPort2), "Theta A1 1.20.0, port, no slash")
    }

}
