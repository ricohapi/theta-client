# RICOH360 THETA client チュートリアル

## 事前準備

- モジュールの`build.gradle`の`dependencies`に次を追加します。
  ```
  implementation "com.ricoh360.thetaclient:theta-client:1.11.0"
  ```
- 本 SDK を使用したアプリケーションが動作するスマートフォンと THETA を無線 LAN 接続しておきます。

## THETA client のインスタンス作成

インスタンス作成時にカメラの日時設定、言語設定、シャッター音量設定、スリープまでの時間設定、電源オフまでの時間設定を行うこともできます。

- 言語設定(THETA S, THETA SC では指定しても無視される)

  - “en-US”
  - “en-GB”
  - “ja”
  - “fr”
  - “de”
  - “zh-TW”
  - “zh-CN”
  - “it”
  - “ko”

- 日時設定

  - YYYY:MM:DD HH:MM:SS+HH:MM の形式

- スリープモードに入るまでの時間(分)

  - 自動オフしない, 3 分, 5 分, 7 分, 10 分

- スリープから自動電源オフまでの時間(分)

  - 自動オフしない, 5 分, 10 分, 15 分, 30 分

- シャッター音
  - 0〜100

```kotlin
import com.ricoh360.pf.theta.ThetaRepository

val thetaUrl = "192.168.1.1:80"
val thetaConfig = ThetaRepository.Config()
// thetaConfig.dateTime = "2023:01:01 12:34:56+09:00" // set current time
// thetaConfig.language = ThetaRepository.LanguageEnum.EN_US
// thetaConfig.shutterVolume = 40 // 0 to 100
// thetaConfig.sleepDelay = ThetaRepository.SleepDelayEnum.SLEEP_DELAY_5M
// thetaConfig.offDelay = ThetaRepository.OffDelayEnum.DISABLE
thetaRepository = ThetaRepository.newInstance(thetaUrl, thetaConfig)
```

## 静止画を撮影する

まず`PhotoCapture.Builder`を使って撮影設定を行い、`PhotoCapture`オブジェクトを生成します。

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

プレビューを表示する方法は[プレビューを表示する](#プレビューを表示する)をご覧ください。

### 静止画撮影時に設定できる項目

- 露出補正
  - -2.0, -1.7, -1.3, -1.0, -0.7, -0.3, 0.0, 0.3, 0.7, 1.0, 1.3, 1.7, 2.0
- セルフタイマー
  - 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
- 解像度

| 機種                | 解像度指定 | 横(pixel) | 縦(pixel) |
| ------------------- | ---------- | --------- | --------- |
| THETA X             | IMAGE_11K  | 11,008    | 5,504     |
| THETA X             | IMAGE_5_5K | 5,504     | 2,752     |
| THETA Z1            | IMAGE_6_7K | 6,720     | 3,360     |
| THETA S, SC, SC2, V | IMAGE_5K   | 5,376     | 2,688     |

- 画像処理

  - なし
  - ノイズ軽減
  - HDR

- GPS オン/オフ (THETA X 以外は指定しても無視される)

  - ON
  - OFF

- ISO 上限 (THETA V ファームウェア v2.50.1 以前、THETA S、THETA SC では指定しても無視される)
  - 200, 250, 320, 400, 500, 640, 800, 1000, 1250, 1600, 2000, 2500, 3200

#### 静止画撮影でビットレートの数値を設定する場合(THETA X)

THETA X では静止画撮影のビットレートの数値を設定することができます([api-spec](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/options/_bitrate.md)参照)。
THETA Client でこの値を設定する際は、以下の手順が必要です：

1. `PhotoCapture`オブジェクトを生成
1. `setOptions()`で`bitrate`を設定
1. `takePicture()`で撮影

この手順が必要な理由は、`PhotoCapture.Builder.build()`内で`captureMode`の値が`image`にセットされる際に、
カメラ側の`bitrate`の値がリセットされるためです。

```kotlin
val photoCapture = thetaRepository.getPhotoCaptureBuilder().build()

val options = ThetaRepository.Options()
options.bitrate = ThetaRepository.BitrateNumber(1048576)
thetaRepository.setOptions(options)

photoCapture.takePicture(TakenCallback())
```

## 動画を撮影する

まず`VideoCapture.Builder`を使って撮影設定を行い、`VideoCapture`オブジェクトを生成します。

```kotlin
val videoCapture: VideoCapture = VideoCapture.Builder()
    .setAutoIsoUpperLimit(IsoEnum.ISO_800)
    .setFileFormat(VideoFileFormatEnum.VIDEO_5_7K_30F)
    .build()
```

上の例では ISO 感度の最大値を 800 に、解像度を 5.7K に設定しています。

Theta S と Theta SC 以外ではプレビューを表示できます。表示方法は[プレビューを表示する](#プレビューを表示する)をご覧ください

次に`VideoCapture.startCapture()`を呼んで動画の撮影を開始します。引数には MP4 ファイルを HTTP GET するコールバック関数を渡します。

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

次に`VideoCapturing.stopCapture()`を呼んで動画の撮影を終了します。撮影終了後、MP4 ファイルの生成が完了すると、`startCapture()`に渡したコールバック関数が呼ばれます。

```kotlin
videoCapturing.stopCapture()
```

### 動画撮影時に設定できる項目

- 解像度

| 機種         | 解像度指定     | 横(pixel) | 縦(pixel) |
| ------------ | -------------- | --------- | --------- |
| THETA X      | VIDEO_5_7K_30F | 5,760     | 2,880     |
| THETA X      | VIDEO_4K_30F   | 3,840     | 1,920     |
| THETA X      | VIDEO_2K_30F   | 1,920     | 960       |
| THETA Z1     | VIDEO_4K       | 3,840     | 1,920     |
| THETA Z1     | VIDEO_2K       | 1,920     | 960       |
| THETA SC2, V | VIDEO_4K       | 3,840     | 1,920     |
| THETA SC2, V | VIDEO_2K       | 1,920     | 960       |
| THETA S, SC  | VIDEO_FULL_HD  | 1,920     | 1,080     |

- 最長撮影時間(分)

  - 5
  - 25

- ISO 上限 (THETA V ファームウェア v2.50.1 以前、THETA S、THETA SC では指定しても無視される)
  - 200, 250, 320, 400, 500, 640, 800, 1000, 1250, 1600, 2000, 2500, 3200

## プレビューを表示する

プレビューは equirectangular 形式の motion JPEG です。

| 機種        | 横(pixel) | 縦(pixel) | フレームレート(fps) | 備考                        |
| ----------- | --------- | --------- | ------------------- | --------------------------- |
| THETA X     | 1024      | 512       | 30                  |                             |
| THETA Z1    | 1024      | 512       | 30                  |                             |
| THETA V     | 1024      | 512       | 30                  | ファームウェア v2.21.1 以降 |
| THETA V     | 1024      | 512       | 8                   | ファームウェア v2.20.1 以前 |
| THETA SC2   | 1024      | 512       | 30                  |                             |
| THETA S, SC | 640       | 320       | 10                  |                             |

`ThetaRepository.getLivePreview()`を呼ぶと、`Flow<io.ktor.utils.io.core.ByteReadPacket>`が返るので、そこか各フレームの JPEG を取得します。

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

## THETA 内の静止画・動画を一覧する

THETA 内の静止画（JPEG ファイル）や動画（MP4 ファイル）の一覧は`ThetaRepository.listFiles(fileType, startPosition, entryCount)`を使って取得できます。
`fileType`は`ThetaRepository.FileTypeEnum`型で内容は以下の通りです。
`ThetaRepository.listFiles()`の戻り値型は`ThetaRepository.ThetaFiles`で、`ThetaFiles`のプロパティ`fileList` が THETA 内のファイル一覧です。
`fileList`は `ThetaRepository.FileInfo`のリストです。

JPEG ファイル、MP4 ファイルは`FileInfo.fileUrl`、サムネイルの JPEG ファイルは`FileInfo.thumbnailUrl`の値を参照し、HTTP GET します。

- ThetaRepository.FileTypeEnum

  | 値    | 内容                          |
  | ----- | ----------------------------- |
  | IMAGE | 静止画（JPEG ファイル）を一覧 |
  | VIDEO | 動画（MP4 ファイル）を一覧    |
  | ALL   | 全てのファイルを一覧          |

- ThetaRepository.ThetaFiles

  | Property name | Type             | Contents                                                                                                                                      |
  | ------------- | ---------------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
  | fileList      | List\<FileInfo\> | THETA 内のファイル一覧                                                                                                                        |
  | totalEntries  | Int              | THETA 内のファイル数 ([api spec](https://github.com/ricohapi/theta-api-specs/blob/main/theta-web-api-v2.1/commands/camera.list_files.md)参照) |

- ThetaRepository.FileInfo

  | プロパティ名 | 型     | 内容                                      |
  | ------------ | ------ | ----------------------------------------- |
  | name         | String | ファイル名を表します                      |
  | size         | Long   | ファイルサイズ（バイト数）を表します      |
  | dateTime     | String | 撮影日時（YYYY:MM:DD HH:MM:SS）を表します |
  | fileUrl      | String | ファイルの URL を表します                 |
  | thumbnailUrl | String | サムネールの URL を表します               |

## THETA の情報を取得する

`ThetaRepository.getThetaInfo()`を呼びます。

## THETA の状態を取得する

`ThetaRepository.getThetaState()`を呼びます。

## THETA をリセットする

`ThetaRepository.reset()`を呼びます。
