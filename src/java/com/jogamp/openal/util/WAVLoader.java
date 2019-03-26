/**
* Copyright (c) 2013 JogAmp Community. All rights reserved.
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
* Note: Rewrite started by Julien Goussej 2013-03-27 commit 6292ed9712b17f849f22221aea9d586c3a363c09
*/

package com.jogamp.openal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import com.jogamp.common.util.Bitstream;
import com.jogamp.openal.ALException;

/**
 * A Loader utility for (.wav) files. Creates a WAVData object containing the
 * data used by the AL.alBufferData method.
 */
public class WAVLoader {

    /**
     * This method loads a (.wav) file into a WAVData object.
     *
     * @param filename The name of the (.wav) file
     *
     * @return a WAVData object containing the audio data
     *
     * @throws ALException if the format of the audio if not supported.
     * @throws IOException If the file can no be found or some other IO error
     *                     occurs
     */
    public static WAVData loadFromFile(final String filename) throws ALException, IOException {
        final File soundFile = new File(filename);
        final InputStream is = new FileInputStream(soundFile);
        return loadFromStreamImpl(is);
    }

    /**
     * This method loads a (.wav) file into a WAVData object.
     *
     * @param stream An InputStream for the .WAV stream.
     *
     * @return a WAVData object containing the audio data
     *
     * @throws ALException if the format of the audio if not supported.
     * @throws IOException If the file can no be found or some other IO error
     *                     occurs
     */
    public static WAVData loadFromStream(final InputStream stream) throws ALException, IOException {
        return loadFromStreamImpl(stream);
    }

	private static final int RIFF = 0x52494646;
    private static final int RIFX = 0x52494658;
    private static final int WAVE = 0x57415645;
    private static final int FACT = 0x66616374;
    private static final int FMT  = 0x666D7420;
    private static final int DATA = 0x64617461;

    private static WAVData loadFromStreamImpl(final InputStream aIn) throws ALException, IOException {
    	/**
    	 * references:
    	 * http://www.sonicspot.com/guide/wavefiles.html
    	 * https://ccrma.stanford.edu/courses/422/projects/WaveFormat/
    	 * http://stackoverflow.com/questions/1111539/is-the-endianness-of-format-params-guaranteed-in-riff-wav-files
    	 * http://sharkysoft.com/archive/lava/docs/javadocs/lava/riff/wave/doc-files/riffwave-content.htm
    	 */
        final Bitstream.ByteInputStream bis = new Bitstream.ByteInputStream(aIn);
        final Bitstream<InputStream> bs = new Bitstream<InputStream>(bis, false);
        bs.setThrowIOExceptionOnEOF(true);
		try {
		    final boolean bigEndian; // FIXME: for all data incl. signatures ?

		    final long riffMarker = bs.readUInt32(true /* bigEndian */);
		    if ( RIFF == riffMarker ) {
			    bigEndian = false;
			} else if( RIFX == riffMarker ) {
                bigEndian = true;
			} else {
			    throw new ALException("Invalid RIF header: 0x"+Integer.toHexString((int)riffMarker)+", "+bs);
			}
            final long riffLenL = bs.readUInt32(bigEndian);
			final int riffLenI = Bitstream.uint32LongToInt(riffLenL);
            final long wavMarker = bs.readUInt32(true /* bigEndian */);
			if ( WAVE != wavMarker ) {
                throw new ALException("Invalid WAV header: 0x"+Integer.toHexString((int)wavMarker)+", "+bs);
			}
			boolean foundFmt = false;
		    boolean foundData = false;

		    short sChannels = 0, sSampleSizeInBits = 0;
		    long sampleRate = 0;
		    long chunkLength = 0;
            int dataLength = 0;

			while (!foundData) {
				final int chunkId = (int)bs.readUInt32(true /* bigEndian */);
				chunkLength = bs.readUInt32(bigEndian);
				switch (chunkId) {
				case FMT:
					foundFmt = true;
					@SuppressWarnings("unused")
                    final int compressionCode = bs.readUInt16(bigEndian);
					sChannels = (short)bs.readUInt16(bigEndian);
					sampleRate = bs.readUInt32(bigEndian);
					@SuppressWarnings("unused")
                    final long bytesPerSeconds = bs.readUInt32(bigEndian);
					@SuppressWarnings("unused")
                    final short blockAlignment = (short) bs.readUInt16(bigEndian);
					sSampleSizeInBits = (short) bs.readUInt16(bigEndian);
					bs.skip( 8 * ( chunkLength - 16 ) );
					break;
				case FACT:
					// FIXME: compression format dependent data?
                    bs.skip( 8 * chunkLength );
					break;
				case DATA:
					if (!foundFmt) {
						throw new ALException("WAV fmt chunks must be before data chunks: "+bs);
					}
					foundData = true;
                    dataLength = Bitstream.uint32LongToInt(chunkLength);
					break;
				default:
					// unrecognized chunk, skips it
                    bs.skip( 8 * chunkLength );
				}
			}

			final int channels = sChannels;
			final int sampleSizeInBits = sSampleSizeInBits;
			final float fSampleRate = sampleRate;
			return WAVData.loadFromStream(bs.getSubStream(), dataLength, channels, sampleSizeInBits,
					Math.round(fSampleRate), bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN, false, dataLength);
		} finally {
			bs.close();
		}
    }

}
