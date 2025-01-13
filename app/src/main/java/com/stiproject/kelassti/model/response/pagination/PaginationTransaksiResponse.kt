package com.stiproject.kelassti.model.response.pagination

data class PaginationTransaksiResponse(
    val paginationLinks: List<PaginationLinks>,
    val current: Int,
    val size: Int,
    val total_item: Int,
    val total_page: Int
)