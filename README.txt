/**
* Copyright (c) 2003 Sun Microsystems, Inc. All  Rights Reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
* -Redistribution of source code must retain the above copyright notice, 
* this list of conditions and the following disclaimer.
*
* -Redistribution in binary form must reproduce the above copyright notice, 
* this list of conditions and the following disclaimer in the documentation
* and/or other materials provided with the distribution.
*
* Neither the name of Sun Microsystems, Inc. or the names of contributors may 
* be used to endorse or promote products derived from this software without 
* specific prior written permission.
* 
* This software is provided "AS IS," without a warranty of any kind.
* ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
* ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
* NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN") AND ITS
* LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A
* RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
* IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT
* OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
* PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
* ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
* BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
*
* You acknowledge that this software is not designed or intended for use in the
* design, construction, operation or maintenance of any nuclear facility.
*/

Project: net.java.games.joal.* & net.java.games.sound3d.*
Purpose Open Source Java Bindings for OpenAL and Object-Oriented 3D sound toolkit
Author:
    -- JOAL/Sound3D API Original Author
        Athomas Goldberg
        Wildcard
        Java Games Initiative
        Software Advanced Technologies Group,
        Sun Microsystems
    -- This file updated 06/02/2003
    
Introduction:

This is the source tree for the Java Game Initiative (JGI) Open Source
client game programming APIs for OpenAL and Sound3D.

Build Requirements:

This project has been built in the following environment.
 -- Win32 (Win XP in the case of our machine)
 -- Sun J2SDK 1.4.2 (available at java.sun.com)
 -- OpenAL1.0 SDK from Creative Labs (available at http://developer.creative.com
    under "Gaming -> Development Kits -> Open AL")
 -- MinGW 2.0.0  plus the following updates: (all available at www.mingw.org) 
     -- binutils 2.13.90
     -- w32api-2.2
     -- mingw-runtime-2.4
 -- ANT 1.5.3 (available at apache.org)
 -- JUnit 3.8.1 (available at junit.org) copy junit.jar to the apache-ant lib directory

Directory Organization:

The root contains a master ANT build.xml and the following sub directories:
 -- apidocs   Where the javadocs get built to
 -- lib    Where the Jar and DLL files get built to
 -- src    The actual source for the JGI APIs.
 -- www    JGI project webpage files

Build instructions:

Edit the value of the jdk.home property in the root build.xml file to point
to your Java 2 SDK installation (ex: c:/j2sdk1.4.2)

Edit the value of the openal.home property in the root build.xml file to point
to your OpenAL SDK installation (ex: c:/program files/creative labs/openal 1.0 sdk)

To clean: ant clean
To build:  ant all (or just ant)
To build docs: ant javadoc
To test: ant runtests
    
Release Info:
    Initial Release:  This release contains an implementation of the Java
    bindings for OpenAL, as well as the Sound3D Object-Oriented toolkit for games.

Additional Information
    This release includes source code adapted from the Lightweight Java Games Library
    subject to the following terms and conditions:
    
 * Copyright (c) 2002 Light Weight Java Game Library Project
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are 
 * met:
 * 
 * * Redistributions of source code must retain the above copyright 
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'Light Weight Java Game Library' nor the names of 
 *   its contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


