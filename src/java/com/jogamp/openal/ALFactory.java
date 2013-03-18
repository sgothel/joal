/**
 * Copyright (c) 2003-2005 Sun Microsystems, Inc. All  Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * -Redistribution of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may 
 * be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS
 * LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A
 * RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 * IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT
 * OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
 * PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for use in the
 * design, construction, operation or maintenance of any nuclear facility.
 */

package com.jogamp.openal;

import java.security.AccessController;
import java.security.PrivilegedAction;

import jogamp.openal.ALCImpl;
import jogamp.openal.ALExtImpl;
import jogamp.openal.ALImpl;
import jogamp.openal.Debug;

import com.jogamp.common.os.Platform;

/**
 * This class provides factory methods for generating AL and ALC objects.
 *
 * <p>
 * Select preferred OpenAL native library type via system properties, 
 * i.e. System-OpenAL or bundled Soft-OpenAL.<br/>
 * If the preferred choice fails, implementation falls back to the other.
 * <PRE>
    -Djoal.openal.lib=auto     Prefer System-OpenAL over bundled Soft-OpenAL for OSX. Prefer bundled Soft-OpenAL over System-OpenAL for all others. This is the default.
    -Djoal.openal.lib=system   Prefer System-OpenAL over bundled Soft-OpenAL for all.
    -Djoal.openal.lib=soft     Prefer bundled Soft-OpenAL over System-OpenAL for all.
   </PRE>
 * Note: You may use the 'jnlp.' prefix, allowing using above property names w/ Applets and WebStart,
 * e.g. 'jnlp.joal.openal.lib=system'.
 * </p>
 * 
 * @author Athomas Goldberg, Kenneth Russell, et.al.
 */
public class ALFactory {
  public static final boolean DEBUG = Debug.debug("Factory");
  /** If true, prefer System-OpenAL over bundled Soft-OpenAL, otherwise vice versa. */
  public static final boolean PREFER_SYSTEM_OPENAL;
  
  static {
      Platform.initSingleton();
      final String choice= AccessController.doPrivileged(new PrivilegedAction<String>() {
                    public String run() {
                        return Debug.getProperty("joal.openal.lib", true, null);
                    } });
      boolean useSystem = Platform.OSType.MACOS == Platform.OS_TYPE; // default
      if( null != choice ) {
          if( choice.equals("system") ) {
              useSystem = true;
          } else if( choice.equals("soft") ) {
              useSystem = false;
          }
      }
      PREFER_SYSTEM_OPENAL = useSystem;      
  }
  
  private static boolean initialized = false;
  private static AL al;
  private static ALC alc;
  private static ALExt alext;

  private ALFactory() {}

  private static synchronized void initialize() throws ALException {
    try {
      if (!initialized) {
        if(null == ALImpl.getALProcAddressTable()) {
            throw new ALException("AL not initialized (ProcAddressTable null)");
        }
        initialized = true;
        if(DEBUG) {
            System.err.println("AL initialized");
        }
      }
    } catch (UnsatisfiedLinkError e) {
      throw new ALException(e);
    } catch (ExceptionInInitializerError er) {
	  throw new ALException(er);
    }
  }
  
  /**
   * If the system property <code>joal.SystemOpenAL</code> is set
   * @return
   * @throws ALException
   */
  public static boolean getPreferSystemOpenAL() throws ALException {
    initialize();
    return PREFER_SYSTEM_OPENAL;
  }

  /**
   * Get the default AL object. This object is used to access most of the
   * OpenAL functionality.
   *
   * @return the AL object
   */
  public static AL getAL() throws ALException {
    initialize();
    if (al == null) {
        al = new ALImpl();
    }
    return al;
  }

  /**
   * Get the default ALC object. This object is used to access most of the 
   * OpenAL context functionality.
   *
   * @return the ALC object
   */
  public static ALC getALC() throws ALException{
    initialize();
    if (alc == null) {
        alc = new ALCImpl();
    }
    return alc;
  }

  /**
   * Get the default ALExt object. This object is used to access most of the 
   * OpenAL extension functionality.
   *
   * @return the ALExt object
   */
  public static ALExt getALExt() throws ALException{
    initialize();
    if (alext == null) {
        alext = new ALExtImpl();
    }
    return alext;
  }
}
