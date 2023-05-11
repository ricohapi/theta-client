package com.ricoh360.thetaclient

import com.soywiz.krypto.md5
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.http.auth.parseAuthorizationHeader
import io.ktor.http.encodedPath
import kotlinx.coroutines.runBlocking

const val DEFAULT_AUTH_QOP = "auth"
internal const val HEX_CHARACTERS = "0123456789abcdef"
internal const val DEFAULT_AUTH_NC = "00000001"
internal const val KEY_AUTH_REALM = "realm"
internal const val KEY_AUTH_NONCE = "nonce"
internal const val KEY_AUTH_QOP = "qop"

class DigestAuth(val username: String, password: String? = null) {
    val password: String

    internal var realm: String? = null
    internal var nonce: String? = null
    internal var qop: String = DEFAULT_AUTH_QOP

    companion object {
        internal const val SERIAL_NO_PREFIX_LENGTH = 5 + 2 // THETA + XX
        internal const val NAME_THETA = "THETA"
    }
    init {
        this.password = password ?: let {
            if (username.indexOf(NAME_THETA) == 0 && username.length > SERIAL_NO_PREFIX_LENGTH) {
                username.removeRange(0, SERIAL_NO_PREFIX_LENGTH)
            } else {
                ""
            }
        }
    }

    internal fun updateServerInfo(responseHeader: HttpAuthHeader.Parameterized?) {
        val realm = responseHeader?.parameter(KEY_AUTH_REALM)
        val nonce = responseHeader?.parameter(KEY_AUTH_NONCE)
        val qop = responseHeader?.parameter(KEY_AUTH_QOP) ?: DEFAULT_AUTH_QOP

        setServerInfo(realm, nonce, qop)
    }

    fun setServerInfo(realm: String?, nonce: String?, qop: String = DEFAULT_AUTH_QOP) {
        this.realm = realm
        this.nonce = nonce
        this.qop = qop
    }

    fun makeDigest(
        uri: String,
        method: String,
    ): String {
        return makeDigest(username, password, uri, method, realm, nonce, qop)
    }
}

fun makeDigest(
    username: String,
    password: String,
    uri: String,
    method: String,
    realm: String?,
    nonce: String?,
    qop: String = DEFAULT_AUTH_QOP,
): String {
    val cnonce = randomUUID().replace("-", "")
    val nc = DEFAULT_AUTH_NC

    val a1 = md5("$username:$realm:$password")
    val a2 = md5("$method:$uri")

    val response = md5("$a1:$nonce:$nc:$cnonce:$qop:$a2")

    return "Digest username=\"$username\", realm=\"$realm\", uri=\"$uri\", nonce=\"$nonce\", nc=$nc, qop=\"$qop\", cnonce=\"$cnonce\", response=\"$response\""
}

internal fun md5(data: String): String {
    val byteArray = data.encodeToByteArray()
    val hash = byteArray.md5()
    return bytesToHex(hash.bytes)
}

internal fun bytesToHex(data: ByteArray): String {
    val hexArray = HEX_CHARACTERS.toCharArray()
    val hexChars = CharArray(data.size * 2)
    for (i in data.indices) {
        val v = data[i].toInt() and 0xFF
        hexChars[i * 2] = hexArray[v ushr 4]
        hexChars[i * 2 + 1] = hexArray[v and 0x0F]
    }
    return hexChars.concatToString()
}

internal fun setupDigestAuth(httpClient: HttpClient) {
    runBlocking {
        httpClient.plugin(HttpSend).intercept { request ->
            val originalCall = execute(request)
            if (originalCall.response.status == HttpStatusCode.Unauthorized) {
                ApiClient.digestAuth?.let {
                    originalCall.response.headers[HttpHeaders.WWWAuthenticate]?.let { wwwAuth ->
                        val authHeader = parseAuthorizationHeader(wwwAuth) as HttpAuthHeader.Parameterized
                        it.updateServerInfo(authHeader)
                        request.headers.append(HttpHeaders.Authorization, it.makeDigest(request.url.encodedPath, request.method.value))
                        execute(request)
                    }
                } ?: originalCall
            } else {
                originalCall
            }
        }
    }
}
