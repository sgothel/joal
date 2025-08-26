#! /bin/sh

SDIR=`dirname $0`

if [ -e $SDIR/../../../gluegen/make/scripts/setenv-build-jogamp-x86_64.sh ] ; then
    . $SDIR/../../../gluegen/make/scripts/setenv-build-jogamp-x86_64.sh
fi

#    -Dc.compiler.debug=true  \
#    -Djavacdebuglevel="source,lines,vars" \

#BUILD_ARCHIVE=true \
ant  \
    -Drootrel.build=build-freebsd-x86_64 \
    $* 2>&1 | tee make.joal.all.freebsd-x86_64.log
