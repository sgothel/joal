#! /bin/sh

#export JOGAMP_JAR_CODEBASE="Codebase: *.jogamp.org"
export JOGAMP_JAR_CODEBASE="Codebase: *.goethel.localnet"

#BUILD_ARCHIVE=true \
ant  \
    $* 2>&1 | tee make.joal.all.generic.log
