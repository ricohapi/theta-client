package com.ricoh360.thetaclient.theta_client_flutter

import com.ricoh360.thetaclient.DigestAuth
import com.ricoh360.thetaclient.ThetaRepository.*
import com.ricoh360.thetaclient.capture.Capture
import com.ricoh360.thetaclient.capture.PhotoCapture
import com.ricoh360.thetaclient.capture.VideoCapture
import io.flutter.plugin.common.MethodCall

const val KEY_CLIENT_MODE = "clientMode"

fun toResult(thetaInfo: ThetaInfo): Map<String, Any?> {
    return mapOf<String, Any?>(
        "manufacturer" to thetaInfo.manufacturer,
        "model" to thetaInfo.model,
        "serialNumber" to thetaInfo.serialNumber,
        "wlanMacAddress" to thetaInfo.wlanMacAddress,
        "bluetoothMacAddress" to thetaInfo.bluetoothMacAddress,
        "firmwareVersion" to thetaInfo.firmwareVersion,
        "supportUrl" to thetaInfo.supportUrl,
        "hasGps" to thetaInfo.hasGps,
        "hasGyro" to thetaInfo.hasGyro,
        "uptime" to thetaInfo.uptime,
        "api" to thetaInfo.api,
        "endpoints" to mapOf<String, Int>(
            "httpPort" to thetaInfo.endpoints.httpPort,
            "httpUpdatesPort" to thetaInfo.endpoints.httpUpdatesPort
        ),
        "apiLevel" to thetaInfo.apiLevel
    )
}

fun toCameraErrorList(cameraErrorList: List<CameraErrorEnum>?): List<String>? {
    if (cameraErrorList == null) {
        return null
    }
    val result = mutableListOf<String>()
    cameraErrorList.forEach {
        result.add(it.name)
    }
    return result
}

fun toResult(thetaState: ThetaState): Map<String, Any?> {
    return mapOf<String, Any?>(
        "fingerprint" to thetaState.fingerprint,
        "batteryLevel" to thetaState.batteryLevel,
        "storageUri" to thetaState.storageUri,
        "storageID" to thetaState.storageID,
        "captureStatus" to thetaState.captureStatus.name,
        "recordedTime" to thetaState.recordedTime,
        "recordableTime" to thetaState.recordableTime,
        "capturedPictures" to thetaState.capturedPictures,
        "compositeShootingElapsedTime" to thetaState.compositeShootingElapsedTime,
        "latestFileUrl" to thetaState.latestFileUrl,
        "chargingState" to thetaState.chargingState.name,
        "apiVersion" to thetaState.apiVersion,
        "isPluginRunning" to thetaState.isPluginRunning,
        "isPluginWebServer" to thetaState.isPluginWebServer,
        "function" to thetaState.function?.name,
        "isMySettingChanged" to thetaState.isMySettingChanged,
        "currentMicrophone" to thetaState.currentMicrophone?.name,
        "isSdCard" to thetaState.isSdCard,
        "cameraError" to toCameraErrorList(thetaState.cameraError),
        "isBatteryInsert" to thetaState.isBatteryInsert,
    )
}

fun toResult(fileInfoList: List<FileInfo>): List<Map<String, Any>> {
    val result = mutableListOf<Map<String, Any>>()
    fileInfoList.forEach {
        val map = mutableMapOf<String, Any>(
            "name" to it.name,
            "fileUrl" to it.fileUrl,
            "size" to it.size,
            "dateTime" to it.dateTime,
            "thumbnailUrl" to it.thumbnailUrl,
        )
        it.lat?.run { map.put("lat", this) }
        it.lng?.run { map.put("lng", this) }
        it.width?.run { map.put("width", this) }
        it.height?.run { map.put("height", this) }
        it.intervalCaptureGroupId?.run { map.put("intervalCaptureGroupId", this) }
        it.compositeShootingGroupId?.run { map.put("compositeShootingGroupId", this) }
        it.autoBracketGroupId?.run { map.put("autoBracketGroupId", this) }
        it.recordTime?.run { map.put("recordTime", this) }
        it.isProcessed?.run { map.put("isProcessed", this) }
        it.previewUrl?.run { map.put("previewUrl", this) }
        it.codec?.run { map.put("codec", this.name) }
        it.projectionType?.run { map.put("projectionType", this.name) }
        it.continuousShootingGroupId?.run { map.put("continuousShootingGroupId", this) }
        it.frameRate?.run { map.put("frameRate", this) }
        it.favorite?.run { map.put("favorite", this) }
        it.imageDescription?.run { map.put("imageDescription", this) }
        it.storageID?.run { map.put("storageID", this) }
        result.add(map)
    }
    return result
}

fun toGpsInfo(map: Map<String, Any>): GpsInfo {
    return GpsInfo(
        latitude = (map["latitude"] as Double).toFloat(),
        longitude = (map["longitude"] as Double).toFloat(),
        altitude = (map["altitude"] as Double).toFloat(),
        dateTimeZone = map["dateTimeZone"] as String
    )
}

fun toProxy(map: Map<String, Any>): Proxy {
    return Proxy(
        use = map["use"] as? Boolean ?: false,
        url = map["url"] as? String,
        port = map["port"] as? Int,
        userid = map["userid"] as? String,
        password = map["password"] as? String
    )
}

fun toTimeShift(map: Map<String, Any>): TimeShiftSetting {
    var timeShift = TimeShiftSetting()
    map["isFrontFirst"]?.let {
        timeShift.isFrontFirst = it as Boolean
    }
    map["firstInterval"]?.let {
        timeShift.firstInterval = TimeShiftIntervalEnum.valueOf(it as String)
    }
    map["secondInterval"]?.let {
        timeShift.secondInterval = TimeShiftIntervalEnum.valueOf(it as String)
    }
    return timeShift
}

fun <T> setCaptureBuilderParams(call: MethodCall, builder: Capture.Builder<T>) {
    call.argument<String>(OptionNameEnum.Aperture.name)?.also { enumName ->
        ApertureEnum.values().find { it.name == enumName }?.let {
            builder.setAperture(it)
        }
    }
    call.argument<Int>(OptionNameEnum.ColorTemperature.name)?.also {
        builder.setColorTemperature(it)
    }
    call.argument<String>(OptionNameEnum.ExposureDelay.name)?.also { enumName ->
        ExposureDelayEnum.values().find { it.name == enumName }?.let {
            builder.setExposureDelay(it)
        }
    }
    call.argument<String>(OptionNameEnum.ExposureProgram.name)?.also { enumName ->
        ExposureProgramEnum.values().find { it.name == enumName }?.let {
            builder.setExposureProgram(it)
        }
    }
    call.argument<Map<String, Any>>(OptionNameEnum.GpsInfo.name)?.also {
        builder.setGpsInfo(toGpsInfo(it))
    }
    call.argument<String>("GpsTagRecording")?.also { enumName ->
        GpsTagRecordingEnum.values().find { it.name == enumName }?.let {
            builder.setGpsTagRecording(it)
        }
    }
    call.argument<String>(OptionNameEnum.Iso.name)?.also { enumName ->
        IsoEnum.values().find { it.name == enumName }?.let {
            builder.setIso(it)
        }
    }
    call.argument<String>(OptionNameEnum.IsoAutoHighLimit.name)?.also { enumName ->
        IsoAutoHighLimitEnum.values().find { it.name == enumName }?.let {
            builder.setIsoAutoHighLimit(it)
        }
    }
    call.argument<String>(OptionNameEnum.WhiteBalance.name)?.also { enumName ->
        WhiteBalanceEnum.values().find { it.name == enumName }?.let {
            builder.setWhiteBalance(it)
        }
    }
}

fun setPhotoCaptureBuilderParams(call: MethodCall, builder: PhotoCapture.Builder) {
    call.argument<String>(OptionNameEnum.Filter.name)?.let { enumName ->
        FilterEnum.values().find { it.name == enumName }?.let {
            builder.setFilter(it)
        }
    }
    call.argument<String>("PhotoFileFormat")?.let { enumName ->
        PhotoFileFormatEnum.values().find { it.name == enumName }?.let {
            builder.setFileFormat(it)
        }
    }
}

fun setVideoCaptureBuilderParams(call: MethodCall, builder: VideoCapture.Builder) {
    call.argument<String>(OptionNameEnum.MaxRecordableTime.name)?.let { enumName ->
        MaxRecordableTimeEnum.values().find { it.name == enumName }?.let {
            builder.setMaxRecordableTime(it)
        }
    }
    call.argument<String>("VideoFileFormat")?.let { enumName ->
        VideoFileFormatEnum.values().find { it.name == enumName }?.let {
            builder.setFileFormat(it)
        }
    }
}

fun toGetOptionsParam(data: List<String>): List<OptionNameEnum> {
    val optionNames = mutableListOf<OptionNameEnum>()
    data.forEach { name ->
        OptionNameEnum.values().find { it.name == name }?.let {
            optionNames.add(it)
        }
    }
    return optionNames
}

fun toResult(gpsInfo: GpsInfo): Map<String, Any> {
    return mapOf(
        "latitude" to gpsInfo.latitude,
        "longitude" to gpsInfo.longitude,
        "altitude" to gpsInfo.altitude,
        "dateTimeZone" to gpsInfo.dateTimeZone
    )
}

fun toResult(proxy: Proxy): Map<String, Any?> {
    return mapOf(
        "use" to proxy.use,
        "url" to proxy.url,
        "port" to proxy.port,
        "userid" to proxy.userid,
        "password" to proxy.password
    )
}

fun toResult(timeShift: TimeShiftSetting): Map<String, Any> {
    val result = mutableMapOf<String, Any>()
    timeShift.isFrontFirst?.let { value ->
        result["isFrontFirst"] = value
    }
    timeShift.firstInterval?.let { value ->
        result["firstInterval"] = value.toString()
    }
    timeShift.secondInterval?.let { value ->
        result["secondInterval"] = value.toString()
    }
    return result
}

fun toResult(options: Options): Map<String, Any> {
    val result = mutableMapOf<String, Any>()

    val valueOptions = listOf(
        OptionNameEnum.ColorTemperature,
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
    OptionNameEnum.values().forEach { name ->
        if (name == OptionNameEnum.GpsInfo) {
            options.getValue<GpsInfo>(OptionNameEnum.GpsInfo)?.let { gpsInfo ->
                result[OptionNameEnum.GpsInfo.name] = toResult(gpsInfo)
            }
        } else if (name == OptionNameEnum.Proxy) {
            options.getValue<Proxy>(OptionNameEnum.Proxy)?.let { proxy ->
                result[OptionNameEnum.Proxy.name] = toResult(proxy)
            }
        } else if (name == OptionNameEnum.TimeShift) {
            options.getValue<TimeShiftSetting>(OptionNameEnum.TimeShift)?.let { timeShift ->
                result[OptionNameEnum.TimeShift.name] = toResult(timeShift)
            }
        } else if (valueOptions.contains(name)) {
            addOptionsValueToMap<Any>(options, name, result)
        } else {
            addOptionsEnumToMap(options, name, result)
        }
    }
    return result
}

fun <T : Enum<T>> addOptionsEnumToMap(options: Options, name: OptionNameEnum, map: MutableMap<String, Any>) {
    options.getValue<T>(name)?.let {
        map[name.name] = it.name
    }
}

fun <T> addOptionsValueToMap(options: Options, name: OptionNameEnum, map: MutableMap<String, Any>) {
    options.getValue<T>(name)?.let {
        map[name.name] = it
    }
}

fun toSetOptionsParam(data: Map<String, Any>): Options {
    val options = Options()
    data.forEach { (key, value) ->
        OptionNameEnum.values().find { it.name == key }?.let {
            setOptionValue(options, it, value)
        }
    }
    return options
}

fun setOptionValue(options: Options, name: OptionNameEnum, value: Any) {
    val valueOptions = listOf(
        OptionNameEnum.ColorTemperature,
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
    if (valueOptions.contains(name)) {
        var optionValue = value
        if (name.valueType == Long::class && value is Int) {
            optionValue = value.toLong()
        }
        options.setValue(name, optionValue)
    } else if (name == OptionNameEnum.GpsInfo) {
        @Suppress("UNCHECKED_CAST")
        options.setValue(name, toGpsInfo(value as Map<String, Any>))
    } else if (name == OptionNameEnum.Proxy) {
        @Suppress("UNCHECKED_CAST")
        options.setValue(name, toProxy(value as Map<String, Any>))
    } else if (name == OptionNameEnum.TimeShift) {
        @Suppress("UNCHECKED_CAST")
        options.setValue(name, toTimeShift(value as Map<String, Any>))
    } else {
        getOptionValueEnum(name, value as String)?.let {
            options.setValue(name, it)
        }
    }
}

fun getOptionValueEnum(name: OptionNameEnum, valueName: String): Any? {
    return when (name) {
        OptionNameEnum.Aperture -> ApertureEnum.values().find { it.name == valueName }
        OptionNameEnum.CameraControlSource -> CameraControlSourceEnum.values().find { it.name == valueName }
        OptionNameEnum.CameraMode -> CameraModeEnum.values().find { it.name == valueName }
        OptionNameEnum.CaptureMode -> CaptureModeEnum.values().find { it.name == valueName }
        OptionNameEnum.ExposureCompensation -> ExposureCompensationEnum.values().find { it.name == valueName }
        OptionNameEnum.ExposureDelay -> ExposureDelayEnum.values().find { it.name == valueName }
        OptionNameEnum.ExposureProgram -> ExposureProgramEnum.values().find { it.name == valueName }
        OptionNameEnum.FileFormat -> FileFormatEnum.values().find { it.name == valueName }
        OptionNameEnum.Filter -> FilterEnum.values().find { it.name == valueName }
        OptionNameEnum.Iso -> IsoEnum.values().find { it.name == valueName }
        OptionNameEnum.IsoAutoHighLimit -> ApertureEnum.values().find { it.name == valueName }
        OptionNameEnum.Language -> LanguageEnum.values().find { it.name == valueName }
        OptionNameEnum.MaxRecordableTime -> MaxRecordableTimeEnum.values().find { it.name == valueName }
        OptionNameEnum.NetworkType -> NetworkTypeEnum.values().find { it.name == valueName }
        OptionNameEnum.OffDelay -> OffDelayEnum.values().find { it.name == valueName }
        OptionNameEnum.PowerSaving -> PowerSavingEnum.values().find { it.name == valueName }
        OptionNameEnum.PreviewFormat -> PreviewFormatEnum.values().find { it.name == valueName }
        OptionNameEnum.ShootingMethod -> ShootingMethodEnum.values().find { it.name == valueName }
        OptionNameEnum.ShutterSpeed -> ShutterSpeedEnum.values().find { it.name == valueName }
        OptionNameEnum.SleepDelay -> SleepDelayEnum.values().find { it.name == valueName }
        OptionNameEnum.WhiteBalance -> WhiteBalanceEnum.values().find { it.name == valueName }
        OptionNameEnum.WhiteBalanceAutoStrength -> WhiteBalanceAutoStrengthEnum.values().find { it.name == valueName }
        OptionNameEnum.WlanFrequency -> WlanFrequencyEnum.values().find { it.name == valueName }
        else -> null
    }
}

fun toDigestAuthParam(data: Map<*, *>): DigestAuth? {
    val username = data["username"] as? String ?: run {
        return null
    }
    val password = data["password"] as? String
    return DigestAuth(username, password)
}

fun toConfigParam(data: Map<String, Any>): Config {
    val config = Config()
    data.forEach { (key, value) ->
        when (key) {
            OptionNameEnum.DateTimeZone.name -> config.dateTime = value.toString()
            OptionNameEnum.Language.name -> config.language =
                getOptionValueEnum(OptionNameEnum.Language, value as String) as LanguageEnum?
            OptionNameEnum.OffDelay.name -> config.offDelay =
                getOptionValueEnum(OptionNameEnum.OffDelay, value as String) as OffDelayEnum?
            OptionNameEnum.SleepDelay.name -> config.sleepDelay =
                getOptionValueEnum(OptionNameEnum.SleepDelay, value as String) as SleepDelayEnum?
            OptionNameEnum.ShutterVolume.name -> config.shutterVolume = value as Int
            KEY_CLIENT_MODE -> config.clientMode = toDigestAuthParam(value as Map<*, *>)
        }
    }
    return config
}

fun toTimeoutParam(map: Map<String, Any>): Timeout {
    return Timeout(
        connectTimeout = map["connectTimeout"]!!.let { (it as Number).toLong() },
        requestTimeout = map["requestTimeout"]!!.let { (it as Number).toLong() },
        socketTimeout = map["socketTimeout"]!!.let { (it as Number).toLong() }
    )
}

fun toResult(exif: Exif): Map<String, Any> {
    val result = mutableMapOf<String, Any>()
    result["exifVersion"] = exif.exifVersion
    result["dateTime"] = exif.dateTime
    exif.imageWidth?.let {
        result["imageWidth"] = it
    }
    exif.imageLength?.let {
        result["imageLength"] = it
    }
    exif.gpsLatitude?.let {
        result["gpsLatitude"] = it
    }
    exif.gpsLongitude?.let {
        result["gpsLongitude"] = it
    }
    return result
}

fun toResult(xmp: Xmp): Map<String, Any> {
    val result = mutableMapOf<String, Any>()
    result["fullPanoWidthPixels"] = xmp.fullPanoWidthPixels
    result["fullPanoHeightPixels"] = xmp.fullPanoHeightPixels
    xmp.poseHeadingDegrees?.let {
        result["poseHeadingDegrees"] = it
    }
    return result
}

fun toResult(metadata: Pair<Exif, Xmp>): Map<String, Any> {
    return mapOf<String, Any>(
        "exif" to toResult(metadata.first),
        "xmp" to toResult(metadata.second),
    )
}

fun toListAccessPointsResult(accessPointList: List<AccessPoint>): List<Map<String, Any>> {
    val resultList = mutableListOf<Map<String, Any>>()
    accessPointList.forEach { accessPoint ->
        val result = mutableMapOf<String, Any>()
        result["ssid"] = accessPoint.ssid
        result["ssidStealth"] = accessPoint.ssidStealth
        result["authMode"] = accessPoint.authMode.name
        result["connectionPriority"] = accessPoint.connectionPriority
        result["usingDhcp"] = accessPoint.usingDhcp
        accessPoint.ipAddress?.let {
            result["ipAddress"] = it
        }
        accessPoint.subnetMask?.let {
            result["subnetMask"] = it
        }
        accessPoint.defaultGateway?.let {
            result["defaultGateway"] = it
        }
        accessPoint.proxy?.let {
            result["proxy"] = toResult(proxy = it)
        }
        resultList.add(result)
    }
    return resultList
}

fun toPluginInfosResult(pluginInfoList: List<PluginInfo>): List<Map<String, Any>> {
    val resultList = mutableListOf<Map<String, Any>>()
    pluginInfoList.forEach { pluginInfo ->
        val result = mutableMapOf<String, Any>()
        result["name"] = pluginInfo.name
        result["packageName"] = pluginInfo.packageName
        result["version"] = pluginInfo.version
        result["isPreInstalled"] = pluginInfo.isPreInstalled
        result["isRunning"] = pluginInfo.isRunning
        result["isForeground"] = pluginInfo.isForeground
        result["isBoot"] = pluginInfo.isBoot
        result["hasWebServer"] = pluginInfo.hasWebServer
        result["exitStatus"] = pluginInfo.exitStatus
        result["message"] = pluginInfo.message
        resultList.add(result)
    }
    return resultList
}