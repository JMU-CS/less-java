#!/usr/bin/env bash

GRADLE_WRAPPER="./gradlew"

"$GRADLE_WRAPPER" fatJar
cp build/libs/*-all.jar distribution/lj.jar
