#!/bin/bash

CURRENT=$(cd $(dirname $0);pwd)
cd $CURRENT/..

./gradlew publishToMavenLocal
if [ $? != 0 ]; then
    echo "aar build is failed"
    exit 1
fi
./gradlew podPublishXCFramework
if [ $? != 0 ]; then
    echo "framework build is failed"
    exit 1
fi

echo "copy aar"
rm ./flutter/android/aar/*.aar
cp -f ./kotlin-multiplatform/build/outputs/aar/*.aar ./flutter/android/aar/

echo "copy framework"
rm -d -r ./flutter/ios/frameworks/debug
rm -d -r ./flutter/ios/frameworks/release
cp -r -f ./kotlin-multiplatform/build/cocoapods/publish/* ./flutter/ios/frameworks/

echo "success"
