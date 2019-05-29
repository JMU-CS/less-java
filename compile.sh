#!/usr/bin/env bash

GRADLE_WRAPPER="./gradlew"

"$GRADLE_WRAPPER" -q run -Ptestfile=${1}

