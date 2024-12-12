package com.stiproject.kelassti.retrofit

import com.stiproject.kelassti.model.request.LoginRequest
import com.stiproject.kelassti.model.request.RegisterRequest
import com.stiproject.kelassti.model.response.Msg
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("/auth/register")
    suspend fun userRegister(
        @Body registerRequest: RegisterRequest
    ): Response<Msg>

    @POST("/auth/login")
    suspend fun userLogin(
        @Body loginRequest: LoginRequest
    ): Response<Msg>
}