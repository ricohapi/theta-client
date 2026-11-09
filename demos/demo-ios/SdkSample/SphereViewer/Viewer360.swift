//
//  Viewer360.swift
//  SdkSample
//
//  Copyright 2023 Ricoh Co, Ltd.
//

import Foundation
import WebKit

class Viewer360: WKWebView, WKNavigationDelegate {
    var imageDataUrl: String?
    var isInitialized = false
    /// URL of the image on THETA. imageDataUrl is its data URL.
    private var sourceUrl: String?

    override init(frame: CGRect, configuration: WKWebViewConfiguration) {
        super.init(frame: frame, configuration: configuration)
        loadViewer()
    }

    init(frame: CGRect) {
        let config = WKWebViewConfiguration()
        config.allowsInlineMediaPlayback = true
        super.init(frame: frame, configuration: config)
        loadViewer()
    }

    required init?(coder: NSCoder) {
        super.init(coder: coder)
        loadViewer()
    }

    func loadViewer() {
        navigationDelegate = self
        let path: String = Bundle.main.path(forResource: "index", ofType: "html")!
        let localHtmlUrl = URL(fileURLWithPath: path, isDirectory: false)
        loadFileURL(localHtmlUrl, allowingReadAccessTo: localHtmlUrl)
        configuration.userContentController = WKUserContentController()
    }

    func updateImage(imageUrl: String) {
        guard imageUrl != sourceUrl, let url = URL(string: imageUrl) else {
            return
        }
        sourceUrl = imageUrl
        // The viewer page (file://) cannot load the image of THETA (http://) as a texture,
        // so download it here and pass it as a data URL.
        URLSession.shared.dataTask(with: url) { [weak self] data, response, error in
            var dataUrl: String?
            if let data, (response as? HTTPURLResponse)?.statusCode == 200 {
                dataUrl = SphereView.jpegDataToDataUrl(data: data)
            } else {
                print("Failed to download \(imageUrl): \(error?.localizedDescription ?? String(describing: response))")
            }
            DispatchQueue.main.async {
                guard let self, self.sourceUrl == imageUrl else {
                    return
                }
                guard let dataUrl else {
                    // Allow the same URL to be loaded again
                    self.sourceUrl = nil
                    return
                }
                self.imageDataUrl = dataUrl
                self.callUpdate()
            }
        }.resume()
    }

    func callUpdate() {
        if isInitialized && imageDataUrl != nil {
            evaluateJavaScript("update('\(imageDataUrl!)');")
        }
    }

    // MARK: - WKNavigationDelegate

    func webView(_: WKWebView, didFinish _: WKNavigation!) {
        isInitialized = true
        callUpdate()
    }
}
