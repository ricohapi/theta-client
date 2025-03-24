package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.ColorTemperatureSupport
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ColorTemperatureSupportTest {
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
    fun getOption() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.ColorTemperatureSupport
        )
        val stringOptionNames = listOf(
            "_colorTemperatureSupport"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_color_temperature_support.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.colorTemperatureSupport?.maxTemperature, 10000, "maxTemperature")
        assertEquals(options.colorTemperatureSupport?.minTemperature, 2500, "minTemperature")
        assertEquals(options.colorTemperatureSupport?.stepSize, 100, "stepSize")
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = Pair(
            ColorTemperatureSupport(10000, 3000, 100),
            ThetaRepository.ColorTemperatureSupport(10000, 3000, 100)
        )

        val orgOptions = Options(
            _colorTemperatureSupport = values.first
        )
        val optionsTR = ThetaRepository.Options(orgOptions)
        assertEquals(
            optionsTR.colorTemperatureSupport,
            values.second,
            "colorTemperatureSupport ${values.second}"
        )

        val orgOptionsTR = ThetaRepository.Options(
            colorTemperatureSupport = values.second
        )
        val options = orgOptionsTR.toOptions()
        assertEquals(
            options._colorTemperatureSupport,
            values.first,
            "_colorTemperatureSupport ${values.first}"
        )
    }
}