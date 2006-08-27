/**
* Copyright (c) 2003 Sun Microsystems, Inc. All  Rights Reserved.
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
* NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN") AND ITS
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


#include "eaxfactory.h"
#include "al.h"
#include "eax.h"

#ifdef _WIN32
#include <windows.h>
static HMODULE oalModule = NULL;
static LPALISEXTENSIONPRESENT _ptr_alIsExtensionPresent = NULL;
static LPALGETPROCADDRESS     _ptr_alGetProcAddress     = NULL;
EAXSet	eaxSet; // EAXSet function
EAXGet	eaxGet;	// EAXGet function
#endif

/* 
 * had some problems getting this from extal.h
 */

JNIEXPORT void JNICALL Java_net_java_games_joal_eax_EAXFactory_init
  (JNIEnv *env, jclass clazz) {
#ifdef _WIN32
  if (_ptr_alIsExtensionPresent == NULL) {
    if (oalModule == NULL) {
      oalModule = GetModuleHandle("OpenAL32");
    }
    _ptr_alIsExtensionPresent = (LPALISEXTENSIONPRESENT) GetProcAddress(oalModule, "alIsExtensionPresent");
    _ptr_alGetProcAddress     = (LPALGETPROCADDRESS)     GetProcAddress(oalModule, "alGetProcAddress");
  }

  if (_ptr_alIsExtensionPresent != NULL &&
      _ptr_alGetProcAddress     != NULL) {
    if ((*_ptr_alIsExtensionPresent)("EAX2.0")) {
      eaxSet = (*_ptr_alGetProcAddress)((ALubyte*)"EAXSet");
      eaxGet = (*_ptr_alGetProcAddress)((ALubyte*)"EAXGet");
    }
  }
#endif
}
