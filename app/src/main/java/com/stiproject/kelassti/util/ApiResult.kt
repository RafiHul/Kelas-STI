package com.stiproject.kelassti.util

sealed class ApiResult {
    data class Success<T>(val messageSuccess: String, val data: T?): ApiResult()
    data class Failed(val messageFailed: String): ApiResult()
}