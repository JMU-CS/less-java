#!/bin/bash

printf "Compiling...\n"
if javac -cp .:lj.jar generated/Main.java; then
    printf "Testing...\n"
    java -cp .:generated:lj.jar org.junit.runner.JUnitCore Main
fi

