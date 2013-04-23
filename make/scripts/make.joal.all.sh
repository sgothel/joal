#! /bin/sh

SDIR=`dirname $0` 

$SDIR/make.joal.all.linux-armv6-cross.sh \
&& $SDIR/make.joal.all.linux-armv6hf-cross.sh \
&& $SDIR/make.joal.all.linux-x86_64.sh \
&& $SDIR/make.joal.all.linux-x86.sh \
&& $SDIR/make.joal.all.android-armv6-cross.sh \
