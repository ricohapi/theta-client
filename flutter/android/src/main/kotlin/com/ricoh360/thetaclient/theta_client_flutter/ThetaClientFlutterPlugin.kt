package com.ricoh360.thetaclient.theta_client_flutter

import android.util.Log
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.capture.*
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
    lateinit var eventNotifyChannel: EventChannel
    var eventNotifySink: EventChannel.EventSink? = null

    var photoCaptureBuilder: PhotoCapture.Builder? = null
    var photoCapture: PhotoCapture? = null
    var timeShiftCaptureBuilder: TimeShiftCapture.Builder? = null
    var timeShiftCapture: TimeShiftCapture? = null
    var timeShiftCapturing: TimeShiftCapturing? = null
    var videoCaptureBuilder: VideoCapture.Builder? = null
    var videoCapture: VideoCapture? = null
    var videoCapturing: VideoCapturing? = null
    var limitlessIntervalCaptureBuilder: LimitlessIntervalCapture.Builder? = null
    var limitlessIntervalCapture: LimitlessIntervalCapture? = null
    var limitlessIntervalCapturing: LimitlessIntervalCapturing? = null
    var shotCountSpecifiedIntervalCaptureBuilder: ShotCountSpecifiedIntervalCapture.Builder? = null
    var shotCountSpecifiedIntervalCapture: ShotCountSpecifiedIntervalCapture? = null
    var shotCountSpecifiedIntervalCapturing: ShotCountSpecifiedIntervalCapturing? = null
    var compositeIntervalCaptureBuilder: CompositeIntervalCapture.Builder? = null
    var compositeIntervalCapture: CompositeIntervalCapture? = null
    var compositeIntervalCapturing: CompositeIntervalCapturing? = null
    var burstCaptureBuilder: BurstCapture.Builder? = null
    var burstCapture: BurstCapture? = null
    var burstCapturing: BurstCapturing? = null
    var multiBracketCaptureBuilder: MultiBracketCapture.Builder? = null
    var multiBracketCapture: MultiBracketCapture? = null
    var multiBracketCapturing: MultiBracketCapturing? = null
    var continuousCaptureBuilder: ContinuousCapture.Builder? = null
    var continuousCapture: ContinuousCapture? = null

    companion object {
        const val errorCode: String = "Error"
        const val messageNotInit: String = "Not initialized."
        const val messageNoResult: String = "Result is Null."
        const val messageNoArgument: String = "No Argument."
        const val messageLivePreviewRunning: String = "Live preview is running."

        const val eventNameNotify = "theta_client_flutter/theta_notify"
        const val notifyIdLivePreview = 10001
        const val notifyIdTimeShiftProgress = 10011
        const val notifyIdTimeShiftStopError = 10012
        const val notifyIdTimeShiftCapturing = 10013
        const val notifyIdLimitlessIntervalCaptureStopError = 10004
        const val notifyIdLimitlessIntervalCaptureCapturing = 10005
        const val notifyIdShotCountSpecifiedIntervalCaptureProgress = 10021
        const val notifyIdShotCountSpecifiedIntervalCaptureStopError = 10022
        const val notifyIdShotCountSpecifiedIntervalCaptureCapturing = 10023
        const val notifyIdCompositeIntervalCaptureProgress = 10031;
        const val notifyIdCompositeIntervalCaptureStopError = 10032;
        const val notifyIdCompositeIntervalCaptureCapturing = 10033;
        const val notifyIdMultiBracketCaptureProgress = 10041;
        const val notifyIdMultiBracketCaptureStopError = 10042;
        const val notifyIdMultiBracketCaptureCapturing = 10043;
        const val notifyIdBurstCaptureProgress = 10051;
        const val notifyIdBurstCaptureStopError = 10052;
        const val notifyIdBurstCaptureCapturing = 10053
        const val notifyIdContinuousCaptureProgress = 10061;
        const val notifyIdContinuousCaptureCapturing = 10062;
        const val notifyIdPhotoCapturing = 10071
        const val notifyIdVideoCaptureStopError = 10081
        const val notifyIdVideoCaptureCapturing = 10082
        const val notifyIdVideoCaptureStarted = 10083
        const val notifyIdConvertVideoFormatsProgress = 10091
    }

    fun sendNotifyEvent(id: Int, params: Map<String, Any?>) {
        scopeMain.launch {
            eventNotifySink?.success(toNotify(id, params))
        }
    }

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "theta_client_flutter")
        channel.setMethodCallHandler(this)

        eventNotifyChannel = EventChannel(flutterPluginBinding.binaryMessenger, eventNameNotify)
        eventNotifyChannel.setStreamHandler(
            object : EventChannel.StreamHandler {
                override fun onListen(arguments: Any?, events: EventChannel.EventSink) {
                    eventNotifySink = events
                }

                override fun onCancel(arguments: Any?) {
                    Log.w("Android", "EventChannel onCancel called to theta notify")
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

            "getThetaModel" -> {
                getThetaModel(result)
            }

            "getThetaInfo" -> {
                scope.launch {
                    getThetaInfo(result)
                }
            }

            "getThetaLicense" -> {
                scope.launch {
                    getThetaLicense(result)
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

            "getTimeShiftCaptureBuilder" -> {
                getTimeShiftCaptureBuilder(result)
            }

            "buildTimeShiftCapture" -> {
                scope.launch {
                    buildTimeShiftCapture(call, result)
                }
            }

            "startTimeShiftCapture" -> {
                startTimeShiftCapture(result)
            }

            "stopTimeShiftCapture" -> {
                stopTimeShiftCapture(result)
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

            "getLimitlessIntervalCaptureBuilder" -> {
                getLimitlessIntervalCaptureBuilder(result)
            }

            "buildLimitlessIntervalCapture" -> {
                scope.launch {
                    buildLimitlessIntervalCapture(call, result)
                }
            }

            "startLimitlessIntervalCapture" -> {
                startLimitlessIntervalCapture(result)
            }

            "stopLimitlessIntervalCapture" -> {
                stopLimitlessIntervalCapture(result)
            }

            "getShotCountSpecifiedIntervalCaptureBuilder" -> {
                getShotCountSpecifiedIntervalCaptureBuilder(call, result)
            }

            "buildShotCountSpecifiedIntervalCapture" -> {
                scope.launch {
                    buildShotCountSpecifiedIntervalCapture(call, result)
                }
            }

            "startShotCountSpecifiedIntervalCapture" -> {
                startShotCountSpecifiedIntervalCapture(result)
            }

            "stopShotCountSpecifiedIntervalCapture" -> {
                stopShotCountSpecifiedIntervalCapture(result)
            }

            "getCompositeIntervalCaptureBuilder" -> {
                getCompositeIntervalCaptureBuilder(call, result)
            }

            "buildCompositeIntervalCapture" -> {
                scope.launch {
                    buildCompositeIntervalCapture(call, result)
                }
            }

            "startCompositeIntervalCapture" -> {
                startCompositeIntervalCapture(result)
            }

            "stopCompositeIntervalCapture" -> {
                stopCompositeIntervalCapture(result)
            }

            "getBurstCaptureBuilder" -> {
                getBurstCaptureBuilder(call, result)
            }

            "buildBurstCapture" -> {
                scope.launch {
                    buildBurstCapture(call, result)
                }
            }

            "startBurstCapture" -> {
                startBurstCapture(result)
            }

            "stopBurstCapture" -> {
                stopBurstCapture(result)
            }

            "getMultiBracketCaptureBuilder" -> {
                getMultiBracketCaptureBuilder(result)
            }

            "buildMultiBracketCapture" -> {
                scope.launch {
                    buildMultiBracketCapture(call, result)
                }
            }

            "startMultiBracketCapture" -> {
                startMultiBracketCapture(result)
            }

            "stopMultiBracketCapture" -> {
                stopMultiBracketCapture(result)
            }

            "getContinuousCaptureBuilder" -> {
                getContinuousCaptureBuilder(result)
            }

            "buildContinuousCapture" -> {
                scope.launch {
                    buildContinuousCapture(call, result)
                }
            }

            "startContinuousCapture" -> {
                startContinuousCapture(result)
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

            "getMySettingFromOldModel" -> {
                scope.launch {
                    getMySettingFromOldModel(call, result)
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

            "listPlugins" -> {
                scope.launch {
                    listPlugins(call, result)
                }
            }

            "setPlugin" -> {
                scope.launch {
                    setPlugin(call, result)
                }
            }

            "startPlugin" -> {
                scope.launch {
                    startPlugin(call, result)
                }
            }

            "stopPlugin" -> {
                scope.launch {
                    stopPlugin(call, result)
                }
            }

            "getPluginLicense" -> {
                scope.launch {
                    getPluginLicense(call, result)
                }
            }

            "getPluginOrders" -> {
                scope.launch {
                    getPluginOrders(call, result)
                }
            }

            "setPluginOrders" -> {
                scope.launch {
                    setPluginOrders(call, result)
                }
            }

            "setBluetoothDevice" -> {
                scope.launch {
                    setBluetoothDevice(call, result)
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
        thetaRepository = null
        previewing = false
        photoCaptureBuilder = null
        photoCapture = null
        timeShiftCaptureBuilder = null
        timeShiftCapture = null
        timeShiftCapturing = null
        videoCaptureBuilder = null
        videoCapture = null
        videoCapturing = null
        limitlessIntervalCaptureBuilder = null
        limitlessIntervalCapture = null
        limitlessIntervalCapturing = null
        shotCountSpecifiedIntervalCaptureBuilder = null
        shotCountSpecifiedIntervalCapture = null
        shotCountSpecifiedIntervalCapturing = null
        compositeIntervalCaptureBuilder = null
        compositeIntervalCapture = null
        compositeIntervalCapturing = null
        burstCaptureBuilder = null
        burstCapture = null
        burstCapturing = null
        multiBracketCaptureBuilder = null
        multiBracketCapture = null
        multiBracketCapturing = null
        continuousCaptureBuilder = null
        continuousCapture = null

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

    fun getThetaModel(result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        result.success(thetaRepository?.cameraModel?.name)
    }

    suspend fun getThetaInfo(result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            val response = thetaRepository!!.getThetaInfo()
            val thetaInfoMap = toResult(response).toMutableMap()
            thetaRepository?.cameraModel?.let {
                thetaInfoMap.put("thetaModel", it.name)
            }
            result.success(thetaInfoMap)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun getThetaLicense(result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            val response = thetaRepository!!.getThetaLicense()
            result.success(response)
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
        if (previewing) {
            result.error(errorCode, messageLivePreviewRunning, null)
            return
        }

        val theta = thetaRepository
        if (theta == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }

        previewing = true
        try {
            theta.getLivePreview {
                val data = it.first.sliceArray(IntRange(0, it.second - 1))
                sendNotifyEvent(notifyIdLivePreview, toPreviewNotifyParam(data))
                @Suppress("LABEL_NAME_CLASH")
                return@getLivePreview previewing
            }
            result.success(null)
        } catch (e: Exception) {
            previewing = false
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    fun stopLivePreview(result: Result) {
        previewing = false
        result.success(true)
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
            override fun onSuccess(fileUrl: String?) {
                result.success(fileUrl)
            }

            override fun onCapturing(status: CapturingStatusEnum) {
                sendNotifyEvent(
                    notifyIdPhotoCapturing,
                    toCapturingNotifyParam(status)
                )
            }

            override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
                result.error(exception.javaClass.simpleName, exception.message, null)
            }
        })
    }

    fun getTimeShiftCaptureBuilder(result: Result) {
        val theta = thetaRepository
        if (theta == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        timeShiftCaptureBuilder = theta.getTimeShiftCaptureBuilder()
        result.success(null)
    }

    suspend fun buildTimeShiftCapture(call: MethodCall, result: Result) {
        val theta = thetaRepository
        val timeShiftCaptureBuilder = timeShiftCaptureBuilder
        if (theta == null || timeShiftCaptureBuilder == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        setCaptureBuilderParams(call, timeShiftCaptureBuilder)
        setTimeShiftCaptureBuilderParams(call, timeShiftCaptureBuilder)
        try {
            timeShiftCapture = timeShiftCaptureBuilder.build()
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    fun startTimeShiftCapture(result: Result) {
        val theta = thetaRepository
        val timeShiftCapture = timeShiftCapture
        if (theta == null || timeShiftCapture == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        timeShiftCapturing =
            timeShiftCapture.startCapture(object : TimeShiftCapture.StartCaptureCallback {
                override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    result.error(exception.javaClass.simpleName, exception.message, null)
                }

                override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    sendNotifyEvent(
                        notifyIdTimeShiftStopError,
                        toMessageNotifyParam(exception.message ?: exception.toString())
                    )
                }

                override fun onProgress(completion: Float) {
                    sendNotifyEvent(
                        notifyIdTimeShiftProgress,
                        toCaptureProgressNotifyParam(completion)
                    )
                }

                override fun onCapturing(status: CapturingStatusEnum) {
                    sendNotifyEvent(
                        notifyIdTimeShiftCapturing,
                        toCapturingNotifyParam(status)
                    )
                }

                override fun onCaptureCompleted(fileUrl: String?) {
                    result.success(fileUrl)
                }
            })
    }

    fun stopTimeShiftCapture(result: Result) {
        val theta = thetaRepository
        val timeShiftCapturing = timeShiftCapturing
        if (theta == null || timeShiftCapturing == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        timeShiftCapturing.stopCapture()
        result.success(null)
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
        val theta = thetaRepository
        val capture = videoCapture
        if (theta == null || capture == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        videoCapturing = capture.startCapture(object : VideoCapture.StartCaptureCallback {
            override fun onCaptureCompleted(fileUrl: String?) {
                result.success(fileUrl)
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                result.error(exception.javaClass.simpleName, exception.message, null)
            }

            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                sendNotifyEvent(
                    notifyIdVideoCaptureStopError,
                    toMessageNotifyParam(exception.message ?: exception.toString())
                )
            }

            override fun onCapturing(status: CapturingStatusEnum) {
                sendNotifyEvent(
                    notifyIdVideoCaptureCapturing,
                    toCapturingNotifyParam(status)
                )
            }

            override fun onCaptureStarted(fileUrl: String?) {
                sendNotifyEvent(
                    notifyIdVideoCaptureStarted,
                    toStartedNotifyParam(fileUrl ?: "")
                )
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

    fun getLimitlessIntervalCaptureBuilder(result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        limitlessIntervalCaptureBuilder = thetaRepository?.getLimitlessIntervalCaptureBuilder()
        result.success(null)
    }

    suspend fun buildLimitlessIntervalCapture(call: MethodCall, result: Result) {
        if (thetaRepository == null || limitlessIntervalCaptureBuilder == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        limitlessIntervalCaptureBuilder?.let {
            setCaptureBuilderParams(call, it)
            setLimitlessIntervalCaptureBuilderParams(call, it)
        }
        try {
            limitlessIntervalCapture = limitlessIntervalCaptureBuilder?.build()
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    fun startLimitlessIntervalCapture(result: Result) {
        val theta = thetaRepository
        val capture = limitlessIntervalCapture
        if (theta == null || capture == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        limitlessIntervalCapturing = capture.startCapture(object : LimitlessIntervalCapture.StartCaptureCallback {
            override fun onCaptureCompleted(fileUrls: List<String>?) {
                result.success(fileUrls)
            }

            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                result.error(exception.javaClass.simpleName, exception.message, null)
            }

            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                sendNotifyEvent(
                    notifyIdLimitlessIntervalCaptureStopError,
                    toMessageNotifyParam(exception.message ?: exception.toString())
                )
            }

            override fun onCapturing(status: CapturingStatusEnum) {
                sendNotifyEvent(
                    notifyIdLimitlessIntervalCaptureCapturing,
                    toCapturingNotifyParam(status)
                )
            }
        })
    }

    fun stopLimitlessIntervalCapture(result: Result) {
        if (thetaRepository == null || limitlessIntervalCapturing == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        limitlessIntervalCapturing?.stopCapture()
        result.success(null)
    }

    fun getShotCountSpecifiedIntervalCaptureBuilder(call: MethodCall, result: Result) {
        val theta = thetaRepository
        if (theta == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        (call.arguments as? Int)?.let {
            shotCountSpecifiedIntervalCaptureBuilder = theta.getShotCountSpecifiedIntervalCaptureBuilder(it)
        }
        result.success(null)
    }

    suspend fun buildShotCountSpecifiedIntervalCapture(call: MethodCall, result: Result) {
        val theta = thetaRepository
        val shotCountSpecifiedIntervalCaptureBuilder = shotCountSpecifiedIntervalCaptureBuilder
        if (theta == null || shotCountSpecifiedIntervalCaptureBuilder == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        setCaptureBuilderParams(call, shotCountSpecifiedIntervalCaptureBuilder)
        setShotCountSpecifiedIntervalCaptureBuilderParams(call, shotCountSpecifiedIntervalCaptureBuilder)
        try {
            shotCountSpecifiedIntervalCapture = shotCountSpecifiedIntervalCaptureBuilder.build()
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    fun startShotCountSpecifiedIntervalCapture(result: Result) {
        val theta = thetaRepository
        val shotCountSpecifiedIntervalCapture = shotCountSpecifiedIntervalCapture
        if (theta == null || shotCountSpecifiedIntervalCapture == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        shotCountSpecifiedIntervalCapturing =
            shotCountSpecifiedIntervalCapture.startCapture(object : ShotCountSpecifiedIntervalCapture.StartCaptureCallback {
                override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    result.error(exception.javaClass.simpleName, exception.message, null)
                }

                override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    sendNotifyEvent(
                        notifyIdShotCountSpecifiedIntervalCaptureStopError,
                        toMessageNotifyParam(exception.message ?: exception.toString())
                    )
                }

                override fun onProgress(completion: Float) {
                    sendNotifyEvent(
                        notifyIdShotCountSpecifiedIntervalCaptureProgress,
                        toCaptureProgressNotifyParam(completion)
                    )
                }

                override fun onCapturing(status: CapturingStatusEnum) {
                    sendNotifyEvent(
                        notifyIdShotCountSpecifiedIntervalCaptureCapturing,
                        toCapturingNotifyParam(status)
                    )
                }

                override fun onCaptureCompleted(fileUrls: List<String>?) {
                    result.success(fileUrls)
                }
            })
    }

    fun stopShotCountSpecifiedIntervalCapture(result: Result) {
        val theta = thetaRepository
        val shotCountSpecifiedIntervalCapturing = shotCountSpecifiedIntervalCapturing
        if (theta == null || shotCountSpecifiedIntervalCapturing == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        shotCountSpecifiedIntervalCapturing.stopCapture()
        result.success(null)
    }

    fun getCompositeIntervalCaptureBuilder(call: MethodCall, result: Result) {
        val theta = thetaRepository
        if (theta == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        (call.arguments as? Int)?.let {
            compositeIntervalCaptureBuilder = theta.getCompositeIntervalCaptureBuilder(it)
        }
        result.success(null)
    }

    suspend fun buildCompositeIntervalCapture(call: MethodCall, result: Result) {
        val theta = thetaRepository
        val compositeIntervalCaptureBuilder = compositeIntervalCaptureBuilder
        if (theta == null || compositeIntervalCaptureBuilder == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        setCaptureBuilderParams(call, compositeIntervalCaptureBuilder)
        setCompositeIntervalCaptureBuilderParams(call, compositeIntervalCaptureBuilder)
        try {
            compositeIntervalCapture = compositeIntervalCaptureBuilder.build()
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    fun startCompositeIntervalCapture(result: Result) {
        val theta = thetaRepository
        val compositeIntervalCapture = compositeIntervalCapture
        if (theta == null || compositeIntervalCapture == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        compositeIntervalCapturing =
            compositeIntervalCapture.startCapture(object : CompositeIntervalCapture.StartCaptureCallback {
                override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    result.error(exception.javaClass.simpleName, exception.message, null)
                }

                override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    sendNotifyEvent(
                        notifyIdCompositeIntervalCaptureStopError,
                        toMessageNotifyParam(exception.message ?: exception.toString())
                    )
                }

                override fun onProgress(completion: Float) {
                    sendNotifyEvent(
                        notifyIdCompositeIntervalCaptureProgress,
                        toCaptureProgressNotifyParam(completion)
                    )
                }

                override fun onCapturing(status: CapturingStatusEnum) {
                    sendNotifyEvent(
                        notifyIdCompositeIntervalCaptureCapturing,
                        toCapturingNotifyParam(status)
                    )
                }

                override fun onCaptureCompleted(fileUrls: List<String>?) {
                    result.success(fileUrls)
                }
            })
    }

    fun stopCompositeIntervalCapture(result: Result) {
        val theta = thetaRepository
        val compositeIntervalCapturing = compositeIntervalCapturing
        if (theta == null || compositeIntervalCapturing == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        compositeIntervalCapturing.stopCapture()
        result.success(null)
    }

    fun getBurstCaptureBuilder(call: MethodCall, result: Result) {
        val theta = thetaRepository
        if (theta == null) {
            result.error(errorCode, "theta " + messageNotInit, null)
            return
        }
        val burstCaptureNumVal = ThetaRepository.BurstCaptureNumEnum.values().find {
            it.name == call.argument<String>("burstCaptureNum")
        }
        val burstBracketStepVal = ThetaRepository.BurstBracketStepEnum.values().find {
            it.name == call.argument<String>("burstBracketStep")
        }
        val burstCompensationVal = ThetaRepository.BurstCompensationEnum.values().find {
            it.name == call.argument<String>("burstCompensation")
        }
        val burstMaxExposureTimeVal = ThetaRepository.BurstMaxExposureTimeEnum.values().find {
            it.name == call.argument<String>("burstMaxExposureTime")
        }
        val burstEnableIsoControlVal = ThetaRepository.BurstEnableIsoControlEnum.values().find {
            it.name == call.argument<String>("burstEnableIsoControl")
        }
        val burstOrderVal = ThetaRepository.BurstOrderEnum.values().find {
            it.name == call.argument<String>("burstOrder")
        }

        if (burstCaptureNumVal == null
            || burstBracketStepVal == null
            || burstCompensationVal == null
            || burstMaxExposureTimeVal == null
            || burstEnableIsoControlVal == null
            || burstOrderVal == null
        ) {
            result.error(errorCode, "burstCaptureBuilder " + messageNoArgument, null)
            return
        }

        burstCaptureBuilder = theta.getBurstCaptureBuilder(
            burstCaptureNumVal,
            burstBracketStepVal,
            burstCompensationVal,
            burstMaxExposureTimeVal,
            burstEnableIsoControlVal,
            burstOrderVal
        )
        result.success(null)
    }

    suspend fun buildBurstCapture(call: MethodCall, result: Result) {
        val theta = thetaRepository
        val burstCaptureBuilder = burstCaptureBuilder
        if (theta == null || burstCaptureBuilder == null) {
            result.error(errorCode, "burstCaptureBuilder " + messageNotInit, null)
            return
        }
        setCaptureBuilderParams(call, burstCaptureBuilder)
        setBurstCaptureBuilderParams(call, burstCaptureBuilder)
        try {
            burstCapture = burstCaptureBuilder.build()
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    fun startBurstCapture(result: Result) {
        val theta = thetaRepository
        val burstCapture = burstCapture
        if (theta == null || burstCapture == null) {
            result.error(errorCode, "burstCapture " + messageNotInit, null)
            return
        }
        burstCapturing = burstCapture.startCapture(object : BurstCapture.StartCaptureCallback {
            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                result.error(exception.javaClass.simpleName, exception.message, null)
            }

            override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                sendNotifyEvent(
                    notifyIdBurstCaptureStopError,
                    toMessageNotifyParam(exception.message ?: exception.toString())
                )
            }

            override fun onProgress(completion: Float) {
                sendNotifyEvent(
                    notifyIdBurstCaptureProgress,
                    toCaptureProgressNotifyParam(completion)
                )
            }

            override fun onCapturing(status: CapturingStatusEnum) {
                sendNotifyEvent(
                    notifyIdBurstCaptureCapturing,
                    toCapturingNotifyParam(status)
                )
            }

            override fun onCaptureCompleted(fileUrls: List<String>?) {
                result.success(fileUrls)
            }
        })
    }

    fun stopBurstCapture(result: Result) {
        val theta = thetaRepository
        val burstCapturing = burstCapturing
        if (theta == null || burstCapturing == null) {
            result.error(errorCode, "burstCapturing " + messageNotInit, null)
            return
        }
        burstCapturing.stopCapture()
        result.success(null)
    }

    fun getMultiBracketCaptureBuilder(result: Result) {
        val theta = thetaRepository
        if (theta == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        multiBracketCaptureBuilder = theta.getMultiBracketCaptureBuilder()
        result.success(null)
    }

    suspend fun buildMultiBracketCapture(call: MethodCall, result: Result) {
        val theta = thetaRepository
        val multiBracketCaptureBuilder = multiBracketCaptureBuilder
        if (theta == null || multiBracketCaptureBuilder == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        setCaptureBuilderParams(call, multiBracketCaptureBuilder)
        setMultiBracketCaptureBuilderParams(call, multiBracketCaptureBuilder)
        try {
            multiBracketCapture = multiBracketCaptureBuilder.build()
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    fun startMultiBracketCapture(result: Result) {
        val theta = thetaRepository
        val multiBracketCapture = multiBracketCapture
        if (theta == null || multiBracketCapture == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        multiBracketCapturing =
            multiBracketCapture.startCapture(object : MultiBracketCapture.StartCaptureCallback {
                override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    result.error(exception.javaClass.simpleName, exception.message, null)
                }

                override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
                    sendNotifyEvent(
                        notifyIdMultiBracketCaptureStopError,
                        toMessageNotifyParam(exception.message ?: exception.toString())
                    )
                }

                override fun onProgress(completion: Float) {
                    sendNotifyEvent(
                        notifyIdMultiBracketCaptureProgress,
                        toCaptureProgressNotifyParam(completion)
                    )
                }

                override fun onCapturing(status: CapturingStatusEnum) {
                    sendNotifyEvent(
                        notifyIdMultiBracketCaptureCapturing,
                        toCapturingNotifyParam(status)
                    )
                }

                override fun onCaptureCompleted(fileUrls: List<String>?) {
                    result.success(fileUrls)
                }
            })
    }

    fun stopMultiBracketCapture(result: Result) {
        val theta = thetaRepository
        val multiBracketCapturing = multiBracketCapturing
        if (theta == null || multiBracketCapturing == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        multiBracketCapturing.stopCapture()
        result.success(null)
    }

    fun getContinuousCaptureBuilder(result: Result) {
        val theta = thetaRepository
        if (theta == null) {
            result.error(errorCode, "theta " + messageNotInit, null)
            return
        }
        continuousCaptureBuilder = theta.getContinuousCaptureBuilder()
        result.success(null)
    }

    suspend fun buildContinuousCapture(call: MethodCall, result: Result) {
        val theta = thetaRepository
        val continuousCaptureBuilder = continuousCaptureBuilder
        if (theta == null || continuousCaptureBuilder == null) {
            result.error(errorCode, "continuousCaptureBuilder " + messageNotInit, null)
            return
        }
        setCaptureBuilderParams(call, continuousCaptureBuilder)
        setContinuousCaptureBuilderParams(call, continuousCaptureBuilder)
        try {
            continuousCapture = continuousCaptureBuilder.build()
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    fun startContinuousCapture(result: Result) {
        val theta = thetaRepository
        val continuousCapture = continuousCapture
        if (theta == null || continuousCapture == null) {
            result.error(errorCode, "continuousCapture " + messageNotInit, null)
            return
        }
        continuousCapture.startCapture(object : ContinuousCapture.StartCaptureCallback {
            override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
                result.error(exception.javaClass.simpleName, exception.message, null)
            }

            override fun onProgress(completion: Float) {
                sendNotifyEvent(
                    notifyIdContinuousCaptureProgress,
                    toCaptureProgressNotifyParam(completion)
                )
            }

            override fun onCapturing(status: CapturingStatusEnum) {
                sendNotifyEvent(
                    notifyIdContinuousCaptureCapturing,
                    toCapturingNotifyParam(status)
                )
            }

            override fun onCaptureCompleted(fileUrls: List<String>?) {
                result.success(fileUrls)
            }
        })
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
            val storage = call.argument<String>("storage")?.let { name ->
                ThetaRepository.StorageEnum.values().find {
                    it.name == name
                }
            }
            val response = thetaRepository!!.listFiles(fileType, startPosition, entryCount, storage)
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
            val response = thetaRepository!!.convertVideoFormats(
                fileUrl,
                toLowResolution,
                applyTopBottomCorrection
            ) { completion ->
                sendNotifyEvent(
                    notifyIdConvertVideoFormatsProgress,
                    toCaptureProgressNotifyParam(completion)
                )
            }
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
            val response = thetaRepository?.listAccessPoints()
            response?.let {
                result.success(toListAccessPointsResult(response))
            } ?: run {
                result.error(errorCode, messageNoResult, null)
            }
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

            var proxy: ThetaRepository.Proxy? = null
            (call.argument<Any>("proxy") as? Map<String, Any>)?.let {
                proxy = toProxy(map = it)
            }
            thetaRepository?.setAccessPointDynamically(ssid, ssidStealth, authMode, password, connectionPriority, proxy)
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

            var proxy: ThetaRepository.Proxy? = null
            (call.argument<Any>("proxy") as? Map<String, Any>)?.let {
                proxy = toProxy(map = it)
            }
            thetaRepository?.setAccessPointStatically(ssid, ssidStealth, authMode, password, connectionPriority, ipAddress, subnetMask, defaultGateway, proxy)
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
            thetaRepository?.deleteAccessPoint(params)
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
            val captureModeName = call.argument<String>("captureMode")
            val captureMode = ThetaRepository.CaptureModeEnum.values().find {
                it.name == captureModeName
            }

            var response: ThetaRepository.Options? = null
            captureMode?.let {
                response = thetaRepository?.getMySetting(captureMode = it)
            } ?: run {
                result.error(errorCode, messageNoArgument, null)
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

    suspend fun getMySettingFromOldModel(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }

        try {
            val names = call.argument<Any>("optionNames") as? List<String>
            names?.let { names ->
                val optionNames = toGetOptionsParam(data = names)
                val response = thetaRepository?.getMySetting(optionNames = optionNames)
                response?.let {
                    result.success(toResult(options = it))
                } ?: run {
                    result.error(errorCode, messageNoResult, null)
                }
            } ?: run {
                result.error(errorCode, messageNoArgument, null)
                return
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

    suspend fun listPlugins(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            val response = thetaRepository?.listPlugins()
            response?.let {
                result.success(toPluginInfosResult(pluginInfoList = response))
            } ?: run {
                result.error(errorCode, messageNoResult, null)
            }
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun setPlugin(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            val params = call.arguments as? String
            params?.let {
                thetaRepository?.setPlugin(packageName = params)
                result.success(null)
            } ?: run {
                result.error(errorCode, messageNoArgument, null)
            }
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun startPlugin(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            val params = call.arguments as? String
            thetaRepository?.startPlugin(packageName = params)
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun stopPlugin(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            thetaRepository?.stopPlugin()
            result.success(null)
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun getPluginLicense(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            val params = call.arguments as? String
            params?.let { params ->
                val response = thetaRepository?.getPluginLicense(packageName = params)
                response?.let {
                    result.success(it)
                } ?: run {
                    result.error(errorCode, messageNoResult, null)
                }
            } ?: run {
                result.error(errorCode, messageNoArgument, null)
                return
            }
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun getPluginOrders(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            val response = thetaRepository?.getPluginOrders()
            response?.let {
                result.success(response)
            } ?: run {
                result.error(errorCode, messageNoResult, null)
            }
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun setPluginOrders(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }

        try {
            val params = call.arguments as? List<String>
            params?.let { params ->
                thetaRepository?.setPluginOrders(plugins = params)
                result.success(null)
            } ?: run {
                result.error(errorCode, messageNoArgument, null)
                return
            }
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }

    suspend fun setBluetoothDevice(call: MethodCall, result: Result) {
        if (thetaRepository == null) {
            result.error(errorCode, messageNotInit, null)
            return
        }
        try {
            val params = call.arguments as? String
            params?.let { params ->
                val response = thetaRepository?.setBluetoothDevice(uuid = params)
                response?.let {
                    result.success(it)
                } ?: run {
                    result.error(errorCode, messageNoResult, null)
                }
            } ?: run {
                result.error(errorCode, messageNoArgument, null)
                return
            }
        } catch (e: Exception) {
            result.error(e.javaClass.simpleName, e.message, null)
        }
    }
}