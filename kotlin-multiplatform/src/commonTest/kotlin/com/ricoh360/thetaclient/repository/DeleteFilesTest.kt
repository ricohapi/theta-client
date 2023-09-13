package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.DeleteRequest
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
import kotlin.test.assertTrue

@OptIn(ExperimentalSerializationApi::class, ExperimentalCoroutinesApi::class)
class DeleteFilesTest {
    private val endpoint = "http://192.168.1.1:80/"

    @BeforeTest
    fun setup() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @AfterTest
    fun teardown() {
        MockApiClient.status = HttpStatusCode.OK
    }

    private fun checkRequest(request: HttpRequestData, fileUrls: List<String>) {
        val body = request.body as TextContent
        val js = Json {
            encodeDefaults = true // Encode properties with default value.
            explicitNulls = false // Don't encode properties with null value.
            ignoreUnknownKeys = true // Ignore unknown keys on decode.
        }
        val deleteRequest = js.decodeFromString<DeleteRequest>(body.text)

        // check
        assertEquals(deleteRequest.name, "camera.delete", "command name")
        assertEquals(deleteRequest.parameters.fileUrls, fileUrls, "delete fileUrls")
    }

    /**
     * call deleteFiles.
     */
    @Test
    fun deleteFilesTest() = runTest {
        val fileUrls = listOf(
            "http://file01.JPG",
            "http://file02.JPG",
            "http://file03.JPG"
        )

        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            checkRequest(request, fileUrls)

            ByteReadChannel(Resource("src/commonTest/resources/delete/delete_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.deleteFiles(fileUrls)
    }

    /**
     * Error not json response to deleteFiles call
     */
    @Test
    fun deleteFilesNotJsonResponseTest() = runTest {
        val fileUrls = listOf(
            "http://file01.JPG",
            "http://file02.JPG",
            "http://file03.JPG"
        )

        MockApiClient.onRequest = { _ ->
            ByteReadChannel("Not json")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.deleteFiles(fileUrls)
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
     * Error response to deleteFiles call
     */
    @Test
    fun deleteFilesErrorResponseTest() = runTest {
        val fileUrls = listOf(
            "http://file01.JPG",
            "http://file02.JPG",
            "http://file03.JPG"
        )

        MockApiClient.onRequest = { _ ->
            ByteReadChannel(Resource("src/commonTest/resources/delete/delete_error.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.deleteFiles(fileUrls)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Error response and status error to deleteFiles call
     */
    @Test
    fun deleteFilesErrorResponseAndStatusErrorTest() = runTest {
        val fileUrls = listOf(
            "http://file01.JPG",
            "http://file02.JPG",
            "http://file03.JPG"
        )

        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel(Resource("src/commonTest/resources/delete/delete_error.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.deleteFiles(fileUrls)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Status error to deleteFiles call
     */
    @Test
    fun deleteFilesStatusErrorTest() = runTest {
        val fileUrls = listOf(
            "http://file01.JPG",
            "http://file02.JPG",
            "http://file03.JPG"
        )

        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel("Not json")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.deleteFiles(fileUrls)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("503", 0, true) >= 0, "status error")
        }
    }

    /**
     * Error exception to deleteFiles call
     */
    @Test
    fun deleteFilesExceptionTest() = runTest {
        val fileUrls = listOf(
            "http://file01.JPG",
            "http://file02.JPG",
            "http://file03.JPG"
        )

        MockApiClient.onRequest = { _ ->
            throw ConnectTimeoutException("timeout")
        }

        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.deleteFiles(fileUrls)
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.NotConnectedException) {
            assertTrue(e.message!!.indexOf("time", 0, true) >= 0, "timeout exception")
        }
    }

    /**
     * call deleteAllFiles.
     */
    @Test
    fun deleteAllFilesTest() = runTest {
        val fileUrls = listOf(
            "all"
        )

        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            checkRequest(request, fileUrls)

            ByteReadChannel(Resource("src/commonTest/resources/delete/delete_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.deleteAllFiles()
    }

    /**
     * call deleteAllImageFiles.
     */
    @Test
    fun deleteAllImageFilesTest() = runTest {
        val fileUrls = listOf(
            "image"
        )

        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            checkRequest(request, fileUrls)

            ByteReadChannel(Resource("src/commonTest/resources/delete/delete_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.deleteAllImageFiles()
    }

    /**
     * call deleteAllVideoFiles.
     */
    @Test
    fun deleteAllVideoFilesTest() = runTest {
        val fileUrls = listOf(
            "video"
        )

        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            checkRequest(request, fileUrls)

            ByteReadChannel(Resource("src/commonTest/resources/delete/delete_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.deleteAllVideoFiles()
    }
}
