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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import com.jogamp.openal.ALException;

/**
 * A Loader utility for (.wav) files. Creates a WAVData object containing the
 * data used by the AL.alBufferData method.
 *
 * @author Athomas Goldberg, et.al
 */
public class WAVLoader {
    // private static final int BUFFER_SIZE = 128000;

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
    public static WAVData loadFromFile(String filename) throws IOException {
        File soundFile = new File(filename);
        InputStream is = new FileInputStream(soundFile);
        return loadFromStreamImpl(is);
    }

    /**
     * This method loads a (.wav) file into a WAVData object.
     *
     * @param stream An InputStream for the .WAV stream.
     *
     * @return a WAVData object containing the audio data 
     *
     * @throws UnsupportedAudioFileException if the format of the audio if not
     *                                       supported. 
     * @throws IOException If the file can no be found or some other IO error 
     *                     occurs
     */
    public static WAVData loadFromStream(InputStream stream) throws IOException {
        return loadFromStreamImpl(stream);
    }
    
	private static long readUnsignedIntLittleEndian(DataInputStream is)	throws IOException {
		byte[] buf = new byte[4];
		is.readFully(buf);
		return (buf[0] & 0xFF | ((buf[1] & 0xFF) << 8) | ((buf[2] & 0xFF) << 16) | ((buf[3] & 0xFF) << 24));
	}
    
	private static short readUnsignedShortLittleEndian(DataInputStream is) throws IOException {
		byte[] buf = new byte[2];
		is.readFully(buf);
		return (short) (buf[0] & 0xFF | ((buf[1] & 0xFF) << 8));
	}

    private static WAVData loadFromStreamImpl(InputStream aIn) throws IOException {
    	/**
    	 * references:
    	 * http://www.sonicspot.com/guide/wavefiles.html
    	 * https://ccrma.stanford.edu/courses/422/projects/WaveFormat/
    	 * http://stackoverflow.com/questions/1111539/is-the-endianness-of-format-params-guaranteed-in-riff-wav-files
    	 * http://sharkysoft.com/archive/lava/docs/javadocs/lava/riff/wave/doc-files/riffwave-content.htm
    	 */
    	final DataInputStream din;
    	if (aIn instanceof DataInputStream) {
    		din = (DataInputStream) aIn;
    	} else {
    		din = new DataInputStream(aIn);
    	}
		try {
			if (din.readInt() != 0x52494646) {// "RIFF", little endian
				//FIXME "RIFX", big endian, read the data in big endian in this case
			    throw new ALException("Invalid WAV header");
			}
			// length of the RIFF, unused
			readUnsignedIntLittleEndian(din);
			if (din.readInt() != 0x57415645) {// "WAVE"
			    throw new ALException("Invalid WAV header");
			}
			boolean foundFmt = false;
		    boolean foundData = false;

		    short sChannels = 0, sSampleSizeInBits = 0;
		    long sampleRate = 0;
		    long chunkLength = 0;

			while (!foundData) {
				int chunkId = din.readInt();
				chunkLength = readUnsignedIntLittleEndian(din);
				switch (chunkId) {
				case 0x666D7420: // "fmt "
					foundFmt = true;
					// compression code, unused
					readUnsignedShortLittleEndian(din);
					sChannels = readUnsignedShortLittleEndian(din);
					sampleRate = readUnsignedIntLittleEndian(din);
					// bytes per second, unused
					readUnsignedIntLittleEndian(din);
					// block alignment, unused
					readUnsignedShortLittleEndian(din);
					sSampleSizeInBits = readUnsignedShortLittleEndian(din);
					din.skip(chunkLength - 16);
					break;
				case 0x66616374: // "fact"
					// FIXME: compression format dependent data?
					din.skip(chunkLength);
					break;
				case 0x64617461: // "data"
					if (!foundFmt)
						throw new ALException(
								"WAV fmt chunks must be before data chunks");
					foundData = true;
					break;
				default:
					// unrecognized chunk, skips it
					din.skip(chunkLength);
				}
			}

			final int channels = (int) sChannels;
			final int sampleSizeInBits = sSampleSizeInBits;
			final float fSampleRate = (float) sampleRate;
			//FIXME big endian not supported yet
			final boolean isBigEndian = false;
			return WAVData.loadFromStream(aIn, -1, channels, sampleSizeInBits,
					Math.round(fSampleRate), isBigEndian ? ByteOrder.BIG_ENDIAN
							: ByteOrder.LITTLE_ENDIAN, false);
		} finally {
			din.close();
		}
    }
    
}
