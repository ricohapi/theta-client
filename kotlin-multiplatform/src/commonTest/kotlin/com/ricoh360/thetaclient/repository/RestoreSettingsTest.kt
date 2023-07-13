package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import io.ktor.client.network.sockets.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class RestoreSettingsTest {
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
     * call restoreSettings. for THETA Z1
     */
    @Test
    fun restoreSettingsForZ1Test() = runTest {
        val config = ThetaRepository.Config(
            dateTime = "2022:11:28 09:33:53+09:00",
            language = ThetaRepository.LanguageEnum.JA,
            offDelay = ThetaRepository.OffDelayEnum.OFF_DELAY_10M,
            sleepDelay = ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M,
            shutterVolume = 100
        )

        var counter = 0
        MockApiClient.onRequest = { request ->
            // check request
            when (counter++) {
                0 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        dateTimeZone = config.dateTime,
                    )
                }
                2 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        language = config.language!!.value,
                        sleepDelay = config.sleepDelay!!.sec,
                        offDelay = config.offDelay!!.sec,
                        shutterVolume = config.shutterVolume
                    )
                }
            }

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        ThetaRepository.restoreConfig = config
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_Z1
        thetaRepository.restoreSettings()
        assertEquals(counter, 2, "call restoreSettings")
    }

    /**
     * call restoreSettings. for THETA S
     */
    @Test
    fun restoreSettingsForSTest() = runTest {
        val config = ThetaRepository.Config(
            dateTime = "2022:11:28 09:33:53+09:00",
            language = null,
            offDelay = ThetaRepository.OffDelayEnum.OFF_DELAY_10M,
            sleepDelay = ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M,
            shutterVolume = 100
        )

        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++
            // check request
            when (index) {
                0 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        dateTimeZone = config.dateTime,
                    )
                }
                2 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        language = null,
                        sleepDelay = config.sleepDelay!!.sec,
                        offDelay = config.offDelay!!.sec,
                        shutterVolume = config.shutterVolume
                    )
                }
            }

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        ThetaRepository.restoreConfig = config
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_S
        thetaRepository.restoreSettings()
        assertEquals(counter, 2, "call restoreSettings")
    }

    /**
     * call restoreSettings. Only datetime
     */
    @Test
    fun restoreSettingsOnlyDatetimeTest() = runTest {
        val config = ThetaRepository.Config(
            dateTime = "2022:11:28 09:33:53+09:00",
        )

        var counter = 0
        MockApiClient.onRequest = { request ->
            // check request
            when (counter++) {
                0 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        dateTimeZone = config.dateTime,
                    )
                }
                2 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        language = config.language!!.value,
                        sleepDelay = config.sleepDelay!!.sec,
                        offDelay = config.offDelay!!.sec,
                        shutterVolume = config.shutterVolume
                    )
                }
            }

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        ThetaRepository.restoreConfig = config
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_Z1
        thetaRepository.restoreSettings()
        assertEquals(counter, 1, "call restoreSettings")
    }

    /**
     * Error not json response to restoreSettings call
     */
    @Test
    fun restoreSettingsNotJsonResponseTest() = runTest {
        MockApiClient.onRequest = { _ ->
            ByteReadChannel("Not json")
        }

        try {
            val thetaRepository = ThetaRepository(endpoint)
            val config = ThetaRepository.Config(
                dateTime = "2022:11:28 09:33:53+09:00",
                language = null,
                offDelay = ThetaRepository.OffDelayEnum.OFF_DELAY_10M,
                sleepDelay = ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M,
                shutterVolume = 100
            )
            ThetaRepository.restoreConfig = config
            thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_Z1
            thetaRepository.restoreSettings()
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(
                e.message!!.indexOf("json", 0, true) >= 0 ||
                    e.message!!.indexOf("Illegal", 0, true) >= 0,
                "error response"
            )
        }
    }

    /**
     * Error response to restoreSettings call
     */
    @Test
    fun restoreSettingsErrorResponseTest() = runTest {
        MockApiClient.onRequest = { _ ->
            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_error.json").readText())
        }

        try {
            val thetaRepository = ThetaRepository(endpoint)
            val config = ThetaRepository.Config(
                dateTime = "2022:11:28 09:33:53+09:00",
                language = null,
                offDelay = ThetaRepository.OffDelayEnum.OFF_DELAY_10M,
                sleepDelay = ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M,
                shutterVolume = 100
            )
            ThetaRepository.restoreConfig = config
            thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_Z1
            thetaRepository.restoreSettings()
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Error response and status error to restoreSettings call
     */
    @Test
    fun restoreSettingsErrorResponseAndStatusErrorTest() = runTest {
        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_error.json").readText())
        }

        try {
            val thetaRepository = ThetaRepository(endpoint)
            val config = ThetaRepository.Config(
                dateTime = "2022:11:28 09:33:53+09:00",
                language = null,
                offDelay = ThetaRepository.OffDelayEnum.OFF_DELAY_10M,
                sleepDelay = ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M,
                shutterVolume = 100
            )
            ThetaRepository.restoreConfig = config
            thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_Z1
            thetaRepository.restoreSettings()
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "error response")
        }
    }

    /**
     * Status error to restoreSettings call
     */
    @Test
    fun restoreSettingsStatusErrorTest() = runTest {
        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel("Not json")
        }

        try {
            val thetaRepository = ThetaRepository(endpoint)
            val config = ThetaRepository.Config(
                dateTime = "2022:11:28 09:33:53+09:00",
                language = null,
                offDelay = ThetaRepository.OffDelayEnum.OFF_DELAY_10M,
                sleepDelay = ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M,
                shutterVolume = 100
            )
            ThetaRepository.restoreConfig = config
            thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_Z1
            thetaRepository.restoreSettings()
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("503", 0, true) >= 0, "status error")
        }
    }

    /**
     * Error exception to restoreSettings call
     */
    @Test
    fun restoreSettingsExceptionTest() = runTest {
        MockApiClient.onRequest = { _ ->
            throw ConnectTimeoutException("timeout")
        }

        try {
            val thetaRepository = ThetaRepository(endpoint)
            val config = ThetaRepository.Config(
                dateTime = "2022:11:28 09:33:53+09:00",
                language = null,
                offDelay = ThetaRepository.OffDelayEnum.OFF_DELAY_10M,
                sleepDelay = ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M,
                shutterVolume = 100
            )
            ThetaRepository.restoreConfig = config
            thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_Z1
            thetaRepository.restoreSettings()
            assertTrue(false, "response is normal.")
        } catch (e: ThetaRepository.NotConnectedException) {
            assertTrue(e.message!!.indexOf("time", 0, true) >= 0, "timeout exception")
        }
    }
}
