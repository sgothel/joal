/*
 * Created on Jun 3, 2003
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package net.java.games.joal;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.sound.sampled.UnsupportedAudioFileException;

import junit.framework.TestCase;
import net.java.games.joal.util.WAVData;
import net.java.games.joal.util.WAVLoader;

/**
 * @author Athomas Goldberg
 *
 */
public class ALTest extends TestCase {


    static AL al;
    static ALC alc;
    static ALC.Context context;
    static ALC.Device device;
    final static String TEST_FILE ="lewiscarroll.wav";

    /**
     * Constructor for ALTest.
     * @param arg0
     */
    public ALTest(String arg0) {
        super(arg0);
    }

    public void setUp() {
    	try {
			ALFactory.initialize();
			al = ALFactory.getAL();
			alc = ALFactory.getALC();
			device = alc.alcOpenDevice(null);
			context = alc.alcCreateContext(device, null);
			alc.alcMakeContextCurrent(context);
        } catch (OpenALException e) {
            e.printStackTrace();
        }
    }

    public void tearDown() {
        alc.alcMakeContextCurrent(null);
        alc.alcDestroyContext(context);
        alc.alcCloseDevice(device);
    }

    public static void main(String[] args) {
        System.out.println("begin main");
        junit.textui.TestRunner.run(ALTest.class);
        System.out.println("end main");
    }

    /*
     * Test for void alGenBuffers(int, IntBuffer)
     *//*
    final public void testAlGenBuffersintIntBuffer() {
        System.out.println("begin testAlGenBuffersintintBuffer");
        // try basic case
        try {
            IntBuffer buffers = BufferUtils.newIntBuffer(7);
            al.alGenBuffers(7,buffers);
            for(int i = 0; i < 7; i++) {
                assertFalse(buffers.get(i) == 0);
                assertTrue(al.alIsBuffer(buffers.get(i)));
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
        
        Exception ex = null;
        // buffers == null
        try {
            IntBuffer buffers = null;
            al.alGenBuffers(7,buffers);
            
            
        } catch(IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull(ex);        

        ex = null;
        // buffer too small
        try {
            IntBuffer buffers = BufferUtils.newIntBuffer(5);
            al.alGenBuffers(7,buffers);
        } catch(IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull(ex);

        ex = null;
        // buffer not direct
        try {
            IntBuffer buffers = IntBuffer.allocate(7);
            al.alGenBuffers(7,buffers);
        } catch(IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull(ex);
        System.out.println("end testAlGenBuffersintintBuffer");
    }
*/
    /*
     * Test for void alGenBuffers(int, int[])
     */
    final public void testAlGenBuffersintintArray() {
        
        System.out.println("begin testAlGenBuffersintintArray");
        // try basic case
        try {
            int[] buffers = new int[7];
            al.alGenBuffers(7,buffers);
            for(int i = 0; i < 7; i++) {
                assertFalse(buffers[i] == 0);
                assertTrue(al.alIsBuffer(buffers[i]));
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
        
        Exception ex = null;
        // try exceptions
        try {
            int[] buffers = null;
            al.alGenBuffers(7,buffers);
            
            
        } catch(IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull(ex);        
        ex = null;
        try {
            int[] buffers = new int[5];
            al.alGenBuffers(7,buffers);
        } catch(IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull(ex);
        
        System.out.println("end testAlGenBuffersintintArray");
        
    }

    /*
     * Test for void alDeleteBuffers(int, IntBuffer)
     */
    final public void testAlDeleteBuffersintIntBuffer() {
        System.out.println("begin testAlDeleteBuffersintintArray");
        // try basic case
        try {
            int[] buffers = new int[7];
            al.alGenBuffers(7,buffers);
            for(int i = 0; i < 7; i++) {
                assertFalse(buffers[i] == 0);
                assertTrue(al.alIsBuffer(buffers[i]));
            }
            al.alDeleteBuffers(7,buffers);
            for(int i = 0; i < 7; i++) {
                assertFalse(al.alIsBuffer(buffers[i]));
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
        
        Exception ex = null;
        // try exceptions
        try {
            al.alDeleteBuffers(7,(int[])null);
        } catch(IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull(ex);
        ex = null;
        try {
            int[] buffers = new int[5];
            al.alGenBuffers(5,buffers);
            al.alDeleteBuffers(7,buffers);
        } catch(IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull(ex);
        
        try {
            int[] buffers = new int[7];
            al.alDeleteBuffers(7,buffers);
            assertTrue(al.alGetError() != 0);
        } catch(Exception e) {
            fail("deleting an unfilled buffer list should generate an ALError but not an exception");
        }
        
        System.out.println("end testAlDeleteBuffersintintArray");
    }

    /*
     * Test for void alDeleteBuffers(int, int[])
     */
    final public void testAlDeleteBuffersintintArray() {
        System.out.println("begin testAlDeleteBuffersintIntBuffer");
        // try basic case
        try {
            int[] buffers = new int[7];
            al.alGenBuffers(7,buffers);
            for(int i = 0; i < 7; i++) {
                assertFalse(buffers[i] == 0);
                assertTrue(al.alIsBuffer(buffers[i]));
            }
            al.alDeleteBuffers(7,buffers);
            for(int i = 0; i < 7; i++) {
                assertFalse(al.alIsBuffer(buffers[i]));
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
        
        Exception ex = null;
        // try exceptions
        try {
            al.alDeleteBuffers(7,(int[])null);
        } catch(IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull(ex);
        ex = null;
        try {
            int[] buffers = new int[5];
            al.alGenBuffers(5,buffers);
            al.alDeleteBuffers(7,buffers);
        } catch(IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull(ex);
        
        ex = null;
        try {
            int[] buffers = new int[5];
            al.alDeleteBuffers(7,buffers);
        } catch(IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull(ex);
        
        try {
            int[] buffers = new int[7];
            al.alDeleteBuffers(7,buffers);
            assertTrue(al.alGetError() != 0);
        } catch(Exception e) {
            fail("deleting an unfilled buffer list should generate an ALError but not an exception");
        }
        
        System.out.println("end testAlDeleteBuffersintintArray");
    }

    final public void testAlIsBuffer() {
        System.out.println("begin testALIsBuffer");
        try {
            // check a bufferlist with known bad values
            int[] buffers = new int[7];
            for(int i = 0; i < 7; i++) {
                buffers[i] = -1;
                assertFalse(al.alIsBuffer(buffers[i]));
            }
            // created 
            al.alGenBuffers(7,buffers);
            for(int i = 0; i < 7; i++) {
                assertTrue(al.alIsBuffer(buffers[i]));
            }
            // deleted
            al.alDeleteBuffers(7,buffers);
            for(int i = 0; i < 7; i++) {
                assertFalse(al.alIsBuffer(buffers[i]));
            }            
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        System.out.println("end testALisBuffer");
    }

    /*
     * Test for void alBufferData(int, int, byte[], int, int)
     */
    final public void testAlBufferDataintintbyteArrayintint() {
        System.out.println("begin testAlBufferDataintintbyteArrayintint");
        try {
            int[] buffers = new int[1];
            al.alGenBuffers(1, buffers);
            WAVData wd = WAVLoader.loadFromFile(TEST_FILE);
            int capacity = wd.data.capacity();
            int remaining = wd.data.remaining();
            
            byte[] data = new byte[wd.data.capacity()];
            for(int i = 0; i < data.length; i++) {
                data[i] = wd.data.get(i);
            }
            
            al.alBufferData(
                buffers[0],
                wd.format,
                data,
                wd.size,
                wd.freq
            );
            
            assertFalse(al.alGetBufferi(buffers[0],AL.AL_SIZE) == 0);
        } catch(Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
        Exception ex = null;
        try {
            int[] buffers = new int[1];
            al.alGenBuffers(1, buffers);

            al.alBufferData(
                buffers[0],
                AL.AL_FORMAT_STEREO16,
                (byte[])null,
                0,
                0
            );
        } catch(IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull(ex);
        
        System.out.println("end testAlBufferDataintintbyteArrayintint");
    }

    /*
     * Test for void alBufferData(int, int, ByteBuffer, int, int)
     */
    final public void testAlBufferDataintintByteBufferintint() {
        System.out.println("begin testAlBufferDataintintByteBufferintint");
        try {
            int[] buffers = new int[1];
            al.alGenBuffers(1, buffers);
            WAVData wd =
                WAVLoader.loadFromFile(TEST_FILE);
            al.alBufferData(
                buffers[0],
                wd.format,
                wd.data,
                wd.size,
                wd.freq
            );
            assertFalse(al.alGetBufferi(buffers[0],AL.AL_SIZE) == 0);
        } catch(Exception e) {
            fail(e.toString());
        }
        Exception ex = null;
        try {
            int[] buffers = new int[1];
            al.alGenBuffers(1, buffers);

            al.alBufferData(
                buffers[0],
                AL.AL_FORMAT_STEREO16,
                (ByteBuffer)null,
                0,
                0
            );
        } catch(IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull(ex);
        
        System.out.println("end testAlBufferDataintintByteBufferintint");
    }

    /*
     * Test for void alGetBufferf(int, int, float[])
     */
    final public void testAlGetBufferfintintfloatArray() {
        System.out.println("begin testAlGetBufferfintintfloatArray");
        
        // there are currently NO float attributes for buffers.
        
        System.out.println("end testAlGetBufferfintintfloatArray");
    }

    /*
     * Test for void alGetBufferf(int, int, FloatBuffer)
     */
    final public void testAlGetBufferfintintFloatBuffer() {
        System.out.println("begin testAlGetBufferfintintFloatBuffer");
        
        // there are currently NO float attributes for buffers.
        
        System.out.println("end testAlGetBufferfintintFloatBuffer");
    }

    /*
     * Test for float alGetBufferf(int, int)
     */
    final public void testAlGetBufferfintint() {
        System.out.println("begin testAlGetBufferfintintFloatBuffer");
        
        // there are currently NO float attributes for buffers.
        
        System.out.println("end testAlGetBufferfintintFloatBuffer");
    }

    /*
     * Test for void alGetBufferi(int, int, int[])
     */
    final public void testAlGetBufferiintintintArray() {
        System.out.println("begin testAlGetBufferiintintintArray");
        try {
            int[] buffers = new int[1];
            al.alGenBuffers(1, buffers);
            WAVData wd =
                WAVLoader.loadFromFile(TEST_FILE);
            al.alBufferData(
                buffers[0],
                wd.format,
                wd.data,
                wd.size,
                wd.freq
            );
            int[] size = new int[1];
            int[] freq = new int[1];
            al.alGetBufferi(buffers[0],AL.AL_SIZE, size);
            al.alGetBufferi(buffers[0],AL.AL_FREQUENCY, freq);
            assertEquals(wd.size, size[0]);
            assertEquals(wd.freq, freq[0]);
        } catch(Exception e) {
            fail(e.toString());
        }
        
        Exception ex = null;
        try {
            int[] buffers = new int[1];
            al.alGenBuffers(1, buffers);
            WAVData wd =
                WAVLoader.loadFromFile(TEST_FILE);
            al.alBufferData(
                buffers[0],
                wd.format,
                wd.data,
                wd.size,
                wd.freq
            );
            int[] size = null;
            al.alGetBufferi(buffers[0],AL.AL_SIZE, size);
           
        } catch (IllegalArgumentException e) {
            ex = e;
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertNotNull(ex);
        
        System.out.println("end testAlGetBufferiintintintArray");
    }

    /*
     * Test for void alGetBufferi(int, int, IntBuffer)
     */
    final public void testAlGetBufferiintintIntBuffer() {
        try {
            int[] buffers = new int[1];
            al.alGenBuffers(1, buffers);
            WAVData wd =
                WAVLoader.loadFromFile(TEST_FILE);
            al.alBufferData(
                buffers[0],
                wd.format,
                wd.data,
                wd.size,
                wd.freq
            );
            int[] size = new int[1];
            int[] freq = new int[1];
            al.alGetBufferi(buffers[0],AL.AL_SIZE, size);
            al.alGetBufferi(buffers[0],AL.AL_FREQUENCY, freq);
            assertEquals(wd.size, size[0]);
            assertEquals(wd.freq, freq[0]);
        } catch(Exception e) {
            fail(e.toString());
        }
        Exception ex = null;
        try {
            int[] buffers = new int[1];
            al.alGenBuffers(1, buffers);
            WAVData wd =
                WAVLoader.loadFromFile(TEST_FILE);
            al.alBufferData(
                buffers[0],
                wd.format,
                wd.data,
                wd.size,
                wd.freq
            );
            int[] size = null;
            al.alGetBufferi(buffers[0],AL.AL_SIZE, size);
           
        } catch (IllegalArgumentException e) {
            ex = e;
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        
        assertNotNull(ex);
        ex = null;
         try {
             int[] buffers = new int[1];
             al.alGenBuffers(1, buffers);
             WAVData wd =
                 WAVLoader.loadFromFile(TEST_FILE);
             al.alBufferData(
                 buffers[0],
                 wd.format,
                 wd.data,
                 wd.size,
                 wd.freq
             );
             int[] size = new int[1];
             al.alGetBufferi(buffers[0],AL.AL_SIZE, size);
           
         } catch (IllegalArgumentException e) {
             ex = e;
         } catch (UnsupportedAudioFileException e) {
             e.printStackTrace();
             fail(e.getMessage());
         } catch (IOException e) {
             e.printStackTrace();
             fail(e.getMessage());
         }
        
//         assertNotNull(ex);
    }

    /*
     * Test for int alGetBufferi(int, int)
     */
    final public void testAlGetBufferiintint() {
        //TODO Implement alGetBufferi().
    }

    /*
     * Test for void alGenSources(int, int[])
     */
    final public void testAlGenSourcesintintArray() {
        //TODO Implement alGenSources().
    }

    /*
     * Test for void alGenSources(int, IntBuffer)
     */
    final public void testAlGenSourcesintIntBuffer() {
        //TODO Implement alGenSources().
    }

    /*
     * Test for void alDeleteSources(int, int[])
     */
    final public void testAlDeleteSourcesintintArray() {
        //TODO Implement alDeleteSources().
    }

    /*
     * Test for void alDeleteSources(int, IntBuffer)
     */
    final public void testAlDeleteSourcesintIntBuffer() {
        //TODO Implement alDeleteSources().
    }

    final public void testAlIsSource() {
        //TODO Implement alIsSource().
    }

    final public void testAlSourcei() {
        //TODO Implement alSourcei().
    }

    final public void testAlSourcef() {
        //TODO Implement alSourcef().
    }

    /*
     * Test for void alSourcefv(int, int, float[])
     */
    final public void testAlSourcefvintintfloatArray() {
        //TODO Implement alSourcefv().
    }

    /*
     * Test for void alSourcefv(int, int, FloatBuffer)
     */
    final public void testAlSourcefvintintFloatBuffer() {
        //TODO Implement alSourcefv().
    }

    final public void testAlSource3f() {
        //TODO Implement alSource3f().
    }

    /*
     * Test for void alGetSourcef(int, int, float[])
     */
    final public void testAlGetSourcefintintfloatArray() {
        //TODO Implement alGetSourcef().
    }

    /*
     * Test for void alGetSourcef(int, int, FloatBuffer)
     */
    final public void testAlGetSourcefintintFloatBuffer() {
        //TODO Implement alGetSourcef().
    }

    /*
     * Test for float alGetSourcef(int, int)
     */
    final public void testAlGetSourcefintint() {
        //TODO Implement alGetSourcef().
    }

    /*
     * Test for void alGetSourcefv(int, int, FloatBuffer)
     */
    final public void testAlGetSourcefvintintFloatBuffer() {
        //TODO Implement alGetSourcefv().
    }

    /*
     * Test for void alGetSourcefv(int, int, float[])
     */
    final public void testAlGetSourcefvintintfloatArray() {
        //TODO Implement alGetSourcefv().
    }

    /*
     * Test for void alGetSourcei(int, int, int[])
     */
    final public void testAlGetSourceiintintintArray() {
        //TODO Implement alGetSourcei().
    }

    /*
     * Test for void alGetSourcei(int, int, IntBuffer)
     */
    final public void testAlGetSourceiintintIntBuffer() {
        //TODO Implement alGetSourcei().
    }

    /*
     * Test for int alGetSourcei(int, int)
     */
    final public void testAlGetSourceiintint() {
        //TODO Implement alGetSourcei().
    }

    final public void testAlSourcePlay() {
        //TODO Implement alSourcePlay().
    }

    /*
     * Test for void alSourcePlayv(int, IntBuffer)
     */
    final public void testAlSourcePlayvintIntBuffer() {
        //TODO Implement alSourcePlayv().
    }

    /*
     * Test for void alSourcePlayv(int, int[])
     */
    final public void testAlSourcePlayvintintArray() {
        //TODO Implement alSourcePlayv().
    }

    final public void testAlSourcePause() {
        //TODO Implement alSourcePause().
    }

    /*
     * Test for void alSourcePausev(int, int[])
     */
    final public void testAlSourcePausevintintArray() {
        //TODO Implement alSourcePausev().
    }

    /*
     * Test for void alSourcePausev(int, IntBuffer)
     */
    final public void testAlSourcePausevintIntBuffer() {
        //TODO Implement alSourcePausev().
    }

    final public void testAlSourceStop() {
        //TODO Implement alSourceStop().
    }

    /*
     * Test for void alSourceStopv(int, int[])
     */
    final public void testAlSourceStopvintintArray() {
        //TODO Implement alSourceStopv().
    }

    /*
     * Test for void alSourceStopv(int, IntBuffer)
     */
    final public void testAlSourceStopvintIntBuffer() {
        //TODO Implement alSourceStopv().
    }

    final public void testAlSourceRewind() {
        //TODO Implement alSourceRewind().
    }

    /*
     * Test for void alSourceRewindv(int, int[])
     */
    final public void testAlSourceRewindvintintArray() {
        //TODO Implement alSourceRewindv().
    }

    /*
     * Test for void alSourceRewindv(int, IntBuffer)
     */
    final public void testAlSourceRewindvintIntBuffer() {
        //TODO Implement alSourceRewindv().
    }

    /*
     * Test for void alSourceQueueBuffers(int, int, int[])
     */
    final public void testAlSourceQueueBuffersintintintArray() {
        //TODO Implement alSourceQueueBuffers().
    }

    /*
     * Test for void alSourceQueueBuffers(int, int, IntBuffer)
     */
    final public void testAlSourceQueueBuffersintintIntBuffer() {
        //TODO Implement alSourceQueueBuffers().
    }

    /*
     * Test for void alSourceUnqueueBuffers(int, int, int[])
     */
    final public void testAlSourceUnqueueBuffersintintintArray() {
        //TODO Implement alSourceUnqueueBuffers().
    }

    /*
     * Test for void alSourceUnqueueBuffers(int, int, IntBuffer)
     */
    final public void testAlSourceUnqueueBuffersintintIntBuffer() {
        //TODO Implement alSourceUnqueueBuffers().
    }

    final public void testAlListenerf() {
        //TODO Implement alListenerf().
    }

    final public void testAlListener3f() {
        //TODO Implement alListener3f().
    }

    /*
     * Test for void alListenerfv(int, float[])
     */
    final public void testAlListenerfvintfloatArray() {
        //TODO Implement alListenerfv().
    }

    /*
     * Test for void alListenerfv(int, FloatBuffer)
     */
    final public void testAlListenerfvintFloatBuffer() {
        //TODO Implement alListenerfv().
    }

    final public void testAlListeneri() {
        //TODO Implement alListeneri().
    }

    /*
     * Test for void alGetListenerf(int, float[])
     */
    final public void testAlGetListenerfintfloatArray() {
        //TODO Implement alGetListenerf().
    }

    /*
     * Test for void alGetListenerf(int, FloatBuffer)
     */
    final public void testAlGetListenerfintFloatBuffer() {
        //TODO Implement alGetListenerf().
    }

    /*
     * Test for float alGetListenerf(int)
     */
    final public void testAlGetListenerfint() {
        //TODO Implement alGetListenerf().
    }

    /*
     * Test for void alGetListener3f(int, FloatBuffer, FloatBuffer, FloatBuffer)
     */
    final public void testAlGetListener3fintFloatBufferFloatBufferFloatBuffer() {
        //TODO Implement alGetListener3f().
    }

    /*
     * Test for void alGetListener3f(int, float[], float[], float[])
     */
    final public void testAlGetListener3fintfloatArrayfloatArrayfloatArray() {
        //TODO Implement alGetListener3f().
    }

    /*
     * Test for void alGetListenerfv(int, float[])
     */
    final public void testAlGetListenerfvintfloatArray() {
        //TODO Implement alGetListenerfv().
    }

    /*
     * Test for void alGetListenerfv(int, FloatBuffer)
     */
    final public void testAlGetListenerfvintFloatBuffer() {
        //TODO Implement alGetListenerfv().
    }

    /*
     * Test for void alGetListeneri(int, int[])
     */
    final public void testAlGetListeneriintintArray() {
        //TODO Implement alGetListeneri().
    }

    /*
     * Test for void alGetListeneri(int, IntBuffer)
     */
    final public void testAlGetListeneriintIntBuffer() {
        //TODO Implement alGetListeneri().
    }

    /*
     * Test for int alGetListeneri(int)
     */
    final public void testAlGetListeneriint() {
        //TODO Implement alGetListeneri().
    }

    final public void testAlEnable() {
        //TODO Implement alEnable().
    }

    final public void testAlDisable() {
        //TODO Implement alDisable().
    }

    final public void testAlIsEnabled() {
        //TODO Implement alIsEnabled().
    }

    final public void testAlGetBoolean() {
        //TODO Implement alGetBoolean().
    }

    final public void testAlGetDouble() {
        //TODO Implement alGetDouble().
    }

    final public void testAlGetFloat() {
        //TODO Implement alGetFloat().
    }

    final public void testAlGetInteger() {
        //TODO Implement alGetInteger().
    }

    /*
     * Test for void alGetDoublev(int, DoubleBuffer)
     */
    final public void testAlGetDoublevintDoubleBuffer() {
        //TODO Implement alGetDoublev().
    }

    /*
     * Test for void alGetDoublev(int, double[])
     */
    final public void testAlGetDoublevintdoubleArray() {
        //TODO Implement alGetDoublev().
    }

    /*
     * Test for void alGetFloatv(int, FloatBuffer)
     */
    final public void testAlGetFloatvintFloatBuffer() {
        //TODO Implement alGetFloatv().
    }

    /*
     * Test for void alGetFloatv(int, float[])
     */
    final public void testAlGetFloatvintfloatArray() {
        //TODO Implement alGetFloatv().
    }

    /*
     * Test for void alGetIntegerv(int, IntBuffer)
     */
    final public void testAlGetIntegervintIntBuffer() {
        //TODO Implement alGetIntegerv().
    }

    /*
     * Test for void alGetIntegerv(int, int[])
     */
    final public void testAlGetIntegervintintArray() {
        //TODO Implement alGetIntegerv().
    }

    final public void testAlGetString() {
        //TODO Implement alGetString().
    }

    final public void testAlDistanceModel() {
        //TODO Implement alDistanceModel().
    }

    final public void testAlDopplerFactor() {
        //TODO Implement alDopplerFactor().
    }

    final public void testAlDopplerVelocity() {
        //TODO Implement alDopplerVelocity().
    }

    final public void testAlGetError() {
        //TODO Implement alGetError().
    }

    final public void testAlIsExtensionPresent() {
        //TODO Implement alIsExtensionPresent().
    }

    final public void testAlGetEnumValue() {
        //TODO Implement alGetEnumValue().
    }

}
