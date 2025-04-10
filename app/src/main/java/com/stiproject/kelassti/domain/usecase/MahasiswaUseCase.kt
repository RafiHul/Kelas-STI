package com.stiproject.kelassti.domain.usecase

import com.stiproject.kelassti.data.repository.MahasiswaRepository
import com.stiproject.kelassti.util.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MahasiswaUseCase @Inject constructor(
    private val repo: MahasiswaRepository
){
    suspend fun getMahasiswaByName(name: String): ApiResult {
        val response = repo.getMahasiswaByName(name)
        val body = response.body()
//            val errBody = response.errorBody()

        if (!response.isSuccessful || body == null){
            return ApiResult.Failed("Gagal Mendapatkan Data")
        }

        if (body.data!!.isEmpty()){
            return ApiResult.Failed("Tidak ada mahasiswa dengan nama tersebut")
        }

        return ApiResult.Success(body.message, body.data)
    }
}