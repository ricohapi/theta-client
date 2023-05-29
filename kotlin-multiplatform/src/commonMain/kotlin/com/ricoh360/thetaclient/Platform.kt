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

expect fun randomUUID(): String

/**
 * Get the value of the specified environment variable
 *
 * @param name environment variable name to get its value
 * @return
 */
expect fun getEnvironmentVar(name: String): String?
