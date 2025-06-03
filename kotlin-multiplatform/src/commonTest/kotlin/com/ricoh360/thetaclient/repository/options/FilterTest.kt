package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.ImageFilter
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
class FilterTest {
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
            Pair(ThetaRepository.FilterEnum.DR_COMP, ImageFilter.DR_COMP),
            Pair(ThetaRepository.FilterEnum.NOISE_REDUCTION, ImageFilter.NOISE_REDUCTION),
            Pair(ThetaRepository.FilterEnum.HDR, ImageFilter.HDR),
            Pair(ThetaRepository.FilterEnum.HH_HDR, ImageFilter.HH_HDR)
        )

        assertEquals(values.size, ThetaRepository.FilterEnum.values().size)

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
