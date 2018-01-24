#!/bin/bash

# Clean
rm generated/*.class

# Add ljwrappers
cp ljwrappers/src/*.java generated

# Compile
printf "Compiling...\n"
javac -cp libs/junit-4.12.jar:libs/hamcrest-core-1.3.jar generated/*.java
