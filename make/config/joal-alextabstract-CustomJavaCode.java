private static final ALExtProcAddressTable alExtProcAddressTable;

static {
    alExtProcAddressTable = AccessController.doPrivileged(new PrivilegedAction<ALExtProcAddressTable>() {
                                public ALExtProcAddressTable run() {
                                    final ALExtProcAddressTable alExtProcAddressTable = new ALExtProcAddressTable();
                                    if(null==alExtProcAddressTable) {
                                      throw new RuntimeException("Couldn't instantiate ALExtProcAddressTable");
                                    }
                                    alExtProcAddressTable.reset(ALImpl.alDynamicLookupHelper);
                                    if( !initializeImpl() ) {
                                        throw new RuntimeException("Initialization failure");
                                    }                                      
                                    return alExtProcAddressTable;
                                } } );
}

public static ALExtProcAddressTable getALExtProcAddressTable() { return alExtProcAddressTable; }

