#include <stdio.h>
#include <stdlib.h>
#include "gauss_jordan.cuh"
#include "io_utils.cuh"

int main(int argc, char **argv)
{
    if (argc != 3)
    {
        fprintf(stderr, "Usage: %s <input_file> <output_file>\n", argv[0]);
        return 1;
    }

    double *matrix = NULL;
    int N;

    if (!read_matrix(argv[1], &matrix, &N))
    {
        fprintf(stderr, "Error reading input file\n");
        return 1;
    }

    gauss_jordan_cuda(matrix, N);

    if (!write_result(argv[2], matrix, N))
    {
        fprintf(stderr, "Error writing output file\n");
        free(matrix);
        return 1;
    }

    free(matrix);
    return 0;
}