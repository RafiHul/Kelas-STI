package com.stiproject.kelassti.data.repository

import com.stiproject.kelassti.data.model.request.AddMahasiswaRequest
import com.stiproject.kelassti.data.remote.RetrofitInstance

class MahasiswaRepository {
    private val mahasiswaService = RetrofitInstance.getMahasiswaService

    suspend fun getAllMahasiswa() = mahasiswaService.getAllMahasiswa()
    suspend fun getMahasiswaByName(name: String) = mahasiswaService.getMahasiswaByName(name)
    suspend fun addMahasiswa(jwt: String, addMahasiswaRequest: AddMahasiswaRequest) = mahasiswaService.addMahasiswa(jwt, addMahasiswaRequest)
}