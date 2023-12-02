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
package com.jogamp.openal.test.junit;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.After;
import org.junit.Assert;
import org.junit.runners.MethodSorters;

import com.jogamp.openal.AL;
import com.jogamp.openal.ALC;
import com.jogamp.openal.ALCcontext;
import com.jogamp.openal.ALCdevice;
import com.jogamp.openal.ALExt;
import com.jogamp.openal.ALExtConstants;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.sound3d.AudioSystem3D;
import com.jogamp.openal.test.util.UITestCase;
import com.jogamp.openal.util.ALHelpers;

/**
 * Testing the OpenAL-Soft Debug Extension.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ALDebugExtTest extends UITestCase {

    @Test
    public void test01DebugExt() {
        final ALC alc = ALFactory.getALC();
        final ALCdevice device = alc.alcOpenDevice(null);

        if( !alc.alcIsExtensionPresent(device, ALHelpers.ALC_EXT_debug) ) {
            System.err.println("ALCdevice has no "+ALHelpers.ALC_EXT_debug+" extension");
            return;
        }
        final int[] attributes = new int[] { ALExtConstants.ALC_CONTEXT_FLAGS_EXT, ALExtConstants.ALC_CONTEXT_DEBUG_BIT_EXT };
        final ALCcontext context = alc.alcCreateContext(device, attributes, 0);
        if( !alc.alcMakeContextCurrent(context) ) {
            System.err.println("makeCurrent() failed");
            return;
        }
        // final JoalVersion jv = JoalVersion.getInstance();
        // System.err.println(jv.toString(alc));
        System.err.println("-----------------------------------------------------------------------------------------------------");

        final AL al = ALFactory.getAL();
        if( !al.alIsExtensionPresent(ALHelpers.AL_EXT_debug) ) {
            System.err.println("Current context has no "+ALHelpers.AL_EXT_debug+" extension");
            return;
        }
        final AtomicInteger dbg_counter = new AtomicInteger(0);
        final AtomicReference<String> dbg_msg = new AtomicReference<String>();
        final ALExt.ALDEBUGPROCEXT debug_cb = new ALExt.ALDEBUGPROCEXT() {
            @Override
            public void callback(final int source, final int type, final int id, final int severity, final String message, final ALCcontext userParam) {
                final int count = dbg_counter.incrementAndGet();
                if( 1 == count ) {
                    dbg_msg.set(message);
                }
                System.err.println("AL-Debug["+count+"]: src 0x"+Integer.toHexString(source)+
                                          ", type 0x"+Integer.toHexString(type)+
                                          ", id "+id+
                                          ", severity 0x"+Integer.toHexString(severity)+", msg '"+message+"', ctx "+userParam);
            }
        };
        final String expMessage = "Test Message";
        AudioSystem3D.getALExt().alDebugMessageCallbackEXT(debug_cb, context);
        AudioSystem3D.getALExt().alDebugMessageControlEXT(ALExtConstants.AL_DEBUG_SOURCE_OTHER_EXT,
                                                          ALExtConstants.AL_DEBUG_TYPE_OTHER_EXT,
                                                          ALExtConstants.AL_DEBUG_SEVERITY_NOTIFICATION_EXT,
                                                          0, null, 0,
                                                          true);
        AudioSystem3D.getALExt().alDebugMessageInsertEXT(ALExtConstants.AL_DEBUG_SOURCE_APPLICATION_EXT,
                                                         ALExtConstants.AL_DEBUG_TYPE_OTHER_EXT,
                                                         0,
                                                         ALExtConstants.AL_DEBUG_SEVERITY_NOTIFICATION_EXT, expMessage);
        Assert.assertEquals(1, dbg_counter.get());
        Assert.assertEquals(expMessage, dbg_msg.get());
        alc.alcDestroyContext(context);
        alc.alcCloseDevice(device);
    }

    @After
    @Override
    public void tearDown() {
        super.tearDown();
        final ALC alc = ALFactory.getALC();
        final ALCcontext context = alc.alcGetCurrentContext();
        if(null != context) {
            alc.alcDestroyContext(context);
            final ALCdevice device = alc.alcGetContextsDevice(context);
            if(null != device) {
                alc.alcCloseDevice(device);
            }
        }
    }

    public static void main(final String args[]) throws IOException {
        org.junit.runner.JUnitCore.main(ALDebugExtTest.class.getName());
    }
}
