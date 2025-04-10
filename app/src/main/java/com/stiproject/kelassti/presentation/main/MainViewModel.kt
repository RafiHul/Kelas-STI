package com.stiproject.kelassti.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stiproject.kelassti.domain.usecase.GetUserUseCase
import com.stiproject.kelassti.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val getUserUseCase: GetUserUseCase
): ViewModel() {

    fun getUsersByJwt(onResult: (ApiResult) -> Unit){
        viewModelScope.launch {
            onResult(getUserUseCase.getUsersByJwt())
        }
    }
}