package com.stiproject.kelassti.data.model.response.kas

data class KasData(
    val id: Int,
    val NIM_mahasiswa: Int,
    val nama: String,
    val nominal: Long,
    val type: String,
    val tanggal: String,
    val deskripsi: String
)