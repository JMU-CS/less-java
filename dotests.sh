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

test() {
    local file=${1%"/"}
    if [ -f $file ] && [[ $file =~ .*\.lj ]]; then
        parent=$(dirname $file)
        base=$(basename $file)
        name=${base%.*}
        outdir=$parent/outputs
        base=$outdir/$name
        mkdir -p $outdir
        echo "***************************************"
        echo "Compiling, Running, and Testing $file"
        if [ -e "$parent/$name.in" ]; then
            ./lj $file < "$parent/$name.in" 2>&1 > $base.out
        else
            ./lj $file 2>&1 | grep -wvi time > $base.out
        fi
        echo "Finished"
        echo
        if $set_expected; then
            cp $base.out $base.exp
            echo "Setting the expected output of $file to this actual output."
            echo
        else
            #touch $base.out
            diff -uB $base.out $base.exp > $base.diff
            if [ -s $base.diff ]; then
                echo "Differences detected in actual output; use -s option to set the expected output."
                echo
                cat $base.diff
                echo
                exit 1
            else
                echo "No differences detected between expected and actual output."
                echo
            fi
        fi
    elif [ -d $file ] && $recursive; then
        for sub in $(ls $file); do
            if [ ! "$(basename $file)" == "failing" ]; then
                test $file/$sub
            fi
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
            exit 1
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
    exit 1
fi

for f in $@; do
    test $f
done
