#! /bin/sh

SDIR=`dirname $0` 

$SDIR/make.joal.all.android-aarch64-cross.sh && \
$SDIR/make.joal.all.android-armv6-cross.sh && \
$SDIR/make.joal.all.android-x86-cross.sh && \
$SDIR/make.joal.all.linux-aarch64-cross.sh && \
$SDIR/make.joal.all.linux-armv6hf-cross.sh && \
$SDIR/make.joal.all.linux-x86.sh && \
$SDIR/make.joal.all.linux-x86_64.sh

# $SDIR/make.joal.all.macosx.sh
# $SDIR/make.joal.all.ios.amd64.sh
# $SDIR/make.joal.all.ios.arm64.sh
# $SDIR/make.joal.all.win32.bat
# $SDIR/make.joal.all.win64.bat
# $SDIR/make.joal.all.linux-armv6hf.sh
# $SDIR/make.joal.all.linux-aarch64.sh

