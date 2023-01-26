## How to create React Native project

### Prerequisite

* Build theta-client.

  ```shell
  $ gradle publishToMavenLocal podPublishXCFramework
  ```

* Set the directory of theta-client to environment variable `THETA_CLIENT`.

  ```shell
  $ export THETA_CLIENT=<path to theta-client>
  ```

* Build react-native wrapper

  ```shell
  $ cd react-native
  $ sh ./mkpackage.sh
  ```


* Install react-native

### Creating a project

* Create a project using `react-native-cli`, then add theta-client.

  ```shell
  $ npx react-native init YourProject --template react-native-template-typescript
  $ cd YourProject
  $ yarn add $THETA_CLIENT/react-native/package
  ```

* Settings on android

  * YourProject/android/build.gradle
	* set `minSdkVersion` to 26 or later.

* Settings on iOS
  * YourProject/ios/Podfile
	* Set `platform :ios` to '15.0' or later.
	* Add pod of theta-client.
	```
	pod "THETAClient", :path => ENV["THETA_CLIENT"] + "/kotlin-multiplatform/build/cocoapods/publish/debug"
	```

* Building and execution

  ```
  $ cd YourProject
  $ yarn install
  $ yarn run android

  OR

  $ yarn run ios
  ```

  You can execute metro bundler on other terminal in advance.

  ```
  $ yarn start
  ```
