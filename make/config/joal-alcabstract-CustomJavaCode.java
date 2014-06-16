private static final ALCProcAddressTable alcProcAddressTable;

static {
    alcProcAddressTable = AccessController.doPrivileged(new PrivilegedAction<ALCProcAddressTable>() {
                                public ALCProcAddressTable run() {
                                    final ALCProcAddressTable alcProcAddressTable = new ALCProcAddressTable();
                                    if(null==alcProcAddressTable) {
                                      throw new RuntimeException("Couldn't instantiate ALCProcAddressTable");
                                    }
                                    alcProcAddressTable.reset(ALImpl.alDynamicLookupHelper);
                                    if( !initializeImpl() ) {
                                        throw new RuntimeException("Initialization failure");
                                    }                                      
                                    return alcProcAddressTable;
                                } } );
}

public static ALCProcAddressTable getALCProcAddressTable() { return alcProcAddressTable; }

