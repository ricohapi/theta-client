# RICOH360 THETA SDK チュートリアル

## 使用可能な機種

- RICOH THETA X
- RICOH THETA Z1
- RICOH THETA V
- RICOH THETA S (ファームウェア v1.62 以降のみ)
- RICOH THETA SC

## 事前準備

本 SDK を使用したアプリケーションが動作するスマートフォンと THETA を無線 LAN 接続しておきます。

## SDK のインスタンス作成

```swift
import THETAClient

// THETAがIPアドレスを指定して作成する
ThetaRepository.Companion.shared.doNewInstance(
  endPoint:"http://<THETA IP ADDRESS>"
) {response, error in
  if let instance = response {
    theta = instance
  }
  if let thetaError error {
    // handle error
  }
}
```

- THETA IP ADDRESS

  | モード           | アドレス               |
  | ---------------- | ---------------------- |
  | ダイレクトモード | 192.168.1.1            |
  | その他           | 割り当てた IP アドレス |

- Theta から画像や動画を URLSession を使ってダウンロードする場合は、plain な接続となりますので、接続先のアドレス（デフォルト 192.168.1.1）に応じて Info.plist への接続許可設定が必要になります。デフォルトの場合の Info.plist の例を示します。なお、Xcode の`Signing&Capabilities`→`App Transport Security Exception`でも追加設定することができます。

```xml
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

## 静止画を撮影する

まず`ThetaRepository.getPhotoCaptureBuilder()`を使って撮影設定を行い、`PhotoCapture`オブジェクトを生成します。

```swift
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

上の例では ISO 感度の最大値を 1000 に、ファイルフォーマットを image5k に設定しています。

プレビューを表示する方法は[プレビューを表示する](#プレビューを表示する)をご覧ください。

次に`PhotoCapture.takePicture(callback:)`を呼んで静止画を撮影します。以下のように`PhotoCaptureTakePictureCallback`を実装したコールバック用クラスを作成して呼び出します。

```swift
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

### 静止画撮影時に設定できる項目

- 露出補正 - setExposureCompensation(value:ThetaRepository.ExposureCompensationEnum)

  | 値   | 補正値 | 備考       |
  | ---- | -----: | ---------- |
  | m20  |  -2.0f |            |
  | m17  |  -1.7f |            |
  | m13  |  -1.0f |            |
  | m07  |  -0.7f |            |
  | m03  |  -0.3f |            |
  | zero |   0.0f | デフォルト |
  | p03  |   0.3f |            |
  | p07  |   0.7f |            |
  | p13  |   1.0f |            |
  | p17  |   1.7f |            |
  | p20  |   2.0f |            |

- 露出遅延設定 - setExposureDelay(delay: ThetaRepository.ExposureDelayEnum)
  takePicture コマンドと露出開始間の遅延時間(=セルフタイマー)

  | 値       | 遅延時間(秒) | 備考       |
  | -------- | -----------: | ---------- |
  | delayOff |            0 | デフォルト |
  | delay1   |            1 |            |
  | delay2   |            2 |            |
  | delay3   |            3 |            |
  | delay4   |            4 |            |
  | delay5   |            5 |            |
  | delay6   |            6 |            |
  | delay7   |            7 |            |
  | delay8   |            8 |            |
  | delay9   |            9 |            |
  | delay10  |           10 |            |

- 露出プログラム - setExposureProgram(program:ThetaRepository.ExposureProgramEnum)

  | 値               | 内容             | 備考 |
  | ---------------- | ---------------- | ---- |
  | manual           | 手動             |      |
  | normalProgram    | 通常のプログラム |      |
  | aperturePriority | 絞り優先         |      |
  | shutterPriority  | シャッター優先   |      |
  | isoPriority      | ISO 優先         |      |

- ファイルフォーマット - setFileFormat(fileFormat:ThetaRepository.PhotoFileFormatEnum)

  | 値       | タイプ |    幅 | 高さ |  S  | SC  |  V  | Z1  |  X  |
  | -------- | ------ | ----: | ---: | :-: | :-: | :-: | :-: | :-: |
  | image2k  | jpeg   |  2048 | 1024 |  ○  |  ○  |  ×  |  ×  |  ×  |
  | image5k  | jpeg   |  5376 | 2688 |  ○  |  ○  |  ○  |  ×  |  ×  |
  | image67k | jpeg   |  6720 | 3360 |  ×  |  ×  |  ×  |  ○  |  ×  |
  | rawP67k  | raw+   |  6720 | 3360 |  ×  |  ×  |  ×  |  ○  |  ×  |
  | image55k | jpeg   |  5504 | 2752 |  ×  |  ×  |  ×  |  ×  |  ○  |
  | image11k | jpeg   | 11008 | 5504 |  ×  |  ×  |  ×  |  ×  |  ○  |

- 画像処理 - setFilter(filter:ThetaRepository.Filter)

  | 値             | 内容       | 備考       |
  | -------------- | ---------- | ---------- |
  | off            | なし       |            |
  | noiseReduction | ノイズ軽減 |            |
  | hdr            | HDR        | デフォルト |

- GPS オン/オフ - setGpsTagRecording(value:ThetaRepository.GpsTagRecordingEnum)
  THETA X 以外は指定しても無視される

  | 値  | 内容     | 備考       |
  | --- | -------- | ---------- |
  | on  | GPS あり | デフォルト |
  | off | GPS なし |            |

- ISO 値 - setIso(iso:ThetaRepository.IsoEnum)

  | 値      | ISO 値 | 備考 |
  | ------- | -----: | ---- |
  | isoAuto |      0 |      |
  | iso50   |     50 |      |
  | iso64   |     64 |      |
  | iso80   |     80 |      |
  | iso100  |    100 |      |
  | iso125  |    125 |      |
  | iso160  |    160 |      |
  | iso200  |    200 |      |
  | iso250  |    250 |      |
  | iso320  |    320 |      |
  | iso400  |    400 |      |
  | iso500  |    150 |      |
  | iso640  |    640 |      |
  | iso800  |    800 |      |
  | iso1000 |   1000 |      |
  | iso1250 |   1250 |      |
  | iso1600 |   1600 |      |
  | iso2000 |   2000 |      |
  | iso2500 |   2500 |      |
  | iso3200 |   3200 |      |
  | iso4000 |   4000 |      |
  | iso5000 |   5000 |      |
  | iso6400 |   6400 |      |

- ISO 上限 - setIsoAutoHighLimit(iso:ThetaRepository.IsoAutoHighLimitEnum)
  THETA V ファームウェア v2.50.1 以前、THETA S、THETA SC では指定しても無視される

  | 値      | ISO 上限値 | 備考 |
  | ------- | ---------: | ---- |
  | iso100  |        100 |      |
  | iso125  |        125 |      |
  | iso160  |        160 |      |
  | iso200  |        200 |      |
  | iso250  |        250 |      |
  | iso320  |        320 |      |
  | iso400  |        400 |      |
  | iso500  |        150 |      |
  | iso640  |        640 |      |
  | iso800  |        800 |      |
  | iso1000 |       1000 |      |
  | iso1250 |       1250 |      |
  | iso1600 |       1600 |      |
  | iso2000 |       2000 |      |
  | iso2500 |       2500 |      |
  | iso3200 |       3200 |      |
  | iso4000 |       4000 |      |
  | iso5000 |       5000 |      |
  | iso6400 |       6400 |      |

- 絞り設定 - setAperture(aperture:ThetaRepository.ApertureEnum)

  | 値           | 設定値 | 備考 |
  | ------------ | -----: | ---- |
  | apertureAuto |   自動 |      |
  | aperture20   |   2.0f |      |
  | aperture21   |   2.1f |      |
  | aperture24   |   2.4f |      |
  | aperture35   |   3.5f |      |
  | aperture56   |   5.6f |      |

- 色温度設定 - setColorTemperature(kelvin:Int)

  - 2500 ~ 10000

- GPS 情報 - setGpsInfo(gpsInfo:ThetaRepository.GpsInfo)
  GpsInfo は以下の内容で作成する。
  ThetaRepository.GpsInfo(latitude:longitude:altitude:dateTimeZone:)

  | 値           | 設定値   | 備考         |
  | ------------ | -------- | ------------ |
  | latitude     | 緯度     | 65535 でオフ |
  | longitude    | 経度     | 65535 でオフ |
  | altitude     | 高度     |              |
  | dateTimeZone | 日付時刻 |              |

- ホワイトバランス - setWhiteBalance(whiteBalance:ThetaRepository.WhiteBalanceEnum)

  | 値                   | 設定値                                                 | 備考                                                                               |
  | -------------------- | ------------------------------------------------------ | ---------------------------------------------------------------------------------- |
  | auto                 | 自動                                                   |                                                                                    |
  | daylight             | Outdoor                                                | 約 5,200,000                                                                       |
  | shade                | Shade                                                  | 約 7,000,000                                                                       |
  | cloudyDaylight       | Cloudy                                                 | 約 6,000,000                                                                       |
  | incandescent         | Incandescent light 1                                   | 約 3,200,000                                                                       |
  | warmWhiteFluorescent | Incandescent light 2                                   |                                                                                    |
  | daylightFluorescent  | Fluorescent light 1(daylight)                          |                                                                                    |
  | daywhiteFluorescent  | Fluorescent light 2(natural white)                     |                                                                                    |
  | fluorescent          | Fluorescent light 3 (white)                            | 約 4,000,000                                                                       |
  | bulbFluorescent      | Fluorescent light 4 (light bulb color)                 |                                                                                    |
  | colorTemperature     | CT settings (specified by the colorTemperature option) | RICOH THETA S firmware v01.82 or later and RICOH THETA SC firmware v01.10 or later |
  | underwater           | Underwater                                             | RICOH THETA V firmware v3.21.1 or later                                            |

## 動画を撮影する

まず`ThetaRepository.getVideoCaptureBuilder()`を使って撮影設定を行い、`VideoCapture`オブジェクトを生成します。

```swift
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

上の例では ISO 感度の最大値を 800 に、ファイルフォーマットを`videoHd`に設定しています。

Theta S と Theta SC 以外ではプレビューを表示できます。表示方法は[プレビューを表示する](#プレビューを表示する)をご覧ください

次に`VideoCapture.startCapture(callback:)`を呼んで動画の撮影を開始します。以下のように`VideoCaptureStartCaptureCallback`を実装したコールバック用クラスを作成して呼び出します。

```swift
class Callback: VideoCaptureStartCaptureCallback {
    let callback: (_ fileUrl: String?, _ error: Error?) -> Void
    init(_ callback: @escaping (_ fileUrl: String?, _ error: Error?) -> Void) {
        self.callback = callback
    }
    func onCaptureCompleted(fileUrl: String?) {
        callback(fileUrl, nil)
    }
    func onCaptureFailed(exception: ThetaRepository.ThetaRepositoryException) {
        callback(nil, exception as? Error)
    }
    func onStopFailed(exception: ThetaRepository.ThetaRepositoryException) {
        // handle error of stopCapture
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

次に`VideoCapturing.stopCapture()`を呼んで動画の撮影を終了します。撮影終了後、MP4 ファイルの生成が完了すると、`startCapture(callback:)`に渡したコールバック関数が呼ばれます。

```swift
videoCapturing.stopCapture()
```

### 動画撮影時に設定できる項目

- 露出補正 - setExposureCompensation(value:ThetaRepository.ExposureCompensationEnum)

  | 値   | 補正値 | 備考       |
  | ---- | -----: | ---------- |
  | m20  |  -2.0f |            |
  | m17  |  -1.7f |            |
  | m13  |  -1.0f |            |
  | m07  |  -0.7f |            |
  | m03  |  -0.3f |            |
  | zero |   0.0f | デフォルト |
  | p03  |   0.3f |            |
  | p07  |   0.7f |            |
  | p13  |   1.0f |            |
  | p17  |   1.7f |            |
  | p20  |   2.0f |            |

- 露出遅延設定 - setExposureDelay(delay: ThetaRepository.ExposureDelayEnum)
  startCapture コマンドと露出開始間の遅延時間(=セルフタイマー)

  | 値       | 遅延時間(秒) | 備考       |
  | -------- | -----------: | ---------- |
  | delayOff |            0 | デフォルト |
  | delay1   |            1 |            |
  | delay2   |            2 |            |
  | delay3   |            3 |            |
  | delay4   |            4 |            |
  | delay5   |            5 |            |
  | delay6   |            6 |            |
  | delay7   |            7 |            |
  | delay8   |            8 |            |
  | delay9   |            9 |            |
  | delay10  |           10 |            |

- 露出プログラム - setExposureProgram(program:ThetaRepository.ExposureProgramEnum)

  | 値               | 内容             | 備考 |
  | ---------------- | ---------------- | ---- |
  | manual           | 手動             |      |
  | normalProgram    | 通常のプログラム |      |
  | aperturePriority | 絞り優先         |      |
  | shutterPriority  | シャッター優先   |      |
  | isoPriority      | ISO 優先         |      |

- ファイルフォーマット - setFileFormat(fileFormat:ThetaRepository.VideoFileFormatEnum)

  | 値          | タイプ |   幅 | 高さ | フレームレート | Codec            |  S  | SC  |  V  | Z1  |  X  |
  | ----------- | ------ | ---: | ---: | -------------: | ---------------- | :-: | :-: | :-: | :-: | :-: |
  | videoHd     | mp4    | 1280 |  570 |                |                  |  ○  |  ○  |  ×  |  ×  |  ×  |
  | videoFullHd | mp4    | 1920 | 1080 |                |                  |  ○  |  ○  |  ×  |  ×  |  ×  |
  | video2k     | mp4    | 1920 |  960 |                | H.264/MPEG-4 AVC |  ×  |  ×  |  ○  |  ○  |  ×  |
  | video4k     | mp4    | 3840 | 1920 |                | H.264/MPEG-4 AVC |  ×  |  ×  |  ○  |  ○  |  ×  |
  | video2k30f  | mp4    | 1920 |  960 |             30 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | video2k60f  | mp4    | 1920 |  960 |             60 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | video4k30f  | mp4    | 3840 | 1920 |             30 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | video4k60f  | mp4    | 3840 | 1920 |             60 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | video57k2f  | mp4    | 5760 | 2880 |              2 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | video57k5f  | mp4    | 5760 | 2880 |              5 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | video57k30f | mp4    | 5760 | 2880 |             30 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | video7k2f   | mp4    | 7680 | 3840 |              2 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | video7k5f   | mp4    | 7680 | 3840 |              5 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | video7k10f  | mp4    | 7680 | 3840 |             10 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |

- 最大録画時間設定 - setMaxRecordableTime(time:ThetaRepository.MaxRecordableTimeEnum)

  | 値                 | 内容    | 備考 |
  | ------------------ | ------- | ---- |
  | recordableTime300  | 300 秒  |      |
  | recordableTime1500 | 1500 秒 |      |

- GPS オン/オフ - setGpsTagRecording(value:ThetaRepository.GpsTagRecordingEnum)
  THETA X 以外は指定しても無視される

  | 値  | 内容     | 備考       |
  | --- | -------- | ---------- |
  | on  | GPS あり | デフォルト |
  | off | GPS なし |            |

- ISO 値 - setIso(iso:ThetaRepository.IsoEnum)

  | 値      | ISO 値 | 備考 |
  | ------- | -----: | ---- |
  | isoAuto |      0 |      |
  | iso50   |     50 |      |
  | iso64   |     64 |      |
  | iso80   |     80 |      |
  | iso100  |    100 |      |
  | iso125  |    125 |      |
  | iso160  |    160 |      |
  | iso200  |    200 |      |
  | iso250  |    250 |      |
  | iso320  |    320 |      |
  | iso400  |    400 |      |
  | iso500  |    150 |      |
  | iso640  |    640 |      |
  | iso800  |    800 |      |
  | iso1000 |   1000 |      |
  | iso1250 |   1250 |      |
  | iso1600 |   1600 |      |
  | iso2000 |   2000 |      |
  | iso2500 |   2500 |      |
  | iso3200 |   3200 |      |
  | iso4000 |   4000 |      |
  | iso5000 |   5000 |      |
  | iso6400 |   6400 |      |

- ISO 上限 - setIsoAutoHighLimit(iso:ThetaRepository.IsoAutoHighLimitEnum)
  THETA V ファームウェア v2.50.1 以前、THETA S、THETA SC では指定しても無視される

  | 値      | ISO 上限値 | 備考 |
  | ------- | ---------: | ---- |
  | iso100  |        100 |      |
  | iso125  |        125 |      |
  | iso160  |        160 |      |
  | iso200  |        200 |      |
  | iso250  |        250 |      |
  | iso320  |        320 |      |
  | iso400  |        400 |      |
  | iso500  |        150 |      |
  | iso640  |        640 |      |
  | iso800  |        800 |      |
  | iso1000 |       1000 |      |
  | iso1250 |       1250 |      |
  | iso1600 |       1600 |      |
  | iso2000 |       2000 |      |
  | iso2500 |       2500 |      |
  | iso3200 |       3200 |      |
  | iso4000 |       4000 |      |
  | iso5000 |       5000 |      |
  | iso6400 |       6400 |      |

- 絞り設定 - setAperture(aperture:ThetaRepository.ApertureEnum)

  | 値           | 設定値 | 備考 |
  | ------------ | -----: | ---- |
  | apertureAuto |   自動 |      |
  | aperture20   |   2.0f |      |
  | aperture21   |   2.1f |      |
  | aperture24   |   2.4f |      |
  | aperture35   |   3.5f |      |
  | aperture56   |   5.6f |      |

- 色温度設定 - setColorTemperature(kelvin:Int)

  - 2500 ~ 10000

- GPS 情報 - setGpsInfo(gpsInfo:ThetaRepository.GpsInfo)
  GpsInfo は以下の内容で作成する。
  ThetaRepository.GpsInfo(latitude:longitude:altitude:dateTimeZone:)

  | 値           | 設定値   | 備考         |
  | ------------ | -------- | ------------ |
  | latitude     | 緯度     | 65535 でオフ |
  | longitude    | 経度     | 65535 でオフ |
  | altitude     | 高度     |              |
  | dateTimeZone | 日付時刻 |              |

- ホワイトバランス - setWhiteBalance(whiteBalance:ThetaRepository.WhiteBalanceEnum)

  | 値                   | 設定値                                                 | 備考                                                                               |
  | -------------------- | ------------------------------------------------------ | ---------------------------------------------------------------------------------- |
  | auto                 | 自動                                                   |                                                                                    |
  | daylight             | Outdoor                                                | 約 5,200,000                                                                       |
  | shade                | Shade                                                  | 約 7,000,000                                                                       |
  | cloudyDaylight       | Cloudy                                                 | 約 6,000,000                                                                       |
  | incandescent         | Incandescent light 1                                   | 約 3,200,000                                                                       |
  | warmWhiteFluorescent | Incandescent light 2                                   |                                                                                    |
  | daylightFluorescent  | Fluorescent light 1(daylight)                          |                                                                                    |
  | daywhiteFluorescent  | Fluorescent light 2(natural white)                     |                                                                                    |
  | fluorescent          | Fluorescent light 3 (white)                            | 約 4,000,000                                                                       |
  | bulbFluorescent      | Fluorescent light 4 (light bulb color)                 |                                                                                    |
  | colorTemperature     | CT settings (specified by the colorTemperature option) | RICOH THETA S firmware v01.82 or later and RICOH THETA SC firmware v01.10 or later |
  | underwater           | Underwater                                             | RICOH THETA V firmware v3.21.1 or later                                            |

## プレビューを表示する

プレビューは equirectangular 形式の motion JPEG です。

| 機種     | 横(pixel) | 縦(pixel) | フレームレート(fps) | 備考                        |
| -------- | --------: | --------: | ------------------: | --------------------------- |
| THETA X  |      1024 |       512 |                  30 |                             |
| THETA Z1 |      1024 |       512 |                  30 |                             |
| THETA V  |      1024 |       512 |                  30 | ファームウェア v2.21.1 以降 |
| THETA V  |      1024 |       512 |                   8 | ファームウェア v2.20.1 以前 |
| THETA S  |       640 |       320 |                  10 |                             |
| THETA SC |       640 |       320 |                  10 |                             |

`getLivePreview(frameHandler:completionHandler:)`を呼ぶと、プレビューの各フレームの受信が完了する度に、コールバック関数が呼ばれます。コールバック関数の引数には[`Ktor_ByteReadPacket`](https://api.ktor.io/ktor-io/io.ktor.utils.io.core/-byte-read-packet/index.html)オブジェクトと処理結果を返すコールバック関数が渡されます。`Ktor_ioByteReadPacket`オブジェクトからフレームデータ(JPEG 画像)を抽出するには、`PlatformKt.frameFrom(byteReadPacket:)`を利用します。この関数の戻り値は`Data`オブジェクト(jpeg データ)で`UIImage(data:)`などを使ってデコードして表示することができます。
プレビューを継続する場合は frameHandler コールバック関数内で、処理結果に`true`を設定し、プレビューを終了する場合は、`false`を設定します。
なお、CPU の利用比率が高く長時間プレビュー表示を続けますとシステムが負荷を検出しアプリケーションを終了させてしまう場合があります。そのような場合は、フレームレートを調整(概ね 10fps 程度)して CPU に負荷を掛けないように`Data`を抽出する前にフレームを捨てるようにします。
フレームを受け取る例は以下の通りです。

```swift
class FrameHandler: KotlinSuspendFunction1 {
  static let FrameInterval = CFTimeInterval(1.0/10.0)
  var last: CFTimeInterval = 0
  let handler: (_ frame: Data) -> Bool

  init(_ handler: @escaping (_ frame: Data) -> Bool) {
    self.handler = handler
  }

  func invoke(p1: Any?) async throws -> Any? {
    let now = CACurrentMediaTime()
    if (now - last < Self.FrameInterval) {
      // drop frame
      return true
    }
    autoreleasepool {
      // extract jpeg data from ByteReadPacket
      let data = PlatformKt.frameFrom(
        byteReadPacket: p1 as! Ktor_ioByteReadPacket
      )
      // draw frame and set result
      return handler(data)
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

## カメラを設定する

カメラを設定するには、設定したい内容を`ThetaRepository.Options`に設定して`ThetaRepository.SetOptions(options:)`を呼び出します。

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

`Options`に設定できる項目と内容は以下を参照してください。

- 日付時刻 - dateTimeZone:String

  形式: YYYY:MM:DD hh:mm:ss+(-)hh:mm
  hh 0-23、+(-)hh:mm は、タイムゾーン
  例: 2014:05:18 01:04:29+08:00

- 露出補正 - exposureCompensation:ThetaRepository.ExposureCompensationEnum

  | 値   | 補正値 | 備考       |
  | ---- | -----: | ---------- |
  | m20  |  -2.0f |            |
  | m17  |  -1.7f |            |
  | m13  |  -1.0f |            |
  | m07  |  -0.7f |            |
  | m03  |  -0.3f |            |
  | zero |   0.0f | デフォルト |
  | p03  |   0.3f |            |
  | p07  |   0.7f |            |
  | p13  |   1.0f |            |
  | p17  |   1.7f |            |
  | p20  |   2.0f |            |

- 露出遅延設定 - exposureDelay: ThetaRepository.ExposureDelayEnum
  takePicture/startCapture コマンドと露出開始間の遅延時間(=セルフタイマー)

  | 値       | 遅延時間(秒) | 備考       |
  | -------- | -----------: | ---------- |
  | delayOff |            0 | デフォルト |
  | delay1   |            1 |            |
  | delay2   |            2 |            |
  | delay3   |            3 |            |
  | delay4   |            4 |            |
  | delay5   |            5 |            |
  | delay6   |            6 |            |
  | delay7   |            7 |            |
  | delay8   |            8 |            |
  | delay9   |            9 |            |
  | delay10  |           10 |            |

- 露出プログラム - exposureProgram:ThetaRepository.ExposureProgramEnum

  | 値               | 内容             | 備考 |
  | ---------------- | ---------------- | ---- |
  | manual           | 手動             |      |
  | normalProgram    | 通常のプログラム |      |
  | aperturePriority | 絞り優先         |      |
  | shutterPriority  | シャッター優先   |      |
  | isoPriority      | ISO 優先         |      |

- ファイルフォーマット - fileFormat:ThetaRepository.FileFormatEnum

  | 値       | タイプ |    幅 | 高さ |  S  | SC  |  V  | Z1  |  X  |
  | -------- | ------ | ----: | ---: | :-: | :-: | :-: | :-: | :-: |
  | image2k  | jpeg   |  2048 | 1024 |  ○  |  ○  |  ×  |  ×  |  ×  |
  | image5k  | jpeg   |  5376 | 2688 |  ○  |  ○  |  ○  |  ×  |  ×  |
  | image67k | jpeg   |  6720 | 3360 |  ×  |  ×  |  ×  |  ○  |  ×  |
  | rawP67k  | raw+   |  6720 | 3360 |  ×  |  ×  |  ×  |  ○  |  ×  |
  | image55k | jpeg   |  5504 | 2752 |  ×  |  ×  |  ×  |  ×  |  ○  |
  | image11k | jpeg   | 11008 | 5504 |  ×  |  ×  |  ×  |  ×  |  ○  |

  | 値          | タイプ |   幅 | 高さ | フレームレート | Codec            |  S  | SC  |  V  | Z1  |  X  |
  | ----------- | ------ | ---: | ---: | -------------: | ---------------- | :-: | :-: | :-: | :-: | :-: |
  | videoHd     | mp4    | 1280 |  570 |                |                  |  ○  |  ○  |  ×  |  ×  |  ×  |
  | videoFullHd | mp4    | 1920 | 1080 |                |                  |  ○  |  ○  |  ×  |  ×  |  ×  |
  | video2k     | mp4    | 1920 |  960 |                | H.264/MPEG-4 AVC |  ×  |  ×  |  ○  |  ○  |  ×  |
  | video4k     | mp4    | 3840 | 1920 |                | H.264/MPEG-4 AVC |  ×  |  ×  |  ○  |  ○  |  ×  |
  | video2k30f  | mp4    | 1920 |  960 |             30 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | video2k60f  | mp4    | 1920 |  960 |             60 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | video4k30f  | mp4    | 3840 | 1920 |             30 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | video4k60f  | mp4    | 3840 | 1920 |             60 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | video57k2f  | mp4    | 5760 | 2880 |              2 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | video57k5f  | mp4    | 5760 | 2880 |              5 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | video57k30f | mp4    | 5760 | 2880 |             30 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | video7k2f   | mp4    | 7680 | 3840 |              2 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | video7k5f   | mp4    | 7680 | 3840 |              5 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | video7k10f  | mp4    | 7680 | 3840 |             10 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |

- 画像処理 - filter:ThetaRepository.Filter

  | 値             | 内容       | 備考       |
  | -------------- | ---------- | ---------- |
  | off            | なし       |            |
  | noiseReduction | ノイズ軽減 |            |
  | hdr            | HDR        | デフォルト |

- GPS オン/オフ - isGpsOn:Bool
  THETA X 以外は指定しても無視される

  | 値    | 内容     | 備考       |
  | ----- | -------- | ---------- |
  | true  | GPS あり | デフォルト |
  | false | GPS なし |            |

- ISO 値 - iso:ThetaRepository.IsoEnum

  | 値      | ISO 値 | 備考 |
  | ------- | -----: | ---- |
  | isoAuto |      0 |      |
  | iso50   |     50 |      |
  | iso64   |     64 |      |
  | iso80   |     80 |      |
  | iso100  |    100 |      |
  | iso125  |    125 |      |
  | iso160  |    160 |      |
  | iso200  |    200 |      |
  | iso250  |    250 |      |
  | iso320  |    320 |      |
  | iso400  |    400 |      |
  | iso500  |    150 |      |
  | iso640  |    640 |      |
  | iso800  |    800 |      |
  | iso1000 |   1000 |      |
  | iso1250 |   1250 |      |
  | iso1600 |   1600 |      |
  | iso2000 |   2000 |      |
  | iso2500 |   2500 |      |
  | iso3200 |   3200 |      |
  | iso4000 |   4000 |      |
  | iso5000 |   5000 |      |
  | iso6400 |   6400 |      |

- ISO 上限 - isoAutoHighLimit:ThetaRepository.IsoAutoHighLimitEnum
  THETA V ファームウェア v2.50.1 以前、THETA S、THETA SC では指定しても無視される

  | 値      | ISO 上限値 | 備考 |
  | ------- | ---------: | ---- |
  | iso100  |        100 |      |
  | iso125  |        125 |      |
  | iso160  |        160 |      |
  | iso200  |        200 |      |
  | iso250  |        250 |      |
  | iso320  |        320 |      |
  | iso400  |        400 |      |
  | iso500  |        150 |      |
  | iso640  |        640 |      |
  | iso800  |        800 |      |
  | iso1000 |       1000 |      |
  | iso1250 |       1250 |      |
  | iso1600 |       1600 |      |
  | iso2000 |       2000 |      |
  | iso2500 |       2500 |      |
  | iso3200 |       3200 |      |
  | iso4000 |       4000 |      |
  | iso5000 |       5000 |      |
  | iso6400 |       6400 |      |

- 絞り設定 - aperture:ThetaRepository.ApertureEnum

  | 値           | 設定値 | 備考 |
  | ------------ | -----: | ---- |
  | apertureAuto |   自動 |      |
  | aperture20   |   2.0f |      |
  | aperture21   |   2.1f |      |
  | aperture24   |   2.4f |      |
  | aperture35   |   3.5f |      |
  | aperture56   |   5.6f |      |

- 色温度設定 - colorTemperature:Int

  - 2500 ~ 10000

- GPS 情報 - gpsInfo:ThetaRepository.GpsInfo
  GpsInfo は以下の内容で作成する。
  ThetaRepository.GpsInfo(latitude:longitude:altitude:dateTimeZone:)

  | 値           | 設定値   | 備考         |
  | ------------ | -------- | ------------ |
  | latitude     | 緯度     | 65535 でオフ |
  | longitude    | 経度     | 65535 でオフ |
  | altitude     | 高度     |              |
  | dateTimeZone | 日付時刻 |              |

- ホワイトバランス - whiteBalance:ThetaRepository.WhiteBalanceEnum

  | 値                   | 設定値                                                 | 備考                                                                               |
  | -------------------- | ------------------------------------------------------ | ---------------------------------------------------------------------------------- |
  | auto                 | 自動                                                   |                                                                                    |
  | daylight             | Outdoor                                                | 約 5,200,000                                                                       |
  | shade                | Shade                                                  | 約 7,000,000                                                                       |
  | cloudyDaylight       | Cloudy                                                 | 約 6,000,000                                                                       |
  | incandescent         | Incandescent light 1                                   | 約 3,200,000                                                                       |
  | warmWhiteFluorescent | Incandescent light 2                                   |                                                                                    |
  | daylightFluorescent  | Fluorescent light 1(daylight)                          |                                                                                    |
  | daywhiteFluorescent  | Fluorescent light 2(natural white)                     |                                                                                    |
  | fluorescent          | Fluorescent light 3 (white)                            | 約 4,000,000                                                                       |
  | bulbFluorescent      | Fluorescent light 4 (light bulb color)                 |                                                                                    |
  | colorTemperature     | CT settings (specified by the colorTemperature option) | RICOH THETA S firmware v01.82 or later and RICOH THETA SC firmware v01.10 or later |
  | underwater           | Underwater                                             | RICOH THETA V firmware v3.21.1 or later                                            |

- 最大録画時間設定 - maxRecordableTime:ThetaRepository.MaxRecordableTimeEnum

  | 値                 | 内容    | 備考 |
  | ------------------ | ------- | ---- |
  | recordableTime300  | 300 秒  |      |
  | recordableTime1500 | 1500 秒 |      |

- キャプチャモード - captureMode:ThetaRepository.CaptureModeEnum

  | 値    | 内容             | 備考 |
  | ----- | ---------------- | ---- |
  | image | 静止画撮影モード |      |
  | video | 動画撮影モード   |      |

- 言語設定 - language:ThetaRepository.LanguageEnum
  (THETA S, THETA SC では指定しても無視される)

  | 値   | 内容       | 備考 |
  | ---- | ---------- | ---- |
  | enUs | 米国英語   |      |
  | enGb | 英国英語   |      |
  | ja   | 日本語     |      |
  | fr   | フランス語 |      |
  | de   | ドイツ語   |      |
  | zhTw | 中国語台湾 |      |
  | zhCn | 中国語     |      |
  | it   | イタリア語 |      |
  | ko   | 韓国語     |      |

- スリープモードに入るまでの時間(秒) - sleepDelay:ThetaRepository.SleepDelayEnum/SleepDelaySec

  - ThetaRepository.SleepDelayEnum

    | 値             | 設定値(秒) | 備考      |
    |---------------|-------:|---------|
    | disable       |  65535 | 自動オフしない |
    | sleepDelay3m  |    180 |         |
    | sleepDelay5m  |    300 |         |
    | sleepDelay7m  |    420 |         |
    | sleepDelay10m |    600 |         |

  - ThetaRepository.SleepDelaySec(sec:Int)
    - RICOH THETA V以降の場合
      - 60 ～ 65534、または 65535 (スリープ モードを無効にする場合)。
    - RICOH THETA SまたはSCの場合
      - 30 ～ 1800、または 65535 (スリープ モードを無効にする場合)。

- スリープから自動電源オフまでの時間(秒) offDelay:ThetaRepository.OffDelayEnum/OffDelaySec

  - ThetaRepository.OffDelayEnum

    | 値           | 設定値(秒) | 備考      |
    |-------------|-------:|---------|
    | disable     |  65535 | 自動オフしない |
    | offDelay5m  |    300 |         |
    | offDelay10m |    600 |         |
    | offDelay15m |    900 |         |
    | offDelay30m |   1800 |         |

  - ThetaRepository.OffDelaySec(sec:Int)
    - RICOH THETA V以降の場合
      - 0、または600以上2592000以下（単位：秒）の60の倍数、または65535。65535を設定・取得した場合は0を返します（自動電源オフを無効にする場合）。
    - RICOH THETA SまたはSCの場合
      - 30 ～ 1800、または 65535 (自動電源オフを無効にする場合)。

- シャッター音 - shutterVolume:Int
  - 0 - 100

## カメラの設定を取得する

カメラの設定を取得するには、取得したいオプションの一覧を指定して`ThetaRepository.getOptions(optionNames:)`を呼び出します。

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

- ThetaRepository.OptionNameEnum は以下の表を参照してください

  | 値                    | 意味                 | 型                                           |
  | --------------------- | -------------------- | -------------------------------------------- |
  | aperture              | 絞り                 | ThetaRepository.ApertureEnum                 |
  | capturemode           | キャプチャーモード   | ThetaRepository.CaptureModeEnum              |
  | colortemperature      | 色温度               | Int                                          |
  | datetimezone          | 日時                 | String                                       |
  | exposurecompensation  | 露出補正             | ThetaRepository.ExposureCompensationEnum     |
  | exposuredelay         | 露出遅延時間         | ThetaRepository.ExposureDelayEnum            |
  | exposureprogram       | 露出プログラム       | ThetaRepository.ExposureProgram              |
  | fileformat            | ファイルフォーマット | ThetaRepository.FileFormatEnum               |
  | filter                | 画像フィルタ         | ThetaRepository.FilterEnum                   |
  | gpsinfo               | GPS 情報             | ThetaRepository.GpsInfo                      |
  | isgpson               | GPS フラグ           | Bool                                         |
  | iso                   | ISO 値               | ThetaRepository.IsoEnum                      |
  | isoautohighlimit      | ISO 上限             | ThetaRepository.IsoAutoHighLimitEnum         |
  | language              | 言語                 | ThetaRepository.LanguageEnum                 |
  | maxrecordabletime     | 最長録画時間         | ThetaRepository.MaxRecordableTimeEnum        |
  | offdelay              | 電源オフ時間         | ThetaRepository.OffDelayEnum,OffDelaySec     |
  | sleepdelay            | スリープ時間         | ThetaRepository.SleepDelayEnum,SleepDelaySec |
  | remainingpictures     | 残り画像数           | Int                                          |
  | remainingvideoseconds | 残り録画秒数         | Int                                          |
  | remainingspace        | 残領域               | Long                                         |
  | totalspace            | 合計領域             | Long                                         |
  | shuttervolume         | シャッター音量       | Int                                          |
  | whitebalance          | ホワイトバランス     | ThetaRepository.WhiteBalanceEnum             |

## THETA 内の静止画・動画を一覧する

THETA 内の静止画（JPEG ファイル）や動画（MP4 ファイル）の一覧は`ThetaRepository.listFiles(fileType:startPosition:entryCount:)`を使って取得できます。
`fileType`は`ThetaRepository.FileTypeEnum`型で内容は以下の通りです。
`ThetaRepository.listFiles()`の戻り値型は`ThetaRepository.ThetaFiles`で、`ThetaFiles`のプロパティ`fileList` が THETA 内のファイル一覧です。
`fileList`は `ThetaRepository.FileInfo`のリストです。

- ThetaRepository.FileTypeEnum

  | 値    | 内容                          |
  | ----- | ----------------------------- |
  | image | 静止画（JPEG ファイル）を一覧 |
  | video | 動画（MP4 ファイル）を一覧    |
  | all   | 全てのファイルを一覧          |

- ThetaRepository.ThetaFiles

  | Property name | Type                       | Contents                                                                                                                                      |
  | ------------- | -------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
  | fileList      | [ThetaRepository.FileInfo] | THETA 内のファイル一覧                                                                                                                        |
  | totalEntries  | Int32                      | THETA 内のファイル数 ([api spec](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.list_files.md)参照) |

- ThetaRepository.FileInfo

  | プロパティ名 | 型     | 内容                                      |
  | ------------ | ------ | ----------------------------------------- |
  | name         | String | ファイル名を表します                      |
  | size         | Long   | ファイルサイズ（バイト数）を表します      |
  | dateTime     | String | 撮影日時（YYYY:MM:DD HH:MM:SS）を表します |
  | fileUrl      | String | ファイルの URL を表します                 |
  | thumbnailUrl | String | サムネールの URL を表します               |

```swift
Task {
  do {
    let listFiles: [ThetaRepository.FileInfo] =
      try await withCheckedThrowingContinuation {continuation in
        theta.listFiles(fileType:ThetaRepository.FileTypeEnum.image,
                        startPosition: 0,
                        entryCount: 100) {response, error in
          if let resp = response {
            continuation.resume(returning: resp.fileList)
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

## 静止画・動画を取得する

撮影した静止画(JPEG ファイル）や動画（MP4 ファイル)の URL から URLSession を使ってデータを取得します。以下のような関数を使えば Data としてダウンロードできます。

```swift
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

## サムネイルを取得する

THETA 内のファイルのサムネイルは、`ThetaRepository.listFiles`を使って取得した `ThetaRepository.FileInfo`の`thumbnailUrl`を使って以下のようにダウンロードすることができます。

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

## プレビューを表示する

プレビューは equirectangular 形式の motion JPEG です。

それぞれのフレーム処理を行うコールバック関数を引数として
`ThetaRepository.getPreview(frameHandler:completionHandler:)`を呼び出します。プレビューの取得を継続する場合はコールバック関数の返り値を`true`、終了する場合は`false`とします。コールバック関数は、前述の FrameHandler を利用します。

```swift
theta.getPreview(
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

## THETA をリセットする

接続している Theta をリセットするには、`ThetaRepository.reset(completionHandler:)`を呼び出します。

```swift
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

## THETA の情報を取得する

接続している Theta の情報を取得するには、`ThetaRepository.getThetaInfo(completionHandler:)`を呼び出します。呼び出しに成功すると`ThetaRepository.ThetaInfo`を取得できます。このデータは以下のような情報を含みます。

- ThetaRepository.ThetaInfo

  | プロパティ名    | 型      | 内容                                     |
  | --------------- | ------- | ---------------------------------------- |
  | firmwareVersion | String  | ファームウェアバージョンを表します       |
  | hasGps          | Bool    | GPS 機能を持っているかどうかを表します   |
  | hasGyro         | Bool    | ジャイロを持っているかどうかを表します   |
  | model           | String  | Theta の型番を表します                   |
  | serialNumber    | String  | Theta のシリアル番号を表します           |
  | uptime          | Integer | Theta の電源を入れてからの秒数を表します |

```swift
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

## THETA の状態を取得する

接続している Theta の状態を取得するには、`ThetaRepository.getThetaState(completionHandler:)`を呼び出します。呼び出しに成功すると`ThetaRepository.ThetaState`を取得できます。このデータは以下のような情報を含みます。

- ThetaRepository.ThetaState

  | プロパティ名   | 型                                | 内容                                         |
  | -------------- | --------------------------------- | -------------------------------------------- |
  | batteryLevel   | float                             | バッテリーレベルを表します                   |
  | chargingState  | ThetaRepository.ChargingStateEnum | 充電状態を表します                           |
  | fingerprint    | String                            | 現在の状態ごとに一意に決まる識別子を表します |
  | isSdCard       | Bool                              | SD カードが存在するかどうかを表します        |
  | latestFileUrl  | String                            | 最後の取得したメディアの URL を表します      |
  | recordableTime | int32_t                           | 録画可能秒数を表します                       |
  | recordedTime   | int32_t                           | 録画済み秒数を表します                       |

- ThetaRepository.ChargingStateEnum

  | プロパティ名 | 内容                     |
  | ------------ | ------------------------ |
  | charging     | 充電中を表します         |
  | charged      | 充電完了を表します       |
  | notCharging  | ケーブル未接続を表します |

```swift
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
