package com.stiproject.kelassti.domain.usecase

import com.stiproject.kelassti.data.local.JwtTokenStorage
import com.stiproject.kelassti.data.repository.GetUserRepository
import com.stiproject.kelassti.presentation.state.UserDataHolder
import com.stiproject.kelassti.util.ApiResult
import com.stiproject.kelassti.util.parseErrorMessageJsonToString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserUseCase @Inject constructor(
    private val jwtTokenStorage: JwtTokenStorage,
    private val repo: GetUserRepository,
    private val userDataHolder: UserDataHolder
) {

    suspend fun getUsersByJwt(): ApiResult {
        val jwtStorage = jwtTokenStorage.getJwt()
        if (jwtStorage.isNullOrEmpty()){
            return ApiResult.Failed("Gagal Login, Harap Login Ulang!")
        }

        val response = repo.getUsersByJwt(jwtTokenStorage.getJwtBearer())
        val body = response.body()
        val errorBody = response.errorBody()

        if(response.code() == 401){
            jwtTokenStorage.clearJwtToken()
            return ApiResult.Failed("Token Expired, Harap Login Ulang!")
        }

        if(!response.isSuccessful){
            jwtTokenStorage.clearJwtToken()
            return ApiResult.Failed("${errorBody.parseErrorMessageJsonToString()}, Harap Login Ulang!")
        }

        if(body == null || body.data == null){
            jwtTokenStorage.clearJwtToken()
            return ApiResult.Failed("Gagal Mendapatkan Data User, Harap Login Ulang!")
        }

        userDataHolder.setUserData(body.data)

        return ApiResult.Success("Selamat Datang ${body.data.name}", body.data)
    }

    fun cekUserAdmin(): Boolean {
        return if(userDataHolder.userState.value?.userData?.role == "admin"){
            true
        } else {
            false
        }
    }

}