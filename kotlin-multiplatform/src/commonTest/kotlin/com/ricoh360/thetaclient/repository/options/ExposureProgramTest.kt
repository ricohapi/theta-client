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
class ExposureProgramTest {
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
     * Get option exposureProgram.
     */
    @Test
    fun getOptionExposureProgramTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.ExposureProgram
        )
        val stringOptionNames = listOf(
            "exposureProgram"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_exposure_program_1.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.exposureProgram, ThetaRepository.ExposureProgramEnum.MANUAL)
    }

    /**
     * Set option exposureProgram.
     */
    @Test
    fun setOptionExposureProgramTest() = runTest {
        val value = Pair(ThetaRepository.ExposureProgramEnum.MANUAL, 1)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, exposureProgram = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            exposureProgram = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionExposureProgramTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.ExposureProgramEnum.MANUAL, 1),
            Pair(ThetaRepository.ExposureProgramEnum.NORMAL_PROGRAM, 2),
            Pair(ThetaRepository.ExposureProgramEnum.APERTURE_PRIORITY, 3),
            Pair(ThetaRepository.ExposureProgramEnum.SHUTTER_PRIORITY, 4),
            Pair(ThetaRepository.ExposureProgramEnum.ISO_PRIORITY, 9)
        )

        values.forEach {
            val orgOptions = Options(
                exposureProgram = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.exposureProgram, it.first, "exposureProgram ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                exposureProgram = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options.exposureProgram, it.second, "exposureProgram ${it.second}")
        }
    }
}
