/**
 * Copyright 2010 JogAmp Community. All rights reserved.
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
 
package jogamp.openal;

import com.jogamp.common.jvm.JNILibLoaderBase;
import com.jogamp.common.os.DynamicLibraryBundle;
import com.jogamp.common.os.DynamicLibraryBundleInfo;
import com.jogamp.common.os.Platform;
import com.jogamp.common.util.RunnableExecutor;
import com.jogamp.common.util.cache.TempJarCache;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

public class ALDynamicLibraryBundleInfo implements DynamicLibraryBundleInfo  {
    private static List<String> glueLibNames;
    static {
        AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                Platform.initSingleton();
                
                if(TempJarCache.isInitialized()) {
                   // only: joal.jar -> joal-natives-<os.and.arch>.jar
                   JNILibLoaderBase.addNativeJarLibs(new Class<?>[] { ALDynamicLibraryBundleInfo.class }, null, null );
                }
                return null;
            }
        });
                
        glueLibNames = new ArrayList<String>();
        glueLibNames.add("joal");
    }

    protected ALDynamicLibraryBundleInfo() {
    }

    /** FIXME: not default, maybe local ? **/
    @Override
    public boolean shallLinkGlobal() { return true; }

    /** default **/
    @Override
    public boolean shallLookupGlobal() { return false; }
    
    @Override
    public final List<String> getGlueLibNames() {
        return glueLibNames;
    }

    @Override
    public List<List<String>> getToolLibNames() {
        List<List<String>> libNamesList = new ArrayList<List<String>>();

        List<String> alLibNames = new ArrayList<String>();

        if(Debug.isPropertyDefined("joal.SystemOpenAL", true)) {
            // First test the System OpenAL 
            // this is the default AL lib name, according to the spec
            alLibNames.add("libopenal.so.1"); // unix
            alLibNames.add("OpenAL32"); // windows
            alLibNames.add("OpenAL"); // OSX

            // try this one as well, if spec fails
            alLibNames.add("libOpenAL.so.1");
            alLibNames.add("libopenal.so");
            alLibNames.add("libOpenAL.so");

            // last but not least .. the generic one
            // These names are in use by the bundled OpenAL-soft
            alLibNames.add("openal");
            alLibNames.add("OpenAL");
        } else {
            // First test use of the bundled OpenAL-soft
            // the generic one
            alLibNames.add("openal");
            alLibNames.add("OpenAL");

            // Then try the System OpenAL
            // this is the default AL lib name, according to the spec
            alLibNames.add("libopenal.so.1"); // unix
            alLibNames.add("OpenAL32"); // windows
            alLibNames.add("OpenAL"); // OSX

            // try this one as well, if spec fails
            alLibNames.add("libOpenAL.so.1");
            alLibNames.add("libopenal.so");
            alLibNames.add("libOpenAL.so");
        }

        // last but not least .. the generic one
        alLibNames.add("openal");
        alLibNames.add("OpenAL");

        libNamesList.add(alLibNames);

        return libNamesList;
    }

    @Override
    public final List<String> getToolGetProcAddressFuncNameList() {
        List<String> res = new ArrayList<String>();
        res.add("alGetProcAddress");
        return res;
    }

    @Override
    public final long toolGetProcAddress(long toolGetProcAddressHandle, String funcName) {
        return ALImpl.alGetProcAddress(toolGetProcAddressHandle, funcName);
    }

    @Override
    public boolean useToolGetProcAdressFirst(String funcName) {
        return true;
    }

    @Override
    public RunnableExecutor getLibLoaderExecutor() {
        return DynamicLibraryBundle.getDefaultRunnableExecutor();
    }    
}


