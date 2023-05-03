/**
* Copyright (c) 2003 Sun Microsystems, Inc. All  Rights Reserved.
* Copyright (c) 2011 JogAmp Community. All rights reserved.
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

package com.jogamp.openal.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.jogamp.common.util.IOUtil;
import com.jogamp.openal.ALConstants;
import com.jogamp.openal.UnsupportedAudioFileException;

/**
 * This class is a holder for WAV (.wav ) file Data returned from the WavLoader,
 * or directly via  {@link #loadFromStream(InputStream, int, int, int)}.
 *
 * @author Athomas Goldberg, et.al.
 */
public final class WAVData {
    /** The audio data */
    public final ByteBuffer data;

    /** the format of the Data. One of:
     * <pre>
     * AL.AL_FORMAT_MONO8
     * AL.AL_FORMAT_MONO16
     * AL.AL_FORMAT_STEREO8
     * AL.AL_FORMAT_STEREO16
     * </pre>
     *
     */
    public final int format;

    /** Size (in bytes) of the data */
    public final int size;

    /** The frequency of the data */
    public final int freq;

    /** flag indicating whether or not the sound in the data should loop */
    public final boolean loop;

    public WAVData(final ByteBuffer data, final int format, final int size, final int freq, final boolean loop) {
        this.data = data;
        this.format = format;
        this.size = size;
        this.freq = freq;
        this.loop = loop;
    }

    /**
     * This method loads a (.wav) file into a WAVData object.
     * @param aIn An InputStream for the .WAV stream
     * @param byteCount byte count of expected wav data to be read
     * @param numChannels
     * @param bits
     * @param sampleRate
     * @param byteOrder
     * @return a WAVData object containing the audio data
     *
     * @throws UnsupportedAudioFileException if the format of the audio if not
     *                                       supported.
     * @throws IOException If the file can no be found or some other IO error
     *                     occurs
     */
    public static WAVData loadFromStream(InputStream aIn, final int byteCount, final int numChannels, final int bits, final int sampleRate, final ByteOrder byteOrder, final boolean loop)
      throws IOException {
        if( !(aIn instanceof BufferedInputStream) ) {
            aIn = new BufferedInputStream(aIn);
        }
        // ReadableByteChannel aChannel = Channels.newChannel(aIn);
        int format = ALConstants.AL_FORMAT_MONO8;

        if ((bits == 8) && (numChannels == 1)) {
            format = ALConstants.AL_FORMAT_MONO8;
        } else if ((bits == 16) && (numChannels == 1)) {
            format = ALConstants.AL_FORMAT_MONO16;
        } else if ((bits == 8) && (numChannels == 2)) {
            format = ALConstants.AL_FORMAT_STEREO8;
        } else if ((bits == 16) && (numChannels == 2)) {
            format = ALConstants.AL_FORMAT_STEREO16;
        }
        final ByteBuffer buffer = IOUtil.copyStreamChunk2ByteBuffer(aIn, 0, byteCount);
        final int actualSize = buffer.limit();

        // Must byte swap in case endianess mismatch
        if ( bits == 16 && ByteOrder.nativeOrder() != byteOrder ) {
          final int len = buffer.remaining();
          for (int i = 0; i < len; i += 2) {
            final byte a = buffer.get(i);
            final byte b = buffer.get(i+1);
            buffer.put(i, b);
            buffer.put(i+1, a);
          }
        }

        final WAVData result = new WAVData(buffer, format, actualSize, sampleRate, loop);
        aIn.close();

        return result;
    }

}
