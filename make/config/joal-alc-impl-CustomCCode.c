#include <stdbool.h>

bool alc_is_double_null_terminated_string(ALCdevice *device, int param) {
  return device == NULL && (
    param == ALC_DEVICE_SPECIFIER ||
    param == ALC_CAPTURE_DEVICE_SPECIFIER ||
    param == ALC_ALL_DEVICES_SPECIFIER
  );
}

int strlen_alc(ALCdevice *device, int param, const char* str) {
  int len = 0;
  if (alc_is_double_null_terminated_string(device, param)) {
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
