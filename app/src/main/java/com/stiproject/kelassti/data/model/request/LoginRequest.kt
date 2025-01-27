package com.stiproject.kelassti.data.model.request

data class LoginRequest(
    val usernameByNIM: Int,
    val password: String
)