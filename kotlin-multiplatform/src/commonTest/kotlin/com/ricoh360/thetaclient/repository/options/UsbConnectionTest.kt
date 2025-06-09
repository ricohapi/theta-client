package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.UsbConnection
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UsbConnectionTest {
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
            ThetaRepository.OptionNameEnum.UsbConnection
        )
        val stringOptionNames = listOf(
            "_usbConnection"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_usb_connection_msc.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.usbConnection, ThetaRepository.UsbConnectionEnum.MSC)
    }

    /**
     * Get option UNKNOWN.
     */
    @Test
    fun getOptionUnknownTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.UsbConnection
        )
        val stringOptionNames = listOf(
            "_usbConnection"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_usb_connection_unknown.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.usbConnection, ThetaRepository.UsbConnectionEnum.UNKNOWN)
    }

    /**
     * Set option.
     */
    @Test
    fun setOptionTest() = runTest {
        val value = Pair(ThetaRepository.UsbConnectionEnum.MTP, UsbConnection.MTP)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, usbConnection = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            usbConnection = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.UsbConnectionEnum.UNKNOWN, UsbConnection.UNKNOWN),
            Pair(ThetaRepository.UsbConnectionEnum.MTP, UsbConnection.MTP),
            Pair(ThetaRepository.UsbConnectionEnum.MSC, UsbConnection.MSC),
        )

        values.forEach {
            val orgOptions = Options(
                _usbConnection = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.usbConnection, it.first, "usbConnection ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                usbConnection = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._usbConnection, it.second, "_usbConnection ${it.second}")
        }
    }
}