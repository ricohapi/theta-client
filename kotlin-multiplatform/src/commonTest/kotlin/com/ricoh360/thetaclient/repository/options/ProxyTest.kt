package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.Proxy
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ProxyTest {
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
     * Get option _proxy.
     */
    @Test
    fun getOptionProxyTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.Proxy
        )
        val stringOptionNames = listOf(
            "_proxy"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_proxy_not_use.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.proxy?.use, false)
        assertEquals(options.proxy?.url, "")
        assertEquals(options.proxy?.port, 8080)
    }

    /**
     * Set option _proxy.
     */
    @Test
    fun setOptionProxyTest() = runTest {
        val value = Pair(ThetaRepository.Proxy(use = false, url = "", port = 8080), Proxy(use = false, url = "", port = 8080))

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, proxy = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            proxy = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionProxyTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.Proxy(use = false, url = "", port = 8080), Proxy(use = false, url = "", port = 8080)),
        )

        values.forEach {
            val orgOptions = Options(
                _proxy = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.proxy, it.first, "proxy ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                proxy = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._proxy, it.second, "_proxy ${it.second}")
        }
    }
}