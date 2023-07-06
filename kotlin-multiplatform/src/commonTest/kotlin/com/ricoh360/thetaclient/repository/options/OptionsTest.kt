package com.ricoh360.thetaclient.repository.options

import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.*
import kotlin.test.*

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
        val aiAutoThumbnail = ThetaRepository.AiAutoThumbnailEnum.ON
        val aperture = ThetaRepository.ApertureEnum.APERTURE_2_1
        val bluetoothPower = ThetaRepository.BluetoothPowerEnum.ON
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
        val cameraMode = ThetaRepository.CameraModeEnum.CAPTURE
        val captureMode = ThetaRepository.CaptureModeEnum.IMAGE
        val colorTemperature = 10
        val dateTimeZone = "2014:05:18 01:04:29+08:00"
        val exposureCompensation = ThetaRepository.ExposureCompensationEnum.M0_3
        val exposureDelay = ThetaRepository.ExposureDelayEnum.DELAY_10
        val exposureProgram = ThetaRepository.ExposureProgramEnum.NORMAL_PROGRAM
        val fileFormat = ThetaRepository.FileFormatEnum.IMAGE_11K
        val filter = ThetaRepository.FilterEnum.HDR
        val gpsInfo = ThetaRepository.GpsInfo.disabled
        val isGpsOn = true
        val iso = ThetaRepository.IsoEnum.ISO_125
        val isoAutoHighLimit = ThetaRepository.IsoAutoHighLimitEnum.ISO_1000
        val language = ThetaRepository.LanguageEnum.JA
        val maxRecordableTime = ThetaRepository.MaxRecordableTimeEnum.RECORDABLE_TIME_1500
        val networkType = ThetaRepository.NetworkTypeEnum.DIRECT
        val offDelay = ThetaRepository.OffDelayEnum.OFF_DELAY_10M
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
        val totalSpace = 100L
        val username = "username"
        val whiteBalance = ThetaRepository.WhiteBalanceEnum.WARM_WHITE_FLUORESCENT
        val whiteBalanceAutoStrength = ThetaRepository.WhiteBalanceAutoStrengthEnum.OFF
        val wlanFrequency = ThetaRepository.WlanFrequencyEnum.GHZ_2_4

        val options = ThetaRepository.Options(
            aiAutoThumbnail = aiAutoThumbnail,
            aperture = aperture,
            bluetoothPower = bluetoothPower,
            burstMode = burstMode,
            burstOption = burstOption,
            cameraControlSource = cameraControlSource,
            cameraMode = cameraMode,
            captureMode = captureMode,
            colorTemperature = colorTemperature,
            dateTimeZone = dateTimeZone,
            exposureCompensation = exposureCompensation,
            exposureDelay = exposureDelay,
            exposureProgram = exposureProgram,
            fileFormat = fileFormat,
            filter = filter,
            gpsInfo = gpsInfo,
            isGpsOn = isGpsOn,
            iso = iso,
            isoAutoHighLimit = isoAutoHighLimit,
            language = language,
            maxRecordableTime = maxRecordableTime,
            networkType = networkType,
            offDelay = offDelay,
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
            totalSpace = totalSpace,
            username = username,
            whiteBalance = whiteBalance,
            whiteBalanceAutoStrength = whiteBalanceAutoStrength,
            wlanFrequency = wlanFrequency,
        )

        ThetaRepository.OptionNameEnum.values().forEach {
            assertNotNull(options.getValue(it), "option: ${it.value}")
        }

        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.AiAutoThumbnail), aiAutoThumbnail, "aiAutoThumbnail")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.Aperture), aperture, "aperture")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.BluetoothPower), bluetoothPower, "bluetoothPower")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.BurstMode), burstMode, "burstMode")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.BurstOption), burstOption, "burstOption")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.CameraControlSource), cameraControlSource, "cameraControlSource")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.CameraMode), cameraMode, "cameraMode")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.CaptureMode), captureMode, "captureMode")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.ColorTemperature), colorTemperature, "colorTemperature")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.DateTimeZone), dateTimeZone, "dateTimeZone")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.ExposureCompensation), exposureCompensation, "exposureCompensation")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.ExposureDelay), exposureDelay, "exposureDelay")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.ExposureProgram), exposureProgram, "exposureProgram")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.FileFormat), fileFormat, "fileFormat")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.Filter), filter, "filter")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.GpsInfo), gpsInfo, "gpsInfo")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.IsGpsOn), isGpsOn, "isGpsOn")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.Iso), iso, "iso")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.IsoAutoHighLimit), isoAutoHighLimit, "isoAutoHighLimit")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.Language), language, "language")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.MaxRecordableTime), maxRecordableTime, "maxRecordableTime")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.NetworkType), networkType, "networkType")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.OffDelay), offDelay, "offDelay")
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
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.TotalSpace), totalSpace, "totalSpace")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.Username), username, "userName")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.WhiteBalance), whiteBalance, "whiteBalance")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.WhiteBalanceAutoStrength), whiteBalanceAutoStrength, "whiteBalanceAutoStrength")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.WlanFrequency), wlanFrequency, "wlanFrequency")
    }

    /**
     * Options value set get.
     */
    @Test
    fun optionsSetValueTest() {
        val values = listOf(
            Pair(ThetaRepository.OptionNameEnum.AiAutoThumbnail, ThetaRepository.AiAutoThumbnailEnum.OFF),
            Pair(ThetaRepository.OptionNameEnum.Aperture, ThetaRepository.ApertureEnum.APERTURE_2_1),
            Pair(ThetaRepository.OptionNameEnum.BluetoothPower, ThetaRepository.BluetoothPowerEnum.ON),
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
            Pair(ThetaRepository.OptionNameEnum.CameraMode, ThetaRepository.CameraModeEnum.CAPTURE),
            Pair(ThetaRepository.OptionNameEnum.CaptureMode, ThetaRepository.CaptureModeEnum.IMAGE),
            Pair(ThetaRepository.OptionNameEnum.ColorTemperature, 10),
            Pair(ThetaRepository.OptionNameEnum.DateTimeZone, "2014:05:18 01:04:29+08:00"),
            Pair(ThetaRepository.OptionNameEnum.ExposureCompensation, ThetaRepository.ExposureCompensationEnum.M0_3),
            Pair(ThetaRepository.OptionNameEnum.ExposureDelay, ThetaRepository.ExposureDelayEnum.DELAY_10),
            Pair(ThetaRepository.OptionNameEnum.ExposureProgram, ThetaRepository.ExposureProgramEnum.NORMAL_PROGRAM),
            Pair(ThetaRepository.OptionNameEnum.FileFormat, ThetaRepository.FileFormatEnum.IMAGE_11K),
            Pair(ThetaRepository.OptionNameEnum.Filter, ThetaRepository.FilterEnum.HDR),
            Pair(ThetaRepository.OptionNameEnum.GpsInfo, ThetaRepository.GpsInfo.disabled),
            Pair(ThetaRepository.OptionNameEnum.IsGpsOn, true),
            Pair(ThetaRepository.OptionNameEnum.Iso, ThetaRepository.IsoEnum.ISO_125),
            Pair(ThetaRepository.OptionNameEnum.IsoAutoHighLimit, ThetaRepository.IsoAutoHighLimitEnum.ISO_1000),
            Pair(ThetaRepository.OptionNameEnum.Language, ThetaRepository.LanguageEnum.JA),
            Pair(ThetaRepository.OptionNameEnum.MaxRecordableTime, ThetaRepository.MaxRecordableTimeEnum.RECORDABLE_TIME_1500),
            Pair(ThetaRepository.OptionNameEnum.NetworkType, ThetaRepository.NetworkTypeEnum.ETHERNET),
            Pair(ThetaRepository.OptionNameEnum.OffDelay, ThetaRepository.OffDelayEnum.OFF_DELAY_10M),
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
            Pair(ThetaRepository.OptionNameEnum.TotalSpace, 104L),
            Pair(ThetaRepository.OptionNameEnum.Username, "username"),
            Pair(ThetaRepository.OptionNameEnum.WhiteBalance, ThetaRepository.WhiteBalanceEnum.WARM_WHITE_FLUORESCENT),
            Pair(ThetaRepository.OptionNameEnum.WhiteBalanceAutoStrength, ThetaRepository.WhiteBalanceAutoStrengthEnum.ON),
            Pair(ThetaRepository.OptionNameEnum.WlanFrequency, ThetaRepository.WlanFrequencyEnum.GHZ_5),
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
        val aiAutoThumbnail = Pair(AiAutoThumbnail.OFF, ThetaRepository.AiAutoThumbnailEnum.OFF)
        val aperture = Pair(2.1f, ThetaRepository.ApertureEnum.APERTURE_2_1)
        val bluetoothPower = Pair(BluetoothPower.ON, ThetaRepository.BluetoothPowerEnum.ON)
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
        val cameraMode = Pair(CameraMode.CAPTURE, ThetaRepository.CameraModeEnum.CAPTURE)
        val captureMode = Pair(CaptureMode.IMAGE, ThetaRepository.CaptureModeEnum.IMAGE)
        val colorTemperature = Pair(10, 10)
        val dateTimeZone = Pair("2014:05:18 01:04:29+08:00", "2014:05:18 01:04:29+08:00")
        val exposureCompensation = Pair(-0.3f, ThetaRepository.ExposureCompensationEnum.M0_3)
        val exposureDelay = Pair(10, ThetaRepository.ExposureDelayEnum.DELAY_10)
        val exposureProgram = Pair(2, ThetaRepository.ExposureProgramEnum.NORMAL_PROGRAM)
        val fileFormat = Pair(
            MediaFileFormat(MediaType.JPEG, 11008, 5504, null, null),
            ThetaRepository.FileFormatEnum.IMAGE_11K
        )
        val filter = Pair(ImageFilter.HDR, ThetaRepository.FilterEnum.HDR)
        val gpsInfo = Pair(GpsInfo(65535.0f, 65535.0f, 0f, null, null), ThetaRepository.GpsInfo.disabled)
        val isGpsOn = Pair(GpsTagRecording.ON, true)
        val iso = Pair(125, ThetaRepository.IsoEnum.ISO_125)
        val isoAutoHighLimit = Pair(1000, ThetaRepository.IsoAutoHighLimitEnum.ISO_1000)
        val language = Pair(Language.JA, ThetaRepository.LanguageEnum.JA)
        val maxRecordableTime = Pair(1500, ThetaRepository.MaxRecordableTimeEnum.RECORDABLE_TIME_1500)
        val networkType = Pair(NetworkType.DIRECT, ThetaRepository.NetworkTypeEnum.DIRECT)
        val offDelay = Pair(600, ThetaRepository.OffDelayEnum.OFF_DELAY_10M)
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
        val totalSpace = Pair(104L, 104L)
        val username = Pair("username", "username")
        val whiteBalance = Pair(WhiteBalance._WARM_WHITE_FLUORESCENT, ThetaRepository.WhiteBalanceEnum.WARM_WHITE_FLUORESCENT)
        val whiteBalanceAutoStrength = Pair(WhiteBalanceAutoStrength.OFF, ThetaRepository.WhiteBalanceAutoStrengthEnum.OFF)

        val orgOptions = Options(
            _aiAutoThumbnail = aiAutoThumbnail.first,
            aperture = aperture.first,
            _bluetoothPower = bluetoothPower.first,
            _burstMode = burstMode.first,
            _burstOption = burstOption.first,
            _cameraControlSource = cameraControlSource.first,
            _cameraMode = cameraMode.first,
            captureMode = captureMode.first,
            _colorTemperature = colorTemperature.first,
            dateTimeZone = dateTimeZone.first,
            exposureCompensation = exposureCompensation.first,
            exposureDelay = exposureDelay.first,
            exposureProgram = exposureProgram.first,
            fileFormat = fileFormat.first,
            _filter = filter.first,
            gpsInfo = gpsInfo.first,
            _gpsTagRecording = isGpsOn.first,
            iso = iso.first,
            isoAutoHighLimit = isoAutoHighLimit.first,
            _language = language.first,
            _maxRecordableTime = maxRecordableTime.first,
            _networkType = networkType.first,
            offDelay = offDelay.first,
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
            totalSpace = totalSpace.first,
            _username = username.first,
            whiteBalance = whiteBalance.first,
            _whiteBalanceAutoStrength = whiteBalanceAutoStrength.first
        )
        val options = ThetaRepository.Options(orgOptions)

        assertEquals(options.aiAutoThumbnail, aiAutoThumbnail.second, "aiAutoThumbnail")
        assertEquals(options.aperture, aperture.second, "aperture")
        assertEquals(options.bluetoothPower, bluetoothPower.second, "bluetoothPower")
        assertEquals(options.burstMode, burstMode.second, "burstMode")
        assertEquals(options.burstOption, burstOption.second, "burstOption")
        assertEquals(options.cameraControlSource, cameraControlSource.second, "cameraControlSource")
        assertEquals(options.cameraMode, cameraMode.second, "cameraMode")
        assertEquals(options.captureMode, captureMode.second, "captureMode")
        assertEquals(options.colorTemperature, colorTemperature.second, "colorTemperature")
        assertEquals(options.dateTimeZone, dateTimeZone.second, "dateTimeZone")
        assertEquals(options.exposureCompensation, exposureCompensation.second, "exposureCompensation")
        assertEquals(options.exposureDelay, exposureDelay.second, "exposureDelay")
        assertEquals(options.exposureProgram, exposureProgram.second, "exposureProgram")
        assertEquals(options.fileFormat, fileFormat.second, "fileFormat")
        assertEquals(options.filter, filter.second, "filter")
        assertEquals(options.gpsInfo, gpsInfo.second, "gpsInfo")
        assertEquals(options.isGpsOn, isGpsOn.second, "isGpsOn")
        assertEquals(options.iso, iso.second, "iso")
        assertEquals(options.isoAutoHighLimit, isoAutoHighLimit.second, "isoAutoHighLimit")
        assertEquals(options.language, language.second, "language")
        assertEquals(options.maxRecordableTime, maxRecordableTime.second, "aperture")
        assertEquals(options.networkType, networkType.second, "networkType")
        assertEquals(options.offDelay, offDelay.second, "offDelay")
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
        assertEquals(options.totalSpace, totalSpace.second, "totalSpace")
        assertEquals(options.username, username.second, "username")
        assertEquals(options.whiteBalance, whiteBalance.second, "whiteBalance")
        assertEquals(options.whiteBalanceAutoStrength, whiteBalanceAutoStrength.second, "whiteBalanceAutoStrength")
    }

    @Test
    fun optionsConvertTest() {
        val aiAutoThumbnail = Pair(AiAutoThumbnail.ON, ThetaRepository.AiAutoThumbnailEnum.ON)
        val aperture = Pair(2.1f, ThetaRepository.ApertureEnum.APERTURE_2_1)
        val bluetoothPower = Pair(BluetoothPower.ON, ThetaRepository.BluetoothPowerEnum.ON)
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
        val cameraMode = Pair(CameraMode.CAPTURE, ThetaRepository.CameraModeEnum.CAPTURE)
        val captureMode = Pair(CaptureMode.IMAGE, ThetaRepository.CaptureModeEnum.IMAGE)
        val colorTemperature = Pair(10, 10)
        val dateTimeZone = Pair("2014:05:18 01:04:29+08:00", "2014:05:18 01:04:29+08:00")
        val exposureCompensation = Pair(-0.3f, ThetaRepository.ExposureCompensationEnum.M0_3)
        val exposureDelay = Pair(10, ThetaRepository.ExposureDelayEnum.DELAY_10)
        val exposureProgram = Pair(2, ThetaRepository.ExposureProgramEnum.NORMAL_PROGRAM)
        val fileFormat = Pair(
            MediaFileFormat(MediaType.JPEG, 11008, 5504, null, null),
            ThetaRepository.FileFormatEnum.IMAGE_11K
        )
        val filter = Pair(ImageFilter.HDR, ThetaRepository.FilterEnum.HDR)
        val gpsInfo = Pair(
            GpsInfo(65535.0f, 65535.0f, 0f, "", ""),
            ThetaRepository.GpsInfo(65535.0f, 65535.0f, 0f, "")
        )
        val isGpsOn = Pair(GpsTagRecording.ON, true)
        val iso = Pair(125, ThetaRepository.IsoEnum.ISO_125)
        val isoAutoHighLimit = Pair(1000, ThetaRepository.IsoAutoHighLimitEnum.ISO_1000)
        val language = Pair(Language.JA, ThetaRepository.LanguageEnum.JA)
        val maxRecordableTime = Pair(1500, ThetaRepository.MaxRecordableTimeEnum.RECORDABLE_TIME_1500)
        val networkType = Pair(NetworkType.ETHERNET, ThetaRepository.NetworkTypeEnum.ETHERNET)
        val offDelay = Pair(600, ThetaRepository.OffDelayEnum.OFF_DELAY_10M)
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
        val totalSpace = Pair(104L, 104L)
        val userName = Pair("username", "username")
        val whiteBalance = Pair(WhiteBalance._WARM_WHITE_FLUORESCENT, ThetaRepository.WhiteBalanceEnum.WARM_WHITE_FLUORESCENT)
        val whiteBalanceAutoStrength = Pair(WhiteBalanceAutoStrength.ON, ThetaRepository.WhiteBalanceAutoStrengthEnum.ON)

        val orgOptions = ThetaRepository.Options(
            aiAutoThumbnail = aiAutoThumbnail.second,
            aperture = aperture.second,
            bluetoothPower = bluetoothPower.second,
            burstMode = burstMode.second,
            burstOption = burstOption.second,
            cameraControlSource = cameraControlSource.second,
            cameraMode = cameraMode.second,
            captureMode = captureMode.second,
            colorTemperature = colorTemperature.second,
            dateTimeZone = dateTimeZone.second,
            exposureCompensation = exposureCompensation.second,
            exposureDelay = exposureDelay.second,
            exposureProgram = exposureProgram.second,
            fileFormat = fileFormat.second,
            filter = filter.second,
            gpsInfo = gpsInfo.second,
            isGpsOn = isGpsOn.second,
            iso = iso.second,
            isoAutoHighLimit = isoAutoHighLimit.second,
            language = language.second,
            maxRecordableTime = maxRecordableTime.second,
            networkType = networkType.second,
            offDelay = offDelay.second,
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
            totalSpace = totalSpace.second,
            username = userName.second,
            whiteBalance = whiteBalance.second,
            whiteBalanceAutoStrength = whiteBalanceAutoStrength.second
        )
        val options = orgOptions.toOptions()

        assertEquals(options._aiAutoThumbnail, aiAutoThumbnail.first, "aiAutoThumbnail")
        assertEquals(options.aperture, aperture.first, "aperture")
        assertEquals(options._bluetoothPower, bluetoothPower.first, "bluetoothPower")
        assertEquals(options._burstMode, burstMode.first, "burstMode")
        assertEquals(options._burstOption, burstOption.first, "burstOption")
        assertEquals(options._cameraControlSource, cameraControlSource.first, "cameraControlSource")
        assertEquals(options._cameraMode, cameraMode.first, "cameraMode")
        assertEquals(options.captureMode, captureMode.first, "captureMode")
        assertEquals(options._colorTemperature, colorTemperature.first, "colorTemperature")
        assertEquals(options.dateTimeZone, dateTimeZone.first, "dateTimeZone")
        assertEquals(options.exposureCompensation, exposureCompensation.first, "exposureCompensation")
        assertEquals(options.exposureDelay, exposureDelay.first, "exposureDelay")
        assertEquals(options.exposureProgram, exposureProgram.first, "exposureProgram")
        assertEquals(options.fileFormat, fileFormat.first, "fileFormat")
        assertEquals(options._filter, filter.first, "filter")
        assertEquals(options.gpsInfo, gpsInfo.first, "gpsInfo")
        assertEquals(options._gpsTagRecording, isGpsOn.first, "isGpsOn")
        assertEquals(options.iso, iso.first, "iso")
        assertEquals(options.isoAutoHighLimit, isoAutoHighLimit.first, "isoAutoHighLimit")
        assertEquals(options._language, language.first, "language")
        assertEquals(options._maxRecordableTime, maxRecordableTime.first, "aperture")
        assertEquals(options._networkType, networkType.first, "networkType")
        assertEquals(options.offDelay, offDelay.first, "offDelay")
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
        assertEquals(options.totalSpace, totalSpace.first, "totalSpace")
        assertEquals(options._username, userName.first, "userName")
        assertEquals(options.whiteBalance, whiteBalance.first, "whiteBalance")
        assertEquals(options._whiteBalanceAutoStrength, whiteBalanceAutoStrength.first, "whiteBalanceAutoStrength")
    }
}
