/**
 * Copyright (c) 2010-2023 JogAmp Community. All rights reserved.
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
 */

package com.jogamp.openal.sound3d;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.jogamp.openal.AL;
import com.jogamp.openal.ALC;
import com.jogamp.openal.ALException;
import com.jogamp.openal.ALExt;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.UnsupportedAudioFileException;
import com.jogamp.openal.util.WAVData;
import com.jogamp.openal.util.WAVLoader;

import jogamp.openal.Debug;

/**
 * The AudioSystem3D class provides a set of methods for creating and
 * manipulating a 3D audio environment.
 *
 * @author Athomas Goldberg
 */
public class AudioSystem3D {
  static boolean DEBUG = Debug.debug("AudioSystem3D");
  static final AL al;
  static final ALC alc;
  static final ALExt alExt;
  static final boolean staticAvailable;
  static Listener listener;

  static {
      ALC _alc = null;
      AL _al = null;
      ALExt _alExt = null;
      try {
          _alc = ALFactory.getALC();
          _al = ALFactory.getAL();
          _alExt = ALFactory.getALExt();
      } catch(final Throwable t) {
          if( DEBUG ) {
              System.err.println("AudioSystem3D: Caught "+t.getClass().getName()+": "+t.getMessage());
              t.printStackTrace();
          }
      }
      alc = _alc;
      al = _al;
      alExt = _alExt;
      staticAvailable = null != alc && null != al && null != alExt;
  }

  /**
   * Initialize the Sound3D environment.
   * @deprecated Not required to be called due to static initialization
   */
  @Deprecated
  public static void init() throws ALException {  }

  /**
   * Returns the <code>available state</code> of this instance.
   * <p>
   * The <code>available state</code> is affected by this instance
   * overall availability, i.e. after instantiation.
   * </p>
   */
  public static boolean isAvailable() { return staticAvailable; }

  public int getALError() {
      return al.alGetError();
  }

  /**
   * Creates a new Sound3D Context for a specified device.
   *
   * @param device The device the Context is being created for.
   *
   * @return The new Sound3D context.
   */
  public static Context createContext(final Device device) {
      return new Context(device);
  }

  /**
   * Makes the specified context the current context.
   *
   * @param context the context to make current.
   */
  public static boolean makeContextCurrent(final Context context) {
    return context.makeCurrent();
  }

  /**
   * Release the specified context.
   *
   * @param context the context to release.
   */
  public static boolean releaseContext(final Context context) {
    return context.release();
  }

  /**
   * Opens the named audio device.
   *
   * @param deviceName The specified device name, null for default.
   *
   * @return The device described by the specified name
   */
  public static Device openDevice(final String deviceName) {
    return new Device(deviceName);
  }

  /**
   * Generate an array of Sound3D buffers.
   *
   * @param numBuffers The number of Sound3D buffers to generate.
   *
   * @return an array of (initially enpty) Sound3D buffers.
   */
  public static Buffer[] generateBuffers(final int numBuffers) {
    final Buffer[] result = new Buffer[numBuffers];
    final int[] arr = new int[numBuffers];
    al.alGenBuffers(numBuffers, arr, 0);

    for (int i = 0; i < numBuffers; i++) {
      result[i] = new Buffer(arr[i]);
    }

    return result;
  }

  /**
   * Loads a Sound3D buffer with the specified audio file.
   *
   * @param filename the name of the file to load.
   *
   * @return a new Sound3D buffer containing the audio data from the
   * specified file.
   *
   * @throws IOException If the file cannot be found or some other IO error
   * occurs.
   * @throws UnsupportedAudioFileException If the format of the audio data is
   * not supported
   */
  public static Buffer loadBuffer(final String filename)
    throws IOException, UnsupportedAudioFileException {
    Buffer result;
    final Buffer[] tmp = generateBuffers(1);
    result = tmp[0];

    final WAVData wd = WAVLoader.loadFromFile(filename);
    result.configure(wd.data, wd.format, wd.freq);

    return result;
  }

  /**
   * Loads a Sound3D buffer with the specified audio file.
   *
   * @param stream contains the stream associated with the audio file.
   *
   * @return a new Sound3D buffer containing the audio data from the
   * passed stream.
   *
   * @throws IOException If the stream cannot be read or some other IO error
   * occurs.
   * @throws UnsupportedAudioFileException If the format of the audio data is
   * not supported
   */
  public static Buffer loadBuffer(InputStream stream)
    throws IOException, UnsupportedAudioFileException {
    Buffer result;
    final Buffer[] tmp = generateBuffers(1);
    result = tmp[0];

    if (!(stream instanceof BufferedInputStream)) {
      stream = new BufferedInputStream(stream);
    }
    final WAVData wd = WAVLoader.loadFromStream(stream);

    result.configure(wd.data, wd.format, wd.freq);

    return result;
  }

  /**
   * Loads a Sound3D Source with the specified audio file. This is
   * functionally equivalent to generateSource(loadBuffer(fileName));
   *
   * @param filename the name of the file to load.
   *
   * @return a new Sound3D Source containing the audio data from the
   * specified file.
   *
   * @throws IOException If the file cannot be found or some other IO error
   * occurs.
   * @throws UnsupportedAudioFileException If the format of the audio data is
   * not supported
   */
  public static Source loadSource(final String filename)
    throws IOException, UnsupportedAudioFileException {
    final Buffer buffer = loadBuffer(filename);

    return generateSource(buffer);
  }

  /**
   * Loads a Sound3D Source with the specified audio stream. This is
   * functionally equivalent to generateSource(loadBuffer(stream));
   *
   * @param stream contains the stream associated with the audio file.
   *
   * @return a new Sound3D Source containing the audio data from the
   * passed stream.
   *
   * @throws IOException If the file cannot be found or some other IO error
   * occurs.
   * @throws UnsupportedAudioFileException If the format of the audio data is
   * not supported
   */
  public static Source loadSource(final InputStream stream)
    throws IOException, UnsupportedAudioFileException {
    final Buffer buffer = loadBuffer(stream);

    return generateSource(buffer);
  }

  /**
   * Generates a set of uninitialized Source3D sources
   *
   * @param numSources the number of Sound3D sources to generate.
   *
   * @return an array of uninitialized sources.
   */
  public static Source[] generateSources(final int numSources) {
    final Source[] result = new Source[numSources];
    final int[] arr = new int[numSources];
    al.alGenSources(numSources, arr, 0);

    for (int i = 0; i < numSources; i++) {
      result[i] = new Source(arr[i]);
    }

    return result;
  }

  /**
   * Generate a Sound3D source from an initialized Buffer.
   *
   * @param buff The buffer to be associate with the source.
   *
   * @return the newly generated Source.
   */
  public static Source generateSource(final Buffer buff) {
    Source result = null;
    final Source[] tmp = generateSources(1);
    result = tmp[0];
    result.setBuffer(buff);

    return result;
  }

  /**
   * Get the listener object associated with this Sound3D environment.
   *
   * @return The listener object.
   */
  public static Listener getListener() {
    if (listener == null) {
      listener = new Listener();
    }
    return listener;
  }
}
