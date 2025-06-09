package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.Preset
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
class PresetTest {
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
     * Get option preset.
     */
    @Test
    fun getOptionPresetTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.Preset
        )
        val stringOptionNames = listOf(
            "_preset"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_preset_room.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.preset, ThetaRepository.PresetEnum.ROOM, "preset")
    }

    /**
     * Set option preset.
     */
    @Test
    fun setOptionPresetTest() = runTest {
        val value = Pair(ThetaRepository.PresetEnum.ROOM, Preset.ROOM)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, preset = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            preset = value.first
        )
        kotlin.runCatching {
            thetaRepository.setOptions(options)
        }.onSuccess {
            assertTrue(true, "setOptions Preset")
        }.onFailure {
            println(it.toString())
            assertTrue(false, "setOptions Preset")
        }
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionPresetTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.PresetEnum.ROOM, Preset.ROOM),
            Pair(ThetaRepository.PresetEnum.FACE, Preset.FACE),
            Pair(ThetaRepository.PresetEnum.NIGHT_VIEW, Preset.NIGHT_VIEW),
            Pair(ThetaRepository.PresetEnum.LENS_BY_LENS_EXPOSURE, Preset.LENS_BY_LENS_EXPOSURE),
        )

        values.forEach {
            val orgOptions = Options(
                _preset = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.preset, it.first, "preset ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                preset = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._preset, it.second, "preset ${it.second}")
        }
    }

}
