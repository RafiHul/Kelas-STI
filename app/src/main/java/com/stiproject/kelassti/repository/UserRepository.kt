package com.stiproject.kelassti.repository

import com.stiproject.kelassti.model.request.LoginRequest
import com.stiproject.kelassti.model.request.RegisterRequest
import com.stiproject.kelassti.model.response.login.LoginResponse
import com.stiproject.kelassti.model.response.register.RegisterResponse
import com.stiproject.kelassti.retrofit.RetrofitInstance
import retrofit2.Response

class UserRepository {
    val userService = RetrofitInstance.getUserService

    suspend fun userRegister(data: RegisterRequest): Response<RegisterResponse> {
        return userService.userRegister(data)
    }

    suspend fun userLogin(data: LoginRequest): Response<LoginResponse> {
        return userService.userLogin(data)
    }
}