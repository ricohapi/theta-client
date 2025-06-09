package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.BluetoothPower
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BluetoothPowerTest {
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
     * Get option bluetoothPower.
     */
    @Test
    fun getOptionBluetoothPowerTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.BluetoothPower
        )
        val stringOptionNames = listOf(
            "_bluetoothPower"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_bluetooth_power_on.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.bluetoothPower, ThetaRepository.BluetoothPowerEnum.ON)
    }

    /**
     * Set option bluetoothPower.
     */
    @Test
    fun setOptionBluetoothPowerTest() = runTest {
        val value = Pair(ThetaRepository.BluetoothPowerEnum.ON, BluetoothPower.ON)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, bluetoothPower = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            bluetoothPower = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionBluetoothPowerTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.BluetoothPowerEnum.ON, BluetoothPower.ON),
            Pair(ThetaRepository.BluetoothPowerEnum.OFF, BluetoothPower.OFF),
        )

        values.forEach {
            val orgOptions = Options(
                _bluetoothPower = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.bluetoothPower, it.first, "bluetoothPower ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                bluetoothPower = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._bluetoothPower, it.second, "bluetoothPower ${it.second}")
        }
    }
}
