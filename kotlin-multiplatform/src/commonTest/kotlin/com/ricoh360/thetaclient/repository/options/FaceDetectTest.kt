package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.FaceDetect
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
class FaceDetectTest {
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
     * Get option
     */
    @Test
    fun getOptionTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.FaceDetect
        )
        val stringOptionNames = listOf(
            "_faceDetect"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_face_detect_on.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.faceDetect, ThetaRepository.FaceDetectEnum.ON)
    }

    /**
     * Set option
     */
    @Test
    fun setOptionTest() = runTest {
        val value = Pair(ThetaRepository.FaceDetectEnum.OFF, FaceDetect.OFF)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, faceDetect = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            faceDetect = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.FaceDetectEnum.ON, FaceDetect.ON),
            Pair(ThetaRepository.FaceDetectEnum.OFF, FaceDetect.OFF),
        )

        values.forEach {
            val orgOptions = Options(
                _faceDetect = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.faceDetect, it.first, "faceDetect ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                faceDetect = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._faceDetect, it.second, "_faceDetect ${it.second}")
        }
    }
}