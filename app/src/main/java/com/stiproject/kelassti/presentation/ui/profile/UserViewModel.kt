package com.stiproject.kelassti.presentation.ui.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stiproject.kelassti.data.model.request.LoginRequest
import com.stiproject.kelassti.data.model.request.RegisterRequest
import com.stiproject.kelassti.data.model.response.user.UserData
import com.stiproject.kelassti.data.repository.UserRepository
import com.stiproject.kelassti.util.ApiResult
import com.stiproject.kelassti.util.DataStoreUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(app: Application, val repo: UserRepository): AndroidViewModel(app) {

    private val userState = repo.userState
    val userData = userState.userState

    fun userRegister(data: RegisterRequest, action: (ApiResult) -> Unit){
        viewModelScope.launch {
            action(repo.userRegister(data))
        }
    }

    fun userLogin(context: Context, data: LoginRequest, action: (ApiResult) -> Unit){
        viewModelScope.launch{
            val log = repo.userLogin(data)

            if (log is ApiResult.Success<*>){
                setJwtToken(context,log.data as String) //save access token
            }

            action(log)
        }
    }

    fun getUsersByJwt(action: (ApiResult) -> Unit){
        viewModelScope.launch{

            val jwttoken = getJwtBearer()
            val get = repo.getUsersByJwt(jwttoken)

            if (get is ApiResult.Success<*>){
                repo.userState.setUserData(get.data as UserData) //save data
            }

            action(get)
        }
    }

    fun getMahasiswaByName(name: String, action: (ApiResult) -> Unit){
        viewModelScope.launch{
            action(repo.getMahasiswaByName(name))
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