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

package net.java.games.joal;

import net.java.games.joal.impl.*;

/**
 * This class provides factory methods for generating AL and ALC objects.
 *
 * @author Athomas Goldberg
 * @author Kenneth Russell
 */
public class ALFactory {
  private static boolean initialized = false;
  private static AL al;
  private static ALC alc;

  private ALFactory() {}

  private static synchronized void initialize() throws ALException {
    try {
      if (!initialized) {
        NativeLibLoader.load();
        initialized = true;
      }
    } catch (UnsatisfiedLinkError e) {
      throw new ALException(e);
    }
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
}
