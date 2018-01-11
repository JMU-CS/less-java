#!/bin/bash
gradle shadowJar
cp build/libs/*-all.jar distribution/lj.jar
