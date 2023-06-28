package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.ApiClient
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ThetaRepositoryTest {
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
     * New instance. for THETA Z1
     */
    @Test
    fun newInstanceForZ1Test() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/info/info_z1.json").readText(),
            Resource("src/commonTest/resources/getOptions/get_options_init_z1_done.json").readText()
        )
        val requestPathArray = arrayOf(
            "/osc/info",
            "/osc/commands/execute"
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++

            // check request
            assertEquals(request.url.encodedPath, requestPathArray[index], "request path")
            when (index) {
                4 -> {
                    CheckRequest.checkGetOptions(
                        request,
                        listOf(
                            "dateTimeZone",
                            "offDelay",
                            "sleepDelay",
                            "_shutterVolume",
                            "_language"
                        )
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        ThetaRepository.newInstance(endpoint)
        assertNotNull(ThetaRepository.restoreConfig, "restoreConfig")
        ThetaRepository.restoreConfig?.let {
            assertNotNull(it.dateTime, "dateTime")
            assertNotNull(it.language, "language")
            assertNotNull(it.offDelay, "offDelay")
            assertNotNull(it.sleepDelay, "sleepDelay")
            assertNotNull(it.shutterVolume, "shutterVolume")
        }
    }

    /**
     * New instance. for THETA S
     */
    @Test
    fun newInstanceForSTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/info/info_s.json").readText(),
            Resource("src/commonTest/resources/state/state_s_api1.json").readText(),
            Resource("src/commonTest/resources/startSession/start_session_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/getOptions/get_options_init_s_done.json").readText()
        )
        val requestPathArray = arrayOf(
            "/osc/info",
            "/osc/state",
            "/osc/commands/execute",
            "/osc/commands/execute",
            "/osc/commands/execute"
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++

            // check request
            assertEquals(request.url.encodedPath, requestPathArray[index], "request path")
            when (index) {
                2 -> {
                    CheckRequest.checkCommandName(request, "camera.startSession")
                }
                3 -> {
                    CheckRequest.checkSetOptions(request, clientVersion = 2)
                }
                4 -> {
                    CheckRequest.checkGetOptions(
                        request,
                        listOf("dateTimeZone", "offDelay", "sleepDelay", "_shutterVolume")
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        ThetaRepository.newInstance(endpoint)
        assertNotNull(ThetaRepository.restoreConfig, "restoreConfig")
        ThetaRepository.restoreConfig?.let {
            assertNotNull(it.dateTime, "dateTime")
            assertNull(it.language, "language")
            assertNotNull(it.offDelay, "offDelay")
            assertNotNull(it.sleepDelay, "sleepDelay")
            assertNotNull(it.shutterVolume, "shutterVolume")
        }
    }

    /**
     * New instance. for THETA SC
     */
    @Test
    fun newInstanceForSCTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/info/info_sc.json").readText(),
            Resource("src/commonTest/resources/state/state_s_api1.json").readText(),
            Resource("src/commonTest/resources/startSession/start_session_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/getOptions/get_options_init_s_done.json").readText()
        )
        val requestPathArray = arrayOf(
            "/osc/info",
            "/osc/state",
            "/osc/commands/execute",
            "/osc/commands/execute",
            "/osc/commands/execute"
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++

            // check request
            assertEquals(request.url.encodedPath, requestPathArray[index], "request path")
            when (index) {
                2 -> {
                    CheckRequest.checkCommandName(request, "camera.startSession")
                }
                3 -> {
                    CheckRequest.checkSetOptions(request, clientVersion = 2)
                }
                4 -> {
                    CheckRequest.checkGetOptions(
                        request,
                        listOf("dateTimeZone", "offDelay", "sleepDelay", "_shutterVolume")
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        ThetaRepository.newInstance(endpoint)
        assertNotNull(ThetaRepository.restoreConfig, "restoreConfig")
        ThetaRepository.restoreConfig?.let {
            assertNotNull(it.dateTime, "dateTime")
            assertNull(it.language, "language")
            assertNotNull(it.offDelay, "offDelay")
            assertNotNull(it.sleepDelay, "sleepDelay")
            assertNotNull(it.shutterVolume, "shutterVolume")
        }
    }

    /**
     * New instance. for old THETA S
     */
    @Test
    fun newInstanceForOldSTest() = runTest {
        // setup
        MockApiClient.onRequest = { _ ->
            ByteReadChannel(Resource("src/commonTest/resources/info/info_s_old.json").readText())
        }

        // execute
        try {
            ThetaRepository.newInstance(endpoint)
            assertTrue(false, "newInstance")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("Unsupported RICOH THETA S") >= 0, "Unsupported exception")
        }
    }

    /**
     * New instance with timeout
     */
    @Test
    fun newInstanceWithTimeoutTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/info/info_z1.json").readText(),
            Resource("src/commonTest/resources/getOptions/get_options_init_z1_done.json").readText()
        )
        var counter = 0
        MockApiClient.onRequest = { _ ->
            val index = counter++
            ByteReadChannel(responseArray[index])
        }

        // execute
        val timeout = ThetaRepository.Timeout(
            connectTimeout = 1L,
            requestTimeout = 2L,
            socketTimeout = 3L
        )
        ThetaRepository.newInstance(endpoint, timeout = timeout)
        assertNotNull(ThetaRepository.restoreConfig, "restoreConfig")
        assertEquals(ApiClient.timeout.connectTimeout, timeout.connectTimeout, "connectTimeout")
        assertEquals(ApiClient.timeout.requestTimeout, timeout.requestTimeout, "requestTimeout")
        assertEquals(ApiClient.timeout.socketTimeout, timeout.socketTimeout, "socketTimeout")
    }

    /**
     * New instance with config for THETA Z1
     */
    @Test
    fun newInstanceWithConfigZ1Test() = runTest {
        val config = ThetaRepository.Config(
            dateTime = "2022:11:28 09:33:53+09:00",
            language = ThetaRepository.LanguageEnum.JA,
            offDelay = ThetaRepository.OffDelayEnum.OFF_DELAY_10M,
            sleepDelay = ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M,
            shutterVolume = 100
        )

        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/info/info_z1.json").readText(),
            Resource("src/commonTest/resources/getOptions/get_options_init_z1_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText()
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++
            when (index) {
                2 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        dateTimeZone = config.dateTime,
                    )
                }
                3 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        dateTimeZone = null,
                        language = config.language!!.value,
                        sleepDelay = config.sleepDelay!!.sec,
                        offDelay = config.offDelay!!.sec,
                        shutterVolume = config.shutterVolume
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        ThetaRepository.newInstance(endpoint, config = config)
        assertNotNull(ThetaRepository.restoreConfig, "restoreConfig")
        assertNotNull(ThetaRepository.initConfig, "initConfig")
        ThetaRepository.initConfig?.let {
            assertEquals(it.dateTime, config.dateTime, "dateTime")
            assertEquals(it.language, config.language, "language")
            assertEquals(it.offDelay, config.offDelay, "offDelay")
            assertEquals(it.sleepDelay, config.sleepDelay, "sleepDelay")
            assertEquals(it.shutterVolume, config.shutterVolume, "shutterVolume")
        }
    }

    /**
     * New instance with config for THETA S
     */
    @Test
    fun newInstanceWithConfigSTest() = runTest {
        val config = ThetaRepository.Config(
            dateTime = "2022:11:28 09:33:53+09:00",
            language = ThetaRepository.LanguageEnum.JA,
            offDelay = ThetaRepository.OffDelayEnum.OFF_DELAY_10M,
            sleepDelay = ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M,
            shutterVolume = 100
        )

        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/info/info_s.json").readText(),
            Resource("src/commonTest/resources/state/state_s_api1.json").readText(),
            Resource("src/commonTest/resources/startSession/start_session_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/getOptions/get_options_init_s_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText()
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++
            when (index) {
                5 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        dateTimeZone = config.dateTime,
                    )
                }
                6 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        dateTimeZone = null,
                        language = null,
                        sleepDelay = config.sleepDelay!!.sec,
                        offDelay = config.offDelay!!.sec,
                        shutterVolume = config.shutterVolume
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        ThetaRepository.newInstance(endpoint, config = config)
        assertNotNull(ThetaRepository.restoreConfig, "restoreConfig")
        assertNotNull(ThetaRepository.initConfig, "initConfig")
        ThetaRepository.initConfig?.let {
            assertEquals(it.dateTime, config.dateTime, "dateTime")
            assertEquals(it.language, config.language, "language")
            assertEquals(it.offDelay, config.offDelay, "offDelay")
            assertEquals(it.sleepDelay, config.sleepDelay, "sleepDelay")
            assertEquals(it.shutterVolume, config.shutterVolume, "shutterVolume")
        }
    }

    /**
     * New instance with config for THETA SC
     */
    @Test
    fun newInstanceWithConfigSCTest() = runTest {
        val config = ThetaRepository.Config(
            dateTime = "2022:11:28 09:33:53+09:00",
            language = ThetaRepository.LanguageEnum.JA,
            offDelay = ThetaRepository.OffDelayEnum.OFF_DELAY_10M,
            sleepDelay = ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M,
            shutterVolume = 100
        )

        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/info/info_sc.json").readText(),
            Resource("src/commonTest/resources/state/state_s_api1.json").readText(),
            Resource("src/commonTest/resources/startSession/start_session_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/getOptions/get_options_init_s_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText()
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++
            when (index) {
                5 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        dateTimeZone = config.dateTime,
                    )
                }
                6 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        dateTimeZone = null,
                        language = null,
                        sleepDelay = config.sleepDelay!!.sec,
                        offDelay = config.offDelay!!.sec,
                        shutterVolume = config.shutterVolume
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        ThetaRepository.newInstance(endpoint, config = config)
        assertNotNull(ThetaRepository.restoreConfig, "restoreConfig")
        assertNotNull(ThetaRepository.initConfig, "initConfig")
        ThetaRepository.initConfig?.let {
            assertEquals(it.dateTime, config.dateTime, "dateTime")
            assertEquals(it.language, config.language, "language")
            assertEquals(it.offDelay, config.offDelay, "offDelay")
            assertEquals(it.sleepDelay, config.sleepDelay, "sleepDelay")
            assertEquals(it.shutterVolume, config.shutterVolume, "shutterVolume")
        }
    }

    /**
     * New instance exception info
     */
    @Test
    fun newInstanceInfoExceptionTest() = runTest {
        // setup
        MockApiClient.onRequest = { request ->
            assertEquals(request.url.encodedPath, "/osc/info", "request path")
            throw Exception("info exception")
        }

        // execute
        try {
            ThetaRepository.newInstance(endpoint)
            assertTrue(false, "newInstance")
        } catch (e: ThetaRepository.NotConnectedException) {
            assertTrue(e.message!!.indexOf("info exception") >= 0, "exception info")
        }
    }

    /**
     * New instance exception state
     */
    @Test
    fun newInstanceStateExceptionTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/info/info_s.json").readText()
        )
        val requestPathArray = arrayOf(
            "/osc/info",
            "/osc/state"
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++

            // check request
            assertEquals(request.url.encodedPath, requestPathArray[index], "request path")
            when (index) {
                1 -> {
                    throw ThetaRepository.NotConnectedException("state exception")
                }
            }
            ByteReadChannel(responseArray[index])
        }

        // execute
        try {
            ThetaRepository.newInstance(endpoint)
            assertTrue(false, "newInstance")
        } catch (e: ThetaRepository.NotConnectedException) {
            assertTrue(e.message!!.indexOf("state exception") >= 0, "exception state")
        }
    }

    /**
     * New instance. Error of startSession.
     */
    @Test
    fun newInstanceErrorStartSessionTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/info/info_s.json").readText(),
            Resource("src/commonTest/resources/state/state_s_api1.json").readText(),
            Resource("src/commonTest/resources/startSession/start_session_error.json").readText()
        )
        val requestPathArray = arrayOf(
            "/osc/info",
            "/osc/state",
            "/osc/commands/execute"
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++

            // check request
            assertEquals(request.url.encodedPath, requestPathArray[index], "request path")
            when (index) {
                2 -> {
                    CheckRequest.checkCommandName(request, "camera.startSession")
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        try {
            ThetaRepository.newInstance(endpoint)
            assertTrue(false, "newInstance")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest error") >= 0, "startSession error")
        }
    }

    /**
     * New instance. Error of change api.
     */
    @Test
    fun newInstanceErrorChangeApiTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/info/info_s.json").readText(),
            Resource("src/commonTest/resources/state/state_s_api1.json").readText(),
            Resource("src/commonTest/resources/startSession/start_session_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_error.json").readText()
        )
        val requestPathArray = arrayOf(
            "/osc/info",
            "/osc/state",
            "/osc/commands/execute",
            "/osc/commands/execute"
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++

            // check request
            assertEquals(request.url.encodedPath, requestPathArray[index], "request path")
            when (index) {
                2 -> {
                    CheckRequest.checkCommandName(request, "camera.startSession")
                }
                3 -> {
                    CheckRequest.checkCommandName(request, "camera.setOptions")
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        try {
            ThetaRepository.newInstance(endpoint)
            assertTrue(false, "newInstance")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest error") >= 0, "change api error")
        }
    }

    /**
     * New instance. Error of get config.
     */
    @Test
    fun newInstanceErrorGetConfigTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/info/info_z1.json").readText(),
            Resource("src/commonTest/resources/getOptions/get_options_error.json").readText()
        )
        val requestPathArray = arrayOf(
            "/osc/info",
            "/osc/commands/execute"
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++

            // check request
            assertEquals(request.url.encodedPath, requestPathArray[index], "request path")
            ByteReadChannel(responseArray[index])
        }

        // execute
        try {
            ThetaRepository.newInstance(endpoint)
            assertTrue(false, "newInstance")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest error") >= 0, "get config error")
        }
    }

    /**
     * New instance. Error of set config.
     */
    @Test
    fun newInstanceErrorSetConfigTest() = runTest {
        val config = ThetaRepository.Config(
            dateTime = "2022:11:28 09:33:53+09:00",
            language = ThetaRepository.LanguageEnum.JA,
            offDelay = ThetaRepository.OffDelayEnum.OFF_DELAY_10M,
            sleepDelay = ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M,
            shutterVolume = 100
        )

        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/info/info_z1.json").readText(),
            Resource("src/commonTest/resources/getOptions/get_options_init_z1_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_error.json").readText()
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++
            when (index) {
                2 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        dateTimeZone = config.dateTime,
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        try {
            ThetaRepository.newInstance(endpoint, config = config)
            assertTrue(false, "newInstance")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest error") >= 0, "set config error")
        }
    }

    /**
     * New instance. Error of Json.
     */
    @Test
    fun newInstanceInfoJsonErrorTest() = runTest {
        // setup
        MockApiClient.onRequest = { request ->
            assertEquals(request.url.encodedPath, "/osc/info", "request path")
            ByteReadChannel("Not json")
        }

        // execute
        try {
            ThetaRepository.newInstance(endpoint)
            assertTrue(false, "newInstance")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(
                e.message!!.indexOf("json", 0, true) >= 0 ||
                    e.message!!.indexOf("Illegal", 0, true) >= 0,
                "error response"
            )
        }
    }

    /**
     * New instance. Error of Status.
     */
    @Test
    fun newInstanceInfoStatusErrorTest() = runTest {
        // setup
        MockApiClient.onRequest = { request ->
            assertEquals(request.url.encodedPath, "/osc/info", "request path")
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel("Not json")
        }

        // execute
        try {
            ThetaRepository.newInstance(endpoint)
            assertTrue(false, "newInstance")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("503", 0, true) >= 0, "status error")
        }
    }

    /**
     * Check Theta SC2 and Theta SC2 for business
     *
     */
    @Test
    fun thetaModelTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/info/info_sc2.json").readText(),
            Resource("src/commonTest/resources/getOptions/get_options_done.json").readText()
        )

        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++
            ByteReadChannel(responseArray[index])
        }

        ThetaRepository.newInstance(endpoint)
        assertTrue(ThetaRepository.ThetaModel.THETA_SC2 == ThetaRepository.ThetaModel.get("RICOH THETA SC2", "00118200"), "model SC2")
        assertTrue(ThetaRepository.ThetaModel.THETA_SC2_B == ThetaRepository.ThetaModel.get("RICOH THETA SC2", "40118200"), "model SC2 for business")
        assertNull(ThetaRepository.ThetaModel.get("RICOH THETA SC2"), "No firmware version")
        assertTrue(ThetaRepository.ThetaModel.THETA_Z1 == ThetaRepository.ThetaModel.get("RICOH THETA Z1"), "model Z1")
    }

}
