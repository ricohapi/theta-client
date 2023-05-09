package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.WhiteBalanceAutoStrength
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class WhiteBalanceAutoStrengthTest {
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
     * Get option _whiteBalanceAutoStrength.
     */
    @Test
    fun getOptionWhiteBalanceAutoStrengthTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.WhiteBalanceAutoStrength
        )
        val stringOptionNames = listOf(
            "_whiteBalanceAutoStrength"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_white_balance_auto_strength_off.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.whiteBalanceAutoStrength, ThetaRepository.WhiteBalanceAutoStrengthEnum.OFF)
    }

    /**
     * Set option _whiteBalanceAutoStrength.
     */
    @Test
    fun setOptionAiAutoThumbnailTest() = runTest {
        val value = Pair(ThetaRepository.WhiteBalanceAutoStrengthEnum.ON, WhiteBalanceAutoStrength.ON)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, whiteBalanceAutoStrength = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            whiteBalanceAutoStrength = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionAiAutoThumbnailTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.WhiteBalanceAutoStrengthEnum.ON, WhiteBalanceAutoStrength.ON),
            Pair(ThetaRepository.WhiteBalanceAutoStrengthEnum.OFF, WhiteBalanceAutoStrength.OFF),
        )

        values.forEach {
            val orgOptions = Options(
                _whiteBalanceAutoStrength = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.whiteBalanceAutoStrength, it.first, "whiteBalanceAutoStrength ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                whiteBalanceAutoStrength = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._whiteBalanceAutoStrength, it.second, "_whiteBalanceAutoStrength ${it.second}")
        }
    }
}