package com.stiproject.kelassti.model

import com.stiproject.kelassti.model.response.mahasiswa.MahasiswaData

data class UserState(
    val userData: MahasiswaData? = null,
    val jwtToken: String? = null
)
