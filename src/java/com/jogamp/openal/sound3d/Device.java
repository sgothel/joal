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
 * @author Athomas Goldberg
 */
public class Device {
    private ALCdevice alDev;

    public Device(final ALCdevice realDevice) {
        this.alDev = realDevice;
    }

    /**
     * Create a new device by opening the named audio device.
     *
     * @param deviceName The specified device name, null for default.
     */
    public Device(final String deviceName) {
        this.alDev = AudioSystem3D.alc.alcOpenDevice(deviceName);
    }

    /**
     * Returns the OpenAL context.
     */
    public ALCdevice getALDevice() {
        return alDev;
    }

    /** Returns whether {@link #getALDevice()} is valid, i.e. not null, e.g. not {@link #close()}. */
    public boolean isValid() { return null != alDev; }

    /**
     * closes the device, freeing its resources.
     */
    public void close() {
        AudioSystem3D.alc.alcCloseDevice(alDev);
        alDev = null;
    }
}
