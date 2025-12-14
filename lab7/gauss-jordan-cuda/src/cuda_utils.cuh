#ifndef CUDA_UTILS_CUH
#define CUDA_UTILS_CUH

#include <cuda_runtime.h>
#include <cstdio>
#include <cstdlib>

inline void cudaCheckError(cudaError_t err, const char *file, int line)
{
    if (err != cudaSuccess)
    {
        fprintf(stderr, "CUDA error at %s:%d: %s\n",
                file, line, cudaGetErrorString(err));
        std::exit(EXIT_FAILURE);
    }
}
#define CUDA_CHECK(call) cudaCheckError((call), __FILE__, __LINE__)

struct CudaMatrix
{
    double *data;
    int rows;
    int cols;

    __host__ __device__ inline double &operator()(int row, int col)
    {
        return data[row * cols + col];
    }
};

#endif // CUDA_UTILS_CUH