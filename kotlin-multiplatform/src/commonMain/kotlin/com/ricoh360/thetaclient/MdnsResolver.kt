/*
 * mDNS host name resolver for the client mode
 */
package com.ricoh360.thetaclient

import io.ktor.http.Url
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.Datagram
import io.ktor.network.sockets.InetSocketAddress
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.io.Buffer
import kotlinx.io.readByteArray

/**
 * Resolves "<name>.local", the host name of THETA in the client mode, to an IPv4 address.
 * The hosts in [mdnsHostPrefixes] are resolved with [resolveMdnsHost] of each platform.
 *
 * [resolve] is a one-shot multicast DNS query used on Android, which often cannot resolve mDNS host names.
 * A query sent from a port other than 5353 is answered by unicast to that port (RFC 6762 6.7),
 * so receiving multicast packets is not needed.
 */
@Suppress("MagicNumber") // byte offsets and sizes of the DNS message format (RFC 1035)
internal object MdnsResolver {
    private const val MDNS_ADDRESS = "224.0.0.251"
    private const val MDNS_PORT = 5353
    private const val TYPE_A = 1
    private const val CLASS_IN = 1
    private const val DNS_HEADER_SIZE = 12
    private const val IPV4_SIZE = 4
    private const val TIMEOUT_MILLIS = 1_000L

    /** Address of THETA in the AP mode */
    private const val THETA_AP_ADDRESS = "192.168.1.1"

    /**
     * Number of the tries until resolved.
     * THETA may not answer for a while just after joining the network.
     */
    private const val RETRY_COUNT = 5

    private val logger = ThetaApiLogger()

    /**
     * Replaces the "<name>.local" host of [endpoint] with its IP address.
     *
     * @param endpoint URL such as "http://THETAYR12345678.local"
     * @param retryCount Number of the tries until resolved
     * @param resolver Resolves the host to an IPv4 address with [retryCount] tries.
     * @return [endpoint] whose host is replaced, or [endpoint] as it is when it is not a target.
     * @exception ThetaRepository.NotConnectedException The target cannot be resolved, or is resolved to [THETA_AP_ADDRESS].
     * It is not left to the system resolver, which may resolve it to the IPv6 address THETA refuses.
     */
    suspend fun resolveEndpoint(
        endpoint: String,
        retryCount: Int = RETRY_COUNT,
        resolver: suspend (String, Int) -> String? = ::resolveMdnsHost,
    ): String {
        val host = runCatching { Url(endpoint).host }.getOrNull()
        if (host == null || !isTarget(host)) return endpoint
        val ipAddress = try {
            resolver(host, retryCount)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logger.log("mDNS: $host error: $e")
            null
        }
        if (ipAddress == null) {
            logger.log("mDNS: no answer for $host")
            throw ThetaRepository.NotConnectedException("Cannot resolve $host")
        }
        // assumes THETA never gets 192.168.1.1 in the client mode network; check the subnet if it does.
        if (ipAddress == THETA_AP_ADDRESS) {
            // While the phone is still on the access point of THETA, it answers its AP mode address,
            // which is another device (usually the router) after the phone moves to the client mode network.
            logger.log("mDNS: $host -> $ipAddress is the AP mode address")
            throw ThetaRepository.NotConnectedException("$host is not in client mode network yet")
        }
        logger.log("mDNS: $host -> $ipAddress")
        return endpoint.replaceFirst(host, ipAddress)
    }

    internal fun isTarget(host: String, prefixes: List<String> = mdnsHostPrefixes): Boolean =
        host.endsWith(".local", ignoreCase = true) &&
            prefixes.any { host.startsWith(it, ignoreCase = true) }

    /**
     * Resolves [host] to an IPv4 address.
     *
     * @param retryCount Number of the queries sent until answered
     * @return IPv4 address such as "192.168.1.23", or null when no answer
     */
    internal suspend fun resolve(host: String, retryCount: Int): String? {
        val query = buildQuery(host)
        val selector = SelectorManager(Dispatchers.Default)
        try {
            val socket = aSocket(selector).udp().bind()
            try {
                repeat(retryCount) {
                    val packet = Buffer().apply { write(query) }
                    socket.send(Datagram(packet, InetSocketAddress(MDNS_ADDRESS, MDNS_PORT)))
                    val ipAddress = withTimeoutOrNull(TIMEOUT_MILLIS) {
                        var answer: String? = null
                        while (answer == null) {
                            answer = parseAnswer(socket.receive().packet.readByteArray(), host)
                        }
                        answer
                    }
                    if (ipAddress != null) return ipAddress
                }
            } finally {
                socket.close()
            }
        } finally {
            selector.close()
        }
        return null
    }

    /**
     * Builds a DNS query of the A record of [host].
     */
    internal fun buildQuery(host: String): ByteArray {
        val buffer = Buffer()
        // Header: ID, flags, QDCOUNT = 1, ANCOUNT, NSCOUNT, ARCOUNT
        listOf(0, 0, 1, 0, 0, 0).forEach { buffer.writeShort(it.toShort()) }
        host.trimEnd('.').split('.').forEach { label ->
            val bytes = label.encodeToByteArray()
            buffer.writeByte(bytes.size.toByte())
            buffer.write(bytes)
        }
        buffer.writeByte(0)
        buffer.writeShort(TYPE_A.toShort())
        buffer.writeShort(CLASS_IN.toShort())
        return buffer.readByteArray()
    }

    /**
     * Gets the IPv4 address of [host] from a DNS response.
     *
     * @return IPv4 address, or null when [data] is not a response including the A record of [host]
     */
    internal fun parseAnswer(data: ByteArray, host: String): String? = runCatching {
        val isResponse = (u16(data, 2) and 0x8000) != 0
        if (!isResponse) return null
        val questionCount = u16(data, 4)
        val recordCount = u16(data, 6) + u16(data, 8) + u16(data, 10)
        var offset = DNS_HEADER_SIZE
        repeat(questionCount) {
            offset = readName(data, offset).second + 4 // QTYPE, QCLASS
        }
        repeat(recordCount) {
            val (name, next) = readName(data, offset)
            val type = u16(data, next)
            val length = u16(data, next + 8)
            val rdata = next + 10 // TYPE, CLASS, TTL, RDLENGTH
            if (type == TYPE_A && length == IPV4_SIZE && name.equals(host.trimEnd('.'), ignoreCase = true)) {
                return (0 until IPV4_SIZE).joinToString(".") { (data[rdata + it].toInt() and 0xff).toString() }
            }
            offset = rdata + length
        }
        null
    }.getOrNull()

    private fun u16(data: ByteArray, offset: Int): Int =
        ((data[offset].toInt() and 0xff) shl 8) or (data[offset + 1].toInt() and 0xff)

    /**
     * Reads a domain name, following compression pointers.
     *
     * @return The name and the offset next to the name at [offset]
     */
    private fun readName(data: ByteArray, offset: Int): Pair<String, Int> {
        val labels = mutableListOf<String>()
        var position = offset
        var next = -1
        var jumps = 0
        while (true) {
            val length = data[position].toInt() and 0xff
            when {
                length == 0 -> {
                    if (next < 0) next = position + 1
                    break
                }
                length and 0xc0 == 0xc0 -> { // compression pointer
                    if (next < 0) next = position + 2
                    check(++jumps < 64) { "Too many compression pointers" }
                    position = u16(data, position) and 0x3fff
                }
                else -> {
                    labels.add(data.decodeToString(position + 1, position + 1 + length))
                    position += 1 + length
                }
            }
        }
        return Pair(labels.joinToString("."), next)
    }
}
