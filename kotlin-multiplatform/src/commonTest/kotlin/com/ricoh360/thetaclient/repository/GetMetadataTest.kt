package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.GetMetadataRequest
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalSerializationApi::class, ExperimentalCoroutinesApi::class)
class GetMetadataTest {
    private val endpoint = "http://192.168.1.1:80/"

    @BeforeTest
    fun setup() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @AfterTest
    fun teardown() {
        MockApiClient.status = HttpStatusCode.OK
    }

    private fun checkRequest(request: HttpRequestData, fileUrl: String) {
        val body = request.body as TextContent
        val js = Json {
            encodeDefaults = true // Encode properties with default value.
            explicitNulls = false // Don't encode properties with null value.
            ignoreUnknownKeys = true // Ignore unknown keys on decode.
        }
        val metadataRequest = js.decodeFromString<GetMetadataRequest>(body.text)

        // check
        assertEquals(metadataRequest.name, "camera._getMetadata", "command name")
        assertEquals(metadataRequest.parameters.fileUrl, fileUrl, "getMetadata fileUrl")
    }

    /**
     * call getMetadata. Response is full data.
     */
    @Test
    fun getMetadataFullDataTest() = runTest {
        val fileUrl = "http://file01.JPG"

        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            checkRequest(request, fileUrl)

            ByteReadChannel(Resource("src/commonTest/resources/getMetadata/metadata_full_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val response = thetaRepository.getMetadata(fileUrl)

        assertTrue(response.first.exifVersion.isNotEmpty(), "exif exifVersion")
        assertTrue(response.first.dateTime.isNotEmpty(), "exif dateTime")
        assertTrue(response.first.imageWidth!! > 0, "exif imageWidth")
        assertTrue(response.first.imageLength!! > 0, "exif imageLength")
        assertTrue(response.first.gpsLatitude!! > 0, "exif gpsLatitude")
        assertTrue(response.first.gpsLongitude!! > 0, "exif gpsLongitude")
        assertTrue(response.second.poseHeadingDegrees!! > 0, "xmp poseHeadingDegrees")
        assertTrue(response.second.fullPanoWidthPixels > 0, "xmp fullPanoWidthPixels")
        assertTrue(response.second.fullPanoHeightPixels > 0, "xmp fullPanoHeightPixels")
    }

    /**
     * call getMetadata. Response is minimum data.
     */
    @Test
    fun getMetadataMinimumDataTest() = runTest {
        val fileUrl = "http://file01.JPG"

        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            checkRequest(request, fileUrl)

            ByteReadChannel(Resource("src/commonTest/resources/getMetadata/metadata_minimum_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val response = thetaRepository.getMetadata(fileUrl)

        assertTrue(response.first.exifVersion.isNotEmpty(), "exif exifVersion")
        assertTrue(response.first.dateTime.isNotEmpty(), "exif dateTime")
        assertNull(response.first.imageWidth, "exif imageWidth")
        assertNull(response.first.imageLength, "exif imageLength")
        assertNull(response.first.gpsLatitude, "exif gpsLatitude")
        assertNull(response.first.gpsLongitude, "exif gpsLongitude")
        assertNull(response.second.poseHeadingDegrees, "xmp poseHeadingDegrees")
        assertTrue(response.second.fullPanoWidthPixels > 0, "xmp fullPanoWidthPixels")
        assertTrue(response.second.fullPanoHeightPixels > 0, "xmp fullPanoHeightPixels")
    }

    /**
     * Error not json response to getMetadata call
     */
    @Test
    fun getMetadataNotJsonResponseTest() = runTest {
        val fileUrl = "http://file01.JPG"

        MockApiClient.onRequest = { _ ->
            ByteReadChannel("Not json")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.getMetadata(fileUrl)
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
     * Error response to getMetadata call
     */
    @Test
    fun getMetadataErrorResponseTest() = runTest {
        val fileUrl = "http://file01.JPG"

        MockApiClient.onRequest = { _ ->
            ByteReadChannel(Resource("src/commonTest/resources/getMetadata/metadata_error.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.getMetadata(fileUrl)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Error response and status error to getMetadata call
     */
    @Test
    fun getMetadataErrorResponseAndStatusErrorTest() = runTest {
        val fileUrl = "http://file01.JPG"

        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel(Resource("src/commonTest/resources/getMetadata/metadata_error.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.getMetadata(fileUrl)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Status error to getMetadata call
     */
    @Test
    fun getMetadataStatusErrorTest() = runTest {
        val fileUrl = "http://file01.JPG"

        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel("Not json")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.getMetadata(fileUrl)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("503", 0, true) >= 0, "status error")
        }
    }

    /**
     * Error exception to getMetadata call
     */
    @Test
    fun getMetadataExceptionTest() = runTest {
        val fileUrl = "http://file01.JPG"

        MockApiClient.onRequest = { _ ->
            throw ConnectTimeoutException("timeout")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.getMetadata(fileUrl)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.NotConnectedException) {
            assertTrue(e.message!!.indexOf("time", 0, true) >= 0, "timeout exception")
        }
    }
}
