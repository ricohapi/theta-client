package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CompassDirectionRef
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CompassDirectionRefTest {
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
            ThetaRepository.OptionNameEnum.CompassDirectionRef
        )
        val stringOptionNames = listOf(
            "_compassDirectionRef"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_compass_direction_ref_auto.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.compassDirectionRef, ThetaRepository.CompassDirectionRefEnum.AUTO)
    }

    /**
     * Get option UNKNOWN.
     */
    @Test
    fun getOptionUnknownTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.CompassDirectionRef
        )
        val stringOptionNames = listOf(
            "_compassDirectionRef"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_compass_direction_ref_unknown.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.compassDirectionRef, ThetaRepository.CompassDirectionRefEnum.UNKNOWN)
    }

    /**
     * Set option.
     */
    @Test
    fun setOptionTest() = runTest {
        val value = Pair(ThetaRepository.CompassDirectionRefEnum.MAGNETIC, CompassDirectionRef.MAGNETIC)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, compassDirectionRef = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            compassDirectionRef = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.CompassDirectionRefEnum.UNKNOWN, CompassDirectionRef.UNKNOWN),
            Pair(ThetaRepository.CompassDirectionRefEnum.AUTO, CompassDirectionRef.AUTO),
            Pair(ThetaRepository.CompassDirectionRefEnum.TRUE_NORTH, CompassDirectionRef.TRUE_NORTH),
            Pair(ThetaRepository.CompassDirectionRefEnum.MAGNETIC, CompassDirectionRef.MAGNETIC),
        )

        values.forEach {
            val orgOptions = Options(
                _compassDirectionRef = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.compassDirectionRef, it.first, "compassDirectionRef ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                compassDirectionRef = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._compassDirectionRef, it.second, "_compassDirectionRef ${it.second}")
        }
    }
}