package com.stiproject.kelassti.domain.model

sealed class ValidateDataResult {
    class Failed(val message: String): ValidateDataResult()
    object Success: ValidateDataResult()

}