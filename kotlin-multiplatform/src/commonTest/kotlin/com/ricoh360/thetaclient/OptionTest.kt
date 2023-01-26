package com.ricoh360.thetaclient

import kotlin.test.*

class OptionTest {
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
    fun getOptionsTest() = runTest {
        val optionNames =
            listOf("_aiAutoThumbnail",
                   "_aiAutoThumbnailSupport",
                   "aperture",
                   "apertureSupport",
                   "_cameraControlSource",
                   "_cameraControlSourceSupport",
                   "clientVersion",
                   "clientVersionSupport",
                   "captureMode",
                   "captureModeSupport",
                   "dateTimeZone",
                   "exposureCompensation",
                   "exposureCompensationSupport",
                   "exposureDelay",
                   "exposureDelaySupport",
                   "exposureProgram",
                   "exposureProgramSupport",
                   "fileFormat",
                   "fileFormatSupport",
                   "_filter",
                   "_filterSupport",
                   "gpsInfo",
                   "_gpsTagRecording",
                   "_imageStitching",
                   "_imageStitchingSupport",
                   "iso",
                   "isoSupport",
                   "isoAutoHighLimit",
                   "isoAutoHighLimitSupport",
                   "_language",
                   "_languageSupport",
                   "_maxRecordableTime",
                   "_maxRecordableTimeSupport",
                   "_microphone",
                   "_microphoneSupport",
                   "_microphoneChannel",
                   "_microphoneChannelSupport",
                   "_networkType",
                   "_networkTypeSupport",
                   "offDelay",
                   "offDelaySupport",
                   "remainingPictures",
                   "previewFormat",
                   "previewFormatSupport",
                   "remainingSpace",
                   "remainingVideoSeconds",
                   "_shootingMethod",
                   "_shootingMethodSupport",
                   "shutterSpeed",
                   "shutterSpeedSupport",
                   "_shutterVolume",
                   "_shutterVolumeSupport",
                   "sleepDelay",
                   "sleepDelaySupport",
                   "_topBottomCorrection",
                   "_topBottomCorrectionSupport",
                   "totalSpace",
                   "videoStitching",
                   "videoStitchingSupport",
                   "_visibilityReduction",
                   "_visibilityReductionSupport",
                   "whiteBalance",
                   "whiteBalanceSupport",
            )
        val params = GetOptionsParams(optionNames = optionNames)
        val response = ThetaApi.callGetOptionsCommand(endpoint, params)
        assertEquals("camera.getOptions", response.name, "name")
        assertEquals(CommandState.DONE, response.state, "state")
        assertNotNull(response.results?.options, "results.options")
        val options = response.results!!.options
        assertEquals(AiAutoThumbnail.OFF, options._aiAutoThumbnail, "_aiAutoThumbnail")
        options._aiAutoThumbnailSupport!!.forEach {
            when (it) {
                AiAutoThumbnail.ON -> {
                    assertTrue(true, "aiAutoThumbnail.ON")
                }
                AiAutoThumbnail.OFF -> {
                    assertTrue(true, "aiAutoThumbnail.OFF")
                }
                else -> {
                    assertTrue (false, "aiAutoThunbmailSupport")
                }
            }
        }
        assertEquals(2.0f, options.aperture, "aperture")
        assertNotNull(options.apertureSupport, "apertureSupport")
        assertEquals(CameraControlSource.CAMERA, options._cameraControlSource, "_cameraControlSource")
        options._cameraControlSourceSupport!!.forEach {
            when (it) {
                CameraControlSource.CAMERA -> {
                    assertTrue(true, "CameraControlSource.CAMEARA")
                }
                CameraControlSource.APP -> {
                    assertTrue(true, "CameraControlSource.APP")
                }
                else -> {
                    assertTrue (false, "CameraControlSource failed")
                }
            }
        }
        assertEquals(1, options.clientVersion, "clientVersion")
        assertEquals(2, options.clientVersionSupport?.size, "clientVersionSupport")
        options.clientVersionSupport!!.forEach {
            when (it) {
                1 -> {
                    assertTrue(true, "clientVersion 1")
                }
                2 -> {
                    assertTrue(true, "clientVersion 2")
                }
                else -> {
                    assertTrue (false, "clientVersion failed")
                }
            }
        }
        assertEquals(CaptureMode.IMAGE, options.captureMode, "captureMode")
        assertEquals(2, options.captureModeSupport?.size, "captureModeSupport")
        options.captureModeSupport!!.forEach {
            when (it) {
                CaptureMode.IMAGE -> {
                    assertTrue(true, "CaptureMode.IMAGE")
                }
                CaptureMode.VIDEO -> {
                    assertTrue(true, "CaptureMode.VIDEO")
                }
                else -> {
                    assertTrue (false, "CaptureMode failed")
                }
            }
        }
        assertEquals("2020:01:01 00:00:00+09:00", options.dateTimeZone, "dateTimeZone")
        assertEquals(0f, options.exposureCompensation, "exposureCompensation")
        assertEquals(13, options.exposureCompensationSupport!!.size, "exposureCompensationSupport")
        assertEquals(0, options.exposureDelay, "exposureDelay")
        assertEquals(1, options.exposureDelaySupport!!.size, "exposureDelaySupport")
        assertEquals(2, options.exposureProgram, "exposureProgram")
        assertEquals(4, options.exposureProgramSupport!!.size, "exposureProgramSupport")
        assertNotNull(options.fileFormat, "fileFormat")
        val fileFormat = options.fileFormat!!
        assertEquals(MediaType.JPEG, fileFormat.type, "fileformat.type")
        assertEquals(5376, fileFormat.width, "fileformat.width")
        assertEquals(2688, fileFormat.height, "fileformat.height")
        assertEquals(2, options.fileFormatSupport!!.size, "fileFormatSupport")
        assertEquals(ImageFilter.OFF, options._filter, "_filter")
        assertEquals(5, options._filterSupport!!.size, "_filterSupport")
        options._filterSupport!!.forEach {
            when (it) {
                ImageFilter.OFF -> {
                    assertTrue(true, "imageFilter.OFF")
                }
                ImageFilter.DR_COMP -> {
                    assertTrue(true, "imageFilter.DR_COMP")
                }
                ImageFilter.NOISE_REDUCTION -> {
                    assertTrue(true, "imageFilter.NOISE_REDUCTION")
                }
                ImageFilter.HDR -> {
                    assertTrue(true, "imageFilter.HDR")
                }
                ImageFilter.HH_HDR -> {
                    assertTrue(true, "imageFilter.HH_HDR")
                }
                else -> {
                    assertTrue(false, "imageFilter failed")
                }
            }
        }
        assertNotNull(options.gpsInfo, "gpsInfo")
        val gps = options.gpsInfo!!
        assertEquals(23.532F, gps.lat, "lat")
        assertEquals(23.532F, gps.lng, "lng")
        assertEquals(999f, gps._altitude, "_altitude")
        assertEquals("2014:05:18 01:04:29+08:00", gps._dateTimeZone, "_dateTimeZone")
        assertEquals("WGS84", gps._datum, "_datum")
        assertEquals(GpsTagRecording.OFF, options._gpsTagRecording, "_gpsTagRecording")
        assertEquals(ImageStitching.AUTO, options._imageStitching, "_imageStitching")
        assertEquals(7, options._imageStitchingSupport!!.size, "_imageStitchingSupport")
        options._imageStitchingSupport!!.forEach {
            when (it) {
                ImageStitching.AUTO -> {
                    assertTrue(true, "imageStitching.AUTO")
                }
                ImageStitching.STATIC -> {
                    assertTrue(true, "imageStitching.STATIC")
                }
                ImageStitching.DYNAMIC_AUTO -> {
                    assertTrue(true, "imageStitching.DYNAMIC_AUTO")
                }
                ImageStitching.DYNAMIC_SEMIAUTO -> {
                    assertTrue(true, "imageStitching.DYNAMIC_SEMIAUTO")
                }
                ImageStitching.DYNAMIC_SAVE -> {
                    assertTrue(true, "imageStitching.DYNAMIC_SAVE")
                }
                ImageStitching.DYNAMIC_LOAD -> {
                    assertTrue(true, "imageStitching.DYNAMIC_LOAD")
                }
                ImageStitching.NONE -> {
                    assertTrue(true, "imageStitching.NONE")
                }
                else -> {
                    assertTrue(false, "imageStitching unknown")
                }
            }
        }
        assertEquals(640, options.iso, "iso")
        assertEquals(18, options.isoSupport!!.size, "isoSupport")
        assertEquals(1600, options.isoAutoHighLimit, "isoAutoHighLimit")
        assertEquals(13, options.isoAutoHighLimitSupport!!.size, "isoAutoHighLimitSupport")
        assertEquals(Language.US, options._language, "_language")
        assertEquals(9, options._languageSupport!!.size, "_languageSupport")
        options._languageSupport!!.forEach {
            when (it) {
                Language.US -> {
                    assertTrue(true, "language.US")
                }
                Language.GB -> {
                    assertTrue(true, "language.GB")
                }
                Language.JA -> {
                    assertTrue(true, "language.JA")
                }
                Language.FR -> {
                    assertTrue(true, "language.FR")
                }
                Language.DE -> {
                    assertTrue(true, "language.DE")
                }
                Language.TW -> {
                    assertTrue(true, "language.TW")
                }
                Language.CN -> {
                    assertTrue(true, "language.CN")
                }
                Language.IT -> {
                    assertTrue(true, "language.IT")
                }
                Language.KO -> {
                    assertTrue(true, "language.KO")
                }
                else -> {
                    assertTrue(false, "language unknown")
                }
            }
        }
        assertEquals(180, options._maxRecordableTime, "_maxRecordableTime")
        assertEquals(1, options._maxRecordableTimeSupport!!.size, "_MaxRecordableTimeSupport")
        assertEquals(MicrophoneOption.AUTO, options._microphone, "_microphone")
        assertEquals(3, options._microphoneSupport!!.size, "_microphoneSupport")
        options._microphoneSupport!!.forEach {
            when (it) {
                MicrophoneOption.AUTO -> {
                    assertTrue(true, "_microphone.AUTO")
                }
                MicrophoneOption.INTERNAL -> {
                    assertTrue(true, "_microphone.INTERNAL")
                }
                MicrophoneOption.EXTERNAL -> {
                    assertTrue(true, "_microphone.EXTERNAL")
                }
                else -> {
                    assertTrue(false, "_microphone unknown")
                }
            }
        }
        assertEquals(MicrophoneChannel.SPATIAL, options._microphoneChannel,
                     "_microphoneChannel")
        assertEquals(2, options._microphoneChannelSupport!!.size,
                     "_microphoneChannelSupport")
        options._microphoneChannelSupport!!.forEach {
            when (it) {
                MicrophoneChannel.SPATIAL -> {
                    assertTrue(true, "microphoneChannel.SPATIAL")
                }
                MicrophoneChannel.MONAURAL -> {
                    assertTrue(true, "microphoneChannel.MONAURAL")
                }
                else -> {
                    assertTrue(false, "microphoneChannel unknown")
                }
            }
        }
        assertEquals(NetworkType.DIRECT, options._networkType, "_networkType")
        assertEquals(2, options._networkTypeSupport!!.size, "_networkTypeSupport")
        options._networkTypeSupport!!.forEach {
            when (it) {
                NetworkType.DIRECT -> {
                    assertTrue(true, "networkType.DIRECT")
                }
                NetworkType.CLIENT -> {
                    assertTrue(true, "networkType.CLIENT")
                }
                else -> {
                    assertTrue(false, "networkType unknown")
                }
            }
        }
        assertEquals(1000, options.offDelay, "offDelay")
        assertEquals(4, options.offDelaySupport!!.size, "offDelaySupport")
        assertEquals(1378, options.remainingPictures, "remainingPictures")
        assertNotNull(options.previewFormat, "previewFormat")
        val previewFormat = options.previewFormat!!
        assertEquals(0, previewFormat.width, "previewFormat.width")
        assertEquals(0, previewFormat.height, "previewFormat.height")
        assertEquals(0, previewFormat.framerate, "previewFormat.framerate")
        assertEquals(1, options.previewFormatSupport!!.size, "previewFormatSupport")
        assertEquals(6864153600, options.remainingSpace, "remainingSpace")
        assertEquals(2182, options.remainingVideoSeconds, "remainingVideoSeconds")
        assertEquals(ShootingMethod.NORMAL, options._shootingMethod, "_shootingMethod")
        assertEquals(9, options._shootingMethodSupport!!.size, "_shootingMethodSupport")
        options._shootingMethodSupport!!.forEach {
            when (it) {
                ShootingMethod.NORMAL -> {
                    assertTrue(true, "shootingMethod.NORMAL")
                }
                ShootingMethod.INTERVAL -> {
                    assertTrue(true, "shootingMethod.INTERVAL")
                }
                ShootingMethod.MOVE_INTERVAL -> {
                    assertTrue(true, "shootingMethod.MOVE_INTERVAL")
                }
                ShootingMethod.FIXED_INTERVAL -> {
                    assertTrue(true, "shootingMethod.FIXED_INTERVAL")
                }
                ShootingMethod.BRACKET -> {
                    assertTrue(true, "shootingMethod.BRACKET")
                }
                ShootingMethod.COMPOSITE -> {
                    assertTrue(true, "shootingMethod.COMPOSITE")
                }
                ShootingMethod.CONTINUOUS -> {
                    assertTrue(true, "shootingMethod.CONTINUOUS")
                }
                ShootingMethod.TIMESHIFT -> {
                    assertTrue(true, "shootingMethod.TIMESHIFT")
                }
                ShootingMethod.BURST -> {
                    assertTrue(true, "shootingMethod.BURST")
                }
                else -> {
                    assertTrue(false, "shootingMethod unknown")
                }
            }
        }
        assertEquals(0.0, options.shutterSpeed, "shutterSpeed")
        assertEquals(1, options.shutterSpeedSupport!!.size, "shutterSpeedSupport")
        assertEquals(100, options._shutterVolume, "_shutterVolume")
        assertNotNull(options._shutterVolumeSupport, "_shutterVolumeSupport")
        val shutterVolume = options._shutterVolumeSupport!!
        assertEquals(0, shutterVolume.minShutterVolume, "minShutterVolume")
        assertEquals(100, shutterVolume.maxShutterVolume, "maxShutterVolume")
        assertEquals(1000, options.sleepDelay, "sleepDelay")
        assertEquals(4, options.sleepDelaySupport!!.size, "sleepDelaySupport")
        assertEquals(TopBottomCorrectionOption.APPLY, options._topBottomCorrection,
                     "_topBottomCorrection")
        assertEquals(7, options._topBottomCorrectionSupport!!.size,
                     "_topBottomCorrectionSupport")
        options._topBottomCorrectionSupport!!.forEach {
            when (it) {
                TopBottomCorrectionOption.APPLY -> {
                    assertTrue(true, "topBottomCorrection.APPLY")
                }
                TopBottomCorrectionOption.APPLY_AUTO -> {
                    assertTrue(true, "topBottomCorrection.APPLY_AUTO")
                }
                TopBottomCorrectionOption.APPLY_SEMIAUTO -> {
                    assertTrue(true, "topBottomCorrection.APPLY_SEMIAUTO")
                }
                TopBottomCorrectionOption.APPLY_SAVE -> {
                    assertTrue(true, "topBottomCorrection.APPLY_SAVE")
                }
                TopBottomCorrectionOption.APPLY_LOAD -> {
                    assertTrue(true, "topBottomCorrection.APPLY_LOAD")
                }
                TopBottomCorrectionOption.DISAPPLY -> {
                    assertTrue(true, "topBottomCorrection.DISAPPLY")
                }
                TopBottomCorrectionOption.MANUAL -> {
                    assertTrue(true, "topBottomCorrection.MANUAL")
                }
                else -> {
                    assertTrue(false, "topBottomCorrection unknown")
                }
            }
        }
        assertEquals(8060403712, options.totalSpace, "totalSpace")
        assertEquals(VideoStitching.NONE, options.videoStitching, "videoStitching")
        assertEquals(2, options.videoStitchingSupport!!.size, "videoStitchingSupport")
        options.videoStitchingSupport!!.forEach {
            when (it) {
                VideoStitching.NONE -> {
                    assertTrue(true, "videoStitching.NONE")
                }
                VideoStitching.ONDEVICE -> {
                    assertTrue(true, "videoStitching.ONDEVICE")
                }
                else -> {
                    assertTrue(true, "videoStitching unknown")
                }
            }
        }
        assertEquals(VisibilityReduction.ON, options._visibilityReduction, "_visibilityRecution")
        assertEquals(2, options._visibilityReductionSupport!!.size,
                     "_visibilityReductionSupport")
        options._visibilityReductionSupport!!.forEach {
            when (it) {
                VisibilityReduction.ON -> {
                    assertTrue(true, "visibilityReduction.ON")
                }
                VisibilityReduction.OFF -> {
                    assertTrue(true, "visibilityReduction.OFF")
                }
                else -> {
                    assertTrue(true, "visibilityReduction unknown")
                }
            }
        }
        assertEquals(WhiteBalance.AUTO, options.whiteBalance, "whiteBalance")
        assertEquals(12, options.whiteBalanceSupport!!.size, "whiteBalanceSupport")
        options.whiteBalanceSupport!!.forEach {
            when (it) {
                WhiteBalance.AUTO -> {
                    assertTrue(true, "whiteBalance.AUTO")
                }
                WhiteBalance.DAYLIGHT -> {
                    assertTrue(true, "whiteBalance.DAYLIGHT")
                }
                WhiteBalance.SHADE -> {
                    assertTrue(true, "whiteBalance.SHADE")
                }
                WhiteBalance.CLOUDY_DAYLIGHT -> {
                    assertTrue(true, "whiteBalance.CLOUDY_DAYLIGHT")
                }
                WhiteBalance.INCANDESCENT -> {
                    assertTrue(true, "whiteBalance.INCANDESCENT")
                }
                WhiteBalance._WARM_WHITE_FLUORESCENT -> {
                    assertTrue(true, "whiteBalance._WARM_WHITE_FLUORESCENT")
                }
                WhiteBalance._DAYLIGHT_FLUORESCENT -> {
                    assertTrue(true, "whiteBalance._DAYLIGHT_FLUORESCENT")
                }
                WhiteBalance._DAYWHITE_FLUORESCENT -> {
                    assertTrue(true, "whiteBalance._DAYWHITE_FLUORESCENT")
                }
                WhiteBalance.FLUORESCENT -> {
                    assertTrue(true, "whiteBalance.FLUORESCENT")
                }
                WhiteBalance._BULB_FLUORESCENT -> {
                    assertTrue(true, "whiteBalance._BULB_FLUORESCENT")
                }
                WhiteBalance._COLOR_TEMPERATURE -> {
                    assertTrue(true, "whiteBalance._COLOR_TEMPERATURE")
                }
                WhiteBalance._UNDERWATER -> {
                    assertTrue(true, "whiteBalance._UNDERWATER")
                }
                else -> {
                    assertTrue(true, "whiteBalance unknown")
                }
            }
        }
    }

    @kotlinx.coroutines.ExperimentalCoroutinesApi
    @Test
    fun setOptionsTest() = runTest {
        val optionNames =
            listOf("_aiAutoThumbnail",
                   "aperture",
                   "_cameraControlSource",
                   "captureMode",
                   "dateTimeZone",
                   "exposureCompensation",
                   "exposureDelay",
                   "exposureProgram",
                   "fileFormat",
                   "_filter",
                   "gpsInfo",
                   "_gpsTagRecording",
                   "_imageStitching",
                   "iso",
                   "isoAutoHighLimit",
                   "_language",
                   "_maxRecordableTime",
                   "_microphone",
                   "_microphoneChannel",
                   "_networkType",
                   "offDelay",
                   "remainingPictures",
                   "previewFormat",
                   "remainingSpace",
                   "remainingVideoSeconds",
                   "_shootingMethod",
                   "shutterSpeed",
                   "_shutterVolume",
                   "sleepDelay",
                   "_topBottomCorrection",
                   "totalSpace",
                   "videoStitching",
                   "_visibilityReduction",
                   "whiteBalance",
            )
        val params = GetOptionsParams(optionNames = optionNames)
        val response = ThetaApi.callGetOptionsCommand(endpoint, params)
        val params2 = SetOptionsParams(sessionId = "12345",
                                       options = response.results!!.options)
        val response2 = ThetaApi.callSetOptionsCommand(endpoint, params2)
        assertEquals("camera.setOptions", response2.name, "name")
        assertEquals(CommandState.DONE, response2.state, "state")
    }

    @kotlinx.coroutines.ExperimentalCoroutinesApi
    @Test
    fun optionsCopyTest() = runTest {
        val opt1 = Options(iso = 100)
        val opt2 = opt1.copy(isoAutoHighLimit = 200)
        assertEquals(opt1.iso, opt2.iso, "iso copyed")
        assertNotEquals(opt1.isoAutoHighLimit, opt2.isoAutoHighLimit, "isoAutoHighLimit")
    }
*/
}
