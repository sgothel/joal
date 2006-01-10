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


#include "eaxbind.h"
#include "eax.h"
#ifdef _WIN32
const GUID DSPROPSETID_EAX20_ListenerProperties
				= { 0x306a6a8, 0xb224, 0x11d2, { 0x99, 0xe5, 0x0, 0x0, 0xe8, 0xd8, 0xc7, 0x22 } };

const GUID DSPROPSETID_EAX20_BufferProperties
				= { 0x306a6a7, 0xb224, 0x11d2, {0x99, 0xe5, 0x0, 0x0, 0xe8, 0xd8, 0xc7, 0x22 } };
#endif

JNIEXPORT void JNICALL Java_net_java_games_joal_eax_EAX_EAXSet
  (JNIEnv *env,
   jobject obj,
   jint gflag,
   jint pname,
   jint source,
   jobject buff,
   jint size) {
#ifdef _WIN32   	
   	ALvoid* p = (ALvoid*)(*env)->GetDirectBufferAddress(env,buff);
   	
   	const GUID* guid = (gflag == 1 ? &DSPROPSETID_EAX20_ListenerProperties :
   	                                 &DSPROPSETID_EAX20_BufferProperties);
	eaxSet(guid,
	       (ALuint) pname,
	       (ALuint)source,
	       p,
	       (ALuint)size);
#endif
}

JNIEXPORT void JNICALL Java_net_java_games_joal_eax_EAX_EAXGet
  (JNIEnv *env,
   jobject obj,
   jint gflag,
   jint pname,
   jint source,
   jobject buff,
   jint size) {
#ifdef _WIN32
   	ALvoid* p = (ALvoid*)(*env)->GetDirectBufferAddress(env,buff);
   	const GUID* guid = (gflag == 1 ? &DSPROPSETID_EAX20_ListenerProperties :
   	                                 &DSPROPSETID_EAX20_BufferProperties);
	eaxGet(guid,
	        (ALuint) pname,
	        (ALuint) source,
	        p,
	        (ALuint) size);
#endif
}

