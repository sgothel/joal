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

package net.java.games.joal.util;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.java.games.joal.ALConstants;


/**
 * @author Athomas Goldberg
 *
 */
public class WAVLoader implements ALConstants {
    private static final int BUFFER_SIZE = 128000;

    public static WAVData loadFromFile(String filename)
        throws UnsupportedAudioFileException, 
            IOException {
        WAVData result = null;
        File soundFile = new File(filename);
        AudioInputStream aIn = AudioSystem.getAudioInputStream(soundFile);
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
        aChannel.read(buffer);
        result = new WAVData(buffer, format, size, freq, false);
        aIn.close();
        return result;
    }
}
