package com.jogamp.openal.test.android;

import java.util.Arrays;
import java.util.List;

import com.jogamp.openal.test.android.LauncherUtil.OrderedProperties;

public class OpenALTestActivityLauncher extends LauncherUtil.BaseActivityLauncher {
    static String demo = "com.jogamp.openal.test.android.OpenALTestActivity";
    static String[] sys_pkgs = new String[] { "com.jogamp.common", "com.jogamp.openal" };    
    static String[] usr_pkgs = new String[] { "com.jogamp.openal.test" };
    
    @Override
    public void init() {
       final OrderedProperties props = getProperties();       
       props.setProperty("jogamp.debug.JNILibLoader", "true");
       props.setProperty("jogamp.debug.NativeLibrary", "true");
       props.setProperty("jogamp.debug.NativeLibrary.Lookup", "true");
       props.setProperty("jogamp.debug.IOUtil", "true");
    }
    
    @Override
    public String getActivityName() {
        return demo;
    }
    
    @Override
    public List<String> getSysPackages() {
        return Arrays.asList(sys_pkgs);
    }
    
    @Override
    public List<String> getUsrPackages() {
        return Arrays.asList(usr_pkgs);
    }
}
