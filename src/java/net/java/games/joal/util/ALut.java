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
* NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS
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
*
* Created on Jun 27, 2003
*/

package net.java.games.joal.util;

import java.io.*;
import java.nio.ByteBuffer;

import javax.sound.sampled.UnsupportedAudioFileException;

import net.java.games.joal.*;

/**
 * @author Athomas Goldberg
 *
 */
public final class ALut {

  private static ALC alc;
  private static ALCdevice device;
  private static ALCcontext context;
  private static Thread initializingThread;

  private ALut() { }

  /** Initializes the OpenAL Utility Toolkit, creates an OpenAL
      context and makes it current on the current thread. The ALut may
      only be initialized on one thread at any given time. */
  public static synchronized void alutInit() throws ALException {
    if (context != null) {
      throw new ALException("Already initialized on thread " + initializingThread.getName());
    }
    if (alc == null) {
      alc = ALFactory.getALC();
    }
    String deviceName = null;
    ALCdevice d = alc.alcOpenDevice(deviceName);
    if (d == null) {
      throw new ALException("Error opening default OpenAL device");
    }
    ALCcontext c = alc.alcCreateContext(d, null);
    if (c == null) {
      alc.alcCloseDevice(d);
      throw new ALException("Error creating OpenAL context");
    }
    alc.alcMakeContextCurrent(c);
    if (alc.alcGetError(d) != 0) {
      alc.alcDestroyContext(c);
      alc.alcCloseDevice(d);
      throw new ALException("Error making OpenAL context current");
    }
    // Fully initialized; finish setup
    device = d;
    context = c;
    initializingThread = Thread.currentThread();
  }

  /** Shuts down the OpenAL Utility Toolkit; releases and destroys the
      internal OpenAL context and closes the output device. Must be
      called from the same thread as alutInit(). Most applications
      should not need to call this; only those which wish to toggle
      sound on / off at run time by initializing and un-initializing
      OpenAL need to call it. */
  public static synchronized void alutExit() throws ALException {
    if (context == null) {
      throw new ALException("Not initialized");
    }
    alc.alcMakeContextCurrent(null);
    alc.alcDestroyContext(context);
    alc.alcCloseDevice(device);
    context = null;
    device = null;
    initializingThread = null;
  }

  public static void alutLoadWAVFile(String fileName,
                                     int[] format,
                                     ByteBuffer[] data,
                                     int[] size,
                                     int[] freq,
                                     int[] loop) throws ALException {
    try {
      WAVData wd = WAVLoader.loadFromFile(fileName);
      format[0] = wd.format;
      data[0] = wd.data;
      size[0] = wd.size;
      freq[0] = wd.freq;
      loop[0] = wd.loop ? AL.AL_TRUE : AL.AL_FALSE;
    } catch (Exception e) {
      throw new ALException(e);
    }
  }

  public static void alutLoadWAVFile(InputStream stream,
                                     int[] format,
                                     ByteBuffer[] data,
                                     int[] size,
                                     int[] freq,
                                     int[] loop) throws ALException {
    try {
      if (!(stream instanceof BufferedInputStream)) {
        stream = new BufferedInputStream(stream);
      }
      WAVData wd = WAVLoader.loadFromStream(stream);
      format[0] = wd.format;
      data[0] = wd.data;
      size[0] = wd.size;
      freq[0] = wd.freq;
      loop[0] = wd.loop ? AL.AL_TRUE : AL.AL_FALSE;
    } catch (Exception e) {
      throw new ALException(e);
    }
  }
}
