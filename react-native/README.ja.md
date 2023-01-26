## React Native プロジェクト作成手順

### 事前準備

* theta-client がビルド済み
例
  ```shell
  $ gradle publishToMavenLocal podPublishXCFramework
  ```

* theta-clientのディレクトリを環境変数THETA_CLIENTに設定していること
例
  ```shell
  $ export THETA_CLIENT=<ルートディレクトリ>
  ```

* react-native 用ラッパーがビルド済み
例
  ```shell
  $ cd react-native
  $ sh ./mkpackage.sh
  ```

* react-nativeの環境をインストール済みであること

### プロジェクトの作成

* react-native-cliを使ってプロジェクトを作成後、theta-clientを追加する
例
  ```shell
  $ npx react-native init YourProject --template react-native-template-typescript
  $ cd YourProject
  $ yarn add $THETA_CLIENT/react-native/package
  ```

* android設定

  * YourProject/android/build.gradle
	* `minSdkVersion`を26以上に設定する

* iOS設定
  * YourProject/ios/Podfile
	* `platform :ios` のバージョンを'15.0'以上に設定する
	* theta-client のpodを追加する
```
  pod "THETAClient", :path => ENV["THETA_CLIENT"] + "/kotlin-multiplatform/build/cocoapods/publish/debug"
```

* ビルドと実行
	
  Debug用のビルドと実行は以下の通りです。
  ```
  $ cd YourProject
  $ yarn install
  $ yarn run android

  OR

  $ yarn run ios
  ```
  ※bundlerはmetroで以下のように別端末で事前に実行しておいても構いません。
  ```
  $ yarn start
  ```
