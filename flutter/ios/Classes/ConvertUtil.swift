import Flutter
import UIKit
import THETAClient

let KEY_CLIENT_MODE = "clientMode"

public class ConvertUtil: NSObject {
}

func getEnumValue<T, E: KotlinEnum<T>>(values: KotlinArray<E>, name: String) -> E? {
    for i in 0..<values.size {
        let item = values.get(index: i)!
        if item.name == name {
            return item
        }
    }
    return nil
}

func convertResult(fileInfoList: [ThetaRepository.FileInfo]) -> [[String: Any]] {
    var resultList = [[String: Any]]()
    fileInfoList.forEach({ fileInfo in
        var item = [
            "name": fileInfo.name,
            "size": fileInfo.size,
            "dateTime": fileInfo.dateTime,
            "fileUrl": fileInfo.fileUrl,
            "thumbnailUrl": fileInfo.thumbnailUrl,
        ]
        if let storageID = fileInfo.storageID {
            item["storageID"] = storageID
        }
        resultList.append(item)
    })
    return resultList
}

func convertResult(thetaInfo: ThetaRepository.ThetaInfo) -> [String: Any?] {
    return  [
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
    return  [
        "fingerprint": thetaState.fingerprint,
        "batteryLevel": thetaState.batteryLevel,
        "storageUri": thetaState.storageUri,
        "storageID": thetaState.storageID,
        "captureStatus": thetaState.captureStatus.name,
        "recordedTime": thetaState.recordedTime,
        "recordableTime": thetaState.recordableTime,
        "capturedPictures": thetaState.capturedPictures,
        "compositeShootingElapsedTime": thetaState.compositeShootingElapsedTime,
        "latestFileUrl": thetaState.latestFileUrl,
        "chargingState": thetaState.chargingState.name,
        "apiVersion": thetaState.apiVersion,
        "isPluginRunning": convertKotlinBooleanToBool(value: thetaState.isPluginRunning),
        "isPluginWebServer": convertKotlinBooleanToBool(value: thetaState.isPluginWebServer),
        "function": thetaState.function?.name,
        "isMySettingChanged": convertKotlinBooleanToBool(value: thetaState.isMySettingChanged),
        "currentMicrophone": thetaState.currentMicrophone?.name,
        "isSdCard": convertKotlinBooleanToBool(value: thetaState.isSdCard),
        "cameraError": convertResult(cameraErrorList: thetaState.cameraError),
        "isBatteryInsert": convertKotlinBooleanToBool(value: thetaState.isBatteryInsert),
    ]
}

func setCaptureBuilderParams<T>(params: [String : Any], builder: CaptureBuilder<T>) {
    if let value = params[ThetaRepository.OptionNameEnum.aperture.name]  {
        if let enumValue = getEnumValue(values: ThetaRepository.ApertureEnum.values(), name: value as! String) {
            builder.setAperture(aperture: enumValue)
        }
    }
    if let value = params[ThetaRepository.OptionNameEnum.colortemperature.name]  {
        builder.setColorTemperature(kelvin: value as! Int32)
    }
    if let value = params[ThetaRepository.OptionNameEnum.exposurecompensation.name]  {
        if let enumValue = getEnumValue(values: ThetaRepository.ExposureCompensationEnum.values(), name: value as! String) {
            builder.setExposureCompensation(value: enumValue)
        }
    }
    if let value = params[ThetaRepository.OptionNameEnum.exposuredelay.name]  {
        if let enumValue = getEnumValue(values: ThetaRepository.ExposureDelayEnum.values(), name: value as! String) {
            builder.setExposureDelay(delay: enumValue)
        }
    }
    if let value = params[ThetaRepository.OptionNameEnum.exposureprogram.name]  {
        if let enumValue = getEnumValue(values: ThetaRepository.ExposureProgramEnum.values(), name: value as! String) {
            builder.setExposureProgram(program: enumValue)
        }
    }
    if let value = params[ThetaRepository.OptionNameEnum.gpsinfo.name] {
        builder.setGpsInfo(gpsInfo: toGpsInfo(params: value as! [String : Any]))
    }
    if let value = params["GpsTagRecording"] {
        if let enumValue = getEnumValue(values: ThetaRepository.GpsTagRecordingEnum.values(), name: value as! String) {
            builder.setGpsTagRecording(value: enumValue)
        }
    }
    if let value = params[ThetaRepository.OptionNameEnum.iso.name] {
        if let enumValue = getEnumValue(values: ThetaRepository.IsoEnum.values(), name: value as! String) {
            builder.setIso(iso: enumValue)
        }
    }
    if let value = params[ThetaRepository.OptionNameEnum.isoautohighlimit.name]  {
        if let enumValue = getEnumValue(values: ThetaRepository.IsoAutoHighLimitEnum.values(), name: value as! String) {
            builder.setIsoAutoHighLimit(iso: enumValue)
        }
    }
    if let value = params[ThetaRepository.OptionNameEnum.whitebalance.name]  {
        if let enumValue = getEnumValue(values: ThetaRepository.WhiteBalanceEnum.values(), name: value as! String) {
            builder.setWhiteBalance(whiteBalance: enumValue)
        }
    }
}

func setPhotoCaptureBuilderParams(params: [String : Any], builder: PhotoCapture.Builder) {
    if let value = params[ThetaRepository.OptionNameEnum.filter.name]  {
        if let enumValue = getEnumValue(values: ThetaRepository.FilterEnum.values(), name: value as! String) {
            builder.setFilter(filter: enumValue)
        }
    }
    if let value = params["PhotoFileFormat"]  {
        if let enumValue = getEnumValue(values: ThetaRepository.PhotoFileFormatEnum.values(), name: value as! String) {
            builder.setFileFormat(fileFormat: enumValue)
        }
    }
}

func setVideoCaptureBuilderParams(params: [String : Any], builder: VideoCapture.Builder) {
    if let value = params[ThetaRepository.OptionNameEnum.maxrecordabletime.name]  {
        if let enumValue = getEnumValue(values: ThetaRepository.MaxRecordableTimeEnum.values(), name: value as! String) {
            builder.setMaxRecordableTime(time: enumValue)
        }
    }
    if let value = params["VideoFileFormat"]  {
        if let enumValue = getEnumValue(values: ThetaRepository.VideoFileFormatEnum.values(), name: value as! String) {
            builder.setFileFormat(fileFormat: enumValue)
        }
    }
}

func toBurstOption(params: [String : Any]) -> ThetaRepository.BurstOption {
    var burstCaptureNum: ThetaRepository.BurstCaptureNumEnum? = nil;
    if let name = params["burstCaptureNum"] as? String {
        burstCaptureNum = getEnumValue(values: ThetaRepository.BurstCaptureNumEnum.values(), name: name)
    }
    
    var burstBracketStep: ThetaRepository.BurstBracketStepEnum? = nil;
    if let name = params["burstBracketStep"] as? String {
        burstBracketStep = getEnumValue(values: ThetaRepository.BurstBracketStepEnum.values(), name: name)
    }
    
    var burstCompensation: ThetaRepository.BurstCompensationEnum? = nil;
    if let name = params["burstCompensation"] as? String {
        burstCompensation = getEnumValue(values: ThetaRepository.BurstCompensationEnum.values(), name: name)
    }
    
    var burstMaxExposureTime: ThetaRepository.BurstMaxExposureTimeEnum? = nil;
    if let name = params["burstMaxExposureTime"] as? String {
        burstMaxExposureTime = getEnumValue(values: ThetaRepository.BurstMaxExposureTimeEnum.values(), name: name)
    }
    
    var burstEnableIsoControl: ThetaRepository.BurstEnableIsoControlEnum? = nil;
    if let name = params["burstEnableIsoControl"] as? String {
        burstEnableIsoControl = getEnumValue(values: ThetaRepository.BurstEnableIsoControlEnum.values(), name: name)
    }
    
    var burstOrder: ThetaRepository.BurstOrderEnum? = nil;
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

func convertResult(burstOption: ThetaRepository.BurstOption) -> [String: Any] {
    return [
        "burstCaptureNum": burstOption.burstCaptureNum?.value.name,
        "burstBracketStep": burstOption.burstBracketStep?.value.name,
        "burstCompensation": burstOption.burstCompensation?.value.name,
        "burstMaxExposureTime": burstOption.burstEnableIsoControl?.value.name,
        "burstEnableIsoControl": burstOption.burstEnableIsoControl?.value.name,
        "burstOrder": burstOption.burstOrder?.value.name,
    ]
}

func toGpsInfo(params: [String : Any]) -> ThetaRepository.GpsInfo {
    return ThetaRepository.GpsInfo(
        latitude: params["latitude"] as! Float,
        longitude: params["longitude"] as! Float,
        altitude: params["altitude"] as! Float,
        dateTimeZone: params["dateTimeZone"] as! String
    )
}

func toProxy(params: [String : Any]) -> ThetaRepository.Proxy {
    return ThetaRepository.Proxy(
        use: params["use"] as? Bool ?? false,
        url: params["url"] as? String,
        port: toKotlinInt(value: params["port"]),
        userid: params["userid"] as? String,
        password: params["password"] as? String
    )
}

func toTimeShift(params: [String : Any]) -> ThetaRepository.TimeShiftSetting {
    var firstInterval: ThetaRepository.TimeShiftIntervalEnum? = nil;
    if let name = params["firstInterval"] as? String {
        firstInterval = getEnumValue(values: ThetaRepository.TimeShiftIntervalEnum.values(), name: name)
    }

    var secondInterval: ThetaRepository.TimeShiftIntervalEnum? = nil;
    if let name = params["secondInterval"] as? String {
        secondInterval = getEnumValue(values: ThetaRepository.TimeShiftIntervalEnum.values(), name: name)
    }

    return ThetaRepository.TimeShiftSetting(
        isFrontFirst: toKotlinBoolean(value: params["isFrontFirst"]),
        firstInterval: firstInterval,
        secondInterval: secondInterval
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

func convertResult(gpsInfo: ThetaRepository.GpsInfo) -> [String: Any] {
    return [
        "latitude": gpsInfo.latitude,
        "longitude": gpsInfo.longitude,
        "altitude": gpsInfo.altitude,
        "dateTimeZone": gpsInfo.dateTimeZone,
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

func convertResult(options: ThetaRepository.Options) -> [String: Any] {
    var result = [String: Any]()
    let nameList = ThetaRepository.OptionNameEnum.values()
    for i in 0..<nameList.size {
        let name = nameList.get(index: i)!
        if let value = try? options.getValue(name: name) {
            if value is KotlinEnum<AnyObject> {
                let enumValue = value as! KotlinEnum<AnyObject>
                result[name.name] = enumValue.name
            } else if value is KotlinBoolean {
                result[name.name] = value as! Bool ? true: false
            } else if value is NSNumber || value is String {
                result[name.name] = value
            } else if value is ThetaRepository.BurstOption, let burstOption = value as? ThetaRepository.BurstOption {
                result[name.name] = convertResult(burstOption: burstOption)
            }  else if value is ThetaRepository.GpsInfo {
                let gpsInfo = value as! ThetaRepository.GpsInfo
                result[name.name] = convertResult(gpsInfo: gpsInfo)
            } else if value is ThetaRepository.Proxy, let proxy = value as? ThetaRepository.Proxy {
                result[name.name] = convertResult(proxy: proxy)
            } else if value is ThetaRepository.TimeShiftSetting, let timeshift = value as? ThetaRepository.TimeShiftSetting {
                result[name.name] = convertResult(timeshift: timeshift)
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
    switch (name) {
    case ThetaRepository.OptionNameEnum.aperture.name:
        options.aperture = getEnumValue(values: ThetaRepository.ApertureEnum.values(), name: value as! String)!
        break
    case ThetaRepository.OptionNameEnum.burstmode.name:
        options.burstMode = getEnumValue(values: ThetaRepository.BurstModeEnum.values(), name: value as! String)!
        break
    case ThetaRepository.OptionNameEnum.burstoption.name:
        if let params = value as? [String : Any] {
            options.burstOption = toBurstOption(params: params)
        }
        break
    case ThetaRepository.OptionNameEnum.cameracontrolsource.name:
        options.cameraControlSource = getEnumValue(values: ThetaRepository.CameraControlSourceEnum.values(), name: value as! String)!
        break
    case ThetaRepository.OptionNameEnum.cameramode.name:
        options.cameraMode = getEnumValue(values: ThetaRepository.CameraModeEnum.values(), name: value as! String)!
        break
    case ThetaRepository.OptionNameEnum.capturemode.name:
        options.captureMode = getEnumValue(values: ThetaRepository.CaptureModeEnum.values(), name: value as! String)!
        break
    case ThetaRepository.OptionNameEnum.colortemperature.name:
        options.colorTemperature = KotlinInt(integerLiteral: value as! Int)
        break
    case ThetaRepository.OptionNameEnum.datetimezone.name:
        options.dateTimeZone = value as? String
        break
    case ThetaRepository.OptionNameEnum.exposurecompensation.name:
        options.exposureCompensation = getEnumValue(values: ThetaRepository.ExposureCompensationEnum.values(), name: value as! String)!
        break
    case ThetaRepository.OptionNameEnum.exposuredelay.name:
        options.exposureDelay = getEnumValue(values: ThetaRepository.ExposureDelayEnum.values(), name: value as! String)!
        break
    case ThetaRepository.OptionNameEnum.exposureprogram.name:
        options.exposureProgram = getEnumValue(values: ThetaRepository.ExposureProgramEnum.values(), name: value as! String)!
        break
    case ThetaRepository.OptionNameEnum.fileformat.name:
        options.fileFormat = getEnumValue(values: ThetaRepository.FileFormatEnum.values(), name: value as! String)!
        break
    case ThetaRepository.OptionNameEnum.filter.name:
        options.filter = getEnumValue(values: ThetaRepository.FilterEnum.values(), name: value as! String)!
        break
    case ThetaRepository.OptionNameEnum.gpsinfo.name:
        options.gpsInfo = toGpsInfo(params: value as! [String : Any])
        break
    case ThetaRepository.OptionNameEnum.isgpson.name:
        options.isGpsOn = (value as! Bool) ? true: false
        break
    case ThetaRepository.OptionNameEnum.iso.name:
        options.iso = getEnumValue(values: ThetaRepository.IsoEnum.values(), name: value as! String)!
        break
    case ThetaRepository.OptionNameEnum.isoautohighlimit.name:
        options.isoAutoHighLimit = getEnumValue(values: ThetaRepository.IsoAutoHighLimitEnum.values(), name: value as! String)!
        break
    case ThetaRepository.OptionNameEnum.language.name:
        options.language = getEnumValue(values: ThetaRepository.LanguageEnum.values(), name: value as! String)!
        break
    case ThetaRepository.OptionNameEnum.maxrecordabletime.name:
        options.maxRecordableTime = getEnumValue(values: ThetaRepository.MaxRecordableTimeEnum.values(), name: value as! String)!
        break
    case ThetaRepository.OptionNameEnum.networktype.name:
        options.networkType = getEnumValue(values: ThetaRepository.NetworkTypeEnum.values(), name: value as! String)!
        break
    case ThetaRepository.OptionNameEnum.offdelay.name:
        options.offDelay = getEnumValue(values: ThetaRepository.OffDelayEnum.values(), name: value as! String)!
        break
    case ThetaRepository.OptionNameEnum.password.name:
        options.password = value as? String
        break;
    case ThetaRepository.OptionNameEnum.powersaving.name:
        options.powerSaving = getEnumValue(values: ThetaRepository.PowerSavingEnum.values(), name: value as! String)!
        break;
    case ThetaRepository.OptionNameEnum.previewformat.name:
        options.previewFormat = getEnumValue(values: ThetaRepository.PreviewFormatEnum.values(), name: value as! String)!
        break;
    case ThetaRepository.OptionNameEnum.proxy.name:
        if let params = value as? [String : Any] {
            options.proxy = toProxy(params: params)
        }
        break
    case ThetaRepository.OptionNameEnum.remainingpictures.name:
        options.remainingPictures = KotlinInt(integerLiteral: value as! Int)
        break
    case ThetaRepository.OptionNameEnum.remainingvideoseconds.name:
        options.remainingVideoSeconds = KotlinInt(integerLiteral: value as! Int)
        break
    case ThetaRepository.OptionNameEnum.remainingspace.name:
        options.remainingSpace = KotlinLong(integerLiteral: value as! Int)
        break
    case ThetaRepository.OptionNameEnum.shootingmethod.name:
        options.shootingMethod = getEnumValue(values: ThetaRepository.ShootingMethodEnum.values(), name: value as! String)!
        break;
    case ThetaRepository.OptionNameEnum.shutterspeed.name:
        options.shutterSpeed = getEnumValue(values: ThetaRepository.ShutterSpeedEnum.values(), name: value as! String)!
        break;
    case ThetaRepository.OptionNameEnum.shuttervolume.name:
        options.shutterVolume = KotlinInt(integerLiteral: value as! Int)
        break
    case ThetaRepository.OptionNameEnum.sleepdelay.name:
        options.sleepDelay = getEnumValue(values: ThetaRepository.SleepDelayEnum.values(), name: value as! String)!
        break
    case ThetaRepository.OptionNameEnum.timeshift.name:
        if let params = value as? [String : Any] {
            options.timeShift = toTimeShift(params: params)
        }
        break
    case ThetaRepository.OptionNameEnum.totalspace.name:
        options.totalSpace = KotlinLong(integerLiteral: value as! Int)
        break
    case ThetaRepository.OptionNameEnum.username.name:
        options.username = value as? String
        break;
    case ThetaRepository.OptionNameEnum.whitebalance.name:
        options.whiteBalance = getEnumValue(values: ThetaRepository.WhiteBalanceEnum.values(), name: value as! String)!
        break
    case ThetaRepository.OptionNameEnum.whitebalanceautostrength.name:
        options.whiteBalanceAutoStrength = getEnumValue(values: ThetaRepository.WhiteBalanceAutoStrengthEnum.values(), name: value as! String)!
        break
    case ThetaRepository.OptionNameEnum.wlanfrequency.name:
        options.wlanFrequency = getEnumValue(values: ThetaRepository.WlanFrequencyEnum.values(), name: value as! String)!
        break
    default: break
    }
}

func toDigetAuth(params: [String : String?]?) -> DigestAuth? {
    guard let params = params,
          let username = params["username"] as? String
    else { return nil }
    
    let password = params["password"] as? String
    return DigestAuth(username: username, password: password)
}

func toConfig(params: [String : Any]) -> ThetaRepository.Config {
    let config = ThetaRepository.Config()
    params.forEach { key, value in
        switch (key) {
        case ThetaRepository.OptionNameEnum.datetimezone.name:
            config.dateTime = value as? String
        case ThetaRepository.OptionNameEnum.language.name:
            config.language = getEnumValue(values: ThetaRepository.LanguageEnum.values(), name: value as! String)!
        case ThetaRepository.OptionNameEnum.offdelay.name:
            config.offDelay = getEnumValue(values: ThetaRepository.OffDelayEnum.values(), name: value as! String)!
        case ThetaRepository.OptionNameEnum.sleepdelay.name:
            config.sleepDelay = getEnumValue(values: ThetaRepository.SleepDelayEnum.values(), name: value as! String)!
        case ThetaRepository.OptionNameEnum.shuttervolume.name:
            config.shutterVolume = KotlinInt(integerLiteral: value as! Int)
        case KEY_CLIENT_MODE:
            config.clientMode = toDigetAuth(params: value as? [String : String?])
        default:
            break
        }
    }
    return config
}

func toTimeout(params: [String : Any]) -> ThetaRepository.Timeout {
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
    return  [
        "exif": convertResult(exif: metadata.first!),
        "xmp": convertResult(xmp: metadata.second!),
    ]
}

func convertResult(accessPointList: [ThetaRepository.AccessPoint]) -> [[String: Any]] {
    var resultList = [[String: Any]]()
    accessPointList.forEach({ accessPoint in
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
    })
    return resultList
}

func toPluginInfosResult(pluginInfoList: [ThetaRepository.PluginInfo]) -> [[String: Any]] {
    var resultList = [[String: Any]]()
    pluginInfoList.forEach({ pluginInfo in
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
    })
    return resultList
}
