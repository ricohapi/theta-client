package com.ricoh360.thetaclient.repository

import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.PreviewClientException
import com.ricoh360.thetaclient.ThetaRepository
import io.ktor.client.network.sockets.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.runTest
import kotlin.test.*

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
            throw PreviewClientException("Error PreviewClientException")
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
            throw PreviewClientException("Error PreviewClientException")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val flow = thetaRepository.getLivePreview()
            flow.collect { byteReadPacket ->
                byteReadPacket.release()
            }
            assertTrue(false, "call getLivePreview")
        } catch (e: PreviewClientException) {
            assertTrue(e.message!!.indexOf("PreviewClientException") >= 0, "Exception PreviewClientException")
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
}
