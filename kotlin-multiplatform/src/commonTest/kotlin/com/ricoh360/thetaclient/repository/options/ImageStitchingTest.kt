package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.ImageStitching
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ImageStitchingTest {
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
     * Get option imageStitching.
     */
    @Test
    fun getOptionImageStitchingTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.ImageStitching
        )
        val stringOptionNames = listOf(
            "_imageStitching"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_image_stitching.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.imageStitching, ThetaRepository.ImageStitchingEnum.AUTO, "imageStitching")
    }

    /**
     * Set option imageStitching.
     */
    @Test
    fun setOptionImageStitchingTest() = runTest {
        val value = Pair(ThetaRepository.ImageStitchingEnum.AUTO, ImageStitching.AUTO)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, imageStitching = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            imageStitching = value.first
        )
        kotlin.runCatching {
            thetaRepository.setOptions(options)
        }.onSuccess {
            assertTrue(true, "setOptions ImageStitching")
        }.onFailure {
            println(it.toString())
            assertTrue(false, "setOptions ImageStitching")
        }
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionImageStitchingTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.ImageStitchingEnum.AUTO, ImageStitching.AUTO),
            Pair(ThetaRepository.ImageStitchingEnum.STATIC, ImageStitching.STATIC),
            Pair(ThetaRepository.ImageStitchingEnum.DYNAMIC, ImageStitching.DYNAMIC),
            Pair(ThetaRepository.ImageStitchingEnum.DYNAMIC_AUTO, ImageStitching.DYNAMIC_AUTO),
            Pair(ThetaRepository.ImageStitchingEnum.DYNAMIC_SEMI_AUTO, ImageStitching.DYNAMIC_SEMI_AUTO),
            Pair(ThetaRepository.ImageStitchingEnum.DYNAMIC_SAVE, ImageStitching.DYNAMIC_SAVE),
            Pair(ThetaRepository.ImageStitchingEnum.DYNAMIC_LOAD, ImageStitching.DYNAMIC_LOAD),
            Pair(ThetaRepository.ImageStitchingEnum.NONE, ImageStitching.NONE),
        )

        values.forEach {
            val orgOptions = Options(
                _imageStitching = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.imageStitching, it.first, "imageStitching ${it.second}")
        }
        values.forEach {
            val orgOptions = ThetaRepository.Options(
                imageStitching = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._imageStitching, it.second, "imageStitching ${it.second}")
        }
    }
}
