package com.stiproject.kelassti.presentation.state

import androidx.lifecycle.MutableLiveData
import com.stiproject.kelassti.data.model.response.mahasiswa.MahasiswaData
import com.stiproject.kelassti.presentation.state.UserState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataHolder @Inject constructor(){
    private val _userState: MutableLiveData<UserState> = MutableLiveData(UserState())
    val userState = _userState

    fun setUserData(userDat: MahasiswaData?){
        _userState.value = userState.value?.copy(userData = userDat)
    }

    fun setJwtToken(jwtToken: String){
        _userState.value = userState.value?.copy(jwtToken = jwtToken)
    }
}