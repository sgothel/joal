/**
 * Copyright 2013-2023 JogAmp Community. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY JogAmp Community ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JogAmp Community OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of JogAmp Community.
 */
package com.jogamp.openal.util;


import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import jogamp.openal.Debug;

import com.jogamp.common.ExceptionUtils;
import com.jogamp.common.av.AudioFormat;
import com.jogamp.common.av.AudioSink;
import com.jogamp.common.av.AudioSink.AudioFrame;
import com.jogamp.common.os.Clock;
import com.jogamp.common.util.LFRingbuffer;
import com.jogamp.common.util.PropertyAccess;
import com.jogamp.common.util.Ringbuffer;
import com.jogamp.common.util.locks.LockFactory;
import com.jogamp.common.util.locks.RecursiveLock;
import com.jogamp.openal.AL;
import com.jogamp.openal.ALC;
import com.jogamp.openal.ALCConstants;
import com.jogamp.openal.ALCcontext;
import com.jogamp.openal.ALCdevice;
import com.jogamp.openal.ALConstants;
import com.jogamp.openal.ALExt;
import com.jogamp.openal.ALFactory;

/***
 * OpenAL {@link AudioSink} implementation.
 * <p>
 * Besides given {@link AudioSink} functionality, implementation is fully functional regarding {@link AudioFormat} and all OpenAL parameter.<br/>
 * <ul>
 * <li>All OpenAL parameter can be queried</li>
 * <li>Instance can be constructed with an OpenAL device and context, see {@link #ALAudioSink(ALCdevice, ALCcontext)}</li>
 * <li>Initialization can be performed with OpenAL paramters, see {@link #init(int, int, int, int, int, float, int, int, int)}</li>
 * </ul>
 * </p>
 */
public class ALAudioSink implements AudioSink {
    private static final boolean DEBUG_TRACE;
    private static final ALC alc;
    private static final AL al;
    private static final ALExt alExt;
    private static final boolean staticAvailable;
    private static final float EPSILON = 1.1920929E-7f; // Float.MIN_VALUE == 1.4e-45f ; double EPSILON 2.220446049250313E-16d

    private String deviceSpecifier;
    private ALCdevice device;
    private boolean hasSOFTBufferSamples;
    private boolean hasEXTMcFormats;
    private boolean hasEXTFloat32;
    private boolean hasEXTDouble;
    private boolean hasALC_thread_local_context;
    private int defaultSampleRate;
    private float defaultLatency;
    private float latency;
    private AudioFormat preferredAudioFormat;
    private ALCcontext context;
    private final RecursiveLock lock = LockFactory.createRecursiveLock();
    private boolean threadContextLocked;
    private volatile Thread exclusiveThread = null;

    /** Playback speed, range [0.5 - 2.0], default 1.0. */
    private float playSpeed;
    private float volume = 1.0f;

    static class ALAudioFrame extends AudioFrame {
        private final int alBuffer;

        ALAudioFrame(final int alBuffer) {
            this.alBuffer = alBuffer;
        }
        public ALAudioFrame(final int alBuffer, final int pts, final int duration, final int dataSize) {
            super(pts, duration, dataSize);
            this.alBuffer = alBuffer;
        }

        /** Get this frame's OpenAL buffer name */
        public final int getALBuffer() { return alBuffer; }

        @Override
        public String toString() {
            return "ALAudioFrame[pts " + pts + " ms, l " + duration + " ms, " + byteSize + " bytes, buffer "+alBuffer+"]";
        }
    }

    private int[] alBufferNames = null;
    private int avgFrameDuration = 0; // [ms]
    private int frameGrowAmount = 0;
    private int frameLimit = 0;

    private Ringbuffer<ALAudioFrame> alFramesAvail = null;
    private Ringbuffer<ALAudioFrame> alFramesPlaying = null;
    private volatile int alBufferBytesQueued = 0;
    private volatile int playingPTS = AudioFrame.INVALID_PTS;
    private volatile int enqueuedFrameCount;

    private int alSource = -1; // actually ALuint, but JOAL expects INT_MAX limit is ok!
    private AudioFormat chosenFormat;
    private int alChannelLayout;
    private int alSampleType;
    private int alFormat;
    private boolean available;

    private volatile boolean playRequested = false;

    static {
        Debug.initSingleton();
        DEBUG_TRACE = PropertyAccess.isPropertyDefined("joal.debug.AudioSink.trace", true);

        ALC _alc = null;
        AL _al = null;
        ALExt _alExt = null;
        try {
            _alc = ALFactory.getALC();
            _al = ALFactory.getAL();
            _alExt = ALFactory.getALExt();
        } catch(final Throwable t) {
            if( DEBUG ) {
                System.err.println("ALAudioSink: Caught "+t.getClass().getName()+": "+t.getMessage());
                t.printStackTrace();
            }
        }
        alc = _alc;
        al = _al;
        alExt = _alExt;
        staticAvailable = null != alc && null != al && null != alExt;
    }

    private static boolean checkALCALError(final ALCdevice device, final String prefix, final boolean verbose) {
        if( null == device || !checkALCError(device, prefix, verbose) ) {
            return checkALError(prefix, verbose);
        }
        return null != device;
    }
    private static boolean checkALError(final String prefix, final boolean verbose) {
        final int alErr = al.alGetError();
        final boolean err = ALConstants.AL_NO_ERROR != alErr;
        if( err && verbose ) {
            System.err.println("ALAudioSink."+prefix+": AL error "+err+", "+toHexString(alErr)+", '"+al.alGetString(alErr)+"']");
        }
        return err;
    }
    private static boolean checkALCError(final ALCdevice device, final String prefix, final boolean verbose) {
        final int alcErr = alc.alcGetError(device);
        final boolean err = ALCConstants.ALC_NO_ERROR != alcErr;
        if( err && verbose ) {
            System.err.println("ALAudioSink."+prefix+": ALC error "+err+", err [alc "+toHexString(alcErr)+", "+alc.alcGetString(device, alcErr)+"']");
        }
        return err;
    }

    /**
     * Create a new instance with all new OpenAL objects, i.e. opened default {@link ALCdevice}
     */
    public ALAudioSink() {
        this(null);
    }

    /**
     * Create a new instance based on optional given OpenAL objects.
     *
     * @param alDevice optional OpenAL device, a default device is opened if null.
     */
    public ALAudioSink(final ALCdevice alDevice) {
        available = false;
        chosenFormat = null;

        if( !staticAvailable ) {
            return;
        }
        synchronized(ALAudioSink.class) {
            try {
                if( null == alDevice ) {
                    // Get handle to default device.
                    device = alc.alcOpenDevice(null);
                    if (device == null) {
                        throw new RuntimeException(getThreadName()+": ALAudioSink: Error opening default OpenAL device");
                    }
                } else {
                    device = alDevice;
                }

                // Get the device specifier.
                deviceSpecifier = alc.alcGetString(device, ALCConstants.ALC_DEVICE_SPECIFIER);
                if (deviceSpecifier == null) {
                    throw new RuntimeException(getThreadName()+": ALAudioSink: Error getting specifier for default OpenAL device");
                }

                // Create audio context.
                context = alc.alcCreateContext(device, null);
                if (context == null) {
                    throw new RuntimeException(getThreadName()+": ALAudioSink: Error creating OpenAL context for "+deviceSpecifier);
                }

                lockContext();
                try {
                    // Check for an error.
                    if ( alc.alcGetError(device) != ALCConstants.ALC_NO_ERROR ) {
                        throw new RuntimeException(getThreadName()+": ALAudioSink: Error making OpenAL context current");
                    }
                    hasSOFTBufferSamples = al.alIsExtensionPresent(ALHelpers.AL_SOFT_buffer_samples);
                    hasEXTMcFormats = al.alIsExtensionPresent(ALHelpers.AL_EXT_MCFORMATS);
                    hasEXTFloat32 = al.alIsExtensionPresent(ALHelpers.AL_EXT_FLOAT32);
                    hasEXTDouble = al.alIsExtensionPresent(ALHelpers.AL_EXT_DOUBLE);
                    hasALC_thread_local_context = alc.alcIsExtensionPresent(null, ALHelpers.ALC_EXT_thread_local_context) ||
                                                  alc.alcIsExtensionPresent(device, ALHelpers.ALC_EXT_thread_local_context) ;
                    int checkErrIter = 1;
                    checkALCALError(device, "init."+checkErrIter++, DEBUG);
                    {
                        final int[] value = { 0 };
                        alc.alcGetIntegerv(device, ALCConstants.ALC_FREQUENCY, 1, value, 0);
                        if( checkALCALError(device, "read ALC_FREQUENCY", DEBUG) || 0 == value[0] ) {
                            defaultSampleRate = DefaultFormat.sampleRate;
                            if( DEBUG ) {
                                System.err.println("ALAudioSink.queryDefaultSampleRate: failed, using default "+defaultSampleRate);
                            }
                        } else {
                            defaultSampleRate = value[0];
                            if( DEBUG ) {
                                System.err.println("ALAudioSink.queryDefaultSampleRate: OK "+defaultSampleRate);
                            }
                        }
                        value[0] = 0;
                        alc.alcGetIntegerv(device, ALCConstants.ALC_REFRESH, 1, value, 0);
                        if( checkALCALError(device, "read ALC_FREQUENCY", DEBUG) || 0 == value[0] ) {
                            defaultLatency = 20f/1000f; // OpenAL-Soft default seems to be 50 Hz -> 20ms min latency
                            if( DEBUG ) {
                                System.err.println("ALAudioSink.queryDefaultRefreshRate: failed");
                            }
                        } else {
                            defaultLatency = 1f/value[0]; // Hz -> s
                            if( DEBUG ) {
                                System.err.println("ALAudioSink.queryDefaultRefreshRate: OK "+value[0]+" Hz = "+(1000f*defaultLatency)+" ms");
                            }
                        }
                    }
                    preferredAudioFormat = new AudioFormat(defaultSampleRate, DefaultFormat.sampleSize, DefaultFormat.channelCount, DefaultFormat.signed, DefaultFormat.fixedP, DefaultFormat.planar, DefaultFormat.littleEndian);
                    if( DEBUG ) {
                        final int[] alcvers = { 0, 0 };
                        System.out.println("ALAudioSink: OpenAL Version: "+al.alGetString(ALConstants.AL_VERSION));
                        System.out.println("ALAudioSink: OpenAL Extensions: "+al.alGetString(ALConstants.AL_EXTENSIONS));
                        checkALCALError(device, "init."+checkErrIter++, DEBUG);
                        System.out.println("ALAudioSink: Null device OpenALC:");
                        alc.alcGetIntegerv(null, ALCConstants.ALC_MAJOR_VERSION, 1, alcvers, 0);
                        alc.alcGetIntegerv(null, ALCConstants.ALC_MINOR_VERSION, 1, alcvers, 1);
                        System.out.println("  Version: "+alcvers[0]+"."+alcvers[1]);
                        System.out.println("  Extensions: "+alc.alcGetString(null, ALCConstants.ALC_EXTENSIONS));
                        checkALCALError(device, "init."+checkErrIter++, DEBUG);
                        System.out.println("ALAudioSink: Device "+deviceSpecifier+" OpenALC:");
                        alc.alcGetIntegerv(device, ALCConstants.ALC_MAJOR_VERSION, 1, alcvers, 0);
                        alc.alcGetIntegerv(device, ALCConstants.ALC_MINOR_VERSION, 1, alcvers, 1);
                        System.out.println("  Version: "+alcvers[0]+"."+alcvers[1]);
                        System.out.println("  Extensions: "+alc.alcGetString(device, ALCConstants.ALC_EXTENSIONS));
                        System.out.println("ALAudioSink: hasSOFTBufferSamples "+hasSOFTBufferSamples);
                        System.out.println("ALAudioSink: hasEXTMcFormats "+hasEXTMcFormats);
                        System.out.println("ALAudioSink: hasEXTFloat32 "+hasEXTFloat32);
                        System.out.println("ALAudioSink: hasEXTDouble "+hasEXTDouble);
                        System.out.println("ALAudioSink: hasALC_thread_local_context "+hasALC_thread_local_context);
                        System.out.println("ALAudioSink: preferredAudioFormat "+preferredAudioFormat);
                        System.out.println("ALAudioSink: defaultMixerRefreshRate "+(1000f*defaultLatency)+" ms, "+(1f/defaultLatency)+" Hz");
                        System.out.println("ALAudioSink: maxSupportedChannels "+getMaxSupportedChannels());
                        checkALCALError(device, "init."+checkErrIter++, DEBUG);
                    }

                    if( DEBUG ) {
                        System.err.println("ALAudioSink: Using device: " + deviceSpecifier);
                    }
                    available = true;
                } finally {
                    unlockContext();
                }
                return;
            } catch ( final Exception e ) {
                if( DEBUG ) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
                destroy();
            }
        }
    }

    // Expose AudioSink OpenAL implementation specifics

    /** Return OpenAL global {@link AL}. */
    public static final AL getAL() { return al; }
    /** Return OpenAL global {@link ALC}. */
    public static final ALC getALC() { return alc; }
    /** Return OpenAL global {@link ALExt}. */
    public static final ALExt getALExt() { return alExt; }

    /** Return this instance's OpenAL {@link ALCdevice}. */
    public final ALCdevice getDevice() { return device; }
    /** Return this instance's OpenAL {@link ALCdevice} specifier. */
    public final String getDeviceSpec() { return deviceSpecifier; }
    /** Return this instance's OpenAL {@link ALCcontext}. */
    public final ALCcontext getALContext() { return context; }
    /** Return this instance's OpenAL source ID. */
    public final int getALSource() { return alSource; }

    /** Return whether OpenAL extension <code>AL_SOFT_buffer_samples</code> is available. */
    public final boolean hasSOFTBufferSamples() { return hasSOFTBufferSamples; }
    /** Return whether OpenAL extension <code>AL_EXT_MCFORMATS</code> is available. */
    public final boolean hasEXTMcFormats() { return hasEXTMcFormats; }
    /** Return whether OpenAL extension <code>AL_EXT_FLOAT32</code> is available. */
    public final boolean hasEXTFloat32() { return hasEXTFloat32; }
    /** Return whether OpenAL extension <code>AL_EXT_DOUBLE</code> is available. */
    public final boolean hasEXTDouble() { return hasEXTDouble; }
    /** Return whether OpenAL extension <code>ALC_EXT_thread_local_context</code> is available. */
    public final boolean hasALCThreadLocalContext() { return hasALC_thread_local_context; }

    /** Return this instance's OpenAL channel layout, set after {@link #init(AudioFormat, float, int, int, int)}. */
    public final int getALChannelLayout() { return alChannelLayout; }
    /** Return this instance's OpenAL sample type, set after {@link #init(AudioFormat, float, int, int, int)}. */
    public final int getALSampleType() { return alSampleType; }
    /** Return this instance's OpenAL format, set after {@link #init(AudioFormat, float, int, int, int)}. */
    public final int getALFormat() { return alFormat; }

    // AudioSink implementation ...

    @Override
    public final void lockExclusive() {
        lockContext();
        exclusiveThread = Thread.currentThread();
    }
    @Override
    public final void unlockExclusive() {
        exclusiveThread = null;
        unlockContext();
    }
    private final void lockContext() {
        if( null != exclusiveThread ) {
            if( Thread.currentThread() == exclusiveThread ) {
                return;
            }
            throw new IllegalStateException("Exclusive lock by "+exclusiveThread+", but current is "+Thread.currentThread());
        }
        lock.lock();
        final boolean r;
        if( hasALC_thread_local_context ) {
            r = alExt.alcSetThreadContext(context);
            threadContextLocked = true;
        } else {
            r = alc.alcMakeContextCurrent(context);
            threadContextLocked = false;
        }
        if( !r ) {
            final int alcErr = alc.alcGetError(null);
            if( ALCConstants.ALC_NO_ERROR != alcErr ) {
                final String err = getThreadName()+": ALCError "+toHexString(alcErr)+" while makeCurrent. "+this;
                System.err.println(err);
                ExceptionUtils.dumpStack(System.err);
                lock.unlock();
                throw new RuntimeException(err);
            }
            final int alErr = al.alGetError();
            if( ALCConstants.ALC_NO_ERROR != alErr ) {
                if( DEBUG ) {
                    System.err.println(getThreadName()+": Prev - ALError "+toHexString(alErr)+" @ makeCurrent. "+this);
                    ExceptionUtils.dumpStack(System.err);
                }
            }
            final String err = getThreadName()+": ALCError makeCurrent failed. "+this;
            System.err.println(err);
            ExceptionUtils.dumpStack(System.err);
            lock.unlock();
            throw new RuntimeException(err);
        }
    }
    private final void unlockContext() {
        if( null != exclusiveThread ) {
            if( Thread.currentThread() == exclusiveThread ) {
                return;
            }
            throw new IllegalStateException("Exclusive lock by "+exclusiveThread+", but current is "+Thread.currentThread());
        }
        final boolean r;
        if( threadContextLocked ) {
            r = alExt.alcSetThreadContext(null);
        } else {
            r = alc.alcMakeContextCurrent(null);
        }
        if( DEBUG ) {
            if( !r ) {
                System.err.println(getThreadName()+": unlockContext failed. "+this);
                ExceptionUtils.dumpStack(System.err);
            }
        }
        lock.unlock();
    }
    private final void destroyContext() {
        lock.lock();
        try {
            if( null != context ) {
                try {
                    exclusiveThread = null;
                    if( threadContextLocked ) {
                        alExt.alcSetThreadContext(null);
                    } else {
                        alc.alcMakeContextCurrent(null);
                    }
                    alc.alcDestroyContext(context);
                } catch (final Throwable t) {
                    if( DEBUG ) {
                        ExceptionUtils.dumpThrowable("", t);
                    }
                }
                context = null;
            }
            // unroll lock !
            while(lock.getHoldCount() > 1) {
                lock.unlock();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public final String toString() {
        final int alBuffersLen = null != alBufferNames ? alBufferNames.length : 0;
        final int ctxHash = context != null ? context.hashCode() : 0;
        final int alFramesAvailSize = alFramesAvail != null ? alFramesAvail.size() : 0;
        final int alFramesPlayingSize = alFramesPlaying != null ? alFramesPlaying.size() : 0;
        return String.format("ALAudioSink[avail %b, playReq %b, device '%s', ctx 0x%x, alSource %d"+
               ", chosen %s, al[chan %s, type %s, fmt 0x%x, tlc %b, soft %b, latency %.2f/%.2f ms]"+
               ", playSpeed %.2f, buffers[total %d, avail %d, queued[%d, apts %d, %d ms, %d bytes], queue[g %d, l %d]",
               available, playRequested, deviceSpecifier, ctxHash, alSource, chosenFormat.toString(),
               ALHelpers.alChannelLayoutName(alChannelLayout), ALHelpers.alSampleTypeName(alSampleType),
               alFormat, hasALC_thread_local_context, hasSOFTBufferSamples,
               1000f*latency, 1000f*defaultLatency, playSpeed, alBuffersLen, alFramesAvailSize,
               alFramesPlayingSize, getPTS(), getQueuedTime(), alBufferBytesQueued, frameGrowAmount, frameLimit
               );
    }

    private final String shortString() {
        final int ctxHash = context != null ? context.hashCode() : 0;
        return "[ctx "+toHexString(ctxHash)+", playReq "+playRequested+", alSrc "+alSource+
               ", queued["+alFramesPlaying.size()+", " + alBufferBytesQueued+" bytes], "+
               "queue[g "+frameGrowAmount+", l "+frameLimit+"]";
    }

    public final String getPerfString() {
        final int alBuffersLen = null != alBufferNames ? alBufferNames.length : 0;
        return "Play [buffer "+alFramesPlaying.size()+"/"+alBuffersLen+", apts "+getPTS()+", "+getQueuedTime() + " ms, " + alBufferBytesQueued+" bytes]";
    }

    @Override
    public int getPreferredSampleRate() {
        return defaultSampleRate;
    }

    @Override
    public float getDefaultLatency() {
        return defaultLatency;
    }

    @Override
    public float getLatency() {
        return latency;
    }

    @Override
    public final AudioFormat getPreferredFormat() {
        if( !staticAvailable ) {
            return null;
        }
        return preferredAudioFormat;
    }

    @Override
    public final int getMaxSupportedChannels() {
        if( !staticAvailable ) {
            return 0;
        }
        if( hasEXTMcFormats || hasSOFTBufferSamples ) {
            return 8;
        } else {
            return 2;
        }
    }

    @Override
    public final boolean isSupported(final AudioFormat format) {
        if( !staticAvailable ) {
            return false;
        }
        if( format.planar || !format.littleEndian ) {
            if( DEBUG ) {
                System.err.println(getThreadName()+": ALAudioSink.isSupported: NO.0 "+format);
            }
            return false;
        }
        final int alFormat = ALHelpers.getALFormat(format, al, alExt,
                                                   hasSOFTBufferSamples, hasEXTMcFormats,
                                                   hasEXTFloat32, hasEXTDouble);
        if( ALConstants.AL_NONE != alFormat ) {
            if( DEBUG ) {
                System.err.println(getThreadName()+": ALAudioSink.isSupported: OK "+format+", alFormat "+toHexString(alFormat));
            }
            return true;
        } else {
            if( DEBUG ) {
                System.err.println(getThreadName()+": ALAudioSink.isSupported: NO.1 "+format);
            }
            return false;
        }
    }

    @Override
    public final boolean init(final AudioFormat requestedFormat, final int frameDuration, final int initialQueueSize, final int queueGrowAmount, final int queueLimit) {
        if( !staticAvailable ) {
            return false;
        }
        final int alChannelLayout = ALHelpers.getDefaultALChannelLayout(requestedFormat.channelCount);
        final int alSampleType = ALHelpers.getALSampleType(requestedFormat.sampleSize, requestedFormat.signed, requestedFormat.fixedP);
        final int alFormat;
        if( ALConstants.AL_NONE != alChannelLayout && ALConstants.AL_NONE != alSampleType ) {
            alFormat = ALHelpers.getALFormat(alChannelLayout, alSampleType, al, alExt,
                                             hasSOFTBufferSamples, hasEXTMcFormats,
                                             hasEXTFloat32, hasEXTDouble);
        } else {
            alFormat = ALConstants.AL_NONE;
        }
        if( ALConstants.AL_NONE == alFormat ) {
            // not supported
            if( DEBUG ) {
                System.err.println(getThreadName()+": ALAudioSink.init1: Not supported: "+requestedFormat+", "+toString());
            }
            return false;
        }
        return initImpl(requestedFormat, alChannelLayout, alSampleType, alFormat,
                        frameDuration/1000f, initialQueueSize, queueGrowAmount, queueLimit);
    }

    /**
     * Initializes the sink using the given OpenAL audio parameter and streaming details.
     * @param alChannelLayout OpenAL channel layout
     * @param alSampleType OpenAL sample type
     * @param alFormat OpenAL format
     * @param sampleRate sample rate, e.g. 44100
     * @param sampleSize sample size in bits, e.g. 16
     * @param frameDuration average or fixed frame duration in milliseconds
     *                      helping a caching {@link AudioFrame} based implementation to determine the frame count in the queue.
     *                      See {@link #DefaultFrameDuration}.
     * @param initialQueueSize initial time in milliseconds to queue in this sink, see {@link #DefaultInitialQueueSize}.
     * @param queueGrowAmount time in milliseconds to grow queue if full, see {@link #DefaultQueueGrowAmount}.
     * @param queueLimit maximum time in milliseconds the queue can hold (and grow), see {@link #DefaultQueueLimitWithVideo} and {@link #DefaultQueueLimitAudioOnly}.
     * @return true if successful, otherwise false
     * @see ALHelpers#getAudioFormat(int, int, int, int, int)
     * @see #init(AudioFormat, float, int, int, int)
     */
    public final boolean init(final int alChannelLayout, final int alSampleType, final int alFormat,
                              final int sampleRate, final int sampleSize,
                              final int frameDuration, final int initialQueueSize, final int queueGrowAmount, final int queueLimit) {
        final AudioFormat requestedFormat = ALHelpers.getAudioFormat(alChannelLayout, alSampleType, alFormat, sampleRate, sampleSize);
        if( null == requestedFormat ) {
            if( DEBUG ) {
                System.err.println(getThreadName()+": ALAudioSink.init2: Invalid AL channelLayout "+toHexString(alChannelLayout)+
                        ", sampleType "+toHexString(alSampleType)+", format "+toHexString(alFormat)+" or sample[rate "+sampleRate+", size "+sampleSize+"]; "+toString());
            }
            return false;
        }
        return initImpl(requestedFormat, alChannelLayout, alSampleType, alFormat,
                        frameDuration/1000f, initialQueueSize, queueGrowAmount, queueLimit);
    }

    private final boolean initImpl(final AudioFormat requestedFormat,
                                   final int alChannelLayout, final int alSampleType, final int alFormat,
                                   float frameDurationS, final int initialQueueSize, final int queueGrowAmount, final int queueLimit) {
        lock.lock();
        try {
            this.alChannelLayout = alChannelLayout;
            this.alSampleType = alSampleType;
            this.alFormat = alFormat;

            // Flush all old buffers
            lockContext();
            try {
                stopImpl(true);
                deleteSource();
                destroyBuffers();
            } finally {
                unlockContext();
            }
            frameDurationS = frameDurationS >= 1f/1000f ? frameDurationS : AudioSink.DefaultFrameDuration/1000f;

            {
                final int defRefreshRate = Math.round( 1f / defaultLatency ); // s -> Hz
                final int expMixerRefreshRate = Math.round( 1f / frameDurationS ); // s -> Hz

                if( frameDurationS < defaultLatency ) {
                    // Re-Create audio context with desired refresh-rate
                    if( DEBUG ) {
                        System.err.println(getThreadName()+": ALAudioSink.init: Re-create context as latency exp "+
                                (1000f*frameDurationS)+" ms ("+expMixerRefreshRate+" Hz) < default "+(1000f*defaultLatency)+" ms ("+defRefreshRate+" Hz)");
                    }
                    try {
                        exclusiveThread = null;
                        if( threadContextLocked ) {
                            alExt.alcSetThreadContext(null);
                        } else {
                            alc.alcMakeContextCurrent(null);
                        }
                        alc.alcDestroyContext(context);
                    } catch (final Throwable t) {
                        if( DEBUG ) {
                            ExceptionUtils.dumpThrowable("", t);
                        }
                    }

                    context = alc.alcCreateContext(device, new int[] { ALCConstants.ALC_REFRESH, expMixerRefreshRate }, 0);
                    if (context == null) {
                        throw new RuntimeException(getThreadName()+": ALAudioSink: Error creating OpenAL context for "+deviceSpecifier);
                    }
                } else if( DEBUG ) {
                    System.err.println(getThreadName()+": ALAudioSink.init: Keep context, latency exp "+
                            (1000f*frameDurationS)+" ms ("+expMixerRefreshRate+" Hz) >= default "+(1000f*defaultLatency)+" ms ("+defRefreshRate+" Hz)");
                }
            }

            lockContext();
            try {
                // Get actual refresh rate
                {
                    final int[] value = { 0 };
                    alc.alcGetIntegerv(device, ALCConstants.ALC_REFRESH, 1, value, 0);
                    if( checkALCALError(device, "read ALC_FREQUENCY", DEBUG) || 0 == value[0] ) {
                        latency = defaultLatency;
                        if( DEBUG ) {
                            System.err.println("ALAudioSink.queryRefreshRate: failed, claiming default "+(1000f*latency)+" ms");
                        }
                    } else {
                        latency = 1f/value[0]; // Hz -> ms
                        if( DEBUG ) {
                            System.err.println("ALAudioSink.queryRefreshRate: OK "+value[0]+" Hz = "+(1000f*latency)+" ms");
                        }
                    }
                }
                createSource();

                // Allocate new buffers
                {
                    final int frameDurationMS = Math.round(1000f*frameDurationS);
                    avgFrameDuration = frameDurationMS;
                    final int initialFrameCount = requestedFormat.getFrameCount(
                            initialQueueSize > 0 ? initialQueueSize : AudioSink.DefaultInitialQueueSize, frameDurationMS);
                    // frameDuration, int initialQueueSize, int queueGrowAmount, int queueLimit) {
                    alBufferNames = new int[initialFrameCount];
                    al.alGenBuffers(initialFrameCount, alBufferNames, 0);
                    final int err = al.alGetError();
                    if( ALConstants.AL_NO_ERROR != err ) {
                        alBufferNames = null;
                        throw new RuntimeException(getThreadName()+": ALAudioSink: Error generating Buffers: 0x"+Integer.toHexString(err));
                    }
                    final ALAudioFrame[] alFrames = new ALAudioFrame[initialFrameCount];
                    for(int i=0; i<initialFrameCount; i++) {
                        alFrames[i] = new ALAudioFrame(alBufferNames[i]);
                    }

                    alFramesAvail = new LFRingbuffer<ALAudioFrame>(alFrames);
                    alFramesPlaying = new LFRingbuffer<ALAudioFrame>(ALAudioFrame[].class, initialFrameCount);
                    this.frameGrowAmount = requestedFormat.getFrameCount(
                            queueGrowAmount > 0 ? queueGrowAmount : AudioSink.DefaultQueueGrowAmount, frameDurationMS);
                    this.frameLimit = requestedFormat.getFrameCount(
                            queueLimit > 0 ? queueLimit : AudioSink.DefaultQueueLimitWithVideo, frameDurationMS);
                    if( DEBUG_TRACE ) {
                        alFramesAvail.dump(System.err, "Avail-init");
                        alFramesPlaying.dump(System.err, "Playi-init");
                    }
                }
            } finally {
                unlockContext();
            }

            chosenFormat = requestedFormat;
            if( DEBUG ) {
                System.err.println(getThreadName()+": ALAudioSink.init: OK "+requestedFormat+", "+toString());
            }
        } finally {
            lock.unlock();
        }
        return true;
    }

    @Override
    public final AudioFormat getChosenFormat() {
        return chosenFormat;
    }

    private static int[] concat(final int[] first, final int[] second) {
        final int[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
    /**
    private static <T> T[] concat(T[] first, T[] second) {
        final T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    } */

    private boolean growBuffers() {
        if( !alFramesAvail.isEmpty() || !alFramesPlaying.isFull() ) {
            throw new InternalError("Buffers: Avail is !empty "+alFramesAvail+" or Playing is !full "+alFramesPlaying);
        }
        if( alFramesAvail.capacity() >= frameLimit || alFramesPlaying.capacity() >= frameLimit ) {
            if( DEBUG ) {
                System.err.println(getThreadName()+": ALAudioSink.growBuffers: Frame limit "+frameLimit+" reached: Avail "+alFramesAvail+", Playing "+alFramesPlaying);
            }
            return false;
        }

        final int[] newALBufferNames = new int[frameGrowAmount];
        al.alGenBuffers(frameGrowAmount, newALBufferNames, 0);
        final int err = al.alGetError();
        if( ALConstants.AL_NO_ERROR != err ) {
            if( DEBUG ) {
                System.err.println(getThreadName()+": ALAudioSink.growBuffers: Error generating "+frameGrowAmount+" new Buffers: 0x"+Integer.toHexString(err));
            }
            return false;
        }
        alBufferNames = concat(alBufferNames, newALBufferNames);

        final ALAudioFrame[] newALBuffers = new ALAudioFrame[frameGrowAmount];
        for(int i=0; i<frameGrowAmount; i++) {
            newALBuffers[i] = new ALAudioFrame(newALBufferNames[i]);
        }
        // alFrames = concat(alFrames , newALBuffers);

        alFramesAvail.growEmptyBuffer(newALBuffers);
        alFramesPlaying.growFullBuffer(frameGrowAmount);
        if( alFramesAvail.isEmpty() || alFramesPlaying.isFull() ) {
            throw new InternalError("Buffers: Avail is empty "+alFramesAvail+" or Playing is full "+alFramesPlaying);
        }
        if( DEBUG ) {
            System.err.println(getThreadName()+": ALAudioSink: Buffer grown "+frameGrowAmount+": Avail "+alFramesAvail+", playing "+alFramesPlaying);
        }
        if( DEBUG_TRACE ) {
            alFramesAvail.dump(System.err, "Avail-grow");
            alFramesPlaying.dump(System.err, "Playi-grow");
        }
        return true;
    }

    private void destroyBuffers() {
        if( !staticAvailable ) {
            return;
        }
        if( null != alBufferNames ) {
            try {
                al.alDeleteBuffers(alBufferNames.length, alBufferNames, 0);
            } catch (final Throwable t) {
                if( DEBUG ) {
                    System.err.println("Caught "+t.getClass().getName()+": "+t.getMessage());
                    t.printStackTrace();
                }
            }
            alFramesAvail.clear();
            alFramesAvail = null;
            alFramesPlaying.clear();
            alFramesPlaying = null;
            alBufferBytesQueued = 0;
            // alFrames = null;
            alBufferNames = null;
        }
    }

    private void deleteSource() {
        if( 0 > alSource ) {
            return;
        }
        try {
            al.alDeleteSources(1, new int[] { alSource }, 0);
        } catch (final Throwable t) {
            if( DEBUG ) {
                System.err.println("Caught "+t.getClass().getName()+": "+t.getMessage());
                t.printStackTrace();
            }
        }
        alSource = -1;
    }
    private void createSource() {
        if( 0 <= alSource ) {
            return;
        }
        final int[] val = { -1 };
        al.alGenSources(1, val, 0);
        final int err = al.alGetError();
        if( ALConstants.AL_NO_ERROR != err ) {
            alSource = -1;
            throw new RuntimeException(getThreadName()+": ALAudioSink: Error generating Source: 0x"+Integer.toHexString(err));
        }
        alSource = val[0];
    }

    @Override
    public final void destroy() {
        available = false;
        if( !staticAvailable ) {
            return;
        }
        if( null != context ) {
            lockContext();
        }
        try {
            stopImpl(true);
            deleteSource();
            destroyBuffers();
        } finally {
            destroyContext();
        }
        if( null != device ) {
            try {
                alc.alcCloseDevice(device);
            } catch (final Throwable t) {
                if( DEBUG ) {
                    System.err.println("Caught "+t.getClass().getName()+": "+t.getMessage());
                    t.printStackTrace();
                }
            }
            device = null;
        }
        chosenFormat = null;
    }

    @Override
    public final boolean isAvailable() {
        return available;
    }

    /**
     * Dequeuing playing audio frames.
     * @param wait if true, waiting for completion of audio buffers
     * @param ignoreBufferInconsistency
     * @return dequeued buffer count
     */
    private final int dequeueBuffer(final boolean wait, final boolean ignoreBufferInconsistency) {
        int alErr = ALConstants.AL_NO_ERROR;
        final int releaseBufferCount;
        if( alBufferBytesQueued > 0 ) {
            final int releaseBufferLimes = Math.max(1, alFramesPlaying.size() / 4 );
            final int[] val=new int[1];
            final int avgBufferDura = chosenFormat.getBytesDuration( alBufferBytesQueued / alFramesPlaying.size() );
            final int sleepLimes = releaseBufferLimes * avgBufferDura;
            int i=0;
            int slept = 0;
            int releasedBuffers = 0;
            final long t0 = DEBUG ? Clock.currentNanos() : 0;
            do {
                val[0] = 0;
                al.alGetSourcei(alSource, ALConstants.AL_BUFFERS_PROCESSED, val, 0);
                alErr = al.alGetError();
                if( ALConstants.AL_NO_ERROR != alErr ) {
                    throw new RuntimeException(getThreadName()+": ALError "+toHexString(alErr)+" while quering processed buffers at source. "+this);
                }
                releasedBuffers += val[0];
                if( wait && releasedBuffers < releaseBufferLimes ) {
                    i++;
                    // clip wait at [avgFrameDuration .. 100] ms
                    final int sleep = Math.max(avgFrameDuration, Math.min(100, releaseBufferLimes-releasedBuffers * avgBufferDura)) - 1; // 1 ms off for busy-loop
                    if( slept + sleep + 1 <= sleepLimes ) {
                        if( DEBUG ) {
                            System.err.println(getThreadName()+": ALAudioSink: Dequeue.wait-sleep["+i+"]: avgBufferDura "+avgBufferDura+
                                    ", releaseBuffers "+releasedBuffers+"/"+releaseBufferLimes+", sleep "+sleep+"/"+slept+"/"+sleepLimes+
                                    " ms, playImpl "+(ALConstants.AL_PLAYING == getSourceState(false))+", processed "+val[0]+", "+shortString());
                        }
                        unlockContext();
                        try {
                            Thread.sleep( sleep );
                            slept += sleep;
                        } catch (final InterruptedException e) {
                        } finally {
                            lockContext();
                        }
                    } else {
                        // Empirical best behavior w/ openal-soft (sort of needs min ~21ms to complete processing a buffer even if period < 20ms?)
                        unlockContext();
                        try {
                            Thread.sleep( 1 );
                            slept += 1;
                        } catch (final InterruptedException e) {
                        } finally {
                            lockContext();
                        }
                    }
                }
            } while ( wait && releasedBuffers < releaseBufferLimes && alBufferBytesQueued > 0 );
            releaseBufferCount = releasedBuffers;
            if( DEBUG ) {
                final long t1 = Clock.currentNanos();
                System.err.println(getThreadName()+": ALAudioSink: Dequeue.wait-done["+i+"]: "+TimeUnit.NANOSECONDS.toMillis(t1-t0)+" ms, avgBufferDura "+avgBufferDura+
                        ", releaseBuffers "+releaseBufferCount+"/"+releaseBufferLimes+", slept "+slept+" ms, playImpl "+(ALConstants.AL_PLAYING == getSourceState(false))+
                        ", processed "+val[0]+", "+shortString());
            }
        } else {
            releaseBufferCount = 0;
        }

        if( releaseBufferCount > 0 ) {
            final int[] buffers = new int[releaseBufferCount];
            al.alSourceUnqueueBuffers(alSource, releaseBufferCount, buffers, 0);
            alErr = al.alGetError();
            if( ALConstants.AL_NO_ERROR != alErr ) {
                throw new RuntimeException(getThreadName()+": ALError "+toHexString(alErr)+" while dequeueing "+releaseBufferCount+" buffers. "+this);
            }
            for ( int i=0; i<releaseBufferCount; i++ ) {
                final ALAudioFrame releasedBuffer = alFramesPlaying.get();
                if( null == releasedBuffer ) {
                    if( !ignoreBufferInconsistency ) {
                        throw new InternalError("Internal Error: "+this);
                    }
                } else {
                    if(DEBUG_TRACE) {
                        System.err.println("<  [al "+buffers[i]+", q "+releasedBuffer.alBuffer+"] <- "+shortString()+" @ "+getThreadName());
                    }
                    if( releasedBuffer.alBuffer != buffers[i] ) {
                        if( !ignoreBufferInconsistency ) {
                            alFramesAvail.dump(System.err, "Avail-deq02-post");
                            alFramesPlaying.dump(System.err, "Playi-deq02-post");
                            throw new InternalError("Buffer name mismatch: dequeued: "+buffers[i]+", released "+releasedBuffer+", "+this);
                        }
                    }
                    alBufferBytesQueued -= releasedBuffer.getByteSize();
                    if( !alFramesAvail.put(releasedBuffer) ) {
                        throw new InternalError("Internal Error: "+this);
                    }
                    if(DEBUG_TRACE) {
                        System.err.println("<< [al "+buffers[i]+", q "+releasedBuffer.alBuffer+"] <- "+shortString()+" @ "+getThreadName());
                    }
                }
            }
        }
        return releaseBufferCount;
    }
    private final void dequeueForceAll() {
        if(DEBUG_TRACE) {
            System.err.println("<   _FLUSH_  <- "+shortString()+" @ "+getThreadName());
        }
        final int[] val=new int[1];
        al.alSourcei(alSource, ALConstants.AL_BUFFER, 0); // explicit force zero buffer!
        if(DEBUG_TRACE) {
            al.alGetSourcei(alSource, ALConstants.AL_BUFFERS_PROCESSED, val, 0);
        }
        final int alErr = al.alGetError();
        while ( !alFramesPlaying.isEmpty() ) {
            final ALAudioFrame releasedBuffer = alFramesPlaying.get();
            if( null == releasedBuffer ) {
                throw new InternalError("Internal Error: "+this);
            }
            alBufferBytesQueued -= releasedBuffer.getByteSize();
            if( !alFramesAvail.put(releasedBuffer) ) {
                throw new InternalError("Internal Error: "+this);
            }
        }
        alBufferBytesQueued = 0;
        if(DEBUG_TRACE) {
            System.err.println("<<  _FLUSH_  [al "+val[0]+", err "+toHexString(alErr)+"] <- "+shortString()+" @ "+getThreadName());
            ExceptionUtils.dumpStack(System.err);
        }
    }

    /**
     * Dequeuing playing audio frames.
     * @param wait if true, waiting for completion of audio buffers
     * @param inPTS
     * @param inDuration
     * @return dequeued buffer count
     */
    private final int dequeueBuffer(final boolean wait, final int inPTS, final int inDuration) {
        final int dequeuedBufferCount = dequeueBuffer( wait, false /* ignoreBufferInconsistency */ );
        final ALAudioFrame currentBuffer = alFramesPlaying.peek();
        if( null != currentBuffer ) {
            playingPTS = currentBuffer.getPTS();
        } else {
            playingPTS = inPTS;
        }
        if( DEBUG ) {
            if( dequeuedBufferCount > 0 ) {
                System.err.println(getThreadName()+": ALAudioSink: Write "+inPTS+", "+inDuration+" ms, dequeued "+dequeuedBufferCount+", wait "+wait+", "+getPerfString());
            }
        }
        return dequeuedBufferCount;
    }

    @Override
    public final AudioFrame enqueueData(final int pts, final ByteBuffer bytes, final int byteCount) {
        if( !available || null == chosenFormat ) {
            return null;
        }
        final ALAudioFrame alFrame;

        // OpenAL consumes buffers in the background
        // we first need to initialize the OpenAL buffers then
        // start continuous playback.
        lockContext();
        try {
            final int duration = chosenFormat.getBytesDuration(byteCount);
            if( alFramesAvail.isEmpty() ) {
                // try to dequeue w/o waiting first
                dequeueBuffer(false, pts, duration);
                if( alFramesAvail.isEmpty() ) {
                    // try to grow
                    growBuffers();
                }
                if( alFramesAvail.isEmpty() && alFramesPlaying.size() > 0 && isPlayingImpl0() ) {
                    // possible if grow failed or already exceeds it's limit - only possible if playing ..
                    dequeueBuffer(true /* wait */, pts, duration);
                }
            }

            alFrame = alFramesAvail.get();
            if( null == alFrame ) {
                alFramesAvail.dump(System.err, "Avail");
                throw new InternalError("Internal Error: avail.get null "+alFramesAvail+", "+this);
            }
            alFrame.setPTS(pts);
            alFrame.setDuration(duration);
            alFrame.setByteSize(byteCount);
            if( !alFramesPlaying.put( alFrame ) ) {
                throw new InternalError("Internal Error: "+this);
            }
            final int[] alBufferNames = new int[] { alFrame.alBuffer };
            if( hasSOFTBufferSamples ) {
                final int samplesPerChannel = chosenFormat.getBytesSampleCount(byteCount) / chosenFormat.channelCount;
                // final int samplesPerChannel = ALHelpers.bytesToSampleCount(byteCount, alChannelLayout, alSampleType);
                alExt.alBufferSamplesSOFT(alFrame.alBuffer, chosenFormat.sampleRate, alFormat,
                                          samplesPerChannel, alChannelLayout, alSampleType, bytes);
            } else {
                al.alBufferData(alFrame.alBuffer, alFormat, bytes, byteCount, chosenFormat.sampleRate);
            }

            if(DEBUG_TRACE) {
                System.err.println(">  "+alFrame.alBuffer+" -> "+shortString()+" @ "+getThreadName());
            }

            al.alSourceQueueBuffers(alSource, 1, alBufferNames, 0);
            final int alErr = al.alGetError();
            if( ALConstants.AL_NO_ERROR != alErr ) {
                throw new RuntimeException(getThreadName()+": ALError "+toHexString(alErr)+" while queueing buffer "+toHexString(alBufferNames[0])+". "+this);
            }
            alBufferBytesQueued += byteCount;
            enqueuedFrameCount++; // safe: only written-to while locked!

            if(DEBUG_TRACE) {
                System.err.println(">> "+alFrame.alBuffer+" -> "+shortString()+" @ "+getThreadName());
            }

            playImpl(); // continue playing, fixes issue where we ran out of enqueued data!
        } finally {
            unlockContext();
        }
        return alFrame;
    }

    @Override
    public final boolean isPlaying() {
        if( !available || null == chosenFormat ) {
            return false;
        }
        if( playRequested ) {
            lockContext();
            try {
                return isPlayingImpl0();
            } finally {
                unlockContext();
            }
        } else {
            return false;
        }
    }
    private final boolean isPlayingImpl0() {
        if( playRequested ) {
            return ALConstants.AL_PLAYING == getSourceState(false);
        } else {
            return false;
        }
    }
    private final int getSourceState(final boolean ignoreError) {
        final int[] val = new int[1];
        al.alGetSourcei(alSource, ALConstants.AL_SOURCE_STATE, val, 0);
        final int alErr = al.alGetError();
        if( ALConstants.AL_NO_ERROR != alErr ) {
            final String msg = getThreadName()+": ALError "+toHexString(alErr)+" while querying SOURCE_STATE. "+this;
            if( ignoreError ) {
                if( DEBUG ) {
                    System.err.println(msg);
                }
            } else {
                throw new RuntimeException(msg);
            }
        }
        return val[0];
    }

    @Override
    public final void play() {
        if( !available || null == chosenFormat ) {
            return;
        }
        playRequested = true;
        lockContext();
        try {
            playImpl();
            if( DEBUG ) {
                System.err.println(getThreadName()+": ALAudioSink: PLAY playImpl "+(ALConstants.AL_PLAYING == getSourceState(false))+", "+this);
            }
        } finally {
            unlockContext();
        }
    }
    private final void playImpl() {
        if( playRequested && ALConstants.AL_PLAYING != getSourceState(false) ) {
            al.alSourcePlay(alSource);
            final int alErr = al.alGetError();
            if( ALConstants.AL_NO_ERROR != alErr ) {
                throw new RuntimeException(getThreadName()+": ALError "+toHexString(alErr)+" while start playing. "+this);
            }
        }
    }

    @Override
    public final void pause() {
        if( !available || null == chosenFormat ) {
            return;
        }
        if( playRequested ) {
            lockContext();
            try {
                pauseImpl();
                if( DEBUG ) {
                    System.err.println(getThreadName()+": ALAudioSink: PAUSE playImpl "+(ALConstants.AL_PLAYING == getSourceState(false))+", "+this);
                }
            } finally {
                unlockContext();
            }
        }
    }
    private final void pauseImpl() {
        if( isPlayingImpl0() ) {
            playRequested = false;
            al.alSourcePause(alSource);
            final int alErr = al.alGetError();
            if( ALConstants.AL_NO_ERROR != alErr ) {
                throw new RuntimeException(getThreadName()+": ALError "+toHexString(alErr)+" while pausing. "+this);
            }
        }
    }
    private final void stopImpl(final boolean ignoreError) {
        if( 0 > alSource ) {
            return;
        }
        if( ALConstants.AL_STOPPED != getSourceState(ignoreError) ) {
            playRequested = false;
            al.alSourceStop(alSource);
            final int alErr = al.alGetError();
            if( ALConstants.AL_NO_ERROR != alErr ) {
                final String msg = "ALError "+toHexString(alErr)+" while stopping. "+this;
                if( ignoreError ) {
                    if( DEBUG ) {
                        System.err.println(getThreadName()+": "+msg);
                    }
                } else {
                    throw new RuntimeException(getThreadName()+": ALError "+toHexString(alErr)+" while stopping. "+this);
                }
            }
        }
    }

    @Override
    public final float getPlaySpeed() { return playSpeed; }

    @Override
    public final boolean setPlaySpeed(float rate) {
        if( !available || null == chosenFormat ) {
            return false;
        }
        lockContext();
        try {
            if( Math.abs(1.0f - rate) < 0.01f ) {
                rate = 1.0f;
            }
            if( 0.5f <= rate && rate <= 2.0f ) { // OpenAL limits
                playSpeed = rate;
                al.alSourcef(alSource, ALConstants.AL_PITCH, playSpeed);
                return true;
            }
        } finally {
            unlockContext();
        }
        return false;
    }

    @Override
    public final float getVolume() {
        return volume;
    }

    @Override
    public final boolean setVolume(float v) {
        if( !available || null == chosenFormat ) {
            return false;
        }
        lockContext();
        try {
            if( Math.abs(v) < 0.01f ) {
                v = 0.0f;
            } else if( Math.abs(1.0f - v) < 0.01f ) {
                v = 1.0f;
            }
            if( 0.0f <= v && v <= 1.0f ) { // OpenAL limits
                volume = v;
                al.alSourcef(alSource, ALConstants.AL_GAIN, v);
                return true;
            }
        } finally {
            unlockContext();
        }
        return false;
    }

    @Override
    public final void flush() {
        if( !available || null == chosenFormat ) {
            return;
        }
        lockContext();
        try {
            // pauseImpl();
            stopImpl(false);
            // Redundant: dequeueBuffer( false /* wait */, true /* ignoreBufferInconsistency */);
            dequeueForceAll();
            if( alBufferNames.length != alFramesAvail.size() || alFramesPlaying.size() != 0 ) {
                throw new InternalError("XXX: "+this);
            }
            if( DEBUG ) {
                System.err.println(getThreadName()+": ALAudioSink: FLUSH playImpl "+(ALConstants.AL_PLAYING == getSourceState(false))+", "+this);
            }
        } finally {
            unlockContext();
        }
    }

    @Override
    public final int getEnqueuedFrameCount() {
        return enqueuedFrameCount;
    }

    @Override
    public final int getFrameCount() {
        return null != alBufferNames ? alBufferNames.length : 0;
    }

    @Override
    public final int getQueuedFrameCount() {
        if( !available || null == chosenFormat ) {
            return 0;
        }
        return alFramesPlaying.size();
    }

    @Override
    public final int getFreeFrameCount() {
        if( !available || null == chosenFormat ) {
            return 0;
        }
        return alFramesAvail.size();
    }

    @Override
    public final int getQueuedByteCount() {
        if( !available || null == chosenFormat ) {
            return 0;
        }
        return alBufferBytesQueued;
    }

    @Override
    public final int getQueuedTime() {
        if( !available || null == chosenFormat ) {
            return 0;
        }
        return chosenFormat.getBytesDuration(alBufferBytesQueued);
    }

    @Override
    public final int getPTS() { return playingPTS; }

    private static final String toHexString(final int v) { return "0x"+Integer.toHexString(v); }
    private static final String getThreadName() { return Thread.currentThread().getName(); }
}
