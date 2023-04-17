import Flutter
import UIKit
import THETAClient

public class SwiftThetaClientFlutterPlugin: NSObject, FlutterPlugin, FlutterStreamHandler {
    public func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        self.eventSink = events
        return nil
    }
    
    public func onCancel(withArguments arguments: Any?) -> FlutterError? {
        print("onCancel")
        return nil
    }
    
    var thetaRepository: ThetaRepository? = nil
    static let errorCode: String = "Error"
    static let messageNotInit: String = "Not initialized."
    static let messageNoArgument: String = "No Argument."
    static var endPoint: String = "http://192.168.1.1"
    var eventSink: FlutterEventSink? = nil
    var previewing = false
    
    var photoCaptureBuilder: PhotoCapture.Builder? = nil
    var photoCapture: PhotoCapture? = nil
    var videoCaptureBuilder: VideoCapture.Builder? = nil
    var videoCapture: VideoCapture? = nil
    var videoCapturing: VideoCapturing? = nil
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "theta_client_flutter", binaryMessenger: registrar.messenger())
        let instance = SwiftThetaClientFlutterPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
        
        let eventChannel = FlutterEventChannel(name: "theta_client_flutter/live_preview", binaryMessenger: registrar.messenger())
        eventChannel.setStreamHandler(instance)
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
            self.restoreSettings(result: result)
        case "getThetaInfo":
            self.getThetaInfo(result: result)
        case "getThetaState":
            self.getThetaState(result: result)
        case "getLivePreview":
            self.getLivePreview(result: result)
        case "stopLivePreview":
            previewing = false
        case "listFiles":
            self.listFiles(call: call, result: result)
        case "deleteFiles":
            self.deleteFiles(call: call, result: result)
        case "deleteAllFiles":
            self.deleteAllFiles(result: result)
        case "deleteAllImageFiles":
            self.deleteAllImageFiles(result: result)
        case "deleteAllVideoFiles":
            self.deleteAllVideoFiles(result: result)
        case "getPhotoCaptureBuilder":
            getPhotoCaptureBuilder(result: result)
        case "buildPhotoCapture":
            buildPhotoCapture(call: call, result: result)
        case "takePicture":
            takePicture(result: result)
        case "getVideoCaptureBuilder":
            getVideoCaptureBuilder(result: result)
        case "buildVideoCapture":
            buildVideoCapture(call: call, result: result)
        case "startVideoCapture":
            startVideoCapture(result: result)
        case "stopVideoCapture":
            stopVideoCapture(result: result)
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
        case "setMySetting":
            setMySetting(call: call, result: result)
        case "deleteMySetting":
            deleteMySetting(call: call, result: result)
        default:
            result("Error. no method: " + call.method)
        }
    }
    
    func initialize(call: FlutterMethodCall, result: @escaping FlutterResult) async throws {
        thetaRepository = try await withCheckedThrowingContinuation {continuation in
            let arguments = call.arguments as! [String : Any]
            Self.endPoint = arguments["endpoint"] as! String
            let config: ThetaRepository.Config? = {
                if let configParam = arguments["config"] as? [String : Any] {
                    return toConfig(params: configParam)
                }
                return nil
            }()
            let timeout: ThetaRepository.Timeout? = {
                if let configParam = arguments["timeout"] as? [String : Any] {
                    return toTimeout(params: configParam)
                }
                return nil
            }()
            ThetaRepository.Companion.shared.doNewInstance(
                endpoint:Self.endPoint,
                config:config,
                timeout:timeout
            ) {resp, error in
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
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        thetaRepository!.restoreSettings() { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        }
    }
    
    func getThetaInfo(result: @escaping FlutterResult) {
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        thetaRepository!.getThetaInfo() { response, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                let resultInfo = convertResult(thetaInfo: response!)
                result(resultInfo)
            }
        }
    }
    
    func getThetaState(result: @escaping FlutterResult) {
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        thetaRepository!.getThetaState() { response, error in
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
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        
        class FrameHandler: KotlinSuspendFunction1 {
            weak var plugin: SwiftThetaClientFlutterPlugin?
            init(plugin: SwiftThetaClientFlutterPlugin) {
                self.plugin = plugin
            }
            func invoke(p1: Any?, completionHandler: @escaping (Any?, Error?) -> Void) {
                let nsData = PlatformKt.frameFrom(
                    packet: p1 as! KotlinPair
                )
                let data = FlutterStandardTypedData.init(bytes: nsData)
                
                if let eventSink = plugin?.eventSink {
                    eventSink(data)
                }
                completionHandler(plugin?.previewing, nil)
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
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        let arguments = call.arguments as! [String : Any]
        let fileTypeName = arguments["fileType"] as! String
        let fileType = getEnumValue(values: ThetaRepository.FileTypeEnum.values(), name: fileTypeName)!
        let startPosition: Int32 = arguments["startPosition"] as! Int32
        let entryCount: Int32 = arguments["entryCount"] as! Int32
        thetaRepository!.listFiles(fileType: fileType, startPosition: startPosition, entryCount: entryCount) { files, error in
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
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
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
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        thetaRepository!.deleteAllFiles() { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        }
    }
    
    func deleteAllImageFiles(result: @escaping FlutterResult) {
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        thetaRepository!.deleteAllImageFiles() { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        }
    }
    
    func deleteAllVideoFiles(result: @escaping FlutterResult) {
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        thetaRepository!.deleteAllVideoFiles() { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        }
    }
    
    func getPhotoCaptureBuilder(result: @escaping FlutterResult) {
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        photoCaptureBuilder = thetaRepository!.getPhotoCaptureBuilder()
        result(nil)
    }
    
    func buildPhotoCapture(call: FlutterMethodCall, result: @escaping FlutterResult) {
        if (thetaRepository == nil || photoCaptureBuilder == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        let arguments = call.arguments as! [String : Any]
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
        if (thetaRepository == nil || photoCapture == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        
        class Callback: PhotoCaptureTakePictureCallback {
            let callback: (_ url: String?, _ error: Error?) -> Void
            init(_ callback: @escaping (_ url: String?, _ error: Error?) -> Void) {
                self.callback = callback
            }
            func onSuccess(fileUrl: String) {
                callback(fileUrl, nil)
            }
            func onError(exception: ThetaRepository.ThetaRepositoryException) {
                callback(nil, exception as? Error)
            }
        }
        photoCapture!.takePicture(
            callback: Callback {fileUrl, error in
                if let thetaError = error {
                    let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                    result(flutterError)
                } else {
                    result(fileUrl)
                }
            }
        )
    }
    
    func getVideoCaptureBuilder(result: @escaping FlutterResult) {
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        videoCaptureBuilder = thetaRepository!.getVideoCaptureBuilder()
        result(nil)
    }
    
    func buildVideoCapture(call: FlutterMethodCall, result: @escaping FlutterResult) {
        if (thetaRepository == nil || videoCaptureBuilder == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        let arguments = call.arguments as! [String : Any]
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
        if (thetaRepository == nil || videoCapture == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        
        class Callback: VideoCaptureStartCaptureCallback {
            let callback: (_ url: String?, _ error: Error?) -> Void
            init(_ callback: @escaping (_ url: String?, _ error: Error?) -> Void) {
                self.callback = callback
            }
            func onSuccess(fileUrl: String) {
                callback(fileUrl, nil)
            }
            func onError(exception: ThetaRepository.ThetaRepositoryException) {
                callback(nil, exception as? Error)
            }
        }
        videoCapturing = videoCapture!.startCapture(
            callback: Callback {fileUrl, error in
                if let thetaError = error {
                    let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                    result(flutterError)
                } else {
                    result(fileUrl)
                }
            }
        )
    }
    
    func stopVideoCapture(result: @escaping FlutterResult) {
        if (thetaRepository == nil || videoCapturing == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        videoCapturing!.stopCapture()
        result(nil)
    }
    
    func getOptions(call: FlutterMethodCall, result: @escaping FlutterResult) {
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
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
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
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
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
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
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        thetaRepository!.reset() { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        }
    }
    
    func stopSelfTimer(result: @escaping FlutterResult) {
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        thetaRepository!.stopSelfTimer() { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        }
    }
    
    func convertVideoFormats(call: FlutterMethodCall, result: @escaping FlutterResult) {
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        let arguments = call.arguments as! [String : Any]
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
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        thetaRepository!.cancelVideoConvert() { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        }
    }
    
    func finishWlan(result: @escaping FlutterResult) {
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        thetaRepository!.finishWlan() { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        }
    }
    
    func listAccessPoints(result: @escaping FlutterResult) {
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        thetaRepository!.listAccessPoints() { response, error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(convertResult(accessPointList: response!))
            }
        }
    }
    
    func setAccessPointDynamically(call: FlutterMethodCall, result: @escaping FlutterResult) {
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        let arguments = call.arguments as! [String : Any]
        let ssid = arguments["ssid"] as! String
        let ssidStealth = arguments["ssidStealth"] as! Bool
        let authModeName = arguments["authMode"] as! String
        let authMode = getEnumValue(values: ThetaRepository.AuthModeEnum.values(), name: authModeName)!
        let password = arguments["password"] as! String
        let connectionPriority = arguments["connectionPriority"] as! Int32
        thetaRepository!.setAccessPointDynamically(ssid: ssid, ssidStealth: ssidStealth, authMode: authMode, password: password, connectionPriority: connectionPriority, completionHandler: { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        })
    }
    
    func setAccessPointStatically(call: FlutterMethodCall, result: @escaping FlutterResult) {
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        let arguments = call.arguments as! [String : Any]
        let ssid = arguments["ssid"] as! String
        let ssidStealth = arguments["ssidStealth"] as! Bool
        let authModeName = arguments["authMode"] as! String
        let authMode = getEnumValue(values: ThetaRepository.AuthModeEnum.values(), name: authModeName)!
        let password = arguments["password"] as! String
        let connectionPriority = arguments["connectionPriority"] as! Int32
        let ipAddress = arguments["ipAddress"] as! String
        let subnetMask = arguments["subnetMask"] as! String
        let defaultGateway = arguments["defaultGateway"] as! String
        thetaRepository!.setAccessPointStatically(ssid: ssid, ssidStealth: ssidStealth, authMode: authMode, password: password, connectionPriority: connectionPriority, ipAddress: ipAddress, subnetMask: subnetMask, defaultGateway: defaultGateway,  completionHandler: { error in
            if let thetaError = error {
                let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                result(flutterError)
            } else {
                result(nil)
            }
        })
    }
    
    func deleteAccessPoint(call: FlutterMethodCall, result: @escaping FlutterResult) {
        if (thetaRepository == nil) {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNotInit, details: nil)
            result(flutterError)
            return;
        }
        let arguments = call.arguments as! String
        thetaRepository!.deleteAccessPoint(ssid: arguments, completionHandler: { error in
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
        
        guard let arguments = call.arguments as? [String : Any] else {
            let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: SwiftThetaClientFlutterPlugin.messageNoArgument, details: nil)
            result(flutterError)
            return
        }
        
        var captureMode: ThetaRepository.CaptureModeEnum?
        var optionNames: [ThetaRepository.OptionNameEnum]?
        if let captureModeName = arguments["captureMode"] as? String,
           let mode = getEnumValue(values: ThetaRepository.CaptureModeEnum.values(), name: captureModeName) as? ThetaRepository.CaptureModeEnum {
            captureMode = mode
        } else if let optionNameStrAry = arguments["optionNames"] as? [String],
                  let names = convertGetOptionsParam(params: optionNameStrAry) as? [ThetaRepository.OptionNameEnum] {
            optionNames = names
        }
        
        if let captureMode = captureMode {
            thetaRepository.getMySetting(captureMode: captureMode, completionHandler: { options, error in
                if let thetaError = error {
                    let flutterError = FlutterError(code: SwiftThetaClientFlutterPlugin.errorCode, message: thetaError.localizedDescription, details: nil)
                    result(flutterError)
                } else {
                    result(convertResult(options: options!))
                }
            })
        } else if let optionNames = optionNames {
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
        
        guard let arguments = call.arguments as? [String : Any],
              let captureModeName = arguments["captureMode"] as? String,
              let captureMode = getEnumValue(values: ThetaRepository.CaptureModeEnum.values(), name: captureModeName) as? ThetaRepository.CaptureModeEnum,
              let optionDic = arguments["options"] as? [String: Any] else {
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
        
        guard let arguments = call.arguments as? [String : Any],
              let captureModeName = arguments["captureMode"] as? String,
              let captureMode = getEnumValue(values: ThetaRepository.CaptureModeEnum.values(), name: captureModeName) as? ThetaRepository.CaptureModeEnum else {
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
}
