#!/bin/bash

help() {
    echo "Usage: $0 file"
}

if [ $# == 0 ]; then
    echo "Expecting a file" 1>&2
    echo
    help
    exit
fi

base=$(basename $1)
name=${base%.*}
java -cp $name Main
