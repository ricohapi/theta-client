package com.ricoh360.thetaclientreactnative

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType
import com.facebook.react.bridge.WritableMap
import com.ricoh360.thetaclient.DigestAuth
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.capture.PhotoCapture
import com.ricoh360.thetaclient.capture.TimeShiftCapture
import com.ricoh360.thetaclient.capture.VideoCapture

const val KEY_NOTIFY_NAME = "name"
const val KEY_NOTIFY_PARAMS = "params"
const val KEY_NOTIFY_PARAM_COMPLETION = "completion"

fun toNotify(
  name: String,
  params: WritableMap?
): WritableMap {
  val objects = Arguments.createMap()
  objects.putString(KEY_NOTIFY_NAME, name)
  params?.run {
    objects.putMap(KEY_NOTIFY_PARAMS, params)
  }
  return objects
}

fun toCaptureProgressNotifyParam(value: Float): WritableMap {
  val result = Arguments.createMap()
  result.putDouble(KEY_NOTIFY_PARAM_COMPLETION, value.toDouble())
  return result
}

/**
 * convert option interface
 */
interface OptionConverter {
  fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {}
  fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {}
  fun setPhotoOption(objects: ReadableMap, builder: PhotoCapture.Builder) {}
  fun setTimeShiftOption(objects: ReadableMap, builder: TimeShiftCapture.Builder) {}
  fun setVideoOption(objects: ReadableMap, builder: VideoCapture.Builder) {}
}

/**
 * AiAutoThumbnailConverter
 */
class AiAutoThumbnailConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("aiAutoThumbnail")?.let {
      options.aiAutoThumbnail = ThetaRepository.AiAutoThumbnailEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.aiAutoThumbnail?.let {
      objects.putString("aiAutoThumbnail", it.toString())
    }
  }
}

/**
 * ApertureConverter
 */
class ApertureConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("aperture")?.let {
      options.aperture = ThetaRepository.ApertureEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.aperture?.let {
      objects.putString("aperture", it.toString())
    }
  }

  override fun setPhotoOption(objects: ReadableMap, builder: PhotoCapture.Builder) {
    objects.getString("aperture")?.let {
      builder.setAperture(ThetaRepository.ApertureEnum.valueOf(it))
    }
  }

  override fun setTimeShiftOption(objects: ReadableMap, builder: TimeShiftCapture.Builder) {
    objects.getString("aperture")?.let {
      builder.setAperture(ThetaRepository.ApertureEnum.valueOf(it))
    }
  }

  override fun setVideoOption(objects: ReadableMap, builder: VideoCapture.Builder) {
    objects.getString("aperture")?.let {
      builder.setAperture(ThetaRepository.ApertureEnum.valueOf(it))
    }
  }
}

/**
 * BitrateConverter
 */
class BitrateConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    val type = objects.getType("bitrate")
    if (type == ReadableType.Number) {
      options.bitrate = ThetaRepository.BitrateNumber(objects.getInt("bitrate"));
    } else if (type == ReadableType.String) {
      objects.getString("bitrate")?.let { rateStr ->
        options.bitrate = ThetaRepository.BitrateEnum.valueOf(rateStr)
      }
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.bitrate?.let {
      if (it is ThetaRepository.BitrateEnum) {
        objects.putString("bitrate", it.toString())
      } else if (it is ThetaRepository.BitrateNumber) {
        objects.putInt("bitrate", it.value)
      }
    }
  }
}

/**
 * BluetoothPowerConverter
 */
class BluetoothPowerConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("bluetoothPower")?.let {
      options.bluetoothPower = ThetaRepository.BluetoothPowerEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.bluetoothPower?.let {
      objects.putString("bluetoothPower", it.toString())
    }
  }
}

/**
 * BurstModeConverter
 */
class BurstModeConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("burstMode")?.let {
      options.burstMode = ThetaRepository.BurstModeEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.burstMode?.let {
      objects.putString("burstMode", it.toString())
    }
  }
}

/**
 * BurstOptionConverter
 */
class BurstOptionConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getMap("burstOption")?.let { map ->
      options.burstOption = ThetaRepository.BurstOption(
        burstCaptureNum = map.getString("burstCaptureNum")?.let { ThetaRepository.BurstCaptureNumEnum.valueOf(it) },
        burstBracketStep = map.getString("burstBracketStep")?.let { ThetaRepository.BurstBracketStepEnum.valueOf(it) },
        burstCompensation = map.getString("burstCompensation")?.let { ThetaRepository.BurstCompensationEnum.valueOf(it) },
        burstMaxExposureTime = map.getString("burstMaxExposureTime")?.let { ThetaRepository.BurstMaxExposureTimeEnum.valueOf(it) },
        burstEnableIsoControl = map.getString("burstEnableIsoControl")?.let { ThetaRepository.BurstEnableIsoControlEnum.valueOf(it) },
        burstOrder = map.getString("burstOrder")?.let { ThetaRepository.BurstOrderEnum.valueOf(it) }
      )
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.burstOption?.let {
      val burstOption = Arguments.createMap()
      it.burstCaptureNum?.value?.name?.let { name ->
        burstOption.putString("burstCaptureNum", name)
      }
      it.burstBracketStep?.value?.name?.let { name ->
        burstOption.putString("burstBracketStep", name)
      }
      it.burstCompensation?.value?.name?.let { name ->
        burstOption.putString("burstCompensation", name)
      }
      it.burstMaxExposureTime?.value?.name?.let { name ->
        burstOption.putString("burstMaxExposureTime", name)
      }
      it.burstEnableIsoControl?.value?.name?.let { name ->
        burstOption.putString("burstEnableIsoControl", name)
      }
      it.burstOrder?.value?.name?.let { name ->
        burstOption.putString("burstOrder", name)
      }
      objects.putMap("burstOption", burstOption)
    }
  }
}

/**
 * CameraControlSourceConverter
 */
class CameraControlSourceConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("cameraControlSource")?.let {
      options.cameraControlSource = ThetaRepository.CameraControlSourceEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.cameraControlSource?.let {
      objects.putString("cameraControlSource", it.toString())
    }
  }
}

/**
 * CameraModeConverter
 */
class CameraModeConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("cameraMode")?.let {
      options.cameraMode = ThetaRepository.CameraModeEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.cameraMode?.let {
      objects.putString("cameraMode", it.toString())
    }
  }
}

/**
 * CaptureIntervalConverter
 */
class CaptureIntervalConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    options.captureInterval = objects.getInt("captureInterval")
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.captureInterval?.let {
      objects.putInt("captureInterval", it)
    }
  }
}

/**
 * CaptureModeConverter
 */
class CaptureModeConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("captureMode")?.let {
      options.captureMode = ThetaRepository.CaptureModeEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.captureMode?.let {
      objects.putString("captureMode", it.toString())
    }
  }
}

/**
 * CaptureNumberConverter
 */
class CaptureNumberConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    options.captureNumber = objects.getInt("captureNumber")
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.captureNumber?.let {
      objects.putInt("captureNumber", it)
    }
  }
}

/**
 * ContinuousNumberConverter
 */
class ContinuousNumberConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("continuousNumber")?.let {
      options.continuousNumber = ThetaRepository.ContinuousNumberEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.continuousNumber?.let {
      objects.putString("continuousNumber", it.toString())
    }
  }
}

/**
 * ExposureCompensationConverter
 */
class ExposureCompensationConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("exposureCompensation")?.let {
      options.exposureCompensation = ThetaRepository.ExposureCompensationEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.exposureCompensation?.let {
      objects.putString("exposureCompensation", it.toString())
    }
  }

  override fun setPhotoOption(objects: ReadableMap, builder: PhotoCapture.Builder) {
    objects.getString("exposureCompensation")?.let {
      builder.setExposureCompensation(ThetaRepository.ExposureCompensationEnum.valueOf(it))
    }
  }

  override fun setTimeShiftOption(objects: ReadableMap, builder: TimeShiftCapture.Builder) {
    objects.getString("exposureCompensation")?.let {
      builder.setExposureCompensation(ThetaRepository.ExposureCompensationEnum.valueOf(it))
    }
  }

  override fun setVideoOption(objects: ReadableMap, builder: VideoCapture.Builder) {
    objects.getString("exposureCompensation")?.let {
      builder.setExposureCompensation(ThetaRepository.ExposureCompensationEnum.valueOf(it))
    }
  }
}

/**
 * ExposureDelayConverter
 */
class ExposureDelayConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("exposureDelay")?.let {
      options.exposureDelay = ThetaRepository.ExposureDelayEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.exposureDelay?.let {
      objects.putString("exposureDelay", it.toString())
    }
  }

  override fun setPhotoOption(objects: ReadableMap, builder: PhotoCapture.Builder) {
    objects.getString("exposureDelay")?.let {
      builder.setExposureDelay(ThetaRepository.ExposureDelayEnum.valueOf(it))
    }
  }

  override fun setTimeShiftOption(objects: ReadableMap, builder: TimeShiftCapture.Builder) {
    objects.getString("exposureDelay")?.let {
      builder.setExposureDelay(ThetaRepository.ExposureDelayEnum.valueOf(it))
    }
  }

  override fun setVideoOption(objects: ReadableMap, builder: VideoCapture.Builder) {
    objects.getString("exposureDelay")?.let {
      builder.setExposureDelay(ThetaRepository.ExposureDelayEnum.valueOf(it))
    }
  }
}

/**
 * ExposureProgramConverter
 */
class ExposureProgramConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("exposureProgram")?.let {
      options.exposureProgram = ThetaRepository.ExposureProgramEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.exposureProgram?.let {
      objects.putString("exposureProgram", it.toString())
    }
  }

  override fun setPhotoOption(objects: ReadableMap, builder: PhotoCapture.Builder) {
    objects.getString("exposureProgram")?.let {
      builder.setExposureProgram(ThetaRepository.ExposureProgramEnum.valueOf(it))
    }
  }

  override fun setTimeShiftOption(objects: ReadableMap, builder: TimeShiftCapture.Builder) {
    objects.getString("exposureProgram")?.let {
      builder.setExposureProgram(ThetaRepository.ExposureProgramEnum.valueOf(it))
    }
  }

  override fun setVideoOption(objects: ReadableMap, builder: VideoCapture.Builder) {
    objects.getString("exposureProgram")?.let {
      builder.setExposureProgram(ThetaRepository.ExposureProgramEnum.valueOf(it))
    }
  }
};

/**
 * FileFormatConverter
 */
class FileFormatConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("fileFormat")?.let {
      options.fileFormat = ThetaRepository.FileFormatEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.fileFormat?.let {
      objects.putString("fileFormat", it.toString())
    }
  }

  override fun setPhotoOption(objects: ReadableMap, builder: PhotoCapture.Builder) {
    objects.getString("fileFormat")?.let {
      builder.setFileFormat(ThetaRepository.PhotoFileFormatEnum.valueOf(it))
    }
  }

  override fun setVideoOption(objects: ReadableMap, builder: VideoCapture.Builder) {
    objects.getString("fileFormat")?.let {
      builder.setFileFormat(ThetaRepository.VideoFileFormatEnum.valueOf(it))
    }
  }
}

/**
 * FilterConverter
 */
class FilterConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("filter")?.let {
      options.filter = ThetaRepository.FilterEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.filter?.let {
      objects.putString("filter", it.toString())
    }
  }

  override fun setPhotoOption(objects: ReadableMap, builder: PhotoCapture.Builder) {
    objects.getString("filter")?.let {
      builder.setFilter(ThetaRepository.FilterEnum.valueOf(it))
    }
  }
}

/**
 * GpsTagRecordingConverter
 */
class GpsTagRecordingConverter : OptionConverter {
  override fun setPhotoOption(objects: ReadableMap, builder: PhotoCapture.Builder) {
    objects.getString("gpsTagRecording")?.let {
      builder.setGpsTagRecording(ThetaRepository.GpsTagRecordingEnum.valueOf(it))
    }
  }

  override fun setTimeShiftOption(objects: ReadableMap, builder: TimeShiftCapture.Builder) {
    objects.getString("gpsTagRecording")?.let {
      builder.setGpsTagRecording(ThetaRepository.GpsTagRecordingEnum.valueOf(it))
    }
  }

  override fun setVideoOption(objects: ReadableMap, builder: VideoCapture.Builder) {
    objects.getString("gpsTagRecording")?.let {
      builder.setGpsTagRecording(ThetaRepository.GpsTagRecordingEnum.valueOf(it))
    }
  }
}

/**
 * IsoConverter
 */
class IsoConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("iso")?.let {
      options.iso = ThetaRepository.IsoEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.iso?.let {
      objects.putString("iso", it.toString())
    }
  }

  override fun setPhotoOption(objects: ReadableMap, builder: PhotoCapture.Builder) {
    objects.getString("iso")?.let {
      builder.setIso(ThetaRepository.IsoEnum.valueOf(it))
    }
  }

  override fun setTimeShiftOption(objects: ReadableMap, builder: TimeShiftCapture.Builder) {
    objects.getString("iso")?.let {
      builder.setIso(ThetaRepository.IsoEnum.valueOf(it))
    }
  }

  override fun setVideoOption(objects: ReadableMap, builder: VideoCapture.Builder) {
    objects.getString("iso")?.let {
      builder.setIso(ThetaRepository.IsoEnum.valueOf(it))
    }
  }
}

/**
 * IsoAutoHighLimitConverter
 */
class IsoAutoHighLimitConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("isoAutoHighLimit")?.let {
      options.isoAutoHighLimit = ThetaRepository.IsoAutoHighLimitEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.isoAutoHighLimit?.let {
      objects.putString("isoAutoHighLimit", it.toString())
    }
  }

  override fun setPhotoOption(objects: ReadableMap, builder: PhotoCapture.Builder) {
    objects.getString("isoAutoHighLimit")?.let {
      builder.setIsoAutoHighLimit(ThetaRepository.IsoAutoHighLimitEnum.valueOf(it))
    }
  }

  override fun setTimeShiftOption(objects: ReadableMap, builder: TimeShiftCapture.Builder) {
    objects.getString("isoAutoHighLimit")?.let {
      builder.setIsoAutoHighLimit(ThetaRepository.IsoAutoHighLimitEnum.valueOf(it))
    }
  }

  override fun setVideoOption(objects: ReadableMap, builder: VideoCapture.Builder) {
    objects.getString("isoAutoHighLimit")?.let {
      builder.setIsoAutoHighLimit(ThetaRepository.IsoAutoHighLimitEnum.valueOf(it))
    }
  }
}

/**
 * LanguageConverter
 */
class LanguageConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("language")?.let {
      options.language = ThetaRepository.LanguageEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.language?.let {
      objects.putString("language", it.toString())
    }
  }
}

/**
 * MaxRecordableTimeConverter
 */
class MaxRecordableTimeConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("maxRecordableTime")?.let {
      options.maxRecordableTime = ThetaRepository.MaxRecordableTimeEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.maxRecordableTime?.let {
      objects.putString("maxRecordableTime", it.toString())
    }
  }

  override fun setVideoOption(objects: ReadableMap, builder: VideoCapture.Builder) {
    objects.getString("maxRecordableTime")?.let {
      builder.setMaxRecordableTime(ThetaRepository.MaxRecordableTimeEnum.valueOf(it))
    }
  }
}

/**
 * NetworkTypeConverter
 */
class NetworkTypeConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("networkType")?.let {
      options.networkType = ThetaRepository.NetworkTypeEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.networkType?.let {
      objects.putString("networkType", it.toString())
    }
  }
}

/**
 * OffDelayConverter
 */
class OffDelayConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    val type = objects.getType("offDelay")
    if (type == ReadableType.Number) {
      options.offDelay = ThetaRepository.OffDelaySec(objects.getInt("offDelay"));
    } else if (type == ReadableType.String) {
      objects.getString("offDelay")?.let {
        options.offDelay = ThetaRepository.OffDelayEnum.valueOf(it)
      }
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.offDelay?.let {
      if (it is ThetaRepository.OffDelayEnum) {
        objects.putString("offDelay", it.toString())
      } else if (it is ThetaRepository.OffDelaySec) {
        objects.putInt("offDelay", it.sec)
      }
    }
  }
}

/**
 * PowerSavingConverter
 */
class PowerSavingConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("powerSaving")?.let {
      options.powerSaving = ThetaRepository.PowerSavingEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.powerSaving?.let {
      objects.putString("powerSaving", it.toString())
    }
  }
}

/**
 * PresetConverter
 */
class PresetConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("preset")?.let {
      options.preset = ThetaRepository.PresetEnum.valueOf(it)
    }
  }

   override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.preset?.let {
      objects.putString("preset", it.toString())
    }
  }
 }

/**
 * PreviewFormatConverter
 */
class PreviewFormatConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("previewFormat")?.let {
      options.previewFormat = ThetaRepository.PreviewFormatEnum.valueOf(it)
    }
  }

   override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.previewFormat?.let {
      objects.putString("previewFormat", it.toString())
    }
  }
 }

/**
 * ProxyConverter
 */
class ProxyConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getMap("proxy")?.let {
      options.proxy = ThetaRepository.Proxy(
        use = it.getBoolean("use") ?: false,
        url = it.getString("url"),
        port = if (it.hasKey("port")) it.getInt("port") else null,
        userid = it.getString("userid"),
        password = it.getString("password")
      )
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.proxy?.let {
      val proxy = Arguments.createMap()
      proxy.putBoolean("use", it.use)
      it.url?.let { url ->
        proxy.putString("url", url)
      }
      it.port?.let { port ->
        proxy.putInt("port", port)
      }
      it.userid?.let { userid ->
        proxy.putString("userid", userid)
      }
      it.password?.let { password ->
        proxy.putString("password", password)
      }
      objects.putMap("proxy", proxy)
    }
  }
}

/**
 * TimeShiftConverter
 */
class TimeShiftConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getMap("timeShift")?.let { timeShiftMap ->
      val timeShift = ThetaRepository.TimeShiftSetting()
      if (timeShiftMap.hasKey("isFrontFirst")) {
        timeShift.isFrontFirst = timeShiftMap.getBoolean("isFrontFirst")
      }
      timeShiftMap.getString("firstInterval")?.let {
        timeShift.firstInterval = ThetaRepository.TimeShiftIntervalEnum.valueOf(it)
      }
      timeShiftMap.getString("secondInterval")?.let {
        timeShift.secondInterval = ThetaRepository.TimeShiftIntervalEnum.valueOf(it)
      }
      options.timeShift = timeShift
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.timeShift?.let { timeShiftMap ->
      val timeShift = Arguments.createMap()
      timeShiftMap.isFrontFirst?.let {
        timeShift.putBoolean("isFrontFirst", it)
      }
      timeShiftMap.firstInterval?.let {
        timeShift.putString("firstInterval", it.toString())
      }
      timeShiftMap.secondInterval?.let {
        timeShift.putString("secondInterval", it.toString())
      }
      objects.putMap("timeShift", timeShift)
    }
  }

  override fun setTimeShiftOption(objects: ReadableMap, builder: TimeShiftCapture.Builder) {
    objects.getMap("timeShift")?.let { timeShiftMap ->
      if (timeShiftMap.hasKey("isFrontFirst")) {
        builder.setIsFrontFirst(timeShiftMap.getBoolean("isFrontFirst"))
      }
      timeShiftMap.getString("firstInterval")?.let {
        builder.setFirstInterval(ThetaRepository.TimeShiftIntervalEnum.valueOf(it))
      }
      timeShiftMap.getString("secondInterval")?.let {
        builder.setSecondInterval(ThetaRepository.TimeShiftIntervalEnum.valueOf(it))
      }
    }
  }
}

/**
 * ShootingMethodConverter
 */
class ShootingMethodConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("shootingMethod")?.let {
      options.shootingMethod = ThetaRepository.ShootingMethodEnum.valueOf(it)
    }
  }

   override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.shootingMethod?.let {
      objects.putString("shootingMethod", it.toString())
    }
  }
 }

/**
 * WhiteBalanceConverter
 */
class WhiteBalanceConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("whiteBalance")?.let {
      options.whiteBalance = ThetaRepository.WhiteBalanceEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.whiteBalance?.let {
      objects.putString("whiteBalance", it.toString())
    }
  }

  override fun setPhotoOption(objects: ReadableMap, builder: PhotoCapture.Builder) {
    objects.getString("whiteBalance")?.let {
      builder.setWhiteBalance(ThetaRepository.WhiteBalanceEnum.valueOf(it))
    }
  }

  override fun setTimeShiftOption(objects: ReadableMap, builder: TimeShiftCapture.Builder) {
    objects.getString("whiteBalance")?.let {
      builder.setWhiteBalance(ThetaRepository.WhiteBalanceEnum.valueOf(it))
    }
  }

  override fun setVideoOption(objects: ReadableMap, builder: VideoCapture.Builder) {
    objects.getString("whiteBalance")?.let {
      builder.setWhiteBalance(ThetaRepository.WhiteBalanceEnum.valueOf(it))
    }
  }
}

/**
 * WhiteBalanceAutoStrengthConverter
 */
class WhiteBalanceAutoStrengthConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("whiteBalanceAutoStrength")?.let {
      options.whiteBalanceAutoStrength = ThetaRepository.WhiteBalanceAutoStrengthEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.whiteBalanceAutoStrength?.let {
      objects.putString("whiteBalanceAutoStrength", it.toString())
    }
  }
}

/**
 * WlanFrequencyConverter
 */
class WlanFrequencyConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("wlanFrequency")?.let {
      options.wlanFrequency = ThetaRepository.WlanFrequencyEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.wlanFrequency?.let {
      objects.putString("wlanFrequency", it.toString())
    }
  }
}


/**
 * ColorTemperatureConverter
 */
class ColorTemperatureConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    options.colorTemperature = objects.getInt("colorTemperature")
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.colorTemperature?.let {
      objects.putInt("colorTemperature", it)
    }
  }

  override fun setPhotoOption(objects: ReadableMap, builder: PhotoCapture.Builder) {
    builder.setColorTemperature(objects.getInt("colorTemperature"))
  }

  override fun setTimeShiftOption(objects: ReadableMap, builder: TimeShiftCapture.Builder) {
    builder.setColorTemperature(objects.getInt("colorTemperature"))
  }

  override fun setVideoOption(objects: ReadableMap, builder: VideoCapture.Builder) {
    builder.setColorTemperature(objects.getInt("colorTemperature"))
  }
}

/**
 * CompositeShootingOutputIntervalConverter
 */
class CompositeShootingOutputIntervalConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    options.compositeShootingOutputInterval = objects.getInt("compositeShootingOutputInterval")
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.compositeShootingOutputInterval?.let {
      objects.putInt("compositeShootingOutputInterval", it)
    }
  }
}

/**
 * CompositeShootingTimeConverter
 */
class CompositeShootingTimeConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    options.compositeShootingTime = objects.getInt("compositeShootingTime")
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.compositeShootingTime?.let {
      objects.putInt("compositeShootingTime", it)
    }
  }
}

/**
 * DateTimeZoneConverter
 */
class DateTimeZoneConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("dateTimeZone")?.let {
      options.dateTimeZone = it
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.dateTimeZone?.let {
      objects.putString("dateTimeZone", it)
    }
  }
}

/**
 * IsGpsOnConverter
 */
class IsGpsOnConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    options.isGpsOn = objects.getBoolean("isGpsOn")
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.isGpsOn?.let {
      objects.putBoolean("isGpsOn", it)
    }
  }
}

/**
 * ShutterSpeedConverter
 */
class ShutterSpeedConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("shutterSpeed")?.let {
      options.shutterSpeed = ThetaRepository.ShutterSpeedEnum.valueOf(it)
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.shutterSpeed?.let {
      objects.putString("shutterSpeed", it.toString())
    }
  }
}

/**
 * SleepDelayConverter
 */
class SleepDelayConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    val type = objects.getType("sleepDelay")
    if (type == ReadableType.Number) {
      options.sleepDelay = ThetaRepository.SleepDelaySec(objects.getInt("sleepDelay"))
    } else if (type == ReadableType.String) {
      objects.getString("SleepDelay")?.let {
        options.sleepDelay = ThetaRepository.SleepDelayEnum.valueOf(it)
      }
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.sleepDelay?.let {
      if (it is ThetaRepository.SleepDelayEnum) {
        objects.putString("sleepDelay", it.toString())
      } else if (it is ThetaRepository.SleepDelaySec) {
        objects.putInt("sleepDelay", it.sec)
      }
    }
  }
}

/**
 * RemainingPicturesConverter
 */
class RemainingPicturesConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    options.remainingPictures = objects.getInt("remainingPictures")
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.remainingPictures?.let {
      objects.putInt("remainingPictures", it)
    }
  }
}

/**
 * RemainingVideoSecondsConverter
 */
class RemainingVideoSecondsConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    options.remainingVideoSeconds = objects.getInt("remainingVideoSeconds")
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.remainingVideoSeconds?.let {
      objects.putInt("remainingVideoSeconds", it)
    }
  }
}

/**
 * RemainingSpaceConverter
 */
class RemainingSpaceConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    options.remainingSpace = objects.getDouble("remainingSpace").toLong()
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.remainingSpace?.let {
      objects.putDouble("remainingSpace", it.toDouble())
    }
  }
}

/**
 * TotalSpaceConverter
 */
class TotalSpaceConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    options.totalSpace = objects.getDouble("totalSpace").toLong()
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.totalSpace?.let {
      objects.putDouble("totalSpace", it.toDouble())
    }
  }
}

/**
 * ShutterVolumeConverter
 */
class ShutterVolumeConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    options.shutterVolume = objects.getInt("shutterVolume")
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.shutterVolume?.let {
      objects.putInt("shutterVolume", it)
    }
  }
}

/**
 * GpsInfoConverter
 */
class GpsInfoConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getMap("gpsInfo")?.let {
      options.gpsInfo = ThetaRepository.GpsInfo(
        latitude = it.getDouble("latitude").toFloat(),
        longitude = it.getDouble("longitude").toFloat(),
        altitude = it.getDouble("altitude").toFloat(),
        dateTimeZone = it.getString("dateTimeZone") ?: ""
      )
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.gpsInfo?.let {
      val gpsInfo = Arguments.createMap()
      gpsInfo.putDouble("latitude", options.gpsInfo!!.latitude.toDouble())
      gpsInfo.putDouble("longitude", options.gpsInfo!!.longitude.toDouble())
      gpsInfo.putDouble("altitude", options.gpsInfo!!.altitude.toDouble())
      gpsInfo.putString("dateTimeZone", options.gpsInfo!!.dateTimeZone)
      objects.putMap("gpsInfo", gpsInfo)
    }
  }
}

/**
 * UsernameConverter
 */
class UsernameConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("username")?.let {
      options.username = it
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.username?.let {
      objects.putString("username", it)
    }
  }
}

/**
 * PasswordConverter
 */
class PasswordConverter : OptionConverter {
  override fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {
    objects.getString("password")?.let {
      options.password = it
    }
  }

  override fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {
    options.password?.let {
      objects.putString("password", it)
    }
  }
}

fun configToTheta(objects: ReadableMap): ThetaRepository.Config {
  val config = ThetaRepository.Config()
  config.dateTime = objects.getString("dateTime")

  objects.getString("language")?.let {
    config.language = ThetaRepository.LanguageEnum.valueOf(it)
  }

  objects.getString("offDelay")?.let {
    config.offDelay = ThetaRepository.OffDelayEnum.valueOf(it)
  }

  objects.getString("sleepDelay")?.let {
    config.sleepDelay = ThetaRepository.SleepDelayEnum.valueOf(it)
  }

  config.shutterVolume = if (objects.hasKey("shutterVolume")) {
    objects.getInt("shutterVolume")
  } else {
    null
  }

  config.clientMode = if (objects.hasKey("clientMode")) {
    objects.getMap("clientMode")?.let {
      digestAuthToTheta(it)
    }
  } else {
    null
  }

  return config
}

fun digestAuthToTheta(objects: ReadableMap): DigestAuth? {
  val username = objects.getString("username") ?: run {
    return null
  }
  val password = if (objects.hasKey("password")) {
    objects.getString("password")
  } else {
    null
  }
  return DigestAuth(username, password)
}

fun timeoutToTheta(objects: ReadableMap): ThetaRepository.Timeout {
  return ThetaRepository.Timeout(
    objects.getInt("connectTimeout").toLong(),
    objects.getInt("requestTimeout").toLong(),
    objects.getInt("socketTimeout").toLong(),
  )
}

fun fileInfoFromTheta(fileInfo: ThetaRepository.FileInfo): ReadableMap {
  val result = Arguments.createMap()
  result.putString("name", fileInfo.name)
  result.putString("fileUrl", fileInfo.fileUrl)
  result.putDouble("size", fileInfo.size.toDouble())
  result.putString("dateTime", fileInfo.dateTime)
  fileInfo.lat?.let {
    result.putDouble("lat", it.toDouble())
  }
  fileInfo.lng?.let {
    result.putDouble("lng", it.toDouble())
  }
  fileInfo.width?.let {
    result.putInt("width", it)
  }
  fileInfo.height?.let {
    result.putInt("height", it)
  }
  result.putString("thumbnailUrl", fileInfo.thumbnailUrl)
  fileInfo.intervalCaptureGroupId?.let {
    result.putString("intervalCaptureGroupId", it)
  }
  fileInfo.compositeShootingGroupId?.let {
    result.putString("compositeShootingGroupId", it)
  }
  fileInfo.autoBracketGroupId?.let {
    result.putString("autoBracketGroupId", it)
  }
  fileInfo.recordTime?.let {
    result.putInt("recordTime", it)
  }
  fileInfo.isProcessed?.let {
    result.putBoolean("isProcessed", it)
  }
  fileInfo.previewUrl?.let {
    result.putString("previewUrl", it)
  }
  fileInfo.codec?.let {
    result.putString("codec", it.name)
  }
  fileInfo.projectionType?.let {
    result.putString("projectionType", it.name)
  }
  fileInfo.continuousShootingGroupId?.let {
    result.putString("continuousShootingGroupId", it)
  }
  fileInfo.frameRate?.let {
    result.putInt("frameRate", it)
  }
  fileInfo.favorite?.let {
    result.putBoolean("favorite", it)
  }
  fileInfo.imageDescription?.let {
    result.putString("imageDescription", it)
  }
  fileInfo.storageID?.let {
    result.putString("storageID", it)
  }
  return result
}
