# theta_client_flutter

Flutter plugin for THETA client sdk

## ビルドとライブラリのコピー
ライブラリのビルドとコピーが必要となる。
`theta-client/scripts`の`build_flutter_build.sh`でビルドとコピーを実施できる。

```
theta-client$ ./scripts/build_flutter_build.sh
```

## サンプルアプリの実行
`theta-client/demos/demo-flutter`に移動して、サンプルアプリの実行する。

```
theta-client$ cd ./demos/demo-flutter
demo-flutter$ flutter run
```

## ライブラリのDebug/Release切り替え
ライブラリのDebug/Releaseの切り替えは、設定ファイルを変更する必要がある。
設定ファイルを変更した後は、`flutter clean`の実行が必要となる。

```
demo-flutter$ flutter clean
```

### Androidの設定
Androidでは、Flutter pluginとサンプルアプリの両方の`build.gradle`の`.aar`の参照を変更する。

ライブラリ側: `theta-client/flutter/android/build.gradle`

```
    compileOnly files('aar/theta-client-debug.aar')
    // compileOnly files('aar/theta-client-release.aar')
```

サンプルアプリ側: `theta-client/demos/demo-flutter/android/app/build.gradle`

```
    implementation files('../../packages/theta_client_flutter/android/aar/theta-client-debug.aar')
    // implementation files('../../packages/theta_client_flutter/android/aar/theta-client-release.aar')
```

### iOSの設定
iOSでは、`theta_client.podspec`の`s.vendored_frameworks`の参照を変更する。

`theta-client/flutter/ios/theta_client.podspec`

```
  s.vendored_frameworks = ['frameworks/debug/*.xcframework']
  # s.vendored_frameworks = ['frameworks/release/*.xcframework']
```
