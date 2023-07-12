package com.ricoh360.thetaclient

import android.os.strictmode.DiskReadViolation
import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.repository.UpdateFirmwareTest
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.*

/**
 * Tests for update firmware with actual Theta.
 * If using a mock, all tests are skipped.
 * Prerequisites:
 *   - Set the directory, where firmware files exist, to constant DIR
 *   - Put firmware files on DIR
 *   - Set the path of Theta firmware update API to environment variable THETA_FU_API_PATH
 */
@OptIn(ExperimentalCoroutinesApi::class)
class UpdateFirmwareOnTheta {
    private val endpoint = "http://192.168.1.1:80/"

    @Test
    fun updateFirmwareSc2_bTest() = runTest(dispatchTimeoutMs = TIMEOUT) {
        if(MockApiClient.useMock) {
            println("updateFirmwareSc2_bTest(): This test is just for Actual Theta, so skipped")
            return@runTest
        }

        val thetaRepository = kotlin.runCatching {
            ThetaRepository.newInstance(
                endpoint,
                timeout = ThetaRepository.Timeout(requestTimeout = TIMEOUT, socketTimeout = TIMEOUT)
            )
        }.onFailure {
            assertTrue(false, it.toString())
        }.getOrNull()

        if (thetaRepository?.cameraModel != ThetaRepository.ThetaModel.THETA_SC2_B) {
            assertTrue(false, "Connected Theta is not SC2 for business but ${thetaRepository?.cameraModel}")
            return@runTest
        }

        val firmFile = when (thetaRepository.getThetaInfo().firmwareVersion) {
            VERSION_LATEST -> FILE_PREVIOUS
            else -> FILE_LATEST
        }

        val firmware: ByteArray? = kotlin.runCatching {
            readFile(DIR, firmFile)
        }.onFailure {
            assertTrue(false, it.toString())
        }.getOrNull()
        println("Firmware size: ${firmware?.size}")

        val apiPath = kotlin.runCatching {
            System.getenv(UpdateFirmwareTest.FIRMWARE_UPDATE_API_ENV_NAME)
        }.onFailure {
            assertTrue(false, "${UpdateFirmwareTest.FIRMWARE_UPDATE_API_ENV_NAME} can not be accessed")
        }.getOrNull()
        assertNotNull(apiPath, "API path is null")
        println("API path: $apiPath")

        kotlin.runCatching {
            thetaRepository.updateFirmware(apiPath, listOf(firmware!!), listOf(firmFile))
        }.onFailure {
            assertTrue(false, it.toString())
            return@runTest
        }
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
        // Timeout for socket and coroutine
        const val TIMEOUT = 300_000L
        // lastest firmware version of SC2 for business
        const val VERSION_LATEST = "06.52"
        // latest firmware file of SC2 for business
        const val FILE_LATEST = "bx1_v652.frm"
        // previous firmware file of SC2 for business
        const val FILE_PREVIOUS = "bx1_v641.frm"
        // directory firmware files exist
        val DIR = "/media/sf_vmshare/ex"
    }
}
