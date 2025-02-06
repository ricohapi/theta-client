//
//  Viewer360.swift
//  SdkSample
//
//  Copyright 2023 Ricoh Co, Ltd.
//

import Foundation
import WebKit

class Viewer360: WKWebView, WKNavigationDelegate {
    var imageUrl: String?
    var isInitialized = false

    override init(frame: CGRect, configuration: WKWebViewConfiguration) {
        super.init(frame: frame, configuration: configuration)
        loadViewer()
    }

    init(frame: CGRect) {
        let config = WKWebViewConfiguration()
        config.allowsInlineMediaPlayback = true
        config.setValue(true, forKey: "allowUniversalAccessFromFileURLs")
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
        self.imageUrl = imageUrl
        callUpdate()
    }

    func callUpdate() {
        if isInitialized && imageUrl != nil {
            evaluateJavaScript("update('\(imageUrl!)');")
        }
    }

    // MARK: - WKNavigationDelegate

    func webView(_: WKWebView, didFinish _: WKNavigation!) {
        isInitialized = true
        callUpdate()
    }
}
