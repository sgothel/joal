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

import com.jogamp.openal.AL;
import com.jogamp.openal.ALConstants;
import java.nio.ByteBuffer;


/**
 * The Sound3D Buffer is a container for audio data used in the Sound3D
 * environment.
 *
 * @author Athomas Goldberg, Sven Gothel, et al.
 */
public final class Buffer {
    public final static int FORMAT_MONO8 = AL.AL_FORMAT_MONO8;

    public final static int FORMAT_MONO16 = AL.AL_FORMAT_MONO16;

    public final static int FORMAT_STEREO8 = AL.AL_FORMAT_STEREO8;

    public final static int FORMAT_STEREO16 = AL.AL_FORMAT_STEREO16;

    private int alBufferID;
    private ByteBuffer data;

    public Buffer(final int bufferID) {
        this.alBufferID = bufferID;
    }

    /** Return the OpenAL buffer ID, -1 if invalid. */
    public int getID() { return alBufferID; }

    /** Returns whether {@link #getID()} is valid, i.e. not {@link #delete()}'ed */
    public boolean isValid() {
        return 0 <= alBufferID && AudioSystem3D.al.alIsBuffer(alBufferID);
    }

    /**
     * Delete this buffer, and free its resources.
     */
    public void delete() {
        data = null;
        if( 0 <= alBufferID ) {
            AudioSystem3D.al.alDeleteBuffers(1, new int[] { alBufferID }, 0);
            alBufferID = -1;
        }
    }

    /**
     * Configure the Sound3D buffer
     *
     * @param data the raw audio data
     * @param alFormat the OpenAL format of the data, e.g. <code>FORMAT_MONO8, FORMAT_MONO16,
     *        FORMAT_STEREO8</code> and <code>FORMAT_STEREO16</code>
     * @param freq the frequency of the data
     */
    public void configure(final ByteBuffer data, final int alFormat, final int freq) {
        this.data = data;
        AudioSystem3D.al.alBufferData(alBufferID, alFormat, data, data.capacity(), freq);
    }

    /**
     * Get the bit-depth of the data, (8 or 16)
     *
     * @return the bit-depth of the data
     */
    public int getBitDepth() {
        final int[] i = new int[1];
        AudioSystem3D.al.alGetBufferi(alBufferID, ALConstants.AL_BITS, i, 0);

        return i[0];
    }

    /**
     * Get the number of channels of the data (1-Mono, 2-Stereo)
     *
     * @return the number of audio channels.
     */
    public int getNumChannels() {
        final int[] i = new int[1];
        AudioSystem3D.al.alGetBufferi(alBufferID, ALConstants.AL_CHANNELS, i, 0);

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
        final int[] i = new int[1];
        AudioSystem3D.al.alGetBufferi(alBufferID, ALConstants.AL_FREQUENCY, i, 0);

        return i[0];
    }

    /**
     * Gets the size (in bytes) of the raw data containe in this buffer.
     *
     * @return the size of the data.
     */
    public int getSize() {
        final int[] i = new int[1];
        AudioSystem3D.al.alGetBufferi(alBufferID, ALConstants.AL_SIZE, i, 0);

        return i[0];
    }

    @Override
    public String toString() {
        return "ALBuffer[id "+alBufferID+"]";
    }
}
