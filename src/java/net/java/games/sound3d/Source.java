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
 * @author Athomas Goldberg
 *
 */
public final class Source {
    private final AL al;
    private final int sourceName;
    private Buffer buffer;

    Source(
        AL al,
        int sourceName) {
        this.al = al;
        this.sourceName = sourceName;
    }

    public void play() {
        al.alSourcePlay(sourceName);
    }

    public void pause() {
        al.alSourcePause(sourceName);
    }

    public void stop() {
        al.alSourceStop(sourceName);
    }

    public void rewind() {
        al.alSourceRewind(sourceName);
    }

    public void delete() {
        al.alDeleteSources(1, new int[] { sourceName });
    }

    public void setPitch(float pitch) {
        al.alSourcef(sourceName, AL.AL_PITCH, pitch);
    }

    public float getPitch() {
    	float[] result = new float[1];
        al.alGetSourcef(sourceName, AL.AL_PITCH,result);
        return result[0];
    }

    public void setGain(float gain) {
        al.alSourcef(sourceName, AL.AL_GAIN, gain);
    }

    public float getGain() {
    	float[] result = new float[1];
        al.alGetSourcef(sourceName, AL.AL_GAIN, result);
        return result[0];
    }

    public void setMaxDistance(float maxDistance) {
        al.alSourcef(sourceName, AL.AL_MAX_DISTANCE, maxDistance);
    }

    public float getMaxDistance() {
    	float[] result = new float[1];
        al.alGetSourcef(sourceName, AL.AL_MAX_DISTANCE,result);
        return result[0];
    }

    public void setRolloffFactor(float rolloffFactor) {
        al.alSourcef(sourceName, AL.AL_ROLLOFF_FACTOR, rolloffFactor);
    }

    public float getRolloffFactor() {
    	float[] result = new float[1];
        al.alGetSourcef(sourceName, AL.AL_ROLLOFF_FACTOR, result);
        return result[0];
    }

    public void setReferenceDistance(float referenceDistance) {
        al.alSourcef(sourceName, AL.AL_REFERENCE_DISTANCE, referenceDistance);
    }

    public float getReferenceDistance() {
    	float[] result = new float[1];
        al.alGetSourcef(sourceName, AL.AL_REFERENCE_DISTANCE, result);
        return result[0];
    }

    public void setMinGain(float minGain) {
        al.alSourcef(sourceName, AL.AL_MIN_GAIN, minGain);
    }

    public float getMinGain() {
    	float[] result = new float[1];
        al.alGetSourcef(sourceName, AL.AL_MIN_GAIN, result);
        return result[0];
    }

    public void setMaxGain(float maxGain) {
        al.alSourcef(sourceName, AL.AL_MAX_GAIN, maxGain);
    }

    public float getMaxGain() {
    	float[] result = new float[1];
        al.alGetSourcef(sourceName, AL.AL_MAX_GAIN, result);
        return result[0];
    }

    public void setConeOuterGain(float coneOuterGain) {
        al.alSourcef(sourceName, AL.AL_CONE_OUTER_GAIN, coneOuterGain);
    }

    public float getConeOuterGain() {
    	float[] result = new float[1];
        al.alGetSourcef(sourceName, AL.AL_CONE_OUTER_GAIN, result);
        return result[0];
    }

    public void setPosition(Vec3f position) {
        al.alSource3f(
            sourceName,
            AL.AL_POSITION,
            position.v1,
            position.v2,
            position.v3);
    }

    public void setPosition(
        float x,
        float y,
        float z) {
        al.alSource3f(sourceName, AL.AL_POSITION, x, y, z);
    }

    public Vec3f getPosition() {
        Vec3f result = null;
        float[] pos = new float[3];
        al.alGetSourcefv(sourceName, AL.AL_POSITION, pos);
        result = new Vec3f(pos[0], pos[1], pos[2]);
        return result;
    }

    public void setVelocity(Vec3f velocity) {
        al.alSource3f(
            sourceName,
            AL.AL_VELOCITY,
            velocity.v1,
            velocity.v2,
            velocity.v3);
    }

    public void setVelocity(
        float x,
        float y,
        float z) {
        al.alSource3f(sourceName, AL.AL_VELOCITY, x, y, z);
    }

    public Vec3f getVelocity() {
        Vec3f result = null;
        float[] vel = new float[3];
        al.alGetSourcefv(sourceName, AL.AL_VELOCITY, vel);
        result = new Vec3f(vel[0], vel[1], vel[2]);

        return result;
    }

    public void setDirection(Vec3f direction) {
        al.alSource3f(
            sourceName,
            AL.AL_DIRECTION,
            direction.v1,
            direction.v2,
            direction.v3);
    }

    public void setDirection(
        float x,
        float y,
        float z) {
        al.alSource3f(sourceName, AL.AL_DIRECTION, x, y, z);
    }

    public Vec3f getDirection() {
        Vec3f result = null;
        float[] dir = new float[3];
        al.alGetSourcefv(sourceName, AL.AL_DIRECTION, dir);
        result = new Vec3f(dir[0], dir[1], dir[2]);

        return result;
    }

    public void setSourceRelative(boolean isRelative) {
        int rel = isRelative ? 1 : 0;
        al.alSourcei(sourceName, AL.AL_SOURCE_RELATIVE, rel);
    }

    public boolean isSourceRelative() {
    	int[] result = new int[1];
        al.alGetSourcei(sourceName, AL.AL_SOURCE_RELATIVE, result);
        return result[0] == 1;
    }

    public void setLooping(boolean isLooping) {
        int loop = isLooping ? 1 : 0;
        al.alSourcei(sourceName, AL.AL_LOOPING, loop);
    }

    public int getBuffersQueued() {
    	int[] result = new int[1];
        al.alGetSourcei(sourceName, AL.AL_BUFFERS_QUEUED, result);
        return result[0];
    }

    public int getBuffersProcessed() {
    	int[] result = new int[1];
        al.alGetSourcei(sourceName, AL.AL_BUFFERS_PROCESSED, result);
        return result[0];
    }

    public void setBuffer(Buffer buffer) {
        al.alSourcei(sourceName, AL.AL_BUFFER, buffer.bufferName);
        this.buffer = buffer;
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public void queueBuffers(Buffer[] buffers) {
        int numBuffers = buffers.length;
        int[] arr = new int[numBuffers];

        for (int i = 0; i < numBuffers; i++) {
            arr[i] = buffers[i].bufferName;
        }

        al.alSourceQueueBuffers(sourceName, numBuffers, arr);
    }

    public void unqueueBuffers(Buffer[] buffers) {
        int numBuffers = buffers.length;
        int[] arr = new int[numBuffers];

        for (int i = 0; i < numBuffers; i++) {
            arr[i] = buffers[i].bufferName;
        }
        al.alSourceUnqueueBuffers(sourceName, numBuffers, arr);
    }
}
