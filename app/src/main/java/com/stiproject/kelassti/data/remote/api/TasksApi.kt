package com.stiproject.kelassti.data.remote.api

import com.stiproject.kelassti.data.model.request.TasksRequest
import com.stiproject.kelassti.data.model.response.kas.KasDataByIdResponse
import com.stiproject.kelassti.data.model.response.tasks.TasksByIdResponse
import com.stiproject.kelassti.data.model.response.tasks.TasksMessageResponse
import com.stiproject.kelassti.data.model.response.tasks.TasksResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
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

    @GET("/tasks/{id}")
    suspend fun getTasksById(
        @Path("id") id: Int
    ): Response<TasksByIdResponse>

    @PATCH("/tasks/{id}")
    suspend fun updateTasksById(
        @Header("Authorization") jwt: String,
        @Path("id") id: Int,
        @Body tasksRequest: TasksRequest
    ): Response<TasksByIdResponse>
}