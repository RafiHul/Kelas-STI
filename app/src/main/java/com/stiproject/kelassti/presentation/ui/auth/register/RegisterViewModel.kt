package com.stiproject.kelassti.presentation.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stiproject.kelassti.data.model.request.RegisterRequest
import com.stiproject.kelassti.domain.usecase.AuthUseCase
import com.stiproject.kelassti.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val authUseCase: AuthUseCase
): ViewModel(){

    fun userRegister(data: RegisterRequest, onResult: (ApiResult) -> Unit){
        viewModelScope.launch{
            onResult(authUseCase.register(data))
        }
    }
}