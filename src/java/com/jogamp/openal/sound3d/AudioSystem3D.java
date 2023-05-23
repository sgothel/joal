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
import com.jogamp.openal.ALCConstants;
import com.jogamp.openal.ALCcontext;
import com.jogamp.openal.ALCdevice;
import com.jogamp.openal.ALConstants;
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
 * @author Athomas Goldberg, Sven Gothel, et al.
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

  /** Return OpenAL global {@link AL}. */
  public static final AL getAL() { return al; }
  /** Return OpenAL global {@link ALC}. */
  public static final ALC getALC() { return alc; }
  /** Return OpenAL global {@link ALExt}. */
  public static final ALExt getALExt() { return alExt; }

  public static int getALError() {
      return al.alGetError();
  }

  /**
   * Returns true if an OpenAL ALC or AL error occurred, otherwise false
   * @param device referencing an {@link ALCdevice}, may be null
   * @param prefix prefix to print on error and if `verbose`
   * @param verbose pass true to show errors
   * @param throwException true to throw an ALException on error
   * @return true if an error occurred, otherwise false
   */
  public static boolean checkError(final Device device, final String prefix, final boolean verbose, final boolean throwException) {
      if( !checkALCError(device, prefix, verbose, throwException) ) {
          return checkALError(prefix, verbose, throwException);
      }
      return false; // no error
  }

  /**
   * Returns true if an OpenAL AL error occurred, otherwise false
   * @param prefix prefix to print on error and if `verbose`
   * @param verbose pass true to show errors
   * @param throwException true to throw an ALException on error
   * @return true if an error occurred, otherwise false
   */
  public static boolean checkALError(final String prefix, final boolean verbose, final boolean throwException) {
      final int alErr = al.alGetError();
      if( ALConstants.AL_NO_ERROR != alErr ) {
          final String msg = prefix+": AL error 0x"+Integer.toHexString(alErr)+", '"+al.alGetString(alErr);
          if( verbose ) {
              System.err.println(msg);
          }
          if( throwException ) {
              throw new ALException(msg);
          }
          return true;
      }
      return false;
  }
  /**
   * Returns true if an OpenAL ALC error occurred, otherwise false
   * @param device referencing an {@link ALCdevice}, may be null
   * @param prefix prefix to print on error and if `verbose`
   * @param verbose pass true to show errors
   * @param throwException true to throw an ALException on error
   * @return true if an error occurred, otherwise false
   */
  public static boolean checkALCError(final Device device, final String prefix, final boolean verbose, final boolean throwException) {
      final ALCdevice alcDevice = null != device ? device.getALDevice() : null;
      final int alcErr = alc.alcGetError( alcDevice );
      if( ALCConstants.ALC_NO_ERROR != alcErr ) {
          final String msg = prefix+": ALC error 0x"+Integer.toHexString(alcErr)+", "+alc.alcGetString(alcDevice, alcErr);
          if( verbose ) {
              System.err.println(msg);
          }
          if( throwException ) {
              throw new ALException(msg);
          }
          return true;
      }
      return false;
  }

  /**
   * Creates a new Sound3D Context for a specified device including native {@link ALCcontext} creation.
   *
   * @param device The device the Context is being created for, must be valid
   * @return The new Sound3D context.
   */
  public static Context createContext(final Device device) {
      return new Context(device, null);
  }

  /**
   * Creates a new Sound3D Context for a specified device including native {@link ALCcontext} creation.
   *
   * @param device The device the Context is being created for, must be valid.
   * @param attributes list of {@link ALCcontext} attributes for context creation, maybe empty or null
   * @return The new Sound3D context.
   */
  public static Context createContext(final Device device, final int[] attributes) {
      return new Context(device, attributes);
  }

  /**
   * Returns this thread current context.
   * If no context is current, returns null.
   *
   * @return the context current on this thread, or null if no context is current.
   * @see Context#getCurrentContext()
   * @see #makeContextCurrent(Context)
   * @see #releaseContext(Context)
   */
  public static Context getCurrentContext() {
      return Context.getCurrentContext();
  }

  /**
   * Makes the audio context current on the calling thread.
   * <p>
   * Recursive calls are supported.
   * </p>
   * <p>
   * At any point in time one context can only be current by one thread,
   * and one thread can only have one context current.
   * </p>
   * @param context the context to make current.
   * @param throwException if true, throws ALException if {@link #getALContext()} is null, current thread holds another context or failed to natively make current
   * @return true if current thread holds no other context and context successfully made current, otherwise false
   * @see Context#makeCurrent()
   * @see #releaseContext(Context)
   */
  public static boolean makeContextCurrent(final Context context, final boolean throwException) {
    return context.makeCurrent(throwException);
  }

  /**
   * Releases control of this audio context from the current thread, if implementation utilizes context locking.
   * <p>
   * Recursive calls are supported.
   * </p>
   * @param context the context to release.
   * @param throwException if true, throws ALException if context has not been previously made current on current thread
   *                       or native release failed.
   * @return true if context has previously been made current on the current thread and successfully released, otherwise false
   * @see Context#release()
   * @see #makeContextCurrent(Context)
   */
  public static boolean releaseContext(final Context context, final boolean throwException) {
    return context.release(throwException);
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
