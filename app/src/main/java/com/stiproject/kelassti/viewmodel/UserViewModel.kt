package com.stiproject.kelassti.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stiproject.kelassti.model.request.LoginRequest
import com.stiproject.kelassti.model.request.RegisterRequest
import com.stiproject.kelassti.repository.UserRepository
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
                action("Berhasil Register $body")
            } else {
                action("gagal register $body")
            }
        }
    }

    fun userLogin(data: LoginRequest, action: (String) -> Unit){
        viewModelScope.launch{
            val response = repo.userLogin(data)
            val body = response.body()
            if (response.isSuccessful){
                body?.access_token?.let {
                    action("berhasil login ${body.access_token}")
                } ?: action("gagal login $body")
            } else {
                action("gagal login $body")
            }
        }
    }
}