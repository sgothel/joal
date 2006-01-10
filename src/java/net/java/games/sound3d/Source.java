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

package net.java.games.sound3d;

import net.java.games.joal.AL;

/**
 * This class is used to represent sound-producing objects in the Sound3D
 * environment. It contains methods for setting the position, direction, pitch,
 * gain and other properties along with methods for starting, pausing, rewinding
 * and stopping sudio projecting from a source.
 *
 * @author Athomas Goldberg
 */
public final class Source {
    private final AL al;
    private final int sourceID;
    private Buffer buffer;

    Source(AL al, int sourceID) {
        this.al = al;
        this.sourceID = sourceID;
    }

    /**
     * Beginning playing the audio in this source.
     */
    public void play() {
        al.alSourcePlay(sourceID);
    }

    /**
     * pauses the audio in this Source.
     */
    public void pause() {
        al.alSourcePause(sourceID);
    }

    /**
     * Stops the audio in this Source
     */
    public void stop() {
        al.alSourceStop(sourceID);
    }

    /**
     * Rewinds the audio in this source
     */
    public void rewind() {
        al.alSourceRewind(sourceID);
    }

    /**
     * Delete this source, freeing its resources.
     */
    public void delete() {
        al.alDeleteSources(1, new int[] { sourceID }, 0);
    }

    /**
     * Sets the pitch of the audio on this source. The pitch may be modified
     * without altering the playback speed of the audio.
     *
     * @param pitch the pitch value of this source.
     */
    public void setPitch(float pitch) {
        al.alSourcef(sourceID, AL.AL_PITCH, pitch);
    }

    /**
     * Gets the pitch of the audio on this source. The pitch may be modified
     * without altering the playback speed of the audio.
     *
     * @return the pitch value of this source.
     */
    public float getPitch() {
        float[] result = new float[1];
        al.alGetSourcef(sourceID, AL.AL_PITCH, result, 0);

        return result[0];
    }

    /**
     * Sets the gain of the audio on this source. This can be used to contro
     * the volume of the source.
     *
     * @param gain the gain of the audio on this source
     */
    public void setGain(float gain) {
        al.alSourcef(sourceID, AL.AL_GAIN, gain);
    }

    /**
     * Gets the gain of the audio on this source. This can be used to contro
     * the volume of the source.
     *
     * @return the gain of the audio on this source
     */
    public float getGain() {
        float[] result = new float[1];
        al.alGetSourcef(sourceID, AL.AL_GAIN, result, 0);

        return result[0];
    }

    /**
     * Sets the max distance where there will no longer be any attenuation of
     * the source.
     *
     * @param maxDistance the max ditance for source attentuation.
     */
    public void setMaxDistance(float maxDistance) {
        al.alSourcef(sourceID, AL.AL_MAX_DISTANCE, maxDistance);
    }

    /**
     * Gets the max distance where there will no longer be any attenuation of
     * the source.
     *
     * @return the max ditance for source attentuation.
     */
    public float getMaxDistance() {
        float[] result = new float[1];
        al.alGetSourcef(sourceID, AL.AL_MAX_DISTANCE, result, 0);

        return result[0];
    }

    /**
     * Sets the rolloff rate of the source. The default value is 1.0
     *
     * @param rolloffFactor the rolloff rate of the source.
     */
    public void setRolloffFactor(float rolloffFactor) {
        al.alSourcef(sourceID, AL.AL_ROLLOFF_FACTOR, rolloffFactor);
    }

    /**
     * Gets the rolloff rate of the source. The default value is 1.0
     *
     * @return the rolloff rate of the source.
     */
    public float getRolloffFactor() {
        float[] result = new float[1];
        al.alGetSourcef(sourceID, AL.AL_ROLLOFF_FACTOR, result, 0);

        return result[0];
    }

    /**
     * Sets the distance under which the volume for the source would normally
     * drop by half, before being influenced by rolloff factor or max distance.
     *
     * @param referenceDistance the reference distance for the source.
     */
    public void setReferenceDistance(float referenceDistance) {
        al.alSourcef(sourceID, AL.AL_REFERENCE_DISTANCE, referenceDistance);
    }

    /**
     * Gets the distance under which the volume for the source would normally
     * drop by half, before being influenced by rolloff factor or max distance.
     *
     * @return the reference distance for the source.
     */
    public float getReferenceDistance() {
        float[] result = new float[1];
        al.alGetSourcef(sourceID, AL.AL_REFERENCE_DISTANCE, result, 0);

        return result[0];
    }

    /**
     * Sets the minimum gain for this source.
     *
     * @param minGain the minimum gain for this source.
     */
    public void setMinGain(float minGain) {
        al.alSourcef(sourceID, AL.AL_MIN_GAIN, minGain);
    }

    /**
     * Gets the minimum gain for this source.
     *
     * @return the minimum gain for this source.
     */
    public float getMinGain() {
        float[] result = new float[1];
        al.alGetSourcef(sourceID, AL.AL_MIN_GAIN, result, 0);

        return result[0];
    }

    /**
     * Sets the maximum gain for this source.
     *
     * @param maxGain the maximum gain for this source
     */
    public void setMaxGain(float maxGain) {
        al.alSourcef(sourceID, AL.AL_MAX_GAIN, maxGain);
    }

    /**
     * SGets the maximum gain for this source.
     *
     * @return the maximum gain for this source
     */
    public float getMaxGain() {
        float[] result = new float[1];
        al.alGetSourcef(sourceID, AL.AL_MAX_GAIN, result, 0);

        return result[0];
    }

    /**
     * Sets the gain when outside the oriented cone.
     *
     * @param coneOuterGain the gain when outside the oriented cone.
     */
    public void setConeOuterGain(float coneOuterGain) {
        al.alSourcef(sourceID, AL.AL_CONE_OUTER_GAIN, coneOuterGain);
    }

    /**
     * Gets the gain when outside the oriented cone.
     *
     * @return the gain when outside the oriented cone.
     */
    public float getConeOuterGain() {
        float[] result = new float[1];
        al.alGetSourcef(sourceID, AL.AL_CONE_OUTER_GAIN, result, 0);

        return result[0];
    }

    /**
     * Sets the x,y,z position of the source.
     *
     * @param position a Vec3f object containing the x,y,z position of the
     * source.
     */
    public void setPosition(Vec3f position) {
        al.alSource3f(
            sourceID,
            AL.AL_POSITION,
            position.v1,
            position.v2,
            position.v3);
    }

    /**
     * Sets the x,y,z position of the source.
     *
     * @param x the x position of the source.
     * @param y the y position of the source.
     * @param z the z position of the source.
     */
    public void setPosition(float x, float y, float z) {
        al.alSource3f(sourceID, AL.AL_POSITION, x, y, z);
    }

    /**
     * Gets the x,y,z position of the source.
     *
     * @return a Vec3f object containing the x,y,z position of the
     * source.
     */
    public Vec3f getPosition() {
        Vec3f result = null;
        float[] pos = new float[3];
        al.alGetSourcefv(sourceID, AL.AL_POSITION, pos, 0);
        result = new Vec3f(pos[0], pos[1], pos[2]);

        return result;
    }

    /**
     * Sets the velocity vector of the source.
     *
     * @param velocity the velocity vector of the source
     */
    public void setVelocity(Vec3f velocity) {
        al.alSource3f(
            sourceID,
            AL.AL_VELOCITY,
            velocity.v1,
            velocity.v2,
            velocity.v3);
    }

    /**
     * Sets the velocity vector of the source.
     *
     * @param x the x velocity of the source.
     * @param y the y velocity of the source.
     * @param z the z velocity of the source.
     */
    public void setVelocity(float x, float y, float z) {
        al.alSource3f(sourceID, AL.AL_VELOCITY, x, y, z);
    }

    /**
     * Gets the velocity vector of the source.
     *
     * @return the velocity vector of the source
     */
    public Vec3f getVelocity() {
        Vec3f result = null;
        float[] vel = new float[3];
        al.alGetSourcefv(sourceID, AL.AL_VELOCITY, vel, 0);
        result = new Vec3f(vel[0], vel[1], vel[2]);

        return result;
    }

    /**
     * Sets the direction vector of the source.
     *
     * @param direction the direction vector of the source.
     */
    public void setDirection(Vec3f direction) {
        al.alSource3f(
            sourceID,
            AL.AL_DIRECTION,
            direction.v1,
            direction.v2,
            direction.v3);
    }

    /**
     * Sets the direction vector of the source.
     *
     * @param x the x direction of the source.
     * @param y the y direction of the source.
     * @param z the z direction of the source.
     */
    public void setDirection(float x, float y, float z) {
        al.alSource3f(sourceID, AL.AL_DIRECTION, x, y, z);
    }

    /**
     * Gets the direction vector of the source.
     *
     * @return the direction vector of the source.
     */
    public Vec3f getDirection() {
        Vec3f result = null;
        float[] dir = new float[3];
        al.alGetSourcefv(sourceID, AL.AL_DIRECTION, dir, 0);
        result = new Vec3f(dir[0], dir[1], dir[2]);

        return result;
    }

    /**
     * Determines if the position of the source is relative to the listener. 
     * The default is false.
     * @param isRelative true if the position of the source is relative
     * to the listener, false if the position of the source is relative to the 
     * world.
     */
    public void setSourceRelative(boolean isRelative) {
        int rel = isRelative ? 1 : 0;
        al.alSourcei(sourceID, AL.AL_SOURCE_RELATIVE, rel);
    }

    /**
     * Determines if the position of the source is relative to the listener. 
     * The default is false.
     * @return true if the position of the source is relative
     * to the listener, false if the position of the source is relative to the 
     * world.
     */
    public boolean isSourceRelative() {
        int[] result = new int[1];
        al.alGetSourcei(sourceID, AL.AL_SOURCE_RELATIVE, result, 0);

        return result[0] == 1;
    }

    /**
     * turns looping on or off. 
     *
     * @param isLooping true-looping is on, false-looping is off
     */
    public void setLooping(boolean isLooping) {
        int loop = isLooping ? 1 : 0;
        al.alSourcei(sourceID, AL.AL_LOOPING, loop);
    }

    /**
     * indicates whether looping is turned on or off. 
     *
     * @return true-looping is on, false-looping is off
     */
    public boolean getLooping() {
        boolean result = false;
        int[] tmp = new int[1];
        al.alGetSourcei(sourceID, AL.AL_LOOPING, tmp, 0);
        return tmp[0] == AL.AL_TRUE;
    }

    
    /**
     * Gets the number of buffers currently queued on this source. 
     * @return the number of buffers currently queued on this source.
     */
    public int getBuffersQueued() {
        int[] result = new int[1];
        al.alGetSourcei(sourceID, AL.AL_BUFFERS_QUEUED, result, 0);

        return result[0];
    }

    /**
     * Gets the number of buffers already processed on this source. 
     * @return the number of buffers already processed on this source.
     */
    public int getBuffersProcessed() {
        int[] result = new int[1];
        al.alGetSourcei(sourceID, AL.AL_BUFFERS_PROCESSED, result, 0);

        return result[0];
    }

    /**
     * Sets the buffer associated with this source.
     *
     * @param buffer the buffer associated with this source
     */
    public void setBuffer(Buffer buffer) {
        al.alSourcei(sourceID, AL.AL_BUFFER, buffer.bufferID);
        this.buffer = buffer;
    }

    /**
     * Gets the buffer associated with this source.
     *
     * @return the buffer associated with this source
     */
    public Buffer getBuffer() {
        return buffer;
    }

    /**
     * Queues one or more buffers on a source. Useful for streaming audio, 
     * buffers will be played in the order they are queued.
     *
     * @param buffers a set of initialized (loaded) buffers.
     */
    public void queueBuffers(Buffer[] buffers) {
        int numBuffers = buffers.length;
        int[] arr = new int[numBuffers];

        for (int i = 0; i < numBuffers; i++) {
            arr[i] = buffers[i].bufferID;
        }

        al.alSourceQueueBuffers(sourceID, numBuffers, arr, 0);
    }

    /**
     * Unqueues one or more buffers on a source.
     *
     * @param buffers a set of previously queued buffers.
     */
    public void unqueueBuffers(Buffer[] buffers) {
        int numBuffers = buffers.length;
        int[] arr = new int[numBuffers];

        for (int i = 0; i < numBuffers; i++) {
            arr[i] = buffers[i].bufferID;
        }

        al.alSourceUnqueueBuffers(sourceID, numBuffers, arr, 0);
    }
}
