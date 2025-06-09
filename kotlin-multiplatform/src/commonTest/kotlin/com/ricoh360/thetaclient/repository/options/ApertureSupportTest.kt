package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ApertureSupportTest {
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
            ThetaRepository.OptionNameEnum.ApertureSupport
        )
        val stringOptionNames = listOf(
            "apertureSupport"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_aperture_support_X.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(
            options.apertureSupport,
            listOf(ThetaRepository.ApertureEnum.APERTURE_2_4),
            "apertureSupport"
        )
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = Pair(
            listOf(0.0f, 2.0f, 2.1f, 2.4f, 3.5f, 5.6f),
            listOf(
                ThetaRepository.ApertureEnum.APERTURE_AUTO,
                ThetaRepository.ApertureEnum.APERTURE_2_0,
                ThetaRepository.ApertureEnum.APERTURE_2_1,
                ThetaRepository.ApertureEnum.APERTURE_2_4,
                ThetaRepository.ApertureEnum.APERTURE_3_5,
                ThetaRepository.ApertureEnum.APERTURE_5_6
            )
        )

        val orgOptions = Options(
            apertureSupport = values.first
        )
        val optionsTR = ThetaRepository.Options(orgOptions)
        assertEquals(
            optionsTR.apertureSupport,
            values.second,
            "apertureSupport ${values.second}"
        )

        val orgOptionsTR = ThetaRepository.Options(
            apertureSupport = values.second
        )
        val options = orgOptionsTR.toOptions()
        assertEquals(
            options.apertureSupport,
            null,
            "apertureSupport ${values.first}"
        )
    }
}
