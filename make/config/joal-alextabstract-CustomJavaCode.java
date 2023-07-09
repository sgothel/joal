private static final ALExtProcAddressTable alExtProcAddressTable;

static {
    alExtProcAddressTable = SecurityUtil.doPrivileged(new PrivilegedAction<ALExtProcAddressTable>() {
                                public ALExtProcAddressTable run() {
                                    final ALExtProcAddressTable alExtProcAddressTable = new ALExtProcAddressTable();
                                    alExtProcAddressTable.reset(ALImpl.alDynamicLookupHelper);
                                    /** Not required nor forced
                                    if( !initializeImpl() ) {
                                        throw new RuntimeException("Initialization failure");
                                    } */
                                    return alExtProcAddressTable;
                                } } );
}

public static ALExtProcAddressTable getALExtProcAddressTable() { return alExtProcAddressTable; }

