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

package net.java.games.joal.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;


/**
 * Provides a collection of methods for generating direct Buffers of various
 * types.
 *
 * @author Athomas Goldberg
 */
public class BufferUtils {
    private static final int CHAR = 2;
    private static final int SHORT = 2;
    private static final int INT = 4;
    private static final int LONG = 8;
    private static final int FLOAT = 4;
    private static final int DOUBLE = 8;

    private BufferUtils() {
    }

    /**
     * Create a new direct ByteBuffer of the specified size.
     *
     * @param size (in bytes) of the returned ByteBuffer
     *
     * @return a new direct ByteBuffer of the specified size
     */
    public static ByteBuffer newByteBuffer(int size) {
        ByteBuffer result = null;
        result = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());

        return result;
    }

    /**
     * Create a new direct CharBuffer of the specified size.
     *
     * @param size (in chars) of the returned CharBuffer
     *
     * @return a new direct CharBuffer of the specified size
     */
    public static CharBuffer newCharBuffer(int size) {
        CharBuffer result = null;
        ByteBuffer temp = newByteBuffer(size * CHAR);
        result = temp.asCharBuffer();

        return result;
    }

    /**
     * Create a new direct ShortBuffer of the specified size.
     *
     * @param size (in shorts) of the returned ShortBuffer
     *
     * @return a new direct ShortBuffer of the specified size
     */
    public static ShortBuffer newShortBuffer(int size) {
        ShortBuffer result = null;
        ByteBuffer temp = newByteBuffer(size * SHORT);
        result = temp.asShortBuffer();

        return result;
    }

    /**
     * Create a new direct IntBuffer of the specified size.
     *
     * @param size (in ints) of the returned IntBuffer
     *
     * @return a new direct IntBuffer of the specified size
     */
    public static IntBuffer newIntBuffer(int size) {
        IntBuffer result = null;
        ByteBuffer temp = newByteBuffer(size * INT);
        result = temp.asIntBuffer();

        return result;
    }

    /**
     * Create a new direct LongBuffer of the specified size.
     *
     * @param size (in longs) of the returned LongBuffer
     *
     * @return a new direct LongsBuffer of the specified size
     */
    public static LongBuffer newLongBuffer(int size) {
        LongBuffer result = null;
        ByteBuffer temp = newByteBuffer(size * LONG);
        result = temp.asLongBuffer();

        return result;
    }

    /**
     * Create a new direct FloatBuffer of the specified size.
     *
     * @param size (in floats) of the returned FloatBuffer
     *
     * @return a new direct FloatBuffer of the specified size
     */
    public static FloatBuffer newFloatBuffer(int size) {
        FloatBuffer result = null;
        ByteBuffer temp = newByteBuffer(size * FLOAT);
        result = temp.asFloatBuffer();

        return result;
    }

    /**
     * Create a new direct DoubleBuffer of the specified size.
     *
     * @param size (in doubles) of the returned DoubleBuffer
     *
     * @return a new direct DoubleBuffer of the specified size
     */
    public static DoubleBuffer newDoubleBuffer(int size) {
        DoubleBuffer result = null;
        ByteBuffer temp = newByteBuffer(size * DOUBLE);
        result = temp.asDoubleBuffer();
        return result;
    }
}
