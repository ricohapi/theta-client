package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CameraPower
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CameraPowerTest {
    private val endpoint = "http://192.168.1.1:80/"

    @BeforeTest
    fun setup() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @AfterTest
    fun teardown() {
        MockApiClient.status = HttpStatusCode.OK
    }

    /**
     * Get option _cameraPower.
     */
    @Test
    fun getOptionCameraPowerTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.CameraPower
        )
        val stringOptionNames = listOf(
            "_cameraPower"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_camera_power_saving.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.cameraPower, ThetaRepository.CameraPowerEnum.POWER_SAVING)
    }

    /**
     * Get option _cameraPower UNKNOWN.
     */
    @Test
    fun getOptionCameraPowerUnknownTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.CameraPower
        )
        val stringOptionNames = listOf(
            "_cameraPower"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_camera_unknown.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.cameraPower, ThetaRepository.CameraPowerEnum.UNKNOWN)
    }

    /**
     * Set option _cameraPower.
     */
    @Test
    fun setOptionCameraPowerTest() = runTest {
        val value = Pair(ThetaRepository.CameraPowerEnum.SILENT_MODE, CameraPower.SILENT_MODE)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, cameraPower = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            cameraPower = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionCameraPowerTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.CameraPowerEnum.UNKNOWN, CameraPower.UNKNOWN),
            Pair(ThetaRepository.CameraPowerEnum.ON, CameraPower.ON),
            Pair(ThetaRepository.CameraPowerEnum.OFF, CameraPower.OFF),
            Pair(ThetaRepository.CameraPowerEnum.POWER_SAVING, CameraPower.POWER_SAVING),
            Pair(ThetaRepository.CameraPowerEnum.SILENT_MODE, CameraPower.SILENT_MODE),
        )

        values.forEach {
            val orgOptions = Options(
                _cameraPower = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.cameraPower, it.first, "cameraPower ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                cameraPower = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._cameraPower, it.second, "_cameraPower ${it.second}")
        }
    }
}