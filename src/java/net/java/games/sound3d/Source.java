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

import net.java.games.joal.AL;


/**
 * DOCUMENT ME!
 *
 * @author Athomas Goldberg
 */
public final class Source {
    private final AL al;
    private final int sourceName;
    private Buffer buffer;

    Source(AL al, int sourceName) {
        this.al = al;
        this.sourceName = sourceName;
    }

    /**
     * DOCUMENT ME!
     */
    public void play() {
        al.alSourcePlay(sourceName);
    }

    /**
     * DOCUMENT ME!
     */
    public void pause() {
        al.alSourcePause(sourceName);
    }

    /**
     * DOCUMENT ME!
     */
    public void stop() {
        al.alSourceStop(sourceName);
    }

    /**
     * DOCUMENT ME!
     */
    public void rewind() {
        al.alSourceRewind(sourceName);
    }

    /**
     * DOCUMENT ME!
     */
    public void delete() {
        al.alDeleteSources(1, new int[] { sourceName });
    }

    /**
     * DOCUMENT ME!
     *
     * @param pitch DOCUMENT ME!
     */
    public void setPitch(float pitch) {
        al.alSourcef(sourceName, AL.AL_PITCH, pitch);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getPitch() {
        float[] result = new float[1];
        al.alGetSourcef(sourceName, AL.AL_PITCH, result);

        return result[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @param gain DOCUMENT ME!
     */
    public void setGain(float gain) {
        al.alSourcef(sourceName, AL.AL_GAIN, gain);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getGain() {
        float[] result = new float[1];
        al.alGetSourcef(sourceName, AL.AL_GAIN, result);

        return result[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @param maxDistance DOCUMENT ME!
     */
    public void setMaxDistance(float maxDistance) {
        al.alSourcef(sourceName, AL.AL_MAX_DISTANCE, maxDistance);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getMaxDistance() {
        float[] result = new float[1];
        al.alGetSourcef(sourceName, AL.AL_MAX_DISTANCE, result);

        return result[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @param rolloffFactor DOCUMENT ME!
     */
    public void setRolloffFactor(float rolloffFactor) {
        al.alSourcef(sourceName, AL.AL_ROLLOFF_FACTOR, rolloffFactor);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getRolloffFactor() {
        float[] result = new float[1];
        al.alGetSourcef(sourceName, AL.AL_ROLLOFF_FACTOR, result);

        return result[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @param referenceDistance DOCUMENT ME!
     */
    public void setReferenceDistance(float referenceDistance) {
        al.alSourcef(sourceName, AL.AL_REFERENCE_DISTANCE, referenceDistance);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getReferenceDistance() {
        float[] result = new float[1];
        al.alGetSourcef(sourceName, AL.AL_REFERENCE_DISTANCE, result);

        return result[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @param minGain DOCUMENT ME!
     */
    public void setMinGain(float minGain) {
        al.alSourcef(sourceName, AL.AL_MIN_GAIN, minGain);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getMinGain() {
        float[] result = new float[1];
        al.alGetSourcef(sourceName, AL.AL_MIN_GAIN, result);

        return result[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @param maxGain DOCUMENT ME!
     */
    public void setMaxGain(float maxGain) {
        al.alSourcef(sourceName, AL.AL_MAX_GAIN, maxGain);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getMaxGain() {
        float[] result = new float[1];
        al.alGetSourcef(sourceName, AL.AL_MAX_GAIN, result);

        return result[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @param coneOuterGain DOCUMENT ME!
     */
    public void setConeOuterGain(float coneOuterGain) {
        al.alSourcef(sourceName, AL.AL_CONE_OUTER_GAIN, coneOuterGain);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getConeOuterGain() {
        float[] result = new float[1];
        al.alGetSourcef(sourceName, AL.AL_CONE_OUTER_GAIN, result);

        return result[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @param position DOCUMENT ME!
     */
    public void setPosition(Vec3f position) {
        al.alSource3f(
            sourceName,
            AL.AL_POSITION,
            position.v1,
            position.v2,
            position.v3
        );
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param z DOCUMENT ME!
     */
    public void setPosition(float x, float y, float z) {
        al.alSource3f(sourceName, AL.AL_POSITION, x, y, z);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vec3f getPosition() {
        Vec3f result = null;
        float[] pos = new float[3];
        al.alGetSourcefv(sourceName, AL.AL_POSITION, pos);
        result = new Vec3f(pos[0], pos[1], pos[2]);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param velocity DOCUMENT ME!
     */
    public void setVelocity(Vec3f velocity) {
        al.alSource3f(
            sourceName,
            AL.AL_VELOCITY,
            velocity.v1,
            velocity.v2,
            velocity.v3
        );
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param z DOCUMENT ME!
     */
    public void setVelocity(float x, float y, float z) {
        al.alSource3f(sourceName, AL.AL_VELOCITY, x, y, z);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vec3f getVelocity() {
        Vec3f result = null;
        float[] vel = new float[3];
        al.alGetSourcefv(sourceName, AL.AL_VELOCITY, vel);
        result = new Vec3f(vel[0], vel[1], vel[2]);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param direction DOCUMENT ME!
     */
    public void setDirection(Vec3f direction) {
        al.alSource3f(
            sourceName,
            AL.AL_DIRECTION,
            direction.v1,
            direction.v2,
            direction.v3
        );
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param z DOCUMENT ME!
     */
    public void setDirection(float x, float y, float z) {
        al.alSource3f(sourceName, AL.AL_DIRECTION, x, y, z);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vec3f getDirection() {
        Vec3f result = null;
        float[] dir = new float[3];
        al.alGetSourcefv(sourceName, AL.AL_DIRECTION, dir);
        result = new Vec3f(dir[0], dir[1], dir[2]);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param isRelative DOCUMENT ME!
     */
    public void setSourceRelative(boolean isRelative) {
        int rel = isRelative ? 1 : 0;
        al.alSourcei(sourceName, AL.AL_SOURCE_RELATIVE, rel);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSourceRelative() {
        int[] result = new int[1];
        al.alGetSourcei(sourceName, AL.AL_SOURCE_RELATIVE, result);

        return result[0] == 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param isLooping DOCUMENT ME!
     */
    public void setLooping(boolean isLooping) {
        int loop = isLooping ? 1 : 0;
        al.alSourcei(sourceName, AL.AL_LOOPING, loop);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getBuffersQueued() {
        int[] result = new int[1];
        al.alGetSourcei(sourceName, AL.AL_BUFFERS_QUEUED, result);

        return result[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getBuffersProcessed() {
        int[] result = new int[1];
        al.alGetSourcei(sourceName, AL.AL_BUFFERS_PROCESSED, result);

        return result[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @param buffer DOCUMENT ME!
     */
    public void setBuffer(Buffer buffer) {
        al.alSourcei(sourceName, AL.AL_BUFFER, buffer.bufferID);
        this.buffer = buffer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Buffer getBuffer() {
        return buffer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param buffers DOCUMENT ME!
     */
    public void queueBuffers(Buffer[] buffers) {
        int numBuffers = buffers.length;
        int[] arr = new int[numBuffers];

        for (int i = 0; i < numBuffers; i++) {
            arr[i] = buffers[i].bufferID;
        }

        al.alSourceQueueBuffers(sourceName, numBuffers, arr);
    }

    /**
     * DOCUMENT ME!
     *
     * @param buffers DOCUMENT ME!
     */
    public void unqueueBuffers(Buffer[] buffers) {
        int numBuffers = buffers.length;
        int[] arr = new int[numBuffers];

        for (int i = 0; i < numBuffers; i++) {
            arr[i] = buffers[i].bufferID;
        }

        al.alSourceUnqueueBuffers(sourceName, numBuffers, arr);
    }
}
