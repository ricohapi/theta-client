package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.FirstShootingEnum
import com.ricoh360.thetaclient.transferred.Options
import com.ricoh360.thetaclient.transferred.TimeShift
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
class TimeShiftTest {
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
     * Get option timeShift.
     */
    @Test
    fun getOptionTimeShiftTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.TimeShift
        )
        val stringOptionNames = listOf(
            "_timeShift"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_time_shift.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        kotlin.runCatching {
            val options = thetaRepository.getOptions(optionNames)
        }.onSuccess {
            assertTrue(true, "getOption: timeShift")
        }.onFailure {
            assertTrue(false, "getOption: timeShift")
        }
    }

    /**
     * Set option timeShift.
     */
    @Test
    fun setOptionTimeShiftTest() = runTest {
        val value = Pair(
            ThetaRepository.TimeShiftSetting(
                true,
                ThetaRepository.TimeShiftIntervalEnum.INTERVAL_9,
                ThetaRepository.TimeShiftIntervalEnum.INTERVAL_10
            ),
            TimeShift(FirstShootingEnum.FRONT, 9, 10)
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, timeShift = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            timeShift = value.first
        )
        kotlin.runCatching {
            thetaRepository.setOptions(options)
        }.onSuccess {
            assertTrue(true, "setOptions TimeShift")
        }.onFailure {
            println(it.toString())
            assertTrue(false, "setOptions TimeShift")
        }
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTimeShiftTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.TimeShiftSetting(), TimeShift()),
            Pair(
                ThetaRepository.TimeShiftSetting(null, ThetaRepository.TimeShiftIntervalEnum.INTERVAL_0, ThetaRepository.TimeShiftIntervalEnum.INTERVAL_1),
                TimeShift(null, 0, 1)
            ),
            Pair(
                ThetaRepository.TimeShiftSetting(true, null, ThetaRepository.TimeShiftIntervalEnum.INTERVAL_5),
                TimeShift(FirstShootingEnum.FRONT, null, 5)
            ),
            Pair(
                ThetaRepository.TimeShiftSetting(false, ThetaRepository.TimeShiftIntervalEnum.INTERVAL_10, null),
                TimeShift(FirstShootingEnum.REAR, 10, null)
            ),
        )

        values.forEach {
            val orgOptions = Options(
                _timeShift = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.timeShift, it.first, "timeShift ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                timeShift = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._timeShift, it.second, "timeShift ${it.second}")
        }
    }
}
