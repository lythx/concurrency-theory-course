#include <stdio.h>
#include <cmath>
#include <cuda_runtime.h>
#include "gauss_jordan.cuh"
#include "cuda_utils.cuh"
#include "kernels.cuh"
#include "device_launch_parameters.h"

namespace
{
    constexpr int THREAD_COUNT = 256;
    constexpr int DIRECTION_DOWN = 1;
    constexpr int DIRECTION_UP = 0;
}

void gauss_jordan_cuda(double *h_matrix, int N)
{
    size_t matrix_size = static_cast<size_t>(N) * static_cast<size_t>(N + 1) * sizeof(double);

    double *d_matrix_data = nullptr;
    double *d_temp_data = nullptr;
    double *d_multipliers = nullptr;

    CUDA_CHECK(cudaMalloc(&d_matrix_data, matrix_size));
    CUDA_CHECK(cudaMalloc(&d_temp_data, matrix_size));
    CUDA_CHECK(cudaMalloc(&d_multipliers, N * sizeof(double)));

    CUDA_CHECK(cudaMemcpy(d_matrix_data, h_matrix, matrix_size, cudaMemcpyHostToDevice));

    CudaMatrix d_matrix = {d_matrix_data, N, N + 1};
    CudaMatrix d_contributions = {d_temp_data, N, N + 1};

    bool singular = false;

    // phase 1: downward elimination
    for (int pivot = 0; pivot < N - 1; pivot++)
    {
        // A
        int pivot_row = pivot;
        double pivot_value = 0.0;
        for (int row = pivot; row < N; ++row)
        {
            double candidate_value = 0.0;
            CUDA_CHECK(cudaMemcpy(&candidate_value,
                                  d_matrix.data + row * d_matrix.cols + pivot,
                                  sizeof(double),
                                  cudaMemcpyDeviceToHost));
            if (std::fabs(candidate_value) > std::fabs(pivot_value))
            {
                pivot_value = candidate_value;
                pivot_row = row;
            }
        }

        if (pivot_value == 0.0)
        {
            singular = true;
            break;
        }

        // B
        if (pivot_row != pivot)
        {
            int swap_blocks = (d_matrix.cols + THREAD_COUNT - 1) / THREAD_COUNT;
            kernel_B_swapRows<<<swap_blocks, THREAD_COUNT>>>(d_matrix, pivot, pivot_row);
            CUDA_CHECK(cudaDeviceSynchronize());
        }

        int rows_below = N - pivot - 1;
        int cols_span = d_matrix.cols - pivot;

        int row_blocks = (rows_below + THREAD_COUNT - 1) / THREAD_COUNT;
        // C
        kernel_C_computeMultipliers<<<row_blocks, THREAD_COUNT>>>(
            d_matrix, d_multipliers, pivot, pivot_value, DIRECTION_DOWN);
        CUDA_CHECK(cudaDeviceSynchronize());

        // DE
        int col_blocks = (cols_span + THREAD_COUNT - 1) / THREAD_COUNT;
        dim3 grid_down(rows_below, col_blocks);
        kernel_D_computeContributions<<<grid_down, THREAD_COUNT>>>(
            d_matrix, d_contributions, d_multipliers, pivot, DIRECTION_DOWN);

        int norm_cols = d_matrix.cols - pivot;
        int norm_blocks = (norm_cols + THREAD_COUNT - 1) / THREAD_COUNT;
        kernel_E_normalizePivotRow<<<norm_blocks, THREAD_COUNT>>>(
            d_matrix, pivot, pivot_value);
        CUDA_CHECK(cudaDeviceSynchronize());

        // F
        kernel_F_eliminate<<<grid_down, THREAD_COUNT>>>(
            d_matrix, d_contributions, pivot, DIRECTION_DOWN);
        CUDA_CHECK(cudaDeviceSynchronize());
    }

    if (!singular)
    {
        int last_pivot = N - 1;
        double pivot_value = 0.0;
        CUDA_CHECK(cudaMemcpy(&pivot_value,
                              d_matrix.data + last_pivot * d_matrix.cols + last_pivot,
                              sizeof(double),
                              cudaMemcpyDeviceToHost));
        if (pivot_value == 0.0)
        {
            singular = true;
        }

        // E_{n-1}
        int norm_cols = d_matrix.cols - last_pivot;
        int norm_blocks = (norm_cols + THREAD_COUNT - 1) / THREAD_COUNT;
        kernel_E_normalizePivotRow<<<norm_blocks, THREAD_COUNT>>>(
            d_matrix, last_pivot, pivot_value);
        CUDA_CHECK(cudaDeviceSynchronize());
    }

    if (singular)
    {
        fprintf(stderr, "Matrix is singular\n");
        cudaFree(d_matrix_data);
        cudaFree(d_temp_data);
        cudaFree(d_multipliers);
        return;
    }

    for (int pivot = N - 1; pivot >= 1 && !singular; --pivot)
    {
        double upper_pivot_value = 0.0;
        CUDA_CHECK(cudaMemcpy(&upper_pivot_value,
                              d_matrix.data + pivot * d_matrix.cols + pivot,
                              sizeof(double),
                              cudaMemcpyDeviceToHost));

        int rows_above = pivot;
        int cols_span = d_matrix.cols - pivot;

        int row_blocks = (rows_above + THREAD_COUNT - 1) / THREAD_COUNT;
        // C
        kernel_C_computeMultipliers<<<row_blocks, THREAD_COUNT>>>(
            d_matrix, d_multipliers, pivot, upper_pivot_value, DIRECTION_UP);
        CUDA_CHECK(cudaDeviceSynchronize());

        // D
        int col_blocks = (cols_span + THREAD_COUNT - 1) / THREAD_COUNT;
        dim3 grid_up(rows_above, col_blocks);
        kernel_D_computeContributions<<<grid_up, THREAD_COUNT>>>(
            d_matrix, d_contributions, d_multipliers, pivot, DIRECTION_UP);
        CUDA_CHECK(cudaDeviceSynchronize());

        // F
        kernel_F_eliminate<<<grid_up, THREAD_COUNT>>>(
            d_matrix, d_contributions, pivot, DIRECTION_UP);
        CUDA_CHECK(cudaDeviceSynchronize());
    }

    CUDA_CHECK(cudaMemcpy(h_matrix, d_matrix_data, matrix_size, cudaMemcpyDeviceToHost));

    cudaFree(d_matrix_data);
    cudaFree(d_temp_data);
    cudaFree(d_multipliers);
}