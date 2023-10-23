package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.TopBottomCorrectionOption
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class TopBottomCorrectionTest {
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
            ThetaRepository.OptionNameEnum.TopBottomCorrection
        )
        val stringOptionNames = listOf(
            "_topBottomCorrection"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_top_bottom_correction_apply_auto.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.topBottomCorrection, ThetaRepository.TopBottomCorrectionOptionEnum.APPLY_AUTO)
    }

    /**
     * Set option
     */
    @Test
    fun setOptionTest() = runTest {
        val value = Pair(ThetaRepository.TopBottomCorrectionOptionEnum.APPLY, TopBottomCorrectionOption.APPLY)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, topBottomCorrection = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            topBottomCorrection = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.TopBottomCorrectionOptionEnum.APPLY, TopBottomCorrectionOption.APPLY),
            Pair(ThetaRepository.TopBottomCorrectionOptionEnum.APPLY_AUTO, TopBottomCorrectionOption.APPLY_AUTO),
            Pair(ThetaRepository.TopBottomCorrectionOptionEnum.APPLY_SEMIAUTO, TopBottomCorrectionOption.APPLY_SEMIAUTO),
            Pair(ThetaRepository.TopBottomCorrectionOptionEnum.APPLY_SAVE, TopBottomCorrectionOption.APPLY_SAVE),
            Pair(ThetaRepository.TopBottomCorrectionOptionEnum.APPLY_LOAD, TopBottomCorrectionOption.APPLY_LOAD),
            Pair(ThetaRepository.TopBottomCorrectionOptionEnum.DISAPPLY, TopBottomCorrectionOption.DISAPPLY),
            Pair(ThetaRepository.TopBottomCorrectionOptionEnum.MANUAL, TopBottomCorrectionOption.MANUAL),
        )

        values.forEach {
            val orgOptions = Options(
                _topBottomCorrection = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.topBottomCorrection, it.first, "topBottomCorrection ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                topBottomCorrection = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._topBottomCorrection, it.second, "_topBottomCorrection ${it.second}")
        }
    }
}