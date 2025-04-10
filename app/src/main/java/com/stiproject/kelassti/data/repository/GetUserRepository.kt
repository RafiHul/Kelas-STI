package com.stiproject.kelassti.data.repository

import com.stiproject.kelassti.data.remote.RetrofitInstance

class GetUserRepository {
    private val userService = RetrofitInstance.getUserService

    suspend fun getUsersByJwt(jwt: String) = userService.getUsersByJwt(jwt)
}