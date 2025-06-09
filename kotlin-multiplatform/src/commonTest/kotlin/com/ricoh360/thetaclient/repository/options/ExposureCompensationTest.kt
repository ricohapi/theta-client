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
class ExposureCompensationTest {
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
     * Get option exposureCompensation.
     */
    @Test
    fun getOptionExposureCompensationTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.ExposureCompensation
        )
        val stringOptionNames = listOf(
            "exposureCompensation"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_exposure_compensation_2_0.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.exposureCompensation, ThetaRepository.ExposureCompensationEnum.P2_0)
    }

    /**
     * Get option UNKNOWN.
     */
    @Test
    fun getOptionUnknownTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.ExposureCompensation
        )
        val stringOptionNames = listOf(
            "exposureCompensation"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_exposure_compensation_unknown.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.exposureCompensation, ThetaRepository.ExposureCompensationEnum.UNKNOWN)
    }

    /**
     * Set option exposureCompensation.
     */
    @Test
    fun setOptionExposureCompensationTest() = runTest {
        val value = Pair(ThetaRepository.ExposureCompensationEnum.P2_0, 2.0f)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, exposureCompensation = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            exposureCompensation = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionExposureCompensationTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.ExposureCompensationEnum.UNKNOWN, 999f),
            Pair(ThetaRepository.ExposureCompensationEnum.M4_0, -4.0f),
            Pair(ThetaRepository.ExposureCompensationEnum.M3_7, -3.7f),
            Pair(ThetaRepository.ExposureCompensationEnum.M3_3, -3.3f),
            Pair(ThetaRepository.ExposureCompensationEnum.M3_0, -3.0f),
            Pair(ThetaRepository.ExposureCompensationEnum.M2_7, -2.7f),
            Pair(ThetaRepository.ExposureCompensationEnum.M2_3, -2.3f),
            Pair(ThetaRepository.ExposureCompensationEnum.M2_0, -2.0f),
            Pair(ThetaRepository.ExposureCompensationEnum.M1_7, -1.7f),
            Pair(ThetaRepository.ExposureCompensationEnum.M1_3, -1.3f),
            Pair(ThetaRepository.ExposureCompensationEnum.M1_0, -1.0f),
            Pair(ThetaRepository.ExposureCompensationEnum.M0_7, -0.7f),
            Pair(ThetaRepository.ExposureCompensationEnum.M0_3, -0.3f),
            Pair(ThetaRepository.ExposureCompensationEnum.ZERO, 0.0f),
            Pair(ThetaRepository.ExposureCompensationEnum.P0_3, 0.3f),
            Pair(ThetaRepository.ExposureCompensationEnum.P0_7, 0.7f),
            Pair(ThetaRepository.ExposureCompensationEnum.P1_0, 1.0f),
            Pair(ThetaRepository.ExposureCompensationEnum.P1_3, 1.3f),
            Pair(ThetaRepository.ExposureCompensationEnum.P1_7, 1.7f),
            Pair(ThetaRepository.ExposureCompensationEnum.P2_0, 2.0f),
            Pair(ThetaRepository.ExposureCompensationEnum.P2_3, 2.3f),
            Pair(ThetaRepository.ExposureCompensationEnum.P2_7, 2.7f),
            Pair(ThetaRepository.ExposureCompensationEnum.P3_0, 3.0f),
            Pair(ThetaRepository.ExposureCompensationEnum.P3_3, 3.3f),
            Pair(ThetaRepository.ExposureCompensationEnum.P3_7, 3.7f),
            Pair(ThetaRepository.ExposureCompensationEnum.P4_0, 4.0f)
        )

        assertEquals(ThetaRepository.ExposureCompensationEnum.entries.size, values.size)

        values.forEach {
            val orgOptions = Options(
                exposureCompensation = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.exposureCompensation, it.first, "exposureCompensation ${it.first}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                exposureCompensation = it.first
            )
            val options = orgOptions.toOptions()
            when (it.first) {
                ThetaRepository.ExposureCompensationEnum.UNKNOWN -> {
                    assertEquals(options.exposureCompensation, null)
                }

                else -> assertEquals(
                    options.exposureCompensation,
                    it.second,
                    "exposureCompensation ${it.first}"
                )
            }
        }
    }
}
