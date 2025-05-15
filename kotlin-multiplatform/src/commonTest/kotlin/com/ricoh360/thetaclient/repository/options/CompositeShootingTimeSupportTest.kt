package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CompositeShootingTimeSupport
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CompositeShootingTimeSupportTest {
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
     * Get option.
     */
    @Test
    fun getOption() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.CompositeShootingTimeSupport
        )
        val stringOptionNames = listOf(
            "_compositeShootingTimeSupport"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_composite_shooting_time_support.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.compositeShootingTimeSupport?.max, 86400, "maxTime")
        assertEquals(options.compositeShootingTimeSupport?.min, 600, "minTime")
        assertEquals(options.compositeShootingTimeSupport?.stepSize, 600, "stepSize")
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = Pair(
            CompositeShootingTimeSupport(86400, 600, 600),
            ThetaRepository.ValueRange(86400, 600, 600)
        )

        val orgOptions = Options(
            _compositeShootingTimeSupport = values.first
        )
        val optionsTR = ThetaRepository.Options(orgOptions)
        assertEquals(
            optionsTR.compositeShootingTimeSupport,
            values.second,
            "compositeShootingTimeSupport ${values.second}"
        )

        val orgOptionsTR = ThetaRepository.Options(
            compositeShootingTimeSupport = values.second
        )
        val options = orgOptionsTR.toOptions()
        assertEquals(
            options._compositeShootingTimeSupport,
            null,
            "_compositeShootingTimeSupport ${values.first}"
        )
    }
}