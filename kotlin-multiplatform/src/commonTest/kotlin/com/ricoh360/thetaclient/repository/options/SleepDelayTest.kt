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
class SleepDelayTest {
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
     * Get option sleepDelay.
     */
    @Test
    fun getOptionSleepDelayTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.SleepDelay
        )
        val stringOptionNames = listOf(
            "sleepDelay"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_sleep_delay_180.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.sleepDelay, ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M, "sleepDelay")
    }

    /**
     * Set option sleepDelay.
     */
    @Test
    fun setOptionSleepDelayTest() = runTest {
        val value = Pair(ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M, 180)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, sleepDelay = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            sleepDelay = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionSleepDelayTest() {
        val values = listOf(
            Pair(ThetaRepository.SleepDelayEnum.DISABLE, 65535),
            Pair(ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M, 60 * 3),
            Pair(ThetaRepository.SleepDelayEnum.SLEEP_DELAY_5M, 60 * 5),
            Pair(ThetaRepository.SleepDelayEnum.SLEEP_DELAY_7M, 60 * 7),
            Pair(ThetaRepository.SleepDelayEnum.SLEEP_DELAY_10M, 60 * 10),
            Pair(ThetaRepository.SleepDelaySec(1), 1)
        )

        values.forEach {
            val orgOptions = Options(
                sleepDelay = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.sleepDelay, it.first, "sleepDelay ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                sleepDelay = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options.sleepDelay, it.second, "sleepDelay ${it.second}")
        }
    }
}
