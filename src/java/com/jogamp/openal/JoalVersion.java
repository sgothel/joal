/**
 * Copyright 2013 JogAmp Community. All rights reserved.
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

import java.util.jar.Manifest;

public class JoalVersion extends JogampVersion {
    protected static volatile JoalVersion jogampCommonVersionInfo;

    protected JoalVersion(String packageName, Manifest mf) {
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

    public StringBuilder toString(StringBuilder sb) {
        sb = super.toString(sb).append(Platform.getNewline());
        // getGLInfo(gl, sb);
        return sb;
    }

    public String toString() {
        return toString(null).toString();
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
    
    public static void main(String args[]) {
        System.err.println(VersionUtil.getPlatformInfo());
        System.err.println(GlueGenVersion.getInstance());
        // System.err.println(NativeWindowVersion.getInstance());
        System.err.println(JoalVersion.getInstance());
    }
}

