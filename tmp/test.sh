#!/bin/bash

java -jar ../libs/antlr-4.5.3-complete.jar Test.g4
javac -cp ../libs/antlr-4.5.3-complete.jar *.java
java -cp ../libs/antlr-4.5.3-complete.jar:. org.antlr.v4.gui.TestRig Test program -gui $1

