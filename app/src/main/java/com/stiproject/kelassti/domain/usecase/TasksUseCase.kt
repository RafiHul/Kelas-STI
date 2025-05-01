package com.stiproject.kelassti.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.stiproject.kelassti.data.local.JwtTokenStorage
import com.stiproject.kelassti.data.model.request.TasksRequest
import com.stiproject.kelassti.data.model.response.tasks.TasksData
import com.stiproject.kelassti.data.remote.paging.TasksPagingSource
import com.stiproject.kelassti.data.repository.TasksRepository
import com.stiproject.kelassti.util.ApiResult
import com.stiproject.kelassti.util.parseErrorMessageJsonToString
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TasksUseCase @Inject constructor(
    private val repo: TasksRepository,
    private val jwtTokenStorage: JwtTokenStorage
){

    private val _refreshTrigger = MutableStateFlow(true)

    @OptIn(ExperimentalCoroutinesApi::class)
    val getTasksPage = _refreshTrigger
        .flatMapLatest {getTasksPage()}

    fun refreshTriggered(){
        _refreshTrigger.value = false
        _refreshTrigger.value = true
    }

    fun getTasksPage(): Flow<PagingData<TasksData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { TasksPagingSource() }
        ).flow
    }

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

        refreshTriggered()
        return ApiResult.Success(body.message, null)
    }

    suspend fun getTasksById(id: Int): ApiResult {
        val response = repo.getTasksById(id)
        val body = response.body()

        if (!response.isSuccessful || body == null) {
            return ApiResult.Failed(response.errorBody().parseErrorMessageJsonToString())
        }

        return ApiResult.Success(body.message, body.data)
    }

    suspend fun updateTasksById(id: Int, tasksRequest: TasksRequest): ApiResult {
        val response = repo.updateTasksById(jwtTokenStorage.getJwtBearer(), id, tasksRequest)
        val body = response.body()
        val errBody = response.errorBody()

        if(!response.isSuccessful){
            return ApiResult.Failed(errBody.parseErrorMessageJsonToString())
        }

        if(body == null){
            return ApiResult.Failed("Gagal mengupdate data")
        }

        refreshTriggered()
        return ApiResult.Success("Berhasil mengupdate data", null)
    }
}