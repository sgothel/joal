Project: com.jogamp.openal.* & com.jogamp.openal.sound3d.*
Purpose: Open Source Java Bindings for OpenAL and Object-Oriented 3D sound toolkit
License: See LICENSE.txt
Author:
    -- JOAL/Sound3D API Original Author
        Athomas Goldberg
        Wildcard
        Java Games Initiative
        Software Advanced Technologies Group,
        Sun Microsystems
        (see git log)
    -- This file updated 08/19/2003 by Ole Arndt <ole at sugarshark dot com>
    -- This file updated 11/23/2003 by Gregory Pierce <gpierce@gregorypierce.com>    
    -- This file updated 12/21/2005 by Kenneth Russell <kbr@dev.java.net>
    -- This file updated 02/15/2014 by Sven Gothel <sgothel@jausoft.com> (see git log)

Introduction:
=============

The JOAL Project hosts a reference implementation of the
Java bindings for OpenAL API, and is designed to provide
hardware-supported 3D spatialized audio for applications written
in Java.

This project also hosts the Sound3D Toolkit, a high level
API for spatialized audio built on top of the OpenAL bindings.
This toolkit is designed to provide access to all the features
of OpenAL through an intuitive, easy to use, object-oriented
interface.


Build Requirements:
===================

This project has been built under Win32, Linux, Android and MacOS. 

Check GlueGen's HowToBuild <https://jogamp.org/gluegen/doc/HowToBuild.html>
for basic prerequisites.

Additionally the following packages and tools have been used:

* All Systems:
  -- See GlueGen's HowToBuild <https://jogamp.org/gluegen/doc/HowToBuild.html>

* Windows:
  -- CMake 3.15.2 <https://cmake.org/download/>

* Linux:
  -- cmake
  -- OpenAL Soft: OpenAL: ALSA, OSS, PulseAudio, WaveFile, Null
     
     apt-get install cmake autoconf \
             libpulse-dev libpulse0:amd64 libpulse0:i386 pulseaudio \
             libasound2-dev libasound2:amd64 libasound2:i386

* OSX
  -- OSX 10.2 or later
  -- OSX Developer Tools Xcode
  -- CMake 3.15.2 <https://cmake.org/download/> 
     and install the commandline tools <https://stackoverflow.com/questions/30668601/installing-cmake-command-line-tools-on-a-mac>

JOAL requires the GlueGen workspace to be checked out as a sibling
directory to the joal directory. 
See GlueGen's HowToBuild <https://jogamp.org/gluegen/doc/HowToBuild.html>

Directory Organization:
=======================

  -- make           Build-related files and the main build.xml
  -- src            The actual source for the JOAL APIs.
  -- build          (generated directory) Where the Jar and DLL files get built to
  -- javadoc_public (generated directory) Where the public Javadoc gets built to
  -- unit_tests     A couple of small tests
  -- www            JOAL project webpage files

GIT
====

JOAL can be build w/ openal-soft, which is a git submodule of JOAL.
This is the default for our JogAmp build on all platforms.

Cloning [and pulling] JOAL incl. openal-soft 
can be performed w/ the option '--recurse-submodules'.

   > cd /home/dude/projects/jogamp/
   > git clone --recurse-submodules git://jogamp.org/srv/scm/joal.git
   > cd joal ; git pull --recurse-submodules
 
JOAL Build Instructions:
===================

Change into the joal/make directory
   > cd /home/dude/projects/jogamp/make/

To clean: 
   > ant clean
To build:
   > ant -Dtarget.sourcelevel=1.8 -Dtarget.targetlevel=1.8 -Dtarget.rt.jar=/your/openjdk8/lib/rt.jar
To build docs:
   > ant -Dtarget.sourcelevel=1.8 -Dtarget.targetlevel=1.8 -Dtarget.rt.jar=/your/openjdk8/lib/rt.jar javadoc
To test:
   > ant -Dtarget.sourcelevel=1.8 -Dtarget.targetlevel=1.8 -Dtarget.rt.jar=/your/openjdk8/lib/rt.jar runtests

Instead of properties, you may also use environment variables, 
see GlueGen's HowToBuild <https://jogamp.org/gluegen/doc/HowToBuild.html>.

Release Info:
    Initial Release:  This release contains an implementation of the Java
    bindings for OpenAL, as well as the Sound3D Object-Oriented toolkit for games.
