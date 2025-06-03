package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.ApiClient
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

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
     * New instance. for other camera
     */
    @Test
    fun newInstanceForOtherTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/info/info_other.json").readText(),
            Resource("src/commonTest/resources/getOptions/get_options_init_s_done.json").readText()
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
                1 -> {
                    CheckRequest.checkGetOptions(
                        request,
                        listOf(
                            "dateTimeZone",
                            "offDelay",
                            "sleepDelay",
                            "_shutterVolume",
                        )
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        try {
            ThetaRepository.newInstance(endpoint)
            assertNotNull(ThetaRepository.restoreConfig, "restoreConfig")
            ThetaRepository.restoreConfig?.let {
                assertNotNull(it.dateTime, "dateTime")
                assertNull(it.language, "language")
                assertNotNull(it.offDelay, "offDelay")
                assertNotNull(it.sleepDelay, "sleepDelay")
                assertNotNull(it.shutterVolume, "shutterVolume")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            assertTrue(false, "Error. newInstance")
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
            connectTimeout = 10L,
            requestTimeout = 20L,
            socketTimeout = 30L
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
     * Check Theta models
     */
    @Test
    fun thetaModelTest() = runTest {
        val data = arrayOf(
            arrayOf("RICOH THETA SC2", "12345", ThetaRepository.ThetaModel.THETA_SC2),
            arrayOf("RICOH THETA SC2", "42345", ThetaRepository.ThetaModel.THETA_SC2_B),
            arrayOf("RICOH THETA SC2", null, ThetaRepository.ThetaModel.THETA_SC2),
            arrayOf("RICOH THETA X", null, ThetaRepository.ThetaModel.THETA_X),
            arrayOf("RICOH THETA X", "2222222", ThetaRepository.ThetaModel.THETA_X),
            arrayOf("RICOH THETA Z1", null, ThetaRepository.ThetaModel.THETA_Z1),
            arrayOf("RICOH THETA Z1", "4444444", ThetaRepository.ThetaModel.THETA_Z1),
            arrayOf("RICOH THETA V", null, ThetaRepository.ThetaModel.THETA_V),
            arrayOf("RICOH THETA S", null, ThetaRepository.ThetaModel.THETA_S),
            arrayOf("RICOH THETA SC", null, ThetaRepository.ThetaModel.THETA_SC),
            arrayOf("RICOH360 THETA A1", null, ThetaRepository.ThetaModel.THETA_A1),
        )
        for (item in data) {
            assertEquals(ThetaRepository.ThetaModel.get(item[0] as String, item[1] as? String), item[2], (item[2] as? ThetaRepository.ThetaModel)?.name ?: "null")
        }
    }

    @Test
    fun callSingleRequestTest() = runBlocking {
        var counter = 0
        val jsonString = Resource("src/commonTest/resources/info/info_z1.json").readText()
        MockApiClient.onRequest = { _ ->
            counter += 1
            runBlocking {
                delay(200)
            }
            assertEquals(counter, 1, "No concurrency")
            counter -= 1
            ByteReadChannel(jsonString)
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        val apiJobsList = listOf(
            launch {
                thetaRepository.getThetaInfo()
            },
            launch {
                thetaRepository.getThetaInfo()
            },
        )
        apiJobsList.joinAll()
    }

    @Test
    fun callSingleRequestTimeoutTest() = runBlocking {
        val jsonInfo = Resource("src/commonTest/resources/info/info_z1.json").readText()
        val jsonState = Resource("src/commonTest/resources/state/state_z1.json").readText()
        MockApiClient.onRequest = { request ->
            when (request.url.encodedPath) {
                "/osc/info" -> ByteReadChannel(jsonInfo)
                "/osc/state" -> {
                    runBlocking {
                        delay(500)
                    }
                    ByteReadChannel(jsonState)
                }

                else -> throw Exception("Error")
            }
        }

        val timeout = ThetaRepository.Timeout(
            requestTimeout = 100L,
        )
        // test
        val thetaRepository = ThetaRepository(endpoint, null, timeout)
        val apiJobsList = listOf(
            launch {
                thetaRepository.getThetaInfo()
                try {
                    thetaRepository.getThetaInfo()
                } catch (e: ThetaRepository.NotConnectedException) {
                    assertTrue(false)
                }
            },
            launch {
                try {
                    thetaRepository.getThetaState()
                    assertTrue(false)
                } catch (e: ThetaRepository.NotConnectedException) {
                    assertTrue((e.message?.indexOf("time", 0, true) ?: -1) >= 0, "timeout error")
                }
            },
        )
        apiJobsList.joinAll()
    }
}
