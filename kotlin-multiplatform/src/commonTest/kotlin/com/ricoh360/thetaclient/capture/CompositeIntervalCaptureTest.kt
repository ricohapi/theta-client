package com.ricoh360.thetaclient.capture

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CaptureMode
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CompositeIntervalCaptureTest {
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
     * call startCapture.
     */
    @Test
    fun startCaptureTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/CompositeIntervalCapture/start_capture_progress.json").readText(),
            Resource("src/commonTest/resources/CompositeIntervalCapture/start_capture_progress.json").readText(),
            Resource("src/commonTest/resources/CompositeIntervalCapture/start_capture_progress.json").readText(),
            Resource("src/commonTest/resources/CompositeIntervalCapture/start_capture_done.json").readText(),
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++

            // check request
            when (index) {
                0 -> {
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.IMAGE)
                }

                1 -> {
                    CheckRequest.checkSetOptions(request = request, compositeShootingTime = 600)
                }

                2 -> {
                    CheckRequest.checkCommandName(request, "camera.startCapture")
                }
            }

            ByteReadChannel(responseArray[index])
        }
        val deferred = CompletableDeferred<Unit>()

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_X
        val capture = thetaRepository.getCompositeIntervalCaptureBuilder(600)
            .setCheckStatusCommandInterval(100)
            .build()

        var files: List<String>? = null
        capture.startCapture(object : CompositeIntervalCapture.StartCaptureCallback {
            override fun onCaptureCompleted(fileUrls: List<String>?) {
                files = fileUrls
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
                assertTrue(completion >= 0f, "onProgress")
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error start interval composite shooting")
                deferred.complete(Unit)
            }

            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "onStopFailed")
            }
        })

        runBlocking {
            withTimeout(30_000) {
                deferred.await()
            }
        }

        // check result
        assertTrue(
            files?.firstOrNull()?.startsWith("http://") ?: false,
            "start capture interval composite shooting"
        )
    }

    /**
     * call cancelCapture test
     */
    @Test
    fun cancelCaptureTest() = runTest {
        // setup
        var isStop = false
        val deferredStart = CompletableDeferred<Unit>()
        MockApiClient.onRequest = { request ->
            val textBody = request.body as TextContent
            val path = if (textBody.text.contains("camera.stopCapture")) {
                isStop = true
                "src/commonTest/resources/CompositeIntervalCapture/stop_capture_done.json"
            } else if (textBody.text.contains("camera.setOptions")) {
                "src/commonTest/resources/setOptions/set_options_done.json"
            } else {
                if (!deferredStart.isCompleted) {
                    deferredStart.complete(Unit)
                }
                if (isStop)
                    "src/commonTest/resources/CompositeIntervalCapture/start_capture_done_empty.json"
                else
                    "src/commonTest/resources/CompositeIntervalCapture/start_capture_progress.json"
            }

            ByteReadChannel(Resource(path).readText())
        }

        val deferred = CompletableDeferred<Unit>()

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_X
        val capture = thetaRepository.getCompositeIntervalCaptureBuilder(600)
            .setCheckStatusCommandInterval(100)
            .build()

        var files: List<String>? = listOf()
        val capturing =
            capture.startCapture(object : CompositeIntervalCapture.StartCaptureCallback {
                override fun onCaptureCompleted(fileUrls: List<String>?) {
                    files = fileUrls
                    deferred.complete(Unit)
                }

                override fun onProgress(completion: Float) {
                    assertEquals(completion, 0f, "onProgress")
                }

                override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    assertTrue(false, "error start interval composite shooting")
                    deferred.complete(Unit)
                }

                override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    assertTrue(false, "onStopFailed")
                }
            })

        runBlocking {
            withTimeout(5000) {
                deferredStart.await()
            }
        }

        capturing.cancelCapture()

        runBlocking {
            withTimeout(7000) {
                deferred.await()
            }
        }

        // check result
        assertTrue(
            files?.isEmpty() ?: false,
            "cancel interval composite shooting"
        )
    }

    /**
     * Cancel shooting.
     */
    @Test
    fun cancelShootingTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/CompositeIntervalCapture/start_capture_progress.json").readText(),
            Resource("src/commonTest/resources/CompositeIntervalCapture/start_capture_progress.json").readText(),
            Resource("src/commonTest/resources/CompositeIntervalCapture/start_capture_cancel.json").readText(),
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
        val capture = thetaRepository.getCompositeIntervalCaptureBuilder(600)
            .setCheckStatusCommandInterval(100)
            .build()

        var files: List<String>? = listOf()
        capture.startCapture(object : CompositeIntervalCapture.StartCaptureCallback {
            override fun onCaptureCompleted(fileUrls: List<String>?) {
                files = fileUrls
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
                assertTrue(completion >= 0f, "onProgress")
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error start interval composite shooting")
                deferred.complete(Unit)
            }

            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "onStopFailed")
            }
        })

        runBlocking {
            withTimeout(30_000) {
                deferred.await()
            }
        }

        // check result
        assertNull(files, "shooting is canceled")
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
        val capture = thetaRepository.getCompositeIntervalCaptureBuilder(600)
            .setCheckStatusCommandInterval(timeMillis)
            .build()

        // check result
        assertEquals(
            capture.getCheckStatusCommandInterval(),
            timeMillis,
            "set CheckStatusCommandInterval $timeMillis"
        )
    }

    /**
     * Setting captureInterval.
     */
    @Test
    fun settingCaptureIntervalTest() = runTest {
        val interval = 60

        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText()
        )
        var counter = 0

        MockApiClient.onRequest = { request ->
            val index = counter++

            // check request
            when (index) {
                0 -> {
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.IMAGE)
                }

                1 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        compositeShootingOutputInterval = interval
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        val capture = thetaRepository.getCompositeIntervalCaptureBuilder(600)
            .setCompositeShootingOutputInterval(interval)
            .build()

        // check result
        assertEquals(
            capture.getCompositeShootingOutputInterval(),
            interval,
            "set option _compositeShootingOutputInterval $interval"
        )
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
            thetaRepository.getCompositeIntervalCaptureBuilder(600).build()
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue((e.message?.indexOf("UnitTest", 0, true) ?: -1) >= 0, "")
            exceptionSetCaptureMode = true
        }
        assertTrue(exceptionSetCaptureMode, "setOptions captureMode error response")

        // execute not json response
        var exceptionNotJson = false
        try {
            thetaRepository.getCompositeIntervalCaptureBuilder(600).build()
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
            thetaRepository.getCompositeIntervalCaptureBuilder(600).build()
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(
                (e.message?.indexOf("UnitTest", 0, true) ?: -1) >= 0,
                "status error and json response"
            )
            exceptionStatusJson = true
        }
        assertTrue(exceptionStatusJson, "status error and json response")

        // execute timeout exception
        var exceptionOther = false
        try {
            thetaRepository.getCompositeIntervalCaptureBuilder(600).build()
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
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/CompositeIntervalCapture/start_capture_error.json").readText(), // startCapture error
            "Not json" // json error
        )
        var counter = 0
        MockApiClient.onRequest = { _ ->
            val index = counter++
            ByteReadChannel(responseArray[index])
        }

        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_X
        val capture = thetaRepository.getCompositeIntervalCaptureBuilder(600).build()

        // execute error response
        var deferred = CompletableDeferred<Unit>()
        capture.startCapture(object : CompositeIntervalCapture.StartCaptureCallback {
            override fun onCaptureCompleted(fileUrls: List<String>?) {
                assertTrue(false, "capture interval composite shooting")
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(
                    exception.message!!.indexOf("UnitTest", 0, true) >= 0,
                    "capture interval composite shooting error response"
                )
                deferred.complete(Unit)
            }

            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "onStopFailed")
            }
        })

        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }

        // execute json error response
        deferred = CompletableDeferred()
        capture.startCapture(object : CompositeIntervalCapture.StartCaptureCallback {
            override fun onCaptureCompleted(fileUrls: List<String>?) {
                assertTrue(false, "capture interval composite shooting")
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(
                    exception.message!!.length >= 0,
                    "capture interval composite shooting json error response"
                )
                deferred.complete(Unit)
            }

            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "onStopFailed")
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
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/CompositeIntervalCapture/start_capture_error.json").readText(), // startCapture error
            "Status error UnitTest", // status error not json
            "timeout UnitTest" // timeout
        )
        var counter = 0
        MockApiClient.onRequest = { _ ->
            val index = counter++
            when (index) {
                0 -> MockApiClient.status = HttpStatusCode.OK
                1 -> MockApiClient.status = HttpStatusCode.OK
                2 -> MockApiClient.status = HttpStatusCode.ServiceUnavailable
                3 -> MockApiClient.status = HttpStatusCode.ServiceUnavailable
                4 -> throw ConnectTimeoutException("timeout")
            }
            ByteReadChannel(responseArray[index])
        }

        val thetaRepository = ThetaRepository(endpoint)
        val capture = thetaRepository.getCompositeIntervalCaptureBuilder(600)
            .build()

        // execute status error and json response
        var deferred = CompletableDeferred<Unit>()
        capture.startCapture(object : CompositeIntervalCapture.StartCaptureCallback {
            override fun onCaptureCompleted(fileUrls: List<String>?) {
                assertTrue(false, "capture interval composite shooting")
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(
                    (exception.message?.indexOf("UnitTest", 0, true) ?: -1) >= 0,
                    "status error and json response"
                )
                deferred.complete(Unit)
            }

            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "onStopFailed")
            }
        })

        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }

        // execute status error and not json response
        deferred = CompletableDeferred()
        capture.startCapture(object : CompositeIntervalCapture.StartCaptureCallback {
            override fun onCaptureCompleted(fileUrls: List<String>?) {
                assertTrue(false, "capture interval composite shooting")
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue((exception.message?.indexOf("503", 0, true) ?: -1) >= 0, "status error")
                deferred.complete(Unit)
            }

            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "onStopFailed")
            }
        })

        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }

        // execute timeout exception
        deferred = CompletableDeferred()
        capture.startCapture(object : CompositeIntervalCapture.StartCaptureCallback {
            override fun onCaptureCompleted(fileUrls: List<String>?) {
                assertTrue(false, "capture interval composite shooting")
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(
                    (exception.message?.indexOf("time", 0, true) ?: -1) >= 0,
                    "timeout exception"
                )
                deferred.complete(Unit)
            }

            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "onStopFailed")
            }
        })

        runBlocking {
            withTimeout(5000) {
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
            Resource("src/commonTest/resources/CompositeIntervalCapture/stop_capture_error.json").readText(), // stopCapture error
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

        var capturing =
            CompositeIntervalCapturing(
                endpoint,
                object : CompositeIntervalCapture.StartCaptureCallback {
                    override fun onCaptureCompleted(fileUrls: List<String>?) {
                        assertTrue(false, "capture interval composite shooting")
                        deferred.complete(Unit)
                    }

                    override fun onProgress(completion: Float) {
                    }

                    override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                        assertTrue(false, "onCaptureFailed")
                        deferred.complete(Unit)
                    }

                    override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                        assertTrue(
                            (exception.message?.indexOf("UnitTest", 0, true) ?: -1) >= 0,
                            "stop capture error response"
                        )
                        deferred.complete(Unit)
                    }
                })

        capturing.cancelCapture()

        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }

        deferred = CompletableDeferred()
        capturing =
            CompositeIntervalCapturing(
                endpoint,
                object : CompositeIntervalCapture.StartCaptureCallback {
                    override fun onCaptureCompleted(fileUrls: List<String>?) {
                        assertTrue(false, "capture interval composite shooting")
                        deferred.complete(Unit)
                    }

                    override fun onProgress(completion: Float) {
                    }

                    override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                        assertTrue(false, "onCaptureFailed")
                        deferred.complete(Unit)
                    }

                    override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                        assertTrue(
                            (exception.message?.length ?: -1) >= 0,
                            "stop capture json error response"
                        )
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

    /**
     * Error exception to stopCapture call
     */
    @Test
    fun stopCaptureExceptionTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/CompositeIntervalCapture/stop_capture_error.json").readText(), // status error & error json
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

        var capturing = CompositeIntervalCapturing(
            endpoint,
            object : CompositeIntervalCapture.StartCaptureCallback {
                override fun onCaptureCompleted(fileUrls: List<String>?) {
                    assertTrue(false, "capture interval composite shooting")
                    deferred.complete(Unit)
                }

                override fun onProgress(completion: Float) {
                }

                override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    assertTrue(false, "onCaptureFailed")
                }

                override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    assertTrue(
                        (exception.message?.indexOf("UnitTest", 0, true) ?: -1) >= 0,
                        "status error and json response"
                    )
                    deferred.complete(Unit)
                }
            })

        capturing.cancelCapture()

        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }

        deferred = CompletableDeferred()
        capturing = CompositeIntervalCapturing(
            endpoint,
            object : CompositeIntervalCapture.StartCaptureCallback {
                override fun onCaptureCompleted(fileUrls: List<String>?) {
                    assertTrue(false, "capture interval composite shooting")
                    deferred.complete(Unit)
                }

                override fun onProgress(completion: Float) {
                }

                override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    assertTrue(false, "onCaptureFailed")
                }

                override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    assertTrue(
                        (exception.message?.indexOf("503", 0, true) ?: -1) >= 0,
                        "status error"
                    )
                    deferred.complete(Unit)
                }
            })

        capturing.cancelCapture()

        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }

        deferred = CompletableDeferred()
        capturing = CompositeIntervalCapturing(
            endpoint,
            object : CompositeIntervalCapture.StartCaptureCallback {
                override fun onCaptureCompleted(fileUrls: List<String>?) {
                    assertTrue(false, "capture interval composite shooting")
                    deferred.complete(Unit)
                }

                override fun onProgress(completion: Float) {
                }

                override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    assertTrue(false, "onCaptureFailed")
                }

                override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    assertTrue(
                        (exception.message?.indexOf("time", 0, true) ?: -1) >= 0,
                        "timeout exception"
                    )
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
