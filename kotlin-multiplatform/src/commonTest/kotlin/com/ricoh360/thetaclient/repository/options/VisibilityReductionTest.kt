package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.VisibilityReduction
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
class VisibilityReductionTest {
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
     * Get option _visibilityReduction.
     */
    @Test
    fun getOptionVisibilityReductionTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.VisibilityReduction
        )
        val stringOptionNames = listOf(
            "_visibilityReduction"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_visibility-reduction-on.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.visibilityReduction, ThetaRepository.VisibilityReductionEnum.ON, "visibilityReduction")
    }

    /**
     * Set option _visibilityReduction.
     */
    @Test
    fun setOptionVisibilityReductionTest() = runTest {
        val value = Pair(ThetaRepository.VisibilityReductionEnum.ON, VisibilityReduction.ON)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, visibilityReduction = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            visibilityReduction = value.first
        )
        kotlin.runCatching {
            thetaRepository.setOptions(options)
        }.onSuccess {
            assertTrue(true, "setOptions visibilityReduction")
        }.onFailure {
            println(it.toString())
            assertTrue(false, "setOptions visibilityReduction")
        }
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionVisibilityReductionTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.VisibilityReductionEnum.ON, VisibilityReduction.ON),
            Pair(ThetaRepository.VisibilityReductionEnum.OFF, VisibilityReduction.OFF),
        )

        values.forEach {
            val orgOptions = Options(
                _visibilityReduction = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.visibilityReduction, it.first, "visibilityReduction ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                visibilityReduction = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._visibilityReduction, it.second, "_visibilityReduction ${it.second}")
        }
    }
}
