package com.ricoh360.thetaclient.repository.options

import com.goncalossilva.resources.Resource
import com.ricoh360.thetaclient.CheckRequest
import com.ricoh360.thetaclient.MockApiClient
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.transferred.MediaFileFormat
import com.ricoh360.thetaclient.transferred.MediaType
import com.ricoh360.thetaclient.transferred.Options
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class FileFormatTest {
    private val endpoint = "http://dummy/"

    @BeforeTest
    fun setup() {
        MockApiClient.status = HttpStatusCode.OK
    }

    @AfterTest
    fun teardown() {
        MockApiClient.status = HttpStatusCode.OK
    }

    /**
     * Get option fileFormat.
     */
    @Test
    fun getOptionFileFormatTest() = runTest {
        val optionNames = listOf(
            ThetaRepository.OptionNameEnum.FileFormat
        )
        val stringOptionNames = listOf(
            "fileFormat"
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkGetOptions(request, stringOptionNames)

            ByteReadChannel(Resource("src/commonTest/resources/options/option_file_format_video4k_60f.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = thetaRepository.getOptions(optionNames)
        assertEquals(options.fileFormat, ThetaRepository.FileFormatEnum.VIDEO_4K_60F, "options value")
    }

    /**
     * Set option fileFormat.
     */
    @Test
    fun setOptionFileFormatTest() = runTest {
        val value = Pair(
            ThetaRepository.FileFormatEnum.VIDEO_4K_60F,
            MediaFileFormat(MediaType.MP4, 3840, 1920, "H.264/MPEG-4 AVC", 60)
        )

        MockApiClient.onRequest = { request ->
            // check request
            CheckRequest.checkSetOptions(request, fileFormat = value.second)

            ByteReadChannel(Resource("src/commonTest/resources/setOptions/set_options_done.json").readText())
        }

        val thetaRepository = ThetaRepository(endpoint)
        val options = ThetaRepository.Options(
            fileFormat = value.first
        )
        thetaRepository.setOptions(options)
    }

    /**
     * Convert ThetaRepository.Options to Options.
     */
    @Test
    fun convertOptionFileFormatTest() = runTest {
        val values = listOf(
            Pair(ThetaRepository.FileFormatEnum.IMAGE_2K, MediaFileFormat(MediaType.JPEG, 2048, 1024, null, null)),
            Pair(ThetaRepository.FileFormatEnum.IMAGE_5K, MediaFileFormat(MediaType.JPEG, 5376, 2688, null, null)),
            Pair(ThetaRepository.FileFormatEnum.IMAGE_6_7K, MediaFileFormat(MediaType.JPEG, 6720, 3360, null, null)),
            Pair(ThetaRepository.FileFormatEnum.RAW_P_6_7K, MediaFileFormat(MediaType.RAW, 6720, 3360, null, null)),
            Pair(ThetaRepository.FileFormatEnum.IMAGE_5_5K, MediaFileFormat(MediaType.JPEG, 5504, 2752, null, null)),
            Pair(ThetaRepository.FileFormatEnum.IMAGE_11K, MediaFileFormat(MediaType.JPEG, 11008, 5504, null, null)),
            Pair(ThetaRepository.FileFormatEnum.VIDEO_HD, MediaFileFormat(MediaType.MP4, 1280, 720, null, null)),
            Pair(ThetaRepository.FileFormatEnum.VIDEO_FULL_HD, MediaFileFormat(MediaType.MP4, 1920, 1080, null, null)),
            Pair(ThetaRepository.FileFormatEnum.VIDEO_2K, MediaFileFormat(MediaType.MP4, 1920, 960, "H.264/MPEG-4 AVC", null)),
            Pair(ThetaRepository.FileFormatEnum.VIDEO_4K, MediaFileFormat(MediaType.MP4, 3840, 1920, "H.264/MPEG-4 AVC", null)),
            Pair(ThetaRepository.FileFormatEnum.VIDEO_2K_30F, MediaFileFormat(MediaType.MP4, 1920, 960, "H.264/MPEG-4 AVC", 30)),
            Pair(ThetaRepository.FileFormatEnum.VIDEO_2K_60F, MediaFileFormat(MediaType.MP4, 1920, 960, "H.264/MPEG-4 AVC", 60)),
            Pair(ThetaRepository.FileFormatEnum.VIDEO_4K_30F, MediaFileFormat(MediaType.MP4, 3840, 1920, "H.264/MPEG-4 AVC", 30)),
            Pair(ThetaRepository.FileFormatEnum.VIDEO_4K_60F, MediaFileFormat(MediaType.MP4, 3840, 1920, "H.264/MPEG-4 AVC", 60)),
            Pair(ThetaRepository.FileFormatEnum.VIDEO_5_7K_2F, MediaFileFormat(MediaType.MP4, 5760, 2880, "H.264/MPEG-4 AVC", 2)),
            Pair(ThetaRepository.FileFormatEnum.VIDEO_5_7K_5F, MediaFileFormat(MediaType.MP4, 5760, 2880, "H.264/MPEG-4 AVC", 5)),
            Pair(ThetaRepository.FileFormatEnum.VIDEO_5_7K_30F, MediaFileFormat(MediaType.MP4, 5760, 2880, "H.264/MPEG-4 AVC", 30)),
            Pair(ThetaRepository.FileFormatEnum.VIDEO_7K_2F, MediaFileFormat(MediaType.MP4, 7680, 3840, "H.264/MPEG-4 AVC", 2)),
            Pair(ThetaRepository.FileFormatEnum.VIDEO_7K_5F, MediaFileFormat(MediaType.MP4, 7680, 3840, "H.264/MPEG-4 AVC", 5)),
            Pair(ThetaRepository.FileFormatEnum.VIDEO_7K_10F, MediaFileFormat(MediaType.MP4, 7680, 3840, "H.264/MPEG-4 AVC", 10)),
            Pair(null, MediaFileFormat(MediaType.MP4, 0, 0, "H.264/MPEG-4 AVC", 10))
        )

        values.forEach {
            val orgOptions = Options(
                fileFormat = it.second
            )
            val options = ThetaRepository.Options(orgOptions)
            assertEquals(options.fileFormat, it.first, "fileFormat ${it.second}")
        }

        values.filter {
            it.first != null
        }.forEach {
            val orgOptions = ThetaRepository.Options(
                fileFormat = it.first
            )
            val options = orgOptions.toOptions()
            assertEquals(options.fileFormat, it.second, "fileFormat ${it.second}")
        }
    }
}
