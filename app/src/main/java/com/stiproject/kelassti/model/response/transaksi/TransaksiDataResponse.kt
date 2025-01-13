package com.stiproject.kelassti.model.response.transaksi

import com.stiproject.kelassti.model.response.pagination.PaginationTransaksiResponse

data class TransaksiDataResponse(
    val message: String,
    val data: TransaksiDataArray,
    val page: PaginationTransaksiResponse
)