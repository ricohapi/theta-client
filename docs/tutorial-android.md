# THETA client tutorial for Android

## Advance preparation

- Add following descriptions to the `dependencies` of your module's `build.gradle`.

  ```
  implementation "com.ricoh360.thetaclient:theta-client:1.11.1"
  ```

- Connect the wireless LAN between THETA and the smartphone that runs on the application using this SDK.

## Create an instance of THETA client

When creating an instance, you can set the camera date and time, the language, the shutter volume, the time to sleep, and the time to power off.

- Language setting (ignored by THETA S, THETA SC)
  - "en-US"
  - "en-GB"
  - "ja"
  - "fr"
  - "de"
  - "zh-TW"
  - "zh-CN"
  - "it"
  - "ko"
- Date Adjust
  - YYYY:MM:DD HH:MM:SS+HH:MM format
- Time to enter sleep mode (min)
  - Not Auto Off, 3 min, 5 min, 7 min, 10 min
- Time from sleep to automatic power-off (minutes)
  - Not automatically off, 5 minutes, 10 minutes, 15 minutes, 30 minutes
- Shutter sound
  - 0 ～ 100

```kotlin
Import com.ricoh360.pf.theta.ThetaRepository
Val thetaUrl = "192.168.1.1:80"
Val thetaConfig = ThetaRepository.Config()
// thetaConfig.dateTime = "2023:01:01 12:34:56+09:00" // set current time
// thetaConfig.language = ThetaRepository.LanguageEnum.EN_US
// thetaConfig.shutterVolume = 40 // 0 to 100
// thetaConfig.sleepDelay = ThetaRepository.SleepDelayEnum.SLEEP_DELAY_5M
// thetaConfig.offDelay = ThetaRepository.OffDelayEnum.DISABLE
ThetaRepository = ThetaRepository.newInstance(thetaUrl, thetaConfig)
```

## Shoot still images

First, shooting settings are performed using `PhotoCapture.Builder` to create `PhotoCapture` objects.

```kotlin
class TakenCallback : PhotoCapture.TakePictureCallback {
	override fun onSuccess(fileUrl: String) {
		// get JPEG file
	}
	override fun onError(exception: ThetaRepository.ThetaRepositoryException) {
		// error processing
	}
}

thetaRepository.getPhotoCaptureBuilder()
	.setExposureProgram(ThetaRepository.ExposureProgramEnum.NORMAL_PROGRAM)
	.setWhiteBalance(ThetaRepository.WhiteBalanceEnum.AUTO)
	.setExposureCompensation(ThetaRepository.ExposureCompensationEnum.ZERO)
	.setIsoAutoHighLimit(ThetaRepository.IsoAutoHighLimitEnum.ISO_800)
	.setFilter(ThetaRepository.FilterEnum.HDR)
	.setExposureDelay(ThetaRepository.ExposureDelayEnum.DELAY_OFF) // self-timer
	.setFileFormat(ThetaRepository.IMAGE_11K)
	.build()
	.takePicture(TakenCallback())
```

See [Display a preview](#preview) for instructions on how to view preview.

### Properties that can be set for shooting still images

- Exposure compensation
  - -2.0, -1.7, -1.3, -1.0, -0.7, -0.3, 0.0, 0.3, 0.7, 1.0, 1.3, 1.7, 2.0
- Self-timer
  - 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
- Resolution

| Model               | Specify resolution | Width (Pixel) | Height (Pixel) |
| ------------------- | ------------------ | ------------- | -------------- |
| THETA X             | IMAGE_11K          | 11,008        | 5,504          |
| THETA X             | IMAGE_5_5K         | 5,504         | 2,752          |
| THETA Z1            | IMAGE_6_7K         | 6,720         | 3,360          |
| THETA S, SC, SC2, V | IMAGE_5K           | 5,376         | 2,688          |

- Image processing
  - None
  - Noise reduction
  - HDR
- GPS ON/OFF (ignored except for THETA X)
  - ON
  - OFF
- ISO upper limit (THETA V firmware v2.50.1 or earlier, ignored even if specified in THETA S or THETA SC)
  - 200, 250, 320, 400, 500, 640, 800, 1000, 1250, 1600, 2000, 2500, 3200

#### Set the bitrate value for still image capture (THETA X)

The bitrate value for still image capture can be set in THETA X(see [api-spec](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/options/_bitrate.md)).
To set this value with THETA Client, follow the steps below.

1. Create a `PhotoCapture` object
1. Call `setOptions()` to set the `bitrate` value
1. Call `takePicture()`

The reason this step is necessary is that the `bitrate` value on the camera is reset when the `captureMode` value is set to `image` in `PhotoCapture.Builder.build()`.

```kotlin
val photoCapture = thetaRepository.getPhotoCaptureBuilder().build()

val options = ThetaRepository.Options()
options.bitrate = ThetaRepository.BitrateNumber(1048576)
thetaRepository.setOptions(options)

photoCapture.takePicture(TakenCallback())
```

## Shoot a video

First, shooting settings are performed using `VideoCapture.Builder` to create `VideoCapture` objects.

```kotlin
Val videoCapture: VideoCapture = VideoCapture.Builder()
    .setAutoIsoUpperLimit(IsoEnum.ISO_800)
    .setFileFormat(VideoFileFormatEnum.VIDEO_5_7K_30F)
    .build()
```

In the example above, the maximum ISO sensitivity value is 800 and the resolution is 5.7K.
You can display previews other than THETA S and THETA SC.
See [Display a preview](#preview)
Next, we call `Video Capture.startCapture()` to start recording videos.
The argument is passed to the callback function that HTTP GET the MP4 file.

```kotlin
class TakenCallback : VideoCapture.StartCaptureCallback {
    override fun onCaptureCompleted(fileUrl: String?) {
		// get MP4 file
	}
    override fun onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
		// error processing
	}
    override fun onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
		// error stopCapture
	}
}

val videoCapturing: VideoCapturing = videoCapture.startCapture(TakenCallback())
```

Next, we call `Video Capturing.stopCapture()` to finish recording the video.
When the MP4 file is created after shooting, the callback function passed to `startCapture()` is called.

```kotlin
VideoCapturing.stopCapture()
```

### Properties that can be set when shooting videos

- Resolution

| Model        | Specify resolution | Width (pixel) | Height (pixel) |
| ------------ | ------------------ | ------------- | -------------- |
| THETA X      | VIDEO_5_7K_30F     | 5,760         | 2,880          |
| THETA X      | VIDEO_4K_30F       | 3,840         | 1,920          |
| THETA X      | VIDEO_2K_30F       | 1,920         | 960            |
| THETA Z1     | VIDEO_4K           | 3,840         | 1,920          |
| THETA Z1     | VIDEO_2K           | 1,920         | 960            |
| THETA SC2, V | VIDEO_4K           | 3,840         | 1,920          |
| THETA SC2, V | VIDEO_2K           | 1,920         | 960            |
| THETA S, SC  | VIDEO_FULL_HD      | 1,920         | 1,080          |

- Maximum shooting time (min)
  - 5
  - 25
- ISO upper limit (THETA V firmware v2.50.1 or earlier, ignored even if specified in THETA S or THETA SC)
  - 200, 250, 320, 400, 500, 640, 800, 1000, 1250, 1600, 2000, 2500, 3200

## <a id="preview"></a>Display a preview

The preview is an equirectangular form of motion JPEG.

| Model       | Pixel | Pixel | Frame rate (fps) | Remarks                     |
| ----------- | ----- | ----- | ---------------- | --------------------------- |
| THETA X     | 1024  | 512   | 30               |                             |
| THETA Z1    | 1024  | 512   | 30               |                             |
| THETA V     | 1024  | 512   | 30               | Firmware v2.21.1 or later   |
| THETA V     | 1024  | 512   | 8                | Firmware v2.20.1 or earlier |
| THETA SC2   | 1024  | 512   | 30               |                             |
| THETA S, SC | 640   | 320   | 10               |                             |

If you call `ThetaRepository.getLivePreview()` then "Flow<[io.ktor.utils.io.core.ByteReadPacket](https://api.ktor.io/ktor-io/io.ktor.utils.io.core/-byte-read-packet/index.html)>" returns, you get the JPEG for each frame.

```kotlin
import io.ktor.utils.io.core.ByteReadPacket

thetaRepository.getLivePreview()
	.collect { byteReadPacket ->
		if (isActive) {
			byteReadPacket.inputStream().use {
				// Displaying the stream as JPEG
			}
		}
		byteReadPacket.release()
	}
```

## List still images and videos in THETA

The list of still pictures (JPEG file) and videos (MP4 file) in THETA can be obtained using `ThetaRepository.listFiles(fileType, startPosition, entryCount)`.
The `fileType` is the `ThetaRepository.FileTypeEnum` type, whose contents are as follows.
The return type of `ThetaRepository.listFiles()` is `ThetaRepository.ThetaFiles`, and property `fileList` of `ThetaFiles` is the list of files in THETA.
`fileList` is a list of `ThetaRepository.FileInfo`.

JPEG and MP4 files refer to `FileInfo.fileUrl` and thumbnail JPEG files refer to `FileInfo.thumbnailUrl` and HTTP GET.

- ThetaRepository.FileTypeEnum

  | Value | Content                           |
  | ----- | --------------------------------- |
  | IMAGE | List of still images (JPEG files) |
  | VIDEO | List of videos (MP4 files)        |
  | ALL   | List all files                    |

- ThetaFiles

  | Property name | Type             | Contents                                                                                                                                          |
  | ------------- | ---------------- | ------------------------------------------------------------------------------------------------------------------------------------------------- |
  | fileList      | List\<FileInfo\> | The list of files in THETA                                                                                                                        |
  | totalEntries  | Int              | Number of files in THETA (see [api spec](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.list_files.md)) |

- ThetaRepository.FileInfo

  | Property name | Type   | Contents                                     |
  | ------------- | ------ | -------------------------------------------- |
  | name          | String | Represents the file name                     |
  | size          | Long   | Indicates the file size (in bytes)           |
  | dateTime      | String | Shooting date and time (YYYY:MM:DD HH:MM:SS) |
  | fileUrl       | String | Represents the URL of the file               |
  | thumbnailUrl  | String | Represents a thumbnail URL                   |

## Get the THETA information

`ThetaRepository.getThetaInfo()` is called.

## Get the state of THETA

`ThetaRepository.getThetaState()` is called.

## Reset THETA

`ThetaRepository.reset()` is called.
