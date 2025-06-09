package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ContinuousNumberTest {
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
     * Get option continuousNumber.
     */
    @Test
    fun getOptionContinuousNumberTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.ContinuousNumber
        )
        val stringOptionNames = listOf(
            "continuousNumber"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_continuous_number_20.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.continuousNumber, ThetaRepository.ContinuousNumberEnum.MAX_20)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionContinuousNumberTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.ContinuousNumberEnum.OFF, 0),
            Pair(ThetaRepository.ContinuousNumberEnum.MAX_1, 1),
            Pair(ThetaRepository.ContinuousNumberEnum.MAX_2, 2),
            Pair(ThetaRepository.ContinuousNumberEnum.MAX_3, 3),
            Pair(ThetaRepository.ContinuousNumberEnum.MAX_4, 4),
            Pair(ThetaRepository.ContinuousNumberEnum.MAX_5, 5),
            Pair(ThetaRepository.ContinuousNumberEnum.MAX_6, 6),
            Pair(ThetaRepository.ContinuousNumberEnum.MAX_7, 7),
            Pair(ThetaRepository.ContinuousNumberEnum.MAX_8, 8),
            Pair(ThetaRepository.ContinuousNumberEnum.MAX_9, 9),
            Pair(ThetaRepository.ContinuousNumberEnum.MAX_10, 10),
            Pair(ThetaRepository.ContinuousNumberEnum.MAX_11, 11),
            Pair(ThetaRepository.ContinuousNumberEnum.MAX_12, 12),
            Pair(ThetaRepository.ContinuousNumberEnum.MAX_13, 13),
            Pair(ThetaRepository.ContinuousNumberEnum.MAX_14, 14),
            Pair(ThetaRepository.ContinuousNumberEnum.MAX_15, 15),
            Pair(ThetaRepository.ContinuousNumberEnum.MAX_16, 16),
            Pair(ThetaRepository.ContinuousNumberEnum.MAX_17, 17),
            Pair(ThetaRepository.ContinuousNumberEnum.MAX_18, 18),
            Pair(ThetaRepository.ContinuousNumberEnum.MAX_19, 19),
            Pair(ThetaRepository.ContinuousNumberEnum.MAX_20, 20),
            Pair(ThetaRepository.ContinuousNumberEnum.UNSUPPORTED, -1),
        )

        values.forEach {
            val orgOptions = Options(
                continuousNumber = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.continuousNumber, it.first, "continuousNumber ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                continuousNumber = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options.continuousNumber, it.second, "continuousNumber ${it.second}")
        }

        // Unsupported value
        val unsupportedValues = listOf(
            -2,
            -1,
            21,
        )
        unsupportedValues.forEach {
            val orgOptions = Options(
                continuousNumber = it,
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(
                options.continuousNumber,
                ThetaRepository.ContinuousNumberEnum.UNSUPPORTED,
                "continuousNumber $it",
            )
        }
    }
}
