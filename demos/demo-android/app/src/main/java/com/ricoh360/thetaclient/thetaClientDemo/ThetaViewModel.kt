package com.ricoh360.thetaclient.thetaClientDemo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ricoh360.thetaclient.DigestAuth
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.capture.PhotoCapture
import io.ktor.client.utils.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.streams.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

/**
 * ViewModel of Theta
 *
 * @param thetaUrl The endpoint of the theta.
 * @param ioDispatcher CoroutineDispatcher for calling Web API.
 */
class ThetaViewModel(
    private val thetaUrl: String = "http://192.168.1.1:80/",
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    private var thetaRepository: ThetaRepository
    private val _thetaInfoState =  MutableStateFlow<ThetaRepository.ThetaInfo?>(null)
    val thetaInfoState: StateFlow<ThetaRepository.ThetaInfo?> = _thetaInfoState
    private val _thetaFilesState = MutableStateFlow<ThetaRepository.ThetaFiles?>(null)
    val thetaFilesState: StateFlow<ThetaRepository.ThetaFiles?>  = _thetaFilesState

    private val _previewFlow = MutableStateFlow<Bitmap?>(null)
    val previewFlow: StateFlow<Bitmap?> = _previewFlow
    private var previewJob: Job? = null

    init {
        runBlocking {
            withContext(ioDispatcher) {
                // Theta configuration is optional.
                val thetaConfig = ThetaRepository.Config()
                thetaConfig.dateTime = "2023:01:01 12:34:56+09:00" // set current time
                thetaConfig.language = ThetaRepository.LanguageEnum.JA
                thetaConfig.shutterVolume = 40 // 0 to 100
                thetaConfig.sleepDelay = ThetaRepository.SleepDelayEnum.SLEEP_DELAY_5M
                thetaConfig.offDelay = ThetaRepository.OffDelayEnum.DISABLE

                // Client mode authentication settings
//                val clientMode = DigestAuth("THETAXX12345678", "12345678")
//                thetaConfig.clientMode = clientMode

                thetaRepository = ThetaRepository.newInstance(thetaUrl, thetaConfig)
                // thetaRepository = ThetaRepository.newInstance(thetaUrl) // You can specify just a URL
            }
        }

        viewModelScope.launch(ioDispatcher) {
            _thetaInfoState.emit(thetaRepository.getThetaInfo())
        }

        viewModelScope.launch(ioDispatcher) {
            _thetaFilesState.emit(thetaRepository.listFiles(fileType = ThetaRepository.FileTypeEnum.ALL, entryCount = 100))
        }
    }


    fun startPreview() {
        previewJob?.let {
            return
        }
        previewJob = viewModelScope.launch(ioDispatcher) {
            kotlin.runCatching {
                thetaRepository.getLivePreview()
                    .collect { byteReadPacket ->
                        if (isActive) {
                            byteReadPacket.inputStream().use {
                                _previewFlow.emit(BitmapFactory.decodeStream(it))
                            }
                        }
                        byteReadPacket.release()
                    }
            }.onFailure {
                Timber.e(it)
            }
        }
    }


    @Suppress("BlockingMethodInNonBlockingContext")
    fun startPreviewWithCallback() {
        previewJob?.let {
            return
        }
        previewJob = viewModelScope.launch(ioDispatcher) {
            runCatching {
                thetaRepository.getLivePreview { frame: Pair<ByteArray, Int> ->
                    if(!isActive) {
                        Timber.d("Terminate preview")
                        return@getLivePreview false
                    }
                    frame.first.inputStream().use {
                        _previewFlow.emit(BitmapFactory.decodeStream(it))
                    }
                    return@getLivePreview true
                }
            }.onFailure {
                Timber.e(it)
            }
        }
    }

    fun stopPreview () {
        previewJob?.let {
            it.cancel()
            previewJob = null
        }
    }

    /**
     * Take a photo.
     * @exception ThetaRepository.ThetaRepositoryException
     */
    fun takePhoto(callback: PhotoCapture.TakePictureCallback) {
        stopPreview()
        viewModelScope.launch(ioDispatcher) {
            thetaRepository.getPhotoCaptureBuilder()
                .setExposureProgram(ThetaRepository.ExposureProgramEnum.NORMAL_PROGRAM)
                .setWhiteBalance(ThetaRepository.WhiteBalanceEnum.AUTO)
                .setExposureCompensation(ThetaRepository.ExposureCompensationEnum.ZERO)
                .setIsoAutoHighLimit(ThetaRepository.IsoAutoHighLimitEnum.ISO_800)
                .setFilter(ThetaRepository.FilterEnum.HDR)
                .setExposureDelay(ThetaRepository.ExposureDelayEnum.DELAY_OFF) // self-timer
                .build()
                .takePicture(callback)
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopPreview()
    }

}