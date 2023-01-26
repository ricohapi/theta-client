package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ExposureDelayTest {
    private val endpoint = "http://dummy/"

    @BeforeTest
    fun setup() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @AfterTest
    fun teardown() {
        MockApiClient.status = HttpStatusCode.OK
    }

    /**
     * Get option exposureDelay.
     */
    @Test
    fun getOptionExposureDelayTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.ExposureDelay
        )
        val stringOptionNames = listOf(
            "exposureDelay"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_exposure_delay_1.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.exposureDelay, ThetaRepository.ExposureDelayEnum.DELAY_1)
    }

    /**
     * Set option exposureDelay.
     */
    @Test
    fun setOptionExposureDelayTest() = runTest {
        val value = Pair(ThetaRepository.ExposureDelayEnum.DELAY_3, 3)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, exposureDelay = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            exposureDelay = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionExposureDelayTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.ExposureDelayEnum.DELAY_OFF, 0),
            Pair(ThetaRepository.ExposureDelayEnum.DELAY_1, 1),
            Pair(ThetaRepository.ExposureDelayEnum.DELAY_2, 2),
            Pair(ThetaRepository.ExposureDelayEnum.DELAY_3, 3),
            Pair(ThetaRepository.ExposureDelayEnum.DELAY_4, 4),
            Pair(ThetaRepository.ExposureDelayEnum.DELAY_5, 5),
            Pair(ThetaRepository.ExposureDelayEnum.DELAY_6, 6),
            Pair(ThetaRepository.ExposureDelayEnum.DELAY_7, 7),
            Pair(ThetaRepository.ExposureDelayEnum.DELAY_8, 8),
            Pair(ThetaRepository.ExposureDelayEnum.DELAY_9, 9),
            Pair(ThetaRepository.ExposureDelayEnum.DELAY_10, 10)
        )

        values.forEach {
            val orgOptions = Options(
                exposureDelay = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.exposureDelay, it.first, "exposureDelay ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                exposureDelay = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options.exposureDelay, it.second, "exposureDelay ${it.second}")
        }
    }
}
