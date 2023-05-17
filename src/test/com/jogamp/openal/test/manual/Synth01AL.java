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
import java.nio.ShortBuffer;

import com.jogamp.openal.AL;
import com.jogamp.openal.ALC;
import com.jogamp.openal.ALCcontext;
import com.jogamp.openal.ALCdevice;
import com.jogamp.openal.ALConstants;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.ALVersion;

/**
 * A continuous simple on-thread immutable sine wave synthesizer.
 * <p>
 * Implementation simply finds the best loop'able sample-count for a fixed frequency
 * and plays it indefinitely.
 * </p>
 */
public class Synth01AL {
    /** The value PI, i.e. 180 degrees in radians. */
    public static final float PI = 3.14159265358979323846f;

    /** The value 2PI, i.e. 360 degrees in radians. */
    public static final float TWO_PI = 2f * PI;

    private static final float EPSILON = 1.1920929E-7f; // Float.MIN_VALUE == 1.4e-45f ; double EPSILON 2.220446049250313E-16d

    private static final float SHORT_MAX = 32767.0f; // == Short.MAX_VALUE

    public static void waitForKey(final String message) {
        final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        System.err.println("> Press enter to "+message);
        try {
            System.err.println(stdin.readLine());
        } catch (final IOException e) { e.printStackTrace(); }
    }

    private ALC alc = null;
    private ALCdevice device = null;
    private ALCcontext context = null;
    private AL al = null;

    private final int[] buffers = { 0 };
    private final int[] sources = { 0 };

    private void alCheckError(final String given_label, final boolean throwEx) {
        final int error = al.alGetError();
        if(ALConstants.AL_NO_ERROR != error) {
            final String msg = String.format("ERROR - 0x%X %s (%s)", error, al.alGetString(error), given_label);
            System.err.println(msg);
            if( throwEx ) {
                throw new RuntimeException(msg);
            }
        }
    }

    public void init() {
        alc = ALFactory.getALC();
        device = alc.alcOpenDevice(null);
        context = alc.alcCreateContext(device, null);
        alc.alcMakeContextCurrent(context);
        al = ALFactory.getAL(); // valid after makeContextCurrent(..)
        System.out.println("ALVersion: "+new ALVersion(al).toString());
        System.out.println("Output devices:");
        {
            final String[] outDevices = alc.alcGetDeviceSpecifiers();
            if( null != outDevices ) {
                for (final String name : outDevices) {
                    System.out.println("    "+name);
                }
            }
        }
        alCheckError("setup", true);

        al.alGenBuffers(1, buffers, 0);
        alCheckError("alGenBuffers", true);

        // Set-up sound source
        al.alGenSources(1, sources, 0);
        alCheckError("alGenSources", true);
    }

    public void exit() {
        // Stop the sources
        al.alSourceStopv(1, sources, 0);
        for (int ii = 0; ii < 1; ++ii) {
            al.alSourcei(sources[ii], ALConstants.AL_BUFFER, 0);
        }
        alCheckError("sources: stop and disconnected", true);

        // Clean-up
        al.alDeleteSources(1, sources, 0);
        al.alDeleteBuffers(1, buffers, 0);
        alCheckError("sources/buffers: deleted", true);

        if( null != context ) {
            alc.alcMakeContextCurrent(null);
            alc.alcDestroyContext(context);
            context = null;
        }
        if( null != device ) {
            alc.alcCloseDevice(device);
            device = null;
        }
    }

    public static int findBestWaveCount(final float freq, final int sampleRate, final int minWaves, final int maxWaves) {
        final float period = 1.0f / freq; // [s]
        final float sample_step = ( TWO_PI * freq ) / sampleRate;
        int wave_count;
        float s_diff = Float.MAX_VALUE;
        float sc_diff = Float.MAX_VALUE;
        int wc_best = -1;
        for(wave_count = minWaves; wave_count < maxWaves && s_diff >= EPSILON; ++wave_count) {
            final float d = wave_count * period; // duration [s] for 'i' full waves
            final float sc_f = d * sampleRate; // sample count for 'i' full waves [n]
            final int sc_i = (int)sc_f;
            final float s1 = (float) Math.abs( Math.sin( sample_step * sc_i ) ); // last_step + 1 = next wave start == error to zero
            if( s1 < s_diff ) {
                s_diff = s1;
                sc_diff = sc_f - sc_i; // sample_count delta float - int
                wc_best = wave_count;
            }
        }
        System.err.printf("%nBest: %d/[%d..%d], waves %d, sample_count diff %.12f, sample diff %.12f%n", wave_count, minWaves, maxWaves, wc_best, sc_diff, s_diff);
        return wc_best;
    }

    private static final int SAMPLE_RATE = 44100; // [Hz]

    public void loop(final float freq /* [Hz] */) {
        final float period = 1.0f / freq; // [s]
        final float sample_step = ( TWO_PI * freq ) / SAMPLE_RATE;

        final int wave_count = findBestWaveCount(freq, SAMPLE_RATE, 10, 1000);
        final float duration = wave_count * period; // [s], full waves
        final int sample_count = (int)( duration * SAMPLE_RATE ); // [n]

        System.err.printf("%nFreq %f Hz, period %f [ms], waves %d, duration %f [ms], sample[rate %d, step %f]%n", freq, 1000.0*period, wave_count, 1000.0*duration, SAMPLE_RATE, sample_step);

        // allocate PCM audio buffer
        final ShortBuffer samples = ShortBuffer.allocate(sample_count);

        for(int i=0; i<sample_count; ++i) {
            final float s = (float) Math.sin( sample_step * i );
            samples.put( (short)( SHORT_MAX * s ) );
        }
        samples.rewind();
        alCheckError("populating samples", true);

        // upload buffer to OpenAL
        al.alBufferData(buffers[0], ALConstants.AL_FORMAT_MONO16, samples, sample_count*2, SAMPLE_RATE);
        alCheckError("alBufferData samples", true);

        samples.clear();

        // Play source / buffer
        al.alSourcei(sources[0], ALConstants.AL_BUFFER, buffers[0]);
        alCheckError("alSourcei source <-> buffer", true);

        al.alSourcei(sources[0], ALConstants.AL_LOOPING, 1);
        final int[] loopArray = new int[1];
        al.alGetSourcei(sources[0], ALConstants.AL_LOOPING, loopArray, 0);
        System.err.println("Looping 1: " + (loopArray[0] == ALConstants.AL_TRUE));

        al.alSourceRewind(sources[0]);
        al.alSourcePlay(sources[0]);
        alCheckError("alSourcePlay", true);

        // ---------------------

        final int[] current_playing_state = { 0 };
        al.alGetSourcei(sources[0], ALConstants.AL_SOURCE_STATE, current_playing_state, 0);
        alCheckError("alGetSourcei AL_SOURCE_STATE", true);

        if( ALConstants.AL_PLAYING == current_playing_state[0] ) {
            waitForKey("Stop");
        }
    }

    public static float atof(final String str, final float def) {
        try {
            return Float.parseFloat(str);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return def;
    }

    public static void main(final String[] args) {
        float freq = 100.0f;
        for(int i=0; i<args.length; i++) {
            if(args[i].equals("-f")) {
                i++;
                freq = atof(args[i], freq);
            }
        }
        final Synth01AL o = new Synth01AL();
        o.init();
        o.loop(freq);
        o.exit();
    }
}
