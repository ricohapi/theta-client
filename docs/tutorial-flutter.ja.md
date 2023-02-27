# RICOH360 THETA Clientチュートリアル

## 使用可能な機種

* RICOH THETA X
* RICOH THETA Z1
* RICOH THETA V
* RICOH THETA S (ファームウェアv1.62以降のみ)
* RICOH THETA SC

## Flutterプロジェクトの作成

対応しているプラットフォームは、iOSとAndroid、言語はそれぞれ、kotlin、swiftを指定してプロジェクトを作成する。

``` Terminal
flutter create --platforms=android,ios -i swift -a kotlin your_app_name
```

## プロジェクトの設定
### THETA ClientのFlutter pluginパッケージのビルド
`theta-client/scripts`の`build_flutter_build.sh`を実行して、THETA ClientのFlutter pluginパッケージをビルドする。

### theta clientのコピー
THETA ClientのFlutter pluginパッケージを作成したプロジェクトにコピーする。

`demo-flutter`では、プロジェクト直下`demo-flutter/packages/theta_client_flutter`に配置。
（`build_flutter_build.sh`を実行した場合は、この操作は不要。）

### Flutterのプラグインの設定
`pubspec.yaml`の`dependencies`にコピーした`theta_client_flutter`を追加。

``` pubspec.yaml
dependencies:
  flutter:
    sdk: flutter
  theta_client_flutter:
    path: ./packages/theta_client_flutter
```

### Androidの設定
* `android/app/build.gradle`にライブラリの設定

    ``` build.gradle
    dependencies {
        ...略
        implementation files('../../packages/theta_client_flutter/android/aar/theta-client-debug.aar')
        ...略
    }
    ```

* 最小SDKバージョンを26以上に設定

    ``` build.gradle
        minSdkVersion 26
    ```

### iOSの設定
iOS Deployment Target を15以上に設定

## 事前準備

本SDKを使用したアプリケーションが動作するスマートフォンとTHETAを無線LAN接続しておきます。

## THETA Clientの初期化

``` Dart
import 'package:theta_client_flutter/theta_client_flutter.dart';

final _thetaClientFlutter = ThetaClientFlutter();

_thetaClientFlutter.initialize()
  .then((value) {
    // success
  })
  .onError((error, stackTrace) {
    // handle error
  });

OR

_thetaClientFlutter.initialize('http://<IPアドレス>:<ポート番号>')
  .then((value) {
    // success
  })
  .onError((error, stackTrace) {
    // handle error
  });
```

* THETA IP ADDRESS

  | モード | アドレス |
  |-------|---------|
  |ダイレクトモード| 192.168.1.1 |
  |その他| カメラのIPアドレス|

* Thetaから画像や動画をダウンロードする場合は、plainな接続となりますので、接続先のアドレス（デフォルト192.168.1.1）に応じて各プラットフォーム毎の設定が必要になります。

  * iOS: デフォルトの場合のInfo.plistの例を示します。なお、Xcodeの`Signing&Capabilities`→`App Transport Security Exception`でも追加設定することができます。

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

  * Android: res/xml/network_security_config.xmlに記述するデフォルトの場合の例を示します。

    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <network-security-config>
      <base-config cleartextTrafficPermitted="false" />
      <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="false">192.168.1.1</domain>
      </domain-config>
    </network-security-config>
    ```

## 静止画を撮影する

まず`getPhotoCaptureBuilder()`を使って撮影設定を行い、`PhotoCapture`オブジェクトを生成します。

``` Dart
_thetaClientFlutter.getPhotoCaptureBuilder()
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

上の例ではISO感度の最大値を1000に、ファイルフォーマットをIMAGE_5Kに設定しています。

プレビューを表示する方法は[プレビューを表示する](#プレビューを表示する)をご覧ください。

次に`PhotoCapture.takePicture()`を呼んで静止画を撮影します。

``` Dart
    photoCapture.takePicture((fileUrl) { 
      // fileUrl をGETリクエストを送信してJPEGファイルを受け取る処理
    }, (exception) {
      // catch error while take picture
    });
```

### 静止画撮影時に設定できる項目

* 露出補正 - setExposureCompensation(ExposureCompensationEnum value)

  | 値 | 補正値 |備考|
  |---|---:|---|
  |m2_0|-2.0f||
  |m1_7|-1.7f||
  |m1_3|-1.0f||
  |m0_7|-0.7f||
  |m0_3|-0.3f||
  |zero|0.0f|デフォルト|
  |p0_3|0.3f||
  |p0_7|0.7f||
  |p1_3|1.0f||
  |p1_7|1.7f||
  |p2_0|2.0f||

* 露出遅延設定 - setExposureDelay(ExposureDelayEnum value)
  takePictureコマンドと露出開始間の遅延時間(=セルフタイマー)

  | 値 | 遅延時間(秒) |備考|
  |---|---:|---|
  |delayOff|0|デフォルト|
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

* 露出プログラム - setExposureProgram(ExposureProgramEnum program)

  | 値 | 内容 |備考|
  |---|---|---|
  |manual|手動||
  |normalProgram|通常のプログラム||
  |aperturePriority|絞り優先||
  |shutterPriority|シャッター優先||
  |isoPriority|ISO優先||

* ファイルフォーマット - setFileFormat(PhotoFileFormatEnum fileFormat)

  | 値 | タイプ| 幅 | 高さ |S|SC|V|Z1|X|
  |---|---|--:|--:|:-:|:-:|:-:|:-:|:-:|
  |image_2K|jpeg|2048|1024|○|○|×|×|×|
  |image_5K|jpeg|5376|2688|○|○|○|×|×|
  |image_6_7K|jpeg|6720|3360|×|×|×|○|×|
  |rawP_6_7K|raw+|6720|3360|×|×|×|○|×|
  |image_5_5K|jpeg|5504|2752|×|×|×|×|○|
  |image_11K|jpeg|11008|5504|×|×|×|×|○|

* 画像処理 - setFilter(FilterEnum filter)

  | 値 | 内容 | 備考
  |---|---|---|
  |off|なし||
  |noiseReduction|ノイズ軽減||
  |hdr|HDR|デフォルト|

* GPSオン/オフ - setGpsTagRecording(GpsTagRecordingEnum value)
  THETA X以外は指定しても無視される

  | 値 | 内容 | 備考
  |---|---|---|
  |on|GPSあり|デフォルト|
  |off|GPSなし||

* ISO値 - setIso(IsoEnum iso)

  | 値 | ISO値 |備考|
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

* ISO上限 - setIsoAutoHighLimit(IsoAutoHighLimitEnum iso)
  THETA V ファームウェア v2.50.1以前、THETA S、THETA SCでは指定しても無視される

  | 値 | ISO上限値 |備考|
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

* 絞り設定 - setAperture(ApertureEnum aperture)

  | 値 | 設定値 |備考|
  |---|---:|---|
  |apertureAuto|自動||
  |aperture_2_0|2.0f||
  |aperture_2_1|2.1f||
  |aperture_2_4|2.4f||
  |aperture_3_5|3.5f||
  |aperture_5_6|5.6f||

* 色温度設定 - setColorTemperature(int kelvin)
  * 2500 ~ 10000

* GPS 情報 - setGpsInfo(GpsInfo gpsInfo)
  GpsInfoは以下の内容のObjectとして作成する。

  | 値 | 設定値 |備考|
  |---|---|---|
  |latitude|緯度|65535でオフ|
  |longitude|経度|65535でオフ|
  |altitude|高度||
  |dateTimeZone|日付時刻||

* ホワイトバランス - setWhiteBalance(WhiteBalanceEnum whiteBalance)

  | 値 | 設定値 |備考|
  |---|---|---|
  |auto|自動||
  |daylight|Outdoor|約5,200,000|
  |sade|Shade|約7,000,000|
  |cloudyDaylight|Cloudy|約6,000,000|
  |incandescent|Incandescent light 1|約3,200,000|
  |warmWhiteFluorescent|Incandescent light 2||
  |daylightFluorescent|Fluorescent light 1(daylight)||
  |daywhiteFluorescent|Fluorescent light 2(natural white)||
  |fluorescent|Fluorescent light 3 (white)|約4,000,000|
  |bulbFluorescent|Fluorescent light 4 (light bulb color)||
  |colorTemperature|CT settings (specified by the colorTemperature option)|RICOH THETA S firmware v01.82 or later and RICOH THETA SC firmware v01.10 or later|
  |underwater|Underwater|RICOH THETA V firmware v3.21.1 or later|

## 動画を撮影する

まず`getVideoCaptureBuilder()`を使って撮影設定を行い、`VideoCapture`オブジェクトを生成します。

``` Dart
_thetaClientFlutter.getVideoCaptureBuilder()
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

上の例ではISO感度の最大値を800に、ファイルフォーマットを`videoHD`に設定しています。

表示方法は[プレビューを表示する](#プレビューを表示する)をご覧ください

次に`VideoCapture.startCapture()`を呼んで動画の撮影を開始します。

``` Dart
VideoCapturing videoCapturing = videoCapture.startCapture()
  .then(fileUrl => {
    // GETリクエストを送信してMP4ファイルを受け取る処理
  })
  .catch(error => {
    // handle error
  });
```

次に`VideoCapture.stopCapture()`を呼び出して動画の撮影を終了します。成功すると上記の通り、撮影したファイルのURLを引数にthenが呼び出されます。

``` Dart
videoCapturing.stopCapture();
```

### 動画撮影時に設定できる項目

* 露出補正 - setExposureCompensation(ExposureCompensationEnum value)

  | 値 | 補正値 |備考|
  |---|---:|---|
  |m2_0|-2.0f||
  |m1_7|-1.7f||
  |m1_3|-1.0f||
  |m0_7|-0.7f||
  |m0_3|-0.3f||
  |zero|0.0f|デフォルト|
  |p0_3|0.3f||
  |p0_7|0.7f||
  |p1_3|1.0f||
  |p1_7|1.7f||
  |p2_0|2.0f||

* 露出遅延設定 - setExposureDelay(ExposureDelayEnum value)
  takePictureコマンドと露出開始間の遅延時間(=セルフタイマー)

  | 値 | 遅延時間(秒) |備考|
  |---|---:|---|
  |delayOff|0|デフォルト|
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

* 露出プログラム - setExposureProgram(ExposureProgramEnum program)

  | 値 | 内容 |備考|
  |---|---|---|
  |manual|手動||
  |normalProgram|通常のプログラム||
  |aperturePriority|絞り優先||
  |shutterPriority|シャッター優先||
  |isoPriority|ISO優先||

* ファイルフォーマット - setFileFormat(VideoFileFormatEnum fileFormat)

  | 値 | タイプ| 幅 | 高さ |フレームレート|Codec|S|SC|V|Z1|X|
  |---|---|--:|--:|--:|--|:-:|:-:|:-:|:-:|:-:|
  |videoHD|mp4|1280|570|||○|○|×|×|×|
  |videoFullHD|mp4|1920|1080|||○|○|×|×|×|
  |video_2K|mp4|1920|960||H.264/MPEG-4 AVC|×|×|○|○|×|
  |video_4K|mp4|3840|1920||H.264/MPEG-4 AVC|×|×|○|○|×|
  |video_2K_30F|mp4|1920|960|30|H.264/MPEG-4 AVC|×|×|×|×|○|
  |video_2K_60F|mp4|1920|960|60|H.264/MPEG-4 AVC|×|×|×|×|○|
  |video_4K_30F|mp4|3840|1920|30|H.264/MPEG-4 AVC|×|×|×|×|○|
  |video_4K_60F|mp4|3840|1920|60|H.264/MPEG-4 AVC|×|×|×|×|○|
  |video_5_7K_2F|mp4|5760|2880|2|H.264/MPEG-4 AVC|×|×|×|×|○|
  |video_5_7K_5F|mp4|5760|2880|5|H.264/MPEG-4 AVC|×|×|×|×|○|
  |video_5_7K_30F|mp4|5760|2880|30|H.264/MPEG-4 AVC|×|×|×|×|○|
  |video_7K_2F|mp4|7680|3840|2|H.264/MPEG-4 AVC|×|×|×|×|○|
  |video_7K_5F|mp4|7680|3840|5|H.264/MPEG-4 AVC|×|×|×|×|○|
  |video_7K_10F|mp4|7680|3840|10|H.264/MPEG-4 AVC|×|×|×|×|○|

* 最大録画時間設定 - setMaxRecordableTime(MaxRecordableTimeEnum time)

  | 値 | 内容 | 備考
  |---|---|---|
  |time_300|300秒||
  |time_1500|1500秒||

* GPSオン/オフ - setGpsTagRecording(GpsTagRecordingEnum value)
  THETA X以外は指定しても無視される

  | 値 | 内容 | 備考
  |---|---|---|
  |on|GPSあり|デフォルト|
  |off|GPSなし||

* ISO値 - setIso(IsoEnum iso)

  | 値 | ISO値 |備考|
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

* ISO上限 - setIsoAutoHighLimit(IsoAutoHighLimitEnum iso)
  THETA V ファームウェア v2.50.1以前、THETA S、THETA SCでは指定しても無視される

  | 値 | ISO上限値 |備考|
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

* 絞り設定 - setAperture(ApertureEnum aperture)

  | 値 | 設定値 |備考|
  |---|---:|---|
  |apertureAuto|自動||
  |aperture_2_0|2.0f||
  |aperture_2_1|2.1f||
  |aperture_2_4|2.4f||
  |aperture_3_5|3.5f||
  |aperture_5_6|5.6f||

* 色温度設定 - setColorTemperature(int kelvin)
  * 2500 ~ 10000

* GPS 情報 - setGpsInfo(GpsInfo gpsInfo)
  GpsInfoは以下の内容のObjectとして作成する。

  | 値 | 設定値 |備考|
  |---|---|---|
  |latitude|緯度|65535でオフ|
  |longitude|経度|65535でオフ|
  |altitude|高度||
  |dateTimeZone|日付時刻||

* ホワイトバランス - setWhiteBalance(WhiteBalanceEnum whiteBalance)

  | 値 | 設定値 |備考|
  |---|---|---|
  |auto|自動||
  |daylight|Outdoor|約5,200,000|
  |SHADE|Shade|約7,000,000|
  |cloudyDaylight|Cloudy|約6,000,000|
  |incandescent|Incandescent light 1|約3,200,000|
  |warmWhiteFluorescent|Incandescent light 2||
  |daylightFluorescent|Fluorescent light 1(daylight)||
  |daywhiteFluorescent|Fluorescent light 2(natural white)||
  |fluorescent|Fluorescent light 3 (white)|約4,000,000|
  |bulbFluorescent|Fluorescent light 4 (light bulb color)||
  |colorTemperature|CT settings (specified by the colorTemperature option)|RICOH THETA S firmware v01.82 or later and RICOH THETA SC firmware v01.10 or later|
  |underwater|Underwater|RICOH THETA V firmware v3.21.1 or later|


## プレビューを表示する

プレビューはequirectangular形式のmotion JPEGです。

| 機種 | 横(pixel) | 縦(pixel) | フレームレート(fps) | 備考 |
| ---- | --: | --: | -----------: | ---- |
| THETA X | 1024 | 512 | 30 | |
| THETA Z1 | 1024 | 512 | 30 | |
| THETA V | 1024 | 512 | 30 | ファームウェア v2.21.1以降 |
| THETA V | 1024 | 512 | 8 | ファームウェア v2.20.1以前 |
| THETA S | 640 | 320 | 10 | |
| THETA SC | 640 | 320 | 10 | |

`getLivePreview()`を呼ぶと、プレビューの各フレームの受信が完了する度に、コールバック関数が呼ばれます。コールバック関数の引数には、フレームデータ(JPEG)のメモリイメージが渡されます。
プレビューを終了する場合はコールバック関数の戻り値で、`false`を返してください。

``` Dart
  bool previewing = false;

  bool frameHandler(Uint8List frameData) {

    // frameData is JPEG image data.

    // Return false to preview done.
    return previewing;
  }

  void startLivePreview() {
    previewing = true;
    _thetaClientFlutter.getLivePreview(frameHandler)
      .then((value) {
        // preview done
      })
      .onError((error, stackTrace) {
        // handle error
      });
  }

  @override
  Widget build(BuildContext context) {
    ...略
        child: Image.memory(
          frameData,
          errorBuilder: (a, b, c) {
            return Container(
              color: Colors.black,
            );
          },
          gaplessPlayback: true,
        ),
    ...略
```

## カメラを設定する

カメラを設定するには、設定したい内容を`Options`に設定して`setOptions()`を呼び出します。

``` Dart
final options = Options();
options.aperture = ApertureEnum.apertureAuto;
_thetaClientFlutter.setOptions(options)
  .then((value) {
    // done
  })
  .onError((error, stackTrace) {
    // handle error
  });
```

`Options`に設定できる項目と内容は以下を参照してください。

* 日付時刻 - dateTimeZone:String

  形式: YYYY:MM:DD hh:mm:ss+(-)hh:mm
  hh 0-23、+(-)hh:mm は、タイムゾーン
  例: 2014:05:18 01:04:29+08:00

* 露出補正 - exposureCompensation:ExposureCompensationEnum

  | 値 | 補正値 |備考|
  |---|---:|---|
  |m2_0|-2.0f||
  |m1_7|-1.7f||
  |m1_3|-1.0f||
  |m0_7|-0.7f||
  |m0_3|-0.3f||
  |zero|0.0f|デフォルト|
  |p0_3|0.3f||
  |p0_7|0.7f||
  |p1_3|1.0f||
  |p1_7|1.7f||
  |p2_0|2.0f||

* 露出遅延設定 - exposureDelay:ExposureDelayEnum
  takePictureコマンドと露出開始間の遅延時間(=セルフタイマー)

  | 値 | 遅延時間(秒) |備考|
  |---|---:|---|
  |delayOff|0|デフォルト|
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

* 露出プログラム - exposureProgram:ExposureProgramEnum

  | 値 | 内容 |備考|
  |---|---|---|
  |manual|手動||
  |normalProgram|通常のプログラム||
  |aperturePriority|絞り優先||
  |shutterPriority|シャッター優先||
  |isoPriority|ISO優先||

* ファイルフォーマット - fileFormat:FileFormatEnum

  * 静止画

  | 値 | タイプ| 幅 | 高さ |S|SC|V|Z1|X|
  |---|---|--:|--:|:-:|:-:|:-:|:-:|:-:|
  |image_2K|jpeg|2048|1024|○|○|×|×|×|
  |image_5K|jpeg|5376|2688|○|○|○|×|×|
  |image_6_7K|jpeg|6720|3360|×|×|×|○|×|
  |rawP_6_7K|raw+|6720|3360|×|×|×|○|×|
  |image_5_5K|jpeg|5504|2752|×|×|×|×|○|
  |image_11K|jpeg|11008|5504|×|×|×|×|○|

  * 動画

  | 値 | タイプ| 幅 | 高さ |フレームレート|Codec|S|SC|V|Z1|X|
  |---|---|--:|--:|--:|--|:-:|:-:|:-:|:-:|:-:|
  |videoHD|mp4|1280|570|||○|○|×|×|×|
  |videoFullHD|mp4|1920|1080|||○|○|×|×|×|
  |video_2K|mp4|1920|960||H.264/MPEG-4 AVC|×|×|○|○|×|
  |video_4K|mp4|3840|1920||H.264/MPEG-4 AVC|×|×|○|○|×|
  |video_2K_30F|mp4|1920|960|30|H.264/MPEG-4 AVC|×|×|×|×|○|
  |video_2K_60F|mp4|1920|960|60|H.264/MPEG-4 AVC|×|×|×|×|○|
  |video_4K_30F|mp4|3840|1920|30|H.264/MPEG-4 AVC|×|×|×|×|○|
  |video_4K_60F|mp4|3840|1920|60|H.264/MPEG-4 AVC|×|×|×|×|○|
  |video_5_7K_2F|mp4|5760|2880|2|H.264/MPEG-4 AVC|×|×|×|×|○|
  |video_5_7K_5F|mp4|5760|2880|5|H.264/MPEG-4 AVC|×|×|×|×|○|
  |video_5_7K_30F|mp4|5760|2880|30|H.264/MPEG-4 AVC|×|×|×|×|○|
  |video_7K_2F|mp4|7680|3840|2|H.264/MPEG-4 AVC|×|×|×|×|○|
  |video_7K_5F|mp4|7680|3840|5|H.264/MPEG-4 AVC|×|×|×|×|○|
  |video_7K_10F|mp4|7680|3840|10|H.264/MPEG-4 AVC|×|×|×|×|○|

* 画像処理 - filter:FilterEnum

  | 値 | 内容 | 備考
  |---|---|---|
  |off|なし||
  |noiseReduction|ノイズ軽減||
  |hdr|HDR|デフォルト|

* GPSオン/オフ - isGpsOn:bool
  THETA X以外は指定しても無視される

  | 値 | 内容 | 備考
  |---|---|---|
  |true|GPSあり|デフォルト|
  |false|GPSなし||

* ISO値 - iso:IsoEnum

  | 値 | ISO値 |備考|
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

* ISO上限 - isoAutoHighLimit:IsoAutoHighLimitEnum
  THETA V ファームウェア v2.50.1以前、THETA S、THETA SCでは指定しても無視される

  | 値 | ISO上限値 |備考|
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

* 絞り設定 - aperture:ApertureEnum

  | 値 | 設定値 |備考|
  |---|---:|---|
  |apertureAuto|自動||
  |aperture_2_0|2.0f||
  |aperture_2_1|2.1f||
  |aperture_2_4|2.4f||
  |aperture_3_5|3.5f||
  |aperture_5_6|5.6f||

* 色温度設定 - colorTemperature:int
  * 2500 ~ 10000

* GPS 情報 - gpsInfo:GpsInfo
  GpsInfoは以下の内容のObjectとして作成する。

  | 値 | 設定値 |備考|
  |---|---|---|
  |latitude|緯度|65535でオフ|
  |longitude|経度|65535でオフ|
  |altitude|高度||
  |dateTimeZone|日付時刻||

* ホワイトバランス - whiteBalance:WhiteBalanceEnum

  | 値 | 設定値 |備考|
  |---|---|---|
  |auto|自動||
  |daylight|Outdoor|約5,200,000|
  |sade|Shade|約7,000,000|
  |cloudyDaylight|Cloudy|約6,000,000|
  |incandescent|Incandescent light 1|約3,200,000|
  |warmWhiteFluorescent|Incandescent light 2||
  |daylightFluorescent|Fluorescent light 1(daylight)||
  |daywhiteFluorescent|Fluorescent light 2(natural white)||
  |fluorescent|Fluorescent light 3 (white)|約4,000,000|
  |bulbFluorescent|Fluorescent light 4 (light bulb color)||
  |colorTemperature|CT settings (specified by the colorTemperature option)|RICOH THETA S firmware v01.82 or later and RICOH THETA SC firmware v01.10 or later|
  |underwater|Underwater|RICOH THETA V firmware v3.21.1 or later|

* 最大録画時間設定 - maxRecordableTime:MaxRecordableTimeEnum

  | 値 | 内容 | 備考
  |---|---|---|
  |time_300|300秒||
  |time_1500|1500秒||

* キャプチャモード - captureMode:CaptureModeEnum

  | 値 | 内容 | 備考
  |---|---|---|
  |image|静止画撮影モード||
  |video|動画撮影モード||

* 言語設定 - language:LanguageEnum
(THETA S, THETA SCでは指定しても無視される)

  |値|内容|備考|
  |--|---|---|
  |de|ドイツ語||
  |enGB|英国英語||
  |enUS|米国英語||
  |fr|フランス語||
  |it|イタリア語||
  |ja|日本語||
  |ko|韓国語||
  |zhCN|中国語||
  |zhTW|中国語台湾||

* スリープモードに入るまでの時間(分) - sleepDelay:SleepDelayEnum
  * SleepDelayEnum

    |値|設定値(秒)|備考|
    |--|--:|---|
    |disable|65535|自動オフしない|
    |sleepDelay_3m|180||
    |sleepDelay_5m|300||
    |sleepDelay_7m|420||
    |sleepDelay_10m|600||

* スリープから自動電源オフまでの時間(秒) offDelay:OffDelayEnum
  * OffDelayEnum

    |値|設定値(秒)|備考|
    |--|--:|---|
    |DISABLE|65535|自動オフしない|
    |offDelay_5m|300||
    |offDelay_10m|600||
    |offDelay_15m|900||
    |offDelay_30m|1800||

* シャッター音 - shutterVolume:int
  * 0 - 100

## カメラの設定を取得する
カメラの設定を取得するには、取得したいオプションの一覧を指定して`getOptions()`を呼び出します。

``` Dart
final optionNames = [
  OptionNameEnum.aperture,
  OptionNameEnum.captureMode,
  OptionNameEnum.colorTemperature,
];
_thetaClientFlutter.getOptions(optionNames)
  .then((options) {
    // handle options
  })
  .onError((error, stackTrace) {
    // handle error
  });
```

* OptionNameEnum は以下の表を参照してください

  | 値 | 意味 |型|
  |----|-----|--|
  |aperture|絞り|ApertureEnum|
  |captureMode|キャプチャーモード|CaptureModeEnum|
  |ColorTemperature|色温度|number|
  |dateTimeZone|日時|String|
  |exposureCompensation|露出補正|ExposureCompensationEnum|
  |exposureDelay|露出遅延時間|ExposureDelayEnum|
  |exposureProgram|露出プログラム|ExposureProgram|
  |fileFormat|ファイルフォーマット|FileFormatEnum|
  |filter|画像フィルタ|FilterEnum|
  |gpsInfo|GPS情報|GpsInfo|
  |isGpsOn|GPSフラグ|bool|
  |iso|ISO値|IsoEnum|
  |isoAutoHighLimit|ISO上限|IsoAutoHighLimitEnum|
  |language|言語|LanguageEnum|
  |maxRecordableTime|最長録画時間|MaxRecordableTimeEnum|
  |offDelay|電源オフ時間|OffDelayEnum|
  |sleepDelay|スリープ時間|SleepDelayEnum|
  |remainingPictures|残り画像数|int|
  |remainingVideoSeconds|残り録画秒数|int|
  |remainingSpace|残領域|int|
  |totalSpace|合計領域|int|
  |shutterVolume|シャッター音量|int|
  |whiteBalance|ホワイトバランス|WhiteBalanceEnum|

## THETA内の静止画・動画を一覧する
THETA内の静止画（JPEGファイル）や動画（MP4ファイル）の一覧は`listFiles(FileTypeEnum fileType, int entryCount, [int startPosition])`を使って取得できます。
一覧は、`FileInfo`の一覧になります。

* FileTypeEnum

  |値|内容|
  |---|---|
  |all|全てのファイルを一覧|
  |image|静止画（JPEGファイル）を一覧|
  |video|動画（MP4ファイル）を一覧|

* FileInfo

  |プロパティ名|型|内容|
  |---|---|---|
  |name|String|ファイル名を表します|
  |size|int|ファイルサイズ（バイト数）を表します|
  |dateTime|String|撮影日時（YYYY:MM:DD HH:MM:SS）を表します|
  |fileUrl|String|ファイルのURLを表します|
  |thumbnailUrl|String|サムネールのURLを表します|

``` Dart
_thetaClientFlutter.listFiles(FileTypeEnum.image, 1000, 0)
  .then((files) {
    // handle file list
  })
  .onError((error, stackTrace) {
    // handle error
  });
```

## 静止画・動画を取得する
撮影した静止画(JPEGファイル）や動画（MP4ファイル)のURLからデータを取得します。httpパッケージを使用して、以下のような関数を使えばダウンロードできます。
なお、静止画像の場合は、Image Widgetのnetworkコンストラクタを使用することでダウンロード後表示することができます。

* httpパッケージ
https://pub.dev/packages/http


``` Dart
void downloadFile(String fileUrl, String filePath) async {
  final url = Uri.parse(fileUrl);
  final response = await get(url);
  final file = File(filePath);
  await file.create();
  await file.writeAsBytes(response.bodyBytes);
}
```

## サムネイルを取得する
THETA内のファイルのサムネイルは、`listFiles`を使って取得した `FileInfo`の`thumbnailUrl`を使って以下のようにダウンロードすることができます。

``` Dart
  final url = Uri.parse(fileInfo.thumbnailUrl);
  final response = await get(url);
  final file = File(filePath);
  await file.create();
  await file.writeAsBytes(response.bodyBytes);
```

## プレビューを表示する

プレビューはequirectangular形式のmotion JPEGです。

それぞれのフレーム処理を行うコールバック関数を引数として
`getLivePreview()`を呼び出します。
プレビュー中のフレームは、イベントのパラメータとして受け取ることができます。

``` Dart
  bool previewing = false;

  bool frameHandler(Uint8List frameData) {

    // frameData is JPEG image data.

    // Return false to preview done.
    return previewing;
  }

  void startLivePreview() {
    previewing = true;
    _thetaClientFlutter.getLivePreview(frameHandler)
      .then((value) {
        // preview done
      })
      .onError((error, stackTrace) {
        // handle error
      });
  }

  @override
  Widget build(BuildContext context) {
    ...略
        child: Image.memory(
          frameData,
          errorBuilder: (a, b, c) {
            return Container(
              color: Colors.black,
            );
          },
          gaplessPlayback: true,
        ),
    ...略
```

## THETAをリセットする
接続しているThetaをリセットするには、`reset()`を呼び出します。

``` Dart
_thetaClientFlutter.reset()
  .then((value) {
    // reset done
  })
  .onError((error, stackTrace) {
    // handle error
  });
```

## THETAの情報を取得する
接続しているThetaの情報を取得するには、`getThetaInfo()`を呼び出します。呼び出しに成功すると`ThetaInfo`を取得できます。このデータは以下のような情報を含みます。

* ThetaInfo

  |プロパティ名|型|内容|
  |---|---|---|
  |firmwareVersion|String|ファームウェアバージョンを表します|
  |hasGps|bool|GPS機能を持っているかどうかを表します|
  |hasGyro|bool|ジャイロを持っているかどうかを表します|
  |model|String|Thetaの型番を表します|
  |serialNumber|String|Thetaのシリアル番号を表します|
  |uptime|int|Thetaの電源を入れてからの秒数を表します|

``` Dart
_thetaClientFlutter.getThetaInfo()
  .then((thetaInfo) {
    // processing thetaInfo
  })
  .onError((error, stackTrace) {
    // handle error
  });
```

## THETAの状態を取得する
接続しているThetaの状態を取得するには、`getThetaState()`を呼び出します。呼び出しに成功すると`ThetaState`を取得できます。このデータは以下のような情報を含みます。

* ThetaState

  |プロパティ名|型|内容|
  |---|---|---|
  |batteryLevel|double|バッテリーレベルを表します|
  |chargingState|ChargingStateEnum|充電状態を表します|
  |fingerprint|String|現在の状態ごとに一意に決まる識別子を表します|
  |isSdCard|bool|SDカードが存在するかどうかを表します|
  |latestFileUrl|String|最後の取得したメディアのURLを表します|
  |recordableTime|int|録画可能秒数を表します|
  |recordedTime|int|録画済み秒数を表します|

* ChargingStateEnum

  |プロパティ名|内容|
  |---|---|
  |charging|充電中を表します|
  |completed|充電完了を表します|
  |notCharging|ケーブル未接続を表します|


``` Dart
_thetaClientFlutter.getThetaState()
  .then((thetaState) {
    // processing thetaState
  })
  .onError((error, stackTrace) {
    // handle error
  });
```
