package com.stiproject.kelassti.data.repository

import com.stiproject.kelassti.data.remote.RetrofitInstance

class MahasiswaRepository {
    private val userService = RetrofitInstance.getUserService

    suspend fun getMahasiswaByName(name: String) = userService.getMahasiswaByName(name)
}