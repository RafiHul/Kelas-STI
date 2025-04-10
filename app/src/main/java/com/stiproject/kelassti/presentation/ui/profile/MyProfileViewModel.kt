package com.stiproject.kelassti.presentation.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stiproject.kelassti.data.model.response.user.UserData
import com.stiproject.kelassti.presentation.state.UserDataHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val userDataHolder: UserDataHolder
): ViewModel() {
    private val _userData = MutableLiveData<UserData?>()
    val userData = _userData

    fun setUser(){
        _userData.value = userDataHolder.userState.value?.userData
    }
}