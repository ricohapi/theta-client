package com.ricoh360.thetaclient.capture

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CaptureMode
import io.ktor.client.network.sockets.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoCaptureTest {
    private val endpoint = "http://dummy/"

    @BeforeTest
    fun setup() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @AfterTest
    fun teardown() {
        MockApiClient.status = HttpStatusCode.OK
    }

    /**
     * call takePicture.
     */
    @Test
    fun takePictureTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/PhotoCapture/takepicture_progress.json").readText(),
            Resource("src/commonTest/resources/PhotoCapture/takepicture_done.json").readText()
        )
        val requestPathArray = arrayOf(
            "/osc/commands/execute",
            "/osc/commands/execute",
            "/osc/commands/status"
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++

            // check request
            assertEquals(request.url.encodedPath, requestPathArray[index], "take picture request")
            when (index) {
                0 -> {
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.IMAGE)
                }
                1 -> {
                    CheckRequest.checkCommandName(request, "camera.takePicture")
                }
            }

            ByteReadChannel(responseArray[index])
        }
        val deferred = CompletableDeferred<Unit>()

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        val photoCapture = thetaRepository.getPhotoCaptureBuilder()
            .build()

        assertNull(photoCapture.getFilter(), "set option filter")
        assertNull(photoCapture.getFileFormat(), "set option fileFormat")

        var file: String? = null
        photoCapture.takePicture(object : PhotoCapture.TakePictureCallback {
            override fun onSuccess(fileUrl: String) {
                file = fileUrl
                deferred.complete(Unit)
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error take picture")
                deferred.complete(Unit)
            }
        })
        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }

        // check result
        assertTrue(file?.startsWith("http://") ?: false, "take picture")
    }

    /**
     * call takePicture with filter and fileFormat options.
     */
    @Test
    fun takePictureWithFilterAndFileFormatTest() = runTest {
        // setup
        val filter = ThetaRepository.FilterEnum.HDR
        val fileFormat = ThetaRepository.PhotoFileFormatEnum.IMAGE_5K

        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/PhotoCapture/takepicture_progress.json").readText(),
            Resource("src/commonTest/resources/PhotoCapture/takepicture_done.json").readText()
        )
        val requestPathArray = arrayOf(
            "/osc/commands/execute",
            "/osc/commands/execute",
            "/osc/commands/execute",
            "/osc/commands/status"
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++

            // check request
            assertEquals(request.url.encodedPath, requestPathArray[index], "take picture request")
            when (index) {
                0 -> {
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.IMAGE)
                }
                1 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        filter = filter.filter,
                        fileFormat = fileFormat.fileFormat.toMediaFileFormat()
                    )
                }
                2 -> {
                    CheckRequest.checkCommandName(request, "camera.takePicture")
                }
            }

            ByteReadChannel(responseArray[index])
        }
        val deferred = CompletableDeferred<Unit>()

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        val photoCapture = thetaRepository.getPhotoCaptureBuilder()
            .setFilter(filter)
            .setFileFormat(fileFormat)
            .build()

        assertEquals(photoCapture.getFilter(), filter, "set option filter")
        assertEquals(photoCapture.getFileFormat(), fileFormat, "set option filter")

        var file: String? = null
        photoCapture.takePicture(object : PhotoCapture.TakePictureCallback {
            override fun onSuccess(fileUrl: String) {
                file = fileUrl
                deferred.complete(Unit)
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(false, "error take picture")
                deferred.complete(Unit)
            }
        })

        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }

        // check result
        assertTrue(file?.startsWith("http://") ?: false, "take picture")
    }

    /**
     * Setting filter.
     */
    @Test
    fun settingFilterTest() = runTest {
        // setup
        val filterList = ThetaRepository.FilterEnum.values()

        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText()
        )
        var counter = 0
        var filterIndex = 0

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
                        filter = filterList[filterIndex].filter
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)

        filterList.forEach {
            val photoCapture = thetaRepository.getPhotoCaptureBuilder()
                .setFilter(it)
                .build()

            // check result
            assertEquals(
                photoCapture.getFilter(),
                it,
                "set option filter $filterIndex"
            )

            filterIndex++
            counter = 0
        }
    }

    /**
     * Setting fileFormat.
     */
    @Test
    fun settingFileFormatTest() = runTest {
        // setup
        val fileFormatList = ThetaRepository.PhotoFileFormatEnum.values()

        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText()
        )
        var counter = 0
        var fileFormatIndex = 0

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
                        fileFormat = fileFormatList[fileFormatIndex].fileFormat.toMediaFileFormat()
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)

        fileFormatList.forEach {
            val photoCapture = thetaRepository.getPhotoCaptureBuilder()
                .setFileFormat(it)
                .build()

            // check result
            assertEquals(
                photoCapture.getFileFormat(),
                it,
                "set option fileFormat $fileFormatIndex"
            )

            fileFormatIndex++
            counter = 0
        }
    }

    /**
     * Setting aperture.
     */
    @Test
    fun settingApertureTest() = runTest {
        // setup
        val valueList = ThetaRepository.ApertureEnum.values()

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
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.IMAGE)
                }
                1 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        aperture = valueList[valueIndex].value
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)

        valueList.forEach {
            val photoCapture = thetaRepository.getPhotoCaptureBuilder()
                .setAperture(it)
                .build()

            // check result
            assertEquals(
                photoCapture.getAperture(),
                it,
                "set option aperture $valueIndex"
            )

            valueIndex++
            counter = 0
        }
    }

    /**
     * Setting colorTemperature.
     */
    @Test
    fun settingColorTemperatureTest() = runTest {
        // setup
        val valueList = arrayOf(2500, 2600, 10000)

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
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.IMAGE)
                }
                1 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        colorTemperature = valueList[valueIndex]
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)

        valueList.forEach {
            val photoCapture = thetaRepository.getPhotoCaptureBuilder()
                .setColorTemperature(it)
                .build()

            // check result
            assertEquals(
                photoCapture.getColorTemperature(),
                it,
                "set option colorTemperature $valueIndex"
            )

            valueIndex++
            counter = 0
        }
    }

    /**
     * Setting exposureCompensation.
     */
    @Test
    fun settingExposureCompensationTest() = runTest {
        // setup
        val valueList = ThetaRepository.ExposureCompensationEnum.values()

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
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.IMAGE)
                }
                1 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        exposureCompensation = valueList[valueIndex].value
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)

        valueList.forEach {
            val photoCapture = thetaRepository.getPhotoCaptureBuilder()
                .setExposureCompensation(it)
                .build()

            // check result
            assertEquals(
                photoCapture.getExposureCompensation(),
                it,
                "set option exposureCompensation $valueIndex"
            )

            valueIndex++
            counter = 0
        }
    }

    /**
     * Setting exposureDelay.
     */
    @Test
    fun settingExposureDelayTest() = runTest {
        // setup
        val valueList = ThetaRepository.ExposureDelayEnum.values()

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
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.IMAGE)
                }
                1 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        exposureDelay = valueList[valueIndex].sec
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)

        valueList.forEach {
            val photoCapture = thetaRepository.getPhotoCaptureBuilder()
                .setExposureDelay(it)
                .build()

            // check result
            assertEquals(
                photoCapture.getExposureDelay(),
                it,
                "set option exposureDelay $valueIndex"
            )

            valueIndex++
            counter = 0
        }
    }

    /**
     * Setting exposureProgram.
     */
    @Test
    fun settingExposureProgramTest() = runTest {
        // setup
        val valueList = ThetaRepository.ExposureProgramEnum.values()

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
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.IMAGE)
                }
                1 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        exposureProgram = valueList[valueIndex].value
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)

        valueList.forEach {
            val photoCapture = thetaRepository.getPhotoCaptureBuilder()
                .setExposureProgram(it)
                .build()

            // check result
            assertEquals(
                photoCapture.getExposureProgram(),
                it,
                "set option exposureProgram $valueIndex"
            )

            valueIndex++
            counter = 0
        }
    }

    /**
     * Setting gpsInfo.
     */
    @Test
    fun settingGpsInfoTest() = runTest {
        // setup
        val valueList = arrayOf(
            ThetaRepository.GpsInfo.disabled,
            ThetaRepository.GpsInfo(0f, 0f, 0f, "2022:11:09 11:05:18+09:00"),
            ThetaRepository.GpsInfo(10f, 100f, 20f, "2022:01:01 00:01:00+09:00")
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
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.IMAGE)
                }
                1 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        gpsInfo = valueList[valueIndex].toTransferredGpsInfo()
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)

        valueList.forEach {
            val photoCapture = thetaRepository.getPhotoCaptureBuilder()
                .setGpsInfo(it)
                .build()

            // check result
            assertEquals(
                photoCapture.getGpsInfo(),
                it,
                "set option gpsInfo $valueIndex"
            )

            valueIndex++
            counter = 0
        }
    }

    /**
     * Setting gpsTagRecording.
     */
    @Test
    fun settingGpsTagRecordingTest() = runTest {
        // setup
        val valueList = ThetaRepository.GpsTagRecordingEnum.values()

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
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.IMAGE)
                }
                1 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        gpsTagRecording = valueList[valueIndex].value
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)

        valueList.forEach {
            val photoCapture = thetaRepository.getPhotoCaptureBuilder()
                .setGpsTagRecording(it)
                .build()

            // check result
            assertEquals(
                photoCapture.getGpsTagRecording(),
                it,
                "set option gpsTagRecording $valueIndex"
            )

            valueIndex++
            counter = 0
        }
    }

    /**
     * Setting iso.
     */
    @Test
    fun settingIsoTest() = runTest {
        // setup
        val valueList = ThetaRepository.IsoEnum.values()

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
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.IMAGE)
                }
                1 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        iso = valueList[valueIndex].value
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)

        valueList.forEach {
            val photoCapture = thetaRepository.getPhotoCaptureBuilder()
                .setIso(it)
                .build()

            // check result
            assertEquals(
                photoCapture.getIso(),
                it,
                "set option iso $valueIndex"
            )

            valueIndex++
            counter = 0
        }
    }

    /**
     * Setting isoAutoHighLimit.
     */
    @Test
    fun settingIsoAutoHighLimitTest() = runTest {
        // setup
        val valueList = ThetaRepository.IsoAutoHighLimitEnum.values()

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
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.IMAGE)
                }
                1 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        isoAutoHighLimit = valueList[valueIndex].value
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)

        valueList.forEach {
            val photoCapture = thetaRepository.getPhotoCaptureBuilder()
                .setIsoAutoHighLimit(it)
                .build()

            // check result
            assertEquals(
                photoCapture.getIsoAutoHighLimit(),
                it,
                "set option isoAutoHighLimit $valueIndex"
            )

            valueIndex++
            counter = 0
        }
    }

    /**
     * Setting whiteBalance.
     */
    @Test
    fun settingWhiteBalanceTest() = runTest {
        // setup
        val valueList = ThetaRepository.WhiteBalanceEnum.values()

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
                    CheckRequest.checkSetOptions(request = request, captureMode = CaptureMode.IMAGE)
                }
                1 -> {
                    CheckRequest.checkSetOptions(
                        request = request,
                        whiteBalance = valueList[valueIndex].value
                    )
                }
            }

            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)

        valueList.forEach {
            val photoCapture = thetaRepository.getPhotoCaptureBuilder()
                .setWhiteBalance(it)
                .build()

            // check result
            assertEquals(
                photoCapture.getWhiteBalance(),
                it,
                "set option whiteBalance $valueIndex"
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
            thetaRepository.getPhotoCaptureBuilder()
                .build()
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "setOptions captureMode error response")
            exceptionSetCaptureMode = true
        }
        assertTrue(exceptionSetCaptureMode, "setOptions captureMode error response")

        var exceptionSetOption = false
        try {
            thetaRepository.getPhotoCaptureBuilder()
                .setFilter(ThetaRepository.FilterEnum.HDR)
                .build()
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "setOptions option error response")
            exceptionSetOption = true
        }
        assertTrue(exceptionSetOption, "setOptions option error response")

        // execute not json response
        var exceptionNotJson = false
        try {
            thetaRepository.getPhotoCaptureBuilder()
                .setFilter(ThetaRepository.FilterEnum.HDR)
                .build()
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(
                e.message!!.indexOf("json", 0, true) >= 0 ||
                    e.message!!.indexOf("Illegal", 0, true) >= 0,
                "setOptions option not json error response"
            )
            exceptionNotJson = true
        }
        assertTrue(exceptionNotJson, "setOptions option not json error response")
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

        val thetaRepository = ThetaRepository(endpoint)

        // execute status error and json response
        var exceptionStatusJson = false
        try {
            thetaRepository.getPhotoCaptureBuilder()
                .build()
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("UnitTest", 0, true) >= 0, "status error and json response")
            exceptionStatusJson = true
        }
        assertTrue(exceptionStatusJson, "status error and json response")

        // execute status error and not json response
        var exceptionStatus = false
        try {
            thetaRepository.getPhotoCaptureBuilder()
                .setFilter(ThetaRepository.FilterEnum.HDR)
                .build()
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("503", 0, true) >= 0, "status error")
            exceptionStatus = true
        }
        assertTrue(exceptionStatus, "status error")

        // execute timeout exception
        var exceptionOther = false
        try {
            thetaRepository.getPhotoCaptureBuilder()
                .setFilter(ThetaRepository.FilterEnum.HDR)
                .build()
        } catch (e: ThetaRepository.NotConnectedException) {
            assertTrue(e.message!!.indexOf("time", 0, true) >= 0, "timeout exception")
            exceptionOther = true
        }
        assertTrue(exceptionOther, "other exception")
    }

    /**
     * Error response to takePicture call.
     */
    @Test
    fun takePictureErrorResponseTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/PhotoCapture/takepicture_error.json").readText(), // takePicture error
            Resource("src/commonTest/resources/PhotoCapture/takepicture_progress.json").readText(),
            Resource("src/commonTest/resources/PhotoCapture/takepicture_error.json").readText(), // takePicture status error
            "Not json" // json error
        )
        var counter = 0

        MockApiClient.onRequest = { _ ->
            val index = counter++
            ByteReadChannel(responseArray[index])
        }

        // execute
        val thetaRepository = ThetaRepository(endpoint)
        val photoCapture = thetaRepository.getPhotoCaptureBuilder()
            .build()

        // execute takePicture error response
        var deferred = CompletableDeferred<Unit>()
        photoCapture.takePicture(object : PhotoCapture.TakePictureCallback {
            override fun onSuccess(fileUrl: String) {
                assertTrue(false, "take picture")
                deferred.complete(Unit)
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(exception.message!!.indexOf("UnitTest", 0, true) >= 0, "take picture error response")
                deferred.complete(Unit)
            }
        })
        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }

        // execute status error response
        deferred = CompletableDeferred()
        photoCapture.takePicture(object : PhotoCapture.TakePictureCallback {
            override fun onSuccess(fileUrl: String) {
                assertTrue(false, "take picture")
                deferred.complete(Unit)
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(exception.message!!.indexOf("UnitTest", 0, true) >= 0, "take picture progress error response")
                deferred.complete(Unit)
            }
        })
        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }

        // execute not json response
        deferred = CompletableDeferred()
        photoCapture.takePicture(object : PhotoCapture.TakePictureCallback {
            override fun onSuccess(fileUrl: String) {
                assertTrue(false, "take picture")
                deferred.complete(Unit)
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                print(exception.message)
                assertTrue(exception.message!!.length >= 0, "take picture json error response")
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
     * Error exception to takePicture call.
     */
    @Test
    fun takePictureExceptionTest() = runTest {
        // setup
        val responseArray = arrayOf(
            Resource("src/commonTest/resources/setOptions/set_options_done.json").readText(),
            Resource("src/commonTest/resources/PhotoCapture/takepicture_error.json").readText(), // status error & error json
            "Status error UnitTest", // status error not json
            "timeout UnitTest" // timeout
        )
        var counter = 0

        MockApiClient.onRequest = { _ ->
            val index = counter++
            when (index) {
                0 -> MockApiClient.status = HttpStatusCode.OK
                1 -> MockApiClient.status = HttpStatusCode.ServiceUnavailable
                2 -> MockApiClient.status = HttpStatusCode.ServiceUnavailable
                3 -> throw ConnectTimeoutException("timeout")
            }
            ByteReadChannel(responseArray[index])
        }

        val thetaRepository = ThetaRepository(endpoint)
        val photoCapture = thetaRepository.getPhotoCaptureBuilder()
            .build()

        // execute status error and json response
        var deferred = CompletableDeferred<Unit>()
        photoCapture.takePicture(object : PhotoCapture.TakePictureCallback {
            override fun onSuccess(fileUrl: String) {
                assertTrue(false, "take picture")
                deferred.complete(Unit)
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(exception.message!!.indexOf("UnitTest", 0, true) >= 0, "status error and json response")
                deferred.complete(Unit)
            }
        })
        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }

        // execute status error and not json response
        deferred = CompletableDeferred()
        photoCapture.takePicture(object : PhotoCapture.TakePictureCallback {
            override fun onSuccess(fileUrl: String) {
                assertTrue(false, "take picture")
                deferred.complete(Unit)
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(exception.message!!.indexOf("503", 0, true) >= 0, "status error")
                deferred.complete(Unit)
            }
        })
        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }

        // execute timeout exception
        deferred = CompletableDeferred()
        photoCapture.takePicture(object : PhotoCapture.TakePictureCallback {
            override fun onSuccess(fileUrl: String) {
                assertTrue(false, "take picture")
                deferred.complete(Unit)
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                assertTrue(exception.message!!.indexOf("time", 0, true) >= 0, "timeout exception")
                deferred.complete(Unit)
            }
        })
        runBlocking {
            withTimeout(5000) {
                deferred.await()
            }
        }
    }
}
