package com.stiproject.kelassti.util

sealed class ApiResult<T> {
    data class Success<T>(val messageSuccess: String): ApiResult<T>()
    data class Failed<T>(val messageFailed: String): ApiResult<T>()
}