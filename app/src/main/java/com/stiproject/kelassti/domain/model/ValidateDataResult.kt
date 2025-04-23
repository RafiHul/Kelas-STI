package com.stiproject.kelassti.domain.model

sealed class ValidateDataResult {
    data class Failed(val message: String): ValidateDataResult()
    object Success: ValidateDataResult()

}