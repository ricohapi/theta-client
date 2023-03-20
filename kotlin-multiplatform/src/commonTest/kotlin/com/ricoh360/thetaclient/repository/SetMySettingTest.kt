package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.test.*

@OptIn(ExperimentalSerializationApi::class, ExperimentalCoroutinesApi::class)
class SetMySettingTest {
    private val endpoint = "http://192.168.1.1:80/"

    @BeforeTest
    fun setup() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @AfterTest
    fun teardown() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @Test
    fun setMySettingImageTest_V() = runTest {
        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            ByteReadChannel(Resource("src/commonTest/resources/setMySetting/set_my_setting_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            colorTemperature = 5000,
            exposureCompensation = ThetaRepository.ExposureCompensationEnum.M0_7,
            exposureDelay = ThetaRepository.ExposureDelayEnum.DELAY_4,
            exposureProgram = ThetaRepository.ExposureProgramEnum.SHUTTER_PRIORITY,
            fileFormat = ThetaRepository.FileFormatEnum.IMAGE_5K,
            iso = ThetaRepository.IsoEnum.ISO_AUTO,
            isoAutoHighLimit = ThetaRepository.IsoAutoHighLimitEnum.ISO_1600,
            whiteBalance = ThetaRepository.WhiteBalanceEnum.DAYLIGHT,
            // add options.shutterSpeed when its option is implemented.
        )
        kotlin.runCatching {
            thetaRepository.setMySetting(ThetaRepository.CaptureModeEnum.IMAGE, options)
        }.onSuccess {
            assertTrue(true, "setMySetting")
        }.onFailure {
            println("setMySetting: ${it.toString()}")
            assertTrue(false, "setMySetting")
        }
    }
}
