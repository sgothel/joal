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

package net.java.games.joal;

import java.util.HashMap;

final class ALCImpl implements ALC {
    private final HashMap contextMap = new HashMap();

    ALCImpl() {
        System.loadLibrary("joal");
        /*
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                exit();
            }
        }));
        */
    }

    public Device alcOpenDevice(String deviceName) {
        Device result = null;
        int pointer = openDeviceNative(deviceName);
        if(pointer != 0) {
            result = new Device(openDeviceNative(deviceName));
        }
        return result;
    }

    private native int openDeviceNative(String deviceName);

    public void alcCloseDevice(Device device) {
        if(device != null) {
            closeDeviceNative(device.pointer);
        }
    }

    private native void closeDeviceNative(int pointer);

    public Context alcCreateContext(Device device, int[] attrs) {
        Context result = null;
        if(device != null) {
            int pointer = createContextNative(device.pointer, attrs);
            if (pointer != 0) {
                result = new Context(this, pointer);
                contextMap.put(new Integer(pointer), result);
            }
        }
        return result;
    }

    private native int createContextNative(int pointer, int[] attrs);

    public int alcMakeContextCurrent(Context context) {
        int result = 0;
        int pointer = 0;

        if (context != null) {
            pointer = context.pointer;
        }

        result = makeContextCurrentNative(pointer);

        return result;
    }

    private native int makeContextCurrentNative(int pointer);

    public void alcProcessContext(Context context) {
        if(context != null) {
            processContextNative(context.pointer);
        }
    }

    private native void processContextNative(int pointer);

    public void alcSuspendContext(Context context) {
        if(context != null) {
            suspendContextNative(context.pointer);
        }
    }

    private native void suspendContextNative(int pointer);

    public void alcDestroyContext(Context context) {
        if(context != null) {
            destroyContextNative(context.pointer);
        }
    }

    private native void destroyContextNative(int pointer);

    public native int alcGetError();

    public Context alcGetCurrentContext() {
        Context result = null;
        int pointer = getCurrentContextNative();
        if(pointer != 0) {
            result = (Context) contextMap.get(new Integer(pointer));
        }
        return result;
    }

    private native int getCurrentContextNative();

    public boolean alcIsExtensionPresent(Device device, String extName) {
        boolean result = false;

        return result;
    }

    // public Method getProcAddress(Device device, String funcName);
    public int alcGetEnumValue(Device device, String enumName) {
        return getEnumValueNative(device.pointer, enumName);
    }

    private native int getEnumValueNative(int pointer, String enumValue);

    public String alcGetString(Device device, int attribute) {
        String result = null;
        result = alcGetStringNative(device.pointer, attribute);
        return result;
    }

    private native String alcGetStringNative(int pointer, int attribute);

    public void alcGetIntegerv(
        Device device,
        int attribute,
        int size,
        int[] array) {
        alcGetIntegervNative(device.pointer, attribute, size, array);
    }

    private native void alcGetIntegervNative(
        int pointer,
        int attr,
        int size,
        int[] arr);

    public Device alcGetContextsDevice(Context context) {
        Device result = null;
        if(context != null) {
            int devicePtr = getContextsDeviceNative(context.pointer);
            if(devicePtr != 0) {
                result = new ALC.Device(devicePtr);
            }
        }
        return result;
    }

    private native int getContextsDeviceNative(int context);

    private void exit() {

        Context alcContext = alcGetCurrentContext();

        if (alcContext != null) {
            Device alcDevice = alcGetContextsDevice(alcContext);
            alcMakeContextCurrent(null);
            alcDestroyContext(alcContext);
            alcCloseDevice(alcDevice);
        }
    }
}
