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

import java.nio.ByteBuffer;


/**
 * This class is a holder for WAV (.wav )file Data returned from the WavLoader
 *
 * @author Athomas Goldberg
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

    WAVData(ByteBuffer data, int format, int size, int freq, boolean loop) {
        this.data = data;
        this.format = format;
        this.size = size;
        this.freq = freq;
        this.loop = loop;
    }
}
