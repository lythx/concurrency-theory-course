#include <cuda_runtime.h>
#include <math.h>
#include "kernels.cuh"
#include "device_launch_parameters.h"

__global__ void kernel_B_swapRows(CudaMatrix matrix, int row_a, int row_b)
{
    int col = blockIdx.x * blockDim.x + threadIdx.x;
    if (col < matrix.cols && row_a != row_b)
    {
        double tmp = matrix(row_a, col);
        matrix(row_a, col) = matrix(row_b, col);
        matrix(row_b, col) = tmp;
    }
}

__global__ void kernel_C_computeMultipliers(
    CudaMatrix matrix, double *multipliers, int pivot,
    double pivot_value, int is_downward)
{
    int idx = blockIdx.x * blockDim.x + threadIdx.x;
    int row = is_downward ? (pivot + 1 + idx) : idx;
    int row_limit = is_downward ? matrix.rows : pivot;

    if (row < row_limit)
    {
        multipliers[row] = matrix(row, pivot) / pivot_value;
    }
}

__global__ void kernel_D_computeContributions(
    CudaMatrix matrix, CudaMatrix contributions,
    double *multipliers, int pivot, int is_downward)
{
    int row = is_downward ? (pivot + 1 + blockIdx.x) : blockIdx.x;
    int row_limit = is_downward ? matrix.rows : pivot;

    int column_offset = blockIdx.y * blockDim.x + threadIdx.x;
    int max_cols = is_downward ? (matrix.cols - pivot) : 2;

    if (row < row_limit && column_offset < max_cols)
    {
        int col = is_downward ? (pivot + column_offset)
                              : (column_offset == 0 ? pivot : matrix.cols - 1);
        contributions(row, col) = matrix(pivot, col) * multipliers[row];
    }
}

__global__ void kernel_E_normalizePivotRow(
    CudaMatrix matrix, int pivot, double pivot_value)
{
    int col_offset = blockIdx.x * blockDim.x + threadIdx.x;
    int col = pivot + col_offset;
    if (col < matrix.cols)
    {
        matrix(pivot, col) /= pivot_value;
    }
}

__global__ void kernel_F_eliminate(
    CudaMatrix matrix, CudaMatrix contributions, int pivot, int is_downward)
{
    int row = is_downward ? (pivot + 1 + blockIdx.x) : blockIdx.x;
    int row_limit = is_downward ? matrix.rows : pivot;

    int column_offset = blockIdx.y * blockDim.x + threadIdx.x;
    int max_cols = is_downward ? (matrix.cols - pivot) : 2;

    if (row < row_limit && column_offset < max_cols)
    {
        int col = is_downward ? (pivot + column_offset)
                              : (column_offset == 0 ? pivot : matrix.cols - 1);
        matrix(row, col) -= contributions(row, col);
    }
}
