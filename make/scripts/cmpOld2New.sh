#!/bin/bash

dirscript=`dirname $0`

dirold=../build-old/gensrc/classes/com/jogamp/openal
dirnew=../build/gensrc/classes/com/jogamp/openal
dircmp=cmp-old2new

rm -rf $dircmp
mkdir -p $dircmp

for i in AL ALC ALCConstants ALCcontext ALCdevice ALConstants ALExt ALExtConstants ; do
    echo
    echo processing $i
    awk -f $dirscript/strip-c-comments.awk $dirold/$i.java | sort -u > $dircmp/$i-old.java
    echo created $dircmp/$i-old.java
    awk -f $dirscript/strip-c-comments.awk $dirnew/$i.java | sort -u > $dircmp/$i-new.java
    echo created $dircmp/$i-new.java
    diff -Nurdw $dircmp/$i-old.java $dircmp/$i-new.java > $dircmp/$i-diff.txt
    echo created $dircmp/$i-diff.txt
done
    
