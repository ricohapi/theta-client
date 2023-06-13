package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CameraMode
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class CameraModeTest {
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
     * Get option _cameraMode.
     */
    @Test
    fun getOptionCameraModeTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.CameraMode
        )
        val stringOptionNames = listOf(
            "_cameraMode"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_camera_mode_capture.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.cameraMode, ThetaRepository.CameraModeEnum.CAPTURE)
    }

    /**
     * Set option _cameraMode.
     */
    @Test
    fun setOptionCameraModeTest() = runTest {
        val value = Pair(ThetaRepository.CameraModeEnum.CAPTURE, CameraMode.CAPTURE)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, cameraMode = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            cameraMode = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionCameraModeTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.CameraModeEnum.CAPTURE, CameraMode.CAPTURE),
            Pair(ThetaRepository.CameraModeEnum.PLAYBACK, CameraMode.PLAYBACK),
        )

        values.forEach {
            val orgOptions = Options(
                _cameraMode = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.cameraMode, it.first, "cameraMode ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                cameraMode = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._cameraMode, it.second, "_cameraMode ${it.second}")
        }
    }
}