package com.stiproject.kelassti.presentation.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stiproject.kelassti.data.local.JwtTokenStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogRegViewModel @Inject constructor(
    val jwtStorage: JwtTokenStorage
): ViewModel(){

    private val _jwtToken = MutableLiveData<String>()
    val jwtToken = _jwtToken

    fun getLoginJwtToken(){
        viewModelScope.launch {
            jwtStorage.getLoginTokenObserve {
                _jwtToken.postValue(jwtStorage.getJwt())
            }
        }
    }
}