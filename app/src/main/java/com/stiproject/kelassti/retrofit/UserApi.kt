package com.stiproject.kelassti.retrofit

import com.stiproject.kelassti.model.request.LoginRequest
import com.stiproject.kelassti.model.request.RegisterRequest
import com.stiproject.kelassti.model.response.login.LoginResponse
import com.stiproject.kelassti.model.response.mahasiswa.MahasiswaDataResponse
import com.stiproject.kelassti.model.response.register.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {
    @POST("/auth/register")
    suspend fun userRegister(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>

    @POST("/auth/login")
    suspend fun userLogin(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @GET("/mahasiswa/{id}")
    suspend fun getMahasiswaByNim(
        @Path("id") id: String
    ): Response<MahasiswaDataResponse>

    @GET("/users/profile")
    suspend fun getUsersByJwt(
        @Header("Authorization") jwt: String
    ): Response<MahasiswaDataResponse>
}