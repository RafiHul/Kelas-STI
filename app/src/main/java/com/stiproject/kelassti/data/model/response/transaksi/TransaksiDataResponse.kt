package com.stiproject.kelassti.data.model.response.transaksi

import com.stiproject.kelassti.data.model.response.pagination.PaginationResponse

data class TransaksiDataResponse(
    val message: String,
    val data: TransaksiDataArray,
    val page: PaginationResponse
)