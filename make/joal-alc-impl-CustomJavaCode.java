public java.lang.String alcGetString(ALCdevice device, int param) {
  if (device == null && param == ALC_DEVICE_SPECIFIER) {
    throw new ALException("Call alcGetDeviceSpecifiers to fetch all available device names");
  }

  ByteBuffer buf = alcGetStringImpl(device, param);
  if (buf == null) {
    return null;
  }
  byte[] res = new byte[buf.capacity()];
  buf.get(res);
  try {
    return new String(res, "US-ASCII");
  } catch (UnsupportedEncodingException e) {
    throw new ALException(e);
  }
}

/** Fetches the names of the available ALC device specifiers.
    Equivalent to the C call alcGetString(NULL, ALC_DEVICE_SPECIFIER). */
public java.lang.String[] alcGetDeviceSpecifiers() {
  ByteBuffer buf = alcGetStringImpl(null, ALC_DEVICE_SPECIFIER);
  if (buf == null) {
    return null;
  }
  byte[] bytes = new byte[buf.capacity()];
  buf.get(bytes);
  try {
    ArrayList/*<String>*/ res = new ArrayList/*<String>*/();
    int i = 0;
    while (i < bytes.length) {
      int startIndex = i;
      while ((i < bytes.length) && (bytes[i] != 0))
        i++;
      res.add(new String(bytes, startIndex, i - startIndex, "US-ASCII"));
      i++;
    }
    return (String[]) res.toArray(new String[0]);
  } catch (UnsupportedEncodingException e) {
    throw new ALException(e);
  }
}
