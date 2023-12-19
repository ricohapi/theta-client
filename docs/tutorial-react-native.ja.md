# RICOH360 THETA Client チュートリアル

## 使用可能な機種

- RICOH THETA X
- RICOH THETA Z1
- RICOH THETA V
- RICOH THETA S (ファームウェア v1.62 以降のみ)
- RICOH THETA SC

## 事前準備

本 SDK を使用したアプリケーションが動作するスマートフォンと THETA を無線 LAN 接続しておきます。

## THETA Client の初期化

```Typescript
import {initialize} from 'theta-client-react-native';

initialize()
  .then(() => {
    // success
  })
  .catch(() => {
    // handle error
  });

OR

initialize('http://<IPアドレス>:<ポート番号>')
  .then(() => {
    // success
  })
  .catch(() => {
    // handle error
  });

```

- THETA IP ADDRESS

  | モード           | アドレス             |
  | ---------------- | -------------------- |
  | ダイレクトモード | 192.168.1.1          |
  | その他           | カメラの IP アドレス |

- Theta から画像や動画をダウンロードする場合は、plain な接続となりますので、接続先のアドレス（デフォルト 192.168.1.1）に応じて各プラットフォーム毎の設定が必要になります。

  - iOS: デフォルトの場合の Info.plist の例を示します。なお、Xcode の`Signing&Capabilities`→`App Transport Security Exception`でも追加設定することができます。

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

  - Android: res/xml/network_security_config.xml に記述するデフォルトの場合の例を示します。

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

```Typescript
import {
  getPhotoCaptureBuilder,
  IsoAutoHighLimitEnum,
  PhotoFileFormatEnum,
} from 'theta-client-react-native';

getPhotoCaptureBuilder()
    .setIsoAutoHighLimit(IsoAutoHighLimitEnum.ISO_1000)
    .setFileFormat(PhotoFileFormatEnum.IMAGE_5K)
    .build()
    .then((photoCapture) => {
      // success build photoCapture
    })
    .catch((error) => {
      // handle error
    });
```

上の例では ISO 感度の最大値を 1000 に、ファイルフォーマットを IMAGE_5K に設定しています。

プレビューを表示する方法は[プレビューを表示する](#プレビューを表示する)をご覧ください。

次に`PhotoCapture.takePicture()`を呼んで静止画を撮影します。

```Typescript
photoCapture.takePicture()
  .then(fileUrl => {
    // fileUrl をGETリクエストを送信してJPEGファイルを受け取る処理
  })
  .catch(error => {
    // catch error while take picture
  });

```

### 静止画撮影時に設定できる項目

- 露出補正 - setExposureCompensation(value:ExposureCompensationEnum)

  | 値    | 補正値 | 備考       |
  | ----- | -----: | ---------- |
  | M_2_0 |  -2.0f |            |
  | M_1_7 |  -1.7f |            |
  | M_1_3 |  -1.0f |            |
  | M_0_7 |  -0.7f |            |
  | M_0_3 |  -0.3f |            |
  | ZERO  |   0.0f | デフォルト |
  | P_0_3 |   0.3f |            |
  | P_0_7 |   0.7f |            |
  | P_1_3 |   1.0f |            |
  | P_1_7 |   1.7f |            |
  | P_2_0 |   2.0f |            |

- 露出遅延設定 - setExposureDelay(delay:ExposureDelayEnum)
  takePicture コマンドと露出開始間の遅延時間(=セルフタイマー)

  | 値        | 遅延時間(秒) | 備考       |
  | --------- | -----------: | ---------- |
  | DELAY_OFF |            0 | デフォルト |
  | DELAY_1   |            1 |            |
  | DELAY_2   |            2 |            |
  | DELAY_3   |            3 |            |
  | DELAY_4   |            4 |            |
  | DELAY_5   |            5 |            |
  | DELAY_6   |            6 |            |
  | DELAY_7   |            7 |            |
  | DELAY_8   |            8 |            |
  | DELAY_9   |            9 |            |
  | DELAY_10  |           10 |            |

- 露出プログラム - setExposureProgram(program:ExposureProgramEnum)

  | 値                | 内容             | 備考 |
  | ----------------- | ---------------- | ---- |
  | MANUAL            | 手動             |      |
  | NORMAL_PROGRAM    | 通常のプログラム |      |
  | APERTURE_PRIORITY | 絞り優先         |      |
  | SHUTTER_PRIORITY  | シャッター優先   |      |
  | ISO_PRIORITY      | ISO 優先         |      |

- ファイルフォーマット - setFileFormat(fileFormat:PhotoFileFormatEnum)

  | 値         | タイプ |    幅 | 高さ |  S  | SC  |  V  | Z1  |  X  |
  | ---------- | ------ | ----: | ---: | :-: | :-: | :-: | :-: | :-: |
  | IMAGE_2K   | jpeg   |  2048 | 1024 |  ○  |  ○  |  ×  |  ×  |  ×  |
  | IMAGE_5K   | jpeg   |  5376 | 2688 |  ○  |  ○  |  ○  |  ×  |  ×  |
  | IMAGE_6_7K | jpeg   |  6720 | 3360 |  ×  |  ×  |  ×  |  ○  |  ×  |
  | RAW_P_6_7K | raw+   |  6720 | 3360 |  ×  |  ×  |  ×  |  ○  |  ×  |
  | IMAGE_5_5K | jpeg   |  5504 | 2752 |  ×  |  ×  |  ×  |  ×  |  ○  |
  | IMAGE_11K  | jpeg   | 11008 | 5504 |  ×  |  ×  |  ×  |  ×  |  ○  |

- 画像処理 - setFilter(filter:FilterEnum)

  | 値              | 内容       | 備考       |
  | --------------- | ---------- | ---------- |
  | OFF             | なし       |            |
  | NOISE_REDUCTION | ノイズ軽減 |            |
  | HDR             | HDR        | デフォルト |

- GPS オン/オフ - setGpsTagRecording(value:GpsTagRecordingEnum)
  THETA X 以外は指定しても無視される

  | 値  | 内容     | 備考       |
  | --- | -------- | ---------- |
  | ON  | GPS あり | デフォルト |
  | OFF | GPS なし |            |

- ISO 値 - setIso(iso:IsoEnum)

  | 値       | ISO 値 | 備考 |
  | -------- | -----: | ---- |
  | ISO_AUTO |      0 |      |
  | ISO_50   |     50 |      |
  | ISO_64   |     64 |      |
  | ISO_80   |     80 |      |
  | ISO_100  |    100 |      |
  | ISO_125  |    125 |      |
  | ISO_160  |    160 |      |
  | ISO_200  |    200 |      |
  | ISO_250  |    250 |      |
  | ISO_320  |    320 |      |
  | ISO_400  |    400 |      |
  | ISO_500  |    150 |      |
  | ISO_640  |    640 |      |
  | ISO_800  |    800 |      |
  | ISO_1000 |   1000 |      |
  | ISO_1250 |   1250 |      |
  | ISO_1600 |   1600 |      |
  | ISO_2000 |   2000 |      |
  | ISO_2500 |   2500 |      |
  | ISO_3200 |   3200 |      |
  | ISO_4000 |   4000 |      |
  | ISO_5000 |   5000 |      |
  | ISO_6400 |   6400 |      |

- ISO 上限 - setIsoAutoHighLimit(iso:IsoAutoHighLimitEnum)
  THETA V ファームウェア v2.50.1 以前、THETA S、THETA SC では指定しても無視される

  | 値       | ISO 上限値 | 備考 |
  | -------- | ---------: | ---- |
  | ISO_100  |        100 |      |
  | ISO_125  |        125 |      |
  | ISO_160  |        160 |      |
  | ISO_200  |        200 |      |
  | ISO_250  |        250 |      |
  | ISO_320  |        320 |      |
  | ISO_400  |        400 |      |
  | ISO_500  |        150 |      |
  | ISO_640  |        640 |      |
  | ISO_800  |        800 |      |
  | ISO_1000 |       1000 |      |
  | ISO_1250 |       1250 |      |
  | ISO_1600 |       1600 |      |
  | ISO_2000 |       2000 |      |
  | ISO_2500 |       2500 |      |
  | ISO_3200 |       3200 |      |
  | ISO_4000 |       4000 |      |
  | ISO_5000 |       5000 |      |
  | ISO_6400 |       6400 |      |

- 絞り設定 - setAperture(aperture:ApertureEnum)

  | 値            | 設定値 | 備考 |
  | ------------- | -----: | ---- |
  | APERTURE_AUTO |   自動 |      |
  | APERTURE_20   |   2.0f |      |
  | APERTURE_21   |   2.1f |      |
  | APERTURE_24   |   2.4f |      |
  | APERTURE_35   |   3.5f |      |
  | APERTURE_56   |   5.6f |      |

- 色温度設定 - setColorTemperature(kelvin:number)

  - 2500 ~ 10000

- GPS 情報 - setGpsInfo(gpsInfo:GpsInfo)
  GpsInfo は以下の内容の Object として作成する。

  | 値           | 設定値   | 備考         |
  | ------------ | -------- | ------------ |
  | latitude     | 緯度     | 65535 でオフ |
  | longitude    | 経度     | 65535 でオフ |
  | altitude     | 高度     |              |
  | dateTimeZone | 日付時刻 |              |

- ホワイトバランス - setWhiteBalance(whiteBalance:WhiteBalanceEnum)

  | 値                     | 設定値                                                 | 備考                                                                               |
  | ---------------------- | ------------------------------------------------------ | ---------------------------------------------------------------------------------- |
  | AUTO                   | 自動                                                   |                                                                                    |
  | DAYLIGHT               | Outdoor                                                | 約 5,200,000                                                                       |
  | SHADE                  | Shade                                                  | 約 7,000,000                                                                       |
  | CLOUDY_DAYLIGHT        | Cloudy                                                 | 約 6,000,000                                                                       |
  | INCANDESCENT           | Incandescent light 1                                   | 約 3,200,000                                                                       |
  | WARM_WHITE_FLUORESCENT | Incandescent light 2                                   |                                                                                    |
  | DAYLIGHT_FLUORESCENT   | Fluorescent light 1(daylight)                          |                                                                                    |
  | DAYWHITE_FLUORESCENT   | Fluorescent light 2(natural white)                     |                                                                                    |
  | FLUORESCENT            | Fluorescent light 3 (white)                            | 約 4,000,000                                                                       |
  | BULB_FLUORESCENT       | Fluorescent light 4 (light bulb color)                 |                                                                                    |
  | COLOR_TEMPERATURE      | CT settings (specified by the colorTemperature option) | RICOH THETA S firmware v01.82 or later and RICOH THETA SC firmware v01.10 or later |
  | UNDERWATER             | Underwater                                             | RICOH THETA V firmware v3.21.1 or later                                            |

## 動画を撮影する

まず`getVideoCaptureBuilder()`を使って撮影設定を行い、`VideoCapture`オブジェクトを生成します。

```Typescript
import {
  getVideoCaptureBuilder,
  IsoAutoHighLimitEnum,
  VideoFileFormatEnum,
} from 'theta-client-react-native';

getVideoCaptureBuilder()
  .setIsoAutoHighLimit(IsoAutoHighLimitEnum.ISO_1000)
  .setFileFormat(VideoFileFormatEnum.VIDEO_HD)
  .build()
  .then((videoCapture) => {
    // success build videoCapture
  })
  .catch((error) => {
    // handle error
  });
```

上の例では ISO 感度の最大値を 800 に、ファイルフォーマットを`VIDEO_HD`に設定しています。

表示方法は[プレビューを表示する](#プレビューを表示する)をご覧ください

次に`VideoCapture.startCapture()`を呼んで動画の撮影を開始します。

```Typescript
VideoCapture
  .startCapture((error) => {
    // handle error of stopCapture
  })
  .then((fileUrl) => {
    // send GET requests and receiving MP4 files
  })
  .catch((error) => {
    // handle error of startCapture
  });
```

次に`VideoCapture.stopCapture()`を呼び出して動画の撮影を終了します。成功すると上記の通り、撮影したファイルの URL を引数に then が呼び出されます。

```Typescript
videoCapture.stopCapture();
```

### 動画撮影時に設定できる項目

- 露出補正 - setExposureCompensation(value:ExposureCompensationEnum)

  | 値    | 補正値 | 備考       |
  | ----- | -----: | ---------- |
  | M_2_0 |  -2.0f |            |
  | M_1_7 |  -1.7f |            |
  | M_1_3 |  -1.0f |            |
  | M_0_7 |  -0.7f |            |
  | M_0_3 |  -0.3f |            |
  | ZERO  |   0.0f | デフォルト |
  | P_0_3 |   0.3f |            |
  | P_0_7 |   0.7f |            |
  | P_1_3 |   1.0f |            |
  | P_1_7 |   1.7f |            |
  | P_2_0 |   2.0f |            |

- 露出遅延設定 - setExposureDelay(delay:ExposureDelayEnum)
  takePicture コマンドと露出開始間の遅延時間(=セルフタイマー)

  | 値        | 遅延時間(秒) | 備考       |
  | --------- | -----------: | ---------- |
  | DELAY_OFF |            0 | デフォルト |
  | DELAY_1   |            1 |            |
  | DELAY_2   |            2 |            |
  | DELAY_3   |            3 |            |
  | DELAY_4   |            4 |            |
  | DELAY_5   |            5 |            |
  | DELAY_6   |            6 |            |
  | DELAY_7   |            7 |            |
  | DELAY_8   |            8 |            |
  | DELAY_9   |            9 |            |
  | DELAY_10  |           10 |            |

- 露出プログラム - setExposureProgram(program:ExposureProgramEnum)

  | 値                | 内容             | 備考 |
  | ----------------- | ---------------- | ---- |
  | MANUAL            | 手動             |      |
  | NORMAL_PROGRAM    | 通常のプログラム |      |
  | APERTURE_PRIORITY | 絞り優先         |      |
  | SHUTTER_PRIORITY  | シャッター優先   |      |
  | ISO_PRIORITY      | ISO 優先         |      |

- ファイルフォーマット - setFileFormat(fileFormat:VideoFileFormatEnum)

  | 値             | タイプ |   幅 | 高さ | フレームレート | Codec            |  S  | SC  |  V  | Z1  |  X  |
  | -------------- | ------ | ---: | ---: | -------------: | ---------------- | :-: | :-: | :-: | :-: | :-: |
  | VIDEO_HD       | mp4    | 1280 |  570 |                |                  |  ○  |  ○  |  ×  |  ×  |  ×  |
  | VIDEO_FULL_Hd  | mp4    | 1920 | 1080 |                |                  |  ○  |  ○  |  ×  |  ×  |  ×  |
  | VIDEO_2K       | mp4    | 1920 |  960 |                | H.264/MPEG-4 AVC |  ×  |  ×  |  ○  |  ○  |  ×  |
  | VIDEO_4K       | mp4    | 3840 | 1920 |                | H.264/MPEG-4 AVC |  ×  |  ×  |  ○  |  ○  |  ×  |
  | VIDEO_2K_30F   | mp4    | 1920 |  960 |             30 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | VIDEO_2K_60F   | mp4    | 1920 |  960 |             60 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | VIDEO_4K_30F   | mp4    | 3840 | 1920 |             30 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | VIDEO_4K_60F   | mp4    | 3840 | 1920 |             60 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | VIDEO_5_7K_2F  | mp4    | 5760 | 2880 |              2 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | VIDEO_5_7K_5F  | mp4    | 5760 | 2880 |              5 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | VIDEO_5_7K_30F | mp4    | 5760 | 2880 |             30 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | VIDEO_7K_2F    | mp4    | 7680 | 3840 |              2 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | VIDEO_7K_5F    | mp4    | 7680 | 3840 |              5 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | VIDEO_7K_10F   | mp4    | 7680 | 3840 |             10 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |

- 最大録画時間設定 - setMaxRecordableTime(time:MaxRecordableTimeEnum)

  | 値                  | 内容    | 備考 |
  | ------------------- | ------- | ---- |
  | RECORDABLE_TIME300  | 300 秒  |      |
  | RECORDABLE_TIME1500 | 1500 秒 |      |

- GPS オン/オフ - setGpsTagRecording(value:GpsTagRecordingEnum)
  THETA X 以外は指定しても無視される

  | 値  | 内容     | 備考       |
  | --- | -------- | ---------- |
  | ON  | GPS あり | デフォルト |
  | OFF | GPS なし |            |

- ISO 値 - setIso(iso:IsoEnum)

  | 値       | ISO 値 | 備考 |
  | -------- | -----: | ---- |
  | ISO_AUTO |      0 |      |
  | ISO_50   |     50 |      |
  | ISO_64   |     64 |      |
  | ISO_80   |     80 |      |
  | ISO_100  |    100 |      |
  | ISO_125  |    125 |      |
  | ISO_160  |    160 |      |
  | ISO_200  |    200 |      |
  | ISO_250  |    250 |      |
  | ISO_320  |    320 |      |
  | ISO_400  |    400 |      |
  | ISO_500  |    150 |      |
  | ISO_640  |    640 |      |
  | ISO_800  |    800 |      |
  | ISO_1000 |   1000 |      |
  | ISO_1250 |   1250 |      |
  | ISO_1600 |   1600 |      |
  | ISO_2000 |   2000 |      |
  | ISO_2500 |   2500 |      |
  | ISO_3200 |   3200 |      |
  | ISO_4000 |   4000 |      |
  | ISO_5000 |   5000 |      |
  | ISO_6400 |   6400 |      |

- ISO 上限 - setIsoAutoHighLimit(iso:IsoAutoHighLimitEnum)
  THETA V ファームウェア v2.50.1 以前、THETA S、THETA SC では指定しても無視される

  | 値       | ISO 上限値 | 備考 |
  | -------- | ---------: | ---- |
  | ISO_100  |        100 |      |
  | ISO_125  |        125 |      |
  | ISO_160  |        160 |      |
  | ISO_200  |        200 |      |
  | ISO_250  |        250 |      |
  | ISO_320  |        320 |      |
  | ISO_400  |        400 |      |
  | ISO_500  |        150 |      |
  | ISO_640  |        640 |      |
  | ISO_800  |        800 |      |
  | ISO_1000 |       1000 |      |
  | ISO_1250 |       1250 |      |
  | ISO_1600 |       1600 |      |
  | ISO_2000 |       2000 |      |
  | ISO_2500 |       2500 |      |
  | ISO_3200 |       3200 |      |
  | ISO_4000 |       4000 |      |
  | ISO_5000 |       5000 |      |
  | ISO_6400 |       6400 |      |

- 絞り設定 - setAperture(aperture:ApertureEnum)

  | 値            | 設定値 | 備考 |
  | ------------- | -----: | ---- |
  | APERTURE_AUTO |   自動 |      |
  | APERTURE_20   |   2.0f |      |
  | APERTURE_21   |   2.1f |      |
  | APERTURE_24   |   2.4f |      |
  | APERTURE_35   |   3.5f |      |
  | APERTURE_56   |   5.6f |      |

- 色温度設定 - setColorTemperature(kelvin:number)

  - 2500 ~ 10000

- GPS 情報 - setGpsInfo(gpsInfo:GpsInfo)
  GpsInfo は以下の内容の Object として作成する。

  | 値           | 設定値   | 備考         |
  | ------------ | -------- | ------------ |
  | latitude     | 緯度     | 65535 でオフ |
  | longitude    | 経度     | 65535 でオフ |
  | altitude     | 高度     |              |
  | dateTimeZone | 日付時刻 |              |

- ホワイトバランス - setWhiteBalance(whiteBalance:WhiteBalanceEnum)

  | 値                     | 設定値                                                 | 備考                                                                               |
  | ---------------------- | ------------------------------------------------------ | ---------------------------------------------------------------------------------- |
  | AUTO                   | 自動                                                   |                                                                                    |
  | DAYLIGHT               | Outdoor                                                | 約 5,200,000                                                                       |
  | SHADE                  | Shade                                                  | 約 7,000,000                                                                       |
  | CLOUDY_DAYLIGHT        | Cloudy                                                 | 約 6,000,000                                                                       |
  | INCANDESCENT           | Incandescent light 1                                   | 約 3,200,000                                                                       |
  | WARM_WHITE_FLUORESCENT | Incandescent light 2                                   |                                                                                    |
  | DAYLIGHT_FLUORESCENT   | Fluorescent light 1(daylight)                          |                                                                                    |
  | DAYWHITE_FLUORESCENT   | Fluorescent light 2(natural white)                     |                                                                                    |
  | FLUORESCENT            | Fluorescent light 3 (white)                            | 約 4,000,000                                                                       |
  | BULB_FLUORESCENT       | Fluorescent light 4 (light bulb color)                 |                                                                                    |
  | COLOR_TEMPERATURE      | CT settings (specified by the colorTemperature option) | RICOH THETA S firmware v01.82 or later and RICOH THETA SC firmware v01.10 or later |
  | UNDERWATER             | Underwater                                             | RICOH THETA V firmware v3.21.1 or later                                            |

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

`getLivePreview()`を呼ぶと、プレビューの各フレームの受信が完了する度に、イベント THETA_FRAME_EVENT が発生します。
イベントデータにはフレームデータ(JPEG)の DATA URL が渡されます。
プレビューを終了する場合は`stopLivePreview()`を呼び出します。

```Typescript
import * as React from 'react';
import {NativeModules, NativeEventEmitter} from 'react-native';
import {
  getLivePreview,
  stopLivePreview,
  THETA_FRAME_EVENT,
} from 'theta-client-react-native';

const LivePreview = () => {
  [dataUrl, setDataUrl] = React.useState<string | undefined>();

  React.useEffect(() => {
    const eventListener = new NaiveEventEmitter(NativeModules.ThetaClientReactNative)
      .addListener(THETA_FRAME_EVENT, event => {
        setDataUrl(event.data);
      });
    getLivePreview()
      .then(() => {
        // preview done
      })
      .catch(() => {
        // handle error while previewing
      });
    return (() => {
      stopLivePreview();
      eventListener.remove();
    });
  }, []);

  return (
    ...略
    <Image source={{uri: dataUrl}} style=... />
    ...略
  );
};
```

## カメラを設定する

カメラを設定するには、設定したい内容を`Options`に設定して`SetOptions()`を呼び出します。

```Typescript
import {
  Options,
  setOptions,
  ApertureEnum,
} from 'theta-client-react-native';

let options: Options = {};
options.aperture = ApertureEnum.APERTURE_AUTO;
setOptions(options)
  .then(()=>{
    // done
  })
  .catch(error => {
    // handle error
  });

```

`Options`に設定できる項目と内容は以下を参照してください。

- 日付時刻 - dateTimeZone:string

  形式: YYYY:MM:DD hh:mm:ss+(-)hh:mm
  hh 0-23、+(-)hh:mm は、タイムゾーン
  例: 2014:05:18 01:04:29+08:00

- 露出補正 - exposureCompensation:ExposureCompensationEnum

  | 値    | 補正値 | 備考       |
  | ----- | -----: | ---------- |
  | M_2_0 |  -2.0f |            |
  | M_1_7 |  -1.7f |            |
  | M_1_3 |  -1.0f |            |
  | M_0_7 |  -0.7f |            |
  | M_0_3 |  -0.3f |            |
  | ZERO  |   0.0f | デフォルト |
  | P_0_3 |   0.3f |            |
  | P_0_7 |   0.7f |            |
  | P_1_3 |   1.0f |            |
  | P_1_7 |   1.7f |            |
  | P_2_0 |   2.0f |            |

- 露出遅延設定 - exposureDelay:ExposureDelayEnum
  takePicture コマンドと露出開始間の遅延時間(=セルフタイマー)

  | 値        | 遅延時間(秒) | 備考       |
  | --------- | -----------: | ---------- |
  | DELAY_OFF |            0 | デフォルト |
  | DELAY_1   |            1 |            |
  | DELAY_2   |            2 |            |
  | DELAY_3   |            3 |            |
  | DELAY_4   |            4 |            |
  | DELAY_5   |            5 |            |
  | DELAY_6   |            6 |            |
  | DELAY_7   |            7 |            |
  | DELAY_8   |            8 |            |
  | DELAY_9   |            9 |            |
  | DELAY_10  |           10 |            |

- 露出プログラム - exposureProgram:ExposureProgramEnum

  | 値                | 内容             | 備考 |
  | ----------------- | ---------------- | ---- |
  | MANUAL            | 手動             |      |
  | NORMAL_PROGRAM    | 通常のプログラム |      |
  | APERTURE_PRIORITY | 絞り優先         |      |
  | SHUTTER_PRIORITY  | シャッター優先   |      |
  | ISO_PRIORITY      | ISO 優先         |      |

- ファイルフォーマット - fileFormat:PhotoFileFormatEnum|VideoFileFormatEnum

  - PhotoFileFormatEnum

  | 値         | タイプ |    幅 | 高さ |  S  | SC  |  V  | Z1  |  X  |
  | ---------- | ------ | ----: | ---: | :-: | :-: | :-: | :-: | :-: |
  | IMAGE_2K   | jpeg   |  2048 | 1024 |  ○  |  ○  |  ×  |  ×  |  ×  |
  | IMAGE_5K   | jpeg   |  5376 | 2688 |  ○  |  ○  |  ○  |  ×  |  ×  |
  | IMAGE_6_7K | jpeg   |  6720 | 3360 |  ×  |  ×  |  ×  |  ○  |  ×  |
  | RAW_P_6_7K | raw+   |  6720 | 3360 |  ×  |  ×  |  ×  |  ○  |  ×  |
  | IMAGE_5_5K | jpeg   |  5504 | 2752 |  ×  |  ×  |  ×  |  ×  |  ○  |
  | IMAGE_11K  | jpeg   | 11008 | 5504 |  ×  |  ×  |  ×  |  ×  |  ○  |

  - VideoFileFormatEnum

  | 値             | タイプ |   幅 | 高さ | フレームレート | Codec            |  S  | SC  |  V  | Z1  |  X  |
  | -------------- | ------ | ---: | ---: | -------------: | ---------------- | :-: | :-: | :-: | :-: | :-: |
  | VIDEO_HD       | mp4    | 1280 |  570 |                |                  |  ○  |  ○  |  ×  |  ×  |  ×  |
  | VIDEO_FULL_Hd  | mp4    | 1920 | 1080 |                |                  |  ○  |  ○  |  ×  |  ×  |  ×  |
  | VIDEO_2K       | mp4    | 1920 |  960 |                | H.264/MPEG-4 AVC |  ×  |  ×  |  ○  |  ○  |  ×  |
  | VIDEO_4K       | mp4    | 3840 | 1920 |                | H.264/MPEG-4 AVC |  ×  |  ×  |  ○  |  ○  |  ×  |
  | VIDEO_2K_30F   | mp4    | 1920 |  960 |             30 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | VIDEO_2K_60F   | mp4    | 1920 |  960 |             60 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | VIDEO_4K_30F   | mp4    | 3840 | 1920 |             30 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | VIDEO_4K_60F   | mp4    | 3840 | 1920 |             60 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | VIDEO_5_7K_2F  | mp4    | 5760 | 2880 |              2 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | VIDEO_5_7K_5F  | mp4    | 5760 | 2880 |              5 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | VIDEO_5_7K_30F | mp4    | 5760 | 2880 |             30 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | VIDEO_7K_2F    | mp4    | 7680 | 3840 |              2 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | VIDEO_7K_5F    | mp4    | 7680 | 3840 |              5 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |
  | VIDEO_7K_10F   | mp4    | 7680 | 3840 |             10 | H.264/MPEG-4 AVC |  ×  |  ×  |  ×  |  ×  |  ○  |

- 画像処理 - filter:FilterEnum

  | 値              | 内容       | 備考       |
  | --------------- | ---------- | ---------- |
  | OFF             | なし       |            |
  | NOISE_REDUCTION | ノイズ軽減 |            |
  | HDR             | HDR        | デフォルト |

- GPS オン/オフ - isGpsOn:boolean
  THETA X 以外は指定しても無視される

  | 値    | 内容     | 備考       |
  | ----- | -------- | ---------- |
  | true  | GPS あり | デフォルト |
  | false | GPS なし |            |

- ISO 値 - iso:IsoEnum

  | 値       | ISO 値 | 備考 |
  | -------- | -----: | ---- |
  | ISO_AUTO |      0 |      |
  | ISO_50   |     50 |      |
  | ISO_64   |     64 |      |
  | ISO_80   |     80 |      |
  | ISO_100  |    100 |      |
  | ISO_125  |    125 |      |
  | ISO_160  |    160 |      |
  | ISO_200  |    200 |      |
  | ISO_250  |    250 |      |
  | ISO_320  |    320 |      |
  | ISO_400  |    400 |      |
  | ISO_500  |    150 |      |
  | ISO_640  |    640 |      |
  | ISO_800  |    800 |      |
  | ISO_1000 |   1000 |      |
  | ISO_1250 |   1250 |      |
  | ISO_1600 |   1600 |      |
  | ISO_2000 |   2000 |      |
  | ISO_2500 |   2500 |      |
  | ISO_3200 |   3200 |      |
  | ISO_4000 |   4000 |      |
  | ISO_5000 |   5000 |      |
  | ISO_6400 |   6400 |      |

- ISO 上限 - isoAutoHighLimit:IsoAutoHighLimitEnum
  THETA V ファームウェア v2.50.1 以前、THETA S、THETA SC では指定しても無視される

  | 値       | ISO 上限値 | 備考 |
  | -------- | ---------: | ---- |
  | ISO_100  |        100 |      |
  | ISO_125  |        125 |      |
  | ISO_160  |        160 |      |
  | ISO_200  |        200 |      |
  | ISO_250  |        250 |      |
  | ISO_320  |        320 |      |
  | ISO_400  |        400 |      |
  | ISO_500  |        150 |      |
  | ISO_640  |        640 |      |
  | ISO_800  |        800 |      |
  | ISO_1000 |       1000 |      |
  | ISO_1250 |       1250 |      |
  | ISO_1600 |       1600 |      |
  | ISO_2000 |       2000 |      |
  | ISO_2500 |       2500 |      |
  | ISO_3200 |       3200 |      |
  | ISO_4000 |       4000 |      |
  | ISO_5000 |       5000 |      |
  | ISO_6400 |       6400 |      |

- 絞り設定 - aperture:ApertureEnum

  | 値            | 設定値 | 備考 |
  | ------------- | -----: | ---- |
  | APERTURE_AUTO |   自動 |      |
  | APERTURE_20   |   2.0f |      |
  | APERTURE_21   |   2.1f |      |
  | APERTURE_24   |   2.4f |      |
  | APERTURE_35   |   3.5f |      |
  | APERTURE_56   |   5.6f |      |

- 色温度設定 - colorTemperature:number

  - 2500 ~ 10000

- GPS 情報 - gpsInfo:GpsInfo
  GpsInfo は以下の内容の Object として作成する。

  | 値           | 設定値   | 備考         |
  | ------------ | -------- | ------------ |
  | latitude     | 緯度     | 65535 でオフ |
  | longitude    | 経度     | 65535 でオフ |
  | altitude     | 高度     |              |
  | dateTimeZone | 日付時刻 |              |

- ホワイトバランス - whiteBalance:WhiteBalanceEnum

  | 値                     | 設定値                                                 | 備考                                                                               |
  | ---------------------- | ------------------------------------------------------ | ---------------------------------------------------------------------------------- |
  | AUTO                   | 自動                                                   |                                                                                    |
  | DAYLIGHT               | Outdoor                                                | 約 5,200,000                                                                       |
  | SHADE                  | Shade                                                  | 約 7,000,000                                                                       |
  | CLOUDY_DAYLIGHT        | Cloudy                                                 | 約 6,000,000                                                                       |
  | INCANDESCENT           | Incandescent light 1                                   | 約 3,200,000                                                                       |
  | WARM_WHITE_FLUORESCENT | Incandescent light 2                                   |                                                                                    |
  | DAYLIGHT_FLUORESCENT   | Fluorescent light 1(daylight)                          |                                                                                    |
  | DAYWHITE_FLUORESCENT   | Fluorescent light 2(natural white)                     |                                                                                    |
  | FLUORESCENT            | Fluorescent light 3 (white)                            | 約 4,000,000                                                                       |
  | BULB_FLUORESCENT       | Fluorescent light 4 (light bulb color)                 |                                                                                    |
  | COLOR_TEMPERATURE      | CT settings (specified by the colorTemperature option) | RICOH THETA S firmware v01.82 or later and RICOH THETA SC firmware v01.10 or later |
  | UNDERWATER             | Underwater                                             | RICOH THETA V firmware v3.21.1 or later                                            |

- 最大録画時間設定 - maxRecordableTime:MaxRecordableTimeEnum

  | 値                  | 内容    | 備考 |
  | ------------------- | ------- | ---- |
  | RECORDABLE_TIME300  | 300 秒  |      |
  | RECORDABLE_TIME1500 | 1500 秒 |      |

- キャプチャモード - captureMode:CaptureModeEnum

  | 値    | 内容             | 備考 |
  | ----- | ---------------- | ---- |
  | IMAGE | 静止画撮影モード |      |
  | VIDEO | 動画撮影モード   |      |

- 言語設定 - language:LanguageEnum
  (THETA S, THETA SC では指定しても無視される)

  | 値    | 内容       | 備考 |
  | ----- | ---------- | ---- |
  | EN_US | 米国英語   |      |
  | EN_GB | 英国英語   |      |
  | JA    | 日本語     |      |
  | FR    | フランス語 |      |
  | DE    | ドイツ語   |      |
  | ZH_TW | 中国語台湾 |      |
  | ZH_CN | 中国語     |      |
  | IT    | イタリア語 |      |
  | KO    | 韓国語     |      |

- スリープモードに入るまでの時間(分) - sleepDelay:SleepDelayEnum (number 含む)

  - SleepDelayEnum

    | 値               | 設定値(秒) | 備考      |
    |-----------------|-------:|---------|
    | DISABLE         |  65535 | 自動オフしない |
    | SLEEP_DELAY_3M  |    180 |         |
    | SLEEP_DELAY_5M  |    300 |         |
    | SLEEP_DELAY_7M  |    420 |         |
    | SLEEP_DELAY_10M |    600 |         |

  - number
    - RICOH THETA V以降の場合
        - 60 ～ 65534、または 65535 (スリープ モードを無効にする場合)。
    - RICOH THETA SまたはSCの場合
        - 30 ～ 1800、または 65535 (スリープ モードを無効にする場合)。

- スリープから自動電源オフまでの時間(秒) offDelay:OffDelayEnum (number 含む)

  - OffDelayEnum

    | 値             | 設定値(秒) | 備考      |
    |---------------|-------:|---------|
    | DISABLE       |  65535 | 自動オフしない |
    | OFF_DELAY_5M  |    300 |         |
    | OFF_DELAY_10M |    600 |         |
    | OFF_DELAY_15M |    900 |         |
    | OFF_DELAY_30M |   1800 |         |

  - number
    - RICOH THETA V以降の場合
        - 0、または600以上2592000以下（単位：秒）の60の倍数、または65535。65535を設定・取得した場合は0を返します（自動電源オフを無効にする場合）。
    - RICOH THETA SまたはSCの場合
        - 30 ～ 1800、または 65535 (自動電源オフを無効にする場合)。

- シャッター音 - shutterVolume:number
  - 0 - 100

## カメラの設定を取得する

カメラの設定を取得するには、取得したいオプションの一覧を指定して`getOptions()`を呼び出します。

```Typescript
import {
  OptionNameEnum,
  getOptions,
} from 'theta-client-react-native';


let optionNames: OptionNameEnum[] = [
  OptionNameEnum.Aperture,
  OptionNameEnum.CaptureMode,
  OptionNameEnum.ColorTemperature
];
getOptions(optionNames)
  .then(options => {
    // handle options
  })
  .catch(error => {
    // handle error
  });

```

- OptionNameEnum は以下の表を参照してください

  | 値                    | 意味                 | 型                                         |
  | --------------------- | -------------------- | ------------------------------------------ |
  | Aperture              | 絞り                 | ApertureEnum                               |
  | CaptureMode           | キャプチャーモード   | CaptureModeEnum                            |
  | ColorTemperature      | 色温度               | number                                     |
  | DateTimeZone          | 日時                 | string                                     |
  | ExposureCompensation  | 露出補正             | ExposureCompensationEnum                   |
  | ExposureDelay         | 露出遅延時間         | ExposureDelayEnum                          |
  | ExposureProgram       | 露出プログラム       | ExposureProgram                            |
  | FileFormat            | ファイルフォーマット | PhotoFileFormatEnum \| VideoFileFormatEnum |
  | Filter                | 画像フィルタ         | FilterEnum                                 |
  | GpsInfo               | GPS 情報             | GpsInfo                                    |
  | IsGpsOn               | GPS フラグ           | boolean                                    |
  | Iso                   | ISO 値               | IsoEnum                                    |
  | IsoAutoHighLimit      | ISO 上限             | IsoAutoHighLimitEnum                       |
  | Language              | 言語                 | LanguageEnum                               |
  | MaxRecordableTime     | 最長録画時間         | MaxRecordableTimeEnum                      |
  | OffDelay              | 電源オフ時間         | OffDelayEnum                               |
  | SleepDelay            | スリープ時間         | SleepDelayEnum                             |
  | RemainingPictures     | 残り画像数           | number                                     |
  | RemainingVideoSeconds | 残り録画秒数         | number                                     |
  | RemainingSpace        | 残領域               | number                                     |
  | TotalSpace            | 合計領域             | number                                     |
  | ShutterVolume         | シャッター音量       | number                                     |
  | WhiteBalance          | ホワイトバランス     | WhiteBalanceEnum                           |

## THETA 内の静止画・動画を一覧する

THETA 内の静止画（JPEG ファイル）や動画（MP4 ファイル）の一覧は`listFiles(fileType:FileTypeEnum, startPosition:number, entryCount:number)`を使って取得できます。
`listFiles()`の戻り値型は`ThetaFiles`で、`ThetaFiles`のプロパティ`fileList` が THETA 内のファイル一覧です。
`fileList`は `FileInfo`のリストです。

- FileTypeEnum

  | 値    | 内容                          |
  | ----- | ----------------------------- |
  | IMAGE | 静止画（JPEG ファイル）を一覧 |
  | VIDEO | 動画（MP4 ファイル）を一覧    |
  | ALL   | 全てのファイルを一覧          |

- ThetaFiles

  | Property name | Type             | Contents                                                                                                                                      |
  | ------------- | ---------------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
  | fileList      | List\<FileInfo\> | THETA 内のファイル一覧                                                                                                                        |
  | totalEntries  | number           | THETA 内のファイル数 ([api spec](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.list_files.md)参照) |

- FileInfo

  | プロパティ名 | 型     | 内容                                      |
  | ------------ | ------ | ----------------------------------------- |
  | name         | string | ファイル名を表します                      |
  | size         | number | ファイルサイズ（バイト数）を表します      |
  | dateTime     | string | 撮影日時（YYYY:MM:DD HH:MM:SS）を表します |
  | fileUrl      | string | ファイルの URL を表します                 |
  | thumbnailUrl | string | サムネールの URL を表します               |

```Typescript
import {listFiles, FileTypeEnum} from 'theta-client-react-native';

await listFiles(FileTypeEnum.IMAGE, 0, 1000)
  .then(({fileList, totalEntries}) => {
    // handle file list
  })
  .catch(error => {
    // handle error
  });
```

## 静止画・動画を取得する

撮影した静止画(JPEG ファイル）や動画（MP4 ファイル)の URL からデータを取得します。以下のような関数を使えばダウンロードできます。
なお、静止画像の場合は、Image コンポーネントの source 属性に URL を設定することでダウンロード後表示することができます。

```Typescript
fetch(url)
  .then((res) => {
    if (!res.ok) {
      // error
      throw Error('...');
    }
    return res.blob();
  })
  .then((blob) => {
    // processing data
    // eg: URL.createObjectURL(blob);
  })
  .catch((error) => {
    // handle error
  });
```

## サムネイルを取得する

THETA 内のファイルのサムネイルは、`listFiles`を使って取得した `FileInfo`の`thumbnailUrl`を使って以下のようにダウンロードすることができます。

```Typescript
fetch(fileInfo.thumbnailUrl)
  .then((res) => {
    if (!res.ok) {
      // error
      throw Error('...');
    }
    return res.blob();
  })
  .then((blob) => {
    // processing data
    // eg: URL.createObjectURL(blob);
  })
  .catch((error) => {
    // handle error
  });
```

## プレビューを表示する

プレビューは equirectangular 形式の motion JPEG です。

それぞれのフレーム処理を行うコールバック関数を引数として
`getLivePreview()`を呼び出します。
プレビュー中のフレームは、イベントのパラメータとして受け取ることができます。

```Typescript
import * as React from 'react';
import {NativeModules, NativeEventEmitter} from 'react-native';
import {
  getLivePreview,
  stopLivePreview,
  THETA_FRAME_EVENT,
} from 'theta-client-react-native';

const LivePreview = () => {
  [dataUrl, setDataUrl] = React.useState<string | undefined>();

  React.useEffect(() => {
    const eventListener = new NaiveEventEmitter(NativeModules.ThetaClientReactNative)
      .addListener(THETA_FRAME_EVENT, event => {
        setDataUrl(event.data);
      });
    getLivePreview()
      .then(() => {
        // preview done
      })
      .catch(() => {
        // handle error while previewing
      });
    return (() => {
      stopLivePreview();
      eventListener.remove();
    });
  }, []);

  return (
    ...略
    <Image source={{uri: dataUrl}} style=... />
    ...略
  );
};
```

## THETA をリセットする

接続している Theta をリセットするには、`reset()`を呼び出します。

```Typescript
import {reset} from 'theta-client-react-native';

reset()
  .then(() => {
    // reset done
  })
  .catch((error) => {
    // handle error
  });
```

## THETA の情報を取得する

接続している Theta の情報を取得するには、`getThetaInfo()`を呼び出します。呼び出しに成功すると`ThetaInfo`を取得できます。このデータは以下のような情報を含みます。

- ThetaInfo
  |プロパティ名|型|内容|
  |---|---|---|
  |firmwareVersion|string|ファームウェアバージョンを表します|
  |hasGps|boolean|GPS 機能を持っているかどうかを表します|
  |hasGyro|boolean|ジャイロを持っているかどうかを表します|
  |model|string|Theta の型番を表します|
  |serialNumber|string|Theta のシリアル番号を表します|
  |uptime|number|Theta の電源を入れてからの秒数を表します|

```Typescript
import {getThetaInfo} from 'theta-client-react-native';

getThetaInfo()
  .then((thetaInfo) => {
    // processing thetaInfo
  })
  .catch((error) => {
    // handle error
  });
```

## THETA の状態を取得する

接続している Theta の状態を取得するには、`getThetaState()`を呼び出します。呼び出しに成功すると`ThetaState`を取得できます。このデータは以下のような情報を含みます。

- ThetaState

  | プロパティ名   | 型                | 内容                                         |
  | -------------- | ----------------- | -------------------------------------------- |
  | batteryLevel   | number            | バッテリーレベルを表します                   |
  | chargingState  | ChargingStateEnum | 充電状態を表します                           |
  | fingerprint    | string            | 現在の状態ごとに一意に決まる識別子を表します |
  | isSdCard       | boolean           | SD カードが存在するかどうかを表します        |
  | latestFileUrl  | string            | 最後の取得したメディアの URL を表します      |
  | recordableTime | number            | 録画可能秒数を表します                       |
  | recordedTime   | number            | 録画済み秒数を表します                       |

- ChargingStateEnum

  | プロパティ名 | 内容                     |
  | ------------ | ------------------------ |
  | CHARGING     | 充電中を表します         |
  | CHARGED      | 充電完了を表します       |
  | NOT_CHARGING | ケーブル未接続を表します |

```Typescript
import {getThetaState} from 'theta-client-react-native';

getThetaState()
  .then((thetaState) => {
    // processing thetaState
  })
  .catch((error) => {
    // handle error
  });
```
