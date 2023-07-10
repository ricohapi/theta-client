package com.ricoh360.thetaclient

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.repository.UpdateFirmwareTest
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateFirmwareOnTheta {
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
    fun updateFirmwareByFileTest() = runTest(dispatchTimeoutMs = 120_000L) {
        val dir = "/media/sf_vmshare/ex" // directory firmware files exist
        val file = "bx1_v652.frm" // one of bx1_v652.frm, bx1_v641.frm
        lateinit var firmware: ByteArray
        kotlin.runCatching {
            firmware = readFile(dir, file)
        }.onFailure {
            println(it.toString())
            assertTrue(false, "updateFirmwareTest")
        }
        println("Firmware size: ${firmware.size}")

        lateinit var apiPath: String
        kotlin.runCatching {
            apiPath = System.getenv(UpdateFirmwareTest.FIRMWARE_UPDATE_API_ENV_NAME) ?: ""
        }.onFailure {
            println("${UpdateFirmwareTest.FIRMWARE_UPDATE_API_ENV_NAME} can not be accessed so updateFirmwareTest() is skipped")
            assertTrue(false, "Can't get API path")
        }
        println("API path: $apiPath")

        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, apiPath, "request path")
            ByteReadChannel(Resource("src/commonTest/resources/updateFirmware/update_firmware.done.json").readText())
        }
        val thetaRepository = ThetaRepository(endpoint)
        //thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_SC2_B
        thetaRepository.updateFirmware(apiPath, listOf(firmware), listOf(file))
        assertTrue(true, "call updateFirmware()")
    }

    fun readFile(dir: String, file: String): ByteArray {
        val path = Paths.get(dir, file)
        if(Files.exists(path)) {
            return Files.readAllBytes(path)
        } else {
            throw(IllegalArgumentException("Not exist: ${path.toString()}"))
        }
    }

    companion object {
        // Environment variable name that holds the path of Theta firmware update API.
        const val FIRMWARE_UPDATE_API_ENV_NAME= "THETA_FU_API_PATH"
    }
}