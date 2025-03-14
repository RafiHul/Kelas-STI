package com.stiproject.kelassti.data.model.response.pagination

data class PaginationTransaksiResponse(
    val Links: List<PaginationLinks>,
    val current: Int,
    val size: Int,
    val total_item: Int,
    val total_page: Int
)