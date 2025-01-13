package com.stiproject.kelassti.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stiproject.kelassti.model.request.LoginRequest
import com.stiproject.kelassti.model.request.RegisterRequest
import com.stiproject.kelassti.repository.UserRepository
import com.stiproject.kelassti.util.DataStoreUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(app: Application, val repo: UserRepository): AndroidViewModel(app) {

    val userState = repo.userState
    val userData = userState.userState

    fun userRegister(data: RegisterRequest, action: (String) -> Unit){
        viewModelScope.launch{
            val response = repo.userRegister(data)
            val body = response.body()

            if (response.code() == 409){ //conflict
                action("username telah di gunakan")
                return@launch
            }

            if (response.isSuccessful){
                action(body?.message.toString())
            } else {
                action(body?.message ?: response.message())
            }
        }
    }

    fun userLogin(context: Context, data: LoginRequest, actionSuccessLogin: (String) -> Unit, actionFailedLogin: (String) -> Unit){
        viewModelScope.launch{
            val response = repo.userLogin(data)
            val body = response.body()
            if (response.isSuccessful) {
                body!!.data?.let {
                    setJwtToken(context, it.accessToken)
                    actionSuccessLogin(body.message)
                } ?: actionFailedLogin(body.message)
            } else {
                actionFailedLogin(body?.message ?: "Failed To Connect")
            }
        }
    }

    fun getUsersByJwt(context: Context, action: (String) -> Unit){
        viewModelScope.launch{
            val jwttoken = getJwtBearer()
            val response = repo.getUsersByJwt(jwttoken)
            val body = response.body()
            if (response.isSuccessful && body != null){
                repo.userState.setUserData(body.data)
                action("Selamat Datang ${body.data?.name}")
            } else {
                Log.d("getuserbyjwtviewmodel",response.message())
                clearJwtToken(context)
                action(response.code().toString())
            }
        }
    }

    suspend fun setJwtToken(context: Context,token: String){
        userState.setJwtToken(token)
        DataStoreUtil.saveLoginToken(context,token)
    }

    suspend fun clearJwtToken(context: Context){
        DataStoreUtil.clearLoginInfo(context)
    }

    fun getJwtBearer(): String = "Bearer ${userState.userState.value?.jwtToken}"


}