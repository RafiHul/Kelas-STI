package com.stiproject.kelassti.data.model.request

data class AddOrUpdateMahasiswaRequest(
    val NIM: Int,
    val name: String,
    val phone: String
)