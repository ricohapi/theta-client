package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.EthernetConfig
import com.ricoh360.thetaclient.transferred.IpAddressAllocation
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.Proxy
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class EthernetConfigTest {
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
     * Get option _ethernetConfig.
     */
    @Test
    fun getOptionTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.EthernetConfig
        )
        val stringOptionNames = listOf(
            "_ethernetConfig"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_ethernet_config.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.ethernetConfig?.usingDhcp, true)
        assertEquals(options.ethernetConfig?.ipAddress, "192.168.1.10")
        assertEquals(options.ethernetConfig?.subnetMask, "255.255.0.0")
        assertEquals(options.ethernetConfig?.defaultGateway, "192.168.1.11")
        assertEquals(options.ethernetConfig?.proxy?.use, true)
        assertEquals(options.ethernetConfig?.proxy?.url, "192.168.1.2")
        assertEquals(options.ethernetConfig?.proxy?.port, 8888)
        assertEquals(options.ethernetConfig?.proxy?.userid, null)
        assertEquals(options.ethernetConfig?.proxy?.password, null)
    }

    /**
     * Set option _ethernetConfig.
     */
    @Test
    fun setOptionNotUseProxyTest() = runTest {
        val value = Pair(
            ThetaRepository.EthernetConfig(
                usingDhcp = true,
                ipAddress = "192.168.1.2",
                subnetMask = "255.255.0.0",
                defaultGateway = "192.168.1.12",
                proxy = ThetaRepository.Proxy(use = false)
            ),
            EthernetConfig(
                ipAddressAllocation = IpAddressAllocation.DYNAMIC,
                ipAddress = "192.168.1.2",
                subnetMask = "255.255.0.0",
                defaultGateway = "192.168.1.12",
                _proxy = Proxy(use = false)
            )
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, ethernetConfig = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            ethernetConfig = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Set option _ethernetConfig.
     */
    @Test
    fun setOptionUseProxyTest() = runTest {
        val value = Pair(
            ThetaRepository.EthernetConfig(
                usingDhcp = true,
                ipAddress = "192.168.1.2",
                subnetMask = "255.255.0.0",
                defaultGateway = "192.168.1.12",
                proxy = ThetaRepository.Proxy(use = true, url = "192.168.1.3", port = 80)
            ),
            EthernetConfig(
                ipAddressAllocation = IpAddressAllocation.DYNAMIC,
                ipAddress = "192.168.1.2",
                subnetMask = "255.255.0.0",
                defaultGateway = "192.168.1.12",
                _proxy = Proxy(use = true, url = "192.168.1.3", port = 80)
            )
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, ethernetConfig = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            ethernetConfig = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Set option _ethernetConfig.
     */
    @Test
    fun setOptionUseProxyWithUserTest() = runTest {
        val value = Pair(
            ThetaRepository.EthernetConfig(
                usingDhcp = true,
                ipAddress = "192.168.1.2",
                subnetMask = "255.255.0.0",
                defaultGateway = "192.168.1.12",
                proxy = ThetaRepository.Proxy(use = true, url = "192.168.1.3", port = 80, userid = "abc", password = "123")
            ),
            EthernetConfig(
                ipAddressAllocation = IpAddressAllocation.DYNAMIC,
                ipAddress = "192.168.1.2",
                subnetMask = "255.255.0.0",
                defaultGateway = "192.168.1.12",
                _proxy = Proxy(use = true, url = "192.168.1.3", port = 80, userid = "abc", password = "123")
            )
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, ethernetConfig = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            ethernetConfig = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertTest() = runTest {
        val values = listOf(
            Pair(
                ThetaRepository.EthernetConfig(
                    usingDhcp = false,
                    ipAddress = "192.168.1.3",
                    subnetMask = "255.255.0.0",
                    defaultGateway = "192.168.1.13",
                    proxy = ThetaRepository.Proxy(use = true, url = "192.168.1.3", port = 80, userid = "abc", password = "123")
                ),
                EthernetConfig(
                    ipAddressAllocation = IpAddressAllocation.STATIC,
                    ipAddress = "192.168.1.3",
                    subnetMask = "255.255.0.0",
                    defaultGateway = "192.168.1.13",
                    _proxy = Proxy(use = true, url = "192.168.1.3", port = 80, userid = "abc", password = "123")
                )
            )
        )

        values.forEach {
            val orgOptions = Options(
                _ethernetConfig = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.ethernetConfig, it.first, "ethernetConfig ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                ethernetConfig = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._ethernetConfig, it.second, "_ethernetConfig ${it.second}")
        }
    }
}