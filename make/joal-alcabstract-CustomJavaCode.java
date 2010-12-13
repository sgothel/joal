private static final ALCProcAddressTable alcProcAddressTable;

static {
    alcProcAddressTable = new ALCProcAddressTable();
    if(null==alcProcAddressTable) {
      throw new RuntimeException("Couldn't instantiate ALCProcAddressTable");
    }
    alcProcAddressTable.reset(ALImpl.alDynamicLookupHelper);
}

public static ALCProcAddressTable getALCProcAddressTable() { return alcProcAddressTable; }

