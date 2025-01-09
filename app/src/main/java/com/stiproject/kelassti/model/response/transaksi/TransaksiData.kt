package com.stiproject.kelassti.model.response.transaksi

data class TransaksiData(
    val id: Int,
    val NIM_mahasiswa: Int,
    val nominal: Int,
    val type: String,
    val tanggal: String,
    val deskripsi: String
)