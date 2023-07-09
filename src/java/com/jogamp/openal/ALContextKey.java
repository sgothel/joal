/**
 * Copyright 2023 JogAmp Community. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY JogAmp Community ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JogAmp Community OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of JogAmp Community.
 */
package com.jogamp.openal;

import com.jogamp.common.util.HashUtil;

/**
 * Implementing {@link #equals(Object)} based on the native address
 * and {@link #hashCode()} on the {@link HashUtil#getAddrHash32_EqualDist(long)} with same native address.
 * <p>
 * Both, the native address and its hash code values are cached.
 * </p>
 */
public class ALContextKey {
    static final ALC alc = ALFactory.getALC();
    private final ALCcontext alCtx;
    private final long nativeAddress;
    private final int hashCodeValue;

    /** Creates an instance using the current context as key. */
    public ALContextKey( final Object userParam ) {
        if( null == userParam ) {
            throw new IllegalArgumentException("userParam null");
        }
        if( !(userParam instanceof ALCcontext) ) {
            throw new IllegalArgumentException("userParam not ALCcontext but "+userParam.getClass());
        }
        // alCtx = alc.alcGetCurrentContext();
        alCtx = (ALCcontext) userParam;
        if( null != alCtx ) {
            nativeAddress = alCtx.getDirectBufferAddress();
            hashCodeValue = HashUtil.getAddrHash32_EqualDist(nativeAddress);
        } else {
            nativeAddress = 0;
            hashCodeValue = 0;
        }
    }

    public ALCcontext getContext() { return alCtx; }

    @Override
    public boolean equals(final Object o) {
        if( this == o ) {
            return true;
        }
        if( !(o instanceof ALContextKey) ) {
            return false;
        }
        final ALContextKey o2 = (ALContextKey)o;
        return nativeAddress == o2.nativeAddress;
    }
    @Override
    public int hashCode() {
        return hashCodeValue;
    }

    @Override
    public String toString() {
        return "ALContextKey[alCtx hash 0x"+Integer.toHexString(System.identityHashCode(alCtx))+
                ", native[ptr 0x"+Long.toHexString(nativeAddress)+", hash 0x"+Integer.toHexString(hashCodeValue)+"]]";
    }
}