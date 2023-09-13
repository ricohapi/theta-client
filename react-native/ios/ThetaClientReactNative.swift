import THETAClient

let ERROR_CODE_ERROR = "error"
let MESSAGE_NOT_INIT = "Not initialized."
let MESSAGE_NO_RESULT = "No result."
let MESSAGE_NO_ARGUMENT = "No Argument."
let MESSAGE_NO_PHOTO_CAPTURE = "No photoCapture."
let MESSAGE_NO_PHOTO_CAPTURE_BUILDER = "no photo capture builder."
let MESSAGE_NO_TIMESHIFT_CAPTURE = "No timeShiftCapture."
let MESSAGE_NO_TIMESHIFT_CAPTURE_BUILDER = "no time-shift capture builder."
let MESSAGE_NO_TIMESHIFT_CAPTURING = "no timeShiftCapturing."
let MESSAGE_NO_VIDEO_CAPTURE = "No videoCapture."
let MESSAGE_NO_VIDEO_CAPTURE_BUILDER = "no video capture builder."
let MESSAGE_NO_VIDEO_CAPTURING = "no videoCapturing."

@objc(ThetaClientReactNative)
class ThetaClientReactNative: RCTEventEmitter {
    var thetaRepository: ThetaRepository?
    var previewing = false
    var photoCaptureBuilder: PhotoCapture.Builder?
    var photoCapture: PhotoCapture?
    var timeShiftCaptureBuilder: TimeShiftCapture.Builder?
    var timeShiftCapture: TimeShiftCapture?
    var timeShiftCapturing: TimeShiftCapturing?
    var videoCaptureBuilder: VideoCapture.Builder?
    var videoCapture: VideoCapture?
    var videoCapturing: VideoCapturing?

    static let EVENT_FRAME = "ThetaFrameEvent"
    static let EVENT_NOTIFY = "ThetaNotify"

    static let NOTIFY_TIMESHIFT_PROGRESS = "TIME-SHIFT-PROGRESS"
    static let NOTIFY_VIDEO_CAPTURE_STOP_ERROR = "VIDEO-CAPTURE-STOP-ERROR"

    @objc
    override func supportedEvents() -> [String]! {
        return [ThetaClientReactNative.EVENT_FRAME, ThetaClientReactNative.EVENT_NOTIFY]
    }

    @objc
    override static func requiresMainQueueSetup() -> Bool {
        return true
    }

    @objc
    override func constantsToExport() -> [AnyHashable: Any]! {
        return [
            "DEFAULT_EVENT_NAME": ThetaClientReactNative.EVENT_FRAME,
        ]
    }

    @objc(initialize:withConfig:withTimeout:withResolver:withRejecter:)
    func initialize(
        endPoint: String?,
        config: [AnyHashable: Any]?,
        timeout: [AnyHashable: Any]?,
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        thetaRepository = nil
        photoCaptureBuilder = nil
        photoCapture = nil
        timeShiftCaptureBuilder = nil
        timeShiftCapture = nil
        timeShiftCapturing = nil
        videoCaptureBuilder = nil
        videoCapture = nil
        videoCapturing = nil
        previewing = false

        Task {
            let configParams: ThetaRepository.Config? = {
                if let config = config as? [String: Any] {
                    return toConfig(params: config)
                }
                return nil
            }()
            let timeoutParams: ThetaRepository.Timeout? = {
                if let timeout = timeout as? [String: Any] {
                    return toTimeout(params: timeout)
                }
                return nil
            }()

            let endpoint = endPoint ?? "http://192.168.1.1"
            ThetaRepository.Companion.shared.doNewInstance(
                endpoint: endpoint,
                config: configParams,
                timeout: timeoutParams
            ) { thetaRepository, error in
                if let error {
                    reject(ERROR_CODE_ERROR, error.localizedDescription, error)
                } else {
                    self.thetaRepository = thetaRepository
                    resolve(true)
                }
            }
        }
    }

    @objc(isInitialized:withRejecter:)
    func isInitialized(
        resolve: RCTPromiseResolveBlock,
        reject _: RCTPromiseRejectBlock
    ) {
        resolve(thetaRepository != nil)
    }

    @objc(getThetaModel:withRejecter:)
    func getThetaModel(
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        resolve(thetaRepository.cameraModel?.name)
    }

    @objc(getThetaInfo:withRejecter:)
    func getThetaInfo(
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        thetaRepository.getThetaInfo { response, error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else if let response {
                var info = convertResult(thetaInfo: response)
                if let cameraModel = thetaRepository.cameraModel {
                    info[KEY_THETA_MODEL] = cameraModel.name
                }
                resolve(info)
            } else {
                reject(ERROR_CODE_ERROR, MESSAGE_NO_RESULT, nil)
            }
        }
    }

    @objc(getThetaState:withRejecter:)
    func getThetaState(
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        thetaRepository.getThetaState { response, error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else if let response {
                let state = convertResult(thetaState: response)
                resolve(state)
            } else {
                reject(ERROR_CODE_ERROR, MESSAGE_NO_RESULT, nil)
            }
        }
    }

    @objc(listFiles:withStartPosition:withEntryCount:withStorage:withResolver:withRejecter:)
    func listFiles(
        fileType: String,
        startPosition: Int,
        entryCount: Int,
        storage: String?,
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        guard
            let fileTypeVal = getEnumValue(
                values: ThetaRepository.FileTypeEnum.values(), name: fileType
            )
        else {
            reject(ERROR_CODE_ERROR, MESSAGE_NO_ARGUMENT, nil)
            return
        }

        let storageVal = getEnumValue(
            values: ThetaRepository.StorageEnum.values(), name: storage ?? ""
        )

        thetaRepository.listFiles(
            fileType: fileTypeVal,
            startPosition: Int32(startPosition),
            entryCount: Int32(entryCount),
            storage: storageVal
        ) { files, error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else if let files {
                let resultList = convertResult(fileInfoList: files.fileList)
                let resultMap = [
                    KEY_FILE_LIST: resultList,
                    KEY_TOTAL_ENTRIES: files.totalEntries,
                ]
                resolve(resultMap)
            } else {
                reject(ERROR_CODE_ERROR, MESSAGE_NO_RESULT, nil)
            }
        }
    }

    @objc(deleteFiles:withResolver:withRejecter:)
    func deleteFiles(
        fileUrls: [Any],
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        guard let urls = fileUrls as? [String] else {
            reject(ERROR_CODE_ERROR, MESSAGE_NO_ARGUMENT, nil)
            return
        }

        thetaRepository.deleteFiles(fileUrls: urls) { error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else {
                resolve(true)
            }
        }
    }

    @objc(deleteAllFiles:withRejecter:)
    func deleteAllFiles(
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }

        thetaRepository.deleteAllFiles { error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else {
                resolve(true)
            }
        }
    }

    @objc(deleteAllImageFiles:withRejecter:)
    func deleteAllImageFiles(
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }

        thetaRepository.deleteAllImageFiles { error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else {
                resolve(true)
            }
        }
    }

    @objc(deleteAllVideoFiles:withRejecter:)
    func deleteAllVideoFiles(
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }

        thetaRepository.deleteAllVideoFiles { error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else {
                resolve(true)
            }
        }
    }

    @objc(getOptions:withResolver:withRejecter:)
    func getOptions(
        optionNames: [Any],
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        guard let optionNames = optionNames as? [String] else {
            reject(ERROR_CODE_ERROR, MESSAGE_NO_ARGUMENT, nil)
            return
        }

        let params = convertGetOptionsParam(params: optionNames)
        thetaRepository.getOptions(
            optionNames: params
        ) { response, error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else if let response {
                resolve(convertResult(options: response))
            } else {
                reject(ERROR_CODE_ERROR, MESSAGE_NO_RESULT, nil)
            }
        }
    }

    @objc(setOptions:withResolver:withRejecter:)
    func setOptions(
        options: [AnyHashable: Any],
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        guard let options = options as? [String: Any] else {
            reject(ERROR_CODE_ERROR, MESSAGE_NO_ARGUMENT, nil)
            return
        }
        let params = convertSetOptionsParam(params: options)

        thetaRepository.setOptions(
            options: params
        ) { error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else {
                resolve(true)
            }
        }
    }

    @objc(getLivePreview:withRejecter:)
    func getLivePreview(
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        class FrameHandler: KotlinSuspendFunction1 {
            let thetaClientReactNative: ThetaClientReactNative
            static let FrameInterval = CFTimeInterval(1.0 / 10.0)
            var last: CFTimeInterval = 0

            init(_ thetaClientReactNative: ThetaClientReactNative) {
                self.thetaClientReactNative = thetaClientReactNative
            }

            func invoke(p1: Any?) async throws -> Any? {
                let now = CACurrentMediaTime()
                if now - last > Self.FrameInterval {
                    autoreleasepool {
                        if let frameData = p1 as? KotlinPair<KotlinByteArray, KotlinInt> {
                            let nsData = PlatformKt.frameFrom(
                                packet: frameData
                            )
                            let encodeString = nsData.base64EncodedString()
                            let dataUrl = "data:image/jpeg;base64," + encodeString
                            thetaClientReactNative.sendEvent(
                                withName: ThetaClientReactNative.EVENT_FRAME,
                                body: ["data": dataUrl]
                            )
                        }
                    }
                }
                return thetaClientReactNative.previewing
            }
        }

        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        let frameHandler = FrameHandler(self)
        previewing = true
        thetaRepository.getLivePreview(frameHandler: frameHandler) { error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else {
                resolve(true)
            }
        }
    }

    @objc(stopLivePreview:withRejecter:)
    func stopLivePreview(
        resolve: RCTPromiseResolveBlock,
        reject _: RCTPromiseRejectBlock
    ) {
        previewing = false
        resolve(nil)
    }

    @objc(getPhotoCaptureBuilder:withRejecter:)
    func getPhotoCaptureBuilder(
        resolve: RCTPromiseResolveBlock,
        reject: RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        photoCaptureBuilder = thetaRepository.getPhotoCaptureBuilder()
        resolve(nil)
    }

    @objc(buildPhotoCapture:withResolver:withRejecter:)
    func buildPhotoCapture(
        options: [AnyHashable: Any]?,
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let _ = thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        guard let photoCaptureBuilder else {
            reject(ERROR_CODE_ERROR, MESSAGE_NO_PHOTO_CAPTURE_BUILDER, nil)
            return
        }

        if let options = options as? [String: Any] {
            setCaptureBuilderParams(params: options, builder: photoCaptureBuilder)
            setPhotoCaptureBuilderParams(params: options, builder: photoCaptureBuilder)
        }
        photoCaptureBuilder.build { capture, error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else if let capture {
                self.photoCapture = capture
                self.photoCaptureBuilder = nil
                resolve(true)
            } else {
                reject(ERROR_CODE_ERROR, MESSAGE_NO_PHOTO_CAPTURE, nil)
            }
        }
    }

    @objc(takePicture:withRejecter:)
    func takePicture(
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let _ = thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        guard let photoCapture else {
            reject(ERROR_CODE_ERROR, MESSAGE_NO_PHOTO_CAPTURE, nil)
            return
        }

        class Callback: PhotoCaptureTakePictureCallback {
            let callback: (_ url: String?, _ error: Error?) -> Void
            init(_ callback: @escaping (_ url: String?, _ error: Error?) -> Void) {
                self.callback = callback
            }

            func onSuccess(fileUrl: String) {
                callback(fileUrl, nil)
            }

            func onProgress(completion _: Float) {}

            func onError(exception: ThetaRepository.ThetaRepositoryException) {
                callback(nil, exception.asError())
            }
        }

        photoCapture.takePicture(
            callback: Callback { url, error in
                if let error {
                    reject(ERROR_CODE_ERROR, error.localizedDescription, error)
                } else {
                    self.photoCapture = nil
                    resolve(url)
                }
            })
    }

    @objc(getTimeShiftCaptureBuilder:withRejecter:)
    func getTimeShiftCaptureBuilder(
        resolve: RCTPromiseResolveBlock,
        reject: RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        timeShiftCaptureBuilder = thetaRepository.getTimeShiftCaptureBuilder()
        resolve(nil)
    }

    @objc(buildTimeShiftCapture:withResolver:withRejecter:)
    func buildTimeShiftCapture(
        options: [AnyHashable: Any]?,
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let _ = thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        guard let timeShiftCaptureBuilder else {
            reject(ERROR_CODE_ERROR, MESSAGE_NO_TIMESHIFT_CAPTURE_BUILDER, nil)
            return
        }

        if let options = options as? [String: Any] {
            setCaptureBuilderParams(params: options, builder: timeShiftCaptureBuilder)
            setTimeShiftCaptureBuilderParams(params: options, builder: timeShiftCaptureBuilder)
        }
        timeShiftCaptureBuilder.build { capture, error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else if let capture {
                self.timeShiftCapture = capture
                self.timeShiftCaptureBuilder = nil
                resolve(true)
            } else {
                reject(ERROR_CODE_ERROR, MESSAGE_NO_TIMESHIFT_CAPTURE, nil)
            }
        }
    }

    @objc(startTimeShiftCapture:withRejecter:)
    func startTimeShiftCapture(
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let _ = thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        guard let timeShiftCapture else {
            reject(ERROR_CODE_ERROR, MESSAGE_NO_TIMESHIFT_CAPTURE, nil)
            return
        }

        class Callback: TimeShiftCaptureStartCaptureCallback {
            let callback: (_ url: String?, _ error: Error?) -> Void
            weak var client: ThetaClientReactNative?
            init(
                _ callback: @escaping (_ url: String?, _ error: Error?) -> Void,
                client: ThetaClientReactNative
            ) {
                self.callback = callback
                self.client = client
            }

            func onError(exception: ThetaRepository.ThetaRepositoryException) {
                callback(nil, exception.asError())
            }

            func onProgress(completion: Float) {
                client?.sendEvent(
                    withName: ThetaClientReactNative.EVENT_NOTIFY,
                    body: toNotify(
                        name: ThetaClientReactNative.NOTIFY_TIMESHIFT_PROGRESS,
                        params: toCaptureProgressNotifyParam(value: completion)
                    )
                )
            }

            func onSuccess(fileUrl_ fileUrl: String?) {
                callback(fileUrl, nil)
            }
        }

        timeShiftCapturing = timeShiftCapture.startCapture(
            callback: Callback(
                { url, error in
                    if let error {
                        reject(ERROR_CODE_ERROR, error.localizedDescription, error)
                    } else {
                        resolve(url)
                    }
                }, client: self
            ))
    }

    @objc(cancelTimeShiftCapture:withRejecter:)
    func cancelTimeShiftCapture(
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let _ = thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        guard let timeShiftCapturing else {
            reject(ERROR_CODE_ERROR, MESSAGE_NO_TIMESHIFT_CAPTURING, nil)
            return
        }
        timeShiftCapturing.cancelCapture()
        resolve(nil)
    }

    @objc(getVideoCaptureBuilder:withRejecter:)
    func getVideoCaptureBuilder(
        resolve: RCTPromiseResolveBlock,
        reject: RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        videoCaptureBuilder = thetaRepository.getVideoCaptureBuilder()
        resolve(nil)
    }

    @objc(buildVideoCapture:withResolver:withRejecter:)
    func buildVideoCapture(
        options: [AnyHashable: Any]?,
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let _ = thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        guard let videoCaptureBuilder else {
            reject(ERROR_CODE_ERROR, MESSAGE_NO_VIDEO_CAPTURE_BUILDER, nil)
            return
        }

        if let options = options as? [String: Any] {
            setCaptureBuilderParams(params: options, builder: videoCaptureBuilder)
            setVideoCaptureBuilderParams(params: options, builder: videoCaptureBuilder)
        }
        videoCaptureBuilder.build { capture, error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else if let capture {
                self.videoCapture = capture
                self.videoCaptureBuilder = nil
                resolve(true)
            } else {
                reject(ERROR_CODE_ERROR, MESSAGE_NO_VIDEO_CAPTURE, nil)
            }
        }
    }

    @objc(startVideoCapture:withRejecter:)
    func startVideoCapture(
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let _ = thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        guard let videoCapture else {
            reject(ERROR_CODE_ERROR, MESSAGE_NO_VIDEO_CAPTURE, nil)
            return
        }
        class Callback: VideoCaptureStartCaptureCallback {
            let callback: (_ url: String?, _ error: Error?) -> Void
            weak var client: ThetaClientReactNative?
            init(
                _ callback: @escaping (_ url: String?, _ error: Error?) -> Void,
                client: ThetaClientReactNative
            ) {
                self.callback = callback
                self.client = client
            }

            func onCaptureCompleted(fileUrl: String?) {
                callback(fileUrl, nil)
            }

            func onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                callback(nil, exception.asError())
            }

            func onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                let error = exception.asError()
                client?.sendEvent(
                    withName: ThetaClientReactNative.EVENT_NOTIFY,
                    body: toNotify(
                        name: ThetaClientReactNative.NOTIFY_VIDEO_CAPTURE_STOP_ERROR,
                        params: toMessageNotifyParam(value: error.localizedDescription)
                    )
                )
            }
        }

        videoCapturing = videoCapture.startCapture(
            callback: Callback(
                { url, error in
                    if let error {
                        reject(ERROR_CODE_ERROR, error.localizedDescription, error)
                    } else {
                        self.videoCapture = nil
                        resolve(url)
                    }
                }, client: self
            ))
    }

    @objc(stopVideoCapture:withRejecter:)
    func stopVideoCapture(
        resolve: RCTPromiseResolveBlock,
        reject: RCTPromiseRejectBlock
    ) {
        guard let _ = thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        guard let videoCapturing else {
            reject(ERROR_CODE_ERROR, MESSAGE_NO_VIDEO_CAPTURING, nil)
            return
        }
        videoCapturing.stopCapture()
        resolve(nil)
    }

    @objc(getMetadata:withResolver:withRejecter:)
    func getMetadata(
        fileUrl: String,
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }

        thetaRepository.getMetadata(
            fileUrl: fileUrl
        ) { response, error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else if let response {
                resolve(convertResult(metadata: response))
            } else {
                reject(ERROR_CODE_ERROR, MESSAGE_NO_RESULT, nil)
            }
        }
    }

    @objc(reset:withRejecter:)
    func reset(
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }

        thetaRepository.reset { error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else {
                resolve(true)
            }
        }
    }

    @objc(restoreSettings:withRejecter:)
    func restoreSettings(
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }

        thetaRepository.restoreSettings { error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else {
                resolve(true)
            }
        }
    }

    @objc(stopSelfTimer:withRejecter:)
    func stopSelfTimer(
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }

        thetaRepository.stopSelfTimer { error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else {
                resolve(true)
            }
        }
    }

    @objc(
        convertVideoFormats:withToLowResolution:withApplyTopBottomCorrection:withResolver:
        withRejecter:
    )
    func convertVideoFormats(
        fileUrl: String,
        toLowResolution: Bool,
        applyTopBottomCorrection: Bool,
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }

        thetaRepository.convertVideoFormats(
            fileUrl: fileUrl,
            toLowResolution: toLowResolution,
            applyTopBottomCorrection: applyTopBottomCorrection
        ) { fileUrl, error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else if let fileUrl {
                resolve(fileUrl)
            } else {
                reject(ERROR_CODE_ERROR, MESSAGE_NO_RESULT, nil)
            }
        }
    }

    @objc(cancelVideoConvert:withRejecter:)
    func cancelVideoConvert(
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }

        thetaRepository.cancelVideoConvert { error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else {
                resolve(true)
            }
        }
    }

    @objc(finishWlan:withRejecter:)
    func finishWlan(
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }

        thetaRepository.finishWlan { error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else {
                resolve(true)
            }
        }
    }

    @objc(listAccessPoints:withRejecter:)
    func listAccessPoints(
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }

        thetaRepository.listAccessPoints { response, error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else if let response {
                var list = convertResult(accessPointList: response)
                resolve(list)
            } else {
                reject(ERROR_CODE_ERROR, MESSAGE_NO_RESULT, nil)
            }
        }
    }

    @objc(
        setAccessPointDynamically:
        withSsidStealth:
        withAuthMode:
        withPassword:
        withConnectionPriority:
        withProxy:
        withResolver:
        withRejecter:
    )
    func setAccessPointDynamically(
        ssid: String,
        ssidStealth: Bool,
        authMode: String,
        password: String,
        connectionPriority: Int,
        proxy: [AnyHashable: Any]?,
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        guard
            let authMode = getEnumValue(
                values: ThetaRepository.AuthModeEnum.values(), name: authMode
            )
        else {
            reject(ERROR_CODE_ERROR, MESSAGE_NO_ARGUMENT, nil)
            return
        }

        let proxyParam: ThetaRepository.Proxy? = {
            if let proxy = proxy as? [String: Any] {
                return toProxy(params: proxy)
            }
            return nil
        }()
        thetaRepository.setAccessPointDynamically(
            ssid: ssid,
            ssidStealth: ssidStealth,
            authMode: authMode,
            password: password,
            connectionPriority: Int32(connectionPriority),
            proxy: proxyParam
        ) { error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else {
                resolve(true)
            }
        }
    }

    @objc(
        setAccessPointStatically:
        withSsidStealth:
        withAuthMode:
        withPassword:
        withConnectionPriority:
        withIpAddress:
        withSubnetMask:
        withDefaultGateway:
        withProxy:
        withResolver:
        withRejecter:
    )
    func setAccessPointStatically(
        ssid: String,
        ssidStealth: Bool,
        authMode: String,
        password: String?,
        connectionPriority: Int,
        ipAddress: String,
        subnetMask: String,
        defaultGateway: String,
        proxy: [AnyHashable: Any]?,
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        guard
            let authMode = getEnumValue(
                values: ThetaRepository.AuthModeEnum.values(), name: authMode
            )
        else {
            reject(ERROR_CODE_ERROR, MESSAGE_NO_ARGUMENT, nil)
            return
        }

        let proxyParam: ThetaRepository.Proxy? = {
            if let proxy = proxy as? [String: Any] {
                return toProxy(params: proxy)
            }
            return nil
        }()
        thetaRepository.setAccessPointStatically(
            ssid: ssid,
            ssidStealth: ssidStealth,
            authMode: authMode,
            password: password,
            connectionPriority: Int32(connectionPriority),
            ipAddress: ipAddress,
            subnetMask: subnetMask,
            defaultGateway: defaultGateway,
            proxy: proxyParam
        ) { error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else {
                resolve(true)
            }
        }
    }

    @objc(deleteAccessPoint:withResolver:withRejecter:)
    func deleteAccessPoint(
        ssid: String,
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }

        thetaRepository.deleteAccessPoint(
            ssid: ssid
        ) { error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else {
                resolve(true)
            }
        }
    }

    @objc(getMySetting:withResolver:withRejecter:)
    func getMySetting(
        captureMode: String,
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        guard
            let captureMode = getEnumValue(
                values: ThetaRepository.CaptureModeEnum.values(), name: captureMode
            )
        else {
            reject(ERROR_CODE_ERROR, MESSAGE_NO_ARGUMENT, nil)
            return
        }
        thetaRepository.getMySetting(
            captureMode: captureMode
        ) { response, error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else if let response {
                resolve(convertResult(options: response))
            } else {
                reject(ERROR_CODE_ERROR, MESSAGE_NO_RESULT, nil)
            }
        }
    }

    @objc(getMySettingFromOldModel:withResolver:withRejecter:)
    func getMySettingFromOldModel(
        optionNames: [Any],
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        guard let optionNames = optionNames as? [String] else {
            reject(ERROR_CODE_ERROR, MESSAGE_NO_ARGUMENT, nil)
            return
        }

        let params = convertGetOptionsParam(params: optionNames)
        thetaRepository.getMySetting(
            optionNames: params
        ) { response, error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else if let response {
                resolve(convertResult(options: response))
            } else {
                reject(ERROR_CODE_ERROR, MESSAGE_NO_RESULT, nil)
            }
        }
    }

    @objc(setMySetting:withOptions:withResolver:withRejecter:)
    func setMySetting(
        captureMode: String,
        options: [AnyHashable: Any],
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        guard let options = options as? [String: Any],
              let captureMode = getEnumValue(
                  values: ThetaRepository.CaptureModeEnum.values(), name: captureMode
              )
        else {
            reject(ERROR_CODE_ERROR, MESSAGE_NO_ARGUMENT, nil)
            return
        }

        let params = convertSetOptionsParam(params: options)
        thetaRepository.setMySetting(
            captureMode: captureMode,
            options: params
        ) { error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else {
                resolve(true)
            }
        }
    }

    @objc(deleteMySetting:withResolver:withRejecter:)
    func deleteMySetting(
        captureMode: String,
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        guard
            let captureMode = getEnumValue(
                values: ThetaRepository.CaptureModeEnum.values(), name: captureMode
            )
        else {
            reject(ERROR_CODE_ERROR, MESSAGE_NO_ARGUMENT, nil)
            return
        }
        thetaRepository.deleteMySetting(
            captureMode: captureMode
        ) { error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else {
                resolve(true)
            }
        }
    }

    @objc(listPlugins:withRejecter:)
    func listPlugins(
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        thetaRepository.listPlugins { pluginList, error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else if let pluginList {
                resolve(toPluginInfosResult(pluginInfoList: pluginList))
            } else {
                reject(ERROR_CODE_ERROR, MESSAGE_NO_RESULT, nil)
            }
        }
    }

    @objc(setPlugin:withResolver:withRejecter:)
    func setPlugin(
        packageName: String,
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }

        thetaRepository.setPlugin(packageName: packageName) { error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else {
                resolve(true)
            }
        }
    }

    @objc(startPlugin:withResolver:withRejecter:)
    func startPlugin(
        packageName: String,
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }

        thetaRepository.startPlugin(packageName: packageName) { error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else {
                resolve(true)
            }
        }
    }

    @objc(stopPlugin:withRejecter:)
    func stopPlugin(
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }

        thetaRepository.stopPlugin { error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else {
                resolve(true)
            }
        }
    }

    @objc(getPluginLicense:withResolver:withRejecter:)
    func getPluginLicense(
        packageName: String,
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }

        thetaRepository.getPluginLicense(
            packageName: packageName
        ) { response, error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else if let response {
                resolve(response)
            } else {
                reject(ERROR_CODE_ERROR, MESSAGE_NO_RESULT, nil)
            }
        }
    }

    @objc(getPluginOrders:withRejecter:)
    func getPluginOrders(
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        thetaRepository.getPluginOrders { plugins, error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else if let plugins {
                resolve(plugins)
            } else {
                reject(ERROR_CODE_ERROR, MESSAGE_NO_RESULT, nil)
            }
        }
    }

    @objc(setPluginOrders:withResolver:withRejecter:)
    func setPluginOrders(
        plugins: [Any],
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        guard let plugins = plugins as? [String] else {
            reject(ERROR_CODE_ERROR, MESSAGE_NO_ARGUMENT, nil)
            return
        }

        thetaRepository.setPluginOrders(
            plugins: plugins
        ) { error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else {
                resolve(true)
            }
        }
    }

    @objc(setBluetoothDevice:withResolver:withRejecter:)
    func setBluetoothDevice(
        uuid: String,
        resolve: @escaping RCTPromiseResolveBlock,
        reject: @escaping RCTPromiseRejectBlock
    ) {
        guard let thetaRepository else {
            reject(ERROR_CODE_ERROR, MESSAGE_NOT_INIT, nil)
            return
        }
        thetaRepository.setBluetoothDevice(uuid: uuid) { deviceName, error in
            if let error {
                reject(ERROR_CODE_ERROR, error.localizedDescription, error)
            } else if let deviceName {
                resolve(deviceName)
            } else {
                reject(ERROR_CODE_ERROR, MESSAGE_NO_RESULT, nil)
            }
        }
    }
}
