package com.stiproject.kelassti.domain.usecase

import com.stiproject.kelassti.data.local.JwtTokenStorage
import com.stiproject.kelassti.data.model.request.LoginRequest
import com.stiproject.kelassti.data.model.request.RegisterRequest
import com.stiproject.kelassti.data.repository.AuthRepository
import com.stiproject.kelassti.util.ApiResult
import com.stiproject.kelassti.util.parseErrorMessageJsonToString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUseCase @Inject constructor(
    val jwtStorage: JwtTokenStorage,
    val repo: AuthRepository,
    val getUserUseCase: GetUserUseCase
) {

    suspend fun login(loginData: LoginRequest): ApiResult {
        val response = repo.userLogin(loginData)
        val body = response.body()
        val errorBody = response.errorBody()

        if (!response.isSuccessful){
            return ApiResult.Failed(errorBody.parseErrorMessageJsonToString())
        }

        if (body == null || body.data == null){
            return ApiResult.Failed("Gagal Login")
        }

        val JWT = body.data.accessToken
        jwtStorage.setJwtToken(JWT)
        getUserUseCase.getUsersByJwt() // TODO: ugmmm

        return ApiResult.Success("Berhasil Login", body.data.accessToken)
    }

    suspend fun register(registerData: RegisterRequest): ApiResult {
        val response = repo.userRegister(registerData)
        val body = response.body()
        val errorBody = response.errorBody()

        if (!response.isSuccessful){
            return ApiResult.Failed(errorBody.parseErrorMessageJsonToString())
        }

        if(body == null){
            return ApiResult.Failed("Gagal Register")
        }

        return ApiResult.Success(body.message, null)
    }
}