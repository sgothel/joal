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


#include "albind.h"
#include "extal.h"

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGenBuffersNative__ILjava_nio_IntBuffer_2
  (JNIEnv *env, jobject obj, jint numBuffers, jobject buffers) {
  	if(buffers == 0) {
  		
  	}
  	ALuint *p = (ALuint*)(*env)->GetDirectBufferAddress(env,buffers);
  	alGenBuffers((ALsizei)numBuffers,p);
}
JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGenBuffersNative__I_3I
  (JNIEnv *env, jobject obj, jint numBuffers, jintArray buffers) {
  	ALuint *p = (ALuint*)(*env)->GetPrimitiveArrayCritical(env,buffers,0);
  	if(p) {
	  	alGenBuffers((ALsizei)numBuffers,p);
  	}  	
  	(*env)->ReleasePrimitiveArrayCritical(env,buffers,p,0);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alDeleteBuffersNative__ILjava_nio_IntBuffer_2
  (JNIEnv *env, jobject obj, jint numBuffers, jobject buffers) {
  	ALuint *p = (ALuint*)(*env)->GetDirectBufferAddress(env,buffers);
  	if(p) {
	  	alDeleteBuffers((ALsizei)numBuffers,p);
  	}
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alDeleteBuffersNative__I_3I
  (JNIEnv *env, jobject obj, jint numBuffers, jintArray buffers) {
  	ALuint *p = (ALuint*)(*env)->GetPrimitiveArrayCritical(env,buffers,0);
	if(p) {
	  	alDeleteBuffers((ALsizei)numBuffers,p);
	}
  	(*env)->ReleasePrimitiveArrayCritical(env,buffers,p,0);
}

JNIEXPORT jboolean JNICALL Java_net_java_games_joal_ALImpl_alIsBuffer
  (JNIEnv *env, jobject obj, jint bufferName) {
	jboolean result;
	result = alIsBuffer((ALuint)bufferName);
	return result;
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alBufferDataNative__IILjava_nio_ByteBuffer_2II
													  (JNIEnv *env,
													   jobject obj,
													   jint bufferName,
													   jint format,
													   jobject data,
													   jint size,
													   jint frequency) {
	ALvoid *p = (ALvoid*)(*env)->GetDirectBufferAddress(env,data);
	if(p) {
	  	alBufferData((ALuint)bufferName,
	  			     (ALenum)format,
	  			     p,
	  			     (ALsizei)size,
	  			     (ALsizei)frequency);
	}
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alBufferDataNative__II_3BII
  							(JNIEnv *env,
  							jobject obj,
							jint bufferName,
							jint format,
							jbyteArray data,
							jint size,
							jint frequency) {
	ALvoid *p = (ALvoid*)(*env)->GetPrimitiveArrayCritical(env,data,0);
	if(p) {
	  	alBufferData((ALuint)bufferName,
	  			     (ALenum)format,
	  			     p,
	  			     (ALsizei)size,
	  			     (ALsizei)frequency);
	}
	(*env)->ReleasePrimitiveArrayCritical(env,data,p,0);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetBufferfNative__II_3F
  (JNIEnv *env, jobject obj, jint buffer, jint pname, jfloatArray rv) {
  		ALfloat *result = (ALfloat*)(*env)->GetPrimitiveArrayCritical(env,rv,0);
  		alGetBufferf((ALuint)buffer, (ALenum)pname, result);
  		(*env)->ReleasePrimitiveArrayCritical(env,rv,result,0);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetBufferfNative__IILjava_nio_FloatBuffer_2
  (JNIEnv *env, jobject obj, jint buffer, jint pname, jobject rv) {
	ALfloat *p = (ALfloat*)(*env)->GetDirectBufferAddress(env,rv);
	if(p) {
		alGetBufferf((ALuint)buffer, (ALenum)pname, p); 
	}
}

JNIEXPORT jfloat JNICALL Java_net_java_games_joal_ALImpl_alGetBufferf__II
  (JNIEnv *env, jobject obj, jint buffer, jint pname) {
  	jfloat result;
    alGetBufferf((ALuint)buffer, (ALenum)pname, &result);
  	return result;
}


JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetBufferiNative__II_3I
  (JNIEnv *env, jobject obj, jint buffer, jint pname, jintArray rv) {
  	ALint *p = (ALint*)(*env)->GetPrimitiveArrayCritical(env,rv,0);
  	if(p) {
	  	alGetBufferi((ALuint)buffer, (ALenum)pname, p);
  	}
  	(*env)->ReleasePrimitiveArrayCritical(env,rv,p,0);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetBufferiNative__IILjava_nio_IntBuffer_2
  (JNIEnv *env, jobject obj, jint buffer, jint pname, jobject rv) {
  	ALint *p = (ALint*)(*env)->GetDirectBufferAddress(env,rv);
	if(p) {
	  	alGetBufferi((ALuint)buffer, (ALenum)pname, p);
	}
  	(*env)->ReleasePrimitiveArrayCritical(env,rv,p,0);
}

JNIEXPORT jint JNICALL Java_net_java_games_joal_ALImpl_alGetBufferi__II
  (JNIEnv *env, jobject obj, jint buffer, jint pname) {
  	ALint result;
  	alGetBufferi((ALuint)buffer, (ALenum)pname, &result);
  	return (jint)result;	
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGenSourcesNative__I_3I
  (JNIEnv *env, jobject obj, jint numSources, jintArray sources) {
	ALuint *p = (ALuint*)(*env)->GetPrimitiveArrayCritical(env,sources,0);
	if(p) {
		alGenSources((ALsizei)numSources,p);
	}
	(*env)->ReleasePrimitiveArrayCritical(env,sources,p,0);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGenSourcesNative__ILjava_nio_IntBuffer_2
  (JNIEnv *env, jobject obj, jint numSources, jobject sources) {
	ALuint *p = (ALuint*)(*env)->GetDirectBufferAddress(env,sources);
	if(p) {
		alGenSources((ALsizei)numSources,p);
	}
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alDeleteSourcesNative__I_3I
  (JNIEnv *env, jobject obj, jint numSources, jintArray sources) {
	ALuint *p = (ALuint*)(*env)->GetPrimitiveArrayCritical(env,sources,0);
	if(p) {
	  	alDeleteSources((ALsizei)numSources, p);
	}
  	(*env)->ReleasePrimitiveArrayCritical(env,sources,p,0);
}

/*
 * Class:     net_java_games_joal_AL
 * Method:    alDeleteSources
 * Signature: (ILjava/nio/IntBuffer;)V
 */
JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alDeleteSourcesNative__ILjava_nio_IntBuffer_2
  (JNIEnv *env, jobject obj, jint numSources, jobject sources) {
	ALuint *p = (ALuint*)(*env)->GetDirectBufferAddress(env,sources);
	if(p) {
	  	alDeleteSources((ALsizei)numSources, p);
	}
}

JNIEXPORT jboolean JNICALL Java_net_java_games_joal_ALImpl_alIsSource
  (JNIEnv *env, jobject obj, jint sourceName) {
  	alIsSource((ALuint)sourceName);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSourcei
  (JNIEnv *env, jobject obj, jint source, jint pname, jint value) {
  	alSourcei((ALuint)source,(ALenum)pname, (ALint)value);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSourcef
  (JNIEnv *env, jobject obj, jint source, jint pname, jfloat value) {
  	alSourcef((ALuint)source,(ALenum)pname, (ALfloat)value);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSourcefvNative__II_3F
  (JNIEnv *env, jobject obj, jint source, jint pname, jfloatArray value) {
  	ALfloat *p = (ALfloat*)(*env)->GetPrimitiveArrayCritical(env,value,0);
	if(p) {
	  	alSourcefv((ALuint)source, (ALenum)pname, p);
	}
  	(*env)->ReleasePrimitiveArrayCritical(env,value,p,0);
}

/*
 * Class:     net_java_games_joal_AL
 * Method:    alSourcefv
 * Signature: (IILjava/nio/FloatBuffer;)V
 */
JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSourcefvNative__IILjava_nio_FloatBuffer_2
  (JNIEnv *env, jobject obj, jint source, jint pname, jobject value) {
  	ALfloat *p = (ALfloat*)(*env)->GetDirectBufferAddress(env,value);
  	alSourcefv((ALuint)source, (ALenum)pname, p);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSource3f
  (JNIEnv *env,
   jobject obj,
   jint source,
   jint pname,
   jfloat v1,
   jfloat v2,
   jfloat v3) {
	alSource3f((ALuint)source,
	           (ALenum)pname,
	           (ALfloat)v1,
	           (ALfloat)v2,
	           (ALfloat)v3);
}


JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetSourcefNative__II_3F
  (JNIEnv *env, jobject obj, jint source, jint pname, jfloatArray rv) {
	ALfloat *p = (*env)->GetPrimitiveArrayCritical(env,rv,0);
	if(p) {
	  	alGetSourcef((ALuint)source, (ALenum)pname, p);
	}
	(*env)->ReleasePrimitiveArrayCritical(env,rv,p,0);
}

/*
 * Class:     net_java_games_joal_AL
 * Method:    alGetSourcef
 * Signature: (IILjava/nio/FloatBuffer;)V
 */
JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetSourcefNative__IILjava_nio_FloatBuffer_2
  (JNIEnv *env, jobject obj, jint source, jint pname, jobject rv) {
	ALfloat *p = (*env)->GetDirectBufferAddress(env,rv);
	if(p) {
	  	alGetSourcef((ALuint)source, (ALenum)pname, p);
	}
}

/*
 * Class:     net_java_games_joal_AL
 * Method:    alGetSourcef
 * Signature: (II)F
 */
JNIEXPORT jfloat JNICALL Java_net_java_games_joal_ALImpl_alGetSourcef__II
  (JNIEnv *env, jobject obj, jint source, jint pname) {
  	jfloat result;
  	alGetSourcef((ALuint)source, (ALenum)pname, &result);
  	return result;
}



JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetSourcefvNative__IILjava_nio_FloatBuffer_2
  (JNIEnv *env, jobject obj, jint source, jint pname, jobject value) {
	ALfloat *p = (ALfloat*)(*env)->GetDirectBufferAddress(env,value);
	if(p) {
		alGetSourcefv((ALuint)source, (ALenum)pname, p);
	}
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetSourcefvNative__II_3F
  (JNIEnv *env, jobject obj, jint source, jint pname, jfloatArray value) {
	ALfloat *p = (ALfloat*)(*env)->GetPrimitiveArrayCritical(env,value,0);
	if(p) {
		alGetSourcefv((ALuint)source, (ALenum)pname, p);
	}
	(*env)->ReleasePrimitiveArrayCritical(env,value,p,0);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetSourceiNative__II_3I
  (JNIEnv *env, jobject obj, jint source, jint pname, jintArray rv) {
  	ALint *p = (ALint*)(*env)->GetPrimitiveArrayCritical(env,rv,0);
  	if(p) {
	  	alGetSourcei((ALuint)source, (ALenum)pname, p);
  	}
  	(*env)->ReleasePrimitiveArrayCritical(env,rv,p,0);
}

/*
 * Class:     net_java_games_joal_AL
 * Method:    alGetSourcei
 * Signature: (IILjava/nio/IntBuffer;)V
 */
JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetSourceiNative__IILjava_nio_IntBuffer_2
  (JNIEnv *env, jobject obj, jint source, jint pname, jobject rv) {
  	ALint *p = (ALint*)(*env)->GetDirectBufferAddress(env,rv);
  	if(p) {
	  	alGetSourcei((ALuint)source, (ALenum)pname, p);
  	}
}

/*
 * Class:     net_java_games_joal_AL
 * Method:    alGetSourcei
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_joal_ALImpl_alGetSourcei__II
  (JNIEnv *env, jobject obj, jint source, jint pname) {
  	ALint result;
  	alGetSourcei((ALuint)source, (ALenum)pname, &result);
  	return (jint)result;
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSourcePlay
	(JNIEnv *env, jobject obj, jint source) {
  	alSourcePlay((ALuint)source);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSourcePlayvNative__I_3I
  (JNIEnv *env, jobject obj, jint numSources, jintArray sources) {
	ALuint *p = (ALuint*)(*env)->GetPrimitiveArrayCritical(env,sources,0);
	if(p) {
		alSourcePlayv((ALsizei)numSources,p);
	}
	(*env)->ReleasePrimitiveArrayCritical(env,sources,p,0);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSourcePlayvNative__ILjava_nio_IntBuffer_2
  (JNIEnv *env, jobject obj, jint numSources, jobject sources) {
	ALuint *p = (ALuint*)(*env)->GetDirectBufferAddress(env,sources);
	if(p) {
		alSourcePlayv((ALsizei)numSources,p);
	}
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSourcePause
  (JNIEnv *env, jobject obj, jint source) {
  	alSourcePause((ALuint)source);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSourcePausevNative__I_3I
  (JNIEnv *env, jobject obj, jint numSources, jintArray sources) {
	ALuint *p = (ALuint*)(*env)->GetPrimitiveArrayCritical(env,sources,0);
	if(p) {
		alSourcePausev((ALsizei)numSources,p);
	}
	(*env)->ReleasePrimitiveArrayCritical(env,sources,p,0);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSourcePausevNative__ILjava_nio_IntBuffer_2
  (JNIEnv *env, jobject obj, jint numSources, jobject sources) {
	ALuint *p = (ALuint*)(*env)->GetDirectBufferAddress(env,sources);
	if(p) {
		alSourcePausev((ALsizei)numSources,p);
	}
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSourceStop
	(JNIEnv *env, jobject obj, jint source) {
  	alSourceStop((ALuint)source);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSourceStopvNative__I_3I
  (JNIEnv *env, jobject obj, jint numSources, jintArray sources) {
	ALuint *p = (ALuint*)(*env)->GetPrimitiveArrayCritical(env,sources,0);
	if(p) {
		alSourceStopv((ALsizei)numSources,p);
	}
	(*env)->ReleasePrimitiveArrayCritical(env,sources,p,0);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSourceStopvNative__ILjava_nio_IntBuffer_2
  (JNIEnv *env, jobject obj, jint numSources, jobject sources) {
	ALuint *p = (ALuint*)(*env)->GetDirectBufferAddress(env,sources);
	if(p) {
		alSourceStopv((ALsizei)numSources,p);
	}
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSourceRewind
	(JNIEnv *env, jobject obj, jint source) {
  	alSourceRewind((ALuint)source);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSourceRewindvNative__I_3I
  (JNIEnv *env, jobject obj, jint numSources, jintArray sources) {
	ALuint *p = (ALuint*)(*env)->GetPrimitiveArrayCritical(env,sources,0);
	if(p) {
		alSourceRewindv((ALsizei)numSources,p);
	}
	(*env)->ReleasePrimitiveArrayCritical(env,sources,p,0);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSourceRewindvNative__ILjava_nio_IntBuffer_2
  (JNIEnv *env, jobject obj, jint numSources, jobject sources) {
	ALuint *p = (ALuint*)(*env)->GetDirectBufferAddress(env,sources);
	if(p) {
		alSourceRewindv((ALsizei)numSources,p);
	}
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSourceQueueBuffersNative__IILjava_nio_IntBuffer_2
  (JNIEnv *env, jobject obj, jint source, jint numBuffers, jobject buffers) {
	ALuint *p = (ALuint*)(*env)->GetDirectBufferAddress(env,buffers);
	if(p) {
	  	alSourceQueueBuffers((ALuint)source, (ALsizei)numBuffers, p);
	}
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSourceQueueBuffersNative__II_3I
  (JNIEnv *env, jobject obj, jint source, jint numBuffers, jintArray buffers) {
	ALuint *p = (ALuint*)(*env)->GetPrimitiveArrayCritical(env,buffers,0);
	if(p) {
	  	alSourceQueueBuffers((ALuint)source, (ALsizei)numBuffers, p);
	}
  	(*env)->ReleasePrimitiveArrayCritical(env,buffers,p,0);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSourceUnqueueBuffersNative__II_3I
  (JNIEnv *env, jobject obj, jint source, jint numBuffers, jintArray buffers) {
	ALuint *p = (ALuint*)(*env)->GetPrimitiveArrayCritical(env,buffers,0);
	if(p) {
	  	alSourceUnqueueBuffers((ALuint)source, (ALsizei)numBuffers, p);
	}
  	(*env)->ReleasePrimitiveArrayCritical(env,buffers,p,0);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alSourceUnqueueBuffersNative__IILjava_nio_IntBuffer_2
  (JNIEnv *env, jobject obj, jint source, jint numBuffers, jobject buffers) {
	ALuint *p = (ALuint*)(*env)->GetDirectBufferAddress(env,buffers);
	if(p) {
	  	alSourceUnqueueBuffers((ALuint)source, (ALsizei)numBuffers, p);
	}
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alListenerf
  (JNIEnv *env, jobject obj, jint pname, jfloat value) {
	alListenerf((ALenum)pname,(ALfloat)value);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alListener3f
  (JNIEnv *env, jobject obj, jint pname, jfloat v1, jfloat v2, jfloat v3) {
	alListener3f((ALenum)pname, (ALfloat)v1, (ALfloat)v2, (ALfloat)v3);  	
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alListenerfvNative__I_3F
  (JNIEnv *env, jobject obj, jint pname, jfloatArray rv) {
  	ALfloat *p = (ALfloat*)(*env)->GetPrimitiveArrayCritical(env,rv,0);
  	alListenerfv((ALenum)pname, p);
  	(*env)->ReleasePrimitiveArrayCritical(env,rv,p,0);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alListenerfvNative__ILjava_nio_FloatBuffer_2
	(JNIEnv *env, jobject obj, jint pname, jobject value) {
  	ALfloat *p = (ALfloat*)(*env)->GetDirectBufferAddress(env,value);
  	alListenerfv((ALenum)pname, p);
}


JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetListenerfNative__I_3F
  (JNIEnv *env, jobject obj, jint pname, jfloatArray rv) {
  	ALfloat *p = (ALfloat*)(*env)->GetPrimitiveArrayCritical(env,rv,0);
  	if(p) {
	  	alGetListenerf((ALenum)pname, p);
  	}
  	(*env)->ReleasePrimitiveArrayCritical(env,rv,p,0);
}

/*
 * Class:     net_java_games_joal_AL
 * Method:    alGetListenerf
 * Signature: (ILjava/nio/FloatBuffer;)V
 */
JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetListenerfNative__ILjava_nio_FloatBuffer_2
  (JNIEnv *env, jobject obj, jint pname, jobject rv) {
  	ALfloat *p = (ALfloat*)(*env)->GetDirectBufferAddress(env,rv);
  	if(p) {
	  	alGetListenerf((ALenum)pname, p);
  	}
}

/*
 * Class:     net_java_games_joal_AL
 * Method:    alGetListenerf
 * Signature: (I)F
 */
JNIEXPORT jfloat JNICALL Java_net_java_games_joal_ALImpl_alGetListenerf__I
	(JNIEnv *env, jobject object, jint pname) {
	jfloat result;
  	alGetListenerf((ALenum)pname, &result);
  	return result;
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetListener3fNative__I_3F_3F_3F
  (JNIEnv *env, jobject obj, jint pname, jfloatArray v1, jfloatArray v2, jfloatArray v3) {
  	ALfloat *p1 = (ALfloat*)(*env)->GetPrimitiveArrayCritical(env,v1,0);
  	ALfloat *p2 = (ALfloat*)(*env)->GetPrimitiveArrayCritical(env,v2,0);
  	ALfloat *p3 = (ALfloat*)(*env)->GetPrimitiveArrayCritical(env,v3,0);
  	if(p1 && p2 && p3) {
	  	alGetListener3f((ALenum)pname, p1, p2, p3);
  	}
  	(*env)->ReleasePrimitiveArrayCritical(env,v3,p3,0);
  	(*env)->ReleasePrimitiveArrayCritical(env,v2,p2,0);
  	(*env)->ReleasePrimitiveArrayCritical(env,v1,p1,0);
}


JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetListener3fNative__ILjava_nio_FloatBuffer_2Ljava_nio_FloatBuffer_2Ljava_nio_FloatBuffer_2
  (JNIEnv *env, jobject obj, jint pname, jobject v1, jobject v2, jobject v3) {
  	ALfloat *p1 = (ALfloat*)(*env)->GetDirectBufferAddress(env,v1);
  	ALfloat *p2 = (ALfloat*)(*env)->GetDirectBufferAddress(env,v2);
  	ALfloat *p3 = (ALfloat*)(*env)->GetDirectBufferAddress(env,v3);
  	if(p1 && p2 && p3) {
	  	alGetListener3f((ALenum)pname, p1, p2, p3);
  	}
}


JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetListenerfvNative__I_3F
  (JNIEnv *env, jobject obj, jint pname, jfloatArray value) {
  	ALfloat *p = (ALfloat*)(*env)->GetPrimitiveArrayCritical(env,value,0);
  	if(p) {
	  	alGetListenerfv((ALenum)pname, p);
  	}
  	(*env)->ReleasePrimitiveArrayCritical(env,value,p,0);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetListenerfvNative__ILjava_nio_FloatBuffer_2
  (JNIEnv *env, jobject obj, jint pname, jobject value) {
  	ALfloat *p = (ALfloat*)(*env)->GetDirectBufferAddress(env,value);
  	if(p) {
	  	alGetListenerfv((ALenum)pname, p);
  	}
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetListeneriNative__I_3I
  (JNIEnv *env, jobject obj, jint pname, jintArray value) {
  	ALint *p = (ALint*)(*env)->GetPrimitiveArrayCritical(env,value,0);
  	if(p) {
		alGetListeneri((ALenum)pname, p);
  	}
	(*env)->ReleasePrimitiveArrayCritical(env,value,p,0);
  	
}

/*
 * Class:     net_java_games_joal_AL
 * Method:    alGetListeneri
 * Signature: (ILjava/nio/IntBuffer;)V
 */
JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetListeneriNative__ILjava_nio_IntBuffer_2
  (JNIEnv *env, jobject obj, jint pname, jobject value) {
  	ALint *p = (ALint*)(*env)->GetDirectBufferAddress(env,value);
  	if(p) {
		alGetListeneri((ALenum)pname, p);
  	}
}

/*
 * Class:     net_java_games_joal_AL
 * Method:    alGetListeneri
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_java_games_joal_ALImpl_alGetListeneri__I
  (JNIEnv *env, jobject obj, jint pname) {
  	ALint result;
	alGetListeneri((ALenum)pname, &result);
	return (jint)result;
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alEnable
	(JNIEnv *env, jobject obj, jint capability) {
  	alEnable((ALenum)capability);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alDisable
  (JNIEnv *env, jobject obj, jint capability) {
	alDisable((ALenum)capability);  	
}

JNIEXPORT jboolean JNICALL Java_net_java_games_joal_ALImpl_alIsEnabled
  (JNIEnv *env, jobject obj, jint pname) {
  	return alIsEnabled((ALenum)pname);
}

JNIEXPORT jboolean JNICALL Java_net_java_games_joal_ALImpl_alGetBoolean
  (JNIEnv *env, jobject obj, jint pname) {
	return alGetBoolean((ALenum)pname);  	
}

JNIEXPORT jdouble JNICALL Java_net_java_games_joal_ALImpl_alGetDouble
  (JNIEnv *env, jobject obj, jint pname) {
	return alGetDouble((ALenum)pname);  	
}

JNIEXPORT jfloat JNICALL Java_net_java_games_joal_ALImpl_alGetFloat
  (JNIEnv *env, jobject obj, jint pname) {
	return alGetFloat((ALenum)pname);  	
}

JNIEXPORT jint JNICALL Java_net_java_games_joal_ALImpl_alGetInteger
  (JNIEnv *env, jobject obj, jint pname) {
	return alGetInteger((ALenum)pname);  	
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetDoublevNative__I_3D
  (JNIEnv *env, jobject obj, jint pname, jdoubleArray value) {
	ALdouble *p = (ALdouble*)(*env)->GetPrimitiveArrayCritical(env,value,0);
	if(p) {
		alGetDoublev((ALenum)pname,p);
	}
	(*env)->ReleasePrimitiveArrayCritical(env,value,p,0);
  	
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetDoublevNative__ILjava_nio_DoubleBuffer_2
  (JNIEnv *env, jobject obj, jint pname, jobject value) {
	ALdouble *p = (ALdouble*)(*env)->GetDirectBufferAddress(env,value);
	if(p) {
		alGetDoublev((ALenum)pname,p);
	}
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetFloatvNative__I_3F
  (JNIEnv *env, jobject obj, jint pname, jfloatArray value) {
	ALfloat *p = (ALfloat*)(*env)->GetPrimitiveArrayCritical(env,value,0);
	if(p) {
		alGetFloatv((ALenum)pname,p);
	}
	(*env)->ReleasePrimitiveArrayCritical(env,value,p,0);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetFloatvNative__ILjava_nio_FloatBuffer_2
  (JNIEnv *env, jobject obj, jint pname, jobject value) {
	ALfloat *p = (ALfloat*)(*env)->GetDirectBufferAddress(env,value);
	if(p) {
		alGetFloatv((ALenum)pname,p);
	}
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetIntegervNative__I_3I
  (JNIEnv *env, jobject obj, jint pname, jintArray value) {
	ALint *p = (ALint*)(*env)->GetPrimitiveArrayCritical(env,value,0);
	if(p) {
		alGetIntegerv((ALenum)pname,p);
	}
	(*env)->ReleasePrimitiveArrayCritical(env,value,p,0);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alGetIntegervNative__ILjava_nio_IntBuffer_2
  (JNIEnv *env, jobject obj, jint pname, jobject value) {
	ALint *p = (ALint*)(*env)->GetDirectBufferAddress(env,value);
	if(p) {
		alGetIntegerv((ALenum)pname,p);
	}
}


JNIEXPORT jstring JNICALL Java_net_java_games_joal_ALImpl_alGetString
  (JNIEnv *env, jobject obj, jint pname) {
  	ALubyte* p = alGetString((ALenum)pname);
  	return (*env)->NewStringUTF(env, p);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alDistanceModel
  (JNIEnv *env, jobject obj, jint value) {
  	alDistanceModel((ALenum)value);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alDopplerFactor
  (JNIEnv *env, jobject obj, jfloat value) {
  	alDopplerFactor((ALfloat)value);
}

JNIEXPORT void JNICALL Java_net_java_games_joal_ALImpl_alDopplerVelocity
  (JNIEnv *env, jobject obj, jfloat value) {
	alDopplerVelocity((ALfloat)value);  	
}

JNIEXPORT jint JNICALL Java_net_java_games_joal_ALImpl_alGetError
  (JNIEnv *env, jobject obj) {
	return alGetError();  	
}

JNIEXPORT jboolean JNICALL Java_net_java_games_joal_ALImpl_alIsExtensionPresent
  (JNIEnv *env, jobject obj, jstring extName) {
	ALubyte *str;
	str = (ALubyte*)(*env)->GetStringUTFChars(env, extName, NULL);
	if(!str) {
		return 0;	
	}
	return alIsExtensionPresent(str);
}
