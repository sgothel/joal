#! /bin/sh

if [ -e /usr/local/etc/profile.ant ] ; then
    . /usr/local/etc/profile.ant
fi

JAVA_HOME=`/usr/libexec/java_home -version 21`
PATH=$JAVA_HOME/bin:$PATH
export JAVA_HOME PATH

#    -Dc.compiler.debug=true 

#export JOGAMP_JAR_CODEBASE="Codebase: *.jogamp.org"
export JOGAMP_JAR_CODEBASE="Codebase: *.goethel.localnet"

#BUILD_ARCHIVE=true \
ant \
    -Drootrel.build=build-macosx \
    $* 2>&1 | tee make.joal.all.macosx.log
