package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.BurstBracketStep
import com.ricoh360.thetaclient.transferred.BurstCaptureNum
import com.ricoh360.thetaclient.transferred.BurstCompensation
import com.ricoh360.thetaclient.transferred.BurstEnableIsoControl
import com.ricoh360.thetaclient.transferred.BurstMaxExposureTime
import com.ricoh360.thetaclient.transferred.BurstOption
import com.ricoh360.thetaclient.transferred.BurstOrder
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
                ThetaRepository.BurstOption(burstCaptureNum = ThetaRepository.BurstCaptureNumEnum.BURST_CAPTURE_NUM_1),
                BurstOption(_burstCaptureNum = BurstCaptureNum.BURST_CAPTURE_NUM_1)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCaptureNum = ThetaRepository.BurstCaptureNumEnum.BURST_CAPTURE_NUM_3),
                BurstOption(_burstCaptureNum = BurstCaptureNum.BURST_CAPTURE_NUM_3)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCaptureNum = ThetaRepository.BurstCaptureNumEnum.BURST_CAPTURE_NUM_5),
                BurstOption(_burstCaptureNum = BurstCaptureNum.BURST_CAPTURE_NUM_5)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCaptureNum = ThetaRepository.BurstCaptureNumEnum.BURST_CAPTURE_NUM_7),
                BurstOption(_burstCaptureNum = BurstCaptureNum.BURST_CAPTURE_NUM_7)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCaptureNum = ThetaRepository.BurstCaptureNumEnum.BURST_CAPTURE_NUM_9),
                BurstOption(_burstCaptureNum = BurstCaptureNum.BURST_CAPTURE_NUM_9)
            ),
            Pair(
                ThetaRepository.BurstOption(burstBracketStep = ThetaRepository.BurstBracketStepEnum.BRACKET_STEP_0_0),
                BurstOption(_burstBracketStep = BurstBracketStep.BRACKET_STEP_0_0)
            ),
            Pair(
                ThetaRepository.BurstOption(burstBracketStep = ThetaRepository.BurstBracketStepEnum.BRACKET_STEP_0_3),
                BurstOption(_burstBracketStep = BurstBracketStep.BRACKET_STEP_0_3)
            ),
            Pair(
                ThetaRepository.BurstOption(burstBracketStep = ThetaRepository.BurstBracketStepEnum.BRACKET_STEP_0_7),
                BurstOption(_burstBracketStep = BurstBracketStep.BRACKET_STEP_0_7)
            ),
            Pair(
                ThetaRepository.BurstOption(burstBracketStep = ThetaRepository.BurstBracketStepEnum.BRACKET_STEP_1_0),
                BurstOption(_burstBracketStep = BurstBracketStep.BRACKET_STEP_1_0)
            ),
            Pair(
                ThetaRepository.BurstOption(burstBracketStep = ThetaRepository.BurstBracketStepEnum.BRACKET_STEP_1_3),
                BurstOption(_burstBracketStep = BurstBracketStep.BRACKET_STEP_1_3)
            ),
            Pair(
                ThetaRepository.BurstOption(burstBracketStep = ThetaRepository.BurstBracketStepEnum.BRACKET_STEP_1_7),
                BurstOption(_burstBracketStep = BurstBracketStep.BRACKET_STEP_1_7)
            ),
            Pair(
                ThetaRepository.BurstOption(burstBracketStep = ThetaRepository.BurstBracketStepEnum.BRACKET_STEP_2_0),
                BurstOption(_burstBracketStep = BurstBracketStep.BRACKET_STEP_2_0)
            ),
            Pair(
                ThetaRepository.BurstOption(burstBracketStep = ThetaRepository.BurstBracketStepEnum.BRACKET_STEP_2_3),
                BurstOption(_burstBracketStep = BurstBracketStep.BRACKET_STEP_2_3)
            ),
            Pair(
                ThetaRepository.BurstOption(burstBracketStep = ThetaRepository.BurstBracketStepEnum.BRACKET_STEP_2_7),
                BurstOption(_burstBracketStep = BurstBracketStep.BRACKET_STEP_2_7)
            ),
            Pair(
                ThetaRepository.BurstOption(burstBracketStep = ThetaRepository.BurstBracketStepEnum.BRACKET_STEP_3_0),
                BurstOption(_burstBracketStep = BurstBracketStep.BRACKET_STEP_3_0)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_DOWN_5_0),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_DOWN_5_0)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_DOWN_4_7),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_DOWN_4_7)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_DOWN_4_3),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_DOWN_4_3)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_DOWN_4_0),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_DOWN_4_0)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_DOWN_3_7),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_DOWN_3_7)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_DOWN_3_3),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_DOWN_3_3)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_DOWN_3_0),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_DOWN_3_0)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_DOWN_2_7),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_DOWN_2_7)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_DOWN_2_3),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_DOWN_2_3)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_DOWN_2_0),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_DOWN_2_0)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_DOWN_1_7),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_DOWN_1_7)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_DOWN_1_3),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_DOWN_1_3)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_DOWN_1_0),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_DOWN_1_0)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_DOWN_0_7),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_DOWN_0_7)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_DOWN_0_3),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_DOWN_0_3)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_0_0),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_0_0)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_UP_0_3),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_UP_0_3)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_UP_0_7),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_UP_0_7)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_UP_1_0),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_UP_1_0)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_UP_1_3),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_UP_1_3)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_UP_1_7),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_UP_1_7)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_UP_2_0),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_UP_2_0)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_UP_2_3),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_UP_2_3)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_UP_2_7),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_UP_2_7)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_UP_3_0),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_UP_3_0)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_UP_3_3),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_UP_3_3)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_UP_3_7),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_UP_3_7)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_UP_4_0),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_UP_4_0)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_UP_4_3),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_UP_4_3)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_UP_4_7),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_UP_4_7)
            ),
            Pair(
                ThetaRepository.BurstOption(burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_UP_5_0),
                BurstOption(_burstCompensation = BurstCompensation.BURST_COMPENSATION_UP_5_0)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_0_5),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_0_5)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_0_625),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_0_625)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_0_76923076),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_0_76923076)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_1),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_1)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_1_3),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_1_3)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_1_6),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_1_6)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_2),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_2)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_2_5),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_2_5)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_3_2),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_3_2)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_4),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_4)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_5),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_5)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_6),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_6)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_8),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_8)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_10),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_10)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_13),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_13)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_15),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_15)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_20),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_20)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_25),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_25)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_30),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_30)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_40),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_40)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_50),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_50)
            ),
            Pair(
                ThetaRepository.BurstOption(burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_60),
                BurstOption(_burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_60)
            ),
            Pair(
                ThetaRepository.BurstOption(burstEnableIsoControl = ThetaRepository.BurstEnableIsoControlEnum.OFF),
                BurstOption(_burstEnableIsoControl = BurstEnableIsoControl.OFF)
            ),
            Pair(
                ThetaRepository.BurstOption(burstEnableIsoControl = ThetaRepository.BurstEnableIsoControlEnum.ON),
                BurstOption(_burstEnableIsoControl = BurstEnableIsoControl.ON)
            ),
            Pair(
                ThetaRepository.BurstOption(burstOrder = ThetaRepository.BurstOrderEnum.BURST_BRACKET_ORDER_0),
                BurstOption(_burstOrder = BurstOrder.BURST_BRACKET_ORDER_0)
            ),
            Pair(
                ThetaRepository.BurstOption(burstOrder = ThetaRepository.BurstOrderEnum.BURST_BRACKET_ORDER_1),
                BurstOption(_burstOrder = BurstOrder.BURST_BRACKET_ORDER_1)
            ),
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
