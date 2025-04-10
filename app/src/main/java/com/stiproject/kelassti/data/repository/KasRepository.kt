package com.stiproject.kelassti.data.repository

import com.stiproject.kelassti.data.model.request.KasRequest
import com.stiproject.kelassti.data.remote.RetrofitInstance

class KasRepository() {

    private val kasService = RetrofitInstance.getTransaksiService

    suspend fun getAllKasData() = kasService.getAllTransaksi()

    suspend fun addKasData(jwtToken: String, kasRequest: KasRequest) = kasService.postTransaksi(jwtToken, kasRequest)

    suspend fun getKasDataById(id: String) = kasService.getTransaksiById(id)

    suspend fun updateKasDataById(id: String, jwtToken: String, kasRequest: KasRequest) = kasService.updateTransaksiById(id, jwtToken, kasRequest)
}