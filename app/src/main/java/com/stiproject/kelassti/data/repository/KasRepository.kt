package com.stiproject.kelassti.data.repository

import com.stiproject.kelassti.data.model.request.KasRequest
import com.stiproject.kelassti.data.remote.RetrofitInstance

class KasRepository() {

    private val kasService = RetrofitInstance.getTransaksiService

    suspend fun getAllKasData() = kasService.getAllKas()

    suspend fun addKasData(jwtToken: String, kasRequest: KasRequest) = kasService.postKas(jwtToken, kasRequest)

    suspend fun getKasDataById(id: String) = kasService.getKasById(id)

    suspend fun updateKasDataById(id: String, jwtToken: String, kasRequest: KasRequest) = kasService.updateKasById(id, jwtToken, kasRequest)
}