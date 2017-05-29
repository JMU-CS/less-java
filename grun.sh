#!/bin/bash

cd build/classes/main
java -cp ../../../libs/antlr-4.5.3-complete.jar:. org.antlr.v4.gui.TestRig LJ program -gui ../../../test.lj
