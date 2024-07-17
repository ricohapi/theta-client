package com.ricoh360.thetaclient.theta_client_flutter

import com.ricoh360.thetaclient.DigestAuth
import com.ricoh360.thetaclient.ThetaRepository.*
import com.ricoh360.thetaclient.capture.*
import io.flutter.plugin.common.MethodCall

const val KEY_CLIENT_MODE = "clientMode"
const val KEY_NOTIFY_ID = "id"
const val KEY_NOTIFY_PARAMS = "params"
const val KEY_NOTIFY_PARAM_COMPLETION = "completion"
const val KEY_NOTIFY_PARAM_IMAGE = "image"
const val KEY_NOTIFY_PARAM_MESSAGE = "message"
const val KEY_NOTIFY_PARAM_STATUS = "status"
const val KEY_GPS_INFO = "gpsInfo"
const val KEY_STATE_EXTERNAL_GPS_INFO = "externalGpsInfo"
const val KEY_STATE_INTERNAL_GPS_INFO = "internalGpsInfo"

fun toResult(thetaInfo: ThetaInfo): Map<String, Any?> {
    return mapOf(
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
        "endpoints" to mapOf(
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
    val result = mutableMapOf<String, Any>()
    thetaState.fingerprint?.let {
        result.put("fingerprint", it)
    }
    thetaState.batteryLevel?.let {
        result.put("batteryLevel", it)
    }
    thetaState.storageUri?.let {
        result.put("storageUri", it)
    }
    thetaState.storageID?.let {
        result.put("storageID", it)
    }
    thetaState.captureStatus?.let {
        result.put("captureStatus", it.name)
    }
    thetaState.recordedTime?.let {
        result.put("recordedTime", it)
    }
    thetaState.recordableTime?.let {
        result.put("recordableTime", it)
    }
    thetaState.capturedPictures?.let {
        result.put("capturedPictures", it)
    }
    thetaState.compositeShootingElapsedTime?.let {
        result.put("compositeShootingElapsedTime", it)
    }
    thetaState.latestFileUrl?.let {
        result.put("latestFileUrl", it)
    }
    thetaState.chargingState?.let {
        result.put("chargingState", it.name)
    }
    thetaState.apiVersion?.let {
        result.put("apiVersion", it)
    }
    thetaState.isPluginRunning?.let {
        result.put("isPluginRunning", it)
    }
    thetaState.isPluginWebServer?.let {
        result.put("isPluginWebServer", it)
    }
    thetaState.function?.let {
        result.put("function", it.name)
    }
    thetaState.isMySettingChanged?.let {
        result.put("isMySettingChanged", it)
    }
    thetaState.currentMicrophone?.let {
        result.put("currentMicrophone", it.name)
    }
    thetaState.isSdCard?.let {
        result.put("isSdCard", it)
    }
    thetaState.cameraError?.let {
        toCameraErrorList(it)?.let { list ->
            result.put("cameraError", list)
        }
    }
    thetaState.isBatteryInsert?.let {
        result.put("isBatteryInsert", it)
    }
    thetaState.externalGpsInfo?.let {
        result.put(KEY_STATE_EXTERNAL_GPS_INFO, toResult(it))
    }
    thetaState.internalGpsInfo?.let {
        result.put(KEY_STATE_INTERNAL_GPS_INFO, toResult(it))
    }
    return result
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
        it.dateTimeZone?.run { map.put("dateTimeZone", this) }
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

fun toAutoBracket(list: List<Map<String, Any>>): BracketSettingList {
    val autoBracket = BracketSettingList()

    list.forEach { map ->
        autoBracket.add(
            BracketSetting(
                aperture = (map["aperture"] as? String)?.let { ApertureEnum.valueOf(it) },
                colorTemperature = map["colorTemperature"] as? Int,
                exposureCompensation = (map["exposureCompensation"] as? String)?.let { ExposureCompensationEnum.valueOf(it) },
                exposureProgram = (map["exposureProgram"] as? String)?.let { ExposureProgramEnum.valueOf(it) },
                iso = (map["iso"] as? String)?.let { IsoEnum.valueOf(it) },
                shutterSpeed = (map["shutterSpeed"] as? String)?.let { ShutterSpeedEnum.valueOf(it) },
                whiteBalance = (map["whiteBalance"] as? String)?.let { WhiteBalanceEnum.valueOf(it) },
            )
        )
    }

    return autoBracket
}

fun toResult(autoBracket: BracketSettingList): List<Map<String, Any?>> {
    val resultList: MutableList<Map<String, Any?>> = mutableListOf()

    autoBracket.list.forEach { bracketSetting ->
        resultList.add(
            mapOf(
                "aperture" to bracketSetting.aperture?.name,
                "colorTemperature" to bracketSetting.colorTemperature,
                "exposureCompensation" to bracketSetting.exposureCompensation?.name,
                "exposureProgram" to bracketSetting.exposureProgram?.name,
                "iso" to bracketSetting.iso?.name,
                "shutterSpeed" to bracketSetting.shutterSpeed?.name,
                "whiteBalance" to bracketSetting.whiteBalance?.name
            )
        )
    }

    return resultList
}

fun toBurstOption(map: Map<String, Any>): BurstOption {
    return BurstOption(
        burstCaptureNum = (map["burstCaptureNum"] as? String)?.let { BurstCaptureNumEnum.valueOf(it) },
        burstBracketStep = (map["burstBracketStep"] as? String)?.let { BurstBracketStepEnum.valueOf(it) },
        burstCompensation = (map["burstCompensation"] as? String)?.let { BurstCompensationEnum.valueOf(it) },
        burstMaxExposureTime = (map["burstMaxExposureTime"] as? String)?.let { BurstMaxExposureTimeEnum.valueOf(it) },
        burstEnableIsoControl = (map["burstEnableIsoControl"] as? String)?.let { BurstEnableIsoControlEnum.valueOf(it) },
        burstOrder = (map["burstOrder"] as? String)?.let { BurstOrderEnum.valueOf(it) }
    )
}

fun toResult(burstOption: BurstOption): Map<String, Any?> {
    return mapOf(
        "burstCaptureNum" to burstOption.burstCaptureNum?.name,
        "burstBracketStep" to burstOption.burstBracketStep?.name,
        "burstCompensation" to burstOption.burstCompensation?.name,
        "burstMaxExposureTime" to burstOption.burstMaxExposureTime?.name,
        "burstEnableIsoControl" to burstOption.burstEnableIsoControl?.name,
        "burstOrder" to burstOption.burstOrder?.name
    )
}

fun toEthernetConfig(map: Map<String, Any>): EthernetConfig {
    val proxy = map["proxy"]?.let {
        @Suppress("UNCHECKED_CAST")
        (it as? Map<String, Any>)?.let{ map ->
            toProxy(map)
        }
    }
    return EthernetConfig(
        usingDhcp = map["usingDhcp"] as? Boolean ?: true,
        ipAddress = map["ipAddress"] as? String,
        subnetMask = map["subnetMask"] as? String,
        defaultGateway = map["defaultGateway"] as? String,
        proxy = proxy
    )
}

fun toGpsInfo(map: Map<String, Any>): GpsInfo {
    return GpsInfo(
        latitude = (map["latitude"] as Double).toFloat(),
        longitude = (map["longitude"] as Double).toFloat(),
        altitude = (map["altitude"] as Double).toFloat(),
        dateTimeZone = map["dateTimeZone"] as String
    )
}

fun toOffDelay(value: Any): OffDelay? {
    if (value is Int) {
        return OffDelaySec(value)
    } else if (value is String) {
        getOptionValueEnum(OptionNameEnum.OffDelay, value)?.let {
            return it as OffDelayEnum
        }
    }
    return null
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

fun toSleepDelay(value: Any): SleepDelay? {
    if (value is Int) {
        return SleepDelaySec(value)
    } else if (value is String) {
        getOptionValueEnum(OptionNameEnum.SleepDelay, value)?.let {
            return it as SleepDelayEnum
        }
    }
    return null
}

fun toTimeShift(map: Map<String, Any>): TimeShiftSetting {
    val timeShift = TimeShiftSetting()
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

fun toTopBottomCorrectionRotation(map: Map<String, Any>): TopBottomCorrectionRotation {
    return TopBottomCorrectionRotation(
        pitch = (map["pitch"] as Double).toFloat(),
        roll = (map["roll"] as Double).toFloat(),
        yaw = (map["yaw"] as Double).toFloat()
    )
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
    call.argument<String>(OptionNameEnum.ExposureCompensation.name)?.also { enumName ->
        ExposureCompensationEnum.values().find { it.name == enumName }?.let {
            builder.setExposureCompensation(it)
        }
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
    call.argument<Int>("_capture_interval")?.let {
        if (it >= 0) {
            builder.setCheckStatusCommandInterval(it.toLong())
        }
    }
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
    call.argument<String>(OptionNameEnum.Preset.name)?.let { enumName ->
        PresetEnum.values().find { it.name == enumName }?.let {
            builder.setPreset(it)
        }
    }
}

fun setTimeShiftCaptureBuilderParams(call: MethodCall, builder: TimeShiftCapture.Builder) {
    call.argument<Int>("_capture_interval")?.let {
        if (it >= 0) {
            builder.setCheckStatusCommandInterval(it.toLong())
        }
    }
    call.argument<Map<String, Any>>(OptionNameEnum.TimeShift.name)?.let { timeShiftMap ->
        val timeShift = toTimeShift(timeShiftMap)
        timeShift.isFrontFirst?.let {
            builder.setIsFrontFirst(it)
        }
        timeShift.firstInterval?.let {
            builder.setFirstInterval(it)
        }
        timeShift.secondInterval?.let {
            builder.setSecondInterval(it)
        }
    }
}

fun setVideoCaptureBuilderParams(call: MethodCall, builder: VideoCapture.Builder) {
    call.argument<Int>("_capture_interval")?.let {
        if (it >= 0) {
            builder.setCheckStatusCommandInterval(it.toLong())
        }
    }
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

fun setLimitlessIntervalCaptureBuilderParams(call: MethodCall, builder: LimitlessIntervalCapture.Builder) {
    call.argument<Int>("_capture_interval")?.let {
        if (it >= 0) {
            builder.setCheckStatusCommandInterval(it.toLong())
        }
    }
    call.argument<Int>(OptionNameEnum.CaptureInterval.name)?.also {
        builder.setCaptureInterval(it)
    }
}

fun setShotCountSpecifiedIntervalCaptureBuilderParams(call: MethodCall, builder: ShotCountSpecifiedIntervalCapture.Builder) {
    call.argument<Int>("_capture_interval")?.let {
        if (it >= 0) {
            builder.setCheckStatusCommandInterval(it.toLong())
        }
    }
    call.argument<Int>(OptionNameEnum.CaptureInterval.name)?.also {
        builder.setCaptureInterval(it)
    }
}

fun setCompositeIntervalCaptureBuilderParams(call: MethodCall, builder: CompositeIntervalCapture.Builder) {
    call.argument<Int>("_capture_interval")?.let {
        if (it >= 0) {
            builder.setCheckStatusCommandInterval(it.toLong())
        }
    }
    call.argument<Int>(OptionNameEnum.CompositeShootingOutputInterval.name)?.also {
        builder.setCompositeShootingOutputInterval(it)
    }
}

fun setBurstCaptureBuilderParams(call: MethodCall, builder: BurstCapture.Builder) {
    call.argument<Int>("_capture_interval")?.let {
        if (it >= 0) {
            builder.setCheckStatusCommandInterval(it.toLong())
        }
    }
    call.argument<String>(OptionNameEnum.BurstMode.name)?.let { enumName ->
        BurstModeEnum.values().find { it.name == enumName }?.let {
            builder.setBurstMode(it)
        }
    }
}

fun setMultiBracketCaptureBuilderParams(call: MethodCall, builder: MultiBracketCapture.Builder) {
    call.argument<Int>("_capture_interval")?.let {
        if (it >= 0) {
            builder.setCheckStatusCommandInterval(it.toLong())
        }
    }
    call.argument<List<Map<String, Any>>>(OptionNameEnum.AutoBracket.name)?.also {
        val autoBracket = toAutoBracket(it)
        autoBracket.list.forEach { setting ->
            builder.addBracketParameters(
                aperture = setting.aperture,
                colorTemperature = setting.colorTemperature,
                exposureCompensation = setting.exposureCompensation,
                exposureProgram = setting.exposureProgram,
                iso = setting.iso,
                shutterSpeed = setting.shutterSpeed,
                whiteBalance = setting.whiteBalance,
            )
        }
    }
}

fun setContinuousCaptureBuilderParams(call: MethodCall, builder: ContinuousCapture.Builder) {
    call.argument<Int>("_capture_interval")?.let {
        if (it >= 0) {
            builder.setCheckStatusCommandInterval(it.toLong())
        }
    }
    call.argument<String>("PhotoFileFormat")?.let { enumName ->
        PhotoFileFormatEnum.values().find { it.name == enumName }?.let {
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

fun toResult(ethernetConfig: EthernetConfig): Map<String, Any?> {
    val result = mutableMapOf<String, Any>()

    result["usingDhcp"] = ethernetConfig.usingDhcp

    ethernetConfig.ipAddress?.let { value ->
        result["ipAddress"] = value
    }
    ethernetConfig.subnetMask?.let { value ->
        result["subnetMask"] = value
    }
    ethernetConfig.defaultGateway?.let { value ->
        result["defaultGateway"] = value
    }
    ethernetConfig.proxy?.let {
        result["proxy"] = toResult(proxy = it)
    }
    return result
}

fun toResult(gpsInfo: GpsInfo): Map<String, Any> {
    return mapOf(
        "latitude" to gpsInfo.latitude,
        "longitude" to gpsInfo.longitude,
        "altitude" to gpsInfo.altitude,
        "dateTimeZone" to gpsInfo.dateTimeZone
    )
}

fun toResult(stateGpsInfo: StateGpsInfo): Map<String, Any> {
    return stateGpsInfo.gpsInfo?.let {
        mapOf(KEY_GPS_INFO to toResult(it))
    } ?: mapOf()
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

fun toResult(rotation: TopBottomCorrectionRotation): Map<String, Any> {
    return mapOf(
        "pitch" to rotation.pitch,
        "roll" to rotation.roll,
        "yaw" to rotation.yaw
    )
}

fun toResult(options: Options): Map<String, Any> {
    val result = mutableMapOf<String, Any>()

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
    OptionNameEnum.values().forEach { name ->
        if (name == OptionNameEnum.AutoBracket) {
            options.getValue<BracketSettingList>(OptionNameEnum.AutoBracket)?.let { autoBracket ->
                result[OptionNameEnum.AutoBracket.name] = toResult(autoBracket)
            }
        } else if (name == OptionNameEnum.Bitrate) {
            options.getValue<Bitrate>(OptionNameEnum.Bitrate)?.let { bitrate ->
                if (bitrate is BitrateEnum) {
                    result[OptionNameEnum.Bitrate.name] = bitrate.toString()
                } else if (bitrate is BitrateNumber) {
                    result[OptionNameEnum.Bitrate.name] = bitrate.value
                }
            }
        } else if (name == OptionNameEnum.BurstOption) {
            options.getValue<BurstOption>(OptionNameEnum.BurstOption)?.let { burstOption ->
                result[OptionNameEnum.BurstOption.name] = toResult(burstOption)
            }
        } else if (name == OptionNameEnum.EthernetConfig) {
            options.getValue<EthernetConfig>(OptionNameEnum.EthernetConfig)?.let { ethernetConfig ->
                result[OptionNameEnum.EthernetConfig.name] = toResult(ethernetConfig = ethernetConfig)
            }
        } else if (name == OptionNameEnum.GpsInfo) {
            options.getValue<GpsInfo>(OptionNameEnum.GpsInfo)?.let { gpsInfo ->
                result[OptionNameEnum.GpsInfo.name] = toResult(gpsInfo)
            }
        } else if (name == OptionNameEnum.OffDelay) {
            options.getValue<OffDelay>(OptionNameEnum.OffDelay)?.let { offDelay ->
                result[OptionNameEnum.OffDelay.name] =
                    if (offDelay is OffDelayEnum) offDelay.name else offDelay.sec
            }
        } else if (name == OptionNameEnum.Proxy) {
            options.getValue<Proxy>(OptionNameEnum.Proxy)?.let { proxy ->
                result[OptionNameEnum.Proxy.name] = toResult(proxy)
            }
        } else if (name == OptionNameEnum.SleepDelay) {
            options.getValue<SleepDelay>(OptionNameEnum.SleepDelay)?.let { sleepDelay ->
                result[OptionNameEnum.SleepDelay.name] =
                    if (sleepDelay is SleepDelayEnum) sleepDelay.name else sleepDelay.sec
            }
        } else if (name == OptionNameEnum.TimeShift) {
            options.getValue<TimeShiftSetting>(OptionNameEnum.TimeShift)?.let { timeShift ->
                result[OptionNameEnum.TimeShift.name] = toResult(timeShift)
            }
        } else if (name == OptionNameEnum.TopBottomCorrectionRotation) {
            options.getValue<TopBottomCorrectionRotation>(OptionNameEnum.TopBottomCorrectionRotation)?.let { rotation ->
                result[OptionNameEnum.TopBottomCorrectionRotation.name] = toResult(rotation)
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
    if (valueOptions.contains(name)) {
        var optionValue = value
        if (name.valueType == Long::class && value is Int) {
            optionValue = value.toLong()
        }
        options.setValue(name, optionValue)
    } else if (name == OptionNameEnum.AutoBracket) {
        @Suppress("UNCHECKED_CAST")
        options.setValue(name, toAutoBracket(value as List<Map<String, Any>>))
    } else if (name == OptionNameEnum.Bitrate) {
        if (value is Int) {
            options.setValue(name, BitrateNumber(value))
        } else if (BitrateEnum.values().map { it.name }.contains(value as String)) {
            options.setValue(name, BitrateEnum.valueOf(value))
        }
    } else if (name == OptionNameEnum.BurstOption) {
        @Suppress("UNCHECKED_CAST")
        options.setValue(name, toBurstOption(value as Map<String, Any>))
    } else if (name == OptionNameEnum.EthernetConfig) {
        @Suppress("UNCHECKED_CAST")
        options.setValue(name, toEthernetConfig(value as Map<String, Any>))
    } else if (name == OptionNameEnum.GpsInfo) {
        @Suppress("UNCHECKED_CAST")
        options.setValue(name, toGpsInfo(value as Map<String, Any>))
    } else if (name == OptionNameEnum.OffDelay) {
        toOffDelay(value)?.let {
            options.setValue(name, it)
        }
    } else if (name == OptionNameEnum.Proxy) {
        @Suppress("UNCHECKED_CAST")
        options.setValue(name, toProxy(value as Map<String, Any>))
    } else if (name == OptionNameEnum.SleepDelay) {
        toSleepDelay(value)?.let {
            options.setValue(name, it)
        }
    } else if (name == OptionNameEnum.TimeShift) {
        @Suppress("UNCHECKED_CAST")
        options.setValue(name, toTimeShift(value as Map<String, Any>))
    } else if (name == OptionNameEnum.TopBottomCorrectionRotation) {
        @Suppress("UNCHECKED_CAST")
        options.setValue(name, toTopBottomCorrectionRotation(value as Map<String, Any>))
    } else {
        getOptionValueEnum(name, value as String)?.let {
            options.setValue(name, it)
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
        OptionNameEnum.CameraMode -> CameraModeEnum.values().find { it.name == valueName }
        OptionNameEnum.CaptureMode -> CaptureModeEnum.values().find { it.name == valueName }
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
        OptionNameEnum.NetworkType -> NetworkTypeEnum.values().find { it.name == valueName }
        OptionNameEnum.OffDelay -> OffDelayEnum.values().find { it.name == valueName }
        OptionNameEnum.PowerSaving -> PowerSavingEnum.values().find { it.name == valueName }
        OptionNameEnum.Preset -> PresetEnum.values().find { it.name == valueName }
        OptionNameEnum.PreviewFormat -> PreviewFormatEnum.values().find { it.name == valueName }
        OptionNameEnum.ShootingMethod -> ShootingMethodEnum.values().find { it.name == valueName }
        OptionNameEnum.ShutterSpeed -> ShutterSpeedEnum.values().find { it.name == valueName }
        OptionNameEnum.SleepDelay -> SleepDelayEnum.values().find { it.name == valueName }
        OptionNameEnum.TopBottomCorrection -> TopBottomCorrectionOptionEnum.values().find { it.name == valueName }
        OptionNameEnum.VideoStitching -> VideoStitchingEnum.values().find { it.name == valueName }
        OptionNameEnum.VisibilityReduction -> VisibilityReductionEnum.values().find { it.name == valueName }
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

            OptionNameEnum.OffDelay.name -> {
                toOffDelay(value)?.let {
                    config.offDelay = it
                }
            }

            OptionNameEnum.SleepDelay.name -> {
                toSleepDelay(value)?.let {
                    config.sleepDelay = it
                }
            }

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

fun toNotify(
    id: Int,
    params: Map<String, Any?>?,
): Map<String, Any> {
    val objects = mutableMapOf<String, Any>(
        KEY_NOTIFY_ID to id,
    )
    params?.run {
        objects[KEY_NOTIFY_PARAMS] = params
    }
    return objects
}

fun toCaptureProgressNotifyParam(value: Float): Map<String, Any> {
    return mapOf<String, Any>(
        KEY_NOTIFY_PARAM_COMPLETION to value
    )
}

fun toPreviewNotifyParam(imageData: ByteArray): Map<String, Any> {
    return mapOf<String, Any>(
        KEY_NOTIFY_PARAM_IMAGE to imageData
    )
}

fun toMessageNotifyParam(message: String): Map<String, Any> {
    return mapOf<String, Any>(
        KEY_NOTIFY_PARAM_MESSAGE to message
    )
}

fun toCapturingNotifyParam(status: CapturingStatusEnum): Map<String, Any> {
    return mapOf<String, Any>(
        KEY_NOTIFY_PARAM_STATUS to status.name
    )
}
