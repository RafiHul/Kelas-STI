package com.stiproject.kelassti.model.response.login

data class LoginResponse(
    val data: AccesTokenResponse?,
    val message: String
)