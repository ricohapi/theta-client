package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.test.*

@OptIn(ExperimentalSerializationApi::class, ExperimentalCoroutinesApi::class)
class UpdateFirmwareTest {
    private val endpoint = "http://192.168.1.1:80/"

    @BeforeTest
    fun setup() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @AfterTest
    fun teardown() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @Test
    fun updateFirmwareTest() = runTest {
        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/_writeFile", "request path")
            ByteReadChannel(Resource("src/commonTest/resources/updateFirmware/update_firmware.done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val fileContents = listOf("firmware binary 1".toByteArray(), "firmware binary 2".toByteArray())
        val fileNames = listOf("firm file 1", "firm file 2")

        kotlin.runCatching {
            thetaRepository.updateFirmware(fileContents, fileNames)
        }.onSuccess {
            assertTrue(true, "updateThetaFirmware")
        }.onFailure {
            println("updateThetaFirmware: ${it.toString()}")
            assertTrue(false, "updateThetaFirmware")
        }
    }
}
