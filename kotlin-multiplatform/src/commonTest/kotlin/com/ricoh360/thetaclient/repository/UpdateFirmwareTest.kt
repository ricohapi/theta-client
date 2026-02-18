package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.getEnv
import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.test.*

/**
 * This test uses a non-public API.
 * To execute this test, you have to set following environment variable.
 *    $ export THETA_FU_API_PATH=firmware_update_API_path
 * On iOS, set to Info.plist.
 * If the variable is not set, this test is skipped.
 *
 */
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
        var apiPath: String? = null
        kotlin.runCatching {
            apiPath = getEnv(FIRMWARE_UPDATE_API_ENV_NAME)
        }.onFailure {
            println("$FIRMWARE_UPDATE_API_ENV_NAME can not be accessed so updateFirmwareTest() is skipped")
        }
        apiPath ?: let {
            println("$FIRMWARE_UPDATE_API_ENV_NAME is not set so updateFirmwareTest() is skipped")
            return@runTest
        }
        println("apiPath: $apiPath")
        MockApiClient.onMultipartPostRequest = { _, _, _, _ ->
            Resource("src/commonTest/resources/updateFirmware/update_firmware.done.json").readText().toByteArray()
        }

        val thetaRepository = ThetaRepository(endpoint)
        val filePaths = listOf("/media/sf_vmshare/ex/multipartPostTest")

        kotlin.runCatching {
            thetaRepository.updateFirmware(apiPath!!, filePaths)
        }.onSuccess {
            assertTrue(true, "updateFirmware")
        }.onFailure {
            println("updateThetaFirmware: $it")
            assertTrue(false, "updateFirmware")
        }
    }

    companion object {
        // Environment variable name that holds the path of Theta firmware update API.
        const val FIRMWARE_UPDATE_API_ENV_NAME = "THETA_FU_API_PATH"
    }
}
