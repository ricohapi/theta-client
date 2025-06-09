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

expect class WeakReference<T : Any> internal constructor(referred: T) {
    fun get(): T?
}
