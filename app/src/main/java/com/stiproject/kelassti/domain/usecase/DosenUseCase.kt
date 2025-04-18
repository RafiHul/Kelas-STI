package com.stiproject.kelassti.domain.usecase

import android.util.Log
import com.stiproject.kelassti.data.model.request.TasksRequest
import com.stiproject.kelassti.data.repository.DosenRepository
import com.stiproject.kelassti.util.ApiResult
import com.stiproject.kelassti.util.parseErrorMessageJsonToString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DosenUseCase @Inject constructor(
    private val repo: DosenRepository
) {
    suspend fun getDosen(): ApiResult {
        val response = repo.getDosen()
        val body = response.body()
        val errorBody = response.errorBody()

        if(!response.isSuccessful){
            return ApiResult.Failed(errorBody.parseErrorMessageJsonToString())
        }

        if (body == null){
            return ApiResult.Failed("Gagal Memuat data dosen")
        }

        return ApiResult.Success(body.message, body.data)
    }
}