static final DynamicLibraryBundle alDynamicLookupHelper;
private static final ALProcAddressTable alProcAddressTable;

static {
    alProcAddressTable = new ALProcAddressTable();
    if(null==alProcAddressTable) {
      throw new RuntimeException("Couldn't instantiate ALProcAddressTable");
    }

    alDynamicLookupHelper = SecurityUtil.doPrivileged(new PrivilegedAction<DynamicLibraryBundle>() {
                                public DynamicLibraryBundle run() {
                                    final DynamicLibraryBundle bundle =  new DynamicLibraryBundle(new ALDynamicLibraryBundleInfo());
                                    if(!bundle.isToolLibLoaded()) {
                                      throw new RuntimeException("Couln't load native AL library");
                                    }
                                    if(!bundle.isLibComplete()) {
                                      throw new RuntimeException("Couln't load native AL/JNI glue library");
                                    }
                                    alProcAddressTable.reset(bundle);
                                    /** Not required nor forced
                                    if( !initializeImpl() ) {
                                        throw new RuntimeException("Initialization failure");
                                    } */
                                    return bundle;
                                } } );
}

public static ALProcAddressTable getALProcAddressTable() { return alProcAddressTable; }

static long alGetProcAddress(long alGetProcAddressHandle, java.lang.String procname)
{
    if (alGetProcAddressHandle == 0) {
        throw new RuntimeException("Passed null pointer for method \"alGetProcAddress\"");
    }
    return dispatch_alGetProcAddressStatic(procname, alGetProcAddressHandle);
}

static native long dispatch_alGetProcAddressStatic(String fname, long procAddress);

