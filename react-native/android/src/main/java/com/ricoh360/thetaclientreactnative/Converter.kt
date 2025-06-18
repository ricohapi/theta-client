package com.ricoh360.thetaclientreactnative

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableArray
import com.ricoh360.thetaclient.DigestAuth
import com.ricoh360.thetaclient.ThetaRepository.*
import com.ricoh360.thetaclient.capture.*
import com.ricoh360.thetaclient.websocket.CameraEvent
import kotlin.reflect.KClass

const val KEY_NOTIFY_NAME = "name"
const val KEY_NOTIFY_PARAMS = "params"
const val KEY_NOTIFY_PARAM_COMPLETION = "completion"
const val KEY_NOTIFY_PARAM_EVENT = "event"
const val KEY_NOTIFY_PARAM_MESSAGE = "message"
const val KEY_NOTIFY_PARAM_STATUS = "status"
const val KEY_NOTIFY_PARAM_FILE_URL = "fileUrl"
const val KEY_GPS_INFO = "gpsInfo"
const val KEY_STATE_EXTERNAL_GPS_INFO = "externalGpsInfo"
const val KEY_STATE_INTERNAL_GPS_INFO = "internalGpsInfo"
const val KEY_STATE_BOARD_TEMP = "boardTemp"
const val KEY_STATE_BATTERY_TEMP = "batteryTemp"
const val KEY_SSID = "ssid"
const val KEY_SSID_STEALTH = "ssidStealth"
const val KEY_AUTH_MODE = "authMode"
const val KEY_PASSWORD = "password"
const val KEY_CONNECTION_PRIORITY = "connectionPriority"
const val KEY_IP_ADDRESS = "ipAddress"
const val KEY_SUBNET_MASK = "subnetMask"
const val KEY_DEFAULT_GATEWAY = "defaultGateway"
const val KEY_PROXY = "proxy"
const val KEY_MAC_ADDRESS = "macAddress"
const val KEY_HOST_NAME = "hostName"
const val KEY_DHCP_LEASE_ADDRESS = "dhcpLeaseAddress"
const val KEY_TOP_BOTTOM_CORRECTION_ROTATION = "topBottomCorrectionRotation"
const val KEY_TOP_BOTTOM_CORRECTION_ROTATION_PITCH = "pitch"
const val KEY_TOP_BOTTOM_CORRECTION_ROTATION_ROLL = "roll"
const val KEY_TOP_BOTTOM_CORRECTION_ROTATION_YAW = "yaw"
const val KEY_TOP_BOTTOM_CORRECTION_ROTATION_SUPPORT = "topBottomCorrectionRotationSupport"
const val KEY_MAX = "max"
const val KEY_MIN = "min"
const val KEY_STEP_SIZE = "stepSize"
const val KEY_GPS_TAG_RECORDING_SUPPORT = "gpsTagRecordingSupport"
const val KEY_COMPOSITE_SHOOTING_OUTPUT_INTERVAL_SUPPORT = "compositeShootingOutputIntervalSupport"
const val KEY_COMPOSITE_SHOOTING_TIME_SUPPORT = "compositeShootingTimeSupport"
const val KEY_APERTURE_SUPPORT = "apertureSupport"
const val KEY_WLAN_FREQUENCY_CL_MODE = "wlanFrequencyClMode"
const val KEY_WLAN_FREQUENCY_CL_MODE_2_4 = "enable2_4"
const val KEY_WLAN_FREQUENCY_CL_MODE_5_2 = "enable5_2"
const val KEY_WLAN_FREQUENCY_CL_MODE_5_8 = "enable5_8"
const val KEY_DNS1 = "dns1"
const val KEY_DNS2 = "dns2"

val optionItemNameToEnum: Map<String, OptionNameEnum> = mutableMapOf(
  "accessInfo" to OptionNameEnum.AccessInfo,
  "aiAutoThumbnail" to OptionNameEnum.AiAutoThumbnail,
  "aiAutoThumbnailSupport" to OptionNameEnum.AiAutoThumbnailSupport,
  "aperture" to OptionNameEnum.Aperture,
  KEY_APERTURE_SUPPORT to OptionNameEnum.ApertureSupport,
  "autoBracket" to OptionNameEnum.AutoBracket,
  "bitrate" to OptionNameEnum.Bitrate,
  "bluetoothPower" to OptionNameEnum.BluetoothPower,
  "bluetoothRole" to OptionNameEnum.BluetoothRole,
  "burstMode" to OptionNameEnum.BurstMode,
  "burstOption" to OptionNameEnum.BurstOption,
  "cameraControlSource" to OptionNameEnum.CameraControlSource,
  "cameraControlSourceSupport" to OptionNameEnum.CameraControlSourceSupport,
  "cameraLock" to OptionNameEnum.CameraLock,
  "cameraLockConfig" to OptionNameEnum.CameraLockConfig,
  "cameraMode" to OptionNameEnum.CameraMode,
  "cameraPower" to OptionNameEnum.CameraPower,
  "cameraPowerSupport" to OptionNameEnum.CameraPowerSupport,
  "captureInterval" to OptionNameEnum.CaptureInterval,
  "captureMode" to OptionNameEnum.CaptureMode,
  "captureNumber" to OptionNameEnum.CaptureNumber,
  "colorTemperature" to OptionNameEnum.ColorTemperature,
  "colorTemperatureSupport" to OptionNameEnum.ColorTemperatureSupport,
  "compassDirectionRef" to OptionNameEnum.CompassDirectionRef,
  "compositeShootingOutputInterval" to OptionNameEnum.CompositeShootingOutputInterval,
  KEY_COMPOSITE_SHOOTING_OUTPUT_INTERVAL_SUPPORT to OptionNameEnum.CompositeShootingOutputIntervalSupport,
  "compositeShootingTime" to OptionNameEnum.CompositeShootingTime,
  KEY_COMPOSITE_SHOOTING_TIME_SUPPORT to OptionNameEnum.CompositeShootingTimeSupport,
  "continuousNumber" to OptionNameEnum.ContinuousNumber,
  "dateTimeZone" to OptionNameEnum.DateTimeZone,
  "ethernetConfig" to OptionNameEnum.EthernetConfig,
  "exposureCompensation" to OptionNameEnum.ExposureCompensation,
  "exposureDelay" to OptionNameEnum.ExposureDelay,
  "exposureDelaySupport" to OptionNameEnum.ExposureDelaySupport,
  "exposureProgram" to OptionNameEnum.ExposureProgram,
  "faceDetect" to OptionNameEnum.FaceDetect,
  "fileFormat" to OptionNameEnum.FileFormat,
  "filter" to OptionNameEnum.Filter,
  "function" to OptionNameEnum.Function,
  "gain" to OptionNameEnum.Gain,
  KEY_GPS_INFO to OptionNameEnum.GpsInfo,
  "imageStitching" to OptionNameEnum.ImageStitching,
  "isGpsOn" to OptionNameEnum.IsGpsOn,
  "iso" to OptionNameEnum.Iso,
  "isoAutoHighLimit" to OptionNameEnum.IsoAutoHighLimit,
  "language" to OptionNameEnum.Language,
  "latestEnabledExposureDelayTime" to OptionNameEnum.LatestEnabledExposureDelayTime,
  "maxRecordableTime" to OptionNameEnum.MaxRecordableTime,
  "microphoneNoiseReduction" to OptionNameEnum.MicrophoneNoiseReduction,
  "mobileNetworkSetting" to OptionNameEnum.MobileNetworkSetting,
  "networkType" to OptionNameEnum.NetworkType,
  "offDelay" to OptionNameEnum.OffDelay,
  KEY_GPS_TAG_RECORDING_SUPPORT to OptionNameEnum.GpsTagRecordingSupport,
  "offDelayUsb" to OptionNameEnum.OffDelayUsb,
  KEY_PASSWORD to OptionNameEnum.Password,
  "powerSaving" to OptionNameEnum.PowerSaving,
  "preset" to OptionNameEnum.Preset,
  "previewFormat" to OptionNameEnum.PreviewFormat,
  KEY_PROXY to OptionNameEnum.Proxy,
  "remainingPictures" to OptionNameEnum.RemainingPictures,
  "remainingVideoSeconds" to OptionNameEnum.RemainingVideoSeconds,
  "remainingSpace" to OptionNameEnum.RemainingSpace,
  "shootingMethod" to OptionNameEnum.ShootingMethod,
  "shutterSpeed" to OptionNameEnum.ShutterSpeed,
  "shutterVolume" to OptionNameEnum.ShutterVolume,
  "sleepDelay" to OptionNameEnum.SleepDelay,
  "timeShift" to OptionNameEnum.TimeShift,
  "topBottomCorrection" to OptionNameEnum.TopBottomCorrection,
  KEY_TOP_BOTTOM_CORRECTION_ROTATION to OptionNameEnum.TopBottomCorrectionRotation,
  KEY_TOP_BOTTOM_CORRECTION_ROTATION_SUPPORT to OptionNameEnum.TopBottomCorrectionRotationSupport,
  "totalSpace" to OptionNameEnum.TotalSpace,
  "usbConnection" to OptionNameEnum.UsbConnection,
  "username" to OptionNameEnum.Username,
  "videoStitching" to OptionNameEnum.VideoStitching,
  "visibilityReduction" to OptionNameEnum.VisibilityReduction,
  "whiteBalance" to OptionNameEnum.WhiteBalance,
  "whiteBalanceAutoStrength" to OptionNameEnum.WhiteBalanceAutoStrength,
  "wlanAntennaConfig" to OptionNameEnum.WlanAntennaConfig,
  "wlanFrequency" to OptionNameEnum.WlanFrequency,
  "wlanFrequencySupport" to OptionNameEnum.WlanFrequencySupport,
  KEY_WLAN_FREQUENCY_CL_MODE to OptionNameEnum.WlanFrequencyClMode,
)

val optionNameEnumToItemName: Map<OptionNameEnum, String> = optionItemNameToEnum.entries.associate { (key, value) -> value to key }

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

fun toEventWebSocketEventNotifyParam(value: CameraEvent): WritableMap {
  val result = Arguments.createMap()
  result.putMap(KEY_NOTIFY_PARAM_EVENT, toResult(value))
  return result
}

fun toMessageNotifyParam(value: String): WritableMap {
  val result = Arguments.createMap()
  result.putString(KEY_NOTIFY_PARAM_MESSAGE, value)
  return result
}

fun toCapturingNotifyParam(status: CapturingStatusEnum): WritableMap {
  val result = Arguments.createMap()
  result.putString(KEY_NOTIFY_PARAM_STATUS, status.name)
  return result
}

fun toStartedNotifyParam(value: String): WritableMap {
  val result = Arguments.createMap()
  result.putString(KEY_NOTIFY_PARAM_FILE_URL, value)
  return result
}

fun toGpsInfo(map: ReadableMap): GpsInfo {
  return GpsInfo(
    latitude = map.getDouble("latitude").toFloat(),
    longitude = map.getDouble("longitude").toFloat(),
    altitude = map.getDouble("altitude").toFloat(),
    dateTimeZone = map.getString("dateTimeZone") ?: ""
  )
}

fun <T> setCaptureBuilderParams(optionMap: ReadableMap, builder: Capture.Builder<T>) {
  optionMap.getString("aperture")?.let {
    builder.setAperture(ApertureEnum.valueOf(it))
  }
  if (optionMap.hasKey("colorTemperature")) {
    builder.setColorTemperature(optionMap.getInt("colorTemperature"))
  }
  optionMap.getString("exposureCompensation")?.let {
    builder.setExposureCompensation(ExposureCompensationEnum.valueOf(it))
  }
  optionMap.getString("exposureDelay")?.let {
    builder.setExposureDelay(ExposureDelayEnum.valueOf(it))
  }
  optionMap.getString("exposureProgram")?.let {
    builder.setExposureProgram(ExposureProgramEnum.valueOf(it))
  }
  optionMap.getMap(KEY_GPS_INFO)?.let {
    builder.setGpsInfo(toGpsInfo(map = it))
  }
  optionMap.getString("gpsTagRecording")?.let {
    builder.setGpsTagRecording(GpsTagRecordingEnum.valueOf(it))
  }
  optionMap.getString("iso")?.let {
    builder.setIso(IsoEnum.valueOf(it))
  }
  optionMap.getString("isoAutoHighLimit")?.let {
    builder.setIsoAutoHighLimit(IsoAutoHighLimitEnum.valueOf(it))
  }
  optionMap.getString("whiteBalance")?.let {
    builder.setWhiteBalance(WhiteBalanceEnum.valueOf(it))
  }
}

fun setPhotoCaptureBuilderParams(optionMap: ReadableMap, builder: PhotoCapture.Builder) {
  val interval = if (optionMap.hasKey("_capture_interval")) optionMap.getInt("_capture_interval") else null
  interval?.let {
    if (it >= 0) {
      builder.setCheckStatusCommandInterval(it.toLong())
    }
  }

  optionMap.getString("filter")?.let {
    builder.setFilter(FilterEnum.valueOf(it))
  }
  optionMap.getString("fileFormat")?.let {
    builder.setFileFormat(PhotoFileFormatEnum.valueOf(it))
  }
  optionMap.getString("preset")?.let {
    builder.setPreset(PresetEnum.valueOf(it))
  }
}

fun setTimeShiftCaptureBuilderParams(optionMap: ReadableMap, builder: TimeShiftCapture.Builder) {
  val interval = if (optionMap.hasKey("_capture_interval")) optionMap.getInt("_capture_interval") else null
  interval?.let {
    if (it >= 0) {
      builder.setCheckStatusCommandInterval(it.toLong())
    }
  }

  optionMap.getMap("timeShift")?.let { timeShiftMap ->
    if (timeShiftMap.hasKey("isFrontFirst")) {
      builder.setIsFrontFirst(timeShiftMap.getBoolean("isFrontFirst"))
    }
    timeShiftMap.getString("firstInterval")?.let {
      builder.setFirstInterval(TimeShiftIntervalEnum.valueOf(it))
    }
    timeShiftMap.getString("secondInterval")?.let {
      builder.setSecondInterval(TimeShiftIntervalEnum.valueOf(it))
    }
  }
}

fun setTimeShiftManualCaptureBuilderParams(optionMap: ReadableMap, builder: TimeShiftManualCapture.Builder) {
  val interval = if (optionMap.hasKey("_capture_interval")) optionMap.getInt("_capture_interval") else null
  interval?.let {
    if (it >= 0) {
      builder.setCheckStatusCommandInterval(it.toLong())
    }
  }

  optionMap.getMap("timeShift")?.let { timeShiftMap ->
    if (timeShiftMap.hasKey("isFrontFirst")) {
      builder.setIsFrontFirst(timeShiftMap.getBoolean("isFrontFirst"))
    }
    timeShiftMap.getString("firstInterval")?.let {
      builder.setFirstInterval(TimeShiftIntervalEnum.valueOf(it))
    }
    timeShiftMap.getString("secondInterval")?.let {
      builder.setSecondInterval(TimeShiftIntervalEnum.valueOf(it))
    }
  }
}

fun setVideoCaptureBuilderParams(optionMap: ReadableMap, builder: VideoCapture.Builder) {
  val interval =
    if (optionMap.hasKey("_capture_interval")) optionMap.getInt("_capture_interval") else null
  interval?.let {
    if (it >= 0) {
      builder.setCheckStatusCommandInterval(it.toLong())
    }
  }
  optionMap.getString("maxRecordableTime")?.let {
    builder.setMaxRecordableTime(MaxRecordableTimeEnum.valueOf(it))
  }
  optionMap.getString("fileFormat")?.let {
    builder.setFileFormat(VideoFileFormatEnum.valueOf(it))
  }
}

fun setLimitlessIntervalCaptureBuilderParams(optionMap: ReadableMap, builder: LimitlessIntervalCapture.Builder) {
  val interval = if (optionMap.hasKey("_capture_interval")) optionMap.getInt("_capture_interval") else null
  interval?.let {
    if (it >= 0) {
      builder.setCheckStatusCommandInterval(it.toLong())
    }
  }
  if (optionMap.hasKey("captureInterval")) {
    builder.setCaptureInterval(optionMap.getInt("captureInterval"))
  }
}

fun setShotCountSpecifiedIntervalCaptureBuilderParams(optionMap: ReadableMap, builder: ShotCountSpecifiedIntervalCapture.Builder) {
  val interval = if (optionMap.hasKey("_capture_interval")) optionMap.getInt("_capture_interval") else null
  interval?.let {
    if (it >= 0) {
      builder.setCheckStatusCommandInterval(it.toLong())
    }
  }
  if (optionMap.hasKey("captureInterval")) {
    builder.setCaptureInterval(optionMap.getInt("captureInterval"))
  }
}

fun setCompositeIntervalCaptureBuilderParams(optionMap: ReadableMap, builder: CompositeIntervalCapture.Builder) {
  val interval = if (optionMap.hasKey("_capture_interval")) optionMap.getInt("_capture_interval") else null
  interval?.let {
    if (it >= 0) {
      builder.setCheckStatusCommandInterval(it.toLong())
    }
  }
  if (optionMap.hasKey("compositeShootingOutputInterval")) {
    builder.setCompositeShootingOutputInterval(optionMap.getInt("compositeShootingOutputInterval"))
  }
}

fun setBurstCaptureBuilderParams(optionMap: ReadableMap, builder: BurstCapture.Builder) {
  val interval = if (optionMap.hasKey("_capture_interval")) optionMap.getInt("_capture_interval") else null
  interval?.let {
    if (it >= 0) {
      builder.setCheckStatusCommandInterval(it.toLong())
    }
  }
  optionMap.getString("burstMode")?.let {
    builder.setBurstMode(BurstModeEnum.valueOf(it))
  }
}

fun setMultiBracketCaptureBuilderParams(optionMap: ReadableMap, builder: MultiBracketCapture.Builder) {
  val settingArray = optionMap.getArray("autoBracket") ?: return
  for (i in 0 until settingArray.size()) {
    settingArray.getMap(i)?.let { setting ->
      builder.addBracketParameters(
        aperture = setting.getString("aperture")?.let { ApertureEnum.valueOf(it) },
        colorTemperature = if (setting.hasKey("colorTemperature")) setting.getInt("colorTemperature") else null,
        exposureCompensation = setting.getString("exposureCompensation")?.let { ExposureCompensationEnum.valueOf(it) },
        exposureProgram = setting.getString("exposureProgram")?.let { ExposureProgramEnum.valueOf(it) },
        iso = setting.getString("iso")?.let { IsoEnum.valueOf(it) },
        shutterSpeed = setting.getString("shutterSpeed")?.let { ShutterSpeedEnum.valueOf(it) },
        whiteBalance = setting.getString("whiteBalance")?.let { WhiteBalanceEnum.valueOf(it) },
      )
    }
  }
}

fun setContinuousCaptureBuilderParams(optionMap: ReadableMap, builder: ContinuousCapture.Builder) {
  val interval = if (optionMap.hasKey("_capture_interval")) optionMap.getInt("_capture_interval") else null
  interval?.let {
    if (it >= 0) {
      builder.setCheckStatusCommandInterval(it.toLong())
    }
  }
  optionMap.getString("fileFormat")?.let {
    builder.setFileFormat(PhotoFileFormatEnum.valueOf(it))
  }
}

fun toGetOptionsParam(optionNames: ReadableArray): MutableList<OptionNameEnum> {
  val optionNameList = mutableListOf<OptionNameEnum>()
  for (index in 0..(optionNames.size() - 1)) {
    optionNames.getString(index)?.let {
      optionNameList.add(OptionNameEnum.valueOf(it))
    }
  }
  return optionNameList
}

fun toResult(options: Options): WritableMap {
  val result = Arguments.createMap()
  val jsonResult = Arguments.createMap()

  val valueOptions = listOf(
    OptionNameEnum.CaptureInterval,
    OptionNameEnum.CaptureNumber,
    OptionNameEnum.ColorTemperature,
    OptionNameEnum.CompositeShootingOutputInterval,
    OptionNameEnum.CompositeShootingTime,
    OptionNameEnum.DateTimeZone,
    OptionNameEnum.IsGpsOn,
    OptionNameEnum.Password,
    OptionNameEnum.RemainingPictures,
    OptionNameEnum.RemainingVideoSeconds,
    OptionNameEnum.RemainingSpace,
    OptionNameEnum.TotalSpace,
    OptionNameEnum.ShutterVolume,
    OptionNameEnum.Username
  )
  val supportOptions = mapOf<OptionNameEnum, KClass<*>>(
    OptionNameEnum.AiAutoThumbnailSupport to AiAutoThumbnailEnum::class,
    OptionNameEnum.ApertureSupport to ApertureEnum::class,
    OptionNameEnum.CameraControlSourceSupport to CameraControlSourceEnum::class,
    OptionNameEnum.CameraPowerSupport to CameraPowerEnum::class,
    OptionNameEnum.ExposureDelaySupport to ExposureDelayEnum::class,
    OptionNameEnum.GpsTagRecordingSupport to GpsTagRecordingEnum::class,
    OptionNameEnum.WlanFrequencySupport to WlanFrequencyEnum::class
  )
  val intValueRangeSupportOptions = listOf(
    OptionNameEnum.ColorTemperatureSupport,
    OptionNameEnum.CompositeShootingOutputIntervalSupport,
    OptionNameEnum.CompositeShootingTimeSupport
  )
  OptionNameEnum.values().forEach { name ->
    if (name == OptionNameEnum.AccessInfo) {
      options.accessInfo?.let {
        result.putMap("accessInfo", toResult(accessInfo = it))
      }
    } else if (name == OptionNameEnum.AutoBracket) {
      options.autoBracket?.let {
        result.putArray("autoBracket", toResult(autoBracket = it))
      }
    } else if (name == OptionNameEnum.Bitrate) {
      options.bitrate?.let { bitrate ->
        if (bitrate is BitrateEnum) {
          result.putString("bitrate", bitrate.toString())
        } else if (bitrate is BitrateNumber) {
          result.putInt("bitrate", bitrate.value)
        }
      }
    } else if (name == OptionNameEnum.BurstOption) {
      options.burstOption?.let {
        result.putMap("burstOption", toResult(burstOption = it))
      }
    } else if (name == OptionNameEnum.EthernetConfig) {
      options.ethernetConfig?.let {
        result.putMap("ethernetConfig", toResult(ethernetConfig = it))
      }
    } else if (name == OptionNameEnum.CameraLockConfig) {
      options.cameraLockConfig?.let {
        result.putMap("cameraLockConfig", toResult(cameraLockConfig = it))
      }
    } else if (name == OptionNameEnum.GpsInfo) {
      options.gpsInfo?.let {
        result.putMap(KEY_GPS_INFO, toResult(gpsInfo = it))
      }
    } else if (name == OptionNameEnum.MobileNetworkSetting) {
      options.mobileNetworkSetting?.let {
        result.putMap("mobileNetworkSetting", toResult(mobileNetworkSetting = it))
      }
    } else if (name == OptionNameEnum.OffDelay) {
      options.offDelay?.let {
        if (it is OffDelayEnum) {
          result.putString("offDelay", it.name)
        } else if (it is OffDelaySec) {
          result.putInt("offDelay", it.sec)
        }
      }
    } else if (name == OptionNameEnum.OffDelayUsb) {
      options.offDelayUsb?.let {
        if (it is OffDelayUsbEnum) {
          result.putString("offDelayUsb", it.name)
        } else if (it is OffDelayUsbSec) {
          result.putInt("offDelayUsb", it.sec)
        }
      }
    } else if (name == OptionNameEnum.Proxy) {
      options.proxy?.let {
        result.putMap(KEY_PROXY, toResult(proxy = it))
      }
    } else if (name == OptionNameEnum.SleepDelay) {
      options.sleepDelay?.let {
        if (it is SleepDelayEnum) {
          result.putString("sleepDelay", it.name)
        } else if (it is SleepDelaySec) {
          result.putInt("sleepDelay", it.sec)
        }
      }
    } else if (name == OptionNameEnum.TimeShift) {
      options.timeShift?.let {
        result.putMap("timeShift", toResult(timeShift = it))
      }
    } else if (name == OptionNameEnum.TopBottomCorrectionRotation) {
      options.topBottomCorrectionRotation?.let {
        result.putMap(KEY_TOP_BOTTOM_CORRECTION_ROTATION, toResult(rotation = it))
      }
    } else if (name == OptionNameEnum.TopBottomCorrectionRotationSupport) {
      options.topBottomCorrectionRotationSupport?.let {
        val json = toJson(rotationSupport = it)
        jsonResult.putString(KEY_TOP_BOTTOM_CORRECTION_ROTATION_SUPPORT, json)
      }
    } else if (name == OptionNameEnum.WlanFrequencyClMode) {
      options.wlanFrequencyClMode?.let {
        result.putMap(KEY_WLAN_FREQUENCY_CL_MODE, toResult(wlanFrequencyClMode = it))
      }
    } else if (valueOptions.contains(name)) {
      addOptionsValueToMap<Any>(options, name, result)
    } else if (supportOptions.keys.contains(name)) {
      addSupportOptionsValueToMap(options, name, result, supportOptions[name]!!)
    } else if (intValueRangeSupportOptions.contains(name)) {
      addValueRangeSupportOptionsValueToMap<Int>(options, name, result)
    } else {
      addOptionsEnumToMap(options, name, result)
    }
  }

  val response = Arguments.createMap()
  response.putMap("options", result)
  response.putMap("json", jsonResult)
  return response
}

fun <T : Enum<T>> addOptionsEnumToMap(options: Options, name: OptionNameEnum, objects: WritableMap) {
  val key = optionNameEnumToItemName[name]
  if (key == null) return
  options.getValue<T>(name)?.let { value ->
    objects.putString(key, value.toString())
  }
}

fun addSupportOptionsValueToMap(
  options: Options,
  name: OptionNameEnum,
  objects: WritableMap,
  valueClazz: KClass<*>
) {
  val key = optionNameEnumToItemName[name] ?: return
  options.getValue<List<*>>(name)?.let { list ->
    val array = Arguments.createArray()
    for (item in list) {
      when {
        // for enum value
        java.lang.Enum::class.java.isAssignableFrom(valueClazz.java) -> {
          array.pushString(item.toString())
        }
      }
    }
    objects.putArray(key, array)
  }
}

fun <T : Number> addValueRangeSupportOptionsValueToMap(
  options: Options,
  name: OptionNameEnum,
  objects: WritableMap
) {
  val key = optionNameEnumToItemName[name] ?: return
  options.getValue<ValueRange<T>>(name)?.let { value ->
    val result = Arguments.createMap()
    when (value.max) {
      is Int -> {
        result.putInt(KEY_MAX, value.max as Int)
        result.putInt(KEY_MIN, value.min as Int)
        result.putInt(KEY_STEP_SIZE, value.stepSize as Int)
      }

      is Double -> {
        result.putDouble(KEY_MAX, value.max as Double)
        result.putDouble(KEY_MIN, value.min as Double)
        result.putDouble(KEY_STEP_SIZE, value.stepSize as Double)
      }
    }
    objects.putMap(key, result)
  }
}


fun <T> addOptionsValueToMap(options: Options, name: OptionNameEnum, objects: WritableMap) {
  val key = optionNameEnumToItemName[name] ?: return
  options.getValue<T>(name)?.let { value ->
    when (value) {
      is String -> {
        objects.putString(key, value)
      }

      is Int -> {
        objects.putInt(key, value)
      }

      is Number -> {
        objects.putDouble(key, value.toDouble())
      }

      else -> {
        objects.putString(key, value.toString())
      }
    }
  }
}

fun toResult(accessInfo: AccessInfo): WritableMap {
  val result = Arguments.createMap()
  result.putString(KEY_SSID, accessInfo.ssid)
  result.putString(KEY_IP_ADDRESS, accessInfo.ipAddress)
  result.putString(KEY_SUBNET_MASK, accessInfo.subnetMask)
  result.putString(KEY_DEFAULT_GATEWAY, accessInfo.defaultGateway)
  accessInfo.dns1?.let {
    result.putString(KEY_DNS1, it)
  }
  accessInfo.dns2?.let {
    result.putString(KEY_DNS2, it)
  }
  result.putString("proxyURL", accessInfo.proxyURL)
  result.putString("frequency", accessInfo.frequency.name)
  result.putInt("wlanSignalStrength", accessInfo.wlanSignalStrength)
  result.putInt("wlanSignalLevel", accessInfo.wlanSignalLevel)
  result.putInt("lteSignalStrength", accessInfo.lteSignalStrength)
  result.putInt("lteSignalLevel", accessInfo.lteSignalLevel)

  accessInfo.dhcpLeaseAddress?.let { list ->
    val array = Arguments.createArray()
    list.forEach {
      array.pushMap(toResult(dhcpLeaseAddress = it))
    }
    if (array.size() > 0) {
      result.putArray(KEY_DHCP_LEASE_ADDRESS, array)
    }
  }
  return result
}

fun toResult(dhcpLeaseAddress: DhcpLeaseAddress): WritableMap {
  val result = Arguments.createMap()
  result.putString(KEY_IP_ADDRESS, dhcpLeaseAddress.ipAddress)
  result.putString(KEY_MAC_ADDRESS, dhcpLeaseAddress.macAddress)
  result.putString(KEY_HOST_NAME, dhcpLeaseAddress.hostName)
  return result
}

fun toResult(autoBracket: BracketSettingList): WritableArray {
  val resultList = Arguments.createArray()

  autoBracket.list?.forEach { bracketSetting ->
    val result = Arguments.createMap()
    bracketSetting.aperture?.name?.let { name ->
      result.putString("aperture", name)
    }
    bracketSetting.colorTemperature?.let { value ->
      result.putInt("colorTemperature", value)
    }
    bracketSetting.exposureCompensation?.name?.let { name ->
      result.putString("exposureCompensation", name)
    }
    bracketSetting.exposureProgram?.name?.let { name ->
      result.putString("exposureProgram", name)
    }
    bracketSetting.iso?.name?.let { name ->
      result.putString("iso", name)
    }
    bracketSetting.shutterSpeed?.name?.let { name ->
      result.putString("shutterSpeed", name)
    }
    bracketSetting.whiteBalance?.name?.let { name ->
      result.putString("whiteBalance", name)
    }
    resultList.pushMap(result)
  }

  return resultList
}

fun toResult(burstOption: BurstOption): WritableMap {
  val result = Arguments.createMap()
  burstOption.burstCaptureNum?.name?.let { name ->
    result.putString("burstCaptureNum", name)
  }
  burstOption.burstBracketStep?.name?.let { name ->
    result.putString("burstBracketStep", name)
  }
  burstOption.burstCompensation?.name?.let { name ->
    result.putString("burstCompensation", name)
  }
  burstOption.burstMaxExposureTime?.name?.let { name ->
    result.putString("burstMaxExposureTime", name)
  }
  burstOption.burstEnableIsoControl?.name?.let { name ->
    result.putString("burstEnableIsoControl", name)
  }
  burstOption.burstOrder?.name?.let { name ->
    result.putString("burstOrder", name)
  }
  return result
}

fun toResult(cameraLockConfig: CameraLockConfig): WritableMap {
  val result = Arguments.createMap()
  cameraLockConfig.isPowerKeyLocked?.let { isPowerKeyLocked ->
    result.putBoolean("isPowerKeyLocked", isPowerKeyLocked)
  }
  cameraLockConfig.isShutterKeyLocked?.let { isShutterKeyLocked ->
    result.putBoolean("isShutterKeyLocked", isShutterKeyLocked)
  }
  cameraLockConfig.isModeKeyLocked?.let { isModeKeyLocked ->
    result.putBoolean("isModeKeyLocked", isModeKeyLocked)
  }
  cameraLockConfig.isWlanKeyLocked?.let { isWlanKeyLocked ->
    result.putBoolean("isWlanKeyLocked", isWlanKeyLocked)
  }
  cameraLockConfig.isFnKeyLocked?.let { isFnKeyLocked ->
    result.putBoolean("isFnKeyLocked", isFnKeyLocked)
  }
  cameraLockConfig.isPanelLocked?.let { isPanelLocked ->
    result.putBoolean("isPanelLocked", isPanelLocked)
  }
  return result
}

fun toResult(fileInfo: FileInfo): ReadableMap {
  val result = Arguments.createMap()
  result.putString("name", fileInfo.name)
  result.putString("fileUrl", fileInfo.fileUrl)
  result.putDouble("size", fileInfo.size.toDouble())
  result.putString("dateTime", fileInfo.dateTime)
  fileInfo.dateTimeZone?.let {
    result.putString("dateTimeZone", it)
  }
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

fun toResult(ethernetConfig: EthernetConfig): WritableMap {
  val result = Arguments.createMap()
  result.putBoolean("usingDhcp", ethernetConfig.usingDhcp)
  ethernetConfig.ipAddress?.let { ipAddress ->
    result.putString(KEY_IP_ADDRESS, ipAddress)
  }
  ethernetConfig.subnetMask?.let { subnetMask ->
    result.putString(KEY_SUBNET_MASK, subnetMask)
  }
  ethernetConfig.defaultGateway?.let { defaultGateway ->
    result.putString(KEY_DEFAULT_GATEWAY, defaultGateway)
  }
  ethernetConfig.dns1?.let { dns1 ->
    result.putString(KEY_DNS1, dns1)
  }
  ethernetConfig.dns2?.let { dns2 ->
    result.putString(KEY_DNS2, dns2)
  }
  ethernetConfig.proxy?.let { proxy ->
    result.putMap(KEY_PROXY, toResult(proxy = proxy))
  }
  return result
}

fun toResult(gpsInfo: GpsInfo): WritableMap {
  val result = Arguments.createMap()
  result.putDouble("latitude", gpsInfo.latitude.toDouble())
  result.putDouble("longitude", gpsInfo.longitude.toDouble())
  result.putDouble("altitude", gpsInfo.altitude.toDouble())
  result.putString("dateTimeZone", gpsInfo.dateTimeZone)
  return result
}

fun toResult(stateGpsInfo: StateGpsInfo): WritableMap {
  val result = Arguments.createMap()
  stateGpsInfo.gpsInfo?.let {
    result.putMap(KEY_GPS_INFO, toResult(it))
  }
  return result
}

fun toResult(proxy: Proxy): WritableMap {
  val result = Arguments.createMap()
  result.putBoolean("use", proxy.use)
  proxy.url?.let { url ->
    result.putString("url", url)
  }
  proxy.port?.let { port ->
    result.putInt("port", port)
  }
  proxy.userid?.let { userid ->
    result.putString("userid", userid)
  }
  proxy.password?.let { password ->
    result.putString(KEY_PASSWORD, password)
  }
  return result
}

fun toResult(timeShift: TimeShiftSetting): WritableMap {
  val result = Arguments.createMap()
  timeShift.isFrontFirst?.let { value ->
    result.putBoolean("isFrontFirst", value)
  }
  timeShift.firstInterval?.let { value ->
    result.putString("firstInterval", value.toString())
  }
  timeShift.secondInterval?.let { value ->
    result.putString("secondInterval", value.toString())
  }
  return result
}

fun toResult(rotation: TopBottomCorrectionRotation): WritableMap {
  val result = Arguments.createMap()
  rotation.pitch?.let { value ->
    result.putDouble(KEY_TOP_BOTTOM_CORRECTION_ROTATION_PITCH, value.toDouble())
  }
  rotation.roll?.let { value ->
    result.putDouble(KEY_TOP_BOTTOM_CORRECTION_ROTATION_ROLL, value.toDouble())
  }
  rotation.yaw?.let { value ->
    result.putDouble(KEY_TOP_BOTTOM_CORRECTION_ROTATION_YAW, value.toDouble())
  }
  return result
}

/**
 * Avoided floating-point errors by converting to JSON string in the bridge,
 * then to object in TypeScript.
 */
fun toJson(rotationSupport: TopBottomCorrectionRotationSupport): String {
  val convertJson = """
    {
      "pitch": {
        "max": ${rotationSupport.pitch.max},
        "min": ${rotationSupport.pitch.min},
        "stepSize": ${rotationSupport.pitch.stepSize}
      },
      "roll": {
        "max": ${rotationSupport.roll.max},
        "min": ${rotationSupport.roll.min},
        "stepSize": ${rotationSupport.roll.stepSize}
      },
      "yaw": {
        "max": ${rotationSupport.yaw.max},
        "min": ${rotationSupport.yaw.min},
        "stepSize": ${rotationSupport.yaw.stepSize}
      }
    }
  """.trimIndent()
  return convertJson
}

fun toResult(state: ThetaState): WritableMap {
  val result = Arguments.createMap()
  state.fingerprint?.let {
    result.putString("fingerprint", it)
  }
  state.batteryLevel?.let {
    result.putDouble("batteryLevel", it.toDouble())
  }
  state.storageUri?.let {
    result.putString("storageUri", it)
  }
  state.storageID?.let {
    result.putString("storageID", it)
  }
  state.captureStatus?.let {
    result.putString("captureStatus", it.toString())
  }
  state.recordedTime?.let {
    result.putInt("recordedTime", it)
  }
  state.recordableTime?.let {
    result.putInt("recordableTime", it)
  }
  state.capturedPictures?.let {
    result.putInt("capturedPictures", it)
  }
  state.compositeShootingElapsedTime?.let {
    result.putString("compositeShootingElapsedTime", it.toString())
  }
  state.latestFileUrl?.let {
    result.putString("latestFileUrl", it)
  }
  state.chargingState?.let {
    result.putString("chargingState", it.toString())
  }
  state.apiVersion?.let {
    result.putInt("apiVersion", it)
  }
  state.isPluginRunning?.let {
    result.putBoolean("isPluginRunning", it)
  }
  state.isPluginWebServer?.let {
    result.putBoolean("isPluginWebServer", it)
  }
  state.function?.let {
    result.putString("function", it.toString())
  }
  state.isMySettingChanged?.let {
    result.putBoolean("isMySettingChanged", it)
  }
  state.currentMicrophone?.let {
    result.putString("currentMicrophone", it.toString())
  }
  state.isSdCard?.let {
    result.putBoolean("isSdCard", it)
  }
  state.cameraError?.let { list ->
    result.putArray("cameraError", Arguments.makeNativeArray(list.map { it.toString() }))
  }
  state.isBatteryInsert?.let {
    result.putString("isBatteryInsert", it.toString())
  }
  state.externalGpsInfo?.let {
    result.putMap(KEY_STATE_EXTERNAL_GPS_INFO, toResult(it))
  }
  state.internalGpsInfo?.let {
    result.putMap(KEY_STATE_INTERNAL_GPS_INFO, toResult(it))
  }
  state.boardTemp?.let {
    result.putInt(KEY_STATE_BOARD_TEMP, it)
  }
  state.batteryTemp?.let {
    result.putInt(KEY_STATE_BATTERY_TEMP, it)
  }
  return result
}

fun toResult(cameraEvent: CameraEvent): WritableMap {
  val result = Arguments.createMap()
  cameraEvent.options?.let {
    result.putMap("options", toResult(options = it).getMap("options"))
  }
  cameraEvent.state?.let {
    result.putMap("state", toResult(it))
  }
  return result
}

fun toResult(wlanFrequencyClMode: WlanFrequencyClMode): WritableMap {
  val result = Arguments.createMap()
  result.putBoolean(KEY_WLAN_FREQUENCY_CL_MODE_2_4, wlanFrequencyClMode.enable2_4)
  result.putBoolean(KEY_WLAN_FREQUENCY_CL_MODE_5_2, wlanFrequencyClMode.enable5_2)
  result.putBoolean(KEY_WLAN_FREQUENCY_CL_MODE_5_8, wlanFrequencyClMode.enable5_8)
  return result
}

fun toResult(mobileNetworkSetting: MobileNetworkSetting): WritableMap {
  val result = Arguments.createMap()
  mobileNetworkSetting.roaming?.name?.let { name ->
    result.putString("roaming", name)
  }
  mobileNetworkSetting.plan?.name?.let { name ->
    result.putString("plan", name)
  }
  return result
}

fun toSetOptionsParam(optionsMap: ReadableMap): Options {
  val result = Options()
  val iterator = optionsMap.keySetIterator()
  while (iterator.hasNextKey()) {
    val optionName = optionItemNameToEnum[iterator.nextKey()]
    OptionNameEnum.values().find { it == optionName }?.let {
      setOptionValue(result, it, optionsMap)
    }
  }
  return result
}

fun setOptionValue(options: Options, name: OptionNameEnum, optionsMap: ReadableMap) {
  val key = optionNameEnumToItemName[name]
  if (key == null) return

  val intOptions = listOf(
    OptionNameEnum.CaptureInterval,
    OptionNameEnum.CaptureNumber,
    OptionNameEnum.ColorTemperature,
    OptionNameEnum.CompositeShootingOutputInterval,
    OptionNameEnum.CompositeShootingTime,
    OptionNameEnum.RemainingPictures,
    OptionNameEnum.RemainingVideoSeconds,
    OptionNameEnum.ShutterVolume
  )
  val stringOptions = listOf(
    OptionNameEnum.DateTimeZone,
    OptionNameEnum.Password,
    OptionNameEnum.Username
  )
  val doubleOptions = listOf(
    OptionNameEnum.RemainingSpace,
    OptionNameEnum.TotalSpace
  )
  val boolOptions = listOf(
    OptionNameEnum.IsGpsOn
  )

  if (intOptions.contains(name)) {
    options.setValue(name, optionsMap.getInt(key))
  } else if (stringOptions.contains(name)) {
    options.setValue(name, optionsMap.getString(key) as Any)
  } else if (doubleOptions.contains(name)) {
    options.setValue(name, optionsMap.getDouble(key).toLong())
  } else if (boolOptions.contains(name)) {
    options.setValue(name, optionsMap.getBoolean(key))
  } else if (name == OptionNameEnum.AccessInfo) {
    optionsMap.getMap(key)?.let {
      options.setValue(name, toAccessInfo(map = it))
    }
  } else if (name == OptionNameEnum.AutoBracket) {
    optionsMap.getArray(key)?.let {
      options.setValue(name, toAutoBracket(list = it))
    }
  } else if (name == OptionNameEnum.Bitrate) {
    val type = optionsMap.getType(key)
    if (type == ReadableType.Number) {
      options.bitrate = BitrateNumber(optionsMap.getInt(key))
    } else if (type == ReadableType.String) {
      optionsMap.getString(key)?.let { rateStr ->
        options.bitrate = BitrateEnum.valueOf(rateStr)
      }
    }
  } else if (name == OptionNameEnum.BurstOption) {
    optionsMap.getMap(key)?.let {
      options.setValue(name, toBurstOption(map = it))
    }
  } else if (name == OptionNameEnum.CameraLockConfig) {
    optionsMap.getMap(key)?.let {
      options.setValue(name, toCameraLockConfig(map = it) as Any)
    }
  } else if (name == OptionNameEnum.EthernetConfig) {
    optionsMap.getMap(key)?.let {
      options.setValue(name, toEthernetConfig(map = it) as Any)
    }
  } else if (name == OptionNameEnum.GpsInfo) {
    optionsMap.getMap(key)?.let {
      options.setValue(name, toGpsInfo(map = it))
    }
  } else if (name == OptionNameEnum.MobileNetworkSetting) {
    optionsMap.getMap(key)?.let {
      options.setValue(name, toMobileNetworkSetting(map = it))
    }
  } else if (name == OptionNameEnum.OffDelay) {
    toOffDelay(optionsMap)?.let {
      options.setValue(name, it)
    }
  } else if (name == OptionNameEnum.OffDelayUsb) {
    toOffDelayUsb(optionsMap)?.let {
      options.setValue(name, it)
    }
  } else if (name == OptionNameEnum.Proxy) {
    optionsMap.getMap(key)?.let {
      options.setValue(name, toProxy(map = it) as Any)
    }
  } else if (name == OptionNameEnum.SleepDelay) {
    toSleepDelay(optionsMap)?.let {
      options.setValue(name, it)
    }
  } else if (name == OptionNameEnum.TimeShift) {
    optionsMap.getMap(key)?.let {
      options.setValue(name, toTimeShift(map = it))
    }
  } else if (name == OptionNameEnum.TopBottomCorrectionRotation) {
    optionsMap.getMap(key)?.let {
      options.setValue(name, toTopBottomCorrectionRotation(map = it))
    }
  } else if (name == OptionNameEnum.WlanFrequencyClMode) {
    optionsMap.getMap(key)?.let {
      options.setValue(name, toWlanFrequencyClMode(it))
    }
  } else {
    (optionsMap.getString(key))?.let { value ->
      getOptionValueEnum(name, value)?.let {
        options.setValue(name, it)
      }
    }
  }
}

fun getOptionValueEnum(name: OptionNameEnum, valueName: String): Any? {
  return when (name) {
    OptionNameEnum.AiAutoThumbnail -> AiAutoThumbnailEnum.values().find { it.name == valueName }
    OptionNameEnum.Aperture -> ApertureEnum.values().find { it.name == valueName }
    OptionNameEnum.BluetoothPower -> BluetoothPowerEnum.values().find { it.name == valueName }
    OptionNameEnum.BluetoothRole -> BluetoothRoleEnum.values().find { it.name == valueName }
    OptionNameEnum.BurstMode -> BurstModeEnum.values().find { it.name == valueName }
    OptionNameEnum.CameraControlSource -> CameraControlSourceEnum.values().find { it.name == valueName }
    OptionNameEnum.CameraLock -> CameraLockEnum.values().find { it.name == valueName }
    OptionNameEnum.CameraMode -> CameraModeEnum.values().find { it.name == valueName }
    OptionNameEnum.CameraPower -> CameraPowerEnum.values().find { it.name == valueName }
    OptionNameEnum.CaptureMode -> CaptureModeEnum.values().find { it.name == valueName }
    OptionNameEnum.CompassDirectionRef -> CompassDirectionRefEnum.values().find { it.name == valueName }
    OptionNameEnum.ContinuousNumber -> ContinuousNumberEnum.values().find { it.name == valueName }
    OptionNameEnum.ExposureCompensation -> ExposureCompensationEnum.values().find { it.name == valueName }
    OptionNameEnum.ExposureDelay -> ExposureDelayEnum.values().find { it.name == valueName }
    OptionNameEnum.ExposureProgram -> ExposureProgramEnum.values().find { it.name == valueName }
    OptionNameEnum.FaceDetect -> FaceDetectEnum.values().find { it.name == valueName }
    OptionNameEnum.FileFormat -> FileFormatEnum.values().find { it.name == valueName }
    OptionNameEnum.Filter -> FilterEnum.values().find { it.name == valueName }
    OptionNameEnum.Function -> ShootingFunctionEnum.values().find { it.name == valueName }
    OptionNameEnum.Gain -> GainEnum.values().find { it.name == valueName }
    OptionNameEnum.ImageStitching -> ImageStitchingEnum.values().find { it.name == valueName }
    OptionNameEnum.Iso -> IsoEnum.values().find { it.name == valueName }
    OptionNameEnum.IsoAutoHighLimit -> IsoAutoHighLimitEnum.values().find { it.name == valueName }
    OptionNameEnum.Language -> LanguageEnum.values().find { it.name == valueName }
    OptionNameEnum.LatestEnabledExposureDelayTime -> ExposureDelayEnum.values().find { it.name == valueName }
    OptionNameEnum.MaxRecordableTime -> MaxRecordableTimeEnum.values().find { it.name == valueName }
    OptionNameEnum.MicrophoneNoiseReduction -> MicrophoneNoiseReductionEnum.values().find { it.name == valueName }
    OptionNameEnum.NetworkType -> NetworkTypeEnum.values().find { it.name == valueName }
    OptionNameEnum.OffDelay -> OffDelayEnum.values().find { it.name == valueName }
    OptionNameEnum.OffDelayUsb -> OffDelayUsbEnum.entries.find { it.name == valueName }
    OptionNameEnum.PowerSaving -> PowerSavingEnum.values().find { it.name == valueName }
    OptionNameEnum.Preset -> PresetEnum.values().find { it.name == valueName }
    OptionNameEnum.PreviewFormat -> PreviewFormatEnum.values().find { it.name == valueName }
    OptionNameEnum.ShootingMethod -> ShootingMethodEnum.values().find { it.name == valueName }
    OptionNameEnum.ShutterSpeed -> ShutterSpeedEnum.values().find { it.name == valueName }
    OptionNameEnum.SleepDelay -> SleepDelayEnum.values().find { it.name == valueName }
    OptionNameEnum.TopBottomCorrection -> TopBottomCorrectionOptionEnum.values().find { it.name == valueName }
    OptionNameEnum.UsbConnection -> UsbConnectionEnum.entries.find { it.name == valueName }
    OptionNameEnum.VideoStitching -> VideoStitchingEnum.values().find { it.name == valueName }
    OptionNameEnum.VisibilityReduction -> VisibilityReductionEnum.values().find { it.name == valueName }
    OptionNameEnum.WhiteBalance -> WhiteBalanceEnum.values().find { it.name == valueName }
    OptionNameEnum.WhiteBalanceAutoStrength -> WhiteBalanceAutoStrengthEnum.values().find { it.name == valueName }
    OptionNameEnum.WlanAntennaConfig -> WlanAntennaConfigEnum.values().find { it.name == valueName }
    OptionNameEnum.WlanFrequency -> WlanFrequencyEnum.values().find { it.name == valueName }
    else -> null
  }
}

fun toListAccessPointsResult(accessPointList: List<AccessPoint>): WritableArray {
  val result = Arguments.createArray()
  accessPointList.forEach {
    val apinfo = Arguments.createMap()
    apinfo.putString(KEY_SSID, it.ssid)
    apinfo.putBoolean(KEY_SSID_STEALTH, it.ssidStealth)
    apinfo.putString(KEY_AUTH_MODE, it.authMode.toString())
    apinfo.putInt(KEY_CONNECTION_PRIORITY, it.connectionPriority)
    apinfo.putBoolean("usingDhcp", it.usingDhcp)
    apinfo.putString(KEY_IP_ADDRESS, it.ipAddress)
    apinfo.putString(KEY_SUBNET_MASK, it.subnetMask)
    apinfo.putString(KEY_DEFAULT_GATEWAY, it.defaultGateway)
    it.dns1?.let { dns1 ->
      apinfo.putString(KEY_DNS1, dns1)
    }
    it.dns2?.let { dns2 ->
      apinfo.putString(KEY_DNS2, dns2)
    }
    it.proxy?.let { proxy ->
      apinfo.putMap(KEY_PROXY, toResult(proxy = proxy))
    }

    result.pushMap(apinfo)
  }
  return result
}

fun toAccessInfo(map: ReadableMap): AccessInfo {
  val dhcpLeaseAddress = map.getArray(KEY_DHCP_LEASE_ADDRESS)?.let { array ->
    val list = mutableListOf<DhcpLeaseAddress>()
    for (i in 0 until array.size()) {
      array.getMap(i)?.let { map ->
        toDhcpLeaseAddress(map)?.let {
          list.add(it)
        }
      }
    }
    if (list.size > 0) list else null
  }

  return AccessInfo(
    ssid = map.getString(KEY_SSID) ?: "",
    ipAddress = map.getString(KEY_IP_ADDRESS) ?: "",
    subnetMask = map.getString(KEY_SUBNET_MASK) ?: "",
    defaultGateway = map.getString(KEY_DEFAULT_GATEWAY) ?: "",
    dns1 = map.getString(KEY_DNS1) ?: "",
    dns2 = map.getString(KEY_DNS2) ?: "",
    proxyURL = map.getString("proxyURL") ?: "",
    frequency = map.getString("frequency")?.let { WlanFrequencyAccessInfoEnum.valueOf(it) } ?: WlanFrequencyAccessInfoEnum.INITIAL_VALUE,
    wlanSignalStrength = map.getInt("wlanSignalStrength"),
    wlanSignalLevel = map.getInt("wlanSignalLevel"),
    lteSignalStrength = map.getInt("lteSignalStrength"),
    lteSignalLevel = map.getInt("lteSignalLevel"),
    dhcpLeaseAddress = dhcpLeaseAddress
  )
}

fun toDhcpLeaseAddress(value: ReadableMap): DhcpLeaseAddress? {
  val ipAddress = value.getString(KEY_IP_ADDRESS)
  val macAddress = value.getString(KEY_MAC_ADDRESS)
  val hostName = value.getString(KEY_HOST_NAME)
  if (ipAddress == null || macAddress == null || hostName == null) {
    return null
  }
  return DhcpLeaseAddress(ipAddress, macAddress, hostName)
}

fun toAutoBracket(list: ReadableArray): BracketSettingList {
  val autoBracket = BracketSettingList()
  for (i in 0 until list.size()) {
    list.getMap(i)?.let { setting ->
      autoBracket.add(
        BracketSetting(
          aperture = setting.getString("aperture")?.let { ApertureEnum.valueOf(it) },
          colorTemperature = if (setting.hasKey("colorTemperature")) setting.getInt("colorTemperature") else null,
          exposureCompensation = setting.getString("exposureCompensation")?.let { ExposureCompensationEnum.valueOf(it) },
          exposureProgram = setting.getString("exposureProgram")?.let { ExposureProgramEnum.valueOf(it) },
          iso = setting.getString("iso")?.let { IsoEnum.valueOf(it) },
          shutterSpeed = setting.getString("shutterSpeed")?.let { ShutterSpeedEnum.valueOf(it) },
          whiteBalance = setting.getString("whiteBalance")?.let { WhiteBalanceEnum.valueOf(it) },
        )
      )
    }
  }
  return autoBracket
}

fun toCameraLockConfig(map: ReadableMap?): CameraLockConfig? {
  return map?.let {
    CameraLockConfig(
      isPowerKeyLocked = if (it.hasKey("isPowerKeyLocked")) it.getBoolean("isPowerKeyLocked") else null,
      isShutterKeyLocked = if (it.hasKey("isShutterKeyLocked")) it.getBoolean("isShutterKeyLocked") else null,
      isModeKeyLocked = if (it.hasKey("isModeKeyLocked")) it.getBoolean("isModeKeyLocked") else null,
      isWlanKeyLocked = if (it.hasKey("isWlanKeyLocked")) it.getBoolean("isWlanKeyLocked") else null,
      isFnKeyLocked = if (it.hasKey("isFnKeyLocked")) it.getBoolean("isFnKeyLocked") else null,
      isPanelLocked = if (it.hasKey("isPanelLocked")) it.getBoolean("isPanelLocked") else null,
    )
  }
}

fun toEthernetConfig(map: ReadableMap?): EthernetConfig? {
  return map?.let {
    EthernetConfig(
      usingDhcp = if (it.hasKey("usingDhcp")) it.getBoolean("usingDhcp") else true,
      ipAddress = if (it.hasKey(KEY_IP_ADDRESS)) it.getString(KEY_IP_ADDRESS) else null,
      subnetMask = if (it.hasKey(KEY_SUBNET_MASK)) it.getString(KEY_SUBNET_MASK) else null,
      defaultGateway = if (it.hasKey(KEY_DEFAULT_GATEWAY)) it.getString(KEY_DEFAULT_GATEWAY) else null,
      dns1 = if (it.hasKey(KEY_DNS1)) it.getString(KEY_DNS1) else null,
      dns2 = if (it.hasKey(KEY_DNS2)) it.getString(KEY_DNS2) else null,
      proxy = if (it.hasKey(KEY_PROXY)) toProxy(map = it.getMap(KEY_PROXY)) else null
    )
  }
}

fun toProxy(map: ReadableMap?): Proxy? {
  return map?.let {
    Proxy(
      use = it.getBoolean("use"),
      url = if (it.hasKey("url")) it.getString("url") else null,
      port = if (it.hasKey("port")) it.getInt("port") else null,
      userid = if (it.hasKey("userid")) it.getString("userid") else null,
      password = if (it.hasKey(KEY_PASSWORD)) it.getString(KEY_PASSWORD) else null
    )
  }
}

fun toBurstOption(map: ReadableMap): BurstOption {
  return BurstOption(
    burstCaptureNum = map.getString("burstCaptureNum")?.let { BurstCaptureNumEnum.valueOf(it) },
    burstBracketStep = map.getString("burstBracketStep")?.let { BurstBracketStepEnum.valueOf(it) },
    burstCompensation = map.getString("burstCompensation")?.let { BurstCompensationEnum.valueOf(it) },
    burstMaxExposureTime = map.getString("burstMaxExposureTime")?.let { BurstMaxExposureTimeEnum.valueOf(it) },
    burstEnableIsoControl = map.getString("burstEnableIsoControl")?.let { BurstEnableIsoControlEnum.valueOf(it) },
    burstOrder = map.getString("burstOrder")?.let { BurstOrderEnum.valueOf(it) }
  )
}

fun toTimeShift(map: ReadableMap): TimeShiftSetting {
  val timeShift = TimeShiftSetting()
  if (map.hasKey("isFrontFirst")) {
    timeShift.isFrontFirst = map.getBoolean("isFrontFirst")
  }
  map.getString("firstInterval")?.let {
    timeShift.firstInterval = TimeShiftIntervalEnum.valueOf(it)
  }
  map.getString("secondInterval")?.let {
    timeShift.secondInterval = TimeShiftIntervalEnum.valueOf(it)
  }
  return timeShift
}

fun toTopBottomCorrectionRotation(map: ReadableMap): TopBottomCorrectionRotation {
  val pitch = map.getDouble(KEY_TOP_BOTTOM_CORRECTION_ROTATION_PITCH)?.toFloat() ?: 0.0f
  val roll = map.getDouble(KEY_TOP_BOTTOM_CORRECTION_ROTATION_ROLL)?.toFloat() ?: 0.0f
  val yaw = map.getDouble(KEY_TOP_BOTTOM_CORRECTION_ROTATION_YAW)?.toFloat() ?: 0.0f
  return TopBottomCorrectionRotation(pitch = pitch, roll = roll, yaw = yaw)
}

fun toMobileNetworkSetting(map: ReadableMap): MobileNetworkSetting {
  return MobileNetworkSetting(
    roaming = map.getString("roaming")?.let { RoamingEnum.valueOf(it) },
    plan = map.getString("plan")?.let { PlanEnum.valueOf(it) }
  )
}

fun configToTheta(objects: ReadableMap): Config {
  val config = Config()
  config.dateTime = objects.getString("dateTime")

  objects.getString("language")?.let {
    config.language = LanguageEnum.valueOf(it)
  }

  config.offDelay = toOffDelay(objects)

  config.sleepDelay = toSleepDelay(objects)

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
  val password = if (objects.hasKey(KEY_PASSWORD)) {
    objects.getString(KEY_PASSWORD)
  } else {
    null
  }
  return DigestAuth(username, password)
}

fun timeoutToTheta(objects: ReadableMap): Timeout {
  return Timeout(
    objects.getInt("connectTimeout").toLong(),
    objects.getInt("requestTimeout").toLong(),
    objects.getInt("socketTimeout").toLong(),
  )
}

fun toOffDelay(objects: ReadableMap): OffDelay? {
  if (!objects.hasKey("offDelay")) {
    return null
  }
  when (objects.getType("offDelay")) {
    ReadableType.String -> {
      (objects.getString("offDelay"))?.let { value ->
        return getOptionValueEnum(OptionNameEnum.OffDelay, value) as? OffDelayEnum
      }
    }

    ReadableType.Number -> {
      return OffDelaySec(objects.getInt("offDelay"))
    }

    else -> {}
  }
  return null
}

fun toOffDelayUsb(objects: ReadableMap): OffDelayUsb? {
  if (!objects.hasKey("offDelayUsb")) {
    return null
  }
  when (objects.getType("offDelayUsb")) {
    ReadableType.String -> {
      (objects.getString("offDelayUsb"))?.let { value ->
        return getOptionValueEnum(OptionNameEnum.OffDelayUsb, value) as? OffDelayUsbEnum
      }
    }

    ReadableType.Number -> {
      return OffDelayUsbSec(objects.getInt("offDelayUsb"))
    }

    else -> {}
  }
  return null
}

fun toSleepDelay(objects: ReadableMap): SleepDelay? {
  if (!objects.hasKey("sleepDelay")) {
    return null
  }
  when (objects.getType("sleepDelay")) {
    ReadableType.String -> {
      (objects.getString("sleepDelay"))?.let { value ->
        return getOptionValueEnum(OptionNameEnum.SleepDelay, value) as? SleepDelayEnum
      }
    }

    ReadableType.Number -> {
      return SleepDelaySec(objects.getInt("sleepDelay"))
    }

    else -> {}
  }
  return null
}

data class SetAccessPointParams(
  val ssid: String,
  val ssidStealth: Boolean?,
  val authMode: AuthModeEnum?,
  val password: String?,
  val connectionPriority: Int?,
  val dns1: String?,
  val dns2: String?,
  val proxy: Proxy?,
)

fun toSetAccessPointParams(objects: ReadableMap): SetAccessPointParams {
  val ssid = objects.getString(KEY_SSID) ?: throw IllegalArgumentException(KEY_SSID)
  val ssidStealth = if (objects.hasKey(KEY_SSID_STEALTH)) {
    objects.getBoolean(KEY_SSID_STEALTH)
  } else {
    null
  }
  val authMode = objects.getString(KEY_AUTH_MODE)?.let { AuthModeEnum.valueOf(it) }
  val password = objects.getString(KEY_PASSWORD)
  val connectionPriority = if (objects.hasKey(KEY_CONNECTION_PRIORITY)) {
    objects.getInt(KEY_CONNECTION_PRIORITY)
  } else {
    null
  }
  val dns1 = objects.getString(KEY_DNS1)
  val dns2 = objects.getString(KEY_DNS2)
  val proxy = if (objects.hasKey(KEY_PROXY)) toProxy(objects.getMap(KEY_PROXY)) else null

  return SetAccessPointParams(
    ssid = ssid,
    ssidStealth = ssidStealth,
    authMode = authMode,
    password = password,
    connectionPriority = connectionPriority,
    dns1 = dns1,
    dns2 = dns2,
    proxy = proxy,
  )
}

data class SetAccessPointStaticallyParams(
  val ipAddress: String,
  val subnetMask: String,
  val defaultGateway: String,
)

fun toSetAccessPointStaticallyParams(objects: ReadableMap): SetAccessPointStaticallyParams {
  val ipAddress = objects.getString(KEY_IP_ADDRESS) ?: throw IllegalArgumentException(KEY_IP_ADDRESS)
  val subnetMask = objects.getString(KEY_SUBNET_MASK) ?: throw IllegalArgumentException(KEY_SUBNET_MASK)
  val defaultGateway = objects.getString(KEY_DEFAULT_GATEWAY) ?: throw IllegalArgumentException(KEY_DEFAULT_GATEWAY)

  return SetAccessPointStaticallyParams(
    ipAddress = ipAddress,
    subnetMask = subnetMask,
    defaultGateway = defaultGateway,
  )
}

fun toWlanFrequencyClMode(objects: ReadableMap): WlanFrequencyClMode {
  val enable2_4 = objects.getBoolean(KEY_WLAN_FREQUENCY_CL_MODE_2_4)
  val enable5_2 = objects.getBoolean(KEY_WLAN_FREQUENCY_CL_MODE_5_2)
  val enable5_8 = objects.getBoolean(KEY_WLAN_FREQUENCY_CL_MODE_5_8)
  return WlanFrequencyClMode(enable2_4 = enable2_4, enable5_2 = enable5_2, enable5_8 = enable5_8)
}
