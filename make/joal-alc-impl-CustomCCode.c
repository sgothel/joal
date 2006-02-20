int strlen_alc(ALCdevice *device, int param, const char* str) {
  int len = 0;
  if ((device == NULL) && (param == ALC_DEVICE_SPECIFIER)) {
    while (*str != 0) {
      while (*str != 0) {
        ++str;
        ++len;
      }
      ++str;
      ++len;
    }
    return len;
  } else {
    return strlen(str);
  }
}
