package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.AiAutoThumbnail
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class AiAutoThumbnailTest {
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
     * Get option _aiAutoThumbnail.
     */
    @Test
    fun getOptionAiAutoThumbnailTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.AiAutoThumbnail
        )
        val stringOptionNames = listOf(
            "_aiAutoThumbnail"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_ai_auto_thumbnail_on.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.aiAutoThumbnail, ThetaRepository.AiAutoThumbnailEnum.ON)
    }

    /**
     * Set option _aiAutoThumbnail.
     */
    @Test
    fun setOptionAiAutoThumbnailTest() = runTest {
        val value = Pair(ThetaRepository.AiAutoThumbnailEnum.ON, AiAutoThumbnail.ON)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, aiAutoThumbnail = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            aiAutoThumbnail = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionAiAutoThumbnailTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.AiAutoThumbnailEnum.ON, AiAutoThumbnail.ON),
            Pair(ThetaRepository.AiAutoThumbnailEnum.OFF, AiAutoThumbnail.OFF),
        )

        values.forEach {
            val orgOptions = Options(
                _aiAutoThumbnail = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.aiAutoThumbnail, it.first, "aiAutoThumbnail ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                aiAutoThumbnail = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._aiAutoThumbnail, it.second, "_aiAutoThumbnail ${it.second}")
        }
    }
}
