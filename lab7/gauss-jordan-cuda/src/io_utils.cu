#include "io_utils.cuh"
#include <stdio.h>
#include <stdlib.h>

bool read_matrix(const char *filename, double **matrix, int *N)
{
    FILE *file = fopen(filename, "r");
    if (!file)
    {
        fprintf(stderr, "Cannot open file: %s\n", filename);
        return false;
    }

    if (fscanf(file, "%d", N) != 1)
    {
        fclose(file);
        return false;
    }

    *matrix = (double *)malloc((*N) * ((*N) + 1) * sizeof(double));

    for (int i = 0; i < *N; i++)
    {
        for (int j = 0; j < *N; j++)
        {
            if (fscanf(file, "%lf", &(*matrix)[i * (*N + 1) + j]) != 1)
            {
                free(*matrix);
                fclose(file);
                return false;
            }
        }
    }

    for (int i = 0; i < *N; i++)
    {
        if (fscanf(file, "%lf", &(*matrix)[i * (*N + 1) + *N]) != 1)
        {
            free(*matrix);
            fclose(file);
            return false;
        }
    }

    fclose(file);
    return true;
}

bool write_result(const char *filename, double *matrix, int N)
{
    FILE *file = fopen(filename, "w");
    if (!file)
    {
        fprintf(stderr, "Cannot write to file: %s\n", filename);
        return false;
    }

    fprintf(file, "%d\n", N);

    for (int i = 0; i < N; i++)
    {
        for (int j = 0; j < N; j++)
        {
            double value = matrix[i * (N + 1) + j];
            if (j == N - 1)
            {
                fprintf(file, "%.10f\n", value);
            }
            else
            {
                fprintf(file, "%.10f ", value);
            }
        }
    }

    for (int i = 0; i < N; i++)
    {
        double value = matrix[i * (N + 1) + N];
        if (i == N - 1)
        {
            fprintf(file, "%.10f\n", value);
        }
        else
        {
            fprintf(file, "%.10f ", value);
        }
    }

    fclose(file);
    return true;
}