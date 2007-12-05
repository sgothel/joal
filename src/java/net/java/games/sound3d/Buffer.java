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

import net.java.games.joal.AL;

import java.nio.ByteBuffer;


/**
 * The Sound3D Buffer is a container for audio data used in the Sound3D
 * environment.
 *
 * @author Athomas Goldberg
 */
public class Buffer {
     public final static int FORMAT_MONO8 = AL.AL_FORMAT_MONO8;

    public final static int FORMAT_MONO16 = AL.AL_FORMAT_MONO16;

    public final static int FORMAT_STEREO8 = AL.AL_FORMAT_STEREO8;

    public final static int FORMAT_STEREO16 = AL.AL_FORMAT_STEREO16;
    final int bufferID;
    private ByteBuffer data;
    private boolean isConfigured = false;
    private final AL al;

    Buffer(AL al, int bufferID) {
        this.bufferID = bufferID;
        this.al = al;
    }

    /**
     * Configure the Sound3D buffer
     *
     * @param data the raw audio data
     * @param format the format of the data: <code>FORMAT_MONO8, FORMAT_MONO16,
     *        FORMAT_STEREO8</code> and <code>FORMAT_STEREO16</code>
     * @param freq the frequency of the data
     */
    public void configure(ByteBuffer data, int format, int freq) {
        if (!isConfigured) {
            this.data = data;
            al.alBufferData(bufferID, format, data, data.capacity(), freq);
        }
    }

    /**
     * Delete this buffer, and free its resources.
     */
    public void delete() {
        data = null;
        al.alDeleteBuffers(1, new int[] { bufferID }, 0);
    }

    /**
     * Get the bit-depth of the data, (8 or 16)
     *
     * @return the bit-depth of the data
     */
    public int getBitDepth() {
        int[] i = new int[1];
        al.alGetBufferi(bufferID, AL.AL_BITS, i, 0);

        return i[0];
    }

    /**
     * Get the number of channels of the data (1-Mono, 2-Stereo)
     *
     * @return the number of audio channels.
     */
    public int getNumChannels() {
        int[] i = new int[1];
        al.alGetBufferi(bufferID, AL.AL_CHANNELS, i, 0);

        return i[0];
    }

    /**
     * Gets the raw data contained in this buffer.
     *
     * @return the raw buffer data.
     */
    public ByteBuffer getData() {
        return data;
    }

    /**
     * Gets the audio frequency of the data contained in this buffer.
     *
     * @return the frequency of the data
     */
    public int getFrequency() {
        int[] i = new int[1];
        al.alGetBufferi(bufferID, AL.AL_FREQUENCY, i, 0);

        return i[0];
    }

    /**
     * Gets the size (in bytes) of the raw data containe in this buffer.
     *
     * @return the size of the data.
     */
    public int getSize() {
        int[] i = new int[1];
        al.alGetBufferi(bufferID, AL.AL_SIZE, i, 0);

        return i[0];
    }
}
