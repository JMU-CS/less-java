#!/bin/bash

# Clean
rm generated/*.class 2>/dev/null

mkdir -p generated/wrappers

# Add ljwrappers
cp ljwrappers/src/*.java generated/wrappers/

# Compile
printf "Compiling wrappers...\n"
javac -cp libs/junit-4.12.jar:libs/hamcrest-core-1.3.jar:generated generated/wrappers/*.java

printf "Compiling main...\n"
javac -cp libs/junit-4.12.jar:libs/hamcrest-core-1.3.jar:generated generated/Main.java
