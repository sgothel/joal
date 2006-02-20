#ifdef _MSC_VER /* Windows, Microsoft compilers */
/* This typedef is only needed for VC6 */
#if _MSC_VER <= 1200
typedef int intptr_t;
#endif
#else
/* This header seems to be available on all other platforms */
#include <inttypes.h>
#endif
#include <string.h>
