package com.ricoh360.thetaclient

import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class LocalMockApiTest {
    val endpoint = "http://localhost:8000/"

    @BeforeTest
    fun setup() {
    }

    @AfterTest
    fun teardown() {
    }
    /*
        @kotlinx.coroutines.ExperimentalCoroutinesApi
        @Test
        fun infoApiTest() = runTest {
            val info = ThetaApi.callInfoApi(endpoint)
            assertEquals("RICOH", info.manufacturer, "info.manufacturer")
            assertEquals("RICOH THETA S", info.model, "info.model")
            assertEquals("00001234", info.serialNumber, "info.serialNumber")
            assertEquals("3c:22:fb:7f:0b:cb", info._wlanMacAddress, "info._wlanMacAddress")
            assertEquals(
                "3c:22:fb:7f:0b:cb", info._bluetoothMacAddress,
                "info._bluetoothMacAddress"
            )
            assertEquals("1.62", info.firmwareVersion, "info.firmwareVersion")
            assertEquals(
                "https://theta360.com/en/support/", info.supportUrl,
                "info.supportUrl"
            )
            assertEquals(false, info.gps, "info.gps")
            assertEquals(false, info.gyro, "info.gyro")
            assertEquals(67, info.uptime, "info.uptime")
            assertEquals(
                listOf(
                    "/osc/info", "/osc/state", "/osc/checkForUpdates",
                    "/osc/commands/execute", "/osc/commands/status"
                ),
                info.api, "info.api"
            )
            assertEquals(listOf(1, 2), info.apiLevel, "info.apiLevel")

            assertEquals(80, info.endpoints.httpPort, "endpoint.httpPort")
            assertEquals(80, info.endpoints.httpUpdatesPort, "endpoint.httpUpdatePort")
        }

        @kotlinx.coroutines.ExperimentalCoroutinesApi
        @Test
        fun stateApiTest() = runTest {
            val response = ThetaApi.callStateApi(endpoint)
            assertEquals("12EGA33", response.fingerprint, "fingerprint")
            val state = response.state
            assertEquals(0.33, state.batteryLevel, "state.batteryLevel")
            assertEquals("${endpoint}files/abcde/", state.storageUri, "state.storageUri")
            assertEquals("storage ID", state._storageID, "state._storageID")
            assertEquals(CaptureStatus.IDLE, state._captureStatus, "state._captureStatus")
            assertEquals(0, state._recordedTime, "state._recordedTime")
            assertEquals(300, state._recordableTime, "state._recordableTime")
            assertEquals(500, state._capturedPictures, "state._capturedPictures")
            assertEquals(
                10, state._compositeShootingElapsedTime,
                "state._compositeShootingElapsedTime"
            )
            assertEquals(
                "${endpoint}files/abcde/100RICOH/R0010015.JPG",
                state._latestFileUrl, "state._latestFileUrl"
            )
            assertEquals(ChargingState.DISCONNECT, state._batteryState, "state._batteryState")
            assertEquals(2, state._apiVersion, "state._apiVersion")
            assertEquals(false, state._pluginRunning, "state._pluginRunning")
            assertEquals(false, state._pluginWebServer, "state._pluginWebServer")
            assertEquals(ShootingFunction.NORMAL, state._function, "state._function")
            assertEquals(false, state._mySettingChanged, "state._mySettingChanged")
            assertEquals(
                MicrophoneOption.AUTO, state._currentMicrophone,
                "state._currentMicrophone"
            )
            assertEquals(StorageOption.IN, state._currentStorage, "state._currentStorage")
            assertEquals(listOf(), state._cameraError, "state._cameraError")
            assertEquals(false, state._batteryInsert, "state._batterylnsert")
        }

        @kotlinx.coroutines.ExperimentalCoroutinesApi
        @Test
        fun listFilesTest() = runTest {
            val params = ListFilesParams(
                fileType = FileType.ALL,
                startPosition = 0,
                _startFileUrl = "${endpoint}files",
                entryCount = 3,
                maxThumbSize = 10,
                _detail = false,
                _sort = SortOrder.OLDEST
            )
            val response = ThetaApi.callListFilesCommand(endpoint, params)
            assertEquals("camera.listFiles", response.name, "name")
            assertEquals(CommandState.DONE, response.state, "state")
            assertNotNull(response.results, "results")
            assertNull(response.progress, "progress")
            assertNull(response.error, "error")

            assertEquals(3, response.results?.totalEntries, "totalEntries")
            assertFalse(response.results!!.entries.isEmpty(), "entries")
            var c = 0
            response.results!!.entries.forEach {
                assertEquals("R00${1001 + c}.JPG", it.name, "name")
                assertEquals(
                    "${endpoint}files/R00${1001 + c}.JPG", it.fileUrl,
                    "fileUrl"
                )
                c++
                assertEquals(4051440, it.size, "size")
                assertEquals("2015:07:10 11:05:18+09:00", it.dateTimeZone, "dateTimeZone")
                assertEquals("2015:07:10 11:05:18", it.dateTime, "dateTime")
                assertEquals(50.5324f, it.lat, "lat")
                assertEquals(-120.2332f, it.lng, "lng")
                assertEquals(5376, it.width, "width")
                assertEquals(2688, it.height, "height")
                assertEquals("thumbnail base64 string", it.thumbnail, "thumbnail")
                assertEquals(3348, it._thumbSize, "_thumbSize")
                assertEquals("123", it._intervalCaptureGroupId, "_intervalCaptureGroupId")
                assertEquals("XYZ", it._compositeShootingGroupId, "_compositeShootingGroupId")
                assertEquals("ABZ", it._autoBracketGroupId, "_autoBracketGroupId")
                assertEquals(34, it._recordTime, "_recordTime")
                assertEquals(true, it.isProcessed, "isProcessed")
                assertEquals("preview url", it.previewUrl, "previewUrl")
                assertEquals("H264", it._codec, "_codec")
                assertEquals(
                    _ProjectionType.EQUIRECTANGULAR, it._projectionType,
                    "_projectionType"
                )
                assertEquals(
                    "CSH", it._continuousShootingGroupId,
                    "_continuousShootingGroupId"
                )
                assertEquals(60, it._frameRate, "_frameRate")
                assertEquals(false, it._favorite, "_favorite")
                assertEquals("good image", it._imageDescription, "_imageDescription")

                // Test to get a photo
                assertEquals(
                    HttpStatusCode.OK,
                    ThetaApi.httpClient.get(it.fileUrl).status
                )
                assertEquals(
                    HttpStatusCode.OK,
                    ThetaApi.httpClient.get(it.getThumnailUrl()).status
                )
            }
            val sres = ThetaApi.callStatusApi(endpoint, StatusApiParams(name = response.name))
            assertEquals(response, sres, "statusapi")
        }

        @kotlinx.coroutines.ExperimentalCoroutinesApi
        @Test
        fun getLivePreviewTest() = runTest {
            var count = 3 // read three frames
            ThetaApi.callGetLivePreviewCommand(endpoint) {
                if (!isActive) {
                    println("CoroutineScope.isActive is false")
                    return@callGetLivePreviewCommand false
                }
                val byteArray = it.readBytes()
                println("Got a preview frame ${byteArray::class.simpleName}")
                it.release()
                assertTrue(true, "Preview callback")
                return@callGetLivePreviewCommand if (--count > 0) true else false
            }
        }

        @kotlinx.coroutines.ExperimentalCoroutinesApi
        @Test
        fun takePictureTest() = runTest {
            val response = ThetaApi.callTakePictureCommand(endpoint)
            assertTrue(true, "take picture")
            assertEquals("camera.takePicture", response.name, "name")
            assertEquals(CommandState.DONE, response.state, "state")
            assertNotNull(response.id, "id")
            assertNotNull(response.results?.fileUrl, "fileUrl")
            val sres = ThetaApi.callStatusApi(endpoint, StatusApiParams(name = response.name))
            assertEquals(response, sres, "statusapi")
        }

        @kotlinx.coroutines.ExperimentalCoroutinesApi
        @Test
        fun cancelVideoConvertTest() = runTest {
            val response = ThetaApi.callCancelVideoConvertCommand(endpoint)
            assertEquals("camera._cancelVideoConvert", response.name, "name")
            assertEquals(CommandState.DONE, response.state, "state")
            val sres = ThetaApi.callStatusApi(endpoint, StatusApiParams(name = response.name))
            assertEquals(response, sres, "statusapi")
        }

        @kotlinx.coroutines.ExperimentalCoroutinesApi
        @Test
        fun convertVideoFormatsTest() = runTest {
            val params = ConvertVideoFormatsParams(
                fileUrl = "file url to convert",
                size = VideoFormat.VIDEO_4K,
                projectionType = _ProjectionType.EQUIRECTANGULAR,
                codec = "H.264/MPEG-4 AVC",
                topBottomCorrection = TopBottomCorrection.APPLY
            )
            val response = ThetaApi.callConvertVideoFormatsCommand(endpoint, params)
            assertEquals("camera._convertVideoFormats", response.name, "name")
            assertEquals(CommandState.DONE, response.state, "state")
            val sres = ThetaApi.callStatusApi(endpoint, StatusApiParams(name = response.name))
            assertEquals(response, sres, "statusapi")
        }

        @kotlinx.coroutines.ExperimentalCoroutinesApi
        @Test
        fun deleteTest() = runTest {
            val fileUrls: List<String> = listOf("file1", "file2", "file3")
            val params = DeleteParams(fileUrls = fileUrls)
            val response = ThetaApi.callDeleteCommand(endpoint, params)
            assertEquals("camera.delete", response.name, "name")
            assertEquals(CommandState.DONE, response.state, "state")
            val sres = ThetaApi.callStatusApi(endpoint, StatusApiParams(name = response.name))
            assertEquals(response, sres, "statusapi")
        }

        @kotlinx.coroutines.ExperimentalCoroutinesApi
        @Test
        fun finishWlanTest() = runTest {
            val response = ThetaApi.callFinishWlanCommand(endpoint)
            assertEquals("camera._finishWlan", response.name, "name")
            assertEquals(CommandState.DONE, response.state, "state")
            val sres = ThetaApi.callStatusApi(endpoint, StatusApiParams(name = response.name))
            assertEquals(response, sres, "statusapi")
        }

        @kotlinx.coroutines.ExperimentalCoroutinesApi
        @Test
        fun getMetadataTest() = runTest {
            val params = GetMetadataParams(fileUrl = "file1")
            val response = ThetaApi.callGetMetadataCommand(endpoint, params)
            assertEquals("camera._getMetadata", response.name, "name")
            assertEquals(CommandState.DONE, response.state, "state")
            assertNotNull(response.results?.exif, "results.exif")
            val exif = response.results!!.exif
            assertEquals("0230", exif.ExifVersion, "exif.ExifVersion")
            assertEquals("image description", exif.ImageDescription, "exif.ImageDescription")
            assertEquals("2021:06:09 19:34:17", exif.DateTime, "exif.DateTime")
            assertEquals(5376, exif.ImageWidth, "exif.ImageWidth")
            assertEquals(2688, exif.ImageLength, "exif.ImageLength")
            assertEquals(1, exif.ColorSpace, "exif.ColorSpace")
            assertEquals(6, exif.Compression, "exif.Compression")
            assertEquals(1, exif.Orientation, "exif.Orientation")
            assertEquals(0, exif.Flash, "exif.Flash")
            assertEquals(0.75f, exif.FocalLength, "exif.FocalLength")
            assertEquals(0, exif.WhiteBalance, "exif.WhiteBalance")
            assertEquals(0.0333, exif.ExposureTime, "exif.ExposureTime")
            assertEquals(2.0, exif.FNumber, "exif.FNumber")
            assertEquals(2, exif.ExposureProgram, "exif.ExposureProgram")
            assertEquals(1, exif.PhotographicSensitivity, "exif.PhotographicSensitivity")
            assertEquals(2.0, exif.ApertureValue, "exif.ApertureValue")
            assertEquals(0.5f, exif.BrightnessValue, "exif.BrightnessValue")
            assertEquals(0.0f, exif.ExposureBiasValue, "exif.ExposureBiasValue")
            assertEquals("north", exif.GPSLatitudeRef, "exif.GPSLatitudeRef")
            assertEquals(12.5, exif.GPSLatitude, "exif.GPSLatitude")
            assertEquals("west", exif.GPSLongitudeRef, "exif.GPSLongitudeRef")
            assertEquals(10.5, exif.GPSLongitude, "exif.GPSLongitude")
            assertEquals("RICOH", exif.Make, "exif.Make")
            assertEquals("RICOH THETA S", exif.Model, "exif.Model")
            assertEquals("RICOH THETA S Ver 1.11", exif.Software, "exif.Software")
            assertEquals("2022 ricoh co ltd.", exif.Copyright, "exif.Copyright")
            assertNotNull(response.results?.xmp, "results.xmp")
            val xmp = response.results!!.xmp
            assertEquals(
                ProjectionType.EQUIRECTANGULAR, xmp.ProjectionType,
                "xmp.ProjectionType"
            )
            assertEquals(true, xmp.UsePanoramaViewer, "xmp.UsePanoramaViewer")
            assertEquals(2.5, xmp.PoseHeadingDegrees, "xmp.PoseHeadingDegrees")
            assertEquals(
                5376, xmp.CroppedAreaImageWidthPixels,
                "xmp.CroppedAreaImageWidthPixels"
            )
            assertEquals(
                2688, xmp.CroppedAreaImageHeightPixels,
                "xmp.CroppedAreaImageHeightPixels"
            )
            assertEquals(5376, xmp.FullPanoWidthPixels, "xmp.FullPanoWidthPixels")
            assertEquals(2688, xmp.FullPanoHeightPixels, "xmp.FullPanoHeightPixels")
            assertEquals(0, xmp.CroppedAreaLeftPixels, "xmp.CroppedAreaLeftPixels")
            assertEquals(0, xmp.CroppedAreaTopPixels, "xmp.CroppedAreaTopPixels")
            val sres = ThetaApi.callStatusApi(endpoint, StatusApiParams(name = response.name))
            assertEquals(response, sres, "statusapi")
        }

        @kotlinx.coroutines.ExperimentalCoroutinesApi
        @Test
        fun resetTest() = runTest {
            val response = ThetaApi.callResetCommand(endpoint)
            assertEquals("camera.reset", response.name, "name")
            assertEquals(CommandState.DONE, response.state, "state")
            val sres = ThetaApi.callStatusApi(endpoint, StatusApiParams(name = response.name))
            assertEquals(response, sres, "statusapi")
        }

        @kotlinx.coroutines.ExperimentalCoroutinesApi
        @Test
        fun listAccessPointsTest() = runTest {
            val response = ThetaApi.callListAccessPointsCommand(endpoint)
            assertEquals("camera._listAccessPoints", response.name, "name")
            assertEquals(CommandState.DONE, response.state, "state")
            assertNotNull(response.results?.accessPoints, "accessPoints")
            response.results!!.accessPoints.forEach {
                assertEquals("dummy ssid", it.ssid, "xmp.ssid")
                assertEquals(true, it.ssidStealth, "xmp.ssidStealth")
                assertEquals(AuthenticationMode.WPA_WPA2_PSK, it.security, "xmp.security")
                assertEquals(0, it.connectionPriority, "xmp.connectionPriority")
                assertEquals(
                    IpAddressAllocation.STATIC, it.ipAddressAllocation,
                    "xmp.ipAddressAllocation"
                )
                assertEquals("172.16.1.13", it.ipAddress, "xmp.ipAddress")
                assertEquals("255.255.0.0", it.subnetMask, "xmp.subnetMask")
                assertEquals("172.16.1.1", it.defaultGateway, "xmp.defaultGateway")
            }
            val sres = ThetaApi.callStatusApi(endpoint, StatusApiParams(name = response.name))
            assertEquals(response, sres, "statusapi")
        }

        @kotlinx.coroutines.ExperimentalCoroutinesApi
        @Test
        fun startCaptureTest() = runTest {
            val params = StartCaptureParams(_mode = ShootingMode.INTERVAL_SHOOTING)
            val response = ThetaApi.callStartCaptureCommand(endpoint, params)
            assertEquals("camera.startCapture", response.name, "name")
            assertEquals(CommandState.DONE, response.state, "state")
            assertNotNull(response.results?.fileUrls, "fileUrls")
        }

        @kotlinx.coroutines.ExperimentalCoroutinesApi
        @Test
        fun stopCaptureTest() = runTest {
            val response = ThetaApi.callStopCaptureCommand(endpoint)
            assertEquals("camera.stopCapture", response.name, "name")
            assertEquals(CommandState.DONE, response.state, "state")
            assertNotNull(response.results?.fileUrls, "fileUrls")
            val sres = ThetaApi.callStatusApi(endpoint, StatusApiParams(name = response.name))
            assertEquals(response, sres, "statusapi")
        }

        @kotlinx.coroutines.ExperimentalCoroutinesApi
        @Test
        fun deleteAccessPointTest() = runTest {
            val params = DeleteAccessPointParams(ssid = "ssid to delete")
            val response = ThetaApi.callDeleteAccessPointCommand(endpoint, params)
            assertEquals("camera._deleteAccessPoint", response.name, "name")
            assertEquals(CommandState.DONE, response.state, "state")
            val sres = ThetaApi.callStatusApi(endpoint, StatusApiParams(name = response.name))
            assertEquals(response, sres, "statusapi")
        }

        @kotlinx.coroutines.ExperimentalCoroutinesApi
        @Test
        fun setAccessPointTest() = runTest {
            val params = SetAccessPointParams(
                ssid = "ssid to set",
                ssidStealth = false,
                security = AuthenticationMode.WEP,
                password = "password",
                connectionPriority = 1,
                ipAddressAllocation = IpAddressAllocation.STATIC,
                ipAddress = "172.16.1.14",
                subnetMask = "255.255.0.0",
                defaultGateway = "172.16.1.1"
            )
            val response = ThetaApi.callSetAccessPointCommand(endpoint, params)
            assertEquals("camera._setAccessPoint", response.name, "name")
            assertEquals(CommandState.DONE, response.state, "state")
            val sres = ThetaApi.callStatusApi(endpoint, StatusApiParams(name = response.name))
            assertEquals(response, sres, "statusapi")
        }

        @kotlinx.coroutines.ExperimentalCoroutinesApi
        @Test
        fun stopSelfTimerTestTest() = runTest {
            val response = ThetaApi.callStopSelfTimerCommand(endpoint)
            assertEquals("camera._stopSelfTimer", response.name, "name")
            assertEquals(CommandState.DONE, response.state, "state")
            val sres = ThetaApi.callStatusApi(endpoint, StatusApiParams(name = response.name))
            assertEquals(response, sres, "statusapi")
        }

        @kotlinx.coroutines.ExperimentalCoroutinesApi
        @Test
        fun exceptionTest() = runTest {
            try {
                ThetaApi.httpClient.get("${endpoint}not/found/data")
                assertTrue(false, "should throw exception")
            } catch (t: Throwable) {
                assertTrue(t is ClientRequestException, "exception type")
                assertEquals(404, t.response.status.value, "received status")
            }
        }

        @kotlinx.coroutines.ExperimentalCoroutinesApi
        @Test
        fun startSessionTest() = runTest {
            val response = ThetaApi.callStartSessionCommand(endpoint)
            assertEquals("camera.startSession", response.name, "name")
            assertEquals(CommandState.DONE, response.state, "state")
            assertNotNull(response.results?.sessionId, "sessionId")
            assertEquals(180, response.results?.timeout, "timeout")
            val sres = ThetaApi.callStatusApi(endpoint, StatusApiParams(name = response.name))
            assertEquals(response, sres, "statusapi")
        }
    */
}
