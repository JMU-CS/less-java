#!/bin/bash

# Run tests
java -cp .:libs/junit-4.12.jar:generated:libs/hamcrest-core-1.3.jar org.junit.runner.JUnitCore Main
