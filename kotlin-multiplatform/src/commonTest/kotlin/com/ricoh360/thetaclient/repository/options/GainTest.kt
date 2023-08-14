package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Gain
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class GainTest {
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
     * Get option
     */
    @Test
    fun getOptionTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.Gain
        )
        val stringOptionNames = listOf(
            "_gain"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_gain_mute.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.gain, ThetaRepository.GainEnum.MUTE)
    }

    /**
     * Set option
     */
    @Test
    fun setOptionTest() = runTest {
        val value = Pair(ThetaRepository.GainEnum.MEGA_VOLUME, Gain.MEGA_VOLUME)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, gain = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            gain = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.GainEnum.NORMAL, Gain.NORMAL),
            Pair(ThetaRepository.GainEnum.MEGA_VOLUME, Gain.MEGA_VOLUME),
            Pair(ThetaRepository.GainEnum.MUTE, Gain.MUTE),
        )

        values.forEach {
            val orgOptions = Options(
                _gain = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.gain, it.first, "gain ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                gain = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._gain, it.second, "_gain ${it.second}")
        }
    }
}