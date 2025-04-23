package com.stiproject.kelassti.data.model.request

data class AddMahasiswaRequest(
    val NIM: Int,
    val name: String,
    val phone: String
)