# For multiarch clang crosscompile, i.e. x86_32 target on x86_64 host

set(CMAKE_SYSTEM_PROCESSOR "x86")

link_directories("/usr/lib32")

if(NOT APPLE)
    set(CMAKE_C_FLAGS "-m32 -include ${PROJECT_SOURCE_DIR}/cmake/glibc-compat-symbols.h")
    set(CMAKE_CXX_FLAGS "-m32 -include ${PROJECT_SOURCE_DIR}/cmake/glibc-compat-symbols.h")
endif()

if(APPLE)
    set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -mmacosx-version-min=10.7")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -mmacosx-version-min=10.7")
endif()

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS}" CACHE STRING "c flags")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}" CACHE STRING "c++ flags")
      
message(STATUS "CMAKE_C_COMPILER  : ${CMAKE_C_COMPILER}")
message(STATUS "CMAKE_CXX_COMPILER: ${CMAKE_CXX_COMPILER}")
message(STATUS "CMAKE_C_FLAGS  : ${CMAKE_C_FLAGS}")
message(STATUS "CMAKE_CXX_FLAGS: ${CMAKE_CXX_FLAGS}")

set(LINKER_FLAGS "-static-libgcc -static-libstdc++")
message(STATUS "LINKER_FLAGS: ${LINKER_FLAGS}")

set(CMAKE_SHARED_LINKER_FLAGS "${LINKER_FLAGS}" CACHE STRING "linker flags" FORCE)
set(CMAKE_MODULE_LINKER_FLAGS "${LINKER_FLAGS}" CACHE STRING "linker flags" FORCE)
set(CMAKE_EXE_LINKER_FLAGS "${LINKER_FLAGS}" CACHE STRING "linker flags" FORCE)

