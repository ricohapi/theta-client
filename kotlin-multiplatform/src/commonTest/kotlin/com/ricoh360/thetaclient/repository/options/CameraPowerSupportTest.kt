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

class CameraPowerSupportTest {
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
     * Get option.
     */
    @Test
    fun getOptionTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.CameraPowerSupport
        )
        val stringOptionNames = listOf(
            "_cameraPowerSupport"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_camera_power_support.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(
            options.cameraPowerSupport,
            listOf(
                ThetaRepository.CameraPowerEnum.ON,
                ThetaRepository.CameraPowerEnum.OFF,
                ThetaRepository.CameraPowerEnum.POWER_SAVING,
                ThetaRepository.CameraPowerEnum.SILENT_MODE,
                ThetaRepository.CameraPowerEnum.UNKNOWN,
            ),
            "cameraPowerSupport"
        )
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = Pair(
            listOf(CameraPower.ON, CameraPower.OFF, CameraPower.POWER_SAVING, CameraPower.SILENT_MODE),
            listOf(
                ThetaRepository.CameraPowerEnum.ON,
                ThetaRepository.CameraPowerEnum.OFF,
                ThetaRepository.CameraPowerEnum.POWER_SAVING,
                ThetaRepository.CameraPowerEnum.SILENT_MODE
            )
        )

        val orgOptions = Options(
            _cameraPowerSupport = values.first
        )
        val optionsTR = ThetaRepository.Options(orgOptions)
        assertEquals(
            optionsTR.cameraPowerSupport,
            values.second,
            "cameraPowerSupport ${values.second}"
        )

        val orgOptionsTR = ThetaRepository.Options(
            cameraPowerSupport = values.second
        )
        val options = orgOptionsTR.toOptions()
        assertEquals(
            options._cameraPowerSupport,
            null,
            "_cameraPowerSupport ${values.first}"
        )
    }
}