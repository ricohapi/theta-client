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
class ApertureTest {
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
     * Get option aperture.
     */
    @Test
    fun getOptionApertureTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.Aperture
        )
        val stringOptionNames = listOf(
            "aperture"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_aperture_2_1.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.aperture, ThetaRepository.ApertureEnum.APERTURE_2_1)
    }

    /**
     * Set option aperture.
     */
    @Test
    fun setOptionApertureTest() = runTest {
        val value = Pair(ThetaRepository.ApertureEnum.APERTURE_2_1, 2.1f)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, aperture = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            aperture = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionApertureTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.ApertureEnum.APERTURE_AUTO, 0.0f),
            Pair(ThetaRepository.ApertureEnum.APERTURE_2_0, 2.0f),
            Pair(ThetaRepository.ApertureEnum.APERTURE_2_1, 2.1f),
            Pair(ThetaRepository.ApertureEnum.APERTURE_2_4, 2.4f),
            Pair(ThetaRepository.ApertureEnum.APERTURE_3_5, 3.5f),
            Pair(ThetaRepository.ApertureEnum.APERTURE_5_6, 5.6f)
        )

        values.forEach {
            val orgOptions = Options(
                aperture = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.aperture, it.first, "aperture ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                aperture = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options.aperture, it.second, "aperture ${it.second}")
        }
    }
}
