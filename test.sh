#!/usr/bin/env bash

# --class-path allows specifying where JUnit should look for tests
# --include-classname allows the name to be anything
# --disable-banner disables the banner asking contributing to JUnit
# --scan-class-path checks the full classpath for tests
# The grep -v filter removes lines that are unnecessary or potentially
#    confusing to an end user.
java -jar libs/junit-platform-console-standalone-1.4.2.jar --class-path ".:generated" --include-classname='.*' --disable-banner --scan-class-path \
    | grep -v -e '\[.*containers.*\]' -e 'JUnit Vintage' -e 'Test run finished after'
