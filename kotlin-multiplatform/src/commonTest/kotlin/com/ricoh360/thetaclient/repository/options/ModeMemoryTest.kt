package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.ModeMemory
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ModeMemoryTest {
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
            ThetaRepository.OptionNameEnum.ModeMemory
        )
        val stringOptionNames = listOf(
            "_modeMemory"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_mode_memory_on.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.modeMemory, ThetaRepository.ModeMemoryEnum.ON)
    }

    /**
     * Get option UNKNOWN.
     */
    @Test
    fun getOptionUnknownTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.ModeMemory
        )
        val stringOptionNames = listOf(
            "_modeMemory"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_mode_memory_unknown.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.modeMemory, ThetaRepository.ModeMemoryEnum.UNKNOWN)
    }

    /**
     * Set option.
     */
    @Test
    fun setOptionTest() = runTest {
        val value = Pair(ThetaRepository.ModeMemoryEnum.OFF, ModeMemory.OFF)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request = request, modeMemory = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            modeMemory = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert option.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.ModeMemoryEnum.UNKNOWN, ModeMemory.UNKNOWN),
            Pair(ThetaRepository.ModeMemoryEnum.ON, ModeMemory.ON),
            Pair(ThetaRepository.ModeMemoryEnum.OFF, ModeMemory.OFF),
        )

        values.forEach {
            val orgOptions = Options(
                _modeMemory = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.modeMemory, it.first, "_modeMemory ${it.second}")
        }
    }
}
