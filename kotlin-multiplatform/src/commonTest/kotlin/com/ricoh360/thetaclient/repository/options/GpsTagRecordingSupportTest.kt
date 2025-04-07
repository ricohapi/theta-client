package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.GpsTagRecording
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GpsTagRecordingSupportTest {
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
            ThetaRepository.OptionNameEnum.GpsTagRecordingSupport
        )
        val stringOptionNames = listOf(
            "_gpsTagRecordingSupport"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_gps_tag_recording_support.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(
            options.gpsTagRecordingSupport,
            listOf(
                ThetaRepository.GpsTagRecordingEnum.ON,
                ThetaRepository.GpsTagRecordingEnum.OFF,
                ThetaRepository.GpsTagRecordingEnum.UNKNOWN,
            ),
            "gpsTagRecordingSupport"
        )
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = Pair(
            listOf(GpsTagRecording.ON, GpsTagRecording.OFF),
            listOf(
                ThetaRepository.GpsTagRecordingEnum.ON,
                ThetaRepository.GpsTagRecordingEnum.OFF
            )
        )

        val orgOptions = Options(
            _gpsTagRecordingSupport = values.first
        )
        val optionsTR = ThetaRepository.Options(orgOptions)
        assertEquals(
            optionsTR.gpsTagRecordingSupport,
            values.second,
            "gpsTagRecordingSupport ${values.second}"
        )

        val orgOptionsTR = ThetaRepository.Options(
            gpsTagRecordingSupport = values.second
        )
        val options = orgOptionsTR.toOptions()
        assertEquals(
            options._gpsTagRecordingSupport,
            values.first,
            "_gpsTagRecordingSupport ${values.first}"
        )
    }
}