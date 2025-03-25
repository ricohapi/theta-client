# iOS demo for theta-client

A simple sample iOS application using [theta-client](https://github.com/ricohapi/theta-client).

## Objective

* Show developers how to use Theta client.

## Functions

* List photos in Theta.
* View sphere photo in Theta.
* Take a photo with Theta.

## Policy

* Use [SwiftUI](https://developer.apple.com/jp/xcode/swiftui/) that can describe UI simply.

## Note

* Change bundle id and signature to your own.
* Before xcode building, execute pod install. Example of Podfile is following:

  ```
  platform :ios, '15.0'
  target 'SdkSample' do
    # Comment the next line if you don't want to use dynamic frameworks
    use_frameworks!

    # Pods for SdkSample
    pod 'THETAClient'
  end
  ```
