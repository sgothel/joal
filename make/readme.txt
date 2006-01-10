We needed to modify the OpenAL headers from creative.com (the ones
from the Windows installer) in the following ways:

 - In the untaken arms of the #ifdef _WIN32 clauses at the top of al.h
   and alc.h, change the #define ALAPIENTRY and #define ALCAPIENTRY to
   define those to the empty token rather than to __cdecl.

 - Hoist the typedefs of ALCdevice and ALCcontext in alc.h out of the
   #ifdef _WIN32 clause.
