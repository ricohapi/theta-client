package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.ConvertVideoFormatsRequest
import com.ricoh360.thetaclient.transferred.TopBottomCorrection
import com.ricoh360.thetaclient.transferred.VideoFormat
import com.ricoh360.thetaclient.transferred._ProjectionType
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
import kotlin.test.assertTrue

@OptIn(ExperimentalSerializationApi::class, ExperimentalCoroutinesApi::class)
class ConvertVideoFormatsTest {
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
        fileUrl: String,
        format: VideoFormat?,
        projectionType: _ProjectionType?,
        codec: String?,
        topBottomCorrection: TopBottomCorrection?
    ) {
        val body = request.body as TextContent
        val js = Json {
            encodeDefaults = true // Encode properties with default value.
            explicitNulls = false // Don't encode properties with null value.
            ignoreUnknownKeys = true // Ignore unknown keys on decode.
        }
        val convertVideoFormatsRequest = js.decodeFromString<ConvertVideoFormatsRequest>(body.text)

        // check
        assertEquals(request.url.encodedPath, "/osc/commands/execute", "command request path")
        assertEquals(convertVideoFormatsRequest.name, "camera._convertVideoFormats", "command name")
        assertEquals(convertVideoFormatsRequest.parameters.fileUrl, fileUrl, "convertVideoFormats fileUrl")
        assertEquals(convertVideoFormatsRequest.parameters.size, format, "convertVideoFormats format")
        assertEquals(convertVideoFormatsRequest.parameters.projectionType, projectionType, "convertVideoFormats projectionType")
        assertEquals(convertVideoFormatsRequest.parameters.codec, codec, "convertVideoFormats codec")
        assertEquals(convertVideoFormatsRequest.parameters.topBottomCorrection, topBottomCorrection, "convertVideoFormats topBottomCorrection")
    }

    /**
     * Execute convertVideoFormats normal test.
     */
    private suspend fun checkCallConvertVideoFormats(
        cameraModel: ThetaRepository.ThetaModel,
        toLowResolution: Boolean,
        applyTopBottomCorrection: Boolean,
        format: VideoFormat?,
        projectionType: _ProjectionType?,
        codec: String?,
        topBottomCorrection: TopBottomCorrection?,
        noConvert: Boolean
    ) {
        print("Camera model: ${cameraModel.value}\n")

        val fileUrl = "http://dummy.MP4"
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/convertVideoFormats/convert_video_formats_progress.json").readText(),
            Resource("src/commonTest/resources/convertVideoFormats/convert_video_formats_done.json").readText()
        )
        val requestPathArray = arrayOf(
            "/osc/commands/execute",
            "/osc/commands/status"
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            assertTrue(!noConvert, "No convert")
            val index = counter++

            // check request
            assertEquals(request.url.encodedPath, requestPathArray[index], "request status")
            when (index) {
                0 -> {
                    checkRequest(request, fileUrl, format!!, projectionType, codec, topBottomCorrection)
                }
            }

            ByteReadChannel(responseArray[index])
        }

        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = cameraModel
        val response = thetaRepository.convertVideoFormats(fileUrl, toLowResolution, applyTopBottomCorrection)
        if (noConvert) {
            assertEquals(response, fileUrl, "No convert")
        } else {
            assertTrue(response != fileUrl, "convert file url")
            assertTrue(response.startsWith("http://"), "convert file url")
        }
    }

    /**
     * call convertVideoFormats for camera model.
     */
    @Test
    fun convertVideoFormatsTest() = runTest {
        val codec = "H.264/MPEG-4 AVC"

        checkCallConvertVideoFormats(
            cameraModel = ThetaRepository.ThetaModel.THETA_Z1,
            toLowResolution = false,
            applyTopBottomCorrection = true,
            format = VideoFormat.VIDEO_4K,
            projectionType = _ProjectionType.EQUIRECTANGULAR,
            codec = codec,
            topBottomCorrection = TopBottomCorrection.APPLY,
            noConvert = false
        )
        checkCallConvertVideoFormats(
            cameraModel = ThetaRepository.ThetaModel.THETA_Z1,
            toLowResolution = true,
            applyTopBottomCorrection = false,
            format = VideoFormat.VIDEO_2K,
            projectionType = _ProjectionType.EQUIRECTANGULAR,
            codec = codec,
            topBottomCorrection = TopBottomCorrection.DISAPPLY,
            noConvert = false
        )

        checkCallConvertVideoFormats(
            cameraModel = ThetaRepository.ThetaModel.THETA_X,
            toLowResolution = true,
            applyTopBottomCorrection = true,
            format = VideoFormat.VIDEO_4K,
            projectionType = null,
            codec = null,
            topBottomCorrection = null,
            noConvert = false
        )
        checkCallConvertVideoFormats(
            cameraModel = ThetaRepository.ThetaModel.THETA_X,
            toLowResolution = false,
            applyTopBottomCorrection = false,
            format = null,
            projectionType = null,
            codec = null,
            topBottomCorrection = null,
            noConvert = true
        )

        checkCallConvertVideoFormats(
            cameraModel = ThetaRepository.ThetaModel.THETA_S,
            toLowResolution = false,
            applyTopBottomCorrection = true,
            format = null,
            projectionType = null,
            codec = null,
            topBottomCorrection = null,
            noConvert = true
        )

        checkCallConvertVideoFormats(
            cameraModel = ThetaRepository.ThetaModel.THETA_SC,
            toLowResolution = false,
            applyTopBottomCorrection = true,
            format = null,
            projectionType = null,
            codec = null,
            topBottomCorrection = null,
            noConvert = true
        )

        checkCallConvertVideoFormats(
            cameraModel = ThetaRepository.ThetaModel.THETA_V,
            toLowResolution = false,
            applyTopBottomCorrection = true,
            format = VideoFormat.VIDEO_4K,
            projectionType = _ProjectionType.EQUIRECTANGULAR,
            codec = codec,
            topBottomCorrection = TopBottomCorrection.APPLY,
            noConvert = false
        )
        checkCallConvertVideoFormats(
            cameraModel = ThetaRepository.ThetaModel.THETA_V,
            toLowResolution = true,
            applyTopBottomCorrection = false,
            format = VideoFormat.VIDEO_2K,
            projectionType = _ProjectionType.EQUIRECTANGULAR,
            codec = codec,
            topBottomCorrection = TopBottomCorrection.DISAPPLY,
            noConvert = false
        )
    }

    /**
     * call convertVideoFormats. If the done is returned immediately after commands/execute.
     */
    @Test
    fun convertVideoFormatsDoneImmediatelyTest() = runTest {
        val fileUrl = "http://dummy.MP4"
        // setup

        MockApiClient.onRequest = { _ ->
            ByteReadChannel(Resource("src/commonTest/resources/convertVideoFormats/convert_video_formats_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val response = thetaRepository.convertVideoFormats(
            fileUrl,
            toLowResolution = false,
            applyTopBottomCorrection = true
        )

        assertTrue(response.startsWith("http://"), "convert file url")
    }

    /**
     * Error response to convertVideoFormats call
     */
    @Test
    fun convertVideoFormatsErrorTest() = runTest {
        val fileUrl = "http://dummy.MP4"
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/convertVideoFormats/convert_video_formats_progress.json").readText(),
            Resource("src/commonTest/resources/convertVideoFormats/convert_video_formats_error.json").readText()
        )
        var counter = 0
        MockApiClient.onRequest = { _ ->
            val index = counter++
            ByteReadChannel(responseArray[index])
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.convertVideoFormats(
                fileUrl,
                toLowResolution = false,
                applyTopBottomCorrection = true
            )
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Error response to convertVideoFormats call immediately after commands/execute
     */
    @Test
    fun convertVideoFormatsErrorImmediatelyTest() = runTest {
        val fileUrl = "http://dummy.MP4"
        // setup
        MockApiClient.onRequest = { _ ->
            ByteReadChannel(Resource("src/commonTest/resources/convertVideoFormats/convert_video_formats_error.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.convertVideoFormats(
                fileUrl,
                toLowResolution = false,
                applyTopBottomCorrection = true
            )
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Error not json response to convertVideoFormats call
     */
    @Test
    fun convertVideoFormatsNotJsonResponseTest() = runTest {
        val fileUrl = "http://dummy.MP4"
        // setup
        MockApiClient.onRequest = { _ ->
            ByteReadChannel("Not json")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.convertVideoFormats(
                fileUrl,
                toLowResolution = false,
                applyTopBottomCorrection = true
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
     * Error response and status error to convertVideoFormats call
     */
    @Test
    fun convertVideoFormatsResponseAndStatusErrorTest() = runTest {
        val fileUrl = "http://dummy.MP4"
        // setup
        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel(Resource("src/commonTest/resources/convertVideoFormats/convert_video_formats_error.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.convertVideoFormats(
                fileUrl,
                toLowResolution = false,
                applyTopBottomCorrection = true
            )
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Status error to convertVideoFormats call
     */
    @Test
    fun convertVideoFormatsStatusErrorTest() = runTest {
        val fileUrl = "http://dummy.MP4"
        // setup
        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel("Not json")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.convertVideoFormats(
                fileUrl,
                toLowResolution = false,
                applyTopBottomCorrection = true
            )
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("503", 0, true) >= 0, "status error")
        }
    }

    /**
     * Error exception to convertVideoFormats call
     */
    @Test
    fun convertVideoFormatsExceptionTest() = runTest {
        val fileUrl = "http://dummy.MP4"
        // setup
        MockApiClient.onRequest = { _ ->
            throw ConnectTimeoutException("timeout")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.convertVideoFormats(
                fileUrl,
                toLowResolution = false,
                applyTopBottomCorrection = true
            )
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.NotConnectedException) {
            assertTrue(e.message!!.indexOf("time", 0, true) >= 0, "timeout exception")
        }
    }
}
