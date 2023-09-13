package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class BitrateTest {
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
     * Get option
     */
    @Test
    fun getOptionTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.Bitrate
        )
        val stringOptionNames = listOf(
            "_bitrate"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_bitrate_fine.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.bitrate, ThetaRepository.BitrateEnum.FINE)
    }

    /**
     * Get option
     */
    @Test
    fun getOptionNumberBitrateTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.Bitrate
        )
        val stringOptionNames = listOf(
            "_bitrate"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_bitrate_100000000.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.bitrate, ThetaRepository.BitrateNumber(100000000))
    }

    /**
     * Set option
     */
    @Test
    fun setOptionTest() = runTest {
        val value = Pair(ThetaRepository.BitrateEnum.FINE, "Fine")

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, bitrate = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            bitrate = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Set option
     */
    @Test
    fun setOptionNumberBitrateTest() = runTest {
        val value = Pair(ThetaRepository.BitrateNumber(100000000), "100000000")

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, bitrate = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            bitrate = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.BitrateEnum.AUTO, "Auto"),
            Pair(ThetaRepository.BitrateEnum.FINE, "Fine"),
            Pair(ThetaRepository.BitrateEnum.NORMAL, "Normal"),
            Pair(ThetaRepository.BitrateEnum.ECONOMY, "Economy"),
            Pair(ThetaRepository.BitrateNumber(12800000), "12800000"),
        )

        values.forEach {
            val orgOptions = Options(
                _bitrate = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.bitrate, it.first, "bitrate ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                bitrate = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._bitrate, it.second, "_bitrate ${it.second}")
        }
    }
}