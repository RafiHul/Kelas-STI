package com.stiproject.kelassti.model.request

data class LoginRequest(
    val usernameByNIM: Int,
    val password: String
)
