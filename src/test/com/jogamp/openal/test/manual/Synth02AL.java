/**
 * Copyright 2023 JogAmp Community. All rights reserved.
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
package com.jogamp.openal.test.manual;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.concurrent.TimeUnit;

import com.jogamp.common.av.AudioFormat;
import com.jogamp.common.av.AudioSink;
import com.jogamp.common.av.AudioSinkFactory;
import com.jogamp.common.nio.Buffers;
import com.jogamp.common.os.Clock;
import com.jogamp.common.util.InterruptSource;
import com.jogamp.common.util.InterruptedRuntimeException;
import com.jogamp.common.util.SourcedInterruptedException;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.JoalVersion;

/**
 * A continuous simple off-thread mutable sine wave synthesizer.
 * <p>
 * Implementation utilizes an off-thread worker thread,
 * allowing to change frequency and amplitude without disturbance.
 * </p>
 * <p>
 * Latency is hardcoded as 1 - 3 times frameDuration, having a frameDuration of 12 ms.
 * Averages around 24 ms.
 * </p>
 * <p>
 * Latency needs improvement to have a highly responsive life-music synthesizer.
 * </p>
 */
public final class Synth02AL {
    private static final boolean DEBUG = false;

    /** The value PI, i.e. 180 degrees in radians. */
    private static final float PI = 3.14159265358979323846f;

    /** The value 2PI, i.e. 360 degrees in radians. */
    private static final float TWO_PI = 2f * PI;

    private static final float EPSILON = 1.1920929E-7f; // Float.MIN_VALUE == 1.4e-45f ; double EPSILON 2.220446049250313E-16d

    private static final float SHORT_MAX = 32767.0f; // == Short.MAX_VALUE

    public static final int frameDuration = 12; // AudioSink.DefaultFrameDuration; // [ms]

    public static final int audioQueueLimit = 3 * frameDuration;

    public static final float MIDDLE_C = 261.625f;

    private final Object stateLock = new Object();
    private volatile float audioAmplitude = 1.0f;
    private volatile float audioFreq = MIDDLE_C;
    private volatile int lastAudioPTS = 0;
    private SynthWorker streamWorker;

    public Synth02AL() {
        streamWorker = new SynthWorker();
    }

    public void setFreq(final float f) {
        audioFreq = f;
    }
    public float getFreq() { return audioFreq; }

    public void setAmplitude(final float a) {
        audioAmplitude = Math.min(1.0f, Math.max(0.0f, a)); // clip [0..1]
    }
    public float getAmplitude() { return audioAmplitude; }

    public void play() {
        synchronized( stateLock ) {
            if( null == streamWorker ) {
                streamWorker = new SynthWorker();
            }
            streamWorker.doResume();
        }
    }

    public void pause(final boolean flush) {
        synchronized( stateLock ) {
            if( null != streamWorker ) {
                streamWorker.doPause(true);
            }
        }
    }

    public void stop() {
        synchronized( stateLock ) {
            if( null != streamWorker ) {
                streamWorker.doStop();
                streamWorker = null;
            }
        }
    }

    public boolean isPlaying() {
        synchronized( stateLock ) {
            if( null != streamWorker ) {
                return streamWorker.isPlaying();
            }
        }
        return false;
    }

    public boolean isRunning() {
        synchronized( stateLock ) {
            if( null != streamWorker ) {
                return streamWorker.isRunning();
            }
        }
        return false;
    }

    public int getGenPTS() { return lastAudioPTS; }

    public int getPTS() { return null != streamWorker ? streamWorker.audioSink.getPTS() : 0; }

    @Override
    public final String toString() {
        synchronized( stateLock ) {
            final String as = null != streamWorker ? streamWorker.audioSink.toString() : "ALAudioSink[null]";
            final int pts = getPTS();
            final int lag = getGenPTS() - pts;
            return getClass().getSimpleName()+"[f "+audioFreq+", a "+audioAmplitude+", state[running "+isRunning()+", playing "+isPlaying()+"], pts[gen "+getGenPTS()+", play "+pts+", lag "+lag+"], "+as+"]";
        }
    }

    private static ByteBuffer allocate(final int size) {
        // return ByteBuffer.allocate(size);
        return Buffers.newDirectByteBuffer(size);
    }

    class SynthWorker extends InterruptSource.Thread {
        private volatile boolean isRunning = false;
        private volatile boolean isPlaying = false;
        private volatile boolean isBlocked = false;

        private volatile boolean shallPause = true;
        private volatile boolean shallStop = false;

        private final AudioSink audioSink;
        private final boolean useFloat32SampleType;
        private final int bytesPerSample;
        private final AudioFormat audioFormat;
        private ByteBuffer sampleBuffer = allocate(2*1000);
        private float lastFreq;
        private float nextSin;
        private boolean upSin;
        private int nextStep;

        /**
         * Starts this daemon thread,
         * <p>
         * This thread pauses after it's started!
         * </p>
         **/
        SynthWorker() {
            setDaemon(true);
            synchronized(this) {
                lastAudioPTS = 0;
                audioSink = AudioSinkFactory.createDefault(Synth02AL.class.getClassLoader());

                // Note: float32 is OpenAL-Soft's internally used format to mix samples etc.
                final AudioFormat f32 = new AudioFormat(audioSink.getPreferredSampleRate(), 4<<3, 1, true /* signed */,
                                                        false /* fixed point */, false /* planar */, true /* littleEndian */);
                if( audioSink.isSupported(f32) ) {
                    useFloat32SampleType = true;
                    bytesPerSample = 4;
                    audioFormat = f32;
                } else {
                    useFloat32SampleType = false;
                    bytesPerSample = 2;
                    audioFormat = new AudioFormat(audioSink.getPreferredSampleRate(), bytesPerSample<<3, 1, true /* signed */,
                                                  true /* fixed point */, false /* planar */, true /* littleEndian */);
                }
                System.err.println("OpenAL float32 supported: "+useFloat32SampleType);

                sampleBuffer = allocate(bytesPerSample*1000);
                audioSink.init(audioFormat, frameDuration, audioQueueLimit, 0, audioQueueLimit);
                lastFreq = 0;
                nextSin = 0;
                upSin = true;
                nextStep = 0;
                start();
                try {
                    this.notifyAll();  // wake-up startup-block
                    while( !isRunning && !shallStop ) {
                        this.wait();  // wait until started
                    }
                } catch (final InterruptedException e) {
                    throw new InterruptedRuntimeException(e);
                }
            }
        }

        private final int findNextStep(final boolean upSin, final float nextSin, final float freq, final int sampleRate, final int sampleCount) {
            final float sample_step = ( TWO_PI * freq ) / sampleRate;

            float s_diff = Float.MAX_VALUE;
            float s_best = 0;
            int i_best = -1;
            float s0 = 0;
            for(int i=0; i < sampleCount && s_diff >= EPSILON ; ++i) {
                final float s1 = (float) Math.sin( sample_step * i );
                final float s_d = Math.abs(nextSin - s1);
                if( s_d < s_diff && ( ( upSin && s1 >= s0 ) || ( !upSin && s1 < s0 ) ) ) {
                    s_best = s1;
                    s_diff = s_d;
                    i_best = i;
                }
                s0 = s1;
            }
            if( DEBUG ) {
                System.err.printf("%nBest: %d/[%d..%d]: s %f / %f (up %b), s_diff %f%n", i_best, 0, sampleCount, s_best, nextSin, upSin, s_diff);
            }
            return i_best;
        }

        private final void enqueueWave() {
            // use local cache of volatiles, stable values
            final float freq = audioFreq;
            final float amp = audioAmplitude;

            final float period = 1.0f / freq; // [s]
            final float sample_step = ( TWO_PI * freq ) / audioFormat.sampleRate;

            final float duration = frameDuration / 1000.0f; // [s]
            final int sample_count = (int)( duration * audioFormat.sampleRate ); // [n]

            final boolean overflow;
            final boolean changedFreq;
            if( Math.abs( freq - lastFreq ) >= EPSILON ) {
                changedFreq = true;
                overflow = false;
                lastFreq = freq;
                nextStep = findNextStep(upSin, nextSin, freq, audioFormat.sampleRate, sample_count);
            } else {
                changedFreq = false;
                if( nextStep + sample_count >= Integer.MAX_VALUE/1000 ) {
                    nextStep = findNextStep(upSin, nextSin, freq, audioFormat.sampleRate, sample_count);
                    overflow = true;
                } else {
                    overflow = false;
                }
            }

            if( DEBUG ) {
                if( changedFreq || overflow ) {
                    final float wave_count = duration / period;
                    System.err.printf("%nFreq %f Hz, period %f [ms], waves %.2f, duration %f [ms], sample[count %d, rate %d, step %f, next[up %b, sin %f, step %d]]%n", freq,
                            1000.0*period, wave_count, 1000.0*duration, sample_count, audioFormat.sampleRate, sample_step, upSin, nextSin, nextStep);
                    // System.err.println(Synth02AL.this.toString());
                }
            }

            if( sampleBuffer.capacity() < bytesPerSample*sample_count ) {
                if( DEBUG ) {
                    System.err.printf("SampleBuffer grow: %d -> %d%n", sampleBuffer.capacity(), bytesPerSample*sample_count);
                }
                sampleBuffer = allocate(bytesPerSample*sample_count);
            }

            {
                int i;
                float s = 0;
                if( useFloat32SampleType ) {
                    final FloatBuffer f32sb = sampleBuffer.asFloatBuffer();
                    final int l = nextStep;
                    for(i=l; i<l+sample_count; ++i) {
                        s = (float) Math.sin( sample_step * i );
                        f32sb.put(s);
                    }
                } else {
                    final int l = nextStep;
                    for(i=l; i<l+sample_count; ++i) {
                        s = (float) Math.sin( sample_step * i );
                        final short s16 = (short)( SHORT_MAX * s * amp );
                        sampleBuffer.put( (byte) ( s16 & 0xff ) );
                        sampleBuffer.put( (byte) ( ( s16 >>> 8 ) & 0xff ) );
                    }
                }
                nextStep = i;
                nextSin = (float) Math.sin( sample_step * nextStep );
                upSin = nextSin >= s;
            }
            sampleBuffer.rewind();
            audioSink.enqueueData(lastAudioPTS, sampleBuffer, sample_count*bytesPerSample);
            sampleBuffer.clear();
            lastAudioPTS += frameDuration;
        }

        public final synchronized void doPause(final boolean waitUntilDone) {
            if( isPlaying ) {
                shallPause = true;
                if( java.lang.Thread.currentThread() != this ) {
                    if( isBlocked && isPlaying ) {
                        this.interrupt();
                    }
                    if( waitUntilDone ) {
                        try {
                            while( isPlaying && isRunning ) {
                                this.wait(); // wait until paused
                            }
                        } catch (final InterruptedException e) {
                            throw new InterruptedRuntimeException(e);
                        }
                    }
                }
            }
        }
        public final synchronized void doResume() {
            if( isRunning && !isPlaying ) {
                shallPause = false;
                if( java.lang.Thread.currentThread() != this ) {
                    try {
                        this.notifyAll();  // wake-up pause-block
                        while( !isPlaying && !shallPause && isRunning ) {
                            this.wait(); // wait until resumed
                        }
                    } catch (final InterruptedException e) {
                        final InterruptedException e2 = SourcedInterruptedException.wrap(e);
                        doPause(false);
                        throw new InterruptedRuntimeException(e2);
                    }
                }
            }
        }
        public final synchronized void doStop() {
            if( isRunning ) {
                shallStop = true;
                if( java.lang.Thread.currentThread() != this ) {
                    if( isBlocked && isRunning ) {
                        this.interrupt();
                    }
                    try {
                        this.notifyAll();  // wake-up pause-block (opt)
                        while( isRunning ) {
                            this.wait();  // wait until stopped
                        }
                    } catch (final InterruptedException e) {
                        throw new InterruptedRuntimeException(e);
                    }
                }
            }
            audioSink.destroy();
        }
        public final boolean isRunning() { return isRunning; }
        public final boolean isPlaying() { return isPlaying; }

        @Override
        public final void run() {
            setName(getName()+"-SynthWorker_"+SynthWorkerInstanceId);
            SynthWorkerInstanceId++;

            audioSink.lockExclusive();

            synchronized ( this ) {
                isRunning = true;
                this.notifyAll(); // wake-up ctor()
            }

            while( !shallStop ) {
                if( shallPause ) {
                    synchronized ( this ) {
                        while( shallPause && !shallStop ) {
                            audioSink.pause();
                            isPlaying = false;
                            this.notifyAll(); // wake-up doPause()
                            try {
                                this.wait();  // wait until resumed
                            } catch (final InterruptedException e) {
                                if( !shallPause ) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        audioSink.play();
                        isPlaying = true;
                        this.notifyAll(); // wake-up doResume()
                    }
                }
                if( !shallStop ) {
                    isBlocked = true;
                    enqueueWave();
                    isBlocked = false;
                }
            }

            audioSink.unlockExclusive();

            synchronized ( this ) {
                isRunning = false;
                isPlaying = false;
                this.notifyAll(); // wake-up doStop()
            }
        }
    }
    static int SynthWorkerInstanceId = 0;

    public static float atof(final String str, final float def) {
        try {
            return Float.parseFloat(str);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return def;
    }

    public static String enterValue(final String message) {
        final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        System.err.println(message);
        try {
            final String s = stdin.readLine();
            System.err.println(s);
            return s;
        } catch (final IOException e) { e.printStackTrace(); }
        return "";
    }

    public static void main(final String[] args) {
        float freq = MIDDLE_C;
        for(int i=0; i<args.length; i++) {
            if(args[i].equals("-f")) {
                i++;
                freq = atof(args[i], freq);
            }
        }
        System.err.println(JoalVersion.getInstance().toString(ALFactory.getALC()));

        final Synth02AL o = new Synth02AL();
        o.setFreq(freq);
        System.err.println("0: "+o);
        o.play();
        System.err.println("1: "+o);
        enterValue("Press enter to start");
        {
            final float min = 100, max = 10000, step = 30;
            final long t0 = Clock.currentNanos();
            for(float f=min; f<max; f+=step) {
                o.setFreq(f);
                try {
                    Thread.sleep(frameDuration);
                } catch (final InterruptedException e) { }
            }
            final long t1 = Clock.currentNanos();
            final int exp = (int)( (max - min) / step ) * frameDuration;
            final int has = (int)TimeUnit.NANOSECONDS.toMillis(t1-t0);
            final int diff = has - exp;
            System.err.println("Loop "+has+" / "+exp+" [ms], diff "+diff+" [ms], "+((float)diff/(float)exp) * 100f+"%");
        }
        o.setFreq( MIDDLE_C );
        System.err.println("c: "+o);
        boolean quit = false;
        while ( !quit ){
            final String in = enterValue("Enter new frequency or just enter to exit");
            if( 0 == in.length() ) {
                quit = true;
            } else {
                freq = atof(in, freq);
                o.setFreq(freq);
                System.err.println("n: "+o);
            }
        }
        o.stop();
    }
}
