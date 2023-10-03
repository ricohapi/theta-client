package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.VideoStitching
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
class VideoStitchingTest {
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
     * Get option videoStitching.
     */
    @Test
    fun getOptionVideoStitchingTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.VideoStitching
        )
        val stringOptionNames = listOf(
            "videoStitching"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_video_stitching-ondevice.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.videoStitching, ThetaRepository.VideoStitchingEnum.ONDEVICE, "videoStitching")
    }

    /**
     * Set option videoStitching.
     */
    @Test
    fun setOptionVideoStitchingTest() = runTest {
        val value = Pair(ThetaRepository.VideoStitchingEnum.ONDEVICE, VideoStitching.ONDEVICE)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, videoStitching = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            videoStitching = value.first
        )
        kotlin.runCatching {
            thetaRepository.setOptions(options)
        }.onSuccess {
            assertTrue(true, "setOptions videoStitching")
        }.onFailure {
            println(it.toString())
            assertTrue(false, "setOptions videoStitching")
        }
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionVideoStitchingTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.VideoStitchingEnum.ONDEVICE, VideoStitching.ONDEVICE),
            Pair(ThetaRepository.VideoStitchingEnum.NONE, VideoStitching.NONE),
        )

        values.forEach {
            val orgOptions = Options(
                videoStitching = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.videoStitching, it.first, "videoStitching ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                videoStitching = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options.videoStitching, it.second, "videoStitching ${it.second}")
        }
    }
}
