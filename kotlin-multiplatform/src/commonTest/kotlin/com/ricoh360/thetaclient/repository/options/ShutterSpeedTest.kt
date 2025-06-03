package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
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
class ShutterSpeedTest {
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
     * Get option shutterSpeed.
     */
    @Test
    fun getOptionShutterSpeedTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.ShutterSpeed
        )
        val stringOptionNames = listOf(
            "shutterSpeed"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_shutter_speed_10.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.shutterSpeed, ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_10, "shutterSpeed")
    }

    /**
     * Set option shutterSpeed.
     */
    @Test
    fun setOptionShutterSpeedTest() = runTest {
        val value = Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_10, 10.0)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, shutterSpeed = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            shutterSpeed = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionShutterSpeedTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_AUTO, 0.0),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_60, 60.0),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_50, 50.0),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_40, 40.0),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_30, 30.0),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_25, 25.0),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_20, 20.0),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_15, 15.0),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_13, 13.0),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_10, 10.0),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_8, 8.0),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_6, 6.0),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_5, 5.0),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_4, 4.0),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_3_2, 3.2),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_2_5, 2.5),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_2, 2.0),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_1_6, 1.6),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_1_3, 1.3),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_1, 1.0),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_1_3, 0.76923076),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_1_6, 0.625),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_2, 0.5),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_2_5, 0.4),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_3, 0.33333333),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_4, 0.25),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_5, 0.2),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_6, 0.16666666),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_8, 0.125),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_10, 0.1),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_13, 0.07692307),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_15, 0.06666666),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_20, 0.05),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_25, 0.04),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_30, 0.03333333),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_40, 0.025),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_50, 0.02),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_60, 0.01666666),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_80, 0.0125),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_100, 0.01),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_125, 0.008),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_160, 0.00625),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_200, 0.005),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_250, 0.004),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_320, 0.003125),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_400, 0.0025),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_500, 0.002),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_640, 0.0015625),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_800, 0.00125),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_1000, 0.001),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_1250, 0.0008),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_1600, 0.000625),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_2000, 0.0005),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_2500, 0.0004),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_3200, 0.0003125),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_4000, 0.00025),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_5000, 0.0002),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_6400, 0.00015625),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_8000, 0.000125),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_10000, 0.0001),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_12500, 0.00008),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_12800, 0.00007812),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_16000, 0.0000625),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_20000, 0.00005),
            Pair(ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_25000, 0.00004),
        )

        values.forEach {
            val orgOptions = Options(
                shutterSpeed = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.shutterSpeed, it.first, "shutterSpeed ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                shutterSpeed = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options.shutterSpeed, it.second, "shutterSpeed ${it.second}")
        }
    }
}
