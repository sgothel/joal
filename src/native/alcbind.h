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
#include <jni.h>
/* Header for class net_java_games_joal_ALCImpl */

#ifndef _Included_net_java_games_joal_ALCImpl
#define _Included_net_java_games_joal_ALCImpl
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     net_java_games_joal_ALCImpl
 * Method:    openDeviceNative
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_joal_ALCImpl_openDeviceNative
  (JNIEnv *, jobject, jstring);

/*
 * Class:     net_java_games_joal_ALCImpl
 * Method:    closeDeviceNative
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_java_games_joal_ALCImpl_closeDeviceNative
  (JNIEnv *, jobject, jint);

/*
 * Class:     net_java_games_joal_ALCImpl
 * Method:    createContextNative
 * Signature: (I[I)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_joal_ALCImpl_createContextNative
  (JNIEnv *, jobject, jint, jintArray);

/*
 * Class:     net_java_games_joal_ALCImpl
 * Method:    makeContextCurrentNative
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_joal_ALCImpl_makeContextCurrentNative
  (JNIEnv *, jobject, jint);

/*
 * Class:     net_java_games_joal_ALCImpl
 * Method:    processContextNative
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_java_games_joal_ALCImpl_processContextNative
  (JNIEnv *, jobject, jint);

/*
 * Class:     net_java_games_joal_ALCImpl
 * Method:    suspendContextNative
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_java_games_joal_ALCImpl_suspendContextNative
  (JNIEnv *, jobject, jint);

/*
 * Class:     net_java_games_joal_ALCImpl
 * Method:    destroyContextNative
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_net_java_games_joal_ALCImpl_destroyContextNative
  (JNIEnv *, jobject, jint);

/*
 * Class:     net_java_games_joal_ALCImpl
 * Method:    alcGetError
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_net_java_games_joal_ALCImpl_alcGetError
  (JNIEnv *, jobject);

/*
 * Class:     net_java_games_joal_ALCImpl
 * Method:    getCurrentContextNative
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_net_java_games_joal_ALCImpl_getCurrentContextNative
  (JNIEnv *, jobject);

/*
 * Class:     net_java_games_joal_ALCImpl
 * Method:    getEnumValueNative
 * Signature: (ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_joal_ALCImpl_getEnumValueNative
  (JNIEnv *, jobject, jint, jstring);

/*
 * Class:     net_java_games_joal_ALCImpl
 * Method:    alcGetStringNative
 * Signature: (II)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_net_java_games_joal_ALCImpl_alcGetStringNative
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     net_java_games_joal_ALCImpl
 * Method:    alcGetIntegervNative
 * Signature: (III[I)V
 */
JNIEXPORT void JNICALL Java_net_java_games_joal_ALCImpl_alcGetIntegervNative
  (JNIEnv *, jobject, jint, jint, jint, jintArray);

/*
 * Class:     net_java_games_joal_ALCImpl
 * Method:    getContextDeviceNative
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_joal_ALCImpl_getContextDeviceNative
  (JNIEnv *, jobject, jint);

#ifdef __cplusplus
}
#endif
#endif
