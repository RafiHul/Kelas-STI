package com.stiproject.kelassti.data.repository

import com.stiproject.kelassti.data.model.request.LoginRequest
import com.stiproject.kelassti.data.model.request.RegisterRequest
import com.stiproject.kelassti.data.remote.RetrofitInstance

class AuthRepository {
    val userService = RetrofitInstance.getUserService

    suspend fun userLogin(data: LoginRequest) = userService.userLogin(data)
    suspend fun userRegister(data: RegisterRequest) = userService.userRegister(data)

}