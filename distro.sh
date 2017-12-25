#!/bin/bash
gradle shadowJar
cp build/libs/*-all.jar distro/lj.jar
