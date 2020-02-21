#!/usr/bin/env bash

GRADLE_WRAPPER="./gradlew"

"$GRADLE_WRAPPER" ui
cp build/libs/lj-ui.jar distribution/lj-ui.jar
java -jar distribution/lj-ui.jar
