/*
 * api depends android
 */
package com.ricoh360.thetaclient

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.lang.ref.WeakReference
import java.util.UUID

/**
 * describe platform
 */
actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

/**
 * converted frame Source data class
 */
actual typealias FrameSource = Bitmap

/**
 * convert [packet] to android Bitmap
 */
actual fun frameFrom(packet: Pair<ByteArray, Int>): FrameSource {
    return BitmapFactory.decodeStream(packet.first.inputStream(0, packet.second))
}

actual fun randomUUID(): String {
    return UUID.randomUUID().toString()
}

actual fun currentTimeMillis(): Long {
    return System.currentTimeMillis()
}

actual typealias WeakReference<T> = WeakReference<T>
