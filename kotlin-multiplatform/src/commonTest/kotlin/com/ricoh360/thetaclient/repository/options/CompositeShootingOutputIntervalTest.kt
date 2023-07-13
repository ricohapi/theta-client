package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class CompositeShootingOutputIntervalTest {
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
     * Get option compositeShootingOutputInterval.
     */
    @Test
    fun getOptionCompositeShootingOutputIntervalTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.CompositeShootingOutputInterval
        )
        val stringOptionNames = listOf(
            "_compositeShootingOutputInterval"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_composite_shooting_output_interval_60.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.compositeShootingOutputInterval, 60)
    }

    /**
     * Set option compositeShootingOutputInterval.
     */
    @Test
    fun setOptionCompositeShootingOutputIntervalTest() = runTest {
        val value = Pair(600, 600)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, compositeShootingOutputInterval = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            compositeShootingOutputInterval = value.first
        )
        thetaRepository.setOptions(options)
    }
}
