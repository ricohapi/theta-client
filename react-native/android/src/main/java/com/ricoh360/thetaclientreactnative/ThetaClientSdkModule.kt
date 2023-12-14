package com.ricoh360.thetaclientreactnative

import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.ThetaRepository.*
import com.ricoh360.thetaclient.capture.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.*
import kotlin.text.String

class ThetaClientReactNativeModule(
  reactContext: ReactApplicationContext
) : ReactContextBaseJavaModule(reactContext), CoroutineScope {

  override val coroutineContext: CoroutineContext = Job()

  override fun getName(): String {
    return NAME
  }

  var previewing: Boolean = false
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
  var theta: ThetaRepository? = null
  var listenerCount: Int = 0

  val messageNotInit: String = "Not initialized."

  /**
   * add event listener for [eventName]
   */
  @ReactMethod
  fun addListener(eventName: String) {
    // Set up any upstream listeners or background tasks as necessary
    if (eventName.equals(EVENT_NAME)) {
      listenerCount++;
    }
  }

  /**
   * remove event listener [count]
   */
  @ReactMethod
  fun removeListeners(count: Int) {
    // Remove upstream listeners, stop unnecessary background tasks
    listenerCount -= count
  }

  fun sendNotifyEvent(param: WritableMap) {
    reactApplicationContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit(EVENT_NOTIFY, param)
  }

  /**
   * retreive constant
   */
  override fun getConstants(): MutableMap<String, Any> =
    hashMapOf("DEFAULT_EVENT_NAME" to EVENT_NAME)

  /**
   * initialize Theta repository with endpoint
   * @param endpoint endpoint to connect THETA
   * @param promise promise to set initialize result
   */
  @ReactMethod
  fun initialize(endpoint: String, config: ReadableMap?, timeout: ReadableMap?, promise: Promise) {
    launch {
      try {
        theta = null
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

        theta = ThetaRepository.newInstance(
          endpoint,
          config?.let { configToTheta(it) },
          timeout?.let { timeoutToTheta(it) }
        )
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * Returns whether it is initialized or not.
   * @param promise promise to set result
   */
  @ReactMethod
  fun isInitialized(promise: Promise) {
    promise.resolve(theta != null)
  }

  /**
   * getThetaModel  -  Returns the connected THETA model
   * @param promise promise to set result
   */
  @ReactMethod
  fun getThetaModel(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    promise.resolve(theta.cameraModel?.name)
  }

  /**
   * getThetaInfo  -  retrieve ThetaInfo from THETA via repository
   * @param promise promise to set result
   */
  @ReactMethod
  fun getThetaInfo(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        val info = theta.getThetaInfo()
        val result = Arguments.createMap()
        result.putString("manufacturer", info.manufacturer)
        result.putString("model", info.model)
        result.putString("serialNumber", info.serialNumber)
        info.wlanMacAddress?.also {
          result.putString("wlanMacAddress", info.wlanMacAddress)
        } ?: result.putNull("wlanMacAddress")
        info.bluetoothMacAddress?.also {
          result.putString("bluetoothMacAddress", info.bluetoothMacAddress)
        } ?: result.putNull("bluetoothMacAddress")
        result.putString("firmwareVersion", info.firmwareVersion)
        result.putString("supportUrl", info.supportUrl)
        result.putBoolean("hasGps", info.hasGps)
        result.putBoolean("hasGyro", info.hasGyro)
        result.putInt("uptime", info.uptime)
        result.putArray("api", Arguments.fromList(info.api))
        val ep = Arguments.createMap()
        ep.putInt("httpPort", info.endpoints.httpPort)
        ep.putInt("httpUpdatesPort", info.endpoints.httpUpdatesPort)
        result.putMap("endpoints", ep)
        result.putArray("apiLevel", Arguments.fromList(info.apiLevel))
        theta.cameraModel?.let {
          result.putString("thetaModel", it.name)
        }
        promise.resolve(result)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * getThetaState  -  retrieve ThetaState from THETA via repository
   * @param promise promise to set result
   */
  @ReactMethod
  fun getThetaState(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        val state = theta.getThetaState()
        val result = Arguments.createMap()
        result.putString("fingerprint", state.fingerprint)
        result.putDouble("batteryLevel", state.batteryLevel.toDouble())
        result.putString("storageUri", state.storageUri)
        result.putString("storageID", state.storageID)
        result.putString("captureStatus", state.captureStatus.toString())
        result.putInt("recordedTime", state.recordedTime)
        result.putInt("recordableTime", state.recordableTime)
        state.capturedPictures?.also {
          result.putInt("capturedPictures", state.capturedPictures!!)
        } ?: result.putNull("capturedPictures")
        state.compositeShootingElapsedTime?.also {
          result.putString("compositeShootingElapsedTime", state.compositeShootingElapsedTime.toString())
        } ?: result.putNull("compositeShootingElapsedTime")
        result.putString("latestFileUrl", state.latestFileUrl)
        result.putString("chargingState", state.chargingState.toString())
        result.putInt("apiVersion", state.apiVersion)
        state.isPluginRunning?.also {
          result.putBoolean("isPluginRunning", state.isPluginRunning!!)
        } ?: result.putNull("isPluginRunning")
        state.isPluginWebServer?.also {
          result.putBoolean("isPluginWebServer", state.isPluginWebServer!!)
        } ?: result.putNull("isPluginWebServer")
        state.function?.also {
          result.putString("function", state.function.toString())
        } ?: result.putNull("function")
        state.isMySettingChanged?.also {
          result.putBoolean("isMySettingChanged", state.isMySettingChanged!!)
        } ?: result.putNull("isMySettingChanged")
        result.putString("currentMicrophone", state.currentMicrophone?.toString())
        result.putBoolean("isSdCard", state.isSdCard)
        state.cameraError?.also {
          result.putArray("cameraError", Arguments.makeNativeArray(state.cameraError!!.map { it.toString() }))
        } ?: result.putNull("cameraError")
        state.isBatteryInsert?.also {
          result.putString("isBatteryInsert", state.isBatteryInsert.toString())
        } ?: result.putNull("isBatteryInsert")
        promise.resolve(result)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * listFiles  -  retrieve File list from THETA via repository
   * @param fileType file type to retrieve
   * @param startPosition start position to retrieve
   * @param entryCount count to retrieve
   * @param storage Desired storage
   * @param promise promise to set result
   */
  @ReactMethod
  fun listFiles(fileType: String, startPosition: Int, entryCount: Int, storage: String?, promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        val (fileList, totalEntries) = theta.listFiles(
          ThetaRepository.FileTypeEnum.valueOf(fileType),
          startPosition,
          entryCount,
          storage?.let { ThetaRepository.StorageEnum.valueOf(it) },
        )
        val resultList = Arguments.createArray()
        fileList.forEach {
          resultList.pushMap(toResult(fileInfo = it))
        }
        val resultMap = Arguments.createMap()
        resultMap.putArray("fileList", resultList)
        resultMap.putInt("totalEntries", totalEntries)
        promise.resolve(resultMap)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * deleteFiles  -  delete specified files
   * @param fileUrls file url list to delete
   * @param promise promise to set result
   */
  @ReactMethod
  fun deleteFiles(fileUrls: ReadableArray, promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    val fileList = mutableListOf<String>()
    for (index in 0..(fileUrls.size() - 1)) {
      fileList.add(fileUrls.getString(index))
    }
    launch {
      try {
        theta.deleteFiles(fileList)
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * deleteAllFiles  -  delete all files
   * @param promise promise to set result
   */
  @ReactMethod
  fun deleteAllFiles(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        theta.deleteAllFiles()
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * deleteAllImageFiles  -  delete all image files
   * @param promise promise to set result
   */
  @ReactMethod
  fun deleteAllImageFiles(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        theta.deleteAllImageFiles()
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * deleteAllVideoFiles  -  delete all video files
   * @param promise promise to set result
   */
  @ReactMethod
  fun deleteAllVideoFiles(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        theta.deleteAllVideoFiles()
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * getOptions  -  get options from THETA via repository
   * @param optionNames option name list to get
   * @param promise promise to set result
   */
  @ReactMethod
  fun getOptions(optionNames: ReadableArray, promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        val response = theta.getOptions(toGetOptionsParam(optionNames = optionNames))
        val result = toResult(options = response)
        promise.resolve(result)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * setOptions  -  set options to THETA via repository
   * @param options options to set
   * @param resolve resolver for setOptions
   * @param rejecter rejecter for setOptions
   */
  @ReactMethod
  fun setOptions(options: ReadableMap, promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        val params = toSetOptionsParam(optionsMap = options)
        theta.setOptions(params)
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * getLivePreview  -  retrieve live preview frame from THETA via repository
   * @param promise promise to set result
   */
  @ReactMethod
  fun getLivePreview(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    fun ByteArray.toBase64(): String = String(Base64.getEncoder().encode(this))
    suspend fun callFrameHandler(packet: Pair<ByteArray, Int>): Boolean {
      if (listenerCount == 0) {
        return previewing
      }
      val param = Arguments.createMap()
      param.putString(
        "data",
        "data:image/jpeg;base64," +
          packet.first.copyOfRange(0, packet.second).toBase64()
      )
      reactApplicationContext
        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
        .emit(EVENT_NAME, param)
      return previewing
    }
    previewing = true
    launch {
      try {
        theta.getLivePreview(::callFrameHandler)
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * stopLivePreview  -  stop live previewing
   */
  @ReactMethod
  fun stopLivePreview() {
    if (theta == null) {
      throw Exception(messageNotInit)
    }
    previewing = false
  }

  /**
   * getPhotoCaptureBuilder  -  get photo capture builder from repository
   * @param promise promise to set result
   */
  @ReactMethod
  fun getPhotoCaptureBuilder(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    photoCaptureBuilder = theta.getPhotoCaptureBuilder()
    promise.resolve(true)
  }

  /**
   * buildPhotoCapture  -  build photo capture
   * @param options option to take picture
   * @param resolve resolver for buildPhotoCapture
   * @param rejecter rejecter for buildPhotoCapture
   */
  @ReactMethod
  fun buildPhotoCapture(options: ReadableMap, promise: Promise) {
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    if (photoCaptureBuilder == null) {
      promise.reject(Exception("no photoCaptureBuilder"))
      return
    }
    launch {
      try {
        photoCaptureBuilder?.let {
          setCaptureBuilderParams(optionMap = options, builder = it)
          setPhotoCaptureBuilderParams(optionMap = options, builder = it)
          photoCapture = it.build()
        }
        promise.resolve(true)
        photoCaptureBuilder = null
      } catch (t: Throwable) {
        promise.reject(t)
        photoCaptureBuilder = null
      }
    }
  }

  /**
   * takePicture  -  take a picture with THETA via repository
   * @param promise promise to set result
   */
  @ReactMethod
  fun takePicture(promise: Promise) {
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    if (photoCapture == null) {
      promise.reject(Exception("no photoCapture"))
      return
    }
    class TakePictureCallback : PhotoCapture.TakePictureCallback {
      override fun onSuccess(fileUrl: String?) {
        promise.resolve(fileUrl)
        photoCapture = null
      }

      override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
        promise.reject(exception)
        photoCapture = null
      }
    }
    launch {
      photoCapture!!.takePicture(TakePictureCallback())
    }
  }

  /**
   * getTimeShiftCaptureBuilder  -  get time-shift builder from repository
   */
  @ReactMethod
  fun getTimeShiftCaptureBuilder(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    timeShiftCaptureBuilder = theta.getTimeShiftCaptureBuilder()
    promise.resolve(true)
  }

  /**
   * buildTimeShiftCapture  -  build time-shift
   * @param options option to execute time-shift
   * @param promise Promise for buildTimeShiftCapture
   */
  @ReactMethod
  fun buildTimeShiftCapture(options: ReadableMap, promise: Promise) {
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    if (timeShiftCaptureBuilder == null) {
      promise.reject(Exception("no timeShiftCaptureBuilder"))
      return
    }
    launch {
      try {
        timeShiftCaptureBuilder?.let {
          setCaptureBuilderParams(optionMap = options, builder = it)
          setTimeShiftCaptureBuilderParams(optionMap = options, builder = it)
          timeShiftCapture = it.build()
        }
        promise.resolve(true)
        timeShiftCaptureBuilder = null
      } catch (t: Throwable) {
        promise.reject(t)
        timeShiftCaptureBuilder = null
      }
    }
  }

  /**
   * startTimeShiftCapture  -  start time-shift
   * @param promise promise for startTimeShiftCapture
   */
  @ReactMethod
  fun startTimeShiftCapture(promise: Promise) {
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    if (timeShiftCapture == null) {
      promise.reject(Exception("no timeShiftCapture"))
      return
    }
    class StartCaptureCallback : TimeShiftCapture.StartCaptureCallback {
      override fun onCaptureCompleted(fileUrl: String?) {
        promise.resolve(fileUrl)
        timeShiftCapture = null
      }

      override fun onProgress(completion: Float) {
        sendNotifyEvent(
          toNotify(NOTIFY_TIMESHIFT_PROGRESS, toCaptureProgressNotifyParam(value = completion))
        )
      }

      override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
        promise.reject(exception)
        timeShiftCapture = null
      }

      override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
        sendNotifyEvent(
          toNotify(
            NOTIFY_TIMESHIFT_STOP_ERROR,
            toMessageNotifyParam(exception.message ?: exception.toString())
          )
        )
      }
    }
    timeShiftCapturing = timeShiftCapture?.startCapture(StartCaptureCallback())
  }

  /**
   * cancelTimeShiftCapture  -  stop time-shift
   * @param promise promise for stopTimeShiftCapture
   */
  @ReactMethod
  fun cancelTimeShiftCapture(promise: Promise) {
    if (theta == null) {
      throw Exception(messageNotInit)
    }
    timeShiftCapturing?.cancelCapture()
  }

  /**
   * getVideoCaptureBuilder  -  get video capture builder
   * @param promise promise to set result
   */
  @ReactMethod
  fun getVideoCaptureBuilder(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    videoCaptureBuilder = theta.getVideoCaptureBuilder()
    promise.resolve(true)
  }

  /**
   * buildVideoCapture  -  build video capture
   * @param options option to capture video
   * @param promise promise to set result
   */
  @ReactMethod
  fun buildVideoCapture(options: ReadableMap, promise: Promise) {
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    if (videoCaptureBuilder == null) {
      promise.reject(Exception("no videoCaptureBuilder"))
      return
    }
    launch {
      try {
        videoCaptureBuilder?.let {
          setCaptureBuilderParams(optionMap = options, builder = it)
          setVideoCaptureBuilderParams(optionMap = options, builder = it)
          videoCapture = it.build()
        }
        promise.resolve(true)
        videoCaptureBuilder = null
      } catch (t: Throwable) {
        promise.reject(t)
        videoCaptureBuilder = null
      }
    }
  }

  /**
   * startCapture  -  start capture video
   * @param promise promise to set result
   */
  @ReactMethod
  fun startVideoCapture(promise: Promise) {
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    if (videoCapture == null) {
      promise.reject(Exception("no videoCapture"))
      return
    }
    class StartCaptureCallback : VideoCapture.StartCaptureCallback {
      override fun onCaptureCompleted(fileUrl: String?) {
        promise.resolve(fileUrl)
        videoCapture = null
        videoCapturing = null
      }

      override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
        promise.reject(exception)
        videoCapture = null
        videoCapturing = null
      }

      override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
        sendNotifyEvent(
          toNotify(
            NOTIFY_VIDEO_CAPTURE_STOP_ERROR,
            toMessageNotifyParam(exception.message ?: exception.toString())
          )
        )
      }
    }
    videoCapturing = videoCapture?.startCapture(StartCaptureCallback())
  }

  /**
   * stopCapture  -  stop capturing video
   */
  @ReactMethod
  fun stopVideoCapture() {
    if (theta == null) {
      throw Exception(messageNotInit)
    }
    videoCapturing?.stopCapture()
  }

  /**
   * getLimitlessIntervalCaptureBuilder  -  get limitless interval capture builder
   * @param promise promise to set result
   */
  @ReactMethod
  fun getLimitlessIntervalCaptureBuilder(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    limitlessIntervalCaptureBuilder = theta.getLimitlessIntervalCaptureBuilder()
    promise.resolve(true)
  }

  /**
   * buildLimitlessIntervalCapture  -  build limitless interval capture
   * @param options option to capture limitless interval
   * @param promise promise to set result
   */
  @ReactMethod
  fun buildLimitlessIntervalCapture(options: ReadableMap, promise: Promise) {
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    if (limitlessIntervalCaptureBuilder == null) {
      promise.reject(Exception("no limitlessIntervalCaptureBuilder"))
      return
    }
    launch {
      try {
        limitlessIntervalCaptureBuilder?.let {
          setCaptureBuilderParams(optionMap = options, builder = it)
          setLimitlessIntervalCaptureBuilderParams(optionMap = options, builder = it)
          limitlessIntervalCapture = it.build()
        }
        promise.resolve(true)
        limitlessIntervalCaptureBuilder = null
      } catch (t: Throwable) {
        promise.reject(t)
        limitlessIntervalCaptureBuilder = null
      }
    }
  }

  /**
   * startCapture  -  start capture limitless interval
   * @param promise promise to set result
   */
  @ReactMethod
  fun startLimitlessIntervalCapture(promise: Promise) {
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    if (limitlessIntervalCapture == null) {
      promise.reject(Exception("no limitlessIntervalCapture"))
      return
    }
    class StartCaptureCallback : LimitlessIntervalCapture.StartCaptureCallback {
      override fun onCaptureCompleted(fileUrls: List<String>?) {
        promise.resolve(fileUrls?.let {
          val resultList = Arguments.createArray()
          it.forEach {
            resultList.pushString(it)
          }
          resultList
        })
        limitlessIntervalCapture = null
        limitlessIntervalCapturing = null
      }

      override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
        promise.reject(exception)
        limitlessIntervalCapture = null
        limitlessIntervalCapturing = null
      }

      override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
        sendNotifyEvent(
          toNotify(
            NOTIFY_LIMITLESS_INTERVAL_CAPTURE_STOP_ERROR,
            toMessageNotifyParam(exception.message ?: exception.toString())
          )
        )
      }
    }
    limitlessIntervalCapturing = limitlessIntervalCapture?.startCapture(StartCaptureCallback())
  }

  /**
   * stopCapture  -  stop capturing limitless interval
   */
  @ReactMethod
  fun stopLimitlessIntervalCapture() {
    if (theta == null) {
      throw Exception(messageNotInit)
    }
    limitlessIntervalCapturing?.stopCapture()
  }

  /**
   * getShotCountSpecifiedIntervalCaptureBuilder  -  get interval shooting with the shot count specified builder from repository
   * @param shotCount shotCount shot count specified
   * @param promise Promise for getShotCountSpecifiedIntervalCaptureBuilder
   */
  @ReactMethod
  fun getShotCountSpecifiedIntervalCaptureBuilder(shotCount: Int, promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    shotCountSpecifiedIntervalCaptureBuilder = theta.getShotCountSpecifiedIntervalCaptureBuilder(shotCount)
    promise.resolve(true)
  }

  /**
   * buildShotCountSpecifiedIntervalCapture  -  build interval shooting with the shot count specified
   * @param options option to execute interval shooting with the shot count specified
   * @param promise Promise for buildShotCountSpecifiedIntervalCapture
   */
  @ReactMethod
  fun buildShotCountSpecifiedIntervalCapture(options: ReadableMap, promise: Promise) {
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    if (shotCountSpecifiedIntervalCaptureBuilder == null) {
      promise.reject(Exception("no shotCountSpecifiedIntervalCaptureBuilder"))
      return
    }
    launch {
      try {
        shotCountSpecifiedIntervalCaptureBuilder?.let {
          setCaptureBuilderParams(optionMap = options, builder = it)
          setShotCountSpecifiedIntervalCaptureBuilderParams(optionMap = options, builder = it)
          shotCountSpecifiedIntervalCapture = it.build()
        }
        promise.resolve(true)
        shotCountSpecifiedIntervalCaptureBuilder = null
      } catch (t: Throwable) {
        promise.reject(t)
        shotCountSpecifiedIntervalCaptureBuilder = null
      }
    }
  }

  /**
   * startShotCountSpecifiedIntervalCapture  -  start interval shooting with the shot count specified
   * @param promise promise for startShotCountSpecifiedIntervalCapture
   */
  @ReactMethod
  fun startShotCountSpecifiedIntervalCapture(promise: Promise) {
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    if (shotCountSpecifiedIntervalCapture == null) {
      promise.reject(Exception("no shotCountSpecifiedIntervalCapture"))
      return
    }
    class StartCaptureCallback : ShotCountSpecifiedIntervalCapture.StartCaptureCallback {
      override fun onCaptureCompleted(fileUrls: List<String>?) {
        promise.resolve(fileUrls?.let {
          val resultList = Arguments.createArray()
          it.forEach {
            resultList.pushString(it)
          }
          resultList
        })
        shotCountSpecifiedIntervalCapture = null
      }

      override fun onProgress(completion: Float) {
        sendNotifyEvent(
          toNotify(
            NOTIFY_SHOT_COUNT_SPECIFIED_INTERVAL_PROGRESS,
            toCaptureProgressNotifyParam(value = completion)
          )
        )
      }

      override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
        sendNotifyEvent(
          toNotify(
            NOTIFY_SHOT_COUNT_SPECIFIED_INTERVAL_STOP_ERROR,
            toMessageNotifyParam(exception.message ?: exception.toString())
          )
        )
      }

      override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
        promise.reject(exception)
        shotCountSpecifiedIntervalCapture = null
      }
    }
    shotCountSpecifiedIntervalCapturing =
      shotCountSpecifiedIntervalCapture?.startCapture(StartCaptureCallback())
  }

  /**
   * cancelShotCountSpecifiedIntervalCapture  -  stop interval shooting with the shot count specified
   * @param promise promise for cancelShotCountSpecifiedIntervalCapture
   */
  @ReactMethod
  fun cancelShotCountSpecifiedIntervalCapture(promise: Promise) {
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    shotCountSpecifiedIntervalCapturing?.cancelCapture()
    promise.resolve(true)
  }

  /**
   * getCompositeIntervalCaptureBuilder  -  get interval composite shooting builder from repository
   * @param shootingTimeSec Shooting time for interval composite shooting (sec)
   * @param promise Promise for getCompositeIntervalCaptureBuilder
   */
  @ReactMethod
  fun getCompositeIntervalCaptureBuilder(shootingTimeSec: Int, promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    compositeIntervalCaptureBuilder = theta.getCompositeIntervalCaptureBuilder(shootingTimeSec)
    promise.resolve(true)
  }

  /**
   * buildCompositeIntervalCapture  -  build interval composite shooting
   * @param options option to execute interval composite shooting
   * @param promise Promise for buildCompositeIntervalCapture
   */
  @ReactMethod
  fun buildCompositeIntervalCapture(options: ReadableMap, promise: Promise) {
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    if (compositeIntervalCaptureBuilder == null) {
      promise.reject(Exception("no compositeIntervalCaptureBuilder"))
      return
    }
    launch {
      try {
        compositeIntervalCaptureBuilder?.let {
          setCaptureBuilderParams(optionMap = options, builder = it)
          setCompositeIntervalCaptureBuilderParams(optionMap = options, builder = it)
          compositeIntervalCapture = it.build()
        }
        promise.resolve(true)
        compositeIntervalCaptureBuilder = null
      } catch (t: Throwable) {
        promise.reject(t)
        compositeIntervalCaptureBuilder = null
      }
    }
  }

  /**
   * startCompositeIntervalCapture  -  start interval composite shooting
   * @param promise promise for startCompositeIntervalCapture
   */
  @ReactMethod
  fun startCompositeIntervalCapture(promise: Promise) {
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    if (compositeIntervalCapture == null) {
      promise.reject(Exception("no compositeIntervalCapture"))
      return
    }
    class StartCaptureCallback : CompositeIntervalCapture.StartCaptureCallback {
      override fun onCaptureCompleted(fileUrls: List<String>?) {
        promise.resolve(fileUrls?.let {
          val resultList = Arguments.createArray()
          it.forEach {
            resultList.pushString(it)
          }
          resultList
        })
        compositeIntervalCapture = null
      }

      override fun onProgress(completion: Float) {
        sendNotifyEvent(
          toNotify(
            NOTIFY_COMPOSITE_INTERVAL_PROGRESS,
            toCaptureProgressNotifyParam(value = completion)
          )
        )
      }

      override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
        sendNotifyEvent(
          toNotify(
            NOTIFY_COMPOSITE_INTERVAL_STOP_ERROR,
            toMessageNotifyParam(exception.message ?: exception.toString())
          )
        )
      }

      override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
        promise.reject(exception)
        compositeIntervalCapture = null
      }
    }
    compositeIntervalCapturing = compositeIntervalCapture?.startCapture(StartCaptureCallback())
  }

  /**
   * cancelCompositeIntervalCapture  -  stop interval composite shooting
   * @param promise promise for cancelCompositeIntervalCapture
   */
  @ReactMethod
  fun cancelCompositeIntervalCapture(promise: Promise) {
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    compositeIntervalCapturing?.cancelCapture()
    promise.resolve(true)
  }

  /**
   * getBurstCaptureBuilder  -  get burst shooting builder from repository
   *
   * @param burstCaptureNum Number of shots for burst shooting
   * @param burstBracketStep Bracket value range between each shot for burst shooting
   * @param burstCompensation Exposure compensation for the base image and entire shooting for burst shooting
   * @param burstMaxExposureTime Maximum exposure time for burst shooting
   * @param burstEnableIsoControl Adjustment with ISO sensitivity for burst shooting
   * @param burstOrder Shooting order for burst shooting
   * @param promise Promise for getBurstCaptureBuilder
   */
  @ReactMethod
  fun getBurstCaptureBuilder(
    burstCaptureNum: String,
    burstBracketStep: String,
    burstCompensation: String,
    burstMaxExposureTime: String,
    burstEnableIsoControl: String,
    burstOrder: String,
    promise: Promise
  ) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }

    burstCaptureBuilder = theta.getBurstCaptureBuilder(
      burstCaptureNum = ThetaRepository.BurstCaptureNumEnum.valueOf(burstCaptureNum),
      burstBracketStep = ThetaRepository.BurstBracketStepEnum.valueOf(burstBracketStep),
      burstCompensation = ThetaRepository.BurstCompensationEnum.valueOf(burstCompensation),
      burstMaxExposureTime = ThetaRepository.BurstMaxExposureTimeEnum.valueOf(burstMaxExposureTime),
      burstEnableIsoControl = ThetaRepository.BurstEnableIsoControlEnum.valueOf(burstEnableIsoControl),
      burstOrder = ThetaRepository.BurstOrderEnum.valueOf(burstOrder)
    )
    promise.resolve(true)
  }

  /**
   * buildBurstCapture  -  build burst shooting
   * @param options option to execute burst shooting
   * @param promise Promise for buildBurstCapture
   */
  @ReactMethod
  fun buildBurstCapture(options: ReadableMap, promise: Promise) {
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    if (burstCaptureBuilder == null) {
      promise.reject(Exception("no burstCaptureBuilder"))
      return
    }
    launch {
      try {
        burstCaptureBuilder?.let {
          setCaptureBuilderParams(optionMap = options, builder = it)
          setBurstCaptureBuilderParams(optionMap = options, builder = it)
          burstCapture = it.build()
        }
        promise.resolve(true)
        burstCaptureBuilder = null
      } catch (t: Throwable) {
        promise.reject(t)
        burstCaptureBuilder = null
      }
    }
  }

  /**
   * startBurstCapture  -  start burst shooting
   * @param promise promise for startBurstCapture
   */
  @ReactMethod
  fun startBurstCapture(promise: Promise) {
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    if (burstCapture == null) {
      promise.reject(Exception("no burstCapture"))
      return
    }
    class StartCaptureCallback : BurstCapture.StartCaptureCallback {
      override fun onCaptureCompleted(fileUrls: List<String>?) {
        promise.resolve(fileUrls?.let {
          val resultList = Arguments.createArray()
          it.forEach {
            resultList.pushString(it)
          }
          resultList
        })
        burstCapture = null
      }

      override fun onProgress(completion: Float) {
        sendNotifyEvent(
          toNotify(
            NOTIFY_BURST_PROGRESS,
            toCaptureProgressNotifyParam(value = completion)
          )
        )
      }

      override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
        sendNotifyEvent(
          toNotify(
            NOTIFY_BURST_STOP_ERROR,
            toMessageNotifyParam(exception.message ?: exception.toString())
          )
        )
      }

      override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
        promise.reject(exception)
        burstCapture = null
      }
    }
    burstCapturing = burstCapture?.startCapture(StartCaptureCallback())
  }

  /**
   * cancelBurstCapture  -  stop burst shooting
   * @param promise promise for cancelBurstCapture
   */
  @ReactMethod
  fun cancelBurstCapture(promise: Promise) {
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    burstCapturing?.cancelCapture()
    promise.resolve(true)
  }

  /**
   * getMultiBracketCaptureBuilder  -  get multi bracket shooting builder from repository
   * @param promise Promise for getMultiBracketCaptureBuilder
   */
  @ReactMethod
  fun getMultiBracketCaptureBuilder(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    multiBracketCaptureBuilder = theta.getMultiBracketCaptureBuilder()
    promise.resolve(true)
  }

  /**
   * buildMultiBracketCapture  -  build multi bracket shooting
   * @param options option to execute multi bracket shooting
   * @param promise Promise for buildMultiBracketCapture
   */
  @ReactMethod
  fun buildMultiBracketCapture(options: ReadableMap, promise: Promise) {
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    if (multiBracketCaptureBuilder == null) {
      promise.reject(Exception("no MultiBracketCaptureBuilder"))
      return
    }
    launch {
      try {
        multiBracketCaptureBuilder?.let {
          setCaptureBuilderParams(optionMap = options, builder = it)
          setMultiBracketCaptureBuilderParams(optionMap = options, builder = it)
          multiBracketCapture = it.build()
        }
        promise.resolve(true)
        multiBracketCaptureBuilder = null
      } catch (t: Throwable) {
        promise.reject(t)
        multiBracketCaptureBuilder = null
      }
    }
  }

  /**
   * startMultiBracketCapture  -  start multi bracket shooting
   * @param promise promise for startMultiBracketCapture
   */
  @ReactMethod
  fun startMultiBracketCapture(promise: Promise) {
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    if (multiBracketCapture == null) {
      promise.reject(Exception("no multiBracketCapture"))
      return
    }
    class StartCaptureCallback : MultiBracketCapture.StartCaptureCallback {
      override fun onCaptureCompleted(fileUrls: List<String>?) {
        promise.resolve(fileUrls?.let {
          val resultList = Arguments.createArray()
          it.forEach {
            resultList.pushString(it)
          }
          resultList
        })
        multiBracketCapture = null
      }

      override fun onProgress(completion: Float) {
        sendNotifyEvent(
          toNotify(
            NOTIFY_MULTI_BRACKET_PROGRESS,
            toCaptureProgressNotifyParam(value = completion)
          )
        )
      }

      override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
        sendNotifyEvent(
          toNotify(
            NOTIFY_MULTI_BRACKET_STOP_ERROR,
            toMessageNotifyParam(exception.message ?: exception.toString())
          )
        )
      }

      override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
        promise.reject(exception)
        multiBracketCapture = null
      }
    }
    multiBracketCapturing = multiBracketCapture?.startCapture(StartCaptureCallback())
  }

  /**
   * cancelMultiBracketCapture  -  stop multi bracket shooting
   * @param promise promise for cancelMultiBracketCapture
   */
  @ReactMethod
  fun cancelMultiBracketCapture(promise: Promise) {
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    multiBracketCapturing?.cancelCapture()
    promise.resolve(true)
  }

  /**
   * getMetadata  -  retrieve meta data from THETA via repository
   * @param promise promise to set result
   */
  @ReactMethod
  fun getMetadata(fileUrl: String, promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        val metaData = theta.getMetadata(fileUrl)
        val exif = Arguments.createMap()
        val xmp = Arguments.createMap()
        metaData.first.let {
          exif.putString("exifVersion", it.exifVersion)
          exif.putString("dateTime", it.dateTime)
          it.imageWidth?.let { it1 -> exif.putInt("imageWidth", it1) }
          it.imageLength?.let { it1 -> exif.putInt("imageLength", it1) }
          it.gpsLatitude?.let { it1 -> exif.putDouble("gpsLatitude", it1) }
          it.gpsLongitude?.let { it1 -> exif.putDouble("gpsLongitude", it1) }
        }
        metaData.second.let {
          it.poseHeadingDegrees?.let { it1 -> xmp.putDouble("poseHeadingDegrees", it1) }
          xmp.putInt("fullPanoWidthPixels", it.fullPanoWidthPixels)
          xmp.putInt("fullPanoHeightPixels", it.fullPanoHeightPixels)
        }
        val metaInfo = Arguments.createMap()
        metaInfo.putMap("exif", exif)
        metaInfo.putMap("xmp", xmp)
        promise.resolve(metaInfo)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * reset  -  reset THETA via repository
   * @param promise promise to set result
   */
  @ReactMethod
  fun reset(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        theta.reset()
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * restoreSettings  -  restore THETA setting before initialization
   * @param promise promise to set result
   */
  @ReactMethod
  fun restoreSettings(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        theta.restoreSettings()
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * stopSelfTimer  -  stop self timer of THETA
   * @param promise promise to set result
   */
  @ReactMethod
  fun stopSelfTimer(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        theta.stopSelfTimer()
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * convertVideoFormats  -  convert video format in THETA
   * @param fileUrl file url to convert
   * @param is4k 4k or not
   * @param applyTopBottomCorrection apply top bottom correction
   * @param promise promise to set result
   */
  @ReactMethod
  fun convertVideoFormats(
    fileUrl: String,
    is4k: Boolean,
    applyTopBottomCorrection: Boolean,
    promise: Promise,
  ) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        val convertedUrl = theta.convertVideoFormats(fileUrl, is4k, applyTopBottomCorrection)
        promise.resolve(convertedUrl)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }

  }

  /**
   * cancelVideoconvert  -  cancel converting video format in THETA
   * @param promise promise to set result
   */
  @ReactMethod
  fun cancelVideoConvert(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        theta.cancelVideoConvert()
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * finishWlan  -  finish wireless lan
   * @param promise promise to set result
   */
  @ReactMethod
  fun finishWlan(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        theta.finishWlan()
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * listAccessPoints  -  list access points
   * @param promise promise to set result
   */
  @ReactMethod
  fun listAccessPoints(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        val response = theta.listAccessPoints()
        promise.resolve(toListAccessPointsResult(accessPointList = response))
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * setAccessPointDynamically  -  set access point with dhcp
   * @param ssid ssid to connect
   * @param ssidStealth ssid is stealth or not
   * @param authMode auth mode to connect
   * @param password password to connect with auth
   * @param connectionPriority connection priority
   * @param proxy Proxy information to be used for the access point.
   * @param promise promise to set result
   */
  @ReactMethod
  fun setAccessPointDynamically(
    ssid: String,
    ssidStealth: Boolean,
    authMode: String,
    password: String,
    connectionPriority: Int,
    proxy: ReadableMap?,
    promise: Promise
  ) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        theta.setAccessPointDynamically(
          ssid,
          ssidStealth,
          ThetaRepository.AuthModeEnum.valueOf(authMode),
          password,
          connectionPriority,
          toProxy(map = proxy)
        )
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * setAccessPointStatically  -  set access point with static connection info
   * @param ssid ssid to connect
   * @param ssidStealth ssid is stealth or not
   * @param authMode auth mode to connect
   * @param password password to connect with auth
   * @param connectionPriority connection priority
   * @param ipAddress static ipaddress to connect
   * @param subnetMask subnet mask for ip address
   * @param defaultGateway default gateway address
   * @param promise promise to set result
   * @param proxy Proxy information to be used for the access point.
   */
  @ReactMethod
  fun setAccessPointStatically(
    ssid: String,
    ssidStealth: Boolean,
    authMode: String,
    password: String,
    connectionPriority: Int,
    ipAddress: String,
    subnetMask: String,
    defaultGateway: String,
    proxy: ReadableMap?,
    promise: Promise
  ) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        theta.setAccessPointStatically(
          ssid,
          ssidStealth,
          ThetaRepository.AuthModeEnum.valueOf(authMode),
          password,
          connectionPriority,
          ipAddress,
          subnetMask,
          defaultGateway,
          toProxy(map = proxy)
        )
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * deleteAccessPoint  -  delete access point related ssid
   * @param ssid ssid to delete
   * @param promise promise to set result
   */
  @ReactMethod
  fun deleteAccessPoint(ssid: String, promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        theta.deleteAccessPoint(ssid)
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * setBluetoothDevice  -  register uuid of a BLE device
   * @param uuid uuid to set
   * @param promise promise to set result
   */
  @ReactMethod
  fun setBluetoothDevice(uuid: String, promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        val deviceName = theta.setBluetoothDevice(uuid)
        promise.resolve(deviceName)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * getMySetting - Acquires the shooting properties set by the camera._setMySetting command.
   * Just for Theta V and later
   * @param captureMode The target shooting mode
   * @param promise promise to set result
   */
  @ReactMethod
  fun getMySetting(captureMode: String, promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        val options = theta.getMySetting(ThetaRepository.CaptureModeEnum.valueOf(captureMode))
        promise.resolve(toResult(options = options))
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * getMySettingFromOldModel - Acquires the shooting properties set by the camera._setMySetting command.
   * Just for Theta S and SC.
   * @param optionNames The target shooting mode
   * @param promise promise to set result
   */
  @ReactMethod
  fun getMySettingFromOldModel(optionNames: ReadableArray, promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        val optionNameList = mutableListOf<ThetaRepository.OptionNameEnum>()
        for (index in 0..(optionNames.size() - 1)) {
          val option = optionNames.getString(index)
          optionNameList.add(ThetaRepository.OptionNameEnum.valueOf(option))
        }
        val options = theta.getMySetting(optionNameList)
        promise.resolve(toResult(options = options))
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * setMySetting - Registers shooting conditions in My Settings
   * @param captureMode The target shooting mode.  RICOH THETA S and SC do not support My Settings in video capture mode.
   * @param options registered to My Settings
   * @param Promise of bolean result, always true
   */
  @ReactMethod
  fun setMySetting(captureMode: String, options: ReadableMap, promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        val thetaOptions = toSetOptionsParam(optionsMap = options)
        theta.setMySetting(ThetaRepository.CaptureModeEnum.valueOf(captureMode), thetaOptions)
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * deleteMySetting - Delete shooting conditions in My Settings. Supported just by Theta X and Z1.
   * @param captureMode The target shooting mode
   * @param promise promise to set result, always true
   */
  @ReactMethod
  fun deleteMySetting(captureMode: String, promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        theta.deleteMySetting(ThetaRepository.CaptureModeEnum.valueOf(captureMode))
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * listPlugins - acquires a list of installed plugins
   * @param promise promise to set result
   */
  @ReactMethod
  fun listPlugins(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        val result = Arguments.createArray()
        theta.listPlugins().forEach {
          val pinfo = Arguments.createMap()
          pinfo.putString("name", it.name)
          pinfo.putString("packageName", it.packageName)
          pinfo.putString("version", it.version)
          pinfo.putBoolean("isPreInstalled", it.isPreInstalled)
          pinfo.putBoolean("isRunning", it.isRunning)
          pinfo.putBoolean("isForeground", it.isForeground)
          pinfo.putBoolean("isBoot", it.isBoot)
          pinfo.putBoolean("hasWebServer", it.hasWebServer)
          pinfo.putString("exitStatus", it.exitStatus)
          pinfo.putString("message", it.message)
          result.pushMap(pinfo)
        }
        promise.resolve(result)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * setPlugin - sets the installed plugin for boot. Supported just by Theta V.
   * @param packageName Package name of the target plugin.
   * @param promise promise to set result
   */
  @ReactMethod
  fun setPlugin(packageName: String, promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        theta.setPlugin(packageName)
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * startPlugin - start the plugin specified by the [packageName].
   * @param packageName Package name of the target plugin.
   * @param promise promise to set result
   */
  @ReactMethod
  fun startPlugin(packageName: String, promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        theta.startPlugin(packageName)
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * stopPlugin - Stop the running plugin.
   * @param promise promise to set result
   */
  @ReactMethod
  fun stopPlugin(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        theta.stopPlugin()
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * getPluginLicense - acquires the license for the installed plugin.
   * @param packageName Package name of the target plugin.
   * @param promise promise to set result
   */
  @ReactMethod
  fun getPluginLicense(packageName: String, promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        val result = theta.getPluginLicense(packageName)
        promise.resolve(result)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * getPluginOrders - Return the plugin orders.  Supported just by Theta X and Z1..
   * @param promise promise to set result, list of package names of plugins.
   */
  @ReactMethod
  fun getPluginOrders(promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        val result = theta.getPluginOrders()
        promise.resolve(Arguments.fromList(result))
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * setPluginOrders - Return the plugin orders.  Supported just by Theta X and Z1.
   * @param plugins list of package names of plugins
   * For Z1, list size must be three. No restrictions for the size for X.
   * When not specifying, set an empty string.
   * If an empty string is placed mid-way, it will be moved to the front.
   * Specifying zero package name will result in an error.
   * @param promise promise to set result
   */
  @ReactMethod
  fun setPluginOrders(plugins: ReadableArray, promise: Promise) {
    val theta = theta
    if (theta == null) {
      promise.reject(Exception(messageNotInit))
      return
    }
    launch {
      try {
        val pluginList = mutableListOf<String>()
        for (index in 0..(plugins.size() - 1)) {
          val plugin = plugins.getString(index)
          pluginList.add(plugin)
        }
        theta.setPluginOrders(pluginList)
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  companion object {
    const val NAME = "ThetaClientReactNative"
    const val EVENT_NAME = "ThetaFrameEvent"
    const val EVENT_NOTIFY = "ThetaNotify"
    const val NOTIFY_TIMESHIFT_PROGRESS = "TIME-SHIFT-PROGRESS"
    const val NOTIFY_TIMESHIFT_STOP_ERROR = "TIME-SHIFT-STOP-ERROR"
    const val NOTIFY_VIDEO_CAPTURE_STOP_ERROR = "VIDEO-CAPTURE-STOP-ERROR"
    const val NOTIFY_LIMITLESS_INTERVAL_CAPTURE_STOP_ERROR = "LIMITLESS-INTERVAL-CAPTURE-STOP-ERROR"
    const val NOTIFY_SHOT_COUNT_SPECIFIED_INTERVAL_PROGRESS = "SHOT-COUNT-SPECIFIED-INTERVAL-PROGRESS"
    const val NOTIFY_SHOT_COUNT_SPECIFIED_INTERVAL_STOP_ERROR = "SHOT-COUNT-SPECIFIED-INTERVAL-STOP-ERROR"
    const val NOTIFY_COMPOSITE_INTERVAL_PROGRESS = "COMPOSITE-INTERVAL-PROGRESS"
    const val NOTIFY_COMPOSITE_INTERVAL_STOP_ERROR = "COMPOSITE-INTERVAL-STOP-ERROR"
    const val NOTIFY_BURST_PROGRESS = "BURST-PROGRESS"
    const val NOTIFY_BURST_STOP_ERROR = "BURST-STOP-ERROR"
    const val NOTIFY_MULTI_BRACKET_PROGRESS = "MULTI-BRACKET-PROGRESS"
    const val NOTIFY_MULTI_BRACKET_STOP_ERROR = "MULTI-BRACKET-STOP-ERROR"
  }
}
