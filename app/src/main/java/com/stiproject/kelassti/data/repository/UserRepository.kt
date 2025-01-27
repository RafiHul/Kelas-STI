package com.stiproject.kelassti.data.repository

import com.stiproject.kelassti.data.remote.RetrofitInstance
import com.stiproject.kelassti.data.model.request.LoginRequest
import com.stiproject.kelassti.data.model.request.RegisterRequest
import com.stiproject.kelassti.data.model.response.login.LoginResponse
import com.stiproject.kelassti.data.model.response.register.RegisterResponse
import com.stiproject.kelassti.presentation.state.UserDataHolder
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDataHolder: UserDataHolder
) {
    val userState = userDataHolder
    val userService = RetrofitInstance.getUserService

    suspend fun userRegister(data: RegisterRequest): Response<RegisterResponse> {
        return userService.userRegister(data)
    }

    suspend fun userLogin(data: LoginRequest): Response<LoginResponse> {
        return userService.userLogin(data)
    }

    suspend fun getUsersByJwt(jwt: String) = userService.getUsersByJwt(jwt)
}