package com.ricoh360.thetaclient.capture

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaApi
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CaptureMode
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

class MultiBracketCaptureTest {

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
     * call startCapture of Theta X
     */
    @Test
    fun startCaptureXTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/MultiBracketCapture/start_capture_progress.json").readText(),
            Resource("src/commonTest/resources/MultiBracketCapture/start_capture_progress.json").readText(),
            Resource("src/commonTest/resources/MultiBracketCapture/start_capture_progress_1.json").readText(),
            Resource("src/commonTest/resources/MultiBracketCapture/start_capture_done.json").readText(),
        )
        val requestPathArray = arrayOf(
            "/osc/commands/execute",
            "/osc/commands/execute",
            "/osc/commands/execute",
            "/osc/commands/status",
            "/osc/commands/status",
            "/osc/commands/status",
        )

        val stateSelfTimer =
            Resource("src/commonTest/resources/MultiBracketCapture/state_self_timer.json").readText()
        val stateShooting =
            Resource("src/commonTest/resources/MultiBracketCapture/state_shooting.json").readText()

        var counter = 0
        var onSelfTimer = false
        MockApiClient.onRequest = { request ->
            val index = counter
            val response = if (request.url.encodedPath != "/osc/state") {
                counter++

                // check request
                assertEquals(
                    request.url.encodedPath,
                    requestPathArray[index],
                    "start capture request"
                )
                when (index) {
                    0 -> {
                        CheckRequest.checkSetOptions(
                            request = request,
                            captureMode = CaptureMode.IMAGE
                        )
                    }

                    2 -> {
                        CheckRequest.checkCommandName(request, "camera.startCapture")
                    }
                }
                responseArray[index]
            } else {
                if (onSelfTimer) stateShooting else stateSelfTimer
            }

            ByteReadChannel(response)
        }
        val deferred = CompletableDeferred<Unit>()

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_X
        val multiBracketCapture = thetaRepository.getMultiBracketCaptureBuilder()
            .setCheckStatusCommandInterval(100)
            .addBracketParameters(
                colorTemperature = 5000,
                exposureCompensation = ThetaRepository.ExposureCompensationEnum.M0_3,
            ).addBracketParameters(
                colorTemperature = 4000,
                whiteBalance = ThetaRepository.WhiteBalanceEnum.CLOUDY_DAYLIGHT,
            ).addBracketParameters(
                exposureProgram = ThetaRepository.ExposureProgramEnum.MANUAL,
                shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_100,
                iso = ThetaRepository.IsoEnum.ISO_800,
                whiteBalance = ThetaRepository.WhiteBalanceEnum.DAYLIGHT,
            ).build()
        ThetaApi.lastSetTimeConsumingOptionTime = 0

        var files: List<String>? = null
        var onCapturingCounter = 0
        multiBracketCapture.startCapture(object : MultiBracketCapture.StartCaptureCallback {
            override fun onCaptureCompleted(fileUrls: List<String>?) {
                assertTrue(true, "onCaptureCompleted")
                files = fileUrls
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
                assertTrue(completion >= 0f, "onProgress")
            }

            override fun onCapturing(status: CapturingStatusEnum) {
                onCapturingCounter++
                if (onSelfTimer) {
                    assertEquals(status, CapturingStatusEnum.CAPTURING)
                } else {
                    onSelfTimer = true
                    assertEquals(status, CapturingStatusEnum.SELF_TIMER_COUNTDOWN)
                }
            }

            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error to stop multi bracket capture")
                deferred.complete(Unit)
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error while multi bracket capture")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(30_000) {
                deferred.await()
            }
        }

        // check result
        assertTrue(files?.get(1)?.startsWith("http://") ?: false, "start multi bracket capture")
        assertTrue(onSelfTimer, "onCapturing self timer")
        assertTrue(onCapturingCounter >= 2, "onCapturing count")
    }

    /**
     * call startCapture of Theta V
     */
    @Test
    fun startCaptureVTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/MultiBracketCapture/start_capture_progress.json").readText(),
            Resource("src/commonTest/resources/MultiBracketCapture/start_capture_progress.json").readText(),
            Resource("src/commonTest/resources/MultiBracketCapture/start_capture_progress_1.json").readText(),
            Resource("src/commonTest/resources/MultiBracketCapture/start_capture_done.json").readText(),
        )
        val requestPathArray = arrayOf(
            "/osc/commands/execute",
            "/osc/commands/execute",
            "/osc/commands/execute",
            "/osc/commands/status",
            "/osc/commands/status",
            "/osc/commands/status",
        )
        val stateShooting =
            Resource("src/commonTest/resources/MultiBracketCapture/state_shooting.json").readText()
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter
            val response = if (request.url.encodedPath != "/osc/state") {
                counter++

                // check request
                assertEquals(
                    request.url.encodedPath,
                    requestPathArray[index],
                    "start capture request"
                )
                when (index) {
                    0 -> {
                        CheckRequest.checkSetOptions(
                            request = request,
                            captureMode = CaptureMode.IMAGE
                        )
                    }

                    2 -> {
                        CheckRequest.checkCommandName(request, "camera.startCapture")
                    }
                }
                responseArray[index]
            } else {
                stateShooting
            }

            ByteReadChannel(response)
        }
        val deferred = CompletableDeferred<Unit>()

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_V
        val multiBracketCapture = thetaRepository.getMultiBracketCaptureBuilder()
            .setCheckStatusCommandInterval(100)
            .addBracketSettingList(
                listOf(
                    ThetaRepository.BracketSetting(
                        exposureProgram = ThetaRepository.ExposureProgramEnum.NORMAL_PROGRAM,
                        colorTemperature = 5000,
                        exposureCompensation = ThetaRepository.ExposureCompensationEnum.M0_3,
                        whiteBalance = ThetaRepository.WhiteBalanceEnum.AUTO
                    ),
                    ThetaRepository.BracketSetting(
                        aperture = ThetaRepository.ApertureEnum.APERTURE_AUTO,
                        exposureProgram = ThetaRepository.ExposureProgramEnum.MANUAL,
                        shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_100,
                        iso = ThetaRepository.IsoEnum.ISO_800,
                        whiteBalance = ThetaRepository.WhiteBalanceEnum.DAYLIGHT,
                    )
                )
            ).build()
        ThetaApi.lastSetTimeConsumingOptionTime = 0

        var files: List<String>? = null
        multiBracketCapture.startCapture(object : MultiBracketCapture.StartCaptureCallback {
            override fun onCaptureCompleted(fileUrls: List<String>?) {
                assertTrue(true, "onCaptureCompleted")
                files = fileUrls
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
                assertTrue(completion >= 0f, "onProgress")
            }

            override fun onCapturing(status: CapturingStatusEnum) {
                assertEquals(status, CapturingStatusEnum.CAPTURING)
            }

            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error to stop multi bracket capture")
                deferred.complete(Unit)
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error while multi bracket capture")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(30_000) {
                deferred.await()
            }
        }

        // check result
        assertTrue(files?.get(1)?.startsWith("http://") ?: false, "start multi bracket capture")
    }

    /**
     * call startCapture of Theta SC2
     */
    @Test
    fun startCaptureSc2Test() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/MultiBracketCapture/start_capture_progress.json").readText(),
            Resource("src/commonTest/resources/MultiBracketCapture/state_self_timer.json").readText(),
            Resource("src/commonTest/resources/MultiBracketCapture/state_shooting.json").readText(),
            Resource("src/commonTest/resources/MultiBracketCapture/state_idle.json").readText(),
        )

        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++
            when (index) {
                0 -> {
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.IMAGE)
                }

                2 -> {
                    CheckRequest.checkCommandName(request, "camera.startCapture")
                }

            }
            val response =
                if (index >= responseArray.size) responseArray[responseArray.size - 1] else responseArray[index]
            ByteReadChannel(response)
        }
        val deferred = CompletableDeferred<Unit>()

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_SC2
        val multiBracketCapture = thetaRepository.getMultiBracketCaptureBuilder()
            .addBracketParameters(
                shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_100,
                iso = ThetaRepository.IsoEnum.ISO_100,
                colorTemperature = 5000,
            ).addBracketParameters(
                shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_250,
                iso = ThetaRepository.IsoEnum.ISO_200,
                colorTemperature = 6000,
            )
            .setCheckStatusCommandInterval(100)
            .build()
        ThetaApi.lastSetTimeConsumingOptionTime = 0

        var files: List<String>? = null
        var capturingCount = 0
        multiBracketCapture.startCapture(object : MultiBracketCapture.StartCaptureCallback {
            override fun onCaptureCompleted(fileUrls: List<String>?) {
                assertTrue(true, "onCaptureCompleted")
                files = fileUrls
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
                assertTrue(completion >= 0f, "onProgress")
            }

            override fun onCapturing(status: CapturingStatusEnum) {
                when (counter) {
                    4 -> assertEquals(status, CapturingStatusEnum.SELF_TIMER_COUNTDOWN)
                    5 -> assertEquals(status, CapturingStatusEnum.CAPTURING)
                    6 -> assertEquals(status, CapturingStatusEnum.CAPTURING)
                }
                capturingCount++
            }

            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error to stop multi bracket capture")
                deferred.complete(Unit)
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error while multi bracket capture")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(30_000) {
                deferred.await()
            }
        }

        // check result
        assertTrue(files?.size == 0, "result multi bracket capture")
        assertTrue(capturingCount >= 2, "onCapturing count")
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
            val path = if (request.url.encodedPath == "/osc/state") {
                "src/commonTest/resources/MultiBracketCapture/state_shooting.json"
            } else {
                val textBody = request.body as TextContent
                if (textBody.text.contains("camera.stopCapture")) {
                    isStop = true
                    "src/commonTest/resources/MultiBracketCapture/stop_capture_done.json"
                } else if (textBody.text.contains("camera.setOptions")) {
                    "src/commonTest/resources/setOptions/set_options_done.json"
                } else {
                    if (!deferredStart.isCompleted) {
                        deferredStart.complete(Unit)
                    }
                    if (isStop)
                        "src/commonTest/resources/MultiBracketCapture/start_capture_done_stopped.json"
                    else
                        "src/commonTest/resources/MultiBracketCapture/start_capture_progress.json"
                }
            }

            ByteReadChannel(Resource(path).readText())
        }

        val deferred = CompletableDeferred<Unit>()

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_X
        val capture = thetaRepository.getMultiBracketCaptureBuilder()
            .setCheckStatusCommandInterval(100)
            .addBracketParameters(
                colorTemperature = 5000,
                exposureCompensation = ThetaRepository.ExposureCompensationEnum.M0_3,
            ).addBracketParameters(
                exposureProgram = ThetaRepository.ExposureProgramEnum.MANUAL,
                shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_1,
                iso = ThetaRepository.IsoEnum.ISO_100,
                whiteBalance = ThetaRepository.WhiteBalanceEnum.DAYLIGHT,
            ).addBracketParameters(
                exposureProgram = ThetaRepository.ExposureProgramEnum.MANUAL,
                shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_1,
                iso = ThetaRepository.IsoEnum.ISO_100,
                whiteBalance = ThetaRepository.WhiteBalanceEnum.DAYLIGHT,
            ).addBracketParameters(
                exposureProgram = ThetaRepository.ExposureProgramEnum.MANUAL,
                shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_1,
                iso = ThetaRepository.IsoEnum.ISO_100,
                whiteBalance = ThetaRepository.WhiteBalanceEnum.DAYLIGHT,
            ).addBracketParameters(
                exposureProgram = ThetaRepository.ExposureProgramEnum.MANUAL,
                shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_1,
                iso = ThetaRepository.IsoEnum.ISO_100,
                whiteBalance = ThetaRepository.WhiteBalanceEnum.DAYLIGHT,
            ).addBracketParameters(
                exposureProgram = ThetaRepository.ExposureProgramEnum.MANUAL,
                shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_1,
                iso = ThetaRepository.IsoEnum.ISO_100,
                whiteBalance = ThetaRepository.WhiteBalanceEnum.DAYLIGHT,
            ).addBracketParameters(
                exposureProgram = ThetaRepository.ExposureProgramEnum.MANUAL,
                shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_1,
                iso = ThetaRepository.IsoEnum.ISO_100,
                whiteBalance = ThetaRepository.WhiteBalanceEnum.DAYLIGHT,
            ).addBracketParameters(
                exposureProgram = ThetaRepository.ExposureProgramEnum.MANUAL,
                shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_1,
                iso = ThetaRepository.IsoEnum.ISO_100,
                whiteBalance = ThetaRepository.WhiteBalanceEnum.DAYLIGHT,
            ).addBracketParameters(
                exposureProgram = ThetaRepository.ExposureProgramEnum.MANUAL,
                shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_1,
                iso = ThetaRepository.IsoEnum.ISO_100,
                whiteBalance = ThetaRepository.WhiteBalanceEnum.DAYLIGHT,
            ).addBracketParameters(
                exposureProgram = ThetaRepository.ExposureProgramEnum.MANUAL,
                shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_1,
                iso = ThetaRepository.IsoEnum.ISO_100,
                whiteBalance = ThetaRepository.WhiteBalanceEnum.DAYLIGHT,
            ).build()
        ThetaApi.lastSetTimeConsumingOptionTime = 0

        var files: List<String>? = null
        val capturing =
            capture.startCapture(object : MultiBracketCapture.StartCaptureCallback {
                override fun onCaptureCompleted(fileUrls: List<String>?) {
                    files = fileUrls
                    deferred.complete(Unit)
                }

                override fun onProgress(completion: Float) {
                    assertEquals(completion, 0f, "onProgress")
                }

                override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    assertTrue(false, "error start interval shooting with the shot count specified")
                    deferred.complete(Unit)
                }

                override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    assertTrue(false, "onStopFailed")
                }
            })

        runBlocking {
            withTimeout(2000) {
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
        assertTrue(files?.size == 0, "cancel multi bracket shooting")
    }

    /**
     * self timer cancel test
     */
    @Test
    fun cancelCaptureWhileSelfTimerTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/MultiBracketCapture/start_capture_progress.json").readText(),
            Resource("src/commonTest/resources/MultiBracketCapture/start_capture_progress.json").readText(),
            Resource("src/commonTest/resources/MultiBracketCapture/start_capture_cancel.json").readText(),
        )
        val stateShooting =
            Resource("src/commonTest/resources/MultiBracketCapture/state_shooting.json").readText()
        var counter = 0
        MockApiClient.onRequest = { request ->
            val response = when (request.url.encodedPath) {
                "/osc/state" -> stateShooting
                else -> {
                    val index = counter++
                    responseArray[index]
                }
            }
            ByteReadChannel(response)
        }
        val deferred = CompletableDeferred<Unit>()

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_X
        val capture = thetaRepository.getMultiBracketCaptureBuilder()
            .addBracketParameters(
                colorTemperature = 5000,
                exposureCompensation = ThetaRepository.ExposureCompensationEnum.M0_3,
            ).addBracketParameters(
                exposureProgram = ThetaRepository.ExposureProgramEnum.MANUAL,
                shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_1,
                iso = ThetaRepository.IsoEnum.ISO_100,
                whiteBalance = ThetaRepository.WhiteBalanceEnum.DAYLIGHT,
            ).setCheckStatusCommandInterval(100)
            .build()
        ThetaApi.lastSetTimeConsumingOptionTime = 0

        var files: List<String>? = listOf()
        capture.startCapture(object : MultiBracketCapture.StartCaptureCallback {
            override fun onCaptureCompleted(fileUrls: List<String>?) {
                files = fileUrls
                deferred.complete(Unit)
            }

            override fun onProgress(completion: Float) {
                assertEquals(completion, 0f, "onProgress")
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error start interval shooting with the shot count specified")
                deferred.complete(Unit)
            }

            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "onStopFailed")
            }
        })

        runBlocking {
            withTimeout(7000) {
                deferred.await()
            }
        }

        // check result
        assertNull(files, "shooting is canceled")
    }
}
