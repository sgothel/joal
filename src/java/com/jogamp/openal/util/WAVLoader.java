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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import com.jogamp.openal.UnsupportedAudioFileException;

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
    public static WAVData loadFromFile(String filename)
        throws UnsupportedAudioFileException, IOException {
        File soundFile = new File(filename);
        try {
            AudioInputStream aIn = AudioSystem.getAudioInputStream(soundFile);
            return loadFromStreamImpl(aIn);
        } catch (javax.sound.sampled.UnsupportedAudioFileException e) {
            throw new UnsupportedAudioFileException(e);
        }            
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
    public static WAVData loadFromStream(InputStream stream)
        throws UnsupportedAudioFileException, IOException {
        AudioInputStream aIn;
        try {
            aIn = AudioSystem.getAudioInputStream(stream);
            return loadFromStreamImpl(aIn);
        } catch (javax.sound.sampled.UnsupportedAudioFileException e) {
            throw new UnsupportedAudioFileException(e);
        }
    }


    private static WAVData loadFromStreamImpl(AudioInputStream aIn)
      throws UnsupportedAudioFileException, IOException {
        final AudioFormat fmt = aIn.getFormat();
        return WAVData.loadFromStream(aIn, -1, fmt.getChannels(), fmt.getSampleSizeInBits(), 
                                      Math.round(fmt.getSampleRate()), 
                                      fmt.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN, false);
    }
    
}
