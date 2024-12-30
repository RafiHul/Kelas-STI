package com.stiproject.kelassti.viewmodel

import android.app.Application
import android.content.Context
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

    fun userRegister(data: RegisterRequest, action: (String) -> Unit){
        viewModelScope.launch{
            val response = repo.userRegister(data)
            val body = response.body()
            if (response.isSuccessful){
                action(body?.message.toString())
            } else {
                action(response.message())
            }
        }
    }

    fun userLogin(context: Context, data: LoginRequest, action: (String) -> Unit){
        viewModelScope.launch{
            val response = repo.userLogin(data)
            val body = response.body()
            if (response.isSuccessful) {
                body!!.data?.let {
                    DataStoreUtil.saveLoginToken(context,it.accessToken)
                    action(body.message)
                } ?: action(body.message)
            } else {
                action(response.message())
            }
        }
    }
}