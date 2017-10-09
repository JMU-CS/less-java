#!/bin/bash

for file in tests; do
    echo "testing $file"
    timeout 10s gr file
    status=$?

    if [ $status -eq 124]; then
        echo"TIMEOUT for $file"
    fi
done
