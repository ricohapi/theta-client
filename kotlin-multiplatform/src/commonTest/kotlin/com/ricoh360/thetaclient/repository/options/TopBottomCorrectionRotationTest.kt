package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.TopBottomCorrectionRotation
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TopBottomCorrectionRotationTest {
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
            ThetaRepository.OptionNameEnum.TopBottomCorrectionRotation
        )
        val stringOptionNames = listOf(
            "_topBottomCorrectionRotation"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_top_bottom_correction_rotation_zero.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.topBottomCorrectionRotation?.pitch, 0.0f)
        assertEquals(options.topBottomCorrectionRotation?.roll, 0.0f)
        assertEquals(options.topBottomCorrectionRotation?.yaw, 0.0f)
    }

    /**
     * Set option
     */
    @Test
    fun setOptionTest() = runTest {
        val value = Pair(ThetaRepository.TopBottomCorrectionRotation(pitch = 1.0f, roll = 1.0f, yaw = 1.0f), TopBottomCorrectionRotation(pitch = "1.0", roll = "1.0", yaw = "1.0"))

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, topBottomCorrectionRotation = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            topBottomCorrectionRotation = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.TopBottomCorrectionRotation(pitch = 1.0f, roll = 1.0f, yaw = 1.0f), TopBottomCorrectionRotation(pitch = "1.0", roll = "1.0", yaw = "1.0")),
        )

        values.forEach {
            val orgOptions = Options(
                _topBottomCorrectionRotation = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.topBottomCorrectionRotation, it.first, "topBottomCorrectionRotation ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                topBottomCorrectionRotation = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._topBottomCorrectionRotation, it.second, "_topBottomCorrectionRotation ${it.second}")
        }
    }
}