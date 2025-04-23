package com.stiproject.kelassti.data.model.response

data class ApiErrorResponse<T>(
    val message: T
)