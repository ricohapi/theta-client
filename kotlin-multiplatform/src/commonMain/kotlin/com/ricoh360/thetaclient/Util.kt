package com.ricoh360.thetaclient

import com.soywiz.krypto.md5
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.withTimeout

internal const val HEX_CHARACTERS = "0123456789abcdef"

internal fun bytesToHex(data: ByteArray, hexCharacters: String = HEX_CHARACTERS): String {
    val hexArray = hexCharacters.toCharArray()
    val hexChars = CharArray(data.size * 2)
    for (i in data.indices) {
        val v = data[i].toInt() and 0xFF
        hexChars[i * 2] = hexArray[v ushr 4]
        hexChars[i * 2 + 1] = hexArray[v and 0x0F]
    }
    return hexChars.concatToString()
}

internal fun md5(data: String): String {
    val byteArray = data.encodeToByteArray()
    val hash = byteArray.md5()
    return bytesToHex(hash.bytes, HEX_CHARACTERS)
}

internal suspend fun <T> syncExecutor(
    semaphore: Semaphore,
    timeout: Long,
    run: suspend () -> T,
): T {
    try {
        withTimeout(timeout) {
            semaphore.acquire()
        }
    } catch (e: Throwable) {
        println("timeout acquire")
        throw e
    }
    try {
        val result = run()
        return result
    } finally {
        semaphore.release()
    }
}
