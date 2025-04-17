package com.stiproject.kelassti.domain.usecase

import android.util.Printer
import com.stiproject.kelassti.data.local.JwtTokenStorage
import com.stiproject.kelassti.data.model.request.TasksRequest
import com.stiproject.kelassti.data.repository.TasksRepository
import com.stiproject.kelassti.util.ApiResult
import com.stiproject.kelassti.util.parseErrorMessageJsonToString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TasksUseCase @Inject constructor(
    private val repo: TasksRepository,
    private val jwtTokenStorage: JwtTokenStorage
){

    suspend fun createTasks(tasksRequest: TasksRequest): ApiResult {
        val response = repo.createTasks(jwtTokenStorage.getJwtBearer(), tasksRequest)
        val body = response.body()
        val errorBody = response.errorBody()

        if(!response.isSuccessful){
            return ApiResult.Failed(errorBody.parseErrorMessageJsonToString())
        }

        if (body == null){
            return ApiResult.Failed("Gagal Menambahkan Data")
        }

        return ApiResult.Success(body.message, null)
    }
}