package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CameraLockConfig
import com.ricoh360.thetaclient.transferred.CameraLockType
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CameraLockConfigTest {
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
            ThetaRepository.OptionNameEnum.CameraLockConfig
        )
        val stringOptionNames = listOf(
            "_cameraLockConfig"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_camera_lock_config_A1.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.cameraLockConfig?.isPowerKeyLocked, true)
        assertEquals(options.cameraLockConfig?.isShutterKeyLocked, false)
        assertEquals(options.cameraLockConfig?.isModeKeyLocked, false)
        assertEquals(options.cameraLockConfig?.isWlanKeyLocked, false)
        assertEquals(options.cameraLockConfig?.isFnKeyLocked, false)
        assertEquals(options.cameraLockConfig?.isPanelLocked, true)
    }

    @Test
    fun getOptionUnknownTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.CameraLockConfig
        )
        val stringOptionNames = listOf(
            "_cameraLockConfig"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_camera_lock_config_unknown.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.cameraLockConfig?.isPowerKeyLocked, true)
        assertEquals(options.cameraLockConfig?.isShutterKeyLocked, true)
        assertEquals(options.cameraLockConfig?.isModeKeyLocked, true)
        assertEquals(options.cameraLockConfig?.isWlanKeyLocked, true)
        assertEquals(options.cameraLockConfig?.isFnKeyLocked, true)
        assertEquals(options.cameraLockConfig?.isPanelLocked, true)
    }

    /**
     * Set option.
     */
    @Test
    fun setOptionTest() = runTest {
        val value = Pair(
            ThetaRepository.CameraLockConfig(
                isPowerKeyLocked = true,
                isShutterKeyLocked = false,
                isModeKeyLocked = true,
                isWlanKeyLocked = true,
                isFnKeyLocked = false,
                isPanelLocked = true
            ),
            CameraLockConfig(
                powerKey = CameraLockType.LOCK,
                shutterKey = CameraLockType.UNLOCK,
                modeKey = CameraLockType.LOCK,
                wlanKey = CameraLockType.LOCK,
                fnKey = CameraLockType.UNLOCK,
                panel = CameraLockType.LOCK
            )
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, cameraLockConfig = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            cameraLockConfig = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionTest() = runTest {
        val values = listOf(
            Pair(
                ThetaRepository.CameraLockConfig(
                    isPowerKeyLocked = true,
                    isShutterKeyLocked = false,
                    isModeKeyLocked = true,
                    isWlanKeyLocked = true,
                    isFnKeyLocked = false,
                    isPanelLocked = true
                ),
                CameraLockConfig(
                    powerKey = CameraLockType.LOCK,
                    shutterKey = CameraLockType.UNLOCK,
                    modeKey = CameraLockType.LOCK,
                    wlanKey = CameraLockType.LOCK,
                    fnKey = CameraLockType.UNLOCK,
                    panel = CameraLockType.LOCK
                )
            ),
        )

        values.forEach {
            val orgOptions = Options(
                _cameraLockConfig = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.cameraLockConfig, it.first, "cameraLockConfig ${it.second}")
        }

        values.forEach {
            val orgOptions = ThetaRepository.Options(
                cameraLockConfig = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options._cameraLockConfig, it.second, "_cameraLockConfig ${it.second}")
        }
    }
}