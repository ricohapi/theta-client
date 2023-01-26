# Flutter plugin for THETA client

## Prerequisite
You have to build library of THETA client and copy it using `scripts/build_flutter_build.sh`.

```
theta-client$ ./scripts/build_flutter_build.sh
```

## Sample application
Move to `theta-client/flutter/example` then execute sample application.

```
theta-client$ cd ./flutter/example
example$ flutter run
```

## Switching Debug/Release of the library
You have to change setting files. After changing, execute `flutter clean`.

```
example$ flutter clean
```

### Settings on Android

Change `.aar` file name both of Theta client and sample application.

Theta client: `theta-client/flutter/android/build.gradle`

```
    compileOnly files('aar/theta-client-debug.aar')
    // compileOnly files('aar/theta-client-release.aar')
```

Sample application: `theta-client/flutter/example/android/app/build.gradle`

```
    implementation files('../../../android/aar/theta-client-debug.aar')
    // implementation files('../../../android/aar/theta-client-release.aar')
```

### Settings on iOS

Change `s.vendored_frameworks` of `theta_client.podspec`.

`theta-client/flutter/ios/theta_client.podspec`:

```
  s.vendored_frameworks = ['frameworks/debug/*.xcframework']
  # s.vendored_frameworks = ['frameworks/release/*.xcframework']
```
