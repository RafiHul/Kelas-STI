package com.stiproject.kelassti.model

import com.stiproject.kelassti.model.response.mahasiswa.MahasiswaDat

data class UserState(
    val userData: MahasiswaDat? = null,
    val jwtToken: String? = null
)
