package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CameraControlSource
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CameraControlSourceSupportTest {
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
            ThetaRepository.OptionNameEnum.CameraControlSourceSupport
        )
        val stringOptionNames = listOf(
            "_cameraControlSourceSupport"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_camera_control_source_support.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(
            options.cameraControlSourceSupport,
            listOf(
                ThetaRepository.CameraControlSourceEnum.CAMERA,
                ThetaRepository.CameraControlSourceEnum.APP,
                ThetaRepository.CameraControlSourceEnum.UNKNOWN,
            ),
            "cameraControlSourceSupport"
        )
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = Pair(
            listOf(CameraControlSource.CAMERA, CameraControlSource.APP),
            listOf(
                ThetaRepository.CameraControlSourceEnum.CAMERA,
                ThetaRepository.CameraControlSourceEnum.APP
            )
        )

        val orgOptions = Options(
            _cameraControlSourceSupport = values.first
        )
        val optionsTR = ThetaRepository.Options(orgOptions)
        assertEquals(
            optionsTR.cameraControlSourceSupport,
            values.second,
            "cameraControlSourceSupport ${values.second}"
        )

        val orgOptionsTR = ThetaRepository.Options(
            cameraControlSourceSupport = values.second
        )
        val options = orgOptionsTR.toOptions()
        assertEquals(
            options._cameraControlSourceSupport,
            values.first,
            "_cameraControlSourceSupport ${values.first}"
        )
    }
}