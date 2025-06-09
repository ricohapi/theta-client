package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.WlanFrequencyClMode
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class WlanFrequencyClModeTest {
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
     * Get option wlanFrequencyClMode.
     */
    @Test
    fun getOptionWlanFrequencyClModeTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.WlanFrequencyClMode
        )
        val stringOptionNames = listOf(
            "_wlanFrequencyCLmode"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_wlan_frequency_cl_mode.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        val value = options.wlanFrequencyClMode
        assertNotNull(value, "wlanFrequencyClmode")
        assertTrue(value.enable2_4, "wlanFrequencyClmode enable2_4")
        assertFalse(value.enable5_2, "wlanFrequencyClmode enable5_2")
        assertFalse(value.enable5_8, "wlanFrequencyClmode enable5_8")
    }

    /**
     * Set option wlanFrequencyClMode.
     */
    @Test
    fun setOptionWlanFrequencyClModeTest() = runTest {
        val value1 =
            Pair(
                ThetaRepository.WlanFrequencyClMode(
                    enable2_4 = true,
                    enable5_2 = false,
                    enable5_8 = true,
                ),
                WlanFrequencyClMode(
                    enable2_4 = true,
                    enable5_2 = false,
                    enable5_8 = true,
                )
            )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, wlanFrequencyClMode = value1.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            wlanFrequencyClMode = value1.first
        )
        thetaRepository.setOptions(options)
        assertTrue(true, "wlanFrequencyClMode")
    }
}
