//
//  UIColorHex.swift
//  SdkSample
//
import UIKit

extension UIColor {
    convenience init(hex: String) {
        var colors:[CGFloat] = Array(repeating: CGFloat(1.0), count: 4)
        var color = UInt32(hex, radix: 16)!
        if (hex.count == 3) {
            for i in 0 ..< 3 {
                colors[2 - i] = CGFloat(color % 16) / CGFloat(16)
                color /= 16
            }
        } else if (hex.count == 6) {
            for i in 0 ..< 3 {
                colors[2 - i] = CGFloat(color % 256) / CGFloat(256)
                color /= 256
            }
        } else if (hex.count == 8) {
            for i in 0 ..< 4 {
                colors[3 - i] = CGFloat(color % 256) / CGFloat(256)
                color /= 256
            }
        }
        self.init(red: colors[0], green: colors[1], blue: colors[2], alpha: colors[3])
    }
}
