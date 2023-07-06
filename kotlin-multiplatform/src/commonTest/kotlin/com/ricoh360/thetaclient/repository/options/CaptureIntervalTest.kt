package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class CaptureIntervalTest {
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
     * Get option captureInterval.
     */
    @Test
    fun getOptionCaptureIntervalTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.CaptureInterval
        )
        val stringOptionNames = listOf(
            "captureInterval"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_capture_interval_3600.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.captureInterval, 3600)
    }

    /**
     * Set option captureInterval.
     */
    @Test
    fun setOptionCaptureIntervalTest() = runTest {
        val value = Pair(3600, 3600)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, captureInterval = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            captureInterval = value.first
        )
        thetaRepository.setOptions(options)
    }
}
