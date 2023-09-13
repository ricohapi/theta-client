package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalSerializationApi::class, ExperimentalCoroutinesApi::class)
class SetBluetoothDeviceTest {
    private val endpoint = "http://192.168.1.1:80/"

    @BeforeTest
    fun setup() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @AfterTest
    fun teardown() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @Test
    fun setBluetoothDeviceTest() = runTest {
        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            ByteReadChannel(Resource("src/commonTest/resources/setBluetoothDevice/set_bluetooth_device_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        kotlin.runCatching {
            thetaRepository.setBluetoothDevice("60b2eefa-865f-4fef-b79c-7a1989e8b14c")
        }.onSuccess {
            assertTrue(true, "setBluetoothDevice")
            assertTrue(it.length > 0, "device name")
        }.onFailure {
            println("setBluetoothDevice: ${it.toString()}")
            assertTrue(false, "setBluetoothDevice")
        }
    }
}