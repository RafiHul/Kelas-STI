package com.stiproject.kelassti.data.model.response.tasks

import com.stiproject.kelassti.data.model.response.pagination.PaginationResponse

data class TasksDataPageResponse(
    val data: TasksDataArray,
    val page: PaginationResponse
)