package com.jogamp.openal.test.junit;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.junit.Test;

import com.jogamp.openal.ALFactory;
import com.jogamp.openal.AL;
import com.jogamp.openal.ALC;
import com.jogamp.openal.ALExt;
import com.jogamp.openal.ALCcontext;
import com.jogamp.openal.ALCdevice;
import com.jogamp.openal.test.resources.ResourceLocation;
import com.jogamp.openal.util.WAVData;
import com.jogamp.openal.util.WAVLoader;


public class ALExtLoopbackDeviceSOFTTest {

    @Test
    public void testAlCLoopbackDeviceSOFT() throws UnsupportedAudioFileException, IOException {
    	
    	ALC alc = ALFactory.getALC();
        ALCdevice device = alc.alcOpenDevice(null);
        AL al = ALFactory.getAL();
        
		boolean have = alc.alcIsExtensionPresent(null, "ALC_SOFT_loopback");
			
        ALCdevice dev;
        Exception ex = null;
        ALExt alext = ALFactory.getALExt();
        
        if (!have) {
            System.out.println("No extension ALC_SOFT_loopback present");

            try {
                dev = alext.alcLoopbackOpenDeviceSOFT(null);
            } catch (Exception x) {
                ex = x;
            }
            assertNotNull(ex);

            return;
        }

        System.out.println("begin testAlCLoopbackDeviceSOFT");

        int[] buffers = new int[1];

        dev = alext.alcLoopbackOpenDeviceSOFT(null);
        assertNotNull(dev);

        WAVData wd = null;
        ALCcontext context = null;

        try {
            wd = WAVLoader.loadFromStream(ResourceLocation.getTestStream0());
            // for whatever reason the type codes differ for this stuff
            context = alc.alcCreateContext(dev, new int[]{
                    ALExt.ALC_FORMAT_CHANNELS_SOFT,
                    ALExt.AL_MONO_SOFT,
                    ALExt.ALC_FORMAT_TYPE_SOFT,
                    ALExt.AL_UNSIGNED_BYTE_SOFT,
                    ALC.ALC_FREQUENCY,
                    wd.freq,
                    0
                }, 0);
            
            boolean state = alc.alcMakeContextCurrent(context);
            assertTrue(state);
        } catch (Exception e) {
            ex = e;
        }
        assertNotNull(ex);
        ex = null;

        // queue the data
        al.alGenBuffers(1, buffers, 0);
        al.alBufferData(buffers[0], wd.format, wd.data, wd.size, wd.freq);

        ByteBuffer bb = ByteBuffer.allocateDirect(wd.size).order(wd.data.order());

        // render it to an output buffer
        try {
            alext.alcRenderSamplesSOFT(dev, wd.data, wd.size);
        } catch (Exception e) {
            ex = e;
        }
        assertNotNull(ex);

        // should the samples be bit identical?
        for (int i=0;i<wd.size;i++) {
            byte a = wd.data.get(i);
            byte b = bb.get(i);
            assertEquals(a, b);
        }

        System.out.println("end testAlCLoopbackDeviceSOFT");
    }

}
