private static final ALCProcAddressTable alcProcAddressTable;

static {
    alcProcAddressTable = new ALCProcAddressTable();
    alcProcAddressTable.reset(ALImpl.alDynamicLookupHelper);
    /** Not required nor forced
    if( !initializeImpl() ) {
        throw new RuntimeException("Initialization failure");
    } */
}

public static ALCProcAddressTable getALCProcAddressTable() { return alcProcAddressTable; }

