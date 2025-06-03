package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.AccessInfo
import com.ricoh360.thetaclient.transferred.DhcpLeaseAddress
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.WlanFrequencyAccessInfo
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AccessInfoTest {
    private val endpoint = "http://192.168.1.1:80/"

    @BeforeTest
    fun setup() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @AfterTest
    fun teardown() {
        MockApiClient.status = HttpStatusCode.OK
    }

    /**
     * Get option _accessInfo.
     */
    @Test
    fun getOptionTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.AccessInfo
        )
        val stringOptionNames = listOf(
            "_accessInfo"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_access_info.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.accessInfo?.ssid, "ssid_test")
        assertEquals(options.accessInfo?.ipAddress, "192.168.1.2")
        assertEquals(options.accessInfo?.subnetMask, "255.255.0.0")
        assertEquals(options.accessInfo?.defaultGateway, "192.168.1.12")
        assertEquals(options.accessInfo?.dns1, "192.168.1.55")
        assertEquals(options.accessInfo?.dns2, "192.168.1.66")
        assertEquals(options.accessInfo?.proxyURL, "http://192.168.1.3")
        assertEquals(
            options.accessInfo?.frequency,
            ThetaRepository.WlanFrequencyAccessInfoEnum.GHZ_2_4
        )
        assertEquals(options.accessInfo?.wlanSignalStrength, -60)
        assertEquals(options.accessInfo?.wlanSignalLevel, 4)
        assertEquals(options.accessInfo?.lteSignalStrength, 0)
        assertEquals(options.accessInfo?.lteSignalLevel, 0)
        assertEquals(options.accessInfo?.dhcpLeaseAddress?.size, 1)
        assertEquals(options.accessInfo?.dhcpLeaseAddress?.get(0)?.ipAddress, "192.168.1.5")
        assertEquals(options.accessInfo?.dhcpLeaseAddress?.get(0)?.macAddress, "58:38:79:12:34:56")
        assertEquals(options.accessInfo?.dhcpLeaseAddress?.get(0)?.hostName, "Macbook-Pro")
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertTest() = runTest {
        val values = listOf(
            Pair(
                ThetaRepository.AccessInfo(
                    ssid = "ssid_test",
                    ipAddress = "192.168.1.2",
                    subnetMask = "255.255.0.0",
                    defaultGateway = "192.168.1.12",
                    dns1 = "192.168.1.55",
                    dns2 = "192.168.1.66",
                    proxyURL = "http://192.168.1.3",
                    frequency = ThetaRepository.WlanFrequencyAccessInfoEnum.GHZ_2_4,
                    wlanSignalStrength = -60,
                    wlanSignalLevel = 4,
                    lteSignalStrength = 0,
                    lteSignalLevel = 0,
                    dhcpLeaseAddress = listOf(
                        ThetaRepository.DhcpLeaseAddress(ipAddress = "192.168.1.5", macAddress = "192.168.1.6", hostName = "192.168.1.7"),
                        ThetaRepository.DhcpLeaseAddress(ipAddress = "192.168.1.8", macAddress = "192.168.1.9", hostName = "192.168.1.10")
                    )
                ),
                AccessInfo(
                    ssid = "ssid_test",
                    ipAddress = "192.168.1.2",
                    subnetMask = "255.255.0.0",
                    defaultGateway = "192.168.1.12",
                    dns1 = "192.168.1.55",
                    dns2 = "192.168.1.66",
                    proxyURL = "http://192.168.1.3",
                    frequency = WlanFrequencyAccessInfo.GHZ_2_4,
                    wlanSignalStrength = -60,
                    wlanSignalLevel = 4,
                    lteSignalStrength = 0,
                    lteSignalLevel = 0,
                    dhcpLeaseAddress = listOf(
                        DhcpLeaseAddress(ipAddress = "192.168.1.5", macAddress = "192.168.1.6", hostName = "192.168.1.7"),
                        DhcpLeaseAddress(ipAddress = "192.168.1.8", macAddress = "192.168.1.9", hostName = "192.168.1.10")
                    )
                )
            )
        )

        values.forEach {
            val orgOptions = Options(
                _accessInfo = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.accessInfo, it.first, "accessInfo ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                accessInfo = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._accessInfo, it.second, "_accessInfo ${it.second}")
        }
    }

    @Test
    fun convertWlanFrequencyAccessInfoTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.WlanFrequencyAccessInfoEnum.UNKNOWN, WlanFrequencyAccessInfo.UNKNOWN),
            Pair(ThetaRepository.WlanFrequencyAccessInfoEnum.GHZ_2_4, WlanFrequencyAccessInfo.GHZ_2_4),
            Pair(ThetaRepository.WlanFrequencyAccessInfoEnum.GHZ_5_2, WlanFrequencyAccessInfo.GHZ_5_2),
            Pair(ThetaRepository.WlanFrequencyAccessInfoEnum.GHZ_5_8, WlanFrequencyAccessInfo.GHZ_5_8),
            Pair(ThetaRepository.WlanFrequencyAccessInfoEnum.INITIAL_VALUE, WlanFrequencyAccessInfo.INITIAL_VALUE),
        )

        values.forEach {
            val orgOptions = Options(
                _accessInfo = AccessInfo(
                    ssid = "ssid_test",
                    ipAddress = "192.168.1.2",
                    subnetMask = "255.255.0.0",
                    defaultGateway = "192.168.1.12",
                    dns1 = "192.168.1.55",
                    dns2 = "192.168.1.66",
                    proxyURL = "http://192.168.1.3",
                    frequency = it.second,
                    wlanSignalStrength = -60,
                    wlanSignalLevel = 4,
                    lteSignalStrength = 0,
                    lteSignalLevel = 0,
                    dhcpLeaseAddress = listOf(
                        DhcpLeaseAddress(ipAddress = "192.168.1.5", macAddress = "192.168.1.6", hostName = "192.168.1.7"),
                        DhcpLeaseAddress(ipAddress = "192.168.1.8", macAddress = "192.168.1.9", hostName = "192.168.1.10")
                    )
                )
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.accessInfo?.frequency, it.first, "WlanFrequencyAccessInfo ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                accessInfo = ThetaRepository.AccessInfo(
                    ssid = "ssid_test",
                    ipAddress = "192.168.1.2",
                    subnetMask = "255.255.0.0",
                    defaultGateway = "192.168.1.12",
                    dns1 = "192.168.1.55",
                    dns2 = "192.168.1.66",
                    proxyURL = "http://192.168.1.3",
                    frequency = it.first,
                    wlanSignalStrength = -60,
                    wlanSignalLevel = 4,
                    lteSignalStrength = 0,
                    lteSignalLevel = 0,
                    dhcpLeaseAddress = listOf(
                        ThetaRepository.DhcpLeaseAddress(ipAddress = "192.168.1.5", macAddress = "192.168.1.6", hostName = "192.168.1.7"),
                        ThetaRepository.DhcpLeaseAddress(ipAddress = "192.168.1.8", macAddress = "192.168.1.9", hostName = "192.168.1.10")
                    )
                )
            )
            val options = orgOptions.toOptions()
            assertEquals(options._accessInfo?.frequency, it.second, "WlanFrequencyAccessInfo ${it.second}")
        }
    }
}