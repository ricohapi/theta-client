package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
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
class IsoTest {
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
     * Get option iso.
     */
    @Test
    fun getOptionIsoTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.Iso
        )
        val stringOptionNames = listOf(
            "iso"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_iso_100.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.iso, ThetaRepository.IsoEnum.ISO_100, "iso")
    }

    /**
     * Set option iso.
     */
    @Test
    fun setOptionIsoTest() = runTest {
        val value = Pair(ThetaRepository.IsoEnum.ISO_100, 100)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, iso = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            iso = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionIsoTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.IsoEnum.ISO_AUTO, 0),
            Pair(ThetaRepository.IsoEnum.ISO_50, 50),
            Pair(ThetaRepository.IsoEnum.ISO_64, 64),
            Pair(ThetaRepository.IsoEnum.ISO_80, 80),
            Pair(ThetaRepository.IsoEnum.ISO_100, 100),
            Pair(ThetaRepository.IsoEnum.ISO_125, 125),
            Pair(ThetaRepository.IsoEnum.ISO_160, 160),
            Pair(ThetaRepository.IsoEnum.ISO_200, 200),
            Pair(ThetaRepository.IsoEnum.ISO_250, 250),
            Pair(ThetaRepository.IsoEnum.ISO_320, 320),
            Pair(ThetaRepository.IsoEnum.ISO_400, 400),
            Pair(ThetaRepository.IsoEnum.ISO_500, 500),
            Pair(ThetaRepository.IsoEnum.ISO_640, 640),
            Pair(ThetaRepository.IsoEnum.ISO_800, 800),
            Pair(ThetaRepository.IsoEnum.ISO_1000, 1000),
            Pair(ThetaRepository.IsoEnum.ISO_1250, 1250),
            Pair(ThetaRepository.IsoEnum.ISO_1600, 1600),
            Pair(ThetaRepository.IsoEnum.ISO_2000, 2000),
            Pair(ThetaRepository.IsoEnum.ISO_2500, 2500),
            Pair(ThetaRepository.IsoEnum.ISO_3200, 3200),
            Pair(ThetaRepository.IsoEnum.ISO_4000, 4000),
            Pair(ThetaRepository.IsoEnum.ISO_5000, 5000),
            Pair(ThetaRepository.IsoEnum.ISO_6400, 6400)
        )

        values.forEach {
            val orgOptions = Options(
                iso = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.iso, it.first, "iso ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                iso = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options.iso, it.second, "iso ${it.second}")
        }
    }
}
