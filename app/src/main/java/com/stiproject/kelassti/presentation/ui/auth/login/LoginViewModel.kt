package com.stiproject.kelassti.presentation.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stiproject.kelassti.data.model.request.LoginRequest
import com.stiproject.kelassti.domain.usecase.AuthUseCase
import com.stiproject.kelassti.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
): ViewModel() {

    fun userLogin(data: LoginRequest, onResult: (ApiResult) -> Unit){
        viewModelScope.launch {
            onResult(authUseCase.login(data))
        }
    }
}