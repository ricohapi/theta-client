package com.ricoh360.thetaclient.repository

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaApi
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.CaptureMode
import com.ricoh360.thetaclient.transferred.GetMySettingParams
import com.ricoh360.thetaclient.transferred.GetMySettingRequest
import com.ricoh360.thetaclient.transferred.GetMySettingResponse
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.*

@OptIn(ExperimentalSerializationApi::class, ExperimentalCoroutinesApi::class)
class GetMySettingTest {
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
    fun getMySettingImageTest_V() = runTest {
        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            ByteReadChannel(Resource("src/commonTest/resources/getMySetting/get_my_setting_done_v.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getMySetting(ThetaRepository.CaptureModeEnum.IMAGE)
        assertNotNull(options.aperture, "aperture")
        assertNotNull(options.colorTemperature, "colorTemperature")
        assertNotNull(options.exposureCompensation, "exposureCompensation")
        assertNotNull(options.exposureDelay, "exposureDelay")
        assertNotNull(options.exposureProgram, "exposureProgram")
        assertNotNull(options.fileFormat, "fileFormat")
        assertNotNull(options.filter, "filter")
        assertNotNull(options.iso, "iso")
        assertNotNull(options.isoAutoHighLimit, "isoAutoHighLimit")
        // Add options.shutterSpeed when its option is implemented.
        assertNotNull(options.whiteBalance, "whiteBalance")
    }

    @Test
    fun getMySettingVideoTest_V() = runTest {
        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            ByteReadChannel(Resource("src/commonTest/resources/getMySetting/get_my_setting_done_video_v.json").readText())
        }
        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getMySetting(ThetaRepository.CaptureModeEnum.VIDEO)
        assertNotNull(options.aperture, "aperture")
        assertNotNull(options.colorTemperature, "colorTemperature")
        assertNotNull(options.exposureCompensation, "exposureCompensation")
        assertNotNull(options.exposureDelay, "exposureDelay")
        assertNotNull(options.exposureProgram, "exposureProgram")
        assertNotNull(options.fileFormat, "fileFormat")
        assertNotNull(options.iso, "iso")
        assertNotNull(options.isoAutoHighLimit, "isoAutoHighLimit")
        // Add options.shutterSpeed when its option is implemented.
        assertNotNull(options.whiteBalance, "whiteBalance")
    }

    @Test
    fun getMySettingTest_S() = runTest {
        MockApiClient.onRequest = { request ->
            // check request
            assertEquals(request.url.encodedPath, "/osc/commands/execute", "request path")
            ByteReadChannel(Resource("src/commonTest/resources/getMySetting/get_my_setting_done_s.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getMySetting(listOf(ThetaRepository.OptionNameEnum.FileFormat, ThetaRepository.OptionNameEnum.WhiteBalance))
        assertNotNull(options.fileFormat, "fileFormat")
        assertNotNull(options.whiteBalance, "whiteBalance")
    }

}
