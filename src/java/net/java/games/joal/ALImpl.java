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

package net.java.games.joal;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;


final class ALImpl implements AL {
    ALImpl() {
        System.loadLibrary("joal");
    }

    // AL_BUFFER RELATED METHODS

    public void alGenBuffers(int n, IntBuffer buffers) {
        if (
            (buffers == null) ||
                !buffers.isDirect() ||
                (n > buffers.capacity())
        ) {
            throw new IllegalArgumentException(
                "buffers must be direct, can not be null" +
                " and buffer capacity must be greater than requested number of " +
                "buffers."
            );
        } else {
            alGenBuffersNative(n, buffers);
        }
    }

    private native void alGenBuffersNative(int n, IntBuffer buffers);

    public void alGenBuffers(int n, int[] buffers) {
        if ((buffers == null) || (n > buffers.length)) {
            throw new IllegalArgumentException(
                "buffers can not be null" +
                " and array length must be greater than requested number of " +
                "buffers."
            );
        } else {
            alGenBuffersNative(n, buffers);
        }
    }

    private native void alGenBuffersNative(int n, int[] buffers);

    public void alDeleteBuffers(int n, IntBuffer buffers) {
        if (
            (buffers == null) ||
                !buffers.isDirect() ||
                (n > buffers.capacity())
        ) {
            throw new IllegalArgumentException(
                "buffers must be direct, can not be null" +
                " and buffer capacity must be greater than requested number of " +
                "buffers."
            );
        } else {
            alDeleteBuffersNative(n, buffers);
        }
    }

    private native void alDeleteBuffersNative(int n, IntBuffer buffers);

    public void alDeleteBuffers(int n, int[] buffers) {
        if ((buffers == null) || (n > buffers.length)) {
            throw new IllegalArgumentException(
                "buffers can not be null" +
                " and array length must be greater than requested number of " +
                "buffers."
            );
        } else {
            alDeleteBuffersNative(n, buffers);
        }
    }

    private native void alDeleteBuffersNative(int n, int[] buffers);

    public native boolean alIsBuffer(int bufferName);

    public void alBufferData(
        int buffername,
        int format,
        byte[] data,
        int size,
        int frequency
    ) {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        } else {
            alBufferDataNative(buffername, format, data, size, frequency);
        }
    }

    private native void alBufferDataNative(
        int buffername,
        int format,
        byte[] data,
        int size,
        int frequency
    );

    public void alBufferData(
        int buffername,
        int format,
        ByteBuffer data,
        int size,
        int frequency
    ) {
        if ((data == null) || !data.isDirect()) {
            throw new IllegalArgumentException(
                "data must be a direct, non-null buffer"
            );
        } else {
            alBufferDataNative(buffername, format, data, size, frequency);
        }
    }

    private native void alBufferDataNative(
        int buffername,
        int format,
        ByteBuffer data,
        int size,
        int frequency
    );

    public void alGetBufferf(int bufferName, int pname, float[] retValue) {
        if (retValue == null) {
            throw new IllegalArgumentException(
                "Return Value argument must not be null."
            );
        } else {
            alGetBufferfNative(bufferName, pname, retValue);
        }
    }

    private native void alGetBufferfNative(
        int bufferName,
        int pname,
        float[] retValue
    );

    public void alGetBufferf(int bufferName, int pname, FloatBuffer retValue) {
        if ((retValue == null) || !retValue.isDirect()) {
            throw new IllegalArgumentException(
                "Return Value argument must be a direct, non-null buffer"
            );
        } else {
            alGetBufferfNative(bufferName, pname, retValue);
        }
    }

    private native void alGetBufferfNative(
        int bufferName,
        int pname,
        FloatBuffer retValue
    );

    public native float alGetBufferf(int bufferName, int pname);

    public void alGetBufferi(int bufferName, int pname, int[] retValue) {
        if (retValue == null) {
            throw new IllegalArgumentException(
                "Return Value argument must not be null"
            );
        } else {
            alGetBufferiNative(bufferName, pname, retValue);
        }
    }

    private native void alGetBufferiNative(
        int bufferName,
        int pname,
        int[] retValue
    );

    public void alGetBufferi(int bufferName, int pname, IntBuffer retValue) {
        if ((retValue == null) || !retValue.isDirect()) {
            throw new IllegalArgumentException(
                "Return Value argument must be a direct, non-null IntBuffer"
            );
        } else {
            alGetBufferiNative(bufferName, pname, retValue);
        }
    }

    private native void alGetBufferiNative(
        int bufferName,
        int pname,
        IntBuffer retValue
    );

    public native int alGetBufferi(int bufferName, int pname);

    // SOURCE RELATED METHODS
    public void alGenSources(int numSources, int[] sources) {
        if ((sources == null) || (numSources > sources.length)) {
            throw new IllegalArgumentException(
                "sources can not be null" +
                " and array length must be greater than requested number of " +
                "sources."
            );
        } else {
            alGenSourcesNative(numSources, sources);
        }
    }

    private native void alGenSourcesNative(int numSources, int[] sources);

    public void alGenSources(int numSources, IntBuffer sources) {
        if (
            (sources == null) ||
                !sources.isDirect() ||
                (numSources > sources.capacity())
        ) {
            throw new IllegalArgumentException(
                "sources buffer must be direct, can not be null" +
                " and capacity must be greater than requested number of " +
                "sources."
            );
        } else {
            alGenSourcesNative(numSources, sources);
        }
    }

    private native void alGenSourcesNative(int numSources, IntBuffer sources);

    public void alDeleteSources(int numSources, int[] sources) {
        if ((sources == null) || (numSources > sources.length)) {
            throw new IllegalArgumentException(
                "sources can not be null" +
                " and array length must be greater than requested number of " +
                "sources."
            );
        } else {
            alDeleteSourcesNative(numSources, sources);
        }
    }

    private native void alDeleteSourcesNative(int numSources, int[] sources);

    public void alDeleteSources(int numSources, IntBuffer sources) {
        if (
            (sources == null) ||
                !sources.isDirect() ||
                (numSources > sources.capacity())
        ) {
            throw new IllegalArgumentException(
                "sources buffer must be direct, can not be null" +
                " and capacity must be greater than requested number of " +
                "sources."
            );
        } else {
            alDeleteSourcesNative(numSources, sources);
        }
    }

    private native void alDeleteSourcesNative(
        int numSources,
        IntBuffer sources
    );

    public native boolean alIsSource(int sourceName);

    public native void alSourcei(int sourcename, int pname, int value);

    public native void alSourcef(int sourcename, int pname, float value);

    public native void alSourcefv(int sourcename, int pname, float[] value);

    public void alSourcefv(int sourcename, int pname, FloatBuffer value) {
        if ((value != null) && !value.isDirect()) {
            throw new IllegalArgumentException("buffer must be direct");
        } else {
            alSourcefvNative(sourcename, pname, value);
        }
    }

    private native void alSourcefvNative(
        int sourcename,
        int pname,
        FloatBuffer value
    );

    public native void alSource3f(
        int sourcename,
        int pname,
        float v1,
        float v2,
        float v3
    );

    public void alGetSourcef(int sourcename, int pname, float[] retValue) {
        if (retValue == null) {
            throw new IllegalArgumentException("retValue must not be null");
        } else {
            alGetSourcefNative(sourcename, pname, retValue);
        }
    }

    private native void alGetSourcefNative(
        int sourcename,
        int pname,
        float[] retValue
    );

    public void alGetSourcef(int sourceName, int pname, FloatBuffer buffer) {
        if ((buffer == null) || !buffer.isDirect()) {
            throw new IllegalArgumentException(
                "Buffer must be direct and non-null"
            );
        } else {
            alGetSourcefNative(sourceName, pname, buffer);
        }
    }

    private native void alGetSourcefNative(
        int sourceName,
        int pname,
        FloatBuffer buffer
    );

    public native float alGetSourcef(int sourceName, int pname);

    public void alGetSourcefv(int sourcename, int pname, FloatBuffer value) {
        if ((value == null) || !value.isDirect()) {
            throw new IllegalArgumentException(
                "Buffer must be direct and non-null"
            );
        } else {
            alGetSourcefvNative(sourcename, pname, value);
        }
    }

    private native void alGetSourcefvNative(
        int sourcename,
        int pname,
        FloatBuffer value
    );


    public void alGetSourcefv(int sourcename, int pname, float[] retValue) {
        if (retValue == null) {
            throw new IllegalArgumentException(
                "retValue arg array must not be null"
            );
        } else {
            alGetSourcefvNative(sourcename, pname, retValue);
        }
    }

    private native void alGetSourcefvNative(
        int sourcename,
        int pname,
        float[] retValue
    );


    public void alGetSourcei(int sourcename, int pname, int[] retValue) {
        if (retValue == null) {
            throw new IllegalArgumentException(
                "retValue arg array must not be null"
            );
        } else {
            alGetSourceiNative(sourcename, pname, retValue);
        }
    }

    private native void alGetSourceiNative(
        int sourcename,
        int pname,
        int[] retValue
    );

    public void alGetSourcei(int sourcename, int pname, IntBuffer value) {
        if ((value == null) || !value.isDirect()) {
            throw new IllegalArgumentException(
                "Buffer must be direct and non-null"
            );
        } else {
            alGetSourceiNative(sourcename, pname, value);
        }
    }

    private native void alGetSourceiNative(
        int sourcename,
        int pname,
        IntBuffer retValue
    );

    public native int alGetSourcei(int sourcename, int pname);


    public native void alSourcePlay(int sourcename);

    public void alSourcePlayv(int numSources, int[] sourcenames) {
        if ((sourcenames == null) || (numSources > sourcenames.length)) {
            throw new IllegalArgumentException(
                "sourcenames must be non-null" +
                " and at least as large as the number of sources requested"
            );
        } else {
            alSourcePlayvNative(numSources, sourcenames);
        }
    }

    private native void alSourcePlayvNative(int numSources, int[] sourcenames);

    public void alSourcePlayv(int numSources, IntBuffer sourcenames) {
        if (
            (sourcenames == null) ||
                (numSources > sourcenames.capacity()) ||
                !sourcenames.isDirect()
        ) {
            throw new IllegalArgumentException(
                "sourcenames buffer must be direct, non-null and have a" +
                " equals or greater capacity than the number of sources" +
                " requested"
            );
        } else {
            alSourcePlayvNative(numSources, sourcenames);
        }
    }

    private native void alSourcePlayvNative(
        int numSources,
        IntBuffer sourcenames
    );

    public native void alSourcePause(int sourcename);

    public void alSourcePausev(int numSources, int[] sourcenames) {
        if ((sourcenames == null) || (numSources > sourcenames.length)) {
            throw new IllegalArgumentException(
                "sourcenames must be non-null" +
                " and at least as large as the number of sources requested"
            );
        } else {
            alSourcePausevNative(numSources, sourcenames);
        }
    }

    private native void alSourcePausevNative(int numSources, int[] sourcenames);

    public void alSourcePausev(int numSources, IntBuffer sourcenames) {
        if (
            (sourcenames == null) ||
                (numSources > sourcenames.capacity()) ||
                !sourcenames.isDirect()
        ) {
            throw new IllegalArgumentException(
                "sourcenames buffer must be direct, non-null and have a" +
                " equals or greater capacity than the number of sources" +
                " requested"
            );
        } else {
            alSourcePausevNative(numSources, sourcenames);
        }
    }

    private native void alSourcePausevNative(
        int numSources,
        IntBuffer sourcenames
    );

    public native void alSourceStop(int sourcename);

    public void alSourceStopv(int numSources, int[] sourcenames) {
        if ((sourcenames == null) || (numSources > sourcenames.length)) {
            throw new IllegalArgumentException(
                "sourcenames must be non-null" +
                " and at least as large as the number of sources requested"
            );
        } else {
            alSourceStopvNative(numSources, sourcenames);
        }
    }

    private native void alSourceStopvNative(int numSources, int[] sourcenames);

    public void alSourceStopv(int numSources, IntBuffer sourcenames) {
        if (
            (sourcenames == null) ||
                (numSources > sourcenames.capacity()) ||
                !sourcenames.isDirect()
        ) {
            throw new IllegalArgumentException(
                "sourcenames buffer must be direct, non-null and have a" +
                " equals or greater capacity than the number of sources" +
                " requested"
            );
        } else {
            alSourcePlayvNative(numSources, sourcenames);
        }
    }

    private native void alSourceStopvNative(
        int numSources,
        IntBuffer sourcenames
    );

    public native void alSourceRewind(int sourcename);

    public void alSourceRewindv(int numSources, int[] sourcenames) {
        if ((sourcenames == null) || (numSources > sourcenames.length)) {
            throw new IllegalArgumentException(
                "sourcenames must be non-null" +
                " and at least as large as the number of sources requested"
            );
        } else {
            alSourceRewindvNative(numSources, sourcenames);
        }
    }

    private native void alSourceRewindvNative(
        int numSources,
        int[] sourcenames
    );

    public void alSourceRewindv(int numSources, IntBuffer sourcenames) {
        if (
            (sourcenames == null) ||
                (numSources > sourcenames.capacity()) ||
                !sourcenames.isDirect()
        ) {
            throw new IllegalArgumentException(
                "sourcenames buffer must be direct, non-null and have a" +
                " equals or greater capacity than the number of sources" +
                " requested"
            );
        } else {
            alSourceRewindvNative(numSources, sourcenames);
        }
    }

    private native void alSourceRewindvNative(
        int numSources,
        IntBuffer sourcenames
    );

    public void alSourceQueueBuffers(
        int sourcename,
        int numBuffers,
        int[] buffernames
    ) {
        if ((buffernames == null) || (numBuffers > buffernames.length)) {
            throw new IllegalArgumentException(
                "buffernames must be non-null and equal or greater " +
                "than the numBuffers specified"
            );
        } else {
            alSourceQueueBuffersNative(sourcename, numBuffers, buffernames);
        }
    }

    private native void alSourceQueueBuffersNative(
        int sourcename,
        int numBuffers,
        int[] buffernames
    );

    public void alSourceQueueBuffers(
        int sourcename,
        int numBuffers,
        IntBuffer buffernames
    ) {
        if (
            (buffernames == null) ||
                !buffernames.isDirect() ||
                (numBuffers > buffernames.capacity())
        ) {
            throw new IllegalArgumentException(
                "only non-null, direct buffers of numBuffers capacity" +
                " or greater may be used."
            );
        } else {
            alSourceQueueBuffersNative(sourcename, numBuffers, buffernames);
        }
    }

    private native void alSourceQueueBuffersNative(
        int sourcename,
        int numBuffers,
        IntBuffer buffernames
    );

    public void alSourceUnqueueBuffers(
        int sourcename,
        int numBuffers,
        int[] buffernames
    ) {
        if ((buffernames == null) || (numBuffers > buffernames.length)) {
            throw new IllegalArgumentException(
                "buffernames must be non-null and equal or greater " +
                "than the numBuffers specified"
            );
        } else {
            alSourceUnqueueBuffersNative(sourcename, numBuffers, buffernames);
        }
    }

    private native void alSourceUnqueueBuffersNative(
        int sourcename,
        int numBuffers,
        int[] buffernames
    );

    public void alSourceUnqueueBuffers(
        int sourcename,
        int numBuffers,
        IntBuffer buffernames
    ) {
        if (
            (buffernames == null) ||
                !buffernames.isDirect() ||
                (numBuffers > buffernames.capacity())
        ) {
            throw new IllegalArgumentException(
                "only non-null, direct buffers of numBuffers capacity" +
                " or greater may be used."
            );
        } else {
            alSourceUnqueueBuffersNative(sourcename, numBuffers, buffernames);
        }
    }

    private native void alSourceUnqueueBuffersNative(
        int sourcename,
        int numBuffers,
        IntBuffer buffernames
    );

    // LISTENER RELATED METHODS
    public native void alListenerf(int pname, float value);

    public native void alListener3f(int pname, float v1, float v2, float v3);

    public native void alListenerfv(int pname, float[] values);

    public native void alListenerfv(int pname, FloatBuffer values);

    public native void alListeneri(int pname, int value);

    public void alGetListenerf(int pname, float[] retValue) {
        if (retValue == null) {
            throw new IllegalArgumentException(
                "retValue must be non-null array"
            );
        }
    }

    private native void alGetListenerfNative(int pname, float[] retValue);

    public void alGetListenerf(int pname, FloatBuffer retValue) {
        if ((retValue == null) || !retValue.isDirect()) {
            throw new IllegalArgumentException(
                "retValue must be a non-null direct buffer"
            );
        } else {
            alGetListenerfNative(pname, retValue);
        }
    }

    private native void alGetListenerfNative(int pname, FloatBuffer retValue);

    public native float alGetListenerf(int pname);

    public void alGetListener3f(
        int pname,
        FloatBuffer v1,
        FloatBuffer v2,
        FloatBuffer v3
    ) {
        if (
            ((v1 == null) || !v1.isDirect()) ||
                ((v2 == null) || !v2.isDirect()) ||
                ((v3 == null) || !v3.isDirect())
        ) {
            throw new IllegalArgumentException(
                "buffers must be non-null and direct"
            );
        } else {
            alGetListener3fNative(pname, v1, v2, v3);
        }
    }

    private native void alGetListener3fNative(
        int pname,
        FloatBuffer v1,
        FloatBuffer v2,
        FloatBuffer v3
    );

    public void alGetListener3f(int pname, float[] v1, float[] v2, float[] v3) {
        if ((v1 == null) || (v2 == null) || (v3 == null)) {
            throw new IllegalArgumentException("Arrays must be non-null");
        } else {
            alGetListener3fNative(pname, v1, v2, v3);
        }
    }

    private native void alGetListener3fNative(
        int pname,
        float[] v1,
        float[] v2,
        float[] v3
    );

    public void alGetListenerfv(int pname, float[] retValue) {
        if (retValue == null) {
            throw new IllegalArgumentException("Array must be non-null");
        } else {
            alGetListenerfvNative(pname, retValue);
        }
    }

    private native void alGetListenerfvNative(int pname, float[] retValue);

    public void alGetListenerfv(int pname, FloatBuffer retValue) {
        if ((retValue == null) || !retValue.isDirect()) {
            throw new IllegalArgumentException(
                "Buffer must be non-null and direct"
            );
        } else {
            alGetListenerfvNative(pname, retValue);
        }
    }

    private native void alGetListenerfvNative(int pname, FloatBuffer retValue);

    public void alGetListeneri(int pname, int[] retValue) {
        if (retValue == null) {
            throw new IllegalArgumentException("Array must be non-null");
        } else {
            alGetListeneriNative(pname, retValue);
        }
    }

    private native void alGetListeneriNative(int pname, int[] retValue);

    public void alGetListeneri(int pname, IntBuffer retValue) {
        if ((retValue == null) || !retValue.isDirect()) {
            throw new IllegalArgumentException(
                "Buffer must be non-null and direct"
            );
        } else {
            alGetListeneriNative(pname, retValue);
        }
    }

    private native void alGetListeneriNative(int pname, IntBuffer retValue);

    public native int alGetListeneri(int pname);

    // STATE RELATED METHODS
    public native void alEnable(int capability);

    public native void alDisable(int capability);

    public native boolean alIsEnabled(int capability);

    public native boolean alGetBoolean(int pname);

    public native double alGetDouble(int pname);

    public native float alGetFloat(int pname);

    public native int alGetInteger(int pname);

    // No Boolean Array states at the moment
    // public native void getBooleanv(int pname, ByteBuffer value);

    public void alGetBooleanv(int pname, boolean[] value) {
        if (value == null) {
            throw new IllegalArgumentException("Array must be non-null");
        } else {
            value[0] = false;
            // do nothing for now, there are no boolean vector props
            // alGetBooleanvNative(pname, value);
        }
    }
    
    private native void alGetBooleanvNative(int pname, boolean[] value);

    public void alGetDoublev(int pname, double[] retValue) {
        if (retValue == null) {
            throw new IllegalArgumentException("Array must be non-null");
        } else {
            alGetDoublevNative(pname, retValue);
        }
    }

    private native void alGetDoublevNative(int pname, double[] retValue);

    public void alGetDoublev(int pname, DoubleBuffer value) {
        if ((value == null) || !value.isDirect()) {
            throw new IllegalArgumentException(
                "Buffer must be non-null and direct"
            );
        } else {
            alGetDoublevNative(pname, value);
        }
    }

    private native void alGetDoublevNative(int pname, DoubleBuffer value);

    public void alGetFloatv(int pname, float[] retValue) {
        if (retValue == null) {
            throw new IllegalArgumentException("Array must be non-null");
        } else {
            alGetFloatvNative(pname, retValue);
        }
    }

    private native void alGetFloatvNative(int pname, float[] retValue);

    public void alGetFloatv(int pname, FloatBuffer value) {
        if ((value == null) || !value.isDirect()) {
            throw new IllegalArgumentException(
                "Buffer must be non-null and direct"
            );
        } else {
            alGetFloatvNative(pname, value);
        }
    }

    private native void alGetFloatvNative(int pname, FloatBuffer value);

    public void alGetIntegerv(int pname, int[] retValue) {
        if (retValue == null) {
            throw new IllegalArgumentException("Array must be non-null");
        } else {
            alGetIntegervNative(pname, retValue);
        }
    }

    private native void alGetIntegervNative(int pname, int[] retValue);

    public void alGetIntegerv(int pname, IntBuffer value) {
        if ((value == null) || !value.isDirect()) {
            throw new IllegalArgumentException(
                "Buffer must be non-null and direct"
            );
        } else {
            alGetIntegervNative(pname, value);
        }
    }

    private native void alGetIntegervNative(int pname, IntBuffer value);

    public native String alGetString(int pname);

    public native void alDistanceModel(int model);

    public native void alDopplerFactor(float value);

    public native void alDopplerVelocity(float value);

    // ERROR RELATED METHODS
    public native int alGetError();

    // EXTENSION RELATED METHODS
    public native boolean alIsExtensionPresent(String extName);

    // public native Method getProcAddress(String methodName);
    public native int alGetEnumValue(String enumName);
    /* (non-Javadoc)
     * @see net.java.games.joal.AL#alGetBooleanv(int, boolean[])
     */

}
