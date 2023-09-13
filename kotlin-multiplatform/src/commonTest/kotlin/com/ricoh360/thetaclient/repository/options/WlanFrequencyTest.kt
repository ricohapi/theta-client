package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.WlanFrequency
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class WlanFrequencyTest {
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
     * Get option wlanFrequency.
     */
    @Test
    fun getOptionWlanFrequencyTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.WlanFrequency
        )
        val stringOptionNames = listOf(
            "_wlanFrequency"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_wlan_frequency_5.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.wlanFrequency, ThetaRepository.WlanFrequencyEnum.GHZ_5, "wlanFrequency")
    }

    /**
     * Set option wlanFrequency.
     */
    @Test
    fun setOptionWlanFrequencyTest() = runTest {
        val value1 = Pair(ThetaRepository.WlanFrequencyEnum.GHZ_5, WlanFrequency.GHZ_5)
        val value2 = Pair(ThetaRepository.WlanFrequencyEnum.GHZ_2_4, WlanFrequency.GHZ_2_4)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, wlanFrequency = value1.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            wlanFrequency = value1.first
        )
        thetaRepository.setOptions(options)
        assertTrue(true, "wlanFrequency 5GHz")

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, wlanFrequency = value2.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }
        options.wlanFrequency = value2.first
        thetaRepository.setOptions(options)
        assertTrue(true, "wlanFrequency 2.4GHz")

    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionWlanFrequencyTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.WlanFrequencyEnum.GHZ_2_4, WlanFrequency.GHZ_2_4),
            Pair(ThetaRepository.WlanFrequencyEnum.GHZ_5, WlanFrequency.GHZ_5),
        )

        values.forEach {
            val orgOptions = Options(
                _wlanFrequency = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.wlanFrequency, it.first, "wlanFrequency ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                wlanFrequency = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._wlanFrequency, it.second, "wlanFrequency ${it.second}")
        }
    }
}
