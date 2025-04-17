package com.stiproject.kelassti.presentation.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stiproject.kelassti.presentation.state.UserDataHolder
import com.stiproject.kelassti.presentation.state.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val userDataHolder: UserDataHolder
): ViewModel() {
    private val _userData: MutableLiveData<UserState> = userDataHolder.userState
    val userData = _userData
}