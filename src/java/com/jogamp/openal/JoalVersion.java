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

package com.jogamp.openal;

import com.jogamp.common.GlueGenVersion;

import com.jogamp.common.os.Platform;
import com.jogamp.common.util.VersionUtil;
import com.jogamp.common.util.JogampVersion;
import com.jogamp.openal.util.ALHelpers;

import java.util.jar.Manifest;

public class JoalVersion extends JogampVersion {
    protected static volatile JoalVersion jogampCommonVersionInfo;

    protected JoalVersion(final String packageName, final Manifest mf) {
        super(packageName, mf);
    }

    public static JoalVersion getInstance() {
        if(null == jogampCommonVersionInfo) { // volatile: ok
            synchronized(JoalVersion.class) {
                if( null == jogampCommonVersionInfo ) {
                    final String packageName = "com.jogamp.openal";
                    final Manifest mf = VersionUtil.getManifest(JoalVersion.class.getClassLoader(), packageName);
                    jogampCommonVersionInfo = new JoalVersion(packageName, mf);
                }
            }
        }
        return jogampCommonVersionInfo;
    }

    public StringBuilder getBriefOSALBuildInfo(StringBuilder sb) {
        if(null==sb) {
            sb = new StringBuilder();
        }
        sb.append("OS: ").append(Platform.getOSName()).append(", version ").append(Platform.getOSVersion()).append(", arch ").append(Platform.getArchName());
        sb.append(Platform.getNewline());
        sb.append("JOAL GIT sha1 ").append(getImplementationCommit());
        sb.append(Platform.getNewline());
        return sb;
    }

    /**
     * Return {@link JogampVersion} package information and AL informal strings.
     * <p>
     * The given {@link ALC} is being used and {@Link ALCdevice} and {@link ALCcontext} are allocated,
     * made current and finally being released.
     * </p>
     * @param alc static {@link ALC} instance
     * @param sb optional StringBuffer to be reused
     */
    public StringBuilder toString(final ALC alc, StringBuilder sb) {
        sb = super.toString(sb).append(Platform.getNewline());
        getALStrings(alc, sb);
        return sb;
    }

    /**
     * Return {@link JogampVersion} package information and AL informal strings.
     * <p>
     * The given {@link ALC} is being used and {@Link ALCdevice} and {@link ALCcontext} are allocated,
     * made current and finally being released.
     * </p>
     * @param alc static {@link ALC} instance
     */
    public String toString(final ALC alc) {
        return toString(alc, null).toString();
    }

    /**
     * Return AL informal strings.
     * <p>
     * The given {@link ALC} is being used and {@Link ALCdevice} and {@link ALCcontext} are allocated,
     * made current and finally being released.
     * </p>
     * @param alc static {@link ALC} instance
     */
    public static StringBuilder getALStrings(final ALC alc, StringBuilder sb) {
        if( null == sb ) {
            sb = new StringBuilder();
        }
        if( null == alc ) {
            sb.append("ALC null");
            return sb;
        }
        final ALCcontext initialContext = alc.alcGetCurrentContext();

        final ALCcontext context;
        final ALCdevice device;
        if( null == initialContext) {
            device = alc.alcOpenDevice(null);
            context = alc.alcCreateContext(device, null);
            alc.alcMakeContextCurrent(context);
        } else {
            context = initialContext;
            device = alc.alcGetContextsDevice(initialContext);
        }
        final AL al = ALFactory.getAL(); // valid after makeContextCurrent(..)
        final ALVersion alv = new ALVersion(al);

        alv.toString(true, sb);
        sb.append("AL_EXTENSIONS  ").append(al.alGetString(ALConstants.AL_EXTENSIONS));
        sb.append(Platform.getNewline());
        final boolean enumerationExtIsPresent = alc.aclEnumerationExtIsPresent();
        final boolean enumerateAllExtIsPresent = alc.aclEnumerateAllExtIsPresent();
        {
            final int[] iversion = { 0, 0 };
            alc.alcGetIntegerv(device, ALCConstants.ALC_MAJOR_VERSION, 1, iversion, 0);
            alc.alcGetIntegerv(device, ALCConstants.ALC_MINOR_VERSION, 1, iversion, 1);
            sb.append("ALC_VERSION     ").append(iversion[0]).append(".").append(iversion[1]);
            sb.append(Platform.getNewline());
            if (!enumerationExtIsPresent && !enumerateAllExtIsPresent) {
                sb.append("ALC_DEF_OUTPUT Unknown (Missing "+
                        ALHelpers.ALC_ENUMERATION_EXT+" and "+ALHelpers.ALC_ENUMERATE_ALL_EXT+")");
                sb.append(Platform.getNewline());
            } else {
                if (enumerationExtIsPresent) {
                    sb.append("ALC_DEF_OUTPUT (With " + ALHelpers.ALC_ENUMERATION_EXT + ") ")
                            .append(alc.alcGetString(device, ALCConstants.ALC_DEFAULT_DEVICE_SPECIFIER));
                    sb.append(Platform.getNewline());
                }
                if (enumerateAllExtIsPresent) {
                    sb.append("ALC_DEF_OUTPUT (With " + ALHelpers.ALC_ENUMERATE_ALL_EXT + ") ")
                            .append(alc.alcGetString(device, ALCConstants.ALC_DEFAULT_ALL_DEVICES_SPECIFIER));
                    sb.append(Platform.getNewline());
                }
            }
            if (enumerationExtIsPresent) {
                sb.append("ALC_DEF_CAPTURE ").append(alc.alcGetString(device, ALCConstants.ALC_CAPTURE_DEFAULT_DEVICE_SPECIFIER));
                sb.append(Platform.getNewline());
            } else {
                sb.append("ALC_DEF_CAPTURE Unknown (Missing "+ALHelpers.ALC_ENUMERATION_EXT+")");
                sb.append(Platform.getNewline());
            }
        }

        if( null == initialContext ) {
            alc.alcMakeContextCurrent(null);
            alc.alcDestroyContext(context);
            alc.alcCloseDevice(device);
        }

        devicesToString(sb, alc, enumerationExtIsPresent, enumerateAllExtIsPresent);
        return sb;
    }

    private static boolean checkALCError(final ALC alc, final ALCdevice device, final String msg)
    {
      final int error = alc.alcGetError(device);
      if (error != ALCConstants.ALC_NO_ERROR) {
        System.err.printf("ALC Error 0x%x occurred: '%s' while '%s'%n", error, alc.alcGetString(device, error), msg);
        return true;
      }
      return false;
    }

    public static void deviceToString(final StringBuilder sb, final ALC alc, final String devName, final boolean isInput, final String defOutDeviceName, final String defInDeviceName) {
        if( isInput ) {
            final boolean isDefault = devName.equals(defInDeviceName);
            sb.append("    "+devName+", input, default "+isDefault+System.lineSeparator());
        } else {
            final boolean isDefault = devName.equals(defOutDeviceName);
            final String defStr = isDefault ? "default " : "";
            final String inOutStr = "output";
            final int mixerFrequency, mixerRefresh, monoSourceCount, stereoSourceCount;
            final int[] val = { 0 };
            final ALCcontext initialContext = alc.alcGetCurrentContext();
            final ALCdevice initialDevice = initialContext != null ? alc.alcGetContextsDevice(initialContext) : null;
            final ALCdevice d = alc.alcOpenDevice(devName);
            if( null == d ) {
                System.err.println("Error: Failed to open "+defStr+inOutStr+" device "+devName);
                return;
            }
            final ALCcontext c = alc.alcCreateContext(d, null);
            if( null == c ) {
                System.err.println("Error: Failed to create context for "+defStr+inOutStr+" device "+devName);
                alc.alcCloseDevice(d);
                return;
            }
            if( !alc.alcMakeContextCurrent(c) ) {
                System.err.println("Error: Failed to make context current for "+defStr+inOutStr+" device "+devName);
                checkALCError(alc, d, "alcMakeContextCurrent");
                alc.alcDestroyContext(c);
                alc.alcCloseDevice(d);
                return;
            }

            {
                val[0] = 0;
                alc.alcGetIntegerv(d, ALCConstants.ALC_FREQUENCY, 1, val, 0);
                if( checkALCError(alc, d, "read ALC_FREQUENCY") ) {
                    mixerFrequency = -1;
                } else {
                    mixerFrequency = val[0];
                }
            }
            {
                val[0] = 0;
                alc.alcGetIntegerv(d, ALCConstants.ALC_REFRESH, 1, val, 0);
                if( checkALCError(alc, d, "read ALC_REFRESH") ) {
                    mixerRefresh = -1;
                } else {
                    mixerRefresh = val[0];
                }
            }
            {
                val[0] = 0;
                alc.alcGetIntegerv(d, ALCConstants.ALC_MONO_SOURCES, 1, val, 0);
                if( checkALCError(alc, d, "read ALC_MONO_SOURCES") ) {
                    monoSourceCount = -1;
                } else {
                    monoSourceCount = val[0];
                }
            }
            {
                val[0] = 0;
                alc.alcGetIntegerv(d, ALCConstants.ALC_STEREO_SOURCES, 1, val, 0);
                if( checkALCError(alc, d, "read ALC_STEREO_SOURCES") ) {
                    stereoSourceCount = -1;
                } else {
                    stereoSourceCount = val[0];
                }
            }
            sb.append("    "+devName+", "+inOutStr+", default "+isDefault+", mixer[freq "+mixerFrequency+", refresh "+mixerRefresh+
                    " (min latency "+(1000f/mixerRefresh)+" ms)], sources[mono "+monoSourceCount+", stereo "+stereoSourceCount+"]"+
                    System.lineSeparator());

            if( null != initialContext ) {
                alc.alcMakeContextCurrent(initialContext);
                if( initialContext.getDirectBufferAddress() != c.getDirectBufferAddress() ) {
                    alc.alcDestroyContext(c);
                }
            } else {
                alc.alcMakeContextCurrent(null);
                alc.alcDestroyContext(c);
            }
            if( initialDevice == null || initialDevice.getDirectBufferAddress() != d.getDirectBufferAddress() ) {
                alc.alcCloseDevice(d);
            }
        }
    }

    public static void devicesToString(final StringBuilder sb, final ALC alc, final boolean enumerationExtIsPresent, final boolean enumerateAllExtIsPresent) {
        if (!enumerationExtIsPresent && !enumerateAllExtIsPresent) {
            sb.append("No output devices infos available (Missing "+
                    ALHelpers.ALC_ENUMERATION_EXT+" and "+ALHelpers.ALC_ENUMERATE_ALL_EXT+")");
        } else {
            if (enumerateAllExtIsPresent) {
                final String defOutAllDeviceName = alc.alcGetString(null, ALCConstants.ALC_DEFAULT_ALL_DEVICES_SPECIFIER);
                sb.append("Output devices (With " + ALHelpers.ALC_ENUMERATE_ALL_EXT + "):" + System.lineSeparator());
                {
                    final String[] outDevices = alc.alcGetAllDeviceSpecifiers();
                    if (null != outDevices) {
                        for (final String devName : outDevices) {
                            deviceToString(sb, alc, devName, false, defOutAllDeviceName, null);
                        }
                    }
                }
            }
            if (enumerationExtIsPresent) {
                final String defOutDeviceName = alc.alcGetString(null, ALCConstants.ALC_DEFAULT_DEVICE_SPECIFIER);
                sb.append("Output devices (With " + ALHelpers.ALC_ENUMERATION_EXT + "):" + System.lineSeparator());
                {
                    final String[] outDevices = alc.alcGetDeviceSpecifiers();
                    if (null != outDevices) {
                        for (final String devName : outDevices) {
                            deviceToString(sb, alc, devName, false, defOutDeviceName, null);
                        }
                    }
                }
            }
        }
        if (!enumerationExtIsPresent) {
            sb.append("No capture devices infos available (Missing " + ALHelpers.ALC_ENUMERATION_EXT + ")");
        } else {
            final String defInDeviceName = alc.alcGetString(null, ALCConstants.ALC_CAPTURE_DEFAULT_DEVICE_SPECIFIER);
            sb.append("Capture devices:" + System.lineSeparator());
            {
                final String[] inDevices = alc.alcGetCaptureDeviceSpecifiers();
                if (null != inDevices) {
                    for (final String devName : inDevices) {
                        deviceToString(sb, alc, devName, true, null, defInDeviceName);
                    }
                }
            }
        }
    }

    public static void main(final String args[]) {
        System.err.println(VersionUtil.getPlatformInfo());
        System.err.println(GlueGenVersion.getInstance());
        // System.err.println(NativeWindowVersion.getInstance());
        // System.err.println(JoalVersion.getInstance());
        System.err.println(JoalVersion.getInstance().toString(ALFactory.getALC()));

    }
}

