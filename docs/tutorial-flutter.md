# THETA Client Tutorial for Flutter

## Create a Flutter Project

The supported platform is iOS and Android, and the language is kotlin and swift, respectively, to create a project.

``` Terminal
flutter create --platforms=android,ios -i swift -a kotlin your_app_name
```

## Project setup

### Flutter plugin package build for THETA Client

Run `theta-client/scripts` in `build_flutter_build.sh` to build the Flutter plugin package for the THETA Client.

### Copy of the THETA client

Copy to the project that created the Flutter plugin package for THETA Client.

`demo-flutter` is placed in `demo-flutter/packages/theta_client_flutter` under the project.
(`build_flutter_build.sh` does not require this operation).

### Flutter plug-in setting

Added `theta_client_flutter` copied to `dependencies` of `pubspec.yaml`.

``` pubspec.yaml
dependencies:
  flutter:
    sdk: flutter
  theta_client_flutter:
    path: ./packages/theta_client_flutter
```
### Android setting

* Describe library settings to `android/app/build.grade`.

``` build.gradle
    dependencies {
        ...
        implementation files('../../packages/theta_client_flutter/android/aar/theta-client-debug.aar')
        ...
    }
```

* Set the minimum SDK version to 26 or higher

``` build.gradle
    MinSdkVersion 26
```
### iOS setting

Set iOS Deployment Target to 15 or higher

## Advance preparation

Connect the wireless LAN between THETA and the smartphone that runs on the application using this SDK.

## Initialize THETA Client

``` Dart
import 'package:theta_client_flutter/theta_client_flutter.dart';

final _thetaClientFlutterPlugin = ThetaClientFlutter();

_thetaClientFlutterPlugin.initialize()
  .then((value) {
    // success
  })
  .onError((error, stackTrace) {
    // handle error
  });

OR

_thetaClientFlutterPlugin.initialize('http://<IP address>:<port number>')
  .then((value) {
    // success
  })
  .onError((error, stackTrace) {
    // handle error
  });
```
* THETA IP ADDRESS

| Mode |Address |
|-------|---------|
|Direct mode|192.168.1.1|
|Other| Camera IP address|

* When downloading images or videos from THETA, the connection is plain, so the settings for each platform are required depending on the destination address (default 192.168.1.1).

	* iOS: shows an example of Info.plist by default.
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

* Android: Shows an example of the Res/xml/network_security_config.xml.

```xml
    <?xml version="1.0" encoding="utf-8"?>
    <network-security-config>
      <base-config cleartextTrafficPermitted="false" />
      <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="false">192.168.1.1</domain>
      </domain-config>
    </network-security-config>
```

## Shoot still images

First, shooting settings are performed using `getPhotoCaptureBuilder()` to create `PhotoCapture` objects.

``` Dart
_thetaClientFlutterPlugin.getPhotoCaptureBuilder()
  .setIsoAutoHighLimit(IsoAutoHighLimitEnum.iso1000)
  .setFileFormat(PhotoFileFormatEnum.image_5K)
  .build()
  .then((photoCapture) {
    // success build photoCapture
  })
  .onError((error, stackTrace) {
    // handle error
  });
```
The above example sets the maximum ISO sensitivity to 1000 and the file format to IMAGE_5K.

See [DIsplay a preview](#preview) for instructions on how to view preview.

Next, we call `PhotoCapture.takePicture()` to shoot still pictures.

``` Dart
    photoCapture.takePicture((fileUrl) { 
      // send HTTP GET request for fileUrl and receive JPEG file
    }, (exception) {
      // catch error while take picture
    });
});});
```

## Shoot a video

First, you set up shooting using `getVideoCaptureBuilder()` and create `VideoCapture` objects.

``` Dart
_thetaClientFlutterPlugin.getVideoCaptureBuilder()
  .setIsoAutoHighLimit(IsoAutoHighLimitEnum.iso800)
  .setFileFormat(VideoFileFormatEnum.videoHD)
  .build()
  .then((videoCapture) {
    // success build videoCapture
  })
  .onError((error, stackTrace) {
    // handle error
  });
```

In the example above, the maximum ISO sensitivity is set to 800 and the file format is set to `videoHD`.

See [Display a preview](#preview) for instructions on how to view preview.

Next, we call `VideoCapture.startCapture()` to start recording videos.


``` Dart
VideoCapturing videoCapturing = videoCapture.startCapture()
  .then(fileUrl => {
    // GETリクエストを送信してMP4ファイルを受け取る処理
  })
  .catch(error => {
    // handle error
  });
```

Next, call `VideoCapture.stopCapture()` to finish recording the video.
If successful, then the URL of the shot file is called then as shown above.

``` Dart
VideoCapturing.stopCapture();
```

## <a id="preview"></a>Display a preview

The preview is an equirectangular form of motion JPEG.
`getLivePreview()` calls the callback function every time each frame in the preview is received.
The arguments to the callback function are passed to the memory image of the frame data (JPEG).
To exit the preview, return `false` as the return value of the callback function.

``` Dart
  bool previewing = false;

  bool frameHandler(Uint8List frameData) {

    // frameData is JPEG image data.

    // Return false to preview done.
    return previewing;
  }

  void startLivePreview() {
    previewing = true;
    _thetaClientFlutterPlugin.getLivePreview(frameHandler)
      .then((value) {
        // preview done
      })
      .onError((error, stackTrace) {
        // handle error
      });
  }

  @override
  Widget build(BuildContext context) {
    ... Omitted
        child: Image.memory(
          frameData,
          errorBuilder: (a, b, c) {
            return Container(
              color: Colors.black,
            );
          },
          gaplessPlayback: true,
        ),
    ... Omitted
```

## Set the camera

To configure the camera, set the desired settings to `Options` and call `setOptions()`.


``` Dart
final options = Options();
options.aperture = ApertureEnum.apertureAuto;
_thetaClientFlutterPlugin.setOptions(options)
  .then((value) {
    // done
  })
  .onError((error, stackTrace) {
    // handle error
  });
```

## Acquire camera settings

To get the camera settings, specify the list of options you want to obtain and call `getOptions()`.

``` Dart
final optionNames = [
  OptionNameEnum.aperture,
  OptionNameEnum.captureMode,
  OptionNameEnum.colorTemperature,
];
_thetaClientFlutterPlugin.getOptions(optionNames)
  .then((options) {
    // handle options
  })
  .onError((error, stackTrace) {
    // handle error
  });
  ```

## List still images and videos in THETA

The list of still pictures (JPEG file) and videos (MP4 file) in THETA can be obtained using `listFiles(FileTypeEnum filetype, int entryCount, [int startPosition])`.
The item of the list is a `FileInfo`.

``` Dart
_thetaClientFlutterPlugin.listFiles(FileTypeEnum.image, 1000, 0)
  .then((files) {
    // handle file list
  })
  .onError((error, stackTrace) {
    // handle error
  });
```

## Download still images and videos

You can retrieve data from the URL of still pictures (JPEG file) or videos (MP4 file) shot.
You can download using the http package by using the following functions:
For still images, the image can be downloaded and displayed using the Image Widget network constructor.

* Http package: https://pub.dev/packages/http

``` Dart
void downloadFile(String fileUrl, String filePath) async {
  final url = Uri.parse(fileUrl);
  final response = await get(url);
  final file = File(filePath);
  await file.create();
  await file.writeAsBytes(response.bodyBytes);
}
```

## Obtain thumbnails

Thumbnails of the files in THETA can be downloaded using the `FileInfo.thumbnailUrl` acquired by `listFiles()` as follows:

``` Dart
  final url = Uri.parse(fileInfo.thumbnailUrl);
  final response = await get(url);
  final file = File(filePath);
  await file.create();
  await file.writeAsBytes(response.bodyBytes);
```

## <a id="preview"></a>Display a preview

The preview is an equirectangular images of motion JPEG format.

As an argument to a callback function that performs each frame processing
Call `getLivePreview()`.
A previewing frame can be received as an event parameter.

``` Dart
  bool previewing = false;

  bool frameHandler(Uint8List frameData) {

    // frameData is JPEG image data.

    // Return false to preview done.
    return previewing;
  }

  void startLivePreview() {
    previewing = true;
    _thetaClientFlutterPlugin.getLivePreview(frameHandler)
      .then((value) {
        // preview done
      })
      .onError((error, stackTrace) {
        // handle error
      });
  }

  @override
  Widget build(BuildContext context) {
    ... Omitted
        child: Image.memory(
          frameData,
          errorBuilder: (a, b, c) {
            return Container(
              color: Colors.black,
            );
          },
          gaplessPlayback: true,
        ),
    ... Omitted
```

## Get the THETA information

To obtain the information about the connected THETA, call `getThetaInfo()`.
If the call is successful, you can get `ThetaInfo`.

``` Dart
_thetaClientFlutterPlugin.getThetaInfo()
  .then((thetaInfo) {
    // processing thetaInfo
  })
  .onError((error, stackTrace) {
    // handle error
  });
```

## Get the state of THETA

To get the state of the connected THETA, call `getThetaState()`.
Successful calling allows you to acquire the `ThetaState`.

``` Dart
_thetaClientFlutterPlugin.getThetaState()
  .then((thetaState) {
    // processing thetaState
  })
  .onError((error, stackTrace) {
    // handle error
  });
```

## Reset THETA

Call `reset()` to reset the connected THETA.

``` Dart
_thetaClientFlutterPlugin.reset()
  .then((value) {
    // reset done
  })
  .onError((error, stackTrace) {
    // handle error
  });
  ```
  
