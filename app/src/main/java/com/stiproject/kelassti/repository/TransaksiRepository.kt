package com.stiproject.kelassti.repository

import com.stiproject.kelassti.model.request.KasRequest
import com.stiproject.kelassti.model.response.transaksi.TransaksiDataByIdResponse
import com.stiproject.kelassti.model.response.transaksi.TransaksiDataResponse
import com.stiproject.kelassti.retrofit.RetrofitInstance
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransaksiRepository @Inject constructor() {
    val transaksiService = RetrofitInstance.getTransaksiService

    suspend fun getAllTransaksi(): Response<TransaksiDataResponse> {
        return transaksiService.getTransaksi()
    }

    suspend fun postTransaksi(jwtToken: String, kasRequest: KasRequest): Response<TransaksiDataByIdResponse> {
        return transaksiService.postTransaksi(jwtToken,kasRequest)
    }

    suspend fun getTransaksiById(id: String): Response<TransaksiDataByIdResponse> {
        return transaksiService.getTransaksiById(id)
    }

    suspend fun updateTransaksiById(id: String, jwtToken: String, kasRequest: KasRequest): Response<TransaksiDataByIdResponse> {
        return transaksiService.updateTransaksiById(id,jwtToken,kasRequest)
    }
}