/*
 * Created on Saturday, July 10 2010 17:08
 */
package jogamp.openal;

import com.jogamp.common.nio.Buffers;
import com.jogamp.openal.ALException;
import com.jogamp.openal.ALCdevice;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * ALC implementation.
 * @author Michael Bien
 */
public class ALCImpl extends ALCAbstractImpl {

    public String alcGetString(final ALCdevice device, final int param) {
        if (device == null && param == ALC_DEVICE_SPECIFIER) {
            throw new ALException("Call alcGetDeviceSpecifiers to fetch all available device names");
        }

        final ByteBuffer buf = alcGetStringImpl(device, param);
        if (buf == null) {
            return null;
        }
        final byte[] res = new byte[buf.capacity()];
        buf.get(res);
        try {
            return new String(res, "US-ASCII");
        } catch (final UnsupportedEncodingException e) {
            throw new ALException(e);
        }
    }

    /** Entry point (through function pointer) to C language function: <br> <code> const ALCchar *  alcGetString(ALCdevice *  device, ALCenum param); </code>    */
    public ByteBuffer alcGetStringImpl(final ALCdevice device, final int param) {

        final long __addr_ = getALCProcAddressTable()._addressof_alcGetString;
        if (__addr_ == 0) {
            throw new UnsupportedOperationException("Method \"alcGetStringImpl\" not available");
        }
        ByteBuffer _res;
        _res = dispatch_alcGetStringImpl1(((device == null) ? null : device.getBuffer()), param, __addr_);
        if (_res == null) {
            return null;
        }
        Buffers.nativeOrder(_res);
        return _res;
    }
    private native java.nio.ByteBuffer dispatch_alcGetStringImpl1(ByteBuffer deviceBuffer, int param, long addr);

    /**
     * Fetches the names of the available ALC device specifiers.
     * Equivalent to the C call alcGetString(NULL, ALC_DEVICE_SPECIFIER).
     */
    public String[] alcGetDeviceSpecifiers() {
        return getDoubleNullTerminatedString(ALC_DEVICE_SPECIFIER);
    }

    /**
     * Fetches the names of the available ALC capture device specifiers.
     * Equivalent to the C call alcGetString(NULL, ALC_CAPTURE_DEVICE_SPECIFIER).
     */
    public String[] alcGetCaptureDeviceSpecifiers() {
        return getDoubleNullTerminatedString(ALC_CAPTURE_DEVICE_SPECIFIER);
    }

    private String[] getDoubleNullTerminatedString(final int which) {
        final ByteBuffer buf = alcGetStringImpl(null, which);
        if (buf == null) {
            return null;
        }
        final byte[] bytes = new byte[buf.capacity()];
        buf.get(bytes);
        try {
            final ArrayList/*<String>*/ res = new ArrayList/*<String>*/();
            int i = 0;
            while (i < bytes.length) {
                final int startIndex = i;
                while ((i < bytes.length) && (bytes[i] != 0)) {
                    i++;
                }
                res.add(new String(bytes, startIndex, i - startIndex, "US-ASCII"));
                i++;
            }
            return (String[]) res.toArray(new String[res.size()]);
        } catch (final UnsupportedEncodingException e) {
            throw new ALException(e);
        }
    }
}
