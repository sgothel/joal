package com.jogamp.openal.test.junit;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.jogamp.openal.ALFactory;
import com.jogamp.openal.AL;
import com.jogamp.openal.ALC;
import com.jogamp.openal.ALExt;
import com.jogamp.openal.ALCcontext;
import com.jogamp.openal.ALCdevice;
import com.jogamp.openal.UnsupportedAudioFileException;
import com.jogamp.openal.test.resources.ResourceLocation;
import com.jogamp.openal.test.util.UITestCase;
import com.jogamp.openal.util.WAVData;
import com.jogamp.openal.util.WAVLoader;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ALExtLoopbackDeviceSOFTTest extends UITestCase {
    static boolean dumpSamples = false;

    @Test
    public void test01Mono() throws UnsupportedAudioFileException, IOException {
        testAlCLoopbackDeviceSOFTImpl(0, 0.4f);
    }
    @Test
    public void test02Stereo() throws UnsupportedAudioFileException, IOException {
        testAlCLoopbackDeviceSOFTImpl(3, 0.8f);
    }

    void testAlCLoopbackDeviceSOFTImpl(int srcIdx, float expAccuracy) throws UnsupportedAudioFileException, IOException {
    	final ALC alc = ALFactory.getALC();
        final AL al = ALFactory.getAL();
        final ALExt alext = ALFactory.getALExt();

        System.out.println("Available null device OpenAL Extensions:"+alc.alcGetString(null, ALC.ALC_EXTENSIONS));
        if ( !alc.alcIsExtensionPresent(null, "ALC_SOFT_loopback") ) {
            System.out.println("No extension ALC_SOFT_loopback present");
            return;
        }
        final InputStream inputStream;
        final int fmtChannels, fmtData, sampleSize, channelCount;
        final boolean useShort;
        if( 0 == srcIdx ) {
            inputStream = ResourceLocation.getTestStream0();
            fmtChannels = ALExt.AL_MONO_SOFT;
            fmtData = ALExt.AL_BYTE_SOFT;
            sampleSize = 8;
            channelCount = 1;
            useShort = false;
        } else if( 3 == srcIdx ) {
            inputStream = ResourceLocation.getTestStream3();
            fmtChannels = ALExt.AL_STEREO_SOFT;
            fmtData = ALExt.AL_SHORT_SOFT;
            sampleSize = 16;
            channelCount = 2;
            useShort = true;
        } else {
            return;
        }

        ALCdevice dev = null;
        Exception ex = null;
        try {
            System.out.println("begin testAlCLoopbackDeviceSOFT");

            dev = alext.alcLoopbackOpenDeviceSOFT(null);
            {
                final int alcDevError = alc.alcGetError(dev);
                System.err.printf("CreatedSoftDevice: alcError 0x%X, dev %s%n", alcDevError, dev);
            }
            assertNotNull(dev);

            WAVData wd = null;

            ALCcontext context = null;
            try {
                // int alChannelLayout = ALHelpers.getDefaultALChannelLayout(1);
                // int alSampleType = ALHelpers.getALSampleType(8, true, true);
                // alFormat = ALHelpers.getALFormat(alChannelLayout, alSampleType, hasSOFTBufferSamples, al, alExt);

                wd = WAVLoader.loadFromStream(inputStream);
                System.err.println("fmt "+wd.format);
                System.err.println("freq "+wd.freq);
                System.err.println("size "+wd.size);

                final boolean supported = alext.alcIsRenderFormatSupportedSOFT(dev, wd.freq, fmtChannels, fmtData);
                final String msg = String.format("Supported channels 0x%X, data 0x%X: %b", fmtChannels, fmtData, supported);
                System.err.println(msg);
                Assert.assertTrue("Not supported: "+msg, supported);

                context = alc.alcCreateContext(dev, new int[]{
                        ALExt.ALC_FORMAT_CHANNELS_SOFT,
                        fmtChannels,
                        ALExt.ALC_FORMAT_TYPE_SOFT,
                        fmtData,
                        ALC.ALC_FREQUENCY,
                        wd.freq,
                        0
                    }, 0);

                final int alcDevError = alc.alcGetError(dev);
                System.err.printf("CreatedSoftContext: alcError 0x%X, context %s%n", alcDevError, context);
            } catch (Exception e) {
                ex = e;
            }
            assertNull(ex);
            assertNotNull("ALC context null", context);

            assertTrue("Could not make context current", alc.alcMakeContextCurrent(context));
            try {
                final int dataSize = Math.min(4096, wd.size);

                int[] source = { 0 };
                al.alGenSources(1, source, 0);
                assertEquals("Could not gen source", AL.AL_NO_ERROR, al.alGetError());

                final int[] buffer = new int[1];
                final int[] tmp = new int[1];

                al.alGenBuffers(1, buffer, 0);
                assertEquals("Could not generate AL buffer", AL.AL_NO_ERROR, al.alGetError());

                al.alBufferData(buffer[0], wd.format, wd.data, dataSize, wd.freq);
                assertEquals("Could not fill AL source buffer "+buffer[0]+", sz "+dataSize+", "+wd.data, AL.AL_NO_ERROR, al.alGetError());
                al.alGetBufferi(buffer[0], AL.AL_SIZE, tmp, 0);
                assertEquals("Could not get buffer size "+buffer[0], AL.AL_NO_ERROR, al.alGetError());
                System.err.println("Buffer size "+tmp[0]+" of "+dataSize);
                al.alSourcei(source[0], AL.AL_BUFFER, buffer[0]);
                assertEquals("Could source buffer "+buffer[0], AL.AL_NO_ERROR, al.alGetError());

                al.alSourcePlay(source[0]);
                assertEquals("Could not play source "+source[0], AL.AL_NO_ERROR, al.alGetError());

                final ByteBuffer bbSink = ByteBuffer.allocateDirect(dataSize).order(wd.data.order());

                final ShortBuffer sbSink = bbSink.asShortBuffer();
                final ShortBuffer sbSrc = wd.data.asShortBuffer();

                // render it to an output buffer
                try {
                    final int samplesPerChannel = dataSize / ( ( sampleSize / 8 ) * channelCount );
                    alext.alcRenderSamplesSOFT(dev, bbSink, samplesPerChannel);
                } catch (Exception e) {
                    ex = e;
                }
                assertNull(ex);

                final int alError = al.alGetError();
                final int alcDevError = alc.alcGetError(dev);
                System.err.printf("alcRendering: alError 0x%X, alcError 0x%X%n", alError, alcDevError);

                final int srcLimit;
                if( useShort ) {
                    srcLimit = sbSink.limit();
                } else {
                    srcLimit = bbSink.limit();
                }

                float deltaT = 0f;
                int totalSize = 0;

                for (int i=0; i<srcLimit; i++) {
                    final int a, b, a_u, b_u;
                    final float delta;
                    if( dumpSamples ) {
                        if(0==totalSize%3) {
                            System.err.printf("%n[%04d]: ", i);
                        }
                    }
                    if( useShort ) {
                        a = sbSrc.get(i);
                        b = sbSink.get(i);
                        a_u = -Short.MIN_VALUE + a;
                        b_u = -Short.MIN_VALUE + b;
                        delta = Math.abs(Math.abs(a_u - b_u) / (float)(0xffff+1));
                        if( dumpSamples ) {
                            System.err.printf("%6d %04X-> %6d %04X (%1.5f), ", a, a_u, b, b_u, delta);
                        }
                    } else {
                        // compare int8_t values only
                        a = wd.data.get(i);
                        b = bbSink.get(i);
                        a_u = -Byte.MIN_VALUE + a;
                        b_u = -Byte.MIN_VALUE + b;
                        delta = Math.abs(Math.abs(a_u - b_u) / (float)(0xff+1));
                        if( dumpSamples ) {
                            System.err.printf("%4d %02X-> %4d %02X (%1.5f), ", a, a_u, b, b_u, delta);
                        }
                    }
                    deltaT += delta;
                    totalSize++;
                }
                if( dumpSamples ) {
                    System.err.println();
                }

                final float deltaA = deltaT / totalSize;
                final float accuracy = Math.abs(1f - deltaA);
                final String msg = String.format("Size[%04d s, %04d b, %04d b-t], Delta T %f, A %f, Accuracy %f / %f", totalSize, dataSize, wd.size,
                        deltaT, deltaA, accuracy, expAccuracy);
                System.err.println(msg);
                Assert.assertTrue("Too many rendering artifacts: "+msg, expAccuracy <= accuracy);
            } finally {
                alc.alcMakeContextCurrent(null);
            }
        } finally {
            if( null != dev ) {
                alc.alcCloseDevice(dev);
            }
        }

        System.out.println("end testAlCLoopbackDeviceSOFT");
    }

    public static void main(String args[]) throws IOException {
        dumpSamples = true;
        org.junit.runner.JUnitCore.main(ALExtLoopbackDeviceSOFTTest.class.getName());
    }


}
