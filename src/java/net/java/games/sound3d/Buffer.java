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

package net.java.games.sound3d;

import java.nio.ByteBuffer;

import net.java.games.joal.AL;


/**
 * DOCUMENT ME!
 *
 * @author Athomas Goldberg
 */
public class Buffer {
    //DOCUMENT ME!
    public final static int FORMAT_MONO8 = AL.AL_FORMAT_MONO8;

    //DOCUMENT ME!
    public final static int FORMAT_MONO16 = AL.AL_FORMAT_MONO16;

    //DOCUMENT ME!
    public final static int FORMAT_STEREO8 = AL.AL_FORMAT_STEREO8;

    //DOCUMENT ME!
    public final static int FORMAT_STEREO16 = AL.AL_FORMAT_STEREO16;
    final int bufferName;
    private ByteBuffer data;
    private boolean isConfigured = false;
    private final AL al;

    Buffer(AL al, int bufferName) {
        this.bufferName = bufferName;
        this.al = al;
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     * @param format DOCUMENT ME!
     * @param freq DOCUMENT ME!
     */
    public void configure(ByteBuffer data, int format, int freq) {
        if (!isConfigured) {
            al.alBufferData(bufferName, format, data, data.capacity(), freq);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void delete() {
        data = null;
        al.alDeleteBuffers(1, new int[] { bufferName });
    }

    /**
     * DOCUMENT ME!
     *
     */
    public int getBitDepth() {
        int[] i = new int[1];
        al.alGetBufferi(bufferName, AL.AL_BITS, i);

        return i[0];
    }

    /**
     * DOCUMENT ME!
     *
     */
    public int getNumChannels() {
        int[] i = new int[1];
        al.alGetBufferi(bufferName, AL.AL_CHANNELS, i);

        return i[0];
    }

    /**
     * DOCUMENT ME!
     *
     */
    public ByteBuffer getData() {
        return data;
    }

    /**
     * DOCUMENT ME!
     *
     */
    public int getFrequency() {
        int[] i = new int[1];
        al.alGetBufferi(bufferName, AL.AL_FREQUENCY, i);

        return i[0];
    }

    /**
     * DOCUMENT ME!
     *
     */
    public int getSize() {
        int[] i = new int[1];
        al.alGetBufferi(bufferName, AL.AL_SIZE, i);

        return i[0];
    }
}
