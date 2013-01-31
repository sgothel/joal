
package com.jogamp.openal.test.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.net.URLConnection;

import com.jogamp.common.util.IOUtil;

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
        URLConnection conn = IOUtil.getResource(rl.getClass(), fileName);        
        if (conn == null) {
            return null;
        }
        InputStream stream = null;
        try {
            stream = new BufferedInputStream(conn.getInputStream());
        } catch (IOException e) {
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
