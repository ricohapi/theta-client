# THETA Client

This library provides a way to control RICOH THETA using [RICOH THETA API v2.1](https://github.com/ricohapi/theta-api-specs/tree/main/theta-web-api-v2.1).
Your app can perform the following actions:
* Take a photo and video
* Acquire a list of photos and videos
* Acquire a JPEG file and MP4 file
* Acquire the status of THETA
* Acquire and set properties of THETA

## Supported Environments
* Android native (Kotlin)
* iOS native (Swift)
* React Native
* Flutter

## Supported Models
* THETA X
* THETA Z1
* THETA V
* THETA SC
* THETA S (firmware version 01.62 or later)

## Directory Structure
* theta-client
  * demos: Demo applications
  * docs: Documentation
  * kotlin-multiplatform: Library body ([Kotlin Multiplatform Mobile](https://kotlinlang.org/docs/multiplatform-mobile-getting-started.html))
  * react-native: React Native package
  * flutter: Flutter plugin

## Build

### Android (aar)
```
theta-client$ ./gradlew publishToMavenLocal
```

aar is output to `theta-client/kotlin-multiplatform/build/outputs/aar`

### iOS (XCFramework)
```
theta-client$ ./gradlew podPublishXCFramework
```

XCFramework is output to `theta-client/kotlin-multiplatform/build/cocoapods/publish`

### ReactNative & Flutter
See README in each directory.(`react-native`, `flutter`)

### Test
```
theta-client$ ./gradlew testReleaseUnitTest
```

## How to Use
See tutorial in `docs` directory.


## License

[MIT License](LICENSE)
