package com.stiproject.kelassti.data.model.response.kas

import com.stiproject.kelassti.data.model.response.pagination.PaginationResponse

data class KasDataResponse(
    val message: String,
    val data: KasDataArray,
    val page: PaginationResponse
)