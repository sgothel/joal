set THISDIR="C:\JOGL"

set J2RE_HOME=c:\jre1.7.0_45_x64
set JAVA_HOME=c:\jdk1.7.0_45_x64
set ANT_PATH=C:\apache-ant-1.8.2
set CMAKE_PATH=C:\cmake-2.8.10.2-win32-x86
set CMAKE_C_COMPILER=c:\mingw64\bin\gcc

set PATH=%JAVA_HOME%\bin;%ANT_PATH%\bin;c:\mingw64\bin;c:\mingw\bin;%CMAKE_PATH%\bin;%PATH%

set LIB_GEN=%THISDIR%\lib
set CLASSPATH=.;%THISDIR%\build-win64\classes
REM    -Dc.compiler.debug=true 

set SOURCE_LEVEL=1.6
set TARGET_LEVEL=1.6
set TARGET_RT_JAR=c:\jre1.6.0_30\lib\rt.jar

REM set JOGAMP_JAR_CODEBASE=Codebase: *.jogamp.org
set JOGAMP_JAR_CODEBASE=Codebase: *.goethel.localnet

ant -Dc.compiler.debug=true -Drootrel.build=build-win64 %1 %2 %3 %4 %5 %6 %7 %8 %9 > make.joal.all.win64.log 2>&1
