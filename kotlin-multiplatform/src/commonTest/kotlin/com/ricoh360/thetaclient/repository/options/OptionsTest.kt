package com.ricoh360.thetaclient.repository.options

import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.AccessInfo
import com.ricoh360.thetaclient.transferred.AiAutoThumbnail
import com.ricoh360.thetaclient.transferred.AutoBracket
import com.ricoh360.thetaclient.transferred.BluetoothPower
import com.ricoh360.thetaclient.transferred.BluetoothRole
import com.ricoh360.thetaclient.transferred.BracketParameter
import com.ricoh360.thetaclient.transferred.BurstBracketStep
import com.ricoh360.thetaclient.transferred.BurstCaptureNum
import com.ricoh360.thetaclient.transferred.BurstCompensation
import com.ricoh360.thetaclient.transferred.BurstEnableIsoControl
import com.ricoh360.thetaclient.transferred.BurstMaxExposureTime
import com.ricoh360.thetaclient.transferred.BurstMode
import com.ricoh360.thetaclient.transferred.BurstOption
import com.ricoh360.thetaclient.transferred.BurstOrder
import com.ricoh360.thetaclient.transferred.CameraControlSource
import com.ricoh360.thetaclient.transferred.CameraLock
import com.ricoh360.thetaclient.transferred.CameraLockConfig
import com.ricoh360.thetaclient.transferred.CameraLockType
import com.ricoh360.thetaclient.transferred.CameraMode
import com.ricoh360.thetaclient.transferred.CameraPower
import com.ricoh360.thetaclient.transferred.CaptureMode
import com.ricoh360.thetaclient.transferred.ColorTemperatureSupport
import com.ricoh360.thetaclient.transferred.CompassDirectionRef
import com.ricoh360.thetaclient.transferred.CompositeShootingOutputIntervalSupport
import com.ricoh360.thetaclient.transferred.CompositeShootingTimeSupport
import com.ricoh360.thetaclient.transferred.DhcpLeaseAddress
import com.ricoh360.thetaclient.transferred.EthernetConfig
import com.ricoh360.thetaclient.transferred.FaceDetect
import com.ricoh360.thetaclient.transferred.FirstShootingEnum
import com.ricoh360.thetaclient.transferred.Gain
import com.ricoh360.thetaclient.transferred.GpsInfo
import com.ricoh360.thetaclient.transferred.GpsTagRecording
import com.ricoh360.thetaclient.transferred.ImageFilter
import com.ricoh360.thetaclient.transferred.ImageStitching
import com.ricoh360.thetaclient.transferred.IpAddressAllocation
import com.ricoh360.thetaclient.transferred.Language
import com.ricoh360.thetaclient.transferred.MediaFileFormat
import com.ricoh360.thetaclient.transferred.MediaType
import com.ricoh360.thetaclient.transferred.MicrophoneNoiseReduction
import com.ricoh360.thetaclient.transferred.MobileNetworkSetting
import com.ricoh360.thetaclient.transferred.NetworkType
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.PitchSupport
import com.ricoh360.thetaclient.transferred.Plan
import com.ricoh360.thetaclient.transferred.PowerSaving
import com.ricoh360.thetaclient.transferred.Preset
import com.ricoh360.thetaclient.transferred.PreviewFormat
import com.ricoh360.thetaclient.transferred.Proxy
import com.ricoh360.thetaclient.transferred.Roaming
import com.ricoh360.thetaclient.transferred.RollSupport
import com.ricoh360.thetaclient.transferred.ShootingFunction
import com.ricoh360.thetaclient.transferred.ShootingMethod
import com.ricoh360.thetaclient.transferred.TimeShift
import com.ricoh360.thetaclient.transferred.TopBottomCorrectionOption
import com.ricoh360.thetaclient.transferred.TopBottomCorrectionRotation
import com.ricoh360.thetaclient.transferred.TopBottomCorrectionRotationSupport
import com.ricoh360.thetaclient.transferred.UsbConnection
import com.ricoh360.thetaclient.transferred.VideoStitching
import com.ricoh360.thetaclient.transferred.VisibilityReduction
import com.ricoh360.thetaclient.transferred.WhiteBalance
import com.ricoh360.thetaclient.transferred.WhiteBalanceAutoStrength
import com.ricoh360.thetaclient.transferred.WlanAntennaConfig
import com.ricoh360.thetaclient.transferred.WlanFrequency
import com.ricoh360.thetaclient.transferred.WlanFrequencyAccessInfo
import com.ricoh360.thetaclient.transferred.WlanFrequencyClMode
import com.ricoh360.thetaclient.transferred.YawSupport
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class OptionsTest {
    @BeforeTest
    fun setup() {
    }

    @AfterTest
    fun teardown() {
    }

    /**
     * Options primary constructor.
     */
    @Test
    fun optionsPrimaryConstructorTest() {
        val accessInfo = ThetaRepository.AccessInfo(
            ssid = "ssid_test",
            ipAddress = "192.168.1.2",
            subnetMask = "255.255.0.0",
            defaultGateway = "192.168.1.12",
            dns1 = "192.168.1.55",
            dns2 = "192.168.1.66",
            proxyURL = "http://192.168.1.3",
            frequency = ThetaRepository.WlanFrequencyAccessInfoEnum.GHZ_2_4,
            wlanSignalStrength = -60,
            wlanSignalLevel = 4,
            lteSignalStrength = 0,
            lteSignalLevel = 0,
            dhcpLeaseAddress = listOf(
                ThetaRepository.DhcpLeaseAddress(ipAddress = "192.168.1.5", macAddress = "192.168.1.6", hostName = "192.168.1.7"),
                ThetaRepository.DhcpLeaseAddress(ipAddress = "192.168.1.8", macAddress = "192.168.1.9", hostName = "192.168.1.10")
            )
        )
        val aiAutoThumbnail = ThetaRepository.AiAutoThumbnailEnum.ON
        val aiAutoThumbnailSupport = listOf(ThetaRepository.AiAutoThumbnailEnum.ON, ThetaRepository.AiAutoThumbnailEnum.OFF)
        val aperture = ThetaRepository.ApertureEnum.APERTURE_2_1
        val apertureSupport = listOf(ThetaRepository.ApertureEnum.APERTURE_2_1, ThetaRepository.ApertureEnum.APERTURE_3_5)
        val autoBracket = ThetaRepository.BracketSettingList().add(
            ThetaRepository.BracketSetting(
                exposureProgram = ThetaRepository.ExposureProgramEnum.NORMAL_PROGRAM,
                whiteBalance = ThetaRepository.WhiteBalanceEnum.DAYLIGHT,
            )
        ).add(
            ThetaRepository.BracketSetting(
                aperture = ThetaRepository.ApertureEnum.APERTURE_2_0,
                colorTemperature = 6800,
                exposureProgram = ThetaRepository.ExposureProgramEnum.MANUAL,
                iso = ThetaRepository.IsoEnum.ISO_800,
                shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_100,
                whiteBalance = ThetaRepository.WhiteBalanceEnum.CLOUDY_DAYLIGHT,
            )
        )
        val bitrate = ThetaRepository.BitrateEnum.FINE
        val bluetoothPower = ThetaRepository.BluetoothPowerEnum.ON
        val bluetoothRole = ThetaRepository.BluetoothRoleEnum.CENTRAL
        val burstMode = ThetaRepository.BurstModeEnum.ON
        val burstOption = ThetaRepository.BurstOption(
            burstCaptureNum = ThetaRepository.BurstCaptureNumEnum.BURST_CAPTURE_NUM_1,
            burstBracketStep = ThetaRepository.BurstBracketStepEnum.BRACKET_STEP_0_0,
            burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_0_0,
            burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_15,
            burstEnableIsoControl = ThetaRepository.BurstEnableIsoControlEnum.OFF,
            burstOrder = ThetaRepository.BurstOrderEnum.BURST_BRACKET_ORDER_0
        )
        val cameraControlSource = ThetaRepository.CameraControlSourceEnum.CAMERA
        val cameraControlSourceSupport = listOf(ThetaRepository.CameraControlSourceEnum.CAMERA, ThetaRepository.CameraControlSourceEnum.APP)
        val cameraLock = ThetaRepository.CameraLockEnum.UNLOCK
        val cameraLockConfig = ThetaRepository.CameraLockConfig(
            isPowerKeyLocked = true,
            isShutterKeyLocked = false,
            isModeKeyLocked = false,
            isWlanKeyLocked = false,
            isFnKeyLocked = true,
            isPanelLocked = false
        )
        val cameraMode = ThetaRepository.CameraModeEnum.CAPTURE
        val cameraPower = ThetaRepository.CameraPowerEnum.ON
        val cameraPowerSupport = listOf(
            ThetaRepository.CameraPowerEnum.ON,
            ThetaRepository.CameraPowerEnum.OFF,
            ThetaRepository.CameraPowerEnum.POWER_SAVING,
            ThetaRepository.CameraPowerEnum.SILENT_MODE
        )
        val captureInterval = 6
        val captureMode = ThetaRepository.CaptureModeEnum.IMAGE
        val captureNumber = 0
        val colorTemperature = 10
        val colorTemperatureSupport = ThetaRepository.ValueRange(10000, 2000, 100)
        val compassDirectionRef = ThetaRepository.CompassDirectionRefEnum.AUTO
        val compositeShootingOutputInterval = 60
        val compositeShootingOutputIntervalSupport = ThetaRepository.ValueRange(600, 0, 60)
        val compositeShootingTime = 600
        val compositeShootingTimeSupport = ThetaRepository.ValueRange(86400, 600, 600)
        val continuousNumber = ThetaRepository.ContinuousNumberEnum.MAX_1
        val dateTimeZone = "2014:05:18 01:04:29+08:00"
        val ethernetConfig = ThetaRepository.EthernetConfig(
            usingDhcp = true,
            ipAddress = "192.168.1.2",
            subnetMask = "255.255.0.0",
            defaultGateway = "192.168.1.12",
            dns1 = "192.168.1.55",
            dns2 = "192.168.1.66",
            proxy = ThetaRepository.Proxy(use = true, url = "192.168.1.3", port = 80, userid = "abc", password = "123")
        )
        val exposureCompensation = ThetaRepository.ExposureCompensationEnum.M0_3
        val exposureDelay = ThetaRepository.ExposureDelayEnum.DELAY_10
        val exposureDelaySupport = listOf(ThetaRepository.ExposureDelayEnum.DELAY_OFF, ThetaRepository.ExposureDelayEnum.DELAY_10)
        val exposureProgram = ThetaRepository.ExposureProgramEnum.NORMAL_PROGRAM
        val faceDetect = ThetaRepository.FaceDetectEnum.ON
        val fileFormat = ThetaRepository.FileFormatEnum.IMAGE_11K
        val filter = ThetaRepository.FilterEnum.HDR
        val function = ThetaRepository.ShootingFunctionEnum.NORMAL
        val gain = ThetaRepository.GainEnum.NORMAL
        val gpsInfo = ThetaRepository.GpsInfo.disabled
        val gpsTagRecordingSupport = listOf(ThetaRepository.GpsTagRecordingEnum.ON, ThetaRepository.GpsTagRecordingEnum.OFF)
        val imageStitching = ThetaRepository.ImageStitchingEnum.AUTO
        val isGpsOn = true
        val iso = ThetaRepository.IsoEnum.ISO_125
        val isoAutoHighLimit = ThetaRepository.IsoAutoHighLimitEnum.ISO_1000
        val language = ThetaRepository.LanguageEnum.JA
        val latestEnabledExposureDelayTime = ThetaRepository.ExposureDelayEnum.DELAY_10
        val maxRecordableTime = ThetaRepository.MaxRecordableTimeEnum.RECORDABLE_TIME_1500
        val microphoneNoiseReduction = ThetaRepository.MicrophoneNoiseReductionEnum.ON
        val mobileNetworkSetting = ThetaRepository.MobileNetworkSetting(roaming = ThetaRepository.RoamingEnum.OFF, plan = ThetaRepository.PlanEnum.SORACOM)
        val networkType = ThetaRepository.NetworkTypeEnum.DIRECT
        val offDelay = ThetaRepository.OffDelayEnum.OFF_DELAY_10M
        val offDelayUsb = ThetaRepository.OffDelayUsbEnum.OFF_DELAY_10M
        val password = "password"
        val powerSaving = ThetaRepository.PowerSavingEnum.ON
        val preset = ThetaRepository.PresetEnum.FACE
        val previewFormat = ThetaRepository.PreviewFormatEnum.W1024_H512_F30
        val proxy = ThetaRepository.Proxy(use = false, url = "", port = 8080)
        val remainingPictures = 100
        val remainingVideoSeconds = 100
        val remainingSpace = 100L
        val shootingMethod = ThetaRepository.ShootingMethodEnum.NORMAL
        val shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_10
        val shutterVolume = 100
        val sleepDelay = ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M
        val timeShift = ThetaRepository.TimeShiftSetting(
            true,
            ThetaRepository.TimeShiftIntervalEnum.INTERVAL_0,
            ThetaRepository.TimeShiftIntervalEnum.INTERVAL_1
        )
        val topBottomCorrection = ThetaRepository.TopBottomCorrectionOptionEnum.APPLY_AUTO
        val topBottomCorrectionRotation = ThetaRepository.TopBottomCorrectionRotation(1.0f, 2.0f, 3.0f)
        val topBottomCorrectionRotationSupport = ThetaRepository.TopBottomCorrectionRotationSupport(
            pitch = ThetaRepository.ValueRange(100f, -100f, 0.2f),
            roll = ThetaRepository.ValueRange(200f, -200f, 0.4f),
            yaw = ThetaRepository.ValueRange(300f, -300f, 0.6f)
        )
        val totalSpace = 100L
        val usbConnection = ThetaRepository.UsbConnectionEnum.MSC
        val username = "username"
        val videoStitching = ThetaRepository.VideoStitchingEnum.ONDEVICE
        val visibilityReduction = ThetaRepository.VisibilityReductionEnum.ON
        val whiteBalance = ThetaRepository.WhiteBalanceEnum.WARM_WHITE_FLUORESCENT
        val whiteBalanceAutoStrength = ThetaRepository.WhiteBalanceAutoStrengthEnum.OFF
        val wlanAntennaConfig = ThetaRepository.WlanAntennaConfigEnum.MIMO
        val wlanFrequency = ThetaRepository.WlanFrequencyEnum.GHZ_2_4
        val wlanFrequencySupport = listOf(ThetaRepository.WlanFrequencyEnum.GHZ_2_4, ThetaRepository.WlanFrequencyEnum.GHZ_5)
        val wlanFrequencyClMode = ThetaRepository.WlanFrequencyClMode(
            enable2_4 = true,
            enable5_2 = false,
            enable5_8 = false,
        )

        val options = ThetaRepository.Options(
            accessInfo = accessInfo,
            aiAutoThumbnail = aiAutoThumbnail,
            aiAutoThumbnailSupport = aiAutoThumbnailSupport,
            aperture = aperture,
            apertureSupport = apertureSupport,
            autoBracket = autoBracket,
            bitrate = bitrate,
            bluetoothPower = bluetoothPower,
            bluetoothRole = bluetoothRole,
            burstMode = burstMode,
            burstOption = burstOption,
            cameraControlSource = cameraControlSource,
            cameraControlSourceSupport = cameraControlSourceSupport,
            cameraPowerSupport = cameraPowerSupport,
            cameraLock = cameraLock,
            cameraLockConfig = cameraLockConfig,
            cameraMode = cameraMode,
            cameraPower = cameraPower,
            captureInterval = captureInterval,
            captureMode = captureMode,
            captureNumber = captureNumber,
            colorTemperature = colorTemperature,
            colorTemperatureSupport = colorTemperatureSupport,
            compassDirectionRef = compassDirectionRef,
            compositeShootingOutputInterval = compositeShootingOutputInterval,
            compositeShootingOutputIntervalSupport = compositeShootingOutputIntervalSupport,
            compositeShootingTime = compositeShootingTime,
            compositeShootingTimeSupport = compositeShootingTimeSupport,
            continuousNumber = continuousNumber,
            dateTimeZone = dateTimeZone,
            ethernetConfig = ethernetConfig,
            exposureCompensation = exposureCompensation,
            exposureDelay = exposureDelay,
            exposureDelaySupport = exposureDelaySupport,
            exposureProgram = exposureProgram,
            faceDetect = faceDetect,
            fileFormat = fileFormat,
            filter = filter,
            function = function,
            gain = gain,
            gpsInfo = gpsInfo,
            gpsTagRecordingSupport = gpsTagRecordingSupport,
            imageStitching = imageStitching,
            isGpsOn = isGpsOn,
            iso = iso,
            isoAutoHighLimit = isoAutoHighLimit,
            language = language,
            latestEnabledExposureDelayTime = latestEnabledExposureDelayTime,
            maxRecordableTime = maxRecordableTime,
            microphoneNoiseReduction = microphoneNoiseReduction,
            mobileNetworkSetting = mobileNetworkSetting,
            networkType = networkType,
            offDelay = offDelay,
            offDelayUsb = offDelayUsb,
            password = password,
            powerSaving = powerSaving,
            preset = preset,
            previewFormat = previewFormat,
            proxy = proxy,
            remainingPictures = remainingPictures,
            remainingVideoSeconds = remainingVideoSeconds,
            remainingSpace = remainingSpace,
            shootingMethod = shootingMethod,
            shutterSpeed = shutterSpeed,
            shutterVolume = shutterVolume,
            sleepDelay = sleepDelay,
            timeShift = timeShift,
            topBottomCorrection = topBottomCorrection,
            topBottomCorrectionRotation = topBottomCorrectionRotation,
            topBottomCorrectionRotationSupport = topBottomCorrectionRotationSupport,
            totalSpace = totalSpace,
            usbConnection = usbConnection,
            username = username,
            videoStitching = videoStitching,
            visibilityReduction = visibilityReduction,
            whiteBalance = whiteBalance,
            whiteBalanceAutoStrength = whiteBalanceAutoStrength,
            wlanAntennaConfig = wlanAntennaConfig,
            wlanFrequency = wlanFrequency,
            wlanFrequencySupport = wlanFrequencySupport,
            wlanFrequencyClMode = wlanFrequencyClMode,
        )

        ThetaRepository.OptionNameEnum.entries.forEach {
            assertNotNull(options.getValue(it), "option: ${it.value}")
        }

        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.AccessInfo), accessInfo, "accessInfo")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.AiAutoThumbnail), aiAutoThumbnail, "aiAutoThumbnail")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.AiAutoThumbnailSupport), aiAutoThumbnailSupport, "aiAutoThumbnailSupport")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.Aperture), aperture, "aperture")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.ApertureSupport), apertureSupport, "apertureSupport")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.AutoBracket), autoBracket, "autoBracket")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.Bitrate), bitrate, "bitrate")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.BluetoothPower), bluetoothPower, "bluetoothPower")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.BluetoothRole), bluetoothRole, "bluetoothRole")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.BurstMode), burstMode, "burstMode")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.BurstOption), burstOption, "burstOption")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.CameraControlSource), cameraControlSource, "cameraControlSource")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.CameraControlSourceSupport), cameraControlSourceSupport, "cameraControlSourceSupport")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.CameraPowerSupport), cameraPowerSupport, "cameraPowerSupport")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.CameraLock), cameraLock, "cameraLock")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.CameraLockConfig), cameraLockConfig, "cameraLockConfig")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.CameraMode), cameraMode, "cameraMode")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.CameraPower), cameraPower, "cameraPower")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.CaptureInterval), captureInterval, "captureInterval")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.CaptureMode), captureMode, "captureMode")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.CaptureNumber), captureNumber, "captureNumber")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.ColorTemperature), colorTemperature, "colorTemperature")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.ColorTemperatureSupport), colorTemperatureSupport, "colorTemperatureSupport")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.CompassDirectionRef), compassDirectionRef, "compassDirectionRef")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.CompositeShootingOutputInterval), compositeShootingOutputInterval, "compositeShootingOutputInterval")
        assertEquals(
            options.getValue(ThetaRepository.OptionNameEnum.CompositeShootingOutputIntervalSupport),
            compositeShootingOutputIntervalSupport,
            "compositeShootingOutputIntervalSupport"
        )
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.CompositeShootingTime), compositeShootingTime, "compositeShootingTime")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.ContinuousNumber), continuousNumber, "continuousNumber")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.DateTimeZone), dateTimeZone, "dateTimeZone")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.EthernetConfig), ethernetConfig, "ethernetConfig")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.ExposureCompensation), exposureCompensation, "exposureCompensation")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.ExposureDelay), exposureDelay, "exposureDelay")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.ExposureDelaySupport), exposureDelaySupport, "exposureDelaySupport")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.ExposureProgram), exposureProgram, "exposureProgram")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.FaceDetect), faceDetect, "faceDetect")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.FileFormat), fileFormat, "fileFormat")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.Filter), filter, "filter")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.Function), function, "function")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.Gain), gain, "gain")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.GpsInfo), gpsInfo, "gpsInfo")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.GpsTagRecordingSupport), gpsTagRecordingSupport, "gpsTagRecordingSupport")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.ImageStitching), imageStitching, "imageStitching")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.IsGpsOn), isGpsOn, "isGpsOn")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.Iso), iso, "iso")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.IsoAutoHighLimit), isoAutoHighLimit, "isoAutoHighLimit")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.Language), language, "language")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.LatestEnabledExposureDelayTime), latestEnabledExposureDelayTime, "latestEnabledExposureDelayTime")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.MaxRecordableTime), maxRecordableTime, "maxRecordableTime")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.MicrophoneNoiseReduction), microphoneNoiseReduction, "microphoneNoiseReduction")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.MobileNetworkSetting), mobileNetworkSetting, "mobileNetworkSetting")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.NetworkType), networkType, "networkType")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.OffDelay), offDelay, "offDelay")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.OffDelayUsb), offDelayUsb, "offDelayUsb")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.Password), password, "password")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.PowerSaving), powerSaving, "powerSaving")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.Preset), preset, "preset")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.PreviewFormat), previewFormat, "previewFormat")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.Proxy), proxy, "proxy")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.RemainingPictures), remainingPictures, "remainingPictures")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.RemainingVideoSeconds), remainingVideoSeconds, "remainingVideoSeconds")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.RemainingSpace), remainingSpace, "remainingSpace")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.ShootingMethod), shootingMethod, "shootingMethod")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.ShutterSpeed), shutterSpeed, "shutterSpeed")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.ShutterVolume), shutterVolume, "shutterVolume")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.SleepDelay), sleepDelay, "sleepDelay")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.TimeShift), timeShift, "timeShift")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.TopBottomCorrection), topBottomCorrection, "topBottomCorrection")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.TopBottomCorrectionRotation), topBottomCorrectionRotation, "topBottomCorrectionRotation")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.TopBottomCorrectionRotationSupport), topBottomCorrectionRotationSupport, "topBottomCorrectionRotationSupport")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.TotalSpace), totalSpace, "totalSpace")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.UsbConnection), usbConnection, "usbConnection")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.Username), username, "userName")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.VideoStitching), videoStitching, "videoStitching")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.VisibilityReduction), visibilityReduction, "visibilityReduction")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.WhiteBalance), whiteBalance, "whiteBalance")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.WhiteBalanceAutoStrength), whiteBalanceAutoStrength, "whiteBalanceAutoStrength")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.WlanAntennaConfig), wlanAntennaConfig, "wlanAntennaConfig")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.WlanFrequency), wlanFrequency, "wlanFrequency")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.WlanFrequencySupport), wlanFrequencySupport, "wlanFrequencySupport")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.WlanFrequencyClMode), wlanFrequencyClMode, "wlanFrequencyClMode")
    }

    /**
     * Options value set get.
     */
    @Test
    fun optionsSetValueTest() {
        val values = listOf(
            Pair(
                ThetaRepository.OptionNameEnum.AccessInfo, ThetaRepository.AccessInfo(
                    ssid = "ssid_test",
                    ipAddress = "192.168.1.2",
                    subnetMask = "255.255.0.0",
                    defaultGateway = "192.168.1.12",
                    dns1 = "192.168.1.55",
                    dns2 = "192.168.1.66",
                    proxyURL = "http://192.168.1.3",
                    frequency = ThetaRepository.WlanFrequencyAccessInfoEnum.GHZ_2_4,
                    wlanSignalStrength = -60,
                    wlanSignalLevel = 4,
                    lteSignalStrength = 0,
                    lteSignalLevel = 0,
                    dhcpLeaseAddress = listOf(
                        ThetaRepository.DhcpLeaseAddress(ipAddress = "192.168.1.5", macAddress = "192.168.1.6", hostName = "192.168.1.7"),
                        ThetaRepository.DhcpLeaseAddress(ipAddress = "192.168.1.8", macAddress = "192.168.1.9", hostName = "192.168.1.10")
                    )
                )
            ),
            Pair(ThetaRepository.OptionNameEnum.AiAutoThumbnail, ThetaRepository.AiAutoThumbnailEnum.OFF),
            Pair(ThetaRepository.OptionNameEnum.Aperture, ThetaRepository.ApertureEnum.APERTURE_2_1),
            Pair(ThetaRepository.OptionNameEnum.ApertureSupport, listOf(ThetaRepository.ApertureEnum.APERTURE_AUTO, ThetaRepository.ApertureEnum.APERTURE_2_4)),
            Pair(
                ThetaRepository.OptionNameEnum.AutoBracket,
                ThetaRepository.BracketSettingList().add(
                    ThetaRepository.BracketSetting(
                        exposureProgram = ThetaRepository.ExposureProgramEnum.NORMAL_PROGRAM,
                        whiteBalance = ThetaRepository.WhiteBalanceEnum.DAYLIGHT,
                    )
                ).add(
                    ThetaRepository.BracketSetting(
                        aperture = ThetaRepository.ApertureEnum.APERTURE_2_0,
                        colorTemperature = 6800,
                        exposureProgram = ThetaRepository.ExposureProgramEnum.MANUAL,
                        iso = ThetaRepository.IsoEnum.ISO_800,
                        shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_100,
                        whiteBalance = ThetaRepository.WhiteBalanceEnum.CLOUDY_DAYLIGHT,
                    )
                )
            ),
            Pair(ThetaRepository.OptionNameEnum.Bitrate, ThetaRepository.BitrateEnum.FINE),
            Pair(ThetaRepository.OptionNameEnum.BluetoothPower, ThetaRepository.BluetoothPowerEnum.ON),
            Pair(ThetaRepository.OptionNameEnum.BluetoothRole, ThetaRepository.BluetoothRoleEnum.CENTRAL),
            Pair(ThetaRepository.OptionNameEnum.BurstMode, ThetaRepository.BurstModeEnum.ON),
            Pair(
                ThetaRepository.OptionNameEnum.BurstOption, ThetaRepository.BurstOption(
                    burstCaptureNum = ThetaRepository.BurstCaptureNumEnum.BURST_CAPTURE_NUM_1,
                    burstBracketStep = ThetaRepository.BurstBracketStepEnum.BRACKET_STEP_0_0,
                    burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_0_0,
                    burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_15,
                    burstEnableIsoControl = ThetaRepository.BurstEnableIsoControlEnum.OFF,
                    burstOrder = ThetaRepository.BurstOrderEnum.BURST_BRACKET_ORDER_0
                )
            ),
            Pair(ThetaRepository.OptionNameEnum.CameraControlSource, ThetaRepository.CameraControlSourceEnum.CAMERA),
            Pair(ThetaRepository.OptionNameEnum.CameraLock, ThetaRepository.CameraLockEnum.BASIC_LOCK),
            Pair(
                ThetaRepository.OptionNameEnum.CameraLockConfig,
                ThetaRepository.CameraLockConfig(
                    isPowerKeyLocked = true,
                    isShutterKeyLocked = false,
                    isModeKeyLocked = true,
                    isWlanKeyLocked = true,
                    isFnKeyLocked = false,
                    isPanelLocked = false
                )
            ),
            Pair(ThetaRepository.OptionNameEnum.CameraMode, ThetaRepository.CameraModeEnum.CAPTURE),
            Pair(ThetaRepository.OptionNameEnum.CameraPower, ThetaRepository.CameraPowerEnum.ON),
            Pair(ThetaRepository.OptionNameEnum.CaptureInterval, 4),
            Pair(ThetaRepository.OptionNameEnum.CaptureMode, ThetaRepository.CaptureModeEnum.IMAGE),
            Pair(ThetaRepository.OptionNameEnum.CaptureNumber, 0),
            Pair(ThetaRepository.OptionNameEnum.ColorTemperature, 10),
            Pair(ThetaRepository.OptionNameEnum.ColorTemperatureSupport, ThetaRepository.ValueRange(10000, 2000, 100)),
            Pair(ThetaRepository.OptionNameEnum.CompassDirectionRef, ThetaRepository.CompassDirectionRefEnum.TRUE_NORTH),
            Pair(ThetaRepository.OptionNameEnum.CompositeShootingOutputInterval, 60),
            Pair(ThetaRepository.OptionNameEnum.CompositeShootingOutputIntervalSupport, ThetaRepository.ValueRange(600, 0, 60)),
            Pair(ThetaRepository.OptionNameEnum.CompositeShootingTime, 600),
            Pair(ThetaRepository.OptionNameEnum.CompositeShootingTimeSupport, ThetaRepository.ValueRange(86400, 600, 600)),
            Pair(ThetaRepository.OptionNameEnum.DateTimeZone, "2014:05:18 01:04:29+08:00"),
            Pair(
                ThetaRepository.OptionNameEnum.EthernetConfig,
                ThetaRepository.EthernetConfig(
                    usingDhcp = true,
                    ipAddress = "192.168.1.2",
                    subnetMask = "255.255.0.0",
                    defaultGateway = "192.168.1.12",
                    dns1 = "192.168.1.55",
                    dns2 = "192.168.1.66",
                    proxy = ThetaRepository.Proxy(use = true, url = "192.168.1.3", port = 80, userid = "abc", password = "123")
                )
            ),
            Pair(ThetaRepository.OptionNameEnum.ExposureCompensation, ThetaRepository.ExposureCompensationEnum.M0_3),
            Pair(ThetaRepository.OptionNameEnum.ExposureDelay, ThetaRepository.ExposureDelayEnum.DELAY_10),
            Pair(ThetaRepository.OptionNameEnum.ExposureDelaySupport, listOf(ThetaRepository.ExposureDelayEnum.DELAY_OFF, ThetaRepository.ExposureDelayEnum.DELAY_10)),
            Pair(ThetaRepository.OptionNameEnum.ExposureProgram, ThetaRepository.ExposureProgramEnum.NORMAL_PROGRAM),
            Pair(ThetaRepository.OptionNameEnum.FaceDetect, ThetaRepository.FaceDetectEnum.OFF),
            Pair(ThetaRepository.OptionNameEnum.FileFormat, ThetaRepository.FileFormatEnum.IMAGE_11K),
            Pair(ThetaRepository.OptionNameEnum.Filter, ThetaRepository.FilterEnum.HDR),
            Pair(ThetaRepository.OptionNameEnum.Function, ThetaRepository.ShootingFunctionEnum.NORMAL),
            Pair(ThetaRepository.OptionNameEnum.Gain, ThetaRepository.GainEnum.NORMAL),
            Pair(ThetaRepository.OptionNameEnum.GpsInfo, ThetaRepository.GpsInfo.disabled),
            Pair(ThetaRepository.OptionNameEnum.ImageStitching, ThetaRepository.ImageStitchingEnum.AUTO),
            Pair(ThetaRepository.OptionNameEnum.IsGpsOn, true),
            Pair(ThetaRepository.OptionNameEnum.Iso, ThetaRepository.IsoEnum.ISO_125),
            Pair(ThetaRepository.OptionNameEnum.IsoAutoHighLimit, ThetaRepository.IsoAutoHighLimitEnum.ISO_1000),
            Pair(ThetaRepository.OptionNameEnum.Language, ThetaRepository.LanguageEnum.JA),
            Pair(ThetaRepository.OptionNameEnum.LatestEnabledExposureDelayTime, ThetaRepository.ExposureDelayEnum.DELAY_10),
            Pair(ThetaRepository.OptionNameEnum.MaxRecordableTime, ThetaRepository.MaxRecordableTimeEnum.RECORDABLE_TIME_1500),
            Pair(ThetaRepository.OptionNameEnum.MicrophoneNoiseReduction, ThetaRepository.MicrophoneNoiseReductionEnum.ON),
            Pair(
                ThetaRepository.OptionNameEnum.MobileNetworkSetting,
                ThetaRepository.MobileNetworkSetting(roaming = ThetaRepository.RoamingEnum.ON, plan = ThetaRepository.PlanEnum.SORACOM)
            ),
            Pair(ThetaRepository.OptionNameEnum.NetworkType, ThetaRepository.NetworkTypeEnum.ETHERNET),
            Pair(ThetaRepository.OptionNameEnum.OffDelay, ThetaRepository.OffDelayEnum.OFF_DELAY_10M),
            Pair(ThetaRepository.OptionNameEnum.OffDelayUsb, ThetaRepository.OffDelayUsbEnum.OFF_DELAY_10M),
            Pair(ThetaRepository.OptionNameEnum.Password, "password"),
            Pair(ThetaRepository.OptionNameEnum.PowerSaving, ThetaRepository.PowerSavingEnum.OFF),
            Pair(ThetaRepository.OptionNameEnum.Preset, ThetaRepository.PresetEnum.NIGHT_VIEW),
            Pair(ThetaRepository.OptionNameEnum.PreviewFormat, ThetaRepository.PreviewFormatEnum.W1024_H512_F30),
            Pair(ThetaRepository.OptionNameEnum.Proxy, ThetaRepository.Proxy(use = false, url = "", port = 8080)),
            Pair(ThetaRepository.OptionNameEnum.RemainingPictures, 101),
            Pair(ThetaRepository.OptionNameEnum.RemainingVideoSeconds, 102),
            Pair(ThetaRepository.OptionNameEnum.RemainingSpace, 103L),
            Pair(ThetaRepository.OptionNameEnum.ShootingMethod, ThetaRepository.ShootingMethodEnum.NORMAL),
            Pair(ThetaRepository.OptionNameEnum.ShutterSpeed, ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_30),
            Pair(ThetaRepository.OptionNameEnum.ShutterVolume, 10),
            Pair(ThetaRepository.OptionNameEnum.SleepDelay, ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M),
            Pair(
                ThetaRepository.OptionNameEnum.TimeShift,
                ThetaRepository.TimeShiftSetting(true, ThetaRepository.TimeShiftIntervalEnum.INTERVAL_2, ThetaRepository.TimeShiftIntervalEnum.INTERVAL_3)
            ),
            Pair(ThetaRepository.OptionNameEnum.TopBottomCorrection, ThetaRepository.TopBottomCorrectionOptionEnum.APPLY),
            Pair(ThetaRepository.OptionNameEnum.TopBottomCorrectionRotation, ThetaRepository.TopBottomCorrectionRotation(pitch = 1.0f, roll = 1.0f, yaw = 1.0f)),
            Pair(
                ThetaRepository.OptionNameEnum.TopBottomCorrectionRotationSupport, ThetaRepository.TopBottomCorrectionRotationSupport(
                    pitch = ThetaRepository.ValueRange(100f, -100f, 0.2f),
                    roll = ThetaRepository.ValueRange(200f, -200f, 0.4f),
                    yaw = ThetaRepository.ValueRange(300f, -300f, 0.6f)
                )
            ),
            Pair(ThetaRepository.OptionNameEnum.TotalSpace, 104L),
            Pair(ThetaRepository.OptionNameEnum.UsbConnection, ThetaRepository.UsbConnectionEnum.MTP),
            Pair(ThetaRepository.OptionNameEnum.Username, "username"),
            Pair(ThetaRepository.OptionNameEnum.VideoStitching, ThetaRepository.VideoStitchingEnum.NONE),
            Pair(ThetaRepository.OptionNameEnum.VisibilityReduction, ThetaRepository.VisibilityReductionEnum.OFF),
            Pair(ThetaRepository.OptionNameEnum.WhiteBalance, ThetaRepository.WhiteBalanceEnum.WARM_WHITE_FLUORESCENT),
            Pair(ThetaRepository.OptionNameEnum.WhiteBalanceAutoStrength, ThetaRepository.WhiteBalanceAutoStrengthEnum.ON),
            Pair(ThetaRepository.OptionNameEnum.WlanAntennaConfig, ThetaRepository.WlanAntennaConfigEnum.SISO),
            Pair(ThetaRepository.OptionNameEnum.WlanFrequency, ThetaRepository.WlanFrequencyEnum.GHZ_5),
            Pair(
                ThetaRepository.OptionNameEnum.WlanFrequencyClMode,
                ThetaRepository.WlanFrequencyClMode(
                    enable2_4 = true,
                    enable5_2 = false,
                    enable5_8 = false,
                )
            ),
        )
        val options = ThetaRepository.Options()
        values.forEach {
            options.setValue(it.first, it.second)
        }

        values.forEach {
            assertEquals(options.getValue(it.first), it.second, "option: ${it.first.value}")
        }
    }

    /**
     * Options secondary constructor.
     */
    @Test
    fun optionsSecondaryConstructorTest() {
        val accessInfo = Pair(
            AccessInfo(
                ssid = "ssid_test",
                ipAddress = "192.168.1.2",
                subnetMask = "255.255.0.0",
                defaultGateway = "192.168.1.12",
                dns1 = "192.168.1.55",
                dns2 = "192.168.1.66",
                proxyURL = "http://192.168.1.3",
                frequency = WlanFrequencyAccessInfo.GHZ_2_4,
                wlanSignalStrength = -60,
                wlanSignalLevel = 4,
                lteSignalStrength = 0,
                lteSignalLevel = 0,
                dhcpLeaseAddress = listOf(
                    DhcpLeaseAddress(ipAddress = "192.168.1.5", macAddress = "192.168.1.6", hostName = "192.168.1.7"),
                    DhcpLeaseAddress(ipAddress = "192.168.1.8", macAddress = "192.168.1.9", hostName = "192.168.1.10")
                )
            ), ThetaRepository.AccessInfo(
                ssid = "ssid_test",
                ipAddress = "192.168.1.2",
                subnetMask = "255.255.0.0",
                defaultGateway = "192.168.1.12",
                dns1 = "192.168.1.55",
                dns2 = "192.168.1.66",
                proxyURL = "http://192.168.1.3",
                frequency = ThetaRepository.WlanFrequencyAccessInfoEnum.GHZ_2_4,
                wlanSignalStrength = -60,
                wlanSignalLevel = 4,
                lteSignalStrength = 0,
                lteSignalLevel = 0,
                dhcpLeaseAddress = listOf(
                    ThetaRepository.DhcpLeaseAddress(ipAddress = "192.168.1.5", macAddress = "192.168.1.6", hostName = "192.168.1.7"),
                    ThetaRepository.DhcpLeaseAddress(ipAddress = "192.168.1.8", macAddress = "192.168.1.9", hostName = "192.168.1.10")
                )
            )
        )
        val aiAutoThumbnail = Pair(AiAutoThumbnail.OFF, ThetaRepository.AiAutoThumbnailEnum.OFF)
        val aiAutoThumbnailSupport = Pair(listOf(AiAutoThumbnail.ON, AiAutoThumbnail.OFF), listOf(ThetaRepository.AiAutoThumbnailEnum.ON, ThetaRepository.AiAutoThumbnailEnum.OFF))
        val aperture = Pair(2.1f, ThetaRepository.ApertureEnum.APERTURE_2_1)
        val apertureSupport = Pair(listOf(2.1f, 3.5f), listOf(ThetaRepository.ApertureEnum.APERTURE_2_1, ThetaRepository.ApertureEnum.APERTURE_3_5))
        val autoBracket = Pair(
            AutoBracket(
                2, listOf(
                    BracketParameter(exposureProgram = 2, whiteBalance = WhiteBalance.DAYLIGHT),
                    BracketParameter(
                        aperture = 2.0F,
                        _colorTemperature = 6800,
                        exposureProgram = 1,
                        iso = 800,
                        shutterSpeed = 0.01,
                        whiteBalance = WhiteBalance.CLOUDY_DAYLIGHT,
                    )
                )
            ),
            ThetaRepository.BracketSettingList().add(
                ThetaRepository.BracketSetting(
                    exposureProgram = ThetaRepository.ExposureProgramEnum.NORMAL_PROGRAM,
                    whiteBalance = ThetaRepository.WhiteBalanceEnum.DAYLIGHT,
                )
            ).add(
                ThetaRepository.BracketSetting(
                    aperture = ThetaRepository.ApertureEnum.APERTURE_2_0,
                    colorTemperature = 6800,
                    exposureProgram = ThetaRepository.ExposureProgramEnum.MANUAL,
                    iso = ThetaRepository.IsoEnum.ISO_800,
                    shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_100,
                    whiteBalance = ThetaRepository.WhiteBalanceEnum.CLOUDY_DAYLIGHT,
                )
            )
        )
        val bitrate = Pair("Fine", ThetaRepository.BitrateEnum.FINE)
        val bluetoothPower = Pair(BluetoothPower.ON, ThetaRepository.BluetoothPowerEnum.ON)
        val bluetoothRole = Pair(BluetoothRole.CENTRAL, ThetaRepository.BluetoothRoleEnum.CENTRAL)
        val burstMode = Pair(BurstMode.ON, ThetaRepository.BurstModeEnum.ON)
        val burstOption = Pair(
            BurstOption(
                _burstCaptureNum = BurstCaptureNum.BURST_CAPTURE_NUM_1,
                _burstBracketStep = BurstBracketStep.BRACKET_STEP_0_0,
                _burstCompensation = BurstCompensation.BURST_COMPENSATION_0_0,
                _burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_15,
                _burstEnableIsoControl = BurstEnableIsoControl.OFF,
                _burstOrder = BurstOrder.BURST_BRACKET_ORDER_0
            ), ThetaRepository.BurstOption(
                burstCaptureNum = ThetaRepository.BurstCaptureNumEnum.BURST_CAPTURE_NUM_1,
                burstBracketStep = ThetaRepository.BurstBracketStepEnum.BRACKET_STEP_0_0,
                burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_0_0,
                burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_15,
                burstEnableIsoControl = ThetaRepository.BurstEnableIsoControlEnum.OFF,
                burstOrder = ThetaRepository.BurstOrderEnum.BURST_BRACKET_ORDER_0
            )
        )
        val cameraControlSource = Pair(CameraControlSource.CAMERA, ThetaRepository.CameraControlSourceEnum.CAMERA)
        val cameraControlSourceSupport =
            Pair(listOf(CameraControlSource.CAMERA, CameraControlSource.APP), listOf(ThetaRepository.CameraControlSourceEnum.CAMERA, ThetaRepository.CameraControlSourceEnum.APP))
        val cameraLock = Pair(CameraLock.UNLOCK, ThetaRepository.CameraLockEnum.UNLOCK)
        val cameraLockConfig = Pair(
            CameraLockConfig(
                powerKey = CameraLockType.LOCK,
                shutterKey = CameraLockType.LOCK,
                modeKey = CameraLockType.LOCK,
                wlanKey = CameraLockType.UNLOCK,
                fnKey = CameraLockType.UNLOCK,
                panel = CameraLockType.UNLOCK
            ),
            ThetaRepository.CameraLockConfig(
                isPowerKeyLocked = true,
                isShutterKeyLocked = true,
                isModeKeyLocked = true,
                isWlanKeyLocked = false,
                isFnKeyLocked = false,
                isPanelLocked = false
            )
        )
        val cameraMode = Pair(CameraMode.CAPTURE, ThetaRepository.CameraModeEnum.CAPTURE)
        val cameraPower = Pair(CameraPower.OFF, ThetaRepository.CameraPowerEnum.OFF)
        val cameraPowerSupport = Pair(
            listOf(CameraPower.ON, CameraPower.OFF, CameraPower.POWER_SAVING, CameraPower.SILENT_MODE),
            listOf(
                ThetaRepository.CameraPowerEnum.ON,
                ThetaRepository.CameraPowerEnum.OFF,
                ThetaRepository.CameraPowerEnum.POWER_SAVING,
                ThetaRepository.CameraPowerEnum.SILENT_MODE
            )
        )
        val captureInterval = Pair(5, 5)
        val captureMode = Pair(CaptureMode.IMAGE, ThetaRepository.CaptureModeEnum.IMAGE)
        val captureNumber = Pair(9999, 9999)
        val colorTemperature = Pair(10, 10)
        val colorTemperatureSupport = Pair(ColorTemperatureSupport(10000, 2000, 100), ThetaRepository.ValueRange(10000, 2000, 100))
        val compassDirectionRef = Pair(CompassDirectionRef.MAGNETIC, ThetaRepository.CompassDirectionRefEnum.MAGNETIC)
        val compositeShootingOutputInterval = Pair(60, 60)
        val compositeShootingOutputIntervalSupport = Pair(CompositeShootingOutputIntervalSupport(600, 0, 60), ThetaRepository.ValueRange(600, 0, 60))
        val compositeShootingTime = Pair(600, 600)
        val compositeShootingTimeSupport = Pair(CompositeShootingTimeSupport(86400, 600, 600), ThetaRepository.ValueRange(86400, 600, 600))
        val dateTimeZone = Pair("2014:05:18 01:04:29+08:00", "2014:05:18 01:04:29+08:00")
        val ethernetConfig = Pair(
            EthernetConfig(
                ipAddressAllocation = IpAddressAllocation.DYNAMIC,
                ipAddress = "192.168.1.2",
                subnetMask = "255.255.0.0",
                defaultGateway = "192.168.1.12",
                dns1 = "192.168.1.55",
                dns2 = "192.168.1.66",
                _proxy = Proxy(use = true, url = "192.168.1.3", port = 80, userid = "abc", password = "123")
            ),
            ThetaRepository.EthernetConfig(
                usingDhcp = true,
                ipAddress = "192.168.1.2",
                subnetMask = "255.255.0.0",
                defaultGateway = "192.168.1.12",
                dns1 = "192.168.1.55",
                dns2 = "192.168.1.66",
                proxy = ThetaRepository.Proxy(use = true, url = "192.168.1.3", port = 80, userid = "abc", password = "123")
            )
        )
        val exposureCompensation = Pair(-0.3f, ThetaRepository.ExposureCompensationEnum.M0_3)
        val exposureDelay = Pair(10, ThetaRepository.ExposureDelayEnum.DELAY_10)
        val exposureDelaySupport = Pair(listOf(0, 10), listOf(ThetaRepository.ExposureDelayEnum.DELAY_OFF, ThetaRepository.ExposureDelayEnum.DELAY_10))
        val exposureProgram = Pair(2, ThetaRepository.ExposureProgramEnum.NORMAL_PROGRAM)
        val faceDetect = Pair(FaceDetect.OFF, ThetaRepository.FaceDetectEnum.OFF)
        val fileFormat = Pair(
            MediaFileFormat(MediaType.JPEG, 11008, 5504, null, null),
            ThetaRepository.FileFormatEnum.IMAGE_11K
        )
        val filter = Pair(ImageFilter.HDR, ThetaRepository.FilterEnum.HDR)
        val function = Pair(ShootingFunction.NORMAL, ThetaRepository.ShootingFunctionEnum.NORMAL)
        val gain = Pair(Gain.NORMAL, ThetaRepository.GainEnum.NORMAL)
        val gpsInfo = Pair(GpsInfo(65535.0f, 65535.0f, 0f, null, null), ThetaRepository.GpsInfo.disabled)
        val gpsTagRecordingSupport = Pair(listOf(GpsTagRecording.ON, GpsTagRecording.OFF), listOf(ThetaRepository.GpsTagRecordingEnum.ON, ThetaRepository.GpsTagRecordingEnum.OFF))
        val imageStitching = Pair(ImageStitching.AUTO, ThetaRepository.ImageStitchingEnum.AUTO)
        val isGpsOn = Pair(GpsTagRecording.ON, true)
        val iso = Pair(125, ThetaRepository.IsoEnum.ISO_125)
        val isoAutoHighLimit = Pair(1000, ThetaRepository.IsoAutoHighLimitEnum.ISO_1000)
        val language = Pair(Language.JA, ThetaRepository.LanguageEnum.JA)
        val latestEnabledExposureDelayTime = Pair(10, ThetaRepository.ExposureDelayEnum.DELAY_10)
        val maxRecordableTime = Pair(1500, ThetaRepository.MaxRecordableTimeEnum.RECORDABLE_TIME_1500)
        val microphoneNoiseReduction = Pair(MicrophoneNoiseReduction.OFF, ThetaRepository.MicrophoneNoiseReductionEnum.OFF)
        val mobileNetworkSetting = Pair(
            MobileNetworkSetting(roaming = Roaming.ON, plan = Plan.SORACOM_PLAN_DU),
            ThetaRepository.MobileNetworkSetting(roaming = ThetaRepository.RoamingEnum.ON, plan = ThetaRepository.PlanEnum.SORACOM_PLAN_DU)
        )
        val networkType = Pair(NetworkType.DIRECT, ThetaRepository.NetworkTypeEnum.DIRECT)
        val offDelay = Pair(600, ThetaRepository.OffDelayEnum.OFF_DELAY_10M)
        val offDelayUsb = Pair(600, ThetaRepository.OffDelayUsbEnum.OFF_DELAY_10M)
        val password = Pair("password", "password")
        val powerSaving = Pair(PowerSaving.OFF, ThetaRepository.PowerSavingEnum.OFF)
        val preset = Pair(Preset.LENS_BY_LENS_EXPOSURE, ThetaRepository.PresetEnum.LENS_BY_LENS_EXPOSURE)
        val previewFormat = Pair(PreviewFormat(1024, 512, 30), ThetaRepository.PreviewFormatEnum.W1024_H512_F30)
        val proxy = Pair(Proxy(use = false, url = "", port = 8080), ThetaRepository.Proxy(use = false, url = "", port = 8080))
        val remainingPictures = Pair(101, 101)
        val remainingVideoSeconds = Pair(102, 102)
        val remainingSpace = Pair(103L, 103L)
        val shootingMethod = Pair(ShootingMethod.NORMAL, ThetaRepository.ShootingMethodEnum.NORMAL)
        val shutterSpeed = Pair(10.0, ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_10)
        val shutterVolume = Pair(10, 10)
        val sleepDelay = Pair(180, ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M)
        val timeShift = Pair(
            TimeShift(FirstShootingEnum.FRONT, 4, 5),
            ThetaRepository.TimeShiftSetting(true, ThetaRepository.TimeShiftIntervalEnum.INTERVAL_4, ThetaRepository.TimeShiftIntervalEnum.INTERVAL_5)
        )
        val topBottomCorrection = Pair(TopBottomCorrectionOption.DISAPPLY, ThetaRepository.TopBottomCorrectionOptionEnum.DISAPPLY)
        val topBottomCorrectionRotation = Pair(TopBottomCorrectionRotation("3.0", "2.0", "1.0"), ThetaRepository.TopBottomCorrectionRotation(3.0f, 2.0f, 1.0f))
        val topBottomCorrectionRotationSupport = Pair(
            TopBottomCorrectionRotationSupport(
                pitch = PitchSupport(100f, -100f, 0.2f),
                roll = RollSupport(200f, -200f, 0.4f),
                yaw = YawSupport(300f, -300f, 0.6f)
            ), ThetaRepository.TopBottomCorrectionRotationSupport(
                pitch = ThetaRepository.ValueRange(100f, -100f, 0.2f),
                roll = ThetaRepository.ValueRange(200f, -200f, 0.4f),
                yaw = ThetaRepository.ValueRange(300f, -300f, 0.6f)
            )
        )
        val totalSpace = Pair(104L, 104L)
        val usbConnection = Pair(UsbConnection.MTP, ThetaRepository.UsbConnectionEnum.MTP)
        val username = Pair("username", "username")
        val videoStitching = Pair(VideoStitching.NONE, ThetaRepository.VideoStitchingEnum.NONE)
        val visibilityReduction = Pair(VisibilityReduction.OFF, ThetaRepository.VisibilityReductionEnum.OFF)
        val whiteBalance = Pair(WhiteBalance._WARM_WHITE_FLUORESCENT, ThetaRepository.WhiteBalanceEnum.WARM_WHITE_FLUORESCENT)
        val wlanAntennaConfig = Pair(WlanAntennaConfig.SISO, ThetaRepository.WlanAntennaConfigEnum.SISO)
        val whiteBalanceAutoStrength = Pair(WhiteBalanceAutoStrength.OFF, ThetaRepository.WhiteBalanceAutoStrengthEnum.OFF)
        val wlanFrequency = Pair(WlanFrequency.GHZ_5, ThetaRepository.WlanFrequencyEnum.GHZ_5)
        val wlanFrequencySupport =
            Pair(listOf(WlanFrequency.GHZ_2_4, WlanFrequency.GHZ_5), listOf(ThetaRepository.WlanFrequencyEnum.GHZ_2_4, ThetaRepository.WlanFrequencyEnum.GHZ_5))
        val wlanFrequencyClMode = Pair(
            WlanFrequencyClMode(
                enable2_4 = true,
                enable5_2 = false,
                enable5_8 = false,
            ),
            ThetaRepository.WlanFrequencyClMode(
                enable2_4 = true,
                enable5_2 = false,
                enable5_8 = false,
            )
        )

        val orgOptions = Options(
            _accessInfo = accessInfo.first,
            _aiAutoThumbnail = aiAutoThumbnail.first,
            _aiAutoThumbnailSupport = aiAutoThumbnailSupport.first,
            aperture = aperture.first,
            apertureSupport = apertureSupport.first,
            _autoBracket = autoBracket.first,
            _bitrate = bitrate.first,
            _bluetoothPower = bluetoothPower.first,
            _bluetoothRole = bluetoothRole.first,
            _burstMode = burstMode.first,
            _burstOption = burstOption.first,
            _cameraControlSource = cameraControlSource.first,
            _cameraControlSourceSupport = cameraControlSourceSupport.first,
            _cameraPowerSupport = cameraPowerSupport.first,
            _cameraLock = cameraLock.first,
            _cameraLockConfig = cameraLockConfig.first,
            _cameraMode = cameraMode.first,
            _cameraPower = cameraPower.first,
            captureInterval = captureInterval.first,
            captureMode = captureMode.first,
            captureNumber = captureNumber.first,
            _colorTemperature = colorTemperature.first,
            _colorTemperatureSupport = colorTemperatureSupport.first,
            _compassDirectionRef = compassDirectionRef.first,
            _compositeShootingOutputInterval = compositeShootingOutputInterval.first,
            _compositeShootingOutputIntervalSupport = compositeShootingOutputIntervalSupport.first,
            _compositeShootingTime = compositeShootingTime.first,
            _compositeShootingTimeSupport = compositeShootingTimeSupport.first,
            dateTimeZone = dateTimeZone.first,
            _ethernetConfig = ethernetConfig.first,
            exposureCompensation = exposureCompensation.first,
            exposureDelay = exposureDelay.first,
            exposureDelaySupport = exposureDelaySupport.first,
            exposureProgram = exposureProgram.first,
            _faceDetect = faceDetect.first,
            fileFormat = fileFormat.first,
            _filter = filter.first,
            _function = function.first,
            _gain = gain.first,
            gpsInfo = gpsInfo.first,
            _gpsTagRecording = isGpsOn.first,
            _gpsTagRecordingSupport = gpsTagRecordingSupport.first,
            _imageStitching = imageStitching.first,
            iso = iso.first,
            isoAutoHighLimit = isoAutoHighLimit.first,
            _language = language.first,
            _latestEnabledExposureDelayTime = latestEnabledExposureDelayTime.first,
            _maxRecordableTime = maxRecordableTime.first,
            _microphoneNoiseReduction = microphoneNoiseReduction.first,
            _mobileNetworkSetting = mobileNetworkSetting.first,
            _networkType = networkType.first,
            offDelay = offDelay.first,
            _offDelayUSB = offDelayUsb.first,
            _password = password.first,
            _powerSaving = powerSaving.first,
            _preset = preset.first,
            previewFormat = previewFormat.first,
            _proxy = proxy.first,
            remainingPictures = remainingPictures.first,
            remainingVideoSeconds = remainingVideoSeconds.first,
            remainingSpace = remainingSpace.first,
            _shootingMethod = shootingMethod.first,
            shutterSpeed = shutterSpeed.first,
            _shutterVolume = shutterVolume.first,
            sleepDelay = sleepDelay.first,
            _timeShift = timeShift.first,
            _topBottomCorrection = topBottomCorrection.first,
            _topBottomCorrectionRotation = topBottomCorrectionRotation.first,
            _topBottomCorrectionRotationSupport = topBottomCorrectionRotationSupport.first,
            totalSpace = totalSpace.first,
            _usbConnection = usbConnection.first,
            _username = username.first,
            videoStitching = videoStitching.first,
            _visibilityReduction = visibilityReduction.first,
            whiteBalance = whiteBalance.first,
            _wlanAntennaConfig = wlanAntennaConfig.first,
            _whiteBalanceAutoStrength = whiteBalanceAutoStrength.first,
            _wlanFrequency = wlanFrequency.first,
            _wlanFrequencySupport = wlanFrequencySupport.first,
            _wlanFrequencyCLmode = wlanFrequencyClMode.first,
        )
        val options = ThetaRepository.Options(orgOptions)

        assertEquals(options.accessInfo, accessInfo.second, "accessInfo")
        assertEquals(options.aiAutoThumbnail, aiAutoThumbnail.second, "aiAutoThumbnail")
        assertEquals(options.aiAutoThumbnailSupport, aiAutoThumbnailSupport.second, "aiAutoThumbnailSupport")
        assertEquals(options.aperture, aperture.second, "aperture")
        assertEquals(options.apertureSupport, apertureSupport.second, "apertureSupport")
        assertEquals(options.autoBracket, autoBracket.second, "autoBracket")
        assertEquals(options.bitrate, bitrate.second, "bitrate")
        assertEquals(options.bluetoothPower, bluetoothPower.second, "bluetoothPower")
        assertEquals(options.bluetoothRole, bluetoothRole.second, "bluetoothRole")
        assertEquals(options.burstMode, burstMode.second, "burstMode")
        assertEquals(options.burstOption, burstOption.second, "burstOption")
        assertEquals(options.cameraControlSource, cameraControlSource.second, "cameraControlSource")
        assertEquals(options.cameraControlSourceSupport, cameraControlSourceSupport.second, "cameraControlSourceSupport")
        assertEquals(options.cameraPowerSupport, cameraPowerSupport.second, "cameraPowerSupport")
        assertEquals(options.cameraLock, cameraLock.second, "cameraLock")
        assertEquals(options.cameraLockConfig, cameraLockConfig.second, "cameraLockConfig")
        assertEquals(options.cameraMode, cameraMode.second, "cameraMode")
        assertEquals(options.cameraPower, cameraPower.second, "cameraPower")
        assertEquals(options.captureInterval, captureInterval.second, "captureInterval")
        assertEquals(options.captureMode, captureMode.second, "captureMode")
        assertEquals(options.captureNumber, captureNumber.second, "captureNumber")
        assertEquals(options.colorTemperature, colorTemperature.second, "colorTemperature")
        assertEquals(options.colorTemperatureSupport, colorTemperatureSupport.second, "colorTemperatureSupport")
        assertEquals(options.compassDirectionRef, compassDirectionRef.second, "compassDirectionRef")
        assertEquals(options.compositeShootingOutputInterval, compositeShootingOutputInterval.second, "compositeShootingOutputInterval")
        assertEquals(options.compositeShootingOutputIntervalSupport, compositeShootingOutputIntervalSupport.second, "compositeShootingOutputIntervalSupport")
        assertEquals(options.compositeShootingTime, compositeShootingTime.second, "compositeShootingTime")
        assertEquals(options.compositeShootingTimeSupport, compositeShootingTimeSupport.second, "compositeShootingTimeSupport")
        assertEquals(options.dateTimeZone, dateTimeZone.second, "dateTimeZone")
        assertEquals(options.ethernetConfig, ethernetConfig.second, "ethernetConfig")
        assertEquals(options.exposureCompensation, exposureCompensation.second, "exposureCompensation")
        assertEquals(options.exposureDelay, exposureDelay.second, "exposureDelay")
        assertEquals(options.exposureDelaySupport, exposureDelaySupport.second, "exposureDelaySupport")
        assertEquals(options.exposureProgram, exposureProgram.second, "exposureProgram")
        assertEquals(options.faceDetect, faceDetect.second, "faceDetect")
        assertEquals(options.fileFormat, fileFormat.second, "fileFormat")
        assertEquals(options.filter, filter.second, "filter")
        assertEquals(options.function, function.second, "function")
        assertEquals(options.gain, gain.second, "gain")
        assertEquals(options.gpsInfo, gpsInfo.second, "gpsInfo")
        assertEquals(options.gpsTagRecordingSupport, gpsTagRecordingSupport.second, "gpsTagRecordingSupport")
        assertEquals(options.imageStitching, imageStitching.second, "imageStitching")
        assertEquals(options.isGpsOn, isGpsOn.second, "isGpsOn")
        assertEquals(options.iso, iso.second, "iso")
        assertEquals(options.isoAutoHighLimit, isoAutoHighLimit.second, "isoAutoHighLimit")
        assertEquals(options.language, language.second, "language")
        assertEquals(options.latestEnabledExposureDelayTime, latestEnabledExposureDelayTime.second, "latestEnabledExposureDelayTime")
        assertEquals(options.maxRecordableTime, maxRecordableTime.second, "maxRecordableTime")
        assertEquals(options.microphoneNoiseReduction, microphoneNoiseReduction.second, "microphoneNoiseReduction")
        assertEquals(options.mobileNetworkSetting, mobileNetworkSetting.second, "mobileNetworkSetting")
        assertEquals(options.networkType, networkType.second, "networkType")
        assertEquals(options.offDelay, offDelay.second, "offDelay")
        assertEquals(options.offDelayUsb, offDelayUsb.second, "offDelayUsb")
        assertEquals(options.password, password.second, "password")
        assertEquals(options.powerSaving, powerSaving.second, "powerSaving")
        assertEquals(options.preset, preset.second, "preset")
        assertEquals(options.previewFormat, previewFormat.second, "previewFormat")
        assertEquals(options.proxy, proxy.second, "proxy")
        assertEquals(options.remainingPictures, remainingPictures.second, "remainingPictures")
        assertEquals(options.remainingVideoSeconds, remainingVideoSeconds.second, "remainingVideoSeconds")
        assertEquals(options.remainingSpace, remainingSpace.second, "remainingSpace")
        assertEquals(options.shootingMethod, shootingMethod.second, "shootingMethod")
        assertEquals(options.shutterSpeed, shutterSpeed.second, "shutterSpeed")
        assertEquals(options.shutterVolume, shutterVolume.second, "shutterVolume")
        assertEquals(options.sleepDelay, sleepDelay.second, "sleepDelay")
        assertEquals(options.timeShift, timeShift.second, "timeShift")
        assertEquals(options.topBottomCorrection, topBottomCorrection.second, "topBottomCorrection")
        assertEquals(options.topBottomCorrectionRotation, topBottomCorrectionRotation.second, "topBottomCorrectionRotation")
        assertEquals(options.topBottomCorrectionRotationSupport, topBottomCorrectionRotationSupport.second, "topBottomCorrectionRotationSupport")
        assertEquals(options.totalSpace, totalSpace.second, "totalSpace")
        assertEquals(options.usbConnection, usbConnection.second, "usbConnection")
        assertEquals(options.username, username.second, "username")
        assertEquals(options.videoStitching, videoStitching.second, "videoStitching")
        assertEquals(options.visibilityReduction, visibilityReduction.second, "visibilityReduction")
        assertEquals(options.whiteBalance, whiteBalance.second, "whiteBalance")
        assertEquals(options.wlanAntennaConfig, wlanAntennaConfig.second, "wlanAntennaConfig")
        assertEquals(options.whiteBalanceAutoStrength, whiteBalanceAutoStrength.second, "whiteBalanceAutoStrength")
        assertEquals(options.wlanFrequency, wlanFrequency.second, "wlanFrequency")
        assertEquals(options.wlanFrequencySupport, wlanFrequencySupport.second, "wlanFrequencySupport")
        assertEquals(options.wlanFrequencyClMode, wlanFrequencyClMode.second, "wlanFrequencyClMode")
    }

    @Test
    fun optionsConvertTest() {
        val accessInfo = Pair(
            AccessInfo(
                ssid = "ssid_test",
                ipAddress = "192.168.1.2",
                subnetMask = "255.255.0.0",
                defaultGateway = "192.168.1.12",
                dns1 = "192.168.1.55",
                dns2 = "192.168.1.66",
                proxyURL = "http://192.168.1.3",
                frequency = WlanFrequencyAccessInfo.GHZ_2_4,
                wlanSignalStrength = -60,
                wlanSignalLevel = 4,
                lteSignalStrength = 0,
                lteSignalLevel = 0,
                dhcpLeaseAddress = listOf(
                    DhcpLeaseAddress(ipAddress = "192.168.1.5", macAddress = "192.168.1.6", hostName = "192.168.1.7"),
                    DhcpLeaseAddress(ipAddress = "192.168.1.8", macAddress = "192.168.1.9", hostName = "192.168.1.10")
                )
            ), ThetaRepository.AccessInfo(
                ssid = "ssid_test",
                ipAddress = "192.168.1.2",
                subnetMask = "255.255.0.0",
                defaultGateway = "192.168.1.12",
                dns1 = "192.168.1.55",
                dns2 = "192.168.1.66",
                proxyURL = "http://192.168.1.3",
                frequency = ThetaRepository.WlanFrequencyAccessInfoEnum.GHZ_2_4,
                wlanSignalStrength = -60,
                wlanSignalLevel = 4,
                lteSignalStrength = 0,
                lteSignalLevel = 0,
                dhcpLeaseAddress = listOf(
                    ThetaRepository.DhcpLeaseAddress(ipAddress = "192.168.1.5", macAddress = "192.168.1.6", hostName = "192.168.1.7"),
                    ThetaRepository.DhcpLeaseAddress(ipAddress = "192.168.1.8", macAddress = "192.168.1.9", hostName = "192.168.1.10")
                )
            )
        )
        val aiAutoThumbnail = Pair(AiAutoThumbnail.ON, ThetaRepository.AiAutoThumbnailEnum.ON)
        val aperture = Pair(2.1f, ThetaRepository.ApertureEnum.APERTURE_2_1)
        val autoBracket = Pair(
            AutoBracket(
                2, listOf(
                    BracketParameter(exposureProgram = 2, whiteBalance = WhiteBalance.DAYLIGHT),
                    BracketParameter(
                        aperture = 2.0F,
                        _colorTemperature = 6800,
                        exposureProgram = 1,
                        iso = 800,
                        shutterSpeed = 0.01,
                        whiteBalance = WhiteBalance.CLOUDY_DAYLIGHT,
                    )
                )
            ),
            ThetaRepository.BracketSettingList().add(
                ThetaRepository.BracketSetting(
                    exposureProgram = ThetaRepository.ExposureProgramEnum.NORMAL_PROGRAM,
                    whiteBalance = ThetaRepository.WhiteBalanceEnum.DAYLIGHT,
                )
            ).add(
                ThetaRepository.BracketSetting(
                    aperture = ThetaRepository.ApertureEnum.APERTURE_2_0,
                    colorTemperature = 6800,
                    exposureProgram = ThetaRepository.ExposureProgramEnum.MANUAL,
                    iso = ThetaRepository.IsoEnum.ISO_800,
                    shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_100,
                    whiteBalance = ThetaRepository.WhiteBalanceEnum.CLOUDY_DAYLIGHT,
                )
            )
        )
        val bitrate = Pair("Fine", ThetaRepository.BitrateEnum.FINE)
        val bluetoothPower = Pair(BluetoothPower.ON, ThetaRepository.BluetoothPowerEnum.ON)
        val bluetoothRole = Pair(BluetoothRole.CENTRAL, ThetaRepository.BluetoothRoleEnum.CENTRAL)
        val burstMode = Pair(BurstMode.ON, ThetaRepository.BurstModeEnum.ON)
        val burstOption = Pair(
            BurstOption(
                _burstCaptureNum = BurstCaptureNum.BURST_CAPTURE_NUM_1,
                _burstBracketStep = BurstBracketStep.BRACKET_STEP_0_0,
                _burstCompensation = BurstCompensation.BURST_COMPENSATION_0_0,
                _burstMaxExposureTime = BurstMaxExposureTime.MAX_EXPOSURE_TIME_15,
                _burstEnableIsoControl = BurstEnableIsoControl.OFF,
                _burstOrder = BurstOrder.BURST_BRACKET_ORDER_0
            ), ThetaRepository.BurstOption(
                burstCaptureNum = ThetaRepository.BurstCaptureNumEnum.BURST_CAPTURE_NUM_1,
                burstBracketStep = ThetaRepository.BurstBracketStepEnum.BRACKET_STEP_0_0,
                burstCompensation = ThetaRepository.BurstCompensationEnum.BURST_COMPENSATION_0_0,
                burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.MAX_EXPOSURE_TIME_15,
                burstEnableIsoControl = ThetaRepository.BurstEnableIsoControlEnum.OFF,
                burstOrder = ThetaRepository.BurstOrderEnum.BURST_BRACKET_ORDER_0
            )
        )
        val cameraControlSource = Pair(CameraControlSource.CAMERA, ThetaRepository.CameraControlSourceEnum.CAMERA)
        val cameraControlSourceSupport =
            Pair(listOf(CameraControlSource.CAMERA, CameraControlSource.APP), listOf(ThetaRepository.CameraControlSourceEnum.CAMERA, ThetaRepository.CameraControlSourceEnum.APP))
        val cameraLock = Pair(CameraLock.BASIC_LOCK, ThetaRepository.CameraLockEnum.BASIC_LOCK)
        val cameraLockConfig = Pair(
            CameraLockConfig(
                powerKey = CameraLockType.LOCK,
                shutterKey = CameraLockType.LOCK,
                modeKey = CameraLockType.LOCK,
                wlanKey = CameraLockType.UNLOCK,
                fnKey = CameraLockType.UNLOCK,
                panel = CameraLockType.UNLOCK
            ),
            ThetaRepository.CameraLockConfig(
                isPowerKeyLocked = true,
                isShutterKeyLocked = true,
                isModeKeyLocked = true,
                isWlanKeyLocked = false,
                isFnKeyLocked = false,
                isPanelLocked = false
            )
        )
        val cameraMode = Pair(CameraMode.CAPTURE, ThetaRepository.CameraModeEnum.CAPTURE)
        val cameraPower = Pair(CameraPower.SILENT_MODE, ThetaRepository.CameraPowerEnum.SILENT_MODE)
        val captureInterval = Pair(20, 20)
        val captureMode = Pair(CaptureMode.IMAGE, ThetaRepository.CaptureModeEnum.IMAGE)
        val captureNumber = Pair(30, 30)
        val colorTemperature = Pair(10, 10)
        val compassDirectionRef = Pair(CompassDirectionRef.AUTO, ThetaRepository.CompassDirectionRefEnum.AUTO)
        val compositeShootingOutputInterval = Pair(60, 60)
        val compositeShootingTime = Pair(600, 600)
        val dateTimeZone = Pair("2014:05:18 01:04:29+08:00", "2014:05:18 01:04:29+08:00")
        val ethernetConfig = Pair(
            EthernetConfig(
                ipAddressAllocation = IpAddressAllocation.DYNAMIC,
                ipAddress = "192.168.1.2",
                subnetMask = "255.255.0.0",
                defaultGateway = "192.168.1.12",
                dns1 = "192.168.1.55",
                dns2 = "192.168.1.66",
                _proxy = Proxy(use = true, url = "192.168.1.3", port = 80, userid = "abc", password = "123")
            ),
            ThetaRepository.EthernetConfig(
                usingDhcp = true,
                ipAddress = "192.168.1.2",
                subnetMask = "255.255.0.0",
                defaultGateway = "192.168.1.12",
                dns1 = "192.168.1.55",
                dns2 = "192.168.1.66",
                proxy = ThetaRepository.Proxy(use = true, url = "192.168.1.3", port = 80, userid = "abc", password = "123")
            )
        )
        val exposureCompensation = Pair(-0.3f, ThetaRepository.ExposureCompensationEnum.M0_3)
        val exposureDelay = Pair(10, ThetaRepository.ExposureDelayEnum.DELAY_10)
        val exposureProgram = Pair(2, ThetaRepository.ExposureProgramEnum.NORMAL_PROGRAM)
        val faceDetect = Pair(FaceDetect.ON, ThetaRepository.FaceDetectEnum.ON)
        val fileFormat = Pair(
            MediaFileFormat(MediaType.JPEG, 11008, 5504, null, null),
            ThetaRepository.FileFormatEnum.IMAGE_11K
        )
        val filter = Pair(ImageFilter.HDR, ThetaRepository.FilterEnum.HDR)
        val function = Pair(ShootingFunction.SELF_TIMER, ThetaRepository.ShootingFunctionEnum.SELF_TIMER)
        val gain = Pair(Gain.MEGA_VOLUME, ThetaRepository.GainEnum.MEGA_VOLUME)
        val gpsInfo = Pair(
            GpsInfo(65535.0f, 65535.0f, 0f, "", ""),
            ThetaRepository.GpsInfo(65535.0f, 65535.0f, 0f, "")
        )
        val imageStitching = Pair(ImageStitching.AUTO, ThetaRepository.ImageStitchingEnum.AUTO)
        val isGpsOn = Pair(GpsTagRecording.ON, true)
        val iso = Pair(125, ThetaRepository.IsoEnum.ISO_125)
        val isoAutoHighLimit = Pair(1000, ThetaRepository.IsoAutoHighLimitEnum.ISO_1000)
        val language = Pair(Language.JA, ThetaRepository.LanguageEnum.JA)
        val latestEnabledExposureDelayTime = Pair(10, ThetaRepository.ExposureDelayEnum.DELAY_10)
        val maxRecordableTime = Pair(1500, ThetaRepository.MaxRecordableTimeEnum.RECORDABLE_TIME_1500)
        val microphoneNoiseReduction = Pair(MicrophoneNoiseReduction.ON, ThetaRepository.MicrophoneNoiseReductionEnum.ON)
        val mobileNetworkSetting = Pair(
            MobileNetworkSetting(roaming = Roaming.OFF, plan = Plan.SORACOM),
            ThetaRepository.MobileNetworkSetting(roaming = ThetaRepository.RoamingEnum.OFF, plan = ThetaRepository.PlanEnum.SORACOM)
        )
        val networkType = Pair(NetworkType.ETHERNET, ThetaRepository.NetworkTypeEnum.ETHERNET)
        val offDelay = Pair(600, ThetaRepository.OffDelayEnum.OFF_DELAY_10M)
        val offDelayUsb = Pair(600, ThetaRepository.OffDelayUsbEnum.OFF_DELAY_10M)
        val password = Pair("password", "password")
        val powerSaving = Pair(PowerSaving.OFF, ThetaRepository.PowerSavingEnum.OFF)
        val preset = Pair(Preset.FACE, ThetaRepository.PresetEnum.FACE)
        val previewFormat = Pair(PreviewFormat(640, 320, 30), ThetaRepository.PreviewFormatEnum.W640_H320_F30)
        val proxy = Pair(Proxy(use = false, url = "", port = 8080), ThetaRepository.Proxy(use = false, url = "", port = 8080))
        val remainingPictures = Pair(101, 101)
        val remainingVideoSeconds = Pair(102, 102)
        val remainingSpace = Pair(103L, 103L)
        val shootingMethod = Pair(ShootingMethod.TIMESHIFT, ThetaRepository.ShootingMethodEnum.TIME_SHIFT)
        val shutterSpeed = Pair(20.0, ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_20)
        val shutterVolume = Pair(10, 10)
        val sleepDelay = Pair(180, ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M)
        val timeShift = Pair(
            TimeShift(FirstShootingEnum.REAR, 6, 7),
            ThetaRepository.TimeShiftSetting(false, ThetaRepository.TimeShiftIntervalEnum.INTERVAL_6, ThetaRepository.TimeShiftIntervalEnum.INTERVAL_7)
        )
        val topBottomCorrection = Pair(TopBottomCorrectionOption.MANUAL, ThetaRepository.TopBottomCorrectionOptionEnum.MANUAL)
        val topBottomCorrectionRotation = Pair(TopBottomCorrectionRotation("0.0", "0.0", "0.0"), ThetaRepository.TopBottomCorrectionRotation(0.0f, 0.0f, 0.0f))
        val totalSpace = Pair(104L, 104L)
        val usbConnection = Pair(UsbConnection.MSC, ThetaRepository.UsbConnectionEnum.MSC)
        val userName = Pair("username", "username")
        val videoStitching = Pair(VideoStitching.NONE, ThetaRepository.VideoStitchingEnum.NONE)
        val visibilityReduction = Pair(VisibilityReduction.OFF, ThetaRepository.VisibilityReductionEnum.OFF)
        val whiteBalance = Pair(WhiteBalance._WARM_WHITE_FLUORESCENT, ThetaRepository.WhiteBalanceEnum.WARM_WHITE_FLUORESCENT)
        val wlanAntennaConfig = Pair(WlanAntennaConfig.MIMO, ThetaRepository.WlanAntennaConfigEnum.MIMO)
        val whiteBalanceAutoStrength = Pair(WhiteBalanceAutoStrength.ON, ThetaRepository.WhiteBalanceAutoStrengthEnum.ON)
        val wlanFrequency = Pair(WlanFrequency.GHZ_5, ThetaRepository.WlanFrequencyEnum.GHZ_5)
        val wlanFrequencySupport =
            Pair(listOf(WlanFrequency.GHZ_2_4, WlanFrequency.GHZ_5), listOf(ThetaRepository.WlanFrequencyEnum.GHZ_2_4, ThetaRepository.WlanFrequencyEnum.GHZ_5))
        val wlanFrequencyClMode = Pair(
            WlanFrequencyClMode(
                enable2_4 = true,
                enable5_2 = false,
                enable5_8 = false,
            ),
            ThetaRepository.WlanFrequencyClMode(
                enable2_4 = true,
                enable5_2 = false,
                enable5_8 = false,
            )
        )

        val orgOptions = ThetaRepository.Options(
            accessInfo = accessInfo.second,
            aiAutoThumbnail = aiAutoThumbnail.second,
            aperture = aperture.second,
            autoBracket = autoBracket.second,
            bitrate = bitrate.second,
            bluetoothPower = bluetoothPower.second,
            bluetoothRole = bluetoothRole.second,
            burstMode = burstMode.second,
            burstOption = burstOption.second,
            cameraControlSource = cameraControlSource.second,
            cameraControlSourceSupport = cameraControlSourceSupport.second,
            cameraLock = cameraLock.second,
            cameraLockConfig = cameraLockConfig.second,
            cameraMode = cameraMode.second,
            cameraPower = cameraPower.second,
            captureInterval = captureInterval.second,
            captureMode = captureMode.second,
            captureNumber = captureNumber.second,
            colorTemperature = colorTemperature.second,
            compassDirectionRef = compassDirectionRef.second,
            compositeShootingOutputInterval = compositeShootingOutputInterval.second,
            compositeShootingTime = compositeShootingTime.second,
            dateTimeZone = dateTimeZone.second,
            ethernetConfig = ethernetConfig.second,
            exposureCompensation = exposureCompensation.second,
            exposureDelay = exposureDelay.second,
            exposureProgram = exposureProgram.second,
            faceDetect = faceDetect.second,
            fileFormat = fileFormat.second,
            filter = filter.second,
            function = function.second,
            gain = gain.second,
            gpsInfo = gpsInfo.second,
            imageStitching = imageStitching.second,
            isGpsOn = isGpsOn.second,
            iso = iso.second,
            isoAutoHighLimit = isoAutoHighLimit.second,
            language = language.second,
            latestEnabledExposureDelayTime = latestEnabledExposureDelayTime.second,
            maxRecordableTime = maxRecordableTime.second,
            microphoneNoiseReduction = microphoneNoiseReduction.second,
            mobileNetworkSetting = mobileNetworkSetting.second,
            networkType = networkType.second,
            offDelay = offDelay.second,
            offDelayUsb = offDelayUsb.second,
            password = password.second,
            powerSaving = powerSaving.second,
            preset = preset.second,
            previewFormat = previewFormat.second,
            proxy = proxy.second,
            remainingPictures = remainingPictures.second,
            remainingVideoSeconds = remainingVideoSeconds.second,
            remainingSpace = remainingSpace.second,
            shootingMethod = shootingMethod.second,
            shutterSpeed = shutterSpeed.second,
            shutterVolume = shutterVolume.second,
            sleepDelay = sleepDelay.second,
            timeShift = timeShift.second,
            topBottomCorrection = topBottomCorrection.second,
            topBottomCorrectionRotation = topBottomCorrectionRotation.second,
            totalSpace = totalSpace.second,
            usbConnection = usbConnection.second,
            username = userName.second,
            videoStitching = videoStitching.second,
            visibilityReduction = visibilityReduction.second,
            whiteBalance = whiteBalance.second,
            wlanAntennaConfig = wlanAntennaConfig.second,
            whiteBalanceAutoStrength = whiteBalanceAutoStrength.second,
            wlanFrequency = wlanFrequency.second,
            wlanFrequencySupport = wlanFrequencySupport.second,
            wlanFrequencyClMode = wlanFrequencyClMode.second,
        )
        val options = orgOptions.toOptions()

        assertEquals(options._accessInfo, accessInfo.first, "accessInfo")
        assertEquals(options._aiAutoThumbnail, aiAutoThumbnail.first, "aiAutoThumbnail")
        assertEquals(options.aperture, aperture.first, "aperture")
        assertEquals(options._autoBracket, autoBracket.first, "autoBracket")
        assertEquals(options._bitrate, bitrate.first, "bitrate")
        assertEquals(options._bluetoothPower, bluetoothPower.first, "bluetoothPower")
        assertEquals(options._bluetoothRole, bluetoothRole.first, "bluetoothRole")
        assertEquals(options._burstMode, burstMode.first, "burstMode")
        assertEquals(options._burstOption, burstOption.first, "burstOption")
        assertEquals(options._cameraControlSource, cameraControlSource.first, "cameraControlSource")
        assertEquals(options._cameraControlSourceSupport, cameraControlSourceSupport.first, "cameraControlSourceSupport")
        assertEquals(options._cameraLock, cameraLock.first, "cameraLock")
        assertEquals(options._cameraLockConfig, cameraLockConfig.first, "cameraLockConfig")
        assertEquals(options._cameraMode, cameraMode.first, "cameraMode")
        assertEquals(options._cameraPower, cameraPower.first, "cameraPower")
        assertEquals(options.captureInterval, captureInterval.first, "captureInterval")
        assertEquals(options.captureMode, captureMode.first, "captureMode")
        assertEquals(options.captureNumber, captureNumber.first, "captureNumber")
        assertEquals(options._colorTemperature, colorTemperature.first, "colorTemperature")
        assertEquals(options._compassDirectionRef, compassDirectionRef.first, "compassDirectionRef")
        assertEquals(options._compositeShootingOutputInterval, compositeShootingOutputInterval.first, "compositeShootingOutputInterval")
        assertEquals(options._compositeShootingTime, compositeShootingTime.first, "compositeShootingTime")
        assertEquals(options.dateTimeZone, dateTimeZone.first, "dateTimeZone")
        assertEquals(options._ethernetConfig, ethernetConfig.first, "ethernetConfig")
        assertEquals(options.exposureCompensation, exposureCompensation.first, "exposureCompensation")
        assertEquals(options.exposureDelay, exposureDelay.first, "exposureDelay")
        assertEquals(options.exposureProgram, exposureProgram.first, "exposureProgram")
        assertEquals(options._faceDetect, faceDetect.first, "faceDetect")
        assertEquals(options.fileFormat, fileFormat.first, "fileFormat")
        assertEquals(options._filter, filter.first, "filter")
        assertEquals(options._function, function.first, "function")
        assertEquals(options._gain, gain.first, "gain")
        assertEquals(options.gpsInfo, gpsInfo.first, "gpsInfo")
        assertEquals(options._gpsTagRecording, isGpsOn.first, "isGpsOn")
        assertEquals(options._imageStitching, imageStitching.first, "imageStitching")
        assertEquals(options.iso, iso.first, "iso")
        assertEquals(options.isoAutoHighLimit, isoAutoHighLimit.first, "isoAutoHighLimit")
        assertEquals(options._language, language.first, "language")
        assertEquals(options._latestEnabledExposureDelayTime, latestEnabledExposureDelayTime.first, "latestEnabledExposureDelayTime")
        assertEquals(options._maxRecordableTime, maxRecordableTime.first, "maxRecordableTime")
        assertEquals(options._microphoneNoiseReduction, microphoneNoiseReduction.first, "microphoneNoiseReduction")
        assertEquals(options._mobileNetworkSetting, mobileNetworkSetting.first, "mobileNetworkSetting")
        assertEquals(options._networkType, networkType.first, "networkType")
        assertEquals(options.offDelay, offDelay.first, "offDelay")
        assertEquals(options._offDelayUSB, offDelayUsb.first, "offDelayUsb")
        assertEquals(options._password, password.first, "password")
        assertEquals(options._powerSaving, powerSaving.first, "powerSaving")
        assertEquals(options._preset, preset.first, "preset")
        assertEquals(options.previewFormat, previewFormat.first, "previewFormat")
        assertEquals(options._proxy, proxy.first, "proxy")
        assertEquals(options.remainingPictures, remainingPictures.first, "remainingPictures")
        assertEquals(options.remainingVideoSeconds, remainingVideoSeconds.first, "remainingVideoSeconds")
        assertEquals(options.remainingSpace, remainingSpace.first, "remainingSpace")
        assertEquals(options._shootingMethod, shootingMethod.first, "shootingMethod")
        assertEquals(options.shutterSpeed, shutterSpeed.first, "shutterSpeed")
        assertEquals(options._shutterVolume, shutterVolume.first, "shutterVolume")
        assertEquals(options.sleepDelay, sleepDelay.first, "sleepDelay")
        assertEquals(options._timeShift, timeShift.first, "timeShift")
        assertEquals(options._topBottomCorrection, topBottomCorrection.first, "topBottomCorrection")
        assertEquals(options._topBottomCorrectionRotation, topBottomCorrectionRotation.first, "topBottomCorrectionRotation")
        assertEquals(options.totalSpace, totalSpace.first, "totalSpace")
        assertEquals(options._usbConnection, usbConnection.first, "usbConnection")
        assertEquals(options._username, userName.first, "userName")
        assertEquals(options.videoStitching, videoStitching.first, "videoStitching")
        assertEquals(options._visibilityReduction, visibilityReduction.first, "visibilityReduction")
        assertEquals(options.whiteBalance, whiteBalance.first, "whiteBalance")
        assertEquals(options._wlanAntennaConfig, wlanAntennaConfig.first, "wlanAntennaConfig")
        assertEquals(options._whiteBalanceAutoStrength, whiteBalanceAutoStrength.first, "whiteBalanceAutoStrength")
        assertEquals(options._wlanFrequency, wlanFrequency.first, "wlanFrequency")
        assertEquals(options._wlanFrequencySupport, wlanFrequencySupport.first, "wlanFrequencySupport")
        assertEquals(options._wlanFrequencyCLmode, wlanFrequencyClMode.first, "wlanFrequencyClMode")
    }
}
