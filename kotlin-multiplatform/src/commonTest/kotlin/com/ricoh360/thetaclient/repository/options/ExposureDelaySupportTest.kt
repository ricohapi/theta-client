package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ExposureDelaySupportTest {
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
            ThetaRepository.OptionNameEnum.ExposureDelaySupport
        )
        val stringOptionNames = listOf(
            "exposureDelaySupport"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_exposure_delay_support.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.exposureDelaySupport?.get(0), ThetaRepository.ExposureDelayEnum.DELAY_OFF)
        assertEquals(options.exposureDelaySupport?.get(1), ThetaRepository.ExposureDelayEnum.DELAY_1)
        assertEquals(options.exposureDelaySupport?.get(2), ThetaRepository.ExposureDelayEnum.DELAY_2)
        assertEquals(options.exposureDelaySupport?.get(3), ThetaRepository.ExposureDelayEnum.DELAY_3)
        assertEquals(options.exposureDelaySupport?.get(4), ThetaRepository.ExposureDelayEnum.DELAY_4)
        assertEquals(options.exposureDelaySupport?.get(5), ThetaRepository.ExposureDelayEnum.DELAY_5)
        assertEquals(options.exposureDelaySupport?.get(6), ThetaRepository.ExposureDelayEnum.DELAY_6)
        assertEquals(options.exposureDelaySupport?.get(7), ThetaRepository.ExposureDelayEnum.DELAY_7)
        assertEquals(options.exposureDelaySupport?.get(8), ThetaRepository.ExposureDelayEnum.DELAY_8)
        assertEquals(options.exposureDelaySupport?.get(9), ThetaRepository.ExposureDelayEnum.DELAY_9)
        assertEquals(options.exposureDelaySupport?.get(10), ThetaRepository.ExposureDelayEnum.DELAY_10)

    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = Pair(
            listOf(99, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            listOf(
                ThetaRepository.ExposureDelayEnum.UNKNOWN,
                ThetaRepository.ExposureDelayEnum.DELAY_OFF,
                ThetaRepository.ExposureDelayEnum.DELAY_1,
                ThetaRepository.ExposureDelayEnum.DELAY_2,
                ThetaRepository.ExposureDelayEnum.DELAY_3,
                ThetaRepository.ExposureDelayEnum.DELAY_4,
                ThetaRepository.ExposureDelayEnum.DELAY_5,
                ThetaRepository.ExposureDelayEnum.DELAY_6,
                ThetaRepository.ExposureDelayEnum.DELAY_7,
                ThetaRepository.ExposureDelayEnum.DELAY_8,
                ThetaRepository.ExposureDelayEnum.DELAY_9,
                ThetaRepository.ExposureDelayEnum.DELAY_10,
            )
        )

        val orgOptions = Options(
            exposureDelaySupport = values.first
        )
        val optionsTR = ThetaRepository.Options(orgOptions)
        assertEquals(
            optionsTR.exposureDelaySupport,
            values.second,
            "exposureDelaySupport ${values.second}"
        )

        val orgOptionsTR = ThetaRepository.Options(
            exposureDelaySupport = values.second
        )
        val options = orgOptionsTR.toOptions()
        assertEquals(
            options.exposureDelaySupport,
            null,
            "exposureDelaySupport ${values.first}"
        )
    }
}