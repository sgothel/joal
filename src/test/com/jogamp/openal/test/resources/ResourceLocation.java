
package com.jogamp.openal.test.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.net.URLConnection;

import com.jogamp.common.util.IOUtil;

/** just a tag to locate the resources */
public class ResourceLocation {
    /** WAV 22050Hz, 1 channel, S8_LE */
    public static final String lewiscarrol_wav = "lewiscarroll.wav";
    public static final int lewiscarrol_wav_size = 1025476;
    /** CDR 44100Hz, 2 channels, S16_BE */
    public static final String aa_cdr = "aa.cdr";
    public static final int aa_cdr_size = 846704;
    /** CDR 44100Hz, 2 channels, S16_LE */
    public static final String aa_cd = "aa.cd";
    public static final int aa_cd_size = 846704;
    /** CDR 44100Hz, 2 channels, S16_LE */
    public static final String aa_wav = "aa.wav";
    public static final int aa_wav_size = 846748;

    static final ResourceLocation rl;

    static {
        rl = new ResourceLocation();

    }

    /** WAV 22050Hz, 1 channel, S8_LE */
    public static InputStream getTestStream0() {
        return getInputStream(lewiscarrol_wav, true);
    }
    public static int getTestStream0Size() {
        return lewiscarrol_wav_size;
    }
    /** CDR 44100Hz, 2 channels, S16_BE */
    public static InputStream getTestStream1() {
        return getInputStream(aa_cdr, true);
    }
    public static int getTestStream1Size() {
        return aa_cdr_size;
    }
    /** CDR 44100Hz, 2 channels, S16_LE */
    public static InputStream getTestStream2() {
        return getInputStream(aa_cd, true);
    }
    public static int getTestStream2Size() {
        return aa_cd_size;
    }
    /** WAV 44100Hz, 2 channels, S16_LE */
    public static InputStream getTestStream3() {
        return getInputStream(aa_wav, true);
    }
    public static int getTestStream3Size() {
        return aa_wav_size;
    }

    public static InputStream getInputStream(final String fileName) {
        return getInputStream(fileName, false);
    }

    public static InputStream getInputStream(final String fileName, final boolean throwException) {
        final URLConnection conn = IOUtil.getResource(fileName, rl.getClass().getClassLoader(), rl.getClass());
        if (conn == null) {
            return null;
        }
        InputStream stream = null;
        try {
            stream = new BufferedInputStream(conn.getInputStream());
        } catch (final IOException e) {
            if(throwException && null == stream) {
                throw new RuntimeException("File '"+fileName+"' not found: "+e.getMessage());
            } else {
                System.err.println("File '"+fileName+"' not found");
                e.printStackTrace();
            }
        }
        return stream;
    }
}
