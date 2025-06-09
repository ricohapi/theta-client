package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaApi
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CaptureMode
import com.ricoh360.thetaclient.transferred.GetOptionsRequest
import com.ricoh360.thetaclient.transferred.ImageFilter
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalSerializationApi::class, ExperimentalCoroutinesApi::class)
class GetOptionsTest {
    private val endpoint = "http://192.168.1.1:80/"

    @BeforeTest
    fun setup() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @AfterTest
    fun teardown() {
        MockApiClient.status = HttpStatusCode.OK
    }

    private fun checkRequest(request: HttpRequestData, optionNames: List<String>) {
        assertEquals(request.headers.get("Cache-Control"), "no-store")
        val body = request.body as TextContent
        val js = Json {
            encodeDefaults = true // Encode properties with default value.
            explicitNulls = false // Don't encode properties with null value.
            ignoreUnknownKeys = true // Ignore unknown keys on decode.
        }
        val getOptionsRequest = js.decodeFromString<GetOptionsRequest>(body.text)

        // check
        assertEquals(getOptionsRequest.name, "camera.getOptions", "command name")
        assertEquals(getOptionsRequest.parameters.optionNames.sorted(), optionNames.sorted(), "optionNames")
    }

    /**
     * call getOptions.
     */
    @Test
    fun getOptionsTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.RemainingPictures,
            ThetaRepository.OptionNameEnum.RemainingVideoSeconds
        )
        val stringOptionNames = listOf(
            "remainingPictures",
            "remainingVideoSeconds"
        )

        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            checkRequest(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/getOptions/get_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        optionNames.forEach {
            assertNotNull(options.getValue(it), "option ${it.value}")
        }
        ThetaRepository.OptionNameEnum.entries.forEach {
            if (!optionNames.contains(it)) {
                assertNull(options.getValue(it), "option ${it.value}")
            }
        }
    }

    /**
     * call getOptions. Check duplication option names
     */
    @Test
    fun getOptionsDuplicationNameTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.RemainingPictures,
            ThetaRepository.OptionNameEnum.RemainingPictures,
            ThetaRepository.OptionNameEnum.RemainingPictures,
            ThetaRepository.OptionNameEnum.RemainingPictures,
            ThetaRepository.OptionNameEnum.RemainingVideoSeconds
        )
        val stringOptionNames = listOf(
            "remainingPictures",
            "remainingVideoSeconds"
        )

        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            checkRequest(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/getOptions/get_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        optionNames.forEach {
            assertNotNull(options.getValue(it), "option ${it.value}")
        }
        ThetaRepository.OptionNameEnum.entries.forEach {
            if (!optionNames.contains(it)) {
                assertNull(options.getValue(it), "option ${it.value}")
            }
        }
    }

    /**
     * Error not json response to getOptions call
     */
    @Test
    fun getOptionsNotJsonResponseTest() = runTest {
        MockApiClient.onRequest = { _ ->
            ByteReadChannel("Not json")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val optionNames = listOf(
                ThetaRepository.OptionNameEnum.RemainingPictures,
                ThetaRepository.OptionNameEnum.RemainingVideoSeconds
            )
            thetaRepository.getOptions(optionNames)
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
     * Error response to getOptions call
     */
    @Test
    fun getOptionsErrorResponseTest() = runTest {
        MockApiClient.onRequest = { _ ->
            ByteReadChannel(Resource("src/commonTest/resources/getOptions/get_options_error.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val optionNames = listOf(
                ThetaRepository.OptionNameEnum.RemainingPictures,
                ThetaRepository.OptionNameEnum.RemainingVideoSeconds
            )
            thetaRepository.getOptions(optionNames)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Error response and status error to getOptions call
     */
    @Test
    fun getOptionsErrorResponseAndStatusErrorTest() = runTest {
        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel(Resource("src/commonTest/resources/getOptions/get_options_error.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val optionNames = listOf(
                ThetaRepository.OptionNameEnum.RemainingPictures,
                ThetaRepository.OptionNameEnum.RemainingVideoSeconds
            )
            thetaRepository.getOptions(optionNames)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Status error to getOptions call
     */
    @Test
    fun getOptionsStatusErrorTest() = runTest {
        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel("Not json")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val optionNames = listOf(
                ThetaRepository.OptionNameEnum.RemainingPictures,
                ThetaRepository.OptionNameEnum.RemainingVideoSeconds
            )
            thetaRepository.getOptions(optionNames)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("503", 0, true) >= 0, "status error")
        }
    }

    /**
     * Error exception to getOptions call
     */
    @Test
    fun getOptionsExceptionTest() = runTest {
        MockApiClient.onRequest = { _ ->
            throw ConnectTimeoutException("timeout")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            val optionNames = listOf(
                ThetaRepository.OptionNameEnum.RemainingPictures,
                ThetaRepository.OptionNameEnum.RemainingVideoSeconds
            )
            thetaRepository.getOptions(optionNames)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.NotConnectedException) {
            assertTrue(e.message!!.indexOf("time", 0, true) >= 0, "timeout exception")
        }
    }

    /**
     * Get consuming option
     */
    @Test
    fun getConsumingOptionTest() = runTest {
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/getOptions/get_options_filter_hdr.json").readText(),
            Resource("src/commonTest/resources/getOptions/get_options_filter_off.json").readText(),
            Resource("src/commonTest/resources/getOptions/get_options_capture_mode_image.json").readText(),
            Resource("src/commonTest/resources/getOptions/get_options_capture_mode_video.json").readText(),
        )
        var counter = 0
        MockApiClient.onRequest = { _ ->
            val index = counter++
            ByteReadChannel(responseArray[index])
        }

        val thetaRepository = ThetaRepository(endpoint)
        assertNull(ThetaApi.currentOptions._filter, "_filter")
        thetaRepository.getOptions(listOf(ThetaRepository.OptionNameEnum.Filter))
        assertEquals(ThetaApi.currentOptions._filter, ImageFilter.HDR, "_filter")
        thetaRepository.getOptions(listOf(ThetaRepository.OptionNameEnum.Filter))
        assertEquals(ThetaApi.currentOptions._filter, ImageFilter.OFF, "_filter")
        assertEquals(ThetaApi.lastSetTimeConsumingOptionTime, 0, "_filter")

        assertNull(ThetaApi.currentOptions.captureMode, "captureMode")
        thetaRepository.getOptions(listOf(ThetaRepository.OptionNameEnum.CameraMode))
        assertEquals(ThetaApi.currentOptions.captureMode, CaptureMode.IMAGE, "captureMode")
        thetaRepository.getOptions(listOf(ThetaRepository.OptionNameEnum.CameraMode))
        assertEquals(ThetaApi.currentOptions.captureMode, CaptureMode.VIDEO, "captureMode")
        assertEquals(ThetaApi.lastSetTimeConsumingOptionTime, 0, "captureMode")
    }

    /**
     * Get consuming option exception
     */
    @Test
    fun getConsumingOptionExceptionTest() = runTest {
        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel(Resource("src/commonTest/resources/getOptions/get_options_error.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.getOptions(listOf(ThetaRepository.OptionNameEnum.Filter))
            assertTrue(false, "response is normal.")
        } catch (_: ThetaRepository.ThetaWebApiException) {
        }
        assertNull(ThetaApi.currentOptions._filter, "_filter")
        assertEquals(ThetaApi.lastSetTimeConsumingOptionTime, 0, "_filter")

        try {
            thetaRepository.getOptions(listOf(ThetaRepository.OptionNameEnum.CaptureMode))
            assertTrue(false, "response is normal.")
        } catch (_: ThetaRepository.ThetaWebApiException) {
        }
        assertNull(ThetaApi.currentOptions.captureMode, "captureMode")
        assertEquals(ThetaApi.lastSetTimeConsumingOptionTime, 0, "captureMode")
    }
}
