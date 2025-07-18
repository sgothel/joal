# For normal gcc compilation, but use static-libgcc

set(CMAKE_SYSTEM_NAME Linux)
set(CMAKE_SYSTEM_VERSION 1)

# Skip utils + examples due to libsndfile dependency
set(ALSOFT_UTILS OFF)
set(ALSOFT_EXAMPLES OFF)

if(NOT APPLE)
    # -idirafter will be searched after implicit system-dir include '-I =/usr/include' from TARGET_PLATFORM_SYSROOT
    set(CMAKE_C_FLAGS "-march=armv6 -mfpu=vfp -mfloat-abi=hard -marm -include ${PROJECT_SOURCE_DIR}/../../gluegen/make/stub_includes/platform/glibc-compat-symbols.h")
    set(CMAKE_CXX_FLAGS "-march=armv6 -mfpu=vfp -mfloat-abi=hard -marm -include ${PROJECT_SOURCE_DIR}/../../gluegen/make/stub_includes/platform/glibc-compat-symbols.h")
endif()

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS}" CACHE STRING "c flags")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}" CACHE STRING "c++ flags")
message(STATUS "TCF: CMAKE_C_FLAGS: ${CMAKE_C_FLAGS}")
message(STATUS "TCF: CMAKE_CXX_FLAGS: ${CMAKE_CXX_FLAGS}")

set(LINKER_FLAGS "-march=armv6 -mfpu=vfp -mfloat-abi=hard -marm -static-libgcc -static-libstdc++ -L${TARGET_PLATFORM_USRLIBS} -static-libgcc -static-libstdc++")
message(STATUS "TCF: LINKER_FLAGS: ${LINKER_FLAGS}")

set(CMAKE_SHARED_LINKER_FLAGS "${LINKER_FLAGS}" CACHE STRING "linker flags" FORCE)
set(CMAKE_MODULE_LINKER_FLAGS "${LINKER_FLAGS}" CACHE STRING "linker flags" FORCE)
set(CMAKE_EXE_LINKER_FLAGS "${LINKER_FLAGS}" CACHE STRING "linker flags" FORCE)

# here is the target environment located
SET(CMAKE_FIND_ROOT_PATH ${TARGET_PLATFORM_SYSROOT};${TARGET_PLATFORM_SYSROOT}/usr;${TARGET_PLATFORM_USRROOT}/usr)
message(STATUS "TCF: CMAKE_FIND_ROOT_PATH: ${CMAKE_FIND_ROOT_PATH}")

# adjust the default behaviour of the FIND_XXX() commands:
# search headers and libraries in the target environment, search 
# programs in the host environment
set(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
set(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
set(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)

# set env vars so that pkg-config will look in the appropriate directory for
# .pc files (as there seems to be no way to force using ${HOST}-pkg-config)
# Passing env-var via CMAKE_TOOLCHAIN_FILE didn't work w/ cmake 3.25.1 on GNU/Linux (jau)
set(PKG_CONFIG_LIBDIR "${TARGET_PLATFORM_USRROOT}/usr/lib/pkgconfig" CACHE STRING "pkg_config libdir")
set(ENV{PKG_CONFIG_LIBDIR} "${PKG_CONFIG_LIBDIR}")
set(ENV{PKG_CONFIG_PATH} "")
message(STATUS "TCF: PKG_CONFIG_LIBDIR: ${PKG_CONFIG_LIBDIR}")
message(STATUS "TCF: env PKG_CONFIG_LIBDIR: $ENV{PKG_CONFIG_LIBDIR}")
message(STATUS "TCF: env PKG_CONFIG_PATH: $ENV{PKG_CONFIG_PATH}")

# Use TARGET_PLATFORM_USRROOT prefix for pkg-config includes
set(PKG_CONFIG_SYSROOT_DIR "${TARGET_PLATFORM_USRROOT}" CACHE STRING "pkg_config sysroot prefix")
set(ENV{PKG_CONFIG_SYSROOT_DIR} "${PKG_CONFIG_SYSROOT_DIR}")
message(STATUS "TCF: PKG_CONFIG_SYSROOT_DIR: ${PKG_CONFIG_SYSROOT_DIR}")
message(STATUS "TCF: env PKG_CONFIG_SYSROOT_DIR: $ENV{PKG_CONFIG_SYSROOT_DIR}")
