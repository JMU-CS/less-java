#!/bin/bash

if [ $# -eq 0 ]; then
    echo "Usage: ./lj.sh <file>"
    exit
fi

gradle run -Ptestfile="$@" && echo "output:" && cat compiler.out
