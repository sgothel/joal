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

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import net.java.games.joal.AL;
import net.java.games.joal.ALC;
import net.java.games.joal.ALFactory;
import net.java.games.joal.util.WAVData;
import net.java.games.joal.util.WAVLoader;


/**
 * DOCUMENT ME!
 *
 * @author Athomas Goldberg
 */
public class AudioSystem3D {
    private static AL al;
    private static ALC alc;
    private static Listener listener;

    /**
     * DOCUMENT ME!
     */
    public static void init() {
        ALFactory.initialize();
        al = ALFactory.getAL();
        alc = ALFactory.getALC();
    }

    /**
     * DOCUMENT ME!
     *
     * @param device DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Context createContext(Device device) {
        Context result = null;
        ALC.Context realContext = alc.alcCreateContext(device.realDevice, null);
        result = new Context(alc, realContext, device);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     */
    public static void makeContextCurrent(Context context) {
        ALC.Context realContext = null;

        if (context != null) {
            realContext = context.realContext;
        }

        alc.alcMakeContextCurrent(realContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param deviceName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Device openDevice(String deviceName) {
        Device result = null;
        ALC.Device realDevice = alc.alcOpenDevice(deviceName);
        result = new Device(alc, realDevice);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param numBuffers DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Buffer[] generateBuffers(int numBuffers) {
        Buffer[] result = new Buffer[numBuffers];
        int[] arr = new int[numBuffers];
        al.alGenBuffers(numBuffers, arr);

        for (int i = 0; i < numBuffers; i++) {
            result[i] = new Buffer(al, arr[i]);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param filename DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws UnsupportedAudioFileException DOCUMENT ME!
     */
    public static Buffer loadBuffer(String filename)
        throws IOException, UnsupportedAudioFileException {
        Buffer result;
        Buffer[] tmp = generateBuffers(1);
        result = tmp[0];

        WAVData wd = WAVLoader.loadFromFile(filename);
        result.configure(wd.data, wd.format, wd.freq);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param filename DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws UnsupportedAudioFileException DOCUMENT ME!
     */
    public static Source loadSource(String filename)
        throws IOException, UnsupportedAudioFileException {
        Buffer buffer = loadBuffer(filename);
        return generateSource(buffer);
    }

    /**
     * DOCUMENT ME!
     *
     * @param numSources DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Source[] generateSources(int numSources) {
        Source[] result = new Source[numSources];
        int[] arr = new int[numSources];
        al.alGenSources(numSources, arr);

        for (int i = 0; i < numSources; i++) {
            result[i] = new Source(al, arr[i]);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param buff DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Source generateSource(Buffer buff) {
        Source result = null;
        Source[] tmp = generateSources(1);
        result = tmp[0];
        result.setBuffer(buff);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Listener getListener() {
        if (listener == null) {
            listener = new Listener(al);
        }

        return listener;
    }
}
