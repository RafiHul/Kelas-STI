package com.stiproject.kelassti.data.model.request

data class KasRequest(
    val NIM_mahasiswa: Int,
    val deskripsi: String,
    val nominal: Int,
    val type: String
)