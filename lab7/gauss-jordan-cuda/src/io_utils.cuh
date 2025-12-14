#ifndef IO_UTILS_H
#define IO_UTILS_H

#include <stdbool.h>

bool read_matrix(const char *filename, double **matrix, int *N);
bool write_result(const char *filename, double *matrix, int N);

#endif