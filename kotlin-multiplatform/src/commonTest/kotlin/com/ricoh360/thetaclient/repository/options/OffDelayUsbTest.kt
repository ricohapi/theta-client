package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class OffDelayUsbTest {
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
     * Get option offDelay.
     */
    @Test
    fun getOptionOffDelayUsbTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.OffDelayUsb
        )
        val stringOptionNames = listOf(
            "_offDelayUSB"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_off_delay_usb_600.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.offDelayUsb, ThetaRepository.OffDelayUsbEnum.OFF_DELAY_10M, "offDelayUsb")
    }

    /**
     * Set option offDelayUsb.
     */
    @Test
    fun setOptionOffDelayUsbTest() = runTest {
        val value = Pair(ThetaRepository.OffDelayUsbEnum.OFF_DELAY_10M, 600)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, offDelayUsb = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            offDelayUsb = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionOffDelayUsbTest() {
        val values = listOf(
            Pair(ThetaRepository.OffDelayUsbEnum.DISABLE, 65535),
            Pair(ThetaRepository.OffDelayUsbEnum.OFF_DELAY_10M, 60 * 10),
            Pair(ThetaRepository.OffDelayUsbEnum.OFF_DELAY_1H, 60 * 60),
            Pair(ThetaRepository.OffDelayUsbEnum.OFF_DELAY_2H, 60 * 60 * 2),
            Pair(ThetaRepository.OffDelayUsbEnum.OFF_DELAY_4H, 60 * 60 * 4),
            Pair(ThetaRepository.OffDelayUsbEnum.OFF_DELAY_8H, 60 * 60 * 8),
            Pair(ThetaRepository.OffDelayUsbEnum.OFF_DELAY_12H, 60 * 60 * 12),
            Pair(ThetaRepository.OffDelayUsbEnum.OFF_DELAY_18H, 60 * 60 * 18),
            Pair(ThetaRepository.OffDelayUsbEnum.OFF_DELAY_24H, 60 * 60 * 24),
            Pair(ThetaRepository.OffDelayUsbEnum.OFF_DELAY_2D, 60 * 60 * 24 * 2),
            Pair(ThetaRepository.OffDelayUsbSec(60), 60)
        )

        values.forEach {
            val orgOptions = Options(
                _offDelayUSB = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.offDelayUsb, it.first, "offDelayUsb ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                offDelayUsb = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._offDelayUSB, it.second, "offDelayUsb ${it.second}")
        }

        assertEquals(ThetaRepository.OffDelayUsbEnum.get(0), ThetaRepository.OffDelayUsbEnum.DISABLE, "DISABLE")
    }
}
