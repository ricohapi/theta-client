package com.ricoh360.thetaclient.theta_client_flutter

import com.ricoh360.thetaclient.DigestAuth
import com.ricoh360.thetaclient.ThetaRepository.*
import com.ricoh360.thetaclient.capture.*
import io.flutter.plugin.common.MethodCall
import kotlin.reflect.KClass

const val KEY_CLIENT_MODE = "clientMode"
const val KEY_NOTIFY_ID = "id"
const val KEY_NOTIFY_PARAMS = "params"
const val KEY_NOTIFY_PARAM_COMPLETION = "completion"
const val KEY_NOTIFY_PARAM_IMAGE = "image"
const val KEY_NOTIFY_PARAM_MESSAGE = "message"
const val KEY_NOTIFY_PARAM_STATUS = "status"
const val KEY_NOTIFY_PARAM_FILE_URL = "fileUrl"
const val KEY_GPS_INFO = "gpsInfo"
const val KEY_STATE_EXTERNAL_GPS_INFO = "externalGpsInfo"
const val KEY_STATE_INTERNAL_GPS_INFO = "internalGpsInfo"
const val KEY_WLAN_FREQUENCY_CL_MODE_2_4 = "enable2_4"
const val KEY_WLAN_FREQUENCY_CL_MODE_5_2 = "enable5_2"
const val KEY_WLAN_FREQUENCY_CL_MODE_5_8 = "enable5_8"
const val KEY_IP_ADDRESS = "ipAddress"
const val KEY_MAC_ADDRESS = "macAddress"
const val KEY_HOST_NAME = "hostName"
const val KEY_DHCP_LEASE_ADDRESS = "dhcpLeaseAddress"
const val KEY_TOP_BOTTOM_CORRECTION_ROTATION_PITCH = "pitch"
const val KEY_TOP_BOTTOM_CORRECTION_ROTATION_ROLL = "roll"
const val KEY_TOP_BOTTOM_CORRECTION_ROTATION_YAW = "yaw"
const val KEY_MAX = "max"
const val KEY_MIN = "min"
const val KEY_STEP_SIZE = "stepSize"
const val KEY_SSID = "ssid"
const val KEY_SSID_STEALTH = "ssidStealth"
const val KEY_AUTH_MODE = "authMode"
const val KEY_PASSWORD = "password"
const val KEY_CONNECTION_PRIORITY = "connectionPriority"
const val KEY_SUBNET_MASK = "subnetMask"
const val KEY_DEFAULT_GATEWAY = "defaultGateway"
const val KEY_PROXY = "proxy"
const val KEY_APERTURE_SUPPORT = "apertureSupport"
const val KEY_DNS1 = "dns1"
const val KEY_DNS2 = "dns2"

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

fun toAccessInfo(map: Map<String, Any>): AccessInfo {
    return AccessInfo(
        ssid = map[KEY_SSID] as String,
        ipAddress = map[KEY_IP_ADDRESS] as String,
        subnetMask = map[KEY_SUBNET_MASK] as String,
        defaultGateway = map[KEY_DEFAULT_GATEWAY] as String,
        dns1 = map[KEY_DNS1] as? String,
        dns2 = map[KEY_DNS2] as? String,
        proxyURL = map["proxyURL"] as String,
        frequency = (map["frequency"] as String).let { WlanFrequencyAccessInfoEnum.valueOf(it) },
        wlanSignalStrength = map["wlanSignalStrength"] as Int,
        wlanSignalLevel = map["wlanSignalLevel"] as Int,
        lteSignalStrength = map["lteSignalStrength"] as Int,
        lteSignalLevel = map["lteSignalLevel"] as Int,
        dhcpLeaseAddress = (map[KEY_DHCP_LEASE_ADDRESS] as? List<Map<String, Any>>)?.mapNotNull { toDhcpLeaseAddress(it) }?.takeIf { it.isNotEmpty() }
    )
}

fun toResult(accessInfo: AccessInfo): Map<String, Any> {
    val result = mutableMapOf<String, Any>(
        KEY_SSID to accessInfo.ssid,
        KEY_IP_ADDRESS to accessInfo.ipAddress,
        KEY_SUBNET_MASK to accessInfo.subnetMask,
        KEY_DEFAULT_GATEWAY to accessInfo.defaultGateway,
        "proxyURL" to accessInfo.proxyURL,
        "frequency" to accessInfo.frequency.name,
        "wlanSignalStrength" to accessInfo.wlanSignalStrength,
        "wlanSignalLevel" to accessInfo.wlanSignalLevel,
        "lteSignalStrength" to accessInfo.lteSignalStrength,
        "lteSignalLevel" to accessInfo.lteSignalLevel
    )
    accessInfo.dns1?.let {
        result.put(KEY_DNS1, it)
    }
    accessInfo.dns2?.let {
        result.put(KEY_DNS2, it)
    }
    accessInfo.dhcpLeaseAddress?.mapNotNull { toResult(dhcpLeaseAddress = it) }?.takeIf { it.isNotEmpty() }?.let { array ->
        result[KEY_DHCP_LEASE_ADDRESS] = array
    }
    return result
}

fun toDhcpLeaseAddress(map: Map<String, Any>): DhcpLeaseAddress? {
    val ipAddress = map[KEY_IP_ADDRESS] as? String
    val macAddress = map[KEY_MAC_ADDRESS] as? String
    val hostName = map[KEY_HOST_NAME] as? String
    if (ipAddress == null || macAddress == null || hostName == null) {
        return null
    }
    return DhcpLeaseAddress(ipAddress, macAddress, hostName)
}

fun toResult(dhcpLeaseAddress: DhcpLeaseAddress): Map<String, Any> {
    val result = mutableMapOf<String, Any>(
        KEY_IP_ADDRESS to dhcpLeaseAddress.ipAddress,
        KEY_MAC_ADDRESS to dhcpLeaseAddress.macAddress,
        KEY_HOST_NAME to dhcpLeaseAddress.hostName
    )
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

fun toCameraLockConfig(map: Map<String, Any>): CameraLockConfig {
    return CameraLockConfig(
        isPowerKeyLocked = map["isPowerKeyLocked"] as? Boolean,
        isShutterKeyLocked = map["isShutterKeyLocked"] as? Boolean,
        isModeKeyLocked = map["isModeKeyLocked"] as? Boolean,
        isWlanKeyLocked = map["isWlanKeyLocked"] as? Boolean,
        isFnKeyLocked = map["isFnKeyLocked"] as? Boolean,
        isPanelLocked = map["isPanelLocked"] as? Boolean,
    )
}

fun toEthernetConfig(map: Map<String, Any>): EthernetConfig {
    val proxy = map[KEY_PROXY]?.let {
        @Suppress("UNCHECKED_CAST")
        (it as? Map<String, Any>)?.let { map ->
            toProxy(map)
        }
    }
    return EthernetConfig(
        usingDhcp = map["usingDhcp"] as? Boolean ?: true,
        ipAddress = map[KEY_IP_ADDRESS] as? String,
        subnetMask = map[KEY_SUBNET_MASK] as? String,
        defaultGateway = map[KEY_DEFAULT_GATEWAY] as? String,
        dns1 = map[KEY_DNS1] as? String,
        dns2 = map[KEY_DNS2] as? String,
        proxy = proxy
    )
}

fun toMobileNetworkSetting(map: Map<String, Any>): MobileNetworkSetting {
    return MobileNetworkSetting(
        roaming = (map["roaming"] as? String)?.let { RoamingEnum.valueOf(it) },
        plan = (map["plan"] as? String)?.let { PlanEnum.valueOf(it) }
    )
}

fun toResult(mobileNetworkSetting: MobileNetworkSetting): Map<String, Any?> {
    return mapOf(
        "roaming" to mobileNetworkSetting.roaming?.name,
        "plan" to mobileNetworkSetting.plan?.name
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

fun toOffDelayUsb(value: Any): OffDelayUsb? {
    if (value is Int) {
        return OffDelayUsbSec(value)
    } else if (value is String) {
        getOptionValueEnum(OptionNameEnum.OffDelayUsb, value)?.let {
            return it as OffDelayUsbEnum
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
        password = map[KEY_PASSWORD] as? String
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
        pitch = (map[KEY_TOP_BOTTOM_CORRECTION_ROTATION_PITCH] as Double).toFloat(),
        roll = (map[KEY_TOP_BOTTOM_CORRECTION_ROTATION_ROLL] as Double).toFloat(),
        yaw = (map[KEY_TOP_BOTTOM_CORRECTION_ROTATION_YAW] as Double).toFloat()
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

fun setTimeShiftManualCaptureBuilderParams(call: MethodCall, builder: TimeShiftManualCapture.Builder) {
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

fun toResult(cameraLockConfig: CameraLockConfig): Map<String, Any?> {
    val result = mutableMapOf<String, Any>()

    cameraLockConfig.isPowerKeyLocked?.let { value ->
        result["isPowerKeyLocked"] = value
    }
    cameraLockConfig.isShutterKeyLocked?.let { value ->
        result["isShutterKeyLocked"] = value
    }
    cameraLockConfig.isModeKeyLocked?.let { value ->
        result["isModeKeyLocked"] = value
    }
    cameraLockConfig.isWlanKeyLocked?.let { value ->
        result["isWlanKeyLocked"] = value
    }
    cameraLockConfig.isFnKeyLocked?.let { value ->
        result["isFnKeyLocked"] = value
    }
    cameraLockConfig.isPanelLocked?.let { value ->
        result["isPanelLocked"] = value
    }

    return result
}

fun toResult(ethernetConfig: EthernetConfig): Map<String, Any?> {
    val result = mutableMapOf<String, Any>()

    result["usingDhcp"] = ethernetConfig.usingDhcp

    ethernetConfig.ipAddress?.let { value ->
        result[KEY_IP_ADDRESS] = value
    }
    ethernetConfig.subnetMask?.let { value ->
        result[KEY_SUBNET_MASK] = value
    }
    ethernetConfig.defaultGateway?.let { value ->
        result[KEY_DEFAULT_GATEWAY] = value
    }
    ethernetConfig.dns1?.let { value ->
        result[KEY_DNS1] = value
    }
    ethernetConfig.dns2?.let { value ->
        result[KEY_DNS2] = value
    }
    ethernetConfig.proxy?.let {
        result[KEY_PROXY] = toResult(proxy = it)
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
        KEY_PASSWORD to proxy.password
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
        KEY_TOP_BOTTOM_CORRECTION_ROTATION_PITCH to rotation.pitch,
        KEY_TOP_BOTTOM_CORRECTION_ROTATION_ROLL to rotation.roll,
        KEY_TOP_BOTTOM_CORRECTION_ROTATION_YAW to rotation.yaw
    )
}

fun toResult(topBottomCorrectionRotationSupport: TopBottomCorrectionRotationSupport): Map<String, Any> {
    return mapOf(
        KEY_TOP_BOTTOM_CORRECTION_ROTATION_PITCH to toResultValueRangeString(valueRange = topBottomCorrectionRotationSupport.pitch),
        KEY_TOP_BOTTOM_CORRECTION_ROTATION_ROLL to toResultValueRangeString(valueRange = topBottomCorrectionRotationSupport.roll),
        KEY_TOP_BOTTOM_CORRECTION_ROTATION_YAW to toResultValueRangeString(valueRange = topBottomCorrectionRotationSupport.yaw)
    )
}

fun <T : Number> toResultValueRangeString(valueRange: ValueRange<T>): Map<String, String> {
    return mapOf(
        KEY_MAX to valueRange.max.toString(),
        KEY_MIN to valueRange.min.toString(),
        KEY_STEP_SIZE to valueRange.stepSize.toString()
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
        OptionNameEnum.CompositeShootingTimeSupport,
    )
    OptionNameEnum.values().forEach { name ->
        if (name == OptionNameEnum.AccessInfo) {
            options.getValue<AccessInfo>(OptionNameEnum.AccessInfo)?.let { accessInfo ->
                result[OptionNameEnum.AccessInfo.name] = toResult(accessInfo)
            }
        } else if (name == OptionNameEnum.AutoBracket) {
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
        } else if (name == OptionNameEnum.CameraLockConfig) {
            options.getValue<CameraLockConfig>(OptionNameEnum.CameraLockConfig)?.let { cameraLockConfig ->
                result[OptionNameEnum.CameraLockConfig.name] = toResult(cameraLockConfig)
            }
        } else if (name == OptionNameEnum.EthernetConfig) {
            options.getValue<EthernetConfig>(OptionNameEnum.EthernetConfig)?.let { ethernetConfig ->
                result[OptionNameEnum.EthernetConfig.name] = toResult(ethernetConfig = ethernetConfig)
            }
        } else if (name == OptionNameEnum.GpsInfo) {
            options.getValue<GpsInfo>(OptionNameEnum.GpsInfo)?.let { gpsInfo ->
                result[OptionNameEnum.GpsInfo.name] = toResult(gpsInfo)
            }
        } else if (name == OptionNameEnum.MobileNetworkSetting) {
            options.getValue<MobileNetworkSetting>(OptionNameEnum.MobileNetworkSetting)?.let { mobileNetworkSetting ->
                result[OptionNameEnum.MobileNetworkSetting.name] = toResult(mobileNetworkSetting)
            }
        } else if (name == OptionNameEnum.OffDelay) {
            options.getValue<OffDelay>(OptionNameEnum.OffDelay)?.let { offDelay ->
                result[OptionNameEnum.OffDelay.name] =
                    if (offDelay is OffDelayEnum) offDelay.name else offDelay.sec
            }
        } else if (name == OptionNameEnum.OffDelayUsb) {
            options.getValue<OffDelayUsb>(OptionNameEnum.OffDelayUsb)?.let { offDelayUsb ->
                result[OptionNameEnum.OffDelayUsb.name] =
                    if (offDelayUsb is OffDelayUsbEnum) offDelayUsb.name else offDelayUsb.sec
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
        } else if (name == OptionNameEnum.TopBottomCorrectionRotationSupport) {
            options.getValue<TopBottomCorrectionRotationSupport>(OptionNameEnum.TopBottomCorrectionRotationSupport)?.let { support ->
                result[OptionNameEnum.TopBottomCorrectionRotationSupport.name] = toResult(topBottomCorrectionRotationSupport = support)
            }
        } else if (name == OptionNameEnum.WlanFrequencyClMode) {
            options.getValue<WlanFrequencyClMode>(OptionNameEnum.WlanFrequencyClMode)?.let {
                result[OptionNameEnum.WlanFrequencyClMode.name] = toResult(it)
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

fun addSupportOptionsValueToMap(
    options: Options,
    name: OptionNameEnum,
    map: MutableMap<String, Any>,
    valueClazz: KClass<*>
) {
    options.getValue<List<*>>(name)?.let { list ->
        val array = mutableListOf<String>()
        for (item in list) {
            when {
                // for enum value
                java.lang.Enum::class.java.isAssignableFrom(valueClazz.java) -> {
                    array.add(item.toString())
                }
            }
        }
        map[name.name] = array
    }
}

fun <T : Number> addValueRangeSupportOptionsValueToMap(
    options: Options,
    name: OptionNameEnum,
    map: MutableMap<String, Any>
) {
    options.getValue<ValueRange<T>>(name)?.let { value ->
        map[name.name] = mapOf(
            KEY_MAX to value.max,
            KEY_MIN to value.min,
            KEY_STEP_SIZE to value.stepSize
        )
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
    } else if (name == OptionNameEnum.AccessInfo) {
        @Suppress("UNCHECKED_CAST")
        options.setValue(name, toAccessInfo(value as Map<String, Any>))
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
    } else if (name == OptionNameEnum.CameraLockConfig) {
        @Suppress("UNCHECKED_CAST")
        options.setValue(name, toCameraLockConfig(value as Map<String, Any>))
    } else if (name == OptionNameEnum.EthernetConfig) {
        @Suppress("UNCHECKED_CAST")
        options.setValue(name, toEthernetConfig(value as Map<String, Any>))
    } else if (name == OptionNameEnum.GpsInfo) {
        @Suppress("UNCHECKED_CAST")
        options.setValue(name, toGpsInfo(value as Map<String, Any>))
    } else if (name == OptionNameEnum.MobileNetworkSetting) {
        @Suppress("UNCHECKED_CAST")
        options.setValue(name, toMobileNetworkSetting(value as Map<String, Any>))
    } else if (name == OptionNameEnum.OffDelay) {
        toOffDelay(value)?.let {
            options.setValue(name, it)
        }
    } else if (name == OptionNameEnum.OffDelayUsb) {
        toOffDelayUsb(value)?.let {
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
    } else if (name == OptionNameEnum.WlanFrequencyClMode) {
        @Suppress("UNCHECKED_CAST")
        options.setValue(name, toWlanFrequencyClMode(value as Map<String, Any>))
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
        OptionNameEnum.OffDelayUsb -> OffDelayUsbEnum.values().find { it.name == valueName }
        OptionNameEnum.PowerSaving -> PowerSavingEnum.values().find { it.name == valueName }
        OptionNameEnum.Preset -> PresetEnum.values().find { it.name == valueName }
        OptionNameEnum.PreviewFormat -> PreviewFormatEnum.values().find { it.name == valueName }
        OptionNameEnum.ShootingMethod -> ShootingMethodEnum.values().find { it.name == valueName }
        OptionNameEnum.ShutterSpeed -> ShutterSpeedEnum.values().find { it.name == valueName }
        OptionNameEnum.SleepDelay -> SleepDelayEnum.values().find { it.name == valueName }
        OptionNameEnum.TopBottomCorrection -> TopBottomCorrectionOptionEnum.values().find { it.name == valueName }
        OptionNameEnum.UsbConnection -> UsbConnectionEnum.values().find { it.name == valueName }
        OptionNameEnum.VideoStitching -> VideoStitchingEnum.values().find { it.name == valueName }
        OptionNameEnum.VisibilityReduction -> VisibilityReductionEnum.values().find { it.name == valueName }
        OptionNameEnum.WhiteBalance -> WhiteBalanceEnum.values().find { it.name == valueName }
        OptionNameEnum.WhiteBalanceAutoStrength -> WhiteBalanceAutoStrengthEnum.values().find { it.name == valueName }
        OptionNameEnum.WlanAntennaConfig -> WlanAntennaConfigEnum.values().find { it.name == valueName }
        OptionNameEnum.WlanFrequency -> WlanFrequencyEnum.values().find { it.name == valueName }
        else -> null
    }
}

fun toDigestAuthParam(data: Map<*, *>): DigestAuth? {
    val username = data["username"] as? String ?: run {
        return null
    }
    val password = data[KEY_PASSWORD] as? String
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
        result[KEY_SSID] = accessPoint.ssid
        result[KEY_SSID_STEALTH] = accessPoint.ssidStealth
        result[KEY_AUTH_MODE] = accessPoint.authMode.name
        result[KEY_CONNECTION_PRIORITY] = accessPoint.connectionPriority
        result["usingDhcp"] = accessPoint.usingDhcp
        accessPoint.ipAddress?.let {
            result[KEY_IP_ADDRESS] = it
        }
        accessPoint.subnetMask?.let {
            result[KEY_SUBNET_MASK] = it
        }
        accessPoint.defaultGateway?.let {
            result[KEY_DEFAULT_GATEWAY] = it
        }
        accessPoint.dns1?.let {
            result[KEY_DNS1] = it
        }
        accessPoint.dns2?.let {
            result[KEY_DNS2] = it
        }
        accessPoint.proxy?.let {
            result[KEY_PROXY] = toResult(proxy = it)
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

fun toResult(wlanFrequencyClMode: WlanFrequencyClMode): Map<String, Any> {
    return mapOf<String, Any>(
        KEY_WLAN_FREQUENCY_CL_MODE_2_4 to wlanFrequencyClMode.enable2_4,
        KEY_WLAN_FREQUENCY_CL_MODE_5_2 to wlanFrequencyClMode.enable5_2,
        KEY_WLAN_FREQUENCY_CL_MODE_5_8 to wlanFrequencyClMode.enable5_8,
    )
}

fun toWlanFrequencyClMode(map: Map<String, Any>): WlanFrequencyClMode {
    return WlanFrequencyClMode(
        map[KEY_WLAN_FREQUENCY_CL_MODE_2_4] as Boolean,
        map[KEY_WLAN_FREQUENCY_CL_MODE_5_2] as Boolean,
        map[KEY_WLAN_FREQUENCY_CL_MODE_5_8] as Boolean,
    )
}

fun toCapturingNotifyParam(status: CapturingStatusEnum): Map<String, Any> {
    return mapOf<String, Any>(
        KEY_NOTIFY_PARAM_STATUS to status.name
    )
}

fun toStartedNotifyParam(value: String): Map<String, Any> {
    return mapOf<String, Any>(
        KEY_NOTIFY_PARAM_FILE_URL to value
    )
}

data class SetAccessPointParams(
    val ssid: String,
    val ssidStealth: Boolean?,
    val authMode: AuthModeEnum,
    val password: String?,
    val connectionPriority: Int?,
    val dns1: String?,
    val dns2: String?,
    val proxy: Proxy?,
)

fun toSetAccessPointParams(arguments: Map<*, *>): SetAccessPointParams {
    val ssid = arguments[KEY_SSID] as? String ?: throw IllegalArgumentException(KEY_SSID)
    val ssidStealth = arguments[KEY_SSID_STEALTH] as? Boolean
    val authMode = (arguments[KEY_AUTH_MODE] as? String?)?.let {
        AuthModeEnum.valueOf(it)
    } ?: throw IllegalArgumentException(KEY_AUTH_MODE)
    val password = arguments[KEY_PASSWORD] as? String
    val connectionPriority = arguments[KEY_CONNECTION_PRIORITY] as? Int
    val dns1 = arguments[KEY_DNS1] as? String
    val dns2 = arguments[KEY_DNS2] as? String
    val proxy = (arguments[KEY_PROXY] as? Map<String, Any>)?.let { toProxy(it) }

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

fun toSetAccessPointStaticallyParams(arguments: Map<*, *>): SetAccessPointStaticallyParams {
    val ipAddress = arguments[KEY_IP_ADDRESS] as? String ?: throw IllegalArgumentException(KEY_IP_ADDRESS)
    val subnetMask = arguments[KEY_SUBNET_MASK] as? String ?: throw IllegalArgumentException(KEY_SUBNET_MASK)
    val defaultGateway = arguments[KEY_DEFAULT_GATEWAY] as? String ?: throw IllegalArgumentException(KEY_DEFAULT_GATEWAY)

    return SetAccessPointStaticallyParams(
        ipAddress = ipAddress,
        subnetMask = subnetMask,
        defaultGateway = defaultGateway,
    )
}
