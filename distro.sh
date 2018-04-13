#!/bin/bash
gradle fatJar
cp build/libs/*-all.jar distribution/lj.jar
