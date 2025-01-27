package com.stiproject.kelassti.presentation.state

import com.stiproject.kelassti.data.model.response.mahasiswa.MahasiswaData

data class UserState(
    val userData: MahasiswaData? = null,
    val jwtToken: String? = null
)