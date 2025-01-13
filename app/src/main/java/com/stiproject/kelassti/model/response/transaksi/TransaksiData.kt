package com.stiproject.kelassti.model.response.transaksi

import com.stiproject.kelassti.model.response.pagination.PaginationTransaksiResponse

data class TransaksiData(
    val id: Int,
    val NIM_mahasiswa: Int,
    val nama: String,
    val nominal: Int,
    val type: String,
    val tanggal: String,
    val deskripsi: String,
    val page: PaginationTransaksiResponse
)