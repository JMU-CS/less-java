#!/usr/bin/env bash

GRADLE_WRAPPER="./gradlew"

help() {
    echo "Usage: $0 [options] file"
    echo ""
    echo "  Options:"
    echo "    -h  Print this help message and exit"
    echo "    -t  Print type changes found by type inference"
    echo "    -v  Print output from Gradle"
}

args="run"
verbose=false
while getopts :thv flag; do
  case $flag in
    h)
      help
      exit
      ;;
    t)
      args="${args} -PprintTypeChanges"
      ;;
    v)
      verbose=true
      ;;
    \?)
      echo "Unrecognized option $OPTARG" 1>&2
      echo
      help
      exit
      ;;
  esac
done
shift $((OPTIND - 1))

if ! $verbose; then
  args="${args} -q"
fi

if [ $# == 0 ]; then
  echo "Expecting a file" 1>&2
  echo
  help
  exit
fi

args="${args} -Ptestfile=${1}"
base=$(basename $1)
name=${base%.*}
rm -r $name
mkdir $name
#tmp_dir=$(mktemp -d -t less-java-$name)
"$GRADLE_WRAPPER" $args
cp -r ./generated/* $name
exit
