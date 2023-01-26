package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.ImageFilter
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class FilterTest {
    private val endpoint = "http://dummy/"

    @BeforeTest
    fun setup() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @AfterTest
    fun teardown() {
        MockApiClient.status = HttpStatusCode.OK
    }

    /**
     * Get option filter.
     */
    @Test
    fun getOptionFilterTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.Filter
        )
        val stringOptionNames = listOf(
            "_filter"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_filter_hdr.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.filter, ThetaRepository.FilterEnum.HDR)
    }

    /**
     * Set option filter.
     */
    @Test
    fun setOptionFilterTest() = runTest {
        val value = Pair(ThetaRepository.FilterEnum.HDR, ImageFilter.HDR)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, filter = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            filter = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionFilterTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.FilterEnum.OFF, ImageFilter.OFF),
            Pair(ThetaRepository.FilterEnum.NOISE_REDUCTION, ImageFilter.NOISE_REDUCTION),
            Pair(ThetaRepository.FilterEnum.HDR, ImageFilter.HDR),
            Pair(null, ImageFilter.DR_COMP),
            Pair(null, ImageFilter.HH_HDR)
        )

        values.forEach {
            val orgOptions = Options(
                _filter = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.filter, it.first, "filter ${it.second}")
        }

        values.filter {
            it.first != null
        }.forEach {
            val orgOptions = ThetaRepository.Options(
                filter = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._filter, it.second, "filter ${it.second}")
        }
    }
}
