package com.stiproject.kelassti.data.remote.api

import com.stiproject.kelassti.data.model.request.TasksRequest
import com.stiproject.kelassti.data.model.response.tasks.TasksMessageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface TasksApi {

    @POST("/tasks")
    suspend fun createTasks(
        @Header("Authorization") jwt: String,
        @Body tasksRequest: TasksRequest
    ): Response<TasksMessageResponse>

//    @GET("/transaksi/{id}")
//    suspend fun getTransaksiById(
//        @Path("id") id: String
//    ): Response<TransaksiDataByIdResponse>
}