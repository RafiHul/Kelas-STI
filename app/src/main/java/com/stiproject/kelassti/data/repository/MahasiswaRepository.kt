package com.stiproject.kelassti.data.repository

import com.stiproject.kelassti.data.model.request.AddOrUpdateMahasiswaRequest
import com.stiproject.kelassti.data.remote.RetrofitInstance

class MahasiswaRepository {
    private val mahasiswaService = RetrofitInstance.getMahasiswaService

    suspend fun getAllMahasiswa() = mahasiswaService.getAllMahasiswa()
    suspend fun getMahasiswaByName(name: String) = mahasiswaService.getMahasiswaByName(name)
    suspend fun getMahasiswaByNim(nim: String) = mahasiswaService.getMahasiswaByNim(nim)
    suspend fun addMahasiswa(jwt: String, addOrUpdateMahasiswaRequest: AddOrUpdateMahasiswaRequest) = mahasiswaService.addMahasiswa(jwt, addOrUpdateMahasiswaRequest)
    suspend fun deleteMahasiswa(jwt: String, nim: String) = mahasiswaService.deleteMahasiswa(jwt, nim)
    suspend fun updateMahasiswa(jwt: String, nim: String, addOrUpdateMahasiswaRequest: AddOrUpdateMahasiswaRequest) = mahasiswaService.updateMahasiswa(jwt, nim, addOrUpdateMahasiswaRequest)

}