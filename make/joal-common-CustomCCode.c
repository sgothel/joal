#ifdef _MSC_VER /* Windows, Microsoft compilers */
/* This typedef is apparently needed for compilers before VC8 */
#if _MSC_VER < 1400
typedef int intptr_t;
#endif
#else
/* This header seems to be available on all other platforms */
#include <inttypes.h>
#endif
#include <string.h>
