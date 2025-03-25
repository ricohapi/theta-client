# iOS demo for theta-client

A simple sample iOS application using [theta-client](https://github.com/ricohapi/theta-client).

## 目的

* Theta clientを使う開発者に、簡単な実例を示す

## Functions

* List photos in Theta.
* View sphere photo in Theta.
* Take a photo with Theta.

## 設計方針

* UIの簡潔な記述ができる[SwiftUI](https://developer.apple.com/jp/xcode/swiftui/)を使う

## 留意点

* bundle id、signature情報はご自身のものに変更ください。
* xcodeでのビルド前にpod installを実行しておくこと。
  Podfileは以下のようにtheta-clientを設定している
  ```
  platform :ios, '15.0'
  target 'SdkSample' do
    # Comment the next line if you don't want to use dynamic frameworks
    use_frameworks!

    # Pods for SdkSample
    pod 'THETAClient'
  end
  ```
