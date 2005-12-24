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

package net.java.games.joal.util;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import javax.sound.sampled.*;

import net.java.games.joal.*;

/**
 * A Loader utility for (.wav) files. Creates a WAVData object containing the
 * data used by the AL.alBufferData method.
 *
 * @author Athomas Goldberg
 */
public class WAVLoader implements ALConstants {
    private static final int BUFFER_SIZE = 128000;

    /**
     * This method loads a (.wav) file into a WAVData object.
     *
     * @param filename The name of the (.wav) file
     *
     * @return a WAVData object containing the audio data 
     *
     * @throws UnsupportedAudioFileException if the format of the audio if not
     *                                       supported. 
     * @throws IOException If the file can no be found or some other IO error 
     *                     occurs
     */
    public static WAVData loadFromFile(String filename)
        throws UnsupportedAudioFileException, IOException {
        WAVData result = null;
        File soundFile = new File(filename);
        AudioInputStream aIn = AudioSystem.getAudioInputStream(soundFile);
        return readFromStream(aIn);
    }

    /**
     * This method loads a (.wav) file into a WAVData object.
     *
     * @param stream An InputStream for the .WAV file.
     *
     * @return a WAVData object containing the audio data 
     *
     * @throws UnsupportedAudioFileException if the format of the audio if not
     *                                       supported. 
     * @throws IOException If the file can no be found or some other IO error 
     *                     occurs
     */
    public static WAVData loadFromStream(InputStream stream)
        throws UnsupportedAudioFileException, IOException {
        WAVData result = null;
        AudioInputStream aIn = AudioSystem.getAudioInputStream(stream);
        return readFromStream(aIn);
    }


    private static WAVData readFromStream(AudioInputStream aIn)
      throws UnsupportedAudioFileException, IOException {
        ReadableByteChannel aChannel = Channels.newChannel(aIn);
        AudioFormat fmt = aIn.getFormat();
        int numChannels = fmt.getChannels();
        int bits = fmt.getSampleSizeInBits();
        int format = AL_FORMAT_MONO8;

        if ((bits == 8) && (numChannels == 1)) {
            format = AL_FORMAT_MONO8;
        } else if ((bits == 16) && (numChannels == 1)) {
            format = AL_FORMAT_MONO16;
        } else if ((bits == 8) && (numChannels == 2)) {
            format = AL_FORMAT_STEREO8;
        } else if ((bits == 16) && (numChannels == 2)) {
            format = AL_FORMAT_STEREO16;
        }

        int freq = Math.round(fmt.getSampleRate());
        int size = aIn.available();
        ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        while (buffer.remaining() > 0) {
          aChannel.read(buffer);
        }
        buffer.rewind();

        // Must byte swap on big endian platforms
        // Thanks to swpalmer on javagaming.org forums for hint at fix
        if ((bits == 16) && (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN)) {
          int len = buffer.remaining();
          for (int i = 0; i < len; i += 2) {
            byte a = buffer.get(i);
            byte b = buffer.get(i+1);
            buffer.put(i, b);
            buffer.put(i+1, a);
          }
        }

        WAVData result = new WAVData(buffer, format, size, freq, false);
        aIn.close();

        return result;
    }
}
