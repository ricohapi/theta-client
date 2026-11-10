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

/**
 * Android often cannot resolve mDNS host names (.local).
 * Tested on THETA X (serial number YRxxxxxxxx), V (YLxxxxxxxx) and Z1 (YNxxxxxxxx). Add other models after testing on them.
 * THETA V and Z1 answer the IPv6 link-local address too, but refuse HTTP on it, so the OS resolution does not work.
 */
internal actual val mdnsHostPrefixes: List<String> = listOf("THETAYR", "THETAYL", "THETAYN")

internal actual suspend fun resolveMdnsHost(host: String, retryCount: Int): String? =
    MdnsResolver.resolve(host, retryCount)
