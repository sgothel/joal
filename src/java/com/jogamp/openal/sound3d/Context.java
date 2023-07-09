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
 * @author Athomas Goldberg, Sven Gothel, et al.
 */
public final class Context {
    private final RecursiveLock lock = LockFactory.createRecursiveLock();
    private final Device device;
    private volatile ALCcontext alCtx;
    private boolean threadContextLocked;
    public final boolean hasALC_thread_local_context;
    private static final ThreadLocal<Context> currentContext = new ThreadLocal<Context>();

    /**
     * Creates a new Context for a given {@link ALCcontext} for the specified device.
     *
     * @param realContext {@link ALCcontext} instance, maybe null
     * @param device The device the Context belongs to, must be valid
     */
    public Context(final ALCcontext realContext, final Device device) {
        this.device = device;
        this.alCtx = realContext;
        {
            final boolean v;
            if( makeCurrent(false) ) {
                v = AudioSystem3D.alc.alcIsExtensionPresent(null, ALHelpers.ALC_EXT_thread_local_context) ||
                    AudioSystem3D.alc.alcIsExtensionPresent(device.getALDevice(), ALHelpers.ALC_EXT_thread_local_context);
                release(false);
            } else {
                v = false;
            }
            hasALC_thread_local_context = v;
        }
    }

    /**
     * Creates a new Context for a specified device including native {@link ALCcontext} creation.
     *
     * @param device The device the Context is being created for, must be valid.
     * @param attributes list of {@link ALCcontext} attributes for context creation, maybe empty or null
     */
    public Context(final Device device, final int[] attributes) {
        this( createImpl(device.getALDevice(), attributes), device);
    }

    /**
     * Creates a new {@link ALCcontext}.
     *
     * @param alDevice a valid {@link ALCdevice}
     * @param attributes lost of {@link ALCcontext} attributes for context creation
     */
    private static ALCcontext createImpl(final ALCdevice alDevice, final int[] attributes) {
        if( null != attributes && attributes.length > 0 ) {
            return AudioSystem3D.alc.alcCreateContext(alDevice, attributes, 0);
        } else {
            return AudioSystem3D.alc.alcCreateContext(alDevice, null);
        }
    }

    /**
     * Creates the internal {@link ALCcontext} instance if {@link #getALContext()} is null
     * @param attributes lost of {@link ALCcontext} attributes for context creation
     * @return true if the internal context has been successfully created, otherwise false
     */
    public boolean create(final int[] attributes) {
        lock.lock();
        try {
            if( null == alCtx ) {
                alCtx = createImpl(device.getALDevice(), attributes);
                return null != alCtx;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Recreates the internal {@link ALCcontext} instance, i.e. destroys it first if {@link #getALContext()} not null.
     * <p>
     * Context is made current again if it was current before.
     * </p>
     * @param attributes lost of {@link ALCcontext} attributes for context creation
     * @return true if the internal context has been successfully recreated and made current again if was current before, otherwise false
     */
    public boolean recreate(final int[] attributes) {
        lock.lock();
        try {
            final boolean wasCurrent = this == getCurrentContext();
            destroyImpl();
            alCtx = createImpl(device.getALDevice(), attributes);
            if( null != alCtx ) {
                if( wasCurrent ) {
                    return makeCurrentImpl();
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    /** Returns the OpenAL {@link ALCcontext}. */
    public ALCcontext getALContext() { return alCtx; }

    /** Returns whether {@link #getALContext()} is valid, i.e. not null, e.g. not {@link #destroy()}'ed. */
    public boolean isValid() { return null != alCtx; }

    /** Return {@link ALC#alcGetError(ALCdevice)} using {@link #getDevice()}. */
    public int getALCError() {
        return device.getALCError();
    }

    /**
     * destroys this context freeing its resources.
     */
    public void destroy() {
        lock.lock();
        try {
            destroyImpl();
            if( currentContext.get() == this ) {
                currentContext.set(null);
            }
            // unroll lock !
            while(lock.getHoldCount() > 1) {
                lock.unlock();
            }
        } finally {
            lock.unlock();
        }
    }
    private void destroyImpl() {
        if( null != alCtx ) {
            if( threadContextLocked ) {
                AudioSystem3D.alExt.alcSetThreadContext(null);
            } else {
                AudioSystem3D.alc.alcMakeContextCurrent(null);
            }
            AudioSystem3D.alc.alcDestroyContext(alCtx);
            alCtx = null;
        }
    }

    /**
     * Returns this thread current context.
     * If no context is current, returns null.
     *
     * @return the context current on this thread, or null if no context is current.
     * @see #makeCurrent()
     * @see #release()
     */
    public static Context getCurrentContext() {
        return currentContext.get();
    }

    /** Return the lock count of this context, i.e. 0 if not locked, 1 if locked once, >1 for recursive locks. */
    public int getLockCount() {
        return lock.getHoldCount();
    }

    /**
     * Makes the audio context current on the calling thread.
     * <p>
     * Recursive call to {@link #makeCurrent()} and hence {@link #release()} are supported.
     * </p>
     * <p>
     * At any point in time one context can only be current on one thread,
     * and one thread can only have one context current.
     * </p>
     * @param throwException if true, throws ALException if {@link #getALContext()} is null, current thread holds another context or failed to natively make current
     * @return true if {@link #getALContext()} is valid, current thread holds no other context and context successfully made current, otherwise false
     * @see #release()
     */
    public boolean makeCurrent(final boolean throwException) throws ALException {
        lock.lock();

        if( null == alCtx ) {
            lock.unlock();
            if( throwException ) {
                throw new ALException("Invalid "+this);
            }
            return false;
        }

        // One context can only be current on one thread,
        // and one thread can only have one context current!
        final Context current = getCurrentContext();
        if (current != null) {
            if (current == this) { // implicit recursive locking, lock.getHoldCount() > 1
                return true;
            } else {
                lock.unlock();
                if( throwException ) {
                    throw new ALException("Current thread "+Thread.currentThread()+" holds another "+current+" while claiming this "+this);
                }
                return false;
            }
        }
        final boolean r = makeCurrentImpl();
        if( r ) {
            currentContext.set(this);
        } else {
            lock.unlock();
            if( throwException ) {
                throw new ALException("Context make current failed "+this);
            }
        }
        return r;
    }
    private boolean makeCurrentImpl() {
        if( hasALC_thread_local_context ) {
            threadContextLocked = true;
            return AudioSystem3D.alExt.alcSetThreadContext(alCtx);
        } else {
            threadContextLocked = false;
            return AudioSystem3D.alc.alcMakeContextCurrent(alCtx);
        }
    }

    /**
     * Releases control of this audio context from the current thread, if implementation utilizes context locking.
     * <p>
     * Recursive call to {@link #makeCurrent()} and hence {@link #release()} are supported.
     * </p>
     * <p>
     * If native release fails, internal lock is not released.
     * </p>
     * @param throwException if true, throws ALException if context has not been previously made current on current thread
     *                       or native release failed.
     * @return true if context has previously been made current on the current thread and successfully released, otherwise false
     * @see #makeCurrent()
     */
    public boolean release(final boolean throwException) throws ALException {
        if( !lock.isOwner( Thread.currentThread() ) )  {
            if( throwException ) {
                throw new ALException("Context not held on current thread "+Thread.currentThread()+", "+this);
            }
            return false;
        }
        if( lock.getHoldCount() == 1 ) {
            final boolean r;
            if( threadContextLocked ) {
                r = AudioSystem3D.alExt.alcSetThreadContext(null);
            } else {
                r = AudioSystem3D.alc.alcMakeContextCurrent(null);
            }
            if( r ) {
                currentContext.set(null);
            } else {
                if( throwException ) {
                    throw new ALException("Context release failed "+this);
                }
                return false; // skip unlock!
            }
        }
        lock.unlock();
        return true;
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

    @Override
    public String toString() {
        final String alCtxStr = null != alCtx ? "0x"+Integer.toHexString(alCtx.hashCode()) : "null";
        return "ALContext[this 0x"+Integer.toHexString(hashCode())+", alCtx "+alCtxStr+" lockCount "+lock.getHoldCount()+", on "+device+"]";
    }
}
