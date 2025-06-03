package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class MaxRecordableTimeTest {
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
     * Get option maxRecordableTime.
     */
    @Test
    fun getOptionMaxRecordableTimeTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.MaxRecordableTime
        )
        val stringOptionNames = listOf(
            "_maxRecordableTime"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_max_recordable_time_1500.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.maxRecordableTime, ThetaRepository.MaxRecordableTimeEnum.RECORDABLE_TIME_1500, "maxRecordableTime")
    }

    /**
     * Set option maxRecordableTime.
     */
    @Test
    fun setOptionMaxRecordableTimeTest() = runTest {
        val value = Pair(ThetaRepository.MaxRecordableTimeEnum.RECORDABLE_TIME_1500, 1500)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, maxRecordableTime = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            maxRecordableTime = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionMaxRecordableTimeTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.MaxRecordableTimeEnum.UNKNOWN, 1),
            Pair(ThetaRepository.MaxRecordableTimeEnum.RECORDABLE_TIME_180, 180),
            Pair(ThetaRepository.MaxRecordableTimeEnum.RECORDABLE_TIME_300, 300),
            Pair(ThetaRepository.MaxRecordableTimeEnum.RECORDABLE_TIME_1500, 1500),
            Pair(ThetaRepository.MaxRecordableTimeEnum.RECORDABLE_TIME_3000, 3000),
            Pair(ThetaRepository.MaxRecordableTimeEnum.RECORDABLE_TIME_7200, 7200),
            Pair(ThetaRepository.MaxRecordableTimeEnum.DO_NOT_UPDATE_MY_SETTING_CONDITION, -1)
        )

        assertEquals(ThetaRepository.MaxRecordableTimeEnum.entries.size, values.size, "enum size")

        values.forEach {
            val orgOptions = Options(
                _maxRecordableTime = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.maxRecordableTime, it.first, "maxRecordableTime ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                maxRecordableTime = it.first
            )
            val options = orgOptions.toOptions()
            when (it.first) {
                ThetaRepository.MaxRecordableTimeEnum.UNKNOWN -> {
                    assertNull(options._maxRecordableTime, "maxRecordableTime UNKNOWN")
                }

                else -> {
                    assertEquals(
                        options._maxRecordableTime,
                        it.second,
                        "maxRecordableTime ${it.second}"
                    )
                }
            }
        }
    }
}
