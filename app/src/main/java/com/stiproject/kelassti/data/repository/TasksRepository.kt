package com.stiproject.kelassti.data.repository

import com.stiproject.kelassti.data.model.request.TasksRequest
import com.stiproject.kelassti.data.remote.RetrofitInstance

class TasksRepository {
    private val tasksService = RetrofitInstance.getTasksService

    suspend fun createTasks(jwt: String, tasksRequest: TasksRequest) = tasksService.createTasks(jwt, tasksRequest)

}