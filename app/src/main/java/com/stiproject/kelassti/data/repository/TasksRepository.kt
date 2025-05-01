package com.stiproject.kelassti.data.repository

import com.stiproject.kelassti.data.model.request.TasksRequest
import com.stiproject.kelassti.data.remote.RetrofitInstance

class TasksRepository {
    private val tasksService = RetrofitInstance.getTasksService

    suspend fun createTasks(jwt: String, tasksRequest: TasksRequest) = tasksService.createTasks(jwt, tasksRequest)
    suspend fun getTasksById(id: Int) = tasksService.getTasksById(id)
    suspend fun updateTasksById(jwt: String, id: Int, tasksRequest: TasksRequest) = tasksService.updateTasksById(jwt, id, tasksRequest)

}