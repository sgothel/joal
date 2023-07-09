private static final ALCProcAddressTable alcProcAddressTable;

static {
    alcProcAddressTable = SecurityUtil.doPrivileged(new PrivilegedAction<ALCProcAddressTable>() {
                                public ALCProcAddressTable run() {
                                    final ALCProcAddressTable alcProcAddressTable = new ALCProcAddressTable();
                                    alcProcAddressTable.reset(ALImpl.alDynamicLookupHelper);
                                    /** Not required nor forced
                                    if( !initializeImpl() ) {
                                        throw new RuntimeException("Initialization failure");
                                    } */
                                    return alcProcAddressTable;
                                } } );
}

public static ALCProcAddressTable getALCProcAddressTable() { return alcProcAddressTable; }

