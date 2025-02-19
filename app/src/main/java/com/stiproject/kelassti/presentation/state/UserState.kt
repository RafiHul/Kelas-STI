package com.stiproject.kelassti.presentation.state

import com.stiproject.kelassti.data.model.response.user.UserData

data class UserState(
    val userData: UserData? = null,
    val jwtToken: String? = null
)