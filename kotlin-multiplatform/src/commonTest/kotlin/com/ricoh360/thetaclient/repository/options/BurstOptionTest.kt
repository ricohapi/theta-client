package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class BurstOptionTest {
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
            ThetaRepository.OptionNameEnum.BurstOption
        )
        val stringOptionNames = listOf(
            "_burstOption"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_burst_option_default.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.burstOption?.burstCaptureNum, ThetaRepository.BurstCaptureNumEnum.BURST_CAPTURE_NUM_1)
        assertEquals(options.burstOption?.burstBracketStep, ThetaRepository.BurstBracketStepEnum.BRACKET_STEP_0_0)
        assertEquals(options.burstOption?.burstCompensation, ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_0_0)
        assertEquals(options.burstOption?.burstMaxExposureTime, ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_15)
        assertEquals(options.burstOption?.burstEnableIsoControl, ThetaRepository.BurstEnableIsoControlEnum.OFF)
        assertEquals(options.burstOption?.burstOrder, ThetaRepository.BurstOrderEnum.BURST_BRACKET_ORDER_0)
    }

    /**
     * Set option.
     */
    @Test
    fun setOptionTest() = runTest {
        val value = Pair(
            ThetaRepository.BurstOption(
                burstCaptureNum = ThetaRepository.BurstCaptureNumEnum.BURST_CAPTURE_NUM_1,
                burstBracketStep = ThetaRepository.BurstBracketStepEnum.BRACKET_STEP_0_0,
                burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_0_0,
                burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_15,
                burstEnableIsoControl = ThetaRepository.BurstEnableIsoControlEnum.OFF,
                burstOrder = ThetaRepository.BurstOrderEnum.BURST_BRACKET_ORDER_0
            ), BurstOption(
                _burstCaptureNum = BurstCaptureNum.BURST_CAPTURE_NUM_1,
                _burstBracketStep = BurstBracketStep.BRACKET_STEP_0_0,
                _burstCompensation = BurstCompensation.BURST_COMPENSATION_0_0,
                _burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_15,
                _burstEnableIsoControl = BurstEnableIsoControl.OFF,
                _burstOrder = BurstOrder.BURST_BRACKET_ORDER_0
            )
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, burstOption = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            burstOption = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = listOf(
            Pair(
                ThetaRepository.BurstOption(
                    burstCaptureNum = ThetaRepository.BurstCaptureNumEnum.BURST_CAPTURE_NUM_1,
                    burstBracketStep = ThetaRepository.BurstBracketStepEnum.BRACKET_STEP_0_0,
                    burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_0_0,
                    burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_15,
                    burstEnableIsoControl = ThetaRepository.BurstEnableIsoControlEnum.OFF,
                    burstOrder = ThetaRepository.BurstOrderEnum.BURST_BRACKET_ORDER_0
                ), BurstOption(
                    _burstCaptureNum = BurstCaptureNum.BURST_CAPTURE_NUM_1,
                    _burstBracketStep = BurstBracketStep.BRACKET_STEP_0_0,
                    _burstCompensation = BurstCompensation.BURST_COMPENSATION_0_0,
                    _burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_15,
                    _burstEnableIsoControl = BurstEnableIsoControl.OFF,
                    _burstOrder = BurstOrder.BURST_BRACKET_ORDER_0
                )
            )
        )

        values.forEach {
            val orgOptions = Options(
                _burstOption = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.burstOption, it.first, "burstOption ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                burstOption = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._burstOption, it.second, "burstOption ${it.second}")
        }
    }
}
