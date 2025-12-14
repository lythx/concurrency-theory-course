#ifndef GAUSS_JORDAN_CUH
#define GAUSS_JORDAN_CUH

#include <cuda_runtime.h>

void gauss_jordan_cuda(double *h_matrix, int N);

#endif