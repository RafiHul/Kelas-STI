package com.stiproject.kelassti.data.repository

import com.stiproject.kelassti.data.remote.RetrofitInstance

class DosenRepository {

    private val dosenService = RetrofitInstance.getDosenService

    suspend fun getDosen() = dosenService.getDosen()
}