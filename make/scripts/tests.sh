function print_usage() {
    echo "Usage: $0 java-exe joal-build-dir"
    echo "e.g.: bash scripts/tests.sh `which java` -d64 ../build-x86_64"
}
    
if [ -z "$1" -o -z "$2" ] ; then
    print_usage
    exit 0
fi

javaexe="$1"
shift
bdir=$1
shift
bdir_base=`basename $bdir`

if [ ! -x "$javaexe" ] ; then
    echo java-exe "$javaexe" is not an executable
    print_usage
    exit 1
fi
if [ ! -d $bdir ] ; then
    echo build-dir $bdir is not a directory
    print_usage
    exit 1
fi

#
# See https://github.com/kcat/openal-soft/blob/master/docs/env-vars.txt
# export ALSOFT_LOGLEVEL=3
#

rm -f java-run.log

spath=`dirname $0`

unset CLASSPATH

which "$javaexe" 2>&1 | tee -a java-run.log
"$javaexe" -version 2>&1 | tee -a java-run.log

GLUEGEN_DIR=$spath/../../../gluegen
GLUEGEN_BDIR=$GLUEGEN_DIR/$bdir_base
if [ ! -d $GLUEGEN_DIR -o ! -d $GLUEGEN_BDIR ] ; then
    echo GLUEGEN not found
    print_usage
    exit
fi
JUNIT_JAR=$GLUEGEN_DIR/make/lib/junit.jar

if [ -z "$ANT_PATH" ] ; then
    ANT_PATH=$(dirname $(dirname $(which ant)))
    if [ -e $ANT_PATH/lib/ant.jar ] ; then
        export ANT_PATH
        echo autosetting ANT_PATH to $ANT_PATH
    fi
fi
if [ -z "$ANT_PATH" ] ; then
    echo ANT_PATH does not exist, set it
    print_usage
    exit
fi
ANT_JARS=$ANT_PATH/lib/ant.jar:$ANT_PATH/lib/ant-junit.jar

function jrun() {
    #D_ARGS="-Djogamp.debug=all"
    #D_ARGS="-Djogamp.debug.Bitstream"
    #D_ARGS="-Djogamp.debug.NativeLibrary=true -Djoal.debug=true"
    #D_ARGS="-Djogamp.debug.AudioSink"
    #D_ARGS="-Djogamp.debug.AudioSink -Djoal.debug.AudioSink.trace"
    #D_ARGS="-Djoal.debug.AudioSink.trace"
    #D_ARGS="-Djoal.debug=all"
    #D_ARGS="-Djogamp.debug.JNILibLoader"
    #X_ARGS="-verbose:jni"
    #X_ARGS="-Xcheck:jni"
    #X_ARGS="-verbose:jni -Xcheck:jni -Xrs -Xnoclassgc"
    #X_ARGS="-Xrs"

    # StartFlightRecording: delay=10s,
    # FlightRecorderOptions: stackdepth=2048
    # Enable remote connection to jmc: jcmd <PID> ManagementAgent.start jmxremote.authenticate=false jmxremote.ssl=false jmxremote.port=7091
    # X_ARGS="-XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints $X_ARGS"
    # X_ARGS="-XX:FlightRecorderOptions=stackdepth=2048,threadbuffersize=16k $X_ARGS"
    # X_ARGS="-XX:StartFlightRecording=delay=10s,dumponexit=true,filename=java-run.jfr $X_ARGS"

    export CLASSPATH=$GLUEGEN_BDIR/gluegen-rt.jar:$bdir/jar/joal.jar:$bdir/jar/joal-test.jar:$JUNIT_JAR:$ANT_JARS
    #export CLASSPATH=$GLUEGEN_BDIR/gluegen-rt.jar:$bdir/jar/joal.jar:$bdir/jar/joal-natives.jar:$bdir/jar/joal-test.jar:$JUNIT_JAR:$ANT_JARS
    echo CLASSPATH $CLASSPATH

    echo
    echo "Test Start: $*"
    echo
    echo "$javaexe" $javaxargs $X_ARGS $D_ARGS $C_ARG $*
    #gdb --args "$javaexe" $javaxargs $X_ARGS $D_ARGS $C_ARG $*
    "$javaexe" $javaxargs $X_ARGS $D_ARGS $C_ARG $*
    echo
    echo "Test End: $*"
    echo
}

function testnormal() {
    jrun $* 2>&1 | tee -a java-run.log
}


#testnormal com.jogamp.openal.JoalVersion $*
#testnormal com.jogamp.openal.test.manual.OpenALTest $*
#testnormal com.jogamp.openal.test.manual.Sound3DTest $*
#testnormal com.jogamp.openal.test.manual.Synth01AL $*
#testnormal com.jogamp.openal.test.manual.Synth02AL $*
testnormal com.jogamp.openal.test.manual.Synth02bAL $*
#testnormal com.jogamp.openal.test.manual.ALCSystemEventTest $*
#testnormal com.jogamp.openal.test.junit.ALDebugExtTest $*
#testnormal com.jogamp.openal.test.junit.ALVersionTest $*
#testnormal com.jogamp.openal.test.junit.ALutWAVLoaderTest $*
#testnormal com.jogamp.openal.test.junit.ALExtLoopbackDeviceSOFTTest $*
