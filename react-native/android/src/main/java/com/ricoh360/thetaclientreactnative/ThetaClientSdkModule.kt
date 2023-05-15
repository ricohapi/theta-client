package com.ricoh360.thetaclientreactnative

import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.ricoh360.thetaclient.ThetaRepository
import com.ricoh360.thetaclient.capture.PhotoCapture
import com.ricoh360.thetaclient.capture.VideoCapture
import com.ricoh360.thetaclient.capture.VideoCapturing
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import kotlin.coroutines.*
import java.util.*
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
  var videoCaptureBuilder: VideoCapture.Builder? = null
  var videoCapture: VideoCapture? = null
  var videoCapturing: VideoCapturing? = null
  lateinit var theta: ThetaRepository
  var listenerCount: Int = 0

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
  fun initialize(endpoint: String, promise: Promise) {
    launch {
      try {
        theta = ThetaRepository.newInstance(endpoint)
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /**
   * getThetaInfo  -  retrieve ThetaInfo from THETA via repository
   * @param promise promise to set result
   */
  @ReactMethod
  fun getThetaInfo(promise: Promise) {
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
   * @param promise promise to set result
   */
  @ReactMethod
  fun listFiles(fileType: String, startPosition: Int, entryCount: Int, promise: Promise) {
    launch {
      try {
        val (fileList, totalEntries) = theta.listFiles(
          ThetaRepository.FileTypeEnum.valueOf(fileType),
          startPosition,
          entryCount
        )
        val resultlist = Arguments.createArray()
        fileList.forEach {
          val result = Arguments.createMap()
          result.putString("name", it.name)
          result.putDouble("size", it.size.toDouble())
          result.putString("dateTime", it.dateTime)
          result.putString("thumbnailUrl", it.thumbnailUrl)
          result.putString("fileUrl", it.fileUrl)
          resultlist.pushMap(result)
        }
        val resultmap = Arguments.createMap()
        resultmap.putArray("fileList", resultlist)
        resultmap.putInt("totalEntries", totalEntries)
        promise.resolve(resultmap)
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
    launch {
      try {
        theta.deleteAllVideoFiles()
        promise.resolve(true)
      } catch (t: Throwable) {
        promise.reject(t)
      }
    }
  }

  /** option converter */
  private val converters = mapOf(
    "aperture" to ApertureConverter(),
    "bluetoothPower" to BluetoothPowerConverter(),
    "cameraMode" to CameraModeConverter(),
    "captureMode" to CaptureModeConverter(),
    "colorTemperature" to ColorTemperatureConverter(),
    "dateTimeZone" to DateTimeZoneConverter(),
    "exposureCompensation" to ExposureCompensationConverter(),
    "exposureDelay" to ExposureDelayConverter(),
    "exposureProgram" to ExposureProgramConverter(),
    "fileFormat" to FileFormatConverter(),
    "filter" to FilterConverter(),
    "gpsInfo" to GpsInfoConverter(),
    "isGpsOn" to IsGpsOnConverter(),
    "iso" to IsoConverter(),
    "isoAutoHighLimit" to IsoAutoHighLimitConverter(),
    "language" to LanguageConverter(),
    "maxRecordableTime" to MaxRecordableTimeConverter(),
    "offDelay" to OffDelayConverter(),
    "sleepDelay" to SleepDelayConverter(),
    "remainingPictures" to RemainingPicturesConverter(),
    "remainingVideoSeconds" to RemainingVideoSecondsConverter(),
    "remainingSpace" to RemainingSpaceConverter(),
    "totalSpace" to TotalSpaceConverter(),
    "shutterVolume" to ShutterVolumeConverter(),
    "whiteBalance" to WhiteBalanceConverter(),
    "whiteBalanceAutoStrength" to WhiteBalanceAutoStrengthConverter(),
    "_gpsTagRecording" to GpsTagRecordingConverter(),
  )

  /** OptionNameEnum to option */
  private val optionEnumToOption = mapOf(
    "Aperture" to "aperture",
    "BluetoothPower" to "bluetoothPower",
    "CameraMode" to "cameraMode",
    "CaptureMode" to "captureMode",
    "ColorTemperature" to "colorTemperature",
    "DateTimeZone" to "dateTimeZone",
    "ExposureCompensation" to "exposureCompensation",
    "ExposureDelay" to "exposureDelay",
    "ExposureProgram" to "exposureProgram",
    "FileFormat" to "fileFormat",
    "Filter" to "filter",
    "GpsInfo" to "gpsInfo",
    "IsGpsOn" to "isGpsOn",
    "Iso" to "iso",
    "IsoAutoHighLimit" to "isoAutoHighLimit",
    "Language" to "language",
    "MaxRecordableTime" to "maxRecordableTime",
    "OffDelay" to "offDelay",
    "SleepDelay" to "sleepDelay",
    "RemainingPictures" to "remainingPictures",
    "RemainingVideoSeconds" to "remainingVideoSeconds",
    "RemainingSpace" to "remainingSpace",
    "TotalSpace" to "totalSpace",
    "ShutterVolume" to "shutterVolume",
    "WhiteBalance" to "whiteBalance",
    "WhiteBalanceAutoStrength" to "whiteBalanceAutoStrength",
  )

  /**
   * getOptions  -  get options from THETA via repository
   * @param optionNames option name list to get
   * @param promise promise to set result
   */
  @ReactMethod
  fun getOptions(optionNames: ReadableArray, promise: Promise) {
    launch {
      try {
        val optionNameList = mutableListOf<ThetaRepository.OptionNameEnum>()
        for (index in 0..(optionNames.size() - 1)) {
          val option = optionNames.getString(index)
          optionNameList.add(ThetaRepository.OptionNameEnum.valueOf(option))
        }
        val options = theta.getOptions(optionNameList)
        val result = Arguments.createMap()
        for (index in 0..(optionNames.size() - 1)) {
          val option = optionEnumToOption[optionNames.getString(index)]
          val cvt = converters[option]
          cvt?.setFromTheta(options, result)
        }
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
    launch {
      try {
        val thetaOptions = ThetaRepository.Options()
        val iterator = options.keySetIterator()
        while (iterator.hasNextKey()) {
          val key = iterator.nextKey()
          val cvt = converters[key]
          cvt?.setToTheta(thetaOptions, options)
        }
        theta.setOptions(thetaOptions)
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
    previewing = false
  }

  /**
   * getPhotoCaptureBuilder  -  get photo capture builder from repository
   */
  @ReactMethod
  fun getPhotoCaptureBuilder() {
    photoCaptureBuilder = theta.getPhotoCaptureBuilder()
  }

  /**
   * buildPhotoCapture  -  build photo capture
   * @param options option to take picture
   * @param resolve resolver for buildPhotoCapture
   * @param rejecter rejecter for buildPhotoCapture
   */
  @ReactMethod
  fun buildPhotoCapture(options: ReadableMap, promise: Promise) {
    if (photoCaptureBuilder == null) {
      promise.reject(Exception("no photoCaptureBuilder"))
      return
    }
    launch {
      try {
        val iterator = options.keySetIterator()
        while (iterator.hasNextKey()) {
          val key = iterator.nextKey()
          val cvt = converters[key]
          cvt?.setPhotoOption(options, photoCaptureBuilder!!)
        }
        photoCapture = photoCaptureBuilder!!.build()
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
    if (photoCapture == null) {
      promise.reject(Exception("no photoCapture"))
      return
    }
    class TakePictureCallback : PhotoCapture.TakePictureCallback {
      override fun onSuccess(fileUrl: String) {
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
   * getVideoCaptureBuilder  -  get video capture builder
   */
  @ReactMethod
  fun getVideoCaptureBuilder() {
    videoCaptureBuilder = theta.getVideoCaptureBuilder()
  }

  /**
   * buildVideoCapture  -  build video capture
   * @param options option to capture video
   * @param promise promise to set result
   */
  @ReactMethod
  fun buildVideoCapture(options: ReadableMap, promise: Promise) {
    if (videoCaptureBuilder == null) {
      promise.reject(Exception("no videoCaptureBuilder"))
      return
    }
    launch {
      try {
        val iterator = options.keySetIterator()
        while (iterator.hasNextKey()) {
          val key = iterator.nextKey()
          val cvt = converters[key]
          cvt?.setVideoOption(options, videoCaptureBuilder!!)
        }
        videoCapture = videoCaptureBuilder!!.build()
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
  fun startCapture(promise: Promise) {
    if (videoCapture == null) {
      promise.reject(Exception("no videoCapture"))
      return
    }
    class StartCaptureCallback : VideoCapture.StartCaptureCallback {
      override fun onSuccess(fileUrl: String) {
        promise.resolve(fileUrl)
        videoCapture = null
        videoCapturing = null
      }

      override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
        promise.reject(exception)
        videoCapture = null
        videoCapturing = null
      }
    }
    videoCapturing = videoCapture!!.startCapture(StartCaptureCallback())
  }

  /**
   * stopCapture  -  stop capturing video
   */
  @ReactMethod
  fun stopCapture() {
    videoCapturing?.stopCapture()
  }

  /**
   * getMetadata  -  retrieve meta data from THETA via repository
   * @param promise promise to set result
   */
  @ReactMethod
  fun getMetadata(fileUrl: String, promise: Promise) {
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
    launch {
      try {
        val result = Arguments.createArray()
        theta.listAccessPoints().forEach {
          val apinfo = Arguments.createMap()
          apinfo.putString("ssid", it.ssid)
          apinfo.putBoolean("ssidStealth", it.ssidStealth)
          apinfo.putString("authMode", it.authMode.toString())
          apinfo.putInt("connectionPriority", it.connectionPriority)
          apinfo.putBoolean("usingDhcp", it.usingDhcp)
          apinfo.putString("ipAddress", it.ipAddress)
          apinfo.putString("subnetMask", it.subnetMask)
          apinfo.putString("defaultGateway", it.defaultGateway)
          result.pushMap(apinfo)
        }
        promise.resolve(result)
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
   * @param promise promise to set result
   */
  @ReactMethod
  fun setAccessPointDynamically(
    ssid: String,
    ssidStealth: Boolean,
    authMode: String,
    password: String,
    connectionPriority: Int,
    promise: Promise
  ) {
    launch {
      try {
        theta.setAccessPointDynamically(
          ssid,
          ssidStealth,
          ThetaRepository.AuthModeEnum.valueOf(authMode),
          password,
          connectionPriority
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
    promise: Promise
  ) {
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
    launch {
      try {
        val options = theta.getMySetting(ThetaRepository.CaptureModeEnum.valueOf(captureMode))
        val result = Arguments.createMap()
        converters.forEach {
          val cvt = it.value
          cvt?.setFromTheta(options, result)
        }
        promise.resolve(result)
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
    launch {
      try {
        val optionNameList = mutableListOf<ThetaRepository.OptionNameEnum>()
        for (index in 0..(optionNames.size() - 1)) {
          val option = optionNames.getString(index)
          optionNameList.add(ThetaRepository.OptionNameEnum.valueOf(option))
        }
        val options = theta.getMySetting(optionNameList)
        val result = Arguments.createMap()
        for (index in 0..(optionNames.size() - 1)) {
          val option = optionEnumToOption[optionNames.getString(index)]
          val cvt = converters[option]
          cvt?.setFromTheta(options, result)
        }
        promise.resolve(result)
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
    launch {
      try {
        val thetaOptions = ThetaRepository.Options()
        val iterator = options.keySetIterator()
        while (iterator.hasNextKey()) {
          val key = iterator.nextKey()
          val cvt = converters[key]
          cvt?.setToTheta(thetaOptions, options)
        }
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
  }
}
