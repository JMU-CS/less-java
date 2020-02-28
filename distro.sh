#!/usr/bin/env bash

GRADLE_WRAPPER="./gradlew"

"$GRADLE_WRAPPER" clean

"$GRADLE_WRAPPER" fatJar
cp build/libs/lj-all.jar distribution/lj.jar
cp libs/*.jar distribution/

"$GRADLE_WRAPPER" ui
cp build/libs/lj-ui.jar distribution/lj-ui.jar

