package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.GpsTagRecording
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class IsGpsOnTest {
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
     * Get option isGpsOn.
     */
    @Test
    fun getOptionIsGpsOnTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.IsGpsOn
        )
        val stringOptionNames = listOf(
            "_gpsTagRecording"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_gps_tag_recording_on.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.isGpsOn, true, "isGpsOn")
    }

    /**
     * Set option isGpsOn.
     */
    @Test
    fun setOptionIsGpsOnTest() = runTest {
        val value = Pair(true, GpsTagRecording.ON)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, gpsTagRecording = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            isGpsOn = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionIsGpsOnTest() = runTest {
        val values = listOf(
            Pair(true, GpsTagRecording.ON),
            Pair(false, GpsTagRecording.OFF)
        )

        values.forEach {
            val orgOptions = Options(
                _gpsTagRecording = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.isGpsOn, it.first, "isGpsOn ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                isGpsOn = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._gpsTagRecording, it.second, "gpsTagRecording ${it.second}")
        }
    }
}
