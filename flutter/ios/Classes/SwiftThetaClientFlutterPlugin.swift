import Flutter
import THETAClient
import UIKit

let EVENT_NOTIFY = "theta_client_flutter/theta_notify"
let NOTIFY_LIVE_PREVIEW = 10001
let NOTIFY_TIME_SHIFT_PROGRESS = 10011
let NOTIFY_TIME_SHIFT_STOP_ERROR = 10012
let NOTIFY_TIME_SHIFT_CAPTURING = 10013
let NOTIFY_LIMITLESS_INTERVAL_CAPTURE_STOP_ERROR = 10004
let NOTIFY_LIMITLESS_INTERVAL_CAPTURE_CAPTURING = 10005
let NOTIFY_SHOT_COUNT_SPECIFIED_INTERVAL_CAPTURE_PROGRESS = 10021
let NOTIFY_SHOT_COUNT_SPECIFIED_INTERVAL_CAPTURE_STOP_ERROR = 10022
let NOTIFY_SHOT_COUNT_SPECIFIED_INTERVAL_CAPTURE_CAPTURING = 10023
let NOTIFY_COMPOSITE_INTERVAL_PROGRESS = 10031
let NOTIFY_COMPOSITE_INTERVAL_STOP_ERROR = 10032
let NOTIFY_COMPOSITE_INTERVAL_CAPTURING = 10033
let NOTIFY_MULTI_BRACKET_INTERVAL_PROGRESS = 10041
let NOTIFY_MULTI_BRACKET_INTERVAL_STOP_ERROR = 10042
let NOTIFY_MULTI_BRACKET_INTERVAL_CAPTURING = 10043
let NOTIFY_BURST_PROGRESS = 10051
let NOTIFY_BURST_STOP_ERROR = 10052
let NOTIFY_BURST_CAPTURING = 10053
let NOTIFY_CONTINUOUS_PROGRESS = 10061
let NOTIFY_CONTINUOUS_CAPTURING = 10062
let NOTIFY_PHOTO_CAPTURING = 10071
let NOTIFY_VIDEO_CAPTURE_STOP_ERROR = 10081
let NOTIFY_VIDEO_CAPTURE_CAPTURING = 10082
let NOTIFY_VIDEO_CAPTURE_STARTED = 10083
let NOTIFY_CONVERT_VIDEO_FORMATS_PROGRESS = 10091

public class SwiftThetaClientFlutterPlugin: NSObject, FlutterPlugin, FlutterStreamHandler {
    public func onListen(withArguments _: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        eventSink = events
        return nil
    }

    public func onCancel(withArguments _: Any?) -> FlutterError? {
        print("onCancel")
        return nil
    }

    var thetaRepository: ThetaRepository? = nil
    static let errorCode: String = "Error"
    static let messageNotInit: String = "Not initialized."
    static let messageNoResult: String = "Result is Null."
    static let messageNoArgument: String = "No Argument."
    static let messageLivePreviewRunning = "Live preview is running."
    static var endPoint: String = "http://192.168.1.1"
    var eventSink: FlutterEventSink? = nil
    var previewing = false

    var photoCaptureBuilder: PhotoCapture.Builder? = nil
    var photoCapture: PhotoCapture? = nil
    var timeShiftCaptureBuilder: TimeShiftCapture.Builder? = nil
    var timeShiftCapture: TimeShiftCapture? = nil
    var timeShiftCapturing: TimeShiftCapturing? = nil
    var videoCaptureBuilder: VideoCapture.Builder? = nil
    var videoCapture: VideoCapture? = nil
    var videoCapturing: VideoCapturing? = nil
    var limitlessIntervalCaptureBuilder: LimitlessIntervalCapture.Builder? = nil
    var limitlessIntervalCapture: LimitlessIntervalCapture? = nil
    var limitlessIntervalCapturing: LimitlessIntervalCapturing? = nil
    var shotCountSpecifiedIntervalCaptureBuilder: ShotCountSpecifiedIntervalCapture.Builder? = nil
    var shotCountSpecifiedIntervalCapture: ShotCountSpecifiedIntervalCapture? = nil
    var shotCountSpecifiedIntervalCapturing: ShotCountSpecifiedIntervalCapturing? = nil
    var compositeIntervalCaptureBuilder: CompositeIntervalCapture.Builder? = nil
    var compositeIntervalCapture: CompositeIntervalCapture? = nil
    var compositeIntervalCapturing: CompositeIntervalCapturing? = nil
    var burstCaptureBuilder: BurstCapture.Builder? = nil
    var burstCapture: BurstCapture? = nil
    var burstCapturing: BurstCapturing? = nil
    var multiBracketCaptureBuilder: MultiBracketCapture.Builder? = nil
    var multiBracketCapture: MultiBracketCapture? = nil
    var multiBracketCapturing: MultiBracketCapturing? = nil
    var continuousCaptureBuilder: ContinuousCapture.Builder? = nil
    var continuousCapture: ContinuousCapture? = nil

    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "theta_client_flutter", binaryMessenger: registrar.messenger())
        let instance = SwiftThetaClientFlutterPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)

        let eventChannel = FlutterEventChannel(name: EVENT_NOTIFY, binaryMessenger: registrar.messenger())
        eventChannel.setStreamHandler(instance)
    }

    func sendNotifyEvent(id: Int, params: [String: Any]?) {
        if let eventSink = eventSink {
            DispatchQueue.main.async {
                eventSink(toNotify(id: id, params: params))
            }
        }
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        switch call.method {
        case "getPlatformVersion":
            result("iOS " + UIDevice.current.systemVersion)
        case "initialize":
            Task.detached {
                try await self.initialize(call: call, result: result)
            }
        case "isInitialized":
            result(thetaRepository != nil)
        case "restoreSettings":
            restoreSettings(result: result)
        case "getThetaModel":
            getThetaModel(result: result)
        case "getThetaInfo":
            getThetaInfo(result: result)
        case "getThetaLicense":
            getThetaLicense(result: result)
        case "getThetaState":
            getThetaState(result: result)
        case "getLivePreview":
            getLivePreview(result: result)
        case "stopLivePreview":
            stopLivePreview(result: result)
        case "listFiles":
            listFiles(call: call, result: result)
        case "deleteFiles":
            deleteFiles(call: call, result: result)
        case "deleteAllFiles":
            deleteAllFiles(result: result)
        case "deleteAllImageFiles":
            deleteAllImageFiles(result: result)
        case "deleteAllVideoFiles":
            deleteAllVideoFiles(result: result)
        case "getPhotoCaptureBuilder":
            getPhotoCaptureBuilder(result: result)
        case "buildPhotoCapture":
            buildPhotoCapture(call: call, result: result)
        case "takePicture":
            takePicture(result: result)
        case "getTimeShiftCaptureBuilder":
            getTimeShiftCaptureBuilder(result: result)
        case "buildTimeShiftCapture":
            buildTimeShiftCapture(call: call, result: result)
        case "startTimeShiftCapture":
            startTimeShiftCapture(result: result)
        case "stopTimeShiftCapture":
            stopTimeShiftCapture(result: result)
        case "getVideoCaptureBuilder":
            getVideoCaptureBuilder(result: result)
        case "buildVideoCapture":
            buildVideoCapture(call: call, result: result)
        case "startVideoCapture":
            startVideoCapture(result: result)
        case "stopVideoCapture":
            stopVideoCapture(result: result)
        case "getLimitlessIntervalCaptureBuilder":
            getLimitlessIntervalCaptureBuilder(result: result)
        case "buildLimitlessIntervalCapture":
            buildLimitlessIntervalCapture(call: call, result: result)
        case "startLimitlessIntervalCapture":
            startLimitlessIntervalCapture(result: result)
        case "stopLimitlessIntervalCapture":
            stopLimitlessIntervalCapture(result: result)
        case "getShotCountSpecifiedIntervalCaptureBuilder":
            getShotCountSpecifiedIntervalCaptureBuilder(call: call, result: result)
        case "buildShotCountSpecifiedIntervalCapture":
            buildShotCountSpecifiedIntervalCapture(call: call, result: result)
        case "startShotCountSpecifiedIntervalCapture":
            startShotCountSpecifiedIntervalCapture(result: result)
        case "stopShotCountSpecifiedIntervalCapture":
            stopShotCountSpecifiedIntervalCapture(result: result)
        case "getCompositeIntervalCaptureBuilder":
            getCompositeIntervalCaptureBuilder(call: call, result: result)
        case "buildCompositeIntervalCapture":
            buildCompositeIntervalCapture(call: call, result: result)
        case "startCompositeIntervalCapture":
            startCompositeIntervalCapture(result: result)
        case "stopCompositeIntervalCapture":
            stopCompositeIntervalCapture(result: result)
        case "getBurstCaptureBuilder":
            getBurstCaptureBuilder(call: call, result: result)
        case "buildBurstCapture":
            buildBurstCapture(call: call, result: result)
        case "startBurstCapture":
            startBurstCapture(result: result)
        case "stopBurstCapture":
            stopBurstCapture(result: result)
        case "getMultiBracketCaptureBuilder":
            getMultiBracketCaptureBuilder(call: call, result: result)
        case "buildMultiBracketCapture":
            buildMultiBracketCapture(call: call, result: result)
        case "startMultiBracketCapture":
            startMultiBracketCapture(result: result)
        case "stopMultiBracketCapture":
            stopMultiBracketCapture(result: result)
        case "getContinuousCaptureBuilder":
            getContinuousCaptureBuilder(result: result)
        case "buildContinuousCapture":
            buildContinuousCapture(call: call, result: result)
        case "startContinuousCapture":
            startContinuousCapture(result: result)
        case "getOptions":
            getOptions(call: call, result: result)
        case "setOptions":
            setOptions(call: call, result: result)
        case "getMetadata":
            getMetadata(call: call, result: result)
        case "reset":
            reset(result: result)
        case "stopSelfTimer":
            stopSelfTimer(result: result)
        case "convertVideoFormats":
            convertVideoFormats(call: call, result: result)
        case "cancelVideoConvert":
            cancelVideoConvert(result: result)
        case "finishWlan":
            finishWlan(result: result)
        case "listAccessPoints":
            listAccessPoints(result: result)
        case "setAccessPointDynamically":
            setAccessPointDynamically(call: call, result: result)
        case "setAccessPointStatically":
            setAccessPointStatically(call: call, result: result)
        case "deleteAccessPoint":
            deleteAccessPoint(call: call, result: result)
        case "getMySetting":
            getMySetting(call: call, result: result)
        case "getMySettingFromOldModel":
            getMySettingFromOldModel(call: call, result: result)
        case "setMySetting":
            setMySetting(call: call, result: result)
        case "deleteMySetting":
            deleteMySetting(call: call, result: result)
        case "listPlugins":
            listPlugins(result: result)
        case "setPlugin":
            setPlugin(call: call, result: result)
        case "startPlugin":
            startPlugin(call: call, result: result)
        case "stopPlugin":
            stopPlugin(result: result)
        case "getPluginLicense":
            getPluginLicense(call: call, result: result)
        case "getPluginOrders":
            getPluginOrders(result: result)
        case "setPluginOrders":
            setPluginOrders(call: call, result: result)
        case "setBluetoothDevice":
            setBluetoothDevice(call: call, result: result)
        default:
            result("Error. no method: " + call.method)
        }
    }

    func initialize(call: FlutterMethodCall, result: @escaping FlutterResult) async throws {
        thetaRepository = nil
        photoCaptureBuilder = nil
        photoCapture = nil
        timeShiftCaptureBuilder = nil
        timeShiftCapture = nil
        timeShiftCapturing = nil
        videoCaptureBuilder = nil
        videoCapture = nil
        videoCapturing = nil
        limitlessIntervalCaptureBuilder = nil
        limitlessIntervalCapture = nil
        limitlessIntervalCapturing = nil
        shotCountSpecifiedIntervalCaptureBuilder = nil
        shotCountSpecifiedIntervalCapture = nil
        shotCountSpecifiedIntervalCapturing = nil
        compositeIntervalCaptureBuilder = nil
        compositeIntervalCapture = nil
        compositeIntervalCapturing = nil
        previewing = false

        thetaRepository = try await withCheckedThrowingContinuation { continuation in
            let arguments = call.arguments as! [String: Any]
            Self.endPoint = arguments["endpoint"] as! String
            let config: ThetaRepository.Config? = {
                if let configParam = arguments["config"] as? [String: Any] {
                    return toConfig(params: configParam)
                }
                return nil
            }()
            let timeout: ThetaRepository.Timeout? = {
                if let configParam = arguments["timeout"] as? [String: Any] {
                    return toTimeout(params: configParam)
                }
                return nil
            }()
            ThetaRepository.Companion.shared.doNewInstance(
                endpoint: Self.endPoint,
                config: config,
                timeout: timeout
            ) { resp, error in
                if let response = resp {
                    continuation.resume(returning: response)
                    result(nil)
                } else if let thetaError = error {
                    continuation.resume(throwing: thetaError)
                    let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                    result(flutterError)
                }
            }
        }
    }

    func restoreSettings(result: @escaping FlutterResult) {
        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        thetaRepository!.restoreSettings { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        }
    }

    func getThetaModel(result: @escaping FlutterResult) {
        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        result(thetaRepository?.cameraModel?.name)
    }

    func getThetaInfo(result: @escaping FlutterResult) {
        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        thetaRepository!.getThetaInfo { response, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                var resultInfo = convertResult(thetaInfo: response!)
                if let thetaModel = self.thetaRepository?.cameraModel {
                    resultInfo["thetaModel"] = thetaModel.name
                }
                result(resultInfo)
            }
        }
    }

    func getThetaLicense(result: @escaping FlutterResult) {
        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        thetaRepository!.getThetaLicense(completionHandler: { response, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                if let response {
                    result(response)
                } else {
                    let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoResult, details: nil)
                    result(flutterError)
                }
            }
        })
    }

    func getThetaState(result: @escaping FlutterResult) {
        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        thetaRepository!.getThetaState { response, error in
            if let error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: error.localizedDescription, details: nil)
                result(flutterError)
            } else {
                let resultState = convertResult(thetaState: response!)
                result(resultState)
            }
        }
    }

    func getLivePreview(result: @escaping FlutterResult) {
        if previewing {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageLivePreviewRunning, details: nil)
            result(flutterError)
            return
        }

        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }

        class FrameHandler: KotlinSuspendFunction1 {
            weak var plugin: SwiftThetaClientFlutterPlugin?
            init(plugin: SwiftThetaClientFlutterPlugin) {
                self.plugin = plugin
            }

            func invoke(p1: Any?) async throws -> Any? {
                if let frameData = p1 as? KotlinPair<KotlinByteArray, KotlinInt> {
                    let nsData = PlatformKt.frameFrom(
                        packet: frameData
                    )
                    let data = FlutterStandardTypedData(bytes: nsData)
                    plugin?.sendNotifyEvent(id: NOTIFY_LIVE_PREVIEW, params: toPreviewNotifyParam(imageData: data))
                }
                return plugin?.previewing
            }
        }

        previewing = true
        thetaRepository!.getLivePreview(frameHandler: FrameHandler(plugin: self)) { error in
            self.previewing = false
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        }
    }
    
    func stopLivePreview(result: @escaping FlutterResult) {
        previewing = false
        result(true)
    }

    func listFiles(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let thetaRepository = thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }

        guard let arguments = call.arguments as? [String: Any],
              let fileTypeName = arguments["fileType"] as? String,
              let fileType = getEnumValue(values: ThetaRepository.FileTypeEnum.values(), name: fileTypeName),
              let startPosition: Int32 = arguments["startPosition"] as? Int32,
              let entryCount: Int32 = arguments["entryCount"] as? Int32
        else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoArgument, details: nil)
            result(flutterError)
            return
        }
        let storageName = arguments["storage"] as? String ?? ""
        let storage = getEnumValue(values: ThetaRepository.StorageEnum.values(), name: storageName)
        thetaRepository.listFiles(
            fileType: fileType,
            startPosition: startPosition,
            entryCount: entryCount,
            storage: storage
        ) { files, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                let resultList = convertResult(fileInfoList: files!.fileList)
                let resultMap = ["fileList": resultList, "totalEntries": files!.totalEntries]
                result(resultMap)
            }
        }
    }

    func deleteFiles(call: FlutterMethodCall, result: @escaping FlutterResult) {
        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        let arguments = call.arguments as! [String]
        thetaRepository!.deleteFiles(fileUrls: arguments) { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        }
    }

    func deleteAllFiles(result: @escaping FlutterResult) {
        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        thetaRepository!.deleteAllFiles { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        }
    }

    func deleteAllImageFiles(result: @escaping FlutterResult) {
        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        thetaRepository!.deleteAllImageFiles { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        }
    }

    func deleteAllVideoFiles(result: @escaping FlutterResult) {
        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        thetaRepository!.deleteAllVideoFiles { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        }
    }

    func getPhotoCaptureBuilder(result: @escaping FlutterResult) {
        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        photoCaptureBuilder = thetaRepository!.getPhotoCaptureBuilder()
        result(nil)
    }

    func buildPhotoCapture(call: FlutterMethodCall, result: @escaping FlutterResult) {
        if thetaRepository == nil || photoCaptureBuilder == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        let arguments = call.arguments as! [String: Any]
        setCaptureBuilderParams(params: arguments, builder: photoCaptureBuilder!)
        setPhotoCaptureBuilderParams(params: arguments, builder: photoCaptureBuilder!)
        photoCaptureBuilder!.build(completionHandler: { capture, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                self.photoCapture = capture
                result(nil)
            }
        })
    }

    func takePicture(result: @escaping FlutterResult) {
        if thetaRepository == nil || photoCapture == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }

        class Callback: PhotoCaptureTakePictureCallback {
            let callback: (_ url: String?, _ error: Error?) -> Void
            weak var plugin: SwiftThetaClientFlutterPlugin?
            init(_ callback: @escaping (_ url: String?, _ error: Error?) -> Void, plugin: SwiftThetaClientFlutterPlugin) {
                self.callback = callback
                self.plugin = plugin
            }

            func onSuccess(fileUrl: String?) {
                callback(fileUrl, nil)
            }

            func onCapturing(status: CapturingStatusEnum) {
                plugin?.sendNotifyEvent(id: NOTIFY_PHOTO_CAPTURING, params: toCapturingNotifyParam(value: status))
            }

            func onError(exception: ThetaRepository.ThetaRepositoryException) {
                callback(nil, exception.asError())
            }
        }
        photoCapture!.takePicture(
            callback: Callback({ fileUrl, error in
                if let thetaError = error {
                    let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                    result(flutterError)
                } else {
                    result(fileUrl)
                }
            }, plugin: self)
        )
    }

    func getTimeShiftCaptureBuilder(result: @escaping FlutterResult) {
        guard let thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        timeShiftCaptureBuilder = thetaRepository.getTimeShiftCaptureBuilder()
        result(nil)
    }

    func buildTimeShiftCapture(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let _ = thetaRepository, let builder = timeShiftCaptureBuilder else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        let arguments = call.arguments as! [String: Any]
        setCaptureBuilderParams(params: arguments, builder: builder)
        setTimeShiftCaptureBuilderParams(params: arguments, builder: builder)
        builder.build(completionHandler: { capture, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                self.timeShiftCapture = capture
                result(nil)
            }
        })
    }

    func startTimeShiftCapture(result: @escaping FlutterResult) {
        guard let _ = thetaRepository, let capture = timeShiftCapture else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        class Callback: TimeShiftCaptureStartCaptureCallback {
            let callback: (_ url: String?, _ error: Error?) -> Void
            weak var plugin: SwiftThetaClientFlutterPlugin?
            init(_ callback: @escaping (_ url: String?, _ error: Error?) -> Void, plugin: SwiftThetaClientFlutterPlugin) {
                self.callback = callback
                self.plugin = plugin
            }

            func onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                callback(nil, exception.asError())
            }

            func onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                let error = exception.asError()
                plugin?.sendNotifyEvent(id: NOTIFY_TIME_SHIFT_STOP_ERROR, params: toMessageNotifyParam(message: error.localizedDescription))
            }

            func onProgress(completion: Float) {
                plugin?.sendNotifyEvent(id: NOTIFY_TIME_SHIFT_PROGRESS, params: toCaptureProgressNotifyParam(value: completion))
            }
            
            func onCapturing(status: CapturingStatusEnum) {
                plugin?.sendNotifyEvent(id: NOTIFY_TIME_SHIFT_CAPTURING, params: toCapturingNotifyParam(value: status))
            }
            
            func onCaptureCompleted(fileUrl: String?) {
                callback(fileUrl, nil)
            }
        }

        timeShiftCapturing = capture.startCapture(
            callback: Callback({ fileUrl, error in
                                   if let thetaError = error {
                                       let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                                       result(flutterError)
                                   } else {
                                       result(fileUrl)
                                   }
                               },
                               plugin: self)
        )
    }

    func stopTimeShiftCapture(result: @escaping FlutterResult) {
        guard let _ = thetaRepository, let capturing = timeShiftCapturing else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        capturing.stopCapture()
        result(nil)
    }

    func getVideoCaptureBuilder(result: @escaping FlutterResult) {
        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        videoCaptureBuilder = thetaRepository!.getVideoCaptureBuilder()
        result(nil)
    }

    func buildVideoCapture(call: FlutterMethodCall, result: @escaping FlutterResult) {
        if thetaRepository == nil || videoCaptureBuilder == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        let arguments = call.arguments as! [String: Any]
        setCaptureBuilderParams(params: arguments, builder: videoCaptureBuilder!)
        setVideoCaptureBuilderParams(params: arguments, builder: videoCaptureBuilder!)
        videoCaptureBuilder!.build(completionHandler: { capture, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                self.videoCapture = capture
                result(nil)
            }
        })
    }

    func startVideoCapture(result: @escaping FlutterResult) {
        if thetaRepository == nil || videoCapture == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }

        class Callback: VideoCaptureStartCaptureCallback {
            let callback: (_ url: String?, _ error: Error?) -> Void
            weak var plugin: SwiftThetaClientFlutterPlugin?
            init(_ callback: @escaping (_ url: String?, _ error: Error?) -> Void, plugin: SwiftThetaClientFlutterPlugin) {
                self.callback = callback
                self.plugin = plugin
            }

            func onCaptureCompleted(fileUrl: String?) {
                callback(fileUrl, nil)
            }

            func onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                callback(nil, exception.asError())
            }

            func onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                let error = exception.asError()
                plugin?.sendNotifyEvent(id: NOTIFY_VIDEO_CAPTURE_STOP_ERROR, params: toMessageNotifyParam(message: error.localizedDescription))
            }
            
            func onCapturing(status: CapturingStatusEnum) {
                plugin?.sendNotifyEvent(id: NOTIFY_VIDEO_CAPTURE_CAPTURING, params: toCapturingNotifyParam(value: status))
            }
            
            func onCaptureStarted(fileUrl: String?) {
                plugin?.sendNotifyEvent(id: NOTIFY_VIDEO_CAPTURE_STARTED, params: toStartedNotifyParam(value: fileUrl ?? ""))
            }
        }
        videoCapturing = videoCapture!.startCapture(
            callback: Callback({ fileUrl, error in
                                   if let thetaError = error {
                                       let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                                       result(flutterError)
                                   } else {
                                       result(fileUrl)
                                   }
                               },
                               plugin: self)
        )
    }

    func stopVideoCapture(result: @escaping FlutterResult) {
        if thetaRepository == nil || videoCapturing == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        videoCapturing!.stopCapture()
        result(nil)
    }

    func getLimitlessIntervalCaptureBuilder(result: @escaping FlutterResult) {
        guard let thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        limitlessIntervalCaptureBuilder = thetaRepository.getLimitlessIntervalCaptureBuilder()
        result(nil)
    }

    func buildLimitlessIntervalCapture(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let thetaRepository, let limitlessIntervalCaptureBuilder else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        if let arguments = call.arguments as? [String: Any] {
            setCaptureBuilderParams(params: arguments, builder: limitlessIntervalCaptureBuilder)
            setLimitlessIntervalCaptureBuilderParams(params: arguments, builder: limitlessIntervalCaptureBuilder)
        }
        limitlessIntervalCaptureBuilder.build(completionHandler: { capture, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                self.limitlessIntervalCapture = capture
                result(nil)
            }
        })
    }

    func startLimitlessIntervalCapture(result: @escaping FlutterResult) {
        guard let thetaRepository, let limitlessIntervalCapture else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }

        class Callback: LimitlessIntervalCaptureStartCaptureCallback {
            let callback: (_ urls: [String]?, _ error: Error?) -> Void
            weak var plugin: SwiftThetaClientFlutterPlugin?
            init(_ callback: @escaping (_ urls: [String]?, _ error: Error?) -> Void, plugin: SwiftThetaClientFlutterPlugin) {
                self.callback = callback
                self.plugin = plugin
            }

            func onCaptureCompleted(fileUrls: [String]?) {
                callback(fileUrls, nil)
            }

            func onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                callback(nil, exception.asError())
            }

            func onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                let error = exception.asError()
                plugin?.sendNotifyEvent(id: NOTIFY_LIMITLESS_INTERVAL_CAPTURE_STOP_ERROR, params: toMessageNotifyParam(message: error.localizedDescription))
            }

            func onCapturing(status: CapturingStatusEnum) {
                plugin?.sendNotifyEvent(id: NOTIFY_LIMITLESS_INTERVAL_CAPTURE_CAPTURING, params: toCapturingNotifyParam(value: status))
            }
        }
        limitlessIntervalCapturing = limitlessIntervalCapture.startCapture(
            callback: Callback({ urls, error in
                                   if let error {
                                       let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: error.localizedDescription, details: nil)
                                       result(flutterError)
                                   } else {
                                       result(urls)
                                   }
                               },
                               plugin: self)
        )
    }

    func stopLimitlessIntervalCapture(result: @escaping FlutterResult) {
        guard let thetaRepository, let limitlessIntervalCapturing else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        limitlessIntervalCapturing.stopCapture()
        result(nil)
    }

    func getShotCountSpecifiedIntervalCaptureBuilder(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        if let shotCount = call.arguments as? Int32 {
            shotCountSpecifiedIntervalCaptureBuilder = thetaRepository.getShotCountSpecifiedIntervalCaptureBuilder(shotCount: shotCount)
        }
        result(nil)
    }

    func buildShotCountSpecifiedIntervalCapture(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let _ = thetaRepository, let builder = shotCountSpecifiedIntervalCaptureBuilder else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        if let arguments = call.arguments as? [String: Any] {
            setCaptureBuilderParams(params: arguments, builder: builder)
            setShotCountSpecifiedIntervalCaptureBuilderParams(params: arguments, builder: builder)
        }
        builder.build(completionHandler: { capture, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                self.shotCountSpecifiedIntervalCapture = capture
                result(nil)
            }
        })
    }

    func startShotCountSpecifiedIntervalCapture(result: @escaping FlutterResult) {
        guard let _ = thetaRepository, let capture = shotCountSpecifiedIntervalCapture else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        class Callback: ShotCountSpecifiedIntervalCaptureStartCaptureCallback {
            let callback: (_ urls: [String]?, _ error: Error?) -> Void
            weak var plugin: SwiftThetaClientFlutterPlugin?
            init(_ callback: @escaping (_ urls: [String]?, _ error: Error?) -> Void, plugin: SwiftThetaClientFlutterPlugin) {
                self.callback = callback
                self.plugin = plugin
            }

            func onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                callback(nil, exception.asError())
            }

            func onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                let error = exception.asError()
                plugin?.sendNotifyEvent(id: NOTIFY_SHOT_COUNT_SPECIFIED_INTERVAL_CAPTURE_STOP_ERROR, params: toMessageNotifyParam(message: error.localizedDescription))
            }

            func onProgress(completion: Float) {
                plugin?.sendNotifyEvent(id: NOTIFY_SHOT_COUNT_SPECIFIED_INTERVAL_CAPTURE_PROGRESS, params: toCaptureProgressNotifyParam(value: completion))
            }
            
            func onCapturing(status: CapturingStatusEnum) {
                plugin?.sendNotifyEvent(id: NOTIFY_SHOT_COUNT_SPECIFIED_INTERVAL_CAPTURE_CAPTURING, params: toCapturingNotifyParam(value: status))
            }
            
            func onCaptureCompleted(fileUrls: [String]?) {
                callback(fileUrls, nil)
            }
        }

        shotCountSpecifiedIntervalCapturing = capture.startCapture(
            callback: Callback({ fileUrl, error in
                                   if let thetaError = error {
                                       let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                                       result(flutterError)
                                   } else {
                                       result(fileUrl)
                                   }
                               },
                               plugin: self)
        )
    }

    func stopShotCountSpecifiedIntervalCapture(result: @escaping FlutterResult) {
        guard let _ = thetaRepository, let capturing = shotCountSpecifiedIntervalCapturing else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        capturing.stopCapture()
        result(nil)
    }

    func getCompositeIntervalCaptureBuilder(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        if let shootingTimeSec = call.arguments as? Int32 {
            compositeIntervalCaptureBuilder = thetaRepository.getCompositeIntervalCaptureBuilder(shootingTimeSec: shootingTimeSec)
        }
        result(nil)
    }

    func buildCompositeIntervalCapture(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let _ = thetaRepository, let builder = compositeIntervalCaptureBuilder else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        if let arguments = call.arguments as? [String: Any] {
            setCaptureBuilderParams(params: arguments, builder: builder)
            setCompositeIntervalCaptureBuilderParams(params: arguments, builder: builder)
        }
        builder.build(completionHandler: { capture, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                self.compositeIntervalCapture = capture
                result(nil)
            }
        })
    }

    func startCompositeIntervalCapture(result: @escaping FlutterResult) {
        guard let _ = thetaRepository, let capture = compositeIntervalCapture else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        class Callback: CompositeIntervalCaptureStartCaptureCallback {
            let callback: (_ urls: [String]?, _ error: Error?) -> Void
            weak var plugin: SwiftThetaClientFlutterPlugin?
            init(_ callback: @escaping (_ urls: [String]?, _ error: Error?) -> Void, plugin: SwiftThetaClientFlutterPlugin) {
                self.callback = callback
                self.plugin = plugin
            }

            func onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                callback(nil, exception.asError())
            }

            func onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                let error = exception.asError()
                plugin?.sendNotifyEvent(id: NOTIFY_COMPOSITE_INTERVAL_STOP_ERROR, params: toMessageNotifyParam(message: error.localizedDescription))
            }

            func onProgress(completion: Float) {
                plugin?.sendNotifyEvent(id: NOTIFY_COMPOSITE_INTERVAL_PROGRESS, params: toCaptureProgressNotifyParam(value: completion))
            }
            
            func onCapturing(status: CapturingStatusEnum) {
                plugin?.sendNotifyEvent(id: NOTIFY_COMPOSITE_INTERVAL_CAPTURING, params: toCapturingNotifyParam(value: status))
            }
            
            func onCaptureCompleted(fileUrls: [String]?) {
                callback(fileUrls, nil)
            }
        }

        compositeIntervalCapturing = capture.startCapture(
            callback: Callback({ fileUrl, error in
                                   if let thetaError = error {
                                       let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                                       result(flutterError)
                                   } else {
                                       result(fileUrl)
                                   }
                               },
                               plugin: self)
        )
    }

    func stopCompositeIntervalCapture(result: @escaping FlutterResult) {
        guard let _ = thetaRepository, let capturing = compositeIntervalCapturing else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        capturing.stopCapture()
        result(nil)
    }

    // MARK: - BurstCapture

    func getBurstCaptureBuilder(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        guard
            let arguments = call.arguments as? [String: Any],
            let burstCaptureNum = arguments["burstCaptureNum"] as? String,
            let burstCaptureNumVal = getEnumValue(values: ThetaRepository.BurstCaptureNumEnum.values(), name: burstCaptureNum),
            let burstBracketStep = arguments["burstBracketStep"] as? String,
            let burstBracketStepVal = getEnumValue(values: ThetaRepository.BurstBracketStepEnum.values(), name: burstBracketStep),
            let burstCompensation = arguments["burstCompensation"] as? String,
            let burstCompensationVal = getEnumValue(values: ThetaRepository.BurstCompensationEnum.values(), name: burstCompensation),
            let burstMaxExposureTime = arguments["burstMaxExposureTime"] as? String,
            let burstMaxExposureTimeVal = getEnumValue(values: ThetaRepository.BurstMaxExposureTimeEnum.values(), name: burstMaxExposureTime),
            let burstEnableIsoControl = arguments["burstEnableIsoControl"] as? String,
            let burstEnableIsoControlVal = getEnumValue(values: ThetaRepository.BurstEnableIsoControlEnum.values(), name: burstEnableIsoControl),
            let burstOrder = arguments["burstOrder"] as? String,
            let burstOrderVal = getEnumValue(values: ThetaRepository.BurstOrderEnum.values(), name: burstOrder)
        else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoArgument, details: nil)
            result(flutterError)
            return
        }

        burstCaptureBuilder = thetaRepository.getBurstCaptureBuilder(burstCaptureNum: burstCaptureNumVal,
                                                                     burstBracketStep: burstBracketStepVal,
                                                                     burstCompensation: burstCompensationVal,
                                                                     burstMaxExposureTime: burstMaxExposureTimeVal,
                                                                     burstEnableIsoControl: burstEnableIsoControlVal,
                                                                     burstOrder: burstOrderVal)
        result(nil)
    }

    func buildBurstCapture(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let _ = thetaRepository, let builder = burstCaptureBuilder else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        if let arguments = call.arguments as? [String: Any] {
            setCaptureBuilderParams(params: arguments, builder: builder)
            setBurstCaptureBuilderParams(params: arguments, builder: builder)
        }
        builder.build(completionHandler: { capture, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                self.burstCapture = capture
                result(nil)
            }
        })
    }

    func startBurstCapture(result: @escaping FlutterResult) {
        guard let _ = thetaRepository, let capture = burstCapture else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        class Callback: BurstCaptureStartCaptureCallback {
            let callback: (_ urls: [String]?, _ error: Error?) -> Void
            weak var plugin: SwiftThetaClientFlutterPlugin?
            init(_ callback: @escaping (_ urls: [String]?, _ error: Error?) -> Void, plugin: SwiftThetaClientFlutterPlugin) {
                self.callback = callback
                self.plugin = plugin
            }

            func onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                callback(nil, exception.asError())
            }

            func onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                let error = exception.asError()
                plugin?.sendNotifyEvent(id: NOTIFY_BURST_STOP_ERROR, params: toMessageNotifyParam(message: error.localizedDescription))
            }

            func onProgress(completion: Float) {
                plugin?.sendNotifyEvent(id: NOTIFY_BURST_PROGRESS, params: toCaptureProgressNotifyParam(value: completion))
            }

            func onCapturing(status: CapturingStatusEnum) {
                plugin?.sendNotifyEvent(id: NOTIFY_BURST_CAPTURING, params: toCapturingNotifyParam(value: status))
            }

            func onCaptureCompleted(fileUrls: [String]?) {
                callback(fileUrls, nil)
            }
        }

        burstCapturing = capture.startCapture(
            callback: Callback({ fileUrl, error in
                                   if let thetaError = error {
                                       let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                                       result(flutterError)
                                   } else {
                                       result(fileUrl)
                                   }
                               },
                               plugin: self)
        )
    }

    func stopBurstCapture(result: @escaping FlutterResult) {
        guard let _ = thetaRepository, let capturing = burstCapturing else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        capturing.stopCapture()
        result(nil)
    }

    // MARK: - MultiBracketCapture

    func getMultiBracketCaptureBuilder(call _: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        multiBracketCaptureBuilder = thetaRepository.getMultiBracketCaptureBuilder()
        result(nil)
    }

    func buildMultiBracketCapture(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let _ = thetaRepository, let builder = multiBracketCaptureBuilder else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        if let arguments = call.arguments as? [String: Any] {
            setCaptureBuilderParams(params: arguments, builder: builder)
            setMultiBracketCaptureBuilderParams(params: arguments, builder: builder)
        }
        builder.build(completionHandler: { capture, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                self.multiBracketCapture = capture
                result(nil)
            }
        })
    }

    func startMultiBracketCapture(result: @escaping FlutterResult) {
        guard let _ = thetaRepository, let capture = multiBracketCapture else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        class Callback: MultiBracketCaptureStartCaptureCallback {
            let callback: (_ urls: [String]?, _ error: Error?) -> Void
            weak var plugin: SwiftThetaClientFlutterPlugin?
            init(_ callback: @escaping (_ urls: [String]?, _ error: Error?) -> Void, plugin: SwiftThetaClientFlutterPlugin) {
                self.callback = callback
                self.plugin = plugin
            }

            func onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                callback(nil, exception.asError())
            }

            func onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                let error = exception.asError()
                plugin?.sendNotifyEvent(id: NOTIFY_MULTI_BRACKET_INTERVAL_STOP_ERROR, params: toMessageNotifyParam(message: error.localizedDescription))
            }

            func onProgress(completion: Float) {
                plugin?.sendNotifyEvent(id: NOTIFY_MULTI_BRACKET_INTERVAL_PROGRESS, params: toCaptureProgressNotifyParam(value: completion))
            }

            func onCapturing(status: CapturingStatusEnum) {
                plugin?.sendNotifyEvent(id: NOTIFY_MULTI_BRACKET_INTERVAL_CAPTURING, params: toCapturingNotifyParam(value: status))
            }

            func onCaptureCompleted(fileUrls: [String]?) {
                callback(fileUrls, nil)
            }
        }
        multiBracketCapturing = capture.startCapture(
            callback: Callback({ fileUrl, error in
                                   if let thetaError = error {
                                       let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                                       result(flutterError)
                                   } else {
                                       result(fileUrl)
                                   }
                               },
                               plugin: self)
        )
    }

    func stopMultiBracketCapture(result: @escaping FlutterResult) {
        guard let _ = thetaRepository, let capturing = multiBracketCapturing else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        capturing.stopCapture()
        result(nil)
    }

    // MARK: - ContinuousCapture

    func getContinuousCaptureBuilder(result: @escaping FlutterResult) {
        guard let thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        continuousCaptureBuilder = thetaRepository.getContinuousCaptureBuilder()
        result(nil)
    }

    func buildContinuousCapture(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let _ = thetaRepository, let builder = continuousCaptureBuilder else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        if let arguments = call.arguments as? [String: Any] {
            setCaptureBuilderParams(params: arguments, builder: builder)
            setContinuousCaptureBuilderParams(params: arguments, builder: builder)
        }
        builder.build(completionHandler: { capture, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                self.continuousCapture = capture
                result(nil)
            }
        })
    }

    func startContinuousCapture(result: @escaping FlutterResult) {
        guard let _ = thetaRepository, let capture = continuousCapture else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        class Callback: ContinuousCaptureStartCaptureCallback {
            let callback: (_ urls: [String]?, _ error: Error?) -> Void
            weak var plugin: SwiftThetaClientFlutterPlugin?
            init(_ callback: @escaping (_ urls: [String]?, _ error: Error?) -> Void, plugin: SwiftThetaClientFlutterPlugin) {
                self.callback = callback
                self.plugin = plugin
            }

            func onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                callback(nil, exception.asError())
            }

            func onProgress(completion: Float) {
                plugin?.sendNotifyEvent(id: NOTIFY_CONTINUOUS_PROGRESS, params: toCaptureProgressNotifyParam(value: completion))
            }
            
            func onCapturing(status: CapturingStatusEnum) {
                plugin?.sendNotifyEvent(id: NOTIFY_CONTINUOUS_CAPTURING, params: toCapturingNotifyParam(value: status))
            }
            
            func onCaptureCompleted(fileUrls: [String]?) {
                callback(fileUrls, nil)
            }
        }

        capture.startCapture(
            callback: Callback({ fileUrl, error in
                                   if let thetaError = error {
                                       let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                                       result(flutterError)
                                   } else {
                                       result(fileUrl)
                                   }
                               },
                               plugin: self)
        )
    }

    // MARK: -

    func getOptions(call: FlutterMethodCall, result: @escaping FlutterResult) {
        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        let arguments = call.arguments as! [String]
        let params = convertGetOptionsParam(params: arguments)
        thetaRepository!.getOptions(optionNames: params, completionHandler: { options, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(convertResult(options: options!))
            }
        })
    }

    func setOptions(call: FlutterMethodCall, result: @escaping FlutterResult) {
        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        let arguments = call.arguments as! [String: Any]
        let params = convertSetOptionsParam(params: arguments)
        thetaRepository!.setOptions(options: params, completionHandler: { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        })
    }

    func getMetadata(call: FlutterMethodCall, result: @escaping FlutterResult) {
        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        let arguments = call.arguments as! String
        thetaRepository!.getMetadata(fileUrl: arguments, completionHandler: { response, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(convertResult(metadata: response!))
            }
        })
    }

    func reset(result: @escaping FlutterResult) {
        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        thetaRepository!.reset { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        }
    }

    func stopSelfTimer(result: @escaping FlutterResult) {
        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        thetaRepository!.stopSelfTimer { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        }
    }

    func convertVideoFormats(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let thetaRepository = thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        let arguments = call.arguments as! [String: Any]
        let fileUrl = arguments["fileUrl"] as! String
        let toLowResolution = arguments["toLowResolution"] as! Bool
        let applyTopBottomCorrection = arguments["applyTopBottomCorrection"] as! Bool
        thetaRepository.convertVideoFormats(
            fileUrl: fileUrl,
            toLowResolution: toLowResolution,
            applyTopBottomCorrection: applyTopBottomCorrection
        ) { completion in
            self.sendNotifyEvent(id: NOTIFY_CONVERT_VIDEO_FORMATS_PROGRESS, params: toCaptureProgressNotifyParam(value: completion.floatValue))
        } completionHandler: { response, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(response)
            }
        }
    }

    func cancelVideoConvert(result: @escaping FlutterResult) {
        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        thetaRepository!.cancelVideoConvert { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        }
    }

    func finishWlan(result: @escaping FlutterResult) {
        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        thetaRepository!.finishWlan { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        }
    }

    func listAccessPoints(result: @escaping FlutterResult) {
        guard let thetaRepository = thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        thetaRepository.listAccessPoints { response, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                if let response = response {
                    result(convertResult(accessPointList: response))
                } else {
                    let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoResult, details: nil)
                    result(flutterError)
                }
            }
        }
    }

    func setAccessPointDynamically(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let thetaRepository = thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        guard
            let arguments = call.arguments as? [String: Any],
            let ssid = arguments["ssid"] as? String,
            let ssidStealth = arguments["ssidStealth"] as? Bool,
            let authModeName = arguments["authMode"] as? String,
            let authMode = getEnumValue(values: ThetaRepository.AuthModeEnum.values(), name: authModeName),
            let password = arguments["password"] as? String,
            let connectionPriority = arguments["connectionPriority"] as? Int32
        else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoArgument, details: nil)
            result(flutterError)
            return
        }

        var proxy: ThetaRepository.Proxy?
        if let proxyMap = arguments["proxy"] as? [String: Any] {
            proxy = toProxy(params: proxyMap)
        }

        thetaRepository.setAccessPointDynamically(
            ssid: ssid,
            ssidStealth: ssidStealth,
            authMode: authMode,
            password: password,
            connectionPriority: connectionPriority,
            proxy: proxy,
            completionHandler: { error in
                if let thetaError = error {
                    let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                    result(flutterError)
                } else {
                    result(nil)
                }
            }
        )
    }

    func setAccessPointStatically(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let thetaRepository = thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        guard
            let arguments = call.arguments as? [String: Any],
            let ssid = arguments["ssid"] as? String,
            let ssidStealth = arguments["ssidStealth"] as? Bool,
            let authModeName = arguments["authMode"] as? String,
            let authMode = getEnumValue(values: ThetaRepository.AuthModeEnum.values(), name: authModeName),
            let connectionPriority = arguments["connectionPriority"] as? Int32,
            let ipAddress = arguments["ipAddress"] as? String,
            let subnetMask = arguments["subnetMask"] as? String,
            let defaultGateway = arguments["defaultGateway"] as? String
        else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoArgument, details: nil)
            result(flutterError)
            return
        }

        let password = arguments["password"] as? String

        var proxy: ThetaRepository.Proxy?
        if let proxyMap = arguments["proxy"] as? [String: Any] {
            proxy = toProxy(params: proxyMap)
        }

        thetaRepository.setAccessPointStatically(
            ssid: ssid,
            ssidStealth: ssidStealth,
            authMode: authMode,
            password: password,
            connectionPriority: connectionPriority,
            ipAddress: ipAddress,
            subnetMask: subnetMask,
            defaultGateway: defaultGateway,
            proxy: proxy,
            completionHandler: { error in
                if let thetaError = error {
                    let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                    result(flutterError)
                } else {
                    result(nil)
                }
            }
        )
    }

    func deleteAccessPoint(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let thetaRepository = thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        guard let ssid = call.arguments as? String else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoArgument, details: nil)
            result(flutterError)
            return
        }
        thetaRepository.deleteAccessPoint(ssid: ssid, completionHandler: { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        })
    }

    func getMySetting(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let thetaRepository = thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }

        guard let arguments = call.arguments as? [String: Any] else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoArgument, details: nil)
            result(flutterError)
            return
        }

        var captureMode: ThetaRepository.CaptureModeEnum?
        if let captureModeName = arguments["captureMode"] as? String,
           let mode = getEnumValue(values: ThetaRepository.CaptureModeEnum.values(), name: captureModeName) as? ThetaRepository.CaptureModeEnum
        {
            captureMode = mode
        }

        if let captureMode = captureMode {
            thetaRepository.getMySetting(captureMode: captureMode, completionHandler: { options, error in
                if let thetaError = error {
                    let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                    result(flutterError)
                } else {
                    if let options = options {
                        result(convertResult(options: options))
                    } else {
                        let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoResult, details: nil)
                        result(flutterError)
                    }
                }
            })
        } else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoArgument, details: nil)
            result(flutterError)
        }
    }

    func getMySettingFromOldModel(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let thetaRepository = thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }

        guard let arguments = call.arguments as? [String: Any] else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoArgument, details: nil)
            result(flutterError)
            return
        }

        var optionNames: [ThetaRepository.OptionNameEnum]?
        if let optionNameStrAry = arguments["optionNames"] as? [String],
           let names = convertGetOptionsParam(params: optionNameStrAry) as? [ThetaRepository.OptionNameEnum]
        {
            optionNames = names
        }

        if let optionNames = optionNames {
            thetaRepository.getMySetting(optionNames: optionNames, completionHandler: { options, error in
                if let thetaError = error {
                    let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                    result(flutterError)
                } else {
                    result(convertResult(options: options!))
                }
            })
        } else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoArgument, details: nil)
            result(flutterError)
        }
    }

    func setMySetting(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let thetaRepository = thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }

        guard let arguments = call.arguments as? [String: Any],
              let captureModeName = arguments["captureMode"] as? String,
              let captureMode = getEnumValue(values: ThetaRepository.CaptureModeEnum.values(), name: captureModeName) as? ThetaRepository.CaptureModeEnum,
              let optionDic = arguments["options"] as? [String: Any]
        else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoArgument, details: nil)
            result(flutterError)
            return
        }

        let options = convertSetOptionsParam(params: optionDic)
        thetaRepository.setMySetting(captureMode: captureMode, options: options, completionHandler: { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        })
    }

    func deleteMySetting(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let thetaRepository = thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }

        guard let arguments = call.arguments as? [String: Any],
              let captureModeName = arguments["captureMode"] as? String,
              let captureMode = getEnumValue(values: ThetaRepository.CaptureModeEnum.values(), name: captureModeName) as? ThetaRepository.CaptureModeEnum
        else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoArgument, details: nil)
            result(flutterError)
            return
        }

        thetaRepository.deleteMySetting(captureMode: captureMode, completionHandler: { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        })
    }

    func listPlugins(result: @escaping FlutterResult) {
        guard let thetaRepository = thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }

        thetaRepository.listPlugins { response, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                if let response = response {
                    result(toPluginInfosResult(pluginInfoList: response))
                } else {
                    let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoResult, details: nil)
                    result(flutterError)
                }
            }
        }
    }

    func setPlugin(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let thetaRepository = thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }

        guard let arguments = call.arguments as? String else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoArgument, details: nil)
            result(flutterError)
            return
        }

        thetaRepository.setPlugin(packageName: arguments, completionHandler: { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        })
    }

    func startPlugin(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let thetaRepository = thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }

        let arguments = call.arguments as? String
        thetaRepository.startPlugin(packageName: arguments, completionHandler: { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        })
    }

    func stopPlugin(result: @escaping FlutterResult) {
        guard let thetaRepository = thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }

        thetaRepository.stopPlugin(completionHandler: { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        })
    }

    func getPluginLicense(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let thetaRepository = thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }

        guard let arguments = call.arguments as? String else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoArgument, details: nil)
            result(flutterError)
            return
        }

        thetaRepository.getPluginLicense(packageName: arguments, completionHandler: { response, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                if let response = response {
                    result(response)
                } else {
                    let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoResult, details: nil)
                    result(flutterError)
                }
            }
        })
    }

    func getPluginOrders(result: @escaping FlutterResult) {
        guard let thetaRepository = thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }

        thetaRepository.getPluginOrders(completionHandler: { response, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                if let response = response {
                    result(response)
                } else {
                    let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoResult, details: nil)
                    result(flutterError)
                }
            }
        })
    }

    func setPluginOrders(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let thetaRepository = thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }

        guard let arguments = call.arguments as? [String] else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoArgument, details: nil)
            result(flutterError)
            return
        }

        thetaRepository.setPluginOrders(plugins: arguments, completionHandler: { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        })
    }

    func setBluetoothDevice(call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let thetaRepository = thetaRepository else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }

        guard let arguments = call.arguments as? String else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoArgument, details: nil)
            result(flutterError)
            return
        }

        thetaRepository.setBluetoothDevice(uuid: arguments, completionHandler: { response, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                if let response = response {
                    result(response)
                } else {
                    let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoResult, details: nil)
                    result(flutterError)
                }
            }
        })
    }
}
