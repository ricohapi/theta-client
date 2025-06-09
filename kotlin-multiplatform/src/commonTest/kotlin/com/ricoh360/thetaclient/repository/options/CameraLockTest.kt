package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CameraLock
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CameraLockTest {
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
            ThetaRepository.OptionNameEnum.CameraLock
        )
        val stringOptionNames = listOf(
            "_cameraLock"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_camera_lock_unlock.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.cameraLock, ThetaRepository.CameraLockEnum.UNLOCK)
    }

    /**
     * Get option UNKNOWN.
     */
    @Test
    fun getOptionUnknownTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.CameraLock
        )
        val stringOptionNames = listOf(
            "_cameraLock"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_camera_lock_unknown.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.cameraLock, ThetaRepository.CameraLockEnum.UNKNOWN)
    }

    /**
     * Set option.
     */
    @Test
    fun setOptionTest() = runTest {
        val value = Pair(ThetaRepository.CameraLockEnum.BASIC_LOCK, CameraLock.BASIC_LOCK)

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, cameraLock = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            cameraLock = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.CameraLockEnum.UNKNOWN, CameraLock.UNKNOWN),
            Pair(ThetaRepository.CameraLockEnum.UNLOCK, CameraLock.UNLOCK),
            Pair(ThetaRepository.CameraLockEnum.BASIC_LOCK, CameraLock.BASIC_LOCK),
            Pair(ThetaRepository.CameraLockEnum.CUSTOM_LOCK, CameraLock.CUSTOM_LOCK),
        )

        values.forEach {
            val orgOptions = Options(
                _cameraLock = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.cameraLock, it.first, "cameraLock ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                cameraLock = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._cameraLock, it.second, "_cameraLock ${it.second}")
        }
    }
}