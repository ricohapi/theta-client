package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.ShootingFunction
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class FunctionTest {
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
     * Get option
     */
    @Test
    fun getOptionTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.Function
        )
        val stringOptionNames = listOf(
            "_function"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_function_self_timer.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.function, ThetaRepository.ShootingFunctionEnum.SELF_TIMER)
    }

    /**
     * Set option
     */
    @Test
    fun setOptionTest() = runTest {
        val value = Pair(ThetaRepository.ShootingFunctionEnum.MY_SETTING, ShootingFunction.MY_SETTING)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, function = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            function = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.ShootingFunctionEnum.NORMAL, ShootingFunction.NORMAL),
            Pair(ThetaRepository.ShootingFunctionEnum.SELF_TIMER, ShootingFunction.SELF_TIMER),
            Pair(ThetaRepository.ShootingFunctionEnum.MY_SETTING, ShootingFunction.MY_SETTING),
        )

        values.forEach {
            val orgOptions = Options(
                _function = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.function, it.first, "function ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                function = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._function, it.second, "_function ${it.second}")
        }
    }
}