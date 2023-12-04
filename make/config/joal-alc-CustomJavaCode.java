/** Specify if ALC_ENUMERATION_EXT is present */
public boolean alcEnumerationExtIsPresent();

/** Specify if ALC_ENUMERATE_ALL_EXT is present */
public boolean alcEnumerateAllExtIsPresent();

/** Specify if call of alGetString(device, param) must
    must retrun a double null terminted string */
public boolean alcIsDoubleNullTerminatedString(final com.jogamp.openal.ALCdevice device, final int param);

/** Fetches all values of device and param supplied from result of call to alcGetString
    Each value is extracted from string because is a string double null terminated
    Equivalent to the C call alcGetString(device, param). */
public java.lang.String[] alcGetStringAsDoubleNullTerminatedString(final com.jogamp.openal.ALCdevice device, final int param);

/** Fetches the names of the available ALC device specifiers.
    Equivalent to the C call alcGetString(NULL, ALC_DEVICE_SPECIFIER). */
public java.lang.String[] alcGetDeviceSpecifiers();

/** Fetches the names of the available ALC capture device specifiers.
    Equivalent to the C call alcGetString(NULL, ALC_CAPTURE_DEVICE_SPECIFIER). */
public java.lang.String[] alcGetCaptureDeviceSpecifiers();

/** Fetches the names of the available ALC all capture device specifiers.
    Equivalent to the C call alcGetString(NULL, ALC_ALL_DEVICES_SPECIFIER). */
public java.lang.String[] alcGetAllDeviceSpecifiers();
