package com.ricoh360.thetaclient.capture

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CaptureMode
import com.ricoh360.thetaclient.transferred.FirstShootingEnum
import com.ricoh360.thetaclient.transferred.Preset
import com.ricoh360.thetaclient.transferred.TimeShift
import io.ktor.client.network.sockets.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class TimeShiftCaptureTest {
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
     * call startCapture for others than Theta SC2 for business.
     */
    @Test
    fun startCaptureTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/TimeShiftCapture/start_capture_progress.json").readText(),
            Resource("src/commonTest/resources/TimeShiftCapture/start_capture_progress.json").readText(),
            Resource("src/commonTest/resources/TimeShiftCapture/start_capture_progress.json").readText(),
            Resource("src/commonTest/resources/TimeShiftCapture/start_capture_done.json").readText(),
        )
        val requestPathArray = arrayOf(
            "/osc/commands/execute",
            "/osc/commands/execute",
            "/osc/commands/status",
            "/osc/commands/status",
            "/osc/commands/status",
            "/osc/commands/execute",
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++

            // check request
            assertEquals(request.url.encodedPath, requestPathArray[index], "start capture request")
            when (index) {
                0 -> {
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.IMAGE)
                }

                1 -> {
                    CheckRequest.checkCommandName(request, "camera.startCapture")
                }
            }

            ByteReadChannel(responseArray[index])
        }
        val deferred = CompletableDeferred<Unit>()

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_X
        val timeShiftCapture = thetaRepository.getTimeShiftCaptureBuilder().build()

        var file: String? = null
        timeShiftCapture.startCapture(object : TimeShiftCapture.StartCaptureCallback {
            override fun onSuccess(fileUrl: String?) {
                file = fileUrl
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
                assertTrue(completion >= 0f, "onProgress")
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error start time-shift")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(30_000) {
                deferred.await()
            }
        }

        // check result
        assertTrue(file?.startsWith("http://") ?: false, "start time-shift")
    }

    /**
     * call startCapture for Theta SC2 for business.
     * assert that after taking a photo.
     */
    @Test
    fun startCaptureSc2bTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/TimeShiftCapture/start_capture_sc2b.json").readText(),
            Resource("src/commonTest/resources/TimeShiftCapture/start_capture_progress_sc2b.json").readText(),
            Resource("src/commonTest/resources/TimeShiftCapture/start_capture_progress_sc2b.json").readText(),
            Resource("src/commonTest/resources/TimeShiftCapture/start_capture_done_sc2b.json").readText(),
        )
        val requestPathArray = arrayOf(
            "/osc/commands/execute",
            "/osc/commands/execute",
            "/osc/commands/status",
            "/osc/commands/status",
            "/osc/commands/status",
            "/osc/commands/execute",
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++

            // check request
            assertEquals(request.url.encodedPath, requestPathArray[index], "start capture request")
            when (index) {
                0 -> {
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.PRESET, preset = Preset.ROOM)
                }

                1 -> {
                    CheckRequest.checkCommandName(request, "camera.startCapture")
                }
            }

            ByteReadChannel(responseArray[index])
        }
        val deferred = CompletableDeferred<Unit>()

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_SC2_B
        val timeShiftCapture = thetaRepository.getTimeShiftCaptureBuilder().build()

        var file: String? = null
        timeShiftCapture.startCapture(object : TimeShiftCapture.StartCaptureCallback {
            override fun onSuccess(fileUrl: String?) {
                file = fileUrl
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
                assertTrue(completion >= 0f, "onProgress")
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error start time-shift")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(30_000) {
                deferred.await()
            }
        }

        // check result
        assertTrue(file?.startsWith("http://") ?: false, "start time-shift")
    }

    /**
     * call startCapture for Theta SC2 for business.
     * assert that after taking a video.
     */
    @Test
    fun startCaptureSc2bAltTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/TimeShiftCapture/start_capture_sc2b.json").readText(),
            Resource("src/commonTest/resources/TimeShiftCapture/start_capture_progress_sc2b_alt.json").readText(),
            Resource("src/commonTest/resources/TimeShiftCapture/start_capture_progress_sc2b_alt.json").readText(),
            Resource("src/commonTest/resources/TimeShiftCapture/start_capture_done_sc2b_alt.json").readText(),
        )
        val requestPathArray = arrayOf(
            "/osc/commands/execute",
            "/osc/commands/execute",
            "/osc/commands/status",
            "/osc/commands/status",
            "/osc/commands/status",
            "/osc/commands/execute",
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++

            // check request
            assertEquals(request.url.encodedPath, requestPathArray[index], "start capture request")
            when (index) {
                0 -> {
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.PRESET, preset = Preset.ROOM)
                }

                1 -> {
                    CheckRequest.checkCommandName(request, "camera.startCapture")
                }
            }

            ByteReadChannel(responseArray[index])
        }
        val deferred = CompletableDeferred<Unit>()

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_SC2_B
        val timeShiftCapture = thetaRepository.getTimeShiftCaptureBuilder().build()

        var file: String? = null
        timeShiftCapture.startCapture(object : TimeShiftCapture.StartCaptureCallback {
            override fun onSuccess(fileUrl: String?) {
                file = fileUrl
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
                assertTrue(completion >= 0f, "onProgress")
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error start time-shift")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(30_000) {
                deferred.await()
            }
        }

        // check result
        assertTrue(file?.startsWith("http://") ?: false, "start time-shift")
    }

    /**
     * call cancelCapture test
     */
    @Test
    fun cancelCaptureTest() = runTest {
        // setup
        var isStop = false
        MockApiClient.onRequest = { request ->
            val path = if (request.body.toString().contains("camera.stopCapture")) {
                isStop = true
                "src/commonTest/resources/TimeShiftCapture/stop_capture_done.json"
            } else if (request.body.toString().contains("camera.setOptions")) {
                "src/commonTest/resources/setOptions/set_options_done.json"
            } else {
                if (isStop)
                    "src/commonTest/resources/TimeShiftCapture/start_capture_done_empty.json"
                else
                    "src/commonTest/resources/TimeShiftCapture/start_capture_progress.json"
            }

            ByteReadChannel(Resource(path).readText())
        }

        val deferred = CompletableDeferred<Unit>()

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_X
        val timeShiftCapture = thetaRepository.getTimeShiftCaptureBuilder().build()

        var file: String? = null
        val capturing = timeShiftCapture.startCapture(object : TimeShiftCapture.StartCaptureCallback {
            override fun onSuccess(fileUrl: String?) {
                file = fileUrl
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
                assertEquals(completion, 0f, "onProgress")
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error start time-shift")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            delay(1000)
        }

        capturing.cancelCapture()

        runBlocking {
            withTimeout(7000) {
                deferred.await()
            }
        }

        // check result
        assertTrue(file == null, "cancel time-shift")
    }

    /**
     * cancel shooting.
     */
    @Test
    fun cancelShootingTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/TimeShiftCapture/start_capture_progress.json").readText(),
            Resource("src/commonTest/resources/TimeShiftCapture/start_capture_cancel.json").readText(),
        )
        var counter = 0
        MockApiClient.onRequest = { _ ->
            val index = counter++
            ByteReadChannel(responseArray[index])
        }
        val deferred = CompletableDeferred<Unit>()

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_X
        val timeShiftCapture = thetaRepository.getTimeShiftCaptureBuilder().build()

        var file: String? = ""
        timeShiftCapture.startCapture(object : TimeShiftCapture.StartCaptureCallback {
            override fun onSuccess(fileUrl: String?) {
                file = fileUrl
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
                assertTrue(completion >= 0f, "onProgress")
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error start time-shift")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(30_000) {
                deferred.await()
            }
        }

        // check result
        assertNull(file, "cancel time-shift")
    }

    /**
     * cancel shooting with exception.
     */
    @Test
    fun cancelShootingWithExceptionTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/TimeShiftCapture/start_capture_progress.json").readText(),
            Resource("src/commonTest/resources/TimeShiftCapture/start_capture_cancel.json").readText(),
        )
        var counter = 0
        MockApiClient.onRequest = { _ ->
            val index = counter++
            MockApiClient.status = if (index == 2) HttpStatusCode.Forbidden else HttpStatusCode.OK
            ByteReadChannel(responseArray[index])
        }
        val deferred = CompletableDeferred<Unit>()

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_X
        val timeShiftCapture = thetaRepository.getTimeShiftCaptureBuilder().build()

        var file: String? = ""
        timeShiftCapture.startCapture(object : TimeShiftCapture.StartCaptureCallback {
            override fun onSuccess(fileUrl: String?) {
                file = fileUrl
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
                assertTrue(completion >= 0f, "onProgress")
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error start time-shift")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(30_000) {
                deferred.await()
            }
        }

        // check result
        assertNull(file, "cancel time-shift")
    }

    /**
     * Setting CheckStatusCommandInterval.
     */
    @Test
    fun settingCheckStatusCommandIntervalTest() = runTest {
        val timeMillis = 1500L

        MockApiClient.onRequest = {
            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_X
        val capture = thetaRepository.getTimeShiftCaptureBuilder()
            .setCheckStatusCommandInterval(timeMillis)
            .build()

        // check result
        assertEquals(capture.getCheckStatusCommandInterval(), timeMillis, "set CheckStatusCommandInterval $timeMillis")
    }

    /**
     * Setting IsFrontFirst.
     */
    @Test
    fun settingIsFrontFirstTest() = runTest {
        val isFrontFirst = false

        var index = 0
        MockApiClient.onRequest = { request ->
            // check request
            when (index) {
                1 -> {
                    CheckRequest.checkSetOptions(request, timeShift = TimeShift(firstShooting = FirstShootingEnum.REAR))
                }
            }
            index += 1

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_X
        val capture = thetaRepository.getTimeShiftCaptureBuilder()
            .setIsFrontFirst(isFrontFirst)
            .build()

        // check result
        assertEquals(capture.getTimeShiftSetting()?.isFrontFirst ?: true, isFrontFirst, "set setIsFrontFirst $isFrontFirst")
    }

    /**
     * Setting FirstInterval.
     */
    @Test
    fun settingFirstIntervalTest() = runTest {
        val interval: ThetaRepository.TimeShiftIntervalEnum = ThetaRepository.TimeShiftIntervalEnum.INTERVAL_3

        var index = 0
        MockApiClient.onRequest = { request ->
            // check request
            when (index) {
                1 -> {
                    CheckRequest.checkSetOptions(request, timeShift = TimeShift(firstInterval = 3))
                }
            }
            index += 1

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_X
        val capture = thetaRepository.getTimeShiftCaptureBuilder()
            .setFirstInterval(interval)
            .build()

        // check result
        assertEquals(capture.getTimeShiftSetting()?.firstInterval, interval, "set setFirstInterval $interval")
    }

    /**
     * Setting SecondInterval.
     */
    @Test
    fun settingSecondIntervalTest() = runTest {
        val interval: ThetaRepository.TimeShiftIntervalEnum = ThetaRepository.TimeShiftIntervalEnum.INTERVAL_5

        var index = 0
        MockApiClient.onRequest = { request ->
            // check request
            when (index) {
                1 -> {
                    CheckRequest.checkSetOptions(request, timeShift = TimeShift(secondInterval = 5))
                }
            }
            index += 1

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_X
        val capture = thetaRepository.getTimeShiftCaptureBuilder()
            .setSecondInterval(interval)
            .build()

        // check result
        assertEquals(capture.getTimeShiftSetting()?.secondInterval, interval, "set setSecondInterval $interval")
    }

    /**
     * Error response to build call.
     */
    @Test
    fun buildErrorResponseTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_error.json").readText(), // set captureMode error
            "Not json" // json error
        )
        var counter = 0

        MockApiClient.onRequest = { _ ->
            val index = counter++
            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_X

        var exceptionSetCaptureMode = false
        try {
            thetaRepository.getTimeShiftCaptureBuilder().build()
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue((e.message?.indexOf("UnitTest", 0, true) ?: -1) >= 0, "")
            exceptionSetCaptureMode = true
        }
        assertTrue(exceptionSetCaptureMode, "setOptions captureMode error response")

        // execute not json response
        var exceptionNotJson = false
        try {
            thetaRepository.getTimeShiftCaptureBuilder().build()
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(
                (e.message?.indexOf("json", 0, true) ?: -1) >= 0
                        || (e.message?.indexOf("Illegal", 0, true) ?: -1) >= 0,
                "setOptions option not json error response"
            )
            exceptionNotJson = true
        }
        assertTrue(exceptionNotJson, "setOptions option error response")
    }

    /**
     * Error exception to build call.
     */
    @Test
    fun buildExceptionTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_error.json").readText(), // status error & error json
            "timeout UnitTest" // timeout
        )
        var counter = 0

        MockApiClient.onRequest = { _ ->
            val index = counter++
            when (index) {
                0 -> MockApiClient.status = HttpStatusCode.ServiceUnavailable
                1 -> throw ConnectTimeoutException("timeout")
            }
            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_X

        // execute status error and json response
        var exceptionStatusJson = false
        try {
            thetaRepository.getTimeShiftCaptureBuilder().build()
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue((e.message?.indexOf("UnitTest", 0, true) ?: -1) >= 0, "status error and json response")
            exceptionStatusJson = true
        }
        assertTrue(exceptionStatusJson, "status error and json response")

        // execute timeout exception
        var exceptionOther = false
        try {
            thetaRepository.getTimeShiftCaptureBuilder().build()
        } catch (e: ThetaRepository.NotConnectedException) {
            assertTrue((e.message?.indexOf("time", 0, true) ?: -1) >= 0, "timeout exception")
            exceptionOther = true
        }
        assertTrue(exceptionOther, "other exception")
    }

    /**
     * Error response to startCapture call
     */
    @Test
    fun startCaptureErrorResponseTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/TimeShiftCapture/start_capture_error.json").readText(), // startCapture error
            "Not json" // json error
        )
        var counter = 0
        MockApiClient.onRequest = { _ ->
            val index = counter++
            ByteReadChannel(responseArray[index])
        }

        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_X
        val capture = thetaRepository.getTimeShiftCaptureBuilder().build()

        // execute error response
        var deferred = CompletableDeferred<Unit>()
        capture.startCapture(object : TimeShiftCapture.StartCaptureCallback {
            override fun onSuccess(fileUrl: String?) {
                assertTrue(false, "capture time-shift")
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue((exception.message?.indexOf("UnitTest", 0, true) ?: -1) >= 0, "capture time-shift error response")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(500) {
                deferred.await()
            }
        }

        // execute json error response
        deferred = CompletableDeferred()
        capture.startCapture(object : TimeShiftCapture.StartCaptureCallback {
            override fun onSuccess(fileUrl: String?) {
                assertTrue(false, "capture time-shift")
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue((exception.message?.length ?: -1) >= 0, "capture time-shift json error response")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(2000) {
                deferred.await()
            }
        }
    }

    /**
     * Error exception to startCapture call
     */
    @Test
    fun startCaptureExceptionTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/TimeShiftCapture/start_capture_error.json").readText(), // startCapture error
            "Status error UnitTest", // status error not json
            "timeout UnitTest" // timeout
        )
        var counter = 0
        MockApiClient.onRequest = { _ ->
            val index = counter++
            when (index) {
                2 -> MockApiClient.status = HttpStatusCode.ServiceUnavailable
                3 -> MockApiClient.status = HttpStatusCode.ServiceUnavailable
                4 -> throw ConnectTimeoutException("timeout")
                else -> MockApiClient.status = HttpStatusCode.OK
            }
            ByteReadChannel(responseArray[index])
        }

        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_X
        val capture = thetaRepository.getTimeShiftCaptureBuilder().build()

        // execute status error and json response
        var deferred = CompletableDeferred<Unit>()
        capture.startCapture(object : TimeShiftCapture.StartCaptureCallback {
            override fun onSuccess(fileUrl: String?) {
                assertTrue(false, "capture time-shift")
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue((exception.message?.indexOf("UnitTest", 0, true) ?: -1) >= 0, "status error and json response")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(1000) {
                deferred.await()
            }
        }

        // execute status error and not json response
        deferred = CompletableDeferred<Unit>()
        capture.startCapture(object : TimeShiftCapture.StartCaptureCallback {
            override fun onSuccess(fileUrl: String?) {
                assertTrue(false, "capture time-shift")
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue((exception.message?.indexOf("503", 0, true) ?: -1) >= 0, "status error")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(1000) {
                deferred.await()
            }
        }

        // execute timeout exception
        deferred = CompletableDeferred<Unit>()
        capture.startCapture(object : TimeShiftCapture.StartCaptureCallback {
            override fun onSuccess(fileUrl: String?) {
                assertTrue(false, "capture time-shift")
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue((exception.message?.indexOf("time", 0, true) ?: -1) >= 0, "timeout exception")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(1000) {
                deferred.await()
            }
        }
    }

    /**
     * Error response to stopCapture call
     */
    @Test
    fun stopCaptureErrorResponseTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/TimeShiftCapture/stop_capture_error.json").readText(), // stopCapture error
            "Not json" // json error
        )
        var counter = 0
        MockApiClient.onRequest = { _ ->
            val index = counter++
            ByteReadChannel(responseArray[index])
        }
        var deferred = CompletableDeferred<Unit>()

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_X
        val timeShiftCapture = thetaRepository.getTimeShiftCaptureBuilder().build()

        var capturing = timeShiftCapture.startCapture(object : TimeShiftCapture.StartCaptureCallback {
            override fun onSuccess(fileUrl: String?) {
                assertTrue(false, "capture time-shift")
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue((exception.message?.indexOf("UnitTest", 0, true) ?: -1) >= 0, "stop capture error response")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            delay(100)
        }

        capturing.cancelCapture()

        runBlocking {
            withTimeout(1000) {
                deferred.await()
            }
        }

        deferred = CompletableDeferred()
        capturing = timeShiftCapture.startCapture(object : TimeShiftCapture.StartCaptureCallback {
            override fun onSuccess(fileUrl: String?) {
                assertTrue(false, "capture time-shift")
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue((exception.message?.length ?: -1) >= 0, "stop capture json error response")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            delay(100)
        }

        capturing.cancelCapture()

        runBlocking {
            withTimeout(1000) {
                deferred.await()
            }
        }
    }

    /**
     * Error exception to stopCapture call
     */
    @Test
    fun stopCaptureExceptionTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/TimeShiftCapture/stop_capture_error.json").readText(), // status error & error json
            "Status error UnitTest", // status error not json
            "timeout UnitTest" // timeout
        )
        var counter = 0
        MockApiClient.onRequest = { _ ->
            val index = counter++
            when (index) {
                1 -> MockApiClient.status = HttpStatusCode.ServiceUnavailable
                2 -> MockApiClient.status = HttpStatusCode.ServiceUnavailable
                3 -> throw ConnectTimeoutException("timeout")
            }
            ByteReadChannel(responseArray[index])
        }

        var deferred = CompletableDeferred<Unit>()

        var capturing = TimeShiftCapturing(endpoint, object : TimeShiftCapture.StartCaptureCallback {
            override fun onSuccess(fileUrl: String?) {
                assertTrue(false, "capture time-shift")
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue((exception.message?.indexOf("UnitTest", 0, true) ?: -1) >= 0, "status error and json response")
                deferred.complete(Unit)
            }
        })

        capturing.cancelCapture()

        runBlocking {
            withTimeout(1000) {
                deferred.await()
            }
        }

        deferred = CompletableDeferred()
        capturing = TimeShiftCapturing(endpoint, object : TimeShiftCapture.StartCaptureCallback {
            override fun onSuccess(fileUrl: String?) {
                assertTrue(false, "capture time-shift")
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue((exception.message?.indexOf("503", 0, true) ?: -1) >= 0, "status error")
                deferred.complete(Unit)
            }
        })

        capturing.cancelCapture()

        runBlocking {
            withTimeout(1000) {
                deferred.await()
            }
        }

        deferred = CompletableDeferred()
        capturing = TimeShiftCapturing(endpoint, object : TimeShiftCapture.StartCaptureCallback {
            override fun onSuccess(fileUrl: String?) {
                assertTrue(false, "capture time-shift")
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue((exception.message?.indexOf("time", 0, true) ?: -1) >= 0, "timeout exception")
                deferred.complete(Unit)
            }
        })

        capturing.cancelCapture()

        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }
    }
}
