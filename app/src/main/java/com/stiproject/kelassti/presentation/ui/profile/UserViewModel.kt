package com.stiproject.kelassti.presentation.ui.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stiproject.kelassti.data.model.request.LoginRequest
import com.stiproject.kelassti.data.model.request.RegisterRequest
import com.stiproject.kelassti.data.model.response.mahasiswa.MahasiswaAllDataResponse
import com.stiproject.kelassti.data.repository.UserRepository
import com.stiproject.kelassti.util.ApiResult
import com.stiproject.kelassti.util.DataStoreUtil
import com.stiproject.kelassti.util.parseErrorMessageJsonToString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(app: Application, val repo: UserRepository): AndroidViewModel(app) {

    private val userState = repo.userState
    val userData = userState.userState

    fun userRegister(data: RegisterRequest, action: (ApiResult<String>) -> Unit){
        viewModelScope.launch {

            val response = repo.userRegister(data)
            val body = response.body()
            val errorBody = response.errorBody()

            if (!response.isSuccessful){
                action(ApiResult.Failed(errorBody.parseErrorMessageJsonToString()))
                return@launch
            }

            if(body == null){
                action(ApiResult.Failed("Gagal Register"))
                return@launch
            }

            action(ApiResult.Success(body.message))
        }
    }

    fun userLogin(context: Context, data: LoginRequest, action: (ApiResult<String>) -> Unit){
        viewModelScope.launch{

            val response = repo.userLogin(data)
            val body = response.body()
            val errorBody = response.errorBody()

            if (!response.isSuccessful){
                action(ApiResult.Failed(errorBody.parseErrorMessageJsonToString()))
                return@launch
            }

            if (body == null || body.data == null){
                action(ApiResult.Failed("Gagal Login"))
                return@launch
            }

            setJwtToken(context, body.data.accessToken)
            action(ApiResult.Success("Berhasil Login"))
        }
    }

    fun getUsersByJwt(action: (ApiResult<String>) -> Unit){
        viewModelScope.launch{

            val jwttoken = getJwtBearer()
            val response = repo.getUsersByJwt(jwttoken)
            val body = response.body()
            val errorBody = response.errorBody()

            if(response.code() == 401){
                action(ApiResult.Failed("Token Expired, Harap Login Ulang!"))
                return@launch
            }

            if(!response.isSuccessful){
                action(ApiResult.Failed("${errorBody.parseErrorMessageJsonToString()}, Harap Login Ulang!"))
                return@launch
            }

            if(body == null || body.data == null){
                action(ApiResult.Failed("Gagal Mendapatkan Data User, Harap Login Ulang!"))
                return@launch
            }

            repo.userState.setUserData(body.data)
            action(ApiResult.Success("Selamat Datang ${body.data.name}"))
        }
    }

    fun getMahasiswaByName(name: String, action: (ApiResult<MahasiswaAllDataResponse>) -> Unit){
        viewModelScope.launch{
            val response = repo.getMahasiswaByName(name)
            val body = response.body()
//            val errBody = response.errorBody()

            if (!response.isSuccessful || body == null){
                action(ApiResult.Failed(MahasiswaAllDataResponse("Gagal Mendapatkan Data",null)))
                return@launch
            }

            if (body.data!!.isEmpty()){
                action(ApiResult.Failed(MahasiswaAllDataResponse("Tidak ada mahasiswa dengan nama tersebut",null)))
                return@launch
            }

            action(ApiResult.Success(body))
        }
    }

    suspend fun setJwtToken(context: Context,token: String){
        userState.setJwtToken(token)
        DataStoreUtil.saveLoginToken(context,token)
    }

    fun clearJwtToken(context: Context){
        // TODO: add clear jwt in userstate too
        viewModelScope.launch{
            DataStoreUtil.clearLoginInfo(context)
        }
    }

    fun getJwtBearer(): String = "Bearer ${userState.userState.value?.jwtToken}" // TODO: some jwt bugs
}