#! /bin/bash

# Prints a help message to stdout
help() {
    echo "Usage: $0 [options] file..."
    echo ""
    echo "  Options:"
    echo "    -h  Print this help message and exit"
    echo "    -r  Recursively run all LJ files in the given directory"
    echo "    -s  Set the expected output for the LJ files to their actual output."
    echo "        If -s option not present, LJ file output will be compared to the previous expected output"
}

# Takes the name of a file. If the input file is a LJ file, compile and run it.
# If the -s option is used, copy the outputs to the expected outputs file.
# Otherwise, show a diff between the expected and actual output.
# If the input file is a directory and the -r option is given, call test on the
# directory's contents
test() {
    local file=${1%"/"}
    if [ -f $file ] && [[ $file =~ .*\.lj ]]; then
        parent=$(dirname $file)
        base=$(basename $file)
        name=${base%.*}
        outdir=$parent/outputs
        base=$outdir/$name
        mkdir -p $outdir
        echo "Compiling $file"
        ./compile.sh $file 2>&1 | grep -wvi time > $base\_compile.out
        echo "Done compiling"
        echo
        echo "Running $file"
        ./run.sh 2>&1 | tee $base\_run.out
        echo "Done running"
        echo
        echo "Running tests for $file"
        ./test.sh 2>&1 | grep -wvi time > $base\_test.out
        echo "Done testing"
        echo
        if $set_expected; then
            cp $base\_compile.out $base\_compile.exp
            cp $base\_run.out $base\_run.exp
            cp $base\_test.out $base\_test.exp
        else
            touch $base\_compile.exp
            touch $base\_run.exp
            touch $base\_test.exp
            diff -u $base\_compile.out $base\_compile.exp > $base\_compile.diff
            if [ -s $base\_compile.diff ]; then
                ((changes_found += 1))
                echo "Changes detected in compile output; use -s option to set expected output"
                echo
                cat $base\_compile.diff
                echo
            fi
            diff -u $base\_run.out $base\_run.exp > $base\_run.diff
            if [ -s $base\_run.diff ]; then
                ((changes_found += 1))
                echo "Changes detected in run output; use -s option to set expected output"
                echo
                cat $base\_run.diff
                echo
            fi
            diff -u $base\_test.out $base\_test.exp > $base\_test.diff
            if [ -s $base\_test.diff ]; then
                ((changes_found += 1))
                echo "Changes detected in test output; use -s option to set expected output"
                echo
                cat $base\_test.diff
                echo
            fi
        fi
    elif [ -d $file ] && $recursive; then
        for sub in $(ls $file); do
            test $file/$sub
        done
    fi
}

help_flag=false
recursive=false
set_expected=false

# Parse flags
while getopts :hrs flag; do
    case $flag in
        h)
            help_flag=true
            ;;
        r)
            recursive=true
            ;;
        s)
            set_expected=true
            ;;
        \?)
            echo "Unexpected flag $OPTARG" 1>&2
            echo
            help
            exit
            ;;
    esac
done
shift $((OPTIND - 1))

if $help_flag; then
    help
    exit
fi

# Make sure there's at least one file given
if [ $# == 0 ]; then
    echo "Expecting at least one file" 1>&2
    echo
    help
    exit
fi

changes_found=0

# For each input file f:
for f in $@; do
    test $f
done

# Show how many changes were detected
if [ $set_expected == false ]; then
    echo "Detected $changes_found changed outputs"
fi
