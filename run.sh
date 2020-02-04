#!/bin/bash

help() {
    echo "Usage: $0 [file]"
    echo "If no file is specified, the most recently compiled program will be run"
}


name=""
if [ $# == 0 ]; 
then
    cd ./generated/
    recent=$(ls -t | head -n1)
    name=${recent%.*}
    cd ..
else
    base=$(basename $1)
    upbase="$(tr '[:lower:]' '[:upper:]' <<< ${base:0:1})${base:1}"
    name="LJ${upbase%.*}"
fi

java -cp generated $name
exit
