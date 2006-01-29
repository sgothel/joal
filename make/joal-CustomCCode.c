#if defined(_MSC_VER) /* Windows */
#include <windows.h>
static HMODULE oalModule = NULL;
#else
/* Hack for Linux */
#define __USE_GNU
#include <dlfcn.h>
#endif

/*   Java->C glue code:
 *   Java package: net.java.games.joal.impl.ALImpl
 *    Java method: long dynamicLookupFunction0(java.lang.String fname)
 *     C function: ALproc alGetProcAddress(const ALchar *  fname);
 */
JNIEXPORT jlong JNICALL 
Java_net_java_games_joal_impl_ALImpl_dynamicLookupFunction0__Ljava_lang_String_2(JNIEnv *env, jobject _unused, jstring fname) {
  const char* _UTF8fname = NULL;
  ALproc _res;
  if (fname != NULL) {
    if (fname != NULL) {
      _UTF8fname = (*env)->GetStringUTFChars(env, fname, (jboolean*)NULL);
    if (_UTF8fname == NULL) {
      (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/OutOfMemoryError"),
                       "Failed to get UTF-8 chars for argument \"fname\" in native dispatcher for \"alGetProcAddress\"");
      return 0;
    }
    }
  }
#if defined(_MSC_VER) /* Windows */
  if (oalModule == NULL) {
    oalModule = GetModuleHandle("OpenAL32");
  }
  _res = (ALproc) GetProcAddress(oalModule, _UTF8fname);
/* Looks like we can use dlsym on OS X as well as other Unix flavors */
/* #elif defined(__APPLE__) && defined(__MACH__) */ /* OS X */
#else /* Assume vanilla Unix */
  _res = (ALproc) dlsym(RTLD_DEFAULT, _UTF8fname);
#endif

  if (fname != NULL) {
    (*env)->ReleaseStringUTFChars(env, fname, _UTF8fname);
  }
  return (jlong) (intptr_t) _res;
}
