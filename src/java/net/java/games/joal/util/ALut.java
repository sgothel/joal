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
*
* Created on Jun 27, 2003
*/

package net.java.games.joal.util;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.sound.sampled.UnsupportedAudioFileException;

import net.java.games.joal.AL;
import net.java.games.joal.ALC;
import net.java.games.joal.ALFactory;

/**
 * @author Athomas Goldberg
 *
 */
public final class ALut {

    private static ALC alc;
    
    private ALut() { }

    public static void alutInit() {
        ALFactory.initialize();
        alc = ALFactory.getALC();
        String deviceName = null;
        String os = System.getProperty("os.name");
        if (os.startsWith("Windows")) {
            deviceName = "DirectSound3D";
        }
        if (deviceName != null) {
            ALC.Context context;
            ALC.Device device;

            device = alc.alcOpenDevice(deviceName);
            context = alc.alcCreateContext(device, null);
            alc.alcMakeContextCurrent(context);
        } else {
            System.out.println(
                "alutInit does not currently support "
                    + os
                    + ". "
                    + "You'll need to construct your device and context"
                    + "using the ALC functions for the time being. We apologize "
                    + "for the inconvenience.");
        }
    }

    public static void alutLoadWAVFile(
        String fileName,
        int[] format,
        ByteBuffer[] data,
        int[] size,
        int[] freq,
        int[] loop) {
        try {
            WAVData wd = WAVLoader.loadFromFile(fileName);
            format[0] = wd.format;
            data[0] = wd.data;
            size[0] = wd.size;
            freq[0] = wd.freq;
            loop[0] = wd.loop ? AL.AL_TRUE : AL.AL_FALSE;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public static void alutUnloadWAV(
        int format,
        ByteBuffer data,
        int size,
        int freq) {
        // unneeded. here for completeness.
    }

    public static void alutExit() {
        
        ALC.Context context = alc.alcGetCurrentContext();
        ALC.Device device = alc.alcGetContextsDevice(context);
        alc.alcFreeCurrentContext();
        alc.alcDestroyContext(context);
        alc.alcCloseDevice(device);
    }
}
