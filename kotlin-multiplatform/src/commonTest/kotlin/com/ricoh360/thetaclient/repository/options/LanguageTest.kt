package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Language
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class LanguageTest {
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
     * Get option language.
     */
    @Test
    fun getOptionLanguageTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.Language
        )
        val stringOptionNames = listOf(
            "_language"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_language_ja.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.language, ThetaRepository.LanguageEnum.JA, "language")
    }

    /**
     * Set option language.
     */
    @Test
    fun setOptionLanguageTest() = runTest {
        val value = Pair(ThetaRepository.LanguageEnum.JA, Language.JA)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, language = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            language = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionLanguageTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.LanguageEnum.DE, Language.DE),
            Pair(ThetaRepository.LanguageEnum.EN_GB, Language.GB),
            Pair(ThetaRepository.LanguageEnum.EN_US, Language.US),
            Pair(ThetaRepository.LanguageEnum.FR, Language.FR),
            Pair(ThetaRepository.LanguageEnum.IT, Language.IT),
            Pair(ThetaRepository.LanguageEnum.JA, Language.JA),
            Pair(ThetaRepository.LanguageEnum.KO, Language.KO),
            Pair(ThetaRepository.LanguageEnum.ZH_CN, Language.CN),
            Pair(ThetaRepository.LanguageEnum.ZH_TW, Language.TW)
        )

        values.forEach {
            val orgOptions = Options(
                _language = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.language, it.first, "language ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                language = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._language, it.second, "language ${it.second}")
        }
    }
}
