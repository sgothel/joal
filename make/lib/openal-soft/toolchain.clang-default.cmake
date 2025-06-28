# For normal clang compilation

if(NOT APPLE)
    set(CMAKE_C_FLAGS "-include ${PROJECT_SOURCE_DIR}/cmake/glibc-compat-symbols.h")
    set(CMAKE_CXX_FLAGS "-include ${PROJECT_SOURCE_DIR}/cmake/glibc-compat-symbols.h")
endif()

if(APPLE)
    # was 10.5, but `-stdlib=libc++` requires >= 10.7 (deployment target)
    # OpenAL-Soft v1.24.3 (2025-06-28) requires >= 10.14
    # Note:
    # - kAudioObjectPropertyElementMain   since macOS 12
    # - kAudioObjectPropertyElementMaster gone in macOS 12
    set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -mmacosx-version-min=10.14 -DkAudioObjectPropertyElementMain=kAudioObjectPropertyElementMaster")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -mmacosx-version-min=10.14 -DkAudioObjectPropertyElementMain=kAudioObjectPropertyElementMaster")
endif()

# inject additional architectures for fat-binary (macosx)
# CMAKE_EXTRA_ARCHS is set from JOAL's ant build.xml
if(NOT APPLE)
    set(CC_XTRA_FLAGS "")
else()
    set(CC_XTRA_FLAGS "-stdlib=libc++")
endif()
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} ${CC_XTRA_FLAGS} ${CMAKE_EXTRA_ARCHS}")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${CC_XTRA_FLAGS} ${CMAKE_EXTRA_ARCHS}")

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS}" CACHE STRING "c flags")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}" CACHE STRING "c++ flags")

message(STATUS "CMAKE_C_COMPILER  : ${CMAKE_C_COMPILER}")
message(STATUS "CMAKE_CXX_COMPILER: ${CMAKE_CXX_COMPILER}")
message(STATUS "CMAKE_C_FLAGS  : ${CMAKE_C_FLAGS}")
message(STATUS "CMAKE_CXX_FLAGS: ${CMAKE_CXX_FLAGS}")

if(NOT APPLE)
    set(LINKER_FLAGS "-static-libgcc -static-libstdc++")
else()
    set(LINKER_FLAGS "-stdlib=libc++ -lc++abi")
endif()
message(STATUS "LINKER_FLAGS: ${LINKER_FLAGS}")

set(CMAKE_SHARED_LINKER_FLAGS "${LINKER_FLAGS}" CACHE STRING "linker flags" FORCE)
set(CMAKE_MODULE_LINKER_FLAGS "${LINKER_FLAGS}" CACHE STRING "linker flags" FORCE)
set(CMAKE_EXE_LINKER_FLAGS "${LINKER_FLAGS}" CACHE STRING "linker flags" FORCE)

set(CMAKE_CXX_COMPILER "clang++")

