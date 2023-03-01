# THETA CLient Tutorial for iOS

## Available models
* RICOH THETA X
* RICOH THETA Z1
* RICOH THETA V
* RICOH THETA SC2
* RICOH THETA S (firmware v1.62 or later only)
* RICOH THETA SC

## Advance preparation

Connect the wireless LAN between THETA and the smartphone that runs on the application using this SDK.

## To instantiate SDK

``` swift
import THETAClient
// Create ThetaRepository object by specifying an IP address
ThetaRepository.Companion.shared.doNewInstance(
  endPoint:"http://<THETA IP ADDRESS>"
) {response, error in
  if let instance = response {
    theta = instance
  }
  if let thetaError = error {
    // handle error
  }
}
```
* THETA IP ADDRESS

| Mode |Address |
|-------|---------|
|Direct mode|192.168.1.1|
|Other| Assigned IP address|

* When downloading images or videos from THETA using URLSession, the connection is plain. Therefore, the connection permission setting to Info.plist is required depending on the destination address (default 192.168.1.1).
The following is an example of Info.plist by default.
Xcode `Signing & Capabilities` -> `App Transport Security Exception` can also be added.
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
  <dict>
    <key>NSAppTransportSecurity</key>
    <dict>
      <key>NSAllowsArbitraryLoads</key>
      <false/>
      <key>NSExceptionDomains</key>
      <dict>
        <key>192.168.1.1</key>
        <dict>
          <key>NSIncludesSubdomain</key>
          <false/>
          <key>NSTemporaryExceptionAllowsInsecureHTTPLoads</key>
          <true/>
          <key>NSTemporaryExceptionRequiresForwardSecrecy</key>
          <false/>
        </dict>
      </dict>
    </dict>
  </dict>
</plist>
```

## Shoot still images

First, use `ThetaRepository.getPhotoCaptureBuilder()` to set the shooting and create the `PhotoCapture` object.

``` swift
Task {
  do {
    let photoCapture: PhotoCapture = try await withCheckedThrowingContinuation {continuation in
      PhotoCaptureBuilder()
       .setIsoAutoHighLimit(iso: ThetaRepository.IsoAutoHighLimitEnum.iso1000)
       .setFileFormat(fileFormat: ThetaRepository.PhotoFileFormat.image5k)
       .build {capture, error in
         if let photoCapture = capture {
           continuation.resume(returning: photoCapture)
         }
         if let thetaError = error {
           continuation.resume(throwing: thetaError)
         }
       }
    }
  } catch {
    // catch thetaError
  }
}
```
The above example sets the maximum ISO sensitivity to 1000 and the file format to image5k.

See [View preview](#preview) for instructions on how to view preview.

Then we call `PhotoCapture.takePicture(callback:)` to shoot still pictures.
Create and call a callback class that implements `PhotoCaptureTakePictureCallback` as follows.

``` swift
do {
  class Callback: PhotoCaptureTakePictureCallback {
      let callback: (_ fileUrl: String?, _ error: Error?) -> Void
      init(_ callback: @escaping (_ fileUrl: String?, _ error: Error?) -> Void) {
          self.callback = callback
      }
      func onSuccess(fileUrl: String) {
          callback(fileUrl, nil)
      }
      func onError(exception: ThetaRepository.ThetaRepositoryException) {
          callback(nil, exception as? Error)
      }
  }
  let fileUrl: String = try await withCheckedThrowingContinuation {continuation in
      photoCapture.takePicture(
        callback: Callback {fileUrl, error in
            if let photoUrl = fileUrl {
                continuation.resume(returning: photoUrl)
            }
            if let thetaError = error {
                continuation.resume(throwing: thetaError)
            }
        }
      )
  }
  // send GET request for fileUrl and receive a JPEG file
} catch {
  // catch error while take picture
}
```

### Properties that can be set for shooting still images

* Exposure Compensation: `setExposureCompensation(value:ThetaRepository.ExposureCompensationEnum)`

| Value|Correction value |Remarks|
|---|---:|---|
|m20|-2.0f||
|m17|-1.7f||
|m13|-1.0f||
|m07|-0.7f||
|m03|-0.3f||
|zero|0.0f||
|p03|0.3f||
|p07|0.7f||
|p13|1.0f||
|p17|1.7f||
|p20|2.0f||

* Exposure Delay Setting: `setExposureDelay(delay:ThetaRepository.ExposureDelayEnum)`

Delay between the takePicture command and the start of exposure  (= self-timer).

| Value|Delay time (seconds) |Remarks|
|---|---:|---|
|delayOff|0||
|delay1|1||
|delay2|2||
|delay3|3||
|delay4|4||
|delay5|5||
|delay6|6||
|delay7|7||
|delay8|8||
|delay9|9||
|delay10|10||

* Exposure Program: `setExposureProgram(program:ThetaRepository.ExposureProgramEnum)`

| Value|Content |Remarks|
|---|---|---|
|manual|Manual||
|normalProgram|Regular programs||
|aperturePriority|Aperture priority||
|shutterPriority|Shutter priority||
|isoPriority|ISO priority||

* File Format: `setFileFormat(fileFormat:ThetaRepository.PhotoFileFormatEnum)`

| Value|Type| Width|Height |S|SC|SC2|V|Z1|X|
|---|---|--:|--:|:-:|:-:|:-:|:-:|:-:|:-:|
|image2k|Jpeg|2048|1024|+|+|-|-|-|-|
|image5k|Jpeg|5376|2688|+|+|+|+|-|-|
|image67k|Jpeg|6720|3360|-|-|-|-|+|-|
|rawP67k|Raw+|6720|3360|-|-|-|-|+|-|
|image55k|Jpeg|5504|2752|-|-|-|-|-|+|
|image11k|Jpeg|11008|5504|-|-|-|-|-|+|

* Image Processing: `setFilter(filter:ThetaRepository.Filter)`

| Value|Content |Remarks|
|---|---|---|
|off|None|||
|noiseReduction|Noise reduction||
|hdr|HDR||

* GPS on/off: `setGpsTagRecording(value:ThetaRepository.GpsTagRecordingEnum)`

Other than THETA X, this setting is ignored.

| Value|Content |Remarks
|---|---|---|
|on|GPS | |
|off|No GPS||

* ISO value: `setIso(iso:ThetaRepository.IsoEnum)`

| Value|ISO value |Remarks|
|---|---:|---|
|isoAuto|0||
|iso50|50|THETA X only|
|iso64|64|THETA SC2, V and X only|
|iso80|80|THETA S and SC don't support|
|iso100|100||
|iso125|125||
|iso160|160||
|iso200|200||
|iso250|250||
|iso320|320||
|iso400|400||
|iso500|150||
|iso640|640||
|iso800|800||
|iso1000|1000||
|iso1250|1250||
|iso1600|1600||
|iso2000|2000|THETA S and SC don't support|
|iso2500|2500|THETA S and SC don't support|
|iso3200|3200|THETA S and SC don't support|
|iso4000|4000|THETA Z1 only|
|iso5000|5000|THETA Z1 only|
|iso6400|6400|THETA Z1 only|

* ISO upper limit: `setIsoAutoHighLimit(iso:ThetaRepository.IsoAutoHighLimitEnum)`

This setting is ignored by THETA V Firmware v2.50.1 or earlier, THETA S and THETA SC.

| Value|Upper ISO limit |Remarks|
|---|---:|---|
|iso100|100|THETA X only|
|iso125|125|THETA X only|
|iso160|160|THETA X only|
|iso200|200||
|iso250|250||
|iso320|320||
|iso400|400||
|iso500|150||
|iso640|640||
|iso800|800||
|iso1000|1000||
|iso1250|1250||
|iso1600|1600||
|iso2000|2000||
|iso2500|2500||
|iso3200|3200||
|iso4000|4000|THETA Z1 only|
|iso5000|5000|THETA Z1 only|
|iso6400|6400|THETA Z1 only|

* Aperture setting: `setAperture(aperture:ThetaRepository.ApertureEnum)`

| Value|Setting value |Remarks|
|---|---:|---|
|apertureAuto|Automatic||
|aperture20|2.0f|THETA V or prior only|
|aperture21|2.1f|THETA Z1 only|
|aperture24|2.4f|THETA X only|
|aperture35|3.5f|THETA Z1 only|
|aperture56|5.6f|THETA Z1 only|

* Color temperature setting: `setColorTemperature(kelvin:Int)`
  * 2500 to 10000

* GPS information: `setGpsInfo(gpsInfo:ThetaRepository.GpsInfo)`

GpsInfo shall be prepared as follows:
`ThetaRepository.GpsInfo(latitude:longitude:altitude:dateTimeZone:)`

| Value|Setting value |Remarks|
|---|---|---|
|latitude|Latitude | -90 to 90 or 65535 (disabled)|
|longitude|Longitude | -180 to 180 or 65535 (disabled)|
|altitude|Altitude |||
|dateTimeZone|Date and time|YYYY:MM:DD hh:mm:ss+(-)hh:mm or empty string (disabled)|

* White balance: `setWhiteBalance(whiteBalance:ThetaRepository.WhiteBalanceEnum)`

| Value|Setting value |Remarks|
|---|---|---|
|auto|Automatic||
|daylight|Outdoor|About 5,200|
|shade|Shade|About 7,000|
|cloudyDaylight|Cloudy|About 6,000|
|incandescent|Incandescent light 1|About 3,200|
|warmWhiteFluorescent|Incandescent light 2||
|daylightFluorescent|Fluorescent light 1 (daylight)||
|daywhiteFluorescent|Fluorescent light 2 (natural white)||
|fluorescent|Fluorescent light 3 (white)|About 4,000|
|bulbFluorescent|Fluorescent light 4 (light bulb color)||
|colorTemperature|CT settings (specified by the colorTemperature option)|RICOH THETA S firmware v01.82 or later and RICOH THETA SC firmware v01.10 or later|
|underwater|Underwater|RICOH THETA V firmware v3.21.1 or later|

## Shoot a video

First, use the `ThetaRepository.getVideoCaptureBuilder()` to set the shooting and create the `VideoCapture` object.

``` swift
Task {
  do {
    let videoCapture: VideoCapture = try await withCheckedThrowingContinuation {continuation in
      VideoCaptureBuilder()
       .setIsoAutoHighLimit(iso: ThetaRepository.IsoAutoHighLimitEnum.iso800)
       .setFileFormat(fileFormat: ThetaRepository.VideoFileFormatEnum.videoHd)
       .build {capture, error in
         if let videoCapture = capture {
           continuation.resume(returning: videoCapture)
         }
         if let thetaError = error {
           continuation.resume(throwing: thetaError)
         }
       }
    }
  } catch {
    // catch thetaError
  }
}
```

In the example above, the maximum ISO sensitivity is set to 800 and the file format is set to `videoHd`.

You can display previews other than THETA S and THETA SC.
See [Display preview](#preview)

Next, we call `VideoCapture.startCapture(callback:)` to start shooting videos.
Create and call a callback class that implements `VideoCaptureStartCaptureCallback` as follows.

``` swift
class Callback: VideoCaptureStartCaptureCallback {
    let callback: (_ fileUrl: String?, _ error: Error?) -> Void
    init(_ callback: @escaping (_ fileUrl: String?, _ error: Error?) -> Void) {
        self.callback = callback
    }
    func onSuccess(fileUrl: String) {
        callback(fileUrl, nil)
    }
    func onError(exception: ThetaRepository.ThetaRepositoryException) {
        callback(nil, exception as? Error)
    }
}
let videoCapturing = videoCapture.startCapture(
  callback: Callback {fileUrl, error in
    if let videoUrl = fileUrl {
      // send GET request and receive MP4 file
    }
    if let thetaError = error {
      // handle error
    }
  }
)
```

Next, we call `VideoCapturing.stopCapture()` to finish recording the video.
When the MP4 file is created after shooting, the callback function passed to `startCapture(callback:)` is called.

``` swift
VideoCapturing.stopCapture()
```

### Properties that can be set when shooting videos

* Exposure compensation: `setExposureCompensation(value:ThetaRepository.ExposureCompensationEnum)`

| Value|Correction value |Remarks|
|---|---:|---|
|m20|-2.0f||
|m17|-1.7f||
|m13|-1.0f||
|m07|-0.7f||
|m03|-0.3f||
|zero|0.0f||
|p03|0.3f||
|p07|0.7f||
|p13|1.0f||
|p17|1.7f||
|p20|2.0f||

* Exposure delay setting: `setExposureDelay(delay:ThetaRepository.ExposureDelayEnum)`

Delay between the startCapture command and the start of exposure (= self-timer).

| Value|Delay time (seconds) |Remarks|
|---|---:|---|
|delayOff|0||
|delay1|1||
|delay2|2||
|delay3|3||
|delay4|4||
|delay5|5||
|delay6|6||
|delay7|7||
|delay8|8||
|delay9|9||
|delay10|10||

* Exposure program: `setExposureProgram(program:ThetaRepository.ExposureProgramEnum)`

| Value|Content |Remarks|
|---|---|---|
|manual|Manual||
|normalProgram|Regular programs||
|aperturePriority|Aperture priority||
|shutterPriority|Shutter priority||
|isoPriority|ISO priority||

* File format: `setFileFormat(fileFormat:ThetaRepository.VideoFileFormatEnum)`

| Value|Type| Width|Height |Frame rate | Codec|S|SC|SC2|V|Z1|X|
|---|---|--:|--:|--:|--|:-:|:-:|:-:|:-:|:-:|:-:|
|videoHd|mp4|1280|570|||+|+|-|-|-|-|
|videoFullHd|mp4|1920|1080|||+|+|-|-|-|-|
|video2k|mp4|1920|960||H.264/MPEG-4 AVC|-|-|+|+|+|-|
|video4k|mp4|3840|1920||H.264/MPEG-4 AVC|-|-|+|+|+|-|
|video2k30f|mp4|1920|960|30|H.264/MPEG-4 AVC|-|-|-|-|-|+|
|video2k60f|mp4|1920|960|60|H.264/MPEG-4 AVC|-|-|-|-|-|+|
|video4k30f|mp4|3840|1920|30|H.264/MPEG-4 AVC|-|-|-|-|-|+|
|video4k60f|mp4|3840|1920|60|H.264/MPEG-4 AVC|-|-|-|-|-|+|
|video57k2f|mp4|5760|2880|2|H.264/MPEG-4 AVC|-|-|-|-|-|+|
|video57k5f|mp4|5760|2880|5|H.264/MPEG-4 AVC|-|-|-|-|-|+|
|video57k30f|mp4|5760|2880|30|H.264/MPEG-4 AVC|-|-|-|-|-|+|
|video7k2f|mp4|7680|3840|2|H.264/MPEG-4 AVC|-|-|-|-|-|+|
|video7k5f|mp4|7680|3840|5|H.264/MPEG-4 AVC|-|-|-|-|-|+|
|video7k10f|mp4|7680|3840|10|H.264/MPEG-4 AVC|-|-|-|-|-|+|

* Maximum recording time setting: `setMaxRecordableTime(time:ThetaRepository.MaxRecordeTimeEnum)`

| Value|Content |Remarks
|---|---|---|
|recordableTime300|300 seconds||
|recordableTime1500|1500 seconds||

* GPS on/off: `setGpsTagRecording(value:ThetaRepository.GpsTagRecordingEnum)`

Other than THETA X, this setting is ignored.

| Value|Content |Remarks|
|---|---|---|
|on|GPS | |
|off|No GPS||

* ISO value: `setIso(iso:ThetaRepository.IsoEnum)`

| Value|ISO value |Remarks|
|---|---:|---|
|isoAuto|0||
|iso50|50|THETA X only|
|iso64|64|THETA SC2, V and X only|
|iso80|80|THETA S and SC don't support|
|iso100|100||
|iso125|125||
|iso160|160||
|iso200|200||
|iso250|250||
|iso320|320||
|iso400|400||
|iso500|150||
|iso640|640||
|iso800|800||
|iso1000|1000||
|iso1250|1250||
|iso1600|1600||
|iso2000|2000|THETA S and SC don't support|
|iso2500|2500|THETA S and SC don't support|
|iso3200|3200|THETA S and SC don't support|
|iso4000|4000|THETA V and Z1 only|
|iso5000|5000|THETA V and Z1 only|
|iso6400|6400|THETA V and Z1 only|


* ISO upper limit: `setIsoAutoHighLimit(iso:ThetaRepository.IsoAutoHighLimitEnum)`

THETA V Firmware v2.50.1 or earlier is ignored even if specified in THETA S or THETA SC.

| Value|Upper ISO limit|Remarks|
|---|---:|---|
|iso100|100|THETA X only|
|iso125|125|THETA X only|
|iso160|160|THETA X only|
|iso200|200||
|iso250|250||
|iso320|320||
|iso400|400||
|iso500|150||
|iso640|640||
|iso800|800||
|iso1000|1000||
|iso1250|1250||
|iso1600|1600||
|iso2000|2000||
|iso2500|2500||
|iso3200|3200||
|iso4000|4000|THETA V and Z1 only|
|iso5000|5000|THETA V and Z1 only|
|iso6400|6400|THETA V and Z1 only|


* Aperture setting: `setAperture(aperture:ThetaRepository.ApertureEnum)`

| Value|Setting value |Remarks|
|---|---:|---|
|apertureAuto|Automatic||
|aperture20|2.0f|THETA V or prior only|
|aperture21|2.1f|THETA Z1 only|
|aperture24|2.4f|THETA X only|
|aperture35|3.5f|THETA Z1 only|
|aperture56|5.6f|THETA Z1 only|

* Color temperature setting: `setColorTemperature(kelvin:Int)`
  * 2500 to 10000

* GPS information: `setGpsInfo(gpsInfo:ThetaRepository.GpsInfo)`

GpsInfo shall be prepared as follows:
`ThetaRepository.GpsInfo(latitude:longitude:altitude:dateTimeZone:)`

| Value|Setting value |Remarks|
|---|---|---|
|latitude|Latitude | -90 to 90 or 65535 (disabled)|
|longitude|Longitude | -180 to 180 or 65535 (disabled)|
|altitude|Altitude |||
|dateTimeZone|Date and time|YYYY:MM:DD hh:mm:ss+(-)hh:mm or empty string (disabled)|

* White balance: `setWhiteBalance(whiteBalance:ThetaRepository.WhiteBalanceEnum)`

| Value|Setting value |Remarks|
|---|---|---|
|auto|Automatic||
|daylight|Outdoor|About 5,200|
|shade|Shade|About 7,000|
|cloudyDaylight|Cloudy|About 6,000|
|incandescent|Incandescent light 1|About 3,200|
|warmWhiteFluorescent|Incandescent light 2||
|daylightFluorescent|Fluorescent light 1(daylight)||
|daywhiteFluorescent|Fluorescent light 2(natural white)||
|fluorescent|Fluorescent light 3 (white)|About 4,000|
|bulbFluorescent|Fluorescent light 4 (light bulb color)||
|colorTemperature|CT settings (specified by the colorTemperature option)|RICOH THETA S firmware v01.82 or later and RICOH THETA SC firmware v01.10 or later|
|underwater|Underwater|RICOH THETA V firmware v3.21.1 or later|

## <a id="preview"></a>Display a preview

The preview is an equirectangular images of motion JPEG format.

| Model | Width (pixel) | Height (pixel) |Frame rate (fps) | Remarks |
| ---- | --: |--: | -----------: |---- |
| THETA X | 1024 |512 | 30 ||
| THETA Z1 |1024 | 512 |30 | |
| THETA V |1024 | 512 |30 | Firmware v2.21.1 or later |
| THETA V |1024 | 512 |8 | Firmware v2.20.1 or earlier |
| THETA SC2 |1024 | 512 |30 | |
| THETA S |640 | 320 |10 | |
| THETA SC |640 | 320 |10 | |

When we call `getLivePreview(frameHandler:completionHandler:)` then the callback function is called each time each frame in the preview is received.
The arguments to the callback function are passed to the objects [`Ktor_ByteReadPacket`](https://api.ktor.io/ktor-io/io.kitir.utils.io/-byte-read-packet/index.html) and to the callback function that returns the result.

To extract frame data (JPEG images) from `Ktor_ioByteReadPacket` objects, use `PlatformKt.frameFrom(byteReadPacket:)`.
The return value of this function can be decoded and displayed using a `Data` object (jpeg data) `UIImage(data:)`.
Set `true` to the result of processing in the frame Handler callback function to continue preview, and set `false` to the end of preview.

If CPU usage is high and preview is continued for a long time, the system may detect the load and terminate the application.
In such a case, adjust the frame rate (approximately 10 fps) so that the frames are discarded before extracting `Data` so as not to place a load on the CPU.
An example of receiving a frame is shown below.

``` swift
class FrameHandler: KotlinSuspendFunction1 {
  static let FrameInterval = CFTimeInterval(1.0/10.0)
  var last: CFTimeInterval = 0
  let handler: (_ frame: Data) -> Bool

  init(_ handler: @escaping (_ frame: Data) -> Bool) {
    self.handler = handler
  }

  func invoke(p1: Any?, completionHandler: @escaping (_ response: Any?, _ error: Error?) -> Void) {
    let now = CACurrentMediaTime()
    if (now - last < Self.FrameInterval) {
      // drop frame
      completionHandler(true, nil)
      return
    }
    autoreleasepool {
      // extract jpeg data from ByteReadPacket
      let data = PlatformKt.frameFrom(
        byteReadPacket: p1 as! Ktor_ioByteReadPacket
      )
      // draw frame and set result
      completionHandler(handler(data), nil)
    }
  }
}

theta.getLivePreview(
  frameHandler: FrameHandler {data in
    if (isActive) {
      drawFrame(UIImage(data: data))
      return true
    } else {
      return false
    }
  },
  completionHandler: {error in
    // handle error
  }
)
```

## Set the camera

To configure the camera, set to `ThetaRepository.Options` and call `ThetaRepository.SetOptions (options)`.

```swift
Task {
  do {
    var options = Options()
    options.aperture = ThetaRepository.ApertureEnum.apertureAuto
    let _:Bool = try await withCheckedThrowingContinuation {continuation in
      theta.setOptions(options:options) {error in
      if let thetaError = error {
        continuation.resume(throwing: thetaError)
      } else {
        continuation.resume(returning: true)
      }
   }
 } catch {
   // handle error
 }
}
```

Refer to the table below for the items and contents that can be set to "Options".

* Date time: `dateTimeZone:String`

Format: YYYY:MM:DD hh:mm:ss+(-)hh:mm

hh is  0 to 23, +(-)hh:mm are time zones.

Example: 2014:05:18 01:04:29+08:00

* Exposure Compensation: `exposureCompensation:ThetaRepository.ExposureCompensationEnum`

| Value| Correction value | Remarks|
|---|---:|---|
|m20|-2.0f||
|m17|-1.7f||
|m13|-1.0f||
|m07|-0.7f||
|m03|-0.3f||
|zero| 0.0f||
|p03|0.3f||
|p07|0.7f||
|p13|1.0f||
|p17|1.7f||
|p20|2.0f||

* Exposure Delay Setting: `ExposureDelay:ThetaRepository.ExposureDelayEnum`

Delay between the startCapture command and the start of exposure (= self-timer).

| Value| Delay time (seconds) | Remarks|
|---|---:|---|
|delayOff|0||
|delay1|1||
|delay2|2||
|delay3|3||
|delay4|4||
|delay5|5||
|delay6|6||
|delay7|7||
|delay8|8||
|delay9|9||
|delay10|10||

* Exposure Program: `ExposureProgram:ThetaRepository.ExposureProgramEnum`

| Value| Content | Remarks|
|---|---|---|
|manual| Manual||
|normalProgram| Regular programs||
|aperturePriority| Aperture priority||
|shutterPriority| Shutter priority||
|isoPriority| ISO priority||

* File Format: `FileFormat:ThetaRepository.FileFormatEnum`

| Value| Type| Width| Height | Frame rate | Codec| S| SC| SC2|V| Z1| X|
|---|---|--:|--:|--:|--|:-:|:-:|:-:|:-:|:-:|:-:|
|videoHd|mp4|1280|570|||+|+|-|-|-|-|
|videoFullHd|mp4|1920|1080|||+|+|-|-|-|-|
|video2k|mp4|1920|960||H.264/MPEG-4 AVC|-|-|+|+|+|-|
|video4k|mp4|3840|1920||H.264/MPEG-4 AVC|-|-|+|+|+|-|
|video2k30f|mp4|1920|960|30|H.264/MPEG-4 AVC|-|-|-|-|-|+|
|video2k60f|mp4|1920|960|60|H.264/MPEG-4 AVC|-|-|-|-|-|+|
|video4k30f|mp4|3840|1920|30|H.264/MPEG-4 AVC|-|-|-|-|-|+|
|video4k60f|mp4|3840|1920|60|H.264/MPEG-4 AVC|-|-|-|-|-|+|
|video57k2f|mp4|5760|2880|2|H.264/MPEG-4 AVC|-|-|-|-|-|+|
|video57k5f|mp4|5760|2880|5|H.264/MPEG-4 AVC|-|-|-|-|-|+|
|video57k30f|mp4|5760|2880|30|H.264/MPEG-4 AVC|-|-|-|-|-|+|
|video7k2f|mp4|7680|3840|2|H.264/MPEG-4 AVC|-|-|-|-|-|+|
|video7k5f|mp4|7680|3840|5|H.264/MPEG-4 AVC|-|-|-|-|-|+|
|video7k10f|mp4|7680|3840|10|H.264/MPEG-4 AVC|-|-|-|-|-|+|

* Image Processing: `filter:ThetaRepository.Filter`

| Value| Content | Remarks|
|---|---|---|
|off| None|||
|noiseReduction| Noise reduction||
|hdr| HDR| |

* GPS ON/OFF: `isGpsOn:Bool`

Other than THETA X, it is ignored.

| Value| Content | Remarks|
|---|---|---|
|true| GPS | |
|false| No GPS||

* ISO value: `iso:ThetaRepository.IsoEnum`

| Value| ISO value | Remarks|
|---|---:|---|
|isoAuto|0||
|iso50|50||
|iso64|64||
|iso80|80||
|iso100|100||
|iso125|125||
|iso160|160||
|iso200|200||
|iso250|250||
|iso320|320||
|iso400|400||
|iso500|150||
|iso640|640||
|iso800|800||
|iso1000|1000||
|iso1250|1250||
|iso1600|1600||
|iso2000|2000||
|iso2500|2500||
|iso3200|3200||
|iso4000|4000||
|iso5000|5000||
|iso6400|6400||

* ISO ceiling: `isoAutoHighLimit:ThetaRepository.IsoAutoHighLimitEnum`

THETA V Firmware v2.50.1 or earlier is ignored even if specified in THETA S or THETA SC.

| Value| Upper ISO limit | Remarks|
|---|---:|---|
|iso100|100||
|iso125|125||
|iso160|160||
|iso200|200||
|iso250|250||
|iso320|320||
|iso400|400||
|iso500|150||
|iso640|640||
|iso800|800||
|iso1000|1000||
|iso1250|1250||
|iso1600|1600||
|iso2000|2000||
|iso2500|2500||
|iso3200|3200||
|iso4000|4000||
|iso5000|5000||
|iso6400|6400||

* Aperture: `ThetaRepository.ApertureEnum`

| Value| Setting value | Remarks|
|---|---:|---|
|apertureAuto| Automatic||
|aperture20|2.0f||
|aperture21|2.1f||
|aperture24|2.4f||
|aperture35|3.5f||
|aperture56|5.6f||

* Color Temperature Setting: `colorTemperature:Int`
  * 2500 to 10000

* GPS Information: `gpsInfo:ThetaRepository.GpsInfo`

GpsInfo shall be prepared as follows:
`ThetaRepository.GpsInfo(latitude:longitude:altitude:dateTimeZone:)`

| Value|Setting value |Remarks|
|---|---|---|
|latitude|Latitude | -90 to 90 or 65535 (disabled)|
|longitude|Longitude | -180 to 180 or 65535 (disabled)|
|altitude|Altitude |||
|dateTimeZone|Date and time|YYYY:MM:DD hh:mm:ss+(-)hh:mm or empty string (disabled)|

* White Balance: `whiteBalance:ThetaRepository.WhiteBalanceEnum`

| Value| Setting value | Remarks|
|---|---|---|
|auto| Automatic||
|daylight| Outdoor| About 5,200|
|shade| Shade| About 7,000|
|cloudyDaylight| Cloudy| About 6,000|
|incandescent| Incandescent light 1| About 3,200|
|warmWhiteFluorescent|Incandescent light 2||
|daylightFluorescent|Fluorescent light 1 (daylight)||
|daywhiteFluorescent|Fluorescent light 2 (natural white)||
|fluorescent| Fluorescent light 3 (white)| About 4,000|
|bulbFluorescent|Fluorescent light 4 (light bulb color)||
|colorTemperature|CT settings (specified by the colorTemperature option)|RICOH THETA S firmware v01.82 or later and RICOH THETA SC firmware v01.10 or later|
|underwater|Underwater|RICOH THETA V firmware v3.21.1 or later|

* Maximum Recording Time Setting: `maxRecordableTime:ThetaRepository.MaxRecorderTimeEnum`

| Value| Content | Remarks|
|---|---|---|
|recordableTime300| 300 seconds||
|recordableTime1500| 1500 seconds||

* Capture mode: `captureMode:ThetaRepository.CaptureModeEnum`

| Value| Content | Remarks|
|---|---|---|
|image| Still image shooting mode||
|video| Video shooting mode||

* Language Settings: `language:ThetaRepository.LanguageEnum`

Ignored even if specified in THETA S or THETA SC.

|Value|Content|Remarks|
|--|---|---|
|enUs| U.S. English||
|enGb| English||
|ja| Japanese |||
|fr| French |||
|de| German |||
|zhTw| Chinese Taiwan |||
|zhCn| Chinese |||
|it| Italian||
|ko| Korean |||

* Time to enter sleep mode (minutes): `sleepDelay:ThetaRepository.SleepDelayEnum/SleepDelaySec`

  * ThetaRepository.SleepDelayEnum
  
|Value|Set value (minutes)| Remarks|
|--|--:|---|
|disable| 0| Not automatically turned off|
|sleepDelay3m|3||
|sleepDelay5m|5||
|sleepDelay7m|7||
|sleepDelay10m|10||

  * ThetaRepository.SleepDelaySec(sec:Int)
    * 0 to 1800 (seconds)

* Time from sleep to auto power off (minutes): `OffDelay:ThetaRepository.OffDelayEnum/OffDelaySec`

  * ThetaRepository.OffDelayEnum

|Value|Set value (minutes)| Remarks|
|--|--:|---|
|disable| 0| Not automatically turned off|
|offDelay5m|5||
|offDelay10m|10||
|offDelay15m|15||
|offDelay30m|30||

  * ThetaRepository.OffDelaySec(sec:Int)
    * 0 to 1800 (seconds)

* Shutter sound: `shutterVolume:Int`
  * 0 - 100

## Acquire camera settings

To get the camera settings, specify the list of options you want to obtain and call `ThetaRepository.getOptions(OptionNames:)`.

```swift
Task {
  do {
    var optionNames: [ThetaRepository.OptionNameEnum] = [
      ThetaRepository.OptionNameEnum.aperture,
      ThetaRepository.OptionNameEnum.capturemode,
      ThetaRepository.OptionNameEnum.colortemperature
    ]
    let options:Options = try await withCheckedThrowingContinuation {continuation in
      theta.getOptions(optionNames:names, completionHandler: {resp, error in
      if let response = resp {
        continuation.resume(returning: response)
      }
      if let thetaError = error {
        continuation.resume(throwing: thetaError)
      }
   }
 } catch {
   // handle error
 }
}
```

See the table below for the `ThetaRepository.OptionNameEnum`:

| Value| Meaning | Type |
|----|-----|--|
|aperture| aperture|ThetaRepository.ApertureEnum|
|capturemode| capture mode | ThetaRepository.CaptureModeEnum|
|colortemperature| colortemperature |Int|
|datetimezone| date and time|String|
|exposurecompensation| exposure compensation |ThetaRepository.ExposureCompensationEnum|
|exposuredelay| exposure delay time|ThetaRepository.ExposureDelayEnum|
|exposureprogram| exposure program|ThetaRepository.ExposureProgram|
|fileformat| file format|ThetaRepository.FileFormatEnum|
|filter| image filter | ThetaRepository.FilterEnum|
|gpsinfo| GPS information | ThetaRepository.GpsInfo|
|isgpson| GPS flag | Bool|
|iso| ISO value |ThetaRepository.IsoEnum|
|isoautohighlimit| ISO upper limit|ThetaRepository.IsoAutoHighLimitEnum|
|language| language |ThetaRepository.LanguageEnum|
|maxrecordabletime| maximum recording time|ThetaRepository.MaxRecordableTimeEnum|
|offdelay| power off time|ThetaRepository.Off DelayEnum,OffDelaySec|
|sleepdelay| sleep time | ThetaRepository.SleepDelayEnum,SleepDelaySec|
|remainingpictures| number of remaining images |Int|
|remainingvideoseconds| remaining recording seconds|Int|
|remainingspace| remaining area |Long|
|totalspace| total area |Long|
|shuttervolume| shutter volume |Int|
|whitebalance| white balance|ThetaRepository.WhiteBalanceEnum|

## List still images and videos in THETA

You can obtain the list of still images (JPEG files) and videos (MP4 files) in THETA using the `ThetaRepository.listFiles(fileType:startPosition:entryCount:)`.
An item of the list is `ThetaRepository.FileInfo`. 

The `fileType` is the `ThetaRepository.FileTypeEnum` type, whose contents are as follows:

* ThetaRepository.FileTypeEnum

|Value|Content|
|---|---|
|image| List of still images (JPEG files)|
|video| List of videos (MP4 files)|
|all| List all files|

* ThetaRepository.FileInfo

|Property name|Type|Contents|
|---|---|---|
|name| String| Represents the file name|
|size| Long| Indicates the file size (in bytes)|
|dateTime| String| Shooting date and time (YYYY:MM:DD HH:MM:SS)|
|fileUrl| String| Represents the URL of the file|
|thumbnailUrl| String| Represents a thumbnail URL|

``` swift
Task {
  do {
    let listFiles: [ThetaRepository.FileInfo] =
      try await withCheckedThrowingContinuation {continuation in
        theta.listFiles(fileType:ThetaRepository.FileTypeEnum.image,
                        startPosition: 0,
                        entryCount: 100) {response, error in
          if let listFiles = response {
            continuation.resume(returning: listFiles)
          }
          if let thetaError = error {
            continuation.resume(throwing: thetaError)
          }
        }
      }
    // handle file info
  } catch {
    // catch thetaError
  }
}
```

## Download still images and videos

Use `URLSession` to retrieve data from the URL of still images (JPEG file) or videos (MP4 file) shot. You can download it as `Data` using the following functions.

``` swift
func download(url: String) async throws -> Data {
  return try await withCheckedThrowingContinuation {continuation in
    URLSession.shared.dataTask(with: URL(string: url)!) {data, _, error in
      if let receiveData = data {
        continuation.resume(returning: receiveData)
      }
      if let downloadError = error {
        continuation.resume(throwing: downloadError)
      }
    }.resume()
  }
}

Task {
  do {
    let jpeg: Data = try await download(url: "http://.../1234.jpg")
    drawImage(UIImage(data: jpeg)
  } catch {
    // handle download error
  }
}
```

## Obtain thumbnails

The thumbnails of the files in THETA can be downloaded using the `thumbnailUrl` of `ThetaRepository.FileInfo`.

```
Task {
  do {
    let jpeg: Data = try await download(url: fileInfo.thumbnailUrl)
    drawImage(UIImage(data: jpeg)
  } catch {
    // handle download error
  }
}
```

## Reset THETA

To reset the connected THETA, call `ThetaRepository.reset(completionHandler:)`.

``` swift
Task {
  do {
    let _:Bool = try await withCheckedThrowingContinuation {continuation in
      theta.reset(completionHandler:{error in
        if let thetaError = error {
          continuation.resume(throwing: thetaError)
        } else {
          continuation.resume(returning: true)
        }
      })
    }
  } catch {
    // handle error
  }
}
```

## Get the THETA information

To obtain the information about the connected THETA, call `ThetaRepository.getThetaInfo(completionHandler:)`.
Successful calls allow you to obtain `ThetaRepository.ThetaInfo`.
This data contains the following information:

* ThetaRepository.ThetaInfo

|Property name|Type|Contents|
|---|---|---|
|firmwareVersion|String|Represents the firmware version|
|hasGps|Bool|Indicates whether you have a GPS function.|
|hasGyro|Bool|Indicates whether you have a gyro.|
|model|String|Indicates the model number of THETA.|
|serialNumber|String|Represents the THETA serial number|
|uptime|Integer|This means the number of seconds since the THETA power was turned on.|

``` swift
Task {
  do {
    let thetaInfo: ThetaRepository.ThetaInfo = try await withCheckedThrowingContinuation {continuation in
      theta.getThetaInfo {response, error in
        if let thetaInfo = response {
          continuation.resume(returning: thetaInfo)
        }
        if let thetaError = error {
          continuation.resume(throwing: thetaError)
        }
      }
    }
    // handle thetaInfo
  } catch {
    // handle thetaError
  }
}
```

## Get the state of THETA

To get the state of the connected THETA, call `ThetaRepository.getThetaState(completionHandler:)`.
Successful calling allows you to get `ThetaRepository.ThetaState`.
This data contains the following information:

* ThetaRepository.ThetaState

|Property name|Type|Contents|
|---|---|---|
|batteryLevel|Float|Battery level |
|chargingState|ThetaRepository.ChargingStateEnum|Indicates charging status|
|fingerprint|String|Indicates a unique identifier for each current state|
|isSdCard|Bool|Indicates whether an SD card exists.|
|latestFileUrl|String|Represents the URL of the last acquired media|
|recordableTime|Int32_t|Indicates the number of recordable seconds.|
|recordedTime|Int32_t|Indicates the number of recorded seconds|

* ThetaRepository.ChargingStateEnum

|Property name|Details|
|---|---|
|charging|Indicates charging in progress.|
|charged|Indicates completion of charging.|
|notCharging|Indicates disconnection of the cable.|

``` swift
Task {
  do {
    let thetaState: ThetaRepository.ThetaState = try await withCheckedThrowingContinuation {continuation in
      theta.getThetaState {response, error in
        if let thetaState = response {
          continuation.resume(returning: thetaState)
        }
        if let thetaError = error {
          continuation.resume(throwing: thetaError)
        }
      }
    }
    // handle thetaState
  } catch {
    // handle thetaError
  }
}
```
