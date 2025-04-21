package com.stiproject.kelassti.data.remote.api

import com.stiproject.kelassti.data.model.request.TasksRequest
import com.stiproject.kelassti.data.model.response.tasks.TasksMessageResponse
import com.stiproject.kelassti.data.model.response.tasks.TasksResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface TasksApi {

    @POST("/tasks")
    suspend fun createTasks(
        @Header("Authorization") jwt: String,
        @Body tasksRequest: TasksRequest
    ): Response<TasksMessageResponse>

    @GET("/tasks")
    suspend fun getTasksPage(
        @Query("page") page: String,
        @Query("size") size: String
    ): Response<TasksResponse>
}