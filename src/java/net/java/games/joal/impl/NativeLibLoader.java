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

package net.java.games.joal.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.*;

import com.sun.gluegen.runtime.*;

public class NativeLibLoader {
  private static volatile boolean loadingEnabled = true;
  private static volatile boolean didLoading;

  private NativeLibLoader() {}

  public static void disableLoading() {
    loadingEnabled = false;
  }

  public static void enableLoading() {
    loadingEnabled = true;
  }

  public static void load() {
    if (!didLoading && loadingEnabled) {
      synchronized (NativeLibLoader.class) {
        if (!didLoading && loadingEnabled) {
          didLoading = true;
          AccessController.doPrivileged(new PrivilegedAction() {
              public Object run() {
                // Workaround for problem in OpenAL32.dll, which is actually
                // the "wrapper" DLL which looks for real OpenAL
                // implementations like nvopenal.dll and "*oal.dll".
                // joal.dll matches this wildcard and a bug in OpenAL32.dll
                // causes a call through a null function pointer.
                loadLibraryInternal("joal_native");

                // Workaround for 4845371.
                // Make sure the first reference to the JNI GetDirectBufferAddress is done
                // from a privileged context so the VM's internal class lookups will succeed.
                // FIXME: need to figure out an appropriate entry point to call for JOAL
                //          JAWT jawt = new JAWT();
                //          JAWTFactory.JAWT_GetAWT(jawt);

                return null;
              }
            });
        }
      }
    }
  }

  private static void loadLibraryInternal(String libraryName) {
    String sunAppletLauncher = System.getProperty("sun.jnlp.applet.launcher");
    boolean usingJNLPAppletLauncher = Boolean.valueOf(sunAppletLauncher).booleanValue();

    if (usingJNLPAppletLauncher) {
        try {
          Class jnlpAppletLauncherClass = Class.forName("org.jdesktop.applet.util.JNLPAppletLauncher");
          Method jnlpLoadLibraryMethod = jnlpAppletLauncherClass.getDeclaredMethod("loadLibrary", new Class[] { String.class });
          jnlpLoadLibraryMethod.invoke(null, new Object[] { libraryName });
        } catch (Exception e) {
          Throwable t = e;
          if (t instanceof InvocationTargetException) {
            t = ((InvocationTargetException) t).getTargetException();
          }
          if (t instanceof Error)
            throw (Error) t;
          if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
          }
          // Throw UnsatisfiedLinkError for best compatibility with System.loadLibrary()
          throw (UnsatisfiedLinkError) new UnsatisfiedLinkError().initCause(e);
        }
    } else {
      System.loadLibrary(libraryName);
    }
  }
}
