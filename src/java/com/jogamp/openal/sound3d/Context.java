/**
* Copyright (c) 2010-2023 JogAmp Community. All rights reserved.
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

package com.jogamp.openal.sound3d;

import com.jogamp.common.util.locks.LockFactory;
import com.jogamp.common.util.locks.RecursiveLock;
import com.jogamp.openal.*;
import com.jogamp.openal.util.ALHelpers;


/**
 * This class provides a Sound3D Context associated with a specified device.
 *
 * @author Athomas Goldberg
 */
public class Context {
    private final RecursiveLock lock = LockFactory.createRecursiveLock();
    private Device device;
    private ALCcontext alCtx;
    private boolean threadContextLocked;
    private boolean hasALC_thread_local_context;

    public Context(final ALCcontext realContext, final Device device) {
        this.device = device;
        this.alCtx = realContext;
        {
            hasALC_thread_local_context = false;
            final boolean v;
            if( makeCurrent() ) {
                v = AudioSystem3D.alc.alcIsExtensionPresent(null, ALHelpers.ALC_EXT_thread_local_context) ||
                    AudioSystem3D.alc.alcIsExtensionPresent(device.getALDevice(), ALHelpers.ALC_EXT_thread_local_context);
                release();
            } else {
                v = false;
            }
            hasALC_thread_local_context = v;
        }
    }

    /**
     * Creates a new Context for a specified device.
     *
     * @param device The device the Context is being created for.
     */
    public Context(final Device device) {
        this.device = device;
        this.alCtx = AudioSystem3D.alc.alcCreateContext(device.getALDevice(), null);
    }

    /**
     * Returns the OpenAL context.
     */
    public ALCcontext getALContext() {
        return alCtx;
    }

    /** Returns whether {@link #getALContext()} is valid, i.e. not null, e.g. not {@link #destroy()}'ed. */
    public boolean isValid() { return null != alCtx; }

    public int getALCError() {
        return AudioSystem3D.alc.alcGetError(device.getALDevice());
    }

    /**
     * destroys this context freeing its resources.
     */
    public void destroy() {
        lock.lock();
        try {
            if( null != alCtx ) {
                if( threadContextLocked ) {
                    AudioSystem3D.alExt.alcSetThreadContext(null);
                } else {
                    AudioSystem3D.alc.alcMakeContextCurrent(null);
                }
                AudioSystem3D.alc.alcDestroyContext(alCtx);
                alCtx = null;
            }
            device = null;
            // unroll lock !
            while(lock.getHoldCount() > 1) {
                lock.unlock();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Makes this context current.
     */
    public boolean makeCurrent() {
        final boolean r;
        lock.lock();
        if( hasALC_thread_local_context ) {
            threadContextLocked = true;
            r = AudioSystem3D.alExt.alcSetThreadContext(alCtx);
        } else {
            threadContextLocked = false;
            r = AudioSystem3D.alc.alcMakeContextCurrent(alCtx);
        }
        if( !r ) {
            lock.unlock();
        }
        return r;
    }

    /**
     * Release this context.
     */
    public boolean release() {
        final boolean r;
        if( threadContextLocked ) {
            r = AudioSystem3D.alExt.alcSetThreadContext(null);
        } else {
            r = AudioSystem3D.alc.alcMakeContextCurrent(null);
        }
        lock.unlock();
        return r;
    }

    /**
     * Suspend this context
     */
    public void suspend() {
        AudioSystem3D.alc.alcSuspendContext(alCtx);
    }

    /**
     * Gets the device associated with this context.
     *
     * @return the device associated with this context.
     */
    public Device getDevice() {
        return device;
    }
}
