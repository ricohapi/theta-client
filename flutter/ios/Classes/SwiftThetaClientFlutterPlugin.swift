import Flutter
import THETAClient
import UIKit

let EVENT_NOTIFY = "theta_client_flutter/theta_notify"
let NOTIFY_LIVE_PREVIEW = 10001
let NOTIFY_TIME_SHIFT_PROGRESS = 10002
let NOTIFY_VIDEO_CAPTURE_STOP_ERROR = 10003
let NOTIFY_LIMITLESS_INTERVAL_CAPTURE_STOP_ERROR = 10004
let NOTIFY_SHOT_COUNT_SPECIFIED_INTERVAL_CAPTURE_PROGRESS = 10005
let NOTIFY_SHOT_COUNT_SPECIFIED_INTERVAL_CAPTURE_STOP_ERROR = 10006

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

    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "theta_client_flutter", binaryMessenger: registrar.messenger())
        let instance = SwiftThetaClientFlutterPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)

        let eventChannel = FlutterEventChannel(name: EVENT_NOTIFY, binaryMessenger: registrar.messenger())
        eventChannel.setStreamHandler(instance)
    }

    func sendNotifyEvent(id: Int, params: [String: Any]?) {
        if let eventSink = eventSink {
            eventSink(toNotify(id: id, params: params))
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
        case "getThetaState":
            getThetaState(result: result)
        case "getLivePreview":
            getLivePreview(result: result)
        case "stopLivePreview":
            previewing = false
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

    func getThetaState(result: @escaping FlutterResult) {
        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        thetaRepository!.getThetaState { response, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                let resultState = convertResult(thetaState: response!)
                result(resultState)
            }
        }
    }

    func getLivePreview(result: @escaping FlutterResult) {
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
            init(_ callback: @escaping (_ url: String?, _ error: Error?) -> Void) {
                self.callback = callback
            }

            func onSuccess(fileUrl: String?) {
                callback(fileUrl, nil)
            }

            func onProgress(completion _: Float) {}

            func onError(exception: ThetaRepository.ThetaRepositoryException) {
                callback(nil, exception.asError())
            }
        }
        photoCapture!.takePicture(
            callback: Callback { fileUrl, error in
                if let thetaError = error {
                    let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                    result(flutterError)
                } else {
                    result(fileUrl)
                }
            }
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

            func onError(exception: ThetaRepository.ThetaRepositoryException) {
                callback(nil, exception.asError())
            }

            func onProgress(completion: Float) {
                plugin?.sendNotifyEvent(id: NOTIFY_TIME_SHIFT_PROGRESS, params: toCaptureProgressNotifyParam(value: completion))
            }

            func onSuccess(fileUrl: String?) {
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
        if thetaRepository == nil {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return
        }
        let arguments = call.arguments as! [String: Any]
        let fileUrl = arguments["fileUrl"] as! String
        let toLowResolution = arguments["toLowResolution"] as! Bool
        let applyTopBottomCorrection = arguments["applyTopBottomCorrection"] as! Bool
        thetaRepository!.convertVideoFormats(fileUrl: fileUrl, toLowResolution: toLowResolution, applyTopBottomCorrection: applyTopBottomCorrection) { response, error in
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
