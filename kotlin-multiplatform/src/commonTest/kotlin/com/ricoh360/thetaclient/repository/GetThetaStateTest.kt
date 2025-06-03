package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CameraError
import com.ricoh360.thetaclient.transferred.CameraState
import com.ricoh360.thetaclient.transferred.CaptureStatus
import com.ricoh360.thetaclient.transferred.ChargingState
import com.ricoh360.thetaclient.transferred.MicrophoneOption
import com.ricoh360.thetaclient.transferred.ShootingFunction
import com.ricoh360.thetaclient.transferred.StateApiResponse
import com.ricoh360.thetaclient.transferred.StorageOption
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GetThetaStateTest {
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
     * call getThetaState for THETA Z1.
     */
    @Test
    fun getThetaStateForZ1Test() = runTest {
        // setup
        val jsonString = Resource("src/commonTest/resources/state/state_z1.json").readText()
        MockApiClient.onRequest = { request ->
            assertEquals(request.headers.get("Cache-Control"), "no-store")
            assertEquals(request.url.encodedPath, "/osc/state", "request path")
            ByteReadChannel(jsonString)
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        val thetaState = thetaRepository.getThetaState()

        // check
        assertTrue(thetaState.fingerprint?.isNotEmpty() ?: false, "state fingerprint")
        assertTrue((thetaState.batteryLevel ?: 0f) > 0, "state batteryLevel")
        assertTrue(thetaState.storageUri!!.startsWith("http://"), "state storageUri")
        assertNull(thetaState.storageID, "state storageUri")
        assertEquals(
            thetaState.captureStatus,
            ThetaRepository.CaptureStatusEnum.IDLE,
            "state captureStatus"
        )
        assertTrue((thetaState.recordedTime ?: -1) >= 0, "state recordedTime")
        assertTrue((thetaState.recordableTime ?: -1) >= 0, "state recordableTime")
        assertTrue(thetaState.capturedPictures!! >= 0, "state capturedPictures")
        assertTrue(thetaState.compositeShootingElapsedTime!! >= 0, "compositeShootingElapsedTime")
        assertTrue(thetaState.latestFileUrl?.startsWith("http://") ?: false, "state latestFileUrl")
        assertEquals(
            thetaState.chargingState,
            ThetaRepository.ChargingStateEnum.NOT_CHARGING,
            "state chargingState"
        )
        assertEquals(thetaState.apiVersion, 2, "state apiVersion")
        assertTrue(!thetaState.isPluginRunning!!, "state isPluginRunning")
        assertTrue(thetaState.isPluginWebServer!!, "state isPluginWebServer")
        assertEquals(
            thetaState.function,
            ThetaRepository.ShootingFunctionEnum.SELF_TIMER,
            "state function"
        )
        assertTrue(!thetaState.isMySettingChanged!!, "state isMySettingChanged")
        assertNull(thetaState.currentMicrophone, "state currentMicrophone")
        assertFalse(thetaState.isSdCard ?: true, "state isSdCard")
        assertEquals(
            thetaState.cameraError!![0],
            ThetaRepository.CameraErrorEnum.COMPASS_CALIBRATION,
            "state cameraError"
        )
        assertNull(thetaState.isBatteryInsert, "state isBatteryInsert")
    }

    /**
     * call getThetaState for THETA X.
     */
    @Test
    fun getThetaStateForXTest() = runTest {
        // setup
        val jsonString = Resource("src/commonTest/resources/state/state_x.json").readText()
        MockApiClient.onRequest = { request ->
            assertEquals(request.headers.get("Cache-Control"), "no-store")
            assertEquals(request.url.encodedPath, "/osc/state", "request path")
            ByteReadChannel(jsonString)
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        val thetaState = thetaRepository.getThetaState()

        // check
        assertTrue(thetaState.fingerprint?.isNotEmpty() ?: false, "state fingerprint")
        assertTrue((thetaState.batteryLevel ?: 0f) > 0, "state batteryLevel")
        assertTrue(thetaState.storageUri!!.startsWith("http://"), "state storageUri")
        assertTrue(thetaState.storageID!!.isNotEmpty(), "state storageUri")
        assertEquals(
            thetaState.captureStatus,
            ThetaRepository.CaptureStatusEnum.IDLE,
            "state captureStatus"
        )
        assertTrue((thetaState.recordedTime ?: -1) >= 0, "state recordedTime")
        assertTrue((thetaState.recordableTime ?: -1) >= 0, "state recordableTime")
        assertTrue(thetaState.capturedPictures!! >= 0, "state capturedPictures")
        assertNull(thetaState.compositeShootingElapsedTime, "compositeShootingElapsedTime")
        assertTrue(thetaState.latestFileUrl?.startsWith("http://") ?: false, "state latestFileUrl")
        assertEquals(
            thetaState.chargingState,
            ThetaRepository.ChargingStateEnum.NOT_CHARGING,
            "state chargingState"
        )
        assertEquals(thetaState.apiVersion, 2, "state apiVersion")
        assertTrue(!thetaState.isPluginRunning!!, "state isPluginRunning")
        assertTrue(thetaState.isPluginWebServer!!, "state isPluginWebServer")
        assertEquals(
            thetaState.function,
            ThetaRepository.ShootingFunctionEnum.NORMAL,
            "state function"
        )
        assertTrue(!thetaState.isMySettingChanged!!, "state isMySettingChanged")
        assertEquals(
            thetaState.currentMicrophone!!,
            ThetaRepository.MicrophoneOptionEnum.INTERNAL,
            "state currentMicrophone"
        )
        assertFalse(thetaState.isSdCard ?: true, "state isSdCard")
        assertEquals(
            thetaState.cameraError!![0],
            ThetaRepository.CameraErrorEnum.HIGH_TEMPERATURE_WARNING,
            "state cameraError"
        )
        assertTrue(thetaState.isBatteryInsert!!, "state isBatteryInsert")

        assertNull(thetaState.boardTemp, "state boardTemp")
        assertNull(thetaState.batteryTemp, "state batteryTemp")
    }

    /**
     * call getThetaState with gps for THETA X.
     */
    @Test
    fun getThetaStateGpsForXTest() = runTest {
        // setup
        val jsonString = Resource("src/commonTest/resources/state/state_x_gpsinfo.json").readText()
        MockApiClient.onRequest = { request ->
            assertEquals(request.url.encodedPath, "/osc/state", "request path")
            ByteReadChannel(jsonString)
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        val thetaState = thetaRepository.getThetaState()

        // check
        assertTrue(thetaState.fingerprint?.isNotEmpty() ?: false, "state fingerprint")
        assertTrue((thetaState.batteryLevel ?: 0f) > 0, "state batteryLevel")
        assertEquals(thetaState.storageUri?.startsWith("http://"), true, "state storageUri")
        assertEquals(thetaState.storageID?.isNotEmpty(), true, "state storageUri")
        assertEquals(
            thetaState.captureStatus,
            ThetaRepository.CaptureStatusEnum.IDLE,
            "state captureStatus"
        )
        assertTrue((thetaState.recordedTime ?: -1) >= 0, "state recordedTime")
        assertTrue((thetaState.recordableTime ?: -1) >= 0, "state recordableTime")
        assertTrue(thetaState.capturedPictures!! >= 0, "state capturedPictures")
        assertNull(thetaState.compositeShootingElapsedTime, "compositeShootingElapsedTime")
        assertTrue(thetaState.latestFileUrl?.startsWith("http://") ?: false, "state latestFileUrl")
        assertEquals(
            thetaState.chargingState,
            ThetaRepository.ChargingStateEnum.CHARGING,
            "state chargingState"
        )
        assertEquals(thetaState.apiVersion, 2, "state apiVersion")
        assertEquals(thetaState.isPluginRunning, false, "state isPluginRunning")
        assertEquals(thetaState.isPluginWebServer, false, "state isPluginWebServer")
        assertEquals(
            thetaState.function,
            ThetaRepository.ShootingFunctionEnum.NORMAL,
            "state function"
        )
        assertEquals(thetaState.isMySettingChanged, false, "state isMySettingChanged")
        assertEquals(
            thetaState.currentMicrophone,
            ThetaRepository.MicrophoneOptionEnum.INTERNAL,
            "state currentMicrophone"
        )
        assertFalse(thetaState.isSdCard ?: true, "state isSdCard")
        assertEquals(thetaState.cameraError?.size, 0, "state cameraError")
        assertEquals(thetaState.isBatteryInsert, true, "state isBatteryInsert")

        assertNotNull(thetaState.externalGpsInfo?.gpsInfo, "state externalGpsInfo")
        assertNotNull(
            thetaState.externalGpsInfo?.gpsInfo?.altitude,
            "state externalGpsInfo.altitude"
        )
        assertNotNull(
            thetaState.externalGpsInfo?.gpsInfo?.dateTimeZone,
            "state externalGpsInfo.dateTimeZone"
        )
        assertNotNull(
            thetaState.externalGpsInfo?.gpsInfo?.latitude,
            "state externalGpsInfo.latitude"
        )
        assertNotNull(
            thetaState.externalGpsInfo?.gpsInfo?.longitude,
            "state externalGpsInfo.longitude"
        )

        assertNotNull(thetaState.internalGpsInfo?.gpsInfo, "state internalGpsInfo")
        assertEquals(
            thetaState.internalGpsInfo?.gpsInfo,
            ThetaRepository.GpsInfo.disabled,
            "state internalGpsInfo disabled"
        )

        assertEquals(thetaState.boardTemp, 28, "state boardTemp")
        assertEquals(thetaState.batteryTemp, 30, "state batteryTemp")
    }

    /**
     * call getThetaState with gps on for THETA Z1.
     */
    @Test
    fun getThetaStateGpsOnForZ1Test() = runTest {
        // setup
        val jsonString = Resource("src/commonTest/resources/state/state_z1_gps_on.json").readText()
        MockApiClient.onRequest = { request ->
            assertEquals(request.url.encodedPath, "/osc/state", "request path")
            ByteReadChannel(jsonString)
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        val thetaState = thetaRepository.getThetaState()

        // check
        assertTrue(thetaState.fingerprint?.isNotEmpty() ?: false, "state fingerprint")
        assertTrue((thetaState.batteryLevel ?: 0f) > 0, "state batteryLevel")
        assertEquals(thetaState.storageUri?.startsWith("http://"), true, "state storageUri")
        assertNull(thetaState.storageID, "state storageUri")
        assertEquals(
            thetaState.captureStatus,
            ThetaRepository.CaptureStatusEnum.IDLE,
            "state captureStatus"
        )
        assertTrue((thetaState.recordedTime ?: -1) >= 0, "state recordedTime")
        assertTrue((thetaState.recordableTime ?: -1) >= 0, "state recordableTime")
        assertTrue((thetaState.capturedPictures ?: -1) >= 0, "state capturedPictures")
        assertTrue(
            (thetaState.compositeShootingElapsedTime ?: -1) >= 0,
            "compositeShootingElapsedTime"
        )
        assertTrue(thetaState.latestFileUrl?.startsWith("http://") ?: false, "state latestFileUrl")
        assertEquals(
            thetaState.chargingState,
            ThetaRepository.ChargingStateEnum.CHARGING,
            "state chargingState"
        )
        assertEquals(thetaState.apiVersion, 2, "state apiVersion")
        assertEquals(thetaState.isPluginRunning, false, "state isPluginRunning")
        assertEquals(thetaState.isPluginWebServer, true, "state isPluginWebServer")
        assertEquals(
            thetaState.function,
            ThetaRepository.ShootingFunctionEnum.NORMAL,
            "state function"
        )
        assertEquals(thetaState.isMySettingChanged, false, "state isMySettingChanged")
        assertNull(thetaState.currentMicrophone, "state currentMicrophone")
        assertFalse(thetaState.isSdCard ?: false, "state isSdCard")
        assertEquals(
            thetaState.cameraError?.get(0),
            ThetaRepository.CameraErrorEnum.COMPASS_CALIBRATION,
            "state cameraError"
        )
        assertNull(thetaState.isBatteryInsert, "state isBatteryInsert")

        assertNotNull(thetaState.externalGpsInfo?.gpsInfo, "state externalGpsInfo")
        assertNotNull(
            thetaState.externalGpsInfo?.gpsInfo?.altitude,
            "state externalGpsInfo.altitude"
        )
        assertNotNull(
            thetaState.externalGpsInfo?.gpsInfo?.dateTimeZone,
            "state externalGpsInfo.dateTimeZone"
        )
        assertNotNull(
            thetaState.externalGpsInfo?.gpsInfo?.latitude,
            "state externalGpsInfo.latitude"
        )
        assertNotNull(
            thetaState.externalGpsInfo?.gpsInfo?.longitude,
            "state externalGpsInfo.longitude"
        )

        assertNull(thetaState.internalGpsInfo?.gpsInfo, "state internalGpsInfo")
    }

    /**
     * call getThetaState with gps off for THETA Z1.
     */
    @Test
    fun getThetaStateGpsOffForZ1Test() = runTest {
        // setup
        val jsonString = Resource("src/commonTest/resources/state/state_z1_gps_off.json").readText()
        MockApiClient.onRequest = { request ->
            assertEquals(request.url.encodedPath, "/osc/state", "request path")
            ByteReadChannel(jsonString)
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        val thetaState = thetaRepository.getThetaState()

        // check
        assertTrue(thetaState.fingerprint?.isNotEmpty() ?: false, "state fingerprint")
        assertTrue((thetaState.batteryLevel ?: 0f) > 0, "state batteryLevel")
        assertEquals(thetaState.storageUri?.startsWith("http://"), true, "state storageUri")
        assertNull(thetaState.storageID, "state storageUri")
        assertEquals(
            thetaState.captureStatus,
            ThetaRepository.CaptureStatusEnum.IDLE,
            "state captureStatus"
        )
        assertTrue((thetaState.recordedTime ?: -1) >= 0, "state recordedTime")
        assertTrue((thetaState.recordableTime ?: -1) >= 0, "state recordableTime")
        assertTrue((thetaState.capturedPictures ?: -1) >= 0, "state capturedPictures")
        assertTrue(
            (thetaState.compositeShootingElapsedTime ?: -1) >= 0,
            "compositeShootingElapsedTime"
        )
        assertEquals(thetaState.latestFileUrl, "", "state latestFileUrl")
        assertEquals(
            thetaState.chargingState,
            ThetaRepository.ChargingStateEnum.CHARGING,
            "state chargingState"
        )
        assertEquals(thetaState.apiVersion, 2, "state apiVersion")
        assertEquals(thetaState.isPluginRunning, false, "state isPluginRunning")
        assertEquals(thetaState.isPluginWebServer, true, "state isPluginWebServer")
        assertEquals(
            thetaState.function,
            ThetaRepository.ShootingFunctionEnum.NORMAL,
            "state function"
        )
        assertEquals(thetaState.isMySettingChanged, false, "state isMySettingChanged")
        assertNull(thetaState.currentMicrophone, "state currentMicrophone")
        assertFalse(thetaState.isSdCard ?: true, "state isSdCard")
        assertEquals(
            thetaState.cameraError?.get(0),
            ThetaRepository.CameraErrorEnum.COMPASS_CALIBRATION,
            "state cameraError"
        )
        assertNull(thetaState.isBatteryInsert, "state isBatteryInsert")

        assertNull(thetaState.externalGpsInfo?.gpsInfo, "state externalGpsInfo")
        assertNull(thetaState.internalGpsInfo?.gpsInfo, "state internalGpsInfo")
    }

    /**
     * Camera error of ELECTRONIC_COMPASS_CALIBRATION for THETA X.
     */
    @Test
    fun electronicCompassCalibrationErrorTest() = runTest {
        // setup
        val jsonString =
            Resource("src/commonTest/resources/state/state_x_electronic_compass_calibration.json").readText()
        MockApiClient.onRequest = { request ->
            assertEquals(request.url.encodedPath, "/osc/state", "request path")
            ByteReadChannel(jsonString)
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        val thetaState = thetaRepository.getThetaState()

        // check
        assertTrue(thetaState.fingerprint?.isNotEmpty() ?: false, "state fingerprint")
        assertTrue((thetaState.batteryLevel ?: 0f) > 0, "state batteryLevel")
        assertTrue(thetaState.storageUri!!.startsWith("http://"), "state storageUri")
        assertTrue(thetaState.storageID!!.isNotEmpty(), "state storageUri")
        assertEquals(
            thetaState.captureStatus,
            ThetaRepository.CaptureStatusEnum.IDLE,
            "state captureStatus"
        )
        assertTrue((thetaState.recordedTime ?: -1) >= 0, "state recordedTime")
        assertTrue((thetaState.recordableTime ?: -1) >= 0, "state recordableTime")
        assertTrue(thetaState.capturedPictures!! >= 0, "state capturedPictures")
        assertNull(thetaState.compositeShootingElapsedTime, "compositeShootingElapsedTime")
        assertTrue(thetaState.latestFileUrl?.startsWith("http://") ?: false, "state latestFileUrl")
        assertEquals(
            thetaState.chargingState,
            ThetaRepository.ChargingStateEnum.NOT_CHARGING,
            "state chargingState"
        )
        assertEquals(thetaState.apiVersion, 2, "state apiVersion")
        assertTrue(!thetaState.isPluginRunning!!, "state isPluginRunning")
        assertTrue(thetaState.isPluginWebServer!!, "state isPluginWebServer")
        assertEquals(
            thetaState.function,
            ThetaRepository.ShootingFunctionEnum.NORMAL,
            "state function"
        )
        assertTrue(!thetaState.isMySettingChanged!!, "state isMySettingChanged")
        assertEquals(
            thetaState.currentMicrophone!!,
            ThetaRepository.MicrophoneOptionEnum.INTERNAL,
            "state currentMicrophone"
        )
        assertFalse(thetaState.isSdCard ?: true, "state isSdCard")
        assertEquals(
            thetaState.cameraError!![0],
            ThetaRepository.CameraErrorEnum.COMPASS_CALIBRATION,
            "state cameraError"
        )
        assertTrue(thetaState.isBatteryInsert!!, "state isBatteryInsert")
    }

    /**
     * Camera error of ELECTRONIC_COMPASS_CALIBRATION for THETA X.
     */
    @Test
    fun unknownErrorTest() = runTest {
        // setup
        val jsonString =
            Resource("src/commonTest/resources/state/state_x_unknown_camera_error.json").readText()
        MockApiClient.onRequest = { request ->
            assertEquals(request.url.encodedPath, "/osc/state", "request path")
            ByteReadChannel(jsonString)
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        val thetaState = thetaRepository.getThetaState()

        // check
        assertTrue(thetaState.fingerprint?.isNotEmpty() ?: false, "state fingerprint")
        assertTrue((thetaState.batteryLevel ?: 0f) > 0, "state batteryLevel")
        assertTrue(thetaState.storageUri!!.startsWith("http://"), "state storageUri")
        assertTrue(thetaState.storageID!!.isNotEmpty(), "state storageUri")
        assertEquals(
            thetaState.captureStatus,
            ThetaRepository.CaptureStatusEnum.IDLE,
            "state captureStatus"
        )
        assertTrue((thetaState.recordedTime ?: -1) >= 0, "state recordedTime")
        assertTrue((thetaState.recordableTime ?: -1) >= 0, "state recordableTime")
        assertTrue(thetaState.capturedPictures!! >= 0, "state capturedPictures")
        assertNull(thetaState.compositeShootingElapsedTime, "compositeShootingElapsedTime")
        assertTrue(thetaState.latestFileUrl?.startsWith("http://") ?: false, "state latestFileUrl")
        assertEquals(
            thetaState.chargingState,
            ThetaRepository.ChargingStateEnum.NOT_CHARGING,
            "state chargingState"
        )
        assertEquals(thetaState.apiVersion, 2, "state apiVersion")
        assertTrue(!thetaState.isPluginRunning!!, "state isPluginRunning")
        assertTrue(thetaState.isPluginWebServer!!, "state isPluginWebServer")
        assertEquals(
            thetaState.function,
            ThetaRepository.ShootingFunctionEnum.NORMAL,
            "state function"
        )
        assertTrue(!thetaState.isMySettingChanged!!, "state isMySettingChanged")
        assertEquals(
            thetaState.currentMicrophone!!,
            ThetaRepository.MicrophoneOptionEnum.INTERNAL,
            "state currentMicrophone"
        )
        assertFalse(thetaState.isSdCard ?: true, "state isSdCard")
        assertEquals(
            thetaState.cameraError!![0],
            ThetaRepository.CameraErrorEnum.UNKNOWN,
            "state cameraError"
        )
        assertTrue(thetaState.isBatteryInsert!!, "state isBatteryInsert")
    }

    /**
     * Check setting of ThetaState.
     */
    @Test
    fun settingThetaStateTest() = runTest {
        val fingerprint = "fingerprint test"

        // Check isSdCard
        val sdOptionList = listOf(StorageOption.SD, StorageOption.IN, null)
        val sdResponseList = mutableListOf<StateApiResponse>()
        sdOptionList.forEach {
            sdResponseList.add(
                StateApiResponse(
                    fingerprint,
                    CameraState(
                        batteryLevel = 0.0,
                        _batteryState = ChargingState.CHARGED,
                        storageUri = "http://dummy",
                        _captureStatus = CaptureStatus.IDLE,
                        _recordedTime = 0,
                        _recordableTime = 0,
                        _apiVersion = 2,
                        _currentStorage = it,
                        _latestFileUrl = "http://192.168.1.1:80/dummy.JPG"
                    )
                )
            )
        }
        sdResponseList.forEachIndexed { index, response ->
            val thetaState = ThetaRepository.ThetaState(response)
            assertEquals(
                thetaState.isSdCard,
                sdOptionList[index] == StorageOption.SD,
                "ThetaState isSdCard"
            )
        }

        // check CaptureStatusEnum
        assertEquals(
            ThetaRepository.CaptureStatusEnum.get(CaptureStatus.UNKNOWN),
            ThetaRepository.CaptureStatusEnum.UNKNOWN,
            "CaptureStatusEnum"
        )
        assertEquals(
            ThetaRepository.CaptureStatusEnum.get(CaptureStatus.SHOOTING),
            ThetaRepository.CaptureStatusEnum.SHOOTING,
            "CaptureStatusEnum"
        )
        assertEquals(
            ThetaRepository.CaptureStatusEnum.get(CaptureStatus.IDLE),
            ThetaRepository.CaptureStatusEnum.IDLE,
            "CaptureStatusEnum"
        )
        assertEquals(
            ThetaRepository.CaptureStatusEnum.get(CaptureStatus.SELF_TIMER_COUNTDOWN),
            ThetaRepository.CaptureStatusEnum.SELF_TIMER_COUNTDOWN,
            "CaptureStatusEnum"
        )
        assertEquals(
            ThetaRepository.CaptureStatusEnum.get(CaptureStatus.BRACKET_SHOOTING),
            ThetaRepository.CaptureStatusEnum.BRACKET_SHOOTING,
            "CaptureStatusEnum"
        )
        assertEquals(
            ThetaRepository.CaptureStatusEnum.get(CaptureStatus.CONVERTING),
            ThetaRepository.CaptureStatusEnum.CONVERTING,
            "CaptureStatusEnum"
        )
        assertEquals(
            ThetaRepository.CaptureStatusEnum.get(CaptureStatus.TIME_SHIFT_SHOOTING),
            ThetaRepository.CaptureStatusEnum.TIME_SHIFT_SHOOTING,
            "CaptureStatusEnum"
        )
        assertEquals(
            ThetaRepository.CaptureStatusEnum.get(CaptureStatus.CONTINUOUS_SHOOTING),
            ThetaRepository.CaptureStatusEnum.CONTINUOUS_SHOOTING,
            "CaptureStatusEnum"
        )
        assertEquals(
            ThetaRepository.CaptureStatusEnum.get(CaptureStatus.RETROSPECTIVE_IMAGE_RECORDING),
            ThetaRepository.CaptureStatusEnum.RETROSPECTIVE_IMAGE_RECORDING,
            "CaptureStatusEnum"
        )
        assertEquals(
            ThetaRepository.CaptureStatusEnum.get(CaptureStatus.BURST_SHOOTING),
            ThetaRepository.CaptureStatusEnum.BURST_SHOOTING,
            "CaptureStatusEnum"
        )
        assertEquals(
            ThetaRepository.CaptureStatusEnum.get(CaptureStatus.TIME_SHIFT_SHOOTING_IDLE),
            ThetaRepository.CaptureStatusEnum.TIME_SHIFT_SHOOTING_IDLE,
            "CaptureStatusEnum"
        )

        // check ChargingStateEnum
        assertEquals(
            ThetaRepository.ChargingStateEnum.get(ChargingState.CHARGING),
            ThetaRepository.ChargingStateEnum.CHARGING,
            "ChargingStateEnum"
        )
        assertEquals(
            ThetaRepository.ChargingStateEnum.get(ChargingState.CHARGED),
            ThetaRepository.ChargingStateEnum.COMPLETED,
            "ChargingStateEnum"
        )
        assertEquals(
            ThetaRepository.ChargingStateEnum.get(ChargingState.DISCONNECT),
            ThetaRepository.ChargingStateEnum.NOT_CHARGING,
            "ChargingStateEnum"
        )

        // check ShootingFunctionEnum
        assertEquals(
            ThetaRepository.ShootingFunctionEnum.get(ShootingFunction.NORMAL),
            ThetaRepository.ShootingFunctionEnum.NORMAL,
            "ShootingFunctionEnum"
        )
        assertEquals(
            ThetaRepository.ShootingFunctionEnum.get(ShootingFunction.SELF_TIMER),
            ThetaRepository.ShootingFunctionEnum.SELF_TIMER,
            "ShootingFunctionEnum"
        )
        assertEquals(
            ThetaRepository.ShootingFunctionEnum.get(ShootingFunction.MY_SETTING),
            ThetaRepository.ShootingFunctionEnum.MY_SETTING,
            "ShootingFunctionEnum"
        )

        // check MicrophoneOptionEnum
        assertEquals(
            ThetaRepository.MicrophoneOptionEnum.get(MicrophoneOption.AUTO),
            ThetaRepository.MicrophoneOptionEnum.AUTO,
            "MicrophoneOptionEnum"
        )
        assertEquals(
            ThetaRepository.MicrophoneOptionEnum.get(MicrophoneOption.INTERNAL),
            ThetaRepository.MicrophoneOptionEnum.INTERNAL,
            "MicrophoneOptionEnum"
        )
        assertEquals(
            ThetaRepository.MicrophoneOptionEnum.get(MicrophoneOption.EXTERNAL),
            ThetaRepository.MicrophoneOptionEnum.EXTERNAL,
            "MicrophoneOptionEnum"
        )

        // check CameraErrorEnum
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.NO_MEMORY),
            ThetaRepository.CameraErrorEnum.NO_MEMORY,
            "CameraErrorEnum"
        )
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.FILE_NUMBER_OVER),
            ThetaRepository.CameraErrorEnum.FILE_NUMBER_OVER,
            "CameraErrorEnum"
        )
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.NO_DATE_SETTING),
            ThetaRepository.CameraErrorEnum.NO_DATE_SETTING,
            "CameraErrorEnum"
        )
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.READ_ERROR),
            ThetaRepository.CameraErrorEnum.READ_ERROR,
            "CameraErrorEnum"
        )
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.NOT_SUPPORTED_MEDIA_TYPE),
            ThetaRepository.CameraErrorEnum.NOT_SUPPORTED_MEDIA_TYPE,
            "CameraErrorEnum"
        )
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.NOT_SUPPORTED_FILE_SYSTEM),
            ThetaRepository.CameraErrorEnum.NOT_SUPPORTED_FILE_SYSTEM,
            "CameraErrorEnum"
        )
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.MEDIA_NOT_READY),
            ThetaRepository.CameraErrorEnum.MEDIA_NOT_READY,
            "CameraErrorEnum"
        )
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.NOT_ENOUGH_BATTERY),
            ThetaRepository.CameraErrorEnum.NOT_ENOUGH_BATTERY,
            "CameraErrorEnum"
        )
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.INVALID_FILE),
            ThetaRepository.CameraErrorEnum.INVALID_FILE,
            "CameraErrorEnum"
        )
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.PLUGIN_BOOT_ERROR),
            ThetaRepository.CameraErrorEnum.PLUGIN_BOOT_ERROR,
            "CameraErrorEnum"
        )
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.IN_PROGRESS_ERROR),
            ThetaRepository.CameraErrorEnum.IN_PROGRESS_ERROR,
            "CameraErrorEnum"
        )
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.CANNOT_RECORDING),
            ThetaRepository.CameraErrorEnum.CANNOT_RECORDING,
            "CameraErrorEnum"
        )
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.CANNOT_RECORD_LOWBAT),
            ThetaRepository.CameraErrorEnum.CANNOT_RECORD_LOWBAT,
            "CameraErrorEnum"
        )
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.CAPTURE_HW_FAILED),
            ThetaRepository.CameraErrorEnum.CAPTURE_HW_FAILED,
            "CameraErrorEnum"
        )
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.CAPTURE_SW_FAILED),
            ThetaRepository.CameraErrorEnum.CAPTURE_SW_FAILED,
            "CameraErrorEnum"
        )
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.INTERNAL_MEM_ACCESS_FAIL),
            ThetaRepository.CameraErrorEnum.INTERNAL_MEM_ACCESS_FAIL,
            "CameraErrorEnum"
        )

        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.UNEXPECTED_ERROR),
            ThetaRepository.CameraErrorEnum.UNEXPECTED_ERROR,
            "CameraErrorEnum"
        )
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.BATTERY_CHARGE_FAIL),
            ThetaRepository.CameraErrorEnum.BATTERY_CHARGE_FAIL,
            "CameraErrorEnum"
        )
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.HIGH_TEMPERATURE_WARNING),
            ThetaRepository.CameraErrorEnum.HIGH_TEMPERATURE_WARNING,
            "CameraErrorEnum"
        )
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.HIGH_TEMPERATURE),
            ThetaRepository.CameraErrorEnum.HIGH_TEMPERATURE,
            "CameraErrorEnum"
        )
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.BATTERY_HIGH_TEMPERATURE),
            ThetaRepository.CameraErrorEnum.BATTERY_HIGH_TEMPERATURE,
            "CameraErrorEnum"
        )
        assertEquals(
            ThetaRepository.CameraErrorEnum.get(CameraError.COMPASS_CALIBRATION),
            ThetaRepository.CameraErrorEnum.COMPASS_CALIBRATION,
            "CameraErrorEnum"
        )

    }

    /**
     * Error not json response to getThetaState call
     */
    @Test
    fun getThetaStateNotJsonResponseTest() = runTest {
        // setup
        val jsonString = "Not json"
        MockApiClient.onRequest = { _ ->
            ByteReadChannel(jsonString)
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.getThetaState()
            assertTrue(false, "call getThetaState")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(
                e.message!!.indexOf("json", 0, true) >= 0 ||
                        e.message!!.indexOf("Illegal", 0, true) >= 0,
                "error response"
            )
        }
    }

    /**
     * Status error to getThetaState call
     */
    @Test
    fun getThetaStateStatusErrorTest() = runTest {
        // setup
        MockApiClient.onRequest = { _ ->
            MockApiClient.status = HttpStatusCode.ServiceUnavailable
            ByteReadChannel("status error")
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.getThetaState()
            assertTrue(false, "status error")
        } catch (e: ThetaRepository.ThetaWebApiException) {
            assertTrue(e.message!!.indexOf("503", 0, true) >= 0, "status error")
        }
    }

    /**
     * Error exception to getThetaState call
     */
    @Test
    fun getThetaStateExceptionTest() = runTest {
        // setup
        MockApiClient.onRequest = { _ ->
            throw ConnectTimeoutException("timeout")
        }

        // test
        val thetaRepository = ThetaRepository(endpoint)
        try {
            thetaRepository.getThetaState()
            assertTrue(false, "status error")
        } catch (e: ThetaRepository.NotConnectedException) {
            assertTrue(e.message!!.indexOf("time", 0, true) >= 0, "timeout exception")
        }
    }
}
