package com.stiproject.kelassti.data.model.response.tasks

import com.stiproject.kelassti.data.model.response.dosen.DosenData

data class TasksData(
    val id: Int,
    val dosen: DosenData,
    val title: String,
    val description: String,
    val deadline: String,
    val createdAt: String
)