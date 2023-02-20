# THETA client tutorial	

* [Advance preparation](#preparation)
* [Create an instance of THETA client](#instance)
* [Take photos](#takephoto)
* [Take videos](#takevideo)
* [Display a preview](#preview)
* [List photos and videos in THETA](#list)
* [Get the metadata of a photo](#metadata)
* [Delete files in THETA](#delete)
* [Get the information of THETA](#info)
* [Get the state of THETA](#state)
* [Reset the THETA](#reset)

## <a id="preparation">Advance preparation</a>

* Update your RICOH THETA to the newest firmware version.
* Connect the wireless LAN between THETA and the smartphone on which an application using this SDK runs.	

## <a id="instance">Create an instance of THETA client</a>

`ThetaRepository.Config` is a class for configuration of THETA.
You can set following properties if necessary:

| Property | Value | Note |
| -------- | ----- | ------- |
| dateTime | YYYY:MM:DD hh:mm:ss+(-)hh:mm | Current date and time |
| language | DE, EN_GB, EN_US, FR, IT, JA, KO, ZH_CN, ZH_TW | Ignored by THETA S, SC and SC2 |
| offDelay | OFF_DELAY_5M, OFF_DELAY_10M, OFF_DELAY_15M, OFF_DELAY_30M, DISABLE | Time from sleep to automatic power-off (minutes)|
| sleepDelay |SLEEP_DELAY_3M, SLEEP_DELAY_5M, SLEEP_DELAY_7M, SLEEP_DELAY_10M, DISABLE | Time to enter sleep mode (minutes) |
| shutterVolume | 0 to 100 ||

Call `ThetaRepository.newInstance(endpoint, config)` to create an instance of THETA client. `endpoint` is the endpoint of THETA Web API, typically "http://192.168.1.1:80".

## <a id="takephoto">Take photos</a>	

First, call `ThetaRepository.getPhotoCaptureBuilder()` to get `PhotoCapture.Builder` object and configure shooting settings if necessary.

| Function | Parameters | Note |
| -------- | ---------- | ---- |
| setExposureCompensation() | M2_0, M1_7, M1_3, M1_0, M0_7, M0_3, ZERO, P0_3, P0_7, P1_0, P1_3, P1_7, P2_0 | -2.0 to +2.0 |
| setExposureDelay() | DELAY_OFF, DELAY_1, DELAY_2, DELAY_3, DELAY_4, DELAY_5, DELAY_6, DELAY_7, DELAY_8, DELAY_9, DELAY_10 | Operating time (sec.) of the self-timer. 0 to 10 seconds |
| setExposureProgram() | MANUAL, NORMAL_PROGRAM, APERTURE_PRIORITY, SHUTTER_PRIORITY, ISO_PRIORITY | APERTURE_PRIORITY is supported only by THETA Z1 |
|setAperture() | APERTURE_AUTO, APERTURE_2_1, APERTURE_3_5, APERTURE_5_6 | Other than APERTURE_AUTO is supported only by THETA Z1 |
| setColorTemperature() | 2500 to 10000  | 100 kelvin steps |
| setGpsInfo() | latitude (-90 to 90), longitude (-180 to 180), altitude (meters), dateTimeZone (YYYY:MM:DD hh:mm:ss+(-)hh:mm) | When GPS is disabled, assign 65535 to latitude and longitude, 0 to altitude, and empty string to dateTimeZone. |
| setGpsTagRecording() | ON, OFF | Turns position information assigning ON/OFF. Supported only by THETA X. |
| setIso() |ISO_AUTO, ISO_100, ISO_200, ISO_400, ISO_800, ISO_1600, ISO_3200 (SC2, V, Z1, X), ISO_6400 (Z1) | Set ISO sensitivity. |
| setIsoAutoHighLimit() | Same as setIso() | Set ISO sensitivity upper limit when ISO sensitivity is set to automatic. Supported only by THETA SC2, V, Z1 and X. ISO_100 is supported only by THETA X. |
| setWhiteBalance() | AUTO, DAYLIGHT, SHADE, CLOUDY_DAYLIGHT, INCANDESCENT, WARM_WHITE_FLUORESCENT, DAYLIGHT_FLUORESCENT, DAYWHITE_FLUORESCENT, FLUORESCENT, BULB_FLUORESCENT, COLOR_TEMPERATURE, UNDERWATER (SC2, V, Z1, X) | |
| setFilter() | OFF, NOISE_REDUCTION, HDR | Image processing filter |
| setFileFormat() | IMAGE_2K (S, SC), IMAGE_5K (S, SC, SC2, V), IMAGE_6_7K (Z1), RAW_P_6_7K (Z1), IMAGE_5_5K (X), IMAGE_11K (X) | |

After settings are performed, call `PhotoCapture.Builder.build()` so that you get `PhotoCapture` object.

Last, call `PhotoCapture.takePicture(callback: TakePictureCallback)`.

```kotlin
interface TakePictureCallback {
        fun onSuccess(fileUrl: String)
        fun onError(exception: ThetaRepositoryException)
}
```

When the JPEG file is created after shooting, `onSuccess()` of the callback is called.
You can HTTP GET `fileUrl` for the JPEG file.

Detail information of each file format is following:

| File format | File type | Width (px) | Height (px) | S | SC | SC2 | V | Z1 | X |
| ----------- | --------- | ----- | ------ | - | -- | --- | - | -- | - |
| IMAGE_2K | JPEG | 2,048 | 1,024 | + | + | - | - | - | - |
| IMAGE_5K | JPEG | 5,376 | 2,688 | + | + | + | + | - | - |
| IMAGE_6_7K | JPEG | 6,720 | 3,360 | - | - | - | - | + | - |
| RAW_P_6_7K | RAW (DNG)| 6,720 | 3,360 | - | - | - | - | + | - |
| IMAGE_5_5K | JPEG | 5,504 | 2,752 | - | - | - | - | - | + |
| IMAGE_11K | JPEG | 11,008 | 5,504 | - | - | - | - | - | + |


## <a id="takevideo">Take videos</a>

First, call `ThetaRepository.getVideoCaptureBuilder()` to get `VideoCapture.Builder` object and configure shooting settings if necessary.

| Function | Parameters | Note |
| -------- | ---------- | ---- |
| setExposureCompensation() | M2_0, M1_7, M1_3, M1_0, M0_7, M0_3, ZERO, P0_3, P0_7, P1_0, P1_3, P1_7, P2_0 | -2.0 to +2.0 |
| setExposureDelay() | DELAY_OFF, DELAY_1, DELAY_2, DELAY_3, DELAY_4, DELAY_5, DELAY_6, DELAY_7, DELAY_8, DELAY_9, DELAY_10 | Operating time (sec.) of the self-timer. 0 to 10 seconds. THETA S and SC supports only DELAY_OFF. |
| setExposureProgram() | MANUAL, NORMAL_PROGRAM, APERTURE_PRIORITY, SHUTTER_PRIORITY, ISO_PRIORITY | APERTURE_PRIORITY is supported only by THETA Z1 |
|setAperture() | APERTURE_AUTO, APERTURE_2_1, APERTURE_3_5, APERTURE_5_6 | Other than APERTURE_AUTO is supported only THETA Z1 |
| setColorTemperature() | 2500 to 10000  | 100 kelvin steps |
| setGpsInfo() | latitude (-90 to 90), longitude (-180 to 180), altitude (meters), dateTimeZone (YYYY:MM:DD hh:mm:ss+(-)hh:mm) | When GPS is disabled, assign 65535 to latitude and longitude, 0 to altitude, and empty string to dateTimeZone. |
| setGpsTagRecording() | ON, OFF | Turns position information assigning ON/OFF. Supported only by THETA X. |
| setIso() |ISO_AUTO, ISO_100, ISO_200, ISO_400, ISO_800, ISO_1600, ISO_3200 (SC2, V, Z1, X), ISO_6400 (SC2, V, Z1) | Set ISO sensitivity. |
| setIsoAutoHighLimit() | Same as setIso() | Set ISO sensitivity upper limit when ISO sensitivity is set to automatic. ISO_100 is supported only by THETA X. |
| setWhiteBalance() | AUTO, DAYLIGHT, SHADE, CLOUDY_DAYLIGHT, INCANDESCENT, WARM_WHITE_FLUORESCENT, DAYLIGHT_FLUORESCENT, DAYWHITE_FLUORESCENT, FLUORESCENT, BULB_FLUORESCENT, COLOR_TEMPERATURE, UNDERWATER (SC2, V, Z1, X) | |
| setMaxRecordableTime() | RECORDABLE_TIME_180, RECORDABLE_TIME_300, RECORDABLE_TIME_1500 | Maximum recordable time (in seconds) of the camera, 300 seconds or 1500 seconds (other than SC2). SC2 supports 180 only. |
| setFileFormat() | VIDEO_HD (S, SC), VIDEO_FULL_HD (S, SC), VIDEO_2K (SC2, V, Z1), VIDEO_4K (SC2, V, Z1), VIDEO_2K_30F (X), VIDEO_2K_60F (X), VIDEO_4K_30F (X), VIDEO_4K_60F (X), VIDEO_5_7K_2F (X), VIDEO_5_7K_5F (X), VIDEO_5_7K_30F (X), VIDEO_7K_2F (X), VIDEO_7K_5F (X), VIDEO_7K_10F (X) | |


After shooting settings are performed, call `VideoCapture.Builder.build()` so that you get `VideoCapture` object.

Next, call `VideoCapture.startCapture(callback: StartCaptureCallback)` to start recording video.

```kotlin
interface StartCaptureCallback {
        fun onSuccess(fileUrl: String)
        fun onError(exception: ThetaRepositoryException)
}
```

`VideoCapture.startCapture(callback)` returns `VideoCapturing` object.
To finish recording the video, call `VideoCapturing.stopCapture()`.
When the MP4 file is created after shooting, `onSuccess()` of the callback is  called.
You can HTTP GET `fileUrl` for the MP4 file.

Detail information of each file format is following:

| File format | File type | Width (px) | Height (px) | Frame rate (fps) | S | SC | SC2 | V | Z1 | X |
| ----------- | --------- | ---------- | ----- | ------ | - | -- | --- | - | -- | - |
| VIDEO_HD | MP4 | 1,280 |720 | 15 | + | + | - | - | - | - |
| VIDEO_FULL_HD | MP4 | 1,920 | 1,080 | 30 | + | + | - | - | - | - |
| VIDEO_2K | MP4 | 1,920 | 960 | 30 | - | - | + | + | + | - |
| VIDEO_4K | MP4 | 3,840 | 1,920 | 30 | - | - | + | + | + | - |
| VIDEO_2K_30F | MP4 | 1,920 | 960 | 30 | - | - | - | - | - | + |
| VIDEO_2K_60F | MP4 | 1,920 | 960 | 60 | - | - | - | - | - | + |
| VIDEO_4K_30F | MP4 | 3,840 | 1,920 | 30 | - | - | - | - | - | + |
| VIDEO_4K_60F | MP4 | 3,840 | 1,920 | 60 | - | - | - | - | - | + |
| VIDEO_5_7K_2F | MP4 | 5,760 | 2,880 | 2 | - | - | - | - | - | + |
| VIDEO_5_7K_5F | MP4 | 5,760 | 2,880 | 5 | - | - | - | - | - | + |
| VIDEO_5_7K_30F | MP4 | 5,760 | 2,880 | 30 | - | - | - | - | - | + |
| VIDEO_7K_2F | MP4 | 7,680 | 3,840 | 2 | - | - | - | - | - | + |
| VIDEO_7K_5F | MP4 | 7,680 | 3,840 | 5 | - | - | - | - | - | + |
| VIDEO_7K_10F | MP4 | 7,680 | 3,840 | 10 | - | - | - | - | - | + |


## <a id="preview">Display a preview</a>

The preview is an equirectangular data of motion JPEG format.
Only available in the still image shooting mode for THETA S and THETA SC.
Data acquisition stops when THETA is operated, shooting is started, or the shooting mode is changed.

On Android, call `ThetaRepository.getLivePreview()` then Flow<[io.ktor.utils.io.core.ByteReadPacket](https://api.ktor.io/ktor-io/io.ktor.utils.io.core/-byte-read-packet.html)> returns, from which you get the JPEG data for each frame.	

On other environment, call `ThetaRepository.getLivePreview(frameHandler)`.
`frameHandler` is a callback function called when each frame is acquired.
If `frameHandler` returns `false`, the preview finishes.
For details, see source codes of demo applications.

Detail information of preview is following:

| Model | Width (px) | Height (px) | Frame rate (fps) |
| ----- | ----- | ------ | ---------- |
| SC2, V, Z1, X | 1,024 | 512 | 30 |
| S, SC | 640 | 320 | 10 |


## <a id="list">List still images and videos in THETA</a>

The list of still images (JPEG files) and videos (MP4 files) in THETA can be obtained using `ThetaRepository.listFiles(fileType, startPosition, entryCount)`.

The `fileType` specifies the file types to acquire:

|Value| File types|	
|---|---|	
|IMAGE|	still images (JPEG files)|	
|VIDEO| videos (MP4 files)|	
|ALL| all files|	

The `startPosition` is the position to start acquiring the file list.
If a number larger than the number of existing files is specified, an empty list is acquired.
Default is the top of the list.

The `entryCount` is the number of files to acquire.
If the number of existing files is smaller than the specified number of files, all available files are only acquired.

`listFiles()` returns a list of `ThetaRepository.FileInfo`:

| Property | Value |
| -------- | ----- |
| name | File name |
| size | File size (bytes) |
| dateTime | File creation or update time. Local time.|
| fileUrl | JPEG or MP4 file URL |
| thumbnailUrl | Thumbnail file URL |


You can HTTP GET `FileInfo.fileUrl` to acquire a JPEG file or a MP4 file.
Also, you can HTTP GET `FileInfo.thumbnailUrl` to acquire thumbnail JPEG file.

## <a id="metadata">Get the metadata of a still image</a>

Call `ThetaRepository.getMetadata(fileUrl)` then you can get the meta information for the specified still image. Its contents are following:

| Object class | Property name | Content |
| ----------- | ------------- | ------- |
|Exif| exifVersion | EXIF Support version |
|Exif| dateTime | Time created or updated |
|Exif| imageWidth | Width (px). THETA X is not supported |
|Exif| imageLength | Height (px). THETA X is not supported |
|Exif| gpsLatitude | Latitude |
|Exif| gpsLongitude | Longitude |
|Xmp| fullPanoWidthPixels | Width (px) when the actual image size is based on a panoramic image |
|Xmp| fullPanoHeightPixels | Height (px) when the actual image size is based on a panoramic image |
|Xmp| poseHeadingDegrees | Compass heading, measured in degrees clockwise from North, for the center the image. Only THETA SC2, V and Z1 are supported. |

## <a id="delete">Delete files in THETA</a>

To delete files in THETA, call `ThetaRepository.deleteFiles(fileUrls: List<String>)`.  The argument is a list of URLs of deleting files.

If you want to delete all files, call one of the functions: `deleteAllFiles()`, `deleteAllImageFiles()` and `deleteAllVideoFiles()`.

## 	<a id="info">Get the information of THETA</a>

Call `ThetaRepository.getThetaInfo()` then you can get `ThetaInfo` object.  Its contents are following:

| Name | Content |
| ---- | ------- |
| model | model name |
| serialNumber | serial number |
| firmwareVersion | firmware version |
| hasGps | presence of GPS |
| hasGyro | presence of gyroscope |
| uptime | elapsed time after startup (seconds) |


## 	<a id="state">Get the state of THETA</a>
Call `ThetaRepository.getThetaState()` then you can get `ThetaState` object. Its contents are following:

| Name | Content | Note |
| ---- | ------- | ---- |
| batteryLevel | 0.0 to 1.0 | Battery level. When using an external power source, 1.0 |
| chargingState | CHARGING, COMPLETED, NOT_CHARGING | Battery charging state |
| recordedTime | recorded time of video (seconds) ||
| recordableTime | remaining time of video (seconds) ||
| latestFileUrl | URL of the last saved file ||
| isSdCard | record to SD card or not | THETA X only |
| fingerprint | Unique value of current state | If the state changes, fingerprint also changes. |

## <a id="reset">Reset the THETA</a>

Call `ThetaRepository.reset()` to reset all device settings and capture settings. After reset, THETA is restarted.
