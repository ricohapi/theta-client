package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CompositeShootingTimeTest {
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
     * Get option compositeShootingTime.
     */
    @Test
    fun getOptionCompositeShootingTimeTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.CompositeShootingTime
        )
        val stringOptionNames = listOf(
            "_compositeShootingTime"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_composite_shooting_time_600.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.compositeShootingTime, 600)
    }

    /**
     * Set option compositeShootingTime.
     */
    @Test
    fun setOptionCompositeShootingTimeTest() = runTest {
        val value = Pair(86400, 86400)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, compositeShootingTime = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            compositeShootingTime = value.first
        )
        thetaRepository.setOptions(options)
    }
}
