package com.stiproject.kelassti.presentation.state

import androidx.lifecycle.MutableLiveData
import com.stiproject.kelassti.data.model.response.user.UserData

class UserDataHolder {
    private val _userState: MutableLiveData<UserState> = MutableLiveData(UserState())
    val userState = _userState

    fun setUserData(userDat: UserData?){
        _userState.value = userState.value?.copy(userData = userDat)
    }

    fun setJwtToken(jwtToken: String?){
        _userState.value = userState.value?.copy(jwtToken = jwtToken)
    }
}