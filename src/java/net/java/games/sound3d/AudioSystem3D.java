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
 */

package net.java.games.sound3d;

import net.java.games.joal.*;
import net.java.games.joal.util.WAVData;
import net.java.games.joal.util.WAVLoader;

import java.io.*;

import javax.sound.sampled.UnsupportedAudioFileException;


/**
 * The AudioSystem3D class provides a set of methods for creating and
 * manipulating a 3D audio environment.
 *
 * @author Athomas Goldberg
 */
public class AudioSystem3D {
  private static AL al;
  private static ALC alc;
  private static Listener listener;

  /**
   * Iniitalize the Sound3D environment. This must be called before
   * other methods in the class can be used.
   */
  public static void init() throws ALException {
    al = ALFactory.getAL();
    alc = ALFactory.getALC();
  }

  /**
   * Creates a new Sound3D Context for a specified device.
   *
   * @param device The device the Context is being created for.
   *
   * @return The new Sound3D context.
   */
  public static Context createContext(Device device) {
    Context result = null;
    ALCcontext realContext = alc.alcCreateContext(device.realDevice, null);
    result = new Context(alc, realContext, device);
    return result;
  }

  /**
   * Makes the specified context the current context.
   *
   * @param context the context to make current.
   */
  public static void makeContextCurrent(Context context) {
    ALCcontext realContext = null;

    if (context != null) {
      realContext = context.realContext;
    }

    alc.alcMakeContextCurrent(realContext);
  }

  /**
   * Opens the specifified audio device. 
   *
   * @param deviceName The specified device name, On windows this will be 
   * DirectSound3D. We will be automating device discovery in upcoming versions
   * of this class.
   *
   * @return The device described by the specifed name.
   */
  public static Device openDevice(String deviceName) {
    Device result = null;
    ALCdevice realDevice = alc.alcOpenDevice(deviceName);
    result = new Device(alc, realDevice);

    return result;
  }

  /**
   * Generate an array of Sound3D buffers.
   *
   * @param numBuffers The number of Sound3D buffers to generate.
   *
   * @return an array of (initially enpty) Sound3D buffers.
   */
  public static Buffer[] generateBuffers(int numBuffers) {
    Buffer[] result = new Buffer[numBuffers];
    int[] arr = new int[numBuffers];
    al.alGenBuffers(numBuffers, arr, 0);

    for (int i = 0; i < numBuffers; i++) {
      result[i] = new Buffer(al, arr[i]);
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
  public static Buffer loadBuffer(String filename)
    throws IOException, UnsupportedAudioFileException {
    Buffer result;
    Buffer[] tmp = generateBuffers(1);
    result = tmp[0];

    WAVData wd = WAVLoader.loadFromFile(filename);
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
    Buffer[] tmp = generateBuffers(1);
    result = tmp[0];
 
    if (!(stream instanceof BufferedInputStream)) {
      stream = new BufferedInputStream(stream);
    }
    WAVData wd = WAVLoader.loadFromStream(stream);
 
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
  public static Source loadSource(String filename)
    throws IOException, UnsupportedAudioFileException {
    Buffer buffer = loadBuffer(filename);

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
  public static Source loadSource(InputStream stream)
    throws IOException, UnsupportedAudioFileException {
    Buffer buffer = loadBuffer(stream);

    return generateSource(buffer);
  }

  /**
   * Generates a set of uninitialized Source3D sources
   *
   * @param numSources the number of Sound3D sources to generate.
   *
   * @return an array of uninitialized sources.
   */
  public static Source[] generateSources(int numSources) {
    Source[] result = new Source[numSources];
    int[] arr = new int[numSources];
    al.alGenSources(numSources, arr, 0);

    for (int i = 0; i < numSources; i++) {
      result[i] = new Source(al, arr[i]);
    }

    return result;
  }

  /**
   * Generate a Sound3D source from an initialized Buffer.
   *
   * @param buff The buffer to generate the source from.
   *
   * @return the newly generated Source.
   */
  public static Source generateSource(Buffer buff) {
    Source result = null;
    Source[] tmp = generateSources(1);
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
      listener = new Listener(al);
    }

    return listener;
  }
}
