package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CameraControlSource
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class CameraControlSourceTest {
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
     * Get option _cameraControlSource.
     */
    @Test
    fun getOptionCameraControlSourceTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.CameraControlSource
        )
        val stringOptionNames = listOf(
            "_cameraControlSource"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_camera_control_source.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.cameraControlSource, ThetaRepository.CameraControlSourceEnum.CAMERA)
    }

    /**
     * Set option _cameraControlSource.
     */
    @Test
    fun setOptionCameraControlSourceTest() = runTest {
        val value = Pair(ThetaRepository.CameraControlSourceEnum.CAMERA, CameraControlSource.CAMERA)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, cameraControlSource = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            cameraControlSource = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionCameraControlSourceTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.CameraControlSourceEnum.CAMERA, CameraControlSource.CAMERA),
            Pair(ThetaRepository.CameraControlSourceEnum.APP, CameraControlSource.APP),
        )

        values.forEach {
            val orgOptions = Options(
                _cameraControlSource = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.cameraControlSource, it.first, "cameraControlSource ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                cameraControlSource = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._cameraControlSource, it.second, "_cameraControlSource ${it.second}")
        }
    }
}