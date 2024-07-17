import Flutter
import THETAClient
import UIKit

let KEY_CLIENT_MODE = "clientMode"
let KEY_NOTIFY_ID = "id"
let KEY_NOTIFY_PARAMS = "params"
let KEY_NOTIFY_PARAM_COMPLETION = "completion"
let KEY_NOTIFY_PARAM_IMAGE = "image"
let KEY_NOTIFY_PARAM_MESSAGE = "message"
let KEY_NOTIFY_PARAM_STATUS = "status"
let KEY_GPS_INFO = "gpsInfo"
let KEY_STATE_EXTERNAL_GPS_INFO = "externalGpsInfo"
let KEY_STATE_INTERNAL_GPS_INFO = "internalGpsInfo"
let KEY_STATE_BOARD_TEMP = "boardTemp"
let KEY_STATE_BATTERY_TEMP = "batteryTemp"

public class ConvertUtil: NSObject {}

func getEnumValue<T, E: KotlinEnum<T>>(values: KotlinArray<E>, name: String) -> E? {
    for i in 0 ..< values.size {
        let item = values.get(index: i)!
        if item.name == name {
            return item
        }
    }
    return nil
}

func convertResult(fileInfoList: [ThetaRepository.FileInfo]) -> [[String: Any]] {
    var resultList = [[String: Any]]()
    fileInfoList.forEach { fileInfo in
        var item = [
            "name": fileInfo.name,
            "fileUrl": fileInfo.fileUrl,
            "size": fileInfo.size,
            "dateTime": fileInfo.dateTime,
            "thumbnailUrl": fileInfo.thumbnailUrl,
        ]
        if let dateTimeZone = fileInfo.dateTimeZone {
            item["dateTimeZone"] = dateTimeZone
        }
        if let lat = fileInfo.lat {
            item["lat"] = lat.floatValue
        }
        if let lng = fileInfo.lng {
            item["lng"] = lng.floatValue
        }
        if let width = fileInfo.width {
            item["width"] = width.intValue
        }
        if let height = fileInfo.height {
            item["height"] = height.intValue
        }
        if let intervalCaptureGroupId = fileInfo.intervalCaptureGroupId {
            item["intervalCaptureGroupId"] = intervalCaptureGroupId
        }
        if let compositeShootingGroupId = fileInfo.compositeShootingGroupId {
            item["compositeShootingGroupId"] = compositeShootingGroupId
        }
        if let autoBracketGroupId = fileInfo.autoBracketGroupId {
            item["autoBracketGroupId"] = autoBracketGroupId
        }
        if let recordTime = fileInfo.recordTime {
            item["recordTime"] = recordTime.intValue
        }
        if let isProcessed = fileInfo.isProcessed {
            item["isProcessed"] = isProcessed.boolValue
        }
        if let previewUrl = fileInfo.previewUrl {
            item["previewUrl"] = previewUrl
        }
        if let codec = fileInfo.codec {
            item["codec"] = codec.name
        }
        if let projectionType = fileInfo.projectionType {
            item["projectionType"] = projectionType.name
        }
        if let continuousShootingGroupId = fileInfo.continuousShootingGroupId {
            item["continuousShootingGroupId"] = continuousShootingGroupId
        }
        if let frameRate = fileInfo.frameRate {
            item["frameRate"] = frameRate.intValue
        }
        if let favorite = fileInfo.favorite {
            item["favorite"] = favorite.boolValue
        }
        if let imageDescription = fileInfo.imageDescription {
            item["imageDescription"] = imageDescription
        }
        if let storageID = fileInfo.storageID {
            item["storageID"] = storageID
        }
        resultList.append(item)
    }
    return resultList
}

func convertResult(thetaInfo: ThetaRepository.ThetaInfo) -> [String: Any?] {
    return [
        "manufacturer": thetaInfo.manufacturer,
        "model": thetaInfo.model,
        "serialNumber": thetaInfo.serialNumber,
        "wlanMacAddress": thetaInfo.wlanMacAddress,
        "bluetoothMacAddress": thetaInfo.bluetoothMacAddress,
        "firmwareVersion": thetaInfo.firmwareVersion,
        "supportUrl": thetaInfo.supportUrl,
        "hasGps": thetaInfo.hasGps,
        "hasGyro": thetaInfo.hasGyro,
        "uptime": thetaInfo.uptime,
        "api": thetaInfo.api,
        "endpoints": [
            "httpPort": thetaInfo.endpoints.httpPort,
            "httpUpdatesPort": thetaInfo.endpoints.httpUpdatesPort,
        ],
        "apiLevel": thetaInfo.apiLevel,
    ]
}

func convertResult(cameraErrorList: [ThetaRepository.CameraErrorEnum]?) -> [String]? {
    guard let cameraErrorList = cameraErrorList else {
        return nil
    }
    var result: [String] = []
    cameraErrorList.forEach { error in
        result.append(error.name)
    }
    return result
}

func convertResult(thetaState: ThetaRepository.ThetaState) -> [String: Any?] {
    var result: [String: Any] = [:]
    if let value = thetaState.fingerprint {
        result["fingerprint"] = value
    }
    if let value = thetaState.batteryLevel {
        result["batteryLevel"] = value
    }
    if let value = thetaState.storageUri {
        result["storageUri"] = value
    }
    if let value = thetaState.storageID {
        result["storageID"] = value
    }
    if let value = thetaState.captureStatus {
        result["captureStatus"] = value.name
    }
    if let value = thetaState.recordedTime {
        result["recordedTime"] = value
    }
    if let value = thetaState.recordableTime {
        result["recordableTime"] = value
    }
    if let value = thetaState.capturedPictures {
        result["capturedPictures"] = value
    }
    if let value = thetaState.compositeShootingElapsedTime {
        result["compositeShootingElapsedTime"] = value
    }
    if let value = thetaState.latestFileUrl {
        result["latestFileUrl"] = value
    }
    if let value = thetaState.chargingState {
        result["chargingState"] = value.name
    }
    if let value = thetaState.apiVersion {
        result["apiVersion"] = value
    }
    if let value = thetaState.isPluginRunning {
        result["isPluginRunning"] = convertKotlinBooleanToBool(value: value)
    }
    if let value = thetaState.isPluginWebServer {
        result["isPluginWebServer"] = convertKotlinBooleanToBool(value: value)
    }
    if let value = thetaState.function {
        result["function"] = value.name
    }
    if let value = thetaState.isMySettingChanged {
        result["isMySettingChanged"] = convertKotlinBooleanToBool(value: value)
    }
    if let value = thetaState.currentMicrophone {
        result["currentMicrophone"] = value.name
    }
    if let value = thetaState.isSdCard {
        result["isSdCard"] = convertKotlinBooleanToBool(value: value)
    }
    if let value = thetaState.cameraError {
        result["cameraError"] = convertResult(cameraErrorList: value)
    }
    if let value = thetaState.isBatteryInsert {
        result["isBatteryInsert"] = convertKotlinBooleanToBool(value: value)
    }
    if let value = thetaState.boardTemp {
        result[KEY_STATE_BOARD_TEMP] = value
    }
    if let value = thetaState.batteryTemp {
        result[KEY_STATE_BATTERY_TEMP] = value
    }
    if let externalGpsInfo = thetaState.externalGpsInfo {
        result[KEY_STATE_EXTERNAL_GPS_INFO] = convertResult(stateGpsInfo: externalGpsInfo)
    }
    if let internalGpsInfo = thetaState.internalGpsInfo {
        result[KEY_STATE_INTERNAL_GPS_INFO] = convertResult(stateGpsInfo: internalGpsInfo)
    }
    return result
}

func setCaptureBuilderParams<T>(params: [String: Any], builder: CaptureBuilder<T>) {
    if let value = params[ThetaRepository.OptionNameEnum.aperture.name] as? String {
        if let enumValue = getEnumValue(values: ThetaRepository.ApertureEnum.values(), name: value) {
            builder.setAperture(aperture: enumValue)
        }
    }
    if let value = params[ThetaRepository.OptionNameEnum.colortemperature.name] as? Int32 {
        builder.setColorTemperature(kelvin: value)
    }
    if let value = params[ThetaRepository.OptionNameEnum.exposurecompensation.name] as? String {
        if let enumValue = getEnumValue(values: ThetaRepository.ExposureCompensationEnum.values(), name: value) {
            builder.setExposureCompensation(value: enumValue)
        }
    }
    if let value = params[ThetaRepository.OptionNameEnum.exposuredelay.name] as? String {
        if let enumValue = getEnumValue(values: ThetaRepository.ExposureDelayEnum.values(), name: value) {
            builder.setExposureDelay(delay: enumValue)
        }
    }
    if let value = params[ThetaRepository.OptionNameEnum.exposureprogram.name] as? String {
        if let enumValue = getEnumValue(values: ThetaRepository.ExposureProgramEnum.values(), name: value) {
            builder.setExposureProgram(program: enumValue)
        }
    }
    if let value = params[ThetaRepository.OptionNameEnum.gpsinfo.name] as? [String: Any] {
        builder.setGpsInfo(gpsInfo: toGpsInfo(params: value))
    }
    if let value = params["GpsTagRecording"] as? String {
        if let enumValue = getEnumValue(values: ThetaRepository.GpsTagRecordingEnum.values(), name: value) {
            builder.setGpsTagRecording(value: enumValue)
        }
    }
    if let value = params[ThetaRepository.OptionNameEnum.iso.name] as? String {
        if let enumValue = getEnumValue(values: ThetaRepository.IsoEnum.values(), name: value) {
            builder.setIso(iso: enumValue)
        }
    }
    if let value = params[ThetaRepository.OptionNameEnum.isoautohighlimit.name] as? String {
        if let enumValue = getEnumValue(values: ThetaRepository.IsoAutoHighLimitEnum.values(), name: value) {
            builder.setIsoAutoHighLimit(iso: enumValue)
        }
    }
    if let value = params[ThetaRepository.OptionNameEnum.whitebalance.name] as? String {
        if let enumValue = getEnumValue(values: ThetaRepository.WhiteBalanceEnum.values(), name: value) {
            builder.setWhiteBalance(whiteBalance: enumValue)
        }
    }
}

func setPhotoCaptureBuilderParams(params: [String: Any], builder: PhotoCapture.Builder) {
    if let interval = params["_capture_interval"] as? Int,
       interval >= 0
    {
        builder.setCheckStatusCommandInterval(timeMillis: Int64(interval))
    }
    if let value = params[ThetaRepository.OptionNameEnum.filter.name] as? String {
        if let enumValue = getEnumValue(values: ThetaRepository.FilterEnum.values(), name: value) {
            builder.setFilter(filter: enumValue)
        }
    }
    if let value = params["PhotoFileFormat"] as? String {
        if let enumValue = getEnumValue(values: ThetaRepository.PhotoFileFormatEnum.values(), name: value) {
            builder.setFileFormat(fileFormat: enumValue)
        }
    }
    if let value = params[ThetaRepository.OptionNameEnum.preset.name] as? String {
        if let enumValue = getEnumValue(values: ThetaRepository.PresetEnum.values(), name: value) {
            builder.setPreset(preset: enumValue)
        }
    }
}

func setTimeShiftCaptureBuilderParams(params: [String: Any], builder: TimeShiftCapture.Builder) {
    if let interval = params["_capture_interval"] as? Int,
       interval >= 0
    {
        builder.setCheckStatusCommandInterval(timeMillis: Int64(interval))
    }
    if let timeShiftParams = params[ThetaRepository.OptionNameEnum.timeshift.name] as? [String: Any] {
        let timeShift = toTimeShift(params: timeShiftParams)
        if let isFrontFirst = timeShift.isFrontFirst {
            builder.setIsFrontFirst(isFrontFirst: isFrontFirst.boolValue)
        }
        if let firstInterval = timeShift.firstInterval {
            builder.setFirstInterval(interval: firstInterval)
        }
        if let secondInterval = timeShift.secondInterval {
            builder.setSecondInterval(interval: secondInterval)
        }
    }
}

func setVideoCaptureBuilderParams(params: [String: Any], builder: VideoCapture.Builder) {
    if let interval = params["_capture_interval"] as? Int,
       interval >= 0
    {
        builder.setCheckStatusCommandInterval(timeMillis: Int64(interval))
    }
    if let value = params[ThetaRepository.OptionNameEnum.maxrecordabletime.name] as? String {
        if let enumValue = getEnumValue(values: ThetaRepository.MaxRecordableTimeEnum.values(), name: value) {
            builder.setMaxRecordableTime(time: enumValue)
        }
    }
    if let value = params["VideoFileFormat"] as? String {
        if let enumValue = getEnumValue(values: ThetaRepository.VideoFileFormatEnum.values(), name: value) {
            builder.setFileFormat(fileFormat: enumValue)
        }
    }
}

func setLimitlessIntervalCaptureBuilderParams(params: [String: Any], builder: LimitlessIntervalCapture.Builder) {
    if let interval = params["_capture_interval"] as? Int,
       interval >= 0
    {
        builder.setCheckStatusCommandInterval(timeMillis: Int64(interval))
    }
    if let value = params[ThetaRepository.OptionNameEnum.captureinterval.name] as? Int32 {
        builder.setCaptureInterval(interval: value)
    }
}

func setShotCountSpecifiedIntervalCaptureBuilderParams(params: [String: Any], builder: ShotCountSpecifiedIntervalCapture.Builder) {
    if let interval = params["_capture_interval"] as? Int,
       interval >= 0
    {
        builder.setCheckStatusCommandInterval(timeMillis: Int64(interval))
    }
    if let value = params[ThetaRepository.OptionNameEnum.captureinterval.name] as? Int32 {
        builder.setCaptureInterval(interval: value)
    }
}

func toAutoBracket(params: [[String: Any]]) -> ThetaRepository.BracketSettingList {
    let autoBracket = ThetaRepository.BracketSettingList()

    params.forEach { map in
        let aperture = {
            if let name = map["aperture"] as? String {
                return getEnumValue(values: ThetaRepository.ApertureEnum.values(), name: name)
            } else {
                return nil
            }
        }()

        let colorTemperature = {
            if let value = map["colorTemperature"] as? Int {
                return toKotlinInt(value: value)
            } else {
                return nil
            }
        }()

        let exposureCompensation = {
            if let name = map["exposureCompensation"] as? String {
                return getEnumValue(values: ThetaRepository.ExposureCompensationEnum.values(), name: name)
            } else {
                return nil
            }
        }()

        let exposureProgram = {
            if let name = map["exposureProgram"] as? String {
                return getEnumValue(values: ThetaRepository.ExposureProgramEnum.values(), name: name)
            } else {
                return nil
            }
        }()

        let iso = {
            if let name = map["iso"] as? String {
                return getEnumValue(values: ThetaRepository.IsoEnum.values(), name: name)
            } else {
                return nil
            }
        }()

        let shutterSpeed = {
            if let name = map["shutterSpeed"] as? String {
                return getEnumValue(values: ThetaRepository.ShutterSpeedEnum.values(), name: name)
            } else {
                return nil
            }
        }()

        let whiteBalance = {
            if let name = map["whiteBalance"] as? String {
                return getEnumValue(values: ThetaRepository.WhiteBalanceEnum.values(), name: name)
            } else {
                return nil
            }
        }()

        autoBracket.add(setting: ThetaRepository.BracketSetting(
            aperture: aperture,
            colorTemperature: colorTemperature,
            exposureCompensation: exposureCompensation,
            exposureProgram: exposureProgram,
            iso: iso,
            shutterSpeed: shutterSpeed,
            whiteBalance: whiteBalance
        ))
    }

    return autoBracket
}

func convertResult(autoBracket: ThetaRepository.BracketSettingList) -> [[String: Any]] {
    var resultList = [[String: Any]]()

    autoBracket.list.forEach { bracketSetting in
        if let setting = bracketSetting as? ThetaRepository.BracketSetting {
            resultList.append([
                "aperture": setting.aperture?.name,
                "colorTemperature": setting.colorTemperature?.intValue,
                "exposureCompensation": setting.exposureCompensation?.name,
                "exposureProgram": setting.exposureProgram?.name,
                "iso": setting.iso?.name,
                "shutterSpeed": setting.shutterSpeed?.name,
                "whiteBalance": setting.whiteBalance?.name,
            ])
        }
    }

    return resultList
}

func setCompositeIntervalCaptureBuilderParams(params: [String: Any], builder: CompositeIntervalCapture.Builder) {
    if let interval = params["_capture_interval"] as? Int,
       interval >= 0
    {
        builder.setCheckStatusCommandInterval(timeMillis: Int64(interval))
    }
    if let value = params[ThetaRepository.OptionNameEnum.compositeshootingoutputinterval.name] as? Int32 {
        builder.setCompositeShootingOutputInterval(sec: value)
    }
}

func setBurstCaptureBuilderParams(params: [String: Any], builder: BurstCapture.Builder) {
    if let interval = params["_capture_interval"] as? Int,
       interval >= 0
    {
        builder.setCheckStatusCommandInterval(timeMillis: Int64(interval))
    }
    if let value = params[ThetaRepository.OptionNameEnum.burstmode.name] as? String {
        if let enumValue = getEnumValue(values: ThetaRepository.BurstModeEnum.values(), name: value) {
            builder.setBurstMode(mode: enumValue)
        }
    }
}

func setMultiBracketCaptureBuilderParams(params: [String: Any], builder: MultiBracketCapture.Builder) {
    if let interval = params["_capture_interval"] as? Int,
       interval >= 0
    {
        builder.setCheckStatusCommandInterval(timeMillis: Int64(interval))
    }
    if let autoBracket = params[ThetaRepository.OptionNameEnum.autobracket.name] as? [[String: Any]] {
        autoBracket.forEach { map in
            let aperture = {
                if let name = map["aperture"] as? String {
                    return getEnumValue(values: ThetaRepository.ApertureEnum.values(), name: name)
                } else {
                    return nil
                }
            }()

            let colorTemperature = {
                if let value = map["colorTemperature"] as? Int {
                    return toKotlinInt(value: value)
                } else {
                    return nil
                }
            }()

            let exposureCompensation = {
                if let name = map["exposureCompensation"] as? String {
                    return getEnumValue(values: ThetaRepository.ExposureCompensationEnum.values(), name: name)
                } else {
                    return nil
                }
            }()

            let exposureProgram = {
                if let name = map["exposureProgram"] as? String {
                    return getEnumValue(values: ThetaRepository.ExposureProgramEnum.values(), name: name)
                } else {
                    return nil
                }
            }()

            let iso = {
                if let name = map["iso"] as? String {
                    return getEnumValue(values: ThetaRepository.IsoEnum.values(), name: name)
                } else {
                    return nil
                }
            }()

            let shutterSpeed = {
                if let name = map["shutterSpeed"] as? String {
                    return getEnumValue(values: ThetaRepository.ShutterSpeedEnum.values(), name: name)
                } else {
                    return nil
                }
            }()

            let whiteBalance = {
                if let name = map["whiteBalance"] as? String {
                    return getEnumValue(values: ThetaRepository.WhiteBalanceEnum.values(), name: name)
                } else {
                    return nil
                }
            }()

            builder.addBracketParameters(
                aperture: aperture,
                colorTemperature: colorTemperature,
                exposureCompensation: exposureCompensation,
                exposureProgram: exposureProgram,
                iso: iso,
                shutterSpeed: shutterSpeed,
                whiteBalance: whiteBalance
            )
        }
    }
}

func setContinuousCaptureBuilderParams(params: [String: Any], builder: ContinuousCapture.Builder) {
    if let interval = params["_capture_interval"] as? Int,
       interval >= 0
    {
        builder.setCheckStatusCommandInterval(timeMillis: Int64(interval))
    }
    if let value = params["PhotoFileFormat"] as? String {
        if let enumValue = getEnumValue(values: ThetaRepository.PhotoFileFormatEnum.values(), name: value) {
            builder.setFileFormat(fileFormat: enumValue)
        }
    }
}

func toBitrate(value: Any) -> ThetaRepositoryBitrate? {
    if value is NSNumber, let intVal = value as? Int32 {
        return ThetaRepository.BitrateNumber(value: intVal)
    } else if let name = value as? String, let enumValue = getEnumValue(values: ThetaRepository.BitrateEnum.values(), name: name) {
        return enumValue
    } else if let str = value as? String {
        return ThetaRepository.BitrateEnum.companion.get(str: str)
    } else {
        return nil
    }
}

func toBurstOption(params: [String: Any]) -> ThetaRepository.BurstOption {
    var burstCaptureNum: ThetaRepository.BurstCaptureNumEnum? = nil
    if let name = params["burstCaptureNum"] as? String {
        burstCaptureNum = getEnumValue(values: ThetaRepository.BurstCaptureNumEnum.values(), name: name)
    }

    var burstBracketStep: ThetaRepository.BurstBracketStepEnum? = nil
    if let name = params["burstBracketStep"] as? String {
        burstBracketStep = getEnumValue(values: ThetaRepository.BurstBracketStepEnum.values(), name: name)
    }

    var burstCompensation: ThetaRepository.BurstCompensationEnum? = nil
    if let name = params["burstCompensation"] as? String {
        burstCompensation = getEnumValue(values: ThetaRepository.BurstCompensationEnum.values(), name: name)
    }

    var burstMaxExposureTime: ThetaRepository.BurstMaxExposureTimeEnum? = nil
    if let name = params["burstMaxExposureTime"] as? String {
        burstMaxExposureTime = getEnumValue(values: ThetaRepository.BurstMaxExposureTimeEnum.values(), name: name)
    }

    var burstEnableIsoControl: ThetaRepository.BurstEnableIsoControlEnum? = nil
    if let name = params["burstEnableIsoControl"] as? String {
        burstEnableIsoControl = getEnumValue(values: ThetaRepository.BurstEnableIsoControlEnum.values(), name: name)
    }

    var burstOrder: ThetaRepository.BurstOrderEnum? = nil
    if let name = params["burstOrder"] as? String {
        burstOrder = getEnumValue(values: ThetaRepository.BurstOrderEnum.values(), name: name)
    }

    return ThetaRepository.BurstOption(
        burstCaptureNum: burstCaptureNum,
        burstBracketStep: burstBracketStep,
        burstCompensation: burstCompensation,
        burstMaxExposureTime: burstMaxExposureTime,
        burstEnableIsoControl: burstEnableIsoControl,
        burstOrder: burstOrder
    )
}

func convertResult(burstOption: ThetaRepository.BurstOption) -> [String: Any?] {
    return [
        "burstCaptureNum": burstOption.burstCaptureNum?.name,
        "burstBracketStep": burstOption.burstBracketStep?.name,
        "burstCompensation": burstOption.burstCompensation?.name,
        "burstMaxExposureTime": burstOption.burstMaxExposureTime?.name,
        "burstEnableIsoControl": burstOption.burstEnableIsoControl?.name,
        "burstOrder": burstOption.burstOrder?.name,
    ]
}

func toEthernetConfig(params: [String: Any]) -> ThetaRepository.EthernetConfig {
    let proxy: ThetaRepository.Proxy? = {
        if let proxyMap = params["proxy"] as? [String: Any] {
            toProxy(params: proxyMap)
        } else {
            nil
        }
    }()
    
    return ThetaRepository.EthernetConfig(
        usingDhcp: params["usingDhcp"] as? Bool ?? true,
        ipAddress: params["ipAddress"] as? String,
        subnetMask: params["subnetMask"] as? String,
        defaultGateway: params["defaultGateway"] as? String,
        proxy: proxy
    )
}

func toGpsInfo(params: [String: Any]) -> ThetaRepository.GpsInfo {
    return ThetaRepository.GpsInfo(
        latitude: Float(params["latitude"] as? Double ?? 0),
        longitude: Float(params["longitude"] as? Double ?? 0),
        altitude: Float(params["altitude"] as? Double ?? 0),
        dateTimeZone: params["dateTimeZone"] as! String
    )
}

func toOffDelay(value: Any) -> ThetaRepositoryOffDelay? {
    if let strValue = value as? String {
        return getEnumValue(values: ThetaRepository.OffDelayEnum.values(), name: strValue)
    } else if let intValue = value as? Int32 {
        return ThetaRepository.OffDelaySec(sec: intValue)
    }
    return nil
}

func toProxy(params: [String: Any]) -> ThetaRepository.Proxy {
    return ThetaRepository.Proxy(
        use: params["use"] as? Bool ?? false,
        url: params["url"] as? String,
        port: toKotlinInt(value: params["port"]),
        userid: params["userid"] as? String,
        password: params["password"] as? String
    )
}

func toSleepDelay(value: Any) -> ThetaRepositorySleepDelay? {
    if let strValue = value as? String {
        return getEnumValue(values: ThetaRepository.SleepDelayEnum.values(), name: strValue)
    } else if let intValue = value as? Int32 {
        return ThetaRepository.SleepDelaySec(sec: intValue)
    }
    return nil
}

func toTimeShift(params: [String: Any]) -> ThetaRepository.TimeShiftSetting {
    var firstInterval: ThetaRepository.TimeShiftIntervalEnum? = nil
    if let name = params["firstInterval"] as? String {
        firstInterval = getEnumValue(values: ThetaRepository.TimeShiftIntervalEnum.values(), name: name)
    }

    var secondInterval: ThetaRepository.TimeShiftIntervalEnum? = nil
    if let name = params["secondInterval"] as? String {
        secondInterval = getEnumValue(values: ThetaRepository.TimeShiftIntervalEnum.values(), name: name)
    }

    return ThetaRepository.TimeShiftSetting(
        isFrontFirst: toKotlinBoolean(value: params["isFrontFirst"]),
        firstInterval: firstInterval,
        secondInterval: secondInterval
    )
}

func toTopBottomCorrectionRotation(params: [String: Any]) -> ThetaRepository.TopBottomCorrectionRotation {
    return ThetaRepository.TopBottomCorrectionRotation(
        pitch: Float(params["pitch"] as? Double ?? 0),
        roll: Float(params["roll"] as? Double ?? 0),
        yaw: Float(params["yaw"] as? Double ?? 0)
    )
}

func convertGetOptionsParam(params: [String]) -> [ThetaRepository.OptionNameEnum] {
    var array: [ThetaRepository.OptionNameEnum] = []
    let values = ThetaRepository.OptionNameEnum.values()
    for name in params {
        array.append(getEnumValue(values: values, name: name)!)
    }
    return array
}

func convertResult(ethernetConfig: ThetaRepository.EthernetConfig) -> [String: Any] {
    var result: [String: Any] = [
        "usingDhcp": ethernetConfig.usingDhcp 
    ]
    if let ipAddress = ethernetConfig.ipAddress {
        result["ipAddress"] = ipAddress
    }
    if let subnetMask = ethernetConfig.subnetMask {
        result["subnetMask"] = subnetMask
    }
    if let defaultGateway = ethernetConfig.defaultGateway {
        result["defaultGateway"] = defaultGateway
    }
    if let proxy = ethernetConfig.proxy {
        result["proxy"] = convertResult(proxy: proxy)
    }
    return result
}

func convertResult(gpsInfo: ThetaRepository.GpsInfo) -> [String: Any] {
    return [
        "latitude": gpsInfo.latitude,
        "longitude": gpsInfo.longitude,
        "altitude": gpsInfo.altitude,
        "dateTimeZone": gpsInfo.dateTimeZone,
    ]
}

func convertResult(stateGpsInfo: ThetaRepository.StateGpsInfo) -> [String: Any] {
    guard let gpsInfo = stateGpsInfo.gpsInfo else { return [:] }
    return [
        KEY_GPS_INFO: convertResult(gpsInfo: gpsInfo),
    ]
}

func convertResult(proxy: ThetaRepository.Proxy) -> [String: Any] {
    return [
        "use": proxy.use,
        "url": proxy.url,
        "port": proxy.port,
        "userid": proxy.userid,
        "password": proxy.password,
    ]
}

func convertResult(timeshift: ThetaRepository.TimeShiftSetting) -> [String: Any] {
    return [
        "isFrontFirst": convertKotlinBooleanToBool(value: timeshift.isFrontFirst),
        "firstInterval": timeshift.firstInterval?.name,
        "secondInterval": timeshift.secondInterval?.name,
    ]
}

func convertResult(rotation: ThetaRepository.TopBottomCorrectionRotation) -> [String: Any] {
    return [
        "pitch": rotation.pitch,
        "roll": rotation.roll,
        "yaw": rotation.yaw,
    ]
}

func convertResult(options: ThetaRepository.Options) -> [String: Any] {
    var result = [String: Any]()
    let nameList = ThetaRepository.OptionNameEnum.values()
    for i in 0 ..< nameList.size {
        let name = nameList.get(index: i)!
        if let value = try? options.getValue(name: name) {
            if value is KotlinEnum<AnyObject> {
                let enumValue = value as! KotlinEnum<AnyObject>
                result[name.name] = enumValue.name
            } else if value is KotlinBoolean {
                result[name.name] = value as! Bool ? true : false
            } else if value is NSNumber || value is String {
                result[name.name] = value
            } else if let bitrate = value as? ThetaRepository.BitrateNumber {
                result[name.name] = bitrate.value
            } else if value is ThetaRepository.BracketSettingList, let autoBracket = value as? ThetaRepository.BracketSettingList {
                result[name.name] = convertResult(autoBracket: autoBracket)
            } else if value is ThetaRepository.BurstOption, let burstOption = value as? ThetaRepository.BurstOption {
                result[name.name] = convertResult(burstOption: burstOption)
            } else if value is ThetaRepository.EthernetConfig, let ethernetConfig = value as? ThetaRepository.EthernetConfig {
                result[name.name] = convertResult(ethernetConfig: ethernetConfig)
            } else if value is ThetaRepository.GpsInfo {
                let gpsInfo = value as! ThetaRepository.GpsInfo
                result[name.name] = convertResult(gpsInfo: gpsInfo)
            } else if let offDelay = value as? ThetaRepositoryOffDelay {
                if let enumValue = offDelay as? ThetaRepository.OffDelayEnum {
                    result[name.name] = enumValue.name
                } else {
                    result[name.name] = offDelay.sec
                }
            } else if value is ThetaRepository.Proxy, let proxy = value as? ThetaRepository.Proxy {
                result[name.name] = convertResult(proxy: proxy)
            } else if let sleepDelay = value as? ThetaRepositorySleepDelay {
                if let enumValue = sleepDelay as? ThetaRepository.SleepDelayEnum {
                    result[name.name] = enumValue.name
                } else {
                    result[name.name] = sleepDelay.sec
                }
            } else if value is ThetaRepository.TimeShiftSetting, let timeshift = value as? ThetaRepository.TimeShiftSetting {
                result[name.name] = convertResult(timeshift: timeshift)
            } else if value is ThetaRepository.TopBottomCorrectionRotation, let rotation = value as? ThetaRepository.TopBottomCorrectionRotation {
                result[name.name] = convertResult(rotation: rotation)
            }
        }
    }
    return result
}

func convertKotlinBooleanToBool(value: Any?) -> Bool? {
    guard let value = value else { return nil }
    guard value is KotlinBoolean, let numVal = value as? NSNumber else { return false }

    return numVal.boolValue
}

func toKotlinInt(value: Any?) -> KotlinInt? {
    guard let value = value as? Int else {
        return nil
    }
    return KotlinInt(integerLiteral: value)
}

func toKotlinBoolean(value: Any?) -> KotlinBoolean? {
    guard let value = value as? Bool else {
        return nil
    }
    return KotlinBoolean(booleanLiteral: value)
}

func convertSetOptionsParam(params: [String: Any]) -> ThetaRepository.Options {
    let options = ThetaRepository.Options()
    params.forEach { key, value in
        setOptionsValue(options: options, name: key, value: value)
    }
    return options
}

func setOptionsValue(options: ThetaRepository.Options, name: String, value: Any) {
    switch name {
    case ThetaRepository.OptionNameEnum.aiautothumbnail.name:
        options.aiAutoThumbnail = getEnumValue(values: ThetaRepository.AiAutoThumbnailEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.aperture.name:
        options.aperture = getEnumValue(values: ThetaRepository.ApertureEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.autobracket.name:
        if let params = value as? [[String: Any]] {
            options.autoBracket = toAutoBracket(params: params)
        }
    case ThetaRepository.OptionNameEnum.bitrate.name:
        options.bitrate = toBitrate(value: value)
    case ThetaRepository.OptionNameEnum.bluetoothpower.name:
        options.bluetoothPower = getEnumValue(values: ThetaRepository.BluetoothPowerEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.bluetoothrole.name:
        options.bluetoothRole = getEnumValue(values: ThetaRepository.BluetoothRoleEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.burstmode.name:
        options.burstMode = getEnumValue(values: ThetaRepository.BurstModeEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.burstoption.name:
        if let params = value as? [String: Any] {
            options.burstOption = toBurstOption(params: params)
        }
    case ThetaRepository.OptionNameEnum.cameracontrolsource.name:
        options.cameraControlSource = getEnumValue(values: ThetaRepository.CameraControlSourceEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.cameramode.name:
        options.cameraMode = getEnumValue(values: ThetaRepository.CameraModeEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.captureinterval.name:
        options.captureInterval = KotlinInt(integerLiteral: value as! Int)
    case ThetaRepository.OptionNameEnum.capturemode.name:
        options.captureMode = getEnumValue(values: ThetaRepository.CaptureModeEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.capturenumber.name:
        options.captureNumber = KotlinInt(integerLiteral: value as! Int)
    case ThetaRepository.OptionNameEnum.colortemperature.name:
        options.colorTemperature = KotlinInt(integerLiteral: value as! Int)
    case ThetaRepository.OptionNameEnum.compositeshootingoutputinterval.name:
        options.compositeShootingOutputInterval = KotlinInt(integerLiteral: value as! Int)
    case ThetaRepository.OptionNameEnum.compositeshootingtime.name:
        options.compositeShootingTime = KotlinInt(integerLiteral: value as! Int)
    case ThetaRepository.OptionNameEnum.continuousnumber.name:
        options.continuousNumber = getEnumValue(values: ThetaRepository.ContinuousNumberEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.datetimezone.name:
        options.dateTimeZone = value as? String
    case ThetaRepository.OptionNameEnum.ethernetconfig.name:
        if let params = value as? [String: Any] {
            options.ethernetConfig = toEthernetConfig(params: params)
        }
    case ThetaRepository.OptionNameEnum.exposurecompensation.name:
        options.exposureCompensation = getEnumValue(values: ThetaRepository.ExposureCompensationEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.exposuredelay.name:
        options.exposureDelay = getEnumValue(values: ThetaRepository.ExposureDelayEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.exposureprogram.name:
        options.exposureProgram = getEnumValue(values: ThetaRepository.ExposureProgramEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.facedetect.name:
        options.faceDetect = getEnumValue(values: ThetaRepository.FaceDetectEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.fileformat.name:
        options.fileFormat = getEnumValue(values: ThetaRepository.FileFormatEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.filter.name:
        options.filter = getEnumValue(values: ThetaRepository.FilterEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.function.name:
        options.function = getEnumValue(values: ThetaRepository.ShootingFunctionEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.gain.name:
        options.gain = getEnumValue(values: ThetaRepository.GainEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.gpsinfo.name:
        options.gpsInfo = toGpsInfo(params: value as! [String: Any])
    case ThetaRepository.OptionNameEnum.imagestitching.name:
        options.imageStitching = getEnumValue(values: ThetaRepository.ImageStitchingEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.isgpson.name:
        options.isGpsOn = (value as! Bool) ? true : false
    case ThetaRepository.OptionNameEnum.iso.name:
        options.iso = getEnumValue(values: ThetaRepository.IsoEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.isoautohighlimit.name:
        options.isoAutoHighLimit = getEnumValue(values: ThetaRepository.IsoAutoHighLimitEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.language.name:
        options.language = getEnumValue(values: ThetaRepository.LanguageEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.latestenabledexposuredelaytime.name:
        options.latestEnabledExposureDelayTime = getEnumValue(values: ThetaRepository.ExposureDelayEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.maxrecordabletime.name:
        options.maxRecordableTime = getEnumValue(values: ThetaRepository.MaxRecordableTimeEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.networktype.name:
        options.networkType = getEnumValue(values: ThetaRepository.NetworkTypeEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.offdelay.name:
        options.offDelay = toOffDelay(value: value)
    case ThetaRepository.OptionNameEnum.password.name:
        options.password = value as? String
    case ThetaRepository.OptionNameEnum.powersaving.name:
        options.powerSaving = getEnumValue(values: ThetaRepository.PowerSavingEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.preset.name:
        options.preset = getEnumValue(values: ThetaRepository.PresetEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.previewformat.name:
        options.previewFormat = getEnumValue(values: ThetaRepository.PreviewFormatEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.proxy.name:
        if let params = value as? [String: Any] {
            options.proxy = toProxy(params: params)
        }
    case ThetaRepository.OptionNameEnum.remainingpictures.name:
        options.remainingPictures = KotlinInt(integerLiteral: value as! Int)
    case ThetaRepository.OptionNameEnum.remainingvideoseconds.name:
        options.remainingVideoSeconds = KotlinInt(integerLiteral: value as! Int)
    case ThetaRepository.OptionNameEnum.remainingspace.name:
        options.remainingSpace = KotlinLong(integerLiteral: value as! Int)
    case ThetaRepository.OptionNameEnum.shootingmethod.name:
        options.shootingMethod = getEnumValue(values: ThetaRepository.ShootingMethodEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.shutterspeed.name:
        options.shutterSpeed = getEnumValue(values: ThetaRepository.ShutterSpeedEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.shuttervolume.name:
        options.shutterVolume = KotlinInt(integerLiteral: value as! Int)
    case ThetaRepository.OptionNameEnum.sleepdelay.name:
        options.sleepDelay = toSleepDelay(value: value)
    case ThetaRepository.OptionNameEnum.timeshift.name:
        if let params = value as? [String: Any] {
            options.timeShift = toTimeShift(params: params)
        }
    case ThetaRepository.OptionNameEnum.topbottomcorrection.name:
        options.topBottomCorrection = getEnumValue(values: ThetaRepository.TopBottomCorrectionOptionEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.topbottomcorrectionrotation.name:
        options.topBottomCorrectionRotation = toTopBottomCorrectionRotation(params: value as! [String: Any])
    case ThetaRepository.OptionNameEnum.totalspace.name:
        options.totalSpace = KotlinLong(integerLiteral: value as! Int)
    case ThetaRepository.OptionNameEnum.username.name:
        options.username = value as? String
    case ThetaRepository.OptionNameEnum.videostitching.name:
        options.videoStitching = getEnumValue(values: ThetaRepository.VideoStitchingEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.visibilityreduction.name:
        options.visibilityReduction = getEnumValue(values: ThetaRepository.VisibilityReductionEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.whitebalance.name:
        options.whiteBalance = getEnumValue(values: ThetaRepository.WhiteBalanceEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.whitebalanceautostrength.name:
        options.whiteBalanceAutoStrength = getEnumValue(values: ThetaRepository.WhiteBalanceAutoStrengthEnum.values(), name: value as! String)!
    case ThetaRepository.OptionNameEnum.wlanfrequency.name:
        options.wlanFrequency = getEnumValue(values: ThetaRepository.WlanFrequencyEnum.values(), name: value as! String)!
    default: break
    }
}

func toDigetAuth(params: [String: String?]?) -> DigestAuth? {
    guard let params = params,
          let username = params["username"] as? String
    else { return nil }

    let password = params["password"] as? String
    return DigestAuth(username: username, password: password)
}

func toConfig(params: [String: Any]) -> ThetaRepository.Config {
    let config = ThetaRepository.Config()
    params.forEach { key, value in
        switch key {
        case ThetaRepository.OptionNameEnum.datetimezone.name:
            config.dateTime = value as? String
        case ThetaRepository.OptionNameEnum.language.name:
            config.language = getEnumValue(values: ThetaRepository.LanguageEnum.values(), name: value as! String)!
        case ThetaRepository.OptionNameEnum.offdelay.name:
            config.offDelay = toOffDelay(value: value)
        case ThetaRepository.OptionNameEnum.sleepdelay.name:
            config.sleepDelay = toSleepDelay(value: value)
        case ThetaRepository.OptionNameEnum.shuttervolume.name:
            config.shutterVolume = KotlinInt(integerLiteral: value as! Int)
        case KEY_CLIENT_MODE:
            config.clientMode = toDigetAuth(params: value as? [String: String?])
        default:
            break
        }
    }
    return config
}

func toTimeout(params: [String: Any]) -> ThetaRepository.Timeout {
    return ThetaRepository.Timeout(
        connectTimeout: params["connectTimeout"] as! Int64,
        requestTimeout: params["requestTimeout"] as! Int64,
        socketTimeout: params["socketTimeout"] as! Int64
    )
}

func convertResult(exif: ThetaRepository.Exif) -> [String: Any] {
    var result = [String: Any]()
    result["exifVersion"] = exif.exifVersion
    result["dateTime"] = exif.dateTime
    if let imageWidth = exif.imageWidth {
        result["imageWidth"] = imageWidth
    }
    if let imageLength = exif.imageLength {
        result["imageLength"] = imageLength
    }
    if let gpsLatitude = exif.gpsLatitude {
        result["gpsLatitude"] = gpsLatitude
    }
    if let gpsLongitude = exif.gpsLongitude {
        result["gpsLongitude"] = gpsLongitude
    }
    return result
}

func convertResult(xmp: ThetaRepository.Xmp) -> [String: Any] {
    var result = [String: Any]()
    result["fullPanoWidthPixels"] = xmp.fullPanoWidthPixels
    result["fullPanoHeightPixels"] = xmp.fullPanoHeightPixels
    if let poseHeadingDegrees = xmp.poseHeadingDegrees {
        result["poseHeadingDegrees"] = poseHeadingDegrees
    }
    return result
}

func convertResult(metadata: KotlinPair<ThetaRepository.Exif, ThetaRepository.Xmp>) -> [String: Any] {
    return [
        "exif": convertResult(exif: metadata.first!),
        "xmp": convertResult(xmp: metadata.second!),
    ]
}

func convertResult(accessPointList: [ThetaRepository.AccessPoint]) -> [[String: Any]] {
    var resultList = [[String: Any]]()
    accessPointList.forEach { accessPoint in
        var result = [String: Any]()
        result["ssid"] = accessPoint.ssid
        result["ssidStealth"] = accessPoint.ssidStealth
        result["authMode"] = accessPoint.authMode.name
        result["connectionPriority"] = accessPoint.connectionPriority
        result["usingDhcp"] = accessPoint.usingDhcp
        if let ipAddress = accessPoint.ipAddress {
            result["ipAddress"] = ipAddress
        }
        if let subnetMask = accessPoint.subnetMask {
            result["subnetMask"] = subnetMask
        }
        if let defaultGateway = accessPoint.defaultGateway {
            result["defaultGateway"] = defaultGateway
        }
        if let proxy = accessPoint.proxy {
            result["proxy"] = convertResult(proxy: proxy)
        }
        resultList.append(result)
    }
    return resultList
}

func toPluginInfosResult(pluginInfoList: [ThetaRepository.PluginInfo]) -> [[String: Any]] {
    var resultList = [[String: Any]]()
    pluginInfoList.forEach { pluginInfo in
        let item = [
            "name": pluginInfo.name,
            "packageName": pluginInfo.packageName,
            "version": pluginInfo.version,
            "isPreInstalled": pluginInfo.isPreInstalled,
            "isRunning": pluginInfo.isRunning,
            "isForeground": pluginInfo.isForeground,
            "isBoot": pluginInfo.isBoot,
            "hasWebServer": pluginInfo.hasWebServer,
            "exitStatus": pluginInfo.exitStatus,
            "message": pluginInfo.message,
        ]
        resultList.append(item)
    }
    return resultList
}

func toNotify(id: Int, params: [String: Any]?) -> [String: Any] {
    var result: [String: Any] = [
        KEY_NOTIFY_ID: id,
    ]
    if let params = params {
        result[KEY_NOTIFY_PARAMS] = params
    }
    return result
}

func toCaptureProgressNotifyParam(value: Float) -> [String: Any] {
    return [
        KEY_NOTIFY_PARAM_COMPLETION: value,
    ]
}

func toPreviewNotifyParam(imageData: FlutterStandardTypedData) -> [String: Any] {
    return [
        KEY_NOTIFY_PARAM_IMAGE: imageData,
    ]
}

func toMessageNotifyParam(message: String) -> [String: Any] {
    return [
        KEY_NOTIFY_PARAM_MESSAGE: message,
    ]
}

func toCapturingNotifyParam(value: CapturingStatusEnum) -> [String: Any] {
    return [
        KEY_NOTIFY_PARAM_STATUS: value.name,
    ]
}
