package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CompositeShootingOutputIntervalSupport
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CompositeShootingOutputIntervalSupportTest {
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
            ThetaRepository.OptionNameEnum.CompositeShootingOutputIntervalSupport
        )
        val stringOptionNames = listOf(
            "_compositeShootingOutputIntervalSupport"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_composite_shooting_output_interval_support.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.compositeShootingOutputIntervalSupport?.max, 600, "maxInterval")
        assertEquals(options.compositeShootingOutputIntervalSupport?.min, 0, "minInterval")
        assertEquals(options.compositeShootingOutputIntervalSupport?.stepSize, 60, "stepSize")
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = Pair(
            CompositeShootingOutputIntervalSupport(600, 0, 60),
            ThetaRepository.ValueRange(600, 0, 60)
        )

        val orgOptions = Options(
            _compositeShootingOutputIntervalSupport = values.first
        )
        val optionsTR = ThetaRepository.Options(orgOptions)
        assertEquals(
            optionsTR.compositeShootingOutputIntervalSupport,
            values.second,
            "compositeShootingOutputIntervalSupport ${values.second}"
        )

        val orgOptionsTR = ThetaRepository.Options(
            compositeShootingOutputIntervalSupport = values.second
        )
        val options = orgOptionsTR.toOptions()
        assertEquals(
            options._compositeShootingOutputIntervalSupport,
            null,
            "_compositeShootingOutputIntervalSupport ${values.first}"
        )
    }
}