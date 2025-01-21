package com.stiproject.kelassti.util

sealed class ApiResult<T> {
    data class Success<T>(val messageSuccess: T): ApiResult<T>()
    data class Failed<T>(val messageFailed: T): ApiResult<T>()
}