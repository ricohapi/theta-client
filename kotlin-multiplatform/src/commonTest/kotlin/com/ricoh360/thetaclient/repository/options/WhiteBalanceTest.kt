package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.WhiteBalance
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class WhiteBalanceTest {
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
     * Get option whiteBalance.
     */
    @Test
    fun getOptionWhiteBalanceTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.WhiteBalance
        )
        val stringOptionNames = listOf(
            "whiteBalance"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_white_balance_daylight.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.whiteBalance, ThetaRepository.WhiteBalanceEnum.DAYLIGHT, "whiteBalance")
    }

    /**
     * Set option whiteBalance.
     */
    @Test
    fun setOptionWhiteBalanceTest() = runTest {
        val value = Pair(ThetaRepository.WhiteBalanceEnum.DAYLIGHT, WhiteBalance.DAYLIGHT)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, whiteBalance = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            whiteBalance = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionWhiteBalanceTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.WhiteBalanceEnum.AUTO, WhiteBalance.AUTO),
            Pair(ThetaRepository.WhiteBalanceEnum.DAYLIGHT, WhiteBalance.DAYLIGHT),
            Pair(ThetaRepository.WhiteBalanceEnum.SHADE, WhiteBalance.SHADE),
            Pair(ThetaRepository.WhiteBalanceEnum.CLOUDY_DAYLIGHT, WhiteBalance.CLOUDY_DAYLIGHT),
            Pair(ThetaRepository.WhiteBalanceEnum.INCANDESCENT, WhiteBalance.INCANDESCENT),
            Pair(ThetaRepository.WhiteBalanceEnum.WARM_WHITE_FLUORESCENT, WhiteBalance._WARM_WHITE_FLUORESCENT),
            Pair(ThetaRepository.WhiteBalanceEnum.DAYLIGHT_FLUORESCENT, WhiteBalance._DAYLIGHT_FLUORESCENT),
            Pair(ThetaRepository.WhiteBalanceEnum.DAYWHITE_FLUORESCENT, WhiteBalance._DAYWHITE_FLUORESCENT),
            Pair(ThetaRepository.WhiteBalanceEnum.FLUORESCENT, WhiteBalance.FLUORESCENT),
            Pair(ThetaRepository.WhiteBalanceEnum.BULB_FLUORESCENT, WhiteBalance._BULB_FLUORESCENT),
            Pair(ThetaRepository.WhiteBalanceEnum.COLOR_TEMPERATURE, WhiteBalance._COLOR_TEMPERATURE),
            Pair(ThetaRepository.WhiteBalanceEnum.UNDERWATER, WhiteBalance._UNDERWATER)
        )

        values.forEach {
            val orgOptions = Options(
                whiteBalance = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.whiteBalance, it.first, "whiteBalance ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                whiteBalance = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options.whiteBalance, it.second, "whiteBalance ${it.second}")
        }
    }
}
