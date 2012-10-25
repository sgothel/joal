
package com.jogamp.openal.test.resources;

import java.io.InputStream;
import java.io.BufferedInputStream;

/** just a tag to locate the resources */
public class ResourceLocation {
    public static final String lewiscarrol_wav = "lewiscarroll.wav";

    static final ResourceLocation rl;

    static {
        rl = new ResourceLocation();

    }

    public static InputStream getTestStream0() {
        return getInputStream(lewiscarrol_wav, true);
    }

    public static InputStream getInputStream(String fileName) {
        return getInputStream(fileName, false);
    }

    public static InputStream getInputStream(String fileName, boolean throwException) {
        InputStream stream = new BufferedInputStream(rl.getClass().getResourceAsStream(fileName));
        if(throwException && null == stream) {
            throw new RuntimeException("File '"+fileName+"' not found");
        }
        return stream;
    }
}
