/** Fetches the names of the available ALC device specifiers.
    Equivalent to the C call alcGetString(NULL, ALC_DEVICE_SPECIFIER). */
public java.lang.String[] alcGetDeviceSpecifiers();

/** Fetches the names of the available ALC capture device specifiers.
    Equivalent to the C call alcGetString(NULL, ALC_CAPTURE_DEVICE_SPECIFIER). */
public java.lang.String[] alcGetCaptureDeviceSpecifiers();
