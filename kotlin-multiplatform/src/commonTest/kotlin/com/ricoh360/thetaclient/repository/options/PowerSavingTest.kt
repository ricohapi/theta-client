package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.PowerSaving
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class PowerSavingTest {
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
     * Get option powerSaving.
     */
    @Test
    fun getOptionPowerSavingTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.PowerSaving
        )
        val stringOptionNames = listOf(
            "_powerSaving"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_powersaving-on.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.powerSaving, ThetaRepository.PowerSavingEnum.ON, "powerSaving")
    }

    /**
     * Set option powerSaving.
     */
    @Test
    fun setOptionPowerSavingTest() = runTest {
        val value = Pair(ThetaRepository.PowerSavingEnum.ON, PowerSaving.ON)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, powerSaving = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            powerSaving = value.first
        )
        kotlin.runCatching {
            thetaRepository.setOptions(options)
        }.onSuccess {
            assertTrue(true, "setOptions powerSaving")
        }.onFailure {
            println(it.toString())
            assertTrue(false, "setOptions powerSaving")
        }
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionPowerSavingTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.PowerSavingEnum.ON, PowerSaving.ON),
            Pair(ThetaRepository.PowerSavingEnum.OFF, PowerSaving.OFF),
        )

        values.forEach {
            val orgOptions = Options(
                _powerSaving = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.powerSaving, it.first, "powerSaving ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                powerSaving = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._powerSaving, it.second, "powerSaving ${it.second}")
        }
    }
}
