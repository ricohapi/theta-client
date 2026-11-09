/*
 * api depends ios
 */
package com.ricoh360.thetaclient

import kotlinx.cinterop.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import platform.Foundation.*
import platform.UIKit.UIDevice
import platform.posix.AF_INET
import platform.posix.SOCK_STREAM
import platform.posix.addrinfo
import platform.posix.freeaddrinfo
import platform.posix.getaddrinfo
import platform.posix.sockaddr_in
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
    return (interval * 1000.0).toLong()
}

@OptIn(ExperimentalNativeApi::class)
actual typealias WeakReference<T> = WeakReference<T>

/**
 * THETA A1 does not answer the IPv6 (AAAA) query, so the OS resolution waits for it for seconds
 * and the connection times out. The other models are resolved by the OS.
 */
internal actual val mdnsHostPrefixes: List<String> = listOf("THETAAA")

/** Interval of the tries of [resolveMdnsHost], which may fail at once while the network is changing */
private const val RESOLVE_RETRY_INTERVAL_MILLIS = 1_000L

/**
 * Resolves [host] with the OS, querying only IPv4 not to wait for the IPv6 answer.
 */
@OptIn(ExperimentalForeignApi::class)
internal actual suspend fun resolveMdnsHost(host: String, retryCount: Int): String? = withContext<String?>(Dispatchers.IO) {
    repeat(retryCount) { index ->
        if (index > 0) delay(RESOLVE_RETRY_INTERVAL_MILLIS)
        memScoped {
            val hints = alloc<addrinfo> {
                ai_family = AF_INET
                ai_socktype = SOCK_STREAM
            }
            val result = allocPointerTo<addrinfo>()
            if (getaddrinfo(host, null, hints.ptr, result.ptr) == 0) {
                try {
                    val address = result.value?.pointed?.ai_addr?.reinterpret<sockaddr_in>()?.pointed
                    if (address != null) {
                        // s_addr is in the network byte order, whose first byte is the lowest on iOS (little endian).
                        val value = address.sin_addr.s_addr
                        return@withContext (0 until 4).joinToString(".") { ((value shr (it * 8)) and 0xffu).toString() }
                    }
                } finally {
                    freeaddrinfo(result.value)
                }
            }
        }
    }
    null
}
