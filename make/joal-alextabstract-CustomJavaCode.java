private static final ALExtProcAddressTable alExtProcAddressTable;

static {
    alExtProcAddressTable = new ALExtProcAddressTable();
    if(null==alExtProcAddressTable) {
      throw new RuntimeException("Couldn't instantiate ALExtProcAddressTable");
    }
    alExtProcAddressTable.reset(ALImpl.alDynamicLookupHelper);
}

public static ALExtProcAddressTable getALExtProcAddressTable() { return alExtProcAddressTable; }

