#! /bin/sh

SDIR=`dirname $0` 

$SDIR/make.joal.all.macosx.sh && \
$SDIR/make.joal.all.ios.amd64.sh && \
$SDIR/make.joal.all.ios.arm64.sh

# $SDIR/make.joal.all.macosx.sh
# $SDIR/make.joal.all.ios.amd64.sh
# $SDIR/make.joal.all.ios.arm64.sh
# $SDIR/make.joal.all.win32.bat
# $SDIR/make.joal.all.win64.bat
# $SDIR/make.joal.all.linux-ppc64le.sh
# $SDIR/make.joal.all.linux-armv6hf.sh
# $SDIR/make.joal.all.linux-aarch64.sh
