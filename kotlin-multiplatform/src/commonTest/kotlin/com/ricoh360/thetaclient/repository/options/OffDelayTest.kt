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
class OffDelayTest {
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
     * Get option offDelay.
     */
    @Test
    fun getOptionOffDelayTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.OffDelay
        )
        val stringOptionNames = listOf(
            "offDelay"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_off_delay_600.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.offDelay, ThetaRepository.OffDelayEnum.OFF_DELAY_10M, "offDelay")
    }

    /**
     * Set option offDelay.
     */
    @Test
    fun setOptionOffDelayTest() = runTest {
        val value = Pair(ThetaRepository.OffDelayEnum.OFF_DELAY_10M, 600)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, offDelay = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            offDelay = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionOffDelayTest() {
        val values = listOf(
            Pair(ThetaRepository.OffDelayEnum.DISABLE, 65535),
            Pair(ThetaRepository.OffDelayEnum.OFF_DELAY_5M, 60 * 5),
            Pair(ThetaRepository.OffDelayEnum.OFF_DELAY_10M, 60 * 10),
            Pair(ThetaRepository.OffDelayEnum.OFF_DELAY_15M, 60 * 15),
            Pair(ThetaRepository.OffDelayEnum.OFF_DELAY_30M, 60 * 30),
            Pair(ThetaRepository.OffDelaySec(60), 60)
        )

        values.forEach {
            val orgOptions = Options(
                offDelay = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.offDelay, it.first, "offDelay ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                offDelay = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options.offDelay, it.second, "offDelay ${it.second}")
        }
    }
}
