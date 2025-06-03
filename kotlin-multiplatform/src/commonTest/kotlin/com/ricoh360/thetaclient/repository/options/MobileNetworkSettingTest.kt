package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.MobileNetworkSetting
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.Plan
import com.ricoh360.thetaclient.transferred.Roaming
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MobileNetworkSettingTest {
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
            ThetaRepository.OptionNameEnum.MobileNetworkSetting
        )
        val stringOptionNames = listOf(
            "_mobileNetworkSetting"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_mobile_network_setting_normal.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.mobileNetworkSetting?.roaming, ThetaRepository.RoamingEnum.OFF)
        assertEquals(options.mobileNetworkSetting?.plan, ThetaRepository.PlanEnum.SORACOM)
    }

    /**
     * Get option UNKNOWN.
     */
    @Test
    fun getOptionUnknownTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.MobileNetworkSetting
        )
        val stringOptionNames = listOf(
            "_mobileNetworkSetting"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_mobile_network_setting_unknown.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.mobileNetworkSetting?.roaming, ThetaRepository.RoamingEnum.UNKNOWN)
        assertEquals(options.mobileNetworkSetting?.plan, ThetaRepository.PlanEnum.UNKNOWN)
    }

    /**
     * Set option.
     */
    @Test
    fun setOptionTest() = runTest {
        val value = Pair(
            ThetaRepository.MobileNetworkSetting(
                roaming = ThetaRepository.RoamingEnum.ON,
                plan = ThetaRepository.PlanEnum.SORACOM_PLAN_DU,
            ), MobileNetworkSetting(
                roaming = Roaming.ON,
                plan = Plan.SORACOM_PLAN_DU,
            )
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, mobileNetworkSetting = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            mobileNetworkSetting = value.first
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
                ThetaRepository.MobileNetworkSetting(roaming = ThetaRepository.RoamingEnum.ON, plan = ThetaRepository.PlanEnum.SORACOM),
                MobileNetworkSetting(roaming = Roaming.ON, plan = Plan.SORACOM)
            ),
            Pair(
                ThetaRepository.MobileNetworkSetting(roaming = ThetaRepository.RoamingEnum.OFF, plan = ThetaRepository.PlanEnum.SORACOM_PLAN_DU),
                MobileNetworkSetting(roaming = Roaming.OFF, plan = Plan.SORACOM_PLAN_DU)
            ),
        )

        values.forEach {
            val orgOptions = Options(
                _mobileNetworkSetting = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.mobileNetworkSetting, it.first, "mobileNetworkSetting ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                mobileNetworkSetting = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._mobileNetworkSetting, it.second, "mobileNetworkSetting ${it.second}")
        }
    }
}
