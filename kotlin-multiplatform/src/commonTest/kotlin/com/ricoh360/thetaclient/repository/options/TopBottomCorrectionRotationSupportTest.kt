package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.PitchSupport
import com.ricoh360.thetaclient.transferred.RollSupport
import com.ricoh360.thetaclient.transferred.TopBottomCorrectionRotationSupport
import com.ricoh360.thetaclient.transferred.YawSupport
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TopBottomCorrectionRotationSupportTest {
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
            ThetaRepository.OptionNameEnum.TopBottomCorrectionRotationSupport
        )
        val stringOptionNames = listOf(
            "_topBottomCorrectionRotationSupport"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_top_bottom_correction_rotation_support.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        options.topBottomCorrectionRotationSupport?.let {
            assertEquals(it.pitch.max, 90.0f)
            assertEquals(it.pitch.min, -90.0f)
            assertEquals(it.pitch.stepSize, 0.1f)

            assertEquals(it.roll.max, 180.0f)
            assertEquals(it.roll.min, -180.0f)
            assertEquals(it.roll.stepSize, 0.1f)

            assertEquals(it.yaw.max, 180.0f)
            assertEquals(it.yaw.min, -180.0f)
            assertEquals(it.yaw.stepSize, 0.1f)
        }
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = listOf(
            Pair(
                ThetaRepository.TopBottomCorrectionRotationSupport(
                    pitch = ThetaRepository.ValueRange(100f, -100f, 0.2f),
                    roll = ThetaRepository.ValueRange(200f, -200f, 0.4f),
                    yaw = ThetaRepository.ValueRange(300f, -300f, 0.6f)
                ), TopBottomCorrectionRotationSupport(
                    pitch = PitchSupport(100f, -100f, 0.2f),
                    roll = RollSupport(200f, -200f, 0.4f),
                    yaw = YawSupport(300f, -300f, 0.6f)
                )
            ),
        )

        values.forEach {
            val orgOptions = Options(
                _topBottomCorrectionRotationSupport = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.topBottomCorrectionRotationSupport, it.first, "topBottomCorrectionRotationSupport ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                topBottomCorrectionRotationSupport = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._topBottomCorrectionRotationSupport, null, "_topBottomCorrectionRotationSupport ${it.second}")
        }
    }
}