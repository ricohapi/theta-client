package com.ricoh360.thetaclient

import korlibs.crypto.md5
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
    scope: CoroutineScope,
    timeout: Long,
    run: suspend () -> T,
): T {
    val deferred = CompletableDeferred<T>()
    scope.launch {
        runBlocking {
            try {
                withTimeout(timeout) {
                    val result = run()
                    deferred.complete(result)
                }
            } catch (e: Throwable) {
                deferred.completeExceptionally(e)
            }
        }
    }
    return deferred.await()
}
