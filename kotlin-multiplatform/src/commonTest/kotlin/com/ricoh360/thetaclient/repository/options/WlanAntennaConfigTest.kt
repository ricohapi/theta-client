package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.WlanAntennaConfig
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class WlanAntennaConfigTest {
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
            ThetaRepository.OptionNameEnum.WlanAntennaConfig
        )
        val stringOptionNames = listOf(
            "_wlanAntennaConfig"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_wlan_antenna_config_siso.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.wlanAntennaConfig, ThetaRepository.WlanAntennaConfigEnum.SISO)
    }

    /**
     * Get option UNKNOWN.
     */
    @Test
    fun getOptionUnknownTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.WlanAntennaConfig
        )
        val stringOptionNames = listOf(
            "_wlanAntennaConfig"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_wlan_antenna_config_unknown.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.wlanAntennaConfig, ThetaRepository.WlanAntennaConfigEnum.UNKNOWN)
    }

    /**
     * Set option.
     */
    @Test
    fun setOptionTest() = runTest {
        val value = Pair(ThetaRepository.WlanAntennaConfigEnum.SISO, WlanAntennaConfig.SISO)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, wlanAntennaConfig = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            wlanAntennaConfig = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.WlanAntennaConfigEnum.UNKNOWN, WlanAntennaConfig.UNKNOWN),
            Pair(ThetaRepository.WlanAntennaConfigEnum.SISO, WlanAntennaConfig.SISO),
            Pair(ThetaRepository.WlanAntennaConfigEnum.MIMO, WlanAntennaConfig.MIMO),
        )

        values.forEach {
            val orgOptions = Options(
                _wlanAntennaConfig = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.wlanAntennaConfig, it.first, "wlanAntennaConfig ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                wlanAntennaConfig = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._wlanAntennaConfig, it.second, "_wlanAntennaConfig ${it.second}")
        }
    }
}