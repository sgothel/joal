/*
 * Copyright (c) 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN
 * MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed or intended for use
 * in the design, construction, operation or maintenance of any nuclear
 * facility.
 */

package net.java.games.joal;

import java.security.*;

import com.sun.gluegen.runtime.*;

class NativeLibLoader {
  static {
    AccessController.doPrivileged(new PrivilegedAction() {
        public Object run() {

          boolean useGlueGen =
            (System.getProperty("joal.use.gluegen") != null);

          if (useGlueGen) {
            // Workaround for problems when OpenAL is not installed;
            // want to be able to download the OpenAL shared library
            // over e.g. Java Web Start and still link against it, so we
            // have to use an alternate loading mechanism to work around
            // the JDK's usage of RTLD_LOCAL on Unix platforms
            NativeLibrary lib = NativeLibrary.open("OpenAL32", "openal", "openal",
                                                   NativeLibLoader.class.getClassLoader());
          }

          // Workaround for problem in OpenAL32.dll, which is actually
          // the "wrapper" DLL which looks for real OpenAL
          // implementations like nvopenal.dll and "*oal.dll".
          // joal.dll matches this wildcard and a bug in OpenAL32.dll
          // causes a call through a null function pointer.
          System.loadLibrary("joal_native");

          // Workaround for 4845371.
          // Make sure the first reference to the JNI GetDirectBufferAddress is done
          // from a privileged context so the VM's internal class lookups will succeed.
          // FIXME: need to figure out an appropriate entry point to call
          //          JAWT jawt = new JAWT();
          //          JAWTFactory.JAWT_GetAWT(jawt);

          return null;
        }
      });
  }

  public static void load() {
  }
}
