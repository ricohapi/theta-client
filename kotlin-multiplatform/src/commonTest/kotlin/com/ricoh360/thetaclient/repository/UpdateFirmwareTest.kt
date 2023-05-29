package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.FIRMWARE_UPDATE_API_ENV_NAME
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.getEnvironmentVar
import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
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
        // Check the setting to execute this test
        getEnvironmentVar(FIRMWARE_UPDATE_API_TEST_ENV_NAME)?.also {
            if(it != "true") {
                println("environment variable ${FIRMWARE_UPDATE_API_TEST_ENV_NAME}: ${it}")
                println("So skip updateFirmwareTest()")
                println("To execute this test set ${FIRMWARE_UPDATE_API_TEST_ENV_NAME} to \"true\"")
                return@runTest
            }
        } ?: run {
            println("No environment variable ${FIRMWARE_UPDATE_API_TEST_ENV_NAME}")
            println("So skip updateFirmwareTest()")
            println("To execute this test set ${FIRMWARE_UPDATE_API_TEST_ENV_NAME} to \"true\"")
            return@runTest
        }
        val apiPath: String? = getEnvironmentVar(FIRMWARE_UPDATE_API_ENV_NAME)
        if(apiPath == null) {
            assertTrue(false, "updateFirmware: ${FIRMWARE_UPDATE_API_ENV_NAME} is not set")
        }
        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, apiPath, "request path")
            ByteReadChannel(Resource("src/commonTest/resources/updateFirmware/update_firmware.done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val fileContents = listOf("firmware binary 1".toByteArray(), "firmware binary 2".toByteArray())
        val fileNames = listOf("firm file 1", "firm file 2")

        kotlin.runCatching {
            thetaRepository.updateFirmware(fileContents, fileNames)
        }.onSuccess {
            assertTrue(true, "updateFirmware")
        }.onFailure {
            println("updateThetaFirmware: ${it.toString()}")
            assertTrue(false, "updateFirmware")
        }
    }

    companion object {
        /**
         * Environment variable name to determine to execute this test or not.
         * Only if its value is "true" this test is executed.
         */
        const val FIRMWARE_UPDATE_API_TEST_ENV_NAME="THETA_FU_API_PATH_TEST"
    }
}
