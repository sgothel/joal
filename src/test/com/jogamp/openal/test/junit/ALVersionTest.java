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
package com.jogamp.openal.test.junit;

import java.io.IOException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.After;
import org.junit.Assert;
import org.junit.runners.MethodSorters;

import com.jogamp.common.util.VersionNumber;
import com.jogamp.openal.ALC;
import com.jogamp.openal.ALCcontext;
import com.jogamp.openal.ALCdevice;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.ALVersion;
import com.jogamp.openal.JoalVersion;
import com.jogamp.openal.test.util.UITestCase;

/**
 * Testing the OpenAL version,
 * comparing against expected OpenAL-Soft version >= 1.1.0 and vendor-version >= 1.23.0,
 * as well as the OpenAL-Soft vendor and renderer strings.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ALVersionTest extends UITestCase {
    static private final String OpenALVendor = "OpenAL Community";
    static private final String OpenALRenderer = "OpenAL Soft";
    static private final VersionNumber ALVersion11 = new VersionNumber(1, 1, 0);
    static private final VersionNumber OpenALVersion1230 = new VersionNumber(1, 23, 0);

    @Test
    public void test01ALVersion() {
        final ALVersion alv = new ALVersion(ALFactory.getALC());
        final int versionComp = alv.getVersion().compareTo(ALVersion11);
        final int vendorVersionComp = alv.getVendorVersion().compareTo(OpenALVersion1230);
        System.out.println("ALVersion: "+alv.toString()+", version >= 1.1: "+versionComp+", vendorVersion >= 1.23.0: "+vendorVersionComp);
        Assert.assertEquals(OpenALVendor, alv.getVendor());
        Assert.assertEquals(OpenALRenderer, alv.getRenderer());
        Assert.assertTrue( versionComp >= 0 );
        Assert.assertTrue( vendorVersionComp >= 0 );
    }

    @Test
    public void test02JoalVersion() {
        final JoalVersion jv = JoalVersion.getInstance();
        System.err.println(jv.toString(ALFactory.getALC()));
    }

    @Test
    public void test03JoalVersionMustNoChangeContextAndDeviceUsed() {
        final ALC alc = ALFactory.getALC();
        final ALCdevice intialDevice = alc.alcOpenDevice(null);
        final ALCcontext initialContext = alc.alcCreateContext(intialDevice, null);
        alc.alcMakeContextCurrent(initialContext);
        final JoalVersion jv = JoalVersion.getInstance();
        System.err.println(jv.toString(alc));
        final ALCcontext currentContext = alc.alcGetCurrentContext();
        Assert.assertNotNull(currentContext);
        Assert.assertEquals(initialContext.getDirectBufferAddress(), currentContext.getDirectBufferAddress());
        final ALCdevice currentDevice = alc.alcGetContextsDevice(currentContext);
        Assert.assertNotNull(currentDevice);
        Assert.assertEquals(intialDevice.getDirectBufferAddress(), currentDevice.getDirectBufferAddress());
    }

    @Test
    public void test04JoalVersionMustNotSetAdditionalContext() {
        final ALC alc = ALFactory.getALC();
        final JoalVersion jv = JoalVersion.getInstance();
        System.err.println(jv.toString(alc));
        final ALCcontext currentContext = alc.alcGetCurrentContext();
        Assert.assertNull(currentContext);
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
        org.junit.runner.JUnitCore.main(ALVersionTest.class.getName());
    }
}
