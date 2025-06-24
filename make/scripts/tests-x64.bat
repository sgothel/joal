
set BLD_SUB=build-win64
set J2RE_HOME=c:\jdk-21
set JAVA_HOME=c:\jdk-21
set ANT_PATH=C:\apache-ant-1.10.5

REM set TEMP=C:\Documents and Settings\jogamp\temp
REM set TMP=C:\Documents and Settings\jogamp\temp
REM set TEMP=C:\Users\jogamp\temp\no-exec
REM set TMP=C:\Users\jogamp\temp\no-exec

set PATH=%J2RE_HOME%\bin;%JAVA_HOME%\bin;%ANT_PATH%\bin;%PATH%
REM set PATH=%J2RE_HOME%\bin;%JAVA_HOME%\bin;%ANT_PATH%\bin;C:\temp;%PATH%
echo PATH %PATH%

set BLD_DIR=..\%BLD_SUB%
REM set LIB_DIR=%cd%\%BLD_DIR%\lib;%cd%\..\..\gluegen\%BLD_SUB%\obj
set LIB_DIR=

set CP_ALL=.;%BLD_DIR%\jar\joal.jar;%BLD_DIR%\jar\joal-test.jar;..\..\gluegen\%BLD_SUB%\gluegen-rt.jar;..\..\gluegen\%BLD_SUB%\gluegen-test-util.jar;..\..\gluegen\make\lib\junit.jar;%ANT_PATH%\lib\ant.jar;%ANT_PATH%\lib\ant-junit.jar
echo CP_ALL %CP_ALL%

REM set MODULE_ARGS=--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.desktop/sun.awt=ALL-UNNAMED --add-opens java.desktop/sun.java2d=ALL-UNNAMED
set MODULE_ARGS=--add-opens java.desktop/sun.awt=ALL-UNNAMED --add-opens java.desktop/sun.java2d=ALL-UNNAMED
set X_ARGS="-Dsun.java2d.noddraw=true" "-Dsun.awt.noerasebackground=true" %MODULE_ARGS%

set D_ARGS="-Djogamp.debug=all"
REM set D_ARGS="-Djogamp.debug.NativeLibrary=true" "-Djogamp.debug.NativeLibrary.Lookup=true"
REM set D_ARGS="-Djogamp.debug.ProcAddressHelper" "-Djogamp.debug.NativeLibrary" "-Djogamp.debug.NativeLibrary.Lookup" "-Djogamp.debug.JNILibLoader" "-Djogamp.debug.TempJarCache" "-Djogamp.debug.JarUtil"
REM set D_ARGS="-Djogamp.debug.ProcAddressHelper=true"
REM set D_ARGS="-Djogamp.debug.JNILibLoader=true" "-Djogamp.debug.NativeLibrary=true" "-Djogamp.debug.NativeLibrary.Lookup=true"

scripts\tests-win.bat %*

