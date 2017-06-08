#!/bin/bash

if [ $# -eq 0 ]; then
    echo "usage: ./lj.sh <file>"
    exit 1
fi

gradle run -Ptestfile="$@" && echo "output:" && cat compiler.out
