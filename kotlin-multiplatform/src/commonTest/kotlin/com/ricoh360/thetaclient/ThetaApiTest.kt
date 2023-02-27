package com.ricoh360.thetaclient

import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class ThetaApiTest {
    val endpoint = "http://192.168.1.1:80/"

    @BeforeTest
    fun setup() {
    }

    @AfterTest
    fun teardown() {
    }

/*
    @Test
    fun infoApiTest() = runTest {
        val info = ThetaApi.callInfoApi(endpoint)
        println("oscInfo: $info")
        assertTrue(info.manufacturer.startsWith("ricoh", ignoreCase = true), "manufacturer")
        assertTrue(info.model.startsWith("RICOH THETA "), "model")
        assertNotNull(info.serialNumber.toIntOrNull(), "serialNumber")
        assertNotNull(info._wlanMacAddress?.isEmpty(), "_wlanMacAddress")
        assertFalse(info._wlanMacAddress!!.isEmpty(), "_wlanMacAddress")
        assertNotNull(info._bluetoothMacAddress?.isEmpty(), "_bluetoothMacAddress")
        assertFalse(info.firmwareVersion.isEmpty(), "firmwareVersion")
        assertFalse(info.supportUrl.isEmpty(), "supportUrl")
        assertTrue(info.api.isNotEmpty(), "api")
        assertTrue(info.apiLevel.isNotEmpty(), "apiLevel")
    }

    @Test
    fun listFilesTest() = runTest {
        val params = ListFilesParams(entryCount = 3)
        val fileList: ListFilesResponse = ThetaApi.callListFilesCommand(endpoint, params)
        println("listFiles: $fileList")
        assertFalse(fileList.name.isEmpty(), "name")
        assertTrue(fileList.state == "done", "state")
        assertNotNull(fileList.results, "results")
        assertNull(fileList.progress, "progress")
        assertNull(fileList.error, "error")

        // Test the recult of command [camera.listFiles](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.list_files.md)
        assertTrue(fileList.results!!.totalEntries >= 1, "totalEntries")
        assertFalse(fileList.results!!.entries.isEmpty(), "entries")
        fileList.results!!.entries.forEach {
            assertFalse(it.name.isEmpty(), "name")
            assertFalse(it.fileUrl.isEmpty(), "fileUrl")
            assertNotNull(it.dateTimeZone, "dateTimeZone")
            assertFalse(it.dateTimeZone!!.isEmpty(), "dateTimeZone")
            assertNull(it.dateTime, "dateTime")
            //assertNotNull(it.lat, "lat")
            //assertNotNull(it.lat, "lng")
            assertNotNull(it.width, "width")
            assertNotNull(it.height, "height")
            //assertNotNull(it.thumbnail, "thumbnail")
            //assertFalse(it.thumbnail!!.isEmpty(), "thumbnail")
            //assertNotNull(it._thumbSize, "_thumbSize")
            assertTrue(it.previewUrl.isEmpty(), "previewUrl")
            assertNotNull(it._projectionType, "_projectionType")
            assertTrue(it._projectionType == "Equirectangular" || it._projectionType == "Dual-Fisheye")

            // Test to get a photo
            assertEquals(HttpStatusCode.OK, ThetaApi.httpClient.get(it.fileUrl).status)
            assertEquals(HttpStatusCode.OK, ThetaApi.httpClient.get(it.getThumnailUrl()).status)
        }
    }

    @Test
    fun getLivePreviewTest() = runTest {
        var count = 3 // read three frames
        ThetaApi.callGetLivePreview(endpoint) {
            if(!isActive) {
                println("CoroutineScope.isActive is false")
                return@callGetLivePreview false
            }
            val byteArray = it.readBytes()
            println("Got a preview frame")
            it.release()
            assertTrue(true, "Preview callback")
            return@callGetLivePreview if (--count > 0) true else false
        }
    }

    @Test
    fun takePictureTest() = runTest {
        ThetaApi.callTakePictureCommand(endpoint)
        assertTrue(true, "take picture")
    }
*/
}
