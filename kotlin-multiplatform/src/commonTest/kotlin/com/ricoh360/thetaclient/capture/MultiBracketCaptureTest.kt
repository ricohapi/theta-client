package com.ricoh360.thetaclient.capture

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CaptureMode
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
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
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++

            // check request
            assertEquals(request.url.encodedPath, requestPathArray[index], "start capture request")
            when (index) {
                0 -> {
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.IMAGE)
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
        val multiBracketCapture = thetaRepository.getMultiBracketCaptureBuilder()
            .addBracketParameters(
                colorTemperature = 5000,
                exposureCompensation = ThetaRepository.ExposureCompensationEnum.M0_3,
            ).addBracketParameters(
                exposureProgram = ThetaRepository.ExposureProgramEnum.MANUAL,
                shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_100,
                iso = ThetaRepository.IsoEnum.ISO_800,
                whiteBalance = ThetaRepository.WhiteBalanceEnum.DAYLIGHT,
            ).build()
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
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++

            // check request
            assertEquals(request.url.encodedPath, requestPathArray[index], "start capture request")
            when (index) {
                0 -> {
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.IMAGE)
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
        thetaRepository.cameraModel = ThetaRepository.ThetaModel.THETA_V
        val multiBracketCapture = thetaRepository.getMultiBracketCaptureBuilder()
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
            Resource("src/commonTest/resources/MultiBracketCapture/state_shooting.json").readText(),
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
            val response = if (index >= 5) responseArray[5] else responseArray[index]
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
            ).build()

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
            val path = if (request.body.toString().contains("camera.stopCapture")) {
                isStop = true
                "src/commonTest/resources/MultiBracketCapture/stop_capture_done.json"
            } else if (request.body.toString().contains("camera.setOptions")) {
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

            ByteReadChannel(Resource(path).readText())
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
        var counter = 0
        MockApiClient.onRequest = { _ ->
            val index = counter++
            ByteReadChannel(responseArray[index])
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
            ).build()

        var files: List<String>? = listOf()
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
            withTimeout(7000) {
                deferred.await()
            }
        }

        // check result
        assertNull(files, "shooting is canceled")
    }
}
