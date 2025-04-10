package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.AiAutoThumbnail
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AiAutoThumbnailSupportTest {
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
    fun getOptionTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.AiAutoThumbnailSupport
        )
        val stringOptionNames = listOf(
            "_aiAutoThumbnailSupport"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_ai_auto_thumbnail_support.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(
            options.aiAutoThumbnailSupport,
            listOf(
                ThetaRepository.AiAutoThumbnailEnum.ON,
                ThetaRepository.AiAutoThumbnailEnum.OFF,
                ThetaRepository.AiAutoThumbnailEnum.UNKNOWN,
            ),
            "aiAutoThumbnailSupport"
        )
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = Pair(
            listOf(AiAutoThumbnail.ON, AiAutoThumbnail.OFF),
            listOf(
                ThetaRepository.AiAutoThumbnailEnum.ON,
                ThetaRepository.AiAutoThumbnailEnum.OFF
            )
        )

        val orgOptions = Options(
            _aiAutoThumbnailSupport = values.first
        )
        val optionsTR = ThetaRepository.Options(orgOptions)
        assertEquals(
            optionsTR.aiAutoThumbnailSupport,
            values.second,
            "aiAutoThumbnailSupport ${values.second}"
        )

        val orgOptionsTR = ThetaRepository.Options(
            aiAutoThumbnailSupport = values.second
        )
        val options = orgOptionsTR.toOptions()
        assertEquals(
            options._aiAutoThumbnailSupport,
            values.first,
            "_aiAutoThumbnailSupport ${values.first}"
        )
    }
}