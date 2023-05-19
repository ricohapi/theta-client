package com.ricoh360.thetaclientreactnative

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType
import com.facebook.react.bridge.WritableMap
import com.ricoh360.thetaclient.DigestAuth
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.capture.PhotoCapture
import com.ricoh360.thetaclient.capture.VideoCapture

/**
 * convert option interface
 */
interface OptionConverter {
  fun setToTheta(options: ThetaRepository.Options, objects: ReadableMap) {}
  fun setFromTheta(options: ThetaRepository.Options, objects: WritableMap) {}
  fun setPhotoOption(objects: ReadableMap, builder: PhotoCapture.Builder) {}
  fun setVideoOption(objects: ReadableMap, builder: VideoCapture.Builder) {}
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

  override fun setVideoOption(objects: ReadableMap, builder: VideoCapture.Builder) {
    objects.getString("aperture")?.let {
      builder.setAperture(ThetaRepository.ApertureEnum.valueOf(it))
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

  override fun setVideoOption(objects: ReadableMap, builder: VideoCapture.Builder) {
    builder.setColorTemperature(objects.getInt("colorTemperature"))
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
  }  else { null }

  config.clientMode = if (objects.hasKey("clientMode")) {
    objects.getMap("clientMode")?.let {
      digestAuthToTheta(it)
    }
  }  else { null }

  return config
}

fun digestAuthToTheta(objects: ReadableMap): DigestAuth? {
  val username = objects.getString("username") ?: run {
    return null
  }
  val password = if (objects.hasKey("password")) { objects.getString("password") }  else { null }
  return DigestAuth(username, password)
}

fun timeoutToTheta(objects: ReadableMap): ThetaRepository.Timeout {
  return ThetaRepository.Timeout(
    objects.getInt("connectTimeout").toLong(),
    objects.getInt("requestTimeout").toLong(),
    objects.getInt("socketTimeout").toLong(),
  )
}
