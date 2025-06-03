package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CaptureMode
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
class CaptureModeTest {
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
     * Get option captureMode.
     */
    @Test
    fun getOptionCaptureModeTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.CaptureMode
        )
        val stringOptionNames = listOf(
            "captureMode"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_capture_mode_image.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.captureMode, ThetaRepository.CaptureModeEnum.IMAGE)
    }

    /**
     * Set option captureMode.
     */
    @Test
    fun setOptionCaptureModeTest() = runTest {
        val value = Pair(ThetaRepository.CaptureModeEnum.IMAGE, CaptureMode.IMAGE)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, captureMode = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            captureMode = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionCaptureModeTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.CaptureModeEnum.IMAGE, CaptureMode.IMAGE),
            Pair(ThetaRepository.CaptureModeEnum.VIDEO, CaptureMode.VIDEO),
            Pair(ThetaRepository.CaptureModeEnum.LIVE_STREAMING, CaptureMode.LIVE_STREAMING),
            Pair(ThetaRepository.CaptureModeEnum.INTERVAL, CaptureMode.INTERVAL),
            Pair(ThetaRepository.CaptureModeEnum.PRESET, CaptureMode.PRESET),
        )

        values.forEach {
            val orgOptions = Options(
                captureMode = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.captureMode, it.first, "captureMode ${it.second}")
        }

        values.filter {
            it.first != null
        }.forEach {
            val orgOptions = ThetaRepository.Options(
                captureMode = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options.captureMode, it.second, "captureMode ${it.second}")
        }
    }
}
