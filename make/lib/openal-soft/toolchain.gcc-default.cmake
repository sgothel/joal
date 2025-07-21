# For normal gcc compilation, but use static-libgcc

if(NOT APPLE)
    set(CMAKE_C_FLAGS "-include ${GLUEGEN_ROOT}/make/stub_includes/platform/glibc-compat-symbols.h")
    set(CMAKE_CXX_FLAGS "-include ${GLUEGEN_ROOT}/make/stub_includes/platform/glibc-compat-symbols.h")
endif()

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS}" CACHE STRING "c flags")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}" CACHE STRING "c++ flags")
message(STATUS "CMAKE_C_FLAGS: ${CMAKE_C_FLAGS}")
message(STATUS "CMAKE_CXX_FLAGS: ${CMAKE_CXX_FLAGS}")

set(LINKER_FLAGS "-static-libgcc -static-libstdc++")
message(STATUS "LINKER_FLAGS: ${LINKER_FLAGS}")

set(CMAKE_SHARED_LINKER_FLAGS "${LINKER_FLAGS}" CACHE STRING "linker flags" FORCE)
set(CMAKE_MODULE_LINKER_FLAGS "${LINKER_FLAGS}" CACHE STRING "linker flags" FORCE)
set(CMAKE_EXE_LINKER_FLAGS "${LINKER_FLAGS}" CACHE STRING "linker flags" FORCE)

message(STATUS "PKG_CONFIG_LIBDIR: $ENV{PKG_CONFIG_LIBDIR}")
message(STATUS "PKG_CONFIG_PATH: $ENV{PKG_CONFIG_PATH}")
