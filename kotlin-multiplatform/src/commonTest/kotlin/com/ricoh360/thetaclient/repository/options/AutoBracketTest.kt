package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.AutoBracket
import com.ricoh360.thetaclient.transferred.BracketParameter
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.WhiteBalance
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AutoBracketTest {
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
     * Get option autoBracket.
     */
    @Test
    fun getOptionAutoBracketTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.AutoBracket
        )
        val stringOptionNames = listOf(
            "_autoBracket"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)
            ByteReadChannel(Resource("src/commonTest/resources/options/option_auto_bracket.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertNotNull(options.autoBracket, "autoBracket null")
        assertEquals(options.autoBracket!!.list.size, 2, "autoBracket size")
        assertEquals(options.autoBracket!!.list[0].colorTemperature, 5000, "autoBracket")
        assertEquals(
            options.autoBracket!!.list[0].exposureCompensation,
            ThetaRepository.ExposureCompensationEnum.ZERO,
            "autoBracket"
        )
        assertEquals(
            options.autoBracket!!.list[0].exposureProgram,
            ThetaRepository.ExposureProgramEnum.MANUAL,
            "autoBracket"
        )
        assertEquals(
            options.autoBracket!!.list[0].iso,
            ThetaRepository.IsoEnum.ISO_400,
            "autoBracket"
        )
        assertEquals(
            options.autoBracket!!.list[0].shutterSpeed,
            ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_250,
            "autoBracket"
        )
        assertEquals(
            options.autoBracket!!.list[0].whiteBalance,
            ThetaRepository.WhiteBalanceEnum.AUTO,
            "autoBracket"
        )
    }

    /**
     * Set option autoBracket.
     */
    @Test
    fun setOptionAutoBracketTest() = runTest {
        //val value = Pair(ThetaRepository.AutoBracketEnum.=MEMBER=, AutoBracket.=MEMBER=)
        val list = ThetaRepository.BracketSettingList()
        list.add(
            ThetaRepository.BracketSetting(
                exposureProgram = ThetaRepository.ExposureProgramEnum.NORMAL_PROGRAM,
                whiteBalance = ThetaRepository.WhiteBalanceEnum.DAYLIGHT,
            )
        ).add(
            ThetaRepository.BracketSetting(
                aperture = ThetaRepository.ApertureEnum.APERTURE_2_0,
                colorTemperature = 6800,
                exposureProgram = ThetaRepository.ExposureProgramEnum.MANUAL,
                iso = ThetaRepository.IsoEnum.ISO_800,
                shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_100,
                whiteBalance = ThetaRepository.WhiteBalanceEnum.CLOUDY_DAYLIGHT,
            )
        )

        val transferred = AutoBracket(
            2, listOf(
                BracketParameter(exposureProgram = 2, whiteBalance = WhiteBalance.DAYLIGHT),
                BracketParameter(
                    aperture = 2.0F,
                    _colorTemperature = 6800,
                    exposureProgram = 1,
                    iso = 800,
                    shutterSpeed = 0.01,
                    whiteBalance = WhiteBalance.CLOUDY_DAYLIGHT
                )
            )
        )


        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, autoBracket = transferred)
            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            autoBracket = list
        )
        kotlin.runCatching {
            thetaRepository.setOptions(options)
        }.onSuccess {
            assertTrue(true, "setOptions AutoBracket")
        }.onFailure {
            println(it.toString())
            assertTrue(false, "setOptions AutoBracket")
        }
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionAutoBracketTest() = runTest {
        val values = listOf(
            Pair(
                ThetaRepository.BracketSettingList().add(
                    ThetaRepository.BracketSetting(
                        exposureProgram = ThetaRepository.ExposureProgramEnum.NORMAL_PROGRAM,
                        whiteBalance = ThetaRepository.WhiteBalanceEnum.DAYLIGHT,
                    )
                ).add(
                    ThetaRepository.BracketSetting(
                        aperture = ThetaRepository.ApertureEnum.APERTURE_2_0,
                        colorTemperature = 6800,
                        exposureProgram = ThetaRepository.ExposureProgramEnum.MANUAL,
                        iso = ThetaRepository.IsoEnum.ISO_800,
                        shutterSpeed = ThetaRepository.ShutterSpeedEnum.SHUTTER_SPEED_ONE_OVER_100,
                        whiteBalance = ThetaRepository.WhiteBalanceEnum.CLOUDY_DAYLIGHT,
                    )
                ),
                AutoBracket(
                    2, listOf(
                        BracketParameter(exposureProgram = 2, whiteBalance = WhiteBalance.DAYLIGHT),
                        BracketParameter(
                            aperture = 2.0F,
                            _colorTemperature = 6800,
                            exposureProgram = 1,
                            iso = 800,
                            shutterSpeed = 0.01,
                            whiteBalance = WhiteBalance.CLOUDY_DAYLIGHT,
                        )
                    )
                ),
            ),
        )

        values.forEach {
            val orgOptions = Options(
                _autoBracket = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.autoBracket, it.first, "autoBracket ${it.second}")
        }
        values.forEach {
            val orgOptions = ThetaRepository.Options(
                autoBracket = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._autoBracket, it.second, "autoBracket ${it.second}")
        }
    }
}
