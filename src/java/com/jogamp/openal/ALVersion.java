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
package com.jogamp.openal;

import com.jogamp.common.os.Platform;
import com.jogamp.common.util.VersionNumber;
import com.jogamp.common.util.VersionNumberString;

public class ALVersion {
    private final String vendor;
    private final String renderer;
    private final VersionNumberString version;
    private final VersionNumberString vendorVersion;

    /**
     * Returns the optional vendor version at the end of the
     * <code>AL_VERSION</code> string if exists, otherwise the {@link VersionNumberString#zeroVersion zero version} instance.
     * <pre>
     *   1.1.0 (1.1 ALSOFT 1.23.1)
     * </pre>
     */
    private static final VersionNumberString getVendorVersion(final VersionNumberString version) {
        // Skip the 1st AL version
        String str = version.getVersionString().substring(version.endOfStringMatch()).trim();
        while ( str.length() > 0 ) {
            final VersionNumberString vv = new VersionNumberString(str, VersionNumber.getDefaultVersionNumberPattern());
            final int eosm = vv.endOfStringMatch();
            if( 0 < eosm ) {
                if( vv.hasMajor() && vv.hasMinor() ) { // Requires at least a defined major and minor version component!
                    return vv;
                }
                str = str.substring( eosm ).trim();
            } else {
                break; // no match
            }
        }
        return VersionNumberString.zeroVersion;
    }

    /**
     * ALVersion Ctor
     * <p>
     * The given {@link AL} is being used and assumed having a current {@link ALCcontext},
     * w/o any resources being allocated.
     * </p>
     * @param al {@link AL} instance with a current {@link ALCcontext}.
     */
    public ALVersion(final AL al) {
        vendor = al.alGetString(ALConstants.AL_VENDOR);
        renderer = al.alGetString(ALConstants.AL_RENDERER);
        version = new VersionNumberString(al.alGetString(ALConstants.AL_VERSION));
        vendorVersion = getVendorVersion(version);
    }

    /**
     * ALVersion Ctor
     * <p>
     * The given {@link ALC} is being used and {@Link ALCdevice} and {@link ALCcontext} are allocated,
     * made current and finally being released.
     * </p>
     * @param alc static {@link ALC} instance
     */
    public ALVersion(final ALC alc) {
        final ALCdevice device = alc.alcOpenDevice(null);
        final ALCcontext context = alc.alcCreateContext(device, null);
        alc.alcMakeContextCurrent(context);
        final AL al = ALFactory.getAL(); // valid after makeContextCurrent(..)
        vendor = al.alGetString(ALConstants.AL_VENDOR);
        renderer = al.alGetString(ALConstants.AL_RENDERER);
        version = new VersionNumberString(al.alGetString(ALConstants.AL_VERSION));
        vendorVersion = getVendorVersion(version);
        alc.alcMakeContextCurrent(null);
        alc.alcDestroyContext(context);
        alc.alcCloseDevice(device);
    }

    /**
     * Return the AL context implementation vendor.
     */
    public String getVendor() { return vendor; }

    /**
     * Return the AL context implementation renderer.
     */
    public String getRenderer() { return renderer; }

    /**
     * Return the AL context implementation version.
     */
    public VersionNumberString getVersion() { return version; }

    /**
     * Returns the optional vendor version at the end of the
     * <code>AL_VERSION</code> string if exists, otherwise the {@link VersionNumberString#zeroVersion zero version} instance.
     * <pre>
     *   1.1.0 (1.1 ALSOFT 1.23.1)
     * </pre>
     */
    public VersionNumberString getVendorVersion() { return vendorVersion; }

    @Override
    public String toString() {
        return toString(false, null).toString();
    }

    public StringBuilder toString(final boolean withNewline, StringBuilder sb) {
        if( null == sb ) {
            sb = new StringBuilder();
        }
        if( withNewline ) {
            sb.append("AL_VENDOR      ").append(getVendor());
            sb.append(Platform.getNewline());
            sb.append("AL_RENDERER    ").append(getRenderer());
            sb.append(Platform.getNewline());
            sb.append("AL_VERSION     ").append(getVersion());
            sb.append(Platform.getNewline());
            sb.append("AL_VENDOR_VERS ").append(getVendorVersion());
            sb.append(Platform.getNewline());
        } else {
            sb.append("vendor ").append(getVendor());
            sb.append(", renderer ").append(getRenderer());
            sb.append(", version ").append(getVersion());
            sb.append(", vendorVersion ").append(getVendorVersion());
        }
        return sb;
    }
}
