cmake_minimum_required(VERSION 3.18)
project(GaussJordanCUDA CUDA CXX)

set(CMAKE_EXPORT_COMPILE_COMMANDS ON)
set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CUDA_STANDARD 14)

find_package(CUDA REQUIRED)

set(CMAKE_CUDA_ARCHITECTURES 75 80 86)

set(SOURCES
    src/main.cu
    src/gauss_jordan.cu
    src/kernels.cu
    src/cuda_utils.cu
    src/io_utils.cu
)

add_executable(gauss_jordan ${SOURCES})

set_target_properties(gauss_jordan PROPERTIES
    CUDA_SEPARABLE_COMPILATION ON
)

if(UNIX)
    target_link_libraries(gauss_jordan m)
endif()
