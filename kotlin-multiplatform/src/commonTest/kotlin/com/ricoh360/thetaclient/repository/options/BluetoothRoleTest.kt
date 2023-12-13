package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.BluetoothRole
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class BluetoothRoleTest {
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
     * Get option bluetoothRole.
     */
    @Test
    fun getOptionBluetoothRoleTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.BluetoothRole
        )
        val stringOptionNames = listOf(
            "_bluetoothRole"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_bluetooth_role_central.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.bluetoothRole, ThetaRepository.BluetoothRoleEnum.CENTRAL)
    }

    /**
     * Set option bluetoothRole.
     */
    @Test
    fun setOptionBluetoothRoleTest() = runTest {
        val value = Pair(ThetaRepository.BluetoothRoleEnum.CENTRAL, BluetoothRole.CENTRAL)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, bluetoothRole = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            bluetoothRole = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionBluetoothRoleTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.BluetoothRoleEnum.CENTRAL, BluetoothRole.CENTRAL),
            Pair(ThetaRepository.BluetoothRoleEnum.PERIPHERAL, BluetoothRole.PERIPHERAL),
            Pair(ThetaRepository.BluetoothRoleEnum.CENTRAL_PERIPHERAL, BluetoothRole.CENTRAL_PERIPHERAL),
        )

        values.forEach {
            val orgOptions = Options(
                _bluetoothRole = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.bluetoothRole, it.first, "bluetoothRole ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                bluetoothRole = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._bluetoothRole, it.second, "bluetoothRole ${it.second}")
        }
    }
}
