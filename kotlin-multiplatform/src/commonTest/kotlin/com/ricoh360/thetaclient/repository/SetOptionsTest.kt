package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.ALLOWED_CAPTURE_INTERVAL
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaApi
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.capture.PhotoCapture
import com.ricoh360.thetaclient.capture.ShotCountSpecifiedIntervalCapture
import com.ricoh360.thetaclient.currentTimeMillis
import com.ricoh360.thetaclient.transferred.CaptureMode
import com.ricoh360.thetaclient.transferred.CommandApiRequest
import com.ricoh360.thetaclient.transferred.ImageFilter
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.SetOptionsRequest
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalSerializationApi::class, ExperimentalCoroutinesApi::class)
class SetOptionsTest {
    private val endpoint = "http://192.168.1.1:80/"

    @BeforeTest
    fun setup() {
        MockApiClient.status = HttpStatusCode.OK
        ThetaApi.lastSetTimeConsumingOptionTime = 0
        MockApiClient.onPreviewRequest = null
        MockApiClient.onPreviewHasNextPart = null
    }

    @AfterTest
    fun teardown() {
        MockApiClient.status = HttpStatusCode.OK
        ThetaApi.lastSetTimeConsumingOptionTime = 0
        MockApiClient.onPreviewRequest = null
        MockApiClient.onPreviewHasNextPart = null
    }

    private fun checkRequest(request: HttpRequestData, options: Options) {
        val body = request.body as TextContent
        val js = Json {
            encodeDefaults = true // Encode properties with default value.
            explicitNulls = false // Don't encode properties with null value.
            ignoreUnknownKeys = true // Ignore unknown keys on decode.
        }
        val setOptionsRequest = js.decodeFromString<SetOptionsRequest>(body.text)

        // check
        assertEquals(setOptionsRequest.name, "camera.setOptions", "command name")
        assertEquals(setOptionsRequest.parameters.options, options, "options")
    }

    /**
     * call setOptions.
     */
    @Test
    fun setOptionsTest() = runTest {
        val orgOptions = Options(_shutterVolume = 100)
        val options = ThetaRepository.Options(shutterVolume = 100)

        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            checkRequest(request, orgOptions)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.setOptions(options)
        assertTrue(true, "response is normal.")
    }

    /**
     * Error not json response to setOptions call
     */
    @Test
    fun setOptionsNotJsonResponseTest() = runTest {
        MockApiClient.onRequest = { _ ->
            ByteReadChannel("Not json")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val options = ThetaRepository.Options(shutterVolume = 100)
            thetaRepository.setOptions(options)
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
     * Error response to setOptions call
     */
    @Test
    fun setOptionsErrorResponseTest() = runTest {
        MockApiClient.onRequest = { _ ->
            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_error.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val options = ThetaRepository.Options(shutterVolume = 100)
            thetaRepository.setOptions(options)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Error response and status error to setOptions call
     */
    @Test
    fun setOptionsErrorResponseAndStatusErrorTest() = runTest {
        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_error.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val options = ThetaRepository.Options(shutterVolume = 100)
            thetaRepository.setOptions(options)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Status error to setOptions call
     */
    @Test
    fun setOptionsStatusErrorTest() = runTest {
        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel("Not json")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val options = ThetaRepository.Options(shutterVolume = 100)
            thetaRepository.setOptions(options)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("503", 0, true) >= 0, "status error")
        }
    }

    /**
     * Error exception to setOptions call
     */
    @Test
    fun setOptionsExceptionTest() = runTest {
        MockApiClient.onRequest = { _ ->
            throw ConnectTimeoutException("timeout")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val options = ThetaRepository.Options(shutterVolume = 100)
            thetaRepository.setOptions(options)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.NotConnectedException) {
            assertTrue(e.message!!.indexOf("time", 0, true) >= 0, "timeout exception")
        }
    }

    private fun getRequestCommand(request: HttpRequestData): String {
        val body = request.body as TextContent
        val js = Json {
            encodeDefaults = true // Encode properties with default value.
            explicitNulls = false // Don't encode properties with null value.
            ignoreUnknownKeys = true // Ignore unknown keys on decode.
        }

        @Serializable
        data class CommandApiRequestAny(
            override val name: String,
            override val parameters: JsonObject,
        ) : CommandApiRequest

        val setOptionsRequest = js.decodeFromString<CommandApiRequestAny>(body.text)
        return setOptionsRequest.name
    }

    /**
     * Test waiting to TakePicture filter settings
     */
    @Test
    fun waitTakePictureTest() = runTest {
        var startTime: Long = 0
        MockApiClient.onRequest = { request ->
            val command = getRequestCommand(request)
            if (command == "camera.takePicture") {
                val interval = currentTimeMillis() - startTime
                assertTrue(interval >= ALLOWED_CAPTURE_INTERVAL, "interval: $interval")
            }
            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(filter = ThetaRepository.FilterEnum.HDR)
        assertEquals(ThetaApi.lastSetTimeConsumingOptionTime, 0)
        thetaRepository.setOptions(options)
        assertTrue(ThetaApi.lastSetTimeConsumingOptionTime > 0)
        startTime = ThetaApi.lastSetTimeConsumingOptionTime
        val photoCapture = thetaRepository.getPhotoCaptureBuilder()
            .build()

        // execute
        val deferred = CompletableDeferred<Unit>()
        photoCapture.takePicture(object : PhotoCapture.TakePictureCallback {
            override fun onSuccess(fileUrl: String?) {
                println("onSuccess")
                deferred.complete(Unit)
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                println("onError")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }
    }

    /**
     * Test waiting to StartCapture filter settings
     */
    @Test
    fun waitStartCaptureTest() = runTest {
        var startTime: Long = 0
        MockApiClient.onRequest = { request ->
            val command = getRequestCommand(request)
            when (command) {
                "camera.startCapture" -> {
                    val interval = currentTimeMillis() - startTime
                    assertTrue(interval >= ALLOWED_CAPTURE_INTERVAL, "interval: $interval")
                    ByteReadChannel(Resource("src/commonTest/resources/ShotCountSpecifiedIntervalCapture/start_capture_cancel.json").readText())
                }

                else -> {
                    ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
                }

            }
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(filter = ThetaRepository.FilterEnum.HDR)
        assertEquals(ThetaApi.lastSetTimeConsumingOptionTime, 0)
        thetaRepository.setOptions(options)
        assertTrue(ThetaApi.lastSetTimeConsumingOptionTime > 0)
        startTime = ThetaApi.lastSetTimeConsumingOptionTime
        val capture = thetaRepository.getShotCountSpecifiedIntervalCaptureBuilder(2)
            .setCheckStatusCommandInterval(100)
            .build()

        // execute
        val deferred = CompletableDeferred<Unit>()
        capture.startCapture(object : ShotCountSpecifiedIntervalCapture.StartCaptureCallback {
            override fun onCaptureCompleted(fileUrls: List<String>?) {
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                deferred.complete(Unit)
            }

            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }
    }

    /**
     * Test waiting to LivePreview filter settings
     */
    @Test
    fun waitLivePreviewTest() = runBlocking {
        MockApiClient.onRequest = { _ ->
            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(filter = ThetaRepository.FilterEnum.HDR)
        assertEquals(ThetaApi.lastSetTimeConsumingOptionTime, 0)
        thetaRepository.setOptions(options)
        assertTrue(ThetaApi.lastSetTimeConsumingOptionTime > 0)
        val startTime = ThetaApi.lastSetTimeConsumingOptionTime
        thetaRepository.getLivePreview {
            val interval = currentTimeMillis() - startTime
            assertTrue(interval >= ALLOWED_CAPTURE_INTERVAL, "interval: $interval")
            return@getLivePreview false
        }
    }

    /**
     * Test waiting to LivePreviewFlow filter settings
     */
    @Test
    fun waitLivePreviewFlowTest() = runTest {
        MockApiClient.onRequest = { _ ->
            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(filter = ThetaRepository.FilterEnum.HDR)
        assertEquals(ThetaApi.lastSetTimeConsumingOptionTime, 0)
        thetaRepository.setOptions(options)
        assertTrue(ThetaApi.lastSetTimeConsumingOptionTime > 0)
        val startTime = ThetaApi.lastSetTimeConsumingOptionTime
        try {
            runBlocking {
                try {
                    thetaRepository.getLivePreview().collect { byteReadPacket ->
                        val interval = currentTimeMillis() - startTime
                        assertTrue(interval >= ALLOWED_CAPTURE_INTERVAL, "interval: $interval")
                        byteReadPacket.release()
                        cancel()
                    }
                } catch (exception: Exception) {
                    assertTrue(true, "cancel job")
                }
            }
        } catch (exception: Exception) {
            assertTrue(true, "cancel job")
        }
    }

    /**
     * Set consuming option
     */
    @Test
    fun setConsumingOptionTest() = runTest {
        MockApiClient.onRequest = { _ ->
            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        assertEquals(ThetaApi.lastSetTimeConsumingOptionTime, 0)
        thetaRepository.setOptions(ThetaRepository.Options(shutterVolume = 100))
        assertEquals(ThetaApi.lastSetTimeConsumingOptionTime, 0, "normal option")

        thetaRepository.setOptions(ThetaRepository.Options(filter = ThetaRepository.FilterEnum.HDR))
        assertTrue(ThetaApi.lastSetTimeConsumingOptionTime > 0)
        ThetaApi.lastSetTimeConsumingOptionTime = 0
        thetaRepository.setOptions(ThetaRepository.Options(filter = ThetaRepository.FilterEnum.HDR))
        assertEquals(ThetaApi.lastSetTimeConsumingOptionTime, 0, "same option")
        thetaRepository.setOptions(ThetaRepository.Options(filter = ThetaRepository.FilterEnum.OFF))
        assertTrue(ThetaApi.lastSetTimeConsumingOptionTime > 0)
        assertEquals(ThetaApi.currentOptions._filter, ImageFilter.OFF, "_filter")

        thetaRepository.setOptions(ThetaRepository.Options(captureMode = ThetaRepository.CaptureModeEnum.IMAGE))
        assertTrue(ThetaApi.lastSetTimeConsumingOptionTime > 0)
        ThetaApi.lastSetTimeConsumingOptionTime = 0
        thetaRepository.setOptions(ThetaRepository.Options(captureMode = ThetaRepository.CaptureModeEnum.IMAGE))
        assertEquals(ThetaApi.lastSetTimeConsumingOptionTime, 0, "same option")
        thetaRepository.setOptions(ThetaRepository.Options(captureMode = ThetaRepository.CaptureModeEnum.VIDEO))
        assertTrue(ThetaApi.lastSetTimeConsumingOptionTime > 0)
        assertEquals(ThetaApi.currentOptions.captureMode, CaptureMode.VIDEO, "captureMode")
    }

    /**
     * Set consuming option exception
     */
    @Test
    fun setConsumingOptionExceptionTest() = runTest {
        MockApiClient.onRequest = { _ ->
            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_error.json").readText())
        }

        var thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.setOptions(ThetaRepository.Options(filter = ThetaRepository.FilterEnum.HDR))
            assertTrue(false, "response is normal.")
        } catch (_: ThetaRepository.ThetaWebApiException) {
        }
        assertEquals(ThetaApi.lastSetTimeConsumingOptionTime, 0, "no change")

        thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.setOptions(ThetaRepository.Options(captureMode = ThetaRepository.CaptureModeEnum.IMAGE))
            assertEquals(ThetaApi.lastSetTimeConsumingOptionTime, 0, "same option")
            assertTrue(false, "response is normal.")
        } catch (_: ThetaRepository.ThetaWebApiException) {
        }
        assertEquals(ThetaApi.lastSetTimeConsumingOptionTime, 0, "no change")
    }
}
