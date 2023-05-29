/*
 * api depends ios
 */
package com.ricoh360.thetaclient

import kotlinx.cinterop.*
import platform.Foundation.*
import platform.UIKit.UIDevice

/**
 * describe platform
 */
actual class Platform actual constructor() {
    actual val platform: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

/**
 * converted frame source data class
 */
actual typealias FrameSource = NSData

/** fixed NSData */
val data: NSMutableData = NSMutableData.create(length = 10)!!

/**
 * convert [packet] to NSData frame source data
 */
actual fun frameFrom(packet: Pair<ByteArray, Int>): FrameSource {
    packet.first.usePinned {
        val range = NSMakeRange(0, data.length)
        data.replaceBytesInRange(
            range = range,
            withBytes = it.addressOf(0),
            length = packet.second.toULong()
        )
        return data
    }
}

actual fun randomUUID(): String {
    return NSUUID.UUID().UUIDString
}

actual fun getEnvironmentVar(name: String): String? {
    return null
}
