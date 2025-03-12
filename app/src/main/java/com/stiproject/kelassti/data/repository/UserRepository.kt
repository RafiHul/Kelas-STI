package com.stiproject.kelassti.data.repository

import com.stiproject.kelassti.data.remote.RetrofitInstance
import com.stiproject.kelassti.data.model.request.LoginRequest
import com.stiproject.kelassti.data.model.request.RegisterRequest
import com.stiproject.kelassti.presentation.state.UserDataHolder
import com.stiproject.kelassti.util.ApiResult
import com.stiproject.kelassti.util.parseErrorMessageJsonToString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDataHolder: UserDataHolder
) {
    val userState = userDataHolder
    val userService = RetrofitInstance.getUserService

    suspend fun userRegister(data: RegisterRequest): ApiResult {
        val response = userService.userRegister(data)
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

    suspend fun userLogin(data: LoginRequest): ApiResult {
        val response = userService.userLogin(data)
        val body = response.body()
        val errorBody = response.errorBody()

        if (!response.isSuccessful){
            return ApiResult.Failed(errorBody.parseErrorMessageJsonToString())
        }

        if (body == null || body.data == null){
            return ApiResult.Failed("Gagal Login")
        }

        return ApiResult.Success("Berhasil Login", body.data.accessToken)
    }

    suspend fun getUsersByJwt(jwt: String): ApiResult {
        val response = userService.getUsersByJwt(jwt)
        val body = response.body()
        val errorBody = response.errorBody()

        if(response.code() == 401){
            return ApiResult.Failed("Token Expired, Harap Login Ulang!")
        }

        if(!response.isSuccessful){
            return ApiResult.Failed("${errorBody.parseErrorMessageJsonToString()}, Harap Login Ulang!")
        }

        if(body == null || body.data == null){
            return ApiResult.Failed("Gagal Mendapatkan Data User, Harap Login Ulang!")
        }

        return ApiResult.Success("Selamat Datang ${body.data.name}", body.data)
    }

    suspend fun getMahasiswaByName(name: String): ApiResult {
        val response = userService.getMahasiswaByName(name)
        val body = response.body()
//            val errBody = response.errorBody()

        if (!response.isSuccessful || body == null){
            return ApiResult.Failed("Gagal Mendapatkan Data")
        }

        if (body.data!!.isEmpty()){
            return ApiResult.Failed("Tidak ada mahasiswa dengan nama tersebut")
        }

        return ApiResult.Success(body.message, body.data)
    }
}