package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.ApiClient
import com.ricoh360.thetaclient.DigestAuth
import com.ricoh360.thetaclient.KEY_AUTH_NONCE
import com.ricoh360.thetaclient.KEY_AUTH_QOP
import com.ricoh360.thetaclient.KEY_AUTH_REALM
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import io.ktor.http.*
import io.ktor.http.auth.AuthScheme
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.http.auth.parseAuthorizationHeader
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ThetaRepositoryClientModeTest {
    private val endpoint = "http://192.168.1.1:80/"

    @BeforeTest
    fun setup() {
        MockApiClient.reset()
    }

    @AfterTest
    fun teardown() {
        MockApiClient.reset()
    }

    private fun checkAuthHeader(authHeaderString: String, username: String, uri: String, realm: String, qop: String, nonce: String) {
        val authHeader = parseAuthorizationHeader(authHeaderString) as HttpAuthHeader.Parameterized
        assertEquals(authHeader.parameter(KEY_AUTH_REALM), realm)
        assertEquals(authHeader.parameter(KEY_AUTH_QOP), qop)
        assertEquals(authHeader.parameter(KEY_AUTH_NONCE), nonce)

        assertEquals(authHeader.parameter("username"), username)
        assertEquals(authHeader.parameter("uri"), uri)

        assertNotNull(authHeader.parameter("response"))
        val cnonce = authHeader.parameter("cnonce")
        assertEquals(cnonce?.length, 32)
    }

    /**
     * New instance. for client mode
     */
    @Test
    fun newInstanceForClientModeTest() = runTest {
        val username = "THETAXX01234567"
        val password = "15105582abc"
        val digestAuth = DigestAuth(username, password)

        // setup
        val statusArray = arrayOf(
            HttpStatusCode.Unauthorized,
            HttpStatusCode.OK,
            HttpStatusCode.Unauthorized,
            HttpStatusCode.OK,
        )
        val realm = "RICOH THETA Z1"
        val qop = "auth"
        val nonce = "nay8d0QmRP+XN0E8r0NdZg=="
        val wwwAuthHeader = headersOf(
            HttpHeaders.WWWAuthenticate,
            HttpAuthHeader.Parameterized(
                AuthScheme.Digest,
                linkedMapOf(
                    "realm" to realm,
                    "qop" to qop,
                    "nonce" to nonce,
                    "algorithm" to "MD5",
                ),
            ).render(),
        )

        val headerArray = arrayOf(
            wwwAuthHeader,
            null,
            wwwAuthHeader,
            null,
        )
        val responseArray = arrayOf(
            "",
            Resource("src/commonTest/resources/info/info_z1.json").readText(),
            "",
            Resource("src/commonTest/resources/getOptions/get_options_init_z1_done.json").readText(),
        )
        val requestPathArray = arrayOf(
            "/osc/info",
            "/osc/info",
            "/osc/commands/execute",
            "/osc/commands/execute",
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++

            // check request
            assertEquals(request.url.encodedPath, requestPathArray[index], "request path")
            val authHeader = request.headers[HttpHeaders.Authorization]
            when (index) {
                0 -> {
                    assertNull(authHeader)
                }
                1 -> {
                    assertNotNull(authHeader)
                    checkAuthHeader(authHeader, username, request.url.encodedPath, realm, qop, nonce)
                }
                2 -> {
                    assertNull(authHeader)
                }
                3 -> {
                    assertNotNull(authHeader)
                    checkAuthHeader(authHeader, username, request.url.encodedPath, realm, qop, nonce)
                }
            }

            MockApiClient.status = statusArray[index]
            MockApiClient.responseHeaders = headerArray[index]
            ByteReadChannel(responseArray[index])
        }

        // execute
        ThetaRepository.newInstance(endpoint, ThetaRepository.Config(clientMode = digestAuth))
        assertEquals(counter, 4)
        assertEquals(ApiClient.digestAuth?.username, username)
        assertEquals(ApiClient.digestAuth?.password, password)
    }

    /**
     * Error authorization exception to client mode
     */
    @Test
    fun authorizationExceptionTest() = runTest {
        val username = "THETAXX01234567"
        val password = "15105582abc"
        val digestAuth = DigestAuth(username, password)

        // setup
        val statusArray = arrayOf(
            HttpStatusCode.Unauthorized,
            HttpStatusCode.Unauthorized,
        )
        val realm = "RICOH THETA Z1"
        val qop = "auth"
        val nonce = "nay8d0QmRP+XN0E8r0NdZg=="
        val wwwAuthHeader = headersOf(
            HttpHeaders.WWWAuthenticate,
            HttpAuthHeader.Parameterized(
                AuthScheme.Digest,
                linkedMapOf(
                    "realm" to realm,
                    "qop" to qop,
                    "nonce" to nonce,
                    "algorithm" to "MD5",
                ),
            ).render(),
        )

        val headerArray = arrayOf(
            wwwAuthHeader,
            wwwAuthHeader,
        )
        val responseArray = arrayOf(
            "",
            Resource("src/commonTest/resources/info/info_z1.json").readText(),
        )
        val requestPathArray = arrayOf(
            "/osc/info",
            "/osc/info",
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++
            // check request
            assertEquals(request.url.encodedPath, requestPathArray[index], "request path")
            val authHeader = request.headers[HttpHeaders.Authorization]
            when (index) {
                0 -> {
                    assertNull(authHeader)
                }
                1 -> {
                    assertNotNull(authHeader)
                }
                2 -> {
                    assertTrue(false, "newInstance")
                }
            }

            MockApiClient.status = statusArray[index]
            MockApiClient.responseHeaders = headerArray[index]
            ByteReadChannel(responseArray[index])
        }

        // execute
        try {
            ThetaRepository.newInstance(endpoint, ThetaRepository.Config(clientMode = digestAuth))
            assertTrue(false, "newInstance")
        } catch (e: ThetaRepository.ThetaUnauthorizedException) {
            assertTrue(e.message!!.indexOf("Unauthorized") >= 0, "exception Unauthorized")
        }
        assertEquals(counter, 2)
        assertEquals(ApiClient.digestAuth?.username, username)
        assertEquals(ApiClient.digestAuth?.password, password)
    }

    /**
     * Error authorization exception to no unauthorized header
     */
    @Test
    fun authorizationEmptyExceptionTest() = runTest {
        val username = "THETAXX01234567"
        val password = "15105582abc"
        val digestAuth = DigestAuth(username, password)

        // setup
        val statusArray = arrayOf(
            HttpStatusCode.Unauthorized,
        )

        val responseArray = arrayOf(
            "",
        )
        val requestPathArray = arrayOf(
            "/osc/info",
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++
            // check request
            assertEquals(request.url.encodedPath, requestPathArray[index], "request path")
            val authHeader = request.headers[HttpHeaders.Authorization]
            when (index) {
                0 -> {
                    assertNull(authHeader)
                }
                1 -> {
                    assertTrue(false, "newInstance")
                }
            }

            MockApiClient.status = statusArray[index]
            ByteReadChannel(responseArray[index])
        }

        // execute
        try {
            ThetaRepository.newInstance(endpoint, ThetaRepository.Config(clientMode = digestAuth))
            assertTrue(false, "newInstance")
        } catch (e: ThetaRepository.ThetaUnauthorizedException) {
            assertTrue(e.message!!.indexOf("Unauthorized") >= 0, "exception Unauthorized")
        }
        assertEquals(counter, 1)
        assertEquals(ApiClient.digestAuth?.username, username)
        assertEquals(ApiClient.digestAuth?.password, password)
    }

    /**
     * Error authorization exception to no client mode setting
     */
    @Test
    fun unauthorizedExceptionTest() = runTest {
        // setup
        val statusArray = arrayOf(
            HttpStatusCode.Unauthorized,
            HttpStatusCode.Unauthorized,
        )
        val realm = "RICOH THETA Z1"
        val qop = "auth"
        val nonce = "nay8d0QmRP+XN0E8r0NdZg=="
        val wwwAuthHeader = headersOf(
            HttpHeaders.WWWAuthenticate,
            HttpAuthHeader.Parameterized(
                AuthScheme.Digest,
                linkedMapOf(
                    "realm" to realm,
                    "qop" to qop,
                    "nonce" to nonce,
                    "algorithm" to "MD5",
                ),
            ).render(),
        )

        val headerArray = arrayOf(
            wwwAuthHeader,
        )
        val responseArray = arrayOf(
            "",
        )
        val requestPathArray = arrayOf(
            "/osc/info",
        )
        var counter = 0
        MockApiClient.onRequest = { request ->
            val index = counter++
            // check request
            assertEquals(request.url.encodedPath, requestPathArray[index], "request path")
            val authHeader = request.headers[HttpHeaders.Authorization]
            when (index) {
                0 -> {
                    assertNull(authHeader)
                }
                1 -> {
                    assertTrue(false, "newInstance")
                }
            }

            MockApiClient.status = statusArray[index]
            MockApiClient.responseHeaders = headerArray[index]
            ByteReadChannel(responseArray[index])
        }

        // execute
        try {
            ThetaRepository.newInstance(endpoint)
            assertTrue(false, "newInstance")
        } catch (e: ThetaRepository.ThetaUnauthorizedException) {
            assertTrue(e.message!!.indexOf("Unauthorized") >= 0, "exception Unauthorized")
        }
        assertEquals(counter, 1)
        assertNull(ApiClient.digestAuth)
    }

    /**
     * Check password setting
     */
    @Test
    fun digestAuthPasswordTest() = runTest {
        val serialNo = "THETAXX01234567"
        val defaultPassword = "01234567"
        val password = "15105582abc"
        val otherName = "00000XX01234567"
        val shortOtherName = "short"

        val digestAuth1 = DigestAuth(serialNo, password)
        assertEquals(digestAuth1.username, serialNo)
        assertEquals(digestAuth1.password, password)

        val digestAuth2 = DigestAuth(serialNo)
        assertEquals(digestAuth2.username, serialNo)
        assertEquals(digestAuth2.password, defaultPassword)

        val digestAuth3 = DigestAuth(otherName)
        assertEquals(digestAuth3.username, otherName)
        assertEquals(digestAuth3.password, "")

        val digestAuth4 = DigestAuth(shortOtherName)
        assertEquals(digestAuth4.username, shortOtherName)
        assertEquals(digestAuth4.password, "")
    }
}
