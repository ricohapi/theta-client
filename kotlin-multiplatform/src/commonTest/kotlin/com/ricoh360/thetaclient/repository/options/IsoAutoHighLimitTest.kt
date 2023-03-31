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
class IsoAutoHighLimitTest {
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
     * Get option isoAutoHighLimit.
     */
    @Test
    fun getOptionIsoAutoHighLimitTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.IsoAutoHighLimit
        )
        val stringOptionNames = listOf(
            "isoAutoHighLimit"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_iso_auto_high_limit_100.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.isoAutoHighLimit, ThetaRepository.IsoAutoHighLimitEnum.ISO_100)
    }

    /**
     * Set option isoAutoHighLimit.
     */
    @Test
    fun setOptionIsoAutoHighLimitTest() = runTest {
        val value = Pair(ThetaRepository.IsoAutoHighLimitEnum.ISO_100, 100)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, isoAutoHighLimit = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            isoAutoHighLimit = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionIsoAutoHighLimitTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.IsoAutoHighLimitEnum.ISO_100, 100),
            Pair(ThetaRepository.IsoAutoHighLimitEnum.ISO_125, 125),
            Pair(ThetaRepository.IsoAutoHighLimitEnum.ISO_160, 160),
            Pair(ThetaRepository.IsoAutoHighLimitEnum.ISO_200, 200),
            Pair(ThetaRepository.IsoAutoHighLimitEnum.ISO_250, 250),
            Pair(ThetaRepository.IsoAutoHighLimitEnum.ISO_320, 320),
            Pair(ThetaRepository.IsoAutoHighLimitEnum.ISO_400, 400),
            Pair(ThetaRepository.IsoAutoHighLimitEnum.ISO_500, 500),
            Pair(ThetaRepository.IsoAutoHighLimitEnum.ISO_640, 640),
            Pair(ThetaRepository.IsoAutoHighLimitEnum.ISO_800, 800),
            Pair(ThetaRepository.IsoAutoHighLimitEnum.ISO_1000, 1000),
            Pair(ThetaRepository.IsoAutoHighLimitEnum.ISO_1250, 1250),
            Pair(ThetaRepository.IsoAutoHighLimitEnum.ISO_1600, 1600),
            Pair(ThetaRepository.IsoAutoHighLimitEnum.ISO_2000, 2000),
            Pair(ThetaRepository.IsoAutoHighLimitEnum.ISO_2500, 2500),
            Pair(ThetaRepository.IsoAutoHighLimitEnum.ISO_3200, 3200),
            Pair(ThetaRepository.IsoAutoHighLimitEnum.ISO_4000, 4000),
            Pair(ThetaRepository.IsoAutoHighLimitEnum.ISO_5000, 5000),
            Pair(ThetaRepository.IsoAutoHighLimitEnum.ISO_6400, 6400),
            Pair(ThetaRepository.IsoAutoHighLimitEnum.DO_NOT_UPDATE_MY_SETTING_CONDITION, -1)
        )

        values.forEach {
            val orgOptions = Options(
                isoAutoHighLimit = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.isoAutoHighLimit, it.first, "isoAutoHighLimit ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                isoAutoHighLimit = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options.isoAutoHighLimit, it.second, "isoAutoHighLimit ${it.second}")
        }
    }
}
