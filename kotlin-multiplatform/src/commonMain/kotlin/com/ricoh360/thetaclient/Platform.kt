/*
 * platform api
 */
package com.ricoh360.thetaclient

/**
 * describe platform
 */
expect class Platform() {
    val platform: String
}

/**
 * converted frame source data class
 */
expect class FrameSource

/**
 * convert [packet] to platform dependent frame source data
 */
expect fun frameFrom(packet: Pair<ByteArray, Int>): FrameSource

internal expect fun randomUUID(): String

internal expect fun currentTimeMillis(): Long

/**
 * Prefixes of "<name>.local" host names that the SDK resolves to IPv4 addresses itself,
 * because the resolution of the platform does not work for them.
 */
internal expect val mdnsHostPrefixes: List<String>

/**
 * Resolves [host] whose prefix is in [mdnsHostPrefixes] to an IPv4 address.
 *
 * @param retryCount Number of the tries until resolved
 * @return IPv4 address such as "192.168.1.23", or null when it cannot be resolved
 */
internal expect suspend fun resolveMdnsHost(host: String, retryCount: Int): String?

expect class WeakReference<T : Any> internal constructor(referred: T) {
    fun get(): T?
}
