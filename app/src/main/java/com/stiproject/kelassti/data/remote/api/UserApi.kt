package com.stiproject.kelassti.data.remote.api

import com.stiproject.kelassti.data.model.request.LoginRequest
import com.stiproject.kelassti.data.model.request.RegisterRequest
import com.stiproject.kelassti.data.model.response.login.LoginResponse
import com.stiproject.kelassti.data.model.response.mahasiswa.MahasiswaAllDataResponse
import com.stiproject.kelassti.data.model.response.mahasiswa.MahasiswaDataResponse
import com.stiproject.kelassti.data.model.response.user.UserDataResponse
import com.stiproject.kelassti.data.model.response.register.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @POST("/auth/register")
    suspend fun userRegister(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>

    @POST("/auth/login")
    suspend fun userLogin(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @GET("/users/profile")
    suspend fun getUsersByJwt(
        @Header("Authorization") jwt: String
    ): Response<UserDataResponse>

    @GET("/mahasiswa")
    suspend fun getMahasiswaByName(
        @Query("name") name: String
    ): Response<MahasiswaAllDataResponse>
}