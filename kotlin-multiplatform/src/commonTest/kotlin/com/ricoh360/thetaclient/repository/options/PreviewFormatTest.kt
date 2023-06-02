package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.PreviewFormat
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class PreviewFormatTest {
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
     * Get option previewFormat.
     */
    @Test
    fun getOptionPreviewFormatTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.PreviewFormat
        )
        val stringOptionNames = listOf(
            "previewFormat"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_previewformat_1024_512_30.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.previewFormat, ThetaRepository.PreviewFormatEnum.W1024_H512_F30, "previewFormat")
    }

    /**
     * Set option previewFormat.
     */
    @Test
    fun setOptionPreviewFormatTest() = runTest {
        val value = Pair(ThetaRepository.PreviewFormatEnum.W1024_H512_F30, PreviewFormat(1024, 512, 30))

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, previewFormat = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            previewFormat = value.first
        )
        kotlin.runCatching {
            thetaRepository.setOptions(options)
        }.onSuccess {
            assertTrue(true, "setOptions PreviewFormat")
        }.onFailure {
            println(it.toString())
            assertTrue(false, "setOptions PreviewFormat")
        }
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionPreviewFormatTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.PreviewFormatEnum.W1024_H512_F30, PreviewFormat(1024, 512, 30)),
            Pair(ThetaRepository.PreviewFormatEnum.W1024_H512_F8, PreviewFormat(1024, 512, 8)),
            Pair(ThetaRepository.PreviewFormatEnum.W1920_H960_F8, PreviewFormat(1920, 960, 8)),
            Pair(ThetaRepository.PreviewFormatEnum.W512_H512_F30, PreviewFormat(512, 512, 30)),
            Pair(ThetaRepository.PreviewFormatEnum.W640_H320_F30, PreviewFormat(640, 320, 30)),
            Pair(ThetaRepository.PreviewFormatEnum.W640_H320_F10, PreviewFormat(640, 320, 10)),
            Pair(ThetaRepository.PreviewFormatEnum.W640_H320_F8, PreviewFormat(640, 320, 8)),
        )

        values.forEach {
            val orgOptions = Options(
                previewFormat = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.previewFormat, it.first, "previewFormat ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                previewFormat = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options.previewFormat, it.second, "previewFormat ${it.second}")
        }
    }
}
