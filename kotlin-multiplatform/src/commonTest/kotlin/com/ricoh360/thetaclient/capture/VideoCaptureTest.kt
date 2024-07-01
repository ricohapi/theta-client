package com.ricoh360.thetaclient.capture

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CaptureMode
import com.ricoh360.thetaclient.transferred.MediaFileFormat
import com.ricoh360.thetaclient.transferred.MediaType
import io.ktor.client.network.sockets.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class VideoCaptureTest {
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
            Resource("src/commonTest/resources/VideoCapture/start_capture_done.json").readText(),
            Resource("src/commonTest/resources/VideoCapture/stop_capture_done.json").readText()
        )
        val deferredStart = CompletableDeferred<Unit>()
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++

            var response = ""
            // check request
            when (index) {
                0 -> {
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.VIDEO)
                    response = responseArray[0]
                }

                1 -> {
                    CheckRequest.checkCommandName(request, "camera.startCapture")
                    response = responseArray[1]
                }

                else -> {
                    if (CheckRequest.getCommandName(request) == "camera.stopCapture") {
                        CheckRequest.checkCommandName(request, "camera.stopCapture")
                        response = responseArray[2]
                    } else if (request.url.encodedPath == "/osc/state") {
                        if (!deferredStart.isCompleted) {
                            deferredStart.complete(Unit)
                        }
                        response =
                            Resource("src/commonTest/resources/VideoCapture/state_shooting.json").readText()
                    }
                }
            }

            ByteReadChannel(response)
        }
        val deferred = CompletableDeferred<Unit>()

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        val videoCapture = thetaRepository.getVideoCaptureBuilder()
            .build()

        assertNull(videoCapture.getMaxRecordableTime(), "set option maxRecordableTime")
        assertNull(videoCapture.getFileFormat(), "set option fileFormat")

        var file: String? = null
        val capturing = videoCapture.startCapture(object : VideoCapture.StartCaptureCallback {
            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error stop capture")
                deferred.complete(Unit)
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error capture video")
                deferred.complete(Unit)
            }

            override fun onCaptureCompleted(fileUrl: String?) {
                file = fileUrl
                deferred.complete(Unit)
            }
        })
        runBlocking {
            withTimeout(10000) {
                deferredStart.await()
            }
        }
        capturing.stopCapture()
        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }
        runBlocking {
            delay(2000)
        }

        // check result
        assertTrue(file?.startsWith("http://") ?: false, "start capture video")
    }

    /**
     * call startCapture.
     */
    @Test
    fun cancelStartCaptureTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/VideoCapture/start_capture_done.json").readText(),
        )
        var counter = 0
        var idleCount = 0
        MockApiClient.onRequest = { request ->
            val index = counter++

            var response = ""
            // check request
            when (index) {
                0 -> {
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.VIDEO)
                    response = responseArray[0]
                }

                1 -> {
                    CheckRequest.checkCommandName(request, "camera.startCapture")
                    response = responseArray[1]
                }

                else -> {
                    if (request.url.encodedPath == "/osc/state") {
                        idleCount += 1
                        response =
                            Resource("src/commonTest/resources/VideoCapture/state_idle.json").readText()
                    } else {
                        assertTrue(false, "error capture video")
                    }
                }
            }

            ByteReadChannel(response)
        }
        val deferred = CompletableDeferred<Unit>()

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        val videoCapture = thetaRepository.getVideoCaptureBuilder()
            .build()

        assertNull(videoCapture.getMaxRecordableTime(), "set option maxRecordableTime")
        assertNull(videoCapture.getFileFormat(), "set option fileFormat")

        var file: String? = "error"
        videoCapture.startCapture(object : VideoCapture.StartCaptureCallback {
            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error stop capture")
                deferred.complete(Unit)
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error capture video")
                deferred.complete(Unit)
            }

            override fun onCaptureCompleted(fileUrl: String?) {
                file = fileUrl
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }

        // check result
        assertNull(file, "cancel capture video")
        assertTrue(idleCount >= 2, "cancel capture video")
    }

    /**
     * call startCapture.
     */
    @Test
    fun startCaptureWithMaxRecordableTimeAndFileFormatTest() = runTest {
        // setup
        val fileFormat = ThetaRepository.VideoFileFormatEnum.VIDEO_5_7K_30F
        val maxRecordableTime = ThetaRepository.MaxRecordableTimeEnum.RECORDABLE_TIME_300

        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
        )
        val requestPathArray = arrayOf(
            "/osc/commands/execute",
            "/osc/commands/execute",
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++

            // check request
            assertEquals(request.url.encodedPath, requestPathArray[index], "start capture request")
            when (index) {
                0 -> {
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.VIDEO)
                }

                1 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        fileFormat = fileFormat.fileFormat.toMediaFileFormat(),
                        maxRecordableTime = maxRecordableTime.sec
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        val videoCapture = thetaRepository.getVideoCaptureBuilder()
            .setMaxRecordableTime(maxRecordableTime)
            .setFileFormat(fileFormat)
            .build()

        assertEquals(
            videoCapture.getMaxRecordableTime(),
            maxRecordableTime,
            "set option maxRecordableTime"
        )
        assertEquals(videoCapture.getFileFormat(), fileFormat, "set option fileFormat")
    }

    /**
     * Setting filter.
     */
    @Test
    fun settingMaxRecordableTimeTest() = runTest {
        // setup
        val valueList = ThetaRepository.MaxRecordableTimeEnum.entries.toTypedArray()

        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText()
        )
        var counter = 0
        var valueIndex = 0

        MockApiClient.onRequest = { request ->
            val index = counter++

            // check request
            when (index) {
                0 -> {
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.VIDEO)
                }

                1 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        maxRecordableTime = valueList[valueIndex].sec
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        valueList.forEach {
            val videoCapture = thetaRepository.getVideoCaptureBuilder()
                .setMaxRecordableTime(it)
                .build()

            // check result
            when (it) {
                ThetaRepository.MaxRecordableTimeEnum.UNKNOWN -> {
                    assertNull(
                        videoCapture.getMaxRecordableTime(),
                        "set option maxRecordableTime $valueIndex"
                    )
                }

                else -> {
                    assertEquals(
                        videoCapture.getMaxRecordableTime(),
                        it,
                        "set option maxRecordableTime $valueIndex"
                    )
                }
            }

            valueIndex++
            counter = 0
        }
    }

    /**
     * Setting fileFormat.
     */
    @Test
    fun settingFileFormatTest() = runTest {
        // setup
        val valueList = listOf(
            Pair(ThetaRepository.VideoFileFormatEnum.VIDEO_HD, MediaFileFormat(MediaType.MP4, 1280, 720, null, null)),
            Pair(ThetaRepository.VideoFileFormatEnum.VIDEO_FULL_HD, MediaFileFormat(MediaType.MP4, 1920, 1080, null, null)),
            Pair(ThetaRepository.VideoFileFormatEnum.VIDEO_2K, MediaFileFormat(MediaType.MP4, 1920, 960, "H.264/MPEG-4 AVC", null)),
            Pair(ThetaRepository.VideoFileFormatEnum.VIDEO_2K_NO_CODEC, MediaFileFormat(MediaType.MP4, 1920, 960, null, null)),
            Pair(ThetaRepository.VideoFileFormatEnum.VIDEO_4K, MediaFileFormat(MediaType.MP4, 3840, 1920, "H.264/MPEG-4 AVC", null)),
            Pair(ThetaRepository.VideoFileFormatEnum.VIDEO_4K_NO_CODEC, MediaFileFormat(MediaType.MP4, 3840, 1920, null, null)),
            Pair(ThetaRepository.VideoFileFormatEnum.VIDEO_2K_30F, MediaFileFormat(MediaType.MP4, 1920, 960, "H.264/MPEG-4 AVC", 30)),
            Pair(ThetaRepository.VideoFileFormatEnum.VIDEO_2K_60F, MediaFileFormat(MediaType.MP4, 1920, 960, "H.264/MPEG-4 AVC", 60)),
            Pair(ThetaRepository.VideoFileFormatEnum.VIDEO_2_7K_1F, MediaFileFormat(MediaType.MP4, 2688, 2688, "H.264/MPEG-4 AVC", 1)),
            Pair(ThetaRepository.VideoFileFormatEnum.VIDEO_2_7K_2F, MediaFileFormat(MediaType.MP4, 2688, 2688, "H.264/MPEG-4 AVC", 2)),
            Pair(ThetaRepository.VideoFileFormatEnum.VIDEO_3_6K_1F, MediaFileFormat(MediaType.MP4, 3648, 3648, "H.264/MPEG-4 AVC", 1)),
            Pair(ThetaRepository.VideoFileFormatEnum.VIDEO_3_6K_2F, MediaFileFormat(MediaType.MP4, 3648, 3648, "H.264/MPEG-4 AVC", 2)),
            Pair(ThetaRepository.VideoFileFormatEnum.VIDEO_4K_30F, MediaFileFormat(MediaType.MP4, 3840, 1920, "H.264/MPEG-4 AVC", 30)),
            Pair(ThetaRepository.VideoFileFormatEnum.VIDEO_4K_60F, MediaFileFormat(MediaType.MP4, 3840, 1920, "H.264/MPEG-4 AVC", 60)),
            Pair(ThetaRepository.VideoFileFormatEnum.VIDEO_5_7K_2F, MediaFileFormat(MediaType.MP4, 5760, 2880, "H.264/MPEG-4 AVC", 2)),
            Pair(ThetaRepository.VideoFileFormatEnum.VIDEO_5_7K_5F, MediaFileFormat(MediaType.MP4, 5760, 2880, "H.264/MPEG-4 AVC", 5)),
            Pair(ThetaRepository.VideoFileFormatEnum.VIDEO_5_7K_30F, MediaFileFormat(MediaType.MP4, 5760, 2880, "H.264/MPEG-4 AVC", 30)),
            Pair(ThetaRepository.VideoFileFormatEnum.VIDEO_7K_2F, MediaFileFormat(MediaType.MP4, 7680, 3840, "H.264/MPEG-4 AVC", 2)),
            Pair(ThetaRepository.VideoFileFormatEnum.VIDEO_7K_5F, MediaFileFormat(MediaType.MP4, 7680, 3840, "H.264/MPEG-4 AVC", 5)),
            Pair(ThetaRepository.VideoFileFormatEnum.VIDEO_7K_10F, MediaFileFormat(MediaType.MP4, 7680, 3840, "H.264/MPEG-4 AVC", 10)),
        )

        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText()
        )
        var counter = 0
        var valueIndex = 0

        MockApiClient.onRequest = { request ->
            val index = counter++

            // check request
            when (index) {
                0 -> {
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.VIDEO)
                }

                1 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        fileFormat = valueList[valueIndex].second
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)

        assertEquals(valueList.size, ThetaRepository.VideoFileFormatEnum.entries.size)
        valueList.forEach {
            val builder = thetaRepository.getVideoCaptureBuilder()
            builder.setFileFormat(it.first)
            assertEquals(builder.options.fileFormat, it.second, "fileFormat ${it.second}")
            val videoCapture = builder.build()

            // check result
            assertEquals(
                videoCapture.getFileFormat(),
                it.first,
                "set option fileFormat $valueIndex"
            )

            valueIndex++
            counter = 0
        }
    }

    /**
     * Error response to build call.
     */
    @Test
    fun buildErrorResponseTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_error.json").readText(), // set captureMode error
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_error.json").readText(), // set option error
            "Not json" // json error
        )
        var counter = 0

        MockApiClient.onRequest = { _ ->
            val index = counter++
            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)

        var exceptionSetCaptureMode = false
        try {
            thetaRepository.getVideoCaptureBuilder()
                .build()
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "")
            exceptionSetCaptureMode = true
        }
        assertTrue(exceptionSetCaptureMode, "setOptions captureMode error response")

        var exceptionSetOption = false
        try {
            thetaRepository.getVideoCaptureBuilder()
                .setMaxRecordableTime(ThetaRepository.MaxRecordableTimeEnum.RECORDABLE_TIME_300)
                .build()
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "")
            exceptionSetOption = true
        }
        assertTrue(exceptionSetOption, "setOptions option error response")

        // execute not json response
        var exceptionNotJson = false
        try {
            thetaRepository.getVideoCaptureBuilder()
                .build()
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(
                e.message!!.indexOf("json", 0, true) >= 0 || e.message!!.indexOf(
                    "Illegal",
                    0,
                    true
                ) >= 0,
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
            "Status error UnitTest", // status error not json
            "timeout UnitTest" // timeout
        )
        var counter = 0

        MockApiClient.onRequest = { _ ->
            val index = counter++
            when (index) {
                0 -> MockApiClient.status = HttpStatusCode.ServiceUnavailable
                1 -> MockApiClient.status = HttpStatusCode.ServiceUnavailable
                2 -> throw ConnectTimeoutException("timeout")
            }
            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)

        // execute status error and json response
        var exceptionStatusJson = false
        try {
            thetaRepository.getVideoCaptureBuilder()
                .build()
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(
                e.message!!.indexOf("UnitTest", 0, true) >= 0,
                "status error and json response"
            )
            exceptionStatusJson = true
        }
        assertTrue(exceptionStatusJson, "status error and json response")

        // execute status error and not json response
        var exceptionStatus = false
        try {
            thetaRepository.getVideoCaptureBuilder()
                .setMaxRecordableTime(ThetaRepository.MaxRecordableTimeEnum.RECORDABLE_TIME_300)
                .build()
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("503", 0, true) >= 0, "status error")
            exceptionStatus = true
        }
        assertTrue(exceptionStatus, "status error")

        // execute timeout exception
        var exceptionOther = false
        try {
            thetaRepository.getVideoCaptureBuilder()
                .build()
        } catch (e: ThetaRepository.NotConnectedException) {
            assertTrue(e.message!!.indexOf("time", 0, true) >= 0, "timeout exception")
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
            Resource("src/commonTest/resources/VideoCapture/start_capture_error.json").readText(), // startCapture error
            "Not json" // json error
        )
        var counter = 0
        MockApiClient.onRequest = { _ ->
            val index = counter++
            ByteReadChannel(responseArray[index])
        }

        val thetaRepository = ThetaRepository(endpoint)
        val videoCapture = thetaRepository.getVideoCaptureBuilder()
            .build()

        // execute error response
        var deferred = CompletableDeferred<Unit>()
        videoCapture.startCapture(object : VideoCapture.StartCaptureCallback {
            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "capture video")
                deferred.complete(Unit)
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(
                    exception.message!!.indexOf("UnitTest", 0, true) >= 0,
                    "capture video error response"
                )
                deferred.complete(Unit)
            }

            override fun onCaptureCompleted(fileUrl: String?) {
                assertTrue(false, "capture video")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }

        // execute json error response
        deferred = CompletableDeferred()
        videoCapture.startCapture(object : VideoCapture.StartCaptureCallback {
            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "capture video")
                deferred.complete(Unit)
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(exception.message!!.length >= 0, "capture video json error response")
                deferred.complete(Unit)
            }

            override fun onCaptureCompleted(fileUrl: String?) {
                assertTrue(false, "capture video")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(5000) {
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
            Resource("src/commonTest/resources/VideoCapture/start_capture_error.json").readText(), // startCapture error
        )
        var counter = 0
        MockApiClient.onRequest = { _ ->
            val index = counter++
            when (index) {
                0 -> MockApiClient.status = HttpStatusCode.OK
                1 -> MockApiClient.status = HttpStatusCode.ServiceUnavailable
            }
            ByteReadChannel(responseArray[index])
        }

        val thetaRepository = ThetaRepository(endpoint)
        val videoCapture = thetaRepository.getVideoCaptureBuilder()
            .build()

        // execute status error and json response
        val deferred = CompletableDeferred<Unit>()
        videoCapture.startCapture(object : VideoCapture.StartCaptureCallback {
            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "capture video")
                deferred.complete(Unit)
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(
                    exception.message!!.indexOf("UnitTest", 0, true) >= 0,
                    "status error and json response"
                )
                deferred.complete(Unit)
            }

            override fun onCaptureCompleted(fileUrl: String?) {
                assertTrue(false, "capture video")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(10000) {
                deferred.await()
            }
        }
    }

    /**
     * Error exception to startCapture call
     */
    @Test
    fun startCaptureNotJsonExceptionTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            "Status error UnitTest", // status error not json
        )
        var counter = 0
        MockApiClient.onRequest = { _ ->
            val index = counter++
            when (index) {
                0 -> MockApiClient.status = HttpStatusCode.OK
                1 -> MockApiClient.status = HttpStatusCode.ServiceUnavailable
            }
            ByteReadChannel(responseArray[index])
        }

        val thetaRepository = ThetaRepository(endpoint)
        val videoCapture = thetaRepository.getVideoCaptureBuilder()
            .build()

        val deferred = CompletableDeferred<Unit>()
        // execute status error and not json response
        videoCapture.startCapture(object : VideoCapture.StartCaptureCallback {
            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "capture video")
                deferred.complete(Unit)
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(exception.message!!.indexOf("503", 0, true) >= 0, "status error")
                deferred.complete(Unit)
            }

            override fun onCaptureCompleted(fileUrl: String?) {
                assertTrue(false, "capture video")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(10000) {
                deferred.await()
            }
        }
    }

    /**
     * Error exception to startCapture call
     */
    @Test
    fun startCaptureTimeoutExceptionTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            "timeout UnitTest" // timeout
        )
        var counter = 0
        MockApiClient.onRequest = { _ ->
            val index = counter++
            when (index) {
                0 -> MockApiClient.status = HttpStatusCode.OK
                1 -> throw ConnectTimeoutException("timeout")
            }
            ByteReadChannel(responseArray[index])
        }

        val thetaRepository = ThetaRepository(endpoint)
        val videoCapture = thetaRepository.getVideoCaptureBuilder()
            .build()

        val deferred = CompletableDeferred<Unit>()

        // execute timeout exception
        videoCapture.startCapture(object : VideoCapture.StartCaptureCallback {
            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "capture video")
                deferred.complete(Unit)
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(exception.message!!.indexOf("time", 0, true) >= 0, "timeout exception")
                deferred.complete(Unit)
            }

            override fun onCaptureCompleted(fileUrl: String?) {
                assertTrue(false, "capture video")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(10000) {
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
            Resource("src/commonTest/resources/VideoCapture/stop_capture_error.json").readText(), // stopCapture error
            "Not json" // json error
        )
        var counter = 0
        MockApiClient.onRequest = { _ ->
            val index = counter++
            ByteReadChannel(responseArray[index])
        }

        // execute error response
        var deferred = CompletableDeferred<Unit>()
        var videoCapturing = VideoCapturing(
            endpoint,
            object : VideoCapture.StartCaptureCallback {
                override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    assertTrue(
                        exception.message!!.indexOf("UnitTest", 0, true) >= 0,
                        "stop capture error response"
                    )
                    deferred.complete(Unit)
                }

                override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    assertTrue(false, "stop capture")
                    deferred.complete(Unit)
                }

                override fun onCaptureCompleted(fileUrl: String?) {
                    assertTrue(false, "stop capture")
                    deferred.complete(Unit)
                }
            }
        )
        videoCapturing.stopCapture()

        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }

        // execute json error response
        deferred = CompletableDeferred()
        videoCapturing = VideoCapturing(
            endpoint,
            object : VideoCapture.StartCaptureCallback {
                override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    assertTrue(exception.message!!.length >= 0, "stop capture json error response")
                    deferred.complete(Unit)
                }

                override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    assertTrue(false, "stop capture")
                    deferred.complete(Unit)
                }

                override fun onCaptureCompleted(fileUrl: String?) {
                    assertTrue(false, "stop capture")
                    deferred.complete(Unit)
                }
            }
        )
        videoCapturing.stopCapture()

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
            Resource("src/commonTest/resources/VideoCapture/stop_capture_error.json").readText(), // status error & error json
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

        // execute status error and json response
        var deferred = CompletableDeferred<Unit>()
        var videoCapturing = VideoCapturing(
            endpoint,
            object : VideoCapture.StartCaptureCallback {
                override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    assertTrue(
                        exception.message!!.indexOf("UnitTest", 0, true) >= 0,
                        "status error and json response"
                    )
                    deferred.complete(Unit)
                }

                override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    assertTrue(false, "stop capture")
                    deferred.complete(Unit)
                }

                override fun onCaptureCompleted(fileUrl: String?) {
                    assertTrue(false, "stop capture")
                    deferred.complete(Unit)
                }
            }
        )
        videoCapturing.stopCapture()

        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }

        // execute status error and not json response
        deferred = CompletableDeferred()
        videoCapturing = VideoCapturing(
            endpoint,
            object : VideoCapture.StartCaptureCallback {
                override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    assertTrue(exception.message!!.indexOf("503", 0, true) >= 0, "status error")
                    deferred.complete(Unit)
                }

                override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    assertTrue(false, "stop capture")
                    deferred.complete(Unit)
                }

                override fun onCaptureCompleted(fileUrl: String?) {
                    assertTrue(false, "stop capture")
                    deferred.complete(Unit)
                }
            }
        )
        videoCapturing.stopCapture()

        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }

        // execute timeout exception
        deferred = CompletableDeferred()
        videoCapturing = VideoCapturing(
            endpoint,
            object : VideoCapture.StartCaptureCallback {
                override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    assertTrue(
                        exception.message!!.indexOf("time", 0, true) >= 0,
                        "timeout exception"
                    )
                    deferred.complete(Unit)
                }

                override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    assertTrue(false, "stop capture")
                    deferred.complete(Unit)
                }

                override fun onCaptureCompleted(fileUrl: String?) {
                    assertTrue(false, "stop capture")
                    deferred.complete(Unit)
                }
            }
        )
        videoCapturing.stopCapture()

        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }
    }
}
