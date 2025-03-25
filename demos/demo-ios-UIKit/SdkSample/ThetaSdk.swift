//
//  ThetaSdk.swift
//  SdkSample
//
import THETAClient
import UIKit

typealias ThetaHandlerWithError<T> = (_ response: T?, _ error: Error?) -> Void
typealias ThetaHandler<T> = (_ response: T) -> Void
typealias ThetaErrorHandler = (_ error: Error?) -> Void
typealias ThetaFrameHandler = (_ frame: Data) -> Bool
typealias ThetaException = ThetaRepository.ThetaRepositoryException
typealias ThetaInfo = ThetaRepository.ThetaInfo
typealias ThetaState = ThetaRepository.ThetaState
typealias ThetaFileInfo = ThetaRepository.FileInfo
typealias ThetaFileType = ThetaRepository.FileTypeEnum

let theta = Theta()

class Theta {
    private static let endPoint: String = "http://192.168.1.1"
    private var thetaRepository: ThetaRepository?
    var lastInfo: ThetaInfo?

    func reset() {
        thetaRepository = nil
    }

    deinit {
        print("MC - Theta.deinit") // not called
    }

    func initialize() async throws {
        if thetaRepository != nil {
            return
        }
        var config: ThetaRepository.Config?

        // Client mode authentication settings
//        config = ThetaRepository.Config()
//        config.clientMode = DigestAuth(username: "THETAXX12345678", password: "12345678")

        thetaRepository = try await withCheckedThrowingContinuation { continuation in
            ThetaRepository.Companion.shared.doNewInstance(
                endpoint: Self.endPoint,
                config: config,
                timeout: ThetaRepository.Timeout(connectTimeout: 1000, requestTimeout: 5000, socketTimeout: 1000)
            ) { resp, error in
                if let response = resp {
                    continuation.resume(returning: response)
                }
                if let thetaError = error {
                    continuation.resume(throwing: thetaError)
                }
            }
        }
    }

    func info(_ completionHandler: @escaping ThetaHandler<ThetaInfo>) async throws {
        try await initialize()
        let response: ThetaInfo = try await withCheckedThrowingContinuation { continuation in
            thetaRepository!.getThetaInfo { resp, error in
                if let response = resp {
                    continuation.resume(returning: response)
                }
                if let thetaError = error {
                    continuation.resume(throwing: thetaError)
                }
            }
        }
        lastInfo = response
        completionHandler(response)
    }

    func livePreview(frameHandler: @escaping ThetaFrameHandler) async throws {
        try await initialize()

        let handler = FrameHandler(frameHandler)

        let _: Bool = try await withCheckedThrowingContinuation { continuation in
            thetaRepository!.getLivePreview(
                frameHandler: handler
            ) { error in
                if let thetaError = error {
                    continuation.resume(throwing: thetaError)
                } else {
                    continuation.resume(returning: true)
                }
            }
        }
    }

    func listPhotos(_ completionHandler: @escaping ThetaHandler<[ThetaFileInfo]>) async throws {
        try await initialize()
        let response: [ThetaFileInfo]
            = try await withCheckedThrowingContinuation { continuation in
                thetaRepository!.listFiles(fileType: ThetaFileType.image, startPosition: 0,
                                           entryCount: 1000)
                { resp, error in
                    if let response = resp {
                        continuation.resume(returning: response.fileList)
                    }
                    if let thetaError = error {
                        continuation.resume(throwing: thetaError)
                    }
                }
            }
        completionHandler(response)
    }

    func takePicture(_ callback: @escaping (_ url: String?) -> Void) async throws {
        try await initialize()
        let photoCapture: PhotoCapture = try await withCheckedThrowingContinuation { continuation in
            thetaRepository!.getPhotoCaptureBuilder()
                .build { capture, error in
                    if let photoCapture = capture {
                        continuation.resume(returning: photoCapture)
                    }
                    if let thetaError = error {
                        continuation.resume(throwing: thetaError)
                    }
                }
        }
        class Callback: PhotoCaptureTakePictureCallback {
            let callback: (_ url: String?, _ error: Error?) -> Void
            init(_ callback: @escaping (_ url: String?, _ error: Error?) -> Void) {
                self.callback = callback
            }

            func onSuccess(fileUrl: String?) {
                callback(fileUrl, nil)
            }

            func onCapturing(status: CapturingStatusEnum) {
                print("takePicture onCapturing: " + status.name)
            }

            func onError(exception: ThetaException) {
                callback(nil, exception.asError())
            }
        }
        let photoUrl: String? = try await withCheckedThrowingContinuation { continuation in
            photoCapture.takePicture(
                callback: Callback { fileUrl, error in
                    if let thetaError = error {
                        continuation.resume(throwing: thetaError)
                    } else {
                        continuation.resume(returning: fileUrl)
                    }
                }
            )
        }
        callback(photoUrl)
    }
}

class FrameHandler: KotlinSuspendFunction1 {
    static let frameInterval = CFTimeInterval(1.0 / 10.0)
    let handler: ThetaFrameHandler
    var prev: CFTimeInterval = 0
    var last: CFTimeInterval = 0
    private let start = Date().timeIntervalSince1970

    init(_ handler: @escaping ThetaFrameHandler) {
        self.handler = handler
    }

    func invoke(p1: Any?) async throws -> Any? {
        startHeartBeat()
        let now = CACurrentMediaTime()
        if now - last > Self.frameInterval {
            var result = false
            print("MC - FrameHandler.invoke - updating new frame - \(Date().timeIntervalSince1970-start)")
            autoreleasepool {
                if let frameData = p1 as? KotlinPair<KotlinByteArray, KotlinInt> {
                    let nsData = PlatformKt.frameFrom(
                        packet: frameData
                    )
                    result = handler(nsData)
                }
            }
            prev = last
            last = now
            return result
        } else {
            return true
        }
    }

    func startHeartBeat() {
        DispatchQueue.main.asyncAfter(deadline: .now() + Self.frameInterval * 2) {
            if self.prev == self.last {
                print("Error")
            }
        }
    }
}
