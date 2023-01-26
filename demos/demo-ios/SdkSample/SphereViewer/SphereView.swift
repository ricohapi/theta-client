//
//  SphereView.swift
//  SdkSample
//

import SwiftUI
import WebKit

struct SphereView: UIViewRepresentable {
    var urlString: String

    func makeUIView(context: Context) -> Viewer360 {
        return Viewer360(frame: .zero)
    }
    
    func updateUIView(_ uiView: Viewer360, context: Context) {
        guard URL(string: urlString) != nil else {
            return
        }
        uiView.updateImage(imageUrl: urlString)
    }

    static func jpegDataToDataUrl(data: Data) -> String {
        let DATA_URL_PREFIX = "data:image/jpeg;base64,"
        return DATA_URL_PREFIX + data.base64EncodedString();
    }
}
