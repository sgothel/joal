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

/**
 * This class contains the context-related OpenAL functions.
 *
 * @author Athomas Goldberg
 */
public interface ALC extends ALCConstants {
    /**
     * This method opens a device by name. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALCdevice* alcOpenDevice(const ALubyte *token);</pre>
     *
     * @param deviceName a string describing the device
     *
     * @return an ALC.Device object for the opened device
     */
    public Device alcOpenDevice(String deviceName);

    /**
     * This method closes a device by name. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>void alcCloseDevice ALCDevice *dev);</pre>
     *
     * @param device the opened device to close
     */
    public void alcCloseDevice(Device device);

    /**
     * This method creates a context using a specified device. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>void alcCreateContext(ALCdevice *dev, ALint* attrList);</pre>
     *
     * @param device an opened device
     * @param attrs a set of attributes:
     * <pre>
     *      ALC_FREQUENCY
     *      ALC_REFRESH
     *      ALC_SYNC
     * </pre>
     *
     * @return a context for the specified device.
     */
    public Context alcCreateContext(Device device, int[] attrs);

    /**
     * This method makes the specified context the current context.<br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALCEnum alcMakeContextCurrent(ALvoid *alcHandle);</pre>
     *
     * @param context the new context
     *
     * @return an error code on failure
     */
    public int alcMakeContextCurrent(Context context);

    public void alcFreeCurrentContext();

    /**
     * This method tells a context to begin processing. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>void alcProcessContext(ALvoid *alHandle);</pre>
     *
     * @param context the context to be processed
     */
    public void alcProcessContext(Context context);

    /**
     * This method suspends processing on the current context. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>void alcSusendContext(ALvoid *alcHandle);</pre>
     *
     * @param context the context to be suspended.
     */
    public void alcSuspendContext(Context context);

    /**
     * This method destroys a context. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALCEnum alcDestroyContext(ALvoid *alcHandle);</pre>
     *
     * @param context the context to be destroyed
     */
    public void alcDestroyContext(Context context);

    /**
     * This method retrieves the current context error state. <br>
     * <br>
     *<b>Interface to C Language function:</b>
     * <pre>ALCEnum alcGetError(ALvoid);</pre>
     * @return the current context error state
     */
    public int alcGetError(Device device);

    /**
     * This method retrieves the current context. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>void* alcGetCurrentContext(ALvoid);</pre>
     *
     * @return the current context
     */
    public Context alcGetCurrentContext();

    /**
     * This method returns the Device associated with the given context. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALCdevice* alcGetContextDevice(ALvoid *alcHandle);</pre>
     *
     * @param context the context whose device is being queried
     *
     * @return the device associated with the specified context.
     */
    public Device alcGetContextsDevice(Context context);

    /**
     * This method queries if a specified context extension is available. <br>
     * <br>
     *<b>Interface to C Language function:</b>
     * <pre>ALboolean alcIsExtensionPresent(ALCdevice *device, ALubyte *extName);</pre>
     *
     * @param device the device to be queried for an extension
     * @param extName a string describing the extension
     *
     * @return true is the extension is available,
     * false if the extension is not available
     */
    public boolean alcIsExtensionPresent(Device device, String extName);

    // public Method getProcAddress(Device device, String funcName);
    /**
     * This method retrieves the constant value for a specified constant name. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALenum alcGetEnumValue(ALCdevice *device, ALubyte *enumName);</pre>
     * 
     * @param device the device to be queried for the constant (?)
     * @param enumName a string representation of the constant name
     * 
     * @return the constant value associated with the string representation
     * of the name
     */
    public int alcGetEnumValue(Device device, String enumName);

    /**
     * This method returns Strings related to the context. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALubyte* alcGetString(ALCDevice *device, ALenum pname);</pre>
     *
     * @param device the device to be queried
     * @param attribute the attribute to be retrieved:
     * <pre>
     *      ALC_DEFAULT_DEVICE_SPECIFIER
     *      ALC_DEVICE_SPECIFIER
     *      ALC_EXTENSIONS
     * </pre>
     *
     * @return the string value of the attribute
     */
    public String alcGetString(Device device, int attribute);

    /**
     * This method retrieves integer properties related to the context. <br>
     * <br>
     * <b>Interface to C Language function:</b>
     * <pre>ALvoid alcGetIntegerv(ALCdevice *device, ALenum token, ALsizei n, ALint *data);</pre>
     *
     * @param device the device to be queried
     * @param attribute the attribute to be retrieved
     * @param size the size of the destination array provided
     * @param retValue an array to hold the data to be returned.
     *
     */
    public void alcGetIntegerv(
        Device device,
        int attribute,
        int size,
        int[] retValue);

    /**
     * This class provides a reference to an OpenAL device
     *
     */
    public class Device {
        final int pointer;

        Device(int pointer) {
            this.pointer = pointer;
        }

        public int hashCode() {
            return (int) pointer;
        }

        public boolean equals(Object obj) {
            boolean result = false;

            if (obj instanceof Device && (obj.hashCode() == pointer)) {
                result = true;
            }

            return result;
        }
    }

    /**
     * This class provides a reference to an OpenAL context
     *
     */
    public static class Context {
        final ALC alcContext;
        final int pointer;

        Context(ALC impl, int pointer) {
            this.pointer = pointer;
            this.alcContext = impl;
        }
    }
}
