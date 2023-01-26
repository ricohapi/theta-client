# React Native demo for theta-client

A simple sample React Native application using [theta-client](https://github.com/ricohapi/theta-client).

## 目的

* Theta clientを使う開発者に、簡単な実例を示す

## Functions

* List photos in Theta.
* View sphere photo in Theta.
* Take a photo with Theta.

## 設計方針

* UIは簡潔な記述する

## 留意点

* bundle id、signature情報はご自身のものに変更ください。
* ビルド前に環境変数THETA_CLIENTにtheta-clientのディレクトリを設定してください。

## ビルドと実行例

* ビルドと実行
  Debug用のビルドと実行は以下の通りです。
  ```
  $ yarn install
  $ yarn run android

  OR

  $ yarn run ios
  ```
  ※bundlerはmetroで以下のように別端末で事前に実行しておいても構いません。
  ```
  $ yarn start
  ```
