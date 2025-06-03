/*
 * api depends ios
 */
package com.ricoh360.thetaclient

import kotlinx.cinterop.*
import platform.Foundation.*
import platform.UIKit.UIDevice
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.WeakReference


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
internal val data: NSMutableData = NSMutableData.create(length = 10u)!!

/**
 * convert [packet] to NSData frame source data
 */
@OptIn(ExperimentalForeignApi::class)
actual fun frameFrom(packet: Pair<ByteArray, Int>): FrameSource {
    packet.first.usePinned {
        val range = NSMakeRange(0u, data.length)
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

actual fun currentTimeMillis(): Long {
    val interval = NSDate().timeIntervalSince1970
    return (interval / 1000.0).toLong()
}

@OptIn(ExperimentalNativeApi::class)
actual typealias WeakReference<T> = WeakReference<T>
