#!/bin/sh

rm -rf package
mkdir package
rm -rf lib
yarn prepack

tar -cf - \
    -vz \
    --exclude '.DS_Store' \
    --exclude '*/__test__' \
    --exclude '._*' \
    --exclude '*/build' \
    --exclude '*/.idea' \
    --exclude '*/xcuserdata' \
    README.md \
    README.ja.md \
    android \
    ios \
    src \
    lib \
    frameworks \
    package.json \
    theta-client-react-native.podspec |
     (cd package; tar -zxf -)
    
