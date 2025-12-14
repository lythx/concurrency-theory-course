#ifndef KERNELS_CUH
#define KERNELS_CUH

#include "cuda_utils.cuh"

__global__ void kernel_B_swapRows(CudaMatrix matrix, int row_a, int row_b);
__global__ void kernel_C_computeMultipliers(
    CudaMatrix matrix, double *multipliers, int pivot, double pivot_value, int is_downward);
__global__ void kernel_D_computeContributions(
    CudaMatrix matrix, CudaMatrix contributions, double *multipliers, int pivot, int is_downward);
__global__ void kernel_E_normalizePivotRow(CudaMatrix matrix, int pivot, double pivot_value);
__global__ void kernel_F_eliminate(
    CudaMatrix matrix, CudaMatrix contributions, int pivot, int is_downward);

#endif // KERNELS_CUH
