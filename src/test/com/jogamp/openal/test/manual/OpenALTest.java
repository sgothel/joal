package com.jogamp.openal.test.manual;

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
import java.io.IOException;
import java.nio.IntBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.openal.AL;
import com.jogamp.openal.ALC;
import com.jogamp.openal.ALCcontext;
import com.jogamp.openal.ALCdevice;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.UnsupportedAudioFileException;
import com.jogamp.openal.eax.EAX;
import com.jogamp.openal.eax.EAXFactory;
import com.jogamp.openal.test.resources.ResourceLocation;
import com.jogamp.openal.util.WAVData;

/**
 * @author Athomas Goldberg, Michael Bien, et.al.
 */
public class OpenALTest {
    private ALC alc = null;
    private ALCdevice device = null;
    private ALCcontext context = null;
    private AL al = null;
    private int[] sources = null;
    private boolean initialized = false;
    
    public OpenALTest() {        
    }
    
    public void init() throws UnsupportedAudioFileException, IOException {
        if( initialized ) {
            return;
        }
        alc = ALFactory.getALC();
        device = alc.alcOpenDevice(null);
        context = alc.alcCreateContext(device, null);
        alc.alcMakeContextCurrent(context);
        al = ALFactory.getAL();
        
        System.out.println("output devices:");
        {
            final String[] outDevices = alc.alcGetDeviceSpecifiers();
            if( null != outDevices ) {
                for (String name : outDevices) {
                    System.out.println("    "+name);
                }
            }
        }
        System.out.println("capture devices:");
        {
            final String[] inDevices = alc.alcGetCaptureDeviceSpecifiers();
            if( null != inDevices ) {
                for (String name : inDevices) {
                    System.out.println("    "+name);
                }
            }
        }

        boolean eaxPresent = al.alIsExtensionPresent("EAX2.0");
        EAX eax = ( eaxPresent ) ? EAXFactory.getEAX() : null;
        System.err.println("EAX present:" + eaxPresent + ", EAX retrieved: "+ (null != eax));

        int[] buffers = new int[1];
        al.alGenBuffers(1, buffers, 0);

        WAVData wd = WAVData.loadFromStream(ResourceLocation.getTestStream0(), 1, 8, 22050);
        al.alBufferData(buffers[0], wd.format, wd.data, wd.size, wd.freq);
        
        sources = new int[1];
        al.alGenSources(1, sources, 0);
        al.alSourcei(sources[0], AL.AL_BUFFER, buffers[0]);

        int[] loopArray = new int[1];
        al.alGetSourcei(sources[0], AL.AL_LOOPING, loopArray, 0);
        System.err.println("Looping 1: " + (loopArray[0] == AL.AL_TRUE));

        int[] loopBuffer = new int[1];
        al.alGetSourcei(sources[0], AL.AL_LOOPING, loopBuffer, 0);
        System.err.println("Looping 2: " + (loopBuffer[0] == AL.AL_TRUE));

        if (eaxPresent && null!=eax) {
            IntBuffer env = Buffers.newDirectIntBuffer(1);
            env.put(EAX.EAX_ENVIRONMENT_BATHROOM);
            eax.setListenerProperty(EAX.DSPROPERTY_EAXLISTENER_ENVIRONMENT, env);
        }
        initialized = true;
    }
    
    public void play() {
        if( !initialized ) {
            return;
        }
        System.out.println("play direct");
        al.alSourcePlay(sources[0]);
    }
    
    public void play3f(float x, float y, float z) {
        if( !initialized ) {
            return;
        }
        System.out.println("play3f "+x+", "+y+", "+z);
        al.alSource3f(sources[0], AL.AL_POSITION, x, y, z);
    }
    
    public void pause() {
        if( !initialized ) {
            return;
        }
        al.alSourcePause(sources[0]);
    }
    
    public void dispose() {
        if( !initialized ) {
            return;
        }
        if( null != sources ) {
            al.alSourceStop(sources[0]);
            al.alDeleteSources(1, sources, 0);
            sources = null;
        }
        if( null != context ) {
            alc.alcDestroyContext(context);
            context = null;
        }
        if( null != device ) {
            alc.alcCloseDevice(device);
            device = null;
        }
        initialized = false;
    }
    
    public static void main(String[] args) throws InterruptedException, UnsupportedAudioFileException, IOException {
        OpenALTest demo = new OpenALTest();
        demo.init();
      
        demo.play();
        Thread.sleep(5000);

        demo.play3f(2f, 2f, 2f);
        Thread.sleep(5000);

        demo.play3f(3f, 3f, 3f);
        Thread.sleep(5000);

        demo.play3f(0f, 0f, 0f);
        Thread.sleep(10000);
        
        demo.dispose();
    }
}
