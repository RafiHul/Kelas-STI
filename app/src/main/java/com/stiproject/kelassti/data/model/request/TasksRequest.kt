package com.stiproject.kelassti.data.model.request

data class TasksRequest(
    val dosenId: Int,
    val description: String,
    val title: String,
    val deadline: String
)