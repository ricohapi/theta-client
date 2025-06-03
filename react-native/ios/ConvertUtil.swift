import THETAClient
import UIKit

let KEY_CLIENT_MODE = "clientMode"
let KEY_NOTIFY_NAME = "name"
let KEY_NOTIFY_PARAMS = "params"
let KEY_NOTIFY_PARAM_COMPLETION = "completion"
let KEY_NOTIFY_PARAM_EVENT = "event"
let KEY_NOTIFY_PARAM_IMAGE = "image"
let KEY_NOTIFY_PARAM_MESSAGE = "message"
let KEY_NOTIFY_PARAM_STATUS = "status"
let KEY_NOTIFY_PARAM_FILE_URL = "fileUrl"
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
let KEY_AI_AUTO_THUMBNAIL_SUPPORT = "aiAutoThumbnailSupport"
let KEY_APERTURE = "aperture"
let KEY_CAMERA_CONTROL_SOURCE_SUPPORT = "cameraControlSourceSupport"
let KEY_CAMERA_POWER_SUPPORT = "cameraPowerSupport"
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
let KEY_STATE_EXTERNAL_GPS_INFO = "externalGpsInfo"
let KEY_STATE_INTERNAL_GPS_INFO = "internalGpsInfo"
let KEY_STATE_BOARD_TEMP = "boardTemp"
let KEY_STATE_BATTERY_TEMP = "batteryTemp"
let KEY_PROXY = "proxy"
let KEY_IP_ADDRESS = "ipAddress"
let KEY_MAC_ADDRESS = "macAddress"
let KEY_HOST_NAME = "hostName"
let KEY_SUBNET_MASK = "subnetMask"
let KEY_DEFAULT_GATEWAY = "defaultGateway"
let KEY_DNS1 = "dns1"
let KEY_DNS2 = "dns2"
let KEY_OPTIONS = "options"
let KEY_STATE = "state"
let KEY_SSID = "ssid"
let KEY_SSID_STEALTH = "ssidStealth"
let KEY_CONNECTION_PRIORITY = "connectionPriority"
let KEY_AUTH_MODE = "authMode"
let KEY_PASSWORD = "password"
let KEY_COLOR_TEMPERATURE_SUPPORT = "colorTemperatureSupport"
let KEY_WLAN_FREQUENCY_CL_MODE = "wlanFrequencyClMode"
let KEY_WLAN_FREQUENCY_CL_MODE_2_4 = "enable2_4"
let KEY_WLAN_FREQUENCY_CL_MODE_5_2 = "enable5_2"
let KEY_WLAN_FREQUENCY_CL_MODE_5_8 = "enable5_8"
let KEY_ROAMING = "roaming"
let KEY_PLAN = "plan"
let KEY_CAMERA_LOCK_CONFIG_IS_POWER_KEY_LOCKED = "isPowerKeyLocked"
let KEY_CAMERA_LOCK_CONFIG_IS_SHUTTER_KEY_LOCKED = "isShutterKeyLocked"
let KEY_CAMERA_LOCK_CONFIG_IS_MODE_KEY_LOCKED = "isModeKeyLocked"
let KEY_CAMERA_LOCK_CONFIG_IS_WLAN_KEY_LOCKED = "isWlanKeyLocked"
let KEY_CAMERA_LOCK_CONFIG_IS_FN_KEY_LOCKED = "isFnKeyLocked"
let KEY_CAMERA_LOCK_CONFIG_IS_PANEL_LOCKED = "isPanelLocked"
let KEY_DHCP_LEASE_ADDRESS = "dhcpLeaseAddress"
let KEY_TOP_BOTTOM_CORRECTION_ROTATION_SUPPORT = "topBottomCorrectionRotationSupport"
let KEY_MAX = "max"
let KEY_MIN = "min"
let KEY_STEP_SIZE = "stepSize"
let KEY_GPS_TAG_RECORDING_SUPPORT = "gpsTagRecordingSupport"
let KEY_COMPOSITE_SHOOTING_OUTPUT_INTERVAL_SUPPORT = "compositeShootingOutputIntervalSupport"
let KEY_COMPOSITE_SHOOTING_TIME_SUPPORT = "compositeShootingTimeSupport"
let KEY_APERTURE_SUPPORT = "apertureSupport"

public class ConvertUtil: NSObject {}

// MARK: - Options convert

let optionItemNameToEnum = [
    // TODO: Add items when adding options
    "accessInfo": ThetaRepository.OptionNameEnum.accessinfo,
    "aiAutoThumbnail": ThetaRepository.OptionNameEnum.aiautothumbnail,
    KEY_AI_AUTO_THUMBNAIL_SUPPORT: ThetaRepository.OptionNameEnum.aiautothumbnailsupport,
    KEY_APERTURE: ThetaRepository.OptionNameEnum.aperture,
    KEY_APERTURE_SUPPORT: ThetaRepository.OptionNameEnum.aperturesupport,
    KEY_AUTO_BRACKET: ThetaRepository.OptionNameEnum.autobracket,
    "bitrate": ThetaRepository.OptionNameEnum.bitrate,
    "bluetoothPower": ThetaRepository.OptionNameEnum.bluetoothpower,
    "bluetoothRole": ThetaRepository.OptionNameEnum.bluetoothrole,
    KEY_BURST_MODE: ThetaRepository.OptionNameEnum.burstmode,
    "burstOption": ThetaRepository.OptionNameEnum.burstoption,
    "cameraControlSource": ThetaRepository.OptionNameEnum.cameracontrolsource,
    KEY_CAMERA_CONTROL_SOURCE_SUPPORT: ThetaRepository.OptionNameEnum.cameracontrolsourcesupport,
    "cameraLock": ThetaRepository.OptionNameEnum.cameralock,
    "cameraLockConfig": ThetaRepository.OptionNameEnum.cameralockconfig,
    "cameraMode": ThetaRepository.OptionNameEnum.cameramode,
    "cameraPower": ThetaRepository.OptionNameEnum.camerapower,
    KEY_CAMERA_POWER_SUPPORT: ThetaRepository.OptionNameEnum.camerapowersupport,
    KEY_CAPTURE_INTERVAL: ThetaRepository.OptionNameEnum.captureinterval,
    "captureMode": ThetaRepository.OptionNameEnum.capturemode,
    "captureNumber": ThetaRepository.OptionNameEnum.capturenumber,
    KEY_COLOR_TEMPERATURE: ThetaRepository.OptionNameEnum.colortemperature,
    KEY_COLOR_TEMPERATURE_SUPPORT: ThetaRepository.OptionNameEnum.colortemperaturesupport,
    KEY_COMPOSITE_SHOOTING_OUTPUT_INTERVAL_SUPPORT: ThetaRepository.OptionNameEnum.compositeshootingoutputintervalsupport,
    "compassDirectionRef": ThetaRepository.OptionNameEnum.compassdirectionref,
    KEY_COMPOSITE_SHOOTING_OUTPUT_INTERVAL: ThetaRepository.OptionNameEnum.compositeshootingoutputinterval,
    "compositeShootingTime": ThetaRepository.OptionNameEnum.compositeshootingtime,
    KEY_COMPOSITE_SHOOTING_TIME_SUPPORT: ThetaRepository.OptionNameEnum.compositeshootingtimesupport,
    "continuousNumber": ThetaRepository.OptionNameEnum.continuousnumber,
    KEY_GPS_DATE_TIME_ZONE: ThetaRepository.OptionNameEnum.datetimezone,
    "ethernetConfig": ThetaRepository.OptionNameEnum.ethernetconfig,
    KEY_EXPOSURE_COMPENSATION: ThetaRepository.OptionNameEnum.exposurecompensation,
    KEY_EXPOSURE_DELAY: ThetaRepository.OptionNameEnum.exposuredelay,
    "exposureDelaySupport": ThetaRepository.OptionNameEnum.exposuredelaysupport,
    KEY_EXPOSURE_PROGRAM: ThetaRepository.OptionNameEnum.exposureprogram,
    "faceDetect": ThetaRepository.OptionNameEnum.facedetect,
    KEY_FILE_FORMAT: ThetaRepository.OptionNameEnum.fileformat,
    KEY_FILTER: ThetaRepository.OptionNameEnum.filter,
    "function": ThetaRepository.OptionNameEnum.function,
    "gain": ThetaRepository.OptionNameEnum.gain,
    KEY_GPS_INFO: ThetaRepository.OptionNameEnum.gpsinfo,
    KEY_GPS_TAG_RECORDING_SUPPORT: ThetaRepository.OptionNameEnum.gpstagrecordingsupport,
    "imageStitching": ThetaRepository.OptionNameEnum.imagestitching,
    "isGpsOn": ThetaRepository.OptionNameEnum.isgpson,
    KEY_ISO: ThetaRepository.OptionNameEnum.iso,
    KEY_ISO_AUTO_HIGH_LIMIT: ThetaRepository.OptionNameEnum.isoautohighlimit,
    KEY_LANGUAGE: ThetaRepository.OptionNameEnum.language,
    "latestEnabledExposureDelayTime": ThetaRepository.OptionNameEnum.latestenabledexposuredelaytime,
    KEY_MAX_RECORDABLE_TIME: ThetaRepository.OptionNameEnum.maxrecordabletime,
    "microphoneNoiseReduction": ThetaRepository.OptionNameEnum.microphonenoisereduction,
    "mobileNetworkSetting": ThetaRepository.OptionNameEnum.mobilenetworksetting,
    "networkType": ThetaRepository.OptionNameEnum.networktype,
    KEY_OFF_DELAY: ThetaRepository.OptionNameEnum.offdelay,
    "offDelayUsb": ThetaRepository.OptionNameEnum.offdelayusb,
    KEY_PROXY_PASSWORD: ThetaRepository.OptionNameEnum.password,
    "powerSaving": ThetaRepository.OptionNameEnum.powersaving,
    KEY_PRESET: ThetaRepository.OptionNameEnum.preset,
    "previewFormat": ThetaRepository.OptionNameEnum.previewformat,
    KEY_PROXY: ThetaRepository.OptionNameEnum.proxy,
    "remainingPictures": ThetaRepository.OptionNameEnum.remainingpictures,
    "remainingVideoSeconds": ThetaRepository.OptionNameEnum.remainingvideoseconds,
    "remainingSpace": ThetaRepository.OptionNameEnum.remainingspace,
    "shootingMethod": ThetaRepository.OptionNameEnum.shootingmethod,
    "shutterSpeed": ThetaRepository.OptionNameEnum.shutterspeed,
    KEY_SHUTTER_VOLUME: ThetaRepository.OptionNameEnum.shuttervolume,
    KEY_SLEEP_DELAY: ThetaRepository.OptionNameEnum.sleepdelay,
    KEY_TIMESHIFT: ThetaRepository.OptionNameEnum.timeshift,
    "topBottomCorrection": ThetaRepository.OptionNameEnum.topbottomcorrection,
    "topBottomCorrectionRotation": ThetaRepository.OptionNameEnum.topbottomcorrectionrotation,
    KEY_TOP_BOTTOM_CORRECTION_ROTATION_SUPPORT: ThetaRepository.OptionNameEnum.topbottomcorrectionrotationsupport,
    "totalSpace": ThetaRepository.OptionNameEnum.totalspace,
    "usbConnection": ThetaRepository.OptionNameEnum.usbconnection,
    "username": ThetaRepository.OptionNameEnum.username,
    "videoStitching": ThetaRepository.OptionNameEnum.videostitching,
    "visibilityReduction": ThetaRepository.OptionNameEnum.visibilityreduction,
    KEY_WHITE_BALANCE: ThetaRepository.OptionNameEnum.whitebalance,
    "whiteBalanceAutoStrength": ThetaRepository.OptionNameEnum.whitebalanceautostrength,
    "wlanAntennaConfig": ThetaRepository.OptionNameEnum.wlanantennaconfig,
    "wlanFrequency": ThetaRepository.OptionNameEnum.wlanfrequency,
    "wlanFrequencySupport": ThetaRepository.OptionNameEnum.wlanfrequencysupport,
    KEY_WLAN_FREQUENCY_CL_MODE: ThetaRepository.OptionNameEnum.wlanfrequencyclmode,
]

let supportOptions: [ThetaRepository.OptionNameEnum: Any.Type] = [
    ThetaRepository.OptionNameEnum.aiautothumbnailsupport: ThetaRepository.AiAutoThumbnailEnum.self,
    ThetaRepository.OptionNameEnum.aperturesupport: ThetaRepository.ApertureEnum.self,
    ThetaRepository.OptionNameEnum.cameracontrolsourcesupport: ThetaRepository.CameraControlSourceEnum.self,
    ThetaRepository.OptionNameEnum.camerapowersupport: ThetaRepository.CameraPowerEnum.self,
    ThetaRepository.OptionNameEnum.exposuredelaysupport: ThetaRepository.ExposureDelayEnum.self,
    ThetaRepository.OptionNameEnum.gpstagrecordingsupport: ThetaRepository.GpsTagRecordingEnum.self,
    ThetaRepository.OptionNameEnum.wlanfrequencysupport: ThetaRepository.WlanFrequencyEnum.self,
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
    case ThetaRepository.OptionNameEnum.accessinfo.name:
        if let params = value as? [String: Any?] {
            options.accessInfo = toAccessInfo(params: params)
        }
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
    case ThetaRepository.OptionNameEnum.cameralock.name:
        options.cameraLock = getEnumValue(
            values: ThetaRepository.CameraLockEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.cameralockconfig.name:
        if let params = value as? [String: Any] {
            options.cameraLockConfig = toCameraLockConfig(params: params)
        }
    case ThetaRepository.OptionNameEnum.cameramode.name:
        options.cameraMode = getEnumValue(
            values: ThetaRepository.CameraModeEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.camerapower.name:
        options.cameraPower = getEnumValue(
            values: ThetaRepository.CameraPowerEnum.values(), name: value as! String
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
    case ThetaRepository.OptionNameEnum.compassdirectionref.name:
        options.compassDirectionRef = getEnumValue(
            values: ThetaRepository.CompassDirectionRefEnum.values(), name: value as! String
        )!
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
    case ThetaRepository.OptionNameEnum.ethernetconfig.name:
        if let params = value as? [String: Any] {
            options.ethernetConfig = toEthernetConfig(params: params)
        }
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
    case ThetaRepository.OptionNameEnum.microphonenoisereduction.name:
        options.microphoneNoiseReduction = getEnumValue(
            values: ThetaRepository.MicrophoneNoiseReductionEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.mobilenetworksetting.name:
        if let params = value as? [String: Any] {
            options.mobileNetworkSetting = toMobileNetworkSetting(params: params)
        }
    case ThetaRepository.OptionNameEnum.networktype.name:
        options.networkType = getEnumValue(
            values: ThetaRepository.NetworkTypeEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.offdelay.name:
        options.offDelay = toOffDelay(value: value)
    case ThetaRepository.OptionNameEnum.offdelayusb.name:
        options.offDelayUsb = toOffDelayUsb(value: value)
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
    case ThetaRepository.OptionNameEnum.usbconnection.name:
        options.usbConnection = getEnumValue(
            values: ThetaRepository.UsbConnectionEnum.values(), name: value as! String
        )!
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
    case ThetaRepository.OptionNameEnum.wlanantennaconfig.name:
        options.wlanAntennaConfig = getEnumValue(
            values: ThetaRepository.WlanAntennaConfigEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.wlanfrequency.name:
        options.wlanFrequency = getEnumValue(
            values: ThetaRepository.WlanFrequencyEnum.values(), name: value as! String
        )!
    case ThetaRepository.OptionNameEnum.wlanfrequencyclmode.name:
        options.wlanFrequencyClMode = toWlanFrequencyClMode(params: value as! [String: Any])
    default: break
    }
}

func convertResult(options: ThetaRepository.Options) -> [String: Any] {
    var result = [String: Any]()
    var jsonResult = [String: Any]()
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
                } else if value is ThetaRepository.AccessInfo,
                          let accessInfo = value as? ThetaRepository.AccessInfo
                {
                    result[key] = convertResult(accessInfo: accessInfo)
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
                } else if value is ThetaRepository.CameraLockConfig,
                          let config = value as? ThetaRepository.CameraLockConfig
                {
                    result[key] = convertResult(cameraLockConfig: config)
                } else if value is ThetaRepository.EthernetConfig,
                          let ethernetConfig = value as? ThetaRepository.EthernetConfig
                {
                    result[key] = convertResult(ethernetConfig: ethernetConfig)
                } else if value is ThetaRepository.GpsInfo {
                    let gpsInfo = value as! ThetaRepository.GpsInfo
                    result[key] = convertResult(gpsInfo: gpsInfo)
                } else if value is ThetaRepository.MobileNetworkSetting,
                          let mobileNetworkSetting = value as? ThetaRepository.MobileNetworkSetting
                {
                    result[key] = convertResult(mobileNetworkSetting: mobileNetworkSetting)
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
                } else if value is ThetaRepository.TopBottomCorrectionRotationSupport,
                          let support = value as? ThetaRepository.TopBottomCorrectionRotationSupport
                {
                    jsonResult[key] = convertJson(topBottomCorrectionRotationSupport: support)
                } else if let offDelay = value as? ThetaRepository.OffDelaySec {
                    result[key] = offDelay.sec_ == 0 ? ThetaRepository.OffDelayEnum.disable.name : offDelay.sec_
                } else if let sleepDelay = value as? ThetaRepository.SleepDelaySec {
                    result[key] = sleepDelay.sec_ == 0 ? ThetaRepository.SleepDelayEnum.disable.name : sleepDelay.sec_
                } else if let enumValue = value as? [AnyObject], let enumType = supportOptions[name] {
                    result[key] = convertSupportResult(supportValueList: enumValue, enumType: enumType)
                } else if let valueRange = value as? ThetaRepositoryValueRange<NSNumber> {
                    result[key] = convertValueRangeSupportResult(valueRange: valueRange)
                } else if let offDelay = value as? ThetaRepository.OffDelaySec {
                    result[key] = offDelay.sec_ == 0 ? ThetaRepository.OffDelayEnum.disable.name : offDelay.sec_
                } else if let offDelayUsb = value as? ThetaRepository.OffDelayUsbSec {
                    result[key] = offDelayUsb.sec_ == 0 ? ThetaRepository.OffDelayUsbEnum.disable.name : offDelayUsb.sec_
                } else if let sleepDelay = value as? ThetaRepository.SleepDelaySec {
                    result[key] = sleepDelay.sec_ == 0 ? ThetaRepository.SleepDelayEnum.disable.name : sleepDelay.sec_
                } else if value is ThetaRepository.WlanFrequencyClMode, let wlanFrequencyClMode = value as? ThetaRepository.WlanFrequencyClMode {
                    result[key] = convertResult(wlanFrequencyClMode: wlanFrequencyClMode)
                } else if let enumValue = value as? [AnyObject], let enumType = supportOptions[name] {
                    result[key] = convertSupportResult(supportValueList: enumValue, enumType: enumType)
                }
                // TODO: Add class item when adding options
            }
        }
    }

    var response = [String: Any]()
    response[KEY_OPTIONS] = result
    response["json"] = jsonResult
    return response
}

// MARK: - Notify event

func toNotify(name: String, params: [String: Any]?) -> [String: Any] {
    var result: [String: Any] = [
        KEY_NOTIFY_NAME: name,
    ]
    if let params {
        result[KEY_NOTIFY_PARAMS] = params
    }
    return result
}

func toCaptureProgressNotifyParam(value: Float) -> [String: Any] {
    [
        KEY_NOTIFY_PARAM_COMPLETION: value,
    ]
}

func toEventWebSocketEventNotifyParam(value: CameraEvent) -> [String: Any] {
    [
        KEY_NOTIFY_PARAM_EVENT: convertResult(cameraEvent: value),
    ]
}

func toMessageNotifyParam(value: String) -> [String: Any] {
    [
        KEY_NOTIFY_PARAM_MESSAGE: value,
    ]
}

func toCapturingNotifyParam(value: CapturingStatusEnum) -> [String: Any] {
    [
        KEY_NOTIFY_PARAM_STATUS: value.name,
    ]
}

func toStartedNotifyParam(value: String) -> [String: Any] {
    [
        KEY_NOTIFY_PARAM_FILE_URL: value,
    ]
}

// MARK: - Capture builder

func setCaptureBuilderParams(params: [String: Any], builder: CaptureBuilder<some Any>) {
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
    if let interval = params[KEY_TIMESHIFT_CAPTURE_INTERVAL] as? Int,
       interval >= 0
    {
        builder.setCheckStatusCommandInterval(timeMillis: Int64(interval))
    }
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

func setTimeShiftManualCaptureBuilderParams(params: [String: Any], builder: TimeShiftManualCapture.Builder) {
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
    if let interval = params[KEY_TIMESHIFT_CAPTURE_INTERVAL] as? Int,
       interval >= 0
    {
        builder.setCheckStatusCommandInterval(timeMillis: Int64(interval))
    }
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
    if let interval = params[KEY_TIMESHIFT_CAPTURE_INTERVAL] as? Int,
       interval >= 0
    {
        builder.setCheckStatusCommandInterval(timeMillis: Int64(interval))
    }
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

func setMultiBracketCaptureBuilderParams(params: [String: Any], builder: MultiBracketCapture.Builder) throws {
    if let autoBracket = params[KEY_AUTO_BRACKET] as? [[String: Any]] {
        try autoBracket.forEach { map in
            let aperture = {
                if let name = map[KEY_APERTURE] as? String {
                    return getEnumValue(values: ThetaRepository.ApertureEnum.values(), name: name)
                } else {
                    return nil
                }
            }()

            let colorTemperature = {
                if let value = map[KEY_COLOR_TEMPERATURE] as? Int {
                    return toKotlinInt(value: value)
                } else {
                    return nil
                }
            }()

            let exposureCompensation = {
                if let name = map[KEY_EXPOSURE_COMPENSATION] as? String {
                    return getEnumValue(values: ThetaRepository.ExposureCompensationEnum.values(), name: name)
                } else {
                    return nil
                }
            }()

            let exposureProgram = {
                if let name = map[KEY_EXPOSURE_PROGRAM] as? String {
                    return getEnumValue(values: ThetaRepository.ExposureProgramEnum.values(), name: name)
                } else {
                    return nil
                }
            }()

            let iso = {
                if let name = map[KEY_ISO] as? String {
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
                if let name = map[KEY_WHITE_BALANCE] as? String {
                    return getEnumValue(values: ThetaRepository.WhiteBalanceEnum.values(), name: name)
                } else {
                    return nil
                }
            }()

            try builder.addBracketParameters(
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
    if let interval = params[KEY_TIMESHIFT_CAPTURE_INTERVAL] as? Int,
       interval >= 0
    {
        builder.setCheckStatusCommandInterval(timeMillis: Int64(interval))
    }
    if let value = params[KEY_FILE_FORMAT] as? String {
        if let enumValue = getEnumValue(
            values: ThetaRepository.PhotoFileFormatEnum.values(), name: value
        ) {
            builder.setFileFormat(fileFormat: enumValue)
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
    guard let value else { return nil }
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

func convertResult(accessInfo: ThetaRepository.AccessInfo) -> [String: Any] {
    var result: [String: Any] = [:]
    result[KEY_SSID] = accessInfo.ssid
    result[KEY_IP_ADDRESS] = accessInfo.ipAddress
    result[KEY_SUBNET_MASK] = accessInfo.subnetMask
    result[KEY_DEFAULT_GATEWAY] = accessInfo.defaultGateway
    if let dns1 = accessInfo.dns1 {
        result[KEY_DNS1] = dns1
    }
    if let dns2 = accessInfo.dns2 {
        result[KEY_DNS2] = dns2
    }
    result["proxyURL"] = accessInfo.proxyURL
    result["frequency"] = accessInfo.frequency.name
    result["wlanSignalStrength"] = accessInfo.wlanSignalStrength
    result["wlanSignalLevel"] = accessInfo.wlanSignalLevel
    result["lteSignalStrength"] = accessInfo.lteSignalStrength
    result["lteSignalLevel"] = accessInfo.lteSignalLevel

    if let list = accessInfo.dhcpLeaseAddress {
        let array = list.map { item in convertResult(dhcpLeaseAddress: item) }
        if !array.isEmpty {
            result[KEY_DHCP_LEASE_ADDRESS] = array
        }
    }
    return result
}

func convertResult(dhcpLeaseAddress: ThetaRepository.DhcpLeaseAddress) -> [String: Any] {
    [
        KEY_IP_ADDRESS: dhcpLeaseAddress.ipAddress,
        KEY_MAC_ADDRESS: dhcpLeaseAddress.macAddress,
        KEY_HOST_NAME: dhcpLeaseAddress.hostName,
    ]
}

func convertResult(fileInfoList: [ThetaRepository.FileInfo]) -> [[String: Any]] {
    var resultList = [[String: Any]]()
    fileInfoList.forEach { fileInfo in
        var item = [
            "name": fileInfo.name,
            "fileUrl": fileInfo.fileUrl,
            "size": fileInfo.size,
            KEY_DATETIME: fileInfo.dateTime,
            "thumbnailUrl": fileInfo.thumbnailUrl,
        ]
        if let dateTimeZone = fileInfo.dateTimeZone {
            item[KEY_GPS_DATE_TIME_ZONE] = dateTimeZone
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
    [
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
    guard let cameraErrorList else {
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
                result[KEY_APERTURE] = aperture
            }
            if let colorTemperature = setting.colorTemperature?.intValue {
                result[KEY_COLOR_TEMPERATURE] = colorTemperature
            }
            if let exposureCompensation = setting.exposureCompensation?.name {
                result[KEY_EXPOSURE_COMPENSATION] = exposureCompensation
            }
            if let exposureProgram = setting.exposureProgram?.name {
                result[KEY_EXPOSURE_PROGRAM] = exposureProgram
            }
            if let iso = setting.iso?.name {
                result[KEY_ISO] = iso
            }
            if let shutterSpeed = setting.shutterSpeed?.name {
                result["shutterSpeed"] = shutterSpeed
            }
            if let whiteBalance = setting.whiteBalance?.name {
                result[KEY_WHITE_BALANCE] = whiteBalance
            }
            resultList.append(result)
        }
    }

    return resultList
}

func convertResult(cameraLockConfig: ThetaRepository.CameraLockConfig) -> [String: Any] {
    var result: [String: Any] = [:]
    if let isPowerKeyLocked = cameraLockConfig.isPowerKeyLocked {
        result[KEY_CAMERA_LOCK_CONFIG_IS_POWER_KEY_LOCKED] = isPowerKeyLocked.boolValue
    }
    if let isShutterKeyLocked = cameraLockConfig.isShutterKeyLocked {
        result[KEY_CAMERA_LOCK_CONFIG_IS_SHUTTER_KEY_LOCKED] = isShutterKeyLocked.boolValue
    }
    if let isModeKeyLocked = cameraLockConfig.isModeKeyLocked {
        result[KEY_CAMERA_LOCK_CONFIG_IS_MODE_KEY_LOCKED] = isModeKeyLocked.boolValue
    }
    if let isWlanKeyLocked = cameraLockConfig.isWlanKeyLocked {
        result[KEY_CAMERA_LOCK_CONFIG_IS_WLAN_KEY_LOCKED] = isWlanKeyLocked.boolValue
    }
    if let isFnKeyLocked = cameraLockConfig.isFnKeyLocked {
        result[KEY_CAMERA_LOCK_CONFIG_IS_FN_KEY_LOCKED] = isFnKeyLocked.boolValue
    }
    if let isPanelLocked = cameraLockConfig.isPanelLocked {
        result[KEY_CAMERA_LOCK_CONFIG_IS_PANEL_LOCKED] = isPanelLocked.boolValue
    }
    return result
}

func convertResult(ethernetConfig: ThetaRepository.EthernetConfig) -> [String: Any] {
    var result: [String: Any] = ["usingDhcp": ethernetConfig.usingDhcp]
    if let ipAddress = ethernetConfig.ipAddress {
        result[KEY_IP_ADDRESS] = ipAddress
    }
    if let subnetMask = ethernetConfig.subnetMask {
        result[KEY_SUBNET_MASK] = subnetMask
    }
    if let defaultGateway = ethernetConfig.defaultGateway {
        result[KEY_DEFAULT_GATEWAY] = defaultGateway
    }
    if let dns1 = ethernetConfig.dns1 {
        result[KEY_DNS1] = dns1
    }
    if let dns2 = ethernetConfig.dns2 {
        result[KEY_DNS2] = dns2
    }
    if let proxy = ethernetConfig.proxy {
        result[KEY_PROXY] = convertResult(proxy: proxy)
    }
    return result
}

func convertResult(gpsInfo: ThetaRepository.GpsInfo) -> [String: Any] {
    [
        KEY_GPS_LATITUDE: gpsInfo.latitude,
        KEY_GPS_LONGITUDE: gpsInfo.longitude,
        KEY_GPS_ALTITUDE: gpsInfo.altitude,
        KEY_GPS_DATE_TIME_ZONE: gpsInfo.dateTimeZone,
    ]
}

func convertResult(stateGpsInfo: ThetaRepository.StateGpsInfo) -> [String: Any] {
    guard let gpsInfo = stateGpsInfo.gpsInfo else { return [:] }
    return [
        KEY_GPS_INFO: convertResult(gpsInfo: gpsInfo),
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
    [
        KEY_TOP_BOTTOM_CORRECTION_ROTATION_PITCH: rotation.pitch,
        KEY_TOP_BOTTOM_CORRECTION_ROTATION_ROLL: rotation.roll,
        KEY_TOP_BOTTOM_CORRECTION_ROTATION_YAW: rotation.yaw,
    ]
}

func convertJson(topBottomCorrectionRotationSupport: ThetaRepository.TopBottomCorrectionRotationSupport) -> String {
    let convertJson = """
    {
      "pitch": {
        "max": \(topBottomCorrectionRotationSupport.pitch.max),
        "min": \(topBottomCorrectionRotationSupport.pitch.min),
        "stepSize": \(topBottomCorrectionRotationSupport.pitch.stepSize)
      },
      "roll": {
        "max": \(topBottomCorrectionRotationSupport.roll.max),
        "min": \(topBottomCorrectionRotationSupport.roll.min),
        "stepSize": \(topBottomCorrectionRotationSupport.roll.stepSize)
      },
      "yaw": {
        "max": \(topBottomCorrectionRotationSupport.yaw.max),
        "min": \(topBottomCorrectionRotationSupport.yaw.min),
        "stepSize": \(topBottomCorrectionRotationSupport.yaw.stepSize)
      }
    }
    """
    return convertJson
}

func convertResult(exif: ThetaRepository.Exif) -> [String: Any] {
    var result = [String: Any]()
    result["exifVersion"] = exif.exifVersion
    result[KEY_DATETIME] = exif.dateTime
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
    [
        "exif": convertResult(exif: metadata.first!),
        "xmp": convertResult(xmp: metadata.second!),
    ]
}

func convertResult(accessPointList: [ThetaRepository.AccessPoint]) -> [[String: Any]] {
    var resultList = [[String: Any]]()
    accessPointList.forEach { accessPoint in
        var result = [String: Any]()
        result[KEY_SSID] = accessPoint.ssid
        result[KEY_SSID_STEALTH] = accessPoint.ssidStealth
        result[KEY_AUTH_MODE] = accessPoint.authMode.name
        result[KEY_CONNECTION_PRIORITY] = accessPoint.connectionPriority
        result["usingDhcp"] = accessPoint.usingDhcp
        if let ipAddress = accessPoint.ipAddress {
            result[KEY_IP_ADDRESS] = ipAddress
        }
        if let subnetMask = accessPoint.subnetMask {
            result[KEY_SUBNET_MASK] = subnetMask
        }
        if let defaultGateway = accessPoint.defaultGateway {
            result[KEY_DEFAULT_GATEWAY] = defaultGateway
        }
        if let dns1 = accessPoint.dns1 {
            result[KEY_DNS1] = dns1
        }
        if let dns2 = accessPoint.dns2 {
            result[KEY_DNS2] = dns2
        }
        if let proxy = accessPoint.proxy {
            result[KEY_PROXY] = convertResult(proxy: proxy)
        }
        resultList.append(result)
    }
    return resultList
}

func convertResult(mobileNetworkSetting: ThetaRepository.MobileNetworkSetting) -> [String: Any] {
    var result: [String: Any] = [:]
    if let roaming = mobileNetworkSetting.roaming {
        result[KEY_ROAMING] = roaming.name
    }
    if let plan = mobileNetworkSetting.plan {
        result[KEY_PLAN] = plan.name
    }
    return result
}

func toPluginInfosResult(pluginInfoList: [ThetaRepository.PluginInfo]) -> [[String: Any]] {
    var resultList = [[String: Any]]()
    pluginInfoList.forEach { pluginInfo in
        let item = [
            KEY_NOTIFY_NAME: pluginInfo.name,
            "packageName": pluginInfo.packageName,
            "version": pluginInfo.version,
            "isPreInstalled": pluginInfo.isPreInstalled,
            "isRunning": pluginInfo.isRunning,
            "isForeground": pluginInfo.isForeground,
            "isBoot": pluginInfo.isBoot,
            "hasWebServer": pluginInfo.hasWebServer,
            "exitStatus": pluginInfo.exitStatus,
            KEY_NOTIFY_PARAM_MESSAGE: pluginInfo.message,
        ]
        resultList.append(item)
    }
    return resultList
}

func convertResult(cameraEvent: CameraEvent) -> [String: Any] {
    var result = [String: Any]()
    if let value = cameraEvent.options {
        result[KEY_OPTIONS] = convertResult(options: value)
    }
    if let value = cameraEvent.state {
        result[KEY_STATE] = convertResult(thetaState: value)
    }

    return result
}

func convertResult(wlanFrequencyClMode: ThetaRepository.WlanFrequencyClMode) -> [String: Any] {
    [
        KEY_WLAN_FREQUENCY_CL_MODE_2_4: wlanFrequencyClMode.enable2_4,
        KEY_WLAN_FREQUENCY_CL_MODE_5_2: wlanFrequencyClMode.enable5_2,
        KEY_WLAN_FREQUENCY_CL_MODE_5_8: wlanFrequencyClMode.enable5_8,
    ]
}

/**
 * Convert support values
 *
 * @param supportValueList Support values
 * @param enumType values type. e.g. ThetaRepository.WlanStandardInfoEnum.self , Int.self {class}.seff
 */
func convertSupportResult(supportValueList: [Any], enumType: Any.Type) -> [Any] {
    var result = [Any]()
    for item in supportValueList {
        // for enum value
        if let value = item as? KotlinEnum<AnyObject>,
           enumType is KotlinEnum<AnyObject>.Type
        {
            result.append(value.name)
        }
    }
    return result
}

func convertValueRangeSupportResult(valueRange: ThetaRepositoryValueRange<some Any>) -> [String: Any] {
    var result: [String: Any] = [:]
    result[KEY_MAX] = valueRange.max
    result[KEY_MIN] = valueRange.min
    result[KEY_STEP_SIZE] = valueRange.stepSize
    return result
}

// MARK: - Convert to theta-client object

func toAccessInfo(params: [String: Any?]) -> ThetaRepository.AccessInfo {
    let frequency = getEnumValue(
        values: ThetaRepository.WlanFrequencyAccessInfoEnum.values(), name: params["frequency"] as! String
    )!

    let dhcpLeaseAddress: [ThetaRepository.DhcpLeaseAddress]? = {
        guard let list = params[KEY_DHCP_LEASE_ADDRESS] as? [[String: Any?]] else {
            return nil
        }

        let array = list.compactMap { toDhcpLeaseAddress(value: $0) }
        return array.isEmpty ? nil : array
    }()

    return ThetaRepository.AccessInfo(
        ssid: params[KEY_SSID] as! String,
        ipAddress: params[KEY_IP_ADDRESS] as! String,
        subnetMask: params[KEY_SUBNET_MASK] as! String,
        defaultGateway: params[KEY_DEFAULT_GATEWAY] as! String,
        dns1: params[KEY_DNS1] as? String,
        dns2: params[KEY_DNS2] as? String,
        proxyURL: params["proxyURL"] as! String,
        frequency: frequency,
        wlanSignalStrength: params["wlanSignalStrength"] as! Int32,
        wlanSignalLevel: params["wlanSignalLevel"] as! Int32,
        lteSignalStrength: params["lteSignalStrength"] as! Int32,
        lteSignalLevel: params["lteSignalLevel"] as! Int32,
        dhcpLeaseAddress: dhcpLeaseAddress
    )
}

func toDhcpLeaseAddress(value: [String: Any?]) -> ThetaRepository.DhcpLeaseAddress? {
    guard let ipAddress = value[KEY_IP_ADDRESS] as? String,
          let macAddress = value[KEY_MAC_ADDRESS] as? String,
          let hostName = value[KEY_HOST_NAME] as? String
    else {
        return nil
    }
    return ThetaRepository.DhcpLeaseAddress(
        ipAddress: ipAddress,
        macAddress: macAddress,
        hostName: hostName
    )
}

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

func toOffDelayUsb(value: Any) -> ThetaRepositoryOffDelayUsb? {
    if value is NSNumber, let intVal = value as? Int32 {
        return ThetaRepository.OffDelayUsbSec(sec: intVal)
    } else if let name = value as? String,
              let enumValue = getEnumValue(values: ThetaRepository.OffDelayUsbEnum.values(), name: name)
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
    if let name = params[KEY_BURST_CAPTURE_NUM] as? String {
        burstCaptureNum = getEnumValue(
            values: ThetaRepository.BurstCaptureNumEnum.values(), name: name
        )
    }

    var burstBracketStep: ThetaRepository.BurstBracketStepEnum? = nil
    if let name = params[KEY_BURST_BRACKET_STEP] as? String {
        burstBracketStep = getEnumValue(
            values: ThetaRepository.BurstBracketStepEnum.values(), name: name
        )
    }

    var burstCompensation: ThetaRepository.BurstCompensationEnum? = nil
    if let name = params[KEY_BURST_COMPENSATION] as? String {
        burstCompensation = getEnumValue(
            values: ThetaRepository.BurstCompensationEnum.values(), name: name
        )
    }

    var burstMaxExposureTime: ThetaRepository.BurstMaxExposureTimeEnum? = nil
    if let name = params[KEY_BURST_MAX_EXPOSURE_TIME] as? String {
        burstMaxExposureTime = getEnumValue(
            values: ThetaRepository.BurstMaxExposureTimeEnum.values(), name: name
        )
    }

    var burstEnableIsoControl: ThetaRepository.BurstEnableIsoControlEnum? = nil
    if let name = params[KEY_BURST_ENABLE_ISO_CONTROL] as? String {
        burstEnableIsoControl = getEnumValue(
            values: ThetaRepository.BurstEnableIsoControlEnum.values(), name: name
        )
    }

    var burstOrder: ThetaRepository.BurstOrderEnum? = nil
    if let name = params[KEY_BURST_ORDER] as? String {
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
    let autoBracket = ThetaRepository.BracketSettingList()

    params.forEach { map in
        let aperture = {
            if let name = map[KEY_APERTURE] as? String {
                return getEnumValue(values: ThetaRepository.ApertureEnum.values(), name: name)
            } else {
                return nil
            }
        }()

        let colorTemperature = {
            if let value = map[KEY_COLOR_TEMPERATURE] as? Int {
                return toKotlinInt(value: value)
            } else {
                return nil
            }
        }()

        let exposureCompensation = {
            if let name = map[KEY_EXPOSURE_COMPENSATION] as? String {
                return getEnumValue(values: ThetaRepository.ExposureCompensationEnum.values(), name: name)
            } else {
                return nil
            }
        }()

        let exposureProgram = {
            if let name = map[KEY_EXPOSURE_PROGRAM] as? String {
                return getEnumValue(values: ThetaRepository.ExposureProgramEnum.values(), name: name)
            } else {
                return nil
            }
        }()

        let iso = {
            if let name = map[KEY_ISO] as? String {
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
            if let name = map[KEY_WHITE_BALANCE] as? String {
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

func toCameraLockConfig(params: [String: Any]) -> ThetaRepository.CameraLockConfig {
    ThetaRepository.CameraLockConfig(
        isPowerKeyLocked: toKotlinBoolean(value: params[KEY_CAMERA_LOCK_CONFIG_IS_POWER_KEY_LOCKED]),
        isShutterKeyLocked: toKotlinBoolean(value: params[KEY_CAMERA_LOCK_CONFIG_IS_SHUTTER_KEY_LOCKED]),
        isModeKeyLocked: toKotlinBoolean(value: params[KEY_CAMERA_LOCK_CONFIG_IS_MODE_KEY_LOCKED]),
        isWlanKeyLocked: toKotlinBoolean(value: params[KEY_CAMERA_LOCK_CONFIG_IS_WLAN_KEY_LOCKED]),
        isFnKeyLocked: toKotlinBoolean(value: params[KEY_CAMERA_LOCK_CONFIG_IS_FN_KEY_LOCKED]),
        isPanelLocked: toKotlinBoolean(value: params[KEY_CAMERA_LOCK_CONFIG_IS_PANEL_LOCKED])
    )
}

func toEthernetConfig(params: [String: Any]) -> ThetaRepository.EthernetConfig {
    var proxy: ThetaRepository.Proxy? = nil
    if let data = params[KEY_PROXY] as? [String: Any] {
        proxy = toProxy(params: data)
    }

    return ThetaRepository.EthernetConfig(
        usingDhcp: params["usingDhcp"] as? Bool ?? true,
        ipAddress: params[KEY_IP_ADDRESS] as? String,
        subnetMask: params[KEY_SUBNET_MASK] as? String,
        defaultGateway: params[KEY_DEFAULT_GATEWAY] as? String,
        dns1: params[KEY_DNS1] as? String,
        dns2: params[KEY_DNS2] as? String,
        proxy: proxy
    )
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
    ThetaRepository.Proxy(
        use: params[KEY_PROXY_USE] as? Bool ?? false,
        url: params[KEY_PROXY_URL] as? String,
        port: toKotlinInt(value: params[KEY_PROXY_PORT]),
        userid: params[KEY_PROXY_USER_ID] as? String,
        password: params[KEY_PROXY_PASSWORD] as? String
    )
}

func toTimeShift(params: [String: Any]) -> ThetaRepository.TimeShiftSetting {
    var firstInterval: ThetaRepository.TimeShiftIntervalEnum? = nil
    if let name = params[KEY_TIMESHIFT_FIRST_INTERVAL] as? String {
        firstInterval = getEnumValue(
            values: ThetaRepository.TimeShiftIntervalEnum.values(), name: name
        )
    }

    var secondInterval: ThetaRepository.TimeShiftIntervalEnum? = nil
    if let name = params[KEY_TIMESHIFT_SECOND_INTERVAL] as? String {
        secondInterval = getEnumValue(
            values: ThetaRepository.TimeShiftIntervalEnum.values(), name: name
        )
    }

    return ThetaRepository.TimeShiftSetting(
        isFrontFirst: toKotlinBoolean(value: params[KEY_TIMESHIFT_IS_FRONT_FIRST]),
        firstInterval: firstInterval,
        secondInterval: secondInterval
    )
}

func toTopBottomCorrectionRotation(params: [String: Any]) -> ThetaRepository.TopBottomCorrectionRotation? {
    let pitch = Float(params[KEY_TOP_BOTTOM_CORRECTION_ROTATION_PITCH] as? Double ?? 0)
    let roll = Float(params[KEY_TOP_BOTTOM_CORRECTION_ROTATION_ROLL] as? Double ?? 0)
    let yaw = Float(params[KEY_TOP_BOTTOM_CORRECTION_ROTATION_YAW] as? Double ?? 0)

    return ThetaRepository.TopBottomCorrectionRotation(
        pitch: pitch,
        roll: roll,
        yaw: yaw
    )
}

func toDigestAuth(params: [String: String?]?) -> DigestAuth? {
    guard let params,
          let username = params["username"] as? String
    else { return nil }

    let password = params[KEY_PROXY_PASSWORD] as? String
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

struct SetAccessPointParams {
    let ssid: String
    let ssidStealth: KotlinBoolean?
    let authMode: ThetaRepository.AuthModeEnum
    let password: String?
    let connectionPriority: KotlinInt?
    let dns1: String?
    let dns2: String?
    let proxy: ThetaRepository.Proxy?
}

func toSetAccessPointParams(params: [String: Any?]) throws -> SetAccessPointParams {
    guard let ssid = params[KEY_SSID] as? String else {
        throw ThetaClientError.invalidArgument(KEY_SSID)
    }
    let ssidStealth = toKotlinBoolean(value: params[KEY_SSID_STEALTH] as? Bool)
    guard let authMode = params[KEY_AUTH_MODE] as? String else {
        throw ThetaClientError.invalidArgument(KEY_AUTH_MODE)
    }
    guard
        let authModeEnum = getEnumValue(
            values: ThetaRepository.AuthModeEnum.values(), name: authMode
        )
    else {
        throw ThetaClientError.invalidArgument(KEY_AUTH_MODE)
    }
    let password = params[KEY_PASSWORD] as? String
    let connectionPriority = toKotlinInt(value: params[KEY_CONNECTION_PRIORITY] as? Int)
    let dns1 = params[KEY_DNS1] as? String
    let dns2 = params[KEY_DNS2] as? String
    let proxy = params[KEY_PROXY]
    let proxyParam: ThetaRepository.Proxy? = {
        if let proxy = proxy as? [String: Any] {
            return toProxy(params: proxy)
        }
        return nil
    }()

    return SetAccessPointParams(
        ssid: ssid,
        ssidStealth: ssidStealth,
        authMode: authModeEnum,
        password: password,
        connectionPriority: connectionPriority,
        dns1: dns1,
        dns2: dns2,
        proxy: proxyParam
    )
}

struct SetAccessPointStaticallyParams {
    let ipAddress: String
    let subnetMask: String
    let defaultGateway: String
}

func toSetAccessPointStaticallyParams(params: [String: Any?]) throws -> SetAccessPointStaticallyParams {
    guard let ipAddress = params[KEY_IP_ADDRESS] as? String else {
        throw ThetaClientError.invalidArgument(KEY_IP_ADDRESS)
    }
    guard let subnetMask = params[KEY_SUBNET_MASK] as? String else {
        throw ThetaClientError.invalidArgument(KEY_SUBNET_MASK)
    }
    guard let defaultGateway = params[KEY_DEFAULT_GATEWAY] as? String else {
        throw ThetaClientError.invalidArgument(KEY_DEFAULT_GATEWAY)
    }

    return SetAccessPointStaticallyParams(
        ipAddress: ipAddress,
        subnetMask: subnetMask,
        defaultGateway: defaultGateway
    )
}

func toWlanFrequencyClMode(params: [String: Any]) -> ThetaRepository.WlanFrequencyClMode? {
    guard let enable2_4 = params[KEY_WLAN_FREQUENCY_CL_MODE_2_4] as? Bool,
          let enable5_2 = params[KEY_WLAN_FREQUENCY_CL_MODE_5_2] as? Bool,
          let enable5_8 = params[KEY_WLAN_FREQUENCY_CL_MODE_5_8] as? Bool
    else { return nil }
    return ThetaRepository.WlanFrequencyClMode(
        enable2_4: enable2_4,
        enable5_2: enable5_2,
        enable5_8: enable5_8
    )
}

func toMobileNetworkSetting(params: [String: Any]) -> ThetaRepository.MobileNetworkSetting {
    var roaming: ThetaRepository.RoamingEnum? = nil
    if let name = params[KEY_ROAMING] as? String {
        roaming = getEnumValue(
            values: ThetaRepository.RoamingEnum.values(), name: name
        )
    }

    var plan: ThetaRepository.PlanEnum? = nil
    if let name = params[KEY_PLAN] as? String {
        plan = getEnumValue(
            values: ThetaRepository.PlanEnum.values(), name: name
        )
    }

    return ThetaRepository.MobileNetworkSetting(
        roaming: roaming,
        plan: plan
    )
}
