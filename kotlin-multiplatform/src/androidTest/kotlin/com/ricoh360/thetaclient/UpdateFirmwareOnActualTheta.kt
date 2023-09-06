package com.ricoh360.thetaclient

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
class UpdateFirmwareOnActualTheta {
    private val endpoint = "http://192.168.1.1:80/"

    /**
     * Test for Theta SC2 for business
     */
    @Test
    fun updateFirmwareSc2_bTest() = runTest(dispatchTimeoutMs = TIMEOUT) {
        if(MockApiClient.useMock) {
            println("updateFirmwareSc2_bTest(): This test is just for Actual Theta, so skipped")
            return@runTest
        }

        val thetaRepository = kotlin.runCatching {
            ThetaRepository.newInstance(endpoint)
        }.onFailure {
            assertTrue(false, it.toString())
        }.getOrNull()

        if (thetaRepository?.cameraModel != ThetaRepository.ThetaModel.THETA_SC2_B) {
            assertTrue(false, "Connected Theta is not SC2 for business but ${thetaRepository?.cameraModel}")
            return@runTest
        }

        val firmFile = when (thetaRepository.getThetaInfo().firmwareVersion) {
            SC2_B_VERSION_LATEST -> DIR + SC2_B_FILE_PREVIOUS
            else -> DIR + SC2_B_FILE_LATEST
        }

        val apiPath = kotlin.runCatching {
            System.getenv(UpdateFirmwareOnActualTheta.FIRMWARE_UPDATE_API_ENV_NAME)
        }.onFailure {
            assertTrue(false, "${UpdateFirmwareOnActualTheta.FIRMWARE_UPDATE_API_ENV_NAME} can not be accessed")
        }.getOrNull()
        assertNotNull(apiPath, "API path is null")
        println("API path: $apiPath")

        kotlin.runCatching {
            thetaRepository.updateFirmware(apiPath, listOf(firmFile), callback = ::getSentPercentage)
        }.onFailure {
            println(it.toString())
            assertTrue(false, it.toString())
            return@runTest
        }
        assertTrue(true, "call updateFirmware()")
    }

    fun getSentPercentage(percent: Int) {
        println("Sent $percent %")
    }

    /**
     * Test for Theta X
     * Connect Theta with client mode, then set X_SERIAL_NUMBER and URL_CL.
     */
    /*@Test
    fun updateFirmwareXTest() = runTest(dispatchTimeoutMs = TIMEOUT) {
        if(MockApiClient.useMock) {
            println("updateFirmwareXTest(): This test is just for Actual Theta, so skipped")
            return@runTest
        }

        val thetaRepository = kotlin.runCatching {
            ThetaRepository.newInstance(
                URL_CL,
                config = ThetaRepository.Config(
                    clientMode = DigestAuth(
                        USERNAME_PREFIX + X_SERIAL_NUMBER,
                        X_SERIAL_NUMBER.substring(PASSWORD_START_POSITION)
                    )
                )
            )
        }.onFailure {
            assertTrue(false, it.toString())
        }.getOrNull()

        if (thetaRepository?.cameraModel != ThetaRepository.ThetaModel.THETA_X) {
            assertTrue(false, "Connected Theta is not X but ${thetaRepository?.cameraModel}")
            return@runTest
        }

        if(thetaRepository.getThetaInfo().firmwareVersion == X_VERSION_LATEST) {
            println("Downgrade the firmware to previous version by fastboot")
            return@runTest
        }

        val apiPath = kotlin.runCatching {
            System.getenv(UpdateFirmwareOnActualTheta.FIRMWARE_UPDATE_API_ENV_NAME)
        }.onFailure {
            assertTrue(false, "${UpdateFirmwareOnActualTheta.FIRMWARE_UPDATE_API_ENV_NAME} can not be accessed")
        }.getOrNull()
        assertNotNull(apiPath, "API path is null")
        println("API path: $apiPath")

        kotlin.runCatching {
            thetaRepository.updateFirmware(apiPath, listOf(DIR + X_FILE_LATEST), callback = ::getSentPercentage)
        }.onFailure {
            assertTrue(false, it.toString())
            return@runTest
        }
        assertTrue(true, "call updateFirmware()")
    }*/

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
        const val TIMEOUT = 600_000L
        // directory firmware files exist
        val DIR = "/media/sf_vmshare/ex/"
        // lastest firmware version of SC2 for business
        const val SC2_B_VERSION_LATEST = "06.52"
        // latest firmware file of SC2 for business
        const val SC2_B_FILE_LATEST = "bx1_v652.frm"
        // previous firmware file of SC2 for business
        const val SC2_B_FILE_PREVIOUS = "bx1_v641.frm"
        // lastest firmware version of X
        const val X_VERSION_LATEST = "2.21.0"
        // latest firmware file of X
        const val X_FILE_LATEST = "cv1_v221.frm"
        //const val X_FILE_LATEST = "cv1_v220_v210.frm"

        // User name of digest authentication is X_USERNAME_PREFIX + X_SERIAL_NUMBER
        const val USERNAME_PREFIX = "THETA"
        // Password of digest authentication is the characters from 3rd character of X_SERIAL_NUMBER
        const val PASSWORD_START_POSITION = 2
        // Serial number of Theta X. Password is assumed not to be changed.
        const val X_SERIAL_NUMBER = "YR15104645"
        //const val X_SERIAL_NUMBER = "YR13000011"
        // IP address in client mode.
        const val URL_CL = "http://192.168.1.10:80/"
    }
}
