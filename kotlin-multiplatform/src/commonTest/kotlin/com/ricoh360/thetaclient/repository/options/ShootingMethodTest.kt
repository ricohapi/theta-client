package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.ShootingMethod
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
class ShootingMethodTest {
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
     * Get option shootingMethod.
     */
    @Test
    fun getOptionShootingMethodTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.ShootingMethod
        )
        val stringOptionNames = listOf(
            "_shootingMethod"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_shooting_method_normal.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(
            options.shootingMethod,
            ThetaRepository.ShootingMethodEnum.NORMAL,
            "shootingMethod"
        )
    }

    /**
     * Set option shootingMode.
     */
    @Test
    fun setOptionShootingModeTest() = runTest {
        val value = Pair(ThetaRepository.ShootingMethodEnum.NORMAL, ShootingMethod.NORMAL)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, shootingMethod = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            shootingMethod = value.first
        )
        kotlin.runCatching {
            thetaRepository.setOptions(options)
        }.onSuccess {
            assertTrue(true, "setOptions ShootingMethod")
        }.onFailure {
            println(it.toString())

        }
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionShootingMethodTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.ShootingMethodEnum.NORMAL, ShootingMethod.NORMAL),
            Pair(ThetaRepository.ShootingMethodEnum.INTERVAL, ShootingMethod.INTERVAL),
            Pair(ThetaRepository.ShootingMethodEnum.MOVE_INTERVAL, ShootingMethod.MOVE_INTERVAL),
            Pair(ThetaRepository.ShootingMethodEnum.FIXED_INTERVAL, ShootingMethod.FIXED_INTERVAL),
            Pair(ThetaRepository.ShootingMethodEnum.BRACKET, ShootingMethod.BRACKET),
            Pair(ThetaRepository.ShootingMethodEnum.COMPOSITE, ShootingMethod.COMPOSITE),
            Pair(ThetaRepository.ShootingMethodEnum.CONTINUOUS, ShootingMethod.CONTINUOUS),
            Pair(ThetaRepository.ShootingMethodEnum.TIME_SHIFT, ShootingMethod.TIMESHIFT),
            Pair(ThetaRepository.ShootingMethodEnum.BURST, ShootingMethod.BURST),
        )

        values.forEach {
            val orgOptions = Options(
                _shootingMethod = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.shootingMethod, it.first, "shootingMethod ${it.second}")
        }
        values.forEach {
            val orgOptions = ThetaRepository.Options(
                shootingMethod = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._shootingMethod, it.second, "shootingMethod ${it.second}")
        }
    }
}
