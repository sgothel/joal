#! /bin/sh

SDIR=`dirname $0` 

if [ -e $SDIR/../../../gluegen/make/scripts/setenv-build-jogamp-x86.sh ] ; then
    . $SDIR/../../../gluegen/make/scripts/setenv-build-jogamp-x86.sh
fi

#export JOGAMP_JAR_CODEBASE="Codebase: *.jogamp.org"
export JOGAMP_JAR_CODEBASE="Codebase: *.goethel.localnet"

ant  \
    -Drootrel.build=build-x86 \
    $* 2>&1 | tee make.joal.all.linux-x86.log
