static final DynamicLibraryBundle alDynamicLookupHelper;
private static final ALProcAddressTable alProcAddressTable;

static {
    alProcAddressTable = new ALProcAddressTable();
    if(null==alProcAddressTable) {
      throw new RuntimeException("Couldn't instantiate ALProcAddressTable");
    }

    alDynamicLookupHelper = AccessController.doPrivileged(new PrivilegedAction<DynamicLibraryBundle>() {
                                public DynamicLibraryBundle run() {
                                    return new DynamicLibraryBundle(new ALDynamicLibraryBundleInfo());
                                } } );

    if(null==alDynamicLookupHelper) {
      throw new RuntimeException("Null ALDynamicLookupHelper");
    }
    if(!alDynamicLookupHelper.isToolLibLoaded()) {
      throw new RuntimeException("Couln't load native AL library");
    }
    if(!alDynamicLookupHelper.isLibComplete()) {
      throw new RuntimeException("Couln't load native AL/JNI glue library");
    }
    alProcAddressTable.reset(alDynamicLookupHelper);
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

