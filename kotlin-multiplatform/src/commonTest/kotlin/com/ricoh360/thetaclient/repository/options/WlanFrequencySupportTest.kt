package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.WlanFrequency
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class WlanFrequencySupportTest {
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
     * Get option wlanFrequencySupport.
     */
    @Test
    fun getOptionWlanFrequencySupportTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.WlanFrequencySupport
        )
        val stringOptionNames = listOf(
            "_wlanFrequencySupport"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_wlan_frequency_support_with_unknown.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(
            options.wlanFrequencySupport,
            listOf(
                ThetaRepository.WlanFrequencyEnum.GHZ_2_4,
                ThetaRepository.WlanFrequencyEnum.GHZ_5_2,
                ThetaRepository.WlanFrequencyEnum.GHZ_5_8,
                ThetaRepository.WlanFrequencyEnum.UNKNOWN,
                ThetaRepository.WlanFrequencyEnum.UNKNOWN
            ),
            "wlanFrequencySupport"
        )
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionWlanFrequencySupportTest() = runTest {
        val values = Pair(
            listOf(WlanFrequency.GHZ_2_4, WlanFrequency.GHZ_5),
            listOf(
                ThetaRepository.WlanFrequencyEnum.GHZ_2_4,
                ThetaRepository.WlanFrequencyEnum.GHZ_5
            )
        )

        val orgOptions = Options(
            _wlanFrequencySupport = values.first
        )
        val optionsTR = ThetaRepository.Options(orgOptions)
        assertEquals(
            optionsTR.wlanFrequencySupport,
            values.second,
            "wlanFrequencySupport ${values.second}"
        )

        val orgOptionsTR = ThetaRepository.Options(
            wlanFrequencySupport = values.second
        )
        val options = orgOptionsTR.toOptions()
        assertEquals(
            options._wlanFrequencySupport,
            values.first,
            "_wlanFrequencySupport ${values.first}"
        )
    }
}
