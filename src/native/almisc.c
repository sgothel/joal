#include <jni.h>
#include <stdlib.h>

#include <assert.h>

#include "al.h"
#include "alc.h"
#ifndef _MSC_VER /* Non-Windows platforms */
  #define __cdecl /* Trim non-standard keyword */
#endif
#include "efx.h"
#include <string.h>

extern int strlen_alc(ALCdevice *device, int param, const char* str);

/*   Java->C glue code:
 *   Java package: jogamp.openal.ALImpl
 *    Java method: long dispatch_alGetProcAddressStatic(java.lang.String fname)
 *     C function: ALproc alGetProcAddress(const ALchar *  fname);
 */
JNIEXPORT jlong JNICALL 
Java_jogamp_openal_ALImpl_dispatch_1alGetProcAddressStatic(JNIEnv *env, jclass _unused, jstring fname, jlong procAddress) {
  LPALGETPROCADDRESS ptr_alGetProcAddress;
  const char* _strchars_fname = NULL;
  void *_res;
  if ( NULL != fname ) {
    _strchars_fname = (*env)->GetStringUTFChars(env, fname, (jboolean*)NULL);
  if ( NULL == _strchars_fname ) {
      (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/OutOfMemoryError"),
                       "Failed to get UTF-8 chars for argument \"fname\" in native dispatcher for \"dispatch_alGetProcAddress\"");
      return 0;
    }
  }
  ptr_alGetProcAddress = (LPALGETPROCADDRESS) (intptr_t) procAddress;
  assert(ptr_alGetProcAddress != NULL);
  _res = (* ptr_alGetProcAddress) ((ALchar *) _strchars_fname);
  if ( NULL != fname ) {
    (*env)->ReleaseStringUTFChars(env, fname, _strchars_fname);
  }
  return (jlong) (intptr_t) _res;
}

/*   Java->C glue code:
 *   Java package: jogamp.openal.ALCAbstractImpl
 *    Java method: java.nio.ByteBuffer dispatch_alcGetStringImpl(ALCdevice device, int param)
 *     C function: const ALCchar *  alcGetString(ALCdevice *  device, ALCenum param);
 */
JNIEXPORT jobject JNICALL 
Java_jogamp_openal_ALCImpl_dispatch_1alcGetStringImpl1(JNIEnv *env, jobject _unused, jobject device, jint param, jlong procAddress) {
  LPALCGETSTRING ptr_alcGetString;
  ALCdevice * _device_ptr = NULL;
  const ALCchar *  _res;
  if ( NULL != device ) {
      _device_ptr = (ALCdevice *) (((char*) (*env)->GetDirectBufferAddress(env, device)) + 0);
  }
  ptr_alcGetString = (LPALCGETSTRING) (intptr_t) procAddress;
  assert(ptr_alcGetString != NULL);
  _res = (* ptr_alcGetString) ((ALCdevice *) _device_ptr, (ALCenum) param);
  if (NULL == _res) return NULL;
  return (*env)->NewDirectByteBuffer(env, (void*)_res, strlen_alc(_device_ptr, param, _res));
}

