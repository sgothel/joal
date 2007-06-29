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

package net.java.games.joal.impl;

import java.lang.reflect.Field;

import net.java.games.joal.*;
import com.sun.gluegen.runtime.*;

/** Helper class for managing OpenAL-related proc address tables. */

public class ALProcAddressLookup {
  private static final ALProcAddressTable  alTable  = new ALProcAddressTable();
  private static volatile boolean          alTableInitialized = false;
  private static final ALCProcAddressTable alcTable = new ALCProcAddressTable();
  private static volatile boolean          alcTableInitialized = false;
  private static final DynamicLookup       lookup   = new DynamicLookup();
  private static volatile NativeLibrary    openAL   = null;

  static class DynamicLookup implements DynamicLookupHelper {
    public long dynamicLookupFunction(String functionName) {
      // At some point this may require an OpenAL context to be
      // current as we will actually use alGetProcAddress. Since
      // this routine is currently broken and there are no
      // per-context function pointers anyway we could actually do
      // this work anywhere.
      if (openAL == null) {
        // We choose not to search the system path first because we
        // bundle a very recent version of OpenAL which we would like
        // to override existing installations
        openAL = NativeLibrary.open("OpenAL32", "openal", "OpenAL",
                                    false,
                                    ALProcAddressLookup.class.getClassLoader());
        if (openAL == null) {
          throw new RuntimeException("Unable to find and load OpenAL library");
        }
      }
      return openAL.lookupFunction(functionName);
    }
  }
  
  public static void resetALProcAddressTable() {
    if (!alTableInitialized) {
      synchronized (ALProcAddressLookup.class) {
        if (!alTableInitialized) {
          // At some point this may require an OpenAL context to be
          // current as we will actually use alGetProcAddress. Since
          // this routine is currently broken and there are no
          // per-context function pointers anyway we could actually do
          // this work anywhere. We should also in theory have
          // per-ALcontext ALProcAddressTables and per-ALCdevice
          // ALCProcAddressTables.
          ProcAddressHelper.resetProcAddressTable(alTable, lookup);

          // The above only manages to find addresses for the core OpenAL
          // functions.  The below uses alGetProcAddress() to find the addresses
          // of extensions such as EFX, just as in the C++ examples of the
          // OpenAL 1.1 SDK.
          useALGetProcAddress();

          alTableInitialized = true;
        }
      }
    }
  }

  public static void resetALCProcAddressTable() {
    if (!alcTableInitialized) {
      synchronized (ALProcAddressLookup.class) {
        if (!alcTableInitialized) {
          // At some point this may require an OpenAL device to be
          // created as we will actually use alcGetProcAddress. Since
          // this routine is currently broken and there are no
          // per-device function pointers anyway we could actually do
          // this work anywhere. We should also in theory have
          // per-ALcontext ALProcAddressTables and per-ALCdevice
          // ALCProcAddressTables.
          ProcAddressHelper.resetProcAddressTable(alcTable, lookup);
          alcTableInitialized = true;
        }
      }
    }
  }

  public static ALProcAddressTable getALProcAddressTable() {
    return alTable;
  }

  public static ALCProcAddressTable getALCProcAddressTable() {
    return alcTable;
  }


  /**
   * Retrieves the values of the OpenAL functions using alGetProcAddress().
   */
  private static void useALGetProcAddress() {
    String addrOfPrefix = "_addressof_";
    ALImpl al = (ALImpl) ALFactory.getAL();

    Field[] fields = ALProcAddressTable.class.getFields();
    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];

      // Skip non-address fields
      String fieldname = field.getName();
      if (!fieldname.startsWith(addrOfPrefix)) {
        continue;
      }
      try {
        String functionname = fieldname.substring(addrOfPrefix.length());
        long fieldval = field.getLong(alTable);

        // Skip fields which have already been valued
        if (fieldval != 0) {
          continue;
        }

        // Get the address
        long procAddressVal = al.alGetProcAddress(functionname);
        field.setLong(alTable, procAddressVal);
      }
      catch (Exception ex) {
        throw new RuntimeException("Unable to repopulate ALProcAddressTable values", ex);
      }
    }
  }
}
