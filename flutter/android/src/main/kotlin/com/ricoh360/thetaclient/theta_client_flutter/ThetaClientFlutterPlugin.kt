package com.ricoh360.thetaclient.theta_client_flutter

import android.util.Log
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.capture.PhotoCapture
import com.ricoh360.thetaclient.capture.VideoCapture
import com.ricoh360.thetaclient.capture.VideoCapturing
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** ThetaClientFlutterPlugin */
class ThetaClientFlutterPlugin : FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel

    val scope = CoroutineScope(Dispatchers.IO)
    val scopeMain = CoroutineScope(Dispatchers.Main)
    var endpoint = "http://192.168.1.1:80/"
    var thetaRepository: ThetaRepository? = null
    var previewing = false
    lateinit var eventChannel: EventChannel
    var eventSink: EventChannel.EventSink? = null

    var photoCaptureBuilder: PhotoCapture.Builder? = null
    var photoCapture: PhotoCapture? = null
    var videoCaptureBuilder: VideoCapture.Builder? = null
    var videoCapture: VideoCapture? = null
    var videoCapturing: VideoCapturing? = null

    val errorCode: String = "Error"
    val messageNotInit: String = "Not initialized."
    val messageNoResult: String = "Result is Null."
    val messageNoArgument: String = "No Argument."

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "theta_client_flutter")
        channel.setMethodCallHandler(this)
        eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "theta_client_flutter/live_preview")
        eventChannel.setStreamHandler(
            object : EventChannel.StreamHandler {
                override fun onListen(arguments: Any?, events: EventChannel.EventSink) {
                    eventSink = events
                }
                override fun onCancel(arguments: Any?) {
                    Log.w("Android", "EventChannel onCancel called")
                }
            }
        )
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "getPlatformVersion" -> {
                result.success("Android ${android.os.Build.VERSION.RELEASE}")
            }
            "initialize" -> {
                scope.launch {
                    initialize(call, result)
                }
            }
            "isInitialized" -> {
                isInitialized(result)
            }
            "restoreSettings" -> {
                scope.launch {
                    restoreSettings(result)
                }
            }
            "getThetaInfo" -> {
                scope.launch {
                    getThetaInfo(result)
                }
            }
            "getThetaState" -> {
                scope.launch {
                    getThetaState(result)
                }
            }
            "getLivePreview" -> {
                scope.launch {
                    getLivePreview(result)
                }
            }
            "stopLivePreview" -> {
                stopLivePreview(result)
            }
            "listFiles" -> {
                scope.launch {
                    listFiles(call, result)
                }
            }
            "deleteFiles" -> {
                scope.launch {
                    deleteFiles(call, result)
                }
            }
            "deleteAllFiles" -> {
                scope.launch {
                    deleteAllFiles(result)
                }
            }
            "deleteAllImageFiles" -> {
                scope.launch {
                    deleteAllImageFiles(result)
                }
            }
            "deleteAllVideoFiles" -> {
                scope.launch {
                    deleteAllVideoFiles(result)
                }
            }
            "getPhotoCaptureBuilder" -> {
                getPhotoCaptureBuilder(result)
            }
            "buildPhotoCapture" -> {
                scope.launch {
                    buildPhotoCapture(call, result)
                }
            }
            "takePicture" -> {
                takePicture(result)
            }
            "getVideoCaptureBuilder" -> {
                getVideoCaptureBuilder(result)
            }
            "buildVideoCapture" -> {
                scope.launch {
                    buildVideoCapture(call, result)
                }
            }
            "startVideoCapture" -> {
                startVideoCapture(result)
            }
            "stopVideoCapture" -> {
                stopVideoCapture(result)
            }
            "getOptions" -> {
                scope.launch {
                    getOptions(call, result)
                }
            }
            "setOptions" -> {
                scope.launch {
                    setOptions(call, result)
                }
            }
            "getMetadata" -> {
                scope.launch {
                    getMetadata(call, result)
                }
            }
            "reset" -> {
                scope.launch {
                    reset(result)
                }
            }
            "stopSelfTimer" -> {
                scope.launch {
                    stopSelfTimer(result)
                }
            }
            "convertVideoFormats" -> {
                scope.launch {
                    convertVideoFormats(call, result)
                }
            }
            "cancelVideoConvert" -> {
                scope.launch {
                    cancelVideoConvert(result)
                }
            }
            "finishWlan" -> {
                scope.launch {
                    finishWlan(result)
                }
            }
            "listAccessPoints" -> {
                scope.launch {
                    listAccessPoints(result)
                }
            }
            "setAccessPointDynamically" -> {
                scope.launch {
                    setAccessPointDynamically(call, result)
                }
            }
            "setAccessPointStatically" -> {
                scope.launch {
                    setAccessPointStatically(call, result)
                }
            }
            "deleteAccessPoint" -> {
                scope.launch {
                    deleteAccessPoint(call, result)
                }
            }
            "getMySetting" -> {
                scope.launch {
                    getMySetting(call, result)
                }
            }
            "setMySetting" -> {
                scope.launch {
                    setMySetting(call, result)
                }
            }
            "deleteMySetting" -> {
                scope.launch {
                    deleteMySetting(call, result)
                }
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    suspend fun initialize(call: MethodCall, result: Result) {
        try {
            endpoint = call.argument<String>("endpoint")!!
            val config = call.argument<Map<String, Any>>("config")?.let {
                toConfigParam(it)
            }
            val timeout = call.argument<Map<String, Any>>("timeout")?.let {
                toTimeoutParam(it)
            }
            thetaRepository = ThetaRepository.newInstance(endpoint, config, timeout)
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    fun isInitialized(result: Result) {
        result.success(thetaRepository != null)
    }

    suspend fun restoreSettings(result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            thetaRepository!!.restoreSettings()
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun getThetaInfo(result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            val response = thetaRepository!!.getThetaInfo()
            result.success(toResult(response))
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun getThetaState(result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            val response = thetaRepository!!.getThetaState()
            result.success(toResult(response))
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun getLivePreview(result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }

        previewing = true
        try {
            thetaRepository!!.getLivePreview {
                if (eventSink != null) {
                    val data = it.first.sliceArray(IntRange(0, it.second - 1))
                    scopeMain.launch {
                        if (previewing) {
                            eventSink?.success(data)
                        }
                    }
                }
                @Suppress("LABEL_NAME_CLASH")
                return@getLivePreview previewing
            }
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    fun stopLivePreview(result: Result) {
        previewing = false
        result.success(null)
    }

    fun getPhotoCaptureBuilder(result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        photoCaptureBuilder = thetaRepository!!.getPhotoCaptureBuilder()
        result.success(null)
    }

    suspend fun buildPhotoCapture(call: MethodCall, result: Result) {
        if (thetaRepository == null || photoCaptureBuilder == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        setCaptureBuilderParams(call, photoCaptureBuilder!!)
        setPhotoCaptureBuilderParams(call, photoCaptureBuilder!!)
        try {
            photoCapture = photoCaptureBuilder!!.build()
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    fun takePicture(result: Result) {
        if (thetaRepository == null || photoCapture == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        photoCapture!!.takePicture(object : PhotoCapture.TakePictureCallback {
            override fun onSuccess(fileUrl: String) {
                result.success(fileUrl)
            }
            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                result.error(exception.javaClass.simpleName, exception.message, null)
            }
        })
    }

    fun getVideoCaptureBuilder(result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        videoCaptureBuilder = thetaRepository!!.getVideoCaptureBuilder()
        result.success(null)
    }

    suspend fun buildVideoCapture(call: MethodCall, result: Result) {
        if (thetaRepository == null || videoCaptureBuilder == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        setCaptureBuilderParams(call, videoCaptureBuilder!!)
        setVideoCaptureBuilderParams(call, videoCaptureBuilder!!)
        try {
            videoCapture = videoCaptureBuilder!!.build()
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    fun startVideoCapture(result: Result) {
        if (thetaRepository == null || videoCapture == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        videoCapturing = videoCapture!!.startCapture(object : VideoCapture.StartCaptureCallback {
            override fun onSuccess(fileUrl: String) {
                result.success(fileUrl)
            }
            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                result.error(exception.javaClass.simpleName, exception.message, null)
            }
        })
    }

    fun stopVideoCapture(result: Result) {
        if (thetaRepository == null || videoCapturing == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        videoCapturing!!.stopCapture()
        result.success(null)
    }

    suspend fun listFiles(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            val fileTypeName = call.argument<String>("fileType")!!
            val fileType = ThetaRepository.FileTypeEnum.values().find {
                it.name == fileTypeName
            }!!
            val startPosition = call.argument<Int>("startPosition")!!
            val entryCount = call.argument<Int>("entryCount")!!
            val response = thetaRepository!!.listFiles(fileType, startPosition, entryCount)
            val resultmap: Map<String, Any> = mapOf(
                "fileList" to toResult(response!!.fileList),
                "totalEntries" to response!!.totalEntries,
            )
            result.success(resultmap)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun deleteFiles(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            @Suppress("UNCHECKED_CAST")
            val params = call.arguments as List<String>
            thetaRepository!!.deleteFiles(params)
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun deleteAllFiles(result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            thetaRepository!!.deleteAllFiles()
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun deleteAllImageFiles(result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            thetaRepository!!.deleteAllImageFiles()
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun deleteAllVideoFiles(result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            thetaRepository!!.deleteAllVideoFiles()
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun getOptions(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            @Suppress("UNCHECKED_CAST")
            val params = toGetOptionsParam(call.arguments as List<String>)
            val response = thetaRepository!!.getOptions(params)
            result.success(toResult(response))
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun setOptions(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            @Suppress("UNCHECKED_CAST")
            val params = toSetOptionsParam(call.arguments as Map<String, Any>)
            thetaRepository!!.setOptions(params)
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun getMetadata(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            @Suppress("UNCHECKED_CAST")
            val params = call.arguments as String
            val response = thetaRepository!!.getMetadata(params)
            result.success(toResult(response))
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun reset(result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            thetaRepository!!.reset()
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun stopSelfTimer(result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            thetaRepository!!.stopSelfTimer()
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun convertVideoFormats(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            val fileUrl = call.argument<String>("fileUrl")!!
            val toLowResolution = call.argument<Boolean>("toLowResolution")!!
            val applyTopBottomCorrection = call.argument<Boolean>("applyTopBottomCorrection")!!
            val response = thetaRepository!!.convertVideoFormats(fileUrl, toLowResolution, applyTopBottomCorrection)
            result.success(response)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun cancelVideoConvert(result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            thetaRepository!!.cancelVideoConvert()
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun finishWlan(result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            thetaRepository!!.finishWlan()
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun listAccessPoints(result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            val response = thetaRepository!!.listAccessPoints()
            result.success(toListAccessPointsResult(response))
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun setAccessPointDynamically(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            val ssid = call.argument<String>("ssid")!!
            val ssidStealth = call.argument<Boolean>("ssidStealth")!!
            val authModeName = call.argument<String>("authMode")!!
            val authMode = ThetaRepository.AuthModeEnum.values().find {
                it.name == authModeName
            }!!
            val password = call.argument<String>("password")!!
            val connectionPriority = call.argument<Int>("connectionPriority")!!
            thetaRepository!!.setAccessPointDynamically(ssid, ssidStealth, authMode, password, connectionPriority)
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun setAccessPointStatically(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            val ssid = call.argument<String>("ssid")!!
            val ssidStealth = call.argument<Boolean>("ssidStealth")!!
            val authModeName = call.argument<String>("authMode")!!
            val authMode = ThetaRepository.AuthModeEnum.values().find {
                it.name == authModeName
            }!!
            val password = call.argument<String>("password")!!
            val connectionPriority = call.argument<Int>("connectionPriority")!!
            val ipAddress = call.argument<String>("ipAddress")!!
            val subnetMask = call.argument<String>("subnetMask")!!
            val defaultGateway = call.argument<String>("defaultGateway")!!
            thetaRepository!!.setAccessPointStatically(ssid, ssidStealth, authMode, password, connectionPriority, ipAddress, subnetMask, defaultGateway)
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun deleteAccessPoint(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            val params = call.arguments as String
            thetaRepository!!.deleteAccessPoint(params)
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun getMySetting(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }

        try {
            var captureMode: ThetaRepository.CaptureModeEnum? = null
            var optionNames: List<ThetaRepository.OptionNameEnum>? = null
            if (call.hasArgument("captureMode")) {
                val captureModeName = call.argument<String>("captureMode")
                captureMode = ThetaRepository.CaptureModeEnum.values().find {
                    it.name == captureModeName
                }
            } else if (call.hasArgument("optionNames")) {
                optionNames = toGetOptionsParam(data = call.argument<Any>("optionNames") as List<String>)
            }

            var response: ThetaRepository.Options? = null
            if (captureMode != null) {
                response = thetaRepository?.getMySetting(captureMode = captureMode)
            } else if (optionNames != null) {
                response = thetaRepository?.getMySetting(optionNames = optionNames)
            }

            response?.let {
                result.success(toResult(options = it))
            } ?: run {
                result.error(errorCode, messageNoResult, null)
            }
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun setMySetting(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            val captureModeName = call.argument<String>("captureMode")
            val captureMode = ThetaRepository.CaptureModeEnum.values().find {
                it.name == captureModeName
            }
            val options = toSetOptionsParam(call.argument<Any>("options") as Map<String, Any>)

            captureMode?.let {
                thetaRepository?.setMySetting(captureMode = it, options = options)
                result.success(null)
            } ?: run {
                result.error(errorCode, messageNoArgument, null)
            }
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun deleteMySetting(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            val captureModeName = call.argument<String>("captureMode")
            val captureMode = ThetaRepository.CaptureModeEnum.values().find {
                it.name == captureModeName
            }

            captureMode?.let {
                thetaRepository?.deleteMySetting(captureMode = it)
                result.success(null)
            } ?: run {
                result.error(errorCode, messageNoArgument, null)
            }
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }
}