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


#include "alcbind.h"
#include "extal.h"


JNIEXPORT jint JNICALL Java_net_java_games_joal_ALCImpl_openDeviceNative
  (JNIEnv *env, jobject obj, jstring deviceName) {
  	jint result;
  	ALubyte *str;
  	str = (ALubyte*)(*env)->GetStringUTFChars(env,deviceName,NULL);
  	ALCdevice *device;
  	device = alcOpenDevice(str);
  	(*env)->ReleaseStringUTFChars(env,deviceName,str);
  	result = (jint)device;
  	return result;
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALCImpl_closeDeviceNative
  (JNIEnv *env, jobject obj, jint pointer) {
	ALCdevice* device = (ALCdevice*)pointer;
	alcCloseDevice(device);
}

JNIEXPORT jint JNICALL Java_net_java_games_joal_ALCImpl_createContextNative
  (JNIEnv *env, jobject obj, jint pointer, jintArray attrs) {
  	ALCdevice* device = (ALCdevice*)pointer;
  	ALint* attrList = NULL;
  	if(attrs != NULL) {
	  	attrList = (ALint*)(*env)->GetPrimitiveArrayCritical(env,attrs,0);
  	}
  	jint ctxPtr = (jint)alcCreateContext(device,attrList);
  	(*env)->ReleasePrimitiveArrayCritical(env,attrs,attrList,0);
	return ctxPtr;
}

JNIEXPORT jint JNICALL Java_net_java_games_joal_ALCImpl_makeContextCurrentNative
  (JNIEnv *env, jobject obj, jint pointer) {
  	ALvoid* alcHandle = (ALvoid*)pointer;
  	alcMakeContextCurrent(alcHandle);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALCImpl_processContextNative
  (JNIEnv *env, jobject obj, jint pointer) {
	ALvoid* alcHandle = (ALvoid*)pointer;
	alcProcessContext(alcHandle);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALCImpl_suspendContextNative
  (JNIEnv *env, jobject obj, jint pointer) {
  	ALvoid* alcHandle = (ALvoid*)pointer;
	alcSuspendContext(alcHandle);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALCImpl_destroyContextNative
  (JNIEnv *env, jobject obj, jint pointer) {
  	ALvoid* alcHandle = (ALvoid*)pointer;
	alcDestroyContext(alcHandle);
}
/*
JNIEXPORT jint JNICALL Java_net_java_games_joal_ALCImpl_getError
  (JNIEnv *env, jobject obj) {
  	jint result = 0;
//  	result = alcGetError();
  	return result;
}
*/
JNIEXPORT jint JNICALL Java_net_java_games_joal_ALCImpl_getCurrentContextNative
  (JNIEnv *env, jobject obj) {
	jint result;
	result = (jint)alcGetCurrentContext();
	return result;
}

JNIEXPORT jint JNICALL Java_net_java_games_joal_ALCImpl_getEnumValueNative
  (JNIEnv *env, jobject obj, jint pointer, jstring enumName) {
  	jint result;
  	ALCdevice* device = (ALCdevice*)pointer;
  	ALubyte* str;
  	str = (ALubyte*)(*env)->GetStringUTFChars(env,enumName,NULL);
  	result = alcGetEnumValue(device, str);
  	(*env)->ReleaseStringUTFChars(env,enumName,str);
	return result;  	
}

JNIEXPORT jint JNICALL Java_net_java_games_joal_ALCImpl_getContextsDeviceNative
  (JNIEnv *env, jobject obj, jint ptr) {
  	jint result;
  	ALvoid* alcHandle = (ALvoid*)ptr;
  	result = (jint)alcGetContextsDevice(alcHandle);
  	return result;
}
JNIEXPORT jstring JNICALL Java_net_java_games_joal_ALCImpl_alcGetStringNative
  (JNIEnv *env, jobject obj, jint ptr, jint pname) {
  	ALCdevice *device = (ALCdevice*)ptr;
  	ALubyte* p = alcGetString(device,(ALenum)pname);
  	return (*env)->NewStringUTF(env, p);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALCImpl_alcGetIntegervNative
  (JNIEnv *env, jobject obj, jint ptr, jint pname, jint size, jintArray data) {
	ALint *p = (ALint*)(*env)->GetPrimitiveArrayCritical(env,data,0);
	ALCdevice *device = (ALCdevice*)ptr;
	if(p) {
		alcGetIntegerv(device,(ALenum)pname,(ALsizei)size,p);
	}
	(*env)->ReleasePrimitiveArrayCritical(env,data,p,0);
}
