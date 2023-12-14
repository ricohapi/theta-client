import THETAClient
import UIKit

let KEY_CLIENT_MODE = "clientMode"
let KEY_NOTIFY_NAME = "name"
let KEY_NOTIFY_PARAMS = "params"
let KEY_NOTIFY_PARAM_COMPLETION = "completion"
let KEY_NOTIFY_PARAM_IMAGE = "image"
let KEY_NOTIFY_PARAM_MESSAGE = "message"
let KEY_DATETIME = "dateTime"
let KEY_LANGUAGE = "language"
let KEY_OFF_DELAY = "offDelay"
let KEY_SLEEP_DELAY = "sleepDelay"
let KEY_SHUTTER_VOLUME = "shutterVolume"
let KEY_CONNECT_TIMEOUT = "connectTimeout"
let KEY_REQUEST_TIMEOUT = "requestTimeout"
let KEY_SOCKET_TIMEOUT = "socketTimeout"
let KEY_THETA_MODEL = "thetaModel"
let KEY_TIMESHIFT = "timeShift"
let KEY_APERTURE = "aperture"
let KEY_CAPTURE_INTERVAL = "captureInterval"
let KEY_COMPOSITE_SHOOTING_OUTPUT_INTERVAL = "compositeShootingOutputInterval"
let KEY_COLOR_TEMPERATURE = "colorTemperature"
let KEY_EXPOSURE_COMPENSATION = "exposureCompensation"
let KEY_EXPOSURE_DELAY = "exposureDelay"
let KEY_EXPOSURE_PROGRAM = "exposureProgram"
let KEY_GPS_INFO = "gpsInfo"
let KEY_GPS_TAG_RECORDING = "_gpsTagRecording"
let KEY_ISO = "iso"
let KEY_ISO_AUTO_HIGH_LIMIT = "isoAutoHighLimit"
let KEY_WHITE_BALANCE = "whiteBalance"
let KEY_FILE_FORMAT = "fileFormat"
let KEY_FILTER = "filter"
let KEY_FILE_LIST = "fileList"
let KEY_PRESET = "preset"
let KEY_GPS_LATITUDE = "latitude"
let KEY_GPS_LONGITUDE = "longitude"
let KEY_GPS_ALTITUDE = "altitude"
let KEY_GPS_DATE_TIME_ZONE = "dateTimeZone"
let KEY_MAX_RECORDABLE_TIME = "maxRecordableTime"
let KEY_TOTAL_ENTRIES = "totalEntries"
let KEY_TIMESHIFT_IS_FRONT_FIRST = "isFrontFirst"
let KEY_TIMESHIFT_FIRST_INTERVAL = "firstInterval"
let KEY_TIMESHIFT_SECOND_INTERVAL = "secondInterval"
let KEY_PROXY_USE = "use"
let KEY_PROXY_URL = "url"
let KEY_PROXY_PORT = "port"
let KEY_PROXY_USER_ID = "userid"
let KEY_PROXY_PASSWORD = "password"
let KEY_BURST_MODE = "burstMode"
let KEY_BURST_CAPTURE_NUM = "burstCaptureNum"
let KEY_BURST_BRACKET_STEP = "burstBracketStep"
let KEY_BURST_COMPENSATION = "burstCompensation"
let KEY_BURST_MAX_EXPOSURE_TIME = "burstMaxExposureTime"
let KEY_BURST_ENABLE_ISO_CONTROL = "burstEnableIsoControl"
let KEY_BURST_ORDER = "burstOrder"
let KEY_TOP_BOTTOM_CORRECTION_ROTATION_PITCH = "pitch"
let KEY_TOP_BOTTOM_CORRECTION_ROTATION_ROLL = "roll"
let KEY_TOP_BOTTOM_CORRECTION_ROTATION_YAW = "yaw"
let KEY_TIMESHIFT_CAPTURE_INTERVAL = "_capture_interval"
let KEY_AUTO_BRACKET = "autoBracket"

public class ConvertUtil: NSObject {}

// MARK: - Options convert

let optionItemNameToEnum = [
    // TODO: Add items when adding options
    "aiAutoThumbnail": ThetaRepository.OptionNameEnum.aiautothumbnail,
    "aperture": ThetaRepository.OptionNameEnum.aperture,
    "autoBracket": ThetaRepository.OptionNameEnum.autobracket,
    "bitrate": ThetaRepository.OptionNameEnum.bitrate,
    "bluetoothPower": ThetaRepository.OptionNameEnum.bluetoothpower,
    "bluetoothRole": ThetaRepository.OptionNameEnum.bluetoothrole,
    "burstMode": ThetaRepository.OptionNameEnum.burstmode,
    "burstOption": ThetaRepository.OptionNameEnum.burstoption,
    "cameraControlSource": ThetaRepository.OptionNameEnum.cameracontrolsource,
    "cameraMode": ThetaRepository.OptionNameEnum.cameramode,
    "captureInterval": ThetaRepository.OptionNameEnum.captureinterval,
    "captureMode": ThetaRepository.OptionNameEnum.capturemode,
    "captureNumber": ThetaRepository.OptionNameEnum.capturenumber,
    "colorTemperature": ThetaRepository.OptionNameEnum.colortemperature,
    "compositeShootingOutputInterval": ThetaRepository.OptionNameEnum
        .compositeshootingoutputinterval,
    "compositeShootingTime": ThetaRepository.OptionNameEnum.compositeshootingtime,
    "continuousNumber": ThetaRepository.OptionNameEnum.continuousnumber,
    "dateTimeZone": ThetaRepository.OptionNameEnum.datetimezone,
    "exposureCompensation": ThetaRepository.OptionNameEnum.exposurecompensation,
    "exposureDelay": ThetaRepository.OptionNameEnum.exposuredelay,
    "exposureProgram": ThetaRepository.OptionNameEnum.exposureprogram,
    "faceDetect": ThetaRepository.OptionNameEnum.facedetect,
    "fileFormat": ThetaRepository.OptionNameEnum.fileformat,
    "filter": ThetaRepository.OptionNameEnum.filter,
    "function": ThetaRepository.OptionNameEnum.function,
    "gain": ThetaRepository.OptionNameEnum.gain,
    "gpsInfo": ThetaRepository.OptionNameEnum.gpsinfo,
    "imageStitching": ThetaRepository.OptionNameEnum.imagestitching,
    "isGpsOn": ThetaRepository.OptionNameEnum.isgpson,
    "iso": ThetaRepository.OptionNameEnum.iso,
    "isoAutoHighLimit": ThetaRepository.OptionNameEnum.isoautohighlimit,
    "language": ThetaRepository.OptionNameEnum.language,
    "latestEnabledExposureDelayTime": ThetaRepository.OptionNameEnum.latestenabledexposuredelaytime,
    "maxRecordableTime": ThetaRepository.OptionNameEnum.maxrecordabletime,
    "networkType": ThetaRepository.OptionNameEnum.networktype,
    "offDelay": ThetaRepository.OptionNameEnum.offdelay,
    "password": ThetaRepository.OptionNameEnum.password,
    "powerSaving": ThetaRepository.OptionNameEnum.powersaving,
    "preset": ThetaRepository.OptionNameEnum.preset,
    "previewFormat": ThetaRepository.OptionNameEnum.previewformat,
    "proxy": ThetaRepository.OptionNameEnum.proxy,
    "remainingPictures": ThetaRepository.OptionNameEnum.remainingpictures,
    "remainingVideoSeconds": ThetaRepository.OptionNameEnum.remainingvideoseconds,
    "remainingSpace": ThetaRepository.OptionNameEnum.remainingspace,
    "shootingMethod": ThetaRepository.OptionNameEnum.shootingmethod,
    "shutterSpeed": ThetaRepository.OptionNameEnum.shutterspeed,
    "shutterVolume": ThetaRepository.OptionNameEnum.shuttervolume,
    "sleepDelay": ThetaRepository.OptionNameEnum.sleepdelay,
    "timeShift": ThetaRepository.OptionNameEnum.timeshift,
    "topBottomCorrection": ThetaRepository.OptionNameEnum.topbottomcorrection,
    "topBottomCorrectionRotation": ThetaRepository.OptionNameEnum.topbottomcorrectionrotation,
    "totalSpace": ThetaRepository.OptionNameEnum.totalspace,
    "username": ThetaRepository.OptionNameEnum.username,
    "videoStitching": ThetaRepository.OptionNameEnum.videostitching,
    "visibilityReduction": ThetaRepository.OptionNameEnum.visibilityreduction,
    "whiteBalance": ThetaRepository.OptionNameEnum.whitebalance,
    "whiteBalanceAutoStrength": ThetaRepository.OptionNameEnum.whitebalanceautostrength,
    "wlanFrequency": ThetaRepository.OptionNameEnum.wlanfrequency,
]

let optionNameEnumToItemName = {
    var map = [ThetaRepository.OptionNameEnum: String]()
    optionItemNameToEnum.forEach { tuple in
        map[tuple.value] = tuple.key
    }
    return map
}()

func convertGetOptionsParam(params: [String]) -> [ThetaRepository.OptionNameEnum] {
    var array: [ThetaRepository.OptionNameEnum] = []
    let values = ThetaRepository.OptionNameEnum.values()
    for name in params {
        if let nameEnum = getEnumValue(values: values, name: name) {
            array.append(nameEnum)
        }
    }
    return array
}

func convertSetOptionsParam(params: [String: Any]) -> ThetaRepository.Options {
    let options = ThetaRepository.Options()
    params.forEach { key, value in
        if let nameEnum = optionItemNameToEnum[key] {
            setOptionsValue(options: options, name: nameEnum.name, value: value)
        }
    }
    return options
}

func setOptionsValue(options: ThetaRepository.Options, name: String, value: Any) {
    // TODO: Add items when adding options
    switch name {
    case ThetaRepository.OptionNameEnum.aiautothumbnail.name:
        options.aiAutoThumbnail = getEnumValue(
            values: ThetaRepository.AiAutoThumbnailEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.aperture.name:
        options.aperture = getEnumValue(
            values: ThetaRepository.ApertureEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.autobracket.name:
        if let params = value as? [[String: Any]] {
            options.autoBracket = toAutoBracket(params: params)
        }
    case ThetaRepository.OptionNameEnum.bitrate.name:
        options.bitrate = toBitrate(value: value)
    case ThetaRepository.OptionNameEnum.bluetoothpower.name:
        options.bluetoothPower = getEnumValue(
            values: ThetaRepository.BluetoothPowerEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.bluetoothrole.name:
        options.bluetoothRole = getEnumValue(
            values: ThetaRepository.BluetoothRoleEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.burstmode.name:
        options.burstMode = getEnumValue(
            values: ThetaRepository.BurstModeEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.burstoption.name:
        if let params = value as? [String: Any] {
            options.burstOption = toBurstOption(params: params)
        }
    case ThetaRepository.OptionNameEnum.cameracontrolsource.name:
        options.cameraControlSource = getEnumValue(
            values: ThetaRepository.CameraControlSourceEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.cameramode.name:
        options.cameraMode = getEnumValue(
            values: ThetaRepository.CameraModeEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.captureinterval.name:
        options.captureInterval = KotlinInt(integerLiteral: value as! Int)
    case ThetaRepository.OptionNameEnum.capturemode.name:
        options.captureMode = getEnumValue(
            values: ThetaRepository.CaptureModeEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.capturenumber.name:
        options.captureNumber = KotlinInt(integerLiteral: value as! Int)
    case ThetaRepository.OptionNameEnum.colortemperature.name:
        options.colorTemperature = KotlinInt(integerLiteral: value as! Int)
    case ThetaRepository.OptionNameEnum.compositeshootingoutputinterval.name:
        options.compositeShootingOutputInterval = KotlinInt(integerLiteral: value as! Int)
    case ThetaRepository.OptionNameEnum.compositeshootingtime.name:
        options.compositeShootingTime = KotlinInt(integerLiteral: value as! Int)
    case ThetaRepository.OptionNameEnum.continuousnumber.name:
        options.continuousNumber = getEnumValue(
            values: ThetaRepository.ContinuousNumberEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.datetimezone.name:
        options.dateTimeZone = value as? String
    case ThetaRepository.OptionNameEnum.exposurecompensation.name:
        options.exposureCompensation = getEnumValue(
            values: ThetaRepository.ExposureCompensationEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.exposuredelay.name:
        options.exposureDelay = getEnumValue(
            values: ThetaRepository.ExposureDelayEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.exposureprogram.name:
        options.exposureProgram = getEnumValue(
            values: ThetaRepository.ExposureProgramEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.facedetect.name:
        options.faceDetect = getEnumValue(
            values: ThetaRepository.FaceDetectEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.fileformat.name:
        options.fileFormat = getEnumValue(
            values: ThetaRepository.FileFormatEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.filter.name:
        options.filter = getEnumValue(
            values: ThetaRepository.FilterEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.function.name:
        options.function = getEnumValue(
            values: ThetaRepository.ShootingFunctionEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.gain.name:
        options.gain = getEnumValue(
            values: ThetaRepository.GainEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.gpsinfo.name:
        options.gpsInfo = toGpsInfo(params: value as! [String: Any])
    case ThetaRepository.OptionNameEnum.imagestitching.name:
        options.imageStitching = getEnumValue(
            values: ThetaRepository.ImageStitchingEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.isgpson.name:
        options.isGpsOn = (value as! Bool) ? true : false
    case ThetaRepository.OptionNameEnum.iso.name:
        options.iso = getEnumValue(
            values: ThetaRepository.IsoEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.isoautohighlimit.name:
        options.isoAutoHighLimit = getEnumValue(
            values: ThetaRepository.IsoAutoHighLimitEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.language.name:
        options.language = getEnumValue(
            values: ThetaRepository.LanguageEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.latestenabledexposuredelaytime.name:
        options.latestEnabledExposureDelayTime = getEnumValue(
            values: ThetaRepository.ExposureDelayEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.maxrecordabletime.name:
        options.maxRecordableTime = getEnumValue(
            values: ThetaRepository.MaxRecordableTimeEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.networktype.name:
        options.networkType = getEnumValue(
            values: ThetaRepository.NetworkTypeEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.offdelay.name:
        options.offDelay = toOffDelay(value: value)
    case ThetaRepository.OptionNameEnum.password.name:
        options.password = value as? String
    case ThetaRepository.OptionNameEnum.powersaving.name:
        options.powerSaving = getEnumValue(
            values: ThetaRepository.PowerSavingEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.preset.name:
        options.preset = getEnumValue(
            values: ThetaRepository.PresetEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.previewformat.name:
        options.previewFormat = getEnumValue(
            values: ThetaRepository.PreviewFormatEnum.values(), name: value as! String
        )!
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
        options.shootingMethod = getEnumValue(
            values: ThetaRepository.ShootingMethodEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.shutterspeed.name:
        options.shutterSpeed = getEnumValue(
            values: ThetaRepository.ShutterSpeedEnum.values(), name: value as! String
        )!
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
        options.videoStitching = getEnumValue(
            values: ThetaRepository.VideoStitchingEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.visibilityreduction.name:
        options.visibilityReduction = getEnumValue(
            values: ThetaRepository.VisibilityReductionEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.whitebalance.name:
        options.whiteBalance = getEnumValue(
            values: ThetaRepository.WhiteBalanceEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.whitebalanceautostrength.name:
        options.whiteBalanceAutoStrength = getEnumValue(
            values: ThetaRepository.WhiteBalanceAutoStrengthEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.wlanfrequency.name:
        options.wlanFrequency = getEnumValue(
            values: ThetaRepository.WlanFrequencyEnum.values(), name: value as! String
        )!
    default: break
    }
}

func convertResult(options: ThetaRepository.Options) -> [String: Any] {
    var result = [String: Any]()
    let nameList = ThetaRepository.OptionNameEnum.values()
    for i in 0 ..< nameList.size {
        if let name = nameList.get(index: i),
           let key = optionNameEnumToItemName[name]
        {
            if let value = try? options.getValue(name: name) {
                if let enumValue = value as? KotlinEnum<AnyObject> {
                    result[key] = enumValue.name
                } else if value is KotlinBoolean {
                    result[key] = value as! Bool ? true : false
                } else if value is NSNumber || value is String {
                    result[key] = value
                } else if value is ThetaRepository.BracketSettingList,
                          let autoBracket = value as? ThetaRepository.BracketSettingList
                {
                    result[key] = convertResult(autoBracket: autoBracket)
                } else if let bitrate = value as? ThetaRepository.BitrateNumber {
                    result[key] = bitrate.value
                } else if value is ThetaRepository.BurstOption,
                          let burstOption = value as? ThetaRepository.BurstOption
                {
                    result[key] = convertResult(burstOption: burstOption)
                } else if value is ThetaRepository.GpsInfo {
                    let gpsInfo = value as! ThetaRepository.GpsInfo
                    result[key] = convertResult(gpsInfo: gpsInfo)
                } else if value is ThetaRepository.Proxy,
                          let proxy = value as? ThetaRepository.Proxy
                {
                    result[key] = convertResult(proxy: proxy)
                } else if value is ThetaRepository.TimeShiftSetting,
                          let timeshift = value as? ThetaRepository.TimeShiftSetting
                {
                    result[key] = convertResult(timeshift: timeshift)
                } else if value is ThetaRepository.TopBottomCorrectionRotation, let rotation = value as? ThetaRepository.TopBottomCorrectionRotation {
                    result[key] = convertResult(rotation: rotation)
                } else if let offDelay = value as? ThetaRepository.OffDelaySec {
                    result[key] =
                        offDelay.sec == 0 ? ThetaRepository.OffDelayEnum.disable.name : offDelay.sec
                } else if let sleepDelay = value as? ThetaRepository.SleepDelaySec {
                    result[key] =
                        sleepDelay.sec == 0
                            ? ThetaRepository.SleepDelayEnum.disable.name : sleepDelay.sec
                }
                // TODO: Add class item when adding options
            }
        }
    }
    return result
}

// MARK: - Notify event

func toNotify(name: String, params: [String: Any]?) -> [String: Any] {
    var result: [String: Any] = [
        KEY_NOTIFY_NAME: name,
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

func toMessageNotifyParam(value: String) -> [String: Any] {
    return [
        KEY_NOTIFY_PARAM_MESSAGE: value,
    ]
}

// MARK: - Capture builder

func setCaptureBuilderParams<T>(params: [String: Any], builder: CaptureBuilder<T>) {
    if let value = params[KEY_APERTURE] as? String {
        if let enumValue = getEnumValue(values: ThetaRepository.ApertureEnum.values(), name: value)
        {
            builder.setAperture(aperture: enumValue)
        }
    }
    if let value = params[KEY_COLOR_TEMPERATURE] as? Int32 {
        builder.setColorTemperature(kelvin: value)
    }
    if let value = params[KEY_EXPOSURE_COMPENSATION] as? String {
        if let enumValue = getEnumValue(
            values: ThetaRepository.ExposureCompensationEnum.values(), name: value
        ) {
            builder.setExposureCompensation(value: enumValue)
        }
    }
    if let value = params[KEY_EXPOSURE_DELAY] as? String {
        if let enumValue = getEnumValue(
            values: ThetaRepository.ExposureDelayEnum.values(), name: value
        ) {
            builder.setExposureDelay(delay: enumValue)
        }
    }
    if let value = params[KEY_EXPOSURE_PROGRAM] as? String {
        if let enumValue = getEnumValue(
            values: ThetaRepository.ExposureProgramEnum.values(), name: value
        ) {
            builder.setExposureProgram(program: enumValue)
        }
    }
    if let value = params[KEY_GPS_INFO] as? [String: Any] {
        if let gpsInfo = toGpsInfo(params: value) {
            builder.setGpsInfo(gpsInfo: gpsInfo)
        }
    }
    if let value = params[KEY_GPS_TAG_RECORDING] as? String {
        if let enumValue = getEnumValue(
            values: ThetaRepository.GpsTagRecordingEnum.values(), name: value
        ) {
            builder.setGpsTagRecording(value: enumValue)
        }
    }
    if let value = params[KEY_ISO] as? String {
        if let enumValue = getEnumValue(values: ThetaRepository.IsoEnum.values(), name: value) {
            builder.setIso(iso: enumValue)
        }
    }
    if let value = params[KEY_ISO_AUTO_HIGH_LIMIT] as? String {
        if let enumValue = getEnumValue(
            values: ThetaRepository.IsoAutoHighLimitEnum.values(), name: value
        ) {
            builder.setIsoAutoHighLimit(iso: enumValue)
        }
    }
    if let value = params[KEY_WHITE_BALANCE] as? String {
        if let enumValue = getEnumValue(
            values: ThetaRepository.WhiteBalanceEnum.values(), name: value
        ) {
            builder.setWhiteBalance(whiteBalance: enumValue)
        }
    }
}

func setPhotoCaptureBuilderParams(params: [String: Any], builder: PhotoCapture.Builder) {
    if let value = params[KEY_FILTER] as? String {
        if let enumValue = getEnumValue(values: ThetaRepository.FilterEnum.values(), name: value) {
            builder.setFilter(filter: enumValue)
        }
    }
    if let value = params[KEY_FILE_FORMAT] as? String {
        if let enumValue = getEnumValue(
            values: ThetaRepository.PhotoFileFormatEnum.values(), name: value
        ) {
            builder.setFileFormat(fileFormat: enumValue)
        }
    }
    if let value = params[KEY_PRESET] as? String {
        if let enumValue = getEnumValue(values: ThetaRepository.PresetEnum.values(), name: value) {
            builder.setPreset(preset: enumValue)
        }
    }
}

func setTimeShiftCaptureBuilderParams(params: [String: Any], builder: TimeShiftCapture.Builder) {
    if let interval = params[KEY_TIMESHIFT_CAPTURE_INTERVAL] as? Int,
       interval >= 0
    {
        builder.setCheckStatusCommandInterval(timeMillis: Int64(interval))
    }
    if let timeShiftParams = params[KEY_TIMESHIFT] as? [String: Any] {
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
    if let value = params[KEY_MAX_RECORDABLE_TIME] as? String {
        if let enumValue = getEnumValue(
            values: ThetaRepository.MaxRecordableTimeEnum.values(), name: value
        ) {
            builder.setMaxRecordableTime(time: enumValue)
        }
    }
    if let value = params[KEY_FILE_FORMAT] as? String {
        if let enumValue = getEnumValue(
            values: ThetaRepository.VideoFileFormatEnum.values(), name: value
        ) {
            builder.setFileFormat(fileFormat: enumValue)
        }
    }
}

func setLimitlessIntervalCaptureBuilderParams(params: [String: Any], builder: LimitlessIntervalCapture.Builder) {
    if let value = params[KEY_CAPTURE_INTERVAL] as? Int32 {
        builder.setCaptureInterval(interval: value)
    }
}

func setShotCountSpecifiedIntervalCaptureBuilderParams(params: [String: Any], builder: ShotCountSpecifiedIntervalCapture.Builder) {
    if let interval = params[KEY_TIMESHIFT_CAPTURE_INTERVAL] as? Int,
       interval >= 0
    {
        builder.setCheckStatusCommandInterval(timeMillis: Int64(interval))
    }
    if let value = params[KEY_CAPTURE_INTERVAL] as? Int32 {
        builder.setCaptureInterval(interval: value)
    }
}

func setCompositeIntervalCaptureBuilderParams(params: [String: Any], builder: CompositeIntervalCapture.Builder) {
    if let interval = params[KEY_TIMESHIFT_CAPTURE_INTERVAL] as? Int,
       interval >= 0
    {
        builder.setCheckStatusCommandInterval(timeMillis: Int64(interval))
    }
    if let value = params[KEY_COMPOSITE_SHOOTING_OUTPUT_INTERVAL] as? Int32 {
        builder.setCompositeShootingOutputInterval(sec: value)
    }
}

func setBurstCaptureBuilderParams(params: [String: Any], builder: BurstCapture.Builder) {
    if let interval = params[KEY_TIMESHIFT_CAPTURE_INTERVAL] as? Int,
       interval >= 0
    {
        builder.setCheckStatusCommandInterval(timeMillis: Int64(interval))
    }
    if let value = params[KEY_BURST_MODE] as? String {
        if let enumValue = getEnumValue(values: ThetaRepository.BurstModeEnum.values(),
                                        name: value)
        {
            builder.setBurstMode(mode: enumValue)
        }
    }
}

func setMultiBracketCaptureBuilderParams(params: [String: Any], builder: MultiBracketCapture.Builder) {
    if let autoBracket = params[KEY_AUTO_BRACKET] as? [[String: Any]] {
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

// MARK: - Utility

func getEnumValue<T, E: KotlinEnum<T>>(values: KotlinArray<E>, name: String) -> E? {
    for i in 0 ..< values.size {
        let item = values.get(index: i)!
        if item.name == name {
            return item
        }
    }
    return nil
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

// MARK: - Convert to react-native object

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
    return [
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

func convertResult(burstOption: ThetaRepository.BurstOption) -> [String: Any] {
    var result: [String: Any] = [:]
    if let burstCaptureNum = burstOption.burstCaptureNum {
        result[KEY_BURST_CAPTURE_NUM] = burstCaptureNum.name
    }
    if let burstBracketStep = burstOption.burstBracketStep {
        result[KEY_BURST_BRACKET_STEP] = burstBracketStep.name
    }
    if let burstCompensation = burstOption.burstCompensation {
        result[KEY_BURST_COMPENSATION] = burstCompensation.name
    }
    if let burstMaxExposureTime = burstOption.burstMaxExposureTime {
        result[KEY_BURST_MAX_EXPOSURE_TIME] = burstMaxExposureTime.name
    }
    if let burstEnableIsoControl = burstOption.burstEnableIsoControl {
        result[KEY_BURST_ENABLE_ISO_CONTROL] = burstEnableIsoControl.name
    }
    if let burstOrder = burstOption.burstOrder {
        result[KEY_BURST_ORDER] = burstOrder.name
    }
    return result
}

func convertResult(autoBracket: ThetaRepository.BracketSettingList) -> [[String: Any]] {
    var resultList = [[String: Any]]()

    autoBracket.list.forEach { bracketSetting in
        var result: [String: Any] = [:]
        if let setting = bracketSetting as? ThetaRepository.BracketSetting {
            if let aperture = setting.aperture?.name {
                result["aperture"] = aperture
            }
            if let colorTemperature = setting.colorTemperature?.intValue {
                result["colorTemperature"] = colorTemperature
            }
            if let exposureCompensation = setting.exposureCompensation?.name {
                result["exposureCompensation"] = exposureCompensation
            }
            if let exposureProgram = setting.exposureProgram?.name {
                result["exposureProgram"] = exposureProgram
            }
            if let iso = setting.iso?.name {
                result["iso"] = iso
            }
            if let shutterSpeed = setting.shutterSpeed?.name {
                result["shutterSpeed"] = shutterSpeed
            }
            if let whiteBalance = setting.whiteBalance?.name {
                result["whiteBalance"] = whiteBalance
            }
            resultList.append(result)
        }
    }

    return resultList
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
    var result: [String: Any] = [:]
    result[KEY_PROXY_USE] = proxy.use
    if let url = proxy.url {
        result[KEY_PROXY_URL] = url
    }
    if let port = proxy.port {
        result[KEY_PROXY_PORT] = port
    }
    if let userId = proxy.userid {
        result[KEY_PROXY_USER_ID] = userId
    }
    if let password = proxy.password {
        result[KEY_PROXY_PASSWORD] = password
    }
    return result
}

func convertResult(timeshift: ThetaRepository.TimeShiftSetting) -> [String: Any] {
    var result: [String: Any] = [:]
    if let isFrontFirst = convertKotlinBooleanToBool(value: timeshift.isFrontFirst) {
        result[KEY_TIMESHIFT_IS_FRONT_FIRST] = isFrontFirst
    }
    if let firstInterval = timeshift.firstInterval?.name {
        result[KEY_TIMESHIFT_FIRST_INTERVAL] = firstInterval
    }
    if let secondInterval = timeshift.secondInterval?.name {
        result[KEY_TIMESHIFT_SECOND_INTERVAL] = secondInterval
    }
    return result
}

func convertResult(rotation: ThetaRepository.TopBottomCorrectionRotation) -> [String: Any] {
    return [
        KEY_TOP_BOTTOM_CORRECTION_ROTATION_PITCH: rotation.pitch,
        KEY_TOP_BOTTOM_CORRECTION_ROTATION_ROLL: rotation.roll,
        KEY_TOP_BOTTOM_CORRECTION_ROTATION_YAW: rotation.yaw,
    ]
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

func convertResult(metadata: KotlinPair<ThetaRepository.Exif, ThetaRepository.Xmp>) -> [String: Any]
{
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

// MARK: - Convert to theta-client object

func toBitrate(value: Any) -> ThetaRepositoryBitrate? {
    if value is NSNumber, let intVal = value as? Int32 {
        return ThetaRepository.BitrateNumber(value: intVal)
    } else if let name = value as? String,
              let enumValue = getEnumValue(values: ThetaRepository.BitrateEnum.values(), name: name)
    {
        return enumValue
    } else if let str = value as? String {
        return ThetaRepository.BitrateEnum.companion.get(str: str)
    } else {
        return nil
    }
}

func toOffDelay(value: Any) -> ThetaRepositoryOffDelay? {
    if value is NSNumber, let intVal = value as? Int32 {
        return ThetaRepository.OffDelaySec(sec: intVal)
    } else if let name = value as? String,
              let enumValue = getEnumValue(values: ThetaRepository.OffDelayEnum.values(), name: name)
    {
        return enumValue
    } else {
        return nil
    }
}

func toSleepDelay(value: Any) -> ThetaRepositorySleepDelay? {
    if value is NSNumber, let intVal = value as? Int32 {
        return ThetaRepository.SleepDelaySec(sec: intVal)
    } else if let name = value as? String,
              let enumValue = getEnumValue(values: ThetaRepository.SleepDelayEnum.values(), name: name)
    {
        return enumValue
    } else {
        return nil
    }
}

func toBurstOption(params: [String: Any]) -> ThetaRepository.BurstOption {
    var burstCaptureNum: ThetaRepository.BurstCaptureNumEnum? = nil
    if let name = params["burstCaptureNum"] as? String {
        burstCaptureNum = getEnumValue(
            values: ThetaRepository.BurstCaptureNumEnum.values(), name: name
        )
    }

    var burstBracketStep: ThetaRepository.BurstBracketStepEnum? = nil
    if let name = params["burstBracketStep"] as? String {
        burstBracketStep = getEnumValue(
            values: ThetaRepository.BurstBracketStepEnum.values(), name: name
        )
    }

    var burstCompensation: ThetaRepository.BurstCompensationEnum? = nil
    if let name = params["burstCompensation"] as? String {
        burstCompensation = getEnumValue(
            values: ThetaRepository.BurstCompensationEnum.values(), name: name
        )
    }

    var burstMaxExposureTime: ThetaRepository.BurstMaxExposureTimeEnum? = nil
    if let name = params["burstMaxExposureTime"] as? String {
        burstMaxExposureTime = getEnumValue(
            values: ThetaRepository.BurstMaxExposureTimeEnum.values(), name: name
        )
    }

    var burstEnableIsoControl: ThetaRepository.BurstEnableIsoControlEnum? = nil
    if let name = params["burstEnableIsoControl"] as? String {
        burstEnableIsoControl = getEnumValue(
            values: ThetaRepository.BurstEnableIsoControlEnum.values(), name: name
        )
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

func toAutoBracket(params: [[String: Any]]) -> ThetaRepository.BracketSettingList {
    let autoBracket = ThetaRepository.BracketSettingList(list: NSMutableArray())

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

func toGpsInfo(params: [String: Any]) -> ThetaRepository.GpsInfo? {
    guard let latitude = params[KEY_GPS_LATITUDE] as? Double,
          let longitude = params[KEY_GPS_LONGITUDE] as? Double,
          let altitude = params[KEY_GPS_ALTITUDE] as? Double,
          let dateTimeZone = params[KEY_GPS_DATE_TIME_ZONE] as? String
    else { return nil }

    return ThetaRepository.GpsInfo(
        latitude: Float(latitude),
        longitude: Float(longitude),
        altitude: Float(altitude),
        dateTimeZone: dateTimeZone
    )
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

func toTimeShift(params: [String: Any]) -> ThetaRepository.TimeShiftSetting {
    var firstInterval: ThetaRepository.TimeShiftIntervalEnum? = nil
    if let name = params["firstInterval"] as? String {
        firstInterval = getEnumValue(
            values: ThetaRepository.TimeShiftIntervalEnum.values(), name: name
        )
    }

    var secondInterval: ThetaRepository.TimeShiftIntervalEnum? = nil
    if let name = params["secondInterval"] as? String {
        secondInterval = getEnumValue(
            values: ThetaRepository.TimeShiftIntervalEnum.values(), name: name
        )
    }

    return ThetaRepository.TimeShiftSetting(
        isFrontFirst: toKotlinBoolean(value: params["isFrontFirst"]),
        firstInterval: firstInterval,
        secondInterval: secondInterval
    )
}

func toTopBottomCorrectionRotation(params: [String: Any]) -> ThetaRepository.TopBottomCorrectionRotation? {
    guard let pitch = params[KEY_TOP_BOTTOM_CORRECTION_ROTATION_PITCH] as? Double,
          let roll = params[KEY_TOP_BOTTOM_CORRECTION_ROTATION_ROLL] as? Double,
          let yaw = params[KEY_TOP_BOTTOM_CORRECTION_ROTATION_YAW] as? Double else { return nil }

    return ThetaRepository.TopBottomCorrectionRotation(
        pitch: Float(pitch),
        roll: Float(roll),
        yaw: Float(yaw)
    )
}

func toDigestAuth(params: [String: String?]?) -> DigestAuth? {
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
        case KEY_DATETIME:
            config.dateTime = value as? String
        case KEY_LANGUAGE:
            config.language = getEnumValue(
                values: ThetaRepository.LanguageEnum.values(), name: value as? String ?? ""
            )
        case KEY_OFF_DELAY:
            config.offDelay = toOffDelay(value: value)
        case KEY_SLEEP_DELAY:
            config.sleepDelay = toSleepDelay(value: value)
        case KEY_SHUTTER_VOLUME:
            if let value = value as? Int {
                config.shutterVolume = KotlinInt(integerLiteral: value)
            }
        case KEY_CLIENT_MODE:
            config.clientMode = toDigestAuth(params: value as? [String: String?])
        default:
            break
        }
    }
    return config
}

func toTimeout(params: [String: Any]) -> ThetaRepository.Timeout? {
    guard let connectTimeout = params[KEY_CONNECT_TIMEOUT] as? Int64,
          let requestTimeout = params[KEY_REQUEST_TIMEOUT] as? Int64,
          let socketTimeout = params[KEY_SOCKET_TIMEOUT] as? Int64
    else { return nil }
    return ThetaRepository.Timeout(
        connectTimeout: connectTimeout,
        requestTimeout: requestTimeout,
        socketTimeout: socketTimeout
    )
}
