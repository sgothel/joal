/*
 * Created on Jun 3, 2003
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.jogamp.openal;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.jogamp.openal.util.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Athomas Goldberg
 *
 */
public class ALTest {

    static AL al;
    static ALC alc;
    static ALCcontext context;
    static ALCdevice device;
    final static String TEST_FILE = "lewiscarroll.wav";

    @BeforeClass
    public static void setUp() {
        try {
            al = ALFactory.getAL();
            alc = ALFactory.getALC();
            device = alc.alcOpenDevice(null);
            context = alc.alcCreateContext(device, null);
            alc.alcMakeContextCurrent(context);
        } catch (ALException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() {
        alc.alcMakeContextCurrent(null);
        alc.alcDestroyContext(context);
        alc.alcCloseDevice(device);
    }


    /*
     * Test for void alGenBuffers(int, IntBuffer)
     *//*
    @Test public void testAlGenBuffersintIntBuffer() {
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

    @Test
    public void testAlGenBuffersintintArray() {

        System.out.println("begin testAlGenBuffersintintArray");
        // try basic case
        try {
            int[] buffers = new int[7];
            al.alGenBuffers(7, buffers, 0);
            for (int i = 0; i < 7; i++) {
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
            al.alGenBuffers(7, buffers, 0);


        } catch (IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull(ex);
        ex = null;
        try {
            int[] buffers = new int[5];
            al.alGenBuffers(7, buffers, 0);
        } catch (IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull(ex);

        System.out.println("end testAlGenBuffersintintArray");

    }

    /*
     * Test for void alDeleteBuffers(int, IntBuffer)
     */
    @Test
    public void testAlDeleteBuffersintIntBuffer() {
        System.out.println("begin testAlDeleteBuffersintintArray");
        // try basic case
        try {
            int[] buffers = new int[7];
            al.alGenBuffers(7, buffers, 0);
            for (int i = 0; i < 7; i++) {
                assertFalse(buffers[i] == 0);
                assertTrue(al.alIsBuffer(buffers[i]));
            }
            al.alDeleteBuffers(7, buffers, 0);
            for (int i = 0; i < 7; i++) {
                assertFalse(al.alIsBuffer(buffers[i]));
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }

        Exception ex = null;
        // try exceptions
        try {
            al.alDeleteBuffers(7, (int[]) null, 0);
        } catch (IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull(ex);
        ex = null;
        try {
            int[] buffers = new int[5];
            al.alGenBuffers(5, buffers, 0);
            al.alDeleteBuffers(7, buffers, 0);
        } catch (IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull(ex);

        try {
            int[] buffers = new int[7];
            al.alDeleteBuffers(7, buffers, 0);
            assertTrue(al.alGetError() != 0);
        } catch (Exception e) {
            fail("deleting an unfilled buffer list should generate an ALError but not an exception");
        }

        System.out.println("end testAlDeleteBuffersintintArray");
    }

    /*
     * Test for void alDeleteBuffers(int, int[])
     */
    @Test
    public void testAlDeleteBuffersintintArray() {
        System.out.println("begin testAlDeleteBuffersintIntBuffer");
        // try basic case
        try {
            int[] buffers = new int[7];
            al.alGenBuffers(7, buffers, 0);
            for (int i = 0; i < 7; i++) {
                assertFalse(buffers[i] == 0);
                assertTrue(al.alIsBuffer(buffers[i]));
            }
            al.alDeleteBuffers(7, buffers, 0);
            for (int i = 0; i < 7; i++) {
                assertFalse(al.alIsBuffer(buffers[i]));
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }

        Exception ex = null;
        // try exceptions
        try {
            al.alDeleteBuffers(7, (int[]) null, 0);
        } catch (IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull(ex);
        ex = null;
        try {
            int[] buffers = new int[5];
            al.alGenBuffers(5, buffers, 0);
            al.alDeleteBuffers(7, buffers, 0);
        } catch (IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull(ex);

        ex = null;
        try {
            int[] buffers = new int[5];
            al.alDeleteBuffers(7, buffers, 0);
        } catch (IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull(ex);

        try {
            int[] buffers = new int[7];
            al.alDeleteBuffers(7, buffers, 0);
            assertTrue(al.alGetError() != 0);
        } catch (Exception e) {
            fail("deleting an unfilled buffer list should generate an ALError but not an exception");
        }

        System.out.println("end testAlDeleteBuffersintintArray");
    }

    @Test
    public void testAlIsBuffer() {
        System.out.println("begin testALIsBuffer");
        try {
            // check a bufferlist with known bad values
            int[] buffers = new int[7];
            for (int i = 0; i < 7; i++) {
                buffers[i] = -1;
                assertFalse(al.alIsBuffer(buffers[i]));
            }
            // created 
            al.alGenBuffers(7, buffers, 0);
            for (int i = 0; i < 7; i++) {
                assertTrue(al.alIsBuffer(buffers[i]));
            }
            // deleted
            al.alDeleteBuffers(7, buffers, 0);
            for (int i = 0; i < 7; i++) {
                assertFalse(al.alIsBuffer(buffers[i]));
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        System.out.println("end testALisBuffer");
    }

    /*
     * Test for void alBufferData(int, int, Buffer, int, int)
     */
    @Test
    public void testAlBufferDataintintByteBufferintint() {
        System.out.println("begin testAlBufferDataintintByteBufferintint");
        try {
            int[] buffers = new int[1];
            al.alGenBuffers(1, buffers, 0);
            WAVData wd =
                    WAVLoader.loadFromFile(TEST_FILE);
            al.alBufferData(
                    buffers[0],
                    wd.format,
                    wd.data,
                    wd.size,
                    wd.freq);
            int[] tmp = new int[1];
            al.alGetBufferi(buffers[0], AL.AL_SIZE, tmp, 0);
            assertFalse(tmp[0] == 0);
        } catch (Exception e) {
            fail(e.toString());
        }
        Exception ex = null;
        try {
            int[] buffers = new int[1];
            al.alGenBuffers(1, buffers, 0);

            al.alBufferData(
                    buffers[0],
                    AL.AL_FORMAT_STEREO16,
                    null,
                    0,
                    0);
        } catch (IllegalArgumentException e) {
            ex = e;
        }
        assertNotNull(ex);

        System.out.println("end testAlBufferDataintintByteBufferintint");
    }

    /*
     * Test for void alGetBufferf(int, int, float[])
     */
    @Test
    public void testAlGetBufferfintintfloatArray() {
        System.out.println("begin testAlGetBufferfintintfloatArray");

        // there are currently NO float attributes for buffers.

        System.out.println("end testAlGetBufferfintintfloatArray");
    }

    /*
     * Test for void alGetBufferf(int, int, FloatBuffer)
     */
    @Test
    public void testAlGetBufferfintintFloatBuffer() {
        System.out.println("begin testAlGetBufferfintintFloatBuffer");

        // there are currently NO float attributes for buffers.

        System.out.println("end testAlGetBufferfintintFloatBuffer");
    }

    /*
     * Test for float alGetBufferf(int, int)
     */
    @Test
    public void testAlGetBufferfintint() {
        System.out.println("begin testAlGetBufferfintintFloatBuffer");

        // there are currently NO float attributes for buffers.

        System.out.println("end testAlGetBufferfintintFloatBuffer");
    }

    /*
     * Test for void alGetBufferi(int, int, int[])
     */
    @Test
    public void testAlGetBufferiintintintArray() {
        System.out.println("begin testAlGetBufferiintintintArray");
        try {
            int[] buffers = new int[1];
            al.alGenBuffers(1, buffers, 0);
            WAVData wd =
                    WAVLoader.loadFromFile(TEST_FILE);
            al.alBufferData(
                    buffers[0],
                    wd.format,
                    wd.data,
                    wd.size,
                    wd.freq);
            int[] size = new int[1];
            int[] freq = new int[1];
            al.alGetBufferi(buffers[0], AL.AL_SIZE, size, 0);
            al.alGetBufferi(buffers[0], AL.AL_FREQUENCY, freq, 0);
            assertEquals(wd.size, size[0]);
            assertEquals(wd.freq, freq[0]);
        } catch (Exception e) {
            fail(e.toString());
        }

        Exception ex = null;
        try {
            int[] buffers = new int[1];
            al.alGenBuffers(1, buffers, 0);
            WAVData wd =
                    WAVLoader.loadFromFile(TEST_FILE);
            al.alBufferData(
                    buffers[0],
                    wd.format,
                    wd.data,
                    wd.size,
                    wd.freq);
            int[] size = null;
            al.alGetBufferi(buffers[0], AL.AL_SIZE, size, 0);

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
    @Test
    public void testAlGetBufferiintintIntBuffer() {
        try {
            int[] buffers = new int[1];
            al.alGenBuffers(1, buffers, 0);
            WAVData wd =
                    WAVLoader.loadFromFile(TEST_FILE);
            al.alBufferData(
                    buffers[0],
                    wd.format,
                    wd.data,
                    wd.size,
                    wd.freq);
            int[] size = new int[1];
            int[] freq = new int[1];
            al.alGetBufferi(buffers[0], AL.AL_SIZE, size, 0);
            al.alGetBufferi(buffers[0], AL.AL_FREQUENCY, freq, 0);
            assertEquals(wd.size, size[0]);
            assertEquals(wd.freq, freq[0]);
        } catch (Exception e) {
            fail(e.toString());
        }
        Exception ex = null;
        try {
            int[] buffers = new int[1];
            al.alGenBuffers(1, buffers, 0);
            WAVData wd =
                    WAVLoader.loadFromFile(TEST_FILE);
            al.alBufferData(
                    buffers[0],
                    wd.format,
                    wd.data,
                    wd.size,
                    wd.freq);
            int[] size = null;
            al.alGetBufferi(buffers[0], AL.AL_SIZE, size, 0);

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
            al.alGenBuffers(1, buffers, 0);
            WAVData wd =
                    WAVLoader.loadFromFile(TEST_FILE);
            al.alBufferData(
                    buffers[0],
                    wd.format,
                    wd.data,
                    wd.size,
                    wd.freq);
            int[] size = new int[1];
            al.alGetBufferi(buffers[0], AL.AL_SIZE, size, 0);

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
}
