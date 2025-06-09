package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.MicrophoneNoiseReduction
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MicrophoneNoiseReductionTest {
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
    fun getOptionTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.MicrophoneNoiseReduction
        )
        val stringOptionNames = listOf(
            "_microphoneNoiseReduction"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_microphone_noise_reduction_on.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.microphoneNoiseReduction, ThetaRepository.MicrophoneNoiseReductionEnum.ON)
    }

    /**
     * Get option UNKNOWN.
     */
    @Test
    fun getOptionUnknownTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.MicrophoneNoiseReduction
        )
        val stringOptionNames = listOf(
            "_microphoneNoiseReduction"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_microphone_noise_reduction_unknown.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.microphoneNoiseReduction, ThetaRepository.MicrophoneNoiseReductionEnum.UNKNOWN)
    }

    /**
     * Set option.
     */
    @Test
    fun setOptionTest() = runTest {
        val value = Pair(ThetaRepository.MicrophoneNoiseReductionEnum.OFF, MicrophoneNoiseReduction.OFF)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, microphoneNoiseReduction = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            microphoneNoiseReduction = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.MicrophoneNoiseReductionEnum.UNKNOWN, MicrophoneNoiseReduction.UNKNOWN),
            Pair(ThetaRepository.MicrophoneNoiseReductionEnum.ON, MicrophoneNoiseReduction.ON),
            Pair(ThetaRepository.MicrophoneNoiseReductionEnum.OFF, MicrophoneNoiseReduction.OFF),
        )

        values.forEach {
            val orgOptions = Options(
                _microphoneNoiseReduction = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.microphoneNoiseReduction, it.first, "microphoneNoiseReduction ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                microphoneNoiseReduction = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._microphoneNoiseReduction, it.second, "_microphoneNoiseReduction ${it.second}")
        }
    }
}