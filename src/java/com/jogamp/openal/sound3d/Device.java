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

import com.jogamp.openal.*;


/**
 * This class provides a handle to a specific audio device.
 *
 * @author Athomas Goldberg, Sven Gothel, et al.
 */
public final class Device {
    private String name;
    private ALCdevice alDev;

    /**
     * Create a new device by {@link #open()}'ing the named audio device.
     *
     * @param deviceName The specified device name, null for default.
     */
    public Device(final String deviceName) {
        this.name = deviceName;
        this.alDev = null;
        open();
    }

    /** Returns the device name. */
    public String getName() { return name; }

    /** Returns the OpenAL {@link ALCdevice}. */
    public ALCdevice getALDevice() { return alDev; }

    /** Return {@link ALC#alcGetError(ALCdevice)} */
    public int getALCError() {
        return AudioSystem3D.alc.alcGetError(alDev);
    }

    /** Returns whether {@link #getALDevice()} is open and valid, i.e. not null, e.g. not {@link #close()}. */
    public boolean isValid() { return null != alDev; }

    /**
     * Opens the device if not yet opened
     * @return true if already open or newly opened
     * @see #isValid()
     * @see #clone()
     */
    public boolean open() {
        if( null == alDev ) {
            alDev = AudioSystem3D.alc.alcOpenDevice(name);
            if( null != alDev && null == name ) {
                name = AudioSystem3D.alc.alcGetString(alDev, ALCConstants.ALC_DEVICE_SPECIFIER);
            }
        }
        return isValid();
    }

    /**
     * closes the device, freeing its resources.
     */
    public void close() {
        if( null != alDev ) {
            AudioSystem3D.alc.alcCloseDevice(alDev);
            alDev = null;
        }
    }

    @Override
    public String toString() {
        final String alStr = null != alDev ? "0x"+Integer.toHexString(alDev.hashCode()) : "null";
        return "ALDevice[this 0x"+Integer.toHexString(hashCode())+", name '"+name+"', alDev "+alStr+"]";
    }

}
