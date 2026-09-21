cmake_minimum_required(VERSION 3.22.1)
project(Aerocore)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

add_library(Aerocore SHARED
    ../../cpp/aerocore_native.cpp
)

find_library(log-lib log)

target_link_libraries(Aerocore
    ${log-lib}
)
