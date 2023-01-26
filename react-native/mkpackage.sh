#!/bin/sh

rm -rf package
mkdir package
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
    package.json \
    theta-client-react-native.podspec |
     (cd package; tar -zxf -)
    
