#!/bin/bash

rm -rf build
mkdir build

javac -d build src/snapshot/*.java
jar --create --file snapshot.jar --manifest manifest.txt -C build .

echo "Build complete."