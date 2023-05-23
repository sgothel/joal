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
import java.util.concurrent.TimeUnit;

import com.jogamp.common.os.Clock;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.JoalVersion;
import com.jogamp.openal.util.SimpleSineSynth;

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
        float freq = SimpleSineSynth.MIDDLE_C;
        for(int i=0; i<args.length; i++) {
            if(args[i].equals("-f")) {
                i++;
                freq = atof(args[i], freq);
            }
        }
        System.err.println(JoalVersion.getInstance().toString(ALFactory.getALC()));

        final SimpleSineSynth o = new SimpleSineSynth();
        o.setFreq(freq);
        o.setAmplitude(0.5f);
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
                    Thread.sleep( o.getLatency() );
                } catch (final InterruptedException e) { }
            }
            final long t1 = Clock.currentNanos();
            final int exp = (int)( (max - min) / step ) * o.getLatency();
            final int has = (int)TimeUnit.NANOSECONDS.toMillis(t1-t0);
            final int diff = has - exp;
            System.err.println("Loop "+has+" / "+exp+" [ms], diff "+diff+" [ms], "+((float)diff/(float)exp) * 100f+"%");
        }
        o.setFreq( SimpleSineSynth.MIDDLE_C );
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
