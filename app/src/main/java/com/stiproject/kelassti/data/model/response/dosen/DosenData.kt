package com.stiproject.kelassti.data.model.response.dosen

data class DosenData(
    val id: Int,
    val matkul: String,
    val name: String,
    val phone: String,
    val whatsapp: String
){
    override fun toString(): String {
        return name
    }
}
