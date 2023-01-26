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
        val aperture = ThetaRepository.ApertureEnum.APERTURE_2_1
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
        val offDelay = ThetaRepository.OffDelayEnum.OFF_DELAY_10M
        val sleepDelay = ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M
        val remainingPictures = 100
        val remainingVideoSeconds = 100
        val remainingSpace = 100L
        val totalSpace = 100L
        val shutterVolume = 100
        val whiteBalance = ThetaRepository.WhiteBalanceEnum.WARM_WHITE_FLUORESCENT

        val options = ThetaRepository.Options(
            aperture = aperture,
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
            offDelay = offDelay,
            sleepDelay = sleepDelay,
            remainingPictures = remainingPictures,
            remainingVideoSeconds = remainingVideoSeconds,
            remainingSpace = remainingSpace,
            totalSpace = totalSpace,
            shutterVolume = shutterVolume,
            whiteBalance = whiteBalance
        )

        ThetaRepository.OptionNameEnum.values().forEach {
            assertNotNull(options.getValue(it), "option: ${it.value}")
        }

        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.Aperture), aperture, "aperture")
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
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.OffDelay), offDelay, "offDelay")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.SleepDelay), sleepDelay, "sleepDelay")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.RemainingPictures), remainingPictures, "remainingPictures")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.RemainingVideoSeconds), remainingVideoSeconds, "remainingVideoSeconds")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.RemainingSpace), remainingSpace, "remainingSpace")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.TotalSpace), totalSpace, "totalSpace")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.ShutterVolume), shutterVolume, "shutterVolume")
        assertEquals(options.getValue(ThetaRepository.OptionNameEnum.WhiteBalance), whiteBalance, "whiteBalance")
    }

    /**
     * Options value set get.
     */
    @Test
    fun optionsSetValueTest() {
        val values = listOf(
            Pair(ThetaRepository.OptionNameEnum.Aperture, ThetaRepository.ApertureEnum.APERTURE_2_1),
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
            Pair(ThetaRepository.OptionNameEnum.OffDelay, ThetaRepository.OffDelayEnum.OFF_DELAY_10M),
            Pair(ThetaRepository.OptionNameEnum.SleepDelay, ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M),
            Pair(ThetaRepository.OptionNameEnum.RemainingPictures, 101),
            Pair(ThetaRepository.OptionNameEnum.RemainingVideoSeconds, 102),
            Pair(ThetaRepository.OptionNameEnum.RemainingSpace, 103L),
            Pair(ThetaRepository.OptionNameEnum.TotalSpace, 104L),
            Pair(ThetaRepository.OptionNameEnum.ShutterVolume, 10),
            Pair(ThetaRepository.OptionNameEnum.WhiteBalance, ThetaRepository.WhiteBalanceEnum.WARM_WHITE_FLUORESCENT)
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
        val aperture = Pair(2.1f, ThetaRepository.ApertureEnum.APERTURE_2_1)
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
        val offDelay = Pair(600, ThetaRepository.OffDelayEnum.OFF_DELAY_10M)
        val sleepDelay = Pair(180, ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M)
        val remainingPictures = Pair(101, 101)
        val remainingVideoSeconds = Pair(102, 102)
        val remainingSpace = Pair(103L, 103L)
        val totalSpace = Pair(104L, 104L)
        val shutterVolume = Pair(10, 10)
        val whiteBalance = Pair(WhiteBalance._WARM_WHITE_FLUORESCENT, ThetaRepository.WhiteBalanceEnum.WARM_WHITE_FLUORESCENT)

        val orgOptions = Options(
            aperture = aperture.first,
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
            offDelay = offDelay.first,
            sleepDelay = sleepDelay.first,
            remainingPictures = remainingPictures.first,
            remainingVideoSeconds = remainingVideoSeconds.first,
            remainingSpace = remainingSpace.first,
            totalSpace = totalSpace.first,
            _shutterVolume = shutterVolume.first,
            whiteBalance = whiteBalance.first
        )
        val options = ThetaRepository.Options(orgOptions)

        assertEquals(options.aperture, aperture.second, "aperture")
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
        assertEquals(options.offDelay, offDelay.second, "offDelay")
        assertEquals(options.sleepDelay, sleepDelay.second, "sleepDelay")
        assertEquals(options.remainingPictures, remainingPictures.second, "remainingPictures")
        assertEquals(options.remainingVideoSeconds, remainingVideoSeconds.second, "remainingVideoSeconds")
        assertEquals(options.remainingSpace, remainingSpace.second, "remainingSpace")
        assertEquals(options.totalSpace, totalSpace.second, "totalSpace")
        assertEquals(options.shutterVolume, shutterVolume.second, "shutterVolume")
        assertEquals(options.whiteBalance, whiteBalance.second, "whiteBalance")
    }

    @Test
    fun optionsConvertTest() {
        val aperture = Pair(2.1f, ThetaRepository.ApertureEnum.APERTURE_2_1)
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
        val offDelay = Pair(600, ThetaRepository.OffDelayEnum.OFF_DELAY_10M)
        val sleepDelay = Pair(180, ThetaRepository.SleepDelayEnum.SLEEP_DELAY_3M)
        val remainingPictures = Pair(101, 101)
        val remainingVideoSeconds = Pair(102, 102)
        val remainingSpace = Pair(103L, 103L)
        val totalSpace = Pair(104L, 104L)
        val shutterVolume = Pair(10, 10)
        val whiteBalance = Pair(WhiteBalance._WARM_WHITE_FLUORESCENT, ThetaRepository.WhiteBalanceEnum.WARM_WHITE_FLUORESCENT)

        val orgOptions = ThetaRepository.Options(
            aperture = aperture.second,
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
            offDelay = offDelay.second,
            sleepDelay = sleepDelay.second,
            remainingPictures = remainingPictures.second,
            remainingVideoSeconds = remainingVideoSeconds.second,
            remainingSpace = remainingSpace.second,
            totalSpace = totalSpace.second,
            shutterVolume = shutterVolume.second,
            whiteBalance = whiteBalance.second
        )
        val options = orgOptions.toOptions()

        assertEquals(options.aperture, aperture.first, "aperture")
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
        assertEquals(options.offDelay, offDelay.first, "offDelay")
        assertEquals(options.sleepDelay, sleepDelay.first, "sleepDelay")
        assertEquals(options.remainingPictures, remainingPictures.first, "remainingPictures")
        assertEquals(options.remainingVideoSeconds, remainingVideoSeconds.first, "remainingVideoSeconds")
        assertEquals(options.remainingSpace, remainingSpace.first, "remainingSpace")
        assertEquals(options.totalSpace, totalSpace.first, "totalSpace")
        assertEquals(options._shutterVolume, shutterVolume.first, "shutterVolume")
        assertEquals(options.whiteBalance, whiteBalance.first, "whiteBalance")
    }
}
