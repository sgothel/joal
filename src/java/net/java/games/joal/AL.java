/*
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


/**
 * This class contains the core OpenAL functions
 *
 * @author Athomas Goldberg
 */
public interface AL extends ALConstants {
    // AL_BUFFER RELATED METHODS

    /**
     * This method generates one or more buffers.  <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid alGenBuffers(ALsizei n, ALuint *buffers);</pre>
     *
     * @param n the number of buffers to be generated
     * @param buffers an IntBuffer to contain the ids of the new buffers.
     *        IntBuffer must be a direct, non-null buffer, and buffer capacity
     *        must be equal to or greater than the number of buffers to be
     *        generated. Use BufferUtils.newIntBuffer(int capacity) to create
     *        an appropriate buffer.
     */
//    public void alGenBuffers(int n, IntBuffer buffers);

    /**
     * This method generates one or more buffers.  <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid alGenBuffers(ALsizei n, ALuint *buffers);</pre>
     *
     * @param n the number of buffers to be generated
     * @param buffers an int array to hold the IDs of the new buffers. Array
     *        must be non-null, and length must be equal to or greater than
     *        the number of buffers to be generated.
     */
    public void alGenBuffers(int n, int[] buffers);

    /**
     * This method deletes one or more buffers.  <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid alDeleteBuffers(ALsizei n, ALuint *buffers);</pre>
     *
     * @param n number of buffers to be deleted.
     * @param buffers a direct, non-null IntBuffer containing buffer names to
     *        be deleted.
     */
//    public void alDeleteBuffers(int n, IntBuffer buffers);

    /**
     * This method deletes one or more buffers.  <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid alDeleteBuffers(ALsizei n, ALuint *buffers);</pre>
     *
     * @param n number of buffers to be deleted.
     * @param buffers a direct, non-null IntBuffer containing buffer names to
     *        be deleted.
     */
    public void alDeleteBuffers(int n, int[] buffers);

    /**
     * This method tests if a buffer id is valid.  <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALboolean alIsBuffer(ALuint buffer);</pre>
     *
     * @param bufferID the name of the buffer.
     *
     * @return true if the buffer ID is valid
     */
    public boolean alIsBuffer(int bufferID);

    /**
     * This method fills a buffer with audio data.  <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid alBufferData(ALuint buffer, ALenum format, ALvoid *data, ALsizei size, ALsizei freq);</pre>
     *
     * @param bufferID name of buffer to be filled with data
     * @param format the format type from among the following: AL_MONO8, AL_MONO16,
     *        AL_STEREO8, AL_STEREO16.
     * @param data the audio data, must be non-null array
     * @param frequency the frequency of the audio data
     */
    public void alBufferData(
        int bufferID,
        int format,
        byte[] data,
        int size,
        int frequency
    );

    /**
     * This method fills a buffer with audio data.  <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid alBufferData(ALuint buffer, ALenum format, ALvoid *data, ALsizei size, ALsizei freq);</pre>
     *
     * @param bufferID name of buffer to be filled with data
     * @param format the format type from among the following: AL_MONO8, AL_MONO16,
     *        AL_STEREO8, AL_STEREO16.
     * @param data the audio data Must be non-null, direct ByteBuffer
     * @param frequency the frequency of the audio data
     */
    public void alBufferData(
        int bufferID,
        int format,
        ByteBuffer data,
        int size,
        int frequency
    );

    /**
     * This method retrieves a floating point property of a buffer.  <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid alGetBufferf(ALuint buffer, ALuint pname, float value);</pre>
     * <br><br>
     *
     * @param bufferID Buffer ID whose attribute is being retrieved
     * @param pname the name of the attribute to be retrieved
     * @param retValue a single-element array to hold the retrieved value.
     */
    public void alGetBufferf(int bufferID, int pname, float[] retValue);

    /**
     * This method retrieves a floating point property of a buffer.  <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid alGetBufferf(ALuint buffer, ALuint pname, ALfloat *value);</pre>
     *
     * @param bufferID Buffer ID whose attribute is being retrieved
     * @param pname the name of the attribute to be retrieved
     * @param retValue a single-element buffer to hold the retrieved value.
     */
//    public void alGetBufferf(int bufferID, int pname, FloatBuffer retValue);

    /**
     * This method retrieves a floating point property of a buffer.  <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid alGetBufferf(ALuint buffer, ALuint pname, ALfloat *value);</pre>
     *
     * @param bufferID Buffer ID whose attribute is being retrieved
     * @param pname the name of the attribute to be retrieved
     *
     * @return retValue the retrieved value.
     */
    public float alGetBufferf(int bufferID, int pname);

    /**
     * This method retrieves a integer property of a buffer.  <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid alGetBufferi(ALuint buffer, ALuint pname, ALint *value);</pre>
     *
     * @param bufferID Buffer ID whose attribute is being retrieved
     * @param pname the name of the attribute to be retrieved
     * @param retValue a single-element array to hold the retrieved value.
     */
    public void alGetBufferi(int bufferID, int pname, int[] retValue);

    /**
     * This method retrieves a integer property of a buffer.  <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid alGetBufferi(ALuint buffer, ALuint pname, ALint value);</pre>
     *
     * @param bufferID Buffer ID whose attribute is being retrieved
     * @param pname the name of the attribute to be retrieved
     * @param retValue a single-element IntBuffer to hold the retrieved value.
     */
//    public void alGetBufferi(int bufferID, int pname, IntBuffer retValue);

    /**
     * This method retrieves a integer property of a buffer.  <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid alGetBufferi(ALuint buffer, ALuint pname, ALint *value);</pre>
     *
     * @param bufferID Buffer ID whose attribute is being retrieved
     * @param pname the name of the attribute to be retrieved
     *
     * @return retValue the retrieved value.
     */
    public int alGetBufferi(int bufferID, int pname);

    // SOURCE RELATED METHODS

    /**
     * This method generates one or more sources.  <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid alGenSources(ALsizei n, ALuint *sources);</pre>
     *
     * @param numSources the number of sources to be generated
     * @param sources an integer array to hold the ids of the new sources
     */
    public void alGenSources(int numSources, int[] sources);

    /**
     * This method generates one or more sources.  <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid alGenSources(ALsizei n, ALuint *sources);</pre>
     *
     * @param numSources the number of sources to be generated
     * @param sources an IntBuffer to hold the IDs of the new sources
     */
//    public void alGenSources(int numSources, IntBuffer sources);

    /**
     * This method deletes one or more sources.  <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid alDeleteSources(ALsizei n, ALuint *sources);</pre>
     *
     * @param numSources the number of sources to be generated
     * @param sources an int array containing the IDs of the sources to be
     *        deleted
     */
    public void alDeleteSources(int numSources, int[] sources);

    /**
     * This method deletes one or more sources.  <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid alDeleteSources(ALsizei n, ALuint *sources);</pre>
     *
     * @param numSources the number of sources to be generated
     * @param sources an IntBuffer containing the IDs of the sources to be
     *        deleted
     */
//    public void alDeleteSources(int numSources, IntBuffer sources);

    /**
     * This method tests if a source ID is valid. <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid alDeleteSources(ALsizei n, ALuint *sources);</pre>
     *
     * @param sourceID a source ID to be tested for validity
     *
     * @return true if the source ID is valid, or false if the source ID is not
     *         valid
     */
    public boolean alIsSource(int sourceID);

    /**
     * This method sets a floating point property of a source. <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid alSourcei(ALuint sourceID, ALuint pname, ALfloat value);</pre>
     *
     * @param sourceID source ID whose attribute is being set
     * @param pname the name of the attribute to set:
     * <pre>
     *     AL_PITCH
     *     AL_GAIN
     *     AL_MAX_DISTANCE
     *     AL_ROLLOFF_FACTOR
     *     AL_REFERENCE_DISTANCE
     *     AL_MIN_GAIN
     *     AL_MAX_GAIN
     *     AL_CONE_OUTER_GAIN
     * </pre>
     * @param value the value to set the attribute to
     */
    public void alSourcef(int sourceID, int pname, float value);

    /**
     * This method sets a floating-point vector property of a source. <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid aSourcefv(ALuint source, ALenum pname, ALfloat *values)</pre>
     *
     * @param sourceID source ID whose attribute is being set
     * @param pname the nameof the attribute to set:
     * <pre>
     *      AL_POSITION
     *      AL_VELOCITY
     *      AL_DIRECTION
     * </pre>
     * @param value a float array containing the vector to set the attribute to.
     */
    public void alSourcefv(int sourceID, int pname, float[] value);

    /**
     * This method sets a floating-point vector property of a source. <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid aSourcefv(ALuint source, ALenum pname, ALfloat *values)</pre>
     *
     * @param sourceID source ID whose attribute is being set
     * @param pname the nameof the attribute to set:
     * <pre>
     *      AL_POSITION
     *      AL_VELOCITY
     *      AL_DIRECTION
     * </pre>
     * @param value direct FloatBuffer containing the vector to set the attribute to.
     */
//    public void alSourcefv(int sourceID, int pname, FloatBuffer value);


    /**
     * This method sets a source property requiring three floating point values. <br>
     * <br>
     * <b>Interface to C lanuage function:</b>
     * <pre>ALvoid alSource3f (ALuint source, ALenum pname, ALfloat v1, ALfloat v2, ALfloat v3);</pre>
     * 
     * @param sourceID the id of the source whose atribute is being set.
     * @param pname the name of the attribute being set.
     * <pre>
     *      AL_POSITION
     *      AL_VELOCITY
     *      AL_DIRECTION
     * </pre>
     * @param v1 the first float value which the attribute will be set to
     * @param v2 the second float value which the attribute will be set to
     * @param v3 the third float value which the attribute will be set to
     */
    public void alSource3f(
        int sourceID,
        int pname,
        float v1,
        float v2,
        float v3
    );

    /**
     * This method sets a integer property of a source. <br>
     * <br>
     * <b>Interface to C language function:</b>
     * <pre>ALvoid aSourcei(ALuint source, ALenum pname, ALint value)</pre>
     *
     * @param sourceID source ID whose attribute is being set
     * @param pname the nameof the attribute to set:
     * <pre>
     *      AL_SOURCE_RELATIVE
     *      AL_LOOPING
     *      AL_BUFFER
     *      AL_SOURCE_STATE
     * </pre>
     * @param value the int value to set the attribute to.
     */
    public void alSourcei(int sourceID, int pname, int value);

    /**
     * This methof retrieves a floating point property of a source. <br>
     * <br>
     * <b>Interface to C language unction:</b>
     * <pre>ALvoid alGetSourcef(ALuint source, ALenum pname, ALfloat *value);</pre>
     *
     * @param sourceID the id of the source whose attribute is being retrieved.
     * @param pname he name of the attribute to retrieve
     * <pre>
     *      AL_PITCH
     *      AL_GAIN
     *      AL_MIN_GAIN
     *      AL_MAX_GAIN
     *      AL_MAX_DISTANCE
     *      AL_ROLLOFF_DISTANCE
     *      AL_CONE_OUTER_GAIN
     *      AL_CONE_INNER_ANGLE
     *      AL_CONE_OUTER_ANGLE
     *      AL_REFERENCE_DISTANCE
     * </pre>
     * @param retValue a single-element float array to hold the value being retrieved.
     */
    public void alGetSourcef(int sourceID, int pname, float[] retValue);

    /**
     * This methof retrieves a floating point property of a source. <br>
     * <br>
     * <b>Interface to C language unction:</b>
     * <pre>ALvoid alGetSourcef(ALuint source, ALenum pname, ALfloat *value);</pre>
     *
     * @param sourceID the id of the source whose attribute is being retrieved.
     * @param pname he name of the attribute to retrieve
     * <pre>
     *      AL_PITCH
     *      AL_GAIN
     *      AL_MIN_GAIN
     *      AL_MAX_GAIN
     *      AL_MAX_DISTANCE
     *      AL_ROLLOFF_DISTANCE
     *      AL_CONE_OUTER_GAIN
     *      AL_CONE_INNER_ANGLE
     *      AL_CONE_OUTER_ANGLE
     *      AL_REFERENCE_DISTANCE
     * </pre>
     * @param buffer a direct FloatBuffer to hold the value being retrieved.
     */
//    public void alGetSourcef(int sourceID, int pname, FloatBuffer buffer);

    /**
     * This methof retrieves a floating point property of a source. <br>
     * <br>
     * <b>Interface to C language unction:</b>
     * <pre>ALvoid alGetSourcef(ALuint source, ALenum pname, ALfloat *value);</pre>
     *
     * @param sourceID the id of the source whose attribute is being retrieved.
     * @param pname he name of the attribute to retrieve
     * <pre>
     *      AL_PITCH
     *      AL_GAIN
     *      AL_MIN_GAIN
     *      AL_MAX_GAIN
     *      AL_MAX_DISTANCE
     *      AL_ROLLOFF_DISTANCE
     *      AL_CONE_OUTER_GAIN
     *      AL_CONE_INNER_ANGLE
     *      AL_CONE_OUTER_ANGLE
     *      AL_REFERENCE_DISTANCE
     * </pre>
     * @return the floating point value being retrieved.
     */
    public float alGetSourcef(int sourceID, int pname);

    /**
     * This method retrieves a floating point vector property of a source. <br>
     * <br>
     * <b>Interface to C language unction:</b>
     * <pre>ALvoid alGetSourcef(ALuint source, ALenum pname, ALfloat *value);</pre>
     * 
     *
     * @param sourceID the id of the source whose attribute is being retrieved.
     * @param pname the name of the attribute to retrieve
     * <pre>
     *      AL_POSITION
     *      AL_VELOCITY
     *      AL_DIRECTION
     * </pre>
     * @param value a direct FloatBuffer to hold the value being retrieved
     */
//    public void alGetSourcefv(int sourceID, int pname, FloatBuffer value);

    /**
     * This method retrieves a floating point vector property of a source. <br>
     * <br>
     * <b>Interface to C language unction:</b>
     * <pre>ALvoid alGetSourcef(ALuint source, ALenum pname, ALfloat *value);</pre>
     * 
     *
     * @param sourceID the id of the source whose attribute is being retrieved.
     * @param pname the name of the attribute to retrieve
     * <pre>
     *      AL_POSITION
     *      AL_VELOCITY
     *      AL_DIRECTION
     * </pre>
     * @param retValue a float array to hold the value being retrieved
     */
    public void alGetSourcefv(int sourceID, int pname, float[] retValue);

    /**
     * This method retrieves an integer property of a source. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alGetSourcei(ALuint source, Alenum pname, ALint *value);</pre>
     *
     * @param sourceID source id whose attribute is being retrieved.
     * @param pname the name of the attribute being retrieved.
     * <pre>
     *      AL_SOURCE_RELATIVE
     *      AL_BUFFER
     *      AL_LOOPING
     *      AL_SOURCE_STATE
     *      AL_BUFFERS_QUEUED
     *      AL_BUFFERS_PROCESSED
     * </pre>
     * @param retValue an int array to hold the value being retrieved
     */
    public void alGetSourcei(int sourceID, int pname, int[] retValue);

    /**
     * This method retrieves an integer property of a source. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alGetSourcei(ALuint source, Alenum pname, ALint *value);</pre>
     *
     * @param sourceID source id whose attribute is being retrieved.
     * @param pname the name of the attribute being retrieved.
     * <pre>
     *      AL_SOURCE_RELATIVE
     *      AL_BUFFER
     *      AL_LOOPING
     *      AL_SOURCE_STATE
     *      AL_BUFFERS_QUEUED
     *      AL_BUFFERS_PROCESSED
     * </pre>
     * @param retValue a direct IntBuffer to hold the value being retrieved
     */
//    public void alGetSourcei(int sourceID, int pname, IntBuffer retValue);

    /**
     * This method retrieves an integer property of a source. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alGetSourcei(ALuint source, Alenum pname, ALint *value);</pre>
     *
     * @param sourceID source id whose attribute is being retrieved.
     * @param pname the name of the attribute being retrieved.
     * <pre>
     *      AL_SOURCE_RELATIVE
     *      AL_BUFFER
     *      AL_LOOPING
     *      AL_SOURCE_STATE
     *      AL_BUFFERS_QUEUED
     *      AL_BUFFERS_PROCESSED
     * </pre>
     * @return the value being retrieved
     */
    public int alGetSourcei(int sourceID, int pname);

    /**
     * This method plays a source. <br>
     * <br>
     *<b>Interface to C Language function:</b>
     *<pre>ALvoid alSourcePlay(ALuint source);</pre>
     *
     * @param sourceID the id of the source to be played
     */
    public void alSourcePlay(int sourceID);

    /**
     * This method plays a set of sources. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alSourcePlayv(Alsizei, ALuint *sources);</pre>
     *
     * @param numSources the number of sources to be plaed
     * @param sourceIDs a direct IntBuffer containing the ids of the sources to be played.
     */
//    public void alSourcePlayv(int numSources, IntBuffer sourceIDs);

    /**
     * This method plays a set of sources. <br>
     * <br>
     * <pre>ALvoid alSourcePlayv(Alsizei, ALuint *sources);</pre>
     *
     * @param numSources the number of sources to be plaed
     * @param sourceIDs an array containing the ids of the sources to be played.
     */
    public void alSourcePlayv(int numSources, int[] sourceIDs);

    /**
     * This method pauses a source. <br>
     * <br>
     *<b>Interface to C Language function:</b>
     *<pre>ALvoid alSourcePause(ALuint source);</pre>
     *
     * @param sourceID the id of the source to be paused
     */
    public void alSourcePause(int sourceID);

    /**
     * This method pauses a set of sources. <br>
     * <br>
     * <pre>ALvoid alSourcePausev(Alsizei, ALuint *sources);</pre>
     *
     * @param numSources the number of sources to be paused
     * @param sourceIDs an array containing the ids of the sources to be paused.
     */
    public void alSourcePausev(int numSources, int[] sourceIDs);

    /**
     * This method pauses a set of sources. <br>
     * <br>
     * <pre>ALvoid alSourcePausev(Alsizei, ALuint *sources);</pre>
     *
     * @param numSources the number of sources to be paused
     * @param sourceIDs an IntBuffer containing the ids of the sources to be paused.
     */
//    public void alSourcePausev(int numSources, IntBuffer sourceIDs);

    /**
     * This method stops a source. <br>
     * <br>
     *<b>Interface to C Language function:</b>
     *<pre>ALvoid alSourceStop(ALuint source);</pre>
     *
     * @param sourceID the id of the source to be stopped
     */
    public void alSourceStop(int sourceID);

    /**
     * This method stops a set of sources. <br>
     * <br>
     * <pre>ALvoid alSourceStopv(Alsizei, ALuint *sources);</pre>
     *
     * @param numSources the number of sources to be stopped
     * @param sourceIDs an array containing the ids of the sources to be stopped.
     */
    public void alSourceStopv(int numSources, int[] sourceIDs);

    /**
     * This method stops a set of sources. <br>
     * <br>
     * <pre>ALvoid alSourceStopv(Alsizei, ALuint *sources);</pre>
     *
     * @param numSources the number of sources to be stopped
     * @param sourceIDs a direct IntBuffer containing the ids of the sources to be stopped.
     */
//    public void alSourceStopv(int numSources, IntBuffer sourceIDs);

    /**
     * This method rewinds a source. <br>
     * <br>
     *<b>Interface to C Language function:</b>
     *<pre>ALvoid alSourceRewind(ALuint source);</pre>
     *
     * @param sourceID the id of the source to be rewound
     */
    public void alSourceRewind(int sourceID);

    /**
     * This method rewinds a set of sources. <br>
     * <br>
     *<b>Interface to C Language function:</b>
     * <pre>ALvoid alSourceRewindv(Alsizei, ALuint *sources);</pre>
     *
     * @param numSources the number of sources to be rewound
     * @param sourceIDs an array containing the ids of the sources to be rewound.
     */
    public void alSourceRewindv(int numSources, int[] sourceIDs);

    /**
     * This method rewinds a set of sources. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alSourceRewindv(Alsizei, ALuint *sources);</pre>
     *
     * @param numSources the number of sources to be rewound
     * @param sourceIDs a direct IntBuffercontaining the ids of the sources to be rewound.
     */
//    public void alSourceRewindv(int numSources, IntBuffer sourceIDs);

    /**
     * This method queues a set of buffers on a source. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>alSourceQueueBuffers(ALuint source, ALsizei n, ALuint *buffers);</pre>
     * 
     * @param sourceID the id of the source to queue buffers onto
     * @param numBuffers the number of buffers to be queued
     * @param bufferIDs an array containing the list of buffer ids to be queued
     */
    public void alSourceQueueBuffers(
        int sourceID,
        int numBuffers,
        int[] bufferIDs
    );

    /**
     * This method queues a set of buffers on a source. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>alSourceQueueBuffers(ALuint source, ALsizei n, ALuint *buffers);</pre>
     * 
     * @param sourceID the id of the source to queue buffers onto
     * @param numBuffers the number of buffers to be queued
     * @param bufferIDs a direct IntBuffer containing the list of buffer ids to be queued
     *//*
    public void alSourceQueueBuffers(
        int sourceID,
        int numBuffers,
        IntBuffer bufferIDs
    );*/

    /**
     * This method unqueues a set of buffers attached to a source. 
     * The unqueue operation will only take place if all <i>n</i> buffers
     * can be removed from the queue<br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alSourceUnqueueBuffers(ALuint source, ALsizei n, ALuint *buffers);</pre>
     *
     * @param source the id of the source to unqueue buffers from
     * @param numBuffers the number of buffers to be unqueued
     * @param bufferIDs an array of buffer ids to be unqueued
     */
    public void alSourceUnqueueBuffers(
        int source,
        int numBuffers,
        int[] bufferIDs
    );

    /**
     * This method unqueues a set of buffers attached to a source. 
     * The unqueue operation will only take place if all <i>n</i> buffers
     * can be removed from the queue<br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alSourceUnqueueBuffers(ALuint source, ALsizei n, ALuint *buffers);</pre>
     *
     * @param source the id of the source to unqueue buffers from
     * @param numBuffers the number of buffers to be unqueued
     * @param bufferIDs a direct IntBuffer of buffer ids to be unqueued
     *//*
    public void alSourceUnqueueBuffers(
        int source,
        int numBuffers,
        IntBuffer bufferIDs
    );*/

    // LISTENER RELATED METHODS
    
    
    /**
     * This method sets a floating point property for the listener. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alListenerf(ALenum pname, ALfloat value);</pre>
     * 
     * @param pname the name of the attribute to be set
     * @param value the value to set the attribute to.
     */
    public void alListenerf(int pname, float value);

    /**
     * This method sets a listener property requireing 3 floating point values. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alListener3f(ALenum pname, ALfloat v1, ALfloat v2, ALfloat v3);</pre>
     *
     * @param pname the name of the attribute to be set:
     * <pre>
     *      AL_POSITION
     *      AL_VELOCITY
     *      AL_ORIENTATION
     * </pre>
     * @param v1 the first value to set the attribute to 
     * @param v2 the second value to set the attribute to
     * @param v3 the third value to set the attribute to
     */    
    public void alListener3f(int pname, float v1, float v2, float v3);

    /**
     * This method sets a floating point vector property of the listener. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alListenerfv(ALenum pname, ALfloat *values);</pre>
     *
     * @param pname the name of the attribute to be set:
     * <pre>
     *      AL_POSITION
     *      AL_VELOCITY
     *      AL_ORIENTATION 
     * </pre>
     * @param values a float array containng the value to set the attribute to
     */
    public void alListenerfv(int pname, float[] values);

    /**
     * This method sets a floating point vector property of the listener. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alListenerfv(ALenum pname, ALfloat *values);</pre>
     *
     * @param pname the name of the attribute to be set:
     * <pre>
     *      AL_POSITION
     *      AL_VELOCITY
     *      AL_ORIENTATION 
     * </pre>
     * @param values a direct FloatBuffer containng the value to set the attribute to
     */
//    public void alListenerfv(int pname, FloatBuffer values);

    /**
     * This method sets an integer property on the listener. 
     * Note: there are no integer listener attributes at this time.<br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alListeneri(ALenum pname, ALint value);</pre>
     *
     * @param pname the name of the attribute to set
     * @param value the value to set the attribute to.
     */
    public void alListeneri(int pname, int value);

    /**
     * This method retrieves a floating point property of the listener. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alGetListenerf(ALenum pname, ALfloat *value);</pre>
     *
     * @param pname the name of the attribute to be retrieved:
     * <pre>
     *      AL_GAIN
     * </pre>
     * @param retValue a single-element array to hold the retrieved value
     */
    public void alGetListenerf(int pname, float[] retValue);

    /**
     * This method retrieves a floating point property of the listener. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alGetListenerf(ALenum pname, ALfloat *value);</pre>
     *
     * @param pname the name of the attribute to be retrieved:
     * <pre>
     *      AL_GAIN
     * </pre>
     * @param retValue a direct FloatBuffer to hold the retrieved value
     */
//    public void alGetListenerf(int pname, FloatBuffer retValue);

    /**
     * This method retrieves a floating point property of the listener. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alListeneri(ALenum pname, ALfloat *value);</pre>
     *
     * @param pname the name of the attribute to be retrieved:
     * <pre>
     *      AL_GAIN
     * </pre>
     * @return the retrieved value
     */
    public float alGetListenerf(int pname);

    /**
     * This method retrieves a 3-element floating point property of the listener. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alGetListener3f(ALenum pname, ALfloat *v1, ALfloat *v2, ALfloat *v3);</pre>
     *
     * @param pname the name of the attribute to be retrieved:
     * <pre>
     *      AL_POSITION
     *      AL_VELOCITY
     * </pre>
     * 
     * @param v1 a FloatBuffer to hold the first value
     * @param v2 a FloatBuffer to hold the second value
     * @param v3 a FloatBuffer to hold the third value
     */
/*    public void alGetListener3f(
        int pname,
        FloatBuffer v1,
        FloatBuffer v2,
        FloatBuffer v3
    );
*/
    /**
     * This method retrieves a 3-element floating point property of the listener. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alGetListener3f(ALenum pname, ALfloat *v1, ALfloat *v2, ALfloat *v3);</pre>
     *
     * @param pname the name of the attribute to be retrieved:
     * <pre>
     *      AL_POSITION
     *      AL_VELOCITY
     * </pre>
     * 
     * @param v1 a single element array to hold the first value
     * @param v2 a single element array to hold the second value
     * @param v3 a single element array to hold the third value
     */
    public void alGetListener3f(int pname, float[] v1, float[] v2, float[] v3);

    /**
     * This method retrieves a floating point-vector property of the listener. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alGetListenerfv(ALenum pname, ALint *value);</pre>
     *
     * @param pname the nameof the atribute to be retrieved:
     * <pre>
     *      AL_POSITION
     *      AL_VELOCITY
     *      AL_ORIENTATION
     * </pre>
     * @param retValue an array to hold the retrieved value
     */
    public void alGetListenerfv(int pname, float[] retValue);

    /**
     * This method retrieves a floating point-vector property of the listener. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alGetListenerfv(ALenum pname, ALint *value);</pre>
     *
     * @param pname the nameof the atribute to be retrieved:
     * <pre>
     *      AL_POSITION
     *      AL_VELOCITY
     *      AL_ORIENTATION
     * </pre>
     * @param retValue a FloatBuffer to hold the retrieved value
     */
//    public void alGetListenerfv(int pname, FloatBuffer retValue);

    /**
     * This method retrieves an integer property of the listener. 
     * Note: there are no integer listener properties at this time.<br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alGetListeneri(ALenum pname, ALint *value);</pre>
     *
     * @param pname the nameof the attribute to be retrieved
     * @param retValue an int array to hold the retrieved value.
     */
    public void alGetListeneri(int pname, int[] retValue);

    /**
     * This method retrieves an integer property of the listener. <br>
     * Note: there are no integer listener properties at this time.<br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alGetListeneri(ALenum pname, ALint *value);</pre>
     *
     * @param pname the nameof the attribute to be retrieved
     * @param retValue an IntBuffer to hold the retrieved value.
     */
//    public void alGetListeneri(int pname, IntBuffer retValue);

    /**
     * This method retrieves an integer property of the listener. <br>
     * Note: there are no integer listener properties at this time.<br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alGetListeneri(ALenum pname, ALint *value);</pre>
     *
     * @param pname the nameof the attribute to be retrieved
     *
     * @return the retrieved value
     */
    public int alGetListeneri(int pname);

    // STATE RELATED METHODS
    
    /**
     * This method enables a feature of the OpenAL driver. Note: at this time
     * there are no features to be enabled with this feature.<br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alDisable(ALenum cpability);</pre>
     *
     * @param capability the name of the capbility to be enabled.
     */
    
    public void alEnable(int capability);

    /**
     * This method disables a feature of the OpenAL driver. Note: at this time
     * there are no features to be disabled with this feature.<br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alDisable(ALenum cpability);</pre>
     *
     * @param capability the name of the capbility to be disabled.
     */
    public void alDisable(int capability);

    /**
     * This method returns a bolean indicating if a specific feature is enabled
     * in the OpenAL driver. Note: At this time this function always returns
     * false, as there are no capabilities to be enabled<br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALboolean alIsEnabled(ALenum cpability);</pre>
     * 
     * @param capability the name of the capability to check
     *
     * @return true, if the capability is enabled,
     * false if the capability is disabled.
     */
    public boolean alIsEnabled(int capability);

    /**
     * This method returs a boolean OpenAL state. Note: there are no 
     * boolean state values at this time.<br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALboolean alGetBoolean(ALenum pname);</pre>
     *
     * @param pname the state to be queried
     *
     * @return the state described by pname
     */
    public boolean alGetBoolean(int pname);

    /**
     * This method returns a double precision loating point OpenAL state. 
     * Note at the time there are no double stat values.<br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALdouble alGetDouble(ALEnum pname);</pre>
     * 
     * @param pname the state to be queried
     *
     * @return the sate described by pname
     */
    public double alGetDouble(int pname);

    /**
     * This method returns a floating point OpenAL state. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALfoat alGetFloat(ALenum pname);</pre>
     *
     * @param pname the sateto be queried:
     * <pre>
     *      AL_DOPPLER_FACTOR
     *      AL_DOPPLER_VELOCITY
     * </pre>
     *
     * @return the state described by pname
     */
    public float alGetFloat(int pname);

    /**
     * This method returns an integer OpenAL state. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALint alGetInteger(ALenum pname);</pre>
     *
     * @param pname the name of the state to be queried:
     * <pre>
     *      AL_DISTANCE_MODEL
     * </pre>
     * @return the state described by pname
     */
    public int alGetInteger(int pname);

    // No Boolean Array states at the moment
    // public  void getBooleanv(int pname, ByteBuffer value);
    
    /**
     * This function retrieves a boolean OpenAL state. Note: at this time
     * there are no boolean state variables<br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alGetBooleanv(ALenum pname, ALboolean *value);</pre>
     *
     * @param pname the name of the state to be retrieved
     * @param value a single element array to hold the retrieved state
     */    
    public void alGetBooleanv(int pname, boolean[] value);
    
    /**
     * This method retrieves a double precision floating point OpenAL state. 
     * Note: there are no double precision floating point states at this time.
     * <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alGetDoublev(ALenum, ALdouble *value);</pre>
     *
     * @param pname the state to be retrieved
     * @param value a DoubleBuffer to hold the retrieved state
     */
//    public void alGetDoublev(int pname, DoubleBuffer value);

    /**
     * This method retrieves a double precision floating point OpenAL state. 
     * Note: there are no double precision floating point states at this time.
     * <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alGetDoublev(ALenum, ALdouble *value);</pre>
     *
     * @param pname the state to be retrieved
     * @param value a single element array to hold the retrieved state
     */
    public void alGetDoublev(int pname, double[] value);

    /**
     * This method returns a floating point OpenAL state. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alGetFloatv(ALenum pname, ALfloat *value);</pre>
     *
     * @param pname the state to be retrieved
     * <pre>
     *      AL_DOPPLER_FACTOR
     *      AL_DOPPLER_VELOCITY
     * </pre>
     * @param value a single element FloatBuffer to hold the retrieved value.
     */
//    public void alGetFloatv(int pname, FloatBuffer value);

    /**
     * This method returns a floating point OpenAL state. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alGetFloatv(ALenum pname, ALfloat *value);</pre>
     *
     * @param pname the state to be retrieved
     * <pre>
     *      AL_DOPPLER_FACTOR
     *      AL_DOPPLER_VELOCITY
     * </pre>
     * @param value a single element float array to hold the retrieved value.
     */
    public void alGetFloatv(int pname, float[] value);

    /**
     * This method returns an integer OpenAL state. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alGetIntegerv(ALenum pname, ALint *data);</pre>
     *
     * @param pname the state to be returned:
     * <pre>
     *      AL_DISTANCE_MODEL
     * </pre>
     * @param value a single-element IntBuffer to hold the retrieved value
     */
    //public void alGetIntegerv(int pname, IntBuffer value);

    /**
     * This method returns an integer OpenAL state. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alGetIntegerv(ALenum pname, ALint *data);</pre>
     *
     * @param pname the state to be returned:
     * <pre>
     *      AL_DISTANCE_MODEL
     * </pre>
     * @param value a single-element array to hold the retrieved value
     */
    public void alGetIntegerv(int pname, int[] value);

    /**
     * This method retrieves an OpenAL string property. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALubyte* alGetString(int pname);</pre>
     *
     * @param pname the name of the state to be retrieved
     *
     * @return the retrieved state
     */
    public String alGetString(int pname);

    /**
     * This method selects the OpenAL distance model.
     * The default distance model is AL_INVERSE_DISTANCE<br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alDistanceModel(ALenum value);</pre>
     *
     * @param model the distance model to set:
     * <pre>
     *      AL_NONE
     *      AL_INVERSE_DISTANCE
     *      AL_INVERSE_DISTANCE_CLAMPED
     * </pre>
     */
    public void alDistanceModel(int model);

    /**
     * This method selects the OpenAL Doppler factor value.
     * The default value is 1.0<br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alDopplerFactor(ALfloat value);</pre>
     *
     * @param value the Doppler scale value to set
     */
    public void alDopplerFactor(float value);

    /**
     * This method selects the OpenAL Doppler velocity value.
     * The default Doppler velocity value is 343.0<b>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alDopplerVelocity(ALfloat value);</pre>
     *
     * @param value The Doppler velocity value to set.
     */
    public void alDopplerVelocity(float value);

    // ERROR RELATED METHODS
    
    /**
     * This method returns the current error state and then clears the
     * error state. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALenum alGetError(ALvoid);</pre>
     * 
     * @return the current error state
     */
    public int alGetError();

    // EXTENSION RELATED METHODS
    /**
     * This ehod tests is a specific extension is available
     * for the OpenAL driver. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALboolean alIsExtensionPresent(ALubyte *extName);</pre>
     * 
     * @param extName a string describing the desired extension
     * 
     * @return true  if the extension is available, 
     * false if the extension is not available.
     */
    public boolean alIsExtensionPresent(String extName);

    // public  Method getProcAddress(String methodName);
    /**
     * This method returns the enumeration value of an OpenAL enum 
     * described by a string. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALenum alGetEnumValue(ALubyte *enumName);</pre>
     * 
     * @param enumName a string describing an OpenAL constant
     * 
     * @return the actual constant for the described constant.
     */
    public int alGetEnumValue(String enumName);
}
