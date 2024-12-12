package com.stiproject.kelassti.model.response

data class AccesTokenMsg(
    val access_token: String?,
    val response: String?,
    val status: Int?,
    val message: String?,
    val name: String?
)