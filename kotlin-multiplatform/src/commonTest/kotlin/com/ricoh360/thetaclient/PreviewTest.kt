package com.ricoh360.thetaclient

import com.ricoh360.thetaclient.transferred.GetLivePreviewRequest
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class PreviewTest {

    private val endpoint = "http://192.168.1.1:80/"

    @BeforeTest
    fun setup() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @AfterTest
    fun teardown() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun checkRequest(request: HttpRequestData) {
        val body = request.body as TextContent
        val js = Json {
            encodeDefaults = true // Encode properties with default value.
            explicitNulls = false // Don't encode properties with null value.
            ignoreUnknownKeys = true // Ignore unknown keys on decode.
        }
        val listFilesRequest = js.decodeFromString<GetLivePreviewRequest>(body.text)

        // check
        assertEquals(listFilesRequest.name, "camera.getLivePreview", "command name")
    }

    /*
        @Test
        fun getLivePreviewTest1() = runTest {
            MockApiClient.onRequest = { request ->
                // check request
                assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
                checkRequest(request)
                ByteReadChannel(Resource("src/commonTest/resources/getLivePreview/preview_dummy_500.txt").readText())
            }

            val READ_COUNT = 1000
            var count = 0
            ThetaApi.callGetLivePreviewCommand(endpoint).collect { byteReadPacket ->
                byteReadPacket.release()
                if(++ count == READ_COUNT) {
                    assertTrue(true, "Read ${READ_COUNT} parts")
                    this.cancel()
                }
            }
        }

        @Test
        fun getLivePreviewTest2() = runTest {
            MockApiClient.onRequest = { request ->
                // check request
                assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
                checkRequest(request)
                ByteReadChannel(Resource("src/commonTest/resources/getLivePreview/preview_dummy_500.txt").readText())
            }

            val READ_COUNT = 1000
            var count = 0
            ThetaApi.callGetLivePreviewCommand(endpoint) handler@ { byteReadPacket ->
                byteReadPacket.release()
                if(++count == READ_COUNT) {
                    assertTrue(true, "Read ${READ_COUNT} parts")
                    return@handler false
                }
                return@handler true
            }
        }
    */
}
