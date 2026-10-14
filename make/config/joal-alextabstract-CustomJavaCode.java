private static final ALExtProcAddressTable alExtProcAddressTable;

static {
    alExtProcAddressTable = new ALExtProcAddressTable();
    alExtProcAddressTable.reset(ALImpl.alDynamicLookupHelper);
    /** Not required nor forced
    if( !initializeImpl() ) {
        throw new RuntimeException("Initialization failure");
    } */
}

public static ALExtProcAddressTable getALExtProcAddressTable() { return alExtProcAddressTable; }

