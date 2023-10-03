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
class LatestEnabledExposureDelayTimeTest {
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
     * Get option _latestEnabledExposureDelayTime.
     */
    @Test
    fun getOptionLatestEnabledExposureDelayTimeTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.LatestEnabledExposureDelayTime
        )
        val stringOptionNames = listOf(
            "_latestEnabledExposureDelayTime"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_latest_enabled_exposure_delay_time_1.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.latestEnabledExposureDelayTime, ThetaRepository.ExposureDelayEnum.DELAY_1)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionLatestEnabledExposureDelayTimeTest() = runTest {
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
            Pair(ThetaRepository.ExposureDelayEnum.DELAY_10, 10),
            Pair(ThetaRepository.ExposureDelayEnum.DO_NOT_UPDATE_MY_SETTING_CONDITION, -1)
        )

        values.forEach {
            val orgOptions = Options(
                _latestEnabledExposureDelayTime = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.latestEnabledExposureDelayTime, it.first, "latestEnabledExposureDelayTime ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                latestEnabledExposureDelayTime = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._latestEnabledExposureDelayTime, it.second, "_latestEnabledExposureDelayTime ${it.second}")
        }
    }
}
