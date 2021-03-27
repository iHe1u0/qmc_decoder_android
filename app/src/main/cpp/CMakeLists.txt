cmake_minimum_required(VERSION 3.10.2)
project("qmc_decoder")
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

include_directories(3rdparty/filesystem/include)

add_library(main SHARED jniInterface.cpp src/decoder.cpp src/seed.hpp)

#find_library(log-lib log )

target_link_libraries(main)

#
#if (MSVC)
#    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /O2")
#    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /std:c++17")
#else(MSVC)
#    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O2 -pipe -std=c++11")
#endif()
#
#if(CMAKE_HOST_SYSTEM_NAME STREQUAL "Linux")
#    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -static -pthread -static-libgcc -static-libstdc++")
#endif()
#
#aux_source_directory(src SRC)
#add_executable(decoder ${SRC})
