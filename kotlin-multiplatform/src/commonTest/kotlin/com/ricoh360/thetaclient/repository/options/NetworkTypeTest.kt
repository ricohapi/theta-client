package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.NetworkType
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkTypeTest {
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
     * Get option networkType.
     */
    @Test
    fun getOptionNetworkTypeTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.NetworkType
        )
        val stringOptionNames = listOf(
            "_networkType"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_network_type_direct.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.networkType, ThetaRepository.NetworkTypeEnum.DIRECT, "networkType")
    }

    /**
     * Get option UNKNOWN.
     */
    @Test
    fun getOptionUnknownTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.NetworkType
        )
        val stringOptionNames = listOf(
            "_networkType"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_network_type_unknown.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.networkType, ThetaRepository.NetworkTypeEnum.UNKNOWN, "networkType")
    }

    /**
     * Set option networkType.
     */
    @Test
    fun setOptionNetworkTypeTest() = runTest {
        val value = Pair(ThetaRepository.NetworkTypeEnum.DIRECT, NetworkType.DIRECT)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, networkType = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            networkType = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionNetworkTypeTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.NetworkTypeEnum.UNKNOWN, NetworkType.UNKNOWN),
            Pair(ThetaRepository.NetworkTypeEnum.CLIENT, NetworkType.CLIENT),
            Pair(ThetaRepository.NetworkTypeEnum.DIRECT, NetworkType.DIRECT),
            Pair(ThetaRepository.NetworkTypeEnum.ETHERNET, NetworkType.ETHERNET),
            Pair(ThetaRepository.NetworkTypeEnum.OFF, NetworkType.OFF),
            Pair(ThetaRepository.NetworkTypeEnum.LTE_D, NetworkType.LTE_D),
            Pair(ThetaRepository.NetworkTypeEnum.LTE_DU, NetworkType.LTE_DU),
            Pair(ThetaRepository.NetworkTypeEnum.LTE_01S, NetworkType.LTE_01S),
            Pair(ThetaRepository.NetworkTypeEnum.LTE_X3, NetworkType.LTE_X3),
            Pair(ThetaRepository.NetworkTypeEnum.LTE_P1, NetworkType.LTE_P1),
            Pair(ThetaRepository.NetworkTypeEnum.LTE_K2, NetworkType.LTE_K2),
            Pair(ThetaRepository.NetworkTypeEnum.LTE_K, NetworkType.LTE_K),
        )

        values.forEach {
            val orgOptions = Options(
                _networkType = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.networkType, it.first, "networkType ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                networkType = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._networkType, it.second, "networkType ${it.second}")
        }
    }
}