package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.FileType
import com.ricoh360.thetaclient.transferred.ListFilesRequest
import io.ktor.client.network.sockets.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ListFilesTest {
    private val endpoint = "http://dummy/"

    @BeforeTest
    fun setup() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @AfterTest
    fun teardown() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun checkRequest(request: HttpRequestData, fileType: FileType, startPosition: Int, entryCount: Int) {
        val body = request.body as TextContent
        val js = Json {
            encodeDefaults = true // Encode properties with default value.
            explicitNulls = false // Don't encode properties with null value.
            ignoreUnknownKeys = true // Ignore unknown keys on decode.
        }
        val listFilesRequest = js.decodeFromString<ListFilesRequest>(body.text)

        // check
        assertEquals(listFilesRequest.name, "camera.listFiles", "command name")
        assertEquals(listFilesRequest.parameters.fileType, fileType, "fileType")
        assertEquals(listFilesRequest.parameters.startPosition, startPosition, "startPosition")
        assertEquals(listFilesRequest.parameters.entryCount, entryCount, "entryCount")
    }

    /**
     * call listFiles with all.
     */
    @Test
    fun listFilesAllFilesTest() = runTest {
        val fileType = ThetaRepository.FileTypeEnum.ALL
        val startPosition = 0
        val entryCount = 10

        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            checkRequest(request, fileType.value, startPosition, entryCount)

            ByteReadChannel(Resource("src/commonTest/resources/listFiles/listfiles_all_50.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val response = thetaRepository.listFiles(fileType, startPosition, entryCount)
        response.forEach {
            assertTrue(it.name.endsWith(".JPG") || it.name.endsWith(".MP4"), "FileInfo name")
            assertTrue(!it.name.startsWith("http://"), "FileInfo name not url")
            assertTrue(it.fileUrl.startsWith("http://"), "FileInfo fileUrl")
            assertTrue(it.dateTime.indexOf("+", 0) < 0, "FileInfo dateTime timezone")
            assertTrue(it.dateTime.length == 16, "FileInfo dateTime length")
            assertTrue(it.size >= 0, "FileInfo size")
            assertEquals(it.thumbnailUrl, it.fileUrl + "?type=thumb", "FileInfo thumbnailUrl")
        }
        assertTrue(response.isNotEmpty(), "entryCount")
    }

    /**
     * call listFiles with image.
     */
    @Test
    fun listFilesImageFilesTest() = runTest {
        val fileType = ThetaRepository.FileTypeEnum.IMAGE
        val startPosition = 0
        val entryCount = 10

        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            checkRequest(request, fileType.value, startPosition, entryCount)

            ByteReadChannel(Resource("src/commonTest/resources/listFiles/listfiles_image_10.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val response = thetaRepository.listFiles(fileType, startPosition, entryCount)
        response.forEach {
            assertTrue(it.name.endsWith(".JPG"), "FileInfo name")
            assertTrue(!it.name.startsWith("http://"), "FileInfo name not url")
            assertTrue(it.fileUrl.startsWith("http://"), "FileInfo fileUrl")
            assertTrue(it.dateTime.indexOf("+", 0) < 0, "FileInfo dateTime timezone")
            assertTrue(it.dateTime.length == 16, "FileInfo dateTime length")
            assertTrue(it.size >= 0, "FileInfo size")
            assertEquals(it.thumbnailUrl, it.fileUrl + "?type=thumb", "FileInfo thumbnailUrl")
        }
        assertTrue(response.isNotEmpty(), "entryCount")
    }

    /**
     * call listFiles with video.
     */
    @Test
    fun listFilesVideoFilesTest() = runTest {
        val fileType = ThetaRepository.FileTypeEnum.VIDEO
        val startPosition = 0
        val entryCount = 10

        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            checkRequest(request, fileType.value, startPosition, entryCount)

            ByteReadChannel(Resource("src/commonTest/resources/listFiles/listfiles_video_10.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val response = thetaRepository.listFiles(fileType, startPosition, entryCount)
        response.forEach {
            assertTrue(it.name.endsWith(".MP4"), "FileInfo name")
            assertTrue(!it.name.startsWith("http://"), "FileInfo name not url")
            assertTrue(it.fileUrl.startsWith("http://"), "FileInfo fileUrl")
            assertTrue(it.dateTime.indexOf("+", 0) < 0, "FileInfo dateTime timezone")
            assertTrue(it.dateTime.length == 16, "FileInfo dateTime length")
            assertTrue(it.size >= 0, "FileInfo size")
            assertEquals(it.thumbnailUrl, it.fileUrl + "?type=thumb", "FileInfo thumbnailUrl")
        }
        assertTrue(response.isNotEmpty(), "entryCount")
    }

    /**
     * call listFiles zero entries.
     */
    @Test
    fun listFilesZeroFilesTest() = runTest {
        val fileType = ThetaRepository.FileTypeEnum.ALL
        val startPosition = 0
        val entryCount = 0

        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            checkRequest(request, fileType.value, startPosition, entryCount)

            ByteReadChannel(Resource("src/commonTest/resources/listFiles/listfiles_zero_entries.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val response = thetaRepository.listFiles(fileType, startPosition, entryCount)
        assertTrue(response.isEmpty(), "entryCount")
    }

    /**
     * Error not json response to listFiles call
     */
    @Test
    fun listFilesNotJsonResponseTest() = runTest {
        val fileType = ThetaRepository.FileTypeEnum.ALL
        val startPosition = 0
        val entryCount = 0

        MockApiClient.onRequest = { _ ->
            ByteReadChannel("Not json")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.listFiles(fileType, startPosition, entryCount)
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
     * Error response to listFiles call
     */
    @Test
    fun listFilesErrorResponseTest() = runTest {
        val fileType = ThetaRepository.FileTypeEnum.ALL
        val startPosition = 0
        val entryCount = 0

        MockApiClient.onRequest = { _ ->
            ByteReadChannel(Resource("src/commonTest/resources/listFiles/listfiles_error.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.listFiles(fileType, startPosition, entryCount)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Error response and status error to listFiles call
     */
    @Test
    fun listFilesErrorResponseAndStatusErrorTest() = runTest {
        val fileType = ThetaRepository.FileTypeEnum.ALL
        val startPosition = 0
        val entryCount = 0

        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel(Resource("src/commonTest/resources/listFiles/listfiles_error.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.listFiles(fileType, startPosition, entryCount)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Status error to listFiles call
     */
    @Test
    fun listFilesStatusErrorTest() = runTest {
        val fileType = ThetaRepository.FileTypeEnum.ALL
        val startPosition = 0
        val entryCount = 0

        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel("Not json")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.listFiles(fileType, startPosition, entryCount)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("503", 0, true) >= 0, "status error")
        }
    }

    /**
     * Error exception to listFiles call
     */
    @Test
    fun listFilesExceptionTest() = runTest {
        val fileType = ThetaRepository.FileTypeEnum.ALL
        val startPosition = 0
        val entryCount = 0

        MockApiClient.onRequest = { _ ->
            throw ConnectTimeoutException("timeout")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.listFiles(fileType, startPosition, entryCount)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.NotConnectedException) {
            assertTrue(e.message!!.indexOf("time", 0, true) >= 0, "timeout exception")
        }
    }
}
