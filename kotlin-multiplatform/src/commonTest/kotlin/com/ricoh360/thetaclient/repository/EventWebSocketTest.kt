package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.websocket.CameraEvent
import com.ricoh360.thetaclient.websocket.EventWebSocket
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EventWebSocketTest {
    private val endpoint = "http://192.168.1.1:80/"

    @BeforeTest
    fun setup() {
        MockApiClient.reset()
    }

    @AfterTest
    fun teardown() {
        MockApiClient.reset()
    }

    /**
     * EventWebSocket normal with options
     */
    @Test
    fun eventOptionsTest() = runTest {
        MockApiClient.onCallWebSocketConnect = {
            assertEquals(it, "ws://192.168.1.1/events")
        }

        val theta = ThetaRepository(endpoint)

        val webSocket = theta.getEventWebSocket()
        var onReceiveCalled = 0
        var onCloseCalled = 0
        val deferred = CompletableDeferred<Unit>()

        webSocket.start(object : EventWebSocket.Callback {
            override fun onReceive(event: CameraEvent) {
                onReceiveCalled += 1
                val options = event.options
                assertNotNull(options)
                assertNotNull(options.fileFormat)
                assertNull(options.aperture)
                assertNull(event.state)
            }

            override fun onClose() {
                onCloseCalled += 1
                deferred.complete(Unit)
            }
        })

        runBlocking {
            delay(100)
        }
        val jsonOptions =
            Resource("src/commonTest/resources/EventWebSocket/options.json").readText()
        MockApiClient.mockWebSocketHttpClient.sendMessage(jsonOptions)

        webSocket.stop()

        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }

        assertEquals(onReceiveCalled, 1)
        assertEquals(onCloseCalled, 1)
    }

    /**
     * EventWebSocket normal with state
     */
    @Test
    fun eventStateTest() = runTest {
        MockApiClient.onCallWebSocketConnect = {
            assertEquals(it, "ws://192.168.1.1/events")
        }

        val theta = ThetaRepository(endpoint)

        val webSocket = theta.getEventWebSocket()
        var onReceiveCalled = 0
        var onCloseCalled = 0
        val deferred = CompletableDeferred<Unit>()

        webSocket.start(object : EventWebSocket.Callback {
            override fun onReceive(event: CameraEvent) {
                onReceiveCalled += 1
                val state = event.state
                assertNotNull(state)
                assertNotNull(state.batteryTemp)
                assertNotNull(state.boardTemp)
                assertNull(state.chargingState)
                assertNull(event.options)
            }

            override fun onClose() {
                onCloseCalled += 1
                deferred.complete(Unit)
            }
        })

        runBlocking {
            delay(100)
        }
        val jsonOptions = Resource("src/commonTest/resources/EventWebSocket/state.json").readText()
        MockApiClient.mockWebSocketHttpClient.sendMessage(jsonOptions)

        webSocket.stop()

        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }

        assertEquals(onReceiveCalled, 1)
        assertEquals(onCloseCalled, 1)
    }

    /**
     * EventWebSocket not json response
     */
    @Test
    fun eventNotJsonTest() = runTest {
        MockApiClient.onCallWebSocketConnect = null

        val theta = ThetaRepository(endpoint)

        val webSocket = theta.getEventWebSocket()
        var onReceiveCalled = 0
        var onCloseCalled = 0
        val deferred = CompletableDeferred<Unit>()

        webSocket.start(object : EventWebSocket.Callback {
            override fun onReceive(event: CameraEvent) {
                onReceiveCalled += 1
            }

            override fun onClose() {
                onCloseCalled += 1
                deferred.complete(Unit)
            }
        })

        runBlocking {
            delay(100)
        }
        MockApiClient.mockWebSocketHttpClient.sendMessage("not json")

        webSocket.stop()

        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }

        assertEquals(onReceiveCalled, 0)
        assertEquals(onCloseCalled, 1)
    }

    /**
     * EventWebSocket response exception
     */
    @Test
    fun eventReceiveExceptionTest() = runTest {
        MockApiClient.onCallWebSocketConnect = null

        val theta = ThetaRepository(endpoint)

        val webSocket = theta.getEventWebSocket()
        var onReceiveCalled = 0
        var onCloseCalled = 0
        val deferred = CompletableDeferred<Unit>()

        webSocket.start(object : EventWebSocket.Callback {
            override fun onReceive(event: CameraEvent) {
                onReceiveCalled += 1
            }

            override fun onClose() {
                onCloseCalled += 1
                deferred.complete(Unit)
            }
        })

        runBlocking {
            delay(100)
        }
        MockApiClient.mockWebSocketHttpClient.sendException("receive error")

        runBlocking {
            withTimeout(5_000) {
                deferred.await()
            }
        }

        assertEquals(onReceiveCalled, 0)
        assertEquals(onCloseCalled, 1)
    }

    /**
     * EventWebSocket connection exception
     */
    @Test
    fun webSocketConnectExceptionTest() = runTest {
        MockApiClient.onCallWebSocketConnect = {
            throw Exception("connect")
        }

        val theta = ThetaRepository(endpoint)

        val webSocket = theta.getEventWebSocket()
        var onReceiveCalled = 0
        var onCloseCalled = 0

        try {
            webSocket.start(object : EventWebSocket.Callback {
                override fun onReceive(event: CameraEvent) {
                    onReceiveCalled += 1
                }

                override fun onClose() {
                    onCloseCalled += 1
                }
            })
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertEquals(e.message, "connect")
        } catch (e: Exception) {
            assertTrue(false, "other error")
        }

        assertEquals(onReceiveCalled, 0)
        assertEquals(onCloseCalled, 0)
    }

}
